package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;

import java.util.List;
import java.util.ArrayList;


/**
 * @author   Denis.Udovenko
 * @version  1.0.0
 */
public class TreeArea 
{   
    private final String plantId;
    private final String name;
    private List<TreeUnit> units = new ArrayList();
    
    
    /**
     * Конструктор класса. 
     */
    public TreeArea(String plantId, String name)
    {
        this.plantId = plantId;
        this.name = name;
    }//Area
    
    
    /**
     * Метод перегружает родительский. Возвращает струку, которая будет 
     * выводится в названиях узлов дерева ассетов.
     * 
     * @return  String
     */
    @Override
    public String toString()
    {
        return "Area " + this.name;
    }//toString
    
    
    /**
     * Метод добавляет юнит в список зоны.
     * 
     * @param   unit  Экземпляр добавляемого юнита.
     * @return  void
     */
    public void addUnit(TreeUnit unit)
    {
        this.units.add(unit);
    }//addUnit
    
    
    /**
     * Метод возвращает название зоны. 
     * 
     * @return  String
     */
    public String getName()
    {
        return this.name;
    }//getName
    
    
    /**
     * Метод возвращает id ассета, к которому притнадлежит зона.
     * 
     * @return  String
     */
    public String getPlant()
    {
        return this.plantId;
    }//getPlant
    
    
    /**
     * Метод возвращает список юнитов зоны. 
     * 
     * @return  List<Unit>
     */
    public List<TreeUnit> getUnits()
    {
        return this.units;
    }//getUnits
}//Area
