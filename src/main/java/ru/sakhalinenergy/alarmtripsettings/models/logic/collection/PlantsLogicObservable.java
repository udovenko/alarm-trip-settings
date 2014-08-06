package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;


/**
 * Интерфейс модели коллекции производственных объектов для использования модели
 * представлениями. Допускает только использование геттерорв.
 * 
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public interface PlantsLogicObservable extends EventsObservable
{
   
    /**
     * Возвращает коллекцию производственных объектов.
     * 
     * @return Коллекцию производственных объектов
     */
    public List<Plant> getPlants();
    
}//PlantsLogicObservable
