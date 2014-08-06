package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;

/**
 * @author   Denis.Udovenko
 * @version  1.0.0
 */
public class TreeUnit
{
    private final String plantId;
    private final String area;
    private final String name;
    
    
    /**
     * Конструктор класса. 
     */
    public TreeUnit(String plantId, String area, String name)
    {
        this.plantId = plantId;
        this.area = area;
        this.name = name;
    }//Unit
    
    
    /**
     * Метод перегружает родительский. Возвращает струку, которая будет 
     * выводится в названиях узлов дерева ассетов.
     * 
     * @return  String
     */
    @Override
    public String toString()
    {
        return "Unit " + this.name;
    }//toString
    
    
    /**
     * Метод возвращает код текущего юнита.
     * 
     * @return  String
     */
    public String getName()
    {
        return this.name;
    }//getName
    
    
    /**
     * Метод возвращает код ассета, к которому принадлежит юнит.
     * 
     * @return  String
     */
    public String getPlant()
    {
        return this.plantId;
    }//getPlant
    
    
    /**
     * Метод возвращает код зоны, к которой отнесен юнит.
     *
     * @return  String
     */
    public String getArea()
    {
        return this.area;
    }//getArea()
}//Unit
