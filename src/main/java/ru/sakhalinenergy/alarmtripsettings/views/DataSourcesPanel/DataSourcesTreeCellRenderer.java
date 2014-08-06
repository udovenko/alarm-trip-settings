package ru.sakhalinenergy.alarmtripsettings.views.DataSourcesPanel;

import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;


/**
 * Класс наследует дефолтный отрисовщик ячейки дерева jTree и переопределяет
 * пороцесс отрисовки ячеек дерева источников данных тагов по выбранному 
 * объекту.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.2
 */
public class DataSourcesTreeCellRenderer extends DefaultTreeCellRenderer
{
       
    /**
     * Метод перегружает стандартный отрисовщик узла дерева тагов и 
     * устанавливает соответствующие иконки в зависимости от класса или 
     * содержания узла.
     * 
     * @param tree Текущий экземпляр дерева
     * @param value Экземпляр текущего выбранного узла дерева
     * @param sel Флаг "узел выбран"
     * @param expanded Флаг "узел развернут"
     * @param leaf Флаг "узел является листом", т.е не имеет детей
     * @param row Индекс строки
     * @param hasFocus Флаг "установлен фокус"
     * @return Component Отредактированный экземпляр узла.
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, 
            leaf, row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        ImageIcon icon = null;
              
        if (node.getUserObject().getClass() == Source.class)
        {
            Source nodeObject = (Source)node.getUserObject();
                              
            //Если узел - заголовок списка тагов SPI:
            if (nodeObject.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon = Main.intoolsIcon;
        
            //Если узел - заголовок списка тагов, полученных из документов:
            if (nodeObject.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
        
            //Если узел - заголовок списка тагов, полученных из DCS:
            if (nodeObject.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
        
            //Если узел - заголовок списка тагов, полученных из ESD:
            if (nodeObject.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
            
            //Если узел - заголовок списка тагов, полученных из FGS:
            if (nodeObject.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
            
        } else if (node.getUserObject().getClass() == SourceProperty.class){
        
            icon = Main.settingIcon;
        }//if
        
        this.setIcon(icon);
        
        return this;
    }//getTreeCellRendererComponent
}//DataSourcesTreeCellRenderer
