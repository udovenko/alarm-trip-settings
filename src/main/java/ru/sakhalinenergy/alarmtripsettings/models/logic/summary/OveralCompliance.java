package ru.sakhalinenergy.alarmtripsettings.models.logic.summary;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * A wrapper for LoopsTable instance. Calculates and stores overall settings 
 * compliance data for compliance charts.
 * 
 * @author Denis Udovenko 
 * @version 1.0.6
 */
public class OveralCompliance extends Model
{
    
    public static enum Event
    {
        SUMMARY_CALCULATED
    }// Event
    
    
    private final Double FULL_CONFORMITY_MIN = 99.0;
    private final Double SEMI_CONFORMITY_MIN = 20.0;
   
    private int totalAlarmsWithSemiConformity = 0;
    private int totalAlarmsWithFullConformity = 0;
    private int totalAlarmsWithNullConformity = 0;
    
    private int alarmsLowLowWithSemiConformity = 0;
    private int alarmsLowLowWithFullConformity = 0;
    private int alarmsLowLowWithNullConformity = 0;
        
    private int alarmsLowWithSemiConformity = 0;
    private int alarmsLowWithFullConformity = 0;
    private int alarmsLowWithNullConformity = 0;
        
    private int alarmsHighWithSemiConformity = 0;
    private int alarmsHighWithFullConformity = 0;
    private int alarmsHighWithNullConformity = 0;
        
    private int alarmsHighHighWithSemiConformity = 0;
    private int alarmsHighHighWithFullConformity = 0;
    private int alarmsHighHighWithNullConformity = 0;
    
    private final LoopsTableObservable loopsTable;
    
    
    
    /**
     * Public constructor.
     * 
     * @param loopsTable LoopsTable instance to be wrapped
     */
    public OveralCompliance(LoopsTableObservable loopsTable)
    {
        this.loopsTable = loopsTable;
        this.loopsTable.on(CollectionEvent.LOOPS_READ, new _LoopsTableUpdateEventHandler());
    }// OveralCompliance
    
    
    /**
     * 
     */
    private class _LoopsTableUpdateEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            totalAlarmsWithSemiConformity = 0;
            totalAlarmsWithFullConformity = 0;
            totalAlarmsWithNullConformity = 0;
        
            alarmsLowLowWithSemiConformity = 0;
            alarmsLowLowWithFullConformity = 0;
            alarmsLowLowWithNullConformity = 0;
        
            alarmsLowWithSemiConformity = 0;
            alarmsLowWithFullConformity = 0;
            alarmsLowWithNullConformity = 0;
        
            alarmsHighWithSemiConformity = 0;
            alarmsHighWithFullConformity = 0;
            alarmsHighWithNullConformity = 0;
        
            alarmsHighHighWithSemiConformity = 0;
            alarmsHighHighWithFullConformity = 0;
            alarmsHighHighWithNullConformity = 0;
        
            Float tempConformity;
        
            //Обходими всю коллекцию лупов:
            for (SettingsSelector tempLoop : loopsTable.getWrappedLoops())
            {
                //Получаем соотношение наижних трипов прибора:
                if (_hasAlarmLowLow(tempLoop))
                {
                    tempConformity = _getAlarmsLowLowMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsLowLowWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsLowLowWithSemiConformity++;
                    else alarmsLowLowWithNullConformity++;
                }//if

                //Получаем соотношение наижних трипов прибора:
                if (_hasAlarmLow(tempLoop))
                {
                    tempConformity = _getAlarmsLowMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsLowWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsLowWithSemiConformity++;
                    else alarmsLowWithNullConformity++;
                }//if

                //Получаем соотношение верхних алармов прибора:
                if (_hasAlarmHigh(tempLoop))
                {
                    tempConformity = _getAlarmsHighMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsHighWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsHighWithSemiConformity++;
                    else alarmsHighWithNullConformity++;
                }//if

                //Получаем соотношение верхних трипов прибора:
                if (_hasAlarmHighHigh(tempLoop))
                {
                    tempConformity = _getAlarmsHighHighMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsHighHighWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsHighHighWithSemiConformity++;
                    else alarmsHighHighWithNullConformity++;
                }//if
            }//for

