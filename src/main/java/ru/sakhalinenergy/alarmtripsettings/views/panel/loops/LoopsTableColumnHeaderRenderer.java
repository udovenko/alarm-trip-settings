package ru.sakhalinenergy.alarmtripsettings.views.panel.loops;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;


/**
 * Implements cell renderer for loops table header.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class LoopsTableColumnHeaderRenderer extends DefaultTableCellRenderer 
{
      
    /**
     * Returns the table header cell renderer depending on header column type.
     * 
     * @param table Parent table
     * @param value Value to assign to the cell at [row, column]
     * @param isSelected True if cell is selected
     * @param hasFocus True if cell has focus
     * @param row Row index of the cell to render
     * @param column Column index of the cell to render
     * @return JComponent Header cell renderer depending on header column type
     */
    @Override
    public JComponent getTableCellRendererComponent(JTable table, Object value, 
        boolean isSelected, boolean hasFocus, int row, int column)
    {
        // Get default header renderer:
        TableCellRenderer defaultHeaderRenderer = table.getTableHeader().getDefaultRenderer();

        // Extract the component used to render column header:
        Component defaultRendererComponent = defaultHeaderRenderer.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);
          
        // Get table model:
        LoopsTableModel tableModel = (LoopsTableModel)table.getModel();
        
        // Get column name from table model using index conversion:
        String columnName = tableModel.getColumnName(table.convertColumnIndexToModel(column));
        
        JLabel label = new JLabel();
        
        // If cell is loop names column header:
        if (columnName.equals(tableModel.LOOP_COLUMN_NAME)) label = new JLabel (Main.loopIcon);
                
        // If cell is SPI alarm columns header:
        if (columnName.equals(tableModel.SPI_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.SPI_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.SPI_HIGH_COLUMN_NAME) || columnName.equals(tableModel.SPI_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.intoolsIcon);
        }// if
            
        // If cell is documents alarm columns header:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.DOCUMENTS_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.DOCUMENTS_HIGH_COLUMN_NAME) || columnName.equals(tableModel.DOCUMENTS_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.documentsIcon);
        }// if
        
        // If cell is systems alarm columns header:
        if (columnName.equals(tableModel.SYSTEMS_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.SYSTEMS_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.SYSTEMS_HIGH_COLUMN_NAME) || columnName.equals(tableModel.SYSTEMS_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.systemsIcon);
        }// if
               
        Border border = new MatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY);
        Border margin = ((JComponent)defaultRendererComponent).getBorder();
        label.setBorder(new CompoundBorder(border, margin));
        label.setOpaque(true);
    
        label.setFont (defaultRendererComponent.getFont());
        label.setForeground (defaultRendererComponent.getForeground());
    
        label.setBackground(new Color(250, 253, 255));
     
        label.setText((String)value);
        label.setHorizontalAlignment(JLabel.LEFT);
        
        return label;
    }// getTableCellRendererComponent
}// LoopsTableColumnHeaderRenderer
