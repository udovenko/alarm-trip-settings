package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;


/**
 * Интерфейс для управления коллекцией производственных объектов контролллером. 
 * Дополняет интерфейс для представлений доступом к сеттерам и методам получения данных.
 * 
 * @author Denis.Udovenko
 * @version 1.0.1
 */
public interface PlantsLogicControllable extends PlantsLogicObservable
{
    /**
     * Создает нить для получения коллекции производственных объектов.
     */
    public void fetch();
    
}//PlantsLogicObservable
