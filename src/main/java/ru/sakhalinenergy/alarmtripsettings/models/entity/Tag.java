package ru.sakhalinenergy.alarmtripsettings.models.entity;

import java.util.Set;
import java.util.List;
import java.util.LinkedHashSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


/**
 * Implements tag entity used by Hibernate framework.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
@Entity
@Table(name="TAGS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag
{
    private Integer id;
    private String name;
    private String modifier;
    private Loop loop;
    private Source source;
    
    private Set<TagSetting> settings = new LinkedHashSet();
    
    
    /**
     * Required constructor.
     */
    public Tag(){}
    
   
    /**
     * Overrides parent "toString()" method. Returns tag name instead.
     * 
     * @return Tag name
     */
    @Override
    public String toString()
    {
        return name;
    }// toString
        
    
    /**
     * Checks if given and current tag have settings with same type.
     * 
     * @param tag Tag to be compared with current one
     * @param settingTypes Setting types to be checked
     * @return True if given and current tag have settings with same type, else false
     */
    public boolean hasSameSettingsTypeAs(Tag tag, List<Integer> settingTypes)
    {
        for (TagSetting currentTagSetting: settings)
        {
            for (TagSetting comparedTagSetting : tag.getSettings())
            {
                if (currentTagSetting == comparedTagSetting) continue;
                if (currentTagSetting.getTypeId() == comparedTagSetting.getTypeId()
                    && settingTypes.contains(currentTagSetting.getTypeId()))
                        return true;
            }// for
        }// for
        
        return false;
    }// hasSameSettingsType
    
    
    /**
     * Returns tag's identifier.
     * 
     * @return Tag's identifier
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    public Integer getId()
    {
        return id;
    }// getId

    
    /**
     * Returns tag's name.
     * 
     * @return Tag's name
     */
    @Column(name="name")
    public String getName() 
    {
        return name;
    }// getName
   
    
    /**
     * Returns tag's modifier.
     * 
     * @return Tag's modifier
     */
    @Column(name="modifier")
    public String getModifier() 
    {
        return modifier;
    }// getModifier
    
    
    
    /**
     * Returns tag's parent data source.
     * 
     * @return Parent data source
     */
    @ManyToOne()
    @JoinColumn(name = "source_id")
    public Source getSource() 
    {
        return source;
    }// getSource
    
    
    /**
     * Sets up tag's identifier.
     * 
     * @param id Tag's identifier
     */
    public void setId(int id) 
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up tag's name.
     * 
     * @param name Tag's name
     */
    public void setName(String name)
    {
        this.name = name;
    }// setName

    
    /**
     * Sets up tag's modifier.
     * 
     * @param modifier Tag's modifier
     */
    public void setModifier(String modifier) 
    {
        this.modifier = modifier;
    }// setModifier
    
    
    /**
     * Returns up tag's settings collection.
     * 
     * @return Tag's settings collection
     */
    @OneToMany(mappedBy  = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @BatchSize(size = 300)
    public Set<TagSetting> getSettings()
    {
        return settings;
    }// getSettings
    
    
    /**
     * Returns tag setting with given type id or null, if setting with such type
     * id not found.
     * 
     * @param typeId Sought-for setting type id
     * @return Tag setting with given type id or null
     */
    public TagSetting getSettingByTypeId(int typeId)
    {
        for (TagSetting tempSetting : settings)
        {
            if (tempSetting.getTypeId() == typeId) return tempSetting;
        }// for
        
        return null;
    }// getSettingByTypeId
    
    
    /**
     * Sets up tag's settings collection.
     * 
     * @param settings Tag's settings collection
     */
    public void setSettings(Set<TagSetting> settings)
    {
        this.settings = settings;
    }// getSettings

    
    /**
     * Returns tag's parent loop.
     * 
     * @return Tag's parent loop
     */
    @ManyToOne
    @JoinColumn(name = "loop_id")
    public Loop getLoop() 
    {
        return loop;
    }// getLoop
    

    /**
     * Sets up tag's parent loop.
     * 
     * @param loop Tag's parent loop
     */
    public void setLoop(Loop loop) 
    {
        this.loop = loop;
    }// setLoop

    
    /**
     * Sets up tag's parent data source.
     * 
     * @param source Tag's parent data source
     */
    public void setSource(Source source) 
    {
        this.source = source;
    }// setSource
}// Tag
