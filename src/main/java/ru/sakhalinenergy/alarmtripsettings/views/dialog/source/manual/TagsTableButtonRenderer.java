package ru.sakhalinenergy.alarmtripsettings.views.dialog.source.manual;

import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


/**
 * Класс реализует отрисовщик кнопки удаления тага.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class TagsTableButtonRenderer extends JButton implements TableCellRenderer
{
 
    /**
     * Конструктор класса.
     */
    public TagsTableButtonRenderer() 
    {
        setOpaque(true);
    }//DcsVariableTableTagsTableButtonRenderer

    
    /**
     * Метод перегружает стандартный метод получения компонента отрисовки 
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
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) 
    {
        //Получаем модель таблицы:
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
            }//else
    
            setText((value == null) ? "" : value.toString());
            
            return this;
        } else {
            
            return null;
        }//else
    }//getTableCellRendererComponent
}//getTableCellRendererComponent