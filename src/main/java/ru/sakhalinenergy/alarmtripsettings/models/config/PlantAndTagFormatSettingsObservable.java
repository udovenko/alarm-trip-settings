package ru.sakhalinenergy.alarmtripsettings.models.config;

import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;


/**
 * An interface for abstract class for working with plant and tag mask settings.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface PlantAndTagFormatSettingsObservable extends EventsObservable
{
    
    /**
     * Returns plant code.
     * 
     * @return Plant code
     */
    public String getPlantCode();
    
    
    /**
     * Returns tag format.
     * 
     * @return Tag format
     */
    public String getTagFormat();
    
}// PlantAndTagFormatSettingsObservable
