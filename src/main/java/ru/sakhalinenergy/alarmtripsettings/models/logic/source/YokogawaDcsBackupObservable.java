package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import java.util.List;


/**
 * An interface for using YokogawaDcsBackup logic by views. Allows only getters
 * and event identifiers.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public interface YokogawaDcsBackupObservable extends TagsSourceObservable
{
    
    /**
     * Returns current backup folder path.
     *
     * @return Backup folder path
     */
    public String getBackupFolderPath();
    
    
    /**
     * Returns stations collection.
     * 
     * @return Stations collection
     */
    public List<YgStationRecord> getStations();
    
}// YokogawaDcsBackupObservable