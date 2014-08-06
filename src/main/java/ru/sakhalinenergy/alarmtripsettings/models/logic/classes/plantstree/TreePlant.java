package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;

import java.util.List;
import java.util.ArrayList;


/**
 * @author   Denis.Udovenko
 * @version  1.0.0
 */
public class TreePlant
{
    private final String id;
    private List<TreeArea> areas = new ArrayList();
    
    
    /**
     * Конструктор класса.
     */
    public TreePlant(String id)
    {
        this.id = id;
    }//Plant
    
    
    /**
     * Метод перегружает родительский. Возвращает струку, которая будет 
     * выводится в названиях узлов дерева ассетов.
     * 
     * @return  String
     */
    @Override
    public String toString()
    {
        return this.id;
    }//toString
    
    
    /**
     * Метод добавляет зону ассета.
     * 
     * @return  void
     */
    public void addArea(TreeArea area)
    {
        this.areas.add(area);
    }//addArea
    
    
    /**
     * Метод возвращает код ассета. 
     * 
     * @return  String
     */
    public String getId()
    {
        return this.id;
    }//getId
    
    
    /**
     * Метод возвращает список зон ассета.
     * 
     * @return  List<Area>
     */
    public List<TreeArea> getAreas()
    {
        return this.areas;
    }//getAreas
}//Plant
