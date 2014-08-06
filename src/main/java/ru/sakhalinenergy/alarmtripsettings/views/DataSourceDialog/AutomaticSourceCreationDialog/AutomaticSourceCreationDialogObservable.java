package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;


/**
 * An interface for automatic source creation dialogs, gives controllers an 
 * access to common events and events binding methods.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface AutomaticSourceCreationDialogObservable 
{
    
    /**
     * Метод добавляет подписчика на заданное событие модели.
     * 
     * @param eventType Идентификатор типа события
     * @param listener Экземпляр подписчика
     */
    public void on(Enum eventType, CustomEventListener listener);
    
    
    /**
     * Метод удаляет всех подписчиков на заданное событие модели.
     * 
     * @param eventType Идентификатор типа события
     */
    public void off(Enum eventType);
        
    
    /**
     * Triggers custom events with given type on descendant object.
     * 
     * @param eventType Type id for event which will be triggered
     * @param event Custom event object
     */
    public void trigger(Enum eventType, CustomEvent event);
    
    
     /**
     * Returns selected plant.
     * 
     * @return Selected plant
     */
    public Plant getPlant();
    
    
    /**
     * Returns selected tag mask.
     * 
     * @return Selected tag mask
     */
    public TagMask getTagMask();
    
}//AutomaticSourceCreationDialogObservable
