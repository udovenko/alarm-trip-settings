package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Класс реализует отрисовщик ячейки дарева тагов. 
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class StationsTreeNodeRenderer implements TreeCellRenderer
{
    private JCheckBox leafRenderer = new JCheckBox();
    
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
    public StationsTreeNodeRenderer() 
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
     * @param   tree       Экземпляр дерева станций
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
        Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
   
        String stringValue = tree.convertValueToText(value, selected, expanded, leaf, row, false);
          
        leafRenderer.setText(stringValue);
        leafRenderer.setSelected(false);
        leafRenderer.setEnabled(tree.isEnabled());
        leafRenderer.setForeground(textForeground);
        leafRenderer.setBackground(textBackground);

        if ((value != null) && (value instanceof DefaultMutableTreeNode))
        {
            userObject = ((DefaultMutableTreeNode)value).getUserObject();
                              
            if (userObject instanceof CheckboxNode)
            {
                CheckboxNode node = (CheckboxNode) userObject;
                leafRenderer.setText(node.getObject().toString());
                leafRenderer.setSelected(node.isSelected());
            }//if
        }//if
               
        returnValue = leafRenderer;
             
        return returnValue;
    }//getTreeCellRendererComponent
}//TagsTreeNodeRenderer
