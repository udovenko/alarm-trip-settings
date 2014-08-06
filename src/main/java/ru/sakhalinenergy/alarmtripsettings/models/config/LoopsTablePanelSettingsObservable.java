package ru.sakhalinenergy.alarmtripsettings.models.config;


/**
 * Interface of loops table settings model for using by views. Allows only 
 * getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface LoopsTablePanelSettingsObservable
{
    
    /**
     * Returns selected loop identifier setting value.
     * 
     * @return Selected loop identifier
     */
    public String getSelectedLoop();
   
}// LoopsTablePanelSettingsObservable