            //Рассчитываем суммарные показатели:
            totalAlarmsWithSemiConformity = alarmsLowLowWithSemiConformity + alarmsLowWithSemiConformity 
                + alarmsHighWithSemiConformity + alarmsHighHighWithSemiConformity;
            totalAlarmsWithFullConformity = alarmsLowLowWithFullConformity + alarmsLowWithFullConformity 
                + alarmsHighWithFullConformity + alarmsHighHighWithFullConformity;
            totalAlarmsWithNullConformity = alarmsLowLowWithNullConformity + alarmsLowWithNullConformity 
                + alarmsHighWithNullConformity + alarmsHighHighWithNullConformity;
            
            // Trigger an event which determines that summary calculation is complete:
            CustomEvent summaryCalculatedEvent = new CustomEvent(new Object());
            events.trigger(Event.SUMMARY_CALCULATED, summaryCalculatedEvent);
        }// customEventOccurred
    }// _LoopsTableUpdateEventHandler

    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного нижнего трипа из 
     * любого источника данных.
     * 
     * @param device Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmLowLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmLL()!= null 
            || loop.getChosenDocumentsAlarmLL() != null 
            || loop.getChosenSystemsAlarmLL() != null);
    }// _hasAlarmLowLow
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного нижнего аларма 
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmH() != null 
            || loop.getChosenDocumentsAlarmH() != null 
            || loop.getChosenSystemsAlarmH() != null);
    }// _hasAlarmHigh
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного верхнего аларма
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmHighHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmHH() != null 
            || loop.getChosenDocumentsAlarmHH() != null 
            || loop.getChosenSystemsAlarmHH() != null);
    }//_hasAlarmHighHigh
    
    
    /**
     * Метод возвращает факт наличия у устройства рассчитанного верхнего трипа 
     * из любого источника данных.
     * 
     * @param   device  Экземпляр устройства
     * @return  Boolean
     */
    private static Boolean _hasAlarmLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmL() != null 
            || loop.getChosenDocumentsAlarmL() != null 
            || loop.getChosenSystemsAlarmL() != null);
    }//_hasAlarmLowLow
    
    
    /**
     * Метод получает максимальную величину соответствия среди рассчитанных
     * нижних трипов устройства.
     * 
     * @param   device  Экземпляр устройства
     * @return  
     */
    private static Float _getAlarmsLowLowMaxConformity(SettingsSelector loop)
    {
        Float tempConformity = 0.0F;
     
        if (loop.getIntoolsAlarmLLConformity() > tempConformity) 
            tempConformity = loop.getIntoolsAlarmLLConformity();
       
        if (loop.getDocumentsAlarmLLConformity() > tempConformity) 
            tempConformity = loop.getDocumentsAlarmLLConformity();
        
        if (loop.getSystemsAlarmLLConformity() > tempConformity) 
            tempConformity = loop.getSystemsAlarmLLConformity();
                    
        return tempConformity;
    }//_getAlarmsLowLowMaxConformity
    
    
    /**
     * Метод получает максимальную величину соответствия среди рассчитанных
     * нижних алармов устройства.
     * 
     * @param   device  Экземпляр устройства
     * @return  Double
     */
    private static Float _getAlarmsLowMaxConformity(SettingsSelector loop)
    {
        Float tempConformity = 0.0F;
        
        if (loop.getDocumentsAlarmLConformity() > tempConformity) 
            tempConformity = loop.getIntoolsAlarmLConformity();

        if (loop.getDocumentsAlarmLConformity() > tempConformity) 
            tempConformity = loop.getDocumentsAlarmLConformity();
        
        if (loop.getSystemsAlarmLConformity() > tempConformity) 
                tempConformity = loop.getSystemsAlarmLConformity();
                
        return tempConformity;
    }//_getAlarmsLowMaxConformity
    
    
    /**
     * Метод получает максимальную величину соответствия среди рассчитанных
     * верхних алармов устройства.
     * 
     * @param   device  Экземпляр устройства
     * @return  Double
     */
    private static Float _getAlarmsHighMaxConformity(SettingsSelector loop)
    {
        Float tempConformity = 0.0F;

        if (loop.getIntoolsAlarmHConformity() > tempConformity) 
            tempConformity = loop.getIntoolsAlarmHConformity();
        
        if (loop.getDocumentsAlarmHConformity() > tempConformity) 
            tempConformity = loop.getDocumentsAlarmHConformity();
                    
        if (loop.getSystemsAlarmHConformity() > tempConformity) 
            tempConformity = loop.getSystemsAlarmHConformity();
        
        return tempConformity;
    }//_getAlarmsHighMaxConformity
    
    
    /**
     * 
     * 
     */
    private static Float _getAlarmsHighHighMaxConformity(SettingsSelector loop)
    {
        Float tempConformity = 0.0F;
                   
        if (loop.getIntoolsAlarmHHConformity() > tempConformity) 
            tempConformity = loop.getIntoolsAlarmHHConformity();
        
        if (loop.getDocumentsAlarmHHConformity() > tempConformity) 
            tempConformity = loop.getDocumentsAlarmHHConformity();
        
        if (loop.getSystemsAlarmHHConformity() > tempConformity) 
            tempConformity = loop.getSystemsAlarmHHConformity();
        
        return tempConformity;
    }//_getAlarmsHighHighMaxConformity
    
    
    /**
     * 
     */
    public int getAlarmsLowLowWithSemiConformity()
    {
        return this.alarmsLowLowWithSemiConformity;
    }// getAlarmsLowLowWithSemiConformity        
            
            
    /**
     * 
     */
    public int getAlarmsLowLowWithFullConformity()
    {
        return this.alarmsLowLowWithFullConformity;
    }// getAlarmsLowLowWithFullConformity
    
    
    /**
     * 
     */
    public int getAlarmsLowLowWithNullConformity()
    {
        return this.alarmsLowLowWithNullConformity;
    }// getAlarmsLowLowWithNullConformity
    
    
    /**
     * 
     */
    public int getAlarmsLowWithSemiConformity()
    {
        return this.alarmsLowWithSemiConformity;
    }// getAlarmsLowWithSemiConformity       
            
            
    /**
     * 
     */
    public int getAlarmsLowWithFullConformity()
    {
        return this.alarmsLowWithFullConformity;
    }// getAlarmsLowWithFullConformity
    
    
    /**
     * 
     * 
     */
    public int getAlarmsLowWithNullConformity()
    {
        return this.alarmsLowWithNullConformity;
    }// getAlarmsLowWithNullConformity
    
    
    /**
     * 
     * 
     */
    public int getAlarmsHighWithSemiConformity()
    {
        return this.alarmsHighWithSemiConformity;
    }// getAlarmsHighWithSemiConformity       
            
      
    /**
     * 
     */
    public int getAlarmsHighWithFullConformity()
    {
        return this.alarmsHighWithFullConformity;
    }// getAlarmsHighWithFullConformity
    
    
    /**
     * 
     */
    public int getAlarmsHighWithNullConformity()
    {
        return this.alarmsHighWithNullConformity;
    }// getAlarmsHighWithNullConformity
    
    
    /**
     * 
     */
    public int getAlarmsHighHighWithSemiConformity()
    {
        return this.alarmsHighHighWithSemiConformity;
    }// getAlarmsHighHighWithSemiConformity       
            
           
    /**
     * 
     * 
     */
    public int getAlarmsHighHighWithFullConformity()
    {
        return this.alarmsHighHighWithFullConformity;
    }// getAlarmsHighHighWithFullConformity
    
    
    /**
     * 
     * 
     */
    public int getAlarmsHighHighWithNullConformity()
    {
        return this.alarmsHighHighWithNullConformity;
    }// getAlarmsHighHighWithNullConformity
    
    
    /**
     * 
     */
    public int getTotalAlarmsWithSemiConformity()
    {
        return this.totalAlarmsWithSemiConformity;
    }// getTotalAlarmsWithSemiConformity
    
    
    /**
     * 
     */
    public int getTotalAlarmsWithFullConformity()
    {
        return this.totalAlarmsWithFullConformity;
    }// getTotalAlarmsWithFullConformity
    
    
    /**
     * 
     * 
     */
    public int getTotalAlarmsWithNullConformity()
    {
        return this.totalAlarmsWithNullConformity;
    }// getTotalAlarmsWithNullConformity
}//SummaryCalculation
