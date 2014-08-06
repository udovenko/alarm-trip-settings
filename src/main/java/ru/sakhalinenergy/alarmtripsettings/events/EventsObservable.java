package ru.sakhalinenergy.alarmtripsettings.events;


/**
 * Interface for objects which allow subscribing on their events.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface EventsObservable 
{
    
    /**
     * Adds listener for given event type.
     * 
     * @param eventType Event type identifier
     * @param listener Event listener instance
     */
    public void on(Enum eventType, CustomEventListener listener);
       
    
    /**
     * Removes listeners group for given event type.
     * 
     * @param eventType Event type identifier
     */
    public void off(Enum eventType);
    
}// EventsObservable
