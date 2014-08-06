package ru.sakhalinenergy.alarmtripsettings.controllers;

import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.MainFormSettings;
import ru.sakhalinenergy.alarmtripsettings.views.AboutDialog;
import ru.sakhalinenergy.alarmtripsettings.views.MainForm.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.MainForm.MainForm;


/**
 * Implements controller for application main form.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class MainFormController 
{
    private final MainForm view;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param view Main form view instance
     */
    public MainFormController(MainForm view)
    {
        this.view = view;
        this.view.events.on(ViewEvent.FORM_CLOSING,            new _SaveUiSettingsRequestListener());
        this.view.events.on(ViewEvent.CONNECT_MENU_ITEM_CLICK, new _OpenConnectToStorageFormRequestHandler());
        this.view.events.on(ViewEvent.VERSION_MENU_ITEM_CLICK, new _OpenVersionDialogRequestHandler());
    }// MainFormController
    
    
    /**
     * Inner class - handler for main form save UI settings request event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _SaveUiSettingsRequestListener implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Get main form config singleton object:
            final MainFormSettings mainFormSettings = MainFormSettings.getInstance();
            
            // Retrieve main form config into config singleton object:
            mainFormSettings.setWindowMaximized(view.isMaximized());
            mainFormSettings.setWindowLeft(view.getFormX());
            mainFormSettings.setWindowTop(view.getFormY());
            mainFormSettings.setWindowWidth(view.getFormWidth());
            mainFormSettings.setWindowHeight(view.getFormHeigt());
            mainFormSettings.setMaximizedWindowWorkspaceHeight(view.getMaximizedWindowWorkspaceHeight());
            mainFormSettings.setMaximizedWindowAssetsTreeWidth(view.getMaximizedWindowAssetsTreeWidth());
            mainFormSettings.setMaximizedWindowTagDetailsTreeWidth(view.getMaximizedWindowTagDetailsTreeWidth());
            mainFormSettings.setMinimizedWindowWorkspaceHeight(view.getMinimizedWindowWorkspaceHeight());
            mainFormSettings.setMinimizedWindowAssetsTreeWidth(view.getMinimizedWindowAssetsTreeWidth());
            mainFormSettings.setMinimizedWindowTagDetailsTreeWidth(view.getMinimizedWindowTagDetailsTreeWidth());
            mainFormSettings.setActiveBottomTab(view.getActiveBottomTab());

            // Subscribe on config save event:
            mainFormSettings.off(ConfigEvent.SAVED);
            mainFormSettings.on(ConfigEvent.SAVED, new CustomEventListener()
            {
                @Override
                public void customEventOccurred(CustomEvent evt)
                {
                    // Close application:
                    System.exit(0);
                }// customEventOccurred
            });// on
            
            // Execute config save thread:
            mainFormSettings.save();
        }// customEventOccurred
    }// _SaveUiSettingsRequestListener
     
    
    /**
     * Inner class - handler for show storage connection dialog menu item click
     * event.
     * 
     * @author Denis Udovenko
     * @version  1.0.3
     */
    private class _OpenConnectToStorageFormRequestHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Ask factory to produce storage connection dialog without immediately reconnection:
            DialogsFactory.produceStorageConnectionDialog(false);
        }// customEventOccurred
    }// _OpenConnectToStorageFormRequestHandler
    
    
    /**
     * Inner class - handler for show about dialog menu item click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _OpenVersionDialogRequestHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Create about dialog:
            final AboutDialog aboutDialog = new AboutDialog();
            
            // Set up version date, number and author:
            aboutDialog.setVersion(Main.VERSION);
            aboutDialog.setDate(Main.LAST_CHANGES_DATE + " " + Main.LAST_CHANGES_TIME);
            aboutDialog.setAuthor(Main.AUTHOR);
            
            java.awt.EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    // Render dialog relative to main form:
                    aboutDialog.setLocationRelativeTo(Main.mainForm);
                    aboutDialog.setVisible(true);
                }// run
            });// invokeLater
        }// customEventOccurred
    }// _OpenVersionDialogRequestHandler
}// MainFormController
    


