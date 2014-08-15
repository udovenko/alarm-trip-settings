package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Класс реализует отрисовщик ячейки дарева тагов. 
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class TagsTreeNodeRenderer implements TreeCellRenderer
{
    private JCheckBox leafRenderer = new JCheckBox();
    private DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

    Color selectionBorderColor, selectionForeground, selectionBackground,
        textForeground, textBackground;

    
    /**
     * Метод возвращает отрисовщик ветви дерева.
     * 
     * @return  JCheckBox
     */
    protected JCheckBox getLeafRenderer()
    {
        return leafRenderer;
    }//getLeafRenderer

    
    /**
     * Контруктор класса. Настраивает стили ячеек.
     */
    public TagsTreeNodeRenderer() 
    {
        Font fontValue;
        fontValue = UIManager.getFont("Tree.font");
    
        if (fontValue != null) 
        {
            leafRenderer.setFont(fontValue);
        }//if
        
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }//TagsTreeNodeRenderer

    
    /**
     * Метод возвращает отрисовщик ячейки дерева тагов в зависимости от того, 
     * является ли ячейка конечной ветвю дерева или нет. Для конечных ветвей
     * отображаются ячейки с чекбоксами.
     * 
     * @param   tree       Экземпляр дерева тагов
     * @param   value      Значение текущей ячейки
     * @param   selected   Флаг того, что ячейка выбрана
     * @param   vexpanded  Флаг того, что узел развернут
     * @param   leaf       Флаг того, что ячейка предстваляет собой конечную ветвь
     * @param   row        Индекс строки в дереве
     * @param   hasFocus   Флаг наличия фокуса
     * @return  Component
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) 
    {
        Component returnValue;
        ImageIcon icon = null;     
        Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
                          
        if (leaf) 
        {
            String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
            
            leafRenderer.setText(stringValue);
            leafRenderer.setSelected(false);
            leafRenderer.setEnabled(tree.isEnabled());
            leafRenderer.setForeground(textForeground);
            leafRenderer.setBackground(textBackground);

            if ((value != null) && (value instanceof DefaultMutableTreeNode))
            {
                userObject = ((DefaultMutableTreeNode)value).getUserObject();
                              
                if (userObject instanceof TagNodeModel)
                {
                    TagNodeModel node = (TagNodeModel) userObject;
                    leafRenderer.setText(node.getTag().toString());
                    leafRenderer.setSelected(node.isSelected());
                    
                }//if
            }//if
            
            returnValue = leafRenderer;
    
        } else {
      
            Source tempSource;
            
            nonLeafRenderer.setForeground(textForeground);
            nonLeafRenderer.setBackground(textBackground);
            returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree, value, false, expanded, leaf, row, false);
                        
            //Если узел - источник данных:
            if (userObject.getClass() == Source.class)
            {
                tempSource  = (Source)userObject;
                if (tempSource.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon =  Main.intoolsIcon;
                if (tempSource.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
                if (tempSource.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
                if (tempSource.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
                if (tempSource.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
            }//if
             
            nonLeafRenderer.setIcon(icon);
        }//else
        
        return returnValue;
    }//getTreeCellRendererComponent
}//TagsTreeNodeRenderer
