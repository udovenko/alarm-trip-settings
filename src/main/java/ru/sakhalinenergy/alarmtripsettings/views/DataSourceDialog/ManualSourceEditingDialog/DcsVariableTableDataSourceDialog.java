package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.awt.Component;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.config.DcsVariableTableDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;


/**
 * Класс реализует панель настроек для добавления в базу новой таблицы 
 * переменных DCS.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class DcsVariableTableDataSourceDialog extends DialogWithEvents implements ManualSourceEditingDialogObservable
{
    
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final TagsSourceObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final DcsVariableTableDataSourceDialogSettingsObservable config;
    private final boolean editMode;
    
    
    /**
     * Public constructor.
     * 
     * @param model Tags source wrapper
     * @param plants Plants list wrapper
     * @param tagMasks Wrapped masks collection for tag parsing
     * @param config Configuration instance
     * @param editMode New source / edit source mode flag
     */
    public DcsVariableTableDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants,
        TagMasksObservable tagMasks, DcsVariableTableDataSourceDialogSettingsObservable config,
        boolean editMode, String title) 
    {
        initComponents();
        
        //Setting instance fields:
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        this.editMode = editMode;
               
        //Setting dialog icon and title:
        setIconImage(Main.dcsIcon.getImage());
        setTitle(title);
        
        //Устанавливаем формат даты календаря:
        this.backupDatePicker.setFormats(this.dateFormat);
        
        //Подписываемся на события модели:
        this.model.on(SourceEvent.TAG_SET_UPDATED, new ModelTagSetUpdateHandler());
        
        //Создаем модель данных таблицы тагов:
        this.sourceTagsTable.setModel(new DcsTagsTableModel(model, this));
        DcsTagsTableModel tagsTableModel = (DcsTagsTableModel)this.sourceTagsTable.getModel();
                
        //Назначаем всем колонкам таблицы рендерер текущего набора устройств:
        for (TableColumn column : Collections.list(this.sourceTagsTable.getColumnModel().getColumns()))
        {
            if (column.getHeaderValue().equals(tagsTableModel.REMOVE_TAG_BUTTON_COLUMN_NAME))
            {
                column.setCellEditor(new TagsTableButtonEditor());
                column.setCellRenderer(new TagsTableButtonRenderer());
                
            } else {   
                
                column.setCellRenderer(new TagsTableCellRenderer());
            }//else
        }//for
    }//DcsVariableTableSettingsPanel
       
    
    /**
     * Sets dialog lists, applies configuration and renders view.
     * 
     * @param parent Parent component, dialog will be rendered relative to it
     */
    public void render(Component parent)
    {
        setLocationRelativeTo(parent);
        
        //Формируем список форматов тагов:
        for (TagMask tempMask : tagMasks.getMasks()) tagFormatsComboBox.addItem(tempMask);

        //Строим список производственных объектов:
        for (Plant tempPlant : plants.getPlants()) plantsComboBox.addItem(tempPlant);
        
        try //Восстанавливаем конфигурацию:
        {
            _applyConfig();
        } catch (Exception exception) {}
        
         //Отображаем диалог:
        this.setVisible(true);
    }//render
    
    
    /**
     * Внутренний класс-обработчик события обновления набора тагов модели.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.1
     */
    class ModelTagSetUpdateHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            TagsTableModel tagsTableModel = (TagsTableModel)sourceTagsTable.getModel();
            tagsTableModel.fireTableDataChanged();
        }//customEventOccurred
    }//TagNameInputHandler
    
    
    /**
     * Возвращает горизонтальный отступ левого верхнего угла формы от 
     * начала координат экрана.
     *
     * @return Горизонтальный отступ левого верхнего угла формы от начала координат экрана
     */
    public String getFormX()
    {
        return Integer.toString(this.getX());
    }//getFormX    
    
    
    /**
     * Возвращает вертикальный отступ левого верхнего угла формы от 
     * начала координат экрана.
     *
     * @return Вертикальный отступ левого верхнего угла формы от начала координат экрана
     */
    public String getFormY()
    {
        return Integer.toString(this.getY());
    }//getFormX
    
    
    /**
     * Возвращает ширину формы.
     * 
     * @return Ширина формы
     */
    public String getFormWidth()
    {
        return Integer.toString(this.getWidth());
    }//getFormWidth
    
    
    /**
     * Возвращает высоту формы.
     * 
     * @return Высота формы
     */
    public String getFormHeigt()
    {
        return Integer.toString(this.getHeight());
    }//getFormHeigt
    
    
    /**
     * Возвращает выбранный производственный объект.
     * 
     * @return Выбранный производственный объект
     */
    public Plant getPlant()
    {
        return (Plant)plantsComboBox.getSelectedItem();
    }//getPlant
    
    
    /**
     * Возвращает имя источника данных.
     * 
     * @return Имя источника данных
     */
    public String getDataSourceName()
    {
        return this.backupNameTextField.getText();
    }//getDataSourceName
           
    
    /**
     * Возвращвает приоритет источника данных.
     * 
     * @return Приоритет источника данных
     */
    public String getPriority()
    {
        return prioritySpinner.getValue().toString();
    }//getPriority
    
    
    /**
     * Возвращает текущий формат тага.
     * 
     * @return Текущий формат тага
     */
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatsComboBox.getSelectedItem();
    }//getTagFormat
    
    
    /**
     * Возвращает комментарий к источнику данных.
     * 
     * @return Комментарий к источнику данных
     */
    public String getComment()
    {
        return (String)commentTextArea.getText();
    }//getComment
    
    
    /**
     * Метод возвращает дату ревизии документа.
     * 
     * @return Дата ревизии документа
     */
    public Date getBackupDate()
    {
        return backupDatePicker.getDate();
    }//getRevisionDate
    
    
    /**
     * Метод возвращает дату ревизии документа в строковом предствалении.
     * 
     * @return Дата ревизии документа в строковом предствалении
     */
    public String getBackupDateAsString()
    {
        Date date = backupDatePicker.getDate();
        return dateFormat.format(date);
    }//getRevisionDate
       
    
    /**
     * Возвращает значение флага, указывающего на необходимость создавать 
     * контуры для тагов, для которых они не созданы.
     * 
     * @return Значение флага, указывающего на необходимость создавать контуры
     */
    public String getCreateLoopsIfNotExistFlag()
    {
        Boolean createLoopsIfNotExist  = this.createLoopsIfNotExistCheckBox.isSelected();
        return createLoopsIfNotExist.toString();
    }//getCreateLoopsIfNotExistFlag
         
    
    /**
     * Метод восстанавливает настройки панели из полученного экземпляра 
     * настроек.
     * 
     * @throws ParseException
     * @param settings Экземпляр настроек панели
     * @return void
     */
    private void _applyConfig() throws ParseException
    {
        this.setSize(Integer.parseInt(config.getDialogWidth()), Integer.parseInt(config.getDialogHeight()));
        this.setLocation(Integer.parseInt(config.getDialogLeft()), Integer.parseInt(config.getDialogTop()));
        
        if (editMode)
        {
            Source source = model.getEntity();
            prioritySpinner.setValue(source.getPriority());
            backupDatePicker.setDate(source.getDate());
            backupNameTextField.setText(source.getName());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.COMMENT.ID) commentTextArea.setText(tempProperty.getValue());
            }//for
     
        } else {
            
            backupNameTextField.setText(config.getDataSourceName());
            prioritySpinner.setValue(Integer.parseInt(config.getPriority()));
            commentTextArea.setText(config.getComment());
            Date date = this.dateFormat.parse(config.getBackupDate());
            backupDatePicker.setDate(date);
        }//else
        
        //Восстанавливаем выбранный производственный объект:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                this.plantsComboBox.setSelectedItem(tempPlant);
                break;
            }//if
        }//for
        
        for (TagMask tempTagMask : this.tagMasks.getMasks())
        {    
            if (tempTagMask.getExample().equals(config.getTagFormat()))
            {    
                this.tagFormatsComboBox.setSelectedItem(tempTagMask);
                break;
            }//if
        }//for
        
        this.createLoopsIfNotExistCheckBox.setSelected(new Boolean(config.getCreateLoopsIfNotExistFlag()));
    }//_applyConfig
    
    
    /**
     * Метод устанавливает список реализованых форматов тагов.
     * 
     * @param   formats  Список реализованых форматов тагов
     * @return  void
     */
    public void setTagNumberFormatsList(List<String> formats)
    {
        //Очищаем предыдуще перечни форматов:
        this.tagFormatsComboBox.removeAllItems();
                
        //Вносим в комбо-боксы новый список форматов:
        for (int i=0; i<formats.size(); i++)
        {
            this.tagFormatsComboBox.addItem(formats.get(i));
        }//for
    }//setTagNumberFormatsList

    
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
     * Метод обрабатывает событие выбора нового формата тага в выпадающем списке
     * и рассылает событие с контекстом выбранного формата тага всем 
     * подписчикам.
     * 
     * @param evt Cобытие выбора формата тага
     */
    private void tagFormatsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatsComboBoxActionPerformed
        
        TagMask tagFormat = (TagMask)this.tagFormatsComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatsComboBoxActionPerformed

    
    /**
     * Метод обрабатывает нажатие кнопки "Save DCS Variable Table data source"
     * и рассылает всем подписчикам событие.
     * 
     * @param evt Событие нажатия кнопки
     * @return void
     */
    private void addDumpSourceToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDumpSourceToStorageButtonActionPerformed
        
        //Hiding dialog:
        this.setVisible(false);
        
        //Triggering an event:
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addDumpSourceToStorageButtonActionPerformed

    
    /**
     * Обрабатывает событие нажатия кнопки добавления тага.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void addTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTagButtonActionPerformed
        
        CustomEvent inputNewTagEvent = new CustomEvent(newTagNameTextField.getText());
        this.events.trigger(ViewEvent.TAG_NAME_INPUT, inputNewTagEvent);
    }//GEN-LAST:event_addTagButtonActionPerformed

    
    /**
     * Обрабатывает событие выбора нового производственного объекта.
     * 
     * @param evt Событие комбобокса
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
}//DcsVariableTableSettingsPanel
