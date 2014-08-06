package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.ManualSourceEditingDialog;

import ru.sakhalinenergy.alarmtripsettings.Main;
import javax.swing.JComponent;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Класс наследует отрисовщик ячеек таблицы устройств и перегружает его метод 
 * отрисовки.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class TagsTableCellRenderer extends DefaultTableCellRenderer 
{
    
    
    
    /**
     * Конструктор класса.
     */
    public TagsTableCellRenderer()
    {
       
        setOpaque(true);
    }//DevicesTableCellRenderer

    
    /**
     * Метод возвращает отрендерренный компонент ячейки, перегружая дефолтный рендерер.
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
    public JComponent getTableCellRendererComponent(JTable table, Object value, 
        boolean isSelected, boolean hasFocus, int row, int column)
    {
        //Вызваем родительский конструктор рендерера ячейки:
        JComponent cell = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        //Получаем модель таблицы:
        TagsTableModel tableModel = (TagsTableModel)table.getModel();
        
        //Получем имя выбранной колонки из модели, используя преобразование координат между моделью и вью:
        String columnName = tableModel.getColumnName(table.convertColumnIndexToModel(column));
        
        if (tableModel.isCellEditable(table.convertRowIndexToModel(row), table.convertColumnIndexToModel(column)))
        {
            cell.setBackground(Color.WHITE);
        } else {
        
            cell.setBackground(Color.LIGHT_GRAY);
        }//if
               
        cell.setForeground(Color.BLACK);
                
        return cell;
    }//getTableCellRendererComponent
}//DevicesTableCellRenderer
