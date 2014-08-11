package ru.sakhalinenergy.alarmtripsettings.models.logic.summary;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * A wrapper for LoopsTable instance. Calculates SPI data compliance summary 
 * for compliance charts.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
public class IntoolsCompliance extends Compliance
{
 
    // Loop's chosen setting conformity status enumeration:
    private static enum _Confirmed
    {
        FULLY,
        NON,
        AGAINST_DOCUMENTS,
        AGAINST_SYSTEMS;
    }// Confirmed
    
    
    private final static Double FULL_CONFORMITY_MIN = 99.0;
    private final static Double SEMI_CONFORMITY_MIN = 20.0;
    
    private int totalAlarmsCompliantWithDocumentsAndSystems;
    private int totalAlarmsCompliantWithDocuments;
    private int totalAlarmsCompliantWithSystems;
    private int totalAlarmsNonCompliant;
    
    private int alarmsLowLowCompliantWithDocumentsAndSystems;
    private int alarmsLowLowCompliantWithDocuments;
    private int alarmsLowLowCompliantWithSystems;
    private int alarmsLowLowNonCompliant;
        
    private int alarmsLowCompliantWithDocumentsAndSystems;
    private int alarmsLowCompliantWithDocuments;
    private int alarmsLowCompliantWithSystems;
    private int alarmsLowNonCompliant;
        
    private int alarmsHighCompliantWithDocumentsAndSystems;
    private int alarmsHighCompliantWithDocuments;
    private int alarmsHighCompliantWithSystems;
    private int alarmsHighNonCompliant;
      
