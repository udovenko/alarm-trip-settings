package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;

import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.AutomaticSourceCreationDialogObservable;


/**
 * An interface for create DCS Variable Table from Yokogawa backup dialog for
 * using it by controllers. Allows only getters.
 *
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public interface CreateDcsVariableTableFromYokogawaBackupDialogObservable extends AutomaticSourceCreationDialogObservable
{
       
    /**
     * Метод возвращает список выбранных в дереве тагов, которые необходимо
     * вынести в отдельную копию петли.
     * 
     * @return Коллекцию выбранных станций 
     */
    public List<YgStationRecord> getSelectedStations();
    
}//CreateDcsVariableTableFromYokogawaBackupDialogObservable
