package ru.sakhalinenergy.alarmtripsettings;

import java.io.File;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.factories.DialogsFactory;
import ru.sakhalinenergy.alarmtripsettings.models.config.ConfigEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.MainFormSettings;
import ru.sakhalinenergy.alarmtripsettings.views.form.MainForm;
import ru.sakhalinenergy.alarmtripsettings.controllers.MainFormController;


/**
 * Application's main class. Stores basic settings, constants and entry point 
 * method.
 * 
 * @author Denis Udovenko
 * @version 1.1.4
 */
public class Main 
{
    
    // Application's version information:
    public static final String VERSION = "2.1.2.8";
    public static final String LAST_CHANGES_DATE = "20.08.2014";
    public static final String LAST_CHANGES_TIME = "11:41";
    public static final String AUTHOR = "Denis Udovenko";
    
    // Application's .jar file directory:
    public static final String JAR_DIR = _getJarDirectory();
    
    // Application's temp directory:
    public static final String TEMP_DIR = Main.JAR_DIR + File.separator + "temp";
    
    // Images source directory:
    private static final String IMAGES_SOURCE_DIR = "/images";
    
    // Application's icons:
    public static final ImageIcon 
        alarmTripIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/alarm_trip.png")),
        intoolsIcon           = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/intools.png")),
        documentsIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/documents.png")),
        dcsIcon               = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/dcs.png")),
        addDcsSourceIcon      = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/add_dcs_source.png")),
        esdIcon               = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/esd.png")),
        fgsIcon               = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/fgs.png")),
        tagIcon               = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/tag.jpg")),
        settingIcon           = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/setting.jpg")),
        splitLoopIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/split_loop.png")),
        mergeLoopIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/merge_loop.png")),
        mergeAllLoopsIcon     = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/merge_all_loops.png")),
        addSourceIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/add_source.png")),
        editIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/edit.png")),
        removeIcon            = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/remove.png")),
        priorityIcon          = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/priority.png")),
        linkIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/link.png")),
        copyIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/copy.png")),
        dumpIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/dump.png")),
        storageConnectionIcon = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/storage_connection.png")),
        updateSpiIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/update_spi.png")),
        roundingIcon          = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/rounding.png")),
        tableErrorIcon        = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/table_error.png")),
        aboutIcon             = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/about.png")),
        loopIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/loop.png")),
        systemsIcon           = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/systems.png")),
        progressIcon          = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/progress.png")),
        yokogawaIcon          = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/yokogawa.png")),
        honeywellIcon         = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/honeywell.png")),
        excelIcon             = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/excel.png")),
        plantIcon             = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/plant.jpg")),
        areaIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/area.jpg")),
        unitIcon              = new ImageIcon(Main.class.getResource(IMAGES_SOURCE_DIR + "/unit.jpg"));
    
    // Application's images:
    public static final Image 
        sakhalinEnergyLogoImage = Toolkit.getDefaultToolkit().createImage(Main.class.getResource(IMAGES_SOURCE_DIR + "/logo.png"));
        
    // Application's main form:
    public static MainForm mainForm;

    
    /**
     * Application's entry point method. Loads configuration for main from and
     * shows it. Launches connection to storage.
     * 
     * @param args[] Command line parameters
     */
    public static void main(String args[])
    {
        // Set up interface look and feel:
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        // Set callback for main form settngs load event:
        final MainFormSettings mainFormSettings = MainFormSettings.getInstance();
        mainFormSettings.off(ConfigEvent.LOADED);
        mainFormSettings.on(ConfigEvent.LOADED, new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {        
                // Create application's main form:
                mainForm = new MainForm(mainFormSettings);
                new MainFormController(mainForm);

                mainForm.render();

                // Initiate storage connection:
                DialogsFactory.produceStorageConnectionDialog(true);
            }// customEventOccurred
        });// on
        
        // Load main form settings:
        mainFormSettings.fetch();
    }// main
    
    
    /**
     * Returns application .jar file's directory.
     * 
     * @return Application .jar file's directory
     */
    private static String _getJarDirectory()
    {
        // Get jar execution directory:
        String jarFilePath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String jarDirectory = new File(jarFilePath).getParent();
                
        return jarDirectory.replaceAll("%20"," "); 
    }// _getJarDirectory 
}// Main
