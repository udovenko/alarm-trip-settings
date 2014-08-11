package ru.sakhalinenergy.alarmtripsettings.models.logic.summary;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.Model;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * Abstract parent for alarms compliance summary wrappers.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class Compliance extends Model
{
    // Model's events enumeration:
    public static enum Event
    {
        SUMMARY_CALCULATED
    }// Event
    
    
    protected final LoopsTableObservable loopsTable;
    
    
    /**
     * Public constructor. Subscribes model on wrapped loops table events.
     * 
     * @param loopsTable Loops table which overall compliance summary will be calculated
     */
    public Compliance(LoopsTableObservable loopsTable)
    {
        this.loopsTable = loopsTable;
        this.loopsTable.on(CollectionEvent.LOOPS_READ, _getLoopsTableUpdateEventHandler());
    }// OveralCompliance
    
    
    /**
     * Abstract factory method which returns instance of inner class - handler 
     * for for wrapped loops table data read event.
     * 
     * @return instance of inner class - handler for for wrapped loops table data read event
     */
    protected abstract CustomEventListener _getLoopsTableUpdateEventHandler();

    
    /**
     * Detects presence of chosen alarm LL in given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return True if loop has chosen LL alarm, else false
     */
    protected static Boolean _hasAlarmLowLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmLL() != null 
            || loop.getChosenDocumentsAlarmLL() != null 
            || loop.getChosenSystemsAlarmLL() != null);
    }// _hasAlarmLowLow
    
    
    /**
     * Detects presence of chosen alarm H in given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return True if loop has chosen H alarm, else false
     */
    protected static Boolean _hasAlarmHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmH() != null 
            || loop.getChosenDocumentsAlarmH() != null 
            || loop.getChosenSystemsAlarmH() != null);
    }// _hasAlarmHigh
    
    
    /**
     * Detects presence of chosen alarm HH in given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return True if loop has chosen HH alarm, else false
     */
    protected static Boolean _hasAlarmHighHigh(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmHH() != null 
            || loop.getChosenDocumentsAlarmHH() != null 
            || loop.getChosenSystemsAlarmHH() != null);
    }// _hasAlarmHighHigh
    
    
    /**
     * Detects presence of chosen alarm L in given loop.
     * 
     * @param loop Loop instance wrapped in settings selection logic
     * @return True if loop has chosen L alarm, else false
     */
    protected static Boolean _hasAlarmLow(SettingsSelector loop)
    {
        return (loop.getChosenIntoolsAlarmL() != null 
            || loop.getChosenDocumentsAlarmL() != null 
            || loop.getChosenSystemsAlarmL() != null);
    }// _hasAlarmLow
}// Summary
