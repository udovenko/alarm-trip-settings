package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import java.util.Date;
import java.util.Collections;
import javax.swing.table.TableColumn;
import java.awt.Component;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.config.DocumentDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.DialogWithEvents;


/**
 * Класс реализует панель для добавления нового источника данных - документа.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.7
 */
public class DocumentDataSourceDialog extends DialogWithEvents implements ManualSourceEditingDialogObservable
{
    
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final TagsSourceObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final DocumentDataSourceDialogSettingsObservable config;
    private final boolean editMode;
    
        
    /**
     * Конструктор класса.
     * 
     * @param model Экземпляр модели источника данных тагов
     * @param plants Коллекция производиственных объектов
     * @param tagMasks Коллекция маск (регулярных выражений) для парсинга имен тагов
     * @param config Экземпляр модели конфигурации
     * @param title Заголовок диалога
     */
    public DocumentDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, DocumentDataSourceDialogSettingsObservable config, 
        boolean editMode, String title) 
    {
        initComponents();
        
        //Устанавливаем поля модели и конфигурации:
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        this.editMode = editMode;
        
        //Подписываемся на события модели:
        this.model.on(SourceEvent.TAG_SET_UPDATED, new ModelTagSetUpdateHandler());
        
        //Устанавливаем иконку диалога:
        this.setIconImage(Main.documentsIcon.getImage());
        
        //Устанавливаем заголовок:
        this.setTitle(title);
        
        //Устанавливаем формат даты календаря:
        this.documentRevisionDatePicker.setFormats(this.dateFormat);
        
        //Создаем модель данных таблицы тагов:
        this.sourceTagsTable.setModel(new TagsTableModel(this.model, this));
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
    }//DocumentSettingsPanel
    
            
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
            tagFormatsComboBox.addItem(tempMask);
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
        return this.documentNameTextField.getText();
    }//getDataSourceName
    
    
    /**
     * Возвращает ссылку на документ в системе документооборота.
     * 
     * @return Cсылку на документ в системе документооборота
     */
    public String getDocumentLink()
    {
        return this.documentLinkTextField.getText();
    }//getDocumentLink
    
    
    /**
     * Возвращает номер документа.
     * 
     * @return Номер документа
     */
    public String getDocumemntNumber()
    {
        return this.documentNumberTextField.getText(); 
    }//getDocumemntNumber
    
    
    /**
     * Возвращвает приоритет источника данных.
     * 
     * @return Приоритет источника данных
     */
    public String getPriority()
    {
        return this.prioritySpinner.getValue().toString();
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
        return (String)this.commentTextArea.getText();
    }//getComment
    
    
    /**
     * Метод возвращает дату ревизии документа в виде строки.
     * 
     * @return Дата ревизии документа
     */
    public Date getRevisionDate()
    {
        return this.documentRevisionDatePicker.getDate();
    }//getRevisionDate
    
    
    /**
     * Метод возвращает дату ревизии документа в виде строки.
     * 
     * @return Дата ревизии документа
     */
    public String getRevisionDateString()
    {
        Date date = this.documentRevisionDatePicker.getDate();
        return this.dateFormat.format(date);
    }//getRevisionDate
    
    
    /**
     * Метод восстанавливает настройки панели из полученного экземпляра 
     * настроек.
     * 
     * @throws  ParseException
     * @param   settings        Экземпляр настроек панели
     * @return  void
     */
    private void _applyConfig() throws ParseException
    {
        this.setSize(Integer.parseInt(config.getDialogWidth()), Integer.parseInt(config.getDialogHeight()));
        this.setLocation(Integer.parseInt(config.getDialogLeft()), Integer.parseInt(config.getDialogTop()));
        
        //Если диалог открыт в режиме редактирования существующего источника данных:
        if (editMode)
        {
            Source source = model.getEntity();
            documentNameTextField.setText(source.getName());
            prioritySpinner.setValue(source.getPriority());
            documentRevisionDatePicker.setDate(source.getDate());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.COMMENT.ID) commentTextArea.setText(tempProperty.getValue());
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DOCUMENT_LINK.ID) documentLinkTextField.setText(tempProperty.getValue());
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DOCUMENT_NUMBER.ID) documentNumberTextField.setText(tempProperty.getValue());
            }//for
       
        } else { //Если диалог открыт в режиме создания: 
            
            documentNameTextField.setText(config.getDataSourceName());
            prioritySpinner.setValue(Integer.parseInt(config.getPriority()));
            documentLinkTextField.setText(config.getDocumentLink());
            documentNumberTextField.setText(config.getDocumentNumber());
            commentTextArea.setText(config.getComment());
            Date date = this.dateFormat.parse(config.getRevisionDate());
            documentRevisionDatePicker.setDate(date);
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
    }//restoreSettings
    
       
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
     * Метод обрабатывает событие выбора нового формата тага в выпадающем списке
     * и рассылает событие с контекстом выбранного формата тага всем 
     * подписчикам.
     * 
     * @param   evt  событие выбора формата тага
     * @return  void 
     */
    private void tagFormatsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatsComboBoxActionPerformed
        
        TagMask tagMask = (TagMask)this.tagFormatsComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagMask);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatsComboBoxActionPerformed

    
    /**
     * Метод обрабатывает нажатие кнопки "Save Document source" и рассылает всем
     * подписчикам событие с контекстом текущих настроек панели.
     * 
     * @param   evt  Событие нажатия кнопки
     * @return  void
     */
    private void addDocumentSourceToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDocumentSourceToStorageButtonActionPerformed
        
        this.setVisible(false);
        
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addDocumentSourceToStorageButtonActionPerformed

    
    /**
     * Обрабатывает событие нажатия кнопки добавления тага.
     * 
     * @param evt Событие нажатия кнопки
     * @return void
     */
    private void addTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTagButtonActionPerformed

        CustomEvent inputNewTagEvent = new CustomEvent(newTagNameTextField.getText());
        this.events.trigger(ViewEvent.TAG_NAME_INPUT, inputNewTagEvent);
    }//GEN-LAST:event_addTagButtonActionPerformed

    
    /**
     * Обрабатывает событие выбора нового производственного объекта.
     * 
     * @param evt Событие комбобокса
     * @return void
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
}//DocumentSettingsPanel
