package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of "Create/edit SPI export data source" dialog settings model for 
 * using by views. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface IntoolsExportDataSourceDialogSettingsObservable extends DataSourceDialogSettingsObservable
{
    
    /**
     * Returns export date.
     * 
     * @return Export date
     */
    public String getExportDate();
   
       
    /**
     * Returns SPI database name.
     * 
     * @return SPI database name
     */
    public String getDatabaseName();
   
}// IntoolsExportDataSourceDialogControllerObservable
