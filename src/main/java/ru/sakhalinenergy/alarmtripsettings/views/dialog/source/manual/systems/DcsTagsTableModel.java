package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems;

import java.util.Arrays;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.ManualSourceEditingDialogObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.TagsTableModel;


/**
 * Implements a model for DCS tags table. 
 * 
 * @author Denis Udovenko
 * @version 1.0.7
 */
public class DcsTagsTableModel extends TagsTableModel
{
    public final String ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME   = "LL possible:";
    public final String ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME       = "L possible:";
    public final String ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME      = "H possible:";
    public final String ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME = "HH possible:";
    public final String SOURCE_SYSTEM_COLUMN_NAME                 = "Source:";

    
    /**
     * Public constructor. Sets data source, parent view instance and extends
     * table fields set.
     * 
     * @param source Wrapped source instance
     * @param parentView Table parent view
     */
    public DcsTagsTableModel(TagsSourceObservable source, ManualSourceEditingDialogObservable parentView)
    {
        // Call superclass constructor:
        super(source, parentView);
        
        // Extend superclass table fieldset:
        fields.addAll(Arrays.asList(SOURCE_SYSTEM_COLUMN_NAME, ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME, 
            ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME, ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME, ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME));
    }// DcsTagsTableModel
    
        
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
  
