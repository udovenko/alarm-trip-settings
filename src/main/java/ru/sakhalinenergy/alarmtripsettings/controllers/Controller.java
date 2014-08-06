package ru.sakhalinenergy.alarmtripsettings.controllers;

import java.io.File;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreePlant;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;


/**
 * Abstract superclass, implements common methods for all application 
 * controllers. 
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class Controller 
{
    
    /**
     * Extracts plant code from given plants tree node object.
     *  
     * @param plantsTreeObject Plants tree node object
     * @return Plant code string
     */
    protected String _extractPlantCode(Object plantsTreeObject)
    {
        // Choose model constructor depending on given node object class:
        if (plantsTreeObject.getClass().equals(TreePlant.class))
        {
            TreePlant plant = (TreePlant)plantsTreeObject;
            return plant.getId();
        } else if (plantsTreeObject.getClass().equals(TreeArea.class)) {
            
            TreeArea area = (TreeArea)plantsTreeObject;
            return area.getPlant();
        } else if (plantsTreeObject.getClass().equals(TreeUnit.class)) {
                
            TreeUnit unit = (TreeUnit)plantsTreeObject;
            return unit.getPlant();
        }// else if
        
        return null;
    }// _extractPlantCode
    
    
    /**
     * Copies given string into OS buffer. 
     *  
     * @param buffedString String to be copied into buffer
     */
    protected void _copyToClipboard(String buffedString)
    {
        StringSelection stringSelection = new StringSelection(buffedString);
        Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        clpbrd.setContents (stringSelection, null);
    }// _copyToClipboard

    
    /**
     * Opens given URI string in system's default browser.
     * 
     * @param link URI string
     */ 
    protected void _openUri(String link)
    {
        try
        {
            Desktop.getDesktop().browse(new URI(link));
        } catch (Exception exception){
        
            Object[] errorWrapper = {"URI open error", exception};
            CustomEvent errorEvent = new CustomEvent(errorWrapper);
            new ThreadErrorEventHandler().customEventOccurred(errorEvent);
        }// catch
    }// _openUri
    
    
    /**
     * Renders warning message.
     * 
     * @param message Message text
     * @param parent Parent component relative to which message will be rendered
     */
    protected void _handleWarning(String message, Component parent)
    {
        JOptionPane.showMessageDialog(parent, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }// handleWarning
    
    
    /**
     * Shows file selection dialog with given title and relative to given 
     * parent component.
     * 
     * @param title Dialog title
     * @param extensionsFilter File extensions filter for dialog
     * @param parent Parent component relative to which dialog will be positioned
     * @return Absolute path to selected file or null if dialog just closed
     */
    protected String _showSelectPathToFileDialog(String title, FileNameExtensionFilter extensionsFilter, Component parent)
    {
        assert (title != null) : "Dialog title is null!";
        assert (extensionsFilter != null) : "Extensions filter is null!";
        
        // Create a file selection dialog:
        String workingDirectory = System.getProperty("user.dir");
        final JFileChooser fileChooser = new JFileChooser(workingDirectory);
        fileChooser.setFileFilter(extensionsFilter);

        // Show created dialog with according to given parent and title:
        int dialog_result = fileChooser.showDialog(parent, title);

        // Handle dialog result:
        if (dialog_result == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            String filename = file.getAbsolutePath();

            return filename;
        }// if
        
        return null;
    }// _showSelectPathToFileDialog
        
    
    /**
     * Inner class - handler for models thread errors.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    public class ThreadErrorEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] exceptionWrapper = (Object[])event.getSource();
            String comment = (String)exceptionWrapper[0];
            Exception exception = (Exception)exceptionWrapper[1];
            JOptionPane.showMessageDialog(null, comment + ": " + exception, "Error!", JOptionPane.ERROR_MESSAGE);
        }// customEventOccurred
    }// ThreadErrorEventHandler
}// Controller
