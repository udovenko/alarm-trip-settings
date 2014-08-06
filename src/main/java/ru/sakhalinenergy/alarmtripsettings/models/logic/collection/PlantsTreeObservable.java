package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import java.util.Set;


/**
 * Интерфейс модели дерева ассетов для использования модели представлениями. 
 * Допускает только использование геттерорв.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface PlantsTreeObservable 
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
     * Возвращает коллекцию ассетов модели.
     * 
     * @return Коллекцию ассетов модели
     */
    public Set<Plant> getPlants();
    
}//AssetsTreeObservable
