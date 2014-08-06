package ru.sakhalinenergy.alarmtripsettings.models.logic.settings;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 *
 * @author Denis.Udovenko
 * @version 1.0.1
 */
public abstract class SettingsSelector extends Comparator
{
    protected static String DELETED_SETTING_RESERVED_WORD = "deleted";
    
    protected final Float CONFORMITY_DELTA = 100.0F / 2.0F;
    private final Loop loop;
    
    //Calculated comnformity for each type or geroup of settings:
    private float intoolsAlarmLLConformity, intoolsAlarmLConformity, 
        intoolsAlarmHConformity, intoolsAlarmHHConformity,
        
        documentsAlarmLLConformity, documentsAlarmLConformity,
        documentsAlarmHConformity, documentsAlarmHHConformity,
            
        systemsAlarmLLConformity, systemsAlarmLConformity,
        systemsAlarmHConformity, systemsAlarmHHConformity;
    
    //Chosen settings from loop tags set. Each setting is nullable, null means that particular setting wasn't chosen:
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
    }//SettingsSelectionLogic
    
    
    /**
     * Метод рассчитывает для каждого параметра из группы выбранных нижних 
     * трипов прибора процент совпадения с остальными значениями в группе.
     */
    protected void _calculateAlarmsLLConformity()
    {
        byte comparingResult;
        
        if (chosenIntoolsAlarmLL != null && chosenDocumentsAlarmLL != null)
        {    
            //Проверяем на соответвие нижний трип из SPI и нижний трип из документов: 
            comparingResult = _compare(chosenIntoolsAlarmLL.getValue(), chosenDocumentsAlarmLL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmLLConformity += CONFORMITY_DELTA;
                documentsAlarmLLConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenIntoolsAlarmLL != null && chosenSystemsAlarmLL != null)
        {
            //Проверяем на соответвие нижний трип из SPI и нижний трип из систем:       
            comparingResult = _compare(chosenIntoolsAlarmLL.getValue(), chosenSystemsAlarmLL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmLLConformity += CONFORMITY_DELTA;
                systemsAlarmLLConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenDocumentsAlarmLL != null && chosenSystemsAlarmLL != null)
        { 
            //Проверяем на соответвие нижний трип из документов и нижний трип из систем:       
            comparingResult = _compare(chosenDocumentsAlarmLL.getValue(), chosenSystemsAlarmLL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                documentsAlarmLLConformity += CONFORMITY_DELTA;
                systemsAlarmLLConformity += CONFORMITY_DELTA;
            }//if
        }//if
    }//_calculateAlarmsLLConformity
    
    
    /**
     * Метод рассчитывает для каждого параметра из группы выбранных нижних 
     * алармов прибора процент совпадения с остальными значениями в группе.
     */
    protected void _calculateAlarmsLConformity()
    {
        byte comparingResult;
        
        if (chosenIntoolsAlarmL != null && chosenDocumentsAlarmL != null)
        {    
            //Проверяем на соответвие нижний аларм из SPI и нижний аларм из документов: 
            comparingResult = _compare(chosenIntoolsAlarmL.getValue(), chosenDocumentsAlarmL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmLConformity += CONFORMITY_DELTA;
                documentsAlarmLConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenIntoolsAlarmL != null && chosenSystemsAlarmL != null)
        {
            //Проверяем на соответвие нижний аларм из SPI и нижний аларм из систем:       
            comparingResult = _compare(chosenIntoolsAlarmL.getValue(), chosenSystemsAlarmL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmLConformity += CONFORMITY_DELTA;
                systemsAlarmLConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenDocumentsAlarmL != null && chosenSystemsAlarmL != null)
        { 
            //Проверяем на соответвие нижний аларм из документов и нижний аларм из систем:       
            comparingResult = _compare(chosenDocumentsAlarmL.getValue(), chosenSystemsAlarmL.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                documentsAlarmLConformity += CONFORMITY_DELTA;
                systemsAlarmLConformity += CONFORMITY_DELTA;
            }//if
        }//if
    }//_calculateAlarmsLConformity
    
    
    /**
     * Метод рассчитывает для каждого параметра из группы выбранных верхних 
     * алармов прибора процент совпадения с остальными значениями в группе.
     */
    protected void _calculateAlarmsHConformity()
    {
        byte comparingResult;
        
        if (chosenIntoolsAlarmH != null && chosenDocumentsAlarmH != null)
        {    
            //Проверяем на соответвие верхний аларм из SPI и верхний аларм из документов: 
            comparingResult = _compare(chosenIntoolsAlarmH.getValue(), chosenDocumentsAlarmH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmHConformity += CONFORMITY_DELTA;
                documentsAlarmHConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenIntoolsAlarmH != null && chosenSystemsAlarmH != null)
        {
            //Проверяем на соответвие верхний аларм из SPI и верхний аларм из систем:       
            comparingResult = _compare(chosenIntoolsAlarmH.getValue(), chosenSystemsAlarmH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmHConformity += CONFORMITY_DELTA;
                systemsAlarmHConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenDocumentsAlarmH != null && chosenSystemsAlarmH != null)
        { 
            //Проверяем на соответвие верхний аларм из документов и верхний аларм из систем:       
            comparingResult = _compare(chosenDocumentsAlarmH.getValue(), chosenSystemsAlarmH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                documentsAlarmHConformity += CONFORMITY_DELTA;
                systemsAlarmHConformity += CONFORMITY_DELTA;
            }//if
        }//if
    }//_calculateAlarmsHConformity
    
    
    /**
     * Метод рассчитывает для каждого параметра из группы выбранных верхних 
     * трипов прибора процент совпадения с остальными значениями в группе.
     */
    protected void _calculateAlarmsHHConformity()
    {
        byte comparingResult;
        
        if (chosenIntoolsAlarmHH != null && chosenDocumentsAlarmHH != null)
        {    
            //Проверяем на соответвие верхний трип из SPI и верхний трип из документов: 
            comparingResult = _compare(chosenIntoolsAlarmHH.getValue(), chosenDocumentsAlarmHH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmHHConformity += CONFORMITY_DELTA;
                documentsAlarmHHConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenIntoolsAlarmHH != null && chosenSystemsAlarmHH != null)
        {
            //Проверяем на соответвие верхний трип из SPI и верхний трип из систем:       
            comparingResult = _compare(chosenIntoolsAlarmHH.getValue(), chosenSystemsAlarmHH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                intoolsAlarmHHConformity += CONFORMITY_DELTA;
                systemsAlarmHHConformity += CONFORMITY_DELTA;
            }//if
        }//if
        
        if (chosenDocumentsAlarmHH != null && chosenSystemsAlarmHH != null)
        { 
            //Проверяем на соответвие верхний трип из документов и верхний трип из систем:       
            comparingResult = _compare(chosenDocumentsAlarmHH.getValue(), chosenSystemsAlarmHH.getValue());
            
            if (comparingResult == COMPARED_AS_DOUBLE_EQUALS || comparingResult == COMPARED_AS_STRING_EQUALS)
            {
                documentsAlarmHHConformity += CONFORMITY_DELTA;
                systemsAlarmHHConformity += CONFORMITY_DELTA;
            }//if
        }//if
    }//_calculateAlarmsHHConformity

    
    /**
     * @return the loop
     */
    public Loop getEntity() 
    {
        return loop;
    }

    
    /**
     * @return the intoolsAlarmLLConformity
     */
    public float getIntoolsAlarmLLConformity() 
    {
        return intoolsAlarmLLConformity;
    }

    
    /**
     * @return the intoolsAlarmLConformity
     */
    public float getIntoolsAlarmLConformity() 
    {
        return intoolsAlarmLConformity;
    }

    
    /**
     * @return the intoolsAlarmHConformity
     */
    public float getIntoolsAlarmHConformity() 
    {
        return intoolsAlarmHConformity;
    }

    
    /**
     * @return the intoolsAlarmHHConformity
     */
    public float getIntoolsAlarmHHConformity() 
    {
        return intoolsAlarmHHConformity;
    }

    
    /**
     * @return the documentsAlarmLLConformity
     */
    public float getDocumentsAlarmLLConformity() 
    {
        return documentsAlarmLLConformity;
    }

    
    /**
     * @return the documentsAlarmLConformity
     */
    public float getDocumentsAlarmLConformity() 
    {
        return documentsAlarmLConformity;
    }

    
    /**
     * @return the documentsAlarmHConformity
     */
    public float getDocumentsAlarmHConformity() 
    {
        return documentsAlarmHConformity;
    }

    
    /**
     * @return the documentsAlarmHHConformity
     */
    public float getDocumentsAlarmHHConformity() 
    {
        return documentsAlarmHHConformity;
    }

    
    /**
     * @return the systemsAlarmLLConformity
     */
    public float getSystemsAlarmLLConformity() 
    {
        return systemsAlarmLLConformity;
    }

    
    /**
     * @return the systemsAlarmLConformity
     */
    public float getSystemsAlarmLConformity() 
    {
        return systemsAlarmLConformity;
    }

    
    /**
     * @return the systemsAlarmHConformity
     */
    public float getSystemsAlarmHConformity() 
    {
        return systemsAlarmHConformity;
    }

    
    /**
     * @return the systemsAlarmHHConformity
     */
    public float getSystemsAlarmHHConformity() 
    {
        return systemsAlarmHHConformity;
    }

    
    /**
     * @return the chosenIntoolsAlarmLL
     */
    public TagSetting getChosenIntoolsAlarmLL() 
    {
        return chosenIntoolsAlarmLL;
    }

    
    /**
     * @return the chosenIntoolsAlarmL
     */
    public TagSetting getChosenIntoolsAlarmL() 
    {
        return chosenIntoolsAlarmL;
    }

    
    /**
     * @return the chosenIntoolsAlarmH
     */
    public TagSetting getChosenIntoolsAlarmH() 
    {
        return chosenIntoolsAlarmH;
    }

    
    /**
     * @return the chosenIntoolsAlarmHH
     */
    public TagSetting getChosenIntoolsAlarmHH() 
    {
        return chosenIntoolsAlarmHH;
    }

    
    /**
     * @return the chosenDocumentsAlarmLL
     */
    public TagSetting getChosenDocumentsAlarmLL() 
    {
        return chosenDocumentsAlarmLL;
    }

    
    /**
     * @return the chosenDocumentsAlarmL
     */
    public TagSetting getChosenDocumentsAlarmL() 
    {
        return chosenDocumentsAlarmL;
    }

    
    /**
     * @return the chosenDocumentsAlarmH
     */
    public TagSetting getChosenDocumentsAlarmH() 
    {
        return chosenDocumentsAlarmH;
    }

    
    /**
     * @return the chosenDocumentsAlarmHH
     */
    public TagSetting getChosenDocumentsAlarmHH() 
    {
        return chosenDocumentsAlarmHH;
    }
 
    
    /**
     * @return the chosenSystemsAlarmLL
     */
    public TagSetting getChosenSystemsAlarmLL() 
    {
        return chosenSystemsAlarmLL;
    }

    
    /**
     * @return the chosenSystemsAlarmL
     */
    public TagSetting getChosenSystemsAlarmL() 
    {
        return chosenSystemsAlarmL;
    }

    
    /**
     * @return the chosenSystemsAlarmH
     */
    public TagSetting getChosenSystemsAlarmH() 
    {
        return chosenSystemsAlarmH;
    }

    
    /**
     * @return the chosenSystemsAlarmHH
     */
    public TagSetting getChosenSystemsAlarmHH() 
    {
        return chosenSystemsAlarmHH;
    }
}//SettingsSelectionLogicNew
