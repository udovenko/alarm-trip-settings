package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import java.util.ArrayList;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.transform.Transformers;


/**
 * Модель дерева ассетов.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class PlantsTree extends Model implements PlantsTreeObservable, PlantsTreeControllable
{
    private final Set<Plant> plants = new LinkedHashSet();
    
    
    /**
     * Возвращает коллекцию ассетов модели.
     * 
     * @return Коллекцию ассетов модели
     */
    @Override
    public Set<Plant> getPlants()
    {
        return plants;
    }//getPlants
    
    
    /**
     * Создает нить для получения контуров с уникальными наборами элементов
     * структуры Plant - Area - Unit и построения на их базе дерва ассетов.
     */
    public void fetch()
    {
        //Создаем анонимную нить для создания коллекции тагов из текущего листа книги MS Excel:
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
                    
                    //Если запрос не вернул результатов - завершаем работу нити:
                    if (loopsWithUniquePAU.isEmpty()) return new HashMap(); 
                
                    String currentPlantCode, previousPlantCode = null, 
                        currentAreaCode = null, previousAreaCode = null;
                
                    Plant currentPlant = null;
                    TreeArea currentArea = null;
                    TreeUnit tempUnit;
                
                    //Обходим полученные контуры и строим дерево производственных объектов:
                    for (Loop tempLoop : loopsWithUniquePAU) 
                    {
                        currentPlantCode = tempLoop.getPlant();
                        currentAreaCode = tempLoop.getArea();
                   
                        //Если код ассета предыдущего контура не совпадает с текущим:
                        if (previousPlantCode == null || !previousPlantCode.equals(currentPlantCode))
                        {
                            previousPlantCode = currentPlantCode;
                            previousAreaCode = null;
                            currentPlant = (Plant)session.get(Plant.class, currentPlantCode);
                            
                            //Если ассет есть в базе, добаволяем его в коллекцию:
                            if (currentPlant != null) plants.add(currentPlant);
                            else {
                                
                                //Если ассета нет в базе, добавляем в коллекцию пустой:
                                Plant newEmptyPlant = new Plant();
                                newEmptyPlant.setId(currentPlantCode);
                                newEmptyPlant.setName("(unknown)");
                                plants.add(newEmptyPlant);
                                currentPlant = newEmptyPlant;
                            }//else    
                        }//if
     
                        //Если код зоны предыдущего контура не сопадает с текущим:
                        if (previousAreaCode == null || !previousAreaCode.equals(currentAreaCode))
                        {
                            previousAreaCode = currentAreaCode;
                            currentArea = new TreeArea(currentPlantCode, currentAreaCode);
                            currentPlant.addArea(currentArea);
                        }//if
                    
                        //Добавляем юнит к текущей зоне:
                        tempUnit = new TreeUnit(currentPlantCode, currentAreaCode, tempLoop.getUnit());
                        currentArea.addUnit(tempUnit);
                    }//for
                    
                } catch (Exception exception) {
            
                    _invokeExceptionInEdt("Building PAU tree error", exception, WorkerThread.Event.ERROR);
                } finally {
            
                    if (session != null && session.isOpen()) session.close();
                }//finally
                
                return new HashMap();
            }//doInBackground
        };//WorkerThread
        
        //Подписываем подписчиков модели на события нити:
        if (events.get(CollectionEvent.THREAD_ERROR) != null) treeReader.events.on(WorkerThread.Event.ERROR, events.get(CollectionEvent.THREAD_ERROR));
        if (events.get(CollectionEvent.THREAD_WARNING) != null) treeReader.events.on(WorkerThread.Event.WARNING, events.get(CollectionEvent.THREAD_WARNING));
        if (events.get(CollectionEvent.THREAD_PROGRESS) != null) treeReader.events.on(WorkerThread.Event.PROGRESS, events.get(CollectionEvent.THREAD_PROGRESS));
        if (events.get(CollectionEvent.TREE_READ) != null) treeReader.events.on(WorkerThread.Event.WORK_DONE, events.get(CollectionEvent.TREE_READ));
        
        treeReader.execute();
    }//fetch
}//AssetsTree
