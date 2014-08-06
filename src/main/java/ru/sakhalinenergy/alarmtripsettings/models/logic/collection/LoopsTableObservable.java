package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Interface of loops table model for using by views. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.4
 */
public interface LoopsTableObservable extends EventsObservable
{

    /**
     * Returns current loops list.
     * 
     * @return Loops list
     */
    public List<Loop> getLoops();
    
    
    /**
     * Wraps each loop of current collection into setting selector wrapper and
     * returns a list of wrapped loops.
     * 
     * @return list of loops wrapped into settings selection logic
     */
    public List<SettingsSelector> getWrappedLoops();
    
    
    /**
     * Checks neighbor loop in the list and returns true if it equals (by its 
     * components names) to given one.
     * 
     * @param loop Loop to be checked for split
     * @return True if loop is split, else false
     */
    public boolean isLoopSplit(Loop loop);
    
    
    /**
     * Returns a list of data sources which contain tags form current loops 
     * collection.
     * 
     * @return Data sources list
     */
    public List<Source> getSources();
    
}// LoopsTableObservable
