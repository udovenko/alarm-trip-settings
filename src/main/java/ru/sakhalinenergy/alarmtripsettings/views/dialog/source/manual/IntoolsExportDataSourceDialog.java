package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Component;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.swing.table.TableColumn;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.IntoolsExportDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;


/**
 * Класс реализует панель для добавления нового источника данных - экспорта из 
 * SPI.
 * 
 * @author Denis.Udovenko
 * @version 1.0.6
 */
public class IntoolsExportDataSourceDialog extends DialogWithEvents implements ManualSourceEditingDialogObservable
{

    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final TagsSourceObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final IntoolsExportDataSourceDialogSettingsObservable config;
    private final boolean editMode;
    
    
    /**
     * Конструктор класса. Инициализирует основные компоненты.
     */
    public IntoolsExportDataSourceDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, IntoolsExportDataSourceDialogSettingsObservable config, 
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
        
        //Устанавливаем иконку и заголовок диалога:
        this.setIconImage(Main.intoolsIcon.getImage());
        this.setTitle(title);
        
        //Устанавливаем формат даты календаря:
        this.exportDatePicker.setFormats(this.dateFormat);
        
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
        
        try //Восстанавливаем конфигурацию:
        {
            _applyConfig();
        } catch (Exception exception) {}
    }//FgsVariableTableSettingsPanel

    
    /**
     * Отрисовывает списки, применяет настройки диалога и выводит его на экран.
     * 
     * @param parent Родительский фрейм, относительно которого будет выведен на экран диалог
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
     * Метод устанавливает список реализованых форматов тагов.
     * 
     * @param   formats  Список реализованых форматов тагов
     * @return  void
     */
    public void setTagNumberFormatsList(List<String> formats)
    {
        //Очищаем предыдуще перечни форматов:
        this.tagFormatComboBox.removeAllItems();
                
        //Вносим в комбо-боксы новый список форматов:
        for (int i=0; i<formats.size(); i++)
        {
            this.tagFormatComboBox.addItem(formats.get(i));
        }//for
    }//setTagNumberFormatsList
    
    
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
     * Возвращает выбранный производственный объект.
     * 
     * @return Выбранный производственный объект
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)plantsComboBox.getSelectedItem();
    }//getPlant
    
    
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
     * Возвращает имя базы данных.
     * 
     * @return Имя базы данных
     */
    public String getDatabaseName()
    {
        return databaseNameTextField.getText();
    }//getDatabaseName
    
    
    /**
     * Возвращает дату экспорта данных из базы.
     * 
     * @return Дату экспорта
     */
    public Date getExportDate()
    {
        return exportDatePicker.getDate();
    }//getExportDate
    
    
    /**
     * Возвращает дату экспорта данных из базы в виде строки.
     * 
     * @return Дату экспорта в виде строки
     */
    public String getExportDateString()
    {
        Date date = exportDatePicker.getDate();
        return dateFormat.format(date);
    }//getExportDate
    
   
    /**
     * Метод восстанавливает настройки панели из полученного экземпляра 
     * настроек.
     * 
     * @throws ParseException
     * @return void
     */
    private void _applyConfig() throws ParseException
    {
        this.setSize(Integer.parseInt(config.getDialogWidth()), Integer.parseInt(config.getDialogHeight()));
        this.setLocation(Integer.parseInt(config.getDialogLeft()), Integer.parseInt(config.getDialogTop()));
        
        //If dialog is in edit mode, properties control elements will display current entity data:
        if (editMode)
        {
            Source source = model.getEntity();
            
            sourceNameTextField.setText(source.getName());
            prioritySpinner.setValue(source.getPriority());
            exportDatePicker.setDate(source.getDate());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.COMMENT.ID) commentTextArea.setText(tempProperty.getValue());
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.DATABASE_NAME.ID) databaseNameTextField.setText(tempProperty.getValue());
            }//for
                        
        } else { //If dialog is in new source creation mode, properties control elements will display config settings:
        
            sourceNameTextField.setText(config.getDataSourceName());
            prioritySpinner.setValue(Integer.parseInt(config.getPriority()));
            commentTextArea.setText(config.getComment());
            databaseNameTextField.setText(config.getDatabaseName());
            Date date = this.dateFormat.parse(config.getExportDate());
            exportDatePicker.setDate(date);
        }//else
        
        //Restoring selected plant:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                this.plantsComboBox.setSelectedItem(tempPlant);
                break;
            }//if
        }//for
        
        //Restoring selected tag mask:
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
     * Метод устанавливает код текущего ассета для модели таблицы тагов текущего 
     * диалога и меняет метку с заголовком кода ассета.
     * 
     * @param   plantCode  Новый код ассета
     * @return  void 
     */
    /*public void setPlantCode(String plantCode)
    {
        IntoolsExportTagsTableModel tagsTableModel = (IntoolsExportTagsTableModel)this.sourceTagsTable.getModel();
        tagsTableModel.setPlantCode(plantCode);
        this.plantCodeLabel.setText(plantCode);
    }//setPlantCode*/
    
    
    /**
     * Метод устанавливает класс, описывающий таг модели данных таблицы тагов.
     * 
     * @param   TagClass  Класс тага, который будет описывать добавляемые таги таблицы
     * @return  void
     */
    /*public void setTableModelTagClass(Class<? extends SourceTag> TagClass)
    {
        IntoolsExportTagsTableModel tagsTableModel = (IntoolsExportTagsTableModel)this.sourceTagsTable.getModel();
        tagsTableModel.setTagClass(TagClass);
    }//setTableModelTagFormat*/
    
    
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
        exportDatePicker = new org.jdesktop.swingx.JXDatePicker();
        exportDatePickerLabel = new javax.swing.JLabel();
        commentTextAreaLabel = new javax.swing.JLabel();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();
        commentTextAreaScrollPane = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        sourceTagsTableScrollPane = new javax.swing.JScrollPane();
        sourceTagsTable = new javax.swing.JTable();
        sourceTagsTableLabel = new javax.swing.JLabel();
        addSourceButton = new javax.swing.JButton();
        databaseNameTextField = new javax.swing.JTextField();
        databaseNameTextFieldLabel = new javax.swing.JLabel();
        prioritySpinner = new javax.swing.JSpinner();
        plantsComboBox = new javax.swing.JComboBox();
        newTagNameTextField = new javax.swing.JTextField();
        addTagButton = new javax.swing.JButton();

        plantCodeLabelCaption.setText("Current plant code:");

        sourceNameTextFieldLabel.setText("Name:");

        priorityTextFieldLabel.setText("Priority:");

        exportDatePickerLabel.setText("Export date:");

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

        databaseNameTextFieldLabel.setText("Database name:");

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
                    .addComponent(databaseNameTextField)
                    .addComponent(sourceTagsTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(plantCodeLabelCaption)
                            .addComponent(sourceNameTextFieldLabel)
                            .addComponent(exportDatePickerLabel)
                            .addComponent(tagFormatComboBoxLabel)
                            .addComponent(sourceNameTextField)
                            .addComponent(exportDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                            .addComponent(tagFormatComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(commentTextAreaScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(commentTextAreaLabel)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(prioritySpinner, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(priorityTextFieldLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(newTagNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTagButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(addSourceButton, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sourceTagsTableLabel)
                            .addComponent(databaseNameTextFieldLabel))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(exportDatePickerLabel)
                    .addComponent(commentTextAreaLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(exportDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tagFormatComboBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(commentTextAreaScrollPane))
                .addGap(13, 13, 13)
                .addComponent(databaseNameTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(databaseNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
     * @param   evt  событие выбора формата тага
     * @return  void 
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
        
        this.setVisible(false);
        
        CustomEvent saveSourceEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SAVE_SOURCE_DATA, saveSourceEvent);
    }//GEN-LAST:event_addSourceButtonActionPerformed

    
    /**
     * Метод обрабатывает событие выбора производственного объекта и рассылает 
     * всем подписчикам событие с контекстом сущности объекта.
     * 
     * @param evt Событие нажатия кнопки
     */
    private void plantsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantsComboBoxActionPerformed
        
        Plant plant = (Plant)plantsComboBox.getSelectedItem();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        this.events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);
    }//GEN-LAST:event_plantsComboBoxActionPerformed

    
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addSourceButton;
    private javax.swing.JButton addTagButton;
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JLabel commentTextAreaLabel;
    private javax.swing.JScrollPane commentTextAreaScrollPane;
    private javax.swing.JTextField databaseNameTextField;
    private javax.swing.JLabel databaseNameTextFieldLabel;
    private org.jdesktop.swingx.JXDatePicker exportDatePicker;
    private javax.swing.JLabel exportDatePickerLabel;
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
