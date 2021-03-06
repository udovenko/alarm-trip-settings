package ru.sakhalinenergy.alarmtripsettings.controllers;

import java.awt.Component;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.PlantsTreePanelSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsTree;
import ru.sakhalinenergy.alarmtripsettings.models.storage.HibernateUtil;
import ru.sakhalinenergy.alarmtripsettings.views.panel.plants.PlantsTreePanel;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.ProgressInformationDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.storage.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.storage.StorageConnectionDialog;


/**
 * Implements controller for storage connection dialog.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class StorageConnectionDialogController extends Controller
{
    private final StorageConnectionDialog view;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param view Storage connection dialog instance
     */
    public StorageConnectionDialogController(StorageConnectionDialog view)
    {
        this.view = view;
        
        this.view.on(ViewEvent.SELECT_PATH_TO_SQLITE_DATABSE_BUTTON_CLICK, new _selectPathToSqliteDatabaseButtonClickHandler());
        this.view.on(ViewEvent.CONNECT_TO_STORAGE_BUTTON_CLICK, new _ConnectToStorageButtonClickHandler());
    }// StorageConnectionDialogController
    
    
    /**
     * Inner class - handler for "Select SQLite database path" button click 
     * event of view.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _selectPathToSqliteDatabaseButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Show file selection dialog and handling its result:
            FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database", "sqlite");
            String filepath = _showSelectPathToFileDialog("Select SQLite database file", filter, (Component)view);
            if (filepath != null) view.setSqliteDatabasePath(filepath);
        }// customEventOccurred
    }// _selectPathToSqliteDatabaseButtonClickHandler
    
    
    /**
     * Inner class - handler for view event for initiating connection to storage.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _ConnectToStorageButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            final StorageConnectionDialogSettings dialogSettings = StorageConnectionDialogSettings.getInstance();
            
            // Retrieve view config into config singleton object:
            dialogSettings.setMySqlDatabase(view.getMySqlStorageDatabaseName());
            dialogSettings.setMySqlHost(view.getMySqlStorageHost());
            dialogSettings.setMySqlPassword(view.getMySqlStoragePassword());
            dialogSettings.setMySqlPort(view.getMySqlStoragePort());
            dialogSettings.setMySqlUser(view.getMySqlStorageUserName());
            dialogSettings.setSqliteDatabasePath(view.getSqliteDatabasePath());
            dialogSettings.setStorageType(view.getStorageType());

            connectAndCreatePlantsTree(dialogSettings);
            
            // Hide dialog:
            view.close();
        }// customEventOccurred
    }// _ConnectToStorageButtonClickHandler
    
    
    /**
     * Tries to connect to storage and in case of success creates new plants
     * tree view and restores its selection and expansion state. In case of
     * failure shows back storage connection dialog.
     * 
     * @param connectionSettings Storage connection settings object
     */
    public void connectAndCreatePlantsTree(final StorageConnectionDialogSettings connectionSettings)
    {
        // Create plants tree model:
        final PlantsTree plantsTree = new PlantsTree();
        final PlantsTreePanelSettings plantsTreePanelSettings = PlantsTreePanelSettings.getInstance();
        
        // Create and render progress information dialog:
        final ProgressInformationDialog progressInformationDialog =
            new ProgressInformationDialog(plantsTree, CollectionEvent.THREAD_PROGRESS);
        progressInformationDialog.render("Progress information", Main.mainForm);
                        
        // Subscribe on plants tree view config loaded event:
        plantsTreePanelSettings.off(ConfigEvent.LOADED);
        plantsTreePanelSettings.on(ConfigEvent.LOADED, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                // Set up Hibernate session factory with loaded settings:
                HibernateUtil.setSessionFactory(connectionSettings);
                
                // Create plants tree panel view:
                PlantsTreePanel plantsTreePanel = new PlantsTreePanel(plantsTree, plantsTreePanelSettings);
                new PlantsTreePanelController(plantsTree, plantsTreePanel);
                Main.mainForm.setPlantsTreePanelContent(plantsTreePanel);
                
                // Subscribe on plants tree model's thread error event:
                plantsTree.off(CollectionEvent.THREAD_ERROR);
                plantsTree.on(CollectionEvent.THREAD_ERROR, new CustomEventListener()
                {
                    @Override
                    public void customEventOccurred(CustomEvent evt)
                    {
                        // Hide progress information dialog and show back storage connection dialog:
                        progressInformationDialog.close();
                        view.render(Main.mainForm);
                    }// customEventOccurred
                });// on
                
                // Subscribe on plants tree model's tree read event:
                plantsTree.on(CollectionEvent.TREE_READ, new CustomEventListener()
                {
                    @Override
                    public void customEventOccurred(CustomEvent evt)
                    {
                        // Hide progeress information dialog and save connection config:
                        connectionSettings.save();
                        progressInformationDialog.close();
                    }// customEventOccurred
                });// on
                
                plantsTree.fetch();
            }// customEventOccurred
        });// on
       
        // Execute plants tree panel view config loading:
        plantsTreePanelSettings.fetch();
    }// connectAndCreatePlantsTree
}// StorageConnectionDialogDispatcher
