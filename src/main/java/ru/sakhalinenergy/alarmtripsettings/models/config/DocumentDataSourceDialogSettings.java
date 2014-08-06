package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements "Create/edit document data source" dialog settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class DocumentDataSourceDialogSettings extends DataSourceDialogSettings implements DocumentDataSourceDialogSettingsObservable
{
    private static DocumentDataSourceDialogSettings instance;
    private static final String SECTION_NAME = "DocumentDataSourceDialog";
        
    // Settings fields:
    @Entry private String revisionDate;
    @Entry private String documentNumber;
    @Entry private String documentLink;

    
    /**
     * Making constructor private to forbade new class instances.
     */
    private DocumentDataSourceDialogSettings(){}
    
    
    /**
     * Returns reference on a single dialog settings class instance. If instance 
     * not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static DocumentDataSourceDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new DocumentDataSourceDialogSettings();
        }// if
        
        return instance;
    }// getInstance
    
    
    /**
     * Returns document revision date setting value.
     * 
     * @return Document revision date
     */
    @Override
    public String getRevisionDate() 
    {
        return revisionDate;
    }// getRevisionDate
    

    /**
     * Sets up document revision date setting value.
     * 
     * @param revisionDate Document revision date
     */
    public void setRevisionDate(String revisionDate) 
    {
        this.revisionDate = revisionDate;
        hasChanged = true;
    }// setRevisionDate
    

    /**
     * Returns document number setting value.
     * 
     * @return Document number
     */
    @Override
    public String getDocumentNumber() 
    {
        return documentNumber;
    }// getDocumentNumber

    
    /**
     * Sets up document number setting value.
     * 
     * @param documentNumber Document number
     */
    public void setDocumentNumber(String documentNumber) 
    {
        this.documentNumber = documentNumber;
        hasChanged = true;
    }// setDocumentNumber
    

    /**
     * Returns document link setting value.
     * 
     * @return Document link
     */
    @Override
    public String getDocumentLink() 
    {
        return documentLink;
    }// getDocumentLink
    

    /**
     * Sets up document link setting value.
     * 
     * @param documentLink Document link
     */
    public void setDocumentLink(String documentLink) 
    {
        this.documentLink = documentLink;
        hasChanged = true;
    }// setDocumentLink
}// DocumentDataSourceDialogSettings 
