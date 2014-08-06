package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import java.util.Arrays;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;


/**
 * @author   Denis.Udovenko
 * @version  1.0.4
 */
public class DcsTagsTableModel extends TagsTableModel
{
    
    public final String ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME = "LL possible:";
    public final String ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME = "L possible:";
    public final String ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME = "H possible:";
    public final String ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME = "HH possible:";
    public final String SOURCE_SYSTEM_COLUMN_NAME = "Source:";

    
    /**
     * Public constructor.
     * 
     * @param source Wrapped source instance
     * @param parentView Table parent view
     */
    public DcsTagsTableModel(TagsSourceObservable source, ManualSourceEditingDialogObservable parentView)
    {
        //Calling superclass constructor:
        super(source, parentView);
        
        //Extending superclass table fieldset:
        fields.addAll(Arrays.asList(SOURCE_SYSTEM_COLUMN_NAME, ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME, 
            ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME, ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME, ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME));
    }//DcsTagsTableModel
    
        
    /**
     * Метод указывает, что текущая модель таблицы является доступной для
     * редактирования.
     * 
     * @param row Индекс строки ячейки
     * @param column Индекс столбца ячейки
     * @return Is editable flag for cell
     */
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        
        //Если поле - кнопка удаления тага:
        if (this.fields.get(column).equals(this.REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null) return true;
        
        //Если данные вводятся в поле имени тага для пустой строки:    
        if (this.fields.get(column).equals(this.TAG_NAME_COLUMN_NAME) && currentTag == null) return true;
        
        //Если данные вводятся в поле Alarm LL уже созданного тага впервые:
        if (this.fields.get(column).equals(this.ALARM_LOW_LOW_COLUMN_NAME) && currentTag != null) return true;
                
        //Если вводится флаг Possible нижнего аларма в поле созданного тага, для которого уже создана уставка:
        if (this.fields.get(column).equals(this.ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID) != null) return true;
        }//if
        
        //Если данные вводятся в поле Alarm L уже созданного тага впервые:
        if (this.fields.get(column).equals(this.ALARM_LOW_COLUMN_NAME) && currentTag != null) return true;
                
