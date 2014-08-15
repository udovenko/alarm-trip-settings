package ru.sakhalinenergy.alarmtripsettings.factories;

import javax.swing.SwingUtilities;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.SummaryCallback;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.config.Config;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.IntoolsExportDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.DocumentDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.DcsVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.EsdVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.FgsVariableTableDataSourceDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromYokogawaBackupDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellDcsExportDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.ExcelBook;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.YokogawaDcsBackup;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellDcsExport;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellScadaDatabase;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogic;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasks;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSource;
import ru.sakhalinenergy.alarmtripsettings.views.StorageConnectionDialog.StorageConnectionDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.loop.LoopSplittingDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.IntoolsExportDataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.excel.CreateDataSourceFromExcelDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa.CreateDcsVariableTableFromYokogawaBackupDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.honeywell.CreateDcsVariableTableFromHoneywellDcsExportDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.honeywell.CreateDcsVariableTableFromHoneywellScadaDatabaseDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.DocumentDataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems.DcsVariableTableDataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems.EsdVariableTableDataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems.FgsVariableTableDataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.IntoolsExportDataSourceDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.CreateDataSourceFromMsExcelController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.CreateDcsVariableTableFromYokogawaBackupDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.DocumentDataSourceDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.DcsVariableTableDataSourceDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.EsdVariableTableDataSourceDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.FgsVariableTableDataSourceDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.StorageConnectionDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.LoopSplittingDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.CreateDcsVariableTableFromHoneywellDcsExportDialogController;
import ru.sakhalinenergy.alarmtripsettings.controllers.DataSourceDialog.CreateDcsVariableTableFromHoneywellScadaExportDialogController;


