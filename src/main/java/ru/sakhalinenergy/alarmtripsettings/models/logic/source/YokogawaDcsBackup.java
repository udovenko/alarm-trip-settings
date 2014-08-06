package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.HashMap;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.io.FileNotFoundException;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgFileHeader;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgFileHeaderContents;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgBlockRecord;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgTagRecord;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 * Класс реализует нить для создания источника данных - таблицы переменных DCS 
 * из бэкапа Yokogawa DCS.
 * 
 * @author Denis.Udovenko
 * @version 1.0.5
 */
public class YokogawaDcsBackup extends TagsSource implements YokogawaDcsBackupObservable, YokogawaDcsBackupControllable
{
    private String backupFolderPath;
    private List<YgStationRecord> stations;
        
    
    /**
     * Public constructor.
     * 
     * @param source Source instance to be wrapped in current logic
     */
    public YokogawaDcsBackup(Source source)
    {
        super(source);
    }//YokogawaDcsBackupDataSourceCreator
     
    
    /**
     * Sets backup folder path field value.
     * 
     * @param backupFolderPath Path to backup folder
     */
    @Override
    public void setBackupFolderPath(String backupFolderPath)
    {
        this.backupFolderPath = backupFolderPath;
    }//setBackupFolderPath
    
    
    /**
     * Returns current backup folder path.
     *
     * @return Backup folder path
     */
    @Override
    public String getBackupFolderPath()
    {
        return backupFolderPath;
    }//getBackupFolderPath
    
    
    /**
     * Returns stations collection.
     * 
     * @return Stations collection
     */
    @Override
    public List<YgStationRecord> getStations()
    {
        return stations;
    }//getStations
    
    
    /**
     * Метод читает заголовок файла диаграммы функциональных блоков и возвращает
     * экземпляр заголовкам со всеми необходимыми параметрами.
     * 
     * @throws  FileNotFoundException
     * @throws  IOException
     * @param   station                Имя станции
     * @param   diagramFileName        Имя файла диаграммы
     * @return  Экземпляр заголовка диаграммы функциональных блоков
     */
    protected YgFileHeader readFileHeader(String filePath) throws FileNotFoundException, IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        byte[] headerBytes = new byte[512];
        file.read(headerBytes);
        
        YgFileHeader header = new YgFileHeader(headerBytes); 
        
