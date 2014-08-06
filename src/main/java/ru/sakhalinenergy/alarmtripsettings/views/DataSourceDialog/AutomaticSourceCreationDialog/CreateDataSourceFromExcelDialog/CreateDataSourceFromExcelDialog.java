package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDataSourceFromExcelDialog;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.TableColumn;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Component;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.ExcelBookObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.ViewEvent;


/**
 * Класс реализует диалог для создания источника данных из листа MS Excel. 
 * Является подписчиком полученной в конструкторе модели (паттерн Observer), 
 * также принимает подписку контроллеров на собственные события. В обоих случаях 
 * используется протокол вытягивания данных, т.е. подписчики не получают 
 * информацию из контекста событий, я запрашивают ее сами через интерфейс 
 * класса-субъекта.
 * 
 * @author Denis.Udovenko
 * @version 1.0.8
 */
public class CreateDataSourceFromExcelDialog extends DialogWithEvents implements CreateDataSourceFromExcelDialogObservable
{

    private static final String NO_FIELD_SELECTED_VALUE = null;
    
    private static final Byte KEY_FIELD_COLUMN_INDEX           = 1;
    private static final Byte KEY_VALUE_COLUMN_INDEX           = 2;
    private static final Byte VALUE_FIELD_COLUMN_INDEX         = 3;
    private static final Byte POSSIBLE_FLAG_FIELD_COLUMN_INDEX = 4;
    
    private static final Byte ALARM_LOW_LOW_ROW_INDEX   = 0;
    private static final Byte ALARM_LOW_ROW_INDEX       = 1;
    private static final Byte ALARM_HIGH_ROW_INDEX      = 2;
    private static final Byte ALARM_HIGH_HIGH_ROW_INDEX = 3;
    private static final Byte RANGE_MIN_ROW_INDEX       = 4;
    private static final Byte RANGE_MAX_ROW_INDEX       = 5;
    private static final Byte UNITS_ROW_INDEX           = 6;
    private static final Byte SOURCE_SYSTEM_ROW_INDEX   = 7;
    
