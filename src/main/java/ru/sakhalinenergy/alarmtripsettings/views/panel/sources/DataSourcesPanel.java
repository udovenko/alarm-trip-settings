package ru.sakhalinenergy.alarmtripsettings.views.panel.sources;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;
import ru.sakhalinenergy.alarmtripsettings.views.panel.Panel;


/**
 * Implements panel with data sources to which selected PAU object's tags
 * belong.
 * 
 * @author Denis Udovenko
 * @version 1.0.5
 */
public class DataSourcesPanel extends Panel 
{
   
    private final LoopsTableObservable model;    
     
    
    /**
     * Public constructor. Sets model instance and initializes components.
     *
     * @param model Loops table instance
     */
    public DataSourcesPanel(LoopsTableObservable model) 
    {
        this.model = model;
        
        initComponents();
        
        // Set sources tree mouse listener:
        sourcesTree.addMouseListener(new _SourcesTreeMouseAdapter());
        
        // Set context menu items icons:
        copyNodeNameToClipboardMenuItem.setIcon(Main.copyIcon);
        openLinkedDocumentMenuItem.setIcon(Main.linkIcon);
        createDataSourcesManuallyMenu.setIcon(Main.addSourceIcon);
        createIntoolsExportDataSourceManuallyMenuItem.setIcon(Main.intoolsIcon);
        createDocumentManuallyMenuItem.setIcon(Main.documentsIcon);
        createDataSourceFromMsExcelListMenuItem.setIcon(Main.excelIcon);
        createDcsVariableTableManuallyMenuItem.setIcon(Main.dcsIcon);
        createEsdVariableTableManuallyMenuItem.setIcon(Main.esdIcon);
        createFgsVariableTableManuallyMenuItem.setIcon(Main.fgsIcon);
        createDataSourceFromYokogawaDcsBackupMenuItem.setIcon(Main.yokogawaIcon);
        createDataSourceFromHoneywellDcsBackupMenuItem.setIcon(Main.honeywellIcon);
        createDataSourceFromHoneywellScadaExportMenuItem.setIcon(Main.honeywellIcon);
        editSelectedSourceMenuItem.setIcon(Main.editIcon);
        removeAllRelatedToSourceMenuItem.setIcon(Main.removeIcon);
        
        // Subscribe on model's events:
        model.on(CollectionEvent.LOOPS_READ, new _ModelUpdatedEventHandler());
    }// DataSourcesPanel
       
        
    /**
     * Inner class - handler for model's update event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _ModelUpdatedEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            sourcesTree.setCellRenderer(new DataSourcesTreeCellRenderer());
                
            // Get tree model and root node:
            DefaultTreeModel treeModel = (DefaultTreeModel)sourcesTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
            root.removeAllChildren();
            List<Source> sortedSources = model.getSources();

            // Sort collection by source type and priority:
            Collections.sort(sortedSources, new Comparator<Source>()
            {
                @Override
                public int compare(Source source1, Source source2)
                {
                    if (source1.getTypeId() != source2.getTypeId()) return source1.getTypeId() - source2.getTypeId();
                    if (source1.getTypeId() == source2.getTypeId()) return source2.getPriority() - source1.getPriority();
                    return 0;
                }// compare
            });// sort
        
            // Add data source nodes to root:
            for (Source source : sortedSources)
            {
                DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(source);
                       
                // Add properties nodes to data source node:
                for (SourceProperty sourceProperty : source.getProperties())
                {
                    DefaultMutableTreeNode sourcePropertyNode = new DefaultMutableTreeNode(sourceProperty);
                    sourceNode.add(sourcePropertyNode);
                }// for
            
                root.add(sourceNode);
            }// for
        
            // Reload tree:   
            treeModel.reload(root);
        }// customEventOccurred
    }// _ModelUpdatedEventHandler
     
        
    /**
     * Inner class implements mouse adapter for data sources panel.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _SourcesTreeMouseAdapter extends MouseAdapter
    {
        
        /**
         * Handles mouse right click on sources panel and configures popup
         * menu's content depending on click context.
         * 
         * @param event Mouse event object
         */
        private void showSourcesTreePopupMenu(MouseEvent event)
        {
            int x = event.getX();
            int y = event.getY();
            JTree tree = (JTree)event.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            
            // Set context dependent items disabled by default:
            removeAllRelatedToSourceMenuItem.setEnabled(false);
            editSelectedSourceMenuItem.setEnabled(false);
            openLinkedDocumentMenuItem.setEnabled(false);
            
            // If one of nodes was clicked:
            if (path != null)
            {    
                tree.setSelectionPath(path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

                if (node.getUserObject() instanceof Source)
                {
                    removeAllRelatedToSourceMenuItem.setEnabled(true);
                    editSelectedSourceMenuItem.setEnabled(true);
                    openLinkedDocumentMenuItem.setEnabled(false);
                                    
                    Source currentSource = (Source)node.getUserObject();
                    for (SourceProperty property : currentSource.getProperties())
                    {
                        if (property.getTypeId() == SourcesPropertiesTypes.DOCUMENT_LINK.ID)
                        { 
                            openLinkedDocumentMenuItem.setEnabled(true);
                            break;
                        }// if
                    }// for
                }// if
            }// if
                        
            DataSourcesPanel.this.dataSourcesListPopupMenu.show(tree, x, y);
        }// showSourcesTreePopupMenu
        
        
        /**
         * Overrides default mouse pressed event handler.
         * 
         * @param event Mouse pressed event object
         */
        @Override
        public void mousePressed(MouseEvent event)
        {
            if (event.isPopupTrigger()) showSourcesTreePopupMenu(event);
	}// mousePressed
	
        
        /**
         * Overrides default mouse released event handler.
         * 
         * @param event Mouse released event object
         */
        @Override
        public void mouseReleased(MouseEvent event) 
        {
            if (event.isPopupTrigger()) showSourcesTreePopupMenu(event);
	}// mouseReleased
    }// SourcesTreeMouseAdapter
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dataSourcesListPopupMenu = new javax.swing.JPopupMenu();
        copyNodeNameToClipboardMenuItem = new javax.swing.JMenuItem();
        openLinkedDocumentMenuItem = new javax.swing.JMenuItem();
        createDataSourcesManuallyMenu = new javax.swing.JMenu();
        createIntoolsExportDataSourceManuallyMenuItem = new javax.swing.JMenuItem();
        createDocumentManuallyMenuItem = new javax.swing.JMenuItem();
        createDcsVariableTableManuallyMenuItem = new javax.swing.JMenuItem();
        createEsdVariableTableManuallyMenuItem = new javax.swing.JMenuItem();
        createFgsVariableTableManuallyMenuItem = new javax.swing.JMenuItem();
        createDataSourceFromMsExcelListMenuItem = new javax.swing.JMenuItem();
        createDataSourceFromYokogawaDcsBackupMenuItem = new javax.swing.JMenuItem();
        createDataSourceFromHoneywellDcsBackupMenuItem = new javax.swing.JMenuItem();
        createDataSourceFromHoneywellScadaExportMenuItem = new javax.swing.JMenuItem();
        editSelectedSourceMenuItem = new javax.swing.JMenuItem();
        removeAllRelatedToSourceMenuItem = new javax.swing.JMenuItem();
        sourcesTreeScrollPane = new javax.swing.JScrollPane();
        sourcesTree = new javax.swing.JTree();

