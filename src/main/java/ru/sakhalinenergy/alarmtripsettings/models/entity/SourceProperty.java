package ru.sakhalinenergy.alarmtripsettings.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;


/**
 * Implements data source property entity used by Hibernate framework.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
@Entity
@Table(name="SOURCE_PROPERTIES")
public class SourceProperty
{
    private int id;
    private int typeId;
    private String value;
    private Source source;
    
    
    /**
     * Required constructor.
     */
    public SourceProperty(){}
    
    
    /**
     * Overrides parent "toString()" method. Returns property type name 
     * and property value instead.
     *
     * @return String with property type name and property value
     */
    @Override
    public String toString()
    {
        Class<?> SourcePropertyTypeClass = null;
        String sourcePropertyTypeName = null;
        
        try
        {
            SourcePropertyTypeClass = SourcesPropertiesTypes.getSourcePropertyTypeById(typeId);
            sourcePropertyTypeName = (String)SourcePropertyTypeClass.getDeclaredField("NAME").get(null);
        } catch (Exception exception){}
                
        return sourcePropertyTypeName + ": " + value;
    }// toString
    

    /**
     * Returns data source's property identifier.
     * 
     * @return Source's property identifier
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
     * Returns data source's property type identifier.
     * 
     * @return Data source's property type identifier
     */
    @Column(name="type_id")
    public int getTypeId()
    {
        return this.typeId;
    }// getTypeId
    
    
    /**
     * Returns data source's property value.
     * 
     * @return Data source's property value
     */
    @Column(name="value")
    public String getValue()
    {
        return this.value;
    }// getValue
  
  
    /**
     * Returns parent data source for current property.
     * 
     * @return Parent data source
     */
    @ManyToOne
    @JoinColumn(name = "source_id")
    public Source getSource() 
    {
        return source;
    }// getSource
    
    
    /**
     * Sets up data source's property identifier.
     * 
     * @param id Data source's property identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up data source's property type identifier.
     * 
     * @param typeId Data source's property type identifier
     */
    public void setTypeId(int typeId) 
    {
        this.typeId = typeId;
    }// setTypeId
    

    /**
     * Sets up data source's property value.
     * 
     * @param value Data source's property value
     */
    public void setValue(String value) 
    {
        this.value = value;
    }// setValue

 
    /**
     * Sets up parent data source for current property.
     * 
     * @param source Parent data source
     */
    public void setSource(Source source) 
    {
        this.source = source;
    }// setSource
}// SourceProperty
