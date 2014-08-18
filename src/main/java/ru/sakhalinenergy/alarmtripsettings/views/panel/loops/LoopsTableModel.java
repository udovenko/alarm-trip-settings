package ru.sakhalinenergy.alarmtripsettings.views.panel.loops;

import java.util.List;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;


/**
 * Implements loops table model.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class LoopsTableModel extends AbstractTableModel
{
    private final List<SettingsSelector> wrapedLoops;
        
    public final String LOOP_COLUMN_NAME                = "Loop:";
    
    public final String SPI_LOW_LOW_COLUMN_NAME         = "SPI LL:";
    public final String DOCUMENTS_LOW_LOW_COLUMN_NAME   = "Doc LL:";
    public final String SYSTEMS_LOW_LOW_COLUMN_NAME     = "Sys LL:";
    
    public final String SPI_LOW_COLUMN_NAME             = "SPI L:";
    public final String DOCUMENTS_LOW_COLUMN_NAME       = "Doc L:";
    public final String SYSTEMS_LOW_COLUMN_NAME         = "Sys L:";
    
    public final String SPI_HIGH_COLUMN_NAME            = "SPI H:";
    public final String DOCUMENTS_HIGH_COLUMN_NAME      = "Doc H:";
    public final String SYSTEMS_HIGH_COLUMN_NAME        = "Sys H:";
    
    public final String SPI_HIGH_HIGH_COLUMN_NAME       = "SPI HH:";
    public final String DOCUMENTS_HIGH_HIGH_COLUMN_NAME = "Doc HH:";
    public final String SYSTEMS_HIGH_HIGH_COLUMN_NAME   = "Sys HH:";
    
    private final List<String> fields = Arrays.asList(LOOP_COLUMN_NAME,
        SPI_LOW_LOW_COLUMN_NAME, DOCUMENTS_LOW_LOW_COLUMN_NAME, SYSTEMS_LOW_LOW_COLUMN_NAME, 
        SPI_LOW_COLUMN_NAME, DOCUMENTS_LOW_COLUMN_NAME, SYSTEMS_LOW_COLUMN_NAME,
        SPI_HIGH_COLUMN_NAME, DOCUMENTS_HIGH_COLUMN_NAME, SYSTEMS_HIGH_COLUMN_NAME,
        SPI_HIGH_HIGH_COLUMN_NAME, DOCUMENTS_HIGH_HIGH_COLUMN_NAME, SYSTEMS_HIGH_HIGH_COLUMN_NAME);
    
       
    /**
     * Public constructor. Sets wrapped loops list.
     * 
     * @param wrapedLoops Wrapped loops list to be displayed in table
     */
    public LoopsTableModel(List<SettingsSelector> wrapedLoops)
    {
        this.wrapedLoops = wrapedLoops;
    }// LoopsTableModel
    
    
    /**
     * Returns table columns count.
     * 
     * @return Table columns count
     */
    @Override
    public int getColumnCount()
    {
        return fields.size();
    }// getColumnCount
    
    
    /**
     * Returns table rows count.
     * 
     * @return Table rows count
     */
    @Override
    public int getRowCount() 
    { 
        return wrapedLoops.size();
    }// getRowCount
    
    
    /**
     * Returns column name by given column index.
     * 
     * @param col Column index
     * @return Column name
     */
    @Override
    public String getColumnName(int col) 
    {
        return fields.get(col);
    }// getColumnName
     
    
    /**
     * Returns cell value for given row and column indexes.
     * 
     * @param row Row index
     * @param col Column index
     * @return Cell value
     */
    @Override
    public Object getValueAt(int row, int col) 
    {
        Object result = null; 
        SettingsSelector currentWraper = wrapedLoops.get(row);
        
        if (fields.get(col).equals(LOOP_COLUMN_NAME)) result = currentWraper.getEntity().toString();
                
        if (fields.get(col).equals(SPI_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmLL() != null ? currentWraper.getChosenIntoolsAlarmLL().getValue() : ""; 
        if (fields.get(col).equals(DOCUMENTS_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmLL() != null ? currentWraper.getChosenDocumentsAlarmLL().getValue() : "";
        if (fields.get(col).equals(SYSTEMS_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmLL() != null ? currentWraper.getChosenSystemsAlarmLL().getValue() : "";
        
        if (fields.get(col).equals(SPI_LOW_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmL() != null ? currentWraper.getChosenIntoolsAlarmL().getValue() : ""; 
        if (fields.get(col).equals(DOCUMENTS_LOW_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmL() != null ? currentWraper.getChosenDocumentsAlarmL().getValue() : "";
        if (fields.get(col).equals(SYSTEMS_LOW_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmL() != null ? currentWraper.getChosenSystemsAlarmL().getValue() : "";
        
        if (fields.get(col).equals(SPI_HIGH_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmH() != null ? currentWraper.getChosenIntoolsAlarmH().getValue() : ""; 
        if (fields.get(col).equals(DOCUMENTS_HIGH_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmH() != null ? currentWraper.getChosenDocumentsAlarmH().getValue() : "";
        if (fields.get(col).equals(SYSTEMS_HIGH_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmH() != null ? currentWraper.getChosenSystemsAlarmH().getValue() : "";
        
        if (fields.get(col).equals(SPI_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmHH() != null ? currentWraper.getChosenIntoolsAlarmHH().getValue() : ""; 
        if (fields.get(col).equals(DOCUMENTS_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmHH() != null ? currentWraper.getChosenDocumentsAlarmHH().getValue() : "";
        if (fields.get(col).equals(SYSTEMS_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmHH() != null ? currentWraper.getChosenSystemsAlarmHH().getValue() : "";
        
        return result;
    }// getValueAt
}// LoopsTableModel
