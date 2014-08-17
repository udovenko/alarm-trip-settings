package ru.sakhalinenergy.alarmtripsettings.controllers;

import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsTree;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.PlantsTreePanelSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.LoopsTablePanelSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.OveralCompliance;
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.IntoolsCompliance;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.OneProgressBarDialog;
import ru.sakhalinenergy.alarmtripsettings.views.PlantsTreePanel.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.PlantsTreePanel.PlantsTreePanel;
import ru.sakhalinenergy.alarmtripsettings.views.LoopsTablePanel.LoopsTablePanel;
import ru.sakhalinenergy.alarmtripsettings.views.panel.sources.DataSourcesPanel;
import ru.sakhalinenergy.alarmtripsettings.views.OverallComplianceSummaryPanel;
import ru.sakhalinenergy.alarmtripsettings.views.IntoolsComplianceSummaryPanel;


/**
 * Implements controller for plants tree panel.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class PlantsTreePanelController
{
    private final PlantsTreePanel view;
    private final PlantsTree model;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Plants tree model instance
     * @param view Plants tree panel view instance
     */
    public PlantsTreePanelController(PlantsTree model, PlantsTreePanel view)
    {
        this.model = model;
        this.view = view;
        this.view.events.on(ViewEvent.PLANTS_TREE_NODE_SELECTION, new _GetLoopsForSelectedNodeRequestListener());
        this.view.events.on(ViewEvent.PLANTS_TREE_EXPANSION_STATE_CHANGE, new _PlantsTreeExpansionStateChangeHandler()); 
    }// PlantsTreePanelController
    
    
    /**
     * Inner class - handler for plants tree node selection event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _GetLoopsForSelectedNodeRequestListener implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Save tree view config:
            _savePlantsTreeConfig();
            
            Object nodeObject = (Object)evt.getSource();
            String[] filters = null;
            
            // Get loop table filter depending on selected node class:
            if (nodeObject.getClass() == Plant.class)
            {    
                Plant plantNode = (Plant)nodeObject;
                filters = new String[]{plantNode.getId()};
            } else if (nodeObject.getClass() == TreeArea.class) {    
                
                TreeArea areaNode = (TreeArea)nodeObject;
                filters = new String[]{areaNode.getPlant(), areaNode.getName()};
            } else if (nodeObject.getClass() == TreeUnit.class) {  
                
                TreeUnit unitNode = (TreeUnit)nodeObject;
                filters = new String[]{unitNode.getPlant(), unitNode.getArea(), unitNode.getName()};
            }//if
                      
            final String[] filtersFinal = filters;
            
            // Get loops table view config sngleton and define config load callback:
            final LoopsTablePanelSettings loopsTablePanelSettings = LoopsTablePanelSettings.getInstance();
            loopsTablePanelSettings.off(ConfigEvent.LOADED);
            loopsTablePanelSettings.on(ConfigEvent.LOADED, new CustomEventListener()
            {
                @Override
                public void customEventOccurred(CustomEvent evt)
                {
                    LoopsTable loopsCollection = new LoopsTable();
                    
                    // Create loops table panel view:
                    LoopsTablePanel loopsTablePanel = new LoopsTablePanel(loopsCollection, loopsTablePanelSettings);
                    new LoopsTablePanelController(loopsCollection, loopsTablePanel);
                    Main.mainForm.setDevicesTableAndTaskbarPanelContent(loopsTablePanel);
                    
                    // Create data sources tree panel:
                    DataSourcesPanel dataSourcesPanel = new DataSourcesPanel(loopsCollection);
                    new DataSourcesPanelController(dataSourcesPanel);
                    Main.mainForm.setDataSourcesPanelContent(dataSourcesPanel);
                    
                    // Create overall compliance summary panel:
                    OveralCompliance overalComplianceSummary = new OveralCompliance(loopsCollection);
                    OverallComplianceSummaryPanel overallComplianceSummaryPanel = new OverallComplianceSummaryPanel(overalComplianceSummary);
                    Main.mainForm.setOverallComplianceSummaryPanelContent(overallComplianceSummaryPanel);
                    
                    // Create SPI compliance summary panel:
                    IntoolsCompliance intoolsComplianceSummary = new IntoolsCompliance(loopsCollection);
                    IntoolsComplianceSummaryPanel intoolsComplianceSummaryPanel = new IntoolsComplianceSummaryPanel(intoolsComplianceSummary);
                    Main.mainForm.setIntoolsComplianceSummaryPanelContent(intoolsComplianceSummaryPanel);
                                     
                    // Create dialog with single progress bap for process visualization:
                    loopsCollection.off(CollectionEvent.THREAD_PROGRESS);
                    final OneProgressBarDialog oneProgressBarDialog = new OneProgressBarDialog(loopsCollection, CollectionEvent.THREAD_PROGRESS);
                    oneProgressBarDialog.render("Getting loops for selected PAU object", Main.mainForm); 
                                                            
                    // Subscribe on loops table model's loops read event:
                    loopsCollection.on(CollectionEvent.LOOPS_READ, new CustomEventListener()
                    {
                        @Override
                        public void customEventOccurred(CustomEvent evt)
                        {
                            oneProgressBarDialog.close();
                        }// customEventOccurred
                    });// on
                    
                    // Execute loops table collection fetch thread:
                    loopsCollection.fetch(filtersFinal);
                }// customEventOccurred
            });// on
            
            // Execute config loading:
            loopsTablePanelSettings.fetch();
        }// customEventOccurred
    }// _GetDevicesForSelectedNodeRequestListener
    
    
    /**
     * Inner class - handler for plants tree expansion state change event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _PlantsTreeExpansionStateChangeHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Save plants tree config:
            _savePlantsTreeConfig();
        }// customEventOccurred
    }// _PlantsTreeExpansionStateChangeHandler
    
    
    /**
     * Retrieves plants tree view config into config singleton object and saves
     * it.
     */
    private void _savePlantsTreeConfig()
    {
        PlantsTreePanelSettings panelSettings = PlantsTreePanelSettings.getInstance();
        String[] treeExppansionState = view.getTreeExpansionState();
        panelSettings.setExpandedPlants(treeExppansionState[0]);
        panelSettings.setExpandedAreas(treeExppansionState[1]);
        panelSettings.save();
    }// _savePlantsTreeConfig
}// PlantsTree
