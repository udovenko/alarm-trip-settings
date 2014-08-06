package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.HashMap;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import org.hibernate.Session;


/**
 * Implements logic to work with plants collection.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class PlantsLogic extends Model implements PlantsLogicObservable
{
    private List<Plant> plants;
    
    
    /**
     * Returns current plants list.
     * 
     * @return Plants list
     */
    public List<Plant> getPlants()
    {
        return plants;
    }// getPlants
    
    
    /**
     * Creates a thread for fetching plants collection. Subscribes models events
     * listeners on thread events and executes it.
     */
    public void fetch()
    {
        // Create a thread:
        WorkerThread plantsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                Session session = null;
                
                try 
                {
                    // Fetch plants collection:
                    session = HibernateUtil.getSessionFactory().openSession();
                    plants = session.createCriteria(Plant.class).list();
                
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Fetching plants collection error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }// finally
                
                return new HashMap();
            }// doInBackground
        };// plantsReader
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(plantsReader, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.PLANTS_READ);
        
        // Execute thread:
        plantsReader.execute();
    }// fetch
}// PlantsLogic
