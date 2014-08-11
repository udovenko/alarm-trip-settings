package ru.sakhalinenergy.alarmtripsettings.models.config;

import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;
import ru.sakhalinenergy.alarmtripsettings.Main;


/**
 * Implements configuration's file read/write operations.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public final class IniFile 
{
    private static final String INI_FILE_PATH = Main.JAR_DIR + File.separator + "config.ini";
            
    private static IniFile instance;
    private final Wini winiInstance;
    
    
    /**
     * Making constructor private to forbade new class instances.
     *
     * @throws IOException
     */
    private IniFile() throws IOException
    {
        this.winiInstance =  new Wini(new File(INI_FILE_PATH));
    }// IniFile
    
    
    /**
     * Returns reference on a single configuration file class instance. If
     * instance not created yet - creates it.
     *
     * @throws IOException
     * @return Reference to configuration file singleton object
     */
    public static IniFile getInstance() throws IOException
    {
        if (instance == null) 
        {
            instance = new IniFile();
        }// if
        
        return instance;
    }// getInstance
    
    
    /**
     * Retrieves given parameter's value from given configuration file section.
     * 
     * @param sectionName Name of configuration file section
     * @param settingName Parameter name
     * @return Received parameter value
     */
    public String get(String sectionName, String settingName)
    {
        return this.winiInstance.get(sectionName, settingName);
    }// get
    
    
    /**
     * Sets up given parameter's value in given configuration file section.
     * 
     * @param sectionName Name of configuration file section
     * @param settingName Parameter name
     * @param value Value to be set
     */
    public void put(String sectionName, String settingName, String value)
    {
        this.winiInstance.put(sectionName, settingName, value);
    }// set
    
    
    /**
     * Saves configuration changes to file.
     * 
     * @throws IOException
     */
    public void store() throws IOException
    {
        this.winiInstance.store();
    }// store
}// IniFile