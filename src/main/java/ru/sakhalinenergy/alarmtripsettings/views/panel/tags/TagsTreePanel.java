package ru.sakhalinenergy.alarmtripsettings.views.panel.tags;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;


/**
 * Класс реализует вью дерева тегов устройства.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.4
 */
public class TagsTreePanel extends javax.swing.JPanel 
{
    public static final Byte COPY_NODE_NAME_TO_CLIPBOARD_EVENT = 1;
    public Events events = new Events();
    
    private final SettingsSelector model;
    
    
    /**
     * Конструктор вью. Инициализирует компоненты вью и назначает модель.
     */
    public TagsTreePanel(SettingsSelector model) 
    {
        this.model = model;
                
        initComponents();
        this.copyNodeNameToClipboardMenuItem.setIcon(Main.copyIcon);
        
        render();
    }//TagsTreePanel
       
    
    /**
     * Метод формирует дерево тагов.
     */
    private void render()
    {
        //Создаем рендерер дерева тагов устройства:
        this.loopTagsTree.setCellRenderer(new TagsTreeCellRenderer(model));
        this.loopTagsTree.addMouseListener(new TagsTreeMouseAdapter());
                
        //Получаем модель дерева и корневой узел:
        DefaultTreeModel treeModel = (DefaultTreeModel)this.loopTagsTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
        TagsTreeOperator.buildTree(root, model.getEntity());
                
        //Перегружаем дерево:
        treeModel.reload(root);
        
        TreePath tempPath;
        DefaultMutableTreeNode tempNode;
        Object tempNodeObject;
        
        //Раскрываем все узлы дерева:
        for (int i = 0; i < this.loopTagsTree.getRowCount(); i++)
        {
            tempPath = this.loopTagsTree.getPathForRow(i);
            tempNode = (DefaultMutableTreeNode)tempPath.getLastPathComponent();
            tempNodeObject = tempNode.getUserObject();
            
            //Раскрываем все узлы, не являющиеся тагами и таги, у которых есть хотя бы одна уставка:
            if (tempNodeObject.getClass() != Tag.class) this.loopTagsTree.expandRow(i);
            else {
            
                Tag tempTag = (Tag)tempNodeObject;
                for (TagSetting tempSetting : tempTag.getSettings())
                {
                    if (tempSetting.getTypeId() == SettingsTypes.ALARM_LL_SETTING.ID 
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_L_SETTING.ID
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_H_SETTING.ID
                        || tempSetting.getTypeId() == SettingsTypes.ALARM_HH_SETTING.ID) 
                    this.loopTagsTree.expandRow(i);
                }//for
            }//else
        }//for
    }//render
    
    
    /**
     * Внутренний класс, описывающий адаптер мыши дерева тагов текущей выбранной
     * петли.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.0
     */
    private class TagsTreeMouseAdapter extends MouseAdapter
    {
        
        /**
         * Метод - обработчик событий мыши для дерева тагов.
         * 
         * @param   e  Событие мыши
         * @return  void
         */
        private void showSourcesTreePopupMenu(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            JTree tree = (JTree)e.getSource();
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null) return;	
            
            tree.setSelectionPath(path);
            
            TagsTreePanel.this.treePopupMenu.show(tree, x, y);
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
     * Метод рассылает всем подписчикам событие клика по пункту контекстного 
     * меню копирования имени выбранного узла в буфер обмена.
     * 
     * @param   evt    События клика по пункту контекстного меню
     * @return  void
     */
    private void copyNodeNameToClipboardMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyNodeNameToClipboardMenuItemActionPerformed
        
        TreePath path = this.loopTagsTree.getSelectionPath();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
        CustomEvent copyNodeNameToClipboardEvent = new CustomEvent(node.getUserObject());
        this.events.trigger(ViewEvent.COPY_NODE_NAME_TO_CLIPBOARD, copyNodeNameToClipboardEvent);    
    }//GEN-LAST:event_copyNodeNameToClipboardMenuItemActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem copyNodeNameToClipboardMenuItem;
    private javax.swing.JTree loopTagsTree;
    private javax.swing.JScrollPane tagDetailsTreeScrollPane;
    private javax.swing.JPopupMenu treePopupMenu;
    // End of variables declaration//GEN-END:variables
}
