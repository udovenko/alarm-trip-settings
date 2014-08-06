package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Класс реализует нить обработки файла экспорта из DCS Honeywell и создания 
 * источника данных на его основании. 
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class HoneywellDcsExport extends TagsSource implements HoneywellDcsExportObservable
{
    //Параметры инстанса:
    public static final String TAG_NAME_ENTRY_NAME = "Name";
    public static final String BASE_ENTRY_NAME = "Base";
    public static final String UNITS_ENTRY_NAME = "EUDESC";
    public static final String ALARM_LL_ENTRY_NAME = "PVLLALM.TP";
    public static final String ALARM_L_ENTRY_NAME = "PVLOALM.TP";
    public static final String ALARM_H_ENTRY_NAME = "PVHIALM.TP";
    public static final String ALARM_HH_ENTRY_NAME = "PVHHALM.TP";
    public static final String RANGE_MIN_ENTRY_NAME = "PVEULO";
    public static final String RANGE_MAX_ENTRY_NAME = "PVEUHI";
    
    //Параметры значений:
    private static final String PROPER_BLOCK_NAME = "DATAACQ:DATAACQ";
    private static final String NOT_A_NUMBER_FLAG = "NaN";
    
    //Глобальные переменные для доступа к данным потока:
    private String filePath;
    private DataInputStream instancesDataStream;
    private BufferedReader instancesReader;
    
    
    /**
     * Контсруктор класса. 
     */
    public HoneywellDcsExport(Source source)
    {
        super(source);
    }//DcsExport
               
    
    /**
     * Метод возвращает путь к текущему файлу экспорта DCS.
     * 
     * @return  String
     */
    public String getFilePath()
    {
        return this.filePath;
    }//getFilePath
    
    
    /**
     * Sets up path to export file.
     * 
     * @param filePath Path to export file
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
        
        CustomEvent filePathUpdatedEvent = new CustomEvent(filePath);
        events.trigger(SourceEvent.FILE_PATH_UPDATED, filePathUpdatedEvent);
    }//setFilePath
    
    
    /**
     * Метод подсчитывает общее количество доступных тагов в файле экспорта. 
     * 
     * @throws IOException
     * @return Рассчитанное количество инстансов
     */
    private int _calculateInstances() throws IOException
    {
        FileInputStream fileStream = new FileInputStream(this.filePath);
        DataInputStream dataInputStream = new DataInputStream(fileStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
        String strLine;
        int instancesCount = 0;
  
        //Читаем файл построчно:
        while ((strLine = br.readLine()) != null)   
        {
            if (strLine.trim().startsWith(TAG_NAME_ENTRY_NAME))
            {
                instancesCount++;
            }//if
        }//while
        
        dataInputStream.close();
        
        return instancesCount;
    }//calculateTags
    
    
    /**
     * Метод читает следующий инстанс относительно положения указателя в файле
     * экспорта DCS Honeywell.
     * 
     * @throws IOException
     * @return Хеш-массив свойств инчстанса или null, если достигнут конец файла
     */
    private HashMap<String, String> _readNextInstance() throws IOException
    {
        //Если буфер для чтения потока еще не создан, создаем его:
        if (this.instancesDataStream == null)
        {
            FileInputStream fileStream = new FileInputStream(this.filePath);
            this.instancesDataStream = new DataInputStream(fileStream);
            this.instancesReader = new BufferedReader(new InputStreamReader(this.instancesDataStream));
        }//if
        
        String tempLine, previousLine;
                
        //Читаем файл построчно:
        while ((tempLine = this.instancesReader.readLine()) != null)   
        {
            if (tempLine.trim().startsWith(TAG_NAME_ENTRY_NAME))
            {
                HashMap<String, String> result = new HashMap();
                String trimmedTempLine;
                previousLine = tempLine;
                
                //Поолучаем имя тага вместе с названием блока:
                String tagName = tempLine.replace(TAG_NAME_ENTRY_NAME, "").trim();
                
                //Отсекаем название блока:
                if (tagName.indexOf(".") > -1) tagName = tagName.substring(0, tagName.indexOf("."));
                result.put(TAG_NAME_ENTRY_NAME, tagName);
                
                while ((tempLine = this.instancesReader.readLine()) != null)   
                {
                    trimmedTempLine = tempLine.trim();
                    
                    //Если начало строки совпадает с заголовком единиц измерения:                    
                    if (trimmedTempLine.startsWith(UNITS_ENTRY_NAME))
                    {
                        String units = trimmedTempLine.replace(UNITS_ENTRY_NAME, "").trim();
                        result.put(UNITS_ENTRY_NAME, units);
                    }//if
                    
                    //Если начало строки совпадает с заголовком нижнего трипа:                    
                    if (trimmedTempLine.startsWith(ALARM_LL_ENTRY_NAME))
                    {
                        String alarmLowLow = trimmedTempLine.replace(ALARM_LL_ENTRY_NAME, "").trim();
                        if (!alarmLowLow.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_LL_ENTRY_NAME)) result.put(ALARM_LL_ENTRY_NAME, alarmLowLow);
                    }//if
                    
                    //Если начало строки совпадает с заголовком нижнего аларма:                    
                    if (trimmedTempLine.startsWith(ALARM_L_ENTRY_NAME))
                    {
                        String alarmLow = trimmedTempLine.replace(ALARM_L_ENTRY_NAME, "").trim();
                        if (!alarmLow.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_L_ENTRY_NAME)) result.put(ALARM_L_ENTRY_NAME, alarmLow);
                    }//if
                    
                    //Если начало строки совпадает с заголовком верхнего аларма:                    
                    if (trimmedTempLine.startsWith(ALARM_H_ENTRY_NAME))
                    {
                        String alarmHigh = trimmedTempLine.replace(ALARM_H_ENTRY_NAME, "").trim();
                        if (!alarmHigh.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_H_ENTRY_NAME)) result.put(ALARM_H_ENTRY_NAME, alarmHigh);
                    }//if
                    
                    //Если начало строки совпадает с заголовком верхнего трипа:                    
                    if (trimmedTempLine.startsWith(ALARM_HH_ENTRY_NAME))
                    {
                        String alarmHighHigh = trimmedTempLine.replace(ALARM_HH_ENTRY_NAME, "").trim();
                        if (!alarmHighHigh.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_HH_ENTRY_NAME)) result.put(ALARM_HH_ENTRY_NAME, alarmHighHigh);
                    }//if
                    
                    //Если начало строки совпадает с заголовком нижней границы диапазона:                    
                    if (trimmedTempLine.startsWith(RANGE_MIN_ENTRY_NAME))
                    {
                        String rangeMin = trimmedTempLine.replace(RANGE_MIN_ENTRY_NAME, "").trim();
                        if (!result.containsKey(RANGE_MIN_ENTRY_NAME)) result.put(RANGE_MIN_ENTRY_NAME, rangeMin);
                    }//if
                    
                    //Если начало строки совпадает с заголовком верхней границы диапазона:                    
                    if (trimmedTempLine.startsWith(RANGE_MAX_ENTRY_NAME))
                    {
                        String rangeMax = trimmedTempLine.replace(RANGE_MAX_ENTRY_NAME, "").trim();
                        if (!result.containsKey(RANGE_MAX_ENTRY_NAME)) result.put(RANGE_MAX_ENTRY_NAME, rangeMax);
                    }//if
                    
                    //Если тип текущего блока не соответвует нужному, возвращаем пустой результат:
                    if (trimmedTempLine.startsWith(BASE_ENTRY_NAME))
                    {
                        String base = trimmedTempLine.replace(BASE_ENTRY_NAME, "").trim();
                        if (!base.equals(PROPER_BLOCK_NAME)) return result; 
                        result.put(BASE_ENTRY_NAME, base);
                    }//if
                    
                    //Если это - вторая пустая строка подряд, считаем что инстанс кончился и возвращаем результат:
                    if (trimmedTempLine.equals("") && previousLine.trim().equals(""))
                    {
                        return result;
                    }//if
                    
                    previousLine = tempLine;
                }//while
            }//if
        }//while
               
        return null;
    }//readNextInstance
    
    
    /**
     * Метод перегружает родительскую заглушку и реализует нить получения данных 
     * из файла экспорта DCS и записи их в таблицу переменных.
     * 
     * @return Хэш-массив с результатами обработки
     */
    public void readTags()
    {
        //Creating tags reader thread:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                //Объявляем необходимые переменные:
                HashMap<String, String> tempInstance = new HashMap();
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
        
                Tag tempTag;
                TagSetting tempTagSetting;
                int tagsProcessed = 0, tagsCount = 0;
                
                //Публикуем текущий прогресс:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Calculating instances count");
                publish(progress);
        
                try //Подсчитываем количество инстансов в экспорте DCS:
                {
                    tagsCount = _calculateInstances();
                    
                } catch (Exception exception) {
                         
                    _invokeExceptionInEdt("Error getting instatnces count", exception, WorkerThread.Event.ERROR);
                }//catch
        
                //Публикуем текущий прогресс:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Reading instances");
                publish(progress);
        
                //Читаем файл по инстансам и переносим таги с уставками в таблицу переменных:
                while (tempInstance != null)
                {
                    try
                    {
                        tempInstance = _readNextInstance();
                
                        if (tempInstance != null)
                        {
                            //Создаем таг:
                            tempTag = addTag(tempInstance.get(TAG_NAME_ENTRY_NAME));
                          
                            //Adding range min setting:
                            if (tempInstance.containsKey(RANGE_MIN_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(RANGE_MIN_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                            
                            //Adding range max setting:
                            if (tempInstance.containsKey(RANGE_MAX_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(RANGE_MAX_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                            
                            //Adding units setting:
                            if (tempInstance.containsKey(UNITS_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.UNITS_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(UNITS_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                    
                            //If dump instance contains LL setting, adding it to a tag:
                            if (tempInstance.containsKey(ALARM_LL_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_LL_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                    
                            //If dump instance contains L setting, adding it to a tag:
                            if (tempInstance.containsKey(ALARM_L_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_L_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                    
                            //If dump instance contains H setting, adding it to a tag:
                            if (tempInstance.containsKey(ALARM_H_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_H_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                    
                            //If dump instance contains HH setting, adding it to a tag:
                            if (tempInstance.containsKey(ALARM_HH_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_HH_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }//if
                        }//if
                                          
                    } catch (Exception exception) {
            
                        _invokeExceptionInEdt("Error reading dump instance", exception, WorkerThread.Event.WARNING);
                    }//catch
            
                    tagsProcessed++;
            
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)tagsProcessed / (double)tagsCount * 100));
                    publish(progress);
                }//while
            
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribing existing subscribers on current thread events:
        if (events.get(SourceEvent.THREAD_ERROR) != null) tagsReader.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) tagsReader.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) tagsReader.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.TAGS_READ) != null) tagsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.TAGS_READ));
                
        tagsReader.execute();
    }// readTags       
}// DcsExport
