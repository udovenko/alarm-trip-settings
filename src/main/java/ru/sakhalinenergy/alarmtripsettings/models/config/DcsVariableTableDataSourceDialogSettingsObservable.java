package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of "Create/edit DCS Variable Table data source" dialog settings 
 * model for using by views. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface DcsVariableTableDataSourceDialogSettingsObservable extends VariableTableDataSourceDialogSettingsObservable
{
    
    /**
     * Returns configuration value for flag determines necessity to create new
     * loop in storage if no suitable loop was found for a tag during parsing.
     * 
     * @return Crate new loops flag
     */
    public String getCreateLoopsIfNotExistFlag();
    
}// DcsVariableTableDataSourceDialogSettingsObservable
