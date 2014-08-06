package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements MS Excel book parsing settings model.
 * 
 * @author Denis Udovenko
 * @version 1.0.8
 */
public class ExcelBookParsingDialogSettings extends Config implements ExcelBookParsingDialogSettingsObservable
{
    private static ExcelBookParsingDialogSettings instance;
    private static String SECTION_NAME = "ExcelBookParsingSettings";
 
    // Settings fields:
    @Entry private String plantCode;
    @Entry private String dataSourceTypeName;
    @Entry private String tagFormat;
    
    @Entry private String sheetName;
    @Entry private String tagNameField;
    
    @Entry private String alarmLowLowKeyField;
    @Entry private String alarmLowLowKeyValue;
    @Entry private String alarmLowLowValueField;
    @Entry private String alarmLowLowPossibleFlagField;
    
    @Entry private String alarmLowKeyField;
    @Entry private String alarmLowKeyValue;
    @Entry private String alarmLowValueField;
    @Entry private String alarmLowPossibleFlagField;
    
    @Entry private String alarmHighKeyField;
    @Entry private String alarmHighKeyValue;
    @Entry private String alarmHighValueField;
    @Entry private String alarmHighPossibleFlagField;
    
    @Entry private String alarmHighHighKeyField;
    @Entry private String alarmHighHighKeyValue;
    @Entry private String alarmHighHighValueField;
    @Entry private String alarmHighHighPossibleFlagField;
    
    @Entry private String rangeMinKeyField;
    @Entry private String rangeMinKeyValue;
    @Entry private String rangeMinValueField;
    
    @Entry private String rangeMaxKeyField;
    @Entry private String rangeMaxKeyValue;
    @Entry private String rangeMaxValueField;
        
    @Entry private String unitsKeyField;
    @Entry private String unitsKeyValue;
    @Entry private String unitsValueField;
        
