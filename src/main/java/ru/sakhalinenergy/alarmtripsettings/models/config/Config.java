package ru.sakhalinenergy.alarmtripsettings.models.config;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.lang.reflect.Field;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.models.WorkerThread;
import ru.sakhalinenergy.alarmtripsettings.models.Model;


/**
 * An abstract parent for all configuration classes. Implements configuration 
 * save and load methods.
 *
 * @author Denis Udovenko
 * @version 1.0.6
 */
public abstract class Config extends Model
{

    protected Boolean hasChanged = true;    
    
    
    /**
     * Returns full class fields list including inherited fields up to Object
     * class.
     * 
     * @param ConfigClass Class which fields list will be received
     * @return Full list of given class fields
     */
    private static List<Field> _getEntryFields(Class ConfigClass)
    {
        List fields = new ArrayList(Arrays.asList(ConfigClass.getDeclaredFields()));
  
        // If ancestor class for current one exists, add its fields to list recursively:
        if (ConfigClass.getSuperclass() != null) fields.addAll(_getEntryFields(ConfigClass.getSuperclass()));
        
        return fields;
    }// _getEntryFields
    
       
    /**
     * Creates and executes thread for configuration data loading from file into
     * appropriate instance fields. Subscribes model events listeners on thread
     * events.
     */
    public void fetch()
    {
        // If no changes in config instance were made, do not load it twice:
        if (!hasChanged)
        { 
            CustomEvent loadedEvent = new CustomEvent(new Object());
            events.trigger(ConfigEvent.LOADED, loadedEvent);
            
            return;
        }// if

        // Create thread for data loading from configuration file: 
        WorkerThread settingsLoader = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                try
                {
                    // Get config singleton object for config file processing:
                    IniFile iniFile = IniFile.getInstance();
                    
                    // Avoid simultaneous config file access:
                    synchronized(iniFile)
                    {
                        // Get ini file section name field and make it accessible:
                        Field sectionName = Config.this.getClass().getDeclaredField("SECTION_NAME");
                        sectionName.setAccessible(true);
                    
                        // Find all instance fields with "@Entry" annotation and put their values to config:
                        for (Field field : _getEntryFields(Config.this.getClass()))
                        {   
                            field.setAccessible(true);
                            if (field.isAnnotationPresent(Entry.class)) 
                            {
                                field.set(Config.this, iniFile.get((String)sectionName.get(null), field.getName()));
                            }// if
                        }// for
                    }// synchronized
                   
                } catch (Exception exception){
            
                    _invokeExceptionInEdt("Config instace initializing error", exception, WorkerThread.Event.ERROR);
                }// catch
                
                // Set up actuality flag:
                hasChanged = false;
                return new HashMap();
            }// doInBackground
        };// settingsLoader
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(settingsLoader, ConfigEvent.THREAD_PROGRESS, 
            ConfigEvent.THREAD_WARNING, ConfigEvent.THREAD_ERROR, ConfigEvent.LOADED);
        
        // Execute thread:
        settingsLoader.execute();
    }// fetch
    
    
    /**
     * Creates and executes thread for configuration data saving to file from
     * appropriate instance fields. Subscribes model events listeners on thread
     * events.
     */
    public void save()
    {
        // If nothing was changed, do not save anything and just trigger success event:
        if (!hasChanged)
        { 
            CustomEvent loadedEvent = new CustomEvent(new Object());
            events.trigger(ConfigEvent.SAVED, loadedEvent);
            
            return;
        }// if

        // Create thread for data saving to configuration file: 
        WorkerThread settingsSaver = new WorkerThread()
        {
            @Override
            protected HashMap doInBackground()
            {
                try
                {
                    // Get config singleton object for config file processing:
                    IniFile iniFile = IniFile.getInstance();
                    
                    // Avoid simultaneous config file access:
                    synchronized(iniFile)
                    {
                         // Get ini file section name field and make it accessible:
                        Field sectionName = Config.this.getClass().getDeclaredField("SECTION_NAME");
                        sectionName.setAccessible(true);
                    
                        // Find all instance fields with "@Entry" annotation and retrieve their values from config:
                        for (Field field : _getEntryFields(Config.this.getClass()))
                        {
                            field.setAccessible(true);
                            if (field.isAnnotationPresent(Entry.class)) 
                            {
                                iniFile.put((String)sectionName.get(null), field.getName(), (String)field.get(Config.this));
                            }// if
                        }// for
                              
                        iniFile.store();
                    }// synchronized
                    
                } catch (final Exception exception) {
                    
                    _invokeExceptionInEdt("Config saving error", exception, WorkerThread.Event.ERROR);
                }//catch
                
                // Set up actuality flag:
                hasChanged = false;
                return new HashMap();
            }// doInBackground
        };// settingsLoader
        
        // Resubscribe model's events listeners on thread events:
        _subscribeOnThreadEvents(settingsSaver, ConfigEvent.THREAD_PROGRESS, 
            ConfigEvent.THREAD_WARNING, ConfigEvent.THREAD_ERROR, ConfigEvent.SAVED);
        
        // Execute thread:
        settingsSaver.execute();
    }// save
}// Config
