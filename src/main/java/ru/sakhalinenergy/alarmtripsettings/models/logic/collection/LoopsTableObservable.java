package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Интерфейс модели таблицы контуров для использования представлениями. 
 * Допускает только использование геттерорв.
 *
 * @author Denis.Udovenko
 * @version 1.0.4
 */
public interface LoopsTableObservable 
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
     * Возвращает текущую коллекцию контуров модели.
     * 
     * @return Текущую коллекцию контуров модели
     */
    public List<Loop> getLoops();
    
    
    /**
     * Wraps each loop of current collection into setting selector wrapper and
     * returns a list of wrapped loops.
     * 
     * @return list of loops wrapped into settings selection logic
     */
    public List<SettingsSelector> getWrappedLoops();
    
    
    /**
     * Checks neighbor loop in the list and returns true if it equals (by its 
     * components names) to given one.
     * 
     * @param loop Loop to be checked for split
     * @return True if loop is split, else false
     */
    public boolean isLoopSplit(Loop loop);
    
    
    /**
     * Возвращает набор источников данных тагов текущей коллекции контуров.
     * 
     * @return Набор источников данных
     */
    public List<Source> getSources();
    
}//LoopsTableObservable
