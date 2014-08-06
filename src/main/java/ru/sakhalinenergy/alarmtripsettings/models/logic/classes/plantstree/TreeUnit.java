package ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree;


/**
 * Implements plant unit class for plants tree node.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TreeUnit
{
    private final String plantId;
    private final String area;
    private final String name;
    
    
    /**
     * Public constructor.
     * 
     * @param plantId Plant identifier
     * @param area Area identifier
     * @param name Unit name
     */
    public TreeUnit(String plantId, String area, String name)
    {
        this.plantId = plantId;
        this.area = area;
        this.name = name;
    }// TreeUnit
    
    
    /**
     * Overrides parent "toString()" method. Returns unit caption and name
     * string instead.
     * 
     * @return Unit caption and name string
     */
    @Override
    public String toString()
    {
        return "Unit " + name;
    }// toString
    
    
    /**
     * Returns unit name (code).
     * 
     * @return Unit name
     */
    public String getName()
    {
        return name;
    }// getName
    
    
    /**
     * Returns unit's parent plant identifier.
     * 
     * @return Unit's parent plant identifier
     */
    public String getPlant()
    {
        return plantId;
    }// getPlant
    
    
    /**
     * Returns unit's parent area identifier.
     *
     * @return Unit's parent area identifier
     */
    public String getArea()
    {
        return area;
    }// getArea
}// TreeUnit
