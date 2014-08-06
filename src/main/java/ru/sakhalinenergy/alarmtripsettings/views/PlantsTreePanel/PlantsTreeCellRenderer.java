package ru.sakhalinenergy.alarmtripsettings.views.PlantsTreePanel;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;


/**
 * @author   Denis.Udovenko
 * @version  1.0.0
 */
public class PlantsTreeCellRenderer extends DefaultTreeCellRenderer
{
    
    /**
     * 
     * 
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, 
            row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        ImageIcon icon = null;
                
        if (node.getUserObject().getClass() == Plant.class)    icon = Main.plantIcon;
        if (node.getUserObject().getClass() == TreeArea.class) icon = Main.areaIcon;
        if (node.getUserObject().getClass() == TreeUnit.class) icon = Main.unitIcon;
        
                
        this.setIcon(icon);
        return this;
    }// getTreeCellRendererComponent
}// PlantsTreeCellRenderer 
