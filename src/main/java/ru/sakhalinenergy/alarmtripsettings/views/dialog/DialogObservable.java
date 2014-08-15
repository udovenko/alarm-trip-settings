package ru.sakhalinenergy.alarmtripsettings.views.dialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;


/**
 * An interface for dialogs with events support.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface DialogObservable 
{
    
    /**
     * Adds a new subscriber on given event type in subscribers list.
     * 
     * @param eventType Type of event we subscribing on
     * @param listener New subscriber instance
     */
    public void on(Enum eventType, CustomEventListener listener);
    
    
    /**
     * Removes all subscribers for given event type.
     * 
     * @param eventType Type of event for which subscribers will be removed
     */
    public void off(Enum eventType);
        
    
    /**
     * Triggers custom events with given type on descendant object.
     * 
     * @param eventType Type id for event will be triggered
     * @param event Custom event object
     */
    public void trigger(Enum eventType, CustomEvent event);
    
}// DialogWithEventsObservable
