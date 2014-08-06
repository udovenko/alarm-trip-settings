package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;


/**
 * Implements plants (assets) tree logic.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class PlantsTree extends Model implements PlantsTreeObservable
{
    private final Set<Plant> plants = new LinkedHashSet();
    
    
    /**
     * Returns tree plants list.
     * 
     * @return Tree plants list
     */
    @Override
    public Set<Plant> getPlants()
    {
        return plants;
    }// getPlants
    
    
    /**
     * Creates a thread for fetching loops with unique Plant/Area/Unit 
     * combinations and creating plants tree. Subscribes models events listeners
     * on thread events and executes it.
     */
    public void fetch()
    {
        // Create a thread:
        WorkerThread treeReader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                Session session = null;
                List<Loop> loopsWithUniquePAU = new ArrayList<Loop>();
                HashMap<ProgressInfoKey, Object> progress = new HashMap();
                progress.put(ProgressInfoKey.CYCLE_CAPTION, " ");    
                progress.put(ProgressInfoKey.CYCLE_PERCENTAGE, 0);
        
                try 
                {
                    progress.put(ProgressInfoKey.CYCLE_CAPTION, "Getting PAU tree from storage"); 
                    publish(progress);
                    
                    session = HibernateUtil.getSessionFactory().openSession();

                    ProjectionList projectionList = Projections.projectionList();
                    projectionList.add(Projections.property("plant"), "plant");
                    projectionList.add(Projections.property("area"), "area");
                    projectionList.add(Projections.property("unit"), "unit");
            
                    loopsWithUniquePAU = session.createCriteria(Loop.class)
                        .setProjection(Projections.distinct(projectionList))
                        .setResultTransformer(Transformers.aliasToBean(Loop.class))
                        .list();
                    
                    // If request is empty - return an empty hash:
                    if (loopsWithUniquePAU.isEmpty()) return new HashMap(); 
                
                    String currentPlantCode, previousPlantCode = null, 
                        currentAreaCode = null, previousAreaCode = null;
                
                    Plant currentPlant = null;
                    TreeArea currentArea = null;
                    TreeUnit tempUnit;
                
                    // Loop through received loops and build plants tree:
                    for (Loop tempLoop : loopsWithUniquePAU) 
                    {
                        currentPlantCode = tempLoop.getPlant();
                        currentAreaCode = tempLoop.getArea();
                   
                        // If previous loop's plant code does not much current one:
                        if (previousPlantCode == null || !previousPlantCode.equals(currentPlantCode))
                        {
                            previousPlantCode = currentPlantCode;
                            previousAreaCode = null;
                            currentPlant = (Plant)session.get(Plant.class, currentPlantCode);
                            
                            // If plant name is in database, add it to collection:
                            if (currentPlant != null) plants.add(currentPlant);
                            else {
                                
                                // If plant has no matched name in database, add new plant with "(unknown)" name:
                                Plant newEmptyPlant = new Plant();
                                newEmptyPlant.setId(currentPlantCode);
                                newEmptyPlant.setName("(unknown)");
                                plants.add(newEmptyPlant);
                                currentPlant = newEmptyPlant;
                            }// else    
                        }// if
     
                        // If previous loop's area code does not mutch current one:
                        if (previousAreaCode == null || !previousAreaCode.equals(currentAreaCode))
                        {
                            previousAreaCode = currentAreaCode;
                            currentArea = new TreeArea(currentPlantCode, currentAreaCode);
                            currentPlant.addArea(currentArea);
                        }// if
                    
                        // Add unit to current area:
                        tempUnit = new TreeUnit(currentPlantCode, currentAreaCode, tempLoop.getUnit());
                        currentArea.addUnit(tempUnit);
                    }// for
                    
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Building PAU tree error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }// finally
                
                return new HashMap();
            }// doInBackground
        };// WorkerThread
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(treeReader, CollectionEvent.THREAD_PROGRESS, 
            CollectionEvent.THREAD_WARNING, CollectionEvent.THREAD_ERROR, CollectionEvent.TREE_READ);
        
        // Execute thread:
        treeReader.execute();
    }// fetch
}// PlantsTree
