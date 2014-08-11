package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;


/**
 * Interface of tags data source model for using by views. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.4
 */
public interface TagsSourceObservable extends ModelObservable
{
   
    /**
     * Returns current selected plant entity.
     * 
     * @return Plant entity
     */
    public Plant getPlant();
    
    
    /**
     * Returns current tags data source entity. Attention: direct setting up of
     * entity's properties will not trigger any events for views and 
     * controllers.
     * 
     * @return Current data source entity
     */
    public Source getEntity();

    
    /**
     * Returns tags list sorted by tag name.
     * 
     * @return Tags list sorted by tag name
     */
    public List<Tag> getSortedTags();
    
}// TagsSourceObservable
