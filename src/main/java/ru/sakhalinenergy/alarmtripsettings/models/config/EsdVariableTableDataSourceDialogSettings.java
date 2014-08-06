package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Create/edit ESD Variable Table data source" dialog settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class EsdVariableTableDataSourceDialogSettings extends VariableTableDataSourceDialogSettings implements VariableTableDataSourceDialogSettingsObservable
{
    private static EsdVariableTableDataSourceDialogSettings instance;
    private static final String SECTION_NAME = "EsdVariableTableDataSourceDialog";

    
    /**
     * Making constructor private to forbade new class instances.
     */
    private EsdVariableTableDataSourceDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static EsdVariableTableDataSourceDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new EsdVariableTableDataSourceDialogSettings();
        }// if
        
        return instance;
    }// getInstance
}// EsdVariableTableDataSourceDialogSettings
