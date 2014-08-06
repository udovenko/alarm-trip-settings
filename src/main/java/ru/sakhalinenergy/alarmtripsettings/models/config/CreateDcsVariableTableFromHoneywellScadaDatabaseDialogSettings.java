package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Honeywell DCS export parsing" dialog settings.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings extends PlantAndTagFormatSettings 
    implements CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettingsObservable
{
    private static CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings instance;
    private static final String SECTION_NAME = "CreateDcsVariableTableFromHoneywellScadaDatabaseDialog";
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings();
        }// if
        
        return instance;
    }// getInstance
}// CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings
