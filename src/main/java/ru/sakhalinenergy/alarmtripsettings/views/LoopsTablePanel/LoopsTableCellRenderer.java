package ru.sakhalinenergy.alarmtripsettings.views.LoopsTablePanel;

import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.Main;
import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;


/**
 * Класс наследует отрисовщик ячеек таблицы устройств и перегружает его метод 
 * отрисовки.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.5
 */
public class LoopsTableCellRenderer extends DefaultTableCellRenderer 
{
    private final LoopsTableObservable loopsTable;    
    
    private final Color LOOP_TO_BE_SPLIT_NAME_FONT_COLOR = Color.RED;     //Цвет имени петли, требующей разделения
    private final Color HALF_SPLIT_LOOP_NAME_FONT_COLOR = Color.MAGENTA;  //Цвет имени тага частично разделенной петли
    private final Color SPLIT_LOOP_NAME_FONT_COLOR = Color.BLUE;          //Цвет имени тага полностью разделенной петли
    
    //Цвета ячеек для групп алармов:
    private final Color ALARM_LOW_LOW_GROUP_COLOR = new Color(245, 240, 255);
    private final Color ALARM_LOW_GROUP_COLOR = new Color(223, 253, 255);
    private final Color ALARM_HIGH_GROUP_COLOR = new Color(209, 255, 214);
    private final Color ALARM_HIGH_HIGH_GROUP_COLOR = new Color(250, 255, 222);
    
    
    /**
     * Public constructor.
     * 
     * @param loopsTable Loops table instance
     */
    public LoopsTableCellRenderer(LoopsTableObservable loopsTable)
    {
        this.loopsTable = loopsTable;
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
        LoopsTableModel tableModel = (LoopsTableModel)table.getModel();
                        
        //Получаем устройство:
        SettingsSelector wrapedLoop = loopsTable.getWrappedLoops().get(table.convertRowIndexToModel(row));
        
        //Получем имя выбранной колонки из модели, используя преобразование координат между моделью и вью:
        String columnName = tableModel.getColumnName(table.convertColumnIndexToModel(column));
        
        //Изменяем цвет шрифта для невыбранных разделенных петель:
        cell.setForeground(Color.BLACK);
        //if (columnName.equals(tableModel.LOOP_COLUMN_NAME) && loop.getSplit() && !loop.hasDuplicatedAlarms()) cell.setForeground(this.SPLIT_LOOP_NAME_FONT_COLOR);
        //if (columnName.equals(tableModel.LOOP_COLUMN_NAME) && loop.getSplit() && loop.hasDuplicatedAlarms()) cell.setForeground(this.HALF_SPLIT_LOOP_NAME_FONT_COLOR);
        //if (columnName.equals(tableModel.LOOP_COLUMN_NAME) && !loop.getSplit() && loop.hasDuplicatedAlarms()) cell.setForeground(this.LOOP_TO_BE_SPLIT_NAME_FONT_COLOR);
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SPI_LOW_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenIntoolsAlarmLL() != null ? wrapedLoop.getIntoolsAlarmLLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_LOW_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenDocumentsAlarmLL() != null ? wrapedLoop.getDocumentsAlarmLLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_LOW_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.SYSTEMS_LOW_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenSystemsAlarmLL() != null ? wrapedLoop.getSystemsAlarmLLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_LOW_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SPI_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenIntoolsAlarmL() != null ? wrapedLoop.getIntoolsAlarmLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.DOCUMENTS_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenDocumentsAlarmL() != null ? wrapedLoop.getDocumentsAlarmLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.SYSTEMS_LOW_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenSystemsAlarmL() != null ? wrapedLoop.getSystemsAlarmLConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_LOW_GROUP_COLOR);
        }//if     
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SPI_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenIntoolsAlarmH() != null ? wrapedLoop.getIntoolsAlarmHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.DOCUMENTS_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenDocumentsAlarmH() != null ? wrapedLoop.getDocumentsAlarmHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.SYSTEMS_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenSystemsAlarmH() != null ? wrapedLoop.getSystemsAlarmHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из SPI:
        if (columnName.equals(tableModel.SPI_HIGH_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenIntoolsAlarmHH() != null ? wrapedLoop.getIntoolsAlarmHHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_HIGH_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.DOCUMENTS_HIGH_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenDocumentsAlarmHH() != null ? wrapedLoop.getDocumentsAlarmHHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_HIGH_GROUP_COLOR);
        }//if
        
        //Подсвечиваем ячейки колонки с нижними трипами из документов:
        if (columnName.equals(tableModel.SYSTEMS_HIGH_HIGH_COLUMN_NAME))
        {
            Double intensity = wrapedLoop.getChosenSystemsAlarmHH() != null ? wrapedLoop.getSystemsAlarmHHConformity() * 2.55 : 255.0;
            Color background = new Color(255, intensity.intValue(), intensity.intValue());
            cell.setBackground(background);
            if (intensity.intValue() > 250) cell.setBackground(this.ALARM_HIGH_HIGH_GROUP_COLOR);
        }//if
        
        //Обрабатываем выделенную строку:
        if (isSelected)
        {
            cell.setFont(new Font("Arial", Font.BOLD, 12));
            cell.setForeground(Color.BLACK);
            cell.setBorder(new MatteBorder(1, 0, 1, 0, Color.BLACK));
        }//if
             
        //Убираем подсветку ячейки с маской прибора и подсвечиваем разделенные петли:
        if (columnName.equals(tableModel.LOOP_COLUMN_NAME)) 
        {
            boolean loopIsSplit = loopsTable.isLoopSplit(wrapedLoop.getEntity());
            boolean loopHasDuplicatedAlarms = wrapedLoop.getEntity().hasDuplicatedAlarms(); 

            cell.setBackground(Color.WHITE);
            if (loopIsSplit && !loopHasDuplicatedAlarms) cell.setForeground(this.SPLIT_LOOP_NAME_FONT_COLOR);
            if (loopIsSplit && loopHasDuplicatedAlarms) cell.setForeground(this.HALF_SPLIT_LOOP_NAME_FONT_COLOR);
            if (!loopIsSplit && loopHasDuplicatedAlarms) cell.setForeground(this.LOOP_TO_BE_SPLIT_NAME_FONT_COLOR);
        }//if

        return cell;
    }//getTableCellRendererComponent
}//DevicesTableCellRenderer
