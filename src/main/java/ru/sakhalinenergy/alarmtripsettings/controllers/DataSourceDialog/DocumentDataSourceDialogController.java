package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.config.DocumentDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceControllable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.DocumentDataSourceDialog;


/**
 * Implements controller for create/edit document data source dialog and its
 * model.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
public class DocumentDataSourceDialogController extends ManualSourceEditingDialogController
{
        
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Wrapped tags source instance
     * @param view Create/edit data source dialog
     */
    public DocumentDataSourceDialogController(TagsSourceControllable model, DocumentDataSourceDialog view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SAVE_SOURCE_DATA, new _SaveSourceButtonClickHandler());
    }// DocumentDataSourceDialogController
       
    
    /**
     * Internal class - handler for save data source to storage button click  
     * event.
     *
     * @author Denis Udovenko
     * @version 1.0.5
     */
    private class _SaveSourceButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            DocumentDataSourceDialog castedView = (DocumentDataSourceDialog)view;
            DocumentDataSourceDialogSettings viewSettings = DocumentDataSourceDialogSettings.getInstance();
            
            // Retrieve view settings to settings singleton object:
            viewSettings.setPlantCode(castedView.getPlant().getId());
            viewSettings.setComment(castedView.getComment());
            viewSettings.setDataSourceName(castedView.getDataSourceName());
            viewSettings.setDialogHeight(castedView.getFormHeigt());
            viewSettings.setDialogLeft(castedView.getFormX());
            viewSettings.setDialogTop(castedView.getFormY());
            viewSettings.setDialogWidth(castedView.getFormWidth());
            viewSettings.setDocumentLink(castedView.getDocumentLink());
            viewSettings.setDocumentNumber(castedView.getDocumemntNumber());
            viewSettings.setPriority(castedView.getPriority());
            viewSettings.setRevisionDate(castedView.getRevisionDateString());
            viewSettings.setTagFormat(castedView.getTagMask().getExample());
            
            // Set tags data source required attributes:
            Source modelEntity = model.getEntity();
            modelEntity.setTypeId(SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID);
            modelEntity.setPriority(Integer.parseInt(castedView.getPriority()));
            modelEntity.setName(castedView.getDataSourceName());
            modelEntity.setDate(castedView.getRevisionDate());
            
            SourceProperty property;
                    
            // Add document link:
            if (!viewSettings.getDocumentLink().trim().equals(""))
            {
                property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.DOCUMENT_LINK.ID);
                property.setValue(viewSettings.getDocumentLink());
                model.setProperty(property);
            }// if
            
            // Add document number:
            if (!viewSettings.getDocumentNumber().trim().equals(""))
            {
                property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.DOCUMENT_NUMBER.ID);
                property.setValue(viewSettings.getDocumentNumber());
                model.setProperty(property);
            }// if
            
            // Add document comment:
            if (!viewSettings.getComment().trim().equals(""))
            {
                property = new SourceProperty();
                property.setTypeId(SourcesPropertiesTypes.COMMENT.ID);
                property.setValue(viewSettings.getComment());
                model.setProperty(property);
            }// if
         
            // Save view config:
            viewSettings.save();
            
            // Save tags data source to storage:
            _saveDataSource(true);
        }// customEventOccurred
    }// _SaveSourceButtonClickHandler
}// DocumentDataSourceDialogController