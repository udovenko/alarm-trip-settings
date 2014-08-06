package ru.sakhalinenergy.alarmtripsettings.models.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;


/**
 * Implements plant entity used by Hibernate framework.
 *  
 * @author Denis Udovenko
 * @version 1.0.1
 */
@Entity
@Table(name="PLANTS")
public class Plant 
{
    private String id;
    private String name;
    private List<TreeArea> areas = new ArrayList();
    
    
    /**
     * Required constructor.
     */
    public Plant(){}

    
    /**
     * Overrides parent "toString()" method. Returns plant identifier and name 
     * instead.
     * 
     * @return Plant identifier and name
     */
    @Override
    public String toString()
    {
        return id + " " + name;
    }// toString
    
    
    /**
     * Returns plant identifier.
     * 
     * @return Plant identifier
     */
    @Id
    @Column(name="id")
    public String getId() 
    {
        return id;
    }// getId

    
    /**
     * Sets up plant identifier.
     * 
     * @param id Plant identifier
     */
    public void setId(String id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Returns plant name.
     * 
     * @return Plant name
     */
    @Column(name="name")
    public String getName() 
    {
        return name;
    }// getName

    
    /**
     * Sets up plant name.
     * 
     * @param name Plant name
     */
    public void setName(String name) 
    {
        this.name = name;
    }// setName

    
    /**
     * Returns plant areas list.
     * 
     * @return Plant areas list
     */
    @Transient
    public List<TreeArea> getAreas() 
    {
        return areas;
    }// getAreas
    

    /**
     * Sets up plant areas list.
     * 
     * @param areas Plant areas list
     */
    public void setAreas(List<TreeArea> areas) 
    {
        this.areas = areas;
    }// setAreas
    
    
    /**
     * Adds area to current plant areas collection.
     * 
     * @param araea Area to be added
     */
    public void addArea(TreeArea araea)
    {
        areas.add(araea);
    }// addArea
}// Plant
