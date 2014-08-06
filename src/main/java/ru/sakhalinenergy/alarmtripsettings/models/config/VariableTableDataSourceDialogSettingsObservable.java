package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface for settings models of system variable tables dialogs for using
 * by view. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface VariableTableDataSourceDialogSettingsObservable extends DataSourceDialogSettingsObservable
{
    
    /**
     * Returns system's backup date.
     * 
     * @return System's backup date
     */
    public String getBackupDate();
    
}// EsdVariableTableDataSourceDialogSettingsObservable
