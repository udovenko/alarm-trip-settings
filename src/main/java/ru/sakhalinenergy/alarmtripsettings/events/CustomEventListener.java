package ru.sakhalinenergy.alarmtripsettings.events;

import java.util.EventListener;


/**
 * Interface for custom event listener classes.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface CustomEventListener extends EventListener 
{
    public void customEventOccurred(CustomEvent evt);
}// CustomEventListener
