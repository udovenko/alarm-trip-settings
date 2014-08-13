package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEventsObservable;


/**
 * Interface for automatic source creation dialogs, gives controllers access to
 * common events and events binding methods.
 *
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface AutomaticSourceCreationDialogObservable extends DialogWithEventsObservable
{
    
    /**
     * Returns selected plant.
     * 
     * @return Selected plant
     */
    public Plant getPlant();
    
    
    /**
     * Returns selected tag mask.
     * 
     * @return Selected tag mask
     */
    public TagMask getTagMask();
    
}// AutomaticSourceCreationDialogObservable
