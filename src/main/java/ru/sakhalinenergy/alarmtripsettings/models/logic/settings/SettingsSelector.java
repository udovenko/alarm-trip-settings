package ru.sakhalinenergy.alarmtripsettings.models.logic.settings;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 * Implements abstract parent for classes with tag settings selection logic.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public abstract class SettingsSelector extends Comparator
{
    protected static String DELETED_SETTING_RESERVED_WORD = "deleted";
    
    protected final Float CONFORMITY_DELTA = 100.0F / 2.0F;
    private final Loop loop;
    
    // Calculated comnformity for each type or group of settings:
    private float intoolsAlarmLLConformity, intoolsAlarmLConformity, 
        intoolsAlarmHConformity, intoolsAlarmHHConformity,
        
        documentsAlarmLLConformity, documentsAlarmLConformity,
        documentsAlarmHConformity, documentsAlarmHHConformity,
            
        systemsAlarmLLConformity, systemsAlarmLConformity,
        systemsAlarmHConformity, systemsAlarmHHConformity;
    
    // Chosen settings from loop tags set. Each setting is nullable, null means
    // that particular setting wasn't chosen:
    protected TagSetting chosenIntoolsAlarmLL, chosenIntoolsAlarmL,
        chosenIntoolsAlarmH, chosenIntoolsAlarmHH,
    
        chosenDocumentsAlarmLL, chosenDocumentsAlarmL, 
        chosenDocumentsAlarmH, chosenDocumentsAlarmHH,
            
        chosenSystemsAlarmLL, chosenSystemsAlarmL,
        chosenSystemsAlarmH, chosenSystemsAlarmHH;
        
     
    /**
     * Protected constructor for abstract class. 
     * 
     * @param loop Loop instance to be wrapped by current logic
     */
    protected SettingsSelector(Loop loop)
    {
        this.loop = loop;
    }// SettingsSelectionLogic
    
    
    /**
     * Calculates conformity percentage against other settings in group for each
     * setting from selected LL alarms.
     */
    protected void _calculateAlarmsLLConformity()
    {
        Enum comparingResult;
        
        if (chosenIntoolsAlarmLL != null && chosenDocumentsAlarmLL != null)
        {    
            // Check alarms LL from documents and SPI for matching: 
            comparingResult = _compare(chosenIntoolsAlarmLL.getValue(), chosenDocumentsAlarmLL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmLLConformity += CONFORMITY_DELTA;
                documentsAlarmLLConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenIntoolsAlarmLL != null && chosenSystemsAlarmLL != null)
        {
            // Check alarms LL from SPI and systems for matching:        
            comparingResult = _compare(chosenIntoolsAlarmLL.getValue(), chosenSystemsAlarmLL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmLLConformity += CONFORMITY_DELTA;
                systemsAlarmLLConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenDocumentsAlarmLL != null && chosenSystemsAlarmLL != null)
        { 
            // Check alarms LL from documents and systems for matching: 
            comparingResult = _compare(chosenDocumentsAlarmLL.getValue(), chosenSystemsAlarmLL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                documentsAlarmLLConformity += CONFORMITY_DELTA;
                systemsAlarmLLConformity += CONFORMITY_DELTA;
            }// if
        }// if
    }// _calculateAlarmsLLConformity
    
    
    /**
     * Calculates conformity percentage against other settings in group for each
     * setting from selected L alarms.
     */
    protected void _calculateAlarmsLConformity()
    {
        Enum comparingResult;
        
        if (chosenIntoolsAlarmL != null && chosenDocumentsAlarmL != null)
        {    
            // Check alarms L from documents and SPI for matching: 
            comparingResult = _compare(chosenIntoolsAlarmL.getValue(), chosenDocumentsAlarmL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmLConformity += CONFORMITY_DELTA;
                documentsAlarmLConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenIntoolsAlarmL != null && chosenSystemsAlarmL != null)
        {
            // Check alarms L from SPI and systems for matching: 
            comparingResult = _compare(chosenIntoolsAlarmL.getValue(), chosenSystemsAlarmL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmLConformity += CONFORMITY_DELTA;
                systemsAlarmLConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenDocumentsAlarmL != null && chosenSystemsAlarmL != null)
        { 
            // Check alarms L from documents and systems for matching: 
            comparingResult = _compare(chosenDocumentsAlarmL.getValue(), chosenSystemsAlarmL.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                documentsAlarmLConformity += CONFORMITY_DELTA;
                systemsAlarmLConformity += CONFORMITY_DELTA;
            }// if
        }// if
    }// _calculateAlarmsLConformity
    
    
    /**
     * Calculates conformity percentage against other settings in group for each
     * setting from selected H alarms.
     */
    protected void _calculateAlarmsHConformity()
    {
        Enum comparingResult;
        
        if (chosenIntoolsAlarmH != null && chosenDocumentsAlarmH != null)
        {    
            // Check alarms H from SPI and documents for matching: 
            comparingResult = _compare(chosenIntoolsAlarmH.getValue(), chosenDocumentsAlarmH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmHConformity += CONFORMITY_DELTA;
                documentsAlarmHConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenIntoolsAlarmH != null && chosenSystemsAlarmH != null)
        {
            // Check alarms H from SPI and systems for matching: 
            comparingResult = _compare(chosenIntoolsAlarmH.getValue(), chosenSystemsAlarmH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmHConformity += CONFORMITY_DELTA;
                systemsAlarmHConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenDocumentsAlarmH != null && chosenSystemsAlarmH != null)
        { 
            // Check alarms H from documents and systems for matching: 
            comparingResult = _compare(chosenDocumentsAlarmH.getValue(), chosenSystemsAlarmH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                documentsAlarmHConformity += CONFORMITY_DELTA;
                systemsAlarmHConformity += CONFORMITY_DELTA;
            }// if
        }// if
    }// _calculateAlarmsHConformity
    
    
    /**
     * Calculates conformity percentage against other settings in group for each
     * setting from selected HH alarms.
     */
    protected void _calculateAlarmsHHConformity()
    {
        Enum comparingResult;
        
        if (chosenIntoolsAlarmHH != null && chosenDocumentsAlarmHH != null)
        {    
            // Check alarms HH from SPI and documents for matching: 
            comparingResult = _compare(chosenIntoolsAlarmHH.getValue(), chosenDocumentsAlarmHH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmHHConformity += CONFORMITY_DELTA;
                documentsAlarmHHConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenIntoolsAlarmHH != null && chosenSystemsAlarmHH != null)
        {
            // Check alarms HH from SPI and systems for matching: 
            comparingResult = _compare(chosenIntoolsAlarmHH.getValue(), chosenSystemsAlarmHH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                intoolsAlarmHHConformity += CONFORMITY_DELTA;
                systemsAlarmHHConformity += CONFORMITY_DELTA;
            }// if
        }// if
        
        if (chosenDocumentsAlarmHH != null && chosenSystemsAlarmHH != null)
        { 
            // Check alarms HH from documents and systems for matching: 
            comparingResult = _compare(chosenDocumentsAlarmHH.getValue(), chosenSystemsAlarmHH.getValue());
            
            if (comparingResult == Compared.AS_DOUBLE_EQUALS || comparingResult == Compared.AS_STRING_EQUALS)
            {
                documentsAlarmHHConformity += CONFORMITY_DELTA;
                systemsAlarmHHConformity += CONFORMITY_DELTA;
            }// if
        }// if
    }// _calculateAlarmsHHConformity

    
    /**
     * Returns loop entity.
     * 
     * @return Loop entity
     */
    public Loop getEntity() 
    {
        return loop;
    }// getEntity

    
    /**
     * Returns calculated SPI alarm LL conformity value.
     * 
     * @return Calculated SPI alarm LL conformity
     */
    public float getIntoolsAlarmLLConformity() 
    {
        return intoolsAlarmLLConformity;
    }// getIntoolsAlarmLLConformity

    
    /**
     * Returns calculated SPI alarm L conformity value.
     * 
     * @return Calculated SPI alarm L conformity
     */
    public float getIntoolsAlarmLConformity() 
    {
        return intoolsAlarmLConformity;
    }// getIntoolsAlarmLConformity

    
    /**
     * Returns calculated SPI alarm H conformity value.
     * 
     * @return Calculated SPI alarm H conformity
     */
    public float getIntoolsAlarmHConformity() 
    {
        return intoolsAlarmHConformity;
    }// getIntoolsAlarmHConformity

    
    /**
     * Returns calculated SPI alarm HH conformity value.
     * 
     * @return Calculated SPI alarm HH conformity
     */
    public float getIntoolsAlarmHHConformity() 
    {
        return intoolsAlarmHHConformity;
    }// getIntoolsAlarmHHConformity

    
    /**
     * Returns calculated documents alarm LL conformity value.
     * 
     * @return Calculated documents alarm LL conformity
     */
    public float getDocumentsAlarmLLConformity() 
    {
        return documentsAlarmLLConformity;
    }// getDocumentsAlarmLLConformity

    
    /**
     * Returns calculated documents alarm L conformity value.
     * 
     * @return Calculated documents alarm L conformity
     */
    public float getDocumentsAlarmLConformity() 
    {
        return documentsAlarmLConformity;
    }// getDocumentsAlarmLConformity

    
    /**
     * Returns calculated documents alarm H conformity value.
     * 
     * @return Calculated documents alarm H conformity
     */
    public float getDocumentsAlarmHConformity() 
    {
        return documentsAlarmHConformity;
    }// getDocumentsAlarmHConformity

    
    /**
     * Returns calculated documents alarm HH conformity value.
     * 
     * @return Calculated documents alarm HH conformity
     */
    public float getDocumentsAlarmHHConformity() 
    {
        return documentsAlarmHHConformity;
    }// getDocumentsAlarmHHConformity

    
    /**
     * Returns calculated systems alarm LL conformity value.
     * 
     * @return Calculated systems alarm LL conformity
     */
    public float getSystemsAlarmLLConformity() 
    {
        return systemsAlarmLLConformity;
    }// getSystemsAlarmLLConformity

    
    /**
     * Returns calculated systems alarm L conformity value.
     * 
     * @return Calculated systems alarm L conformity
     */
    public float getSystemsAlarmLConformity() 
    {
        return systemsAlarmLConformity;
    }// getSystemsAlarmLConformity

    
    /**
     * Returns calculated systems alarm H conformity value.
     * 
     * @return Calculated systems alarm H conformity
     */
    public float getSystemsAlarmHConformity() 
    {
        return systemsAlarmHConformity;
    }// getSystemsAlarmHConformity

    
    /**
     * Returns calculated systems alarm HH conformity value.
     * 
     * @return Calculated systems alarm HH conformity
     */
    public float getSystemsAlarmHHConformity() 
    {
        return systemsAlarmHHConformity;
    }// getSystemsAlarmHHConformity

    
    /**
     * Returns chosen SPI alarm LL setting.
     * 
     * @return Chosen SPI alarm LL setting
     */
    public TagSetting getChosenIntoolsAlarmLL() 
    {
        return chosenIntoolsAlarmLL;
    }// getChosenIntoolsAlarmLL

    
    /**
     * Returns chosen SPI alarm L setting.
     * 
     * @return Chosen SPI alarm L setting
     */
    public TagSetting getChosenIntoolsAlarmL() 
    {
        return chosenIntoolsAlarmL;
    }// getChosenIntoolsAlarmL

    
    /**
     * Returns chosen SPI alarm H setting.
     * 
     * @return Chosen SPI alarm H setting
     */
    public TagSetting getChosenIntoolsAlarmH() 
    {
        return chosenIntoolsAlarmH;
    }// getChosenIntoolsAlarmH

    
    /**
     * Returns chosen SPI alarm HH setting.
     * 
     * @return Chosen SPI alarm HH setting
     */
    public TagSetting getChosenIntoolsAlarmHH() 
    {
        return chosenIntoolsAlarmHH;
    }// getChosenIntoolsAlarmHH

    
    /**
     * Returns chosen documents alarm LL setting.
     * 
     * @return Chosen documents alarm LL setting
     */
    public TagSetting getChosenDocumentsAlarmLL() 
    {
        return chosenDocumentsAlarmLL;
    }// getChosenDocumentsAlarmLL

    
    /**
     * Returns chosen documents alarm L setting.
     * 
     * @return Chosen documents alarm L setting
     */
    public TagSetting getChosenDocumentsAlarmL() 
    {
        return chosenDocumentsAlarmL;
    }// getChosenDocumentsAlarmL

    
    /**
     * Returns chosen documents alarm H setting.
     * 
     * @return Chosen documents alarm H setting
     */
    public TagSetting getChosenDocumentsAlarmH() 
    {
        return chosenDocumentsAlarmH;
    }// getChosenDocumentsAlarmH

    
    /**
     * Returns chosen documents alarm HH setting.
     * 
     * @return Chosen documents alarm HH setting
     */
    public TagSetting getChosenDocumentsAlarmHH() 
    {
        return chosenDocumentsAlarmHH;
    }// getChosenDocumentsAlarmHH
 
    
    /**
     * Returns chosen systems alarm LL setting.
     * 
     * @return Chosen systems alarm LL setting
     */
    public TagSetting getChosenSystemsAlarmLL() 
    {
        return chosenSystemsAlarmLL;
    }// getChosenSystemsAlarmLL

    
    /**
     * Returns chosen systems alarm L setting.
     * 
     * @return Chosen systems alarm L setting
     */
    public TagSetting getChosenSystemsAlarmL() 
    {
        return chosenSystemsAlarmL;
    }// getChosenSystemsAlarmL

    
    /**
     * Returns chosen systems alarm H setting.
     * 
     * @return Chosen systems alarm H setting
     */
    public TagSetting getChosenSystemsAlarmH() 
    {
        return chosenSystemsAlarmH;
    }// getChosenSystemsAlarmH

    
    /**
     * Returns chosen systems alarm HH setting.
     * 
     * @return Chosen systems alarm HH setting
     */
    public TagSetting getChosenSystemsAlarmHH() 
    {
        return chosenSystemsAlarmHH;
    }// getChosenSystemsAlarmHH
}// SettingsSelector
