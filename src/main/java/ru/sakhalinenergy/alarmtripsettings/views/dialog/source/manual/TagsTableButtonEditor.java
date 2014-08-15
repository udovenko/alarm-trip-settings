package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.DefaultCellEditor;


/**
 * Implements button cell editor for tags table.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class TagsTableButtonEditor extends DefaultCellEditor 
{
    protected JButton button;
    private String label;
    
    
    /**
     * Public constructor. Sets button style and action listener.
     */
    public TagsTableButtonEditor() 
    {
        super(new JCheckBox());
    
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                fireEditingStopped();
            }// actionPerformed
        });// addActionListener
    }// TagsTableButtonEditor

    
    /**
     * Returns the component that should be added to the client's component 
     * hierarchy. Once installed in the client's hierarchy this component will 
     * then be able to draw and receive user input.
     * 
     * @param table Table that is asking the editor to edit
     * @param value Value of the cell to be edited
     * @param isSelected True if the cell is to be rendered with highlighting
     * @param row Row index of cell being edited
     * @param column Column index of cell being edited
     * @return Component for editing
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) 
    {
        if (isSelected)
        {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
        
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }// else
        
        label = (value == null) ? "" : value.toString();
        button.setText(label);
                
        return button;
    }// getTableCellEditorComponent
    
    
    /**
     * Returns the value contained in the editor.
     * 
     * @return Value contained in the editor
     */
    @Override
    public Object getCellEditorValue() 
    {
        return label;
    }// getCellEditorValue
}// DcsVariableTableTagsTableButtonEditor