package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;


/**
 * Реализует модель для работы с коллекцией масок (регулярных выраженийц) для
 * парсинга имен тагов.
 *
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public class TagMasks extends Model implements TagMasksObservable
{
    private List<TagMask> masks;
    
    
    /**
     * Возвращает коллекцию масок имен тагов текущего экземпляра модели.
     * 
     * @return Коллекцию масок
     */
    @Override
    public List<TagMask> getMasks()
    {
        return masks;
    }//getMasks
    
        
    /**
     * Создает нить для получения коллекции масок из базы данных.
     */
    public void fetch()
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread tagMasksReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {        
                Session session = null;
                                
                try 
                {
                    session = HibernateUtil.getSessionFactory().openSession();

                    Criteria criteria = session.createCriteria(TagMask.class);
                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

                    masks = criteria.list();

                } catch (HibernateException exception) {

                    _invokeExceptionInEdt("Getting tag masks error", exception, WorkerThread.Event.ERROR);

                } finally {

                    if (session != null && session.isOpen()) session.close();
                }//finally
                
                return new HashMap();
            }//doInBackground
        };//WorkerThread
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(CollectionEvent.THREAD_ERROR) != null) tagMasksReader.events.on(WorkerThread.Event.ERROR, events.get(CollectionEvent.THREAD_ERROR));
        if (events.get(CollectionEvent.THREAD_WARNING) != null) tagMasksReader.events.on(WorkerThread.Event.WARNING, events.get(CollectionEvent.THREAD_WARNING));
        if (events.get(CollectionEvent.THREAD_PROGRESS) != null) tagMasksReader.events.on(WorkerThread.Event.PROGRESS, events.get(CollectionEvent.THREAD_PROGRESS));
        if (events.get(CollectionEvent.MASKS_READ) != null) tagMasksReader.events.on(WorkerThread.Event.WORK_DONE, events.get(CollectionEvent.MASKS_READ));
        
        tagMasksReader.execute();
    }//fetch
}//TagMasks
