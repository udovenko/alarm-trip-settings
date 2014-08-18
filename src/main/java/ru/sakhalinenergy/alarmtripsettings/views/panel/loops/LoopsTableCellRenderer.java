package ru.sakhalinenergy.alarmtripsettings.views.panel.loops;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;


/**
 * Implements cell renderer for loops table.
 * 
 * @author Denis Udovenko
 * @version 1.0.6
 */
public class LoopsTableCellRenderer extends DefaultTableCellRenderer 
{
    private final LoopsTableObservable loopsTable;    
    
    private static final Color LOOP_TO_BE_SPLIT_NAME_FONT_COLOR = Color.RED;     // Color for name of loop which needs to be splitted
    private static final Color HALF_SPLIT_LOOP_NAME_FONT_COLOR = Color.MAGENTA;  // Color for name of partially split loop
    private static final Color SPLIT_LOOP_NAME_FONT_COLOR = Color.BLUE;          // Color for name of completely split loop
    
    // Background colors for alarm goups cells:
    private static final Color ALARM_LOW_LOW_GROUP_COLOR = new Color(245, 240, 255);
    private static final Color ALARM_LOW_GROUP_COLOR = new Color(223, 253, 255);
    private static final Color ALARM_HIGH_GROUP_COLOR = new Color(209, 255, 214);
    private static final Color ALARM_HIGH_HIGH_GROUP_COLOR = new Color(250, 255, 222);
    
    
    /**
     * Public constructor. Sets loops table instance.
     * 
     * @param loopsTable Loops table instance
     */
    public LoopsTableCellRenderer(LoopsTableObservable loopsTable)
    {
        this.loopsTable = loopsTable;
        setOpaque(true);
    }// LoopsTableCellRenderer

    
    /**
     * Returns the table cell renderer depending on cell type and alarm group.
     * 
     * @param table Parent table
     * @param value Value to assign to the cell at [row, column]
     * @param isSelected True if cell is selected
     * @param hasFocus True if cell has focus
     * @param row Row index of the cell to render
     * @param column Column index of the cell to render
     * @return JComponent Cell renderer depending on cell type and alarm group
     */
    @Override
    public JComponent getTableCellRendererComponent(JTable table, Object value, 
        boolean isSelected, boolean hasFocus, int row, int column)
    {
        // Get superclass renderer component:
        JComponent cell = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Get table model:
        LoopsTableModel tableModel = (LoopsTableModel)table.getModel();
                        
        // Get loop wrapped into settings selection logic:
        SettingsSelector wrapedLoop = loopsTable.getWrappedLoops().get(table.convertRowIndexToModel(row));
        
        // Get column name from table model using index conversion:
        String columnName = tableModel.getColumnName(table.convertColumnIndexToModel(column));
        
        // Set default font color:
        cell.setForeground(Color.BLACK);
        
        // Set SPI LL alarm cells background color:
        if (columnName.equals(tableModel.SPI_LOW_LOW_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenIntoolsAlarmLL(),
                wrapedLoop.getIntoolsAlarmLLConformity(), ALARM_LOW_LOW_GROUP_COLOR);
        }// if
        
        // Set documents LL alarm cells background color:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_LOW_COLUMN_NAME))
        {
             _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenDocumentsAlarmLL(),
                wrapedLoop.getDocumentsAlarmLLConformity(), ALARM_LOW_LOW_GROUP_COLOR);
        }// if
        
        // Set systems LL alarm cells background color:
        if (columnName.equals(tableModel.SYSTEMS_LOW_LOW_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenSystemsAlarmLL(),
                wrapedLoop.getSystemsAlarmLLConformity(), ALARM_LOW_LOW_GROUP_COLOR);
        }// if
        
        // Set SPI L alarm cells background color:
        if (columnName.equals(tableModel.SPI_LOW_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenIntoolsAlarmL(),
                wrapedLoop.getIntoolsAlarmLConformity(), ALARM_LOW_GROUP_COLOR);
        }// if
        