    @Entry private String sourceSystemKeyField;
    @Entry private String sourceSystemKeyValue;
    @Entry private String sourceSystemValueField;
        
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private ExcelBookParsingDialogSettings(){}
    
    
    /**
     * Returns reference on a single MS Excel book parsing settings class 
     * instance. If instance not created yet - creates it.
     *
     * @return Reference on MS Excel book parsing settings singleton object
     */
    public static ExcelBookParsingDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new ExcelBookParsingDialogSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Sets up selected plant code setting value.
     * 
     * @param plantCode Selected plant code
     */
    public void setPlantCode(String plantCode)
    {
        this.plantCode = plantCode;
        hasChanged = true;
    }// setPlantCode
    
    
    /**
     * Sets up selected tag format setting value.
     * 
     * @param tagFormat Selected tag format
     */
    public void setTagFormat(String tagFormat)
    {
        this.tagFormat = tagFormat;
        hasChanged = true;
    }// setTagFormat
        
    
    /**
     * Sets up data source type name.
     * 
     * @param dataSourceTypeName Data source type name
     */
    public void setDataSourceTypeName(String dataSourceTypeName) 
    {
        this.dataSourceTypeName = dataSourceTypeName;
        hasChanged = true;
    }// setDataSourceTypeName
    
    
    /**
     * Sets up a name of selected list in MS Excel book.
     * 
     * @param sheetName Name of selected list in MS Excel book
     */
    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
        hasChanged = true;
    }// setSheetName
    
    
    /**
     * Sets up a name of filed which contains tag names.
     * 
     * @param tagNameField Name of filed which contains tag names
     */
    public void setTagNameField(String tagNameField)
    {
        this.tagNameField = tagNameField;
        hasChanged = true;
    }// setTagNameField
    
    
    /**
     * Sets up a name of field which contains LL alarm key.
     * 
     * @param alarmLowLowKeyField Name of field which contains LL alarm key
     */
    public void setAlarmLowLowKeyField(String alarmLowLowKeyField)
    {
        this.alarmLowLowKeyField = alarmLowLowKeyField;
        hasChanged = true;
    }// setAlarmLowLowKeyField
    
    
    /**
     * Sets up LL alarm key value.
     * 
     * @param alarmLowLowKeyValue LL alarm key value
     */
    public void setAlarmLowLowKeyValue(String alarmLowLowKeyValue)
    {
        this.alarmLowLowKeyValue = alarmLowLowKeyValue;
        hasChanged = true;
    }// setAlarmLowLowKeyValue
    
    
    /**
     * Sets up a name of field which contains LL alarm value.
     * 
     * @param alarmLowLowValueField Name of field which contains LL alarm value
     */
    public void setAlarmLowLowValueField(String alarmLowLowValueField)
    {
        this.alarmLowLowValueField = alarmLowLowValueField;
        hasChanged = true;
    }// setAlarmLowLowValueField
    
    
    /**
     * Sets up a name of field which contains "Possible" flag value for LL alarm.
     * 
     * @param alarmLowLowPossibleFlagField Name of field which contains "Possible" flag value for LL alarm
     */
    public void setAlarmLowLowPossibleFlagField(String alarmLowLowPossibleFlagField)
    {
        this.alarmLowLowPossibleFlagField = alarmLowLowPossibleFlagField;
        hasChanged = true;
    }// setAlarmLowLowPossibleFlagField
    
    
    /**
     * Sets up a name of field which contains L alarm key.
     * 
     * @param alarmLowKeyField Name of field which contains L alarm key
     */
    public void setAlarmLowKeyField(String alarmLowKeyField)
    {
        this.alarmLowKeyField = alarmLowKeyField;
        hasChanged = true;
    }// setAlarmLowKeyField
    
    
    /**
     * Sets up L alarm key value.
     * 
     * @param alarmLowKeyValue L alarm key value
     */
    public void setAlarmLowKeyValue(String alarmLowKeyValue)
    {
        this.alarmLowKeyValue = alarmLowKeyValue;
        hasChanged = true;
    }// setAlarmLowKeyValue
    
    
    /**
     * Sets up a name of field which contains L alarm value.
     * 
     * @param alarmLowValueField Name of field which contains L alarm value
     */
    public void setAlarmLowValueField(String alarmLowValueField)
    {
        this.alarmLowValueField = alarmLowValueField;
        hasChanged = true;
    }// setAlarmLowValueField
    
    
    /**
     * Sets up a name of field which contains "Possible" flag value for L alarm.
     * 
     * @param alarmLowPossibleFlagField Name of field which contains "Possible" flag value for L alarm
     */
    public void setAlarmLowPossibleFlagField(String alarmLowPossibleFlagField)
    {
        this.alarmLowPossibleFlagField = alarmLowPossibleFlagField;
        hasChanged = true;
    }// setAlarmLowPossibleFlagField
    
    
    /**
     * Sets up a name of field which contains H alarm key.
     * 
     * @param alarmHighKeyField Name of field which contains H alarm key
     */
    public void setAlarmHighKeyField(String alarmHighKeyField)
    {
        this.alarmHighKeyField = alarmHighKeyField;
        hasChanged = true;
    }// setAlarmHighKeyField
    
    
    /**
     * Sets up H alarm key value.
     * 
     * @param alarmHighKeyValue H alarm key value
     */
    public void setAlarmHighKeyValue(String alarmHighKeyValue)
    {
        this.alarmHighKeyValue = alarmHighKeyValue;
        hasChanged = true;
    }// setAlarmHighKeyValue
    
    
    /**
     * Sets up a name of field which contains H alarm value.
     * 
     * @param alarmHighValueField Name of field which contains H alarm value
     */
    public void setAlarmHighValueField(String alarmHighValueField)
    {
        this.alarmHighValueField = alarmHighValueField;
        hasChanged = true;
    }// setAlarmHighValueField
    
    
    /**
     * Sets up a name of field which contains "Possible" flag value for H alarm.
     * 
     * @param alarmHighPossibleFlagField Name of field which contains "Possible" flag value for H alarm
     */
    public void setAlarmHighPossibleFlagField(String alarmHighPossibleFlagField)
    {
        this.alarmHighPossibleFlagField = alarmHighPossibleFlagField;
        hasChanged = true;
    }// setAlarmHighPossibleFlagField
    
    
    /**
     * Sets up a name of field which contains HH alarm key.
     * 
     * @param alarmHighHighKeyField Name of field which contains HH alarm key
     */
    public void setAlarmHighHighKeyField(String alarmHighHighKeyField)
    {
        this.alarmHighHighKeyField = alarmHighHighKeyField;
        hasChanged = true;
    }// setAlarmHighHighKeyField
    
    
    /**
     * Sets up HH alarm key value.
     * 
     * @param alarmHighHighKeyValue HH alarm key value
     */
    public void setAlarmHighHighKeyValue(String alarmHighHighKeyValue)
    {
        this.alarmHighHighKeyValue = alarmHighHighKeyValue;
        hasChanged = true;
    }// setAlarmHighHighKeyValue
    
    
    /**
     * Sets up a name of field which contains HH alarm value.
     * 
     * @param alarmHighHighValueField Name of field which contains HH alarm value
     */
    public void setAlarmHighHighValueField(String alarmHighHighValueField)
    {
        this.alarmHighHighValueField = alarmHighHighValueField;
        hasChanged = true;
    }// setAlarmHighHighValueField
    
    
    /**
     * Sets up a name of field which contains "Possible" flag value for HH alarm.
     * 
     * @param alarmHighHighPossibleFlagField Name of field which contains "Possible" flag value for HH alarm
     */
    public void setAlarmHighHighPossibleFlagField(String alarmHighHighPossibleFlagField)
    {
        this.alarmHighHighPossibleFlagField = alarmHighHighPossibleFlagField;
        hasChanged = true;
    }// setAlarmHighHighPossibleFlagField
    
    
    /**
     * Sets up a name of field which contains range min setting key.
     * 
     * @param rangeMinKeyField Name of field which contains range min setting key
     */
    public void setRangeMinKeyField(String rangeMinKeyField)
    {
        this.rangeMinKeyField = rangeMinKeyField;
        hasChanged = true;
    }// setRangeMinKeyField
    
    
    /**
     * Sets up range min setting key value.
     * 
     * @param rangeMinKeyValue Range min setting key value
     */
    public void setRangeMinKeyValue(String rangeMinKeyValue)
    {
        this.rangeMinKeyValue = rangeMinKeyValue;
        hasChanged = true;
    }// setRangeMinKeyValue
    
    
    /**
     * Sets up a name of field which contains range min setting value.
     * 
     * @param rangeMinValueField Name of field which contains range min setting value
     */
    public void setRangeMinValueField(String rangeMinValueField)
    {
        this.rangeMinValueField = rangeMinValueField;
        hasChanged = true;
    }// setRangeMinValueField
        
    
    /**
     * Sets up a name of field which contains range max setting key.
     * 
     * @param rangeMinKeyField Name of field which contains range max setting key
     */
    public void setRangeMaxKeyField(String rangeMinKeyField)
    {
        this.rangeMaxKeyField = rangeMinKeyField;
        hasChanged = true;
    }// setRangeMaxKeyField
    
    
    /**
     * Sets up range max setting key value.
     * 
     * @param rangeMaxKeyValue Range max setting key value
     */
    public void setRangeMaxKeyValue(String rangeMaxKeyValue)
    {
        this.rangeMaxKeyValue = rangeMaxKeyValue;
        hasChanged = true;
    }// setRangeMaxKeyValue
    
    
    /**
     * Sets up a name of field which contains range max setting value.
     * 
     * @param rangeMaxValueField Name of field which contains range max setting value
     */
    public void setRangeMaxValueField(String rangeMaxValueField)
    {
        this.rangeMaxValueField = rangeMaxValueField;
        hasChanged = true;
    }// setRangeMaxValueField
    
    
    /**
     * Sets up a name of field which contains units setting key.
     * 
     * @param unitsKeyField Name of field which contains units setting key
     */
    public void setUnitsKeyField(String unitsKeyField)
    {
        this.unitsKeyField = unitsKeyField;
        hasChanged = true;
    }// setUnitsKeyField
    
    
    /**
     * Sets up units setting key value.
     * 
     * @param unitsKeyValue Units setting key value
     */
    public void setUnitsKeyValue(String unitsKeyValue)
    {
        this.unitsKeyValue = unitsKeyValue;
        hasChanged = true;
    }// setUnitsKeyValue
    
    
    /**
     * Sets up a name of field which contains units setting value.
     * 
     * @param unitsValueField Name of field which contains units setting value
     */
    public void setUnitsValueField(String unitsValueField)
    {
        this.unitsValueField = unitsValueField;
        hasChanged = true;
    }// setUnitsValueField
    
    
    /**
     * Sets up a name of field which contains source system setting key.
     * 
     * @param sourceSystemKeyField Name of field which contains source system setting key
     */
    public void setSourceSystemKeyField(String sourceSystemKeyField)
    {
        this.sourceSystemKeyField = sourceSystemKeyField;
        hasChanged = true;
    }//  setSourceSystemKeyField
    
    
    /**
     * Sets up source system setting key value.
     * 
     * @param sourceSystemKeyValue Source system setting key value
     */
    public void setSourceSystemKeyValue(String sourceSystemKeyValue)
    {
        this.sourceSystemKeyValue = sourceSystemKeyValue;
        hasChanged = true;
    }// setSourceSystemKeyValue
    
    
    /**
     * Sets up a name of field which contains source system setting value.
     * 
     * @param sourceSystemValueField Name of field which contains source system setting value
     */
    public void setSourceSystemValueField(String sourceSystemValueField)
    {
        this.sourceSystemValueField = sourceSystemValueField;
        hasChanged = true;
    }// setSourceSystemValueField
        
    
    /**
     * Returns selected plant code setting value.
     * 
     * @return Selected plant code
     */
    @Override
    public String getPlantCode()
    {
        return plantCode;
    }// getPlantCode
    
        
    /**
     * Returns selected tag format setting value. 
     * 
     * @return Selected tag format
     */
    @Override
    public String getTagFormat() 
    {
        return tagFormat;
    }// getTagFormat
    
    
    /**
     * Returns data source type name setting value.
     * 
     * @return Data source type name
     */
    @Override
    public String getDataSourceTypeName() 
    {
        return dataSourceTypeName;
    }// getDataSourceTypeName
    
    
    /**
     * Returns a name of selected list in MS Excel book.
     * 
     * @return Name of selected list in MS Excel book
     */
    @Override
    public String getSheetName()
    {
        return sheetName;
    }// getSheetName
    
    
    /**
     * Returns a name of filed which contains tag names.
     * 
     * @return Name of filed which contains tag names
     */
    @Override
    public String getTagNameField()
    {
        return tagNameField;
    }// getTagNameField
    
    
    /**
     * Returns a name of field which contains LL alarm key.
     * 
     * @return Name of field which contains LL alarm key
     */
    @Override
    public String getAlarmLowLowKeyField()
    {
        return alarmLowLowKeyField;
    }// getAlarmLowLowKeyField
    
    
    /**
     * Returns LL alarm key value.
     * 
     * @return LL alarm key value
     */
    @Override
    public String getAlarmLowLowKeyValue()
    {
        return alarmLowLowKeyValue;
    }// getAlarmLowLowKeyValue
    
    
    /**
     * Returns a name of field which contains LL alarm value.
     * 
     * @return Name of field which contains LL alarm value
     */
    @Override
    public String getAlarmLowLowValueField()
    {
        return alarmLowLowValueField;
    }// getAlarmLowLowValueField
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for LL alarm.
     * 
     * @return Name of field which contains "Possible" flag value for LL alarm
     */
    @Override
    public String getAlarmLowLowPossibleFlagField()
    {
        return alarmLowLowPossibleFlagField;
    }// getAlarmLowLowPossibleFlagField
    
    
    /**
     * Returns a name of field which contains L alarm key.
     * 
     * @return Name of field which contains L alarm key
     */
    @Override
    public String getAlarmLowKeyField()
    {
        return alarmLowKeyField;
    }// getAlarmLowKeyField
    
    
    /**
     * Returns L alarm key value.
     * 
     * @return L alarm key value
     */
    @Override
    public String getAlarmLowKeyValue()
    {
        return alarmLowKeyValue;
    }// getAlarmLowKeyValue
    
    
    /**
     * Returns a name of field which contains L alarm value.
     * 
     * @return Name of field which contains L alarm value
     */
    @Override
    public String getAlarmLowValueField()
    {
        return alarmLowValueField;
    }// getAlarmLowValueField
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for L alarm.
     * 
     * @return Name of field which contains "Possible" flag value for L alarm
     */
    @Override
    public String getAlarmLowPossibleFlagField()
    {
        return alarmLowPossibleFlagField;
    }// getAlarmLowPossibleFlagField
    
    
    /**
     * Returns a name of field which contains H alarm key.
     * 
     * @return Name of field which contains H alarm key
     */
    @Override
    public String getAlarmHighKeyField()
    {
        return alarmHighKeyField;
    }// getAlarmHighKeyField
    
    
    /**
     * Returns H alarm key value.
     * 
     * @return H alarm key value
     */
    @Override
    public String getAlarmHighKeyValue()
    {
        return alarmHighKeyValue;
    }// getAlarmHighKeyValue
    
    
    /**
     * Returns a name of field which contains H alarm value
     * 
     * @return Name of field which contains H alarm value
     */
    @Override
    public String getAlarmHighValueField()
    {
        return alarmHighValueField;
    }// getAlarmHighValueField
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for H alarm.
     * 
     * @return Name of field which contains "Possible" flag value for H alarm
     */
    @Override
    public String getAlarmHighPossibleFlagField()
    {
        return alarmHighPossibleFlagField;
    }// getAlarmHighPossibleFlagField
    
    
    /**
     * Returns a name of field which contains HH alarm key.
     * 
     * @return Name of field which contains HH alarm key
     */
    @Override
    public String getAlarmHighHighKeyField()
    {
        return alarmHighHighKeyField;
    }// getAlarmHighHighKeyField
    
    
    /**
     * Returns HH alarm key value.
     * 
     * @return HH alarm key value
     */
    @Override
    public String getAlarmHighHighKeyValue()
    {
        return alarmHighHighKeyValue;
    }// getAlarmHighHighKeyValue
    
    
    /**
     * Returns a name of field which contains HH alarm value.
     * 
     * @return Name of field which contains HH alarm value
     */
    @Override
    public String getAlarmHighHighValueField()
    {
        return alarmHighHighValueField;
    }// getAlarmHighHighValueField
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for HH alarm.
     * 
     * @return Name of field which contains "Possible" flag value for HH alarm
     */
    @Override
    public String getAlarmHighHighPossibleFlagField()
    {
        return alarmHighHighPossibleFlagField;
    }// getAlarmHighHighPossibleFlagField
    
    
    /**
     * Returns a name of field which contains range min setting key.
     * 
     * @return Name of field which contains range min setting key
     */
    @Override
    public String getRangeMinKeyField()
    {
        return rangeMinKeyField;
    }// getRangeMinKeyField
    
    
    /**
     * Returns range min key setting value.
     * 
     * @return Range min key
     */
    @Override
    public String getRangeMinKeyValue()
    {
        return rangeMinKeyValue;
    }// getRangeMinKeyValue
    
    
    /**
     * Returns a name of field which contains range min setting value.
     * 
     * @return Name of field which contains range min setting value
     */
    @Override
    public String getRangeMinValueField()
    {
        return rangeMinValueField;
    }// getRangeMinValueField
        
    
    /**
     * Returns a name of field which contains range max setting key.
     * 
     * @return Name of field which contains range max setting key
     */
    @Override
    public String getRangeMaxKeyField()
    {
        return rangeMaxKeyField;
    }// getRangeMaxKeyField
    
    
    /**
     * Returns range max key setting value.
     * 
     * @return Range max key
     */
    @Override
    public String getRangeMaxKeyValue()
    {
        return rangeMaxKeyValue;
    }// setRangeMaxKeyValue
    
    
    /**
     * Returns a name of field which contains range max setting value.
     * 
     * @return Name of field which contains range max setting value
     */
    @Override
    public String getRangeMaxValueField()
    {
        return rangeMaxValueField;
    }// getRangeMaxValueField
    
    
    /**
     * Returns a name of field which contains units setting key.
     * 
     * @return Name of field which contains units setting key
     */
    @Override
    public String getUnitsKeyField()
    {
        return unitsKeyField;
    }// getUnitsKeyField
    
    
    /**
     * Returns units key setting value.
     * 
     * @return Units key
     */
    @Override
    public String getUnitsKeyValue()
    {
        return unitsKeyValue;
    }// getUnitsKeyValue
    
    
    /**
     * Returns a name of field which contains units setting value.
     * 
     * @return Name of field which contains units setting value
     */
    @Override
    public String getUnitsValueField()
    {
        return unitsValueField;
    }// getUnitsValueField
    
    
    /**
     * Returns a name of field which contains source system setting key.
     * 
     * @return Name of field which contains source system setting key
     */
    @Override
    public String getSourceSystemKeyField()
    {
        return sourceSystemKeyField;
    }// getSourceSystemKeyField
    
    
    /**
     * Returns source system key setting value.
     * 
     * @return Source system key
     */
    @Override
    public String getSourceSystemKeyValue()
    {
        return sourceSystemKeyValue;
    }// getSourceSystemKeyValue
    
    
    /**
     * Returns a name of field which contains source system setting value.
     * 
     * @return Name of field which contains source system setting value
     */
    @Override
    public String getSourceSystemValueField()
    {
        return sourceSystemValueField;
    }// getSourceSystemValueField
}// ExcelBookParsingSettings
