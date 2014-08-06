package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;


/**
 * Интерфейс для управления моделью источника данных тагов контролллером. 
 * Дополняет интерфейс для представлений доступом к сеттерам и методам получения 
 * данных.
 * 
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public interface TagsSourceControllable extends TagsSourceObservable
{
   
    /**
     * Метод устанавливает значение текущей сущности ассета модели и рассылает 
     * всем подписчикам соответвующее событие.
     * 
     * @param plant Код ассета
     */
    public void setPlant(Plant plant);
    
    
    /**
     * Устанавливает маску (регулярное выражение) для парсинга имен тага.
     * 
     * @param mask Маска для парсинга имен тага
     */
    public void setTagMask(TagMask mask);
    
    
    /**
     * Добавляет или обнорвляет свойство источника данных.
     * 
     * @param property Свойство источника
     */
    public void setProperty(SourceProperty property);
    
    
    /**
     * Метод пытается разобрать полученное имя тага по заданной маске, и, если
     * это удалось, создает соответствующие экземпляры тага и контура и 
     * связывает их с текущим источником.
     * 
     * @param tagName Имя тага
     * @param mask Маска, по которой будет разбираться имя тага
     * @return Ссылку на созданный таг
     */
    public Tag addTag(String tagName) throws Exception;
    
    
     /**
     * Метод удаляет заданный таг из коллекции тагов источника.
     * 
     * @param tag Ссылка на таг, который необходимо удалить
     */
    public void removeTag(Tag tag);
    
    
    /**
     * Метод добавляет настройку заданного тага.
     * 
     * @param tag Ссылка на таг, настройку котрого добавляем
     * @param setting Экземпляр настройки, которую добавляем
     */
    public void addTagSetting(Tag tag, TagSetting setting);
    
    
    /**
     * Updates particular existing tag setting value, is new value passes 
     * emptiness checking. If value is updated, triggers TAG_SET_UPDATED_EVENT.
     * 
     * @param setting Tag setting instance
     * @param newValue New value to be set
     */
    public void updateTagSettingValue(TagSetting setting, String newValue);

    
    /**
     * Removes given settings from its tag settings collection. 
     * 
     * @param settingToRemove Setting to be removed instance
     */
    public void removeTagSetting(TagSetting settingToRemove);
    
    
    /**
     * Add property to particular tag setting, if given property value passes
     * checking for an emptiness.
     * 
     * @param setting Tag setting to which a new property will added
     * @param property A new property which will be added
     */
    public void addTagSettingProperty(TagSetting setting, TagSettingProperty property);
    
    
    /**
     * Updates given property value if new value passes checking for emptiness.
     * 
     * @param property Property instance for which new value will be set
     * @param newValue New value for given property
     */
    public void updateTagSettingPropertyValue(TagSettingProperty property, String newValue);
    
    
    /**
     * Removes tag setting property from setting properties collection.
     * 
     * @param propertyToRemove Tag setting property to be remove
     */
    public void removeTagSettingProperty(TagSettingProperty propertyToRemove);
    
    
    /**
     * Creates and launches a thread for saving data source to storage and 
     * handles tread events.
     * 
     * @param createLoopsIfNotExist Flag defines a necessity of creations new loops and string tags within new loop 
     */
    public void save(boolean createLoopsIfNotExist);
    
}//TagsSourceControllable