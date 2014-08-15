package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 * Implements button cell renderer for tags table.
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class TagsTableButtonRenderer extends JButton implements TableCellRenderer
{
 
    /**
     * Public constructor.
     */
    public TagsTableButtonRenderer() 
    {
        setOpaque(true);
    }// TagsTableButtonRenderer

    
    /**
     * Returns the button component used for drawing the cell.
     * 
     * @param table Parent table
     * @param value Value to assign to the cell
     * @param isSelected True if cell is selected
     * @param hasFocus True if cell has focus
     * @param row Row index of the cell to render
     * @param column Column index of the cell to render
     * @return Button component used for drawing the cell
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) 
    {
        // Get parent table model:
        TagsTableModel tableModel = (TagsTableModel)table.getModel();
      
        if (tableModel.isCellEditable(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column)))
        {
            if (isSelected) 
            {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            
            } else {
            
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }// else
    
            setText((value == null) ? "" : value.toString());
            return this;
        
        } else return null;
    }// getTableCellRendererComponent
}// TagsTableButtonRenderer