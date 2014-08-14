package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.models.config.IntoolsExportDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog.IntoolsExportDataSourceDialog;


/**
 * Implements controller for create/edit SPI export data source dialog and its 
 * model.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class IntoolsExportDataSourceDialogController extends ManualSourceEditingDialogController
{
       
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags source instance
     * @param view Create/edit SPI export dialog
     */
    public IntoolsExportDataSourceDialogController(TagsSourceControllable model, IntoolsExportDataSourceDialog view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SAVE_SOURCE_DATA, new _SaveSourceButtonClickHandler());
    }// IntoolsExportDataSourceDialogController
    
        
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
            IntoolsExportDataSourceDialog castedView = (IntoolsExportDataSourceDialog)view;
            IntoolsExportDataSourceDialogSettings dialogSettings = IntoolsExportDataSourceDialogSettings.getInstance();
            
            // Retrieve view config into config singleton object:
            dialogSettings.setPlantCode(castedView.getPlant().getId());
            dialogSettings.setComment(castedView.getComment());
            dialogSettings.setDataSourceName(castedView.getDataSourceName());
            dialogSettings.setDatabaseName(castedView.getDatabaseName());
            dialogSettings.setDialogHeight(castedView.getFormHeigt());
            dialogSettings.setDialogLeft(castedView.getFormX());
            dialogSettings.setDialogTop(castedView.getFormY());
            dialogSettings.setDialogWidth(castedView.getFormWidth());
            dialogSettings.setExportDate(castedView.getExportDateString());
            dialogSettings.setPriority(castedView.getPriority());
            dialogSettings.setTagFormat(castedView.getTagMask().getExample());
            
            // Set up required tags source attributes:
            Source modelEntity = model.getEntity();
            modelEntity.setTypeId(SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID);
            modelEntity.setPriority(Integer.parseInt(castedView.getPriority()));
            modelEntity.setName(castedView.getDataSourceName());
            modelEntity.setDate(castedView.getExportDate());
                 
            SourceProperty property;
                    
            // Add source database name, if exists:
            if (!dialogSettings.getDatabaseName().trim().equals(""))
            {
                property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.DATABASE_NAME.ID);
                property.setValue(dialogSettings.getDatabaseName());
                model.setProperty(property);
            }// if
            
            // Add comment, if exists:
            if (!dialogSettings.getComment().trim().equals(""))
            {
                property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.COMMENT.ID);
                property.setValue(dialogSettings.getComment());
                model.setProperty(property);
            }// if
            
            // Save view config:
            dialogSettings.save();
            
            // Savie tags data source to storage:
            _saveDataSource(true);
        }// customEventOccurred
    }// _SaveSourceButtonClickHandler    
}// IntoolsExportDataSourceDialogController