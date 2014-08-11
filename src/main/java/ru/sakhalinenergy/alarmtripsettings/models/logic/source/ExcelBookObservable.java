package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Interface of MS Excel book model for using by views. Allows only getters.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public interface ExcelBookObservable extends TagsSourceObservable
{
 
    /**
     * Returns current book path, if book is connected to provider.
     * 
     * @return Path to book or null if book is not connected
     */
    public String getBookFilePath();
        
    
    /**
     * Returns sheets and fields hash received from book.
     * 
     * @return Book sheets and fields hash
     */
    public HashMap getSheetsAndFields();
        
    
    /**
     * Returns current data source entity.
     * 
     * @return Data source entity
     */
    public Source getSource(); 

}// ExcelBookObservable
