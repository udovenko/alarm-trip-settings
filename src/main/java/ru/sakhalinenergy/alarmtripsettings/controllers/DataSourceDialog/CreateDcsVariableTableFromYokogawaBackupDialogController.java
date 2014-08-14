package ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog;

import java.io.File;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromYokogawaBackupDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSource;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.YokogawaDcsBackupControllable;
import ru.sakhalinenergy.alarmtripsettings.views.Progress.TwoProgressBarsWithLogDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa.CreateDcsVariableTableFromYokogawaBackupDialogObservable;
import ru.sakhalinenergy.alarmtripsettings.controllers.Controller;


/**
 * Provides interactions between dialog for data source creation from Yokogawa 
 * DCS backup dialog and its model.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class CreateDcsVariableTableFromYokogawaBackupDialogController extends Controller
{
    private final YokogawaDcsBackupControllable model;
    private final CreateDcsVariableTableFromYokogawaBackupDialogObservable view;
    
    
    /**
     * Public controller. Subscribes handlers on view events.
     * 
     * @param model Yokogawa DCS backup model
     * @param view Dialog for creating Yokogawa DCS data source from backup
     */
    public CreateDcsVariableTableFromYokogawaBackupDialogController(YokogawaDcsBackupControllable model, 
        CreateDcsVariableTableFromYokogawaBackupDialogObservable view)
    {
        this.model = model;
        this.view = view;
        
        this.view.on(ViewEvent.CHANGE_PLANT_SELECTION, new _PlantSelectionHandler());
        this.view.on(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, new _TagFormatSelectionHandler());        
        this.view.on(ViewEvent.SET_PATH_TO_BACKUP_FOLDER_BUTTON_CLICK, new _SetPathToBackupFolderButtonClickHandler());
        this.view.on(ViewEvent.RUN_BACKUP_PARSING_BUTTON_CLICK, new _RunBackupParsingButtonClickHandler());
    }// CreateDcsVariableTableFromYokogawaBackupDialogController
    
    
    /**
     * Inner class - handler for view plant selection event: 
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _PlantSelectionHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Plant plant = (Plant)event.getSource();
            model.setPlant(plant);
        }// customEventOccurred
    }// _PlantSelectionHandler
        
    
    /**
     * Inner class - handler for view tag format (mask) selection event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagFormatSelectionHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            TagMask tagMask = (TagMask)event.getSource();
            model.setTagMask(tagMask);
        }// customEventOccurred
    }// _TagFormatSelectionHandler
    
    
    /**
     * Inner class - handler for set path to backup folder button click 
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _SetPathToBackupFolderButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Create select directory path dialog:
            String working_directory = System.getProperty("user.dir");
            JFileChooser folderChooser = new JFileChooser(working_directory);
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
            // Render dialog with title:
            int dialog_result = folderChooser.showDialog((Component)view, "Select Yokogawa DCS backup folder");

            // Handle directory selection result:
            if (dialog_result == JFileChooser.APPROVE_OPTION)
            {
                // Get directory path:
                File directory = folderChooser.getSelectedFile();
                final String backupPath = directory.getAbsolutePath();
                    
                // Subsceribe on model's receive thread error events:
                model.off(SourceEvent.THREAD_ERROR);
                model.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
                                
                // Execute model's thread for stations reading:
                model.setBackupFolderPath(backupPath);
                model.readStations();
            }// if
        }// customEventOccurred
    }// _CreateDataSourceYokogawaDcsBackupMenuItemClickHandler
    
    
    /**
     * Inner class - handler for run backup parsing button click 
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _RunBackupParsingButtonClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Save view config:
            CreateDcsVariableTableFromYokogawaBackupDialogSettings config = CreateDcsVariableTableFromYokogawaBackupDialogSettings.getInstance();
            config.setPlantCode(view.getPlant().getId());
            config.setTagFormat(view.getTagMask().getExample());
            config.save();
            
            // Create and render dialog with two progress bars and log text field:
            model.off(SourceEvent.THREAD_PROGRESS);
            model.off(SourceEvent.THREAD_WARNING);
            final TwoProgressBarsWithLogDialog twoProgressBarsWithLogDialog = 
                new TwoProgressBarsWithLogDialog(model, SourceEvent.THREAD_PROGRESS, SourceEvent.THREAD_WARNING);
            twoProgressBarsWithLogDialog.render("Reading tags Yokogawa DCS backup", Main.mainForm);
                        
            // Subscribe on model's thread error event:
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
                    String log = twoProgressBarsWithLogDialog.getLog();
                    if (!log.trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog((Component)view, 
                            "There is a number of exceptions appeared during parsing. Parsing log will be copied to buffer.", 
                            "Attention", JOptionPane.INFORMATION_MESSAGE);
                        _copyToClipboard(log);
                    }// if
                    
                    twoProgressBarsWithLogDialog.close();
                    DialogsFactory.produceDcsVariableTableDataSourceDialog((TagsSource)model, false, "Confirm DCS Yokogawa export data source");
                }// customEventOccurred
            });// on
            
            // Execute thread:
            model.readTags(view.getSelectedStations());
        }// customEventOccurred
    }// _RunBackupParsingButtonClickHandler
}// CreateDcsVariableTableFromYokogawaBackupDialogController