        // If current cell - "Possible" flag for existing Alarm LL:
        if (fields.get(column).equals(ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID) != null) return true;
        }// if
      
        // If current cell - "Possible" flag for existing Alarm L:
        if (fields.get(column).equals(ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID) != null) return true;
        }// if
      
        // If current cell - "Possible" flag for existing Alarm H:
        if (fields.get(column).equals(ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID) != null) return true;
        }// if
   
        // If current cell - "Possible" flag for existing Alarm HH:
        if (fields.get(column).equals(ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            if (currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID) != null) return true;
        }//if
                
        // If current cell - source system for existing tag:
        if (fields.get(column).equals(SOURCE_SYSTEM_COLUMN_NAME) && currentTag != null) return true;
            
        // Call superclass method for rest of fields:
        return super.isCellEditable(row, column);
    }// isCellEditable

    
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

        // If current cell is "Possible" flag for LL alarm:
        if (fields.get(col).equals(ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmLowLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            if (alarmLowLowSetting != null)
            {
                TagSettingProperty possibleFlag = alarmLowLowSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }// if
        }// if    

        // If current cell is "Possible" flag for L alarm:
        if (fields.get(col).equals(ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmLowSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            if (alarmLowSetting != null)
            {
                TagSettingProperty possibleFlag = alarmLowSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }// if
        }// if 

        // If current cell is "Possible" flag for H alarm:
        if (fields.get(col).equals(ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            if (alarmHighSetting != null)
            {
                TagSettingProperty possibleFlag = alarmHighSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }// if
        }// if 

        // If current cell is "Possible" flag for HH alarm:
        if (fields.get(col).equals(ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME))
        {
            TagSetting alarmHighHighSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            if (alarmHighHighSetting != null)
            {
                TagSettingProperty possibleFlag = alarmHighHighSetting.getPropertyByTypeId(SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID);
                if (possibleFlag != null) return possibleFlag.getValue();
            }// if
        }// if 

        // If current cell is source system:
        if (fields.get(col).equals(SOURCE_SYSTEM_COLUMN_NAME))
        {
            TagSetting sourceSystemSetting = currentTag.getSettingByTypeId(SettingsTypes.SOURCE_SYSTEM_SETTING.ID);
            if (sourceSystemSetting != null) return sourceSystemSetting.getValue();
        }// if
            
        // Call superclass method for rest of fields:
        return super.getValueAt(row, col);
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
        TagSetting newSetting = null, currentSetting = null;
        TagSettingProperty newSettingProperty = null, currentSettingProperty = null;
               
        // If current cell is "Possible" flag for LL alarm setting:
        if (fields.get(col).equals(ALARM_LOW_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_LL_SETTING.ID);
            newSettingProperty = _permitSettingSettingProperty(currentSetting, SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
        }// if
        
        // If current cell is "Possible" flag for L alarm setting:
        if (fields.get(col).equals(ALARM_LOW_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_L_SETTING.ID);
            newSettingProperty = _permitSettingSettingProperty(currentSetting, SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
        }// if
                
        // If current cell is "Possible" flag for H alarm setting:
        if (fields.get(col).equals(ALARM_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_H_SETTING.ID);
            newSettingProperty = _permitSettingSettingProperty(currentSetting, SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
        }// if
        
        // If current cell is "Possible" flag for HH alarm setting:
        if (fields.get(col).equals(ALARM_HIGH_HIGH_POSSIBLE_FLAG_COLUMN_NAME) && currentTag != null)
        {   
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.ALARM_HH_SETTING.ID);
            newSettingProperty = _permitSettingSettingProperty(currentSetting, SettingsPropertiesTypes.POSSIBLE_SETTING_PROPERTY.ID, (String)value);
        }// if
        
        // If current cell is source system setting:
        if (fields.get(col).equals(SOURCE_SYSTEM_COLUMN_NAME) && currentTag != null)
        {
            // If tag hasn't such type of setting yet - create it:
            currentSetting = currentTag.getSettingByTypeId(SettingsTypes.SOURCE_SYSTEM_SETTING.ID);
            if (currentSetting == null) newSetting = _createTagSetting(SettingsTypes.SOURCE_SYSTEM_SETTING.ID, (String)value);
        }// if
        
        // If new setting was created, trigger add tag setting event:
        if (newSetting != null) 
        { 
            Object[] tagAndSetting = new Object[]{currentTag, newSetting};
            CustomEvent inputNewTagEvent = new CustomEvent(tagAndSetting);
            parentView.trigger(ViewEvent.TAG_SETTING_ADD, inputNewTagEvent);
        
        // If an existing setting was modified:  
        } else if (currentSetting != null && newSettingProperty == null && currentSettingProperty == null) { 
            
            String newValue = (String)value;
            
            // If setting receives new value:
            if (!newValue.trim().equals(""))
            {  
                Object[] settingAndValue = new Object[]{currentSetting, value};
                CustomEvent updateTagSettingEvent = new CustomEvent(settingAndValue);
                parentView.trigger(ViewEvent.TAG_SETTING_UPDATE, updateTagSettingEvent);
            
            } else { // If setting being deleted by clearing its empty value:
                
                CustomEvent deleteTagSettingEvent = new CustomEvent(currentSetting);
                parentView.trigger(ViewEvent.TAG_SETTING_DELETE, deleteTagSettingEvent);
            }// else
            
        } else if (newSettingProperty != null) { // If new tag setting property was crerated:
            
            Object[] settingAndSettingPropertyPair = new Object[]{currentSetting, newSettingProperty};
            CustomEvent inputTagSettingPropertyEvent = new CustomEvent(settingAndSettingPropertyPair);
            parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_INPUT, inputTagSettingPropertyEvent);
        
        } else if (currentSettingProperty != null) { // If an existing setting property was modified:
            
            String newValue = (String)value;
            
            // If setting property receives new value:
            if (!newValue.trim().equals(""))
            { 
                Object[] settingPropertyAndValuePair = new Object[]{currentSettingProperty, value};
                CustomEvent updateTagSettingPropertyEvent = new CustomEvent(settingPropertyAndValuePair);
                parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_UPDATE, updateTagSettingPropertyEvent);
                
            } else { //If setting property being deleted by setting its empty value:
            
                CustomEvent deleteTagSettingPropertyEvent = new CustomEvent(currentSettingProperty);
                parentView.trigger(ViewEvent.TAG_SETTING_PROPERTY_DELETE, deleteTagSettingPropertyEvent);
            }// else    
        
        } else { // If hothing was happend with additional columns, call suprclass method to handle basic columns changes:
        
            super.setValueAt(value, row, col);
        }// else
    }// setValueAt
    
    
    /**
     * Returns new tag setting property if setting exists and does not have such
     * property yet.
     * 
     * @param setting Tag setting which will be checked for property adding ability
     * @param propertyTypeId Property type identifier
     * @param value Property value
     * @return Permitted tag setting property instance or null
     */
    protected static TagSettingProperty _permitSettingSettingProperty(TagSetting setting,
        int propertyTypeId, String value)
    {
        // If setting exists:
        if (setting != null)
        {
            // If setting does not have given property yet:
            TagSettingProperty currentSettingProperty = setting.getPropertyByTypeId(propertyTypeId);
            if (currentSettingProperty == null)
            {    
                TagSettingProperty property = new TagSettingProperty();
                property.setTypeId(propertyTypeId);
                property.setValue((String)value);

                return property;
            }// if
        }// if
        
        return null;
    }// _addPropertyToSetting
}// DcsTagsTableModel
