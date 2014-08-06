package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;


/**
 * Интерфейс модели источника данных тагов для использования представлениями. 
 * Допускает только использование геттерорв.
 *
 * @author Denis.Udovenko
 * @version 1.0.4
 */
public interface TagsSourceObservable extends ModelObservable
{
   
    /**
     * Метод возвращает текущую сущность ассета модели.
     * 
     * @return Текущий код ассета
     */
    public Plant getPlant();
    
    
    /**
     * Возвращает текущую сущность источника данных тагов. Внимание: установка
     * свойств сущности напрямую не будет вызывать рассылку событий для 
     * представлений и контроллеров!
     * 
     * @return Cущность источника данных тагов
     */
    public Source getEntity();

    
    /**
     * Возвращает коллекцию тагов, отсортированных по имени.
     * 
     * @return Коллекцию тагов, отсортированных по имени
     */
    public List<Tag> getSortedTags();
    
}//TagsSourceObservable
