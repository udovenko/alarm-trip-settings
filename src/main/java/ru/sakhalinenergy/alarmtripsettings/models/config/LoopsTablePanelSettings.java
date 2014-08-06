package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements loops table settings model.
 *  
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class LoopsTablePanelSettings extends Config implements LoopsTablePanelSettingsObservable
{
    private static LoopsTablePanelSettings instance;
    private static final String SECTION_NAME = "LoopsTablePanel";
    
    @Entry private String selectedLoop;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private LoopsTablePanelSettings(){}
    
    
    /**
     * Returns reference on a single instance of loops table settings class. 
     * If instance not created yet - creates it.
     *
     * @return Reference to loops table settings singleton object
     */
    public static LoopsTablePanelSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new LoopsTablePanelSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns selected loop identifier setting value.
     * 
     * @return Selected loop identifier
     */
    @Override
    public String getSelectedLoop() 
    {
        return selectedLoop;
    }// getSelectedLoop

    
    /**
     * Sets up selected loop identifier setting value.
     * 
     * @param selectedLoop Selected loop identifier
     */
    public void setSelectedLoop(String selectedLoop) 
    {
        this.selectedLoop = selectedLoop;
        hasChanged = true;
    }// setSelectedLoop
}// LoopsTablePanelSettings
