package ru.sakhalinenergy.alarmtripsettings.views.panel;

import javax.swing.JPanel;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.Events;


/**
 * Abstract parent for all application panels.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class Panel extends JPanel
{
    
    protected Events events = new Events();
    
    
    /**
     * Adds a new subscriber on given event type in subscribers list.
     * 
     * @param eventType Type of event we subscribing on
     * @param listener New subscriber instance
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        events.on(eventType, listener);
    }// on
    
    
    /**
     * Removes all subscribers for given event type.
     * 
     * @param eventType Type of event for which subscribers will be removed
     */
    public void off(Enum eventType)
    {
        events.off(eventType);
    }// off
    
    
    /**
     * Triggers custom events with given type on descendant object.
     * 
     * @param eventType Type id for event which will be triggered
     * @param event Custom event object
     */
    public void trigger(Enum eventType, CustomEvent event)
    {
        events.trigger(eventType, event);
    }//trigger
}// Panel
