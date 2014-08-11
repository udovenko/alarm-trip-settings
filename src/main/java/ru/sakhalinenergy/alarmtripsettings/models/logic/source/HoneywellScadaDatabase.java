package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 * Implements logic for creating data source by parsing Honeywell SCADA database
 * export file.
 *
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class HoneywellScadaDatabase extends TagsSource implements HoneywellScadaDatabaseObservable, TagsSourceControllable
{
    
    // Database driver paramaeters:
    private static final String DRIVER_NAME = "sun.jdbc.odbc.JdbcOdbcDriver";
    private static final String DRIVER_TYPE = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
    
    // Table structure:
    public static final String TABLE_NAME = "qAnalogPoint";
    public static final String TAG_NAME_FIELD_NAME = "ItemName";
    public static final String UNITS_FIELD_NAME = "Units";
    public static final String ALARM_TYPE_1_FIELD_NAME = "AlarmType1";
    public static final String ALARM_TYPE_2_FIELD_NAME = "AlarmType2";
    public static final String ALARM_TYPE_3_FIELD_NAME = "AlarmType3";
    public static final String ALARM_TYPE_4_FIELD_NAME = "AlarmType4";
    public static final String ALARM_PRIORITY_1_FIELD_NAME = "AlarmPriority1";
    public static final String ALARM_PRIORITY_2_FIELD_NAME = "AlarmPriority2";
    public static final String ALARM_PRIORITY_3_FIELD_NAME = "AlarmPriority3";
    public static final String ALARM_PRIORITY_4_FIELD_NAME = "AlarmPriority4";
    public static final String ALARM_LIMIT_1_FIELD_NAME = "AlarmLimit1";
    public static final String ALARM_LIMIT_2_FIELD_NAME = "AlarmLimit2";
    public static final String ALARM_LIMIT_3_FIELD_NAME = "AlarmLimit3";
    public static final String ALARM_LIMIT_4_FIELD_NAME = "AlarmLimit4";
    public static final String RANGE_LOW_FIELD_NAME = "RangeLow";
    public static final String RANGE_HIGH_FIELD_NAME = "RangeHigh";
    
    // Settings types:
    public static final String ALARM_LOW_LOW_TYPE_NAME = "PVLowLow";
    public static final String ALARM_LOW_TYPE_NAME = "PVLow";
    public static final String ALARM_HIGH_TYPE_NAME = "PVHigh";
    public static final String ALARM_HIGH_HIGH_TYPE_NAME = "PVHighHigh";
    
    // Model's fields:
    private final String userName = "";
    private final String password = "";
    private String filePath;
    private String connectionString;
    private Connection connection;
       
    
    /**
     * Public constructor. Sets up initial source instance.
     * 
     * @param source Source entity instance to be wrapped in current logic
     */
    public HoneywellScadaDatabase(Source source)
    {
        super(source);
    }// ScadaDatabase
    
    
    /**
     * Returns path to SCADA database file.
     * 
     * @return path to database file
     */
    public String getFilePath()
    {
        return this.filePath;
    }// getFilePath
    
    
    /**
     * Sets up current database file path and builds a connection string for it. 
     * Triggers file path update event.
     * 
     * @param filePath File path to database file
     */
    public void setFilePath(String filePath)
    {
        assert (filePath != null) : "File path is null!";
        
        this.filePath = filePath;
        this.connectionString = DRIVER_TYPE + ";DBQ=" + this.filePath + ";ReadOnly=False;ExtendedAnsiSQL=1;";
        
        CustomEvent filePathUpdatedEvent = new CustomEvent(filePath);
        events.trigger(SourceEvent.FILE_PATH_UPDATED, filePathUpdatedEvent);
    }// setFilePath
    
    
    /**
     * Sets up database connection with current instance credential fields data.
     * 
     * @throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException
     */
    private void _connect() throws ClassNotFoundException, InstantiationException,
        IllegalAccessException, SQLException
    {
        assert (connectionString != null && !connectionString.trim().equals("")) : "Connection string is null or empty!";
        assert (userName != null) : "User name is null!";
        assert (password != null) : "Password is null!";
        
        // Load driver class:
        Class.forName(DRIVER_NAME);
        
        // Connect to database: 
        connection = DriverManager.getConnection(connectionString, userName, password);
    }// _connect
    
    
    /**
     * Closes connection to SCADA database.
     * 
     * @throws SQLException
     */
    private void _disconnect() throws SQLException
    {
        this.connection.close();
    }// _disconnect
       
    
    /**
     * Executes select query to SCADA database and returns result rows list.
     * 
     * @throws SQLException
     * @param query Query string
     * @return Result record set as list of hashes for each row
     */
    private List<HashMap> _runSelectQuery(String query) throws SQLException
    {
        Statement statement = this.connection.createStatement();
        
        ResultSet recordset = statement.executeQuery(query);
        
        // Receive record set field names array:
        ResultSetMetaData recordsetMetadata = recordset.getMetaData(); 
        List columnNames = new ArrayList();
         
        for (int i = 1; i <= recordsetMetadata.getColumnCount(); i++)
        {
            columnNames.add(recordsetMetadata.getColumnName(i));
        }// for
         
        List result = new ArrayList();
         
        while (recordset.next()) 
        {
            HashMap row = new HashMap();
             
            for (int i = 1; i <= columnNames.size(); i++)
            {
                row.put((String)columnNames.get(i - 1), recordset.getString(i));
            }// for
             
            result.add(row);
        }// while
         
        recordset.close();
        statement.close();
         
        return result;
   }// _runSelectQuery
    
    
    /**
     * Executes analog SACDA points tags query form database.
     * 
     * @throws SQLException
     * @return Analog points tags record set
     */
    private List<HashMap> _getAnalogPointsTags() throws SQLException
    {
        // Assemble and run query:      
        return this._runSelectQuery("SELECT "
            + TAG_NAME_FIELD_NAME + ", " + UNITS_FIELD_NAME + ", " 
            + ALARM_TYPE_1_FIELD_NAME + ", " + ALARM_TYPE_2_FIELD_NAME + ", "
            + ALARM_TYPE_3_FIELD_NAME + ", " + ALARM_TYPE_4_FIELD_NAME + ", "
            + ALARM_PRIORITY_1_FIELD_NAME + ", " + ALARM_PRIORITY_2_FIELD_NAME + ", "
            + ALARM_PRIORITY_3_FIELD_NAME + ", " + ALARM_PRIORITY_4_FIELD_NAME + ", "
            + ALARM_LIMIT_1_FIELD_NAME + ", " + ALARM_LIMIT_2_FIELD_NAME + ", "
            + ALARM_LIMIT_3_FIELD_NAME + ", " + ALARM_LIMIT_4_FIELD_NAME + ", "
            + RANGE_LOW_FIELD_NAME + ", " + RANGE_HIGH_FIELD_NAME
            + " FROM " + TABLE_NAME);
    }// _getAnalogPointsTags
 
    
    /**
     * Analyzes tag data record from SCADA database and creates setting with
     * given type if one of fields in record contains it. If setting found, adds
     * it to a tag.
     * 
     * @param tagData Tag data record
     * @param dbSettingTypeName Tag setting type name in database
     * @param settingType Application internal setting type id
     * @param tag Reference to a tag for which new setting will be added
     */
    private void _addTagSettingFromTagDataRecord(HashMap tagData, String dbSettingTypeName, int settingType, Tag tag)
    {
        TagSetting tempTagSetting;
        
        if (tagData.get(ALARM_TYPE_1_FIELD_NAME).equals(dbSettingTypeName))
        {    
            tempTagSetting = new TagSetting();
            tempTagSetting.setTypeId(settingType);
            tempTagSetting.setValue((String)tagData.get(ALARM_LIMIT_1_FIELD_NAME));
            addTagSetting(tag, tempTagSetting);
                        
        } else if (tagData.get(ALARM_TYPE_2_FIELD_NAME).equals(dbSettingTypeName)) {
                            
            tempTagSetting = new TagSetting();
            tempTagSetting.setTypeId(settingType);
            tempTagSetting.setValue((String)tagData.get(ALARM_LIMIT_2_FIELD_NAME));
            addTagSetting(tag, tempTagSetting);

        } else if (tagData.get(ALARM_TYPE_3_FIELD_NAME).equals(dbSettingTypeName)) {    

            tempTagSetting = new TagSetting();
            tempTagSetting.setTypeId(settingType);
            tempTagSetting.setValue((String)tagData.get(ALARM_LIMIT_3_FIELD_NAME));
            addTagSetting(tag, tempTagSetting);

        } else if (tagData.get(ALARM_TYPE_4_FIELD_NAME).equals(dbSettingTypeName)) {

            tempTagSetting = new TagSetting();
            tempTagSetting.setTypeId(settingType);
            tempTagSetting.setValue((String)tagData.get(ALARM_LIMIT_4_FIELD_NAME));
            addTagSetting(tag, tempTagSetting);
        }// if
    }// _addTagSettingFromTagDataRecord
    
    
    /**
     * Creates a thread for reading tags data from Honeywell SCADA database file. 
     * Subscribes model's events listeners on thread events and executes it.
     */
    public void readTags()
    {
        // Create a thread:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap<Byte, Object> doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                       
                try // Try to connect to SCADA dastabase:
                {
                    // Publish current sprogress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Connecting to database");
                    publish(progress);
        
                    _connect();
                
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("SCDA database connection error", exception, WorkerThread.Event.ERROR);
                    return new HashMap();
                }// catch 
                
                List<HashMap> databaseTags = null;
                int tagsCount = 0, tagsProcessed = 0;
                                
                try // Receive full list of analog points tags and their parameters:
                {
                    // Publish current sprogress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Getting analog points dataset");
                    publish(progress);
            
                    databaseTags = _getAnalogPointsTags();
                    tagsCount = databaseTags.size();
            
                } catch (Exception exception) {
        
                    _invokeExceptionInEdt("Getting analog points tags list error", exception, WorkerThread.Event.ERROR);
                    return new HashMap();
                }// catch
                 
                // Publish current sprogress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Creating data source tags");
                publish(progress);
        
                Tag tempTag;                
                TagSetting tempTagSetting;
                String tagName, rangeMin, rangeMax, units;
                                    
                // Iterate through received analog points tags set:
                for (HashMap tagData : databaseTags)
                {
                    tagName = (String)tagData.get(TAG_NAME_FIELD_NAME);
                    rangeMin = (String)tagData.get(RANGE_LOW_FIELD_NAME);
                    rangeMax = (String)tagData.get(RANGE_HIGH_FIELD_NAME);
                    units = (String)tagData.get(UNITS_FIELD_NAME);
              
                    try
                    {
                        // Add new tag:
                        tempTag = addTag(tagName);
                                                  
                        // Add range min setting to tag:
                        tempTagSetting = new TagSetting();
                        tempTagSetting.setTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
                        tempTagSetting.setValue(rangeMin);
                        addTagSetting(tempTag, tempTagSetting);
                        
                        // Add range max setting:
                        tempTagSetting = new TagSetting();
                        tempTagSetting.setTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
                        tempTagSetting.setValue(rangeMax);
                        addTagSetting(tempTag, tempTagSetting);
                        
                        // Add units setting:
                        tempTagSetting = new TagSetting();
                        tempTagSetting.setTypeId(SettingsTypes.UNITS_SETTING.ID);
                        tempTagSetting.setValue(units);
                        addTagSetting(tempTag, tempTagSetting);
            
                        // Add LL alarm setting:
                        _addTagSettingFromTagDataRecord(tagData, ALARM_LOW_LOW_TYPE_NAME, SettingsTypes.ALARM_LL_SETTING.ID, tempTag);
                        
                        // Add L alarm setting:
                        _addTagSettingFromTagDataRecord(tagData, ALARM_LOW_TYPE_NAME, SettingsTypes.ALARM_L_SETTING.ID, tempTag);
                                    
                        // Add H alarm setting:
                        _addTagSettingFromTagDataRecord(tagData, ALARM_HIGH_TYPE_NAME, SettingsTypes.ALARM_H_SETTING.ID, tempTag);
                        
                        // Add HH alarm setting:
                        _addTagSettingFromTagDataRecord(tagData, ALARM_HIGH_HIGH_TYPE_NAME, SettingsTypes.ALARM_HH_SETTING.ID, tempTag);
                        
                    } catch (Exception exception) {

                        _invokeExceptionInEdt("SCADA instance reading error", exception, WorkerThread.Event.WARNING);
                    }// catch

                    // Publish current sprogress:
                    tagsProcessed++;
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)(((double)tagsProcessed) / ((double)tagsCount) * 100));
                    publish(progress);
                }// for

                try // Disconnect from SCDA database:
                {
                    // Publish current sprogress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Disconnecting from database");
                    publish(progress);
                    
                    _disconnect();

                } catch (Exception exception){

                    _invokeExceptionInEdt("SCADA database connection closing error", exception, WorkerThread.Event.WARNING);
                }// catch
                
                return new HashMap();
            }// doInBackground
        };// tagsReader
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(tagsReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.TAGS_READ);
                
        // Executing thread:
        tagsReader.execute();
    }// readTags
}// HoneywellScadaDatabase