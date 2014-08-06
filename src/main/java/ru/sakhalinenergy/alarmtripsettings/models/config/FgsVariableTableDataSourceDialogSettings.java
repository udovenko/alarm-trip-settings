package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Create/edit FGS Variable Table data source" dialog settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class FgsVariableTableDataSourceDialogSettings extends VariableTableDataSourceDialogSettings implements VariableTableDataSourceDialogSettingsObservable
{
    private static FgsVariableTableDataSourceDialogSettings instance;
    private static final String SECTION_NAME = "FgsVariableTableDataSourceDialog";
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private FgsVariableTableDataSourceDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static FgsVariableTableDataSourceDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new FgsVariableTableDataSourceDialogSettings();
        }// if
        
        return instance;
    }// getInstance
}// FgsVariableTableDataSourceDialogSettings
