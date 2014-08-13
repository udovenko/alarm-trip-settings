package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog;

import javax.swing.JComboBox;
import ru.sakhalinenergy.alarmtripsettings.models.config.PlantAndTagFormatSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;


/**
 * Abstract parent for all data source dialog classes.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public abstract class DataSourceDialog extends DialogWithEvents
{
    protected final ModelObservable model;
    protected final PlantsLogicObservable plants;
    protected final TagMasksObservable tagMasks;
    protected final PlantAndTagFormatSettingsObservable config;
    
    
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Wrapper model with data source creating logic
     * @param plants Plants collection
     * @param tagMasks Tag masks (formats) collection
     * @param config Dialog configuration object
     */
    protected DataSourceDialog(ModelObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, PlantAndTagFormatSettingsObservable config) 
    {
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
    }// DataSourceDialog
    
    
    /**
     * Builds plants list in given combo box object and restores plant selection
     * from dialog configuration object.
     * 
     * @param plantsComboBox Combo box object where plants list will be built
     */
    protected void _buildPlantsList(JComboBox plantsComboBox)
    {
        for (Plant tempPlant : plants.getPlants()) plantsComboBox.addItem(tempPlant);
        
        // Restore plant selection:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                plantsComboBox.setSelectedItem(tempPlant);
                break;
            }// if
        }// for
    }// _buildPlantsList
    
    
    /**
     * Builds tag masks list in given combo box object and restores tag mask
     * selection from dialog configuration object.
     * 
     * @param tagMasksComboBox Combo box object where tag masks list will be built
     */
    protected void _buildTagMasksList(JComboBox tagMasksComboBox)
    {
        // Build tag formats list:
        for (TagMask tempMask : tagMasks.getMasks()) tagMasksComboBox.addItem(tempMask);
        
        //Восстанавливаем выбранную маску тага:
        for (TagMask tempTagMask : this.tagMasks.getMasks())
        {    
            if (tempTagMask.getExample().equals(config.getTagFormat()))
            {    
                tagMasksComboBox.setSelectedItem(tempTagMask);
                break;
            }// if
        }// for
    }// _buildTagMasksList
}// DataSourceDialog
