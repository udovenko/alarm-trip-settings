package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of create/edit tags data source dialogs settings classes for using
 * by views. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface DataSourceDialogSettingsObservable extends PlantAndTagFormatSettingsObservable
{
    
    /**
     * Returns data source name setting value.
     * 
     * @return Data source name
     */
    public String getDataSourceName(); 
   

    /**
     * Returns vertical offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @return Dialog's top offset
     */
    public String getDialogTop(); 
     

    /**
     * Returns horizontal offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @return Dialog's left offset
     */
    public String getDialogLeft();

    
    /**
     * Returns dialog's width setting value.
     * 
     * @return Dialog's width
     */
    public String getDialogWidth();

    
    /**
     * Returns dialog's height setting value.
     * 
     * @return Dialog's height
     */
    public String getDialogHeight();
   

    /**
     * Returns data source's priority setting value.
     * 
     * @return Data source's priority
     */
    public String getPriority();
     

    /**
     * Returns data source's comment setting value.
     * 
     * @return Data source's comment
     */
    public String getComment(); 
    
}// DataSourceDialogSettingsObservable
