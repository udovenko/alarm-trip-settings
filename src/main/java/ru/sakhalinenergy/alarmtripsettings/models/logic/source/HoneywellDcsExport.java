package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.HashMap;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Implements logic for creating data source by parsing DCS Honeywell export 
 * file.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class HoneywellDcsExport extends TagsSource implements HoneywellDcsExportObservable, TagsSourceControllable
{
    // Honeywell instance parameters:
    public static final String TAG_NAME_ENTRY_NAME = "Name";
    public static final String BASE_ENTRY_NAME = "Base";
    public static final String UNITS_ENTRY_NAME = "EUDESC";
    public static final String ALARM_LL_ENTRY_NAME = "PVLLALM.TP";
    public static final String ALARM_L_ENTRY_NAME = "PVLOALM.TP";
    public static final String ALARM_H_ENTRY_NAME = "PVHIALM.TP";
    public static final String ALARM_HH_ENTRY_NAME = "PVHHALM.TP";
    public static final String RANGE_MIN_ENTRY_NAME = "PVEULO";
    public static final String RANGE_MAX_ENTRY_NAME = "PVEUHI";
    
    // Honeywell values parameters:
    private static final String PROPER_BLOCK_NAME = "DATAACQ:DATAACQ";
    private static final String NOT_A_NUMBER_FLAG = "NaN";
    
    // Model's fields:
    private String filePath;
    private DataInputStream instancesDataStream;
    private BufferedReader instancesReader;
    
    
    /**
     * Public constructor. Sets up initial source instance.
     * 
     * @param source Data source entity instance to be wrapped in this logic
     */
    public HoneywellDcsExport(Source source)
    {
        super(source);
    }// HoneywellDcsExport
               
    
    /**
     * Returns path to current Honeywell DCS export file.
     * 
     * @return Path to DCS export file
     */
    public String getFilePath()
    {
        return this.filePath;
    }// getFilePath
    
    
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
    }// setFilePath
    
    
    /**
     * Calculates available Honeywell instances count in DCS export file.
     * 
     * @throws IOException
     * @return Honeywell instances count
     */
    private int _calculateInstances() throws IOException
    {
        FileInputStream fileStream = new FileInputStream(this.filePath);
        DataInputStream dataInputStream = new DataInputStream(fileStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(dataInputStream));
        String strLine;
        int instancesCount = 0;
  
        // Read file by lines:
        while ((strLine = br.readLine()) != null)   
        {
            if (strLine.trim().startsWith(TAG_NAME_ENTRY_NAME))
            {
                instancesCount++;
            }// if
        }// while
        
        dataInputStream.close();
        
        return instancesCount;
    }// _calculateInstances
    
    
    /**
     * Reads next Honeywell instance relative to current pointer position in 
     * DCS export file.
     * 
     * @throws IOException
     * @return Instance properties hash or null if end of file has reached
     */
    private HashMap<String, String> _readNextInstance() throws IOException
    {
        // If instances data buffer wasn't created yet - create it:
        if (this.instancesDataStream == null)
        {
            FileInputStream fileStream = new FileInputStream(this.filePath);
            this.instancesDataStream = new DataInputStream(fileStream);
            this.instancesReader = new BufferedReader(new InputStreamReader(this.instancesDataStream));
        }// if
        
        String tempLine, previousLine;
                
        // Read file by lines:
        while ((tempLine = this.instancesReader.readLine()) != null)   
        {
            if (tempLine.trim().startsWith(TAG_NAME_ENTRY_NAME))
            {
                HashMap<String, String> result = new HashMap();
                String trimmedTempLine;
                previousLine = tempLine;
                
                // Receive tag name (together with block name):
                String tagName = tempLine.replace(TAG_NAME_ENTRY_NAME, "").trim();
                
                // Cut out block name:
                if (tagName.indexOf(".") > -1) tagName = tagName.substring(0, tagName.indexOf("."));
                result.put(TAG_NAME_ENTRY_NAME, tagName);
                
                while ((tempLine = this.instancesReader.readLine()) != null)   
                {
                    trimmedTempLine = tempLine.trim();
                    
                    // If string begns with units entry header:                    
                    if (trimmedTempLine.startsWith(UNITS_ENTRY_NAME))
                    {
                        String units = trimmedTempLine.replace(UNITS_ENTRY_NAME, "").trim();
                        result.put(UNITS_ENTRY_NAME, units);
                    }// if
                    
                    // If string begns with LL alarm entry header:                                        
                    if (trimmedTempLine.startsWith(ALARM_LL_ENTRY_NAME))
                    {
                        String alarmLowLow = trimmedTempLine.replace(ALARM_LL_ENTRY_NAME, "").trim();
                        if (!alarmLowLow.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_LL_ENTRY_NAME)) result.put(ALARM_LL_ENTRY_NAME, alarmLowLow);
                    }// if
                    
                    // If string begns with L alarm entry header:                    
                    if (trimmedTempLine.startsWith(ALARM_L_ENTRY_NAME))
                    {
                        String alarmLow = trimmedTempLine.replace(ALARM_L_ENTRY_NAME, "").trim();
                        if (!alarmLow.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_L_ENTRY_NAME)) result.put(ALARM_L_ENTRY_NAME, alarmLow);
                    }// if
                    
                    // If string begns with H alarm entry header:                    
                    if (trimmedTempLine.startsWith(ALARM_H_ENTRY_NAME))
                    {
                        String alarmHigh = trimmedTempLine.replace(ALARM_H_ENTRY_NAME, "").trim();
                        if (!alarmHigh.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_H_ENTRY_NAME)) result.put(ALARM_H_ENTRY_NAME, alarmHigh);
                    }// if
                    
                    // If string begns with HH alarm entry header:                    
                    if (trimmedTempLine.startsWith(ALARM_HH_ENTRY_NAME))
                    {
                        String alarmHighHigh = trimmedTempLine.replace(ALARM_HH_ENTRY_NAME, "").trim();
                        if (!alarmHighHigh.equals(NOT_A_NUMBER_FLAG) && !result.containsKey(ALARM_HH_ENTRY_NAME)) result.put(ALARM_HH_ENTRY_NAME, alarmHighHigh);
                    }// if
                    
                    // If string begns with range min alarm entry header:                    
                    if (trimmedTempLine.startsWith(RANGE_MIN_ENTRY_NAME))
                    {
                        String rangeMin = trimmedTempLine.replace(RANGE_MIN_ENTRY_NAME, "").trim();
                        if (!result.containsKey(RANGE_MIN_ENTRY_NAME)) result.put(RANGE_MIN_ENTRY_NAME, rangeMin);
                    }// if
                    
                    // If string begns with range max alarm entry header:                    
                    if (trimmedTempLine.startsWith(RANGE_MAX_ENTRY_NAME))
                    {
                        String rangeMax = trimmedTempLine.replace(RANGE_MAX_ENTRY_NAME, "").trim();
                        if (!result.containsKey(RANGE_MAX_ENTRY_NAME)) result.put(RANGE_MAX_ENTRY_NAME, rangeMax);
                    }// if
                    
                    // If string begins with block type header:
                    if (trimmedTempLine.startsWith(BASE_ENTRY_NAME))
                    {
                        String base = trimmedTempLine.replace(BASE_ENTRY_NAME, "").trim();
                        if (!base.equals(PROPER_BLOCK_NAME)) return result; 
                        result.put(BASE_ENTRY_NAME, base);
                    }// if
                    
                    // If this is a second empty line in sequence, beleive that instance is read and return result:
                    if (trimmedTempLine.equals("") && previousLine.trim().equals("")) return result;
                                        
                    previousLine = tempLine;
                }// while
            }// if
        }// while
               
        return null;
    }// _readNextInstance
    
    
    /**
     * Creates a thread for reading tags data from Honeywell DCS export file. 
     * Subscribes models events listeners on thread events and executes it.
     */
    public void readTags()
    {
        // Create a thread:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<String, String> tempInstance = new HashMap();
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
        
                Tag tempTag;
                TagSetting tempTagSetting;
                int tagsProcessed = 0, tagsCount = 0;
                
                // Publish current progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Calculating instances count");
                publish(progress);
        
                try // Calculate Honeywell instances count:
                {
                    tagsCount = _calculateInstances();
                } catch (Exception exception) {
                         
                    _invokeExceptionInEdt("Error getting instatnces count", exception, WorkerThread.Event.ERROR);
                }// catch
        
                // Publish current progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Reading instances");
                publish(progress);
        
                // Read file by instances and put data into tags collection:
                while (tempInstance != null)
                {
                    try
                    {
                        tempInstance = _readNextInstance();
                
                        if (tempInstance != null)
                        {
                            // Create tag:
                            tempTag = addTag(tempInstance.get(TAG_NAME_ENTRY_NAME));
                          
                            // Add range min setting:
                            if (tempInstance.containsKey(RANGE_MIN_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(RANGE_MIN_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                            
                            // Add range max setting:
                            if (tempInstance.containsKey(RANGE_MAX_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(RANGE_MAX_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                            
                            // Add units setting:
                            if (tempInstance.containsKey(UNITS_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.UNITS_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(UNITS_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                    
                            // If dump instance contains LL setting, add it to the tag:
                            if (tempInstance.containsKey(ALARM_LL_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_LL_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                    
                            // If dump instance contains L setting, adding it to the tag:
                            if (tempInstance.containsKey(ALARM_L_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_L_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_L_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                    
                            // If dump instance contains H setting, adding it to the tag:
                            if (tempInstance.containsKey(ALARM_H_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_H_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_H_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                    
                            // If dump instance contains HH setting, adding it to the tag:
                            if (tempInstance.containsKey(ALARM_HH_ENTRY_NAME))
                            {
                                tempTagSetting = new TagSetting();
                                tempTagSetting.setTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
                                tempTagSetting.setValue(tempInstance.get(ALARM_HH_ENTRY_NAME));
                                addTagSetting(tempTag, tempTagSetting);
                            }// if
                        }// if
                                          
                    } catch (Exception exception) {
            
                        _invokeExceptionInEdt("Error reading dump instance", exception, WorkerThread.Event.WARNING);
                    }// catch
            
                    tagsProcessed++;
            
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)tagsProcessed / (double)tagsCount * 100));
                    publish(progress);
                }// while
            
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(tagsReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.TAGS_READ);
        
        // Execute thread:
        tagsReader.execute();
    }// readTags       
}// HoneywellDcsExport
