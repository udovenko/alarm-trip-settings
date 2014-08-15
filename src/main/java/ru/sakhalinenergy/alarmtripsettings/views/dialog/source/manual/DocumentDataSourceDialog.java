package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.util.Date;
import java.util.Collections;
import java.awt.Component;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.DocumentDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;


/**
 * Implements dialog for create/edit document data source.
 * 
 * @author Denis Udovenko
 * @version 1.0.7
 */
public class DocumentDataSourceDialog extends ManualSourceEditingDialog implements ManualSourceEditingDialogObservable
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
    public DocumentDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, DocumentDataSourceDialogSettingsObservable config, 
        boolean editMode, String title) 
    {
        // Call superclass constructor:
        super(model, plants, tagMasks, config, editMode, title);
        
        initComponents();
        
        // Subscribe on model's events:
        model.on(SourceEvent.TAG_SET_UPDATED, _getModelTagSetUpdateHandler(sourceTagsTable));
        
        // Set dialog icon:
        setIconImage(Main.documentsIcon.getImage());
        
        // Set up calendar date format:
        documentRevisionDatePicker.setFormats(dateFormat);
        
        // Set tags table model:
        sourceTagsTable.setModel(new TagsTableModel(model, this));
        TagsTableModel tagsTableModel = (TagsTableModel)sourceTagsTable.getModel();
                        
        // Set cell renderes depending on column name:
        for (TableColumn column : Collections.list(sourceTagsTable.getColumnModel().getColumns()))
        {
            if (column.getHeaderValue().equals(tagsTableModel.REMOVE_TAG_BUTTON_COLUMN_NAME))
            {
                column.setCellEditor(new TagsTableButtonEditor());
                column.setCellRenderer(new TagsTableButtonRenderer());
                
            } else {   
                
                column.setCellRenderer(new TagsTableCellRenderer());
            }// else
        }// for
    }// DocumentSettingsPanel
    
            
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
        
        // Set relative location:
        setLocationRelativeTo(parent);
        
        // Apply config:
        _applyConfig();
                
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
        return documentNameTextField.getText();
    }// getDataSourceName
    
    
    /**
     * Returns link to a source document.
     * 
     * @return Link to a source document
     */
    public String getDocumentLink()
    {
        return documentLinkTextField.getText();
    }// getDocumentLink
    
    
    /**
     * Returns source document number.
     * 
     * @return Source document number
     */
    public String getDocumemntNumber()
    {
        return this.documentNumberTextField.getText(); 
    }// getDocumemntNumber
    
    
    /**
     * Returns data source priority.
     * 
     * @return Data source priority
     */
    public String getPriority()
    {
        return this.prioritySpinner.getValue().toString();
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
    }// getTagFormat
    
    
    /**
     * Returns data source comment.
     * 
     * @return Data source comment
     */
    public String getComment()
    {
        return (String)this.commentTextArea.getText();
    }// getComment
    
    
    /**
     * Returns source document revision date.
     * 
     * @return Source document revision date
     */
    public Date getRevisionDate()
    {
        return this.documentRevisionDatePicker.getDate();
    }// getRevisionDate
    
    
    /**
     * Returns source document revision date as string.
     * 
     * @return Source document revision date as string
     */
    public String getRevisionDateString()
    {
        Date date = this.documentRevisionDatePicker.getDate();
        return this.dateFormat.format(date);
    }// getRevisionDate
    
    
    /**
     * Restores dialog's settings from configuration object.
     */
    private void _applyConfig()
    {
        // Call superclass configuration method:
        _applyConfig(plantsComboBox, tagFormatsComboBox, prioritySpinner,
            documentNameTextField, commentTextArea);
        
        // Cast models to concrete classes:
        DocumentDataSourceDialogSettingsObservable castedConfig 
            = (DocumentDataSourceDialogSettingsObservable)config;
        
        // Apply rest of configuration settings:
        if (editMode)
        {
            Source source = ((TagsSourceObservable)model).getEntity();
            documentRevisionDatePicker.setDate(source.getDate());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DOCUMENT_LINK.ID) documentLinkTextField.setText(tempProperty.getValue());
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DOCUMENT_NUMBER.ID) documentNumberTextField.setText(tempProperty.getValue());
            }// for
       
        } else { // If dialog opens in create mode:
            
            documentLinkTextField.setText(castedConfig.getDocumentLink());
            documentNumberTextField.setText(castedConfig.getDocumentNumber());
            try
            {
                Date date = this.dateFormat.parse(castedConfig.getRevisionDate());
                documentRevisionDatePicker.setDate(date);
            } catch (Exception exception){}
        }// else
    }// restoreSettings
    
       
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        documentNameTextFieldLabel = new javax.swing.JLabel();
        documentNameTextField = new javax.swing.JTextField();
        priorityTextFieldLabel = new javax.swing.JLabel();
        documentRevisionDatePicker = new org.jdesktop.swingx.JXDatePicker();
        documentRevisionDatePickerLabel = new javax.swing.JLabel();
        tagFormatsComboBox = new javax.swing.JComboBox();
        tagFormatsComboBoxLabel = new javax.swing.JLabel();
        commentTextAreaLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        documentNumberTextFieldLabel = new javax.swing.JLabel();
        documentNumberTextField = new javax.swing.JTextField();
        documentLinkTextFieldLabel = new javax.swing.JLabel();
        documentLinkTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        sourceTagsTable = new javax.swing.JTable();
        sourceTagsTableLabel = new javax.swing.JLabel();
        addDocumentSourceToStorageButton = new javax.swing.JButton();
        plantsComboBoxCaption = new javax.swing.JLabel();
        prioritySpinner = new javax.swing.JSpinner();
        newTagNameTextField = new javax.swing.JTextField();
        addTagButton = new javax.swing.JButton();
        plantsComboBox = new javax.swing.JComboBox();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        setAlwaysOnTop(true);

        documentNameTextFieldLabel.setText("Document name:");

        priorityTextFieldLabel.setText("Priority:");

        documentRevisionDatePickerLabel.setText("Revision date:");

        tagFormatsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatsComboBoxActionPerformed(evt);
            }
        });

        tagFormatsComboBoxLabel.setText("Tag format:");

        commentTextAreaLabel.setText("Comment:");

        commentTextArea.setColumns(20);
        commentTextArea.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        commentTextArea.setLineWrap(true);
        commentTextArea.setRows(5);
        commentTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(commentTextArea);

        documentNumberTextFieldLabel.setText("Document number (include version):");

        documentLinkTextFieldLabel.setText("Document link:");

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
        jScrollPane3.setViewportView(sourceTagsTable);

        sourceTagsTableLabel.setText("Tags set table:");

        addDocumentSourceToStorageButton.setText("Save");
        addDocumentSourceToStorageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDocumentSourceToStorageButtonActionPerformed(evt);
            }
        });

        plantsComboBoxCaption.setText("Current plant:");
        plantsComboBoxCaption.setToolTipText("");

        addTagButton.setText("Add tag");
        addTagButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTagButtonActionPerformed(evt);
            }
        });

        plantsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addDocumentSourceToStorageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(documentNumberTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(documentNumberTextFieldLabel)
                            .addComponent(documentLinkTextFieldLabel)
                            .addComponent(sourceTagsTableLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(documentLinkTextField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(documentNameTextFieldLabel)
                                .addComponent(tagFormatsComboBoxLabel)
                                .addComponent(documentRevisionDatePickerLabel)
                                .addComponent(documentRevisionDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(documentNameTextField)
                                .addComponent(tagFormatsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(plantsComboBoxCaption))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(commentTextAreaLabel)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(prioritySpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(priorityTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantsComboBoxCaption)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documentNameTextFieldLabel)
                    .addComponent(priorityTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documentNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(prioritySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(documentRevisionDatePickerLabel)
                    .addComponent(commentTextAreaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(documentRevisionDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tagFormatsComboBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tagFormatsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(documentNumberTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(documentLinkTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentLinkTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(sourceTagsTableLabel)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addDocumentSourceToStorageButton)
                    .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addTagButton))
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
        
        TagMask tagMask = (TagMask)this.tagFormatsComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagMask);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatsComboBoxActionPerformed

    
    /**
     * Handles "Save document data source" button click event and triggers 
     * appropriate for all subscribers.
     * 
     * @param evt Button click event object
     */
    private void addDocumentSourceToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDocumentSourceToStorageButtonActionPerformed
        
        this.setVisible(false);
        
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addDocumentSourceToStorageButtonActionPerformed

    
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
    private javax.swing.JButton addDocumentSourceToStorageButton;
    private javax.swing.JButton addTagButton;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JLabel commentTextAreaLabel;
    private javax.swing.JTextField documentLinkTextField;
    private javax.swing.JLabel documentLinkTextFieldLabel;
    private javax.swing.JTextField documentNameTextField;
    private javax.swing.JLabel documentNameTextFieldLabel;
    private javax.swing.JTextField documentNumberTextField;
    private javax.swing.JLabel documentNumberTextFieldLabel;
    private org.jdesktop.swingx.JXDatePicker documentRevisionDatePicker;
    private javax.swing.JLabel documentRevisionDatePickerLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField newTagNameTextField;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JLabel plantsComboBoxCaption;
    private javax.swing.JSpinner prioritySpinner;
    private javax.swing.JLabel priorityTextFieldLabel;
    private javax.swing.JTable sourceTagsTable;
    private javax.swing.JLabel sourceTagsTableLabel;
    private javax.swing.JComboBox tagFormatsComboBox;
    private javax.swing.JLabel tagFormatsComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}// DocumentDataSourceDialog
