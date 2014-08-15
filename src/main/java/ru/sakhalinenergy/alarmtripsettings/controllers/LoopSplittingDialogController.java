package ru.sakhalinenergy.alarmtripsettings.controllers;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.loop.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.loop.LoopSplittingDialog;


/**
 * Implements controller for loop splitting dialog. Unlike controlled view, uses 
 * LoopsTable instance as model because it's responsible for splitting loop
 * in database.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class LoopSplittingDialogController extends Controller
{
    private final LoopsTable model;
    private final LoopSplittingDialog view;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model LoopsTable to which view own model belongs
     * @param view Loop splitting dialog instance
     */
    public LoopSplittingDialogController(LoopsTable model, LoopSplittingDialog view)
    {
        this.model = model;
        this.view = view;
        
        view.on(ViewEvent.RUN_SPLITTING_BUTTON_CLICK, new _SplitLoopButtonHandler());
    }// LoopSplittingDialogController
    
    
    /**
     * Inner class - handler for split loop context menu item click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _SplitLoopButtonHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            List<Tag> tagsToSeperate = (List<Tag>)event.getSource();
            
            //Подписываемся на события прогоресса нити:
            /*model.on(THREAD_PROGRESS_EVENT, new CustomEventListener()
            {
                @Override
                public void customEventOccurred(CustomEvent evt)
                {
                    HashMap<Byte, Object> progress = (HashMap)evt.getSource();
                    oneProgressBarWithLogDialog.setProgressCaption((String)progress.get(PROGRESS_CYCLE_CAPTION_KEY));
                    oneProgressBarWithLogDialog.setProgress((Integer)progress.get(PROGRESS_CYCLE_PERCENTAGE_KEY));
                }//customEventOccurred
            });//addProgressEventListener*/
            
            // Subscribe on model's thread error event:
            model.off(CollectionEvent.THREAD_ERROR);
            model.on(CollectionEvent.THREAD_ERROR, new ThreadErrorEventHandler());
            
            //
            model.off(CollectionEvent.THREAD_WARNING);
            model.on(CollectionEvent.THREAD_WARNING, new CustomEventListener()
            { 
                @Override
                public void customEventOccurred(CustomEvent event)
                {
                    Object[] exceptionWrapper = (Object[])event.getSource();
                    String comment = (String)exceptionWrapper[0];
                    Exception exception = (Exception)exceptionWrapper[1];
                    //oneProgressBarWithLogDialog.addLogRecord(comment + ": " + exception);
                }// customEventOccurred
            });// on
            
            // Subscribe on model's loop split event:
            model.off(CollectionEvent.LOOP_SPLIT);
            model.on(CollectionEvent.LOOP_SPLIT, new CustomEventListener()
            { 
                @Override
                public void customEventOccurred(CustomEvent event)
                {
                    //oneProgressBarDialog.setVisible(false);
            
                    //
                    //DialogsFactory.produceStorageConnectionDialog(true);
                }// customEventOccurred
            });// on
            
            model.splitLoop(tagsToSeperate);
        }// customEventOccurred
    }// _SplitLoopButtonHandler
}// LoopSplittingDialogController
