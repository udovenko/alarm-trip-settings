package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.models.config.VariableTableDataSourceDialogSettingsObservable;
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
 * Класс реализует панель для добавления нового источника данных - таблицы 
 * переменных FGS вручную.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class FgsVariableTableDataSourceDialog extends DialogWithEvents implements ManualSourceEditingDialogObservable
{
    
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final TagsSourceObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final VariableTableDataSourceDialogSettingsObservable config;
    private final boolean editMode;
    
    
    /**
     * Конструктор класса. Инициализирует основные компоненты.
     */
    public FgsVariableTableDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, VariableTableDataSourceDialogSettingsObservable config, 
        boolean editMode, String title)
    {
        initComponents();
        
        //Setting up instance fields:
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        this.editMode = editMode;
                
        //Subsceribing on model events:
        this.model.on(SourceEvent.TAG_SET_UPDATED, new ModelTagSetUpdateHandler());
        
        //Setting up dialog icon and title:
        this.setIconImage(Main.fgsIcon.getImage());
        this.setTitle(title);
        
        //Устанавливаем формат даты календаря:
        this.backupDatePicker.setFormats(this.dateFormat);
        
        //Создаем модель данных таблицы тагов:
        this.sourceTagsTable.setModel(new TagsTableModel(model, this));
        TagsTableModel tagsTableModel = (TagsTableModel)this.sourceTagsTable.getModel();
                        
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
    }//FgsVariableTableSettingsPanel
   
    
    /**
     * Отрисовывает списки, применяет настройки диалога и выводит его на экран.
     * 
     * @param parent Родительский фрейм, относитеольно которого будет выведен на экран диалог
     */
    public void render(Component parent)
    {
        this.setLocationRelativeTo(parent);
        
        //Формируем список форматов тагов:
        for (TagMask tempMask : tagMasks.getMasks())
        {
            tagFormatComboBox.addItem(tempMask);
        }//for
        
        //Строим список производственных объектов:
        for (Plant tempPlant : plants.getPlants()) this.plantsComboBox.addItem(tempPlant);
                        
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
     * Возвращает имя источника данных.
     * 
     * @return Имя источника данных
     */
    public String getDataSourceName()
    {
        return sourceNameTextField.getText();
    }//getDataSourceName
         
    
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
    @Override
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatComboBox.getSelectedItem();
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
    }//getBackupDate
    
    
    /**
     * Метод возвращает дату ревизии документа.
     * 
     * @return Дата ревизии документа
     */
    public String getBackupDateAsString()
    {
        Date date = backupDatePicker.getDate();
        return dateFormat.format(date);
    }//getBackupDateAsString
    
    
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
            sourceNameTextField.setText(source.getName());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.COMMENT.ID) commentTextArea.setText(tempProperty.getValue());
            }//for
     
        } else {
            
            sourceNameTextField.setText(config.getDataSourceName());
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
                this.tagFormatComboBox.setSelectedItem(tempTagMask);
                break;
            }//if
        }//for
    }//_applyConfig
   
    
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
        setMinimumSize(new java.awt.Dimension(200, 300));

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

        addSourceButton.setText("Add FGS Variable Table source");
        addSourceButton.setActionCommand("Save");
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
                    .addComponent(sourceTagsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
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
                            .addComponent(commentTextAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(commentTextAreaLabel)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(prioritySpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(priorityTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sourceTagsTableLabel)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
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
     * Метод обрабатывает событие выбора нового формата тага в выпадающем списке
     * и рассылает событие с контекстом выбранного формата тага всем 
     * подписчикам.
     * 
     * @param evt Событие выбора формата тага
     */
    private void tagFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatComboBoxActionPerformed
        
        TagMask tagFormat = (TagMask)this.tagFormatComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatComboBoxActionPerformed

    
    /**
     * Метод обрабатывает нажатие кнопки "Save FGS Variable Table data source"
     * и рассылает всем подписчикам событие с контекстом текущих настроек
     * панели.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void addSourceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSourceButtonActionPerformed
     
        //Hide dialog:
        this.setVisible(false);
        
        //Trigger save source event:
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addSourceButtonActionPerformed

    
    /**
     * Метод обрабатывает событие выбора нового формата тага в выпадающем списке
     * и рассылает событие с контекстом выбранного формата тага всем 
     * подписчикам.
     * 
     * @param evt Cобытие выбора формата тага
     */
    private void plantsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantsComboBoxActionPerformed

        Plant plant = (Plant)plantsComboBox.getSelectedItem();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        this.events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);
    }//GEN-LAST:event_plantsComboBoxActionPerformed

    
    /**
     * Обрабатывает событие выбора нового производственного объекта.
     * 
     * @param evt Событие комбобокса
     */
    private void addTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTagButtonActionPerformed
        
        CustomEvent inputNewTagEvent = new CustomEvent(newTagNameTextField.getText());
        this.events.trigger(ViewEvent.TAG_NAME_INPUT, inputNewTagEvent);
    }//GEN-LAST:event_addTagButtonActionPerformed

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
}//FgsVariableTableSettingsPanel
