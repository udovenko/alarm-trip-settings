package ru.sakhalinenergy.alarmtripsettings.views.LoopsTablePanel;

import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JComponent;
import javax.swing.border.MatteBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.Border;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 * Класс наследует отрисовщик ячеек таблицы устройств и перегружает его метод 
 * отрисовки для заголовков таблицы устройств.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class LoopsTableColumnHeaderRenderer extends DefaultTableCellRenderer 
{
      
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
        TableCellRenderer tcr = table.getTableHeader ().getDefaultRenderer ();

        // Extract the component used to render the column header.
        Component c = tcr.getTableCellRendererComponent (table, value,
                                                       isSelected,
                                                       hasFocus,
                                                       row, column);
          
      
        //Получаем модель таблицы:
        LoopsTableModel tableModel = (LoopsTableModel)table.getModel();
        
        //Получем имя выбранной колонки из модели, используя преобразование координат между моделью и вью:
        String columnName = tableModel.getColumnName(table.convertColumnIndexToModel(column));
        
        JLabel label = new JLabel();
        
        //Если колонка - заголовок названия контуров, устанавливаем соответвующую иконку:
        if (columnName.equals(tableModel.LOOP_COLUMN_NAME))
        {
            label = new JLabel (Main.loopIcon);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SPI_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.SPI_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.SPI_HIGH_COLUMN_NAME) || columnName.equals(tableModel.SPI_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.intoolsIcon);
        }//if
            
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.DOCUMENTS_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.DOCUMENTS_HIGH_COLUMN_NAME) || columnName.equals(tableModel.DOCUMENTS_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.documentsIcon);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SYSTEMS_LOW_LOW_COLUMN_NAME) || columnName.equals(tableModel.SYSTEMS_LOW_COLUMN_NAME)
            || columnName.equals(tableModel.SYSTEMS_HIGH_COLUMN_NAME) || columnName.equals(tableModel.SYSTEMS_HIGH_HIGH_COLUMN_NAME))
        {
            label = new JLabel (Main.systemsIcon);
        }//if
               
        Border border = new MatteBorder(0,0,1,1, Color.LIGHT_GRAY);
        Border margin = ((JComponent) c).getBorder ();
        label.setBorder(new CompoundBorder(border, margin));
        label.setOpaque(true);
    
        label.setFont (c.getFont ());
        label.setForeground (c.getForeground ());
    
        label.setBackground(new Color(250, 253, 255));
     
        label.setText ((String) value);
        label.setHorizontalAlignment(JLabel.LEFT);
        
        return label;
    }//getTableCellRendererComponent
}//DevicesTableColumnHeaderRenderer
