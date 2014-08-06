package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of "Create/edit document data source" dialog settings model for 
 * using by views. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface DocumentDataSourceDialogSettingsObservable extends DataSourceDialogSettingsObservable
{
    
    /**
     * Returns document revision date setting value.
     * 
     * @return Document revision date
     */
    public String getRevisionDate();
     

    /**
     * Returns document number setting value.
     * 
     * @return Document number
     */
    public String getDocumentNumber();

    
    /**
     * Returns document link setting value.
     * 
     * @return Document link
     */
    public String getDocumentLink();

}// DocumentDataSourceDialogSettingsObservable
