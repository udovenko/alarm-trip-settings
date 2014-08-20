package ru.sakhalinenergy.alarmtripsettings.views.form;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.models.config.MainFormSettingsObservable;


/**
 * Implements application's main form.
 * 
 * @author Denis Udovenko
 * @version 1.1.8
 */
public class MainForm extends JFrame
{
    private final Events events = new Events();
    
    private static final String LOGS_TAB_NAME                       = "log";
    private static final String OVERALL_COMPLIANCE_SUMMARY_TAB_NAME = "overall_compliance_summary";
    private static final String INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME = "intools_compliance_summary";
    private static final String DATA_SOURCES_TAB_NAME               = "data_sources";
    
    private int windowLeftBeforeMaximizing; 
    private int windowTopBeforeMaximizing; 
    private int windowWitdthBeforeMaximizing; 
    private int windowHeigthBeforeMaximizing; 
   
    private int maximizedWindowWorkspaceHeight;
    private int maximizedWindowPlantsTreeWidth;
    private int maximizedWindowTagsTreeWidth;
    
    private int minimizedWindowWorkspaceHeight;
    private int minimizedWindowPlantsTreeWidth;
    private int minimizedWindowTagsTreeWidth;
    
    private final MainFormSettingsObservable config;
    
    
    /**
     * Public constructor. Sets form configuration object, initializes 
     * components and applies configuration.
     * 
     * @param config Main form configuration object
     */
    public MainForm(MainFormSettingsObservable config)
    {
        this.config = config;
        
        initComponents();
        
        setIconImage(Main.alarmTripIcon.getImage());
        
        // Set main menu items icons:
        openStorageMenuItem.setIcon(Main.storageConnectionIcon);
        versionMenuItem.setIcon(Main.aboutIcon);
    }// MainFrom

    
    /**
     * Sets relative location, restores configuration and shows form.
     */
    public void render()
    {
        setLocationRelativeTo(null);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Restore configuration and show form:
                _applyConfig();
                setVisible(true);
                
            }//run
        });//invokeLater
    }// render
    
    
    /**
     * Adds a new subscriber on given event type in subscribers list.
     * 
     * @param eventType Type of event we subscribing on
     * @param listener New subscriber instance
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        events.on(eventType, listener);
    }// on
    
    
    /**
     * Removes all subscribers for given event type.
     * 
     * @param eventType Type of event for which subscribers will be removed
     */
    public void off(Enum eventType)
    {
        events.off(eventType);
    }// off
    
    
    /**
     * Triggers custom events with given type on descendant object.
     * 
     * @param eventType Type id for event which will be triggered
     * @param event Custom event object
     */
    public void trigger(Enum eventType, CustomEvent event)
    {
        events.trigger(eventType, event);
    }//trigger
    
    
    /**
     * Sets content panel for plants tree scroll pane.
     * 
     * @param plantsTreePanel Plants tree panel
     */
    public void setPlantsTreePanelContent(JPanel plantsTreePanel)
    {
        plantsTreeScrollPane.add(plantsTreePanel);
        plantsTreeScrollPane.setViewportView(plantsTreePanel);
    }// setPlantsTreePanelContent
    
    
    /**
     * Sets content panel for loops table and task bar pane.
     * 
     * @param loopsTableandTaskBarPanel Loops table panel
     */
    public void setLoopsTableAndTaskbarPanelContent(JPanel loopsTableandTaskBarPanel)
    {
        loopsTablePanelScrollPane.add(loopsTableandTaskBarPanel);
        loopsTablePanelScrollPane.setViewportView(loopsTableandTaskBarPanel);
    }// setLoopsTableAndTaskbarPanelContent
    
    
    /**
     * Sets content panel for overall compliance summary scroll pane.
     * 
     * @param overallComplianceSummaryPanel Overall compliance summary panel  
     */
    public void setOverallComplianceSummaryPanelContent(JPanel overallComplianceSummaryPanel)
    {
        overallComplianceSummaryScrollPane.add(overallComplianceSummaryPanel);
        overallComplianceSummaryScrollPane.setViewportView(overallComplianceSummaryPanel);
    }// setOverallComplianceSummaryPanelContent
    
    
    /**
     * Sets content panel for SPI compliance summary scroll pane.
     * 
     * @param intoolsComplianceSummaryPanel SPI compliance summary panel  
     */
    public void setIntoolsComplianceSummaryPanelContent(JPanel intoolsComplianceSummaryPanel)
    {
        intoolsComplianceSummaryScrollPane.add(intoolsComplianceSummaryPanel);
        intoolsComplianceSummaryScrollPane.setViewportView(intoolsComplianceSummaryPanel);
    }// setIntoolsComplianceSummaryPanelContent
    

    /**
     * Sets content panel for data sources tree scroll pane.
     * 
     * @param  dataSourcesPanel Data sources tree panel
     */
    public void setDataSourcesPanelContent(JPanel dataSourcesPanel)
    {
        objectDataSourcesTreeScrollPane.add(dataSourcesPanel);
        objectDataSourcesTreeScrollPane.setViewportView(dataSourcesPanel);
    }// setDataSourcesPanelContent
    
    
    /**
     * Sets content panel for tags tree tree scroll pane.
     * 
     * @param tagsTreePanel Tags tree panel
     */
    public void setTagsTreePanelContent(JPanel tagsTreePanel)
    {
        tagsTreeScrollPane.add(tagsTreePanel);
        tagsTreeScrollPane.setViewportView(tagsTreePanel);
    }// setTagsTreePanelContent
    
    
    /**
     * Returns current form extended state flag as string.
     * 
     * @return "true" if form is maximized, else "false"
     */
    public String isMaximized()
    {
        if (getExtendedState() == Frame.MAXIMIZED_BOTH) return Boolean.TRUE.toString();
        else return Boolean.FALSE.toString();
    }// isMaximized
    
    
    /**
     * Returns horizontal offset of left corner of form from screen initial
     * coordinates.
     *
     * @return Left offset of form
     */
    public String getFormLeft()
    {
        return Integer.toString(windowLeftBeforeMaximizing);
    }// getFormLeft

    
    /**
     * Returns vertical offset of top corner of form from screen initial
     * coordinates.
     *
     * @return Top offset of form
     */
    public String getFormTop()
    {
        return Integer.toString(windowTopBeforeMaximizing);
    }// getFormTop
    
    
    /**
     * Returns width of from.
     * 
     * @return Width of from
     */
    public String getFormWidth()
    {
        return Integer.toString(windowWitdthBeforeMaximizing);
    }// getFormWidth
    
    
    /**
     * Returns height of from.
     * 
     * @return Height of from
     */
    public String getFormHeigt()
    {
        return Integer.toString(windowHeigthBeforeMaximizing);
    }// getFormHeigt
    
    
    /**
     * Returns pants tree width for maximized form.
     * 
     * @return Pants tree width for maximized form
     */
    public String getMaximizedWindowPlantsTreeWidth()
    {
        return Integer.toString(maximizedWindowPlantsTreeWidth);
    }// getMaximizedWindowPlantsTreeWidth
    
    
    /**
     * Returns workspace height for maximized form.
     * 
     * @return Workspace height for maximized form
     */
    public String getMaximizedWindowWorkspaceHeight()
    {
        return Integer.toString(maximizedWindowWorkspaceHeight);
    }// getMaximizedWindowWorkspaceHeight
    
    
    /**
     * Returns tags tree width for maximized form.
     * 
     * @return Tags tree width for maximized form
     */
    public String getMaximizedWindowTagsTreeWidth()
    {
        return Integer.toString(maximizedWindowTagsTreeWidth);
    }// getMaximizedWindowTagsTreeWidth
    
    
    /**
     * Returns pants tree width for not maximized form.
     * 
     * @return Pants tree width for not maximized form
     */
    public String getMinimizedWindowPlantsTreeWidth()
    {
        return Integer.toString(minimizedWindowPlantsTreeWidth);
    }// getMinimizedWindowPlantsTreeWidth
    
    
    /**
     * Returns workspace height for not maximized form.
     * 
     * @return Workspace height for not maximized form
     */
    public String getMinimizedWindowWorkspaceHeight()
    {
        return Integer.toString(minimizedWindowWorkspaceHeight);
    }// getMinimizedWindowWorkspaceHeight
    
    
    /**
     * Returns tags tree width for not maximized form.
     * 
     * @return Tags tree width for not maximized form
     */
    public String getMinimizedWindowTagsTreeWidth()
    {
        return Integer.toString(minimizedWindowTagsTreeWidth);
    }// getMinimizedWindowTagsTreeWidth
    
    
    /**
     * Returns a name of current active bottom tab.
     * 
     * @return Name of current active bottom tab
     */
    public String getActiveBottomTab()
    {
        if (bottomTabbedPane.getSelectedComponent() == applicationLogScrollPane) return LOGS_TAB_NAME;
        if (bottomTabbedPane.getSelectedComponent() == overallComplianceSummaryScrollPane) return OVERALL_COMPLIANCE_SUMMARY_TAB_NAME;
        if (bottomTabbedPane.getSelectedComponent() == intoolsComplianceSummaryScrollPane) return INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME;
        if (bottomTabbedPane.getSelectedComponent() == objectDataSourcesTreeScrollPane) return DATA_SOURCES_TAB_NAME;
        
        return null;
    }// getActiveBottomTab
       
    
    /**
     * Restores form settings from configuration object.
     */
    private void _applyConfig()
    {
        // Apply sizes for not maximized from:
        windowWitdthBeforeMaximizing = Integer.parseInt(config.getWindowWidth());
        windowHeigthBeforeMaximizing = Integer.parseInt(config.getWindowHeight());
        setSize(windowWitdthBeforeMaximizing, windowHeigthBeforeMaximizing);
        
        // Apply position for not maximized form:
        windowLeftBeforeMaximizing = Integer.parseInt(config.getWindowLeft());
        windowTopBeforeMaximizing  = Integer.parseInt(config.getWindowTop());
        setLocation(windowLeftBeforeMaximizing, windowTopBeforeMaximizing);
               
        // Restore size fields values: 
        maximizedWindowWorkspaceHeight = Integer.parseInt(config.getMaximizedWindowWorkspaceHeight());
        maximizedWindowPlantsTreeWidth = Integer.parseInt(config.getMaximizedWindowPlantsTreeWidth());
        maximizedWindowTagsTreeWidth   = Integer.parseInt(config.getMaximizedWindowTagsTreeWidth());
        minimizedWindowWorkspaceHeight = Integer.parseInt(config.getMinimizedWindowWorkspaceHeight());
        minimizedWindowPlantsTreeWidth = Integer.parseInt(config.getMinimizedWindowPlantsTreeWidth());
        minimizedWindowTagsTreeWidth   = Integer.parseInt(config.getMinimizedWindowTagsTreeWidth());
        
        // If maximize form window, if approprite setting is true;
        if (config.getWindowMaximized().equals(Boolean.TRUE.toString()))
        {
            setExtendedState(Frame.MAXIMIZED_BOTH);
               
            // Apply workspace panels sizes for maximized from:
            workspaceAndLogSplitPane.setDividerLocation(maximizedWindowWorkspaceHeight);
            assetsTreeAndTagsInfoSplitPanel.setDividerLocation(maximizedWindowPlantsTreeWidth);
            loopsTableAndTagsTreeSplitPanel.setDividerLocation(maximizedWindowTagsTreeWidth);
        
        } else {
        
            // Apply workspace panels sizes for not maximized from:
            workspaceAndLogSplitPane.setDividerLocation(minimizedWindowWorkspaceHeight);
            assetsTreeAndTagsInfoSplitPanel.setDividerLocation(minimizedWindowPlantsTreeWidth);
            loopsTableAndTagsTreeSplitPanel.setDividerLocation(minimizedWindowTagsTreeWidth);
        }// else
        
        // Set active tab for bottom pane:
        if (config.getActiveBottomTab().equals(LOGS_TAB_NAME)) bottomTabbedPane.setSelectedComponent(applicationLogScrollPane);
        if (config.getActiveBottomTab().equals(OVERALL_COMPLIANCE_SUMMARY_TAB_NAME)) bottomTabbedPane.setSelectedComponent(overallComplianceSummaryScrollPane);
        if (config.getActiveBottomTab().equals(INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME)) bottomTabbedPane.setSelectedComponent(intoolsComplianceSummaryScrollPane);
        if (config.getActiveBottomTab().equals(DATA_SOURCES_TAB_NAME)) bottomTabbedPane.setSelectedComponent(objectDataSourcesTreeScrollPane);
    }// _applyConfig
             
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        workspaceAndLogSplitPane = new javax.swing.JSplitPane();
        assetsTreeAndTagsInfoSplitPanel = new javax.swing.JSplitPane();
        loopsTableAndTagsTreeSplitPanel = new javax.swing.JSplitPane();
        tagsTreeScrollPane = new javax.swing.JScrollPane();
        tagsTree = new javax.swing.JTree();
        loopsTablePanel = new javax.swing.JPanel();
        loopsTablePanelScrollPane = new javax.swing.JScrollPane();
        plantsTreeScrollPane = new javax.swing.JScrollPane();
        bottomTabbedPane = new javax.swing.JTabbedPane();
        applicationLogScrollPane = new javax.swing.JScrollPane();
        applicationLogTextArea = new javax.swing.JTextArea();
        overallComplianceSummaryScrollPane = new javax.swing.JScrollPane();
        intoolsComplianceSummaryScrollPane = new javax.swing.JScrollPane();
        objectDataSourcesTreeScrollPane = new javax.swing.JScrollPane();
        statusBar = new org.jdesktop.swingx.JXStatusBar();
        statusBarDatabaseNameLabel = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openStorageMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        versionMenuItem = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        workspaceAndLogSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tagsTreeScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tagsTreeScrollPaneComponentResized(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tagsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tagsTree.setRootVisible(false);
        tagsTree.setShowsRootHandles(true);
        tagsTreeScrollPane.setViewportView(tagsTree);

        loopsTableAndTagsTreeSplitPanel.setRightComponent(tagsTreeScrollPane);

        javax.swing.GroupLayout loopsTablePanelLayout = new javax.swing.GroupLayout(loopsTablePanel);
        loopsTablePanel.setLayout(loopsTablePanelLayout);
        loopsTablePanelLayout.setHorizontalGroup(
            loopsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
            .addGroup(loopsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(loopsTablePanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );
        loopsTablePanelLayout.setVerticalGroup(
            loopsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(loopsTablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(loopsTablePanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );

        loopsTableAndTagsTreeSplitPanel.setLeftComponent(loopsTablePanel);

        assetsTreeAndTagsInfoSplitPanel.setRightComponent(loopsTableAndTagsTreeSplitPanel);

        plantsTreeScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                plantsTreeScrollPaneComponentResized(evt);
            }
        });
        assetsTreeAndTagsInfoSplitPanel.setLeftComponent(plantsTreeScrollPane);

        workspaceAndLogSplitPane.setLeftComponent(assetsTreeAndTagsInfoSplitPanel);

        bottomTabbedPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                bottomTabbedPaneComponentResized(evt);
            }
        });

        applicationLogScrollPane.setWheelScrollingEnabled(false);

        applicationLogTextArea.setEditable(false);
        applicationLogTextArea.setColumns(20);
        applicationLogTextArea.setRows(5);
        applicationLogScrollPane.setViewportView(applicationLogTextArea);

        bottomTabbedPane.addTab("Log", applicationLogScrollPane);

        overallComplianceSummaryScrollPane.setBorder(null);
        overallComplianceSummaryScrollPane.setWheelScrollingEnabled(false);
        bottomTabbedPane.addTab("Overall compliance summary", overallComplianceSummaryScrollPane);
        bottomTabbedPane.addTab("SPI compliance summary", intoolsComplianceSummaryScrollPane);

        objectDataSourcesTreeScrollPane.setBorder(null);
        objectDataSourcesTreeScrollPane.setWheelScrollingEnabled(false);
        bottomTabbedPane.addTab("Data sources", objectDataSourcesTreeScrollPane);

        workspaceAndLogSplitPane.setRightComponent(bottomTabbedPane);

        statusBar.setMinimumSize(new java.awt.Dimension(27, 10));
        statusBar.add(statusBarDatabaseNameLabel);

        fileMenu.setText("File");

        openStorageMenuItem.setText("Connect...");
        openStorageMenuItem.setToolTipText("");
        openStorageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStorageMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openStorageMenuItem);

        mainMenuBar.add(fileMenu);

        aboutMenu.setText("About");

        versionMenuItem.setText("Version...");
        versionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionMenuItemActionPerformed(evt);
            }
        });
        aboutMenu.add(versionMenuItem);

        mainMenuBar.add(aboutMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(workspaceAndLogSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(workspaceAndLogSplitPane)
                .addGap(0, 0, 0)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * Handles "File -> Connect..." main menu item click event and triggers 
     * appropriate event for all subscribers.
     * 
     * @param evt Main menu item click event object
     */
    private void openStorageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStorageMenuItemActionPerformed
        
        CustomEvent myEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CONNECT_MENU_ITEM_CLICK, myEvent);
    }//GEN-LAST:event_openStorageMenuItemActionPerformed
           
    
    /**
     * Handles tags tree pane resize event and sets new tags tree width value 
     * in appropriate field depending on form expanded state.
     * 
     * @param evt Tags tree pane resize event object
     */
    private void tagsTreeScrollPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tagsTreeScrollPaneComponentResized
        
        if (getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowTagsTreeWidth = loopsTableAndTagsTreeSplitPanel.getDividerLocation();
        else minimizedWindowTagsTreeWidth = loopsTableAndTagsTreeSplitPanel.getDividerLocation();
    }//GEN-LAST:event_tagsTreeScrollPaneComponentResized
   
    
    /**
     * Handles "About -> Version..." main menu item click event and triggers 
     * appropriate event for all subscribers.
     * 
     * @param evt Main menu item click event object
     */
    private void versionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versionMenuItemActionPerformed
        
        CustomEvent myEvent = new CustomEvent(new Object());
        trigger(ViewEvent.VERSION_MENU_ITEM_CLICK, myEvent);
    }//GEN-LAST:event_versionMenuItemActionPerformed
   
           
    /**
     * Handles form closing event and triggers appropriate event for all
     * subscribers.
     * 
     * @param evt Form closing event object
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        CustomEvent myEvent = new CustomEvent(new Object());
        trigger(ViewEvent.FORM_CLOSING, myEvent);
    }//GEN-LAST:event_formWindowClosing

        
    /**
     * Handles bottom tabbed pane resize event and sets new bottom tabbed pane's
     * height value in appropriate field depending on form expanded state.
     * 
     * @param evt Bottom tabbed pane resize event object
     */
    private void bottomTabbedPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bottomTabbedPaneComponentResized
 
        if (getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowWorkspaceHeight = workspaceAndLogSplitPane.getDividerLocation();
        else minimizedWindowWorkspaceHeight = workspaceAndLogSplitPane.getDividerLocation();     
    }//GEN-LAST:event_bottomTabbedPaneComponentResized

    
    /**
     * Handles plants tree pane resize event and sets plants tree pane's
     * width value in appropriate field depending on form expanded state.
     * 
     * @param evt Plants tree pane resize event object
     */
    private void plantsTreeScrollPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_plantsTreeScrollPaneComponentResized
        
        if (getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowPlantsTreeWidth = assetsTreeAndTagsInfoSplitPanel.getDividerLocation();
        else minimizedWindowPlantsTreeWidth = assetsTreeAndTagsInfoSplitPanel.getDividerLocation();
    }//GEN-LAST:event_plantsTreeScrollPaneComponentResized

    
    /**
     * Handles form expanded state change event and restores panes dividers 
     * locations for appropriate form state in a task at the end of EDT queue.
     * 
     * @param evt Form expended state change event object
     */
    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // If form was maximized:
                if (getExtendedState() == Frame.MAXIMIZED_BOTH)
                {
                    workspaceAndLogSplitPane.setDividerLocation(maximizedWindowWorkspaceHeight);
                    assetsTreeAndTagsInfoSplitPanel.setDividerLocation(maximizedWindowPlantsTreeWidth);
                    loopsTableAndTagsTreeSplitPanel.setDividerLocation(maximizedWindowTagsTreeWidth);

                } else { // If form was collapsed:
        
                    workspaceAndLogSplitPane.setDividerLocation(minimizedWindowWorkspaceHeight);
                    assetsTreeAndTagsInfoSplitPanel.setDividerLocation(minimizedWindowPlantsTreeWidth);
                    loopsTableAndTagsTreeSplitPanel.setDividerLocation(minimizedWindowTagsTreeWidth);
                }// else
            }// run
        });// invokeLater
    }//GEN-LAST:event_formWindowStateChanged

    
    /**
     * Handles form size changing event sets form size values in appropriate
     * fields if form is not maximized.
     * 
     * @param evt Form size changing event object
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
        if (getExtendedState() != Frame.MAXIMIZED_BOTH)
        {
            windowWitdthBeforeMaximizing = getSize().width;
            windowHeigthBeforeMaximizing = getSize().height;
        }// if
    }//GEN-LAST:event_formComponentResized

    
    /**
     * Handles form position changing event sets form coordinate values in 
     * appropriate fields if form is not maximized.
     * 
     * @param evt Form position changing event object
     */
    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        
        if (getExtendedState() != Frame.MAXIMIZED_BOTH)
        {
            windowLeftBeforeMaximizing = getLocation().x;
            windowTopBeforeMaximizing = getLocation().y;
        }// if
    }//GEN-LAST:event_formComponentMoved
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JScrollPane applicationLogScrollPane;
    private javax.swing.JTextArea applicationLogTextArea;
    private javax.swing.JSplitPane assetsTreeAndTagsInfoSplitPanel;
    private javax.swing.JTabbedPane bottomTabbedPane;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane intoolsComplianceSummaryScrollPane;
    private javax.swing.JSplitPane loopsTableAndTagsTreeSplitPanel;
    private javax.swing.JPanel loopsTablePanel;
    private javax.swing.JScrollPane loopsTablePanelScrollPane;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JScrollPane objectDataSourcesTreeScrollPane;
    private javax.swing.JMenuItem openStorageMenuItem;
    private javax.swing.JScrollPane overallComplianceSummaryScrollPane;
    private javax.swing.JScrollPane plantsTreeScrollPane;
    private org.jdesktop.swingx.JXStatusBar statusBar;
    private javax.swing.JLabel statusBarDatabaseNameLabel;
    private javax.swing.JTree tagsTree;
    private javax.swing.JScrollPane tagsTreeScrollPane;
    private javax.swing.JMenuItem versionMenuItem;
    private javax.swing.JSplitPane workspaceAndLogSplitPane;
    // End of variables declaration//GEN-END:variables
}// MainForm