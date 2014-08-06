package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Implements storage connection settings model.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class StorageConnectionDialogSettings extends Config implements StorageConnectionDialogSettingsObservable
{
    private static StorageConnectionDialogSettings instance;
    private static final String SECTION_NAME = "storageConnection";
    
    @Entry private String storageType;
    @Entry private String mySqlHost;
    @Entry private String mySqlPort;
    @Entry private String mySqlDatabase;
    @Entry private String mySqlUser;
    @Entry private String mySqlPassword;
    @Entry private String sqliteDatabasePath;
    
    
    /**
     * Making constructor private to forbade new class instances.
     */
    private StorageConnectionDialogSettings(){}
    
    
    /**
     * Returns reference on a single class instance of storage connection 
     * settings. If instance not created yet - creates it.
     *
     * @return Reference to storage connection settings singleton object
     */
    public static StorageConnectionDialogSettings getInstance()
    {
        if (instance == null) 
        {
            instance = new StorageConnectionDialogSettings();
        }// if
        
        return instance;
    }// getInstance

    
    /**
     * Returns used storage type name.
     * 
     * @return Used storage type name
     */
    @Override
    public String getStorageType() 
    {
        return storageType;
    }// getStorageType

    
    /**
     * Sets up used storage type name.
     * 
     * @param storageType Used storage type name
     */
    public void setStorageType(String storageType)
    {
        this.storageType = storageType;
        hasChanged = true;
    }// setStorageType
    

    /**
     * Returns MySQL storage host.
     * 
     * @return MySQL storage host
     */
    @Override
    public String getMySqlHost() 
    {
        return mySqlHost;
    }// getMySqlHost

    
    /**
     * Sets up MySQL storage host.
     * 
     * @param mySqlHost MySQL storage host
     */
    public void setMySqlHost(String mySqlHost) 
    {
        this.mySqlHost = mySqlHost;
        hasChanged = true;
    }// setMySqlHost
    

    /**
     * Returns MySQL storage port.
     * 
     * @return MySQL storage port
     */
    @Override
    public String getMySqlPort() 
    {
        return mySqlPort;
    }// getMySqlPort
    

    /**
     * Sets up MySQL storage port.
     * 
     * @param mySqlPort MySQL storage port
     */
    public void setMySqlPort(String mySqlPort) 
    {
        this.mySqlPort = mySqlPort;
        hasChanged = true;
    }// setMySqlPort

    
    /**
     * Returns MySQL storage database name.
     * 
     * @return MySQL storage database name
     */
    @Override
    public String getMySqlDatabase() 
    {
        return mySqlDatabase;
    }// getMySqlDatabase
    

    /**
     * Sets up MySQL storage database name.
     * 
     * @param mySqlDatabase MySQL storage database name
     */
    public void setMySqlDatabase(String mySqlDatabase) 
    {
        this.mySqlDatabase = mySqlDatabase;
        hasChanged = true;
    }// setMySqlDatabase

    
    /**
     * Returns user name for MySQL storage connection.
     * 
     * @return User name for MySQL storage connection
     */
    @Override
    public String getMySqlUser() 
    {
        return mySqlUser;
    }// getMySqlUser
    
    
    /**
     * Sets up user name for MySQL storage connection.
     * 
     * @param mySqlUser User name for MySQL storage connection
     */
    public void setMySqlUser(String mySqlUser) 
    {
        this.mySqlUser = mySqlUser;
        hasChanged = true;
    }// setMySqlUser
    

    /**
     * Returns password for MySQL storage connection.
     * 
     * @return Password for MySQL storage connection
     */
    @Override
    public String getMySqlPassword() 
    {
        return mySqlPassword;
    }// getMySqlPassword
    

    /**
     * Sets up password for MySQL storage connection.
     * 
     * @param mySqlPassword Password for MySQL storage connection
     */
    public void setMySqlPassword(String mySqlPassword) 
    {
        this.mySqlPassword = mySqlPassword;
        hasChanged = true;
    }// setMySqlPassword
    

    /**
     * Returns path to SQLite storage.
     * 
     * @return Path to SQLite storage
     */
    @Override
    public String getSqliteDatabasePath() 
    {
        return sqliteDatabasePath;
    }// getSqliteDatabasePath

    
    /**
     * Sets up path to SQLite storage.
     * 
     * @param sqliteDatabasePath Path to SQLite storage
     */
    public void setSqliteDatabasePath(String sqliteDatabasePath) 
    {
        this.sqliteDatabasePath = sqliteDatabasePath;
        hasChanged = true;
    }// setSqliteDatabasePath
}// StorageConnectionDialogSettings
