package ru.sakhalinenergy.alarmtripsettings.events;

import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Implements application events pull.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class EventsPull 
{
    
    // Pull's own event types:
    public enum Event
    {
        EVENT_RECORDED
    }// OwnEvent
        
    
    private final HashMap<Class, LinkedHashMap> classesWithEvents = new HashMap();
    public Events events = new Events();
    
    
    /**
     * Starts events recording into pull by subscribing on given object's event
     * type.
     * 
     * @param sender Object which events pull will start to record
     * @param senderEvents Object's events collection
     * @param eventType Event type to record identifier
     */
    public void startRecording(final Object sender, final Events senderEvents, final Enum eventType)
    {
        senderEvents.on(eventType, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {   
                LinkedHashMap<Enum, CustomEvent> classEvents;
                
                // Create events hash for given object's class:
                if (classesWithEvents.containsKey(sender.getClass()))
                {
                    classEvents = classesWithEvents.get(sender.getClass());
                } else {
                    
                    classEvents = new LinkedHashMap<Enum, CustomEvent>();
                    classesWithEvents.put(sender.getClass(), classEvents);
                }// else
                
                // Add event to created group:
                classEvents.put(eventType, evt);
                
                // Trigger own "event recorded" event:
                CustomEvent eventRecordedEvent = new CustomEvent(new Object[]{sender.getClass(), eventType});
                EventsPull.this.events.trigger(Event.EVENT_RECORDED, eventRecordedEvent);
            }// customEventOccurred
        });// on
    }// startRecording
  
    
    /**
     * Finds event record in the pull by sender object class and event type.
     * 
     * @param senderClass Sender object type
     * @param eventType Event type identifier
     * @return Found event instance or null if event not found
     */
    public CustomEvent findRecord(Class senderClass, Byte eventType)
    {
        if (!classesWithEvents.containsKey(senderClass)) return null;
        
        LinkedHashMap<Byte, CustomEvent> classEvents = classesWithEvents.get(senderClass);
        if (!classEvents.containsKey(eventType)) return null;
        
        return classEvents.get(eventType);
    }// findRecord
}// EventsPull
