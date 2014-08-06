package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements assets tree settings model.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class PlantsTreePanelSettings extends Config implements PlantsTreePanelSettingsObservable
{
    private static PlantsTreePanelSettings instance;
    private static final String SECTION_NAME = "PlantsTree";
    
    // Setting fields:
    @Entry private String expandedPlants;
    @Entry private String expandedAreas;
    @Entry private String selectedNodeType;
    @Entry private String selectedNodeId;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private PlantsTreePanelSettings(){}
    
    
    /**
     * Returns reference on a single class instance of assets tree settings. If 
     * instance not created yet - creates it.
     *
     * @return Reference to dialog settings singleton object
     */
    public static PlantsTreePanelSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new PlantsTreePanelSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns expanded plant nodes list string.
     * 
     * @return Expanded plant nodes list string
     */
    @Override
    public String getExpandedPlants() 
    {
        return expandedPlants;
    }// getExpandedPlants

    
    /**
     * Sets up expanded plant nodes list as string.
     * 
     * @param expandedPlants Expanded plant nodes list as string
     */
    public void setExpandedPlants(String expandedPlants) 
    {
        this.expandedPlants = expandedPlants;
        hasChanged = true;
    }// setExpandedPlants

    
    /**
     * Returns expanded area nodes list as string.
     * 
     * @return Expanded area nodes list as string
     */
    @Override
    public String getExpandedAreas() 
    {
        return expandedAreas;
    }// getExpandedAreas

    
    /**
     * Sets up expanded area nodes list as string.
     * 
     * @param expandedAreas Expanded area nodes list as string
     */
    public void setExpandedAreas(String expandedAreas) 
    {
        this.expandedAreas = expandedAreas;
        hasChanged = true;
    }// setExpandedAreas

    
    /**
     * Returns selected node type.
     * 
     * @return Selected node type
     */
    @Override
    public String getSelectedNodeType() 
    {
        return selectedNodeType;
    }// getSelectedNodeType

    
    /**
     * Sets up selected node type.
     * 
     * @param selectedNodeType Selected node type
     */
    public void setSelectedNodeType(String selectedNodeType) 
    {
        this.selectedNodeType = selectedNodeType;
        hasChanged = true;
    }// setSelectedNodeType

    
    /**
     * Returns selected node identifier.
     * 
     * @return Selected node identifier
     */
    @Override
    public String getSelectedNodeId() 
    {
        return selectedNodeId;
    }// getSelectedNodeId

    
    /**
     * Sets up selected node identifier.
     * 
     * @param selectedNodeId Selected node identifier
     */
    public void setSelectedNodeId(String selectedNodeId) 
    {
        this.selectedNodeId = selectedNodeId;
        hasChanged = true;
    }// setSelectedNodeId
}// PlantsTreePanelSettings
