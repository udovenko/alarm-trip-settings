package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of assets tree settings model for using by views. Allows only 
 * getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface PlantsTreePanelSettingsObservable 
{
    
    /**
     * Returns expanded plant nodes list string.
     * 
     * @return Expanded plant nodes list string
     */
    public String getExpandedPlants();

    
    /**
     * Returns expanded area nodes list as string.
     * 
     * @return Expanded area nodes list as string
     */
    public String getExpandedAreas(); 

    
    /**
     * Returns selected node type.
     * 
     * @return Selected node type
     */
    public String getSelectedNodeType();

    
    /**
     * Returns selected node identifier.
     * 
     * @return Selected node identifier
     */
    public String getSelectedNodeId(); 

}// PlantsTreePanelSettingsObservable
