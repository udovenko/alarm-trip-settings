package ru.sakhalinenergy.alarmtripsettings.models;

import java.util.HashMap;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;


/**
 * Abstract parent for models threads. Extends SwingWorker thread
 * implementation as most suitable for application.
 * 
 * @author Denis Udovenko
 * @version 1.0.6
 */
public abstract class WorkerThread extends SwingWorker<Object, Object> 
{
    
    // Common thread events enumeration:
    public enum Event
    {    
        PROGRESS,
        WORK_DONE,
        WARNING,
        ERROR
    }// Event
    
    
    public Events events = new Events();
        
    
    /**
     * Puts exception event object to the end of EDT queue. Passes custom 
     * exception comment and exception object itself as event object's context.
     * 
     * @param comment Exception comment
     * @param exception Exception object
     * @param exceptionType Thread exception type identifier (Event.WARNING or Event.ERROR)
     */
    protected void _invokeExceptionInEdt(final String comment, 
        final Throwable exception, final Enum exceptionType)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                // Trigger an event with exception data:
                Object[] exceptionWrapper = {comment, exception};
                CustomEvent myEvent = new CustomEvent(exceptionWrapper);
                events.trigger(exceptionType, myEvent);
            }// run
        });// invokeLater
    }// _invokeExceptionInEdt
            
    
    /**
     * Puts exception event object to the end of EDT queue and stops thread 
     * execution until event handling is finished in EDT. Passes custom 
     * exception comment and exception object itself as event object's context.
     * 
     * @param comment Exception comment
     * @param exception Exception object
     * @param exceptionType Thread exception type identifier (Event.WARNING or Event.ERROR)
     */
    protected void _invokeExceptionInEdtAndWait(final String comment, 
        final Throwable exception, final Enum exceptionType)
    {
        try
        {
            SwingUtilities.invokeAndWait(new Runnable()
            {
                @Override
                public void run()
                {
                    // Trigger an event with exception data:
                    Object[] exceptionWrapper = {comment, exception};
                    CustomEvent myEvent = new CustomEvent(exceptionWrapper);
                    events.trigger(exceptionType, myEvent);
                }// run
            });// invokeAndWait
        } catch (Exception invocationException) {
        
            _invokeExceptionInEdt("Invoke in EDT and wait error", invocationException, Event.ERROR);
        }// catch
    }// _invokeEDTException
    
    
    /**
     * Dummy thread body method, should be overridden in descendant classes.
     *
     * @return Hash with any required information
     */
    @Override
    protected HashMap doInBackground()
    {
        return new HashMap();
    }// doInBackground
    
    
    /**
     * Handles progress publishing from thread body method.
     * 
     * @param processInfo Array of published information from thread body
     */
    @Override
    protected void process(List<Object> processInfo)
    {
        for (int i=0; i<processInfo.size(); i++)
        {
            CustomEvent myEvent = new CustomEvent(processInfo.get(i));
            this.events.trigger(Event.PROGRESS, myEvent);
        }// for
    }// process
    
    
    /**
     * Handles thread body method's work result. Triggers thread work done event
     * with outcome context for all subscribers.
     */
    @Override
    public void done()
    {
        Object result = new Object();
        
        try // Try to get thread body method's result:
        {
            result = get();
        } catch (InterruptedException exception) {
        
        } catch (java.util.concurrent.CancellationException exception) {    
            
        } catch (java.util.concurrent.ExecutionException exception) {
           
            String why = null;
            Throwable cause = exception.getCause();
            if (cause != null)
            {
                why = cause.getMessage();
            } else {
                why = exception.getMessage();
            }// else
            
            // Trigger error event for all subscribers:
            CustomEvent myEvent = new CustomEvent(
                this.getClass().getName() 
                + " error getting thread result: " 
                +  why);
            this.events.trigger(Event.ERROR, myEvent);
        }// catch
         
        CustomEvent myEvent = new CustomEvent(result);
        this.events.trigger(Event.WORK_DONE, myEvent);
    }// done
}// WorkerThread