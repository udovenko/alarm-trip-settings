package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Yokogawa DCS backup parsing" dialog settings.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class CreateDcsVariableTableFromYokogawaBackupDialogSettings extends PlantAndTagFormatSettings implements CreateDcsVariableTableFromYokogawaBackupDialogSettingsObservable
{
    private static CreateDcsVariableTableFromYokogawaBackupDialogSettings instance;
    private static final String SECTION_NAME = "CreateDcsVariableTableFromYokogawaBackupDialog";
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private CreateDcsVariableTableFromYokogawaBackupDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static CreateDcsVariableTableFromYokogawaBackupDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new CreateDcsVariableTableFromYokogawaBackupDialogSettings();
        }// if
        
        return instance;
    }// getInstance
}// CreateDcsVariableTableFromYokogawaBackupDialogSettings
