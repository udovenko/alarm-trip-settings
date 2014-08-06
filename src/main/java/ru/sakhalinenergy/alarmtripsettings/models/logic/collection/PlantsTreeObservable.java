package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.Set;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;


/**
 * Interface of plants tree model for using by views. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface PlantsTreeObservable extends EventsObservable
{
   
    /**
     * Returns tree plants list.
     * 
     * @return Tree plants list
     */
    public Set<Plant> getPlants();
    
}// AssetsTreeObservable
