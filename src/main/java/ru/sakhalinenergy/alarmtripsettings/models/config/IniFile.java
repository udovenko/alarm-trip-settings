package ru.sakhalinenergy.alarmtripsettings.models.config;

import java.io.File;
import java.io.IOException;
import org.ini4j.Wini;


/**
 * Implements configuration's file read/write operations.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public final class IniFile 
{
    private static final String INI_FILE_NAME = "config.ini";
            
    private static IniFile instance;
    private final Wini winiInstance;
    
    
    /**
     * Making constructor private to forbade new class instances.
     *
     * @throws IOException
     */
    private IniFile() throws IOException
    {
        // Get jar execution directory:
        String jarFilePath = IniFile.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String jarDirectory = new File(jarFilePath).getParent();
        
        // Get ".ini" file path: 
        String filePath = jarDirectory + File.separator + INI_FILE_NAME;
        filePath = filePath.replaceAll("%20"," "); 
        
        this.winiInstance =  new Wini(new File(filePath));
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