package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

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
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Implements tags and sources tree cell renderer.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class TagsTreeNodeRenderer implements TreeCellRenderer
{
    private final JCheckBox leafRenderer = new JCheckBox();
    private final DefaultTreeCellRenderer nonLeafRenderer = new DefaultTreeCellRenderer();

    private final Color textForeground, textBackground;

    
    /**
     * Public constructor. Configures tree nodes initial style.
     */
    public TagsTreeNodeRenderer() 
    {
        Font fontValue;
        fontValue = UIManager.getFont("Tree.font");
    
        if (fontValue != null) 
        {
            leafRenderer.setFont(fontValue);
        }// if
        
        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");
    }// TagsTreeNodeRenderer

    
    /**
     * Returns leaf renderer (JCheckBox) instance.
     * 
     * @return Leaf renderer instance
     */
    protected JCheckBox getLeafRenderer()
    {
        return leafRenderer;
    }// getLeafRenderer
    
    
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
                    
                }// if
            }// if
            
            returnValue = leafRenderer;
    
        } else {
      
            Source tempSource;
            
            nonLeafRenderer.setForeground(textForeground);
            nonLeafRenderer.setBackground(textBackground);
            returnValue = nonLeafRenderer.getTreeCellRendererComponent(tree, value, false, expanded, leaf, row, false);
                        
            // If node if data source:
            if (userObject.getClass() == Source.class)
            {
                tempSource  = (Source)userObject;
                if (tempSource.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon =  Main.intoolsIcon;
                if (tempSource.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
                if (tempSource.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
                if (tempSource.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
                if (tempSource.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
            }// if
             
            nonLeafRenderer.setIcon(icon);
        }// else
        
        return returnValue;
    }// getTreeCellRendererComponent
}// TagsTreeNodeRenderer
