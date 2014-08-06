package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Create/edit SPI export data source" dialog settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class IntoolsExportDataSourceDialogSettings extends DataSourceDialogSettings implements IntoolsExportDataSourceDialogSettingsObservable
{
    private static IntoolsExportDataSourceDialogSettings instance;
    private static final String SECTION_NAME = "IntoolsExportDataSourceDialog";
    
    @Entry private String exportDate;
    @Entry private String databaseName;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private IntoolsExportDataSourceDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static IntoolsExportDataSourceDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new IntoolsExportDataSourceDialogSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns export date.
     * 
     * @return Export date
     */
    @Override
    public String getExportDate() 
    {
        return exportDate;
    }// getExportDate

    
    /**
     * Sets up export date.
     *  
     * @param exportDate Export date
     */
    public void setExportDate(String exportDate) 
    {
        this.exportDate = exportDate;
        hasChanged = true;
    }// setExportDate

    
    /**
     * Returns SPI database name.
     * 
     * @return SPI database name
     */
    @Override
    public String getDatabaseName() 
    {
        return databaseName;
    }// getDatabaseName

    
    /**
     * Sets up SPI database name.
     * 
     * @param databaseName SPI database name
     */
    public void setDatabaseName(String databaseName) 
    {
        this.databaseName = databaseName;
        hasChanged = true;
    }// setDatabaseName
}// IntoolsExportDataSourceDialogSettings
