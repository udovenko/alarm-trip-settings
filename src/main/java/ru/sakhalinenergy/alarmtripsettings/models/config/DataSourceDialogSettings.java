package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * An abstract parent for settings classes of create/edit tags data source
 * dialogs.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class DataSourceDialogSettings extends PlantAndTagFormatSettings
{
    // Settings fields:
    @Entry private String dataSourceName;
    @Entry private String dialogTop;
    @Entry private String dialogLeft;
    @Entry private String dialogWidth;
    @Entry private String dialogHeight;
    @Entry private String priority;
    @Entry private String comment;
    
        
    /**
     * Returns data source name setting value.
     * 
     * @return Data source name
     */
    public String getDataSourceName() 
    {
        return dataSourceName;
    }// getDataSourceName
    

    /**
     * Sets data source name settings value.
     * 
     * @param dataSourceName Data source name
     */
    public void setDataSourceName(String dataSourceName) 
    {
        this.dataSourceName = dataSourceName;
        hasChanged = true;
    }// setDataSourceName
    

    /**
     * Returns vertical offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @return Dialog's top offset
     */
    public String getDialogTop() 
    {
        return dialogTop;
    }// getDialogTop
    

    /**
     * Sets up vertical offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @param dialogTop Dialog's top offset
     */
    public void setDialogTop(String dialogTop) 
    {
        this.dialogTop = dialogTop;
        hasChanged = true;
    }// setDialogTop
    

    /**
     * Returns horizontal offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @return Dialog's left offset
     */
    public String getDialogLeft() 
    {
        return dialogLeft;
    }// getDialogLeft

    
    /**
     * Sets up horizontal offset for left upper corner of dialog's window form
     * initial screen coordinates.
     * 
     * @param dialogLeft Dialog's left offset
     */
    public void setDialogLeft(String dialogLeft) 
    {
        this.dialogLeft = dialogLeft;
        hasChanged = true;
    }// setDialogLeft

    
    /**
     * Returns dialog's width setting value.
     * 
     * @return Dialog's width
     */
    public String getDialogWidth() 
    {
        return dialogWidth;
    }// getDialogWidth

    
    /**
     * Sets up dialog's width setting value.
     * 
     * @param dialogWidth Dialog's width
     */
    public void setDialogWidth(String dialogWidth) 
    {
        this.dialogWidth = dialogWidth;
        hasChanged = true;
    }// setDialogWidth

    
    /**
     * Returns dialog's height setting value.
     * 
     * @return Dialog's height
     */
    public String getDialogHeight() 
    {
        return dialogHeight;
    }// getDialogHeight

    
    /**
     * Sets up dialog's height setting value.
     * 
     * @param dialogHeight Dialog's height
     */
    public void setDialogHeight(String dialogHeight) 
    {
        this.dialogHeight = dialogHeight;
        hasChanged = true;
    }// setDialogHeight
    

    /**
     * Returns data source's priority setting value.
     * 
     * @return Data source's priority
     */
    public String getPriority() 
    {
        return priority;
    }// getPriority

    
    /**
     * Sets up data source's priority setting value.
     * 
     * @param priority Data source's priority
     */
    public void setPriority(String priority) 
    {
        this.priority = priority;
        hasChanged = true;
    }// setPriority
    
  
    /**
     * Returns data source's comment setting value.
     * 
     * @return Data source's comment
     */
    public String getComment() 
    {
        return comment;
    }// getComment
    

    /**
     * Sets up data source's comment setting value.
     * 
     * @param comment Data source's comment
     */
    public void setComment(String comment) 
    {
        this.comment = comment;
        hasChanged = true;
    }// setComment
}// DataSourceDialogSettings
