package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;


/**
 * Interface of tags data source model for using by controllers. Extends 
 * interface for views with setters and data control methods.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface TagsSourceControllable extends TagsSourceObservable
{
   
    /**
     * Sets up current plant entity instance and triggers appropriate event for
     * all subscribers.
     * 
     * @param plant Plant entity instance
     */
    public void setPlant(Plant plant);
    
    
    /**
     * Sets up tag mask (format regular expression) for tag names parsing.
     *  
     * @param mask Tag mask entity
     */
    public void setTagMask(TagMask mask);
    
    
    /**
     * Adds or updates data source property.
     * 
     * @param property Data source property
     */
    public void setProperty(SourceProperty property);
    
    
    /**
     * Tries to parse given tag name according to current tag mask and, if it 
     * was successful, crates appropriate tag and its parent loop instances,
     * ties them to current data source and triggers TAG_SET_UPDATED event.
     * 
     * @throws Exception
     * @param tagName Tag name
     * @return Created tag instance
     */
    public Tag addTag(String tagName) throws Exception;
    
    
    /**
     * Removes given tag from data source's tags collection, triggers 
     * TAG_SET_UPDATED event.
     * 
     * @param tag Tag instance to be removed
     */
    public void removeTag(Tag tag);
    
    
    /**
     * Adds setting to given tag, triggers TAG_SET_UPDATED event.
     * 
     * @param tag Tag instance to which new setting will be added
     * @param setting Setting to be added
     */
    public void addTagSetting(Tag tag, TagSetting setting);
    
    
    /**
     * Updates particular existing tag setting value if new value passes 
     * emptiness checking. If value is updated, triggers TAG_SET_UPDATED event.
     * 
     * @param setting Tag setting instance
     * @param newValue New value to be set
     */
    public void updateTagSettingValue(TagSetting setting, String newValue);

    
    /**
     * Removes given settings from its tag settings collection and triggers 
     * TAG_SET_UPDATED event.
     * 
     * @param settingToRemove Setting to be removed instance
     */
    public void removeTagSetting(TagSetting settingToRemove);
    
    
    /**
     * Add property to particular tag setting and triggers TAG_SET_UPDATED 
     * event, if given property value passes checking for an emptiness.
     * 
     * @param setting Tag setting to which a new property will added
     * @param property A new property which will be added
     */
    public void addTagSettingProperty(TagSetting setting, TagSettingProperty property);
    
    
    /**
     * Updates given property value and triggers TAG_SET_UPDATED event, if new 
     * value passes checking for emptiness.
     * 
     * @param property Property instance for which new value will be set
     * @param newValue New value for given property
     */
    public void updateTagSettingPropertyValue(TagSettingProperty property, String newValue);
    
    
    /**
     * Removes tag setting property from setting properties collection and 
     * triggers TAG_SET_UPDATED event
     * 
     * @param propertyToRemove Tag setting property to be remove
     */
    public void removeTagSettingProperty(TagSettingProperty propertyToRemove);
    
    
    /**
     * Creates a thread for saving data source to storage. Subscribes models 
     * events listeners on thread events and executes it.
     * 
     * @param createLoopsIfNotExist Flag defines a necessity of creations new loops and string tags within new loop 
     */
    public void save(boolean createLoopsIfNotExist);
    
}// TagsSourceControllable