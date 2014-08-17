package ru.sakhalinenergy.alarmtripsettings.views.panel.sources;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;


/**
 * Implements data sources tree cell renderer.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class DataSourcesTreeCellRenderer extends DefaultTreeCellRenderer
{
       
    /**
     * Returns component that the renderer uses to draw the value.
     * 
     * @param tree JTree the receiver is being configured for
     * @param value Value of the current tree cell to be set
     * @param selected Specifies that cell will be drawn as selected
     * @param expanded Specifies that node will be drawn in expended state
     * @param leaf Specifies that node represents a leaf 
     * @param row Row index in tree
     * @param hasFocus Specifies that node currently has focus
     * @return Component that the renderer uses to draw the value
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        // Call superclass constructor:
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        ImageIcon icon = null;
              
        if (node.getUserObject().getClass() == Source.class)
        {
            Source nodeObject = (Source)node.getUserObject();
                              
            // If node is SPI export data source:
            if (nodeObject.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon = Main.intoolsIcon;
        
            // If node is document data source:
            if (nodeObject.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
        
            // If node is DCS variable table data source:
            if (nodeObject.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
        
            // If node is ESD variable table data source:
            if (nodeObject.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
            
            // If node is FGS variable table data source:
            if (nodeObject.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
            
        // If node is source property:
        } else if (node.getUserObject().getClass() == SourceProperty.class) {
        
            icon = Main.settingIcon;
        }// if
        
        this.setIcon(icon);
        
        return this;
    }// getTreeCellRendererComponent
}// DataSourcesTreeCellRenderer
