package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.AutomaticSourceCreationDialogObservable;
import ru.sakhalinenergy.alarmtripsettings.controllers.Controller;


/**
 * Abstract superclass for automatic data sources creating controllers, 
 * implements common methods.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
abstract public class AutomaticSourceCreationDialogController extends Controller
{
    protected final TagsSourceControllable model;
    protected final AutomaticSourceCreationDialogObservable view;
    
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped source instance
     * @param view View instance
     */
    public AutomaticSourceCreationDialogController(TagsSourceControllable model, AutomaticSourceCreationDialogObservable view)
    {
        this.model = model;
        this.view = view;
        
        view.on(ViewEvent.CHANGE_PLANT_SELECTION, new _PlantChangeHandler());
        view.on(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, new _TagFormatChangeHandler());
    }// AutomaticSourceCreationDialogController
    
    
    /**
     * Inner class - handler for change plant event.
     * 
     * @author Denis.Udovenko
     * @version 1.0.1
     */
    private class _PlantChangeHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {   System.out.println("set plant!");
            Plant plant = (Plant)event.getSource();
            model.setPlant(plant);
        }// customEventOccurred
    }// PlantChangeHandler
       
    
    /**
     * Inner class - handler tag format (mask) change event.
     *  
     * @author Denis.Udovenko
     * @version 1.0.1
     */
    private class _TagFormatChangeHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {   
            TagMask tagMask = (TagMask)event.getSource();
            model.setTagMask(tagMask);
        }// customEventOccurred
    }// TagFormatChangeHandler
}// AutomaticSourceCreationDialogController
