package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;


/**
 * Implements tag entity wrapper for check box node.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TagNodeModel 
{
    private Tag tag;
    boolean selected;

    
    /**
     * Public constructor. Sets tag entity and election flag.
     * 
     * @param tag Tag entity to be wrapped
     * @param selected Selection flag
     */
    public TagNodeModel(Tag tag, boolean selected) 
    {
        this.tag = tag;
        this.selected = selected;
    }// TagNodeModel

    
    /**
     * Returns wrapper's selection state.
     * 
     * @return True if selected
     */
    public boolean isSelected() 
    {
        return selected;
    }// isSelected
    

    /**
     * Sets wrapper's selection state.
     * 
     * @param selected Selection state
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }// setSelected
    
    
    /**
     * Returns wrapped tag entity.
     * 
     * @return Wrapped tag entity
     */
    public Tag getTag() 
    {
        return tag;
    }// getTag

    
    /**
     * Sets wrapped tag entity.
     * 
     * @param tag Tag entity to be wrapped
     */
    public void setTag(Tag tag)
    {
        this.tag = tag;
    }// setTag

    
    /**
     * Overrides parent "toString()" method. Returns current class name, object
     * and selection state string instead.
     * 
     * @return Current class name, object and selection state string
     */
    @Override
    public String toString() 
    {
        return getClass().getName() + "[" + tag.toString() + "/" + selected + "]";
    }// toString 
}// TagNodeModel