        // Set documents L alarm cells background color:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenDocumentsAlarmL(),
                wrapedLoop.getDocumentsAlarmLConformity(), ALARM_LOW_GROUP_COLOR);
        }// if
        
        // Set systems L alarm cells background color:
        if (columnName.equals(tableModel.SYSTEMS_LOW_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenSystemsAlarmL(),
                wrapedLoop.getSystemsAlarmLConformity(), ALARM_LOW_GROUP_COLOR);
        }// if     
        
        // Set SPI H alarm cells background color:
        if (columnName.equals(tableModel.SPI_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenIntoolsAlarmH(),
                wrapedLoop.getIntoolsAlarmHConformity(), ALARM_HIGH_GROUP_COLOR);
        }// if
        
        // Set documents H alarm cells background color:
        if (columnName.equals(tableModel.DOCUMENTS_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenDocumentsAlarmH(),
                wrapedLoop.getDocumentsAlarmHConformity(), ALARM_HIGH_GROUP_COLOR);
        }// if
        
        // Set systems H alarm cells background color:
        if (columnName.equals(tableModel.SYSTEMS_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenSystemsAlarmH(),
                wrapedLoop.getSystemsAlarmHConformity(), ALARM_HIGH_GROUP_COLOR);
        }// if
        
        // Set SPI HH alarm cells background color:
        if (columnName.equals(tableModel.SPI_HIGH_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenIntoolsAlarmHH(),
                wrapedLoop.getIntoolsAlarmHHConformity(), ALARM_HIGH_HIGH_GROUP_COLOR);
        }// if
        
        // Set documents HH alarm cells background color:
        if (columnName.equals(tableModel.DOCUMENTS_HIGH_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenDocumentsAlarmHH(),
                wrapedLoop.getDocumentsAlarmHHConformity(), ALARM_HIGH_HIGH_GROUP_COLOR);
        }// if
        
        // Set systems HH alarm cells background color:
        if (columnName.equals(tableModel.SYSTEMS_HIGH_HIGH_COLUMN_NAME))
        {
            _setAlarmCellBackgroundColor(cell, wrapedLoop.getChosenSystemsAlarmHH(),
                wrapedLoop.getSystemsAlarmHHConformity(), ALARM_HIGH_HIGH_GROUP_COLOR);
        }// if
        
        // Set style for selected row cells:
        if (isSelected)
        {
            cell.setFont(new Font("Arial", Font.BOLD, 12));
            cell.setForeground(Color.BLACK);
            cell.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
        }// if
             
        // Remove loop number cell background and set font color for loops with splitting fetures: 
        if (columnName.equals(tableModel.LOOP_COLUMN_NAME)) 
        {
            boolean loopIsSplit = loopsTable.isLoopSplit(wrapedLoop.getEntity());
            boolean loopHasDuplicatedAlarms = wrapedLoop.getEntity().hasDuplicatedAlarms(); 

            cell.setBackground(Color.WHITE);
            if (loopIsSplit && !loopHasDuplicatedAlarms) cell.setForeground(SPLIT_LOOP_NAME_FONT_COLOR);
            if (loopIsSplit && loopHasDuplicatedAlarms) cell.setForeground(HALF_SPLIT_LOOP_NAME_FONT_COLOR);
            if (!loopIsSplit && loopHasDuplicatedAlarms) cell.setForeground(LOOP_TO_BE_SPLIT_NAME_FONT_COLOR);
        }// if

        return cell;
    }// getTableCellRendererComponent
    
    
    /**
     * Sets alarm cell background color depending on calculated alarm setting
     * conformity.
     * 
     * @param cell Cell component which background will be set
     * @param alarmSetting Tag's alarm setting instance or null
     * @param alarmSettingConformity Calculated alarm setting conformity
     * @param emptyCellColor Alarm group cells background color for empty cell
     */
    private void _setAlarmCellBackgroundColor(JComponent cell, TagSetting alarmSetting, 
        float alarmSettingConformity, Color emptyCellColor)
    {
        Double intensity = alarmSetting != null ? alarmSettingConformity * 2.55 : 255.0;
        Color background = new Color(255, intensity.intValue(), intensity.intValue());
        cell.setBackground(background);
        if (intensity.intValue() > 250) cell.setBackground(emptyCellColor);
    }// _setAlarmCellBackgroundColor
}// LoopsTableCellRenderer
