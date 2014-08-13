package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;

import java.util.EventObject;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.tree.TreeCellEditor;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JCheckBox;


/**
 * Implements control stations tree node editor.
 *
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class StationsTreeNodeEditor extends AbstractCellEditor implements TreeCellEditor
{
    private final JTree tree;
    private final StationsTreeNodeRenderer renderer = new StationsTreeNodeRenderer();
        

    /**
     * Public constructor. Sets current tree instance.
     * 
     * @param tree Current tree instance
     */
    public StationsTreeNodeEditor (JTree tree) 
    {
        this.tree = tree;
    }// TagsTreeNodeEditor
    
    
    /**
     * Returns the value contained in the editor. In this case - CheckboxNode 
     * object.
     * 
     * @return Value contained in the editor
     */
    @Override
    public Object getCellEditorValue() 
    {
        JCheckBox checkbox = renderer.getLeafRenderer();
        CheckboxNode checkboxNodeObject = renderer.getLeafNodeObject();
        CheckboxNode editorValueCheckboxNodeObject = new CheckboxNode(checkboxNodeObject.getObject(), checkbox.isSelected());
        
        return editorValueCheckboxNodeObject;
    }// getCellEditorValue

    
    /**
     * Asks the editor if it can start editing using given event.
     * 
     * @param event Event the editor should use to consider whether to begin editing or not
     * @return True if editing can be started
     */
    @Override
    public boolean isCellEditable(EventObject event) 
    {
        boolean returnValue = false;
        
        if (event instanceof MouseEvent) 
        {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
            
            if (path != null) 
            {
                Object node = path.getLastPathComponent();
                
                if ((node != null) && (node instanceof DefaultMutableTreeNode)) 
                {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    returnValue = treeNode.isLeaf();
                }// if
            }// if
        }// if
       
        return returnValue;
    }// isCellEditable
    
    
    /**
     * Sets an initial value for the editor. This will cause the editor to 
     * stop editing and lose any partially edited value if the editor is editing
     * when this method is called.
     * 
     * @param tree JTree that is asking the editor to edit
     * @param value Value of the cell to be edited
     * @param selected True if the cell is to be rendered with selection highlighting
     * @param expanded True if the node is expanded
     * @param leaf True if the node is a leaf node
     * @param row The row index of the node being edited
     */
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row) 
    {
        Component editor = renderer.getTreeCellRendererComponent(tree, value,
            true, expanded, leaf, row, true);
        
        // Editor always selected/focused:
        ItemListener itemListener = new ItemListener() 
        {
            public void itemStateChanged(ItemEvent itemEvent) 
            {
                if (stopCellEditing()) fireEditingStopped();
            }// itemStateChanged
        };// ItemListener
        
        if (editor instanceof JCheckBox)
        {
            ((JCheckBox) editor).addItemListener(itemListener);
        }// if

        return editor;
    }// getTreeCellEditorComponent
}// TagsTreeNodeEditor
