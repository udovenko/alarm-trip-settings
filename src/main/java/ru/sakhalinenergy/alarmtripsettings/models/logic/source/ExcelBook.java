package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.io.File;
import java.util.Set;
import java.util.List;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.provider.XlsDataProvider;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Implements logic for creating data source by parsing sheet form given 
 * MS Excel book.
 *
 * @author Denis Udovenko
 * @version 1.1.1
 */
public class ExcelBook extends TagsSource implements ExcelBookObservable, ExcelBookControlable
{
    private static final String BOOK_COPY_PATH = Main.TEMP_DIR + File.separator + "resetFormat.xls";
    
    private XlsDataProvider dataProvider;
    private final HashMap<String, List> sheetsAndFields = new HashMap();
       
    
    /**
     * Public constructor. Sets up initial source instance.
     * 
     * @param source Data source entity instance to be wrapped in this logic
     */
    public ExcelBook(Source source)
    {
        super(source);
    }// ExcelBook
      
    
    /**
     * Disconnects MS Excel data provider form book.
     * 
     * @throws SQLException
     */
    @Override
    public void disconnect() throws SQLException
    {
        dataProvider.disconnect();
    }// disconnect
    
    
    /**
     * Returns MS Excel book connection status.
     * 
     * @throws SQLException
     * @return True, if connection is open, else false 
     */
    @Override
    public boolean isConnected() throws SQLException
    {
        return dataProvider != null && dataProvider.isConnected();
    }// isConnected
           
    
    /**
     * Returns current book path, if book is connected to provider.
     * 
     * @return Path to book or null if book is not connected
     */
    @Override
    public String getBookFilePath()
    {
        return (dataProvider != null) ? dataProvider.filePath : null;
    }// getBookFilePath
    
    
    /**
     * Returns sheets and fields hash received from book.
     * 
     * @return Book sheets and fields hash
     */
    @Override
    public HashMap getSheetsAndFields()
    {
        return sheetsAndFields;
    }// getSheetsAndFields
    
    
    /**
     * Returns current data source entity.
     * 
     * @return Data source entity
     */
    @Override
    public Source getSource()
    {
        return source;
    }// getTags    
    
    
    /**
     * Creates a thread for connecting provider to MS Excel book and reading 
     * sheets and fields hash. Subscribes models events listeners on thread 
     * events and executes it.
     * 
     * @param filePath Path to MS Excel book file
     */
    @Override
    public void connectAndReadStructure(final String filePath)
    {
        // Create a thread:
        WorkerThread sheetsAndTablesReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                              
                try
                {    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Connecting to book via ODBC");
                    publish(progress);
                    
                    // Connect to book:
                    if (isConnected()) disconnect();
                    if (dataProvider == null) dataProvider = new XlsDataProvider(filePath);
                    dataProvider.connect();
                        
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Reading sheets and fields");
                    publish(progress);
                    
                    List<String> tableNames = dataProvider.getTables();
                    List<String> fields;
            
                    // Gather fields lists into sheets and fields hash:
                    for (String tableName : tableNames)
                    {
                        fields = dataProvider.getFields(tableName);
                        sheetsAndFields.put(tableName, fields);
                    }// for
     
                } catch (Exception exception) { // Invoke exception event in EDT:
                     
                    _invokeExceptionInEdt("Error during book connection", exception, WorkerThread.Event.ERROR);
                }// catch
                
                return sheetsAndFields;
            }// doInBackground
        };// sheetsAndTablesReader
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(sheetsAndTablesReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.BOOK_CONNECTED);

        // Execute thread:
        sheetsAndTablesReader.execute();
    }// connectAndReadStructure
    
    
    /**
     * Creates a thread for reading tags from connected MS Excel book sheet. 
     * Subscribes models events listeners on thread events and executes it.
     * 
     * @param tagMask Tag mask which will be used for processing tags in selected sheet
     */
    @Override
    public void readTags(final TagMask tagMask)
    {
        // Create a thread:
        WorkerThread tagsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                // Publish current progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Resetting book format");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 5);
                publish(progress);
                
                // Create data provider for current book copy:
                XlsDataProvider copyBookDataProvider = null;
                                
                try // Create a book copy in a temp directory and reset book's cells format to text:
                {    
                    dataProvider.createDatabaseCopyWithResetFormat(BOOK_COPY_PATH);
                    copyBookDataProvider = new XlsDataProvider(BOOK_COPY_PATH);
                    copyBookDataProvider.connect();
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Book copy creation / cells format resetting error", exception, WorkerThread.Event.ERROR);
                }// catch 
                
                // Publish current progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Executing rows query");
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 70);
                publish(progress);
                
                ExcelBookParsingDialogSettings settingsInstatnce = ExcelBookParsingDialogSettings.getInstance();
        
                // Determine neccessary sheet's fields set:
                Set<String> distinctFields = _selectDistingFields(settingsInstatnce);
                
                // Assemble query from received field names:
                String query = "SELECT ";
                for (String field : distinctFields) query = query + field + ", ";
                query = query.substring(0, query.lastIndexOf(", "));
                query = query + " FROM [" + settingsInstatnce.getSheetName() + "$]";
                
                List<HashMap> tagsRecordset = null;
                int tagsCount = 0;
                
                try // Execute query and get tags set:
                {
                    tagsRecordset = copyBookDataProvider.selectQuery(query);
                    tagsCount = tagsRecordset.size();
            
                } catch (Exception exception) { // Invoke exception event in EDT:
                     
                    _invokeExceptionInEdt("Tags recordset creation error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                // Publish current progress:
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Creating tags");
                publish(progress);

                String tempTagName;
                Tag tempTag;
                TagSetting tempSetting;
                Map<String, Tag> processedTags = new HashMap();
                
                // Iterate through found tag records and try to add tags to collection together with their settings: 
                for (int i = 0; i < tagsCount; i++)
                {
                    HashMap row = tagsRecordset.get(i);
                    tempTagName = (String)row.get(settingsInstatnce.getTagNameField());
                    
                    try
                    {
                        // If tag is already in collection, add settings to existing one:
                        if (processedTags.containsKey(tempTagName)) tempTag = processedTags.get(tempTagName);
                        else {
                        
                            // Add new tag instance in collection: 
                            tempTag = addTag((String)row.get(settingsInstatnce.getTagNameField()));
                        
                            // Save a reference to created tag with tag name key:
                            processedTags.put(tempTagName, tempTag);
                        }// else
                        
                        try // Look for a LL alarm setting in current string:
                        {    
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_LL_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmLowLowKeyField(), settingsInstatnce.getAlarmLowLowKeyValue(), 
                                settingsInstatnce.getAlarmLowLowValueField(), settingsInstatnce.getAlarmLowLowPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a L alarm setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_L_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmLowKeyField(), settingsInstatnce.getAlarmLowKeyValue(), 
                                settingsInstatnce.getAlarmLowValueField(), settingsInstatnce.getAlarmLowPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a H alarm setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_H_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmHighKeyField(), settingsInstatnce.getAlarmHighKeyValue(), 
                                settingsInstatnce.getAlarmHighValueField(), settingsInstatnce.getAlarmHighPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a HH alarm setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.ALARM_HH_SETTING.ID, distinctFields,
                                settingsInstatnce.getAlarmHighHighKeyField(), settingsInstatnce.getAlarmHighHighKeyValue(), 
                                settingsInstatnce.getAlarmHighHighValueField(), settingsInstatnce.getAlarmHighHighPossibleFlagField());
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a range min setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.RANGE_MIN_SETTING.ID, distinctFields,
                                settingsInstatnce.getRangeMinKeyField(), settingsInstatnce.getRangeMinKeyValue(), 
                                settingsInstatnce.getRangeMinValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a range max setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.RANGE_MAX_SETTING.ID, distinctFields,
                                settingsInstatnce.getRangeMaxKeyField(), settingsInstatnce.getRangeMaxKeyValue(), 
                                settingsInstatnce.getRangeMaxValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a units setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.UNITS_SETTING.ID, distinctFields,
                                settingsInstatnce.getUnitsKeyField(), settingsInstatnce.getUnitsKeyValue(), 
                                settingsInstatnce.getUnitsValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                        try // Look for a source sytem setting in current string:
                        {
                            tempSetting = _readTagSetting(row, SettingsTypes.SOURCE_SYSTEM_SETTING.ID, distinctFields,
                                settingsInstatnce.getSourceSystemKeyField(), settingsInstatnce.getSourceSystemKeyValue(), 
                                settingsInstatnce.getSourceSystemValueField(), null);
                            if (tempSetting != null) addTagSetting(tempTag, tempSetting);
                        } catch (Exception exception) {}
                        
                    } catch (Exception exception) { // Invoke exception event in EDT and wait:
                        
                        _invokeExceptionInEdtAndWait("Can't create tag " + tempTagName, exception, WorkerThread.Event.WARNING);
                    }// catch
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)i / (double)tagsCount * 100));
                    publish(progress);
                }// for

                try // Try to disconnect from both initial book and its copy with reseted cells format:
                {
                    dataProvider.disconnect();
                    copyBookDataProvider.disconnect();
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Books disconnecting error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                return new HashMap();
            }// doInBackground
        };// sheetsAndTablesReader
       
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(tagsReader, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.TAGS_READ);
        
        // Execute thread: 
        tagsReader.execute();
    }// readTags
       
    
    /**
     * Filters distinct fields set from book parsing settings for tags query
     * assembling.
     * 
     * @param settingsInstatnce MS Excel book parsing settings instance
     * @return Distinct field names set
     */
    private Set<String> _selectDistingFields(ExcelBookParsingDialogSettings settingsInstatnce)
    {
        Set<String> distinctFields = new TreeSet();
        distinctFields.add(settingsInstatnce.getTagNameField());
        
        if (settingsInstatnce.getAlarmLowLowKeyField() != null && !settingsInstatnce.getAlarmLowLowKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowKeyField());
        if (settingsInstatnce.getAlarmLowLowValueField() != null && !settingsInstatnce.getAlarmLowLowValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowValueField());
        if (settingsInstatnce.getAlarmLowLowPossibleFlagField() != null && !settingsInstatnce.getAlarmLowLowPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowLowPossibleFlagField());
        
        if (settingsInstatnce.getAlarmLowKeyField() != null && !settingsInstatnce.getAlarmLowKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowKeyField());
        if (settingsInstatnce.getAlarmLowValueField() != null && !settingsInstatnce.getAlarmLowValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowValueField());
        if (settingsInstatnce.getAlarmLowPossibleFlagField() != null && !settingsInstatnce.getAlarmLowPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmLowPossibleFlagField());
        
        if (settingsInstatnce.getAlarmHighKeyField() != null && !settingsInstatnce.getAlarmHighKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighKeyField());
        if (settingsInstatnce.getAlarmHighValueField() != null && !settingsInstatnce.getAlarmHighValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighValueField());
        if (settingsInstatnce.getAlarmHighPossibleFlagField() != null && !settingsInstatnce.getAlarmHighPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighPossibleFlagField());
        
        if (settingsInstatnce.getAlarmHighHighKeyField() != null && !settingsInstatnce.getAlarmHighHighKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighKeyField());
        if (settingsInstatnce.getAlarmHighHighValueField() != null && !settingsInstatnce.getAlarmHighHighValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighValueField());
        if (settingsInstatnce.getAlarmHighHighPossibleFlagField() != null && !settingsInstatnce.getAlarmHighHighPossibleFlagField().trim().equals("")) distinctFields.add(settingsInstatnce.getAlarmHighHighPossibleFlagField());
        
        if (settingsInstatnce.getRangeMinKeyField() != null && !settingsInstatnce.getRangeMinKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMinKeyField());
        if (settingsInstatnce.getRangeMinValueField() != null && !settingsInstatnce.getRangeMinValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMinValueField());
        
        if (settingsInstatnce.getRangeMaxKeyField() != null && !settingsInstatnce.getRangeMaxKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMaxKeyField());
        if (settingsInstatnce.getRangeMaxValueField() != null && !settingsInstatnce.getRangeMaxValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getRangeMaxValueField());
        
        if (settingsInstatnce.getUnitsKeyField() != null && !settingsInstatnce.getUnitsKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getUnitsKeyField());
        if (settingsInstatnce.getUnitsValueField() != null && !settingsInstatnce.getUnitsValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getUnitsValueField());
        
        if (settingsInstatnce.getSourceSystemKeyField() != null && !settingsInstatnce.getSourceSystemKeyField().trim().equals("")) distinctFields.add(settingsInstatnce.getSourceSystemKeyField());
        if (settingsInstatnce.getSourceSystemValueField() != null && !settingsInstatnce.getSourceSystemValueField().trim().equals("")) distinctFields.add(settingsInstatnce.getSourceSystemValueField());
        
        return distinctFields;
    }// _selectDistingFields
    
    
    /**
     * Tries to receive tag's setting of given type from tags record set row. 
     * 
     * @throws Exception
     * @param tagRow Tags record set row from MS Excel sheet
     * @param settingType Type id of setting we're looking for
     * @param distinctFields Set on field names, included into query and available in record set row
     * @param keyField Name of field which contains settings keys
     * @param keyValue Key value for sought-for setting (for example "SP_HH")
     * @param valueField Name of field which contains settings values
     * @param possibleFlagField Name of field which contains "Possible" flag values
     */
    private TagSetting _readTagSetting(HashMap tagRow, int settingType, Set distinctFields, 
        String keyField, String keyValue, String valueField, String possibleFlagField) throws Exception
    {
        TagSetting resultSetting = null;
        TagSettingProperty resultSettingProperty;
        String tempKey;
        
        // If fields with setting key and setting value are query fields:
        if (valueField != null && keyField != null && distinctFields.contains(valueField) && distinctFields.contains(keyField))
        {
            tempKey = (String)tagRow.get(keyField);
            tempKey = tempKey.trim();
            if (tempKey.equals(keyValue))
            { 
                resultSetting = new TagSetting();
                resultSetting.setTypeId(settingType);
                resultSetting.setValue((String)tagRow.get(valueField));
            }// if
                     
        // If only setting value field is set, create setting without key checking:
        } else if (valueField != null && distinctFields.contains(valueField)) { 
            
            resultSetting = new TagSetting();
            resultSetting.setTypeId(settingType);
            resultSetting.setValue((String)tagRow.get(valueField));
        }// else
                        
        // If setting was created and parsing settings contain "Possible" flag field, add property to setting:
        if (resultSetting != null && possibleFlagField != null && distinctFields.contains(possibleFlagField))
        {
            String possibleFlagValue = (String)tagRow.get(possibleFlagField);
            
            // If flag value string not empty:
            if (!possibleFlagValue.trim().equals(""))
            {    
                resultSettingProperty = new TagSettingProperty();
                resultSettingProperty.setTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                resultSettingProperty.setValue((String)tagRow.get(possibleFlagField));
                resultSetting.getProperties().add(resultSettingProperty);
            }// if
        }// if
        
        return resultSetting;
    }// _readTagSetting
}// ExcelBookDataSourceCreator