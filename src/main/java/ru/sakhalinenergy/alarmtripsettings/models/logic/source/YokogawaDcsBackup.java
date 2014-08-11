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
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgFileHeader;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgFileHeaderContents;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgBlockRecord;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgTagRecord;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 * Implements logic for creating data source by parsing Yokogawa DSC backup.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
public class YokogawaDcsBackup extends TagsSource implements YokogawaDcsBackupObservable, YokogawaDcsBackupControllable
{
    private String backupFolderPath;
    private List<YgStationRecord> stations;
        
    
    /**
     * Public constructor. Sets up initial source instance.
     * 
     * @param source Source entity instance to be wrapped in current logic
     */
    public YokogawaDcsBackup(Source source)
    {
        super(source);
    }// YokogawaDcsBackupDataSourceCreator
     
    
    /**
     * Sets backup folder path field value.
     * 
     * @param backupFolderPath Path to backup folder
     */
    @Override
    public void setBackupFolderPath(String backupFolderPath)
    {
        this.backupFolderPath = backupFolderPath;
    }// setBackupFolderPath
    
    
    /**
     * Returns current backup folder path.
     *
     * @return Backup folder path
     */
    @Override
    public String getBackupFolderPath()
    {
        return backupFolderPath;
    }// getBackupFolderPath
    
    
    /**
     * Returns stations collection.
     * 
     * @return Stations collection
     */
    @Override
    public List<YgStationRecord> getStations()
    {
        return stations;
    }// getStations
    
    
    /**
     * Reads header of Yokogawa DCS backup file and returns header 
     * instance.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param filePath Path to file
     * @return File header instance
     */
    private YgFileHeader _readFileHeader(String filePath) throws FileNotFoundException, IOException
    {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        byte[] headerBytes = new byte[512];
        file.read(headerBytes);
        
        YgFileHeader header = new YgFileHeader(headerBytes); 
        
        file.close();
        return header;
    }// _readFileHeader
    
    
    /**
     * Reads content of tuning parameters file in a string.
     * 
     * @throws FileNotFoundException 
     * @param filePath Path to tuning parameters file
     * @return Tuning parameters string
     */
    private String _readTunningParameters(String filePath) throws FileNotFoundException
    {
        // Read file content entirely:
        String content = "";
        Scanner in = new Scanner(new File(filePath));
        while(in.hasNext()) content += in.nextLine();
        in.close();
    
        return content;
    }// _readTunningParameters
    
    
    /**
     * Returns list of functional diagram file names for given station.
     * 
     * @param station Yokogawa station record instance
     * @return Functional diagram file names list
     */
    private TreeSet<String> _getStationFbdList(YgStationRecord station)
    {
        TreeSet<String> result = new TreeSet<String>();
        
        // Get directory files list:
        File folder = new File(this.backupFolderPath + File.separator + station.getStationName().trim() + File.separator + "FUNCTION_BLOCK");
        File[] listOfFiles = folder.listFiles();
        
        // Filter file names and put only functional diagram files (.edf) in result list:
        for (File file : listOfFiles) 
        {
            if (file.isFile() && file.getName().startsWith("DR") && file.getName().endsWith(".edf")) result.add(file.getName());
        }// for
        
        return result;
    }// _getStationFbdList
    
    
    /**
     * Reads engineering units library file.
     * 
     * @throws FileNotFoundException  
     * @throws IOException
     * @param filePath Path to engineering units file
     * @param fileHeader Yokogawa file header instance
     * @return Engineering unit names collection
     */
    private List<String> _readAsEngUnitsList(String filePath, YgFileHeader fileHeader) throws FileNotFoundException, IOException
    {
        List<String> result = new ArrayList();
        RandomAccessFile engUnitsFile = new RandomAccessFile(filePath, "r");
        YgFileHeaderContents contentItem;
        
        contentItem = fileHeader.getContentItem("EUNT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
    
        // Move file pointer to beginning of section with units description:
        engUnitsFile.seek(contentItem.getOffset());
        byte[] tempUnitsBytes = new byte[8]; 
        String tempUnits;
        
        // Read all available units:
        for (int i = 0; i < count; i++)
        {
            engUnitsFile.read(tempUnitsBytes);
            tempUnits = new String(tempUnitsBytes);
            result.add(tempUnits);
        }// for
        
        engUnitsFile.close();
        return result;
    }// _readAsEngUnitsList
    
    
    /**
     * Reads functional diagram blocks descriptions from functional diagram 
     * file.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param filePath Path to functional diagram file
     * @param fileHeader Yokogawa file header instance
     * @return List of Yokogawa functional block records
     */
    private List<YgBlockRecord> _readFbdBlocks(String filePath, YgFileHeader fbdHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile diagramFile = new RandomAccessFile(filePath, "r");
        
        List<YgBlockRecord> result = new ArrayList<YgBlockRecord>();
        YgFileHeaderContents contentItem;
        
        contentItem = fbdHeader.getContentItem("RGTL");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
            
        // Move file pointer to beginning of section with functinal blocks description:
        diagramFile.seek(contentItem.getOffset());
        YgBlockRecord tempBlockDescription;
        byte[] temp48BytesArray = new byte[48];
        
        // Read all available block records:
        for (int i = 0; i < count; i++)
        {
            diagramFile.read(temp48BytesArray);
            tempBlockDescription = new YgBlockRecord(temp48BytesArray);
            result.add(tempBlockDescription);
        }// for
        
        diagramFile.close();
        return result;
    }// _readFbdBlocks
    
    
    /**
     * Reads functional block tags set from Yokogawa DCS backup file content.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param  filePath Path to file
     * @param  blockFileHeader Yokogawa file header instance
     * @return List of Yokogawa tag record instances
     */
    private List<YgTagRecord> _readAsBlockTagsList(String filePath, YgFileHeader blockFileHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile blockFile = new RandomAccessFile(filePath, "r");
        
        List<YgTagRecord> result = new ArrayList<YgTagRecord>();
        YgFileHeaderContents contentItem;
        
        contentItem = blockFileHeader.getContentItem("HTLT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
        
        // Move file pointer to beginning of section with functinal block's tag descriptions:
        blockFile.seek(contentItem.getOffset());
        byte[] tagRecordBytes = new byte[128];
        
        // Read all available tag records:
        for (int i = 0; i < count; i++)
        {
            blockFile.read(tagRecordBytes);
            YgTagRecord tagRecord = new YgTagRecord(tagRecordBytes);
            result.add(tagRecord);
        }// for
        
        blockFile.close();
        return result;
    }// _readAsBlockTagsList
    
    
    /**
     * Reads functional block's tuning parameters as string.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param  filePath Path to file
     * @param  blockFileHeader Yokogawa file header instance
     * @return Block tuning parameters string
     */
    private String _readBlockTuningParameters(String filePath, YgFileHeader blockFileHeader) throws FileNotFoundException, IOException
    {
        RandomAccessFile blockFile = new RandomAccessFile(filePath, "r");
        String result = "";
        
        YgFileHeaderContents contentItem;
        
        contentItem = blockFileHeader.getContentItem("HTLT");
        if (contentItem == null) return result;
        
        long count = contentItem.getNumber();
        if (count <= 0) return result;
        
        // Move file pointer to beginning of section with functinal block's tuning parameters:
        blockFile.seek(contentItem.getOffset() + 128 * count);
        byte[] blockParamsBytes = new byte[1024];
        
        blockFile.read(blockParamsBytes);
        return new String(blockParamsBytes);
    }// _readBlockTuningParameters
    
    
    
    /**
     * Filters tag's tuning parameters form functional block's tuning parameters
     * string.
     * 
     * @throws FileNotFoundException
     * @throws IOException
     * @param  tagRecord Yokogawa tag record instance
     * @param  blockTunningParameters Tag's parent block tuning parameters string
     * @return Tag's tuning parameters hash
     */
    private HashMap<String, String> _readTagTuningParameters(YgTagRecord tagRecord, String blockTunningParameters)
    {
        HashMap<String, String> result = new HashMap();
        int tagParamsStartPos = blockTunningParameters.indexOf(tagRecord.getTagName().trim() + ":");
        int tagParamsEndPos = blockTunningParameters.indexOf(";", tagParamsStartPos);
        
        if (tagParamsStartPos > -1)
        {
            String tagTunnigParams = blockTunningParameters.substring(tagParamsStartPos, tagParamsEndPos);
            String[] paramsAndValues = tagTunnigParams.split(",");
            String[] tempParamAndValuePair;
            
            for (String tempParamAndValue : paramsAndValues)
            {
                tempParamAndValuePair = tempParamAndValue.split("=");
                if (tempParamAndValuePair.length > 1) result.put(tempParamAndValuePair[0], tempParamAndValuePair[1]);
            }// for
        }// if
        
        return result;
    }// _readTagTuningParameters
     
    
    /**
     * Creates from Yokogawa backup data and adds system tag to current data 
     * source.
     *
     * @throws Exception
     * @param tagRecord Yokogawa tag record instance
     * @param units Engineering units list
     * @param tagTunningParams Tag's tuning parameters hash
     * @param blockTuningParams Tag's block tuning parameters string
     */
    private void _createSystemTagInstance(YgTagRecord tagRecord, List<String> units,
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
        
        // If tag's settngs contain "trip" setpoints:
        if (hhllParamStartIndex > -1)
        {
            hhllParamEndIndex = blockTuningParams.indexOf(";", hhllParamStartIndex);
            existingAlarms = blockTuningParams.substring(hhllParamStartIndex + 5, hhllParamEndIndex);
                        
            // If tag's settngs contain both low and high "trip" settings:
            if (existingAlarms.equals("HHLL"))
            {
                if (tagTunningParams.containsKey("LL"))
                {    
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("LL")).toString());
                    addTagSetting(tag, setting);
                }// if
                
                if (tagTunningParams.containsKey("HH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("HH")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if    
            
            // If tag's settngs contain only high "trip" setting:
            else if (existingAlarms.equals("HH"))
            { 
                if (tagTunningParams.containsKey("HH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("HH")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if
            
            // If tag's settngs contain only low "trip" setting:
            else if (existingAlarms.equals("LL"))
            { 
                if (tagTunningParams.containsKey("LL"))
                {    
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("LL")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if 
        }// if
        
        
        // If tag's setting contain alarm setpoints:
        if (inhlParamStartIndex > -1)
        {
            inhlParamEndIndex = blockTuningParams.indexOf(";", inhlParamStartIndex);
            existingAlarms = blockTuningParams.substring(inhlParamStartIndex + 5, inhlParamEndIndex);
                        
            // If tag's settings contain both alarm low and alarm high setpoints:
            if (existingAlarms.equals("HL"))
            {
                if (tagTunningParams.containsKey("PL"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PL")).toString());
                    addTagSetting(tag, setting);
                }// if
                                
                if (tagTunningParams.containsKey("PH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PH")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if
            
            // If tag's settings contain alarm high setpoint:
            else if (existingAlarms.equals("H"))
            {
                if (tagTunningParams.containsKey("PH"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PH")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if
            
            // If tag's settings contain alarm low setpoint:
            else if (existingAlarms.equals("L"))
            {
                if (tagTunningParams.containsKey("PL"))
                {   
                    setting = new TagSetting();
                    setting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                    setting.setValue(new Double(tagTunningParams.get("PL")).toString());
                    addTagSetting(tag, setting);
                }// if
            }// if
        }// if
            
        // Add range min settng:                               
        tempDouble = tagRecord.getSl() / Math.pow(10, tagRecord.getDp());
        setting = new TagSetting();
        setting.setTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
        setting.setValue(tempDouble.toString());
        addTagSetting(tag, setting);
                
        // Add range max settng:
        tempDouble = tagRecord.getSh() / Math.pow(10, tagRecord.getDp());
        setting = new TagSetting();
        setting.setTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
        setting.setValue(tempDouble.toString());
        addTagSetting(tag, setting);
                  
        // Add engineering units setting:
        if (tagRecord.getEngUnitsIndex() < units.size())
        {
            setting = new TagSetting();
            setting.setTypeId(SettingsTypes.UNITS_SETTING.ID);
            setting.setValue(units.get(tagRecord.getEngUnitsIndex()));
            addTagSetting(tag, setting);
        }// if  
    }// _createSystemTagInstance
    
    
    /**
     * Creates a thread for getting Yokogawa DCS backup stations collection. 
     * Subscribes model's events listeners on thread events and executes it.
     */
    @Override
    public void readStations()
    {
        //Create a thread:
        WorkerThread stationsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                List<YgStationRecord> stationsList = new ArrayList();
                try
                {
                    // If folder path wasn't set:
                    if (backupFolderPath == null) throw new Exception("Backup folder path is null");
                    
                    // Building stations info file name:
                    String filePath = backupFolderPath + File.separator + "Common" + File.separator + "StnConf.edf";        
                     
                    YgFileHeader stationsFileHeader = _readFileHeader(filePath);
        
                    RandomAccessFile stationsFile = new RandomAccessFile(filePath, "r");
                    YgFileHeaderContents contentItem;
        
                    contentItem = stationsFileHeader.getContentItem("STIF");
                    if (contentItem == null) throw new Exception("Content item STIF not found in StnConf.edf");
        
                    long count = contentItem.getNumber();
                    if (count <= 0) throw new Exception("Stations count in StnConf.edf header is zero");
        
                    // Move file pointer to beginning of section with stations descriptions:
                    stationsFile.seek(contentItem.getOffset());
                    YgStationRecord tempStationRecord;
                    byte[] temp256BytesArray = new byte[256];

                    // Read all available stations:
                    for (int i = 0; i < count; i++)
                    {
                        stationsFile.read(temp256BytesArray);
                        tempStationRecord = new YgStationRecord(temp256BytesArray);
                        if (tempStationRecord.getStationName().startsWith("FCS")) stationsList.add(tempStationRecord);
                    }// for
        
                    stationsFile.close();
            
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Stations collection reading error", exception, WorkerThread.Event.ERROR);
                }// catch
    
                // Set current model's station collection:
                stations = stationsList;
                return new HashMap();
            }// doInBackground
        };// WorkerThread
             
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(stationsReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.STATIONS_READ);
             
        // Execute thread:
        stationsReader.execute();
    }// readStations
        
    
    /**
     * Creates a thread for getting tags from found stations. Subscribes model's 
     * events listeners on thread events and executes it.
     * 
     * @param selectedStations List of stations instances references, from which tags will be read
     */
    @Override
    public void readTags(final List<YgStationRecord> selectedStations)
    {
        // Create a thread:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                // Initialize hash to store progress:
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.OUTER_CYCLE_PERCENTAGE, 0);
                progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, 0);
        
                // Read engineering units library: 
                List<String> units = null;
               
                // Publish current progress:
                progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "Getting units library");
                publish(progress);
        
                try 
                {       
                    YgFileHeader engUnitsFileHeader = _readFileHeader(backupFolderPath + File.separator + "Common" + File.separator + "EngUnit.edf");
                    units = _readAsEngUnitsList(backupFolderPath + File.separator + "Common" + File.separator + "EngUnit.edf", engUnitsFileHeader);
            
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Yokogawa DCS read stations error", exception, WorkerThread.Event.ERROR);
                }// catch 
               
                // Parse all field control stations and gather functional blocks data:
                YgFileHeader diagramFileHeader;
                YgFileHeader blockFileHeader;
                TreeSet<String> functionalBlockDiagrams;
                List<YgBlockRecord> diagramBlockDescriptions;
                List<YgTagRecord> blockTags;
                String tempStationName;
                String tempTuningParameters;
                String tempBlockTuninngParameters;
                HashMap<String, String> tempTagTunningParams;
                int totalStations = selectedStations.size();
                int stationsRead = 0;
                int totalDiagrams;
                int diagramsRead;
        
                // Iterate all found stations:
                for (YgStationRecord station : selectedStations)
                {
                    functionalBlockDiagrams = _getStationFbdList(station);
                    totalDiagrams = functionalBlockDiagrams.size();
                    diagramsRead = 0;
            
                    // Get trimmed station's name:
                    tempStationName = station.getStationName().trim();
            
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.OUTER_CYCLE_CAPTION, "Reading station " + tempStationName);
                    publish(progress);
                    
                    try
                    {
                        // Publish current progress:
                        progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "Reading tunning parameters");
                        progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, 0);
                        publish(progress);
                
                        // Read tuning parameters:
                        tempTuningParameters = _readTunningParameters(backupFolderPath + File.separator + tempStationName
                            + File.separator + "PROG" + File.separator + "TunningData.txt");
                
                        // Iterate all functional diagram files for each station:
                        for (String diagramName : functionalBlockDiagrams)
                        {
                            // Publish current progress:
                            progress.put(ProgressInfoKey.INNER_CYCLE_CAPTION, "Reading diagram " + diagramName);
                            publish(progress);
                    
                            // Read diagram file header and diagram's blocks descriptions:
                            diagramFileHeader = _readFileHeader(backupFolderPath + File.separator + tempStationName
                                + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName);
                            diagramBlockDescriptions = _readFbdBlocks(backupFolderPath + File.separator + tempStationName
                                + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName, diagramFileHeader);
                
                            // Iterate all functional block descriptions:
                            for (YgBlockRecord tempBlockDescription : diagramBlockDescriptions)
                            {
                                blockFileHeader = _readFileHeader(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf");
                                blockTags = _readAsBlockTagsList(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf", blockFileHeader);
                                tempBlockTuninngParameters = _readBlockTuningParameters(backupFolderPath + File.separator + tempStationName 
                                    + File.separator + "FUNCTION_BLOCK" + File.separator + diagramName.replace(".edf", "") 
                                    + File.separator + tempBlockDescription.getTagName().trim() + ".edf", blockFileHeader);
                        
                                // Interate all received tags:
                                for (YgTagRecord tagRecord : blockTags)
                                {
                                    tempTagTunningParams = _readTagTuningParameters(tagRecord, tempTuningParameters);
                       
                                    try // Create system tag instance with its properties:
                                    {
                                        _createSystemTagInstance(tagRecord, units, tempTagTunningParams, tempBlockTuninngParameters);           
                                                                   
                                    } catch (Exception exception) {
                                
                                        _invokeExceptionInEdt("Tag " + tagRecord.getTagName().trim() + " creation error", exception, WorkerThread.Event.WARNING);
                                    }// catch
                                }// for                        
                            }// for
                    
                            diagramsRead++;
                    
                            // Publish current progress:
                            progress.put(ProgressInfoKey.INNER_CYCLE_PERCENTAGE, (int)((double)diagramsRead / (double)totalDiagrams * 100));
                            publish(progress);
                        }// for
                    } catch (Exception exception){
                
                        _invokeExceptionInEdt("Yokogawa DCS backup parsing error", exception, WorkerThread.Event.ERROR);
                    }// catch
            
                    stationsRead++;
                       
                    // Publish current progress:
                    progress.put(ProgressInfoKey.OUTER_CYCLE_PERCENTAGE, (int)((double)stationsRead / (double)totalStations * 100));
                    publish(progress);
                }// for

                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(tagsReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.TAGS_READ);
        
        // Execute thread:
        tagsReader.execute();
    }// doInBackground
}// YokogawaDcsBackupDataSourceCreator
