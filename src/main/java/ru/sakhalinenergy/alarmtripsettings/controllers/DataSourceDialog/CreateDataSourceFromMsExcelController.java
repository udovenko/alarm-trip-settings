package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.ExcelBookControlable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSource;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.views.Progress.ProgressInformationDialog;
import ru.sakhalinenergy.alarmtripsettings.views.Progress.OneProgressBarWithLogDialog;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDataSourceFromExcelDialog.CreateDataSourceFromExcelDialogObservable;


/**
 * Implements controller for creating data source from MS Excel book.
 * 
 * @author Denis Udovenko
 * @version 1.0.7
 */
public class CreateDataSourceFromMsExcelController extends AutomaticSourceCreationDialogController
{
  
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model MS Excel book model
     * @param view Dialog view for creating MS Excel data source
     */
    public CreateDataSourceFromMsExcelController(ExcelBookControlable model, CreateDataSourceFromExcelDialogObservable view)
    {
        super(model, view);
        
        this.view.on(ViewEvent.SELECT_PATH_TO_SPREADSHEET_TEXT_FIELD_BUTTON_CLICK, new _SetPathToSpreadsheetButtonClickHandler());
        this.view.on(ViewEvent.RUN_SPREADSHEET_PARSING_BUTTON_CLICK, new _RunParsingButtonClickEventHandler());
        this.view.on(ViewEvent.DIALOG_CLOSING, new _DialogClosingHandler());
    }// CreateDataSourceFromMsExcelDialogController
        
    
    /**
     * Retrieves view settings into MS Excel dialog settings singleton object. 
     */
    private void _retrieveViewSettings()
    {
        CreateDataSourceFromExcelDialogObservable castedView = (CreateDataSourceFromExcelDialogObservable)view;
        ExcelBookParsingDialogSettings settingsInstance = ExcelBookParsingDialogSettings.getInstance();
        
        settingsInstance.setPlantCode(castedView.getPlant().getId());
        settingsInstance.setDataSourceTypeName(castedView.getDataSourceType());
        settingsInstance.setTagFormat(castedView.getTagMask().getExample());
            
        settingsInstance.setSheetName(castedView.getSheetName());
        settingsInstance.setTagNameField(castedView.getTagFieldName());
            
        settingsInstance.setAlarmLowLowKeyField(castedView.getAlarmLowLowKeyField());
        settingsInstance.setAlarmLowLowKeyValue(castedView.getAlarmLowLowKeyValue());
        settingsInstance.setAlarmLowLowValueField(castedView.getAlarmLowLowValueField());
        settingsInstance.setAlarmLowLowPossibleFlagField(castedView.getAlarmLowLowPossibleFlagField());
            
        settingsInstance.setAlarmLowKeyField(castedView.getAlarmLowKeyField());
        settingsInstance.setAlarmLowKeyValue(castedView.getAlarmLowKeyValue());
        settingsInstance.setAlarmLowValueField(castedView.getAlarmLowValueField());
        settingsInstance.setAlarmLowPossibleFlagField(castedView.getAlarmLowPossibleFlagField());
            
        settingsInstance.setAlarmHighKeyField(castedView.getAlarmHighKeyField());
        settingsInstance.setAlarmHighKeyValue(castedView.getAlarmHighKeyValue());
        settingsInstance.setAlarmHighValueField(castedView.getAlarmHighValueField());
        settingsInstance.setAlarmHighPossibleFlagField(castedView.getAlarmHighPossibleFlagField());
            
        settingsInstance.setAlarmHighHighKeyField(castedView.getAlarmHighHighKeyField());
        settingsInstance.setAlarmHighHighKeyValue(castedView.getAlarmHighHighKeyValue());
        settingsInstance.setAlarmHighHighValueField(castedView.getAlarmHighHighValueField());
        settingsInstance.setAlarmHighHighPossibleFlagField(castedView.getAlarmHighHighPossibleFlagField());
            
        settingsInstance.setRangeMinKeyField(castedView.getRangeMinKeyField());
        settingsInstance.setRangeMinKeyValue(castedView.getRangeMinKeyValue());
        settingsInstance.setRangeMinValueField(castedView.getRangeMinValueField());
            
        settingsInstance.setRangeMaxKeyField(castedView.getRangeMaxKeyField());
        settingsInstance.setRangeMaxKeyValue(castedView.getRangeMaxKeyValue());
        settingsInstance.setRangeMaxValueField(castedView.getRangeMaxValueField());
            
        settingsInstance.setUnitsKeyField(castedView.getUnitsKeyField());
        settingsInstance.setUnitsKeyValue(castedView.getUnitsKeyValue());
        settingsInstance.setUnitsValueField(castedView.getUnitsValueField());
            
        settingsInstance.setSourceSystemKeyField(castedView.getSourceSystemKeyField());
        settingsInstance.setSourceSystemKeyValue(castedView.getSourceSystemKeyValue());
        settingsInstance.setSourceSystemValueField(castedView.getSourceSystemValueField());
    }// _retrieveViewSettings
    
    
    /**
     * Inner class - handler for select backup folder button click event.
     *
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _SetPathToSpreadsheetButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {   
            // Create select directory path dialog:
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Source MS Excel book", "xls");
            String filepath = _showSelectPathToFileDialog("Select MS Excel book", filter, (Component)view);

            // Handle dirrectory choosing result:
            if (filepath != null)
            {
                // Create and render progress information dialog:
                model.off(SourceEvent.THREAD_PROGRESS);
                final ProgressInformationDialog progressInformationDialog =
                    new ProgressInformationDialog(model, SourceEvent.THREAD_PROGRESS);
                progressInformationDialog.render("Progress information", Main.mainForm);
      
                // Subscribe on model's thread error event:
                model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
                              
                // Subscribe on model's book connection and structure read event:
                model.on(SourceEvent.BOOK_CONNECTED, new CustomEventListener()
                { 
                    @Override
                    public void customEventOccurred(CustomEvent event)
                    {
                        progressInformationDialog.close();
                    }// customEventOccurred
                });// on
                        
                //Получаем список листов и их полей:
                ((ExcelBookControlable)model).connectAndReadStructure(filepath);
            }// if
        }// customEventOccurred
    }// _SetPathToSpreadsheetButtonClickHandler

    
    /**
     * Inner class - handler for start MS Excel book parsing button click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _RunParsingButtonClickEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Save current view config:
            _retrieveViewSettings();
            ExcelBookParsingDialogSettings.getInstance().save();
            
            // Create and render progress bar with log dialog:
            model.off(SourceEvent.THREAD_PROGRESS);
            model.off(SourceEvent.THREAD_WARNING);
            final OneProgressBarWithLogDialog oneProgressBarWithLogDialog = 
                new OneProgressBarWithLogDialog(model, SourceEvent.THREAD_PROGRESS, SourceEvent.THREAD_WARNING);
            oneProgressBarWithLogDialog.render("Reading tags from MS Excel list", Main.mainForm);
            
            // Subscribe on model's thread error event:
            model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
              
            // Subscribe on model's MS Excel tags read event:
            model.on(SourceEvent.TAGS_READ, new CustomEventListener()
            { 
                @Override
                public void customEventOccurred(CustomEvent event)
                {
                    // If log is not empty, notyfy user and copy it to buffer:
                    String log = oneProgressBarWithLogDialog.getLog();
                    if (!log.trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog((Component)view, 
                            "There is a number of exceptions appeared during parsing. Parsing log will be copied to buffer.", 
                            "Attention", JOptionPane.INFORMATION_MESSAGE);
                        _copyToClipboard(log);
                    }// if                 
                    
                    // Close progress bar:
                    oneProgressBarWithLogDialog.close();
                    
                    // Detect data source type:
                    int dataSourceTypeId = 0;
                    try
                    {
                        Class SourcesType = SourcesTypes.getSourceTypeByName(((CreateDataSourceFromExcelDialogObservable)view).getDataSourceType());
                        dataSourceTypeId = (Integer)SourcesType.getDeclaredField(SourcesTypes.ID_FIELD_NAME).get(null);
                    
                    } catch (Exception exception) {
                    
                        _handleWarning(exception.getMessage(), (Component)view);
                    }// catch    
                                                 
                    // Open suitable data source dialog with received initial tags set:
                    if (dataSourceTypeId == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID)
                        DialogsFactory.produceDocumentDataSourceDialog((TagsSource)model, false, "Confirm document data source cretaed from MS Excel book");
                    
                    if (dataSourceTypeId == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID)
                        DialogsFactory.produceIntoolsExportDataSourceDialog((TagsSource)model, false, "Confirm Intools export data source cretaed from MS Excel book");
                    
                    if (dataSourceTypeId == SourcesTypes.DCS_VARIABLE_TABLE.ID)
                        DialogsFactory.produceDcsVariableTableDataSourceDialog((TagsSource)model, true, "Edit selected DCS Variable Table data source");
                                        
                    if (dataSourceTypeId == SourcesTypes.ESD_VARIABLE_TABLE.ID)
                        DialogsFactory.produceEsdVariableTableDataSourceDialog((TagsSource)model, true, "Edit selected ESD Variable Table data source");
                                        
                    if (dataSourceTypeId == SourcesTypes.FGS_VARIABLE_TABLE.ID)
                        DialogsFactory.produceEsdVariableTableDataSourceDialog((TagsSource)model, true, "Edit selected FGS Variable Table data source");
                }// customEventOccurred
            });// on

            ((ExcelBookControlable)model).readTags(view.getTagMask());
        }// customEventOccurred
    }// _RunParsingButtonClickEventHandler
    
    
    /**
     * Inner class - handler for dialog closing event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _DialogClosingHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            try
            {
                // If MS Excel book connection is still open - close it:
                ExcelBookControlable castedModel = (ExcelBookControlable)model;
                if (castedModel.isConnected()) castedModel.disconnect();
                        
            } catch (Exception exception) {
                        
                // Render connection closing error message:
                JOptionPane.showMessageDialog(null, exception.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }// catch
        }// customEventOccurred
    }// _DialogClosingHandler
}// CreateDataSourceFromMsExcelDialogController
