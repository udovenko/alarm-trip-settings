package ru.sakhalinenergy.alarmtripsettings.models.config;

import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;


/**
 * Interface of MS Excel book parsing settings model for using by views. Allows
 * only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface ExcelBookParsingDialogSettingsObservable extends EventsObservable
{
    
    /**
     * Returns selected plant code setting value.
     * 
     * @return Selected plant code
     */
    public String getPlantCode();
    
    
    /**
     * Returns selected tag format setting value. 
     * 
     * @return Selected tag format
     */
    public String getTagFormat();
       
    
    /**
     * Returns data source type name setting value.
     * 
     * @return Data source type name
     */
    public String getDataSourceTypeName();
    
    
    /**
     * Returns a name of selected list in MS Excel book.
     * 
     * @return Name of selected list in MS Excel book
     */
    public String getSheetName();
        
    
    /**
     * Returns a name of filed which contains tag names.
     * 
     * @return Name of filed which contains tag names
     */
    public String getTagNameField();
    
    
    /**
     * Returns a name of field which contains LL alarm key.
     * 
     * @return Name of field which contains LL alarm key
     */
    public String getAlarmLowLowKeyField();
    
    
    /**
     * Returns LL alarm key value.
     * 
     * @return LL alarm key value
     */
    public String getAlarmLowLowKeyValue();
    
    
    /**
     * Returns a name of field which contains LL alarm value.
     * 
     * @return Name of field which contains LL alarm value
     */
    public String getAlarmLowLowValueField();
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for LL alarm.
     * 
     * @return Name of field which contains "Possible" flag value for LL alarm
     */
    public String getAlarmLowLowPossibleFlagField();
    
    
    /**
     * Returns a name of field which contains L alarm key.
     * 
     * @return Name of field which contains L alarm key
     */
    public String getAlarmLowKeyField();
    
    
    /**
     * Returns L alarm key value.
     * 
     * @return L alarm key value
     */
    public String getAlarmLowKeyValue();
    
    
    /**
     * Returns a name of field which contains L alarm value.
     * 
     * @return Name of field which contains L alarm value
     */
    public String getAlarmLowValueField();
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for L alarm.
     * 
     * @return Name of field which contains "Possible" flag value for L alarm
     */
    public String getAlarmLowPossibleFlagField();
    
    
    /**
     * Returns a name of field which contains H alarm key.
     * 
     * @return Name of field which contains H alarm key
     */
    public String getAlarmHighKeyField();
    
    
    /**
     * Returns H alarm key value.
     * 
     * @return H alarm key value
     */
    public String getAlarmHighKeyValue();
    
    
    /**
     * Returns a name of field which contains H alarm value
     * 
     * @return Name of field which contains H alarm value
     */
    public String getAlarmHighValueField();
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for H alarm.
     * 
     * @return Name of field which contains "Possible" flag value for H alarm
     */
    public String getAlarmHighPossibleFlagField();
    
    
    /**
     * Returns a name of field which contains HH alarm key.
     * 
     * @return Name of field which contains HH alarm key
     */
    public String getAlarmHighHighKeyField();
    
    
    /**
     * Returns HH alarm key value.
     * 
     * @return HH alarm key value
     */
    public String getAlarmHighHighKeyValue();
    
    
    /**
     * Returns a name of field which contains HH alarm value.
     * 
     * @return Name of field which contains HH alarm value
     */
    public String getAlarmHighHighValueField();
    
    
    /**
     * Returns a name of field which contains "Possible" flag value for HH alarm.
     * 
     * @return Name of field which contains "Possible" flag value for HH alarm
     */
    public String getAlarmHighHighPossibleFlagField();
    
    
    /**
     * Returns a name of field which contains range min setting key.
     * 
     * @return Name of field which contains range min setting key
     */
    public String getRangeMinKeyField();
    
    
    /**
     * Returns range min key setting value.
     * 
     * @return Range min key
     */
    public String getRangeMinKeyValue();
    
    
    /**
     * Returns a name of field which contains range min setting value.
     * 
     * @return Name of field which contains range min setting value
     */
    public String getRangeMinValueField();
        
    
    /**
     * Returns a name of field which contains range max setting key.
     * 
     * @return Name of field which contains range max setting key
     */
    public String getRangeMaxKeyField();
        
    
    /**
     * Returns range max key setting value.
     * 
     * @return Range max key
     */
    public String getRangeMaxKeyValue();
    
    
    /**
     * Returns a name of field which contains range max setting value.
     * 
     * @return Name of field which contains range max setting value
     */
    public String getRangeMaxValueField();
    
    
    /**
     * Returns a name of field which contains units setting key.
     * 
     * @return Name of field which contains units setting key
     */
    public String getUnitsKeyField();
    
    
    /**
     * Returns units key setting value.
     * 
     * @return Units key
     */
    public String getUnitsKeyValue();
    
    
    /**
     * Returns a name of field which contains units setting value.
     * 
     * @return Name of field which contains units setting value
     */
    public String getUnitsValueField();
    
    
    /**
     * Returns a name of field which contains source system setting key.
     * 
     * @return Name of field which contains source system setting key
     */
    public String getSourceSystemKeyField();
    
    
    /**
     * Returns source system key setting value.
     * 
     * @return Source system key
     */
    public String getSourceSystemKeyValue();
    
    
    /**
     * Returns a name of field which contains source system setting value.
     * 
     * @return Name of field which contains source system setting value
     */
    public String getSourceSystemValueField();
    
}// ExcelBookParsingDialogSettingsObservable
