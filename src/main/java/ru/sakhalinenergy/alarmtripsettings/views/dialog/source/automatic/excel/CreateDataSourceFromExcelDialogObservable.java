package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.excel;

import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa.AutomaticSourceCreationDialogObservable;


/**
 * Interface for using "Create source from MS Excel" dialog by controllers.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface CreateDataSourceFromExcelDialogObservable extends AutomaticSourceCreationDialogObservable
{
    
    /**
     * Returns selected data source type.
     * 
     * @return Selected data source type
     */
    public String getDataSourceType();
    
    
    /**
     * Returns selected sheet name of MS Excel book. 
     * 
     * @return Selected sheet name
     */
    public String getSheetName();
        
    
    /**
     * Returns field selected as tag name container.
     * 
     * @return Field selected as tag name container
     */
    public String getTagFieldName();
        
    
    /**
     * Returns field selected as LL alarm setting key container.
     * 
     * @return Field selected as LL alarm key container
     */
    public String getAlarmLowLowKeyField();
    
    
    /**
     * Returns field selected as LL alarm setting key value container.
     * 
     * @return Field selected as LL alarm setting key value container
     */
    public String getAlarmLowLowKeyValue();
    
    
    /**
     * Returns field selected as LL alarm setting value container.
     * 
     * @return Field selected as LL alarm setting value container
     */
    public String getAlarmLowLowValueField();
    
    
    /**
     * Returns field selected as LL alarm setting "Possible" flag container.
     * 
     * @return Field selected as LL alarm setting "Possible" flag container
     */
    public String getAlarmLowLowPossibleFlagField();
    
    
    /**
     * Returns field selected as L alarm setting key container.
     * 
     * @return Field selected as L alarm key container
     */
    public String getAlarmLowKeyField();
    
    
    /**
     * Returns field selected as L alarm setting key value container.
     * 
     * @return Field selected as L alarm setting key value container
     */
    public String getAlarmLowKeyValue();
    
    
    /**
     * Returns field selected as L alarm setting value container.
     * 
     * @return Field selected as L alarm setting value container
     */
    public String getAlarmLowValueField();
    
    
    /**
     * Returns field selected as L alarm setting "Possible" flag container.
     * 
     * @return Field selected as L alarm setting "Possible" flag container
     */
    public String getAlarmLowPossibleFlagField();
    
    
    /**
     * Returns field selected as H alarm setting key container.
     * 
     * @return Field selected as H alarm key container
     */
    public String getAlarmHighKeyField();
    
    
    /**
     * Returns field selected as H alarm setting key value container.
     * 
     * @return Field selected as H alarm setting key value container
     */
    public String getAlarmHighKeyValue();
    
    
    /**
     * Returns field selected as H alarm setting value container.
     * 
     * @return Field selected as H alarm setting value container
     */
    public String getAlarmHighValueField();
    
    
    /**
     * Returns field selected as H alarm setting "Possible" flag container.
     * 
     * @return Field selected as H alarm setting "Possible" flag container
     */
    public String getAlarmHighPossibleFlagField();
    
    
    /**
     * Returns field selected as HH alarm setting key container.
     * 
     * @return Field selected as HH alarm key container
     */
    public String getAlarmHighHighKeyField();
    
    
    /**
     * Returns field selected as HH alarm setting key value container.
     * 
     * @return Field selected as HH alarm setting key value container
     */
    public String getAlarmHighHighKeyValue();
    
    
    /**
     * Returns field selected as HH alarm setting value container.
     * 
     * @return Field selected as HH alarm setting value container
     */
    public String getAlarmHighHighValueField();
            
            
    /**
     * Returns field selected as HH alarm setting "Possible" flag container.
     * 
     * @return Field selected as HH alarm setting "Possible" flag container
     */
    public String getAlarmHighHighPossibleFlagField();
        
    
    /**
     * Returns field selected as range min setting key container.
     * 
     * @return Field selected as range min setting key container
     */
    public String getRangeMinKeyField();
        
    
    /**
     * Returns field selected as range min setting key value container.
     * 
     * @return Field selected as range min setting key value container
     */
    public String getRangeMinKeyValue();
    
    
    /**
     * Returns field selected as range min setting value container.
     * 
     * @return Field selected as range min setting value container
     */
    public String getRangeMinValueField();
            
    
    /**
     * Returns field selected as range max setting key container.
     * 
     * @return Field selected as range max setting key container
     */
    public String getRangeMaxKeyField();
    
            
    /**
     * Returns field selected as range max setting key value container.
     * 
     * @return Field selected as range max setting key value container
     */
    public String getRangeMaxKeyValue();
    
    
    /**
     * Returns field selected as range max setting value container.
     * 
     * @return Field selected as range max setting value container
     */
    public String getRangeMaxValueField();
    
    
    /**
     * Returns field selected as engineering units setting key container.
     * 
     * @return Field selected as engineering units setting key container
     */
    public String getUnitsKeyField();
    
    
    /**
     * Returns field selected as engineering units setting key value container.
     * 
     * @return Field selected as engineering units setting key value container
     */
    public String getUnitsKeyValue();
    
    
    /**
     * Returns field selected as engineering units setting value container.
     * 
     * @return Field selected as engineering units setting value container
     */
    public String getUnitsValueField();
    
    
    /**
     * Returns field selected as source system setting key container.
     * 
     * @return Field selected as source system units setting key container
     */
    public String getSourceSystemKeyField();
        
    
    /**
     * Returns field selected as source system setting key value container.
     * 
     * @return Field selected as source system setting key value container
     */
    public String getSourceSystemKeyValue();
    
    
    /**
     * Returns field selected as source system setting value container.
     * 
     * @return Field selected as source system setting value container
     */
    public String getSourceSystemValueField();
    
}//CreateDataSourceFromExcelDialogObservable
