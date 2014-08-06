package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.DcsVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog.DcsVariableTableDataSourceDialog;


/**
 * Implements controller for create/edit DCS Variable Table data source dialog 
 * and its model.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class DcsVariableTableDataSourceDialogController extends ManualSourceEditingDialogController
{
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags source instance
     * @param view Create/edit DCS Variable Table dialog
     */
    public DcsVariableTableDataSourceDialogController(TagsSourceControllable model, 
        DcsVariableTableDataSourceDialog view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SAVE_SOURCE_DATA, new _SaveSourceButtonClickHandler());
    }// DcsVariableTableDataSourceDialog
    
        
    /**
     * Inner class - handler for save new data source to storage button click
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
            DcsVariableTableDataSourceDialog castedView = (DcsVariableTableDataSourceDialog)view;
            DcsVariableTableDataSourceDialogSettings dialogSettings = DcsVariableTableDataSourceDialogSettings.getInstance();
            
            // Retrieve view config into config singleton object:
            dialogSettings.setPlantCode(castedView.getPlant().getId());
            dialogSettings.setBackupDate(castedView.getBackupDateAsString());
            dialogSettings.setComment(castedView.getComment());
            dialogSettings.setDataSourceName(castedView.getDataSourceName());
            dialogSettings.setDialogHeight(castedView.getFormHeigt());
            dialogSettings.setDialogLeft(castedView.getFormX());
            dialogSettings.setDialogTop(castedView.getFormY());
            dialogSettings.setDialogWidth(castedView.getFormWidth());
            dialogSettings.setPriority(castedView.getPriority());
            dialogSettings.setTagFormat(castedView.getTagMask().getExample());
            dialogSettings.setCreateLoopsIfNotExistFlag(castedView.getCreateLoopsIfNotExistFlag());
            
            // Set up required tags source attributes:
            Source modelEntity = model.getEntity();
            modelEntity.setTypeId(SourcesTypes.DCS_VARIABLE_TABLE.ID);
            modelEntity.setPriority(Integer.parseInt(castedView.getPriority()));
            modelEntity.setName(castedView.getDataSourceName());
            modelEntity.setDate(castedView.getBackupDate());

            // Add tags source comment:
            if (!dialogSettings.getComment().trim().equals(""))
            {
                SourceProperty property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.COMMENT.ID);
                property.setValue(dialogSettings.getComment());
                model.setProperty(property);
            }//if
                      
            // Save view config:
            dialogSettings.save();
                      
            // Save tags source data to storage:
            _saveDataSource(new Boolean(dialogSettings.getCreateLoopsIfNotExistFlag()));
        }// customEventOccurred
    }// _SaveSourceButtonClickHandler   
}// DcsVariableTableDataSourceDialogController