/**
 * Implements static factory to produce dialogs and their controllers for other 
 * application controllers.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class DialogsFactory 
{
         
     /**
      * Receives storage connection config and depending on given flag renders
      * storage connection dialog or initiates reconnection process.
      * 
      * @param autoconnect Flag defines necessity of automatic storage reconnection
      */
     public static void produceStorageConnectionDialog(final Boolean autoconnect)
     {
         // Subscribe on storage connection settigs load event:
         final StorageConnectionDialogSettings storageConnectionDialogSettings = StorageConnectionDialogSettings.getInstance();
         storageConnectionDialogSettings.off(ConfigEvent.LOADED);
         storageConnectionDialogSettings.on(ConfigEvent.LOADED, new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                SwingUtilities.invokeLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // Create storage connection dialog and its controller:
                        StorageConnectionDialog storageConnectionDialog = new StorageConnectionDialog(storageConnectionDialogSettings);
                        storageConnectionDialog.setLocationRelativeTo(Main.mainForm);
                        StorageConnectionDialogController contrlloer = new StorageConnectionDialogController(storageConnectionDialog);
                        
                        // Show dialog or run reconnection to storage:
                        if (autoconnect) contrlloer.connectAndCreatePlantsTree(storageConnectionDialogSettings);
                        else storageConnectionDialog.setVisible(true);
                    }// run
                });// invokeLater
             }// customEventOccurred
         });// on
                 
         // Execute storage connection config fetching:
         storageConnectionDialogSettings.fetch();   
     }// produceStorageConnectionDialog
     
     
     /**
      * Fetches all required data and produces "create/edit SPI export data 
      * source dialog" and its controller.
      * 
      * @param model Tags source model instance
      * @param editMode Dialog mode flag
      * @param title Dialog title
      */
     public static void produceIntoolsExportDataSourceDialog(final TagsSource model,
        final boolean editMode, final String title)
     {
         final PlantsLogic plantsLogic = new PlantsLogic();
         final TagMasks tagMasks = new TagMasks();
         final IntoolsExportDataSourceDialogSettings settings = IntoolsExportDataSourceDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                IntoolsExportDataSourceDialog intoolsExportDataSourceDialog = new IntoolsExportDataSourceDialog(model, plantsLogic, tagMasks, settings, editMode, title);
                new IntoolsExportDataSourceDialogController(model, intoolsExportDataSourceDialog);
                intoolsExportDataSourceDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceIntoolsExportDataSourceDialog     
     
     
     /**
      * Fetches all required data and produces "create/edit documents data 
      * source dialog" and its controller.
      * 
      * @param model Tags source model instance
      * @param editMode Dialog mode flag
      * @param title Dialog title
      */
     public static void produceDocumentDataSourceDialog(final TagsSource model, 
        final boolean editMode, final String title)
     {
         final PlantsLogic plantsLogic = new PlantsLogic();
         final TagMasks tagMasks = new TagMasks();
         final DocumentDataSourceDialogSettings settings = DocumentDataSourceDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                DocumentDataSourceDialog documentDataSourceDialog = new DocumentDataSourceDialog(model, plantsLogic, tagMasks, settings, editMode, title);
                new DocumentDataSourceDialogController(model, documentDataSourceDialog);
                documentDataSourceDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceDocumentDataSourceDialog
     
     
     /**
      * Fetches all required data and produces "create/edit DCS Variable Table 
      * data source dialog" and its controller.
      * 
      * @param model Tags source model instance
      * @param editMode Dialog mode flag
      * @param title Dialog title
      */
     public static void produceDcsVariableTableDataSourceDialog(final TagsSource model, 
        final boolean editMode, final String title)
     {
         final PlantsLogic plantsLogic = new PlantsLogic();
         final TagMasks tagMasks = new TagMasks();
         final DcsVariableTableDataSourceDialogSettings settings = DcsVariableTableDataSourceDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                DcsVariableTableDataSourceDialog dcsVariableTableDataSourceDialog = new DcsVariableTableDataSourceDialog(model, plantsLogic, tagMasks, settings, editMode, title);
                new DcsVariableTableDataSourceDialogController(model, dcsVariableTableDataSourceDialog);
                dcsVariableTableDataSourceDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceDocumentDataSourceDialog
     
     
     /**
      * Fetches all required data and produces "create/edit ESD Variable Table 
      * data source dialog" and its controller.
      * 
      * @param model Tags source model instance
      * @param editMode Dialog mode flag
      * @param title Dialog title
      */
     public static void produceEsdVariableTableDataSourceDialog(final TagsSource model,
        final boolean editMode, final String title)
     {
         final PlantsLogic plantsLogic = new PlantsLogic();
         final TagMasks tagMasks = new TagMasks();
         final EsdVariableTableDataSourceDialogSettings settings = EsdVariableTableDataSourceDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                EsdVariableTableDataSourceDialog esdVariableTableDataSourceDialog = new EsdVariableTableDataSourceDialog(model, plantsLogic, tagMasks, settings, editMode, title);
                new EsdVariableTableDataSourceDialogController(model, esdVariableTableDataSourceDialog);
                esdVariableTableDataSourceDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceEsdVariableTableDataSourceDialog
     
     
     /**
      * Fetches all required data and produces "create/edit FGS Variable Table 
      * data source dialog" and its controller.
      * 
      * @param model Tags source model instance
      * @param editMode Dialog mode flag
      * @param title Dialog title
      */
     public static void produceFgsVariableTableDataSourceDialog(final TagsSource model,
        final boolean editMode, final String title)
     {
         final PlantsLogic plantsLogic = new PlantsLogic();
         final TagMasks tagMasks = new TagMasks();
         final FgsVariableTableDataSourceDialogSettings settings = FgsVariableTableDataSourceDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                FgsVariableTableDataSourceDialog fgsVariableTableDataSourceDialog = new FgsVariableTableDataSourceDialog(model, plantsLogic, tagMasks, settings, editMode, title);
                new FgsVariableTableDataSourceDialogController(model, fgsVariableTableDataSourceDialog);
                fgsVariableTableDataSourceDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceFgsVariableTableDataSourceDialog
     
     
     /**
      * Fetches all required data and produces dialog for creating data source 
      * from MS Excel book.
      * 
      * @param model Tags source wrapped into MS Excel book parsing logic
      */
     public static void produceCreateDataSourceFromExcelDialog(final ExcelBook model)
     {
         final TagMasks tagMasks = new TagMasks();
         final PlantsLogic plantsLogic = new PlantsLogic();
         final ExcelBookParsingDialogSettings settings = ExcelBookParsingDialogSettings.getInstance();
            
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                CreateDataSourceFromExcelDialog createDataSourceFromExcelDialog = new CreateDataSourceFromExcelDialog(model, plantsLogic, tagMasks, settings);
                new CreateDataSourceFromMsExcelController(model, createDataSourceFromExcelDialog);
                createDataSourceFromExcelDialog.render(Main.mainForm);
            }// customEventOccurred
         });// SummaryCallback
            
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, settings, plantsLogic, tagMasks);
     }// produceCreateDataSourceFromExcelDialog
     
     
     /**
      * Fetches all required data and produces a dialog for creating data source
      * by parsing Yokogawa DCS backup.
      * 
      * @param model Source wrapped into Yokogawa DCS backup parsing logic
      */
     public static void produceCreateDcsVariableTableFromYokogawaBackupDialog(final YokogawaDcsBackup model)
     {
         final TagMasks tagMasks = new TagMasks();
         final PlantsLogic plantsLogic = new PlantsLogic();
         final CreateDcsVariableTableFromYokogawaBackupDialogSettings config = CreateDcsVariableTableFromYokogawaBackupDialogSettings.getInstance();
                  
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                CreateDcsVariableTableFromYokogawaBackupDialog view = new CreateDcsVariableTableFromYokogawaBackupDialog(model, 
                    plantsLogic, tagMasks, config);
                new CreateDcsVariableTableFromYokogawaBackupDialogController(model, view); 
                view.render(Main.mainForm);
            }// customEventOccurred
         });// on
      
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, config, plantsLogic, tagMasks);
     }// produceCreateDcsVariableTableFromYokogawaBackupDialog
     
     
     /**
      * Fetches all required data and produces a dialog for creating data source
      * by parsing Honeywell DCS export file.
      * 
      * @param model Source wrapped into Honeywell DCS export parsing logic
      */
     public static void produceCreateDcsVariableTableFromHoneywellDcsExportDialog(final HoneywellDcsExport model)
     {
         final TagMasks tagMasks = new TagMasks();
         final PlantsLogic plantsLogic = new PlantsLogic();
         final CreateDcsVariableTableFromHoneywellDcsExportDialogSettings config = CreateDcsVariableTableFromHoneywellDcsExportDialogSettings.getInstance();
         
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                CreateDcsVariableTableFromHoneywellDcsExportDialog view = new CreateDcsVariableTableFromHoneywellDcsExportDialog(model, 
                    plantsLogic, tagMasks, config);
                new CreateDcsVariableTableFromHoneywellDcsExportDialogController(model, view); 
                view.render(Main.mainForm);
            }// customEventOccurred
         });// on
      
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, config, plantsLogic, tagMasks);
     }// produceCreateDcsVariableTableFromHoneywellDcsExportDialog
     
     
     /**
      * Fetches all required data and produces a dialog for creating data source
      * by parsing Honeywell SCADA database.
      * 
      * @param model Source wrapped into Honeywell SCADA database logic model
      */
     public static void produceCreateDcsVariableTableFromHoneywellScadaDatabaseDialog(final HoneywellScadaDatabase model)
     {
         final TagMasks tagMasks = new TagMasks();
         final PlantsLogic plantsLogic = new PlantsLogic();
         final CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings config = CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettings.getInstance();
         
         // Create summary callback which will be called when all required colletions are fetched:
         SummaryCallback summaryCallback = new SummaryCallback(new CustomEventListener()
         {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                CreateDcsVariableTableFromHoneywellScadaDatabaseDialog view = new CreateDcsVariableTableFromHoneywellScadaDatabaseDialog(model, 
                    plantsLogic, tagMasks, config);
                new CreateDcsVariableTableFromHoneywellScadaExportDialogController(model, view); 
                view.render(Main.mainForm);
            }// customEventOccurred
         });// on
      
         // Unbind previous subscribers, add callback conditions and fetch collections:
         _initilizeDataSourceDialog(summaryCallback, config, plantsLogic, tagMasks);
     }// produceCreateDcsVariableTableFromHoneywellScadaDatabaseDialog
     
     
     /**
      * Produces a dialog for splitting selected loop.
      * 
      * @param loop Loop instance to be split
      * @param loopsTable Loops table model instance, which loop belongs to
      */
     public static void produceSplitSelectedLoopDialog(final Loop loop, LoopsTable loopsTable)
     {
        LoopSplittingDialog loopSplittingDialog = new LoopSplittingDialog(loop);
        loopSplittingDialog.setLocationRelativeTo(Main.mainForm);
        new LoopSplittingDialogController(loopsTable, loopSplittingDialog);
        loopSplittingDialog.render();
     }// produceSplitSelectedLoopDialog
     
     
     /**
      * Removes previous subscribers from collections, adds summary callback 
      * conditions and start fetching collections required for data source 
      * dialog.
      * 
      * @param summaryCallback Summary callback instance which is responsible for dialog creation
      * @param config Dialog configuration object
      * @param plantsLogic Available plants collection instance
      * @param tagMasks Available tag masks collection instance
      */
     private static void _initilizeDataSourceDialog(SummaryCallback summaryCallback,
        Config config, PlantsLogic plantsLogic, TagMasks tagMasks)
     {
         // Unbind previous events:
         config.off(ConfigEvent.LOADED);
         plantsLogic.off(CollectionEvent.PLANTS_READ);
         tagMasks.off(CollectionEvent.MASKS_READ);
            
         // Add new callback conditions:
         summaryCallback.add(plantsLogic, CollectionEvent.PLANTS_READ)
            .add(config, ConfigEvent.LOADED)
            .add(tagMasks, CollectionEvent.MASKS_READ);

         // Fetch models:
         config.fetch();
         plantsLogic.fetch();
         tagMasks.fetch();
     }// _prepareSummaryCallback
}// DialogsFactory
