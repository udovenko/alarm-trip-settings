package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements Honeywell DCS export parsing dialog settings.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class CreateDcsVariableTableFromHoneywellDcsExportDialogSettings extends PlantAndTagFormatSettings implements CreateDcsVariableTableFromHoneywellDcsExportDialogSettingsObservable
{
    private static CreateDcsVariableTableFromHoneywellDcsExportDialogSettings instance;
    private static final String SECTION_NAME = "CreateDcsVariableTableFromHoneywellDcsExportDialog";
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private CreateDcsVariableTableFromHoneywellDcsExportDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static CreateDcsVariableTableFromHoneywellDcsExportDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new CreateDcsVariableTableFromHoneywellDcsExportDialogSettings();
        }// if
        
        return instance;
    }// getInstance
}// CreateDcsVariableTableFromHoneywellDcsExportDialogSettings
