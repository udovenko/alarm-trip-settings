package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Create/edit DCS Variable Table data source" dialog settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class DcsVariableTableDataSourceDialogSettings extends VariableTableDataSourceDialogSettings implements DcsVariableTableDataSourceDialogSettingsObservable
{
    private static DcsVariableTableDataSourceDialogSettings instance;
    private static final String SECTION_NAME = "DcsVariableTableDataSourceDialog";
    
    @Entry private String createLoopsIfNotExistFlag;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private DcsVariableTableDataSourceDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static DcsVariableTableDataSourceDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new DcsVariableTableDataSourceDialogSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns configuration value for flag determines necessity to create new
     * loop in storage if no suitable loop was found for a tag during parsing.
     * 
     * @return Crate new loops flag
     */
    @Override
    public String getCreateLoopsIfNotExistFlag() 
    {
        return createLoopsIfNotExistFlag;
    }// getCreateLoopsIfNotExistFlag

    
    /**
     * Sets up configuration value for flag determines necessity to create new
     * loop in storage if no suitable loop was found for a tag during parsing.
     * 
     * @param createLoopsIfNotExistFlag Crate new loops flag
     */
    public void setCreateLoopsIfNotExistFlag(String createLoopsIfNotExistFlag) 
    {
        this.createLoopsIfNotExistFlag = createLoopsIfNotExistFlag;
        hasChanged = true;
    }// setCreateLoopsIfNotExistFlag
}// DcsVariableTableDataSourceDialogSettings
