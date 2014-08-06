package ru.sakhalinenergy.alarmtripsettings.models.logic.settings;

import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 *
 * @author Denis.Udovenko
 * @version 1.0.4
 */
public class СoincidenceSettingsSelectior extends SettingsSelector
{
    
    //Массивы уставок, выбранных из всех уставок тагов прибора с учетом приоритетов:
    private Set<TagSetting> intoolsSettings = new HashSet(), documentsSettings = new HashSet(), 
        dcsSettings = new HashSet(), esdSettings = new HashSet(), fgsSettings = new HashSet();
    
    
    /**
     * Public constructor. 
     * 
     * @param loop Loop instance to be wrapped by current logic
     */
    public СoincidenceSettingsSelectior(Loop loop)
    {
        super(loop);
        
        //Собираем все настройки контура из его тагов в группы по типу источника:
        for (Tag tempTag : loop.getTags())
        {
            if (tempTag.getSource().getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) intoolsSettings.addAll(tempTag.getSettings());
            if (tempTag.getSource().getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) documentsSettings.addAll(tempTag.getSettings());
            if (tempTag.getSource().getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) dcsSettings.addAll(tempTag.getSettings());
            if (tempTag.getSource().getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) esdSettings.addAll(tempTag.getSettings());
            if (tempTag.getSource().getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) fgsSettings.addAll(tempTag.getSettings());
        }//for
        
        //Filtering setpoints according to priority:
        _filterSetpointsAccordingToPriority(intoolsSettings);
        _filterSetpointsAccordingToPriority(documentsSettings);
        _filterSetpointsAccordingToPriority(dcsSettings);
        _filterSetpointsAccordingToPriority(esdSettings);
        _filterSetpointsAccordingToPriority(fgsSettings);
        
        // Run settings selection methods for each source type:
        _chooseIntoolsSettings();
        _chooseDocumentsSettings();
        
        // Chose settings from systems. Methods call according to settings priority 
        // (if ESD setting is chosen, FGS setting of same type will be skipped). 
        // DCS settings will be chosen last:
        _choosePlcSettings(esdSettings);
        _choosePlcSettings(fgsSettings);
        _chooseDcsSettings();
        
        //Calculating conformity of chosen settings:
        _calculateAlarmsLLConformity();
        _calculateAlarmsLConformity();
        _calculateAlarmsHConformity();
        _calculateAlarmsHHConformity();
    }//СoincidenceSettingsSelectior

    
    /**
     * Filters given collection by leaving only distinct types of settings with
     * highest priority.
     * 
     * @param settings Collection of settings from data sources with the same type
     */
    private void _filterSetpointsAccordingToPriority(Set<TagSetting> settings)
    {
        //Находим настройки, уступающие по приоритету настройкам такого же типа:
        List<TagSetting> settingsToRemove = new ArrayList();
        for (TagSetting settingStrong : settings)
        {
            for (TagSetting settingWeak : settings)
            {
                if (settingStrong.getTypeId() == settingWeak.getTypeId() 
                    && settingStrong.getTag().getSource().getPriority() > settingWeak.getTag().getSource().getPriority())
                {
                    settingsToRemove.add(settingWeak);
                    
                    //Если более актуальная настройка имеет зарезервированное значение "удалена", удалаяем ее вместе с низкоприоритетной:
                    if (settingStrong.getValue().equals(DELETED_SETTING_RESERVED_WORD)) settingsToRemove.add(settingStrong);
                }//if
            }//for
        }//for
        
        //Удаляем настройки, уступающие по приоритету:
        settings.removeAll(settingsToRemove);
    }// _filterIntoolsSetpointsAccordingToPriority
    
    
    /**
     * Chooses first found instances of each type of settings from filtered 
     * collection of SPI settings.
     */
    private void _chooseIntoolsSettings()
    {
        int settingType;
        
        for (TagSetting setting : intoolsSettings)
        {
            settingType = setting.getTypeId();
            
            if (settingType == SettingsTypes.ALARM_LL_SETTING.ID) chosenIntoolsAlarmLL = setting;
            if (settingType == SettingsTypes.ALARM_L_SETTING.ID) chosenIntoolsAlarmL = setting;
            if (settingType == SettingsTypes.ALARM_H_SETTING.ID) chosenIntoolsAlarmH = setting;
            if (settingType == SettingsTypes.ALARM_HH_SETTING.ID) chosenIntoolsAlarmHH = setting;
        }// for
    }// _chooseIntoolsSettings
    
    
    /**
     * Chooses first found instances of each type of settings from filtered 
     * collection of settings received from documents.
     */
    private void _chooseDocumentsSettings()
    {
        int settingType;
        
        for (TagSetting setting : documentsSettings)
        {
            settingType = setting.getTypeId();
            
            if (settingType == SettingsTypes.ALARM_LL_SETTING.ID) chosenDocumentsAlarmLL = setting;
            if (settingType == SettingsTypes.ALARM_L_SETTING.ID) chosenDocumentsAlarmL = setting;
            if (settingType == SettingsTypes.ALARM_H_SETTING.ID) chosenDocumentsAlarmH = setting;
            if (settingType == SettingsTypes.ALARM_HH_SETTING.ID) chosenDocumentsAlarmHH = setting;
        }// for
    }// _chooseIntoolsSettings
      
    
    /**
     * Chooses first found instances of each type of settings from filtered 
     * collection of settings received from PLC (ESD or FGS) system.
     * 
     * @param systemSettings PLC system settings collection
     */
    private void _choosePlcSettings(Set<TagSetting> systemSettings)
    {
        int settingType;
        
        for (TagSetting setting : systemSettings)
        {
            settingType = setting.getTypeId();
            
            if (settingType == SettingsTypes.ALARM_LL_SETTING.ID 
                && chosenSystemsAlarmLL == null) chosenSystemsAlarmLL = setting;
            if (settingType == SettingsTypes.ALARM_L_SETTING.ID 
                && chosenSystemsAlarmL == null) chosenSystemsAlarmL = setting;
            if (settingType == SettingsTypes.ALARM_H_SETTING.ID 
                && chosenSystemsAlarmH == null) chosenSystemsAlarmH = setting;
            if (settingType == SettingsTypes.ALARM_HH_SETTING.ID 
                && chosenSystemsAlarmHH == null) chosenSystemsAlarmHH = setting;
        }//for
    }// _chooseEsdSettings
    
        
    /**
     * Chooses systems setting from filtered DCS setting set, if corresponding 
     * system settings not chosen yet.
     */
    private void _chooseDcsSettings()
    {
        int settingType;
        
        for (TagSetting setting : dcsSettings)
        {
            settingType = setting.getTypeId();
            
            if (settingType == SettingsTypes.ALARM_LL_SETTING.ID && chosenSystemsAlarmLL == null) 
                chosenSystemsAlarmLL = _permitDcsSetting(setting, chosenIntoolsAlarmLL, chosenDocumentsAlarmLL);
            if (settingType == SettingsTypes.ALARM_L_SETTING.ID && chosenSystemsAlarmL == null) 
                chosenSystemsAlarmL = _permitDcsSetting(setting, chosenIntoolsAlarmL, chosenDocumentsAlarmL);
            if (settingType == SettingsTypes.ALARM_H_SETTING.ID && chosenSystemsAlarmH == null)
                chosenSystemsAlarmH = _permitDcsSetting(setting, chosenIntoolsAlarmH, chosenDocumentsAlarmH);
            if (settingType == SettingsTypes.ALARM_HH_SETTING.ID && chosenSystemsAlarmHH == null) 
                chosenSystemsAlarmHH = _permitDcsSetting(setting, chosenIntoolsAlarmHH, chosenDocumentsAlarmHH);
        }// for
    }// _chooseDcsSettings
    
    
    /**
     * Checks given setting is able to be chosen as system setting of according 
     * type. If setting can be chosen, returns it, else return null.
     * 
     * @param setting Setting to be checked
     * @param chosenIntoolsSetting Current chosen Intools setting of according type
     * @param chosenDocumentSetting Current chosen Documents setting of according type
     * @return Checked setting or null
     */
    private TagSetting _permitDcsSetting(TagSetting setting, TagSetting chosenIntoolsSetting, TagSetting chosenDocumentSetting)
    {
        // Get given seting values for comparsion:
        String chosenIntoolsAlarmValue = (chosenIntoolsSetting == null) ? null : chosenIntoolsSetting.getValue();
        String chosenDocumentsAlarmValue = (chosenDocumentSetting == null) ? null : chosenDocumentSetting.getValue();
        
        // If only Documents setting chosen:
        if (chosenDocumentsAlarmValue != null)
        {
            Byte comparsionResult = _compare(chosenDocumentsAlarmValue, setting.getValue());
            
            if (comparsionResult == COMPARED_AS_DOUBLE_EQUALS 
                || comparsionResult == COMPARED_AS_STRING_EQUALS) return setting;
        }// if
        
        // If only SPI setting chosen:
        if (chosenIntoolsAlarmValue != null)
        {
            Byte comparsionResult = _compare(chosenIntoolsAlarmValue, setting.getValue());
            if (comparsionResult == COMPARED_AS_DOUBLE_EQUALS 
                || comparsionResult == COMPARED_AS_STRING_EQUALS) return setting;
        }// if
              
        // If method didn't return control before, seems that setting can't be chosen:
        return null;
    }// _chooseDcsAlarmLL
}//СoincidenceSettingsSelectior
