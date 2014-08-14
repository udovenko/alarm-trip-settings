package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JCheckBox;
import javax.swing.DefaultCellEditor;


/**
 * Класс реализует редактор ячейки таблицы тагов для кнопки удаления тага.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class TagsTableButtonEditor extends DefaultCellEditor 
{
    protected JButton button;
    private String label;
    private boolean isPushed;

    
    /**
     * Конструктор класса.
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
            }//actionPerformed
        });//addActionListener
    }//DcsVariableTableTagsTableButtonEditor

    
    /**
     * Метод перегружает стандартный метод получения компонента редактирования 
     * ячейки.
     * 
     * @param   table       Ссылка на родительскую таблицу
     * @param   value       Значение ячейки
     * @param   isSelected  Флаг "Ячейка выбрана"
     * @param   hasFocus    Флаг "Установлен фокус"
     * @param   row         Индекс строки
     * @param   column      Индекс столлбца
     * @return  JComponent  Компонент отрисованной ячейки
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
        }//else
        
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        
        return button;
    }//getTableCellEditorComponent
    
    
    /**
     * Метод перегружает стандартный метод для получения значения при 
     * редактировании ячейки.
     * 
     * @return  Object
     */
    @Override
    public Object getCellEditorValue() 
    {
        if (isPushed) 
        {
             //System.out.println(label + ": Ouch!");
        }//if
        
        isPushed = false;
        
        return new String(label);
    }//getCellEditorValue

    
    /**
     * Метод перегружает стандартный метод, возвращающий флаг окончания 
     * редакторования ячейки.
     * 
     * @return  boolean
     */
    @Override
    public boolean stopCellEditing() 
    {
        isPushed = false;
        return super.stopCellEditing();
    }//stopCellEditing

    
    /**
     * Метод перегружает стандартный метод, сообщающий о событии завершения 
     * редактирования ячейки.
     * 
     * @return  void
     */
    @Override
    protected void fireEditingStopped() 
    {
        super.fireEditingStopped();
    }//fireEditingStopped
}//DcsVariableTableTagsTableButtonEditor