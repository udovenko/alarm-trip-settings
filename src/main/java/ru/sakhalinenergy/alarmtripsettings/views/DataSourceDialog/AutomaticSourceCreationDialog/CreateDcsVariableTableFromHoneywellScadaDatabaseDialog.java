package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Component;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellScadaDatabaseObservable;


/**
 * Класс реализует вью диалога для создания источника данных из базы данных 
 * SCADA Honeywell.
 * 
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public class CreateDcsVariableTableFromHoneywellScadaDatabaseDialog extends DialogWithEvents implements CreateDcsVariableTableFromHoneywellScadaDatabaseDialogObservable
{
    
    private final HoneywellScadaDatabaseObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettingsObservable config;
    
       
    /**
     * Public constructor.
     * 
     * @param model Source instance wrapped into Honeywell SCADA database model logic
     * @param plants Wrapped plants collection
     * @param tagMasks Wrapped tag masks collection
     * @param config Dialog configuration instance
     */
    public CreateDcsVariableTableFromHoneywellScadaDatabaseDialog(HoneywellScadaDatabaseObservable model, PlantsLogicObservable plants,
        TagMasksObservable tagMasks, CreateDcsVariableTableFromHoneywellScadaDatabaseDialogSettingsObservable config)
    {
        // Setting up instance fields:
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        
        initComponents();
        
        // Устанавливаем иконку диалога:
        this.setIconImage(Main.honeywellIcon.getImage());
        this.setModal(true);
        
        // Subscribe on model events:
        model.on(SourceEvent.FILE_PATH_UPDATED, new _DatabaseFilePathUpdateHandler()
        );// on
    }// CreateDcsVariableTableFromHoneywellBackupDialog

    
    /**
     * Sets up dialog position relative to given parent, builds plants and tag
     * formats list and shows dialog.
     * 
     * @param parent Parent component relative to which dialog will be rendered
     */
    public void render(Component parent)
    {
        this.setLocationRelativeTo(parent);
        
        //Формируем список форматов тагов:
        for (TagMask tempMask : tagMasks.getMasks()) tagFormatComboBox.addItem(tempMask);

        //Строим список производственных объектов:
        for (Plant tempPlant : plants.getPlants()) plantComboBox.addItem(tempPlant);
        
        //Applying config:
        _applyConfig();
        
        this.setVisible(true);
    }//render
    
    
    /**
     * Internal class - handler for model extract file path update events.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _DatabaseFilePathUpdateHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            String exportFilePath = (String)event.getSource();
            CreateDcsVariableTableFromHoneywellScadaDatabaseDialog.this.scadaExportFilePathTextField.setText(exportFilePath);
        }//customEventOccurred
    }//_DatabaseFilePathUpdateHandler
    
    
    /**
     * Restores dialog control elements values from current configuration 
     * instance.
     */
    private void _applyConfig()
    {        
        //Восстанавливаем выбранный производственный объект:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                this.plantComboBox.setSelectedItem(tempPlant);
                break;
            }//if
        }//for
        
        for (TagMask tempTagMask : this.tagMasks.getMasks())
        {    
            if (tempTagMask.getExample().equals(config.getTagFormat()))
            {    
                this.tagFormatComboBox.setSelectedItem(tempTagMask);
                break;
            }//if
        }//for
    }//_applyConfig
    
    
    /**
     * Returns selected plant from plants combo box.
     * 
     * @return Selected plant
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)plantComboBox.getSelectedItem();
    }//getPlant
    
    
    /**
     * Returns selected tag mask from tag format combo box.
     * 
     * @return Selected tag mask
     */
    @Override
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatComboBox.getSelectedItem();
    }//getTagMask
    
    
    /**
     * Метод возвращает текущий путь к файлу экспорта DCS Honeywell.
     * 
     * @return Путь к файлу экспорта DCS Honeywell
     */
    public String getScadaDatabaseFilePath()
    {
        return this.scadaExportFilePathTextField.getText();
    }//getDcsExportFilePath
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scadaExportFilePathTextFieldLabel = new javax.swing.JLabel();
        scadaExportFilePathTextField = new javax.swing.JTextField();
        selectScadaExportFilePathButton = new javax.swing.JButton();
        runHoneywellScadaExportParsingButton = new javax.swing.JButton();
        plantCodeLabelCaption = new javax.swing.JLabel();
        plantCodeLabel = new javax.swing.JLabel();
        plantComboBox = new javax.swing.JComboBox();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();

        setTitle("Create extract from Honeywell SCADA database");
        setResizable(false);

        scadaExportFilePathTextFieldLabel.setText("Path to SCADA database:");

        scadaExportFilePathTextField.setEditable(false);

        selectScadaExportFilePathButton.setText("...");
        selectScadaExportFilePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectScadaExportFilePathButtonActionPerformed(evt);
            }
        });

        runHoneywellScadaExportParsingButton.setText("Run");
        runHoneywellScadaExportParsingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runHoneywellScadaExportParsingButtonActionPerformed(evt);
            }
        });

        plantCodeLabelCaption.setText("Plant:");

        tagFormatComboBoxLabel.setText("Tag format:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(runHoneywellScadaExportParsingButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(tagFormatComboBoxLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(plantCodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(plantComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(plantCodeLabelCaption)
                            .addComponent(scadaExportFilePathTextFieldLabel)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(scadaExportFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectScadaExportFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantCodeLabelCaption)
                    .addComponent(plantCodeLabel)
                    .addComponent(plantComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tagFormatComboBoxLabel))
                .addGap(18, 18, 18)
                .addComponent(scadaExportFilePathTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(scadaExportFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectScadaExportFilePathButton))
                .addGap(18, 18, 18)
                .addComponent(runHoneywellScadaExportParsingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод обрабатывает нажатие конопки выбора пути к файлу базы данных SCADA.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void selectScadaExportFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectScadaExportFilePathButtonActionPerformed
        
        CustomEvent selectScadaDatabaseFilePathButtonClickEvent = new CustomEvent(this);
        events.trigger(
            ViewEvent.SELECT_SCADA_DATABASE_PATH_BUTTON_CLICK,
            selectScadaDatabaseFilePathButtonClickEvent
        );// trigger        
    }//GEN-LAST:event_selectScadaExportFilePathButtonActionPerformed

    
    /**
     * Метод обрабатывает нажатие конопки запуска парсинга выбранного файла 
     * базы данных SCADA.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void runHoneywellScadaExportParsingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runHoneywellScadaExportParsingButtonActionPerformed
        
        this.setVisible(false);
        
        CustomEvent runScadaDatabaseParsingButtonClickEvent = new CustomEvent(this);
        events.trigger(
            ViewEvent.RUN_SCADA_DATABASE_PARSING_BUTTON_CLICK,
            runScadaDatabaseParsingButtonClickEvent
        );// trigger  
    }//GEN-LAST:event_runHoneywellScadaExportParsingButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel plantCodeLabel;
    private javax.swing.JLabel plantCodeLabelCaption;
    private javax.swing.JComboBox plantComboBox;
    private javax.swing.JButton runHoneywellScadaExportParsingButton;
    private javax.swing.JTextField scadaExportFilePathTextField;
    private javax.swing.JLabel scadaExportFilePathTextFieldLabel;
    private javax.swing.JButton selectScadaExportFilePathButton;
    private javax.swing.JComboBox tagFormatComboBox;
    private javax.swing.JLabel tagFormatComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}//CreateDcsVariableTableFromHoneywellScadaExportDialog
