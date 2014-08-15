package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Implements alphanumeric cell renderer for tags table.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TagsTableCellRenderer extends DefaultTableCellRenderer 
{
       
    /**
     * Public constructor.
     */
    public TagsTableCellRenderer()
    {
        setOpaque(true);
    }// TagsTableCellRenderer

    
    /**
     * Returns configured cell component used for drawing.
     * 
     * @param table Parent table
     * @param value Value to assign to the cell
     * @param isSelected True if cell is selected
     * @param hasFocus True if cell has focus
     * @param row Row index of the cell to render
     * @param column Column index of the cell to render
     * @return Configured cell component used for drawing
     */
    @Override
    public JComponent getTableCellRendererComponent(JTable table, Object value, 
        boolean isSelected, boolean hasFocus, int row, int column)
    {
        // Call superclass method:
        JComponent cell = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Get table model:
        TagsTableModel tableModel = (TagsTableModel)table.getModel();
        
        if (tableModel.isCellEditable(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column)))
        {
            cell.setBackground(Color.WHITE);
        } else {
        
            cell.setBackground(Color.LIGHT_GRAY);
        }// else
               
        cell.setForeground(Color.BLACK);
        return cell;
    }// getTableCellRendererComponent
}// TagsTableCellRenderer
