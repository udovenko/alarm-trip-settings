package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;

import java.util.EventObject;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.tree.TreeCellEditor;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;


/**
 *
 * @author   Denis.Udovenko
 * @version  1.0.1 
 */
public class StationsTreeNodeEditor extends AbstractCellEditor implements TreeCellEditor
{
    private final JTree tree;
    
    private StationsTreeNodeRenderer renderer = new StationsTreeNodeRenderer();
    private ChangeEvent changeEvent = null;
    

    /**
     * Конструктор класса. 
     */
    public StationsTreeNodeEditor (JTree tree) 
    {
        this.tree = tree;
    }//TagsTreeNodeEditor
    
    
    /**
     * 
     * 
     */
    @Override
    public Object getCellEditorValue() 
    {
        JCheckBox checkbox = renderer.getLeafRenderer();
        CheckboxNode checkBoxNode = new CheckboxNode(checkbox.getText(),
        checkbox.isSelected());
        
        return checkBoxNode;
    }//getCellEditorValue

    
    /**
     * 
     * 
     */
    @Override
    public boolean isCellEditable(EventObject event) 
    {
        boolean returnValue = false;
        
        if (event instanceof MouseEvent) 
        {
            MouseEvent mouseEvent = (MouseEvent) event;
            TreePath path = tree.getPathForLocation(mouseEvent.getX(),
            mouseEvent.getY());
            
            if (path != null) 
            {
                Object node = path.getLastPathComponent();
                
                if ((node != null) && (node instanceof DefaultMutableTreeNode)) 
                {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
                    returnValue = treeNode.isLeaf();
                }//if
            }//if
        }//if
       
        return returnValue;
    }//isCellEditable
    
    
    /**
     * 
     * 
     */
    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row) 
    {
        Component editor = renderer.getTreeCellRendererComponent(tree, value,
            true, expanded, leaf, row, true);

        // editor always selected / focused
        ItemListener itemListener = new ItemListener() 
        {
            public void itemStateChanged(ItemEvent itemEvent) 
            {
                if (stopCellEditing()) fireEditingStopped();
            }//itemStateChanged
        };//ItemListener
        
        if (editor instanceof JCheckBox)
        {
            ((JCheckBox) editor).addItemListener(itemListener);
        }//if

        return editor;
    }//getTreeCellEditorComponent
}//TagsTreeNodeEditor
