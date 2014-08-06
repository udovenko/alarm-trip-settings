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
 * Implements logic to work with tag formats (parsing masks) collection.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class TagMasks extends Model implements TagMasksObservable
{
    private List<TagMask> masks;
    
    
    /**
     * Returns current tag masks list.
     * 
     * @return Tag masks list
     */
    @Override
    public List<TagMask> getMasks()
    {
        return masks;
    }// getMasks
    
        
    /**
     * Creates a thread for fetching tag masks collection. Subscribes models 
     * events listeners on thread events and executes it.
     */
    public void fetch()
    {
        // Create thread:
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
                }// finally
                
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(tagMasksReader, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.MASKS_READ);
        
        // Execute thread:
        tagMasksReader.execute();
    }// fetch
}// TagMasks
