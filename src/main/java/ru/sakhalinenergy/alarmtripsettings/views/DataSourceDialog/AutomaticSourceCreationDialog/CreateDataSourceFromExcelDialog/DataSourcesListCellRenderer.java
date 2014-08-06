package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDataSourceFromExcelDialog;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;


/**
 * Класс реализует отрисовщик ячеек списка доступных типов источников данных.
 *
 * @author   Denis.Udovenko
 * @version  1.0.1
 */
public class DataSourcesListCellRenderer extends DefaultListCellRenderer 
{
   
    /**
     * 
     * 
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        //Получаем компонент рендерера из родительского класса:
	JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	
        Icon icon = new ImageIcon();
        
	//Назначаем иконку элементу списка в зависимости от того, какой тип источника данных он отображает:
        if (label.getText().equals(SourcesTypes.INTOOLS_EXPORT_DOCUMENT.NAME)) icon = Main.intoolsIcon;
        if (label.getText().equals(SourcesTypes.ALARM_AND_TRIP_SCHEDULE.NAME)) icon = Main.documentsIcon;
        if (label.getText().equals(SourcesTypes.DCS_VARIABLE_TABLE.NAME)) icon = Main.dcsIcon;
        if (label.getText().equals(SourcesTypes.ESD_VARIABLE_TABLE.NAME)) icon = Main.esdIcon;
        if (label.getText().equals(SourcesTypes.FGS_VARIABLE_TABLE.NAME)) icon = Main.fgsIcon;
        
        //Устанавливаем иконку для отображаемого значения:
	label.setIcon(icon);
	return label;
    }//getListCellRendererComponent
}//DataSourcesListCellRenderer