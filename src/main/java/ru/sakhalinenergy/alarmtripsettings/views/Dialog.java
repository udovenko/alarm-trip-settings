package ru.sakhalinenergy.alarmtripsettings.views;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;


/**
 * Abstract parent for all application dialogs.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class Dialog extends JDialog
{
    
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
