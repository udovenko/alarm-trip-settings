package ru.sakhalinenergy.alarmtripsettings.models.logic.source;

import java.sql.SQLException;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;


/**
 * Interface of MS Excel book model for using by controllers.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public interface ExcelBookControlable extends TagsSourceControllable
{
  
    /**
     * Disconnects MS Excel data provider form book.
     * 
     * @throws SQLException
     */
    public void disconnect() throws SQLException;
        
    
    /**
     * Returns MS Excel book connection status.
     * 
     * @throws SQLException
     * @return True, if connection is open, else false 
     */
    public boolean isConnected() throws SQLException;

    
    /**
     * Creates a thread for connecting provider to MS Excel book and reading 
     * sheets and fields hash. Subscribes models events listeners on thread 
     * events and executes it.
     * 
     * @param filePath Path to MS Excel book file
     */
    public void connectAndReadStructure(final String filePath);
    
    
    /**
     * Creates a thread for reading tags from connected MS Excel book sheet. 
     * Subscribes models events listeners on thread events and executes it.
     * 
     * @param tagMask Tag mask which will be used for processing tags in selected sheet
     */
    public void readTags(final TagMask tagMask);
    
}// ExcelBookControlable