    private int alarmsHighHighCompliantWithDocumentsAndSystems;
    private int alarmsHighHighCompliantWithDocuments;
    private int alarmsHighHighCompliantWithSystems;
    private int alarmsHighHighNonCompliant;
    
    
    /**
     * Public constructor. Subscribes model on wrapped loops table events.
     * 
     * @param loopsTable Loops table which SPI compliance summary will be calculated
     */
    public IntoolsCompliance(LoopsTableObservable loopsTable)
    {
        super(loopsTable);
    }// IntoolsCompliance
    
    
    /**
     * Factory method which returns instance of inner class - handler for
     * wrapped loops table data read event.
     * 
     * @return instance of inner class - handler for for wrapped loops table data read event
     */
    @Override
    protected CustomEventListener _getLoopsTableUpdateEventHandler()
    {
        return new _LoopsTableUpdateEventHandler();
    }// _getLoopsTableUpdateEventHandler
    
    
    /**
     * Inner class - handler for wrapped loops table data read event. Calculates
     * SPI compliance summary for each type of alarm and total summary.
     *
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _LoopsTableUpdateEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Enum tempComplianceStatus;

            totalAlarmsCompliantWithDocumentsAndSystems = 0;
            totalAlarmsCompliantWithDocuments = 0;
            totalAlarmsCompliantWithSystems = 0;
            totalAlarmsNonCompliant = 0;

            alarmsLowLowCompliantWithDocumentsAndSystems = 0;
            alarmsLowLowCompliantWithDocuments = 0;
            alarmsLowLowCompliantWithSystems = 0;
            alarmsLowLowNonCompliant = 0;

            alarmsLowCompliantWithDocumentsAndSystems = 0;
            alarmsLowCompliantWithDocuments = 0;
            alarmsLowCompliantWithSystems = 0;
            alarmsLowNonCompliant = 0;

            alarmsHighCompliantWithDocumentsAndSystems = 0;
            alarmsHighCompliantWithDocuments = 0;
            alarmsHighCompliantWithSystems = 0;
            alarmsHighNonCompliant = 0;

            alarmsHighHighCompliantWithDocumentsAndSystems = 0;
            alarmsHighHighCompliantWithDocuments = 0;
            alarmsHighHighCompliantWithSystems = 0;
            alarmsHighHighNonCompliant = 0;

            // Iterate loops collection:
            for (SettingsSelector tempLoop : loopsTable.getWrappedLoops())
            {
                // Get LL alams status:
                if (_hasAlarmLowLow(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsLowLowComplanceType(tempLoop);
                    if (tempComplianceStatus == _Confirmed.FULLY) alarmsLowLowCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_DOCUMENTS) alarmsLowLowCompliantWithDocuments++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_SYSTEMS) alarmsLowLowCompliantWithSystems++;
                    if (tempComplianceStatus == _Confirmed.NON) alarmsLowLowNonCompliant++;
                }// if

                // Get L alams status:
                if (_hasAlarmLow(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsLowComplanceType(tempLoop);
                    if (tempComplianceStatus == _Confirmed.FULLY) alarmsLowCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_DOCUMENTS) alarmsLowCompliantWithDocuments++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_SYSTEMS) alarmsLowCompliantWithSystems++;
                    if (tempComplianceStatus == _Confirmed.NON) alarmsLowNonCompliant++;
                }// if

                // Get H alams status:
                if (_hasAlarmHigh(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsHighComplanceType(tempLoop);
                    if (tempComplianceStatus == _Confirmed.FULLY) alarmsHighCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_DOCUMENTS) alarmsHighCompliantWithDocuments++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_SYSTEMS) alarmsHighCompliantWithSystems++;
                    if (tempComplianceStatus == _Confirmed.NON) alarmsHighNonCompliant++;
                }// if

                // Get HH alams status:
                if (_hasAlarmHighHigh(tempLoop))
                {
                    tempComplianceStatus = _getLoopAlarmsHighHighComplanceType(tempLoop);
                    if (tempComplianceStatus == _Confirmed.FULLY) alarmsHighHighCompliantWithDocumentsAndSystems++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_DOCUMENTS) alarmsHighHighCompliantWithDocuments++;
                    if (tempComplianceStatus == _Confirmed.AGAINST_SYSTEMS) alarmsHighHighCompliantWithSystems++;
                    if (tempComplianceStatus == _Confirmed.NON) alarmsHighHighNonCompliant++;
                }// if
            }// for

            // Calculate total indexes:
            totalAlarmsCompliantWithDocumentsAndSystems = alarmsLowLowCompliantWithDocumentsAndSystems + alarmsLowCompliantWithDocumentsAndSystems 
                + alarmsHighCompliantWithDocumentsAndSystems + alarmsHighHighCompliantWithDocumentsAndSystems;
            totalAlarmsCompliantWithDocuments = alarmsLowLowCompliantWithDocuments + alarmsLowCompliantWithDocuments 
                + alarmsHighCompliantWithDocuments + alarmsHighHighCompliantWithDocuments;
            totalAlarmsCompliantWithSystems = alarmsLowLowCompliantWithSystems + alarmsLowCompliantWithSystems 
                + alarmsHighCompliantWithSystems + alarmsHighHighCompliantWithSystems;
            totalAlarmsNonCompliant = alarmsLowLowNonCompliant + alarmsLowNonCompliant
                + alarmsHighNonCompliant + alarmsHighHighNonCompliant;
                
            // Trigger an event which determines that summary calculation is complete:
            CustomEvent summaryCalculatedEvent = new CustomEvent(new Object());
            events.trigger(Event.SUMMARY_CALCULATED, summaryCalculatedEvent);
        }// customEventOccurred
    }// _LoopsTableUpdateEventHandler
     
    
    /**
     * Determines conformity status of chosen SPI LL alarm against LL alarms 
     * from other data sources within given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Chosen SPI LL alarm conformity status
     */
    private static Enum _getLoopAlarmsLowLowComplanceType(SettingsSelector loop)
    {
        // If setting wasn't chosen or its conformity is below minimum, consider it as non-conformed:
        if (loop.getChosenIntoolsAlarmLL() == null 
            || loop.getIntoolsAlarmLLConformity() <= SEMI_CONFORMITY_MIN) 
                return _Confirmed.NON;
   
        // If setting has full conformity level, consider it as conformed against both documents and systems:
        if (loop.getIntoolsAlarmLLConformity() > FULL_CONFORMITY_MIN) return _Confirmed.FULLY;
        
        // If setting is half-conformed, same as chosen document LL alarm, consider it confirmed against documents:
        if (loop.getChosenDocumentsAlarmLL() != null 
            && loop.getIntoolsAlarmLLConformity() > SEMI_CONFORMITY_MIN
            && loop.getDocumentsAlarmLLConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_DOCUMENTS;
        
        // If setting is half-conformed, same as chosen systems LL alarm, consider it confirmed against systems:
        if (loop.getChosenSystemsAlarmLL() != null 
            && loop.getIntoolsAlarmLLConformity() > SEMI_CONFORMITY_MIN
            && loop.getSystemsAlarmLLConformity() > SEMI_CONFORMITY_MIN)
                return _Confirmed.AGAINST_SYSTEMS;
    
        return _Confirmed.NON;
    }// _getLoopAlarmsLowLowComplanceType
    
    
    /**
     * Determines conformity status of chosen SPI L alarm against L alarms from 
     * other data sources within given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Chosen SPI L alarm conformity status
     */
    private static Enum _getLoopAlarmsLowComplanceType(SettingsSelector loop)
    {
        // If setting wasn't chosen or its conformity is below minimum, consider it as non-conformed:
        if (loop.getChosenIntoolsAlarmL() == null 
            || loop.getIntoolsAlarmLConformity() <= SEMI_CONFORMITY_MIN) 
                return _Confirmed.NON;
   
        // If setting has full conformity level, consider it as conformed against both documents and systems:
        if (loop.getIntoolsAlarmLConformity() > FULL_CONFORMITY_MIN) return _Confirmed.FULLY;
        
        // If setting is half-conformed, same as chosen document L alarm, consider it confirmed against documents:
        if (loop.getChosenDocumentsAlarmL() != null 
            && loop.getIntoolsAlarmLConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmLConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_DOCUMENTS;
        
        // If setting is half-conformed, same as chosen systems L alarm, consider it confirmed against systems:
        if (loop.getChosenSystemsAlarmL() != null 
            && loop.getIntoolsAlarmLConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmLConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_SYSTEMS;
    
        return _Confirmed.NON;
    }// _getLoopAlarmsLowComplanceType
    
    
    /**
     * Determines conformity status of chosen SPI H alarm against H alarms from 
     * other data sources within given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Chosen SPI H alarm conformity status
     */
    private static Enum _getLoopAlarmsHighComplanceType(SettingsSelector loop)
    {
        // If setting wasn't chosen or its conformity is below minimum, consider it as non-conformed:
        if (loop.getChosenIntoolsAlarmH() == null 
            || loop.getIntoolsAlarmHConformity() <= SEMI_CONFORMITY_MIN) 
                return _Confirmed.NON;
   
        // If setting has full conformity level, consider it as conformed against both documents and systems:
        if (loop.getIntoolsAlarmHConformity() > FULL_CONFORMITY_MIN) return _Confirmed.FULLY;
        
        // If setting is half-conformed, same as chosen document H alarm, consider it confirmed against documents:
        if (loop.getChosenDocumentsAlarmH() != null 
            && loop.getIntoolsAlarmHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmHConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_DOCUMENTS;
        
        // If setting is half-conformed, same as chosen systems H alarm, consider it confirmed against systems:
        if (loop.getChosenSystemsAlarmH() != null 
            && loop.getIntoolsAlarmHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmHConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_SYSTEMS;
    
        return _Confirmed.NON;
    }// _getLoopAlarmsHighComplanceType
    
    
    /**
     * Determines conformity status of chosen SPI HH alarm against HH alarms from 
     * other data sources within given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return Chosen SPI HH alarm conformity status
     */
    private static Enum _getLoopAlarmsHighHighComplanceType(SettingsSelector loop)
    {
        // If setting wasn't chosen or its conformity is below minimum, consider it as non-conformed:
        if (loop.getChosenIntoolsAlarmHH() == null
            || loop.getIntoolsAlarmHHConformity() <= SEMI_CONFORMITY_MIN) 
                return _Confirmed.NON;
   
        // If setting has full conformity level, consider it as conformed against both documents and systems:
        if (loop.getIntoolsAlarmHHConformity() > FULL_CONFORMITY_MIN) return _Confirmed.FULLY;
        
        // If setting is half-conformed, same as chosen document HH alarm, consider it confirmed against documents:
        if (loop.getChosenDocumentsAlarmHH() != null 
            && loop.getIntoolsAlarmHHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getDocumentsAlarmHHConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_DOCUMENTS;
        
        // If setting is half-conformed, same as chosen systems HH alarm, consider it confirmed against systems:
        if (loop.getChosenSystemsAlarmHH() != null 
            && loop.getIntoolsAlarmHHConformity() > SEMI_CONFORMITY_MIN 
            && loop.getSystemsAlarmHHConformity() > SEMI_CONFORMITY_MIN) 
                return _Confirmed.AGAINST_SYSTEMS;
    
        return _Confirmed.NON;
    }// _getLoopAlarmsHighHighComplanceType
    

    /**
     * Returns calculated total count of alarms, confirmed against both 
     * documents and systems data.
     * 
     * @return Calculated count of alarms, confirmed against documents and systems data
     */
    public int getTotalAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.totalAlarmsCompliantWithDocumentsAndSystems;
    }// getTotalAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Returns calculated total count of alarms, confirmed against documents 
     * data.
     * 
     * @return Calculated count of alarms, confirmed against documents data
     */
    public int getTotalAlarmsCompliantWithDocuments()
    {
        return this.totalAlarmsCompliantWithDocuments;
    }// getTotalAlarmsCompliantWithDocuments
    
    
    /**
     * Returns calculated total count of alarms, confirmed against systems data.
     * 
     * @return Calculated count of alarms, confirmed against systems data
     */
    public int getTotalAlarmsCompliantWithSystems()
    {
        return this.totalAlarmsCompliantWithSystems;
    }// getTotalAlarmsCompliantWithSystems
    
    
    /**
     * Returns calculated total count of unconfirmed alarms.
     * 
     * @return Calculated count of unconfirmed alarms
     */
    public int getTotalNonCompliantAlarms()
    {
        return this.totalAlarmsNonCompliant;
    }// getTotalNonCompliantAlarms
    
    
    /**
     * Returns calculated count of LL alarms, confirmed against both 
     * documents and systems data.
     * 
     * @return Calculated of LL alarms, confirmed against documents and systems data
     */
    public int getLowLowAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsLowLowCompliantWithDocumentsAndSystems;
    }// getLowLowAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Returns calculated count of LL alarms, confirmed against documents 
     * data.
     * 
     * @return Calculated of LL alarms, confirmed against documents data
     */
    public int getLowLowAlarmsCompliantWithDocuments()
    {
        return this.alarmsLowLowCompliantWithDocuments;
    }// getLowLowAlarmsCompliantWithDocuments
    
    
    /**
     * Returns calculated count of LL alarms, confirmed against systems data.
     * 
     * @return Calculated of LL alarms, confirmed against systems data
     */
    public int getLowLowAlarmsCompliantWithSystems()
    {
        return this.alarmsLowLowCompliantWithSystems;
    }// getLowLowAlarmsCompliantWithSystems
    
    
    /**
     * Returns calculated count of unconfirmed LL alarms.
     * 
     * @return Calculated count of unconfirmed LL alarms
     */
    public int getLowLowNonCompliantAlarms()
    {
        return this.alarmsLowLowNonCompliant;
    }// getLowLowNonCompliantAlarms
    
    
    /**
     * Returns calculated count of L alarms, confirmed against both documents 
     * and systems data.
     * 
     * @return Calculated of L alarms, confirmed against documents and systems data
     */
    public int getLowAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsLowCompliantWithDocumentsAndSystems;
    }// getLowAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Returns calculated count of L alarms, confirmed against documents data.
     * 
     * @return Calculated of L alarms, confirmed against documents data
     */
    public int getLowAlarmsCompliantWithDocuments()
    {
        return this.alarmsLowCompliantWithDocuments;
    }// getLowAlarmsCompliantWithDocuments
    
    
    /**
     * Returns calculated count of L alarms, confirmed against systems data.
     * 
     * @return Calculated of L alarms, confirmed against systems data
     */
    public int getLowAlarmsCompliantWithSystems()
    {
        return this.alarmsLowCompliantWithSystems;
    }// getLowAlarmsCompliantWithSystems
    
    
    /**
     * Returns calculated count of unconfirmed L alarms.
     * 
     * @return Calculated count of unconfirmed L alarms
     */
    public int getLowNonCompliantAlarms()
    {
        return this.alarmsLowNonCompliant;
    }// getLowNonCompliantAlarms
    
    
    /**
     * Returns calculated count of H alarms, confirmed against both documents 
     * and systems data.
     * 
     * @return Calculated of H alarms, confirmed against documents and systems data
     */
    public int getHighAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsHighCompliantWithDocumentsAndSystems;
    }// getHighAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Returns calculated count of H alarms, confirmed against documents data.
     * 
     * @return Calculated of H alarms, confirmed against documents data
     */
    public int getHighAlarmsCompliantWithDocuments()
    {
        return this.alarmsHighCompliantWithDocuments;
    }// getHighAlarmsCompliantWithDocuments
    
    
    /**
     * Returns calculated count of H alarms, confirmed against systems data.
     * 
     * @return Calculated of H alarms, confirmed against systems data
     */
    public int getHighAlarmsCompliantWithSystems()
    {
        return this.alarmsHighCompliantWithSystems;
    }// getHighAlarmsCompliantWithSystems
    
    
    /**
     * Returns calculated count of unconfirmed H alarms.
     * 
     * @return Calculated count of unconfirmed H alarms
     */
    public int getHighNonCompliantAlarms()
    {
        return this.alarmsHighNonCompliant;
    }// getHighNonCompliantAlarms
    
    
    /**
     * Returns calculated count of HH alarms, confirmed against both documents 
     * and systems data.
     * 
     * @return Calculated of HH alarms, confirmed against documents and systems data
     */
    public int getHighHighAlarmsCompliantWithDocumentsAndSystems()
    {
        return this.alarmsHighHighCompliantWithDocumentsAndSystems;
    }// getHighHighAlarmsCompliantWithDocumentsAndSystems
    
    
    /**
     * Returns calculated count of HH alarms, confirmed documents data.
     * 
     * @return Calculated of HH alarms, confirmed against documents data
     */
    public int getHighHighAlarmsCompliantWithDocuments()
    {
        return this.alarmsHighHighCompliantWithDocuments;
    }// getHighHighAlarmsCompliantWithDocuments
    
    
    /**
     * Returns calculated count of HH alarms, confirmed against systems data.
     * 
     * @return Calculated of HH alarms, confirmed against systems data
     */
    public int getHighHighAlarmsCompliantWithSystems()
    {
        return this.alarmsHighHighCompliantWithSystems;
    }// getHighHighAlarmsCompliantWithSystems
    
    
    /**
     * Returns calculated count of unconfirmed HH alarms.
     * 
     * @return Calculated count of unconfirmed HH alarms
     */
    public int getHighHighNonCompliantAlarms()
    {
        return this.alarmsHighHighNonCompliant;
    }// getHighHighNonCompliantAlarms
}// IntoolsComplianceSummary
