package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import java.awt.Component;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.Progress.OneProgressBarDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.ManualSourceEditingDialogObservable;


/**
 * Abstract superclass for manual data sources manipulation dialogs controllers; 
 * implements common methods.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public abstract class ManualSourceEditingDialogController extends AutomaticSourceCreationDialogController
{
       
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags data source instance
     * @param view Dialog view instance
     */
    public ManualSourceEditingDialogController(TagsSourceControllable model, ManualSourceEditingDialogObservable view)
    {
        super(model, view);
        
        view.on(ViewEvent.TAG_NAME_INPUT,              new _TagNameInputHandler());
        view.on(ViewEvent.TAG_SETTING_ADD,             new _TagPropertyAddEventHandler());
        view.on(ViewEvent.TAG_SETTING_UPDATE,          new _TagPropertyUpdateEventHandler());
        view.on(ViewEvent.TAG_SETTING_DELETE,          new _TagPropertyDeleteEventHandler());
        view.on(ViewEvent.TAG_SETTING_PROPERTY_INPUT,  new _TagSettingPropertyInputEventHandler());
        view.on(ViewEvent.TAG_SETTING_PROPERTY_UPDATE, new _TagSettingPropertyUpdateEventHandler());
        view.on(ViewEvent.TAG_SETTING_PROPERTY_DELETE, new _TagSettingPropertyDeleteEventHandler());
        view.on(ViewEvent.REMOVE_TAG,                  new _RemoveTagEventHandler());
    }// DataSourceDialogController
        
        
    /**
     * Inner class - handler for new tag name input event of view.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagNameInputHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            String tagName = (String)evt.getSource();
            try
            {   
                model.addTag(tagName);
            } catch (Exception exception) {
                
                _handleWarning(exception.getMessage(), (Component)view);
            }// catch
        }// customEventOccurred
    }// _TagNameInputHandler
    
    
    /**
     * Inner class - handler for change tag format (mask) event of view.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _RemoveTagEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Tag tag = (Tag)event.getSource();
            model.removeTag(tag);
        }// customEventOccurred
    }// _RemoveTagEventHandler
    
    
    /**
     * Inner class - handler for add tag property event of view.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagPropertyAddEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] tagAndSettingPair = (Object[])event.getSource();
            model.addTagSetting((Tag)tagAndSettingPair[0], (TagSetting)tagAndSettingPair[1]);
        }// customEventOccurred
    }// _TagPropertyUpdateEventHandler
    
    
    /**
     * Inner class - handler for tag setting property input event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagSettingPropertyInputEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] settingAndSetrtingPropertyPair = (Object[])event.getSource();
            model.addTagSettingProperty((TagSetting)settingAndSetrtingPropertyPair[0],(TagSettingProperty)settingAndSetrtingPropertyPair[1]);
        }// customEventOccurred
    }// _TagSettingPropertyInputEventHandler    
    
    
    /**
     * Inner class - handler for existing tag setting delete event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagPropertyDeleteEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            TagSetting tagSettingToBeDeleted = (TagSetting)event.getSource();
            model.removeTagSetting(tagSettingToBeDeleted);
        }// customEventOccurred
    }// _TagPropertyDeleteEventHandler
    
    
    /**
     * Inner class - handler for tag setting property update event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagSettingPropertyUpdateEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] setrtingPropertyAndValuePair = (Object[])event.getSource();
            model.updateTagSettingPropertyValue((TagSettingProperty)setrtingPropertyAndValuePair[0],(String)setrtingPropertyAndValuePair[1]);
        }// customEventOccurred
    }// _TagSettingPropertyUpdateEventHandler    
    
    
    /**
     * Inner class - handler for existing setting value update event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagPropertyUpdateEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] settingAndValuePair = (Object[])event.getSource();
            model.updateTagSettingValue((TagSetting)settingAndValuePair[0], (String)settingAndValuePair[1]);
        }// customEventOccurred
    }// TagPropertyUpdateEventHandler
    
    
    /**
     * Inner class - handler for delete tag setting property event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagSettingPropertyDeleteEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            TagSettingProperty tagSettingPropertyToBeDeleted = (TagSettingProperty)event.getSource();
            model.removeTagSettingProperty(tagSettingPropertyToBeDeleted);
        }// customEventOccurred
    }// TagPropertyUpdateEventHandler
    
    
    /**
     * Shows progress-bar, launches model's thread to save wrapped data source 
     * and handles thread events.
     * 
     * @param createLoopsIfNotExist Flag defines a necessity of creations new loops and string tags within new loop during data source saving
     */
    protected void _saveDataSource(boolean createLoopsIfNotExist)
    {
        // Create and render dialog with single progress bar:
        model.off(SourceEvent.THREAD_PROGRESS);
        model.off(SourceEvent.SOURCE_SAVED);
        final OneProgressBarDialog oneProgressBarDialog = new OneProgressBarDialog(model, SourceEvent.THREAD_PROGRESS);
        oneProgressBarDialog.render("Saving data source to storage...", Main.mainForm);
            
        // Subscribe on model's thread error event:
        model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
          
        // Subscribe on model's data source saved event:
        model.on(SourceEvent.SOURCE_SAVED, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                oneProgressBarDialog.close();
            
                // Ask factory to create storage connection dialog and launch reconnection:
                DialogsFactory.produceStorageConnectionDialog(true);
            }// customEventOccurred
        });// on

        // Execute model's thread for saving tags source to storage:
        model.save(createLoopsIfNotExist);
    }// _saveDataSource
}// DataSourceDialogController