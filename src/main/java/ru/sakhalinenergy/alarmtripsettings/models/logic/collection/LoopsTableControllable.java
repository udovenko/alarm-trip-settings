package ru.sakhalinenergy.alarmtripsettings.models.logic.collection;

import java.util.List;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;


/**
 * Интерфейс для управления моделью таблицы контуров контроллером. Дополняет 
 * интерфейс для представлений доступом к сеттерам дополнительным ключам.
 * 
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public interface LoopsTableControllable extends LoopsTableObservable
{
     
    /**
     * Creates a thread for loop split. Copies given tags parent loop, attaches 
     * tags to copy and saves new loop.
     * 
     * @param tagsToSeparate Tags to separate into new loop
     */
    public void splitLoop(final List<Tag> tagsToSeparate);
    
}//LoopsTableControllable