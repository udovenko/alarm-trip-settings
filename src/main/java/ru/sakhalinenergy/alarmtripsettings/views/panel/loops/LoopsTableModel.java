package ru.sakhalinenergy.alarmtripsettings.views.panel.loops;

import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import java.util.List;
import java.util.Arrays;
import javax.swing.table.AbstractTableModel;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;


/**
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class LoopsTableModel extends AbstractTableModel
{
    private final List<SettingsSelector> wrapedLoops;
        
    public final String LOOP_COLUMN_NAME = "Loop:";
    
    public final String SPI_LOW_LOW_COLUMN_NAME = "SPI LL:";
    public final String DOCUMENTS_LOW_LOW_COLUMN_NAME = "Doc LL:";
    public final String SYSTEMS_LOW_LOW_COLUMN_NAME = "Sys LL:";
    
    public final String SPI_LOW_COLUMN_NAME = "SPI L:";
    public final String DOCUMENTS_LOW_COLUMN_NAME = "Doc L:";
    public final String SYSTEMS_LOW_COLUMN_NAME = "Sys L:";
    
    public final String SPI_HIGH_COLUMN_NAME = "SPI H:";
    public final String DOCUMENTS_HIGH_COLUMN_NAME = "Doc H:";
    public final String SYSTEMS_HIGH_COLUMN_NAME = "Sys H:";
    
    public final String SPI_HIGH_HIGH_COLUMN_NAME = "SPI HH:";
    public final String DOCUMENTS_HIGH_HIGH_COLUMN_NAME = "Doc HH:";
    public final String SYSTEMS_HIGH_HIGH_COLUMN_NAME = "Sys HH:";
    
    private  List<String> fields = Arrays.asList(LOOP_COLUMN_NAME,
        SPI_LOW_LOW_COLUMN_NAME, DOCUMENTS_LOW_LOW_COLUMN_NAME, SYSTEMS_LOW_LOW_COLUMN_NAME, 
        SPI_LOW_COLUMN_NAME, DOCUMENTS_LOW_COLUMN_NAME, SYSTEMS_LOW_COLUMN_NAME,
        SPI_HIGH_COLUMN_NAME, DOCUMENTS_HIGH_COLUMN_NAME, SYSTEMS_HIGH_COLUMN_NAME,
        SPI_HIGH_HIGH_COLUMN_NAME, DOCUMENTS_HIGH_HIGH_COLUMN_NAME, SYSTEMS_HIGH_HIGH_COLUMN_NAME);
    
       
    /**
     * Public constructor.
     * 
     * @param wrapedLoops Wrapped loops collection to be displayed in parent table
     */
    public LoopsTableModel(List<SettingsSelector> wrapedLoops)
    {
        this.wrapedLoops = wrapedLoops;
    }//DevicesTableModel
    
    
    /**
     * Метод возвращает количество колонок модели.
     * 
     * @return  int
     */
    @Override
    public int getColumnCount()
    {
        return this.fields.size();
    }//getColumnCount
    
    
    /**
     * Метод возвращает количество строк модели.
     * 
     * @return  int
     */
    @Override
    public int getRowCount() 
    { 
        return this.wrapedLoops.size();
    }//getRowCount
    
    
    /**
     * Метод возвращает имя колонки по ее индексу.
     * 
     * @param   col     Индекс столбца
     * @return  String
     */
    @Override
    public String getColumnName(int col) 
    {
        return this.fields.get(col);
    }//getColumnName(int col) 
     
    
    /**
     * Метод возвращает значение заданной ячейки.
     * 
     * @param row Индекс строки
     * @param col Индекс столбца
     * @return Значение заданной ячейки
     */
    @Override
    public Object getValueAt(int row, int col) 
    {
        Object result = null; 
        SettingsSelector currentWraper = wrapedLoops.get(row);
        
        if (this.fields.get(col).equals(this.LOOP_COLUMN_NAME)) result = currentWraper.getEntity().toString();
                
        if (this.fields.get(col).equals(this.SPI_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmLL() != null ? currentWraper.getChosenIntoolsAlarmLL().getValue() : ""; 
        if (this.fields.get(col).equals(this.DOCUMENTS_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmLL() != null ? currentWraper.getChosenDocumentsAlarmLL().getValue() : "";
        if (this.fields.get(col).equals(this.SYSTEMS_LOW_LOW_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmLL() != null ? currentWraper.getChosenSystemsAlarmLL().getValue() : "";
        
        if (this.fields.get(col).equals(this.SPI_LOW_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmL() != null ? currentWraper.getChosenIntoolsAlarmL().getValue() : ""; 
        if (this.fields.get(col).equals(this.DOCUMENTS_LOW_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmL() != null ? currentWraper.getChosenDocumentsAlarmL().getValue() : "";
        if (this.fields.get(col).equals(this.SYSTEMS_LOW_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmL() != null ? currentWraper.getChosenSystemsAlarmL().getValue() : "";
        
        if (this.fields.get(col).equals(this.SPI_HIGH_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmH() != null ? currentWraper.getChosenIntoolsAlarmH().getValue() : ""; 
        if (this.fields.get(col).equals(this.DOCUMENTS_HIGH_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmH() != null ? currentWraper.getChosenDocumentsAlarmH().getValue() : "";
        if (this.fields.get(col).equals(this.SYSTEMS_HIGH_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmH() != null ? currentWraper.getChosenSystemsAlarmH().getValue() : "";
        
        if (this.fields.get(col).equals(this.SPI_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenIntoolsAlarmHH() != null ? currentWraper.getChosenIntoolsAlarmHH().getValue() : ""; 
        if (this.fields.get(col).equals(this.DOCUMENTS_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenDocumentsAlarmHH() != null ? currentWraper.getChosenDocumentsAlarmHH().getValue() : "";
        if (this.fields.get(col).equals(this.SYSTEMS_HIGH_HIGH_COLUMN_NAME)) result = currentWraper.getChosenSystemsAlarmHH() != null ? currentWraper.getChosenSystemsAlarmHH().getValue() : "";
        
        return result;
    }//getValueAt
}//DevicesTableModel
