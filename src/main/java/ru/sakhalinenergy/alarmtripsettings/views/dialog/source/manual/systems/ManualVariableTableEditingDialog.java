package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.systems;

import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;
import ru.sakhalinenergy.alarmtripsettings.models.config.VariableTableDataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual.ManualSourceEditingDialog;


/**
 * Abstract parent for manual variable table editing dialog classes.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class ManualVariableTableEditingDialog extends ManualSourceEditingDialog
{
    
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Tags source wrapper
     * @param plants Plants list wrapper
     * @param tagMasks Wrapped masks collection for tag parsing
     * @param config Configuration instance
     * @param editMode New source / edit source mode flag
     * @param title Dialog title
     */
    ManualVariableTableEditingDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, VariableTableDataSourceDialogSettingsObservable config, 
        boolean editMode, String title) 
    {
        super(model, plants, tagMasks, config, editMode, title);
    }// ManualVariableTableEditingDialog
    
    
    /**
     * Restores dialog's settings from configuration object.
     * 
     * @param plantsComboBox Plants list combo box object
     * @param tagMasksComboBox Tag masks list combo box object
     * @param prioritySpinner Data source priority spinner object
     * @param nameTextField Data source name text field object
     * @param commentTextArea Data source comment text area object
     * @param backupDatePicker System's backup date picker object
     */
    protected void _applyConfig(JComboBox plantsComboBox, JComboBox tagMasksComboBox, 
        JSpinner prioritySpinner, JTextField nameTextField, JTextArea commentTextArea,
        JXDatePicker backupDatePicker)
    {
        
        // Call superclass configuration method:
        _applyConfig(plantsComboBox, tagMasksComboBox, prioritySpinner,
            nameTextField, commentTextArea);

        // Aplly rest of settings:
        if (editMode)
        {
            Source source = ((TagsSourceObservable)model).getEntity();
            backupDatePicker.setDate(source.getDate());

        } else {
            
            try
            {    
                // Cast models to concrete classes:
                VariableTableDataSourceDialogSettingsObservable castedConfig 
                    = (VariableTableDataSourceDialogSettingsObservable)config;
                Date date = this.dateFormat.parse(castedConfig.getBackupDate());
                backupDatePicker.setDate(date);
            } catch (Exception exception){}
        }// else
    }// _applyConfig
}// ManualVariableTableEditingDialog
