package ru.sakhalinenergy.alarmtripsettings.models;

import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;


/**
 * Interface of abstract model class. Contains common progress information keys
 * for descendants interfaces.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface ModelObservable extends EventsObservable
{
    public static enum ProgressInfoKey
    {
        CYCLE_CAPTION, 
        CYCLE_PERCENTAGE,
        OUTER_CYCLE_CAPTION,
        OUTER_CYCLE_PERCENTAGE,
        INNER_CYCLE_CAPTION, 
        INNER_CYCLE_PERCENTAGE
    }// ProgressInfoKey   
}// ModelObservable
