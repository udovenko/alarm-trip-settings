package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSource;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellDcsExportDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellDcsExport;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.honeywell.CreateDcsVariableTableFromHoneywellDcsExportDialogObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.OneProgressBarWithLogDialog;


/**
 * Implements controller for interactions between "Create DCS Variable Table
 * from Honeywell extract" dialog and "Honeywell DCS extract" model.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class CreateDcsVariableTableFromHoneywellDcsExportDialogController extends AutomaticSourceCreationDialogController
{
    
    /**
     * Public controller. Subscribes handlers on view events.
     * 
     * @param model Data source wrapped into Honeywell DCS extract logic
     * @param view Dialog instanceDialog instance
     */
    public CreateDcsVariableTableFromHoneywellDcsExportDialogController(HoneywellDcsExport model, 
        CreateDcsVariableTableFromHoneywellDcsExportDialogObservable view)
    {
        super(model, view);

        this.view.on(ViewEvent.SELECT_DCS_EXPORT_FILE_PATH_BUTTON_CLICK, new _SetPathToExportFileButtonClickHandler());
        this.view.on(ViewEvent.RUN_HONEYWELL_DCS_EXPORT_PARSING_BUTTON_CLICK, new _RunParsingButtonClickEventHandler());
    }// CreateDcsVariableTableFromHoneywellDcsExportDialogController
    
        
    /**
     * Inner class - handler for set path to backup folder button click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3     
     */
    private class _SetPathToExportFileButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Showing file selection dialog and handling its result:
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Honeywell DCS export file", "txt");
            String filepath = _showSelectPathToFileDialog("Select Honeywell DCS export file", filter, (Component)view);
            if (filepath != null) ((HoneywellDcsExport)model).setFilePath(filepath);
        }// customEventOccurred
    }// _CreateDataSourceYokogawaDcsBackupMenuItemClickHandler
    
    
    /**
     * Inner class - handler for run Honeywell DCS extract parsing button click 
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    class _RunParsingButtonClickEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            CreateDcsVariableTableFromHoneywellDcsExportDialogSettings config = CreateDcsVariableTableFromHoneywellDcsExportDialogSettings.getInstance();
            config.setPlantCode(view.getPlant().getId());
            config.setTagFormat(view.getTagMask().getExample());
            config.save();
            
            // Create and render progress bar with log dialog:
            model.off(SourceEvent.THREAD_PROGRESS);
            model.off(SourceEvent.THREAD_WARNING);
            final OneProgressBarWithLogDialog oneProgressBarsWithLogDialog = 
                new OneProgressBarWithLogDialog(model, SourceEvent.THREAD_PROGRESS, SourceEvent.THREAD_WARNING);
            oneProgressBarsWithLogDialog.render("Reading tags Yokogawa DCS backup", Main.mainForm);
            
            // Subscribe on models's thread error event:
            model.off(SourceEvent.THREAD_ERROR);
            model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
                
            // Subscribe on model's tagsset read event:
            model.off(SourceEvent.TAGS_READ);
            model.on(SourceEvent.TAGS_READ, new CustomEventListener()
            { 
                @Override
                public void customEventOccurred(CustomEvent event)
                {
                    // If log is not empty, notyfy user and copy it to buffer:
                    String log = oneProgressBarsWithLogDialog.getLog();
                    if (!log.trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog((Component)view, 
                            "There is a number of exceptions appeared during parsing. Parsing log will be copied to buffer.", 
                            "Attention", JOptionPane.INFORMATION_MESSAGE);
                        _copyToClipboard(log);
                    }// if
                    
                    oneProgressBarsWithLogDialog.close();
                    DialogsFactory.produceDcsVariableTableDataSourceDialog((TagsSource)model, false, "Confirm DCS Yokogawa export data source");
                }// customEventOccurred
            });// on
            
            // Execute thread:
            ((HoneywellDcsExport)model).readTags();
        }// customEventOccurred
    }// _RunParsingButtonClickEventHandler
}// CreateDcsVariableTableFromHoneywellDcsExportDialogController
