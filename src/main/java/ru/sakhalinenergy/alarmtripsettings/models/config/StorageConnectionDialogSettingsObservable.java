package ru.sakhalinenergy.alarmtripsettings.models.config;

import ru.sakhalinenergy.alarmtripsettings.events.EventsObservable;


/**
 * Interface of storage connection settings model for using by views. Allows
 * only getters.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface StorageConnectionDialogSettingsObservable  extends EventsObservable
{
    
    /**
     * Returns used storage type name.
     * 
     * @return Used storage type name
     */
    public String getStorageType();
       

    /**
     * Returns MySQL storage host.
     * 
     * @return MySQL storage host
     */
    public String getMySqlHost();
        

    /**
     * Returns MySQL storage port.
     * 
     * @return MySQL storage port
     */
    public String getMySqlPort();
    
    
    /**
     * Returns MySQL storage database name.
     * 
     * @return MySQL storage database name
     */
    public String getMySqlDatabase();
    

    /**
     * Returns user name for MySQL storage connection.
     * 
     * @return User name for MySQL storage connection
     */
    public String getMySqlUser();
    
        
    /**
     * Returns password for MySQL storage connection.
     * 
     * @return Password for MySQL storage connection
     */
    public String getMySqlPassword();
    
    
    /**
     * Returns path to SQLite storage.
     * 
     * @return Path to SQLite storage
     */
    public String getSqliteDatabasePath();
    
}// StorageConnectionDialogSettingsObservable
