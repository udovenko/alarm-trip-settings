package ru.sakhalinenergy.alarmtripsettings.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsPropertiesTypes;


/**
 * Implements tag's setting property entity used by Hibernate framework.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
@Entity
@Table(name="SETTING_PROPERTIES")
public class TagSettingProperty 
{
    private int id;
    private int typeId;
    private String value;
    private TagSetting setting;
 
    
    /**
     * Required constructor.
     */
    public TagSettingProperty(){}
    
    
    /**
     * Overrides parent "toString()" method. Returns setting property type name
     * and value string instead.
     * 
     * @return Setting property type name and value string
     */
    @Override
    public String toString()
    {
        Object settingTypeName = null;
        
        try
        {
            settingTypeName = SettingsPropertiesTypes.getSettingsPropertyTypeById(typeId)
                .getDeclaredField(SettingsPropertiesTypes.NAME_FIELD_NAME).get(null);
        } catch (Exception e){}
        
        return (String)settingTypeName + ": " + value;
    }// toString
    
    
    /**
     * Returns tag's setting property identifier.
     * 
     * @return Tag's setting property identifier
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId()
    {
        return id;
    }// getId
        
    
    /**
     * Returns tag's setting property type identifier.
     *
     * @return Tag's setting property type identifier
     */
    @Column(name = "type_id")
    public int getTypeId()
    {
        return typeId;
    }// getTypeId
     
    
    /**
     * Returns tag's setting property value.
     * 
     * @return Tag's setting property value
     */
    @Column(name = "value")
    public String getValue()
    {
        return value;
    }// getValue

    
    /**
     * Sets up tag's setting property identifier.
     * 
     * @param id Tag's setting property identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up tag's setting property type identifier.
     * 
     * @param typeId Tag's setting property type identifier
     */
    public void setTypeId(int typeId)
    {
        this.typeId = typeId;
    }// setTypeId
    

    /**
     * Sets up tag's setting property value.
     * 
     * @param value Tag's setting property value
     */
    public void setValue(String value) 
    {
        this.value = value;
    }// setValue


    /**
     * Returns tag's setting property settings set.
     * 
     * @return Tag's setting property settings set
     */
    @ManyToOne
    @JoinColumn(name = "setting_id")
    public TagSetting getSetting() 
    {
        return setting;
    }// getSetting

    
    /**
     * Sets up tag's setting property settings set.
     * 
     * @param tagSetting Tag's setting property settings set
     */
    public void setSetting(TagSetting tagSetting) 
    {
        this.setting = tagSetting;
    }// setSetting
}// SettingProperties
