package ru.sakhalinenergy.alarmtripsettings.models;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import java.util.HashMap;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;


/**
 * Абстрактный класс - предок, который наследуют все классы рабочих нитей
 * приложения.
 * 
 * @author Denis.Udovenko
 * @version 1.0.6
 */
public class WorkerThread extends SwingWorker<Object, Object> 
{
    
    public enum Event
    {    
        PROGRESS,
        WORK_DONE,
        WARNING,
        ERROR
    }// Event
    
    
    public Events events = new Events();
        
    
    /**
     * Метод ставит событие исключения в конец очереди EDT. В контексте события
     * передается комментарий и непосредственно экземпляр исключения, 
     * полученного в качестве параметра.
     * 
     * @param comment Комментарий к исключению
     * @param exception Экземпляр исключения
     * @param exceptionType Тип исключения потока (WARNING_EVENT или ERROR_EVENT)
     * 
     */
    protected void _invokeExceptionInEdt(final String comment, 
        final Throwable exception, final Enum exceptionType)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //Рассылаем исключение и комментарий подписчикам:
                Object[] exceptionWrapper = {comment, exception};
                CustomEvent myEvent = new CustomEvent(exceptionWrapper);
                events.trigger(exceptionType, myEvent);
            }//run
        });//invokeLater
    }//_invokeExceptionInEdt
            
    
    /**
     * Метод ставит событие исключения в конец очереди EDT и осанавливает 
     * текущий поток до момента окончания обработки события в EDT. В контексте 
     * события передается комментарий и непосредственно экземпляр исключения, 
     * полученного в качестве параметра.
     * 
     * @param comment Комментарий к исключению
     * @param exception Экземпляр исключения
     * @param exceptionType Тип исключения потока (WARNING_EVENT или ERROR_EVENT)
     * 
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
                    //Рассылаем исключение и комментарий подписчикам:
                    Object[] exceptionWrapper = {comment, exception};
                    CustomEvent myEvent = new CustomEvent(exceptionWrapper);
                    events.trigger(exceptionType, myEvent);
                }//run
            });//invokeAndWait
        } catch (Exception invocationException) {
        
            _invokeExceptionInEdt("Invoke in EDT and wait error", invocationException, Event.ERROR);
        }//catch
    }//_invokeEDTException
    
    
    /**
     * Метод - заглушка основного тела нити, выполняемого в отдельном потоке.
     *
     * @return  HashMap 
     */
    @Override
    protected HashMap doInBackground()
    {
        return new HashMap();
    }//doInBackground
    
    
    /**
     * Метод - заглушка обработчика публикаций тела нити.
     * 
     * @param   processInfo  Массив публикаций тела нити
     * @return  void
     */
    @Override
    protected void process(List<Object> processInfo)
    {
        for (int i=0; i<processInfo.size(); i++)
        {
            CustomEvent myEvent = new CustomEvent(processInfo.get(i));
            this.events.trigger(Event.PROGRESS, myEvent);
        }//for
    }//process
    
    
    /**
     * Метод - обработчик успешного оуончания работы нити. В случае успеха 
     * рассылает всем подписчикам событие окончания работы нити с контекстом 
     * результата.
     * 
     * @return  void
     */
    @Override
    public void done()
    {
        Object result = new Object();
        try {
            result = get();
        } catch (InterruptedException ignore) {
        
        } catch (java.util.concurrent.CancellationException e) {    
            
        } catch (java.util.concurrent.ExecutionException e) {
           
            String why = null;
            Throwable cause = e.getCause();
            if (cause != null)
            {
                why = cause.getMessage();
            } else {
                why = e.getMessage();
            }//else
            
            //Рассылаем текст ошибки подписчикам:
            CustomEvent myEvent = new CustomEvent(
                this.getClass().getName() 
                + " error getting thread result: " 
                +  why);
            this.events.trigger(Event.ERROR, myEvent);
        }//catch
         
        CustomEvent myEvent = new CustomEvent(result);
        this.events.trigger(Event.WORK_DONE, myEvent);
    }//done
}//WorkerThread