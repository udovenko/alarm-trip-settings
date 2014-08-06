package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;


/**
 * Реализует логику для работы с набором тагов и контуров в рамках отдельного 
 * источника данных.
 *
 * @author Denis.Udovenko
 * @version 1.0.5
 */
public class TagsSource extends Model implements TagsSourceObservable, TagsSourceControllable
{
    // Value matched this regexp will considered as empty:
    private static final String EMPTY_VALUE_SYNONYMS = "[-*]|^$";
    
    protected Source source;
    protected Plant plant;
    protected TagMask tagMask;
    protected final Set<Tag> tagsToDelete = new HashSet();
        
    
    /**
     * Конструктор класса.
     * 
     * @param source Экземпляр источника данных, который будет обернут в логику текущего класса
     */
    public TagsSource(Source source)
    {
        this.source = source;
    }//TagsSource
    
    
    /**
     * Возвращает текущую сущность источника данных тагов. Внимание: установка
     * свойств сущности напрямую не будет вызывать рассылку событий для 
     * представлений и контроллеров!
     * 
     * @return Cущность источника данных тагов
     */
    @Override
    public Source getEntity()
    {
        return source;
    }//getEntity
    
    
    /**
     * Метод возвращает текущую сущность ассета модели.
     * 
     * @return Текущий код ассета
     */
    @Override
    public Plant getPlant()
    {
        return plant;
    }//getPlantCode
    
    
    /**
     * Возвращает коллекцию тагов, отсортированных по имени.
     * 
     * @return Коллекцию тагов, отсортированных по имени
     */
    @Override
    public List<Tag> getSortedTags()
    {
        List sortedTags = new ArrayList(source.getTags());
        
        //Сортируем коллекуию по типу настроек:
        Collections.sort(sortedTags, new Comparator<Tag>()
        {
            @Override
            public int compare(Tag tagOne, Tag tagTwo)
            {
                return tagOne.getName().compareTo(tagTwo.getName());
            }//compare
        });//sort
        
        return sortedTags;
    }//getSortedTags
    
    
    /**
     * Метод устанавливает значение текущей сущности ассета модели и рассылает 
     * всем подписчикам соответвующее событие.
     * 
     * @param plant Код ассета
     */
    @Override
    public void setPlant(Plant plant)
    {
        this.plant = plant;
        CustomEvent bookConnectedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.PLANT_CODE_SET, bookConnectedEvent);
    }//setPlantCode
    
    
    /**
     * Устанавливает маску (регулярное выражение) для парсинга имен тага.
     * 
     * @param mask Маска для парсинга имен тага
     */
    @Override
    public void setTagMask(TagMask mask)
    {
        tagMask = mask;
    }//setTagMask
    
    
    /**
     * Добавляет или обнорвляет свойство источника данных.
     * 
     * @param property Свойство источника
     */
    @Override
    public void setProperty(SourceProperty property)
    {
        //If data source already has property with same type, just updatind its value:
        for (SourceProperty tempProperty : source.getProperties())
        {
            if (tempProperty.getTypeId() == property.getTypeId())
            {
                tempProperty.setValue(property.getValue());
                return;
            }//if
        }//for
        
        //If property with same type does not exist, adding new one:
        source.getProperties().add(property);
        property.setSource(source);
    }//setProperty
       
    
    /**
     * Пытается разобрать полученное имя тага по заданной маске, и, если
     * это удалось, создает соответствующие экземпляры тага и контура и 
     * связывает их с текущим источником.
     * 
     * @param tagName Имя тага
     * @param mask Маска, по которой будет разбираться имя тага
     * @return Ссылку на созданный таг
     */
    @Override
    public Tag addTag(String tagName) throws Exception
    {
        //Checking if current tag mask or plant is null:
        if (tagMask == null || plant == null) throw new Exception("Current plant or tag mask is null");
        
        Pattern pattern = Pattern.compile(tagMask.getMask());
        Matcher matcher = pattern.matcher(tagName);

        //If tag name does not match regexp:
        if (!matcher.matches()) throw new Exception("tag name \"" + tagName + "\" does not match selected mask");
                
        //Triyng to get plant code from tag name:
        String plantCode = null;
        try {
        
            plantCode = matcher.group("plant"); 
        } catch (Exception exception) {}
            
        //If tag name has plant code but it does not match current plant code field:
        if (plantCode != null && !plantCode.equals(plant.getId()))
        {
            //Throwing tag creation error:
            throw new Exception("Plant code in tag name \"" + tagName + "\" does not match selected plant code \"" + plant.getId() + "\"");
        }//if
        
        //Crearting a tag:
        Tag tag = new Tag();
        tag.setName(tagName.replace(" ", ""));
        tag.setModifier(matcher.group("modifier"));
                
        Loop loop = new Loop();
        loop.setPlant(plant.getId());
        
        loop.setArea(matcher.group("area"));
        loop.setUnit(matcher.group("unit"));
        loop.setMeasuredVariable(matcher.group("variable"));
        loop.setUniqueIndex(matcher.group("index"));
            
        String suffix = matcher.group("suffix"); 
        loop.setSuffix(suffix != null ? suffix : "");
            
        Loop existingLoop = _findLoop(loop);
            
        //Если такой контур еще не создан и не является родительским ни для одного из тагов источника:
        if (existingLoop == null)
        {
            tag.setLoop(loop);
            loop.getTags().add(tag);
            
        } else { //Если такой контур уже создан, добавляем таг к существующему контуру:
            
            tag.setLoop(existingLoop);
            existingLoop.getTags().add(tag);
        }//else
            
        tag.setSource(source);
        source.getTags().add(tag);
                        
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(tag);
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
            
        return tag;
    }//addTag
    
    
    /**
     * Метод удаляет заданный таг из коллекции тагов источника.
     * 
     * @param tag Ссылка на таг, который необходимо удалить
     */
    @Override
    public void removeTag(Tag tag)
    {
        source.getTags().remove(tag);
        tag.setSource(null);
        
        tag.getLoop().getTags().remove(tag);
        tag.setLoop(null);
        
        tagsToDelete.add(tag);
        
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }//removeTag
    
    
    /**
     * Метод добавляет настройку заданного тага.
     * 
     * @param tag Ссылка на таг, настройку котрого добавляем
     * @param setting Экземпляр настройки, которую добавляем
     */
    @Override
    public void addTagSetting(Tag tag, TagSetting setting)
    {
        setting.setValue(setting.getValue().replace(" ", ""));
        if (_isEmptynessSynonim(setting.getValue())) return;
                
        tag.getSettings().add(setting);
        setting.setTag(tag);
                
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }//addTagSetting
    
    
    /**
     * Updates particular existing tag setting value, is new value passes 
     * emptiness checking. If value is updated, triggers TAG_SET_UPDATED_EVENT.
     * 
     * @param setting Tag setting instance
     * @param newValue New value to be set
     */
    @Override
    public void updateTagSettingValue(TagSetting setting, String newValue)
    {
        newValue = newValue.replace(" ", "");
        
        if (!_isEmptynessSynonim(newValue))
        {
            setting.setValue(newValue);
            
            //Сообщаем подписчикам об обновлении данных в наборе тагов:
            CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
            events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
        }//if
    }//updateTagSetting
    
    
    /**
     * Removes given settings from its tag settings collection. 
     * 
     * @param settingToRemove Setting to be removed instance
     */
    @Override
    public void removeTagSetting(TagSetting settingToRemove)
    {
        settingToRemove.getTag().getSettings().remove(settingToRemove);
        settingToRemove.setTag(null);
                
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }//removeTagSetting
    
    
    /**
     * Add property to particular tag setting, if given property value passes
     * checking for an emptiness.
     * 
     * @param setting Tag setting to which a new property will added
     * @param property A new property which will be added
     */
    @Override
    public void addTagSettingProperty(TagSetting setting, TagSettingProperty property)
    {
        property.setValue(property.getValue().replace(" ", ""));
        if (_isEmptynessSynonim(property.getValue())) return;
        
        setting.getProperties().add(property);
        property.setSetting(setting);
                
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }//addTagSettingProperty
      
    
    /**
     * Updates given property value if new value passes checking for emptiness.
     * 
     * @param property Property instance for which new value will be set
     * @param newValue New value for given property
     */
    @Override
    public void updateTagSettingPropertyValue(TagSettingProperty property, String newValue)
    {
        newValue = newValue.replace(" ", "");
        
        if (!_isEmptynessSynonim(newValue))
        {
            property.setValue(newValue);
            
            //Сообщаем подписчикам об обновлении данных в наборе тагов:
            CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
            events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
        }//if
    }//updateTagSettingPropertyValue
    
    
    /**
     * Removes tag setting property from setting properties collection.
     * 
     * @param propertyToRemove Tag setting property to be remove
     */
    @Override
    public void removeTagSettingProperty(TagSettingProperty propertyToRemove)
    {
        propertyToRemove.getSetting().getProperties().remove(propertyToRemove);
        propertyToRemove.setSetting(null);
                
        //Сообщаем подписчикам об обновлении данных в наборе тагов:
        CustomEvent tagSetUpdatedEvent = new CustomEvent(new Object());
        events.trigger(SourceEvent.TAG_SET_UPDATED, tagSetUpdatedEvent);
    }//removeTagSettingProperty
    
    
    /**
     * 
     * 
     */
    private boolean _isEmptynessSynonim(String value)
    {
        //Check if setting value is synonim of nothing:
        Pattern pattern = Pattern.compile(EMPTY_VALUE_SYNONYMS);
        Matcher matcher = pattern.matcher(value);
        if (matcher.matches()) return true;
        
        return false;
    }//checkValueForEmptynessSynonims
    
    
    /**
     * Ищет среди тагов источника родительский контур, совпадающий по всем
     * параметрам заданным и, в случае успеха, возвращает ссылку на него.
     * 
     * @param searchedLoop Экземпляр контура, совпадения с которым ищем среди родительских контуров тагов
     * @return Ссылка на совпадающий контур или null
     */
    private Loop _findLoop(Loop searchedLoop)
    {
        Loop tempLoop;
        
        for (Tag tempTag : source.getTags())
        {
            tempLoop = tempTag.getLoop();
            if (tempLoop.equals(searchedLoop)) return tempLoop;
        }//for
        
        return null;
    }//findLoop
    
    
    /**
     * Создает и запускает нить для инициализиации коллекцию тагов источника и 
     * их пустые родительские контуры для редактирования источника.
     */
    public void initialize()
    {
        //Создаем анонимную нить для инициализации коллекции тагов источника данных:
        WorkerThread sourceInitializer = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                Session session = null;
                HashMap<ProgressInfoKey, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, " ");    
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                try 
                {
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Initiating remaining tags for selected source"); 
                    publish(progress);                    
                    
                    //Создаем сессию и открываем транзакцию:
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                
                    Source mergedSource = (Source)session.merge(source);
                    Hibernate.initialize(mergedSource.getTags());
    
                    for (Tag tempTag : mergedSource.getTags())
                    {
                        Hibernate.initialize(tempTag.getLoop());
                        tempTag.getLoop().setTags(new HashSet());
                    }//for
                    
                    source = mergedSource;          

                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Data source saving error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                return new HashMap();
            }//doInBackground
        };//sourceInitializer
        
         //Подписываем подписчиков модели на события нити:
        if (events.get(SourceEvent.THREAD_ERROR) != null) sourceInitializer.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) sourceInitializer.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) sourceInitializer.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.SOURCE_INITIALIZED) != null) sourceInitializer.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.SOURCE_INITIALIZED));
                 
        sourceInitializer.execute();
    }//initialize
    
    
    /**
     * Creates and launches a thread for saving data source to storage and 
     * handles tread events.
     * 
     * @param createLoopsIfNotExist Flag defines a necessity of creations new loops and string tags within new loop 
     */
    @Override
    public void save(final boolean createLoopsIfNotExist)
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread sourceSaver = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "Saving source tags");
                
                try
                {
                    //Создаем сессию и открываем транзакцию:
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    //Сохраняем источник данных и его свойства:
                    session.saveOrUpdate(source);

                    //Создаем контур для тагов, или относим их к уже соществующему контуру:
                    Loop tempLoop, tempExistingLoop;
                    List<Loop> tempExistingLoops;
                    
                    int tagsProcessed = 0, tagsTotal = source.getTags().size();
                    
                    for (Tag tempTag : source.getTags())
                    {
                        tempLoop = tempTag.getLoop();
                        
                        if (tempTag.getLoop().getId() == 0)
                        {
                            Criteria criteria = session.createCriteria(Loop.class)
                                .add(Restrictions.eq("plant", tempLoop.getPlant()))
                                .add(Restrictions.eq("area", tempLoop.getArea()))
                                .add(Restrictions.eq("unit", tempLoop.getUnit()))
                                .add(Restrictions.eq("measuredVariable", tempLoop.getMeasuredVariable()))
                                .add(Restrictions.eq("uniqueIndex", tempLoop.getUniqueIndex()))
                                .add(Restrictions.eq("suffix", tempLoop.getSuffix()));
                            tempExistingLoops = criteria.list();
                        
                            if (tempExistingLoops.size() > 0) 
                            {    
                                tempExistingLoop = tempExistingLoops.get(0);
                                tempTag.setLoop(tempExistingLoop);
                                tempExistingLoop.getTags().add(tempTag);
                                session.saveOrUpdate(tempExistingLoop);
                                
                            } else if (createLoopsIfNotExist) session.save(tempLoop);
                            
                        } else session.saveOrUpdate(tempTag);
                        
                        //Публикуем текущий прогресс:
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)tagsProcessed / (double)tagsTotal * 90));
                        publish(progress);
                        
                        tagsProcessed++;
                    }//for
                    
                    //Удаляем все таги, отмеченные к удалению:
                    for (Tag tempTag : tagsToDelete)
                    {
                        session.delete(tempTag);
                    }//for

                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing obsolete loops");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 92);
                    publish(progress);
                    
                    //Удаляем все контуры, больше не содержащие ни одного тага:
                    String hql = "DELETE FROM Loop loop_table WHERE (SELECT COUNT(id) FROM Tag WHERE loop_id = loop_table.id) = 0";
                    session.createQuery(hql).executeUpdate();
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Flushing updates");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 97);
                    publish(progress);
                    
                    //Закрываем транзакцию и сессию:
                    session.flush();
                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Data source saving error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                return new HashMap();
            }//doInBackground
        };//WorkerThread
                
        //Подписываем подписчиков модели на события нити:
        if (events.get(SourceEvent.THREAD_ERROR) != null) sourceSaver.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) sourceSaver.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) sourceSaver.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.SOURCE_SAVED) != null) sourceSaver.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.SOURCE_SAVED));
        
        sourceSaver.execute();
    }//save
    
    
    /**
     * Создает и запускает нить для удаления источника данных и всей связанной 
     * с ним информации из базы данных.
     */
    public void remove()
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread sourceRemover = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                HashMap<Enum, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, "  ");
                
                try
                {
                    //Создаем сессию и открываем транзакцию:
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                    publish(progress);
                    
                    //Удаляем источник данных и его свойства:
                    session.delete(source);
                    session.flush();
                           
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 20);
                    publish(progress);
                    
                    //Удаляем все таги, ссылающиеся на несуществующий источник:
                    String hql = "DELETE FROM Tag WHERE source_id NOT IN (SELECT id FROM Source)";
                    session.createQuery(hql).executeUpdate();
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags settings");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 40);
                    publish(progress);
                    
                    //Удаляем все настройки тагов, ссылающиеся на несуществующий таг:
                    hql = "DELETE FROM TagSetting WHERE tag_id NOT IN (SELECT id FROM Tag)";
                    session.createQuery(hql).executeUpdate();
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing source tags settings properties");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 60);
                    publish(progress);
                    
                    //Удаляем все свойства настроек тагов, ссылающиеся на несуществующие настройки:
                    hql = "DELETE FROM TagSettingProperty WHERE setting_id NOT IN (SELECT id FROM TagSetting)";
                    session.createQuery(hql).executeUpdate();
                    
                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing obsolete loops");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 80);
                    publish(progress);
                    
                    //Удаляем все контуры, больше не содержащие ни одного тага:
                    hql = "DELETE FROM Loop loop_table WHERE (SELECT COUNT(id) FROM Tag WHERE loop_id = loop_table.id) = 0";
                    session.createQuery(hql).executeUpdate();

                    //Публикуем текущий прогресс:
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 100);
                    publish(progress);
                    
                    //Закрываем транзакцию и сессию:
                    session.getTransaction().commit();
                    session.close();
                
                } catch (Exception exception) {
                    
                    _invokeExceptionInEdt("Data source removing error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                return new HashMap();
            }//doInBackground
        };//WorkerThread
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(SourceEvent.THREAD_ERROR) != null) sourceRemover.events.on(WorkerThread.Event.ERROR, events.get(SourceEvent.THREAD_ERROR));
        if (events.get(SourceEvent.THREAD_WARNING) != null) sourceRemover.events.on(WorkerThread.Event.WARNING, events.get(SourceEvent.THREAD_WARNING));
        if (events.get(SourceEvent.THREAD_PROGRESS) != null) sourceRemover.events.on(WorkerThread.Event.PROGRESS, events.get(SourceEvent.THREAD_PROGRESS));
        if (events.get(SourceEvent.SOURCE_REMOVED) != null) sourceRemover.events.on(WorkerThread.Event.WORK_DONE, events.get(SourceEvent.SOURCE_REMOVED));
        
        sourceRemover.execute();
    }//save
}//Source
