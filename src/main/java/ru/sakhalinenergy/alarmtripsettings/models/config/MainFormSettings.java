package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements main form settings model.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class MainFormSettings extends Config implements MainFormSettingsObservable
{
    private static MainFormSettings instance;
    private static String SECTION_NAME = "MainFormSettings";
    
    // Setting fields:
    @Entry private String windowMaximized;
    @Entry private String windowLeft;
    @Entry private String windowTop;
    @Entry private String windowWidth;
    @Entry private String windowHeight;
    @Entry private String maximizedWindowWorkspaceHeight;
    @Entry private String maximizedWindowPlantsTreeWidth;
    @Entry private String maximizedWindowTagsTreeWidth;
    @Entry private String minimizedWindowWorkspaceHeight;
    @Entry private String minimizedWindowPlantsTreeWidth;
    @Entry private String minimizedWindowTagsTreeWidth;
    @Entry private String activeBottomTab;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private MainFormSettings(){}
    
    
    /**
     * Returns reference on a single class instance of main form settings. If
     * instance not created yet - creates it.
     *
     * @return Reference to main form settings singleton object
     */
    public static MainFormSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new MainFormSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns dialog's window expansion state as string values "true"/"false".
     * 
     * @return Window expansion state
     */
    @Override
    public String getWindowMaximized()
    {
        return windowMaximized;
    }// getMainWindowMaximized
    

    /**
     * Sets up dialog's window expansion state as string values "true"/"false".
     * 
     * @param mainWindowMaximized Window expansion state
     */
    public void setWindowMaximized(String mainWindowMaximized) 
    {
        this.windowMaximized = mainWindowMaximized;
        hasChanged = true;
    }// setMainWindowMaximized
    

    /**
     * Returns horizontal offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @return Form left offset
     */
    @Override
    public String getWindowLeft() 
    {
        return windowLeft;
    }// getMainWindowLeft
    

    /**
     * Sets up horizontal offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @param mainWindowLeft Form left offset
     */
    public void setWindowLeft(String mainWindowLeft) 
    {
        this.windowLeft = mainWindowLeft;
        hasChanged = true;
    }// setMainWindowLeft
    

    /**
     * Returns vertical offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @return Form top offset
     */
    @Override
    public String getWindowTop() 
    {
        return windowTop;
    }// getMainWindowTop
    

    /**
     * Sets up vertical offset for left upper corner of form window form
     * initial screen coordinates.
     * 
     * @param mainWindowTop Form top offset
     */
    public void setWindowTop(String mainWindowTop)
    {
        this.windowTop = mainWindowTop;
        hasChanged = true;
    }// setMainWindowTop
    

    /**
     * Returns form width setting value.
     * 
     * @return Form width
     */
    @Override
    public String getWindowWidth() 
    {
        return windowWidth;
    }// getMainWindowWidth
    

    /**
     * Sets up form width setting value.
     * 
     * @param mainWindowWidth Form width
     */
    public void setWindowWidth(String mainWindowWidth) 
    {
        this.windowWidth = mainWindowWidth;
        hasChanged = true;
    }// setMainWindowWidth

    
    /**
     * Returns form height setting value.
     * 
     * @return Form height
     */
    @Override
    public String getWindowHeight() 
    {
        return windowHeight;
    }// getMainWindowHeight
    

    /**
     * Sets up form height setting value.
     * 
     * @param mainWindowHeight Form height
     */
    public void setWindowHeight(String mainWindowHeight) 
    {
        this.windowHeight = mainWindowHeight;
        hasChanged = true;
    }// setMainWindowHeight
    

    /**
     * Returns workspace height setting value for expanded form.
     * 
     * @return Workspace height for expanded form
     */
    @Override
    public String getMaximizedWindowWorkspaceHeight() 
    {
        return maximizedWindowWorkspaceHeight;
    }// getMaximizedWindowWorkspaceHeight
    

    /**
     * Sets up workspace height setting value for expanded form.
     * 
     * @param workspaceHeight Workspace height for expanded form
     */
    public void setMaximizedWindowWorkspaceHeight(String workspaceHeight) 
    {
        this.maximizedWindowWorkspaceHeight = workspaceHeight;
        hasChanged = true;
    }// setMaximizedWindowWorkspaceHeight
    

    /**
     * Returns plants (assets) tree width setting value for expanded form.
     * 
     * @return Assets tree width for expanded form
     */
    @Override
    public String getMaximizedWindowPlantsTreeWidth() 
    {
        return maximizedWindowPlantsTreeWidth;
    }// getMaximizedWindowPlantsTreeWidth
    

    /**
     * Sets up plants (assets) tree width setting value for expanded form.
     * 
     * @param assetsTreeWidth Assets tree width for expanded form
     */
    public void setMaximizedWindowPlantsTreeWidth(String assetsTreeWidth) 
    {
        this.maximizedWindowPlantsTreeWidth = assetsTreeWidth;
        hasChanged = true;
    }// setMaximizedWindowPlantsTreeWidth

    
    /**
     * Returns selected loop tags tree width for expanded form.
     * 
     * @return Tags tree width
     */
    @Override
    public String getMaximizedWindowTagsTreeWidth()
    {
        return maximizedWindowTagsTreeWidth;
    }// getMaximizedWindowTagsTreeWidth
    

    /**
     * Sets up selected loop tags tree width for expanded form.
     * 
     * @param tagDetailsTreeWidth Tags tree width
     */
    public void setMaximizedWindowTagsTreeWidth(String tagDetailsTreeWidth) 
    {
        this.maximizedWindowTagsTreeWidth = tagDetailsTreeWidth;
        hasChanged = true;
    }// setMaximizedWindowTagsTreeWidth

    
    /**
     * Returns active bottom panel tab name.
     * 
     * @return Active bottom panel tab name
     */
    @Override
    public String getActiveBottomTab()
    {
        return activeBottomTab;
    }// getActiveBottomTab
    

    /**
     * Sets up active bottom panel tab name.
     * 
     * @param activeBottomTab Active bottom panel tab name
     */
    public void setActiveBottomTab(String activeBottomTab) 
    {
        this.activeBottomTab = activeBottomTab;
        hasChanged = true;
    }// setActiveBottomTab

    
    /**
     * Returns workspace height setting value for collapsed form.
     * 
     * @return Workspace height for collapsed form
     */
    @Override
    public String getMinimizedWindowWorkspaceHeight() 
    {
        return minimizedWindowWorkspaceHeight;
    }// getMinimizedWindowWorkspaceHeight
    

    /**
     * Sets up workspace height setting value for collapsed form.
     * 
     * @param minimizedWindowWorkspaceHeight Workspace height for collapsed form
     */
    public void setMinimizedWindowWorkspaceHeight(String minimizedWindowWorkspaceHeight) 
    {
        this.minimizedWindowWorkspaceHeight = minimizedWindowWorkspaceHeight;
        hasChanged = true;
    }// setMinimizedWindowWorkspaceHeight
    

    /**
     * Returns plants (assets) tree width setting value for collapsed form.
     * 
     * @return Assets tree width for collapsed form
     */
    @Override
    public String getMinimizedWindowPlantsTreeWidth() 
    {
        return minimizedWindowPlantsTreeWidth;
    }// getMinimizedWindowPlantsTreeWidth

    
    /**
     * Sets up plants (assets) tree width setting value for collapsed form.
     * 
     * @param minimizedWindowAssetsTreeWidth Assets tree width for collapsed form
     */
    public void setMinimizedWindowPlantsTreeWidth(String minimizedWindowAssetsTreeWidth) 
    {
        this.minimizedWindowPlantsTreeWidth = minimizedWindowAssetsTreeWidth;
        hasChanged = true;
    }// setMinimizedWindowPlantsTreeWidth
    

    /**
     * Returns selected loop tags tree width for collapsed form.
     * 
     * @return Selected loop tags tree width for collapsed form
     */
    @Override
    public String getMinimizedWindowTagsTreeWidth() 
    {
        return minimizedWindowTagsTreeWidth;
    }// getMinimizedWindowTagsTreeWidth

    
    /**
     * Sets up selected loop tags tree width for collapsed form.
     * 
     * @param minimizedWindowTagDetailsTreeWidth Selected loop tags tree width for collapsed form
     */
    public void setMinimizedWindowTagsTreeWidth(String minimizedWindowTagDetailsTreeWidth) 
    {
        this.minimizedWindowTagsTreeWidth = minimizedWindowTagDetailsTreeWidth;
        hasChanged = true;
    }// setMinimizedWindowTagsTreeWidth
}// UiSettings
