package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;



/**
 *
 * @author Denis.Udovenko
 * @version 1.0.1
 */
public class TagNodeModel 
{
    private Tag tag;
    boolean selected;

    
    /**
     * 
     */
    public TagNodeModel(Tag tag, boolean selected) 
    {
        this.tag = tag;
        this.selected = selected;
    }// TagNode

    
    /**
     * 
     */
    public boolean isSelected() 
    {
        return selected;
    }// isSelected
    

    /**
     * 
     */
    public void setSelected(boolean newValue)
    {
        selected = newValue;
    }// setSelected
    
    
    /**
     * 
     * 
     */
    public Tag getTag() 
    {
        return tag;
    }// getTag

    
    /**
     * 
     */
    public void setTag(Tag tag)
    {
        this.tag = tag;
    }// setTag

    
    /**
     * 
     * 
     */
    public String toString() 
    {
        return getClass().getName() + "[" + tag.toString() + "/" + selected + "]";
    }// toString 
}// TagNode