        copyNodeNameToClipboardMenuItem.setText("Copy node name");
        copyNodeNameToClipboardMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyNodeNameToClipboardMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(copyNodeNameToClipboardMenuItem);

        openLinkedDocumentMenuItem.setText("Open linked document");
        openLinkedDocumentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openLinkedDocumentMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(openLinkedDocumentMenuItem);

        createDataSourcesManuallyMenu.setText("Create data source manually");

        createIntoolsExportDataSourceManuallyMenuItem.setText("SPI export...");
        createIntoolsExportDataSourceManuallyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createIntoolsExportDataSourceManuallyMenuItemActionPerformed(evt);
            }
        });
        createDataSourcesManuallyMenu.add(createIntoolsExportDataSourceManuallyMenuItem);

        createDocumentManuallyMenuItem.setText("Document...");
        createDocumentManuallyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDocumentManuallyMenuItemActionPerformed(evt);
            }
        });
        createDataSourcesManuallyMenu.add(createDocumentManuallyMenuItem);

        createDcsVariableTableManuallyMenuItem.setText("DCS variable table...");
        createDcsVariableTableManuallyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDcsVariableTableManuallyMenuItemActionPerformed(evt);
            }
        });
        createDataSourcesManuallyMenu.add(createDcsVariableTableManuallyMenuItem);

        createEsdVariableTableManuallyMenuItem.setText("ESD variable table...");
        createEsdVariableTableManuallyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createEsdVariableTableManuallyMenuItemActionPerformed(evt);
            }
        });
        createDataSourcesManuallyMenu.add(createEsdVariableTableManuallyMenuItem);

        createFgsVariableTableManuallyMenuItem.setText("FGS variable table...");
        createFgsVariableTableManuallyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFgsVariableTableManuallyMenuItemActionPerformed(evt);
            }
        });
        createDataSourcesManuallyMenu.add(createFgsVariableTableManuallyMenuItem);

        dataSourcesListPopupMenu.add(createDataSourcesManuallyMenu);

        createDataSourceFromMsExcelListMenuItem.setText("Create data source from MS Excel sheet...");
        createDataSourceFromMsExcelListMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataSourceFromMsExcelListMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(createDataSourceFromMsExcelListMenuItem);

        createDataSourceFromYokogawaDcsBackupMenuItem.setText("Create extract from Yokogawa DCS backup...");
        createDataSourceFromYokogawaDcsBackupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(createDataSourceFromYokogawaDcsBackupMenuItem);

        createDataSourceFromHoneywellDcsBackupMenuItem.setText("Create extract from Honeywell DCS backup...");
        createDataSourceFromHoneywellDcsBackupMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(createDataSourceFromHoneywellDcsBackupMenuItem);

        createDataSourceFromHoneywellScadaExportMenuItem.setText("Create extract from Honeywell SCADA export...");
        createDataSourceFromHoneywellScadaExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createDataSourceFromHoneywellScadaExportMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(createDataSourceFromHoneywellScadaExportMenuItem);

        editSelectedSourceMenuItem.setText("Edit source...");
        editSelectedSourceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSelectedSourceMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(editSelectedSourceMenuItem);

        removeAllRelatedToSourceMenuItem.setText("Remove all data related to source...");
        removeAllRelatedToSourceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeAllRelatedToSourceMenuItemActionPerformed(evt);
            }
        });
        dataSourcesListPopupMenu.add(removeAllRelatedToSourceMenuItem);

        sourcesTreeScrollPane.setBorder(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        sourcesTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        sourcesTree.setRootVisible(false);
        sourcesTree.setShowsRootHandles(true);
        sourcesTree.setVerifyInputWhenFocusTarget(false);
        sourcesTree.setVisibleRowCount(1);
        sourcesTreeScrollPane.setViewportView(sourcesTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourcesTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sourcesTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Handles "Remove all related to source" menu item click event and triggers 
     * appropriate event with selected source reference data.
     * 
     * @param evt Popup menu item click event object
     */
    private void removeAllRelatedToSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllRelatedToSourceMenuItemActionPerformed
        
        TreePath path = sourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickRemoveSelectedSourceDataMenuItemEvent = new CustomEvent(node.getUserObject());
        trigger(ViewEvent.REMOVE_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, clickRemoveSelectedSourceDataMenuItemEvent);
    }//GEN-LAST:event_removeAllRelatedToSourceMenuItemActionPerformed

    
    /**
     * Handles "Open linked document" menu item click event and triggers 
     * appropriate event with selected source reference data.
     * 
     * @param evt Popup menu item click event object
     */
    private void openLinkedDocumentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openLinkedDocumentMenuItemActionPerformed
        
        TreePath path = sourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickOpenLinkedDocumentEvent = new CustomEvent(node.getUserObject());
        trigger(ViewEvent.OPEN_LINKED_DOCUMENT_MENU_ITEM_CLICK, clickOpenLinkedDocumentEvent);
    }//GEN-LAST:event_openLinkedDocumentMenuItemActionPerformed

    
    /**
     * Handles "Copy selected node name to buffer" menu item click event and 
     * triggers appropriate event with selected node user object's reference
     * data.
     * 
     * @param evt Popup menu item click event object
     */
    private void copyNodeNameToClipboardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNodeNameToClipboardMenuItemActionPerformed
        
        TreePath path = sourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent copyNodeNameToClipboardEvent = new CustomEvent(node.getUserObject());
        trigger(ViewEvent.COPY_NODE_NAME_TO_CLIPBOARD_MENU_ITEM_CLICK, copyNodeNameToClipboardEvent);
    }//GEN-LAST:event_copyNodeNameToClipboardMenuItemActionPerformed

        
    /**
     * Handles "Create data source from Yokogawa DCS backup" menu item click 
     * event and triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromYokogawaDcsBackupMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_YOKOGAWA_DCS_BACKUP_MENU_ITEM_CLICK, clickCreateDataSourceFromYokogawaDcsBackupMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed

    
    /**
     * Handles "Create DCS variable table manually" menu item click event and 
     * triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */    
    private void createDcsVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDcsVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateDcsVariableTableManuallyMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DCS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateDcsVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createDcsVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Handles "Create document source manually" menu item click event and 
     * triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */    
    private void createDocumentManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDocumentManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateDocumentManuallyMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DOCUMENT_MANUALLY_MENU_ITEM_CLICK, clickCreateDocumentManuallyMenuItemEvent);
    }//GEN-LAST:event_createDocumentManuallyMenuItemActionPerformed

    
    /**
     * Handles "Create ESD variable table manually" menu item click event and 
     * triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void createEsdVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createEsdVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateEsdVariableTableManuallyMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_ESD_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateEsdVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createEsdVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Handles "Create FGS variable table manually" menu item click event and 
     * triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void createFgsVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFgsVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateFgsVariableTableManuallyMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_FGS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateFgsVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createFgsVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Handles "Create SPI export data source manually" menu item click event 
     * and triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */    
    private void createIntoolsExportDataSourceManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createIntoolsExportDataSourceManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateIntoolsExportManuallyMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_INTOOLS_EXPORT_MANUALLY_MENU_ITEM_CLICK, clickCreateIntoolsExportManuallyMenuItemEvent);
    }//GEN-LAST:event_createIntoolsExportDataSourceManuallyMenuItemActionPerformed

    
    /**
     * Handles "Create data source from Honeywell DCS backup" menu item click 
     * event and triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromHoneywellDcsExportMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_DCS_EXPORT_MENU_ITEM_CLICK, clickCreateDataSourceFromHoneywellDcsExportMenuItemEvent);  
    }//GEN-LAST:event_createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed

    
    /**
     * Handles "Create data source from Honeywell SCADA database" menu item 
     * click event and triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */       
    private void createDataSourceFromHoneywellScadaExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromHoneywellScadaExportMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromHoneywellScadaExportMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_SCADA_EXPORT_MENU_ITEM_CLICK, clickCreateDataSourceFromHoneywellScadaExportMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromHoneywellScadaExportMenuItemActionPerformed

    
    /**
     * Handles "Create data source from MS Excel book" menu item click event and
     * triggers appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void createDataSourceFromMsExcelListMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromMsExcelListMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromMsExcelListMenuItemEvent = new CustomEvent(new Object());
        trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_MS_EXCEL_LIST_MENU_ITEM_CLICK, clickCreateDataSourceFromMsExcelListMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromMsExcelListMenuItemActionPerformed

    
    /**
     * Handles edit selected data source menu item click event and triggers 
     * appropriate event with selected source reference data.
     * 
     * @param evt Popup menu item click event object
     */    
    private void editSelectedSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectedSourceMenuItemActionPerformed
    
        TreePath path = sourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickEditSelectedSourceDataMenuItemEvent = new CustomEvent(node.getUserObject());
        trigger(ViewEvent.EDIT_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, clickEditSelectedSourceDataMenuItemEvent);
    }//GEN-LAST:event_editSelectedSourceMenuItemActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyNodeNameToClipboardMenuItem;
    private javax.swing.JMenuItem createDataSourceFromHoneywellDcsBackupMenuItem;
    private javax.swing.JMenuItem createDataSourceFromHoneywellScadaExportMenuItem;
    private javax.swing.JMenuItem createDataSourceFromMsExcelListMenuItem;
    private javax.swing.JMenuItem createDataSourceFromYokogawaDcsBackupMenuItem;
    private javax.swing.JMenu createDataSourcesManuallyMenu;
    private javax.swing.JMenuItem createDcsVariableTableManuallyMenuItem;
    private javax.swing.JMenuItem createDocumentManuallyMenuItem;
    private javax.swing.JMenuItem createEsdVariableTableManuallyMenuItem;
    private javax.swing.JMenuItem createFgsVariableTableManuallyMenuItem;
    private javax.swing.JMenuItem createIntoolsExportDataSourceManuallyMenuItem;
    private javax.swing.JPopupMenu dataSourcesListPopupMenu;
    private javax.swing.JMenuItem editSelectedSourceMenuItem;
    private javax.swing.JMenuItem openLinkedDocumentMenuItem;
    private javax.swing.JMenuItem removeAllRelatedToSourceMenuItem;
    private javax.swing.JTree sourcesTree;
    private javax.swing.JScrollPane sourcesTreeScrollPane;
    // End of variables declaration//GEN-END:variables
}// DataSourcesPanel
