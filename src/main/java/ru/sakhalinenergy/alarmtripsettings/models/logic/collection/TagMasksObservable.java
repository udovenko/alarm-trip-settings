package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;


/**
 * Interface of tag masks model for using by views. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface TagMasksObservable extends EventsObservable
{

    /**
     * Returns current tag masks list.
     * 
     * @return Tag masks list
     */
    public List<TagMask> getMasks();
    
}// TagMasksObservable
