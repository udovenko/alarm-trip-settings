package ru.sakhalinenergy.alarmtripsettings.models;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.Events;


/**
 * Abstract parent for all models classes.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public abstract class Model implements ModelObservable
{

    protected Events events = new Events();
    
    
    /**
     * Adds new listener for given model's event.
     * 
     * @param eventType Event type identifier
     * @param listener Listener instance
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        this.events.on(eventType, listener);
    }// on
    
    
    /**
     * Removes all model's subscribers for given event type.
     * 
     * @param eventType Event type identifier
     */
    public void off(Enum eventType)
    {
        this.events.off(eventType);
    }// off
    
    
    /**
     * Subscribes model's events listeners on given thread typical events.
     * 
     * @param thread Worker thread which events model's subscribers now will listen
     * @param theadProgressSubscriberType Model's thread progress event type identifier
     * @param theadWarningSubscriberType Model's thread warning event type identifier
     * @param theadErrorSubscriberType Model's thread error event type identifier
     * @param workDoneSubscriberType Model's thread work done event type identifier
     */
    protected void _subscribeOnThreadEvents(WorkerThread thread, Enum theadProgressSubscriberType,
        Enum theadWarningSubscriberType, Enum theadErrorSubscriberType, Enum workDoneSubscriberType)
    {
        if (this.events.get(theadProgressSubscriberType) != null) thread.events.on(WorkerThread.Event.PROGRESS, this.events.get(theadProgressSubscriberType));
        if (this.events.get(theadWarningSubscriberType) != null) thread.events.on(WorkerThread.Event.WARNING, this.events.get(theadWarningSubscriberType));
        if (this.events.get(theadErrorSubscriberType) != null) thread.events.on(WorkerThread.Event.ERROR, this.events.get(theadErrorSubscriberType));
        if (this.events.get(workDoneSubscriberType) != null) thread.events.on(WorkerThread.Event.WORK_DONE, this.events.get(workDoneSubscriberType));
    }// _subscribeOnThreadEvents
}// Model
