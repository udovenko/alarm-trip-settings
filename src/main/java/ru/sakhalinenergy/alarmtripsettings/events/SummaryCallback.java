package ru.sakhalinenergy.alarmtripsettings.events;


/**
 * Implements callback for given events combination.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class SummaryCallback extends Events
{
    private final CustomEventListener callback;
    private long eventsCheckSum = 0;
    private long eventsProcessed = 0;
    
    
    /**
     * Public constructor. Accepts callback instance.
     * 
     * @param callback Summary events callback instance
     */
    public SummaryCallback(CustomEventListener callback)
    {
        this.callback = callback;
    }// SummaryCallback
    
    
    /**
     * Adds event to callback conditions list.
     * 
     * @param eventsObject Object which event will be added
     * @param event Event type identifier
     * @return Current summary callback instance for chain calls
     */
    public SummaryCallback add(EventsObservable eventsObject, final Enum event)
    {
        eventsCheckSum += event.hashCode();
        
        eventsObject.on(event, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                eventsProcessed += event.hashCode();
                
                if (eventsProcessed == eventsCheckSum)
                {
                    CustomEvent emptyEvent = new CustomEvent(new Object());
                    callback.customEventOccurred(emptyEvent);
                }// if
            }// customEventOccurred
        });// on
        
        return this;
    }// add
}// SummaryCallback
