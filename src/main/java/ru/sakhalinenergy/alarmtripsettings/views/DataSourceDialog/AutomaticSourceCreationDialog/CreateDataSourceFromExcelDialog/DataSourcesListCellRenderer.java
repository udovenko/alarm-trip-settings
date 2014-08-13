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
 * Implements cell renderer for available data sources combo box.
 *
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class DataSourcesListCellRenderer extends DefaultListCellRenderer 
{
   
    /**
     * Returns configured renderer component for particular cell.
     * 
     * @param list The JList being painted
     * @param value Value returned by list.getModel().getElementAt(index)
     * @param index Current cell index
     * @param isSelected Specifies that current cell was selected
     * @param cellHasFocus Specifies that current cell has the focus
     * @return Component whose paint() method will render the specified value
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, 
        int index, boolean isSelected, boolean cellHasFocus)
    {
        // Get renderer from paren class:
	JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	
        Icon icon = new ImageIcon();
        
	// Select label's icon depending on data source type:
        if (label.getText().equals(SourcesTypes.INTOOLS_EXPORT_DOCUMENT.NAME)) icon = Main.intoolsIcon;
        if (label.getText().equals(SourcesTypes.ALARM_AND_TRIP_SCHEDULE.NAME)) icon = Main.documentsIcon;
        if (label.getText().equals(SourcesTypes.DCS_VARIABLE_TABLE.NAME)) icon = Main.dcsIcon;
        if (label.getText().equals(SourcesTypes.ESD_VARIABLE_TABLE.NAME)) icon = Main.esdIcon;
        if (label.getText().equals(SourcesTypes.FGS_VARIABLE_TABLE.NAME)) icon = Main.fgsIcon;
        
        // Set selected icon and return label:
	label.setIcon(icon);
	return label;
    }// getListCellRendererComponent
}// DataSourcesListCellRenderer