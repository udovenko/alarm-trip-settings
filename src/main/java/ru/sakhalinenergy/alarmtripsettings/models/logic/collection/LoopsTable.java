package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.СoincidenceSettingsSelectior;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;


/**
 * Реализует логику для работы с коллекцией контуров для выбранного объекта 
 * иерархии дерева ассетов.
 * 
 * @author Denis.Udovenko
 * @version 1.0.5
 */
public class LoopsTable extends Model implements LoopsTableObservable, LoopsTableControllable
{
    private List<Loop> loops;
    private List<SettingsSelector> wrappedLoops;
    
    
    /**
     * Возвращает текущую коллекцию контуров модели.
     * 
     * @return Текущую коллекцию контуров модели
     */
    @Override
    public List<Loop> getLoops()
    {
        return loops;
    }//getLoops
    
    
    /**
     * Wraps each loop of current collection into setting selector wrapper if 
     * wrapped loops list was not initialized yet. Returns a list of wrapped 
     * loops.
     * 
     * @return list of loops wrapped into settings selection logic
     */
    public List<SettingsSelector> getWrappedLoops()
    {
        if (wrappedLoops == null)
        {
            wrappedLoops = new ArrayList();
            SettingsSelector tempWrapper;

            for (Loop tempLoop : loops)
            {
                tempWrapper = new СoincidenceSettingsSelectior(tempLoop);
                wrappedLoops.add(tempWrapper);
            }//for
        }// if
        
        return wrappedLoops;
    }//getWrappedLoops
    
    
    /**
     * Возвращает набор источников данных тагов текущей коллекции контуров.
     * 
     * @return Набор источников данных
     */
    @Override
    public List<Source> getSources()
    {
        List<Source> sources = new ArrayList();
        Source tempSource;
        
        for (Loop tempLoop : loops)
        {
            for (Tag tempTag : tempLoop.getTags())
            {
                tempSource = tempTag.getSource();
                if (!sources.contains(tempSource)) sources.add(tempSource);
            }//for
        }//for
        
        return sources;
    }//getSources
    
    
    /**
     * 
     */
    public Set<Tag> getTagsBySourceId(int sourceId)
    {
        Set<Tag> sourceTags = new LinkedHashSet();
        
        for (Loop tempLoop : loops)
        {
            for (Tag tempTag : tempLoop.getTags())
            {
                if (tempTag.getSource().getId() == sourceId) sourceTags.add(tempTag);
            }//for
        }//for
        
        return sourceTags;
    }// getTagsBySourceId
    
    
    /**
     * Checks neighbor loop in the list and returns true if it equals (by its 
     * components names) to given one.
     * 
     * @param loop Loop to be checked for split
     * @return True if loop is split, else false
     */
    public boolean isLoopSplit(Loop loop)
    {
        boolean result = false;
        int loopIndex = loops.indexOf(loop);
        
        if (loopIndex > 0 && loop.equals(loops.get(loopIndex - 1))) result = true;
        if (loopIndex < loops.size() - 1 && loop.equals(loops.get(loopIndex + 1))) result = true;
        
        return result;
    }// isSplit
    
    
    /**
     * Создает нить для получения контуров для выбранного объекта дерва ассетов.
     */
    public void fetch(final String[] filters)
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
        WorkerThread loopsReader = new WorkerThread()
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
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "getting loops collection"); 
                    publish(progress); 
                     
                    session = HibernateUtil.getSessionFactory().openSession();
                    
                    Criteria criteria = session.createCriteria(Loop.class).add(Restrictions.eq("plant", filters[0])).setCacheable(true);
                    if (filters.length > 1) criteria.add(Restrictions.eq("area", filters[1]));
                    if (filters.length > 2) criteria.add(Restrictions.eq("unit", filters[2]));
            
                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    loops = criteria.list();
            
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "loading tag sets for loops");
                    publish(progress);
                    
                    //Вызываем ленивую инициализацию коллекций тагов каждого контура:
                    int loopsProcessed = 0, loopsTotal = loops.size();
                    for (Loop tempLoop : loops)
                    {
                        Hibernate.initialize(tempLoop.getTags());

                        //Публикуем текущий прогресс:
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)loopsProcessed / (double)loopsTotal * 100));
                        publish(progress);
                        
                        loopsProcessed++;
                    }//for
                                   
                } catch (HibernateException exception) {
            
                    _invokeExceptionInEdt("Loops collection fetching error", exception, WorkerThread.Event.ERROR);
                
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }//finally
                                
                return new HashMap();
            }//doInBackground
        };//WorkerThread
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(CollectionEvent.THREAD_ERROR) != null) loopsReader.events.on(WorkerThread.Event.ERROR, events.get(CollectionEvent.THREAD_ERROR));
        if (events.get(CollectionEvent.THREAD_WARNING) != null) loopsReader.events.on(WorkerThread.Event.WARNING, events.get(CollectionEvent.THREAD_WARNING));
        if (events.get(CollectionEvent.THREAD_PROGRESS) != null) loopsReader.events.on(WorkerThread.Event.PROGRESS, events.get(CollectionEvent.THREAD_PROGRESS));
        if (events.get(CollectionEvent.LOOPS_READ) != null) loopsReader.events.on(WorkerThread.Event.WORK_DONE, events.get(CollectionEvent.LOOPS_READ));
        
        loopsReader.execute();
    }//fetch
    
    
    /**
     * Creates a thread for loop split. Copies given tags parent loop, attaches 
     * tags to copy and saves new loop.
     * 
     * @param tagsToSeparate Tags to separate into new loop
     */
    public void splitLoop(final List<Tag> tagsToSeparate)
    {
        // Create an anonymous thread for loop split:
        WorkerThread loopSplitter = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                int pervLoopId = -1;
                for (Tag tempTag : tagsToSeparate)
                {
                    if (pervLoopId != -1 && tempTag.getLoop().getId() != pervLoopId)
                    {
                        Exception exception = new Exception("Tagsdo not belong to same loop");
                        _invokeExceptionInEdt("Invalid tag set error", exception, WorkerThread.Event.ERROR);
                        return new HashMap();
                    }// if
                }// for    

                // Initializing data for loop split:
                Session session = null;
                HashMap<ProgressInfoKey, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, " ");    
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                try // Split loop in database:
                {
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "getting loops collection"); 
                    publish(progress); 
                     
                    // Receive initial parent loop:
                    Loop initialLoop = tagsToSeparate.get(0).getLoop();
                    
                    // Create new loop with same properties:
                    Loop newLoop = new Loop();
                    newLoop.setPlant(initialLoop.getPlant());
                    newLoop.setArea(initialLoop.getArea());
                    newLoop.setUnit(initialLoop.getUnit());
                    newLoop.setMeasuredVariable(initialLoop.getMeasuredVariable());
                    newLoop.setUniqueIndex(initialLoop.getUniqueIndex());
                    newLoop.setSuffix(initialLoop.getSuffix());
                                      
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    
                    // Attach given tags to new loop:
                    for (Tag tempTag : tagsToSeparate) 
                    {    
                        System.out.println(tempTag);
                        //session.merge(tempTag);
                        initialLoop.getTags().remove(tempTag);
                        tempTag.setLoop(newLoop);
                        newLoop.getTags().add(tempTag);
                    }
                    //newLoop.setTags(new HashSet(tagsToSeparate));
                    
                    //session.update(initialLoop);
                    session.save(newLoop);
                    
                    for (Tag tempTag : newLoop.getTags()) 
                    { 
                        //session.save(tempTag);
                    }
                    
                    session.flush();
                    
                    session.getTransaction().commit();
                    session.close();
                    
                } catch (Exception exception) {
                
                    _invokeExceptionInEdt("Loop split error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }// finally 
                
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(CollectionEvent.THREAD_ERROR) != null) loopSplitter.events.on(WorkerThread.Event.ERROR, events.get(CollectionEvent.THREAD_ERROR));
        if (events.get(CollectionEvent.THREAD_WARNING) != null) loopSplitter.events.on(WorkerThread.Event.WARNING, events.get(CollectionEvent.THREAD_WARNING));
        if (events.get(CollectionEvent.THREAD_PROGRESS) != null) loopSplitter.events.on(WorkerThread.Event.PROGRESS, events.get(CollectionEvent.THREAD_PROGRESS));
        if (events.get(CollectionEvent.LOOP_SPLIT) != null) loopSplitter.events.on(WorkerThread.Event.WORK_DONE, events.get(CollectionEvent.LOOP_SPLIT));
        
        loopSplitter.execute();
    }// splitLoop
}// LoopsTable
