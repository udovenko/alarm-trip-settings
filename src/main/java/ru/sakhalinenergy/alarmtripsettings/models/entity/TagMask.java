package ru.sakhalinenergy.alarmtripsettings.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * Implements tag mask (format) entity used by Hibernate framework.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
@Entity
@Table(name="TAG_MASKS")
public class TagMask 
{
    private int id;
    private String mask;
    private String example;
    
    
    /**
     * Required constructor.
     */
    public TagMask(){}

    
    /**
     * Returns tag mask identifier.
     * 
     * @return Tag mask identifier
     */
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public int getId()
    {
        return id;
    }// getId

    
    /**
     * Overrides parent "toString()" method. Returns tag mask example string
     * instead.
     * 
     * @return Tag mask example string
     */
    @Override
    public String toString()
    {
        return example;
    }// toString
    
    
    /**
     * Returns entity's mask property.
     * 
     * @return Entity's mask property
     */
    @Column(name="mask")
    public String getMask() 
    {
        return mask;
    }// getMask
    

    /**
     * Returns tag mask example property.
     * 
     * @return Tag mask example property
     */
    @Column(name="example")
    public String getExample() 
    {
        return example;
    }// getExample

    
    /**
     * Sets up tag mask identifier.
     * 
     * @param id Tag mask identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up entity's mask property.
     * 
     * @param mask Entity's mask property
     */
    public void setMask(String mask) 
    {
        this.mask = mask;
    }// setMask

    
    /**
     * Sets up tag mask example property.
     * 
     * @param example Tag mask example property
     */
    public void setExample(String example) 
    {
        this.example = example;
    }// setExample
}// TagMask
