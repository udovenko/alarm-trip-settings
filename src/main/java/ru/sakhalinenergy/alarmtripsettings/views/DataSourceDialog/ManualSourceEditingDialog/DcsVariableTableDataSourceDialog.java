package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import java.util.Collections;
import java.util.Date;
import java.awt.Component;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.config.DcsVariableTableDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;


/**
 * Implements dialog for create/edit DCS Variable Table data source.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class DcsVariableTableDataSourceDialog extends ManualSourceEditingDialog implements ManualSourceEditingDialogObservable
{
   
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Tags source wrapper
     * @param plants Plants list wrapper
     * @param tagMasks Wrapped masks collection for tag parsing
     * @param config Configuration instance
     * @param editMode New source / edit source mode flag
     * @param title Dialog title
     */
    public DcsVariableTableDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants,
        TagMasksObservable tagMasks, DcsVariableTableDataSourceDialogSettingsObservable config,
        boolean editMode, String title) 
    {
        // Call superclass constructor:
        super(model, plants, tagMasks, config, editMode, title);
        
        initComponents();
               
        // Set dialog icon:
        setIconImage(Main.dcsIcon.getImage());
        
        // Set up calendar date format:
        backupDatePicker.setFormats(dateFormat);
        
        // Subscribe on model's events:
        model.on(SourceEvent.TAG_SET_UPDATED, _getModelTagSetUpdateHandler(sourceTagsTable));
        
        // Set tags table model:
        sourceTagsTable.setModel(new DcsTagsTableModel(model, this));
        DcsTagsTableModel tagsTableModel = (DcsTagsTableModel)this.sourceTagsTable.getModel();
                
        // Set cell renderes depending on column name:
        for (TableColumn column : Collections.list(this.sourceTagsTable.getColumnModel().getColumns()))
        {
            if (column.getHeaderValue().equals(tagsTableModel.REMOVE_TAG_BUTTON_COLUMN_NAME))
            {
                column.setCellEditor(new TagsTableButtonEditor());
                column.setCellRenderer(new TagsTableButtonRenderer());
                
            } else {   
                
                column.setCellRenderer(new TagsTableCellRenderer());
            }// else
        }// for
    }// DcsVariableTableSettingsPanel
       
    
    /**
     * Render required objects lists, applies dialog settings and shows form on
     * the screen.
     * 
     * @param parent Parent component, dialog will be rendered relative to it
     */
    public void render(Component parent)
    {
        // Build palants list and restore plant selection:
        for (TagMask tempMask : tagMasks.getMasks()) tagFormatsComboBox.addItem(tempMask);

        // Build tag formats list and restore format selection:
        for (Plant tempPlant : plants.getPlants()) plantsComboBox.addItem(tempPlant);
        
        // Apply config:
        _applyConfig();
                
        // Set relative location and show dialog:
        setLocationRelativeTo(parent);
        _show();
    }// render
    
    
    /**
     * Returns selected plant.
     * 
     * @return Selected plant
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)plantsComboBox.getSelectedItem();
    }// getPlant
    
    
    /**
     * Returns data source name.
     * 
     * @return Data source name
     */
    public String getDataSourceName()
    {
        return this.backupNameTextField.getText();
    }// getDataSourceName
           
    
    /**
     * Returns data source priority.
     * 
     * @return Data source priority
     */
    public String getPriority()
    {
        return prioritySpinner.getValue().toString();
    }// getPriority
    
    
    /**
     * Returns selected tag mask.
     * 
     * @return Selected tag mask
     */
    @Override
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatsComboBox.getSelectedItem();
    }// getTagMask
    
    
    /**
     * Returns data source comment.
     * 
     * @return Data source comment
     */
    public String getComment()
    {
        return (String)commentTextArea.getText();
    }// getComment
    
    
    /**
     * Returns DCS backup date.
     * 
     * @return DCS backup date
     */
    public Date getBackupDate()
    {
        return backupDatePicker.getDate();
    }// getRevisionDate
    
    
    /**
     * Returns DCS backup date as string.
     * 
     * @return DCS backup date as string
     */
    public String getBackupDateAsString()
    {
        Date date = backupDatePicker.getDate();
        return dateFormat.format(date);
    }// getRevisionDate
       
    
    /**
     * Returns value of flag which specifies necessity of loops creation for
     * tags currently do not related to any of existing loops in storage.
     * 
     * @return Create not existed loops flag
     */
    public String getCreateLoopsIfNotExistFlag()
    {
        Boolean createLoopsIfNotExist  = this.createLoopsIfNotExistCheckBox.isSelected();
        return createLoopsIfNotExist.toString();
    }// getCreateLoopsIfNotExistFlag
         
    
    /**
     * Restores dialog's settings from configuration object.
     */
    private void _applyConfig() 
    {
        // Call superclass configuration method:
        _applyConfig(plantsComboBox, tagFormatsComboBox, prioritySpinner,
            backupNameTextField, commentTextArea);
        
        // Cast models to concrete classes:
        DcsVariableTableDataSourceDialogSettingsObservable castedConfig 
            = (DcsVariableTableDataSourceDialogSettingsObservable)config;
             
        // Aplly rest of settings:
        if (editMode)
        {
            Source source = ((TagsSourceObservable)model).getEntity();
            backupDatePicker.setDate(source.getDate());

        } else {
            
            try
            {    
                Date date = this.dateFormat.parse(castedConfig.getBackupDate());
                backupDatePicker.setDate(date);
            } catch (Exception exception){}
        }// else
                
        this.createLoopsIfNotExistCheckBox.setSelected(new Boolean(castedConfig.getCreateLoopsIfNotExistFlag()));
    }// _applyConfig

    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        backupDatePicker = new org.jdesktop.swingx.JXDatePicker();
        backupDatePickerLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourceTagsTable = new javax.swing.JTable();
        sourceTagsTableLabel = new javax.swing.JLabel();
        tagFormatsComboBox = new javax.swing.JComboBox();
        tagFormatsComboBoxLabel = new javax.swing.JLabel();
        backupNameTextFieldLabel = new javax.swing.JLabel();
        backupNameTextField = new javax.swing.JTextField();
        addDumpSourceToStorageButton = new javax.swing.JButton();
        priorityTextFieldLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        commentTextAreaLabel = new javax.swing.JLabel();
        currentPlantCodeLabelCaption = new javax.swing.JLabel();
        prioritySpinner = new javax.swing.JSpinner();
        createLoopsIfNotExistCheckBox = new javax.swing.JCheckBox();
        plantsComboBox = new javax.swing.JComboBox();
        newTagNameTextField = new javax.swing.JTextField();
        addTagButton = new javax.swing.JButton();

        setAlwaysOnTop(true);

        backupDatePickerLabel.setText("Backup date:");

        sourceTagsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(sourceTagsTable);

        sourceTagsTableLabel.setText("Tags set table:");

        tagFormatsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatsComboBoxActionPerformed(evt);
            }
        });

        tagFormatsComboBoxLabel.setText("Tag format:");

        backupNameTextFieldLabel.setText("Name:");

        addDumpSourceToStorageButton.setText("Save");
        addDumpSourceToStorageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDumpSourceToStorageButtonActionPerformed(evt);
            }
        });

        priorityTextFieldLabel.setText("Priority:");

        commentTextArea.setColumns(20);
        commentTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        commentTextArea.setLineWrap(true);
        commentTextArea.setRows(5);
        commentTextArea.setWrapStyleWord(true);
        jScrollPane2.setViewportView(commentTextArea);

        commentTextAreaLabel.setText("Comment:");

        currentPlantCodeLabelCaption.setText("Current plant code:");

        createLoopsIfNotExistCheckBox.setText("Create loops if not exist");

        plantsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantsComboBoxActionPerformed(evt);
            }
        });

        addTagButton.setText("Add tag");
        addTagButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTagButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(backupDatePickerLabel)
                                .addComponent(backupDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sourceTagsTableLabel)
                                .addComponent(tagFormatsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tagFormatsComboBoxLabel)
                                .addComponent(backupNameTextFieldLabel)
                                .addComponent(backupNameTextField))
                            .addComponent(currentPlantCodeLabelCaption))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(commentTextAreaLabel)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(prioritySpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(priorityTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(createLoopsIfNotExistCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addDumpSourceToStorageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentPlantCodeLabelCaption)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupNameTextFieldLabel)
                    .addComponent(priorityTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prioritySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupDatePickerLabel)
                    .addComponent(commentTextAreaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backupDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(tagFormatsComboBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tagFormatsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(sourceTagsTableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTagButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDumpSourceToStorageButton)
                    .addComponent(createLoopsIfNotExistCheckBox))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Handles tag mask (format) selection event and triggers appropriate event with 
     * selected tag mask instance data.
     * 
     * @param evt Tag formats combo box selection event object
     */
    private void tagFormatsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatsComboBoxActionPerformed
        
        TagMask tagFormat = (TagMask)this.tagFormatsComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatsComboBoxActionPerformed

    
    /**
     * Handles "Save DCS Variable Table data source" button click event and 
     * triggers appropriate for all subscribers.
     * 
     * @param evt Button click event object
     */
    private void addDumpSourceToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDumpSourceToStorageButtonActionPerformed
        
        // Hide dialog:
        _close();
        
        // Trigger an event:
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addDumpSourceToStorageButtonActionPerformed

    
    /**
     * Handles add tag button click event and triggers appropriate event with 
     * new tag name data.
     * 
     * @param evt Button click event object
     */
    private void addTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTagButtonActionPerformed
        
        CustomEvent inputNewTagEvent = new CustomEvent(newTagNameTextField.getText());
        this.events.trigger(ViewEvent.TAG_NAME_INPUT, inputNewTagEvent);
    }//GEN-LAST:event_addTagButtonActionPerformed

    
    /**
     * Handles plant selection event and triggers appropriate event with 
     * selected plant data.
     * 
     * @param evt Plants combo box selection event object
     */
    private void plantsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantsComboBoxActionPerformed
        
        Plant plant = (Plant)plantsComboBox.getSelectedItem();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        this.events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);
    }//GEN-LAST:event_plantsComboBoxActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDumpSourceToStorageButton;
    private javax.swing.JButton addTagButton;
    private org.jdesktop.swingx.JXDatePicker backupDatePicker;
    private javax.swing.JLabel backupDatePickerLabel;
    private javax.swing.JTextField backupNameTextField;
    private javax.swing.JLabel backupNameTextFieldLabel;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JLabel commentTextAreaLabel;
    private javax.swing.JCheckBox createLoopsIfNotExistCheckBox;
    private javax.swing.JLabel currentPlantCodeLabelCaption;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField newTagNameTextField;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JSpinner prioritySpinner;
    private javax.swing.JLabel priorityTextFieldLabel;
    private javax.swing.JTable sourceTagsTable;
    private javax.swing.JLabel sourceTagsTableLabel;
    private javax.swing.JComboBox tagFormatsComboBox;
    private javax.swing.JLabel tagFormatsComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}// DcsVariableTableDataSourceDialog
