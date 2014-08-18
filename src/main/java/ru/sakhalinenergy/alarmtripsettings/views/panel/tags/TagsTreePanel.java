package ru.sakhalinenergy.alarmtripsettings.views.panel.tags;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.views.panel.Panel;


/**
 * Implements panel for rendering tags tree of the loop selected in loops table.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class TagsTreePanel extends Panel 
{
  
    private final SettingsSelector model;
    
    
    /**
     * Public constructor. Sets loop wrapper model and initializes components.
     *
     * @param model Loop entity wrapped into settings selection logic
     */
    public TagsTreePanel(SettingsSelector model) 
    {
        this.model = model;
                
        initComponents();
        copyNodeNameToClipboardMenuItem.setIcon(Main.copyIcon);
        
        render();
    }// TagsTreePanel
       
    
    /**
     * Renders tags tree.
     */
    private void render()
    {
        // Set tree cell renderer and mouse adapter:
        loopTagsTree.setCellRenderer(new TagsTreeCellRenderer(model));
        loopTagsTree.addMouseListener(new _TagsTreeMouseAdapter());
                
        // Get tree model and root node:
        DefaultTreeModel treeModel = (DefaultTreeModel)loopTagsTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
        // Build tree:
        TagsTreeOperator.buildTree(root, model.getEntity());
                
        // Reload tree:
        treeModel.reload(root);
        
        TreePath tempPath;
        DefaultMutableTreeNode tempNode;
        Object tempNodeObject;
        
        // Iterate tree nodes:
        for (int i = 0; i < loopTagsTree.getRowCount(); i++)
        {
            tempPath = loopTagsTree.getPathForRow(i);
            tempNode = (DefaultMutableTreeNode)tempPath.getLastPathComponent();
            tempNodeObject = tempNode.getUserObject();
            
            // Expand all sources nodes and only tag nodes which contain least one alarm setting:
            if (!(tempNodeObject instanceof Tag)) loopTagsTree.expandRow(i);
            else {
            
                Tag tempTag = (Tag)tempNodeObject;
                for (TagSetting tempSetting : tempTag.getSettings())
                {
                    if (tempSetting.getTypeId() == SettingsTypes.ALARM_LL_SETTING.ID 
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_L_SETTING.ID
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_H_SETTING.ID
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_HH_SETTING.ID) 
                    loopTagsTree.expandRow(i);
                }// for
            }// else
        }// for
    }// render
    
    
    /**
     * Inner class implements mouse adapter for tags tree.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _TagsTreeMouseAdapter extends MouseAdapter
    {
        
        /**
         * Handles right mouse click and shows tags tree popup menu.
         * 
         * @param event Mouse click event object
         */
        private void showSourcesTreePopupMenu(MouseEvent event)
        {
            int x = event.getX();
            int y = event.getY();
            JTree tree = (JTree)event.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            
            if (path == null) return;	
            
            tree.setSelectionPath(path);
            treePopupMenu.show(tree, x, y);
        }// showSourcesTreePopupMenu
        
        
        /**
         * Handles mouse pressed event.
         * 
         * @param event Mouse pressed event
         */
        @Override
        public void mousePressed(MouseEvent event)
        {
            if (event.isPopupTrigger()) showSourcesTreePopupMenu(event);
	}// mousePressed
	
        
        /**
         * Handles mouse released event.
         * 
         * @param event Mouse released event
         */
        @Override
        public void mouseReleased(MouseEvent event) 
        {
            if (event.isPopupTrigger()) showSourcesTreePopupMenu(event);
	}// mouseReleased
    }// _TagsTreeMouseAdapter
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treePopupMenu = new javax.swing.JPopupMenu();
        copyNodeNameToClipboardMenuItem = new javax.swing.JMenuItem();
        tagDetailsTreeScrollPane = new javax.swing.JScrollPane();
        loopTagsTree = new javax.swing.JTree();

        copyNodeNameToClipboardMenuItem.setText("Copy node name");
        copyNodeNameToClipboardMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyNodeNameToClipboardMenuItemActionPerformed(evt);
            }
        });
        treePopupMenu.add(copyNodeNameToClipboardMenuItem);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        loopTagsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        loopTagsTree.setRootVisible(false);
        loopTagsTree.setShowsRootHandles(true);
        tagDetailsTreeScrollPane.setViewportView(loopTagsTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tagDetailsTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(tagDetailsTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Handles "Copy selected node name to clipboard" menu item click event and
     * triggers appropriate event with selected node user object data.
     * 
     * @param evt Popup menu item click object
     */
    private void copyNodeNameToClipboardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNodeNameToClipboardMenuItemActionPerformed
        
        TreePath path = loopTagsTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent copyNodeNameToClipboardEvent = new CustomEvent(node.getUserObject());
        trigger(ViewEvent.COPY_NODE_NAME_TO_CLIPBOARD, copyNodeNameToClipboardEvent);    
    }//GEN-LAST:event_copyNodeNameToClipboardMenuItemActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyNodeNameToClipboardMenuItem;
    private javax.swing.JTree loopTagsTree;
    private javax.swing.JScrollPane tagDetailsTreeScrollPane;
    private javax.swing.JPopupMenu treePopupMenu;
    // End of variables declaration//GEN-END:variables
}// TagsTreePanel
