package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.FgsVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog.FgsVariableTableDataSourceDialog;


/**
 * Implements controller for create/edit FGS Variable Table data source dialog 
 * and its model.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class FgsVariableTableDataSourceDialogController extends ManualSourceEditingDialogController
{
       
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags source instance
     * @param view Create/edit FGS Variable Table dialog
     */
    public FgsVariableTableDataSourceDialogController(TagsSourceControllable model, FgsVariableTableDataSourceDialog view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SAVE_SOURCE_DATA, new _SaveSourceButtonClickHandler());
    }// FgsVariableTableDataSourceDialogController
    
        
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
            FgsVariableTableDataSourceDialog castedView = (FgsVariableTableDataSourceDialog)view;
            FgsVariableTableDataSourceDialogSettings dialogSettings = FgsVariableTableDataSourceDialogSettings.getInstance();
            
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
            
            // Set up required tags source attributes:
            Source modelEntity = model.getEntity();
            modelEntity.setTypeId(SourcesTypes.FGS_VARIABLE_TABLE.ID);
            modelEntity.setPriority(Integer.parseInt(castedView.getPriority()));
            modelEntity.setName(castedView.getDataSourceName());
            modelEntity.setDate(castedView.getBackupDate());

            // Add comment to tags source, if exists:
            if (!dialogSettings.getComment().trim().equals(""))
            {
                SourceProperty property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.COMMENT.ID);
                property.setValue(dialogSettings.getComment());
                model.setProperty(property);
            }// if
            
            // Save view config:
            dialogSettings.save();
                                  
            // Save tags data source to storage:
            _saveDataSource(true);
        }// customEventOccurred
    }// _SaveSourceButtonClickHandler
}// FgsVariableTableDataSourceDialogController
