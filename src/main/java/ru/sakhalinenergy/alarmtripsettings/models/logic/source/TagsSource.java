package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.hibernate.Hibernate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;



/**
 * Implement logic for work with tags set as within separate data source.
 *
 * @author Denis Udovenko
 * @version 1.0.5
 */
public class TagsSource extends Model implements TagsSourceObservable, TagsSourceControllable
{
    // Value matched this regexp will considered as empty:
    private static final String EMPTY_VALUE_SYNONYMS = "[-*]|^$";
    
    protected Source source;
    protected Plant plant;
    protected TagMask tagMask;
    protected final Set<Tag> tagsToDelete = new HashSet();
        
    
    /**
     * Public constructor. Sets up initial source instance.
     * 
     * @param source Source entity instance to be wrapped in current logic
     */
    public TagsSource(Source source)
    {
        this.source = source;
    }// TagsSource
    
    
    /**
     * Returns current tags data source entity. Attention: direct setting up of
     * entity's properties will not trigger any events for views and 
     * controllers.
     * 
     * @return Current data source entity
     */
    @Override
    public Source getEntity()
    {
        return source;
    }// getEntity
    
    
    /**
     * Returns current selected plant entity.
     * 
     * @return Plant entity
     */
    @Override
    public Plant getPlant()
    {
        return plant;
    }// getPlant
    
    
    /**
     * Returns tags list sorted by tag name.
     * 
     * @return Tags list sorted by tag name
     */
    @Override
    public List<Tag> getSortedTags()
    {
        List sortedTags = new ArrayList(source.getTags());
        
        // Sort tags collection:
        Collections.sort(sortedTags, new Comparator<Tag>()
        {
            @Override
            public int compare(Tag tagOne, Tag tagTwo)
            {
                return tagOne.getName().compareTo(tagTwo.getName());
            }// compare
        });// sort
        
        return sortedTags;
    }// getSortedTags
    
    
    /**
     * Sets up current plant entity instance and triggers appropriate event for
     * all subscribers.
     * 
     * @param plant Plant entity instance
     */
    @Override
    public void setPlant(Plant plant)
    {
        this.plant = plant;
        CustomEvent bookConnectedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.PLANT_CODE_SET, bookConnectedEvent);
    }// setPlant
    
    
    /**
     * Sets up tag mask (format regular expression) for tag names parsing.
     *  
     * @param mask Tag mask entity
     */
    @Override
    public void setTagMask(TagMask mask)
    {
        tagMask = mask;
    }// setTagMask
    
    
    /**
     * Adds or updates data source property.
     * 
     * @param property Data source property
     */
    @Override
    public void setProperty(SourceProperty property)
    {
        // If data source already has property with same type, just update its value:
        for (SourceProperty tempProperty : source.getProperties())
        {
            if (tempProperty.getTypeId() == property.getTypeId())
            {
                tempProperty.setValue(property.getValue());
                return;
            }// if
        }// for
        
        // If property with same type does not exist, add new one:
        source.getProperties().add(property);
        property.setSource(source);
    }// setProperty
       
    
    /**
     * Tries to parse given tag name according to current tag mask and, if it 
     * was successful, crates appropriate tag and its parent loop instances,
     * ties them to current data source and triggers TAG_SET_UPDATED event.
     * 
     * @throws Exception
     * @param tagName Tag name
     * @return Created tag instance
     */
    @Override
    public Tag addTag(String tagName) throws Exception
    {
        // Check if current tag mask or plant is null:
        if (tagMask == null || plant == null) throw new Exception("Current plant or tag mask is null");
        
        Pattern pattern = Pattern.compile(tagMask.getMask());
        Matcher matcher = pattern.matcher(tagName);

        // If tag name does not match regexp:
        if (!matcher.matches()) throw new Exception("tag name \"" + tagName + "\" does not match selected mask");
                
        // Try to get plant code from tag name:
        String plantCode = null;
        try {
        
            plantCode = matcher.group("plant"); 
        } catch (Exception exception) {}
            
        // If tag name has plant code but it does not match current plant code field:
        if (plantCode != null && !plantCode.equals(plant.getId()))
        {
            // Throw tag creation error:
            throw new Exception("Plant code in tag name \"" + tagName + "\" does not match selected plant code \"" + plant.getId() + "\"");
        }// if
        
        // Crearte a tag:
        Tag tag = new Tag();
        tag.setName(tagName.replace(" ", ""));
        tag.setModifier(matcher.group("modifier"));
                
        Loop loop = new Loop();
        loop.setPlant(plant.getId());
        
        loop.setArea(matcher.group("area"));
        loop.setUnit(matcher.group("unit"));
        loop.setMeasuredVariable(matcher.group("variable"));
        loop.setUniqueIndex(matcher.group("index"));
            
        String suffix = matcher.group("suffix"); 
        loop.setSuffix(suffix != null ? suffix : "");
            
        Loop existingLoop = _findLoop(loop);
            
        // If loop for created tag does not belong to current data source yet:
        if (existingLoop == null)
        {
            tag.setLoop(loop);
            loop.getTags().add(tag);
            
        } else { // If loop for created tag exists in current data surce, just add new tag to it:
            
            tag.setLoop(existingLoop);
            existingLoop.getTags().add(tag);
        }// else
            
        tag.setSource(source);
        source.getTags().add(tag);
                        
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(tag);
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
            
        return tag;
    }// addTag
    
    
    /**
     * Removes given tag from data source's tags collection, triggers 
     * TAG_SET_UPDATED event.
     * 
     * @param tag Tag instance to be removed
     */
    @Override
    public void removeTag(Tag tag)
    {
        source.getTags().remove(tag);
        tag.setSource(null);
        
        tag.getLoop().getTags().remove(tag);
        tag.setLoop(null);
        
        tagsToDelete.add(tag);
        
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }// removeTag
    
    
    /**
     * Adds setting to given tag, triggers TAG_SET_UPDATED event.
     * 
     * @param tag Tag instance to which new setting will be added
     * @param setting Setting to be added
     */
    @Override
    public void addTagSetting(Tag tag, TagSetting setting)
    {
        setting.setValue(setting.getValue().replace(" ", ""));
        if (_isEmptynessSynonym(setting.getValue())) return;
                
        tag.getSettings().add(setting);
        setting.setTag(tag);
                
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }// addTagSetting
    
    
    /**
     * Updates particular existing tag setting value if new value passes 
     * emptiness checking. If value is updated, triggers TAG_SET_UPDATED event.
     * 
     * @param setting Tag setting instance
     * @param newValue New value to be set
     */
    @Override
    public void updateTagSettingValue(TagSetting setting, String newValue)
    {
        newValue = newValue.replace(" ", "");
        
        if (!_isEmptynessSynonym(newValue))
        {
            setting.setValue(newValue);
            
            // Trigger "tag set updated" event:
            CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
            events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
        }// if
    }// updateTagSettingValue
    
    
    /**
     * Removes given settings from its tag settings collection and triggers 
     * TAG_SET_UPDATED event.
     * 
     * @param settingToRemove Setting to be removed instance
     */
    @Override
    public void removeTagSetting(TagSetting settingToRemove)
    {
        settingToRemove.getTag().getSettings().remove(settingToRemove);
        settingToRemove.setTag(null);
                
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }// removeTagSetting
    
    
    /**
     * Add property to particular tag setting and triggers TAG_SET_UPDATED 
     * event, if given property value passes checking for an emptiness.
     * 
     * @param setting Tag setting to which a new property will added
     * @param property A new property which will be added
     */
    @Override
    public void addTagSettingProperty(TagSetting setting, TagSettingProperty property)
    {
        property.setValue(property.getValue().replace(" ", ""));
        if (_isEmptynessSynonym(property.getValue())) return;
        
        setting.getProperties().add(property);
        property.setSetting(setting);
                
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }// addTagSettingProperty
      
    
    /**
     * Updates given property value and triggers TAG_SET_UPDATED event, if new 
     * value passes checking for emptiness.
     * 
     * @param property Property instance for which new value will be set
     * @param newValue New value for given property
     */
    @Override
    public void updateTagSettingPropertyValue(TagSettingProperty property, String newValue)
    {
        newValue = newValue.replace(" ", "");
        
        if (!_isEmptynessSynonym(newValue))
        {
            property.setValue(newValue);
            
            // Trigger "tag set updated" event:
            CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
            events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
        }// if
    }// updateTagSettingPropertyValue
    
    
    /**
     * Removes tag setting property from setting properties collection and 
     * triggers TAG_SET_UPDATED event
     * 
     * @param propertyToRemove Tag setting property to be remove
     */
    @Override
    public void removeTagSettingProperty(TagSettingProperty propertyToRemove)
    {
        propertyToRemove.getSetting().getProperties().remove(propertyToRemove);
        propertyToRemove.setSetting(null);
                
        // Trigger "tag set updated" event:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }// removeTagSettingProperty
    
    
    /**
     * Checks if setting value is synonym of nothing.
     * 
     * @param value Value to be checked
     * @return True if value considered as empty, else false
     */
    private boolean _isEmptynessSynonym(String value)
    {
        Pattern pattern = Pattern.compile(EMPTY_VALUE_SYNONYMS);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) return true;
        
        return false;
    }// _isEmptynessSynonym
    
    
    /**
     * Searches given loop in current source tags parent loops set.
     * 
     * @param searchedLoop Searched loop
     * @return Reference to found loop or null if nothing was found
     */
    private Loop _findLoop(Loop searchedLoop)
    {
        Loop tempLoop;
        
        for (Tag tempTag : source.getTags())
        {
            tempLoop = tempTag.getLoop();
            if (tempLoop.equals(searchedLoop)) return tempLoop;
        }// for
        
        return null;
    }// _findLoop
    
    
    /**
     * Creates a thread for data source tags and loops collection initialization. 
     * Subscribes models events listeners on thread events and executes it.
     */
    public void initialize()
    {
        // Create a thread:
        WorkerThread sourceInitializer = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                Session session = null;
                HashMap<ProgressInfoKey, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, " ");    
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                try 
                {
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Initiating remaining tags for selected source"); 
                    publish(progress);                    
                    
                    // Create Hibernate session and open transaction:
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                
                    Source mergedSource = (Source)session.merge(source);
                    Hibernate.initialize(mergedSource.getTags());
    
                    for (Tag tempTag : mergedSource.getTags())
                    {
                        Hibernate.initialize(tempTag.getLoop());
                        tempTag.getLoop().setTags(new HashSet());
                    }// for
                    
                    source = mergedSource;          

                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Data source saving error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                return new HashMap();
            }// doInBackground
        };// sourceInitializer
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(sourceInitializer, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.SOURCE_INITIALIZED);
                
        // Execute thread:
        sourceInitializer.execute();
    }// initialize
    
    
    /**
     * Creates a thread for saving data source to storage. Subscribes models 
     * events listeners on thread events and executes it.
     * 
     * @param createLoopsIfNotExist Flag defines a necessity of creations new loops and string tags within new loop 
     */
    @Override
    public void save(final boolean createLoopsIfNotExist)
    {
        // Create a thread:
        WorkerThread sourceSaver = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Saving source tags");
                
                try
                {
                    // Create Hibernate session and open transaction:
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    // Save data source and its properties:
                    session.saveOrUpdate(source);

                    // Create loops for tags or tie tags to existing loops:
                    Loop tempLoop, tempExistingLoop;
                    List<Loop> tempExistingLoops;
                    
                    int tagsProcessed = 0, tagsTotal = source.getTags().size();
                    
                    for (Tag tempTag : source.getTags())
                    {
                        tempLoop = tempTag.getLoop();
                        
                        if (tempTag.getLoop().getId() == 0)
                        {
                            Criteria criteria = session.createCriteria(Loop.class)
                                .add(Restrictions.eq("plant", tempLoop.getPlant()))
                                .add(Restrictions.eq("area", tempLoop.getArea()))
                                .add(Restrictions.eq("unit", tempLoop.getUnit()))
                                .add(Restrictions.eq("measuredVariable", tempLoop.getMeasuredVariable()))
                                .add(Restrictions.eq("uniqueIndex", tempLoop.getUniqueIndex()))
                                .add(Restrictions.eq("suffix", tempLoop.getSuffix()));
                            tempExistingLoops = criteria.list();
                        
                            if (tempExistingLoops.size() > 0) 
                            {    
                                tempExistingLoop = tempExistingLoops.get(0);
                                tempTag.setLoop(tempExistingLoop);
                                tempExistingLoop.getTags().add(tempTag);
                                session.saveOrUpdate(tempExistingLoop);
                                
                            } else if (createLoopsIfNotExist) session.save(tempLoop);
                            
                        } else session.saveOrUpdate(tempTag);
                        
                        // Publish current progress:
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)tagsProcessed / (double)tagsTotal * 90));
                        publish(progress);
                        
                        tagsProcessed++;
                    }// for
                    
                    // Remove tags, marked to be deleted:
                    for (Tag tempTag : tagsToDelete) session.delete(tempTag);
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing obsolete loops");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 92);
                    publish(progress);
                    
                    // Remove empty loops:
                    String hql = "DELETE FROM Loop loop_table WHERE (SELECT COUNT(id) FROM Tag WHERE loop_id = loop_table.id) = 0";
                    session.createQuery(hql).executeUpdate();
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Flushing updates");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 97);
                    publish(progress);
                    
                    // Flush changes, close transaction and Hibernate session:
                    session.flush();
                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Data source saving error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                return new HashMap();
            }// doInBackground
        };// WorkerThread
             
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(sourceSaver, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.SOURCE_SAVED);
        
        // Execute thread:
        sourceSaver.execute();
    }// save
    
    
    /**
     * Creates a thread for removing current data source and all its related 
     * information form storage. Subscribes models events listeners on thread 
     * events and executes it.
     */
    public void remove()
    {
        // Create a thread:
        WorkerThread sourceRemover = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                
                try
                {
                    // Create Hibernate session and open transaction:
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                    publish(progress);
                    
                    // Remove data source and its properties:
                    session.delete(source);
                    session.flush();
                           
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 20);
                    publish(progress);
                    
                    // Remove all tags which reffer to deleted source:
                    String hql = "DELETE FROM Tag WHERE source_id NOT IN (SELECT id FROM Source)";
                    session.createQuery(hql).executeUpdate();
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags settings");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 40);
                    publish(progress);
                    
                    // Remove all tags settngs which feffer to deleted tags:
                    hql = "DELETE FROM TagSetting WHERE tag_id NOT IN (SELECT id FROM Tag)";
                    session.createQuery(hql).executeUpdate();
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags settings properties");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 60);
                    publish(progress);
                    
                    // Remove all tags settings properties which reffer to deleted tags settings:
                    hql = "DELETE FROM TagSettingProperty WHERE setting_id NOT IN (SELECT id FROM TagSetting)";
                    session.createQuery(hql).executeUpdate();
                    
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing obsolete loops");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 80);
                    publish(progress);
                    
                    // Remove all empty loops:
                    hql = "DELETE FROM Loop loop_table WHERE (SELECT COUNT(id) FROM Tag WHERE loop_id = loop_table.id) = 0";
                    session.createQuery(hql).executeUpdate();

                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 100);
                    publish(progress);
                    
                    // Close transaction and current session:
                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                    
                    _invokeExceptionInEdt("Data source removing error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(sourceRemover, SourceEvent.THREAD_PROGRESS, 
            SourceEvent.THREAD_WARNING, SourceEvent.THREAD_ERROR, SourceEvent.SOURCE_REMOVED);
                
        // Execute thread:
        sourceRemover.execute();
    }// remove
}// Source
