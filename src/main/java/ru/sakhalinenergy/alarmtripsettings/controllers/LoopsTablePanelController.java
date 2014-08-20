package ru.sakhalinenergy.alarmtripsettings.controllers;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import javax.swing.JOptionPane;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.config.LoopsTablePanelSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTable;
import ru.sakhalinenergy.alarmtripsettings.views.panel.loops.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.panel.loops.LoopsTablePanel;
import ru.sakhalinenergy.alarmtripsettings.views.panel.tags.TagsTreePanel;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.OneProgressBarDialog;


/**
 * Implements controller for loops table model of selected plants tree object.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class LoopsTablePanelController extends Controller
{
    private final LoopsTablePanel view;
    private final LoopsTable model;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Loops table model instance
     * @param view Loops table panel view instance
     */
    public LoopsTablePanelController(LoopsTable model, LoopsTablePanel view)
    {
        this.view = view;
        this.model = model;
        
        view.on(ViewEvent.CHANGE_LOOPS_TABLE_SELECTION,    new _LoopsTableChageSelectionHandler());
        view.on(ViewEvent.CHANGE_LOOPS_SEARCH_STRING,      new _LoopsSearchHandler());
        view.on(ViewEvent.SPLIT_LOOP_MENU_ITEM_CLICK,      new _SplitLoopMenuItemClickHandler());
        view.on(ViewEvent.MERGE_LOOP_MENU_ITEM_CLICK,      new _MergeLoopMenuItemClickHandler());
        view.on(ViewEvent.MERGE_ALL_LOOPS_MENU_ITEM_CLICK, new _MergeAllSplitLoopsMenuItemClickHandler());
    }// LoopsTablePanelController
    
    
    /**
     * Inner class - handler for select new loop in table event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _LoopsTableChageSelectionHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            SettingsSelector selectedLoopWraper = (SettingsSelector)event.getSource();
                    
            // Save view config:
            LoopsTablePanelSettings panelSettings = LoopsTablePanelSettings.getInstance();
            panelSettings.setSelectedLoop(view.getSelectedLoop());
            panelSettings.save();
            
            // Create and render tags tree panel for selected loop:
            TagsTreePanel tagsTreePanel = new TagsTreePanel(selectedLoopWraper);
            Main.mainForm.setTagsTreePanelContent(tagsTreePanel);
        }// customEventOccurred
    }// _LoopsTableChageSelectionHandler
    
    
    /**
     * Inner class - handler for loops search string change event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _LoopsSearchHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            String searchString = (String)evt.getSource();
            searchString = searchString.replace("*", "\\*");
            
            final String finalSearchString = searchString;
            
            java.awt.EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    LoopsTablePanelController.this.view.setSearchLoopsFilter(finalSearchString);
                }// run
            });// invokeLater
        }// customEventOccurred
    }// _LoopsSearchHandler
    
    
    /**
     * Inner class - handler for split selected loop context menu item click 
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _SplitLoopMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            Loop selectedLoop = (Loop)evt.getSource();
            DialogsFactory.produceSplitSelectedLoopDialog(selectedLoop, model);
        }// customEventOccurred
    }// _SplitLoopMenuItemClickHandler
    
    
    /**
     * Inner class - handler for merge selected loop context menu item click
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.4
     */
    private class _MergeLoopMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            // Get loop instance which will be merged with its copies:
            Loop loopToBeMerged = (Loop)evt.getSource();
                        
            // Show loop merging confirmation dialog:
            int answer = JOptionPane.showConfirmDialog(Main.mainForm,
                "Are you sure you want to merge all copies of " + loopToBeMerged.toString() + " into one loop?",
                "Action confirmation",
            JOptionPane.OK_CANCEL_OPTION);
            
            if (answer == JOptionPane.OK_OPTION)
            {
                Set<Loop> loops = new HashSet(Arrays.asList(loopToBeMerged));
                _mergeLoops(loops, "Merging loop " + loopToBeMerged.toString() + " copies...");
            }// if
        }// customEventOccurred
    }// _MergeLoopMenuItemClickHandler
    
    
    
    /**
     * Inner class - handler for merge all split loops context menu item click
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _MergeAllSplitLoopsMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            int answer = JOptionPane.showConfirmDialog(Main.mainForm,
                "Are you sure you want to merge all split loops within selected object?",
                "Action confirmation",
            JOptionPane.OK_CANCEL_OPTION);
            
            if (answer == JOptionPane.OK_OPTION)
            {
                _mergeLoops(model.getSplitLoops(), "Merging all split loops for selected PAU object...");
            }//if
        }// customEventOccurred
    }// _MergeAllSplitLoopsMenuItemClickHandler
    
    
    /**
     * Creates progress bar, subscribes on model's events and executes model's
     * method for merging copies of loops, given in input set.
     * 
     * @param loops Each loop of this set will be merged with its copies
     * @param progressBarTitle Progress bar dialog title which will be shown during process
     */
    private void _mergeLoops(Set<Loop> loops, String progressBarTitle)
    {
        // Subscribe on model's thread error event:
        model.off(CollectionEvent.THREAD_ERROR);
        model.on(CollectionEvent.THREAD_ERROR, new ThreadErrorEventHandler());

        // Create and render dialog with single progress bar:
        model.off(CollectionEvent.THREAD_PROGRESS);
        final OneProgressBarDialog oneProgressBarDialog = new OneProgressBarDialog(model, CollectionEvent.THREAD_PROGRESS);
        oneProgressBarDialog.render(progressBarTitle, Main.mainForm);

        // Subscribe on model's loops merged event:
        model.off(CollectionEvent.LOOPS_MERGED);
        model.on(CollectionEvent.LOOPS_MERGED, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {   
                oneProgressBarDialog.close();

                // Ask factory to produce storage connection dialog and start reconnection:
                DialogsFactory.produceStorageConnectionDialog(true);
            }// customEventOccurred
        });// addSourceRemovedEventListener

        // Excecute loops merging:
        model.mergeLoops(loops);
    }// _splitLoops
}// LoopsTablePanelController
