package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.EsdVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems.EsdVariableTableDataSourceDialog;


/**
 * Implements controller for create/edit ESD Variable Table data source dialog 
 * and its model.
 *
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class EsdVariableTableDataSourceDialogController extends ManualSourceEditingDialogController
{
        
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags source instance
     * @param view Create/edit ESD Variable Table dialog
     */
    public EsdVariableTableDataSourceDialogController(TagsSourceControllable model,
        EsdVariableTableDataSourceDialog view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SAVE_SOURCE_DATA, new _SaveSourceButtonClickHandler());
    }// EsdVariableTableDataSourceDialogController
    
    
    /**
     * Inner class - handler for save tags data source to storage button click 
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _SaveSourceButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            EsdVariableTableDataSourceDialog castedView = (EsdVariableTableDataSourceDialog)view; 
            EsdVariableTableDataSourceDialogSettings dialogSettings = EsdVariableTableDataSourceDialogSettings.getInstance();
            
            // Retireve view config into config singleton object:
            dialogSettings.setPlantCode(castedView.getPlant().getId());
            dialogSettings.setBackupDate(castedView.getBackupDateAsString());
            dialogSettings.setComment(castedView.getComment());
            dialogSettings.setDataSourceName(castedView.getDataSourceName());
            dialogSettings.setDialogHeight(castedView.getDialogHeight());
            dialogSettings.setDialogLeft(castedView.getDialogLeft());
            dialogSettings.setDialogTop(castedView.getDialogTop());
            dialogSettings.setDialogWidth(castedView.getDialogWidth());
            dialogSettings.setPriority(castedView.getPriority());
            dialogSettings.setTagFormat(castedView.getTagMask().getExample());
            
            // Set up necessary tags source attributes:
            Source modelEntity = model.getEntity();
            modelEntity.setTypeId(SourcesTypes.ESD_VARIABLE_TABLE.ID);
            modelEntity.setPriority(Integer.parseInt(castedView.getPriority()));
            modelEntity.setName(castedView.getDataSourceName());
            modelEntity.setDate(castedView.getBackupDate());

            // Add source comment, if exists:
            if (!dialogSettings.getComment().trim().equals(""))
            {
                SourceProperty property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.COMMENT.ID);
                property.setValue(dialogSettings.getComment());
                model.setProperty(property);
            }// if
            
            // Save view config:
            dialogSettings.save();
            
            // Saving tags data source to storage:
            _saveDataSource(true);
        }// customEventOccurred
    }// _SaveSourceButtonClickHandler
}// EsdVariableTableDataSourceDialogController
