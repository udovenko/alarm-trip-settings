package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTable;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.TagsSourceObservable;
import ru.sakhalinenergy.alarmtripsettings.models.config.DataSourceDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.DataSourceDialog;


/**
 * Abstract parent for manual source editing dialogs classes.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public abstract class ManualSourceEditingDialog extends DataSourceDialog 
{
    protected final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    protected final boolean editMode;
    
    
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Wrapper model with data source creating logic
     * @param plants Plants collection
     * @param tagMasks Tag masks (formats) collection
     * @param config Dialog configuration object
     * @param editMode Create/edit source mode flag
     * @param title Dialog title
     */
    public ManualSourceEditingDialog(TagsSourceObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, DataSourceDialogSettingsObservable config, 
        boolean editMode, String title)
    {
        super(model, plants, tagMasks, config);
        
        this.editMode = editMode;
        setTitle(title);
    }// ManualSourceEditingDialog
    
    
    /**
     * Produces model's tags set update event handler instance for given tags 
     * table object.
     * 
     * @param sourceTagsTable Dialog's tags table object
     * @return Model's tags set update event handler
     */
    protected CustomEventListener _getModelTagSetUpdateHandler(final JTable sourceTagsTable)
    {
        return new CustomEventListener()
        {
            @Override
            public void customEventOccurred(CustomEvent evt)
            {
                TagsTableModel tagsTableModel = (TagsTableModel)sourceTagsTable.getModel();
                tagsTableModel.fireTableDataChanged();
            }// customEventOccurred
        };// CustomEventListener
    }// _getModelTagSetUpdateHandler
    
    
    /**
     * Restores dialog's settings from configuration object.
     * 
     * @param plantsComboBox Plants list combo box object
     * @param tagMasksComboBox Tag masks list combo box object
     * @param prioritySpinner Data source priority spinner object
     * @param nameTextField Data source name text field object
     * @param commentTextArea Data source comment text area object
     */
    protected void _applyConfig(JComboBox plantsComboBox, JComboBox tagMasksComboBox, 
        JSpinner prioritySpinner, JTextField nameTextField, JTextArea commentTextArea)
    {
        // Call superclass configuration method:
        _applyConfig(plantsComboBox, tagMasksComboBox);

        // Cast models to concrete classes:
        DataSourceDialogSettingsObservable castedConfig = (DataSourceDialogSettingsObservable)config;
        TagsSourceObservable castedModel = (TagsSourceObservable)model;
        
        // Restore dialog size and position:
        setSize(Integer.parseInt(castedConfig.getDialogWidth()), Integer.parseInt(castedConfig.getDialogHeight()));
        setLocation(Integer.parseInt(castedConfig.getDialogLeft()), Integer.parseInt(castedConfig.getDialogTop()));
        
        if (editMode)
        {
            Source source = castedModel.getEntity();
            prioritySpinner.setValue(source.getPriority());
            nameTextField.setText(source.getName());
            
            for (SourceProperty tempProperty : source.getProperties())
            {
                if (tempProperty.getTypeId() == SourcesPropertiesTypes.COMMENT.ID) commentTextArea.setText(tempProperty.getValue());
            }// for
     
        } else {
            
            nameTextField.setText(castedConfig.getDataSourceName());
            prioritySpinner.setValue(Integer.parseInt(castedConfig.getPriority()));
            commentTextArea.setText(castedConfig.getComment());
        }// else
    }// _applyConfig
    
    
    /**
     * Returns horizontal offset of dialog's left corner from screen initial
     * coordinates.
     *
     * @return Dialog's left offset
     */
    public String getDialogLeft()
    {
        return Integer.toString(getX());
    }// getDialogLeft    
    
    
    /**
     * Returns vertical offset of dialog's top corner from screen initial
     * coordinates.
     *
     * @return Dialog's top offset
     */
    public String getDialogTop()
    {
        return Integer.toString(getY());
    }// getDialogTop
    
    
    /**
     * Returns dialog's width.
     * 
     * @return Dialog's width
     */
    public String getDialogWidth()
    {
        return Integer.toString(getWidth());
    }// getDialogWidth
    
    
    /**
     * Returns dialog's height.
     * 
     * @return Dialog's height
     */
    public String getDialogHeight()
    {
        return Integer.toString(getHeight());
    }// getDialogHeigt
}// ManualSourceEditingDialog
