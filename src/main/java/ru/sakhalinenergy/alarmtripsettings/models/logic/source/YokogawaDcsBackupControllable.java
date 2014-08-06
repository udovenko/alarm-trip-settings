package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import java.util.List;


/**
 * Interface for using model by controllers. Extends observable interface by
 * adding setters and execution methods.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface YokogawaDcsBackupControllable extends TagsSourceControllable, YokogawaDcsBackupObservable
{
    
    /**
     * Sets backup folder path field value.
     * 
     * @param backupFolderPath Path to backup folder
     */
    public void setBackupFolderPath(String backupFolderPath);
    
    
    /**
     * Creates and launches thread for getting backup stations collection. 
     * Notifies subscribers about thread events. 
     */
    public void readStations();
    
    
    /**
     * Creates and launches a thread to read tags from found stations.
     * 
     * @param selectedStations List of stations instances references, from which tags will be read
     */
    public void readTags(List<YgStationRecord> selectedStations);
    
}//YokogawaDcsBackupControlable