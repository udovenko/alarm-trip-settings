package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of main form settings model for using by views. Allows only 
 * getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface MainFormSettingsObservable 
{
    
    /**
     * Returns dialog's window expansion state as string values "true"/"false".
     * 
     * @return Window expansion state
     */
    public String getWindowMaximized();
    

    /**
     * Returns horizontal offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @return Form left offset
     */
    public String getWindowLeft(); 
    

    /**
     * Returns vertical offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @return Form top offset
     */
    public String getWindowTop(); 
    

    /**
     * Returns form width setting value.
     * 
     * @return Form width
     */
    public String getWindowWidth(); 

    
    /**
     * Returns form height setting value.
     * 
     * @return Form height
     */
    public String getWindowHeight(); 
    

    /**
     * Returns workspace height setting value for expanded form.
     * 
     * @return Workspace height for expanded form
     */
    public String getMaximizedWindowWorkspaceHeight();
        

    /**
     * Returns plants (assets) tree width setting value for expanded form.
     * 
     * @return Assets tree width for expanded form
     */
    public String getMaximizedWindowPlantsTreeWidth();
   
    
    /**
     * Returns selected loop tags tree width for expanded form.
     * 
     * @return Tags tree width
     */
    public String getMaximizedWindowTagsTreeWidth();

    
    /**
     * Returns active bottom panel tab name.
     * 
     * @return Active bottom panel tab name
     */
    public String getActiveBottomTab();

    
    /**
     * Returns workspace height setting value for collapsed form.
     * 
     * @return Workspace height for collapsed form
     */
    public String getMinimizedWindowWorkspaceHeight(); 

    
    /**
     * Returns plants (assets) tree width setting value for collapsed form.
     * 
     * @return Assets tree width for collapsed form
     */
    public String getMinimizedWindowPlantsTreeWidth();
    

    /**
     * Returns selected loop tags tree width for collapsed form.
     * 
     * @return Selected loop tags tree width for collapsed form
     */
    public String getMinimizedWindowTagsTreeWidth();

}// MainFormSettingsObservable
