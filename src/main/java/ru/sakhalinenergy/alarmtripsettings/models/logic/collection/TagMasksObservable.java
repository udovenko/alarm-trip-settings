package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;


/**
 * Интерфейс модели коллекеции масок имен тагов для использования представлениями. 
 * Допускает только использование геттерорв.
 *
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public interface TagMasksObservable extends EventsObservable
{

    /**
     * Возвращает коллекцию масок имен тагов текущего экземпляра модели.
     * 
     * @return Коллекцию масок
     */
    public List<TagMask> getMasks();
    
}//TagMasksObservable
