package ru.sakhalinenergy.alarmtripsettings.views.dialog;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;


/**
 * Abstract parent for all application dialogs.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class Dialog extends JDialog implements DialogObservable
{
    
    protected Events events = new Events();
    
    
    /**
     * Adds a new subscriber on given event type in subscribers list.
     * 
     * @param eventType Type of event we subscribing on
     * @param listener New subscriber instance
     */
    public void on(Enum eventType, CustomEventListener listener)
    {
        this.events.on(eventType, listener);
    }// on
    
    
    /**
     * Removes all subscribers for given event type.
     * 
     * @param eventType Type of event for which subscribers will be removed
     */
    public void off(Enum eventType)
    {
        this.events.off(eventType);
    }// off
    
    
    /**
     * Triggers custom events with given type on descendant object.
     * 
     * @param eventType Type id for event which will be triggered
     * @param event Custom event object
     */
    public void trigger(Enum eventType, CustomEvent event)
    {
        events.trigger(eventType, event);
    }//trigger
    
    
    /**
     * Puts dialog showing task to the end of EDT queue.
     */
    protected void _show()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setVisible(true);
            }// run
        });// invokeLater
    }// _show
    
    
    /**
     * Puts dialog hiding task to the end of EDT queue.
     */
    protected void _close()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setVisible(false);
            }// run
        });// invokeLater
    }// _close
}// Dialog
