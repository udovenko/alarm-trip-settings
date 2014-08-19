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
 * Implements logic to work with loops collection of selected plants tree 
 * object.
 * 
 * @author Denis Udovenko
 * @version 1.0.6
 */
public class LoopsTable extends Model implements LoopsTableObservable
{
    private List<Loop> loops;
    private List<SettingsSelector> wrappedLoops;
    
    
    /**
     * Returns current loops list.
     * 
     * @return Loops list
     */
    @Override
    public List<Loop> getLoops()
    {
        return loops;
    }// getLoops
    
    
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
            }// for
        }// if
        
        return wrappedLoops;
    }// getWrappedLoops
    
    
    /**
     * Returns a list of data sources which contain tags form current loops 
     * collection.
     * 
     * @return Data sources list
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
            }// for
        }// for
        
        return sources;
    }// getSources
    
    
    /**
     * Returns tags which belongs to data source with given identifier.
     * 
     * @param sourceId Parent data source identifier
     * @return Tags which belongs to data source
     */
    public Set<Tag> getTagsBySourceId(int sourceId)
    {
        Set<Tag> sourceTags = new LinkedHashSet();
        
        for (Loop tempLoop : loops)
        {
            for (Tag tempTag : tempLoop.getTags())
            {
                if (tempTag.getSource().getId() == sourceId) sourceTags.add(tempTag);
            }// for
        }// for
        
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
     * Creates a thread for fetching loops collection of selected plants tree 
     * object. Subscribes models events listeners on thread events and executes
     * it.
     * 
     * @param filters Loops collection fetching criteria filters array 
     */
    public void fetch(final String[] filters)
    {
        // Create a thread:
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
                    
                    // Force lazy initialozation of tags collections for each loop:
                    int loopsProcessed = 0, loopsTotal = loops.size();
                    for (Loop tempLoop : loops)
                    {
                        Hibernate.initialize(tempLoop.getTags());

                        // Publish progress:
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, (int)((double)loopsProcessed / (double)loopsTotal * 100));
                        publish(progress);
                        
                        loopsProcessed++;
                    }// for
                                   
                } catch (HibernateException exception) {
            
                    _invokeExceptionInEdt("Loops collection fetching error", exception, WorkerThread.Event.ERROR);
                
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }// finally
                                
                return new HashMap();
            }// doInBackground
        };// WorkerThread

        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(loopsReader, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.LOOPS_READ);
        
        // Execute thread:
        loopsReader.execute();
    }// fetch
    
    
    /**
     * Creates a thread for loop split. Subscribes model's events listeners on 
     * thread events and executes it.
     * 
     * @param tagsToSeparate Tags to separate into new loop
     */
    public void splitLoop(final List<Tag> tagsToSeparate)
    {
        // Create a thread for loop split:
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
                        Exception exception = new Exception("Tags do not belong to same loop");
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
                        initialLoop.getTags().remove(tempTag);
                        tempTag.setLoop(newLoop);
                        newLoop.getTags().add(tempTag);
                    }// for
                    
                    session.save(newLoop);

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
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(loopSplitter, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.LOOP_SPLIT);

        // Execute thread:
        loopSplitter.execute();
    }// splitLoop
    
    
    /**
     * Creates a thread for merging given loops list. Subscribes model's events 
     * listeners on thread events and executes it.
     * 
     * @param loops Loop instances which will be merged with their copies
     */
    public void mergeLoops(final List<Loop> loops)
    {
        // Create a thread for loop split:
        WorkerThread loopsMerger = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                // Check that given loops is really split:
                for (Loop tempLoop : loops)
                {
                    if (!isLoopSplit(tempLoop))
                    {
                        Exception exception = new Exception("One or more of given loops is not spit and can't be merged");
                        _invokeExceptionInEdt("Loop can't be merged error", exception, WorkerThread.Event.ERROR);
                        return new HashMap();
                    }// if
                }// for
                
                Session session = null;
                List<Loop> loopCopies;
                HashMap<ProgressInfoKey, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, " ");    
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
                
                try
                {
                    // Publish current progress:
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Reassigning tags");
                    progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 10);
                    publish(progress);
                    
                    // Open Hibernate session and begin transaction:
                    session = HibernateUtil.getSessionFactory().openSession();
                    session.beginTransaction();
                    
                    // Iterate given loops list:
                    for (Loop tempLoop: loops)
                    {    
                        // Get current loop's copies list:
                        loopCopies = session.createCriteria(Loop.class)
                            .add(Restrictions.eq("plant", tempLoop.getPlant()))
                            .add(Restrictions.eq("area", tempLoop.getArea()))
                            .add(Restrictions.eq("unit", tempLoop.getUnit()))
                            .add(Restrictions.eq("measuredVariable", tempLoop.getMeasuredVariable()))
                            .add(Restrictions.eq("uniqueIndex", tempLoop.getUniqueIndex()))
                            .add(Restrictions.eq("suffix", tempLoop.getSuffix()))
                            .list();
                        
                        // Collect all tags from loop copies:
                        for (Loop tempCopyLoop : loopCopies)
                        {
                            // Update parent loop for each collected tag:
                            if (tempCopyLoop.getId() != tempLoop.getId())
                            {
                                for (Tag tempTag : tempCopyLoop.getTags())
                                {
                                    tempTag.setLoop(tempLoop);
                                    tempLoop.getTags().add(tempTag);
                                }// for
                            }// if    
                        }// for    
                        
                        // Publish current progress:
                        progress.put(ProgressInfoKey.CYCLE_CAPTION, "Removing obsolete loops");
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 55);
                        publish(progress);
                    
                        // Remove empty loops:
                        String hql = "DELETE FROM Loop loop_table WHERE (SELECT COUNT(id) FROM Tag WHERE loop_id = loop_table.id) = 0";
                        session.createQuery(hql).executeUpdate();
                        
                        // Publish current progress:
                        progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 100);
                        publish(progress);
                    }// for
                    
                    session.flush();
                    
                    session.getTransaction().commit();
                    session.close();
                    
                } catch (Exception exception) {
                    
                    _invokeExceptionInEdt("Loops merging error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }// finally 
                
                return new HashMap();
            }// doInBackground
        };// loopsMerger 
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(loopsMerger, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.LOOPS_MERGED);

        // Execute thread:
        loopsMerger.execute();
    }// mergeLoop
}// LoopsTable
