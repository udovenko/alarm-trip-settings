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
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellScadaDatabase;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromHoneywellScadaDatabaseDialogObservable;
import ru.sakhalinenergy.alarmtripsettings.views.Progress.OneProgressBarWithLogDialog;


/**
 * Implements controller for interactions between "Create DCS Variable Table
 * from Honeywell SCADA database" dialog and "Honeywell SCADA database" model.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class CreateDcsVariableTableFromHoneywellScadaExportDialogController extends AutomaticSourceCreationDialogController
{
    
    /**
     * Public constructor. Subscribes handlers on view events.
     * 
     * @param model Source wrapped in Honeywell SCADA database model logic
     * @param view Dialog instance
     */
    public CreateDcsVariableTableFromHoneywellScadaExportDialogController(HoneywellScadaDatabase model, 
        CreateDcsVariableTableFromHoneywellScadaDatabaseDialogObservable view)
    {
        super(model, view);        
        
        this.view.on(ViewEvent.SELECT_SCADA_DATABASE_PATH_BUTTON_CLICK, new _SetPathToDatabaseFileButtonClickHandler());
        this.view.on(ViewEvent.RUN_SCADA_DATABASE_PARSING_BUTTON_CLICK, new _RunParsingButtonClickEventHandler());
    }// CreateDcsVariableTableFromHoneywellScadaExportDialogController
    
    
    /**
     * Inner class - handler for set path to backup folder button click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _SetPathToDatabaseFileButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Showing file selection dialog and handling its result:
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Honeywell SCADA Database", "qdb");
            String filepath = _showSelectPathToFileDialog("Select Honeywell SCADA database file", filter, (Component)view);
            if (filepath != null) ((HoneywellScadaDatabase)model).setFilePath(filepath);
        }// customEventOccurred
    }// _SetPathToDatabaseFileButtonClickHandler
    
    
    /**
     * Inner class - handler for run Honeywell SCADA database parsing button
     * click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    class _RunParsingButtonClickEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings config = CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings.getInstance();
            config.setPlantCode(view.getPlant().getId());
            config.setTagFormat(view.getTagMask().getExample());
            config.save();
            
            // Crate and render single progress bar dialog with with log text field:
            model.off(SourceEvent.THREAD_PROGRESS);
            model.off(SourceEvent.THREAD_WARNING);
            final OneProgressBarWithLogDialog oneProgressBarsWithLogDialog = 
                new OneProgressBarWithLogDialog(model, SourceEvent.THREAD_PROGRESS, SourceEvent.THREAD_WARNING);
            oneProgressBarsWithLogDialog.render("Reading tags from Yokogawa SCADA database", Main.mainForm);
                        
            // Susbscribe on model's thread error event:
            model.off(SourceEvent.THREAD_ERROR);
            model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
                     
            // Subscribe on model's tags set read event:
            model.off(SourceEvent.TAGS_READ);
            model.on(SourceEvent.TAGS_READ, new CustomEventListener()
            { 
                @Override
                public void customEventOccurred(CustomEvent event)
                {
                    // If log is not empty, notyfy user and copy log to buffer:
                    String log = oneProgressBarsWithLogDialog.getLog();
                    if (!log.trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog((Component)view, 
                            "There is a number of exceptions appeared during parsing. Parsing log will be copied to buffer.", 
                            "Attention", JOptionPane.INFORMATION_MESSAGE);
                        _copyToClipboard(log);
                    }// if
                    
                    oneProgressBarsWithLogDialog.close();
                    DialogsFactory.produceDcsVariableTableDataSourceDialog((TagsSource)model, false, "Confirm Yokogawa SCADA database export data source");
                }// customEventOccurred
            });// on
            
            // Executing thread:
            ((HoneywellScadaDatabase)model).readTags();
        }// customEventOccurred
    }// _RunParsingButtonClickEventHandler
}// CreateDcsVariableTableFromHoneywellScadaExportDialogController