        //Если вводится флаг Possible нижнего аларма в поле созданного тага, для которого уже создана уставка:
        if (this.fields.get(column).equals(this.ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID) != null) return true;
        }//if
        
        //Если данные вводятся в поле Alarm H уже созданного тага впервые:
        if (this.fields.get(column).equals(this.ALARM_HIGH_COLUMN_NAME) && currentTag != null) return true;
                
        //Если вводится флаг Possible нижнего аларма в поле созданного тага, для которого уже создана уставка:
        if (this.fields.get(column).equals(this.ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID) != null) return true;
        }//if
        
        //Если данные вводятся в поле Alarm HH уже созданного тага впервые:
        if (this.fields.get(column).equals(this.ALARM_HIGH_HIGH_COLUMN_NAME) && currentTag != null) return true;
                
        //Если вводится флаг Possible нижнего аларма в поле созданного тага, для которого уже создана уставка:
        if (this.fields.get(column).equals(this.ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID) != null) return true;
        }//if
        
        //Если ячейка отображает значение нижней границы диапазона тага:
        if (this.fields.get(column).equals(this.RANGE_MIN_COLUMN_NAME) && currentTag != null) return true;
            
        //Если ячейка отображает значение верхней границы диапазона тага:
        if (this.fields.get(column).equals(this.RANGE_MAX_COLUMN_NAME) && currentTag != null) return true;
                
        //Если ячейка отображает единицы измерения тага:
        if (this.fields.get(column).equals(this.UNITS_COLUMN_NAME) && currentTag != null) return true;
                
        //Если ячейка отображает систему - источник сигнала тага:
        if (this.fields.get(column).equals(this.SOURCE_SYSTEM_COLUMN_NAME) && currentTag != null) return true;
                
        return false;
    }//isCellEditable
    
    
    /**
     * Метод возвращает количество колонок модели.
     * 
     * @return  int
     */
    @Override
    public int getColumnCount()
    {
        return this.fields.size();
    }//getColumnCount
    
    
    /**
     * Метод возвращает количество строк модели.
     * 
     * @return Table rows count
     */
    @Override
    public int getRowCount() 
    { 
        return source.getEntity().getTags().size();
    }//getRowCount
    
    
    /**
     * Метод возвращает имя колонки по ее индексу.
     * 
     * @param   col     Индекс столбца
     * @return  String
     */
    @Override
    public String getColumnName(int col) 
    {
        return this.fields.get(col);
    }//getColumnName(int col) 
     
    
    /**
     * Метод возвращает значение заданной ячейки.
     * 
     * @param row Индекс строки
     * @param col Индекс столбца
     * @return Cell value
     */
    @Override
    public Object getValueAt(int row, int col) 
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        
        //Если поле - кнопка удаления тага:
        if (this.fields.get(col).equals(this.REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null) return "remove";
            
        //Если ячейка отображает имя тага:
        if (this.fields.get(col).equals(this.TAG_NAME_COLUMN_NAME)) return currentTag.getName();
                
        //Если ячейка отображает значение нижнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_LOW_COLUMN_NAME))
        {
            TagSetting alarmLowLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (alarmLowLowSetting != null) return alarmLowLowSetting.getValue();
        }//if
            
        //Если ячейка отображает значение флага Possible настройки нижнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmLowLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (alarmLowLowSetting != null)
            {
                TagSettingProperty possibleFlag = alarmLowLowSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }//if
        }//if    
        
        //Если ячейка отображает значение нижнего аларма тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_COLUMN_NAME))
        {
            TagSetting alarmLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (alarmLowSetting != null) return alarmLowSetting.getValue();
        }//if
            
        //Если ячейка отображает значение флага Possible настройки нижнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (alarmLowSetting != null)
            {
                TagSettingProperty possibleFlag = alarmLowSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }//if
        }//if 
            
        //Если ячейка отображает значение верхнего аларма тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (alarmHighSetting != null) return alarmHighSetting.getValue();
        }//if
            
        //Если ячейка отображает значение флага Possible настройки нижнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (alarmHighSetting != null)
            {
                TagSettingProperty possibleFlag = alarmHighSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }//if
        }//if 
            
        //Если ячейка отображает значение верхнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (alarmHighHighSetting != null) return alarmHighHighSetting.getValue();
        }//if
            
        //Если ячейка отображает значение флага Possible настройки нижнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmHighHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (alarmHighHighSetting != null)
            {
                TagSettingProperty possibleFlag = alarmHighHighSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }//if
        }//if 
            
        //Если ячейка отображает значение нижней границы диапазона тага:
        if (this.fields.get(col).equals(this.RANGE_MIN_COLUMN_NAME))
        {
            TagSetting rangeMinSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
            if (rangeMinSetting != null) return rangeMinSetting.getValue();
        }//if
            
        //Если ячейка отображает значение верхней границы диапазона тага:
        if (this.fields.get(col).equals(this.RANGE_MAX_COLUMN_NAME))
        {
            TagSetting rangeMaxSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
            if (rangeMaxSetting != null) return rangeMaxSetting.getValue();
        }//if
            
        //Если ячейка отображает единицы измерения тага:
        if (this.fields.get(col).equals(this.UNITS_COLUMN_NAME))
        {
            TagSetting unitsSetting = currentTag.getSettingByTypeId(SettingsTypes.UNITS_SETTING.ID);
            if (unitsSetting != null) return unitsSetting.getValue();
        }//if
            
        //Если ячейка отображает единицы измерения тага:
        if (this.fields.get(col).equals(this.SOURCE_SYSTEM_COLUMN_NAME))
        {
            TagSetting sourceSystemSetting = currentTag.getSettingByTypeId(SettingsTypes.SOURCE_SYSTEM_SETTING.ID);
            if (sourceSystemSetting != null) return sourceSystemSetting.getValue();
        }//if
            
        return "";
    }//getValueAt
        
    
    /**
     * Метод вносит значение ячейки в соответвующие поля коллекции тагов.
     * 
     * @param row Индекс строки
     * @param col Индекс колонки
     */
    @Override
    public void setValueAt(Object value, int row, int col)
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        TagSetting newSetting = null, currentSetting = null;
        TagSettingProperty newSettingProperty = null, currentSettingProperty = null;
               
        //Если данные вводятся в поле флага Possible для нижнего трипа уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            
            //Если настройкa есть:
            if (currentSetting != null)
            {
                //Если свойство с флагом Possible еще не добавлено к настройке:
                currentSettingProperty = currentSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (currentSettingProperty == null) newSettingProperty = _createTagSettingProperty(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
            }//if
        }//if
        
        //Если данные вводятся в поле флага Possible для нижнего трипа уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            
            //Если настройкa есть:
            if (currentSetting != null)
            {
                //Если свойство с флагом Possible еще не добавлено к настройке:
                currentSettingProperty = currentSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (currentSettingProperty == null) newSettingProperty = _createTagSettingProperty(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
            }//if
        }//if
                
        //Если данные вводятся в поле флага Possible для нижнего трипа уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            
            //Если настройкa есть:
            if (currentSetting != null)
            {
                //Если свойство с флагом Possible еще не добавлено к настройке:
                currentSettingProperty = currentSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (currentSettingProperty == null) newSettingProperty = _createTagSettingProperty(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
            }//if
        }//if
        
        //Если данные вводятся в поле флага Possible для нижнего трипа уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {   
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            
            //Если настройкa есть:
            if (currentSetting != null)
            {
                //Если свойство с флагом Possible еще не добавлено к настройке:
                currentSettingProperty = currentSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (currentSettingProperty == null) newSettingProperty = _createTagSettingProperty(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
            }//if
        }//if
        
        //Если данные вводятся в поле для указания системы - источника сигнала тага:
        if (this.fields.get(col).equals(this.SOURCE_SYSTEM_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.SOURCE_SYSTEM_SETTING.ID);
            if (currentSetting == null) newSetting = _createTagSetting(SettingsTypes.SOURCE_SYSTEM_SETTING.ID, (String)value);
        }//if
        
        //Если была создана новая настройка, оповещаем об этом подписчиков родительского вью:
        if (newSetting != null) 
        { 
            Object[] tagAndSetting = new Object[]{currentTag, newSetting};
            CustomEvent inputNewTagEvent = new CustomEvent(tagAndSetting);
            parentView.trigger(ViewEvent.TAG_SETTING_ADD, inputNewTagEvent);
        
          //If an existing setting was modified:  
        } else if (currentSetting != null && newSettingProperty == null && currentSettingProperty == null) { 
            
            String newValue = (String)value;
            
            //If setting receives new value:
            if (!newValue.trim().equals(""))
            {  
                Object[] settingAndValue = new Object[]{currentSetting, value};
                CustomEvent updateTagSettingEvent = new CustomEvent(settingAndValue);
                parentView.trigger(ViewEvent.TAG_SETTING_UPDATE, updateTagSettingEvent);
            
            } else { //If setting being deleted by setting its empty value:
                
                CustomEvent deleteTagSettingEvent = new CustomEvent(currentSetting);
                parentView.trigger(ViewEvent.TAG_SETTING_DELETE, deleteTagSettingEvent);
            }//else
            
        } else if (newSettingProperty != null) { //If new tag setting property was crerated:
            
            Object[] settingAndSettingPropertyPair = new Object[]{currentSetting, newSettingProperty};
            CustomEvent inputTagSettingPropertyEvent = new CustomEvent(settingAndSettingPropertyPair);
            parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_INPUT, inputTagSettingPropertyEvent);
        
        } else if (currentSettingProperty != null) { //If an existing setting property was modified:
            
            String newValue = (String)value;
            
            //If setting property receives new value:
            if (!newValue.trim().equals(""))
            { 
                Object[] settingPropertyAndValuePair = new Object[]{currentSettingProperty, value};
                CustomEvent updateTagSettingPropertyEvent = new CustomEvent(settingPropertyAndValuePair);
                parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_UPDATE, updateTagSettingPropertyEvent);
                
            } else { //If setting property being deleted by setting its empty value:
            
                CustomEvent deleteTagSettingPropertyEvent = new CustomEvent(currentSettingProperty);
                parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_DELETE, deleteTagSettingPropertyEvent);
            }//else    
        
        } else { //If hothing was happend with additional columns, calling suprclass methot to handle basic columns changes:
        
            //Calling superclass method to handle adding or deleting of tag or settings adding and updating:
            super.setValueAt(value, row, col);
        }//else
    }//setValueAt
    
    
    /**
     * Creates tag setting property instance with given type and value.
     *
     * @param settingPropertyType Type of setting property to be created
     * @param value Value of setting property to be created
     */
    protected static TagSettingProperty _createTagSettingProperty(int settingPropertyType, String value)
    {
        TagSettingProperty property = new TagSettingProperty();
        property.setTypeId(settingPropertyType);
        property.setValue((String)value);
        
        return property;
    }//_createTagSettingProperty
}//DevicesTableModel
