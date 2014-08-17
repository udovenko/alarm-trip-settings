package ru.sakhalinenergy.alarmtripsettings.controllers;

import javax.swing.JOptionPane;
import com.rits.cloning.Cloner;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.SummaryCallback;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.ExcelBook;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSource;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.YokogawaDcsBackup;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellDcsExport;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellScadaDatabase;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.OneProgressBarDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.progress.ProgressInformationDialog;
import ru.sakhalinenergy.alarmtripsettings.views.panel.sources.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.panel.sources.DataSourcesPanel;


/**
 * Implements controller for data sources tree panel for current selected PAU
 * object.
 *
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class DataSourcesPanelController extends Controller
{
    private final DataSourcesPanel view;
    
    
    /**
     * Public controller. Subscribes handlers on view events.
     * 
     * @param view Data sources panel instance
     */
    public DataSourcesPanelController(DataSourcesPanel view)
    {
        this.view = view;
        
        view.on(ViewEvent.COPY_NODE_NAME_TO_CLIPBOARD_MENU_ITEM_CLICK, new _CopyNodeNameToClipboardMenuItemClickHandler());
        view.on(ViewEvent.OPEN_LINKED_DOCUMENT_MENU_ITEM_CLICK, new _OpenLinkedDocumentMenuItemClickHandler());
        view.on(ViewEvent.CREATE_INTOOLS_EXPORT_MANUALLY_MENU_ITEM_CLICK, new _CreateIntoolsExportMannualyMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DOCUMENT_MANUALLY_MENU_ITEM_CLICK, new _CreateDocumentMannualyMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DCS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, new _CreateDcsVariableTableMannualyMenuItemClickHandler());
        view.on(ViewEvent.CREATE_ESD_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, new _CreateEsdVariableTableMannualyMenuItemClickHandler());
        view.on(ViewEvent.CREATE_FGS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, new _CreateFgsVariableTableMannualyMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DATA_SOURCE_FROM_MS_EXCEL_LIST_MENU_ITEM_CLICK, new _CreateDataSourceFromMsExcelSheetMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DATA_SOURCE_FROM_YOKOGAWA_DCS_BACKUP_MENU_ITEM_CLICK, new _CreateDataSourceYokogawaDcsBackupMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_DCS_EXPORT_MENU_ITEM_CLICK, new _CreateDcsVariableTableFromHoneywellDcsExportMenuItemClickHandler());
        view.on(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_SCADA_EXPORT_MENU_ITEM_CLICK, new _CreateDcsVariableTableFromHoneywellScadaDatabaseMenuItemClickHandler());
        view.on(ViewEvent.EDIT_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, new _EditSelectedDataSourceMenuItemClickHandler());
        view.on(ViewEvent.REMOVE_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, new _RemoveSelectedDataSourceMenuItemClickHandler());
    }// DataSourcesPanelController
    
    
    /**
     * Inner class - handler for copy node name to clipboard context menu click
     * event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CopyNodeNameToClipboardMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object nodeObject = (Object)event.getSource();
            _copyToClipboard(nodeObject.toString());
        }// customEventOccurred
    }// _CopyNodeNameToClipboardMenuItemClickHandler
    
        
    /**
     * Inner class - handler for open related document URI in default browser
     * context menu event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _OpenLinkedDocumentMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Source source = (Source)event.getSource();
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DOCUMENT_LINK.ID)
                {
                    _openUri(tempProperty.getValue());
                    break;
                }// if
            }// for
        }// customEventOccurred
    }// _OpenLinkedDocumentMenuItemClickHandler
    
    
    /**
     * Inner class - handler for open "create SPI export data source manually
     * dialog" context menu event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _CreateIntoolsExportMannualyMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Create an empty tags data source and ask factory to produce dialog:
            TagsSource tagsSource = new TagsSource(new Source());
            DialogsFactory.produceIntoolsExportDataSourceDialog(tagsSource, false, "Create Intools export data source manually");
        }// customEventOccurred
    }// _CreateIntoolsExportMannualyMenuItemClickHandler
    
    
    /**
     * Inner class - handler for open "create document data source manually
     * dialog" context menu event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _CreateDocumentMannualyMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Create an empty tags data source and ask factory to produce dialog:
            TagsSource tagsSource = new TagsSource(new Source());
            DialogsFactory.produceDocumentDataSourceDialog(tagsSource, false, "Create document data source manually");
        }// customEventOccurred
    }// _CreateDocumentMannualyMenuItemClickHandler
    
    
    /**
     * Internal class - handler for open "create DCS Variable Table manually 
     * dialog" context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateDcsVariableTableMannualyMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Crearte a new source wrapper and ask factory to produce new dialog:
            TagsSource tagsSource = new TagsSource(new Source());
            DialogsFactory.produceDcsVariableTableDataSourceDialog(tagsSource, false, "Create document data source manually");
        }// customEventOccurred
    }// _CreateDcsVariableTableMannualyMenuItemClickHandler
    
    
    /**
     * Internal class - handler for open "create ESD Variable Table manually 
     * dialog" context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateEsdVariableTableMannualyMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Crearting a new source wrapper and asking factory to produce new dialog:
            TagsSource tagsSource = new TagsSource(new Source());
            DialogsFactory.produceEsdVariableTableDataSourceDialog(tagsSource, false, "Create ESD Variable Table data source manually");
        }// customEventOccurred
    }// _CreateEsdVariableTableMannualyMenuItemClickHandler
    
    
    /**
     * Internal class - handler for open "create FGS Variable Table manually 
     * dialog" context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateFgsVariableTableMannualyMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Crearting a new source wrapper and asking factory to produce new dialog:
            TagsSource tagsSource = new TagsSource(new Source());
            DialogsFactory.produceFgsVariableTableDataSourceDialog(tagsSource, false, "Create FGS Variable Table data source manually");
        }// customEventOccurred
    }// _CreateFgsVariableTableMannualyMenuItemClickHandler
    
    
    /**
     * Inner class - handler for open "create data source from MS Excel" dialog
     * context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.4
     */
    private class _CreateDataSourceFromMsExcelSheetMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Ask factory to produce dialog with empty model:
            DialogsFactory.produceCreateDataSourceFromExcelDialog(new ExcelBook(new Source()));
        }// customEventOccurred
    }// CreateDataSourceFromMsExcelSheetMenuItemClickHandler
    
    
    /**
     * Inner class - handler for "Create data source from Yokogawa backup"
     * context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateDataSourceYokogawaDcsBackupMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Ask factory to produce requirted dialog with empty source model:
            DialogsFactory.produceCreateDcsVariableTableFromYokogawaBackupDialog(new YokogawaDcsBackup(new Source()));
        }// customEventOccurred
    }// _CreateDataSourceYokogawaDcsBackupMenuItemClickHandler
    
    
    /**
     * Inner class - handler for "Create data source from Honeywell DCS export" 
     * context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateDcsVariableTableFromHoneywellDcsExportMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Ask factory to produce requirted dialog with empty source model:
            DialogsFactory.produceCreateDcsVariableTableFromHoneywellDcsExportDialog(new HoneywellDcsExport(new Source()));
        }// customEventOccurred
    }// _CreateDcsVariableTableFromHoneywellDcsExportMenuItemClickHandler
    
    
    /**
     * Inner class - handler for "Create data source from Honeywell SCADA 
     * database" context menu click event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _CreateDcsVariableTableFromHoneywellScadaDatabaseMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Ask the factory to produce requirted dialog with empty source model:
            DialogsFactory.produceCreateDcsVariableTableFromHoneywellScadaDatabaseDialog(new HoneywellScadaDatabase(new Source()));
        }// customEventOccurred
    }// _CreateDcsVariableTableFromHoneywellScadaDatabaseMenuItemClickHandler
    
    
    /**
     * Inner class - handler for click event on context menu for opening "edit
     * selected data source" dialog.
     * 
     * @author Denis Udovenko
     * @version 1.0.3
     */
    private class _EditSelectedDataSourceMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            // Get data source and clone it to avoid changes of initial instance:
            Source source = (Source)event.getSource();
            Cloner cloner = new Cloner();
            final Source clonedSource = cloner.deepClone(source);
            
            // Create wrapper for source clone:
            final TagsSource sourceLogic = new TagsSource(clonedSource);
            
            // Create and render progress information dialog:
            sourceLogic.off(SourceEvent.THREAD_PROGRESS);
            final ProgressInformationDialog progressInformationDialog =
                new ProgressInformationDialog(sourceLogic, SourceEvent.THREAD_PROGRESS);
            progressInformationDialog.render("Progress information", Main.mainForm);
            
            sourceLogic.off(SourceEvent.THREAD_ERROR);
            sourceLogic.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
            
            // Subscribe on model's source initialization event:
            sourceLogic.off(SourceEvent.SOURCE_INITIALIZED);
            sourceLogic.on(SourceEvent.SOURCE_INITIALIZED, new CustomEventListener()
            {
                @Override
                public void customEventOccurred(CustomEvent evt)
                {
                    progressInformationDialog.close();
                    
                    if (clonedSource.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID)
                        DialogsFactory.produceIntoolsExportDataSourceDialog(sourceLogic, true, "Edit selected Intools export data source");
                                        
                    if (clonedSource.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID)
                        DialogsFactory.produceDocumentDataSourceDialog(sourceLogic, true, "Edit selected document data source");
                                        
                    if (clonedSource.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID)
                        DialogsFactory.produceDcsVariableTableDataSourceDialog(sourceLogic, true, "Edit selected DCS Variable Table data source");
                                        
                    if (clonedSource.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID)
                        DialogsFactory.produceEsdVariableTableDataSourceDialog(sourceLogic, true, "Edit selected ESD Variable Table data source");
                                        
                    if (clonedSource.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID)
                        DialogsFactory.produceFgsVariableTableDataSourceDialog(sourceLogic, true, "Edit selected FGS Variable Table data source");
                }// customEventOccurred
            });// on
            
            // Execute initialization thread:
            sourceLogic.initialize();
        }// customEventOccurred
    }// CreateDataSourceFromMsExcelSheetMenuItemClickHandler
    
    
    /**
     * Inner class - handler for context menu event of removing selected data
     * source from storage.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _RemoveSelectedDataSourceMenuItemClickHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Source source = (Source)event.getSource();
            
            // Ask user's confirmation for removing the data soure:
            int answer = JOptionPane.showConfirmDialog(Main.mainForm,
                "Are you sure you want completely remove all data related to data source \r\n" + source + "?",
                "Action confirmation",
            JOptionPane.OK_CANCEL_OPTION);
            
            // If user confimed data source removing:
            if (answer == JOptionPane.OK_OPTION)
            {
                // Wrap source to be removed in tags source logic:
                TagsSource sourceLogic = new TagsSource(source);
                final StorageConnectionDialogSettings storageConnectionDialogSettings = StorageConnectionDialogSettings.getInstance();
                           
                // Create and render dialog with single progress bar:
                sourceLogic.off(SourceEvent.THREAD_PROGRESS);
                sourceLogic.off(SourceEvent.SOURCE_SAVED);
                final OneProgressBarDialog oneProgressBarDialog = new OneProgressBarDialog(sourceLogic, SourceEvent.THREAD_PROGRESS);
                oneProgressBarDialog.render("Removing data source from storage...", Main.mainForm);
                                
                // Subscribing on thread error event:
                sourceLogic.off(SourceEvent.THREAD_ERROR);
                sourceLogic.on(SourceEvent.THREAD_ERROR, new ThreadErrorEventHandler());
                
                // Create summary callback which will ba called when config and tag masks collections are fetched:
                SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
                {
                    @Override
                    public void customEventOccurred(CustomEvent evt)
                    {
                        oneProgressBarDialog.close();
                        
                        // Ask factory to produce storage connection dialog and start reconnection:
                        DialogsFactory.produceStorageConnectionDialog(true);
                    }// customEventOccurred
                });// on
            
                // Remove previous subscribing from models:
                sourceLogic.off(SourceEvent.SOURCE_REMOVED);
                storageConnectionDialogSettings.off(ConfigEvent.LOADED);
            
                // Add summary callback conditions:
                summaryCallback.add(sourceLogic, SourceEvent.SOURCE_REMOVED)
                    .add(storageConnectionDialogSettings, ConfigEvent.LOADED);
                
                // Executing models threads:
                sourceLogic.remove();
                storageConnectionDialogSettings.fetch();
            }// if
        }// customEventOccurred
    }// _RemoveSelectedDataSourceMenuItemClickHandler
}// DataSourcesPanelController