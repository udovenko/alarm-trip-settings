package ru.sakhalinenergy.alarmtripsettings.models.entity;

import java.util.Set;
import java.util.HashSet;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.BatchSize;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;


/**
 * Implements tag setting entity used by Hibernate framework.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
@Entity
@Table(name="SETTINGS")
public class TagSetting 
{
    private int id;
    private int typeId;
    private String value;
    private Tag tag;
       
    private Set<TagSettingProperty> properties = new HashSet();
        
    
    /**
     * Required constructor.
     */
    public TagSetting(){}
        
    
    /**
     * Overrides parent "toString()" method. Returns setting type name and 
     * value string instead.
     * 
     * @return Setting type name and value string
     */
    @Override
    public String toString()
    {
        Object settingTypeName = null;
        
        try
        {
            settingTypeName = SettingsTypes.getTypeById(typeId)
                .getDeclaredField(SettingsTypes.NAME_FIELD_NAME).get(null);
        } catch (Exception e){}
        
        return (String)settingTypeName + ": " + value;
    }// toString
                
    
    /**
     * Returns tag setting identifier.
     * 
     * @return Tag setting identifier
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public int getId()
    {
        return id;
    }// getId
    

    /**
     * Returns tag setting type identifier.
     * 
     * @return Tag setting type identifier
     */
    @Column(name="param_id")
    public int getTypeId()
    {
        return typeId;
    }// getParamId
    
    
    /**
     * Returns tag setting value.
     * 
     * @return Tag setting value
     */
    @Column(name="value")
    public String getValue()
    {
        return value;
    }// getValue
    
    
    /**
     * Returns tag setting properties set.
     * 
     * @return Tag setting properties set
     */
    @OneToMany(mappedBy  = "setting", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 300)
    public Set<TagSettingProperty> getProperties()
    {
        return properties;
    }// getProperties

    
    /**
     * Returns setting property with given type id or null if property with such
     * type id not found.
     *
     * @param typeId Sought-for property with given type id
     * @return Setting property with given type id
     */
    public TagSettingProperty getPropertyByTypeId(int typeId)
    {
        for (TagSettingProperty tempSettingProperty : properties)
        {
            if (tempSettingProperty.getTypeId() == typeId) return tempSettingProperty;
        }//for
        
        return null;
    }// getPropertyByTypeId
    
    
    /**
     * Returns setting parent tag.
     * 
     * @return Parent tag
     */
    @ManyToOne
    @JoinColumn(name = "tag_id")
    public Tag getTag()
    {
        return tag;
    }// getTag
    
    
    /**
     * Sets up tag setting identifier.
     * 
     * @param id Tag setting identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId
   

    /**
     * Sets up tag setting type identifier.
     * 
     * @param typeId Tag setting type identifier
     */
    public void setTypeId(int typeId) 
    {
        this.typeId = typeId;
    }// setTypeId

    
    /**
     * Sets up tag setting value.
     * 
     * @param value Tag setting value
     */
    public void setValue(String value) 
    {
        this.value = value;
    }// setValue
       
    
    /**
     * Sets up tag setting properties set.
     * 
     * @param properties Tag setting properties set
     */
    public void setProperties(Set<TagSettingProperty> properties)
    {
        this.properties = properties;
    }// getProperties
    
    
    /**
     * Sets up setting parent tag.
     * 
     * @param tag Parent tag
     */
    public void setTag(Tag tag)
    {
        this.tag = tag;
    }// setTag
}// Setting 
