package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;


/**
 * Implements a model for tags table. 
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class TagsTableModel extends AbstractTableModel
{
    protected final TagsSourceObservable source;
    protected final ManualSourceEditingDialogObservable parentView;
    
    public final String REMOVE_TAG_BUTTON_COLUMN_NAME = " ";
    public final String TAG_NAME_COLUMN_NAME          = "Tag:";
    
    public final String ALARM_LOW_LOW_COLUMN_NAME   = "LL value:";
    public final String ALARM_LOW_COLUMN_NAME       = "L value:";
    public final String ALARM_HIGH_COLUMN_NAME      = "H value:";
    public final String ALARM_HIGH_HIGH_COLUMN_NAME = "HH value:";
    public final String RANGE_MIN_COLUMN_NAME       = "Range Min:";
    public final String RANGE_MAX_COLUMN_NAME       = "Range Max:";
    public final String UNITS_COLUMN_NAME           = "Units:";
       
    protected List<String> fields = new ArrayList();
    
    
    /**
     * Public constructor. Sets source model, parent view and table fields set.
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
    }// TagsTableModel
    
    
    /**
     * Specifies that cell with given row and column indexes is editable.
     * 
     * @param row Cell row index
     * @param column Cell column index
     * @return True if cell is editable
     */
    @Override
    public boolean isCellEditable(int row, int column) 
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        
        // If current column - remove tag button:
        if (fields.get(column).equals(REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null) return true;
        
        // If current cell - Alarm LL for existing tag:
        if (fields.get(column).equals(ALARM_LOW_LOW_COLUMN_NAME) && currentTag != null) return true;
               
        // If current cell - Alarm L for existing tag:
        if (fields.get(column).equals(ALARM_LOW_COLUMN_NAME) && currentTag != null) return true;
                
        // If current cell - Alarm H for existing tag:
        if (fields.get(column).equals(ALARM_HIGH_COLUMN_NAME) && currentTag != null) return true;
        
        // If current cell - Alarm HH for existing tag:
        if (fields.get(column).equals(ALARM_HIGH_HIGH_COLUMN_NAME) && currentTag != null) return true;
                       
        // If current cell - range min for existing tag:
        if (fields.get(column).equals(RANGE_MIN_COLUMN_NAME) && currentTag != null) return true;
            
        // If current cell - range max for existing tag:
        if (fields.get(column).equals(RANGE_MAX_COLUMN_NAME) && currentTag != null) return true;
        
        // If current cell - engineering units for existing tag:
        if (fields.get(column).equals(UNITS_COLUMN_NAME) && currentTag != null) return true;
        
        return false;
    }// isCellEditable
    
    
    /**
     * Returns column count of table.
     * 
     * @return Column count of table
     */
    @Override
    public int getColumnCount()
    {
        return fields.size();
    }// getColumnCount
    
    
    /**
     * Returns row count of table.
     * 
     * @return Row count of table
     */
    @Override
    public int getRowCount() 
    { 
        return source.getEntity().getTags().size();
    }// getRowCount
    
    
    /**
     * Returns column name by given index.
     * 
     * @param col Column index
     * @return Column name
     */
    @Override
    public String getColumnName(int col) 
    {
        return fields.get(col);
    }// getColumnName(int col) 
     
    
    /**
     * Returns value of cell with given row and column indexes.
     * 
     * @param row Row index
     * @param col Column index
     * @return Cell value
     */
    @Override
    public Object getValueAt(int row, int col) 
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        
        // If current cell is tag delete button:
        if (fields.get(col).equals(REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null) return "remove";
            
        // If current cell is tag name:
        if (fields.get(col).equals(TAG_NAME_COLUMN_NAME)) return currentTag.getName();
                
        // If current cell is LL alarm:
        if (fields.get(col).equals(ALARM_LOW_LOW_COLUMN_NAME))
        {
            TagSetting alarmLowLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (alarmLowLowSetting != null) return alarmLowLowSetting.getValue();
        }// if
                    
        // If current cell is L alarm:
        if (fields.get(col).equals(ALARM_LOW_COLUMN_NAME))
        {
            TagSetting alarmLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (alarmLowSetting != null) return alarmLowSetting.getValue();
        }// if
                        
        // If current cell is H alarm:
        if (fields.get(col).equals(ALARM_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (alarmHighSetting != null) return alarmHighSetting.getValue();
        }// if
                        
        // If current cell is HH alarm:
        if (fields.get(col).equals(ALARM_HIGH_HIGH_COLUMN_NAME))
        {
            TagSetting alarmHighHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (alarmHighHighSetting != null) return alarmHighHighSetting.getValue();
        }// if
                        
        // If current cell is range min:
        if (fields.get(col).equals(RANGE_MIN_COLUMN_NAME))
        {
            TagSetting rangeMinSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
            if (rangeMinSetting != null) return rangeMinSetting.getValue();
        }// if
            
        // If current cell is range max:
        if (fields.get(col).equals(RANGE_MAX_COLUMN_NAME))
        {
            TagSetting rangeMaxSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
            if (rangeMaxSetting != null) return rangeMaxSetting.getValue();
        }// if
            
        // If current cell is engineering units:
        if (fields.get(col).equals(UNITS_COLUMN_NAME))
        {
            TagSetting unitsSetting = currentTag.getSettingByTypeId(SettingsTypes.UNITS_SETTING.ID);
            if (unitsSetting != null) return unitsSetting.getValue();
        }// if
                        
        return "";
    }// getValueAt
    
    
    /**
     * Sets value for cell with given row and column indexes.
     * 
     * @param value Value to be set
     * @param row Row index
     * @param col Column index
     */
    @Override
    public void setValueAt(Object value, int row, int col)
    {
        Tag currentTag = (Tag)source.getSortedTags().get(row);
        TagSetting setting = null, currentSetting = null;
                
        // If current cell is remove tag button:
        if (fields.get(col).equals(REMOVE_TAG_BUTTON_COLUMN_NAME) && currentTag != null)
        {
            CustomEvent removeTagEvent = new CustomEvent(currentTag);
            parentView.trigger(ViewEvent.REMOVE_TAG, removeTagEvent);
        }// if
                
        // If current cell is LL alarm setting:
        if (fields.get(col).equals(ALARM_LOW_LOW_COLUMN_NAME))
        {
            // If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_LL_SETTING.ID, (String)value);
        }// if
        
        // If current cell is L alarm setting:
        if (fields.get(col).equals(ALARM_LOW_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_L_SETTING.ID, (String)value);
        }// if
        
        // If current cell is H alarm setting:
        if (fields.get(col).equals(ALARM_HIGH_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_H_SETTING.ID, (String)value);
        }// if
        
        // If current cell is HH alarm setting:
        if (fields.get(col).equals(ALARM_HIGH_HIGH_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.ALARM_HH_SETTING.ID, (String)value);
        }// if
        
        // If current cell is range min setting:
        if (fields.get(col).equals(RANGE_MIN_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MIN_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.RANGE_MIN_SETTING.ID, (String)value);
        }// if
        
        // If current cell is range max setting:
        if (fields.get(col).equals(RANGE_MAX_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.RANGE_MAX_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.RANGE_MAX_SETTING.ID, (String)value);
        }// if
        
        // If current cell is engineering units setting:
        if (this.fields.get(col).equals(this.UNITS_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.UNITS_SETTING.ID);
            if (currentSetting == null) setting = _createTagSetting(SettingsTypes.UNITS_SETTING.ID, (String)value);
        }// if
        
        // If new setting was created, trigger add setting event:
        if (setting != null)
        {
            Object[] tagAndSetting = new Object[]{currentTag, setting};
            CustomEvent inputNewTagEvent = new CustomEvent(tagAndSetting);
            parentView.trigger(ViewEvent.TAG_SETTING_ADD, inputNewTagEvent);
        
        } else if (currentSetting != null) { // If an existing setting was modified:
            
            String newValue = (String)value;
            
            // If setting receives new value:
            if (!newValue.trim().equals(""))
            {  
                Object[] settingAndValue = new Object[]{currentSetting, value};
                CustomEvent updateTagSettingEvent = new CustomEvent(settingAndValue);
                parentView.trigger(ViewEvent.TAG_SETTING_UPDATE, updateTagSettingEvent);
            
            } else { // If setting being deleted by setting its empty value:
            
                CustomEvent deleteTagSettingEvent = new CustomEvent(currentSetting);
                parentView.trigger(ViewEvent.TAG_SETTING_DELETE, deleteTagSettingEvent);
            }// else
        }// if
    }// setValueAt
    
    
    /**
     * Creates tag setting instance with given type and value.
     *
     * @param settingType Type of setting to be created
     * @param value Value of setting to be created
     * @return Tag setting
     */
    protected static TagSetting _createTagSetting(int settingType, String value)
    {
        TagSetting setting = new TagSetting();
        setting.setTypeId(settingType);
        setting.setValue((String)value);
        
        return setting;
    }// _createTagSetting
}// TagsTableModel
