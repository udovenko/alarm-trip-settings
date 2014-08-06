package ru.sakhalinenergy.alarmtripsettings.views.DataSourcesPanel;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.JTree;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesPropertiesTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.SourceProperty;


/**
 * Класс описывает панень для отображения дерева документов/источников данных,
 * из которых получены таги текущего выбранного объекта.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.5
 */
public class DataSourcesPanel extends javax.swing.JPanel 
{
   
    public Events events = new Events();
    private final LoopsTableObservable model;    
     
    
    /**
     * Конструктор панели. Вызывает инициализацию всех элементов.
     */
    public DataSourcesPanel(LoopsTableObservable model) 
    {
        initComponents();
        this.SourcesTree.addMouseListener(new SourcesTreeMouseAdapter());
        this.model = model;
        this.copyNodeNameToClipboardMenuItem.setIcon(Main.copyIcon);
        this.openLinkedDocumentMenuItem.setIcon(Main.linkIcon);
        this.createDataSourcesManuallyMenu.setIcon(Main.addSourceIcon);
        this.createIntoolsExportDataSourceManuallyMenuItem.setIcon(Main.intoolsIcon);
        this.createDocumentManuallyMenuItem.setIcon(Main.documentsIcon);
        this.createDataSourceFromMsExcelListMenuItem.setIcon(Main.excelIcon);
        this.createDcsVariableTableManuallyMenuItem.setIcon(Main.dcsIcon);
        this.createEsdVariableTableManuallyMenuItem.setIcon(Main.esdIcon);
        this.createFgsVariableTableManuallyMenuItem.setIcon(Main.fgsIcon);
        this.createDataSourceFromYokogawaDcsBackupMenuItem.setIcon(Main.yokogawaIcon);
        this.createDataSourceFromHoneywellDcsBackupMenuItem.setIcon(Main.honeywellIcon);
        this.createDataSourceFromHoneywellScadaExportMenuItem.setIcon(Main.honeywellIcon);
        this.editSelectedSourceMenuItem.setIcon(Main.editIcon);
        this.removeAllRelatedToSourceMenuItem.setIcon(Main.removeIcon);
        
        //Подписываеся на события модели:
        this.model.on(CollectionEvent.LOOPS_READ, new _ModelUpdatedEventHandler());
    }//DataSourcesPanel
       
        
    /**
     * Внутренний класс - обработчик события обновления модели. 
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class _ModelUpdatedEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            SourcesTree.setCellRenderer(new DataSourcesTreeCellRenderer());
                
            //Получаем модель дерева и корневой узел:
            DefaultTreeModel treeModel = (DefaultTreeModel)SourcesTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
            root.removeAllChildren();
            List<Source> sortedSources = model.getSources();

            //Сортируем коллекуию по типу источника и по приоритету:
            Collections.sort(sortedSources, new Comparator<Source>()
            {
                @Override
                public int compare(Source source1, Source source2)
                {
                    if (source1.getTypeId() != source2.getTypeId()) return source1.getTypeId() - source2.getTypeId();
                    if (source1.getTypeId() == source2.getTypeId()) return source2.getPriority() - source1.getPriority();
                    return 0;
                }//compare
            });//sort
        
            //Добавляем узлы источников:
            for (Source source : sortedSources)
            {
                DefaultMutableTreeNode sourceNode = new DefaultMutableTreeNode(source);
                       
                //Добавляем в текущий узел источника все узлы его свойств:
                for (SourceProperty sourceProperty : source.getProperties())
                {
                    DefaultMutableTreeNode sourcePropertyNode = new DefaultMutableTreeNode(sourceProperty);
                    sourceNode.add(sourcePropertyNode);
                }//for
            
                root.add(sourceNode);
            }//for
        
            //Перегружаем дерево:   
            treeModel.reload(root);
        }//customEventOccurred
    }//_ModelUpdatedEventHandler
     
        
    /**
     * Внутренний класс, описывающий адаптер мыши дерева истосников данных 
     * текущего выбранного объекта.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.2
     */
    private class SourcesTreeMouseAdapter extends MouseAdapter
    {
        /**
         * Метод - обработчик событий мыши для дерева источников данных. 
         * Вызывает контектсное меню только для узлов заголовков источников
         * данных.
         * 
         * @param e Событие мыши
         */
        private void showSourcesTreePopupMenu(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            JTree tree = (JTree)e.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            
            if (path != null)
            {    
                tree.setSelectionPath(path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

                if (node.getUserObject().getClass() == Source.class)
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
                        }//if
                    }//for
                } else {
            
                    removeAllRelatedToSourceMenuItem.setEnabled(false);
                    editSelectedSourceMenuItem.setEnabled(false);
                    openLinkedDocumentMenuItem.setEnabled(false);
                }//else
                
            } else {

                removeAllRelatedToSourceMenuItem.setEnabled(false);
                editSelectedSourceMenuItem.setEnabled(false);
                openLinkedDocumentMenuItem.setEnabled(false);
            }//else
            
            
            DataSourcesPanel.this.dataSourcesListPopupMenu.show(tree, x, y);
        }//myPopupEvent
        
        
        /**
         * Метод перегружает дефолтный обработчик адаптера нажатия клавиши мыши.
         * 
         * @param   e     Событие нажатия клавиши мыши
         * @return  void
         */
        @Override
        public void mousePressed(MouseEvent e)
        {
            if (e.isPopupTrigger()) showSourcesTreePopupMenu(e);
	}//mousePressed
	
        
        /**
         * Метод перегружает дефолтный обработчик адаптера отпускания клавиши 
         * мыши.
         * 
         * @param   e     Событие нажатия клавиши мыши
         * @return  void
         */
        @Override
        public void mouseReleased(MouseEvent e) 
        {
            if (e.isPopupTrigger()) showSourcesTreePopupMenu(e);
	}//mouseReleased
    }//SourcesTreeMouseAdapter
    
    
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
        SourcesTree = new javax.swing.JTree();

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
        SourcesTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        SourcesTree.setRootVisible(false);
        SourcesTree.setShowsRootHandles(true);
        SourcesTree.setVerifyInputWhenFocusTarget(false);
        SourcesTree.setVisibleRowCount(1);
        sourcesTreeScrollPane.setViewportView(SourcesTree);

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
     * Метод рассылает всем подписчикам событие клика по пункту контектсного 
     * меню удаления всех данных, связанных с текущим источником данных и 
     * выбранным объектом. В контексте события передается экземпляр выбранного
     * источника данных.
     *
     * @param evt События клика по пункту контекстного меню
     */
    private void removeAllRelatedToSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeAllRelatedToSourceMenuItemActionPerformed
        
        TreePath path = this.SourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickRemoveSelectedSourceDataMenuItemEvent = new CustomEvent(node.getUserObject());
        this.events.trigger(ViewEvent.REMOVE_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, clickRemoveSelectedSourceDataMenuItemEvent);
    }//GEN-LAST:event_removeAllRelatedToSourceMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню перехода к связанному документу.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void openLinkedDocumentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openLinkedDocumentMenuItemActionPerformed
        
        TreePath path = this.SourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickOpenLinkedDocumentEvent = new CustomEvent(node.getUserObject());
        this.events.trigger(ViewEvent.OPEN_LINKED_DOCUMENT_MENU_ITEM_CLICK, clickOpenLinkedDocumentEvent);
    }//GEN-LAST:event_openLinkedDocumentMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню копирования имени выбранного узла в буфер обмена.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void copyNodeNameToClipboardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNodeNameToClipboardMenuItemActionPerformed
        
        TreePath path = this.SourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent copyNodeNameToClipboardEvent = new CustomEvent(node.getUserObject());
        this.events.trigger(ViewEvent.COPY_NODE_NAME_TO_CLIPBOARD_MENU_ITEM_CLICK, copyNodeNameToClipboardEvent);
    }//GEN-LAST:event_copyNodeNameToClipboardMenuItemActionPerformed

        
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - таблицы переменных DCS из бэкапа
     * DCS Yokogawa.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromYokogawaDcsBackupMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_YOKOGAWA_DCS_BACKUP_MENU_ITEM_CLICK, clickCreateDataSourceFromYokogawaDcsBackupMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromYokogawaDcsBackupMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - таблицы переменных DCS вручную.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createDcsVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDcsVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateDcsVariableTableManuallyMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DCS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateDcsVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createDcsVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - документа вручную.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createDocumentManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDocumentManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateDocumentManuallyMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DOCUMENT_MANUALLY_MENU_ITEM_CLICK, clickCreateDocumentManuallyMenuItemEvent);
    }//GEN-LAST:event_createDocumentManuallyMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - таблицы переменных ESD вручную.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createEsdVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createEsdVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateEsdVariableTableManuallyMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_ESD_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateEsdVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createEsdVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - таблицы переменных FGS вручную.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createFgsVariableTableManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFgsVariableTableManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateFgsVariableTableManuallyMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_FGS_VARIABLE_TABLE_MANUALLY_MENU_ITEM_CLICK, clickCreateFgsVariableTableManuallyMenuItemEvent);
    }//GEN-LAST:event_createFgsVariableTableManuallyMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных - экспорта из SPI вручную.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void createIntoolsExportDataSourceManuallyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createIntoolsExportDataSourceManuallyMenuItemActionPerformed
        
        CustomEvent clickCreateIntoolsExportManuallyMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_INTOOLS_EXPORT_MANUALLY_MENU_ITEM_CLICK, clickCreateIntoolsExportManuallyMenuItemEvent);
    }//GEN-LAST:event_createIntoolsExportDataSourceManuallyMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных из файла экспорта DCS Honeywell.
     * 
     * @param evt События клика по пункту контекстного меню
     * @return void
     */    
    private void createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromHoneywellDcsExportMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_DCS_EXPORT_MENU_ITEM_CLICK, clickCreateDataSourceFromHoneywellDcsExportMenuItemEvent);  
    }//GEN-LAST:event_createDataSourceFromHoneywellDcsBackupMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных из файла экспорта Honeywell SCADA.
     * 
     * @param evt События клика по пункту контекстного меню
     * @return void
     */   
    private void createDataSourceFromHoneywellScadaExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromHoneywellScadaExportMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromHoneywellScadaExportMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_HONEYWELL_SCADA_EXPORT_MENU_ITEM_CLICK, clickCreateDataSourceFromHoneywellScadaExportMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromHoneywellScadaExportMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для создания источника данных из листа книги MS Excel.
     * 
     * @param evt События клика по пункту контекстного меню
     * @return void
     */   
    private void createDataSourceFromMsExcelListMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createDataSourceFromMsExcelListMenuItemActionPerformed
        
        CustomEvent clickCreateDataSourceFromMsExcelListMenuItemEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CREATE_DATA_SOURCE_FROM_MS_EXCEL_LIST_MENU_ITEM_CLICK, clickCreateDataSourceFromMsExcelListMenuItemEvent);
    }//GEN-LAST:event_createDataSourceFromMsExcelListMenuItemActionPerformed

    
    /**
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню для редактировагния выбранного источника данных, содержащее ссылку
     * на выбранный источник данных.
     * 
     * @param evt События клика по пункту контекстного меню
     * @return void
     */   
    private void editSelectedSourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSelectedSourceMenuItemActionPerformed
    
        TreePath path = this.SourcesTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent clickEditSelectedSourceDataMenuItemEvent = new CustomEvent(node.getUserObject());
        this.events.trigger(ViewEvent.EDIT_SELECTED_SOURCE_DATA_MENU_ITEM_CLICK, clickEditSelectedSourceDataMenuItemEvent);
    }//GEN-LAST:event_editSelectedSourceMenuItemActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree SourcesTree;
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
    private javax.swing.JScrollPane sourcesTreeScrollPane;
    // End of variables declaration//GEN-END:variables
}//DataSourcesPanel
