package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;

import java.util.List;
import java.util.ArrayList;


/**
 * Implements plant area class for plants tree node.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TreeArea 
{   
    private final String plantId;
    private final String name;
    private final List<TreeUnit> units = new ArrayList();
    
    
    /**
     * Public constructor.
     * 
     * @param plantId Plant identifier
     * @param name Area name
     */
    public TreeArea(String plantId, String name)
    {
        this.plantId = plantId;
        this.name = name;
    }// TreeArea
    
    
    /**
     * Overrides parent "toString()" method. Returns area caption and name 
     * string instead.
     * 
     * @return Area caption and name string
     */
    @Override
    public String toString()
    {
        return "Area " + name;
    }// toString
    
    
    /**
     * Add a unit to ara units list.
     * 
     * @param unit Unit to be added
     */
    public void addUnit(TreeUnit unit)
    {
        units.add(unit);
    }// addUnit
    
    
    /**
     * Returns area name.
     * 
     * @return Area name
     */
    public String getName()
    {
        return name;
    }// getName
    
    
    /**
     * Returns area's parent plant.
     * 
     * @return Area's parent plant
     */
    public String getPlant()
    {
        return plantId;
    }// getPlant
    
    
    /**
     * Returns area's units list.
     * 
     * @return Area's units list
     */
    public List<TreeUnit> getUnits()
    {
        return this.units;
    }// getUnits
}// TreeArea
