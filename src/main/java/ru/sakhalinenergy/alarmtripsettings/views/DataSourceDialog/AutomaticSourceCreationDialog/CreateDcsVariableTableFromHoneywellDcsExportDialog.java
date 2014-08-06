package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Component;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.HoneywellDcsExportObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromHoneywellDcsExportDialogSettingsObservable;


/**
 * Implements dialog for creating data source from Honeywell DCS export.
 * Honeywell.
 * 
 * @author Denis.Udovenko
 * @version 1.0.4
 */
public class CreateDcsVariableTableFromHoneywellDcsExportDialog extends DialogWithEvents implements CreateDcsVariableTableFromHoneywellDcsExportDialogObservable
{
    
    private final HoneywellDcsExportObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final CreateDcsVariableTableFromHoneywellDcsExportDialogSettingsObservable config;
    
       
    /**
     * Public constructor.
     * 
     * @param model Data source instance wrapped into Honeywell DCS extract logic
     * @param plants Wrapped plants collection
     * @param tagMasks Wrapped tag masks collection
     * @param config Dialog configuration instance
     */
    public CreateDcsVariableTableFromHoneywellDcsExportDialog(HoneywellDcsExportObservable model, 
        PlantsLogicObservable plants, TagMasksObservable tagMasks, CreateDcsVariableTableFromHoneywellDcsExportDialogSettingsObservable config)
    {
        //Setting up instance fields:
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        
        //Initializing dialog components:
        initComponents();
        
        //Setting up dialog icon:
        setIconImage(Main.honeywellIcon.getImage());
        setModal(true);
        
        //Subscribing on models' events:
        model.on(SourceEvent.FILE_PATH_UPDATED, new _ExtractFilePathUpdateHandler());
    }//CreateDcsVariableTableFromHoneywellBackupDialog

    
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
    private class _ExtractFilePathUpdateHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            String exportFilePath = (String)event.getSource();
            dcsExportFilePathTextField.setText(exportFilePath);
        }//customEventOccurred
    }//_ExtractFilePathUpdateHandler
    
    
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
     * Метод устанавливает путь к файлу экспорта DCS Honeywell.
     * 
     * @param path Путь к файлу экспорта DCS Honeywell
     */
    public void setDcsExportFilePath(String path)
    {
        this.dcsExportFilePathTextField.setText(path);
    }//setDcsExportFilePath
    
    
    /**
     * Returns selected plant.
     * 
     * @return Selected plant
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)plantComboBox.getSelectedItem();
    }//getPlantCode
    
    
    /**
     * Returns selected tag mask.
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
    public String getDcsExportFilePath()
    {
        return this.dcsExportFilePathTextField.getText();
    }//getDcsExportFilePath
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dcsExportFilePathTextFieldLabel = new javax.swing.JLabel();
        dcsExportFilePathTextField = new javax.swing.JTextField();
        selectDcsExportFilePathButton = new javax.swing.JButton();
        runHoneywellDcsExportParsingButton = new javax.swing.JButton();
        plantCodeLabelCaption = new javax.swing.JLabel();
        plantComboBox = new javax.swing.JComboBox();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();

        setTitle("Create extract from Honeywell DCS backup");
        setResizable(false);

        dcsExportFilePathTextFieldLabel.setText("Path to Honeywell DCS export:");

        dcsExportFilePathTextField.setEditable(false);

        selectDcsExportFilePathButton.setText("...");
        selectDcsExportFilePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDcsExportFilePathButtonActionPerformed(evt);
            }
        });

        runHoneywellDcsExportParsingButton.setText("Run");
        runHoneywellDcsExportParsingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runHoneywellDcsExportParsingButtonActionPerformed(evt);
            }
        });

        plantCodeLabelCaption.setText("Plant:");

        plantComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantComboBoxActionPerformed(evt);
            }
        });

        tagFormatComboBoxLabel.setText("Tag format:");

        tagFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runHoneywellDcsExportParsingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(dcsExportFilePathTextFieldLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dcsExportFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectDcsExportFilePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tagFormatComboBoxLabel)
                            .addComponent(plantCodeLabelCaption))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(plantComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tagFormatComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantCodeLabelCaption)
                    .addComponent(plantComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagFormatComboBoxLabel)
                    .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(dcsExportFilePathTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dcsExportFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectDcsExportFilePathButton))
                .addGap(18, 18, 18)
                .addComponent(runHoneywellDcsExportParsingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод обрабатывает нажатие конопки выбора пути к файлу экспорта DCS
     * Honeywell.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void selectDcsExportFilePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDcsExportFilePathButtonActionPerformed
        
        CustomEvent selectDcsExportFilePathButtonClickEvent = new CustomEvent(this);
        events.trigger(
            ViewEvent.SELECT_DCS_EXPORT_FILE_PATH_BUTTON_CLICK, 
            selectDcsExportFilePathButtonClickEvent
        );// trigger        
    }//GEN-LAST:event_selectDcsExportFilePathButtonActionPerformed

    
    /**
     * Метод обрабатывает нажатие конопки запуска парсинга выбранного файла 
     * экспорта DCS Honeywell.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void runHoneywellDcsExportParsingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runHoneywellDcsExportParsingButtonActionPerformed
        
        this.setVisible(false);
        
        CustomEvent runHoneywellDcsExportParsingButtonClickEvent = new CustomEvent(this);
        events.trigger(
            ViewEvent.RUN_HONEYWELL_DCS_EXPORT_PARSING_BUTTON_CLICK, 
            runHoneywellDcsExportParsingButtonClickEvent
        );// trigger  
    }//GEN-LAST:event_runHoneywellDcsExportParsingButtonActionPerformed

    
    /**
     * Handles plant code selection event and fires an event contains selected 
     * plant instance.
     * 
     * @param evt Plants combo box event
     */
    private void plantComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantComboBoxActionPerformed
        
        Plant plant = getPlant();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);
    }//GEN-LAST:event_plantComboBoxActionPerformed

    
    /**
     * Handles tag format selection event and fires an event contains selected
     * tag mask instance.
     * 
     * @param evt Tag masks combo box event
     */
    private void tagFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatComboBoxActionPerformed
        
        TagMask tagFormat = getTagMask();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField dcsExportFilePathTextField;
    private javax.swing.JLabel dcsExportFilePathTextFieldLabel;
    private javax.swing.JLabel plantCodeLabelCaption;
    private javax.swing.JComboBox plantComboBox;
    private javax.swing.JButton runHoneywellDcsExportParsingButton;
    private javax.swing.JButton selectDcsExportFilePathButton;
    private javax.swing.JComboBox tagFormatComboBox;
    private javax.swing.JLabel tagFormatComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}//CreateDcsVariableTableFromHoneywellBackupDialog
