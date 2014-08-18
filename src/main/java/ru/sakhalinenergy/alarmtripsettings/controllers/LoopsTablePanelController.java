package ru.sakhalinenergy.alarmtripsettings.controllers;

import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.config.LoopsTablePanelSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTable;
import ru.sakhalinenergy.alarmtripsettings.views.panel.loops.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.panel.loops.LoopsTablePanel;
import ru.sakhalinenergy.alarmtripsettings.views.TagsTreePanel.TagsTreePanel;


/**
 * Implements controller for loops table model of selected plants tree object.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class LoopsTablePanelController 
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
     * @version 1.0.3
     */
    private class _MergeLoopMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            //Получаем экземпляр петли, копии которой будем объединять:
            /*int loopIndex = (Integer)evt.getSource();
            final DbLoop loopToBeMerged = Main.loops.get(loopIndex);
            
            //Выводим диалог для подтверждения объединения петель:
            int answer = JOptionPane.showConfirmDialog(Main.mainForm,
                "Are you sure you want to merge all copies of " + loopToBeMerged.toString() + " into one loop?",
                "Action confirmation",
            JOptionPane.OK_CANCEL_OPTION);
            
            if (answer == JOptionPane.OK_OPTION)
            {
                List<DbLoop> loops = new ArrayList(Arrays.asList(loopToBeMerged));
                
                //Добавляем обработчик события заврешения сохранения источника данных и всей связанной с ним информации:
                Main.loopSplittingAndMergingController.events.off(LoopSplittingAndMergingController.LOOPS_MERGED_EVENT);
                Main.loopSplittingAndMergingController.events.on(LoopSplittingAndMergingController.LOOPS_MERGED_EVENT, new CustomEventListener()
                {
                    @Override
                    public void customEventOccurred(CustomEvent evt)
                    {   
                        Main.storageConnectionController.connectToStorage();
                    }//customEventOccurred
                });//addSourceRemovedEventListener
            
                //Вызываем метод контроллера:
                Main.loopSplittingAndMergingController.mergeLoops(loops);
            }//if*/
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
            /*int answer = JOptionPane.showConfirmDialog(Main.mainForm,
                "Are you sure you want to merge all split loops within selected object?",
                "Action confirmation",
            JOptionPane.OK_CANCEL_OPTION);
            
            if (answer == JOptionPane.OK_OPTION)
            {
                //Добавляем обработчик события заврешения сохранения источника данных и всей связанной с ним информации:
                Main.loopSplittingAndMergingController.events.off(LoopSplittingAndMergingController.LOOPS_MERGED_EVENT);
                Main.loopSplittingAndMergingController.events.on(LoopSplittingAndMergingController.LOOPS_MERGED_EVENT, new CustomEventListener()
                {
                    @Override
                    public void customEventOccurred(CustomEvent evt)
                    {   
                        Main.storageConnectionController.connectToStorage();
                    }//customEventOccurred
                });//addSourceRemovedEventListener
            
                //Вызываем метод контроллера:
                Main.loopSplittingAndMergingController.mergeLoops(Main.loops);
            }//if*/
        }// customEventOccurred
    }// _MergeAllSplitLoopsMenuItemClickHandler
}// LoopsTablePanelController
