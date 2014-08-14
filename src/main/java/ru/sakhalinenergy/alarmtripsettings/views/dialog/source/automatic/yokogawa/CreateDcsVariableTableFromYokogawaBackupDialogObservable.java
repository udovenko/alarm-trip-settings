package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.automatic.yokogawa.AutomaticSourceCreationDialogObservable;


/**
 * Interface of "Create DCS Variable Table from Yokogawa backup" dialog for
 * using by controllers. Allows only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface CreateDcsVariableTableFromYokogawaBackupDialogObservable extends AutomaticSourceCreationDialogObservable
{
       
    /**
     * Returns list of stations selected to be parsed during data source 
     * creating.
     * 
     * @return Selected stations list
     */
    public List<YgStationRecord> getSelectedStations();
    
}// CreateDcsVariableTableFromYokogawaBackupDialogObservable
