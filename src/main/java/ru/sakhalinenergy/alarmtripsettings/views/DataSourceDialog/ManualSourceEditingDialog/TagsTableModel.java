package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;


/**
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public class TagsTableModel extends AbstractTableModel
{
    protected final TagsSourceObservable source;
    protected final ManualSourceEditingDialogObservable parentView;
    
    public final String REMOVE_TAG_BUTTON_COLUMN_NAME = " ";
    public final String TAG_NAME_COLUMN_NAME = "Tag:";
    
    public final String ALARM_LOW_LOW_COLUMN_NAME = "LL value:";
    public final String ALARM_LOW_COLUMN_NAME = "L value:";
    public final String ALARM_HIGH_COLUMN_NAME = "H value:";
    public final String ALARM_HIGH_HIGH_COLUMN_NAME = "HH value:";
    public final String RANGE_MIN_COLUMN_NAME = "Range Min:";
    public final String RANGE_MAX_COLUMN_NAME = "Range Max:";
    public final String UNITS_COLUMN_NAME = "Units:";
       
    protected List<String> fields = new ArrayList();
    
    
    /**
     * Public constructor.
     * 
     * @param source Wrapped source instance
     * @param parentView Table parent view
     */
    public TagsTableModel(TagsSourceObservable source,  ManualSourceEditingDialogObservable parentView)
    {
        this.source = source;
        this.parentView = parentView;
        
        fields.addAll(Arrays.asList(REMOVE_TAG_BUTTON_COLUMN_NAME, TAG_NAME_COLUMN_NAME, 
            ALARM_LOW_LOW_COLUMN_NAME, ALARM_LOW_COLUMN_NAME, ALARM_HIGH_COLUMN_NAME, 
            ALARM_HIGH_HIGH_COLUMN_NAME, RANGE_MIN_COLUMN_NAME, RANGE_MAX_COLUMN_NAME,
            UNITS_COLUMN_NAME));
    }//DocumentTagsTableModel
    
    
    /**
     * Метод указывает, что текущая модель таблицы является доступной для
     * редактирования.
     * 
     * @param   row     Индекс строки ячейки
     * @param   column  Индекс столбца ячейки
     * @return  Boolean
     */
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        
        //Если поле - кнопка удаления тага:
        if (this.fields.get(column).equals(this.REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null) return true;
        
        //Если данные вводятся в поле Alarm LL уже созданного тага:
        if (this.fields.get(column).equals(this.ALARM_LOW_LOW_COLUMN_NAME) && currentTag != null) return true;
               
        //Если данные вводятся в поле Alarm L уже созданного тага:
        if (this.fields.get(column).equals(this.ALARM_LOW_COLUMN_NAME) && currentTag != null) return true;
                
        //Если данные вводятся в поле Alarm H уже созданного тага:
        if (this.fields.get(column).equals(this.ALARM_HIGH_COLUMN_NAME) && currentTag != null) return true;
        
        //Если данные вводятся в поле Alarm HH уже созданного тага впервые:
        if (this.fields.get(column).equals(this.ALARM_HIGH_HIGH_COLUMN_NAME) && currentTag != null) return true;
                       
        //Если ячейка отображает значение нижней границы диапазона тага:
        if (this.fields.get(column).equals(this.RANGE_MIN_COLUMN_NAME) && currentTag != null) return true;
            
        //Если ячейка отображает значение верхней границы диапазона тага:
        if (this.fields.get(column).equals(this.RANGE_MAX_COLUMN_NAME) && currentTag != null) return true;
        
        //Если ячейка отображает единицы измерения тага:
        if (this.fields.get(column).equals(this.UNITS_COLUMN_NAME) && currentTag != null) return true;
        
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
     * @return  int
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
     * @return Значение заданной ячейки
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
                    
        //Если ячейка отображает значение нижнего аларма тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_COLUMN_NAME))
        {
            TagSetting alarmLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (alarmLowSetting != null) return alarmLowSetting.getValue();
        }//if
                        
        //Если ячейка отображает значение верхнего аларма тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (alarmHighSetting != null) return alarmHighSetting.getValue();
        }//if
                        
        //Если ячейка отображает значение верхнего трипа тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (alarmHighHighSetting != null) return alarmHighHighSetting.getValue();
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
        TagSetting setting = null, currentSetting = null;
                
        //Если нажата кнопка удаления тага:
        if (this.fields.get(col).equals(this.REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null)
        {
            CustomEvent removeTagEvent = new CustomEvent(currentTag);
            parentView.trigger(ViewEvent.REMOVE_TAG, removeTagEvent);
        }//if
                
        //Если данные вводятся в поле Alarm LL уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_LOW_COLUMN_NAME))
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_LL_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле Alarm L уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_LOW_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_L_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле Alarm H уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_H_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле Alarm HH уже созданного тага:
        if (this.fields.get(col).equals(this.ALARM_HIGH_HIGH_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_HH_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле для указания нижней границы диапазона тага:
        if (this.fields.get(col).equals(this.RANGE_MIN_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.RANGE_MIN_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле для указания верхней границы диапазона тага:
        if (this.fields.get(col).equals(this.RANGE_MAX_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.RANGE_MAX_SETTING.ID, (String)value);
        }//if
        
        //Если данные вводятся в поле для указания единиц измерения тага:
        if (this.fields.get(col).equals(this.UNITS_COLUMN_NAME) && currentTag != null)
        {
            //If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.UNITS_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.UNITS_SETTING.ID, (String)value);
        }//if
        
        //Если была создана новая настройка, оповещаем об этом подписчиков родительского вью:
        if (setting != null)
        {
            Object[] tagAndSetting = new Object[]{currentTag, setting};
            CustomEvent inputNewTagEvent = new CustomEvent(tagAndSetting);
            parentView.trigger(ViewEvent.TAG_SETTING_ADD, inputNewTagEvent);
        
        } else if (currentSetting != null) { //If an existing setting was modified:
            
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
        }//if
    }//setValueAt
    
    
    /**
     * Creates tag setting instance with given type and value.
     *
     * @param settingType Type of setting to be created
     * @param value Value of setting to be created
     */
    protected static TagSetting _createTagSetting(int settingType, String value)
    {
        TagSetting setting = new TagSetting();
        setting.setTypeId(settingType);
        setting.setValue((String)value);
        
        return setting;
    }//_createTagSetting
}//DevicesTableModel
