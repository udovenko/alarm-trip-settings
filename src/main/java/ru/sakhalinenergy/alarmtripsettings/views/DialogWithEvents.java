package ru.sakhalinenergy.alarmtripsettings.views;

import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;


/**
 * Abstract parent class for dialogs with events support.
 *
 * @author Denis.Udovenko
 * @version 1.0.1
 */
public abstract class DialogWithEvents extends Dialog
{
    
    protected Events events = new Events();
    
    
    /**
     * Метод добавляет подписчика на заданное событие модели.
     * 
     * @param eventType Идентификатор типа события
     * @param listener Экземпляр подписчика
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        this.events.on(eventType, listener);
    }//on
    
    
    /**
     * Метод удаляет всех подписчиков на заданное событие модели.
     * 
     * @param eventType Идентификатор типа события
     */
    public void off(Enum eventType)
    {
        this.events.off(eventType);
    }//off
    
    
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
}//DialogWithEvents
