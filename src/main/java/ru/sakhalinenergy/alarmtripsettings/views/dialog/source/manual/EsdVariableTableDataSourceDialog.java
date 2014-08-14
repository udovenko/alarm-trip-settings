package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.util.Date;
import java.util.Collections;
import java.awt.Component;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.VariableTableDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;


/**
 * Implements dialog for create/edit ESD System Variable Table data source.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class EsdVariableTableDataSourceDialog extends ManualVariableTableEditingDialog implements ManualSourceEditingDialogObservable
{
        
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Tags source wrapper
     * @param plants Plants list wrapper
     * @param tagMasks Wrapped masks collection for tag parsing
     * @param config Configuration instance
     * @param editMode Create/edit data source mode flag
     * @param title Dialog title
     */
    public EsdVariableTableDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, VariableTableDataSourceDialogSettingsObservable config, 
        boolean editMode, String title)
    {
        // Call superclass constructor:
        super(model, plants, tagMasks, config, editMode, title);
        
        initComponents();
        
        // Set dialog icon:
        setIconImage(Main.esdIcon.getImage());
        
        // Set calendar date format:
        backupDatePicker.setFormats(dateFormat);
        
        // Subscribe on model's events:
        model.on(SourceEvent.TAG_SET_UPDATED, _getModelTagSetUpdateHandler(sourceTagsTable));
        
        // Set tags table model:
        sourceTagsTable.setModel(new TagsTableModel(model, this));
        TagsTableModel tagsTableModel = (TagsTableModel)this.sourceTagsTable.getModel();
                        
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
    }// EsdVariableTableDataSourceDialog
    
    
    /**
     * Render required objects lists, applies dialog settings and shows form on
     * the screen.
     * 
     * @param parent Parent component, dialog will be rendered relative to it
     */
    public void render(Component parent)
    {
        // Build palants list and restore plant selection:
        for (TagMask tempMask : tagMasks.getMasks()) tagFormatComboBox.addItem(tempMask);

        // Build tag formats list and restore format selection:
        for (Plant tempPlant : plants.getPlants()) plantsComboBox.addItem(tempPlant);
            
        // Set relative location:
        setLocationRelativeTo(parent);
        
        // Call superclass apply configuration method:
        _applyConfig(plantsComboBox, tagFormatComboBox, prioritySpinner,
            sourceNameTextField, commentTextArea, backupDatePicker);
                
        // Show dialog:
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
        return sourceNameTextField.getText();
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
        return (TagMask)tagFormatComboBox.getSelectedItem();
    }// getTagFormat
    
    
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
     * Returns ESD system backup date.
     * 
     * @return ESD system backup date
     */
    public Date getBackupDate()
    {
        return backupDatePicker.getDate();
    }// getBackupDate
    
    
    /**
     * Returns ESD system date as string.
     * 
     * @return ESD system date as string
     */
    public String getBackupDateAsString()
    {
        Date date = backupDatePicker.getDate();
        return dateFormat.format(date);
    }// getBackupDateAsString
    
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        plantCodeLabelCaption = new javax.swing.JLabel();
        sourceNameTextFieldLabel = new javax.swing.JLabel();
        sourceNameTextField = new javax.swing.JTextField();
        priorityTextFieldLabel = new javax.swing.JLabel();
        backupDatePicker = new org.jdesktop.swingx.JXDatePicker();
        backupDatePickerLabel = new javax.swing.JLabel();
        commentTextAreaLabel = new javax.swing.JLabel();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();
        commentTextAreaScrollPane = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        sourceTagsTableScrollPane = new javax.swing.JScrollPane();
        sourceTagsTable = new javax.swing.JTable();
        sourceTagsTableLabel = new javax.swing.JLabel();
        addSourceButton = new javax.swing.JButton();
        prioritySpinner = new javax.swing.JSpinner();
        plantsComboBox = new javax.swing.JComboBox();
        newTagNameTextField = new javax.swing.JTextField();
        addTagButton = new javax.swing.JButton();

        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(800, 600));

        plantCodeLabelCaption.setText("Current plant code:");

        sourceNameTextFieldLabel.setText("Name:");

        priorityTextFieldLabel.setText("Priority:");

        backupDatePickerLabel.setText("Backup date:");

        commentTextAreaLabel.setText("Comment:");

        tagFormatComboBoxLabel.setText("Tag format:");

        tagFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatComboBoxActionPerformed(evt);
            }
        });

        commentTextArea.setColumns(20);
        commentTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        commentTextArea.setLineWrap(true);
        commentTextArea.setRows(5);
        commentTextArea.setWrapStyleWord(true);
        commentTextAreaScrollPane.setViewportView(commentTextArea);

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
        sourceTagsTableScrollPane.setViewportView(sourceTagsTable);

        sourceTagsTableLabel.setText("Tag set table:");

        addSourceButton.setText("Save");
        addSourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSourceButtonActionPerformed(evt);
            }
        });

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
                    .addComponent(sourceTagsTableScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(tagFormatComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(backupDatePicker, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(plantCodeLabelCaption, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sourceNameTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(backupDatePickerLabel, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(sourceNameTextField, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(tagFormatComboBoxLabel))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(commentTextAreaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(commentTextAreaLabel)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(prioritySpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(priorityTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sourceTagsTableLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addSourceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantCodeLabelCaption)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceNameTextFieldLabel)
                    .addComponent(priorityTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prioritySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backupDatePickerLabel)
                    .addComponent(commentTextAreaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(backupDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tagFormatComboBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(commentTextAreaScrollPane))
                .addGap(25, 25, 25)
                .addComponent(sourceTagsTableLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sourceTagsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addSourceButton)
                    .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTagButton))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Handles tag mask (format) selection event and triggers appropriate event 
     * with selected tag mask data.
     * 
     * @param evt Tag formats combo box selection event object
     */
    private void tagFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatComboBoxActionPerformed
        
        TagMask tagFormat = (TagMask)this.tagFormatComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatComboBoxActionPerformed

    
    /**
     * Handles "Save ESD Variable Table data source" button click event and 
     * triggers appropriate for all subscribers.
     * 
     * @param evt Button click event object
     */
    private void addSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSourceButtonActionPerformed
        
        // Hide dialog:
        _close();
        
        // Trigger an event:
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addSourceButtonActionPerformed

    
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
    private javax.swing.JButton addSourceButton;
    private javax.swing.JButton addTagButton;
    private org.jdesktop.swingx.JXDatePicker backupDatePicker;
    private javax.swing.JLabel backupDatePickerLabel;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JLabel commentTextAreaLabel;
    private javax.swing.JScrollPane commentTextAreaScrollPane;
    private javax.swing.JTextField newTagNameTextField;
    private javax.swing.JLabel plantCodeLabelCaption;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JSpinner prioritySpinner;
    private javax.swing.JLabel priorityTextFieldLabel;
    private javax.swing.JTextField sourceNameTextField;
    private javax.swing.JLabel sourceNameTextFieldLabel;
    private javax.swing.JTable sourceTagsTable;
    private javax.swing.JLabel sourceTagsTableLabel;
    private javax.swing.JScrollPane sourceTagsTableScrollPane;
    private javax.swing.JComboBox tagFormatComboBox;
    private javax.swing.JLabel tagFormatComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}// EsdVariableTableDataSourceDialog
