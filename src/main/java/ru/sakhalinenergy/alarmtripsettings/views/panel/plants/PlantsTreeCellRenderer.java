package ru.sakhalinenergy.alarmtripsettings.views.panel.plants;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;


/**
 * Implements plants (PAU objects) tree cell renderer. 
 * 
 * @author Denis Udovenko
 * @version 1.0.1
 */
public class PlantsTreeCellRenderer extends DefaultTreeCellRenderer
{
    
    /**
     * Returns component that renderer uses to draw tree value depending on
     * tree object type.
     * 
     * @param tree JTree the receiver is being configured for
     * @param value Value of the current tree cell to be set
     * @param selected Specifies that cell will be drawn as selected
     * @param expanded Specifies that node will be drawn in expended state
     * @param leaf Specifies that node represents a leaf 
     * @param row Row index in tree
     * @param hasFocus Specifies that node currently has focus
     * @return Component that renderer uses to draw tree value depending on tree object type
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, 
            row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        ImageIcon icon = null;
                
        if (node.getUserObject() instanceof Plant)    icon = Main.plantIcon;
        if (node.getUserObject() instanceof TreeArea) icon = Main.areaIcon;
        if (node.getUserObject() instanceof TreeUnit) icon = Main.unitIcon;
                        
        setIcon(icon);
        return this;
    }// getTreeCellRendererComponent
}// PlantsTreeCellRenderer 
