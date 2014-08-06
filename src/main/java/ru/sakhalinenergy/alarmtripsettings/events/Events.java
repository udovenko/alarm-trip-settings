package ru.sakhalinenergy.alarmtripsettings.events;

import java.util.HashMap;
import javax.swing.event.EventListenerList;


/**
 * Implements methods set for events support.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class Events 
{
    private final HashMap<Enum, EventListenerList> eventListeners = new HashMap();
    
    
    /**
     * Adds listener for given event type.
     * 
     * @param eventType Event type identifier
     * @param listener Event listener instance
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        // If listeners hash already contains listeners group for given event type:
        if (eventListeners.containsKey(eventType))
        {
            eventListeners.get(eventType).add(CustomEventListener.class, listener);
        
        } else { // If goroup not created yet:
        
            eventListeners.put(eventType, new EventListenerList());
            eventListeners.get(eventType).add(CustomEventListener.class, listener);
        }// else
    }// on
    
    
    /**
     * Adds listeners set for given event type. 
     * 
     * @param eventType Event type identifier
     * @param listeners Event listener instances array
     */
    public void on(Enum eventType, Object[] listeners)
    {
        // If listeners hash already contains group for given event type:
        if (eventListeners.containsKey(eventType))
        {
            for (int i = 0; i < listeners.length; i = i+2)
            {
                if (listeners[i] == CustomEventListener.class) 
                {
                    eventListeners.get(eventType).add(CustomEventListener.class, (CustomEventListener)listeners[i+1]);
                }// if
            }// for
        } else { // If listeners group not created yet:
        
            eventListeners.put(eventType, new EventListenerList());
            for (int i = 0; i < listeners.length; i = i+2)
            {
                if (listeners[i] == CustomEventListener.class) 
                {
                    eventListeners.get(eventType).add(CustomEventListener.class, (CustomEventListener)listeners[i+1]);
                }// if
            }// for
        }// else
    }// on
    
    
    /**
     * Removes listeners group for given event type.
     * 
     * @param eventType Event type identifier
     */
    public void off(Enum eventType)
    {
        // If listeners hash contains group for given event, remove it:
        if (eventListeners.containsKey(eventType))
        {
            eventListeners.remove(eventType);
        }// if
    }// off
    
    
    /**
     * Returns listeners for given event type.
     * 
     * @param eventType Event type identifier
     * @return Listener instances array
     */
    public Object[] get(Enum eventType)
    {
        if (eventListeners.containsKey(eventType)) return eventListeners.get(eventType).getListenerList();
        else return null;
    }// get
    
    
    /**
     * Triggers event of given type, which means executing "customEventOccurred" 
     * method for all listener instances in group.
     * 
     * @param eventType Event type identifier
     * @param event Event instance with appropriate context
     */
    public void trigger(Enum eventType, CustomEvent event)
    {
        // If listeners hash contains group for given event type:
        if (eventListeners.containsKey(eventType))
        {
            Object[] listeners = this.get(eventType);
            for (int i = 0; i < listeners.length; i = i+2)
            {
                if (listeners[i] == CustomEventListener.class) 
                {
                    ((CustomEventListener) listeners[i+1]).customEventOccurred(event);
                }// if
            }// for
        }// if
    }// trigger
}// Events
