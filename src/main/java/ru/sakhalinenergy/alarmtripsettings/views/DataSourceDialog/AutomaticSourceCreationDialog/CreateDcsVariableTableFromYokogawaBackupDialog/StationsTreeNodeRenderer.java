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
 * Implements control stations tree cell renderer.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class StationsTreeNodeRenderer implements TreeCellRenderer
{
    private final JCheckBox leafRenderer = new JCheckBox();
    private final Color textForeground, textBackground;

    private CheckboxNode leafNodeObject;
    
       
    /**
     * Public constructor. Configures tree nodes initial style.
     */
    public StationsTreeNodeRenderer() 
    {
        Font fontValue;
        fontValue = UIManager.getFont("Tree.font");
    
        if (fontValue != null) leafRenderer.setFont(fontValue);
                
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
     * Returns leaf node object (CheckboxNode wrapper).
     * 
     * @return leaf node object
     */
    protected CheckboxNode getLeafNodeObject()
    {
        return leafNodeObject;
    }// getLeafNodeObject
    
    
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
                
                leafNodeObject = node;
            }// if
        }// if
               
        returnValue = leafRenderer;
             
        return returnValue;
    }// getTreeCellRendererComponent
}// TagsTreeNodeRenderer
