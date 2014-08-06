package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;

import java.util.List;
import java.util.ArrayList;


/**
 * Implements plant class for plants tree node.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TreePlant
{
    private final String id;
    private final List<TreeArea> areas = new ArrayList();
    
    
    /**
     * Public constructor.
     * 
     * @param id Plant identifier
     */
    public TreePlant(String id)
    {
        this.id = id;
    }// TreePlant
    
    
    /**
     * Overrides parent "toString()" method. Returns plant identifier string 
     * instead.
     * 
     * @return Plant identifier string
     */
    @Override
    public String toString()
    {
        return id;
    }// toString
    
    
    /**
     * Adds area to current plant.
     * 
     * @param area Area to be added
     */
    public void addArea(TreeArea area)
    {
        areas.add(area);
    }// addArea
    
    
    /**
     * Returns plant identifier.
     * 
     * @return Plant identifier
     */
    public String getId()
    {
        return id;
    }// getId
    
    
    /**
     * Returns plant areas list.
     * 
     * @return Plant areas list
     */
    public List<TreeArea> getAreas()
    {
        return this.areas;
    }// getAreas
}// TreePlant