    private final ExcelBookObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final ExcelBookParsingDialogSettingsObservable config;
        
    
    /**
     * Конструктор диалога.
     */
    public CreateDataSourceFromExcelDialog(ExcelBookObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, ExcelBookParsingDialogSettingsObservable config) 
    {
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
                  
        //Подписываеся на события модели:
        this.model.on(SourceEvent.BOOK_CONNECTED, new _BookConnectedEventHandler());
                    
        initComponents();
        
        //Устанавливаем иконку диалога:
        this.setIconImage(Main.excelIcon.getImage());
        this.setModal(true);
        
        //Запрещаем перемену столбоцов таблицы местами и изменение их ширины:
        parsingSettingsTable.getTableHeader().setReorderingAllowed(false);
        parsingSettingsTable.getTableHeader().setResizingAllowed(false);
        
        //Формируем список доступных типов источников данных:
        try
        {
            for (Class innerClass : SourcesTypes.class.getClasses())
            {
                dataSourceToBeCreatedTypeComboBox.addItem(innerClass.getDeclaredField("NAME").get(null));
            }//for
        } catch (Exception exception) {}
        dataSourceToBeCreatedTypeComboBox.setRenderer(new DataSourcesListCellRenderer());
        dataSourceToBeCreatedTypeComboBox.setSelectedItem(config.getDataSourceTypeName());
    }//CreateDataSourceFromExcelDialog

    
    /**
     * Отрисовывает списки, применяет настройки диалога и выводит его на экран.
     * 
     * @param parent Родительский фрейм, относитеольно которого будет выведен на экран диалог
     */
    public void render(Component parent)
    {
        //Строим список производственных объектов:
        for (Plant tempPlant : plants.getPlants()) this.plantsComboBox.addItem(tempPlant);
        
        //Восстанавливаем выбранный производственный объект:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                this.plantsComboBox.setSelectedItem(tempPlant);
                break;
            }//if
        }//for
        
        //Формируем список форматов тагов:
        for (TagMask tempMask : tagMasks.getMasks()) tagFormatComboBox.addItem(tempMask);
        
        //Восстанавливаем выбранную маску тага:
        for (TagMask tempTagMask : this.tagMasks.getMasks())
        {    
            if (tempTagMask.getExample().equals(config.getTagFormat()))
            {    
                this.tagFormatComboBox.setSelectedItem(tempTagMask);
                break;
            }//if
        }//for
        
        //Блокируем элементы управления настройками до получения соответствующего события от модели:
        _setSettingsEnabled(false);
        
        //Вываодим диалог на экран:
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }//render
    
    
    /**
     * Внутренний класс - обработчик события модели установки соединения с 
     * книгой MS Excel.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _BookConnectedEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            pathToSpreadsheetTextField.setText(model.getBookFilePath());
            HashMap sheetsAndFields = model.getSheetsAndFields();
             _setSheetNamesList(new ArrayList(sheetsAndFields.keySet()));
             _setSettingsEnabled(true);
             _applyConfig();
        }//customEventOccurred
    }//_BookConnectedEventHandler
    
    
    /**
     * Метод устанавливает набор названий листов подключенной книги MS Excel.
     * 
     * @param sheetNamesList Набор названий листов книги MS Excel
     */
    private void _setSheetNamesList(List<String> sheetNamesList)
    {
        this.sourceSheetNameComboBox.removeAllItems();
        for (String name : sheetNamesList)
        {
            this.sourceSheetNameComboBox.addItem(name);
        }//for
    }//setSheetNamesList
          
    
    /**
     * Метод управляет доступностью элементов управления для настройки 
     * параметров обработки книги MS Excel.
     * 
     * @param enabled Флаг, указывающий, сделать доступными или недоступными элементы управления диалога
     */
    private void _setSettingsEnabled(boolean enabled)
    {
        this.sourceSheetNameComboBox.setEnabled(enabled);
        this.tagNameFieldNameComboBox.setEnabled(enabled);
        this.parsingSettingsTable.setEnabled(enabled);
        this.runParsingButton.setEnabled(enabled);
    }//disableSettings
          
    
    /**
     * Метод применяет сохраненную конфигурацию к таблице нрастроек парсинга при
     * условии, что сохраненные в конфигурации значения присцутвуют в списке
     * доступных полей выбранного листа.
     */
    private void _applyConfig()
    {
        //Получаем список доступных полей текущего листа книги:
        String sheetName = (String)sourceSheetNameComboBox.getSelectedItem();
        List<String> fields = (List)model.getSheetsAndFields().get(sheetName);
        
        //Восстанавливаем поле, хранящее имя тага:
        for (String tempField : (List<String>)model.getSheetsAndFields().get(sheetName))
        {    
            if (tempField.equals(config.getTagNameField()))
            {    
                this.tagNameFieldNameComboBox.setSelectedItem(tempField);
                break;
            }//if
        }//for
                
        //Настрорйки для нижнего трипа:
        if (fields.indexOf(config.getAlarmLowLowKeyField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowLowKeyField(), ALARM_LOW_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getAlarmLowLowKeyValue(), ALARM_LOW_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmLowLowValueField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowLowValueField(), ALARM_LOW_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmLowLowPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowLowPossibleFlagField(), ALARM_LOW_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        //Настройки для нижнего аларма:
        if (fields.indexOf(config.getAlarmLowKeyField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowKeyField(), ALARM_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getAlarmLowKeyValue(), ALARM_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmLowValueField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowValueField(), ALARM_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmLowPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmLowPossibleFlagField(), ALARM_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        //Настройки для верхнего аларма:
        if (fields.indexOf(config.getAlarmHighKeyField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighKeyField(), ALARM_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getAlarmHighKeyValue(), ALARM_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmHighValueField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighValueField(), ALARM_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmHighPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighPossibleFlagField(), ALARM_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);

        //Настройки для верхнего трипа:
        if (fields.indexOf(config.getAlarmHighHighKeyField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighHighKeyField(), ALARM_HIGH_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getAlarmHighHighKeyValue(), ALARM_HIGH_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmHighHighValueField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighHighValueField(), ALARM_HIGH_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(config.getAlarmHighHighPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(config.getAlarmHighHighPossibleFlagField(), ALARM_HIGH_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        //Настройки для нижней границы диапазона:
        if (fields.indexOf(config.getRangeMinKeyField()) != -1) parsingSettingsTable.setValueAt(config.getRangeMinKeyField(), RANGE_MIN_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getRangeMinKeyValue(), RANGE_MIN_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getRangeMinValueField()) != -1) parsingSettingsTable.setValueAt(config.getRangeMinValueField(), RANGE_MIN_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        //Настройки для верхней границы диапазона:
        if (fields.indexOf(config.getRangeMaxKeyField()) != -1) parsingSettingsTable.setValueAt(config.getRangeMaxKeyField(), RANGE_MAX_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getRangeMaxKeyValue(), RANGE_MAX_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getRangeMaxValueField()) != -1) parsingSettingsTable.setValueAt(config.getRangeMaxValueField(), RANGE_MAX_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        //Настройки для единиц измерения:
        if (fields.indexOf(config.getUnitsKeyField()) != -1) parsingSettingsTable.setValueAt(config.getUnitsKeyField(), UNITS_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getUnitsKeyValue(), UNITS_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getUnitsValueField()) != -1) parsingSettingsTable.setValueAt(config.getUnitsValueField(), UNITS_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        //Настройки для верхней границы диапазона:
        if (fields.indexOf(config.getSourceSystemKeyField()) != -1) parsingSettingsTable.setValueAt(config.getSourceSystemKeyField(), SOURCE_SYSTEM_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(config.getSourceSystemKeyValue(), SOURCE_SYSTEM_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(config.getSourceSystemValueField()) != -1) parsingSettingsTable.setValueAt(config.getSourceSystemValueField(), SOURCE_SYSTEM_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//_applyConfig

    
    /**
     * Возвращает текущий выбранный производственный объект.
     * 
     * @return Текущий выбранный производственный объект
     */
    public Plant getPlant()
    {
        return (Plant)plantsComboBox.getSelectedItem();
    }//getPlant
        
    
    /**
     * Метод возвращает имя выбранного типа источника данных.
     * 
     * @return Имя выбранного типа источника данных
     */
    public String getDataSourceType()
    {
        return (String)dataSourceToBeCreatedTypeComboBox.getSelectedItem();
    }//getDataSourceType
    
    
    /**
     * Метод возвращает выбранную маску имени тага.
     *
     * @return Маску имени тага
     */
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatComboBox.getSelectedItem();
    }//getTagFormat
    
    
    /**
     * Метод возвращает выбранное имя листа книги MS Excel.
     * 
     * @return Выбранное имя листа книги MS Excel
     */
    public String getSheetName()
    {
        return (String)this.sourceSheetNameComboBox.getSelectedItem();
    }//getSheetName
    
    
    /**
     * Метод возвращает выбранное поле, содержащее имя тага.
     * 
     * @return Поле, содержащее имя тага
     */
    public String getTagFieldName()
    {
        return (String)this.tagNameFieldNameComboBox.getSelectedItem();
    }//getTagFieldName
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки нижнего трипа.
     * 
     * @return Имя поля, хранящего ключ уставки нижнего трипа
     */
    public String getAlarmLowLowKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getAlarmLowLowKeyField
    
    
    /**
     * Метод возвращает значение ключа уставки нижнего трипа.
     * 
     * @return Значение ключа уставки нижнего трипа
     */
    public String getAlarmLowLowKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getAlarmLowLowKeyValue
    
    
    /**
     * Метод возвращаяет имя поля, хранящего значение уставки нижнего трипа.
     * 
     * @return Имя поля, хранящего значение уставки нижнего трипа
     */
    public String getAlarmLowLowValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getAlarmLowLowValueField
    
    
    /**
     * Метод возращает имя поля, хранящего значение флага "Possible" нижнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" нижнего трипа
     */
    public String getAlarmLowLowPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }//getAlarmLowLowPossibleFlagField
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки нижнего аларма.
     * 
     * @return Имя поля, хранящего ключ уставки нижнего аларма
     */
    public String getAlarmLowKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getAlarmLowKeyField
    
    
    /**
     * Метод возвращает значение ключа уставки нижнего аларма.
     * 
     * @return Значение ключа уставки нижнего аларма
     */
    public String getAlarmLowKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getAlarmLowKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки нижнего аларма.
     * 
     * @return Имя поля, хранящего значение уставки нижнего аларма
     */
    public String getAlarmLowValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getAlarmLowValueField
    
    
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" нижнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" нижнего трипа
     */
    public String getAlarmLowPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }//getAlarmLowPossibleFlagField
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки верхнего аларма.
     * 
     * @return Имя поля, хранящего ключ уставки верхнего аларма
     */
    public String getAlarmHighKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getAlarmHighKeyField
    
    
    /**
     * Метод возвращает значение ключа уставки верхнего аларма.
     * 
     * @return Значение ключа уставки верхнего аларма
     */
    public String getAlarmHighKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getAlarmHighKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки верхнего аларма.
     * 
     * @return Имя поля, хранящего значение уставки верхнего аларма
     */
    public String getAlarmHighValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getAlarmHighValueField
    
    
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" верхнего аларма.
     * 
     * @return Имя поля, хранящего значение флага "Possible" верхнего аларма
     */
    public String getAlarmHighPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }//getAlarmHighPossibleFlagField
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ уставки верхнего трипа.
     * 
     * @return Имя поля, хранящего ключ уставки верхнего трипа
     */
    public String getAlarmHighHighKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getAlarmHighHighKeyField
    
    
    /**
     * Метод возвращает значение ключа уставки верхнего трипа.
     * 
     * @return Значение ключа уставки верхнего трипа
     */
    public String getAlarmHighHighKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getAlarmHighHighKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение уставки верхнего трипа.
     * 
     * @return Имя поля, хранящего значение уставки верхнего трипа
     */
    public String getAlarmHighHighValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getAlarmHighHighValueField
    
    
    /**
     * Метод возвращает имя поля, хранящего значение флага "Possible" верхнего трипа.
     * 
     * @return Имя поля, хранящего значение флага "Possible" верхнего трипа
     */
    public String getAlarmHighHighPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }//getAlarmHighHighPossibleFlagField
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ нижней границы диапазона.
     * 
     * @return Имя поля, хранящего ключ нижней границы диапазона
     */
    public String getRangeMinKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getRangeMinKeyField
    
    
    /**
     * Метод возвращает значение ключа нижней границы диапазона.
     * 
     * @return Значение ключа нижней границы диапазона
     */
    public String getRangeMinKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getRangeMinKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение нижней границы диапазона.
     * 
     * @return Имя поля, хранящего значение нижней границы диапазона
     */
    public String getRangeMinValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getRangeMinValueField
        
    
    /**
     * Метод возвращает имя поля, хранящего ключ верхней границы диапазона.
     * 
     * @return Имя поля, хранящего ключ верхней границы диапазона
     */
    public String getRangeMaxKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getRangeMaxKeyField
    
    
    /**
     * Метод возвращает значение ключа верхней границы диапазона.
     * 
     * @return Значение ключа верхней границы диапазона
     */
    public String getRangeMaxKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//setRangeMaxKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение верхней границы диапазона.
     * 
     * @return Имя поля, хранящего значение верхней границы диапазона
     */
    public String getRangeMaxValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getRangeMaxValueField
    
    
    /**
     * Метод возвращает имя поля, хранящего ключ единиц измерения.
     * 
     * @return Имя поля, хранящего ключ единиц измерения
     */
    public String getUnitsKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getUnitsKeyField
    
    
    /**
     * Метод возвращает значение ключа единиц измерения.
     * 
     * @return Значение ключа единиц измерения
     */
    public String getUnitsKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getUnitsKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего значение единиц измерения.
     * 
     * @return Имя поля, хранящего значение единиц измерения
     */
    public String getUnitsValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getUnitsValueField
    
    
    /**
     * Метод возвращвает имя поля, хранящего ключ названия системы-источника 
     * данных.
     * 
     * @return Имя поля, хранящего ключ названия системы-источника данных
     */
    public String getSourceSystemKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }//getSourceSystemKeyField
    
    
    /**
     * Метод возвращает значение ключа названия системы-источника данных.
     * 
     * @return Значение ключа названия системы-источника данных
     */
    public String getSourceSystemKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }//getSourceSystemKeyValue
    
    
    /**
     * Метод возвращает имя поля, хранящего название системы-источника данных.
     * 
     * @return Имя поля, хранящего название системы-источника данных
     */
    public String getSourceSystemValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }//getSourceSystemValueField
   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        parsingSettingsTable = new javax.swing.JTable();
        pathToSpreadsheetTextFieldLabel = new javax.swing.JLabel();
        pathToSpreadsheetTextField = new javax.swing.JTextField();
        selectPathToSpreadsheetTextFieldButton = new javax.swing.JButton();
        sourceSheetNameComboBoxLabel = new javax.swing.JLabel();
        sourceSheetNameComboBox = new javax.swing.JComboBox();
        tagNameFieldNameComboBoxLabel = new javax.swing.JLabel();
        tagNameFieldNameComboBox = new javax.swing.JComboBox();
        runParsingButton = new javax.swing.JButton();
        dataSourceToBeCreatedTypeComboBoxLabel = new javax.swing.JLabel();
        dataSourceToBeCreatedTypeComboBox = new javax.swing.JComboBox();
        plantsComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        plantsComboBox = new javax.swing.JComboBox();

        setTitle("Create data source from MS Excel sheet");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        parsingSettingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Alarm Low Low", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarrm Low", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarm High", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarm High High", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Range Min", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Range Max", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Units", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Source System", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"}
            },
            new String [] {
                "Setting", "Key column", "Key", "Value column", "Possible flag column"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex > ALARM_HIGH_HIGH_ROW_INDEX && columnIndex == POSSIBLE_FLAG_FIELD_COLUMN_INDEX) return false;
                return canEdit [columnIndex];
            }
        });
        parsingSettingsTable.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(parsingSettingsTable);

        pathToSpreadsheetTextFieldLabel.setText("Path to source spreadsheet:");

        pathToSpreadsheetTextField.setEditable(false);

        selectPathToSpreadsheetTextFieldButton.setText("...");
        selectPathToSpreadsheetTextFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPathToSpreadsheetTextFieldButtonActionPerformed(evt);
            }
        });

        sourceSheetNameComboBoxLabel.setText("Source sheet name:");

        sourceSheetNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceSheetNameComboBoxActionPerformed(evt);
            }
        });

        tagNameFieldNameComboBoxLabel.setText("Tag name column:");

        runParsingButton.setText("Run");
        runParsingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runParsingButtonActionPerformed(evt);
            }
        });

        dataSourceToBeCreatedTypeComboBoxLabel.setText("Data source type:");

        plantsComboBoxLabel.setText("plant:");

        tagFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatComboBoxActionPerformed(evt);
            }
        });

        tagFormatComboBoxLabel.setText("Tag format:");

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runParsingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pathToSpreadsheetTextFieldLabel)
                            .addComponent(sourceSheetNameComboBoxLabel)
                            .addComponent(tagNameFieldNameComboBoxLabel)
                            .addComponent(dataSourceToBeCreatedTypeComboBoxLabel)
                            .addComponent(plantsComboBoxLabel)
                            .addComponent(tagFormatComboBoxLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pathToSpreadsheetTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectPathToSpreadsheetTextFieldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sourceSheetNameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tagNameFieldNameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataSourceToBeCreatedTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tagFormatComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(plantsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantsComboBoxLabel)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataSourceToBeCreatedTypeComboBoxLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dataSourceToBeCreatedTypeComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tagFormatComboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathToSpreadsheetTextFieldLabel)
                    .addComponent(pathToSpreadsheetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectPathToSpreadsheetTextFieldButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceSheetNameComboBoxLabel)
                    .addComponent(sourceSheetNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagNameFieldNameComboBoxLabel)
                    .addComponent(tagNameFieldNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(runParsingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод рассылает событие клика по кнопке для выбора пути к файлу книги 
     * MS Excel всем подписчикам.
     *  
     * @param   evt  Cобытие клика
     * @return  void
     */
    private void selectPathToSpreadsheetTextFieldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPathToSpreadsheetTextFieldButtonActionPerformed
        
        CustomEvent selectPathToSpreadsheetTextFieldButtonClickEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.SELECT_PATH_TO_SPREADSHEET_TEXT_FIELD_BUTTON_CLICK, selectPathToSpreadsheetTextFieldButtonClickEvent);       
    }//GEN-LAST:event_selectPathToSpreadsheetTextFieldButtonActionPerformed

    
    /**
     * Метод устанавливает наборы значений доступных полей выбранного листа
     * документа в элементах управления диалога.
     * 
     * @param evt Событие выбора в комбобокске
     */
    private void sourceSheetNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceSheetNameComboBoxActionPerformed
        
        String sheetName = (String)sourceSheetNameComboBox.getSelectedItem();
        List<String> fields = (List)model.getSheetsAndFields().get(sheetName);
        
        this.tagNameFieldNameComboBox.removeAllItems();
        
        //Добавляем в комбобокс поле, означающее что колонка листа не указана:
        JComboBox tableCellsEditorComboBox = new JComboBox();
        tableCellsEditorComboBox.addItem(NO_FIELD_SELECTED_VALUE);
        
        for (String field : fields)
        {    
            this.tagNameFieldNameComboBox.addItem(field);
            tableCellsEditorComboBox.addItem(field);
        }//for
        
        //Устанавливаем редактор значений ячеек для необходимых колонок таблицы настроек парсинга:
        TableColumn keyFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(KEY_FIELD_COLUMN_INDEX);
        TableColumn valueFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(VALUE_FIELD_COLUMN_INDEX);
        TableColumn possibleFlagFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        keyFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
        valueFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
        possibleFlagFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
    }//GEN-LAST:event_sourceSheetNameComboBoxActionPerformed

    
    /**
     * Метод рассылает событие нажатия кнопки запуска парсинга книги MS Excel
     * всем подписчикам и закрывает текущий диалог.
     *  
     * @param   evt  Cобытие клика
     * @return  void
     */
    private void runParsingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runParsingButtonActionPerformed
        
        CustomEvent runParsingButtonClickEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.RUN_SPREADSHEET_PARSING_BUTTON_CLICK, runParsingButtonClickEvent);
        
        this.setVisible(false);
    }//GEN-LAST:event_runParsingButtonActionPerformed

    
    /**
     * Метод рассылает событие закрытия диалога всем подписчикам.
     *  
     * @param   evt  Cобытие клика
     * @return  void
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        CustomEvent dialogClosingEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.DIALOG_CLOSING, dialogClosingEvent);
    }//GEN-LAST:event_formWindowClosing

    
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

    
    /**
     * Метод обрабатывает событие выбора нового формата тага в выпадающем списке
     * и рассылает событие с контекстом выбранного формата тага всем 
     * подписчикам.
     * 
     * @param evt Cобытие выбора формата тага
     * @return void 
     */
    private void tagFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatComboBoxActionPerformed
        
        TagMask tagMask = (TagMask)this.tagFormatComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagMask);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatComboBoxActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dataSourceToBeCreatedTypeComboBox;
    private javax.swing.JLabel dataSourceToBeCreatedTypeComboBoxLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable parsingSettingsTable;
    private javax.swing.JTextField pathToSpreadsheetTextField;
    private javax.swing.JLabel pathToSpreadsheetTextFieldLabel;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JLabel plantsComboBoxLabel;
    private javax.swing.JButton runParsingButton;
    private javax.swing.JButton selectPathToSpreadsheetTextFieldButton;
    private javax.swing.JComboBox sourceSheetNameComboBox;
    private javax.swing.JLabel sourceSheetNameComboBoxLabel;
    private javax.swing.JComboBox tagFormatComboBox;
    private javax.swing.JLabel tagFormatComboBoxLabel;
    private javax.swing.JComboBox tagNameFieldNameComboBox;
    private javax.swing.JLabel tagNameFieldNameComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}//CreateDataSourceFromExcelDialog
