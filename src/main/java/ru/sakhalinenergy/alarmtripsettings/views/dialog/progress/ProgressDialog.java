package ru.sakhalinenergy.alarmtripsettings.views.dialog.progress;

import java.awt.Component;
import javax.swing.SwingUtilities;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;


/**
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class ProgressDialog extends javax.swing.JDialog
{
    protected final ModelObservable model;
    protected final Enum progressEvent;
        
    
    /**
     * Public constructor.
     * 
     * @param model Model instance which progress events will be processed
     * @param progressEvent Model progress event key
     */
    public ProgressDialog(ModelObservable model, Enum progressEvent)
    {
        this.model = model;
        this.progressEvent = progressEvent;
                       
        setModal(true);
        setIconImage(Main.progressIcon.getImage());
    }// OneProgressBarDialog
    
    
    /**
     * Renders dialog with given title and relative to given component in the
     * end of EDT queue.
     * 
     * @param title Dialog title
     * @param parent Parent component
     */
    public void render(final String title, final Component parent)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setTitle(title);
                setLocationRelativeTo(parent);
                setVisible(true);
            }// run
        });// invokeLater
    }// render
    
    
    /**
     * Hides dialog in the end of EDT queue.
     */
    public void close()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setVisible(false);
            }// run
        });// invokeLater
    }// close
}// ProgressBarDialog
