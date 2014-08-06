package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;


/**
 * Interface of plants logic model for using by views. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface PlantsLogicObservable extends EventsObservable
{
   
    /**
     * Returns current plants list.
     * 
     * @return Plants list
     */
    public List<Plant> getPlants();
    
}// PlantsLogicObservable
