package ru.sakhalinenergy.alarmtripsettings.models.entity;

import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;


/**
 * Implements loop entity used by Hibernate framework.
 * 
 * @author Denis.Udovenko
 * @version 1.0.3
 */
@Entity
@Table(name="LOOPS")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Loop
{
    private int id;
    private String plant;
    private String area;
    private String unit;
    private String measuredVariable;
    private String uniqueIndex;
    private String suffix;

    private Set<Tag> tags = new HashSet();
    
    
    /**
     * Required constructor.
     */
    public Loop(){}
    
    
    /**
     * Overrides parent "toString()" method. Returns conventional loop name 
     * instead.
     * 
     * @return Conventional loop name
     */
    @Override
    public String toString()
    {
        return this.getPlant() + "-" + this.getArea() + this.getUnit() + "-" 
            + this.getMeasuredVariable() + "-" + this.getUniqueIndex() + this.getSuffix();
    }// toString
          
    
    /**
     * Override parent "equals()" method. Checks loop equality by name 
     * components.
     * 
     * @param object Object to compare
     * @return True if given object equals to current one
     */
    @Override
    public boolean equals(Object object) 
    { 
        if (object.getClass() != this.getClass()) return false;
        
        Loop comparableLoop = (Loop)object;
        
        if (plant.equals(comparableLoop.plant) && area.equals(comparableLoop.area) 
            && unit.equals(comparableLoop.unit) && measuredVariable.equals(comparableLoop.measuredVariable)
            && uniqueIndex.equals(comparableLoop.uniqueIndex) && suffix.equals(comparableLoop.suffix))
        {
            return true;
        }// if
        
        return false;
    }// equals
    
    
    /**
     * Checks if loop contains tags with same source type and duplicated setting,
     * for example two documents tag with "HH" setting.
     * 
     * @return True if tags with duplicated alarm found
     */
    public boolean hasDuplicatedAlarms()
    {
        // Setting to be checked list:
        List searchedSettings = Arrays.asList(SettingsTypes.ALARM_LL_SETTING.ID, 
            SettingsTypes.ALARM_L_SETTING.ID, SettingsTypes.ALARM_H_SETTING.ID,
            SettingsTypes.ALARM_HH_SETTING.ID);
        
        for (Tag outerTempTag : tags)
        {
            for(Tag innerTempTag : tags)
            {
                if (outerTempTag == innerTempTag) continue;
                
                if (outerTempTag.getSource().getTypeId() == innerTempTag.getSource().getTypeId()
                    && outerTempTag.hasSameSettingsTypeAs(innerTempTag, searchedSettings))
                        return true;
            }// for
        }// for
        
        return false;
    }// hasDuplicatedAlarms
    
    
    /**
     * Returns loop identifier.
     * 
     * @return Loop identifier
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    public int getId()
    {
        return this.id;
    }// getId
    
        
    /**
     * Returns loop tags set.
     * 
     * @return loop tags set
     */
    @OneToMany(mappedBy  = "loop", fetch = FetchType.LAZY)
    @Cascade({CascadeType.ALL})
    @BatchSize(size = 50)
    public Set<Tag> getTags()
    {
        return tags;
    }// getTags

    
    /**
     * Returns loop plant.
     * 
     * @return Loop plant
     */
    @Column(name="plant")
    public String getPlant() 
    {
        return plant;
    }// getPlant
    

    /**
     * Returns loop area.
     * 
     * @return Loop area
     */
    @Column(name="area")
    public String getArea() 
    {
        return area;
    }// getArea

    
    /**
     * Returns loop unit.
     * 
     * @return Loop unit
     */
    @Column(name="unit")
    public String getUnit() 
    {
        return unit;
    }// getUnit

    
    /**
     * Returns loop measured variable.
     * 
     * @return Loop measured variable
     */
    @Column(name="variable")
    public String getMeasuredVariable() 
    {
        return measuredVariable;
    }// getMeasuredVariable

    
    /**
     * Returns loop unique index.
     * 
     * @return Loop unique index
     */
    @Column(name="unique_index")
    public String getUniqueIndex() 
    {
        return uniqueIndex;
    }// getUniqueIndex

    
    /**
     * Returns loop suffix.
     * 
     * @return the suffix
     */
    @Column(name="suffix")
    public String getSuffix() 
    {
        return suffix;
    }// getSuffix

    
    /**
     * Sets up loop identifier.
     * 
     * @param id Loop identifier
     */
    public void setId(int id)
    {
        this.id = id;
    }// setId

    
    /**
     * Sets up loop plant. 
     * 
     * @param plant Loop plant
     */
    public void setPlant(String plant) 
    {
        this.plant = plant;
    }// setPlant

    
    /**
     * Sets up loop area.
     * 
     * @param area Loop area
     */
    public void setArea(String area) 
    {
        this.area = area;
    }// setArea

    
    /**
     * Sets up loop unit.
     * 
     * @param unit Loop unit
     */
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }// setUnit

    
    /**
     * Sets up loop measured variable.
     * 
     * @param measuredVariable Loop measured variable
     */
    public void setMeasuredVariable(String measuredVariable) 
    {
        this.measuredVariable = measuredVariable;
    }// setMeasuredVariable

    
    /**
     * Sets up loop unique index.
     * 
     * @param uniqueIndex Loop unique index
     */
    public void setUniqueIndex(String uniqueIndex) 
    {
        this.uniqueIndex = uniqueIndex;
    }// setUniqueIndex

    
    /**
     * Sets up loop suffix.
     * 
     * @param suffix Loop suffix
     */
    public void setSuffix(String suffix)
    {
        this.suffix = suffix;
    }// setSuffix

    
    /**
     * Sets up loop tags set.
     * 
     * @param tags Loop tags set
     */
    public void setTags(Set<Tag> tags)
    {
        this.tags = tags;
    }// setTags
}// Loop