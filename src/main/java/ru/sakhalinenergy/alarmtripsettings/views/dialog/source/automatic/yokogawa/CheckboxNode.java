package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa;


/**
 * Implements wrapper class for using object with check box node renderer. 
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class CheckboxNode 
{
    private Object object;
    private boolean selected;

    
    /**
     * Public constructor. Set up object to b wrapped and selection selection 
     * state flag.
     * 
     * @param object Object to be wrapped
     * @param selected Selection state flag
     */
    public CheckboxNode(Object object, boolean selected) 
    {
        this.object = object;
        this.selected = selected;
    }// CheckboxNode

    
    /**
     * Returns selection state flag.
     * 
     * @return Selection state flag
     */
    public boolean isSelected() 
    {
        return this.selected;
    }// isSelected
    

    /**
     * Sets up selection state flag.
     * 
     * @param selected Selection state flag
     */
    public void setSelected(boolean selected) 
    {
        this.selected = selected;
    }// setSelected

    
    /**
     * Returns wrapped object.
     * 
     * @return Wrapped object
     */
    public Object getObject() 
    {
        return this.object;
    }// getObject

    
    /**
     * Sets up wrapped object.
     * 
     * @param newObject Object to be wrapped
     */
    public void setObject(Object newObject) 
    {
        this.object = newObject;
    }// setObject

    
    /**
     * Overrides parent "toString()" method. Returns current class name, object
     * and selection state string instead.
     * 
     * @return Current class name, object and selection state string
     */
    @Override
    public String toString() 
    {
        return getClass().getName() + "[" + this.object + "/" + this.selected + "]";
    }// toString 
}// CheckboxNode
