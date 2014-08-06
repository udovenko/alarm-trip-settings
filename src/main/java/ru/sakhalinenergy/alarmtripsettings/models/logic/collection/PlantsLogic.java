package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.HashMap;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import org.hibernate.Session;


/**
 * Реализует логику для работы с коллекцией производственных объектов.
 *
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public class PlantsLogic extends Model implements PlantsLogicObservable, PlantsLogicControllable
{
    private List<Plant> plants;
    
    
    /**
     * Возвращает коллекцию производственных объектов.
     * 
     * @return Коллекцию производственных объектов
     */
    public List<Plant> getPlants()
    {
        return plants;
    }//getPlants
    
    
    /**
     * Создает и запускает нить для получения коллекции производственных объектов.
     */
    @Override
    public void fetch()
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread plantsReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                Session session = null;
                
                try 
                {
                    session = HibernateUtil.getSessionFactory().openSession();
                    plants = session.createCriteria(Plant.class).list();
                
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Fetching plants collection error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }//finally
                
                return new HashMap();
            }//doInBackground
        };//plantsReader
        
        // Подписываем подписчиков модели на события нити:
        if (events.get(CollectionEvent.THREAD_ERROR) != null) plantsReader.events.on(WorkerThread.Event.ERROR, events.get(CollectionEvent.THREAD_ERROR));
        if (events.get(CollectionEvent.THREAD_WARNING) != null) plantsReader.events.on(WorkerThread.Event.WARNING, events.get(CollectionEvent.THREAD_WARNING));
        if (events.get(CollectionEvent.THREAD_PROGRESS) != null) plantsReader.events.on(WorkerThread.Event.PROGRESS, events.get(CollectionEvent.THREAD_PROGRESS));
        if (events.get(CollectionEvent.PLANTS_READ) != null) plantsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(CollectionEvent.PLANTS_READ));
        
        plantsReader.execute();
    }//fetch
}//PlantsLogic