        file.close();
        return header;
    }//getFbdBlocks
    
    
    /**
     * Метод читает содержимое пареметров настройки в строку полностью.
     * 
     * @throws FileNotFoundException 
     * @param filePath Путь к файлу с настройками
     * @return Cодержимое файла в виде строки
     */
    private String readTunningParameters(String filePath) throws FileNotFoundException
    {
        //Читаем содержимое файла полностью:
        String content = "";
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNext()) content += in.nextLine();
        in.close();
    
        return content;
    }//getTunningParameters
    
    
    /**
     * Метод получает список названий диаграм функциональных блоков для заданной
     * станции.
     * 
     * @param   station  Имя станции
     * @return  Отсортированный список имен диаграмм станции
     */
    private TreeSet<String> getStationFbdList(YgStationRecord station)
    {
        TreeSet<String> result = new TreeSet<String>();
        
        //Получаем полный список файлов в дирректории:
        File folder = new File(this.backupFolderPath + File.separator + station.getStationName().trim() + File.separator + "FUNCTION_BLOCK");
        File[] listOfFiles = folder.listFiles();
        
        //Выбираем из списка только файлы с раширением .edf и добавляем их в результат:
        for (File file : listOfFiles) 
        {
            if (file.isFile() && file.getName().startsWith("DR") && file.getName().endsWith(".edf")) result.add(file.getName());
        }//for
        
        return result;
    }//getStationFbdList
    
    
    /**
     * Метод читает содержимое файла бэкапа DCS Yokogawa как библиотеку единиц
     * измерения.
     * 
     * @throws FileNotFoundException  
     * @throws IOException
     * @param  filePath    Путь к файлу
     * @param  fileHeader  Экзкмпляр заголовка файла
     * @return Коллекцию единиц измерения в таком же порядке, как и в файле
     */
    private List<String> readAsEngUnitsList(String filePath, YgFileHeader fileHeader) throws FileNotFoundException, IOException
    {
        List<String> result = new ArrayList();
        RandomAccessFile engUnitsFile = new RandomAccessFile(filePath, "r");
        YgFileHeaderContents contentItem;
        
        contentItem = fileHeader.getContentItem("EUNT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
    
        //Передвигаем указатель чтения файла на начало секции с описанием функциональных блоков:
        engUnitsFile.seek(contentItem.getOffset());
        byte[] tempUnitsBytes = new byte[8]; 
        String tempUnits;
        
        //Читаем все доступные описания блоков:
        for (int i = 0; i < count; i++)
        {
            engUnitsFile.read(tempUnitsBytes);
            tempUnits = new String(tempUnitsBytes);
            result.add(tempUnits);
        }//for
        
        engUnitsFile.close();
        return result;
    }//readEngUnits
    
    
    /**
     * Метод читает из файла описания диаграммы функциональных блоков набор
     * описаний входящих в диаграмму блоков.
     * 
     * @throws  FileNotFoundException
     * @throws  IOException
     * @param   station                Имя станции
     * @param   diagramFileName        Имя файла диаграммы
     * @param   fbdHeader              Экземпляр заголовка диаграммы функциональных блоков
     * @return  Список экземпляров описаний функциональных блоков, входящих в диаграмму
     */
    private List<YgBlockRecord> readFbdBlocks(String filePath, YgFileHeader fbdHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile diagramFile = new RandomAccessFile(filePath, "r");
        
        List<YgBlockRecord> result = new ArrayList<YgBlockRecord>();
        YgFileHeaderContents contentItem;
        
        contentItem = fbdHeader.getContentItem("RGTL");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
            
        //Передвигаем указатель чтения файла на начало секции с описанием функциональных блоков:
        diagramFile.seek(contentItem.getOffset());
        YgBlockRecord tempBlockDescription;
        byte[] temp48BytesArray = new byte[48];
        
        //Читаем все доступные описания блоков:
        for (int i = 0; i < count; i++)
        {
            diagramFile.read(temp48BytesArray);
            tempBlockDescription = new YgBlockRecord(temp48BytesArray);
            result.add(tempBlockDescription);
        }//for
        
        diagramFile.close();
        return result;
    }//readFbdBlocks
    
    
    /**
     * Метод читает содержимое файла бэкапа DCS Yokogawa как набор тагов 
     * функционального блока.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param  filePath Путь к файлу
     * @param  blockFileHeader Экземпляр заголовка файла
     * @return Коллекцию экземпляров тагов
     */
    private List<YgTagRecord> readAsBlockTagsList(String filePath, YgFileHeader blockFileHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile blockFile = new RandomAccessFile(filePath, "r");
        
        List<YgTagRecord> result = new ArrayList<YgTagRecord>();
        YgFileHeaderContents contentItem;
        
        contentItem = blockFileHeader.getContentItem("HTLT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
        
        //Передвигаем указатель чтения файла на начало секции с описанием функциональных блоков:
        blockFile.seek(contentItem.getOffset());
        byte[] tagRecordBytes = new byte[128];
        
        //Читаем все доступные описания блоков:
        for (int i = 0; i < count; i++)
        {
            blockFile.read(tagRecordBytes);
            YgTagRecord tagRecord = new YgTagRecord(tagRecordBytes);
            result.add(tagRecord);
        }//for
        
        blockFile.close();
        return result;
    }//readBlockTags
    
    
    /**
     * 
     * 
     */
    private String readBlockTuninngParameters(String filePath, YgFileHeader blockFileHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile blockFile = new RandomAccessFile(filePath, "r");
        String result = "";
        
        YgFileHeaderContents contentItem;
        
        contentItem = blockFileHeader.getContentItem("HTLT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
        
        //Передвигаем указатель чтения файла на начало секции с описанием функциональных блоков:
        blockFile.seek(contentItem.getOffset() + 128*count);
        byte[] blockParamsBytes = new byte[1024];
        
        blockFile.read(blockParamsBytes);
        return new String(blockParamsBytes);
    }//readBlockTuninngParameters
    
    
    
    /**
     * 
     * 
     */
    private HashMap<String, String> readTagTunningParameters(YgTagRecord tagRecord, String tunnigTunningParameters)
    {
        HashMap<String, String> result = new HashMap();
        int tagParamsStartPos = tunnigTunningParameters.indexOf(tagRecord.getTagName().trim() + ":");
        int tagParamsEndPos = tunnigTunningParameters.indexOf(";", tagParamsStartPos);
        
        if (tagParamsStartPos > -1)
        {
            String tagTunnigParams = tunnigTunningParameters.substring(tagParamsStartPos, tagParamsEndPos);
            String[] paramsAndValues = tagTunnigParams.split(",");
            String[] tempParamAndValuePair;
            
            for (String tempParamAndValue : paramsAndValues)
            {
                tempParamAndValuePair = tempParamAndValue.split("=");
                if (tempParamAndValuePair.length > 1) result.put(tempParamAndValuePair[0], tempParamAndValuePair[1]);
            }//for
        }//if
        
        return result;
    }//readTagTunningParameters
     
    
    /**
     * 
     *
     */
    private void createSystemTagInstance(YgTagRecord tagRecord, List<String> units,
        HashMap<String, String> tagTunningParams, String blockTuningParams) throws Exception
    {
        Tag tag = addTag(tagRecord.getTagName().trim());
         
        TagSetting setting;
        Double tempDouble;
        String existingAlarms;
        int hhllParamStartIndex = blockTuningParams.indexOf("HHLL:");
        int hhllParamEndIndex;
        int inhlParamStartIndex = blockTuningParams.indexOf("INHL:");
        int inhlParamEndIndex;
        
        //Если настройки тага содержат уставки трипов:
        if (hhllParamStartIndex > -1)
        {
            hhllParamEndIndex = blockTuningParams.indexOf(";", hhllParamStartIndex);
            existingAlarms = blockTuningParams.substring(hhllParamStartIndex + 5, hhllParamEndIndex);
                        
            //Если таг содержит уставки и нижнего, и верхнего трипа:
            if (existingAlarms.equals("HHLL"))
            {
                if (tagTunningParams.containsKey("LL"))
                {    
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("LL")).toString());
                    addTagSetting(tag, setting);
                }//if
                
                if (tagTunningParams.containsKey("HH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("HH")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if    
            
            //Если таг содержит уставку верхнего трипа:
            else if (existingAlarms.equals("HH"))
            { 
                if (tagTunningParams.containsKey("HH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("HH")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if
            
            //Если таг содержит уставку нижнего трипа:
            else if (existingAlarms.equals("LL"))
            { 
                if (tagTunningParams.containsKey("LL"))
                {    
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("LL")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if 
        }//if
        
        
        //Если настройки тага содержат уставки алармов:
        if (inhlParamStartIndex > -1)
        {
            inhlParamEndIndex = blockTuningParams.indexOf(";", inhlParamStartIndex);
            existingAlarms = blockTuningParams.substring(inhlParamStartIndex + 5, inhlParamEndIndex);
                        
            //Если таг содержит уставки и нижнего, и верхнего аларма:
            if (existingAlarms.equals("HL"))
            {
                if (tagTunningParams.containsKey("PL"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PL")).toString());
                    addTagSetting(tag, setting);
                }//if
                                
                if (tagTunningParams.containsKey("PH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PH")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if
            
            //Если таг содержит только уставку верхнего аларма:
            else if (existingAlarms.equals("H"))
            {
                if (tagTunningParams.containsKey("PH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PH")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if
            
            //Если таг содержит только уставку нижнего аларма:
            else if (existingAlarms.equals("L"))
            {
                if (tagTunningParams.containsKey("PL"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PL")).toString());
                    addTagSetting(tag, setting);
                }//if
            }//if
        }//if
            
        //Добавляем нижнюю границу диапазона:                               
        tempDouble = tagRecord.getSl() / Math.pow(10, tagRecord.getDp());
        setting = new TagSetting();
        setting.setTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
        setting.setValue(tempDouble.toString());
        addTagSetting(tag, setting);
                
        //Добавляем верхнюю границу диапазона:        
        tempDouble = tagRecord.getSh() / Math.pow(10, tagRecord.getDp());
        setting = new TagSetting();
        setting.setTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
        setting.setValue(tempDouble.toString());
        addTagSetting(tag, setting);
                  
        //Добавляем единицы измерения:
        if (tagRecord.getEngUnitsIndex() < units.size())
        {
            setting = new TagSetting();
            setting.setTypeId(SettingsTypes.UNITS_SETTING.ID);
            setting.setValue(units.get(tagRecord.getEngUnitsIndex()));
            addTagSetting(tag, setting);
        }//if  
    }//createSystemTagInstance
    
    
    /**
     * Creates and launches thread for getting backup stations collection. 
     * Notifies subscribers about thread events. 
     */
    @Override
    public void readStations()
    {
        //Creating a thread:
        WorkerThread stationsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                List<YgStationRecord> stationsList = new ArrayList();
                try
                {
                    //If folder path wasn't set:
                    if (backupFolderPath == null) throw new Exception("Backup folder path is null");
                    
                    //Building stations info file name:
                    String filePath = backupFolderPath + File.separator + "Common" + File.separator + "StnConf.edf";        
                     
                    YgFileHeader stationsFileHeader = readFileHeader(filePath);
        
                    RandomAccessFile stationsFile = new RandomAccessFile(filePath, "r");
                    YgFileHeaderContents contentItem;
        
                    contentItem = stationsFileHeader.getContentItem("STIF");
                    if (contentItem == null) throw new Exception("Content item STIF not found in StnConf.edf");
        
                    long count = contentItem.getNumber();
                    if (count <= 0) throw new Exception("Stations count in StnConf.edf header is zero");
        
                    //Передвигаем указатель чтения файла на начало секции с описанием функциональных блоков:
                    stationsFile.seek(contentItem.getOffset());
                    YgStationRecord tempStationRecord;
                    byte[] temp256BytesArray = new byte[256];

                    //Читаем все доступные описания блоков:
                    for (int i = 0; i < count; i++)
                    {
                        stationsFile.read(temp256BytesArray);
                        tempStationRecord = new YgStationRecord(temp256BytesArray);
                        if (tempStationRecord.getStationName().startsWith("FCS")) stationsList.add(tempStationRecord);
                    }//for
        
                    stationsFile.close();
            
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Stations collection reading error", exception, WorkerThread.Event.ERROR);
                }//catch
    
                //Формируем и возвращаем результат:
                stations = stationsList;
                return new HashMap();
            }//doInBackground
        };//WorkerThread
                
        //Resubscribing existing subscribers on current thread events:
        if (events.get(SourceEvent.THREAD_ERROR) != null) stationsReader.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) stationsReader.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) stationsReader.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.STATIONS_READ) != null) stationsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.STATIONS_READ));
        
        //Executing thread:
        stationsReader.execute();
    }//readStations
        
    
    /**
     * Creates and launches a thread to read tags from found stations.
     * 
     * @param selectedStations List of stations instances references, from which tags will be read
     */
    @Override
    public void readTags(final List<YgStationRecord> selectedStations)
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего бэкапа DCS Yokogawa:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                //Initializing hash map to store progress:
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.OUTER_CYCLE_PERCENTAGE, 0);
                progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, 0);
        
                //Читаем библиотеку единиц измерения: 
                List<String> units = null;
               
                //Публикуем текущий прогресс:
                progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "Getting units library");
                publish(progress);
        
                try 
                {       
                    YgFileHeader engUnitsFileHeader = readFileHeader(backupFolderPath + File.separator + "Common" + File.separator + "EngUnit.edf");
                    units = readAsEngUnitsList(backupFolderPath + File.separator + "Common" + File.separator + "EngUnit.edf", engUnitsFileHeader);
            
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Yokogawa DCS read stations error", exception, WorkerThread.Event.ERROR);
                }//catch 
               
                //Обходим все FCS и собираем данные об их функциональных блоках:
                YgFileHeader diagramFileHeader;
                YgFileHeader blockFileHeader;
                TreeSet<String> functionalBlockDiagrams;
                List<YgBlockRecord> diagramBlockDescriptions;
                List<YgTagRecord> blockTags;
                String tempStationName;
                String tempTunningParameters;
                String tempBlockTuninngParameters;
                HashMap<String, String> tempTagTunningParams;
                int totalStations = selectedStations.size();
                int stationsRead = 0;
                int totalDiagrams;
                int diagramsRead;
        
                //Обходим все полученные станции:
                for (YgStationRecord station : selectedStations)
                {
                    functionalBlockDiagrams = getStationFbdList(station);
                    totalDiagrams = functionalBlockDiagrams.size();
                    diagramsRead = 0;
            
                    //Получаем имя станции без пробелов:
                    tempStationName = station.getStationName().trim();
            
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "Reading station " + tempStationName);
                    publish(progress);
                    
                    try
                    {
                        //Публикуем текущий прогресс:
                        progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "Reading tunning parameters");
                        progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, 0);
                        publish(progress);
                
                        //Читаем параметры настроек:
                        tempTunningParameters = readTunningParameters(backupFolderPath + File.separator + tempStationName
                            + File.separator + "PROG" + File.separator + "TunningData.txt");
                
                        //Для каждой станции обходим все файлы диаграмм функциональных блоков:
                        for (String diagramName : functionalBlockDiagrams)
                        {
                            //Публикуем текущий прогресс:
                            progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "Reading diagram " + diagramName);
                            publish(progress);
                    
                            //Читаем заголовок диаграммы и набор описаний тагов диаграмы:
                            diagramFileHeader = readFileHeader(backupFolderPath + File.separator + tempStationName
                                + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName);
                            diagramBlockDescriptions = readFbdBlocks(backupFolderPath + File.separator + tempStationName
                                + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName, diagramFileHeader);
                
                            //Обходим все описания функциональных блоков:
                            for (YgBlockRecord tempBlockDescription : diagramBlockDescriptions)
                            {
                                blockFileHeader = readFileHeader(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf");
                                blockTags = readAsBlockTagsList(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf", blockFileHeader);
                                tempBlockTuninngParameters = readBlockTuninngParameters(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf", blockFileHeader);
                        
                                //Обходим все полученные таги блока:
                                for (YgTagRecord tagRecord : blockTags)
                                {
                                    tempTagTunningParams = readTagTunningParameters(tagRecord, tempTunningParameters);
                       
                                    try //создаем окончательный экземпляр тага для записи в хранилище и вносим в него все необходимые свойства:
                                    {
                                        //Добаволяем таг в общий список полученных тагов:
                                        createSystemTagInstance(tagRecord, units, tempTagTunningParams, tempBlockTuninngParameters);           
                                                                   
                                    } catch (Exception exception) {
                                
                                        _invokeExceptionInEdt("Tag " + tagRecord.getTagName().trim() + " creation error", exception, WorkerThread.Event.WARNING);
                                    }//catch
                                }//for                        
                            }//for
                    
                            diagramsRead++;
                    
                            //Публикуем текущий прогресс:
                            progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, (int)((double)diagramsRead / (double)totalDiagrams * 100));
                            publish(progress);
                        }//for
                    } catch (Exception exception){
                
                        _invokeExceptionInEdt("Yokogawa DCS backup parsing error", exception, WorkerThread.Event.ERROR);
                    }//catch
            
                    stationsRead++;
                       
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.OUTER_CYCLE_PERCENTAGE, (int)((double)stationsRead / (double)totalStations * 100));
                    publish(progress);
                }//for

                return new HashMap();
            }//doInBackground
         };//WorkerThread
        
        //Resubscribing existing subscribers on current thread events:
        if (events.get(SourceEvent.THREAD_ERROR) != null) tagsReader.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) tagsReader.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) tagsReader.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.TAGS_READ) != null) tagsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.TAGS_READ));
        
        
        //Executing thread:
        tagsReader.execute();
    }//doInBackground
}//YokogawaDcsBackupDataSourceCreator
