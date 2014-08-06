package ru.sakhalinenergy.alarmtripsettings.models.entity;

import java.util.Date;
import java.util.Set;
import java.util.LinkedHashSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Cascade;


/**
 * Implements tags data source entity used by Hibernate framework.
 * 
 * @author Denis Udovenko 
 * @version 1.0.3
 */
@Entity
@Table(name="SOURCES")
public class Source 
{
    
    private int id;
    private int typeId;
    private int priority;
    private String name;
    private Date date;
    
    private Set<Tag> tags = new LinkedHashSet();
    private Set<SourceProperty> properties = new LinkedHashSet();
    
    @Transient
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    
    /**
     * Required constructor.
     */
    public Source(){}
    
    
    /**
     * Overrides parent "toString()" method. Returns source priority, name 
     * and creation date instead.
     * 
     * @return Plant Source priority, name and creation date
     */
    @Override
    public String toString()
    {
        return "p" + this.priority + " " + this.name + " (" + dateFormat.format(date) + ")";
    }// toString
    
    
    /**
     * Returns data source identifier.
     * 
     * @return Data source identifier
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
     * Returns data source type identifier.
     * 
     * @return Data source type identifier
     */
    @Column(name="type_id")
    public int getTypeId()
    {
        return typeId;
    }// getTypeId
    
    
    /**
     * Returns data source priority.
     * 
     * @return Data source priority
     */
    @Column(name="priority")
    public int getPriority()
    {
        return priority;
    }// getPriority
    
    
    /**
     * Returns data source name.
     * 
     * @return Data source name
     */
    @Column(name="name")
    public String getName()
    {
        return name;
    }// getName
    

    /**
     * Returns data source creation date.
     * 
     * @return Data source creation date
     */
    @Column(name = "date")
    @Type(type="timestamp")
    public Date getDate()
    {
        return date;
    }// getDate
    
    
    /**
     * Returns data source tags set.
     * 
     * @return Data source tags set
     */
    @OneToMany(mappedBy  = "source")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    public Set<Tag> getTags() 
    {
        return tags;
    }// getTags
    
    
    /**
     * Returns data source properties set.
     * 
     * @return Data source properties set
     */
    @OneToMany(mappedBy  = "source", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 10)
    public Set<SourceProperty> getProperties()
    {
        return properties;
    }// getProperties
        
    
    /**
     * Sets up data source properties set.
     * 
     * @param properties Data source properties set
     */
    public void setProperties(Set<SourceProperty> properties)
    {
        this.properties = properties;
    }// setProperties
    
    
    /**
     * Sets up data source priority.
     * 
     * @param priority Data source priority
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }// setPriority

    
    /**
     * Sets up data source identifier.
     * 
     * @param id Source identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up data source type identifier.
     * 
     * @param typeId Data source type identifier
     */
    public void setTypeId(int typeId) 
    {
        this.typeId = typeId;
    }// setTypeId

    
    /**
     * Sets up data source name.
     * 
     * @param name Data source name
     */
    public void setName(String name) 
    {
        this.name = name;
    }// setName
    

    /**
     * Sets up data source creation date.
     * 
     * @param date Data source creation date
     */
    public void setDate(Date date) 
    {
        this.date = date;
    }// setDate
  

    /**
     * Sets up data source tags set.
     * 
     * @param tags Data source tags set
     */
    public void setTags(Set<Tag> tags) 
    {
        this.tags = tags;
    }// setTags
}// Source
