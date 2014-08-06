package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Abstract parent for settings models of system variable tables dialogs.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public abstract class VariableTableDataSourceDialogSettings 
    extends DataSourceDialogSettings implements VariableTableDataSourceDialogSettingsObservable
{
    // Settng fields:
    @Entry private String backupDate;
    
    
    /**
     * Returns system's backup date.
     * 
     * @return System's backup date
     */
    @Override
    public String getBackupDate() 
    {
        return backupDate;
    }// getBackupDate

    
    /**
     * Sets up system's backup date.
     * 
     * @param backupDate System's backup date
     */
    public void setBackupDate(String backupDate) 
    {
        this.backupDate = backupDate;
        hasChanged = true;
    }// setBackupDate
}// VariableTableDataSourceDialogSettings
