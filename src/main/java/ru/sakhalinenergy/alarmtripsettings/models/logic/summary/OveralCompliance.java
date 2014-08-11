package ru.sakhalinenergy.alarmtripsettings.models.logic.summary;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * A wrapper for LoopsTable instance. Calculates and stores overall settings 
 * compliance data for compliance charts.
 * 
 * @author Denis Udovenko 
 * @version 1.0.7
 */
public class OveralCompliance extends Compliance
{
   
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
    
        
    /**
     * Public constructor. Subscribes model on wrapped loops table events.
     * 
     * @param loopsTable Loops table which overall compliance summary will be calculated
     */
    public OveralCompliance(LoopsTableObservable loopsTable)
    {
        super(loopsTable);
    }// OveralCompliance
    
    
    /**
     * Abstract factory method which returns instance of inner class - handler 
     * for for wrapped loops table data read event.
     * 
     * @return instance of inner class - handler for for wrapped loops table data read event
     */
    @Override
    protected CustomEventListener _getLoopsTableUpdateEventHandler()
    {
        return new _LoopsTableUpdateEventHandler();
    }// _getLoopsTableUpdateEventHandler
    
    
    /**
     * Inner class - handler for for wrapped loops table data read event. 
     * Calculates overall compliance summary for each type of alarm and total
     * overall compliance summary.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
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
        
            // Iterate loops collection:
            for (SettingsSelector tempLoop : loopsTable.getWrappedLoops())
            {
                // Get LL alams status:
                if (_hasAlarmLowLow(tempLoop))
                {
                    tempConformity = _getAlarmsLowLowMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsLowLowWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsLowLowWithSemiConformity++;
                    else alarmsLowLowWithNullConformity++;
                }// if

                // Get L alams status:
                if (_hasAlarmLow(tempLoop))
                {
                    tempConformity = _getAlarmsLowMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsLowWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsLowWithSemiConformity++;
                    else alarmsLowWithNullConformity++;
                }// if

                // Get H alams status:
                if (_hasAlarmHigh(tempLoop))
                {
                    tempConformity = _getAlarmsHighMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsHighWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsHighWithSemiConformity++;
                    else alarmsHighWithNullConformity++;
                }// if

                // Get HH alams status:
                if (_hasAlarmHighHigh(tempLoop))
                {
                    tempConformity = _getAlarmsHighHighMaxConformity(tempLoop);
                    if (tempConformity > FULL_CONFORMITY_MIN) alarmsHighHighWithFullConformity++;
                    else if (tempConformity > SEMI_CONFORMITY_MIN) alarmsHighHighWithSemiConformity++;
                    else alarmsHighHighWithNullConformity++;
                }// if
            }// for

            // Calculate total indexes:
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
     * Returns maximum conformity for calculated LL alarms of the loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Maximum conformity for calculated LL alarms of the loop 
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
    }// _getAlarmsLowLowMaxConformity
    
    
    /**
     * Returns maximum conformity for calculated L alarms of the loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Maximum conformity for calculated L alarms of the loop 
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
    }// _getAlarmsLowMaxConformity
    
    
    /**
     * Returns maximum conformity for calculated H alarms of the loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Maximum conformity for calculated H alarms of the loop 
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
    }// _getAlarmsHighMaxConformity
    
    
    /**
     * Returns maximum conformity for calculated HH alarms of the loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Maximum conformity for calculated HH alarms of the loop 
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
    }// _getAlarmsHighHighMaxConformity
    
    
    /**
     * Returns count of semi-confirmed LL alarms.
     * 
     * @return Count of semi-confirmed LL alarms
     */
    public int getAlarmsLowLowWithSemiConformity()
    {
        return this.alarmsLowLowWithSemiConformity;
    }// getAlarmsLowLowWithSemiConformity        
            
            
    /**
     * Returns count of fully confirmed LL alarms.
     * 
     * @return Count of fully confirmed LL alarms
     */
    public int getAlarmsLowLowWithFullConformity()
    {
        return this.alarmsLowLowWithFullConformity;
    }// getAlarmsLowLowWithFullConformity
    
    
    /**
     * Returns count of non-confirmed LL alarms.
     * 
     * @return Count of non-confirmed LL alarms
     */
    public int getAlarmsLowLowWithNullConformity()
    {
        return this.alarmsLowLowWithNullConformity;
    }// getAlarmsLowLowWithNullConformity
    
    
    /**
     * Returns count of semi-confirmed L alarms.
     * 
     * @return Count of semi-confirmed L alarms
     */
    public int getAlarmsLowWithSemiConformity()
    {
        return this.alarmsLowWithSemiConformity;
    }// getAlarmsLowWithSemiConformity       
            
            
    /**
     * Returns count of fully confirmed L alarms.
     * 
     * @return Count of fully confirmed L alarms
     */
    public int getAlarmsLowWithFullConformity()
    {
        return this.alarmsLowWithFullConformity;
    }// getAlarmsLowWithFullConformity
    
    
    /**
     * Returns count of non-confirmed L alarms.
     * 
     * @return Count of non-confirmed L alarms
     */
    public int getAlarmsLowWithNullConformity()
    {
        return this.alarmsLowWithNullConformity;
    }// getAlarmsLowWithNullConformity
    
    
    /**
     * Returns count of semi-confirmed H alarms.
     * 
     * @return Count of semi-confirmed H alarms
     */
    public int getAlarmsHighWithSemiConformity()
    {
        return this.alarmsHighWithSemiConformity;
    }// getAlarmsHighWithSemiConformity       
            
      
    /**
     * Returns count of fully confirmed H alarms.
     * 
     * @return Count of fully confirmed H alarms
     */
    public int getAlarmsHighWithFullConformity()
    {
        return this.alarmsHighWithFullConformity;
    }// getAlarmsHighWithFullConformity
    
    
    /**
     * Returns count of non-confirmed H alarms.
     * 
     * @return Count of non-confirmed H alarms
     */
    public int getAlarmsHighWithNullConformity()
    {
        return this.alarmsHighWithNullConformity;
    }// getAlarmsHighWithNullConformity
    
    
    /**
     * Returns count of semi-confirmed HH alarms.
     * 
     * @return Count of semi-confirmed HH alarms
     */
    public int getAlarmsHighHighWithSemiConformity()
    {
        return this.alarmsHighHighWithSemiConformity;
    }// getAlarmsHighHighWithSemiConformity       
            
           
    /**
     * Returns count of fully confirmed HH alarms.
     * 
     * @return Count of fully confirmed HH alarms
     */
    public int getAlarmsHighHighWithFullConformity()
    {
        return this.alarmsHighHighWithFullConformity;
    }// getAlarmsHighHighWithFullConformity
    
    
    /**
     * Returns count of non-confirmed HH alarms.
     * 
     * @return Count of non-confirmed HH alarms
     */
    public int getAlarmsHighHighWithNullConformity()
    {
        return this.alarmsHighHighWithNullConformity;
    }// getAlarmsHighHighWithNullConformity
    
    
    /**
     * Returns total count of semi-confirmed alarms.
     * 
     * @return Total count of semi-confirmed alarms
     */
    public int getTotalAlarmsWithSemiConformity()
    {
        return this.totalAlarmsWithSemiConformity;
    }// getTotalAlarmsWithSemiConformity
    
    
    /**
     * Returns total count of fully confirmed alarms.
     * 
     * @return Total count of fully confirmed alarms
     */
    public int getTotalAlarmsWithFullConformity()
    {
        return this.totalAlarmsWithFullConformity;
    }// getTotalAlarmsWithFullConformity
    
    
    /**
     * Returns total count of non-confirmed alarms.
     * 
     * @return Total count of non-confirmed alarms
     */
    public int getTotalAlarmsWithNullConformity()
    {
        return this.totalAlarmsWithNullConformity;
    }// getTotalAlarmsWithNullConformity
}// OveralCompliance
