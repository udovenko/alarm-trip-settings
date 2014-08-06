package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Abstract class implements plant and tag mask settings.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class PlantAndTagFormatSettings extends Config implements PlantAndTagFormatSettingsObservable
{
    // Setting fields:
    @Entry private String plantCode;
    @Entry private String tagFormat;
    
    
    /**
     * Returns plant code.
     * 
     * @return Plant code
     */
    @Override
    public String getPlantCode()
    {
        return plantCode;
    }// getPlantCode
    
    
    /**
     * Returns tag format.
     * 
     * @return Tag format
     */
    @Override
    public String getTagFormat() 
    {
        return tagFormat;
    }// getTagFormat
    
    
    /**
     * Sets up plant code.
     * 
     * @param plantCode Plant code
     */
    public void setPlantCode(String plantCode)
    {
        this.plantCode = plantCode;
        hasChanged = true;
    }// setPlantCode
        
    
    /**
     * Sets up tag format.
     * 
     * @param tagFormat Tag format
     */
    public void setTagFormat(String tagFormat) 
    {
        this.tagFormat = tagFormat;
        hasChanged = true;
    }// setTagFormat
}// PlantAndTagFormatSettings
