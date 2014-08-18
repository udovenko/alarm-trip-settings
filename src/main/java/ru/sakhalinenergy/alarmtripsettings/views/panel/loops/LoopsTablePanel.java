package ru.sakhalinenergy.alarmtripsettings.views.panel.loops;

import java.util.Collections;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.config.LoopsTablePanelSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;
import ru.sakhalinenergy.alarmtripsettings.views.panel.Panel;


/**
 * Implements panel for rendering loops table for selected PAU object with 
 * selected settings highlight.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public class LoopsTablePanel extends Panel 
{
    
    private final LoopsTableObservable model;
    private final LoopsTablePanelSettingsObservable config;
        
    
    /**
     * Public constructor. Sets loops table and configuration models and 
     * initializes components.
     * 
     * @param model Loops table model instance
     * @param config Panel configuration object
     */
    public LoopsTablePanel(LoopsTableObservable model, LoopsTablePanelSettingsObservable config) 
    {
        // Set loops table and configuration models:
        this.model = model;
        this.config = config;
        
        // Subscribe on model's events:
        model.on(CollectionEvent.LOOPS_READ, new _ModelUpdatedEventHandler());
                        
        initComponents();
        
        // Set popup menu items icons:
        splitLoopMenuItem.setIcon(Main.splitLoopIcon);
        mergeLoopMenuItem.setIcon(Main.mergeLoopIcon);
        mergeAllLoopsMenuItem.setIcon(Main.mergeAllLoopsIcon);
    }// LoopsTablePanel
    
    
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
            // Set table column model:
            loopsTable.setColumnModel(new DefaultTableColumnModel());
                
            // Set table model:
            LoopsTableModel tableModel = new LoopsTableModel(model.getWrappedLoops());
            loopsTable.setModel(tableModel);
               
            // Set header and cell renderers for all table columns:
            for (TableColumn column : Collections.list(loopsTable.getColumnModel().getColumns()))
            {
                column.setHeaderRenderer(new LoopsTableColumnHeaderRenderer());
                column.setCellRenderer(new LoopsTableCellRenderer(model));
            }//for
        
            // Set table sorter:
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
            sorter.addRowSorterListener(new LoopsTablePanel.LoopsTableSotringListener());
            loopsTable.setRowSorter(sorter);
            loopsTable.setUpdateSelectionOnSort(false);              
     
            // Set selection mode and selection event listener:
            ListSelectionModel cellSelectionModel = loopsTable.getSelectionModel();
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new LoopsTablePanel.LoopsTableSelectionListener());
        
            loopsTable.addMouseListener(new LoopsTableMouseAdapter());
        
            // Restore panel configuration:
            _applyConfig();
        }// customEventOccurred
    }// _ModelUpdatedEventHandler
    
    
    /**
     * Returns selected loop identifier.
     * 
     * @return Selected loop identifier
     */
    public String getSelectedLoop()
    {
        int selectedRow = loopsTable.getSelectedRow();
        Loop selectedDevice = model.getLoops().get(loopsTable.convertRowIndexToModel(selectedRow));
        
        return Integer.toString(selectedDevice.getId());
    }// getSelectedLoop
    
    
    /**
     * Restores loops table panel configuration from configuration object.
     */
    private void _applyConfig()
    {
        // Restore loop row selection:
        for (int i = 0; i < loopsTable.getRowCount(); i++)
        {
            Loop device = model.getLoops().get(loopsTable.convertRowIndexToModel(i));
            
            if (device.getId() == Integer.parseInt(config.getSelectedLoop()))
            {
                loopsTable.setRowSelectionInterval(i, i);
                _scrollTableToSelectedCell(i, 0);
                break;
            }// if
        }// for
    }// _applyConfig
       
    
    /**
     * Scrolls table to selected cell.
     * 
     * @param rowIndex Selected row index
     * @param columnIndex Selected column index
     */
    private void _scrollTableToSelectedCell(int rowIndex, int columnIndex)
    {
        if (!(loopsTable.getParent() instanceof JViewport)) return;
        
        JViewport viewport = (JViewport)loopsTable.getParent();
        Rectangle rect = loopsTable.getCellRect(rowIndex, columnIndex, true);
        Point point = viewport.getViewPosition();
        rect.setLocation(rect.x - point.x, rect.y - point.y);

        loopsTable.scrollRectToVisible(rect);
    }// _scrollTableToSelectedCell
    
    
    /**
     * Applies loops filter by given search substring. Leaves only loops with 
     * names contain given substring.
     * 
     * @param searchString Loops search substring
     */
    public void setSearchLoopsFilter(String searchString)
    {
        try // Try to apply filter to current table sorter:
        {
            RowFilter rowFilter = RowFilter.regexFilter(searchString, 0);
            TableRowSorter<TableModel> rowSorter = (TableRowSorter<TableModel>)loopsTable.getRowSorter();
            rowSorter.setRowFilter(rowFilter);
        } catch (Exception exception){}
    }// setSearchLoopsFilter
    
    
    /**
     * Inner class - handler for change loops table selection event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class LoopsTableSelectionListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
            int selectedRow = loopsTable.getSelectedRow();
            SettingsSelector selectedLoopWraper = model.getWrappedLoops().get(loopsTable.convertRowIndexToModel(selectedRow));
            CustomEvent loopSelectionEvent = new CustomEvent(selectedLoopWraper);
            trigger(ViewEvent.CHANGE_LOOPS_TABLE_SELECTION, loopSelectionEvent);
        }// valueChanged
    };// LoopsTableSelectionListener
  
    
    /**
     * Inner class - handler for loops table sorting event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    private class LoopsTableSotringListener implements RowSorterListener
    {
        @Override
        public void sorterChanged(RowSorterEvent rowsorterevent) 
        {
            if (rowsorterevent.getType() == RowSorterEvent.Type.SORTED)
            {
                if (loopsTable.getRowCount() > 0)
                {
                    loopsTable.setRowSelectionInterval(0, 0);
                }// if
            }// if
        }// sorterChanged
    };// LoopsTableSotringListener
    
    
    /**
     * Inner class implements loops table mouse adapter.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class LoopsTableMouseAdapter extends MouseAdapter
    {
        
        /**
         * Handles right mouse click on loops table and shows popup menu.
         * 
         * @param event Mouse event object
         */
        private void showLoopsTablePopupMenu(MouseEvent event)
        {
            int x = event.getX();
            int y = event.getY();
            JTable loopsTable = (JTable)event.getSource();
            int rowIndex = loopsTable.rowAtPoint(event.getPoint());
            
            // Get clicked table row:
            loopsTable.setRowSelectionInterval(rowIndex, rowIndex);
          
            // Get selected loop instance:
            int selectedLoopIndex = loopsTable.convertRowIndexToModel(rowIndex);
            Loop selectedLoop = model.getLoops().get(selectedLoopIndex);
            
            // Enable/disable loop merge menu item depending on loop split state: 
            if (model.isLoopSplit(selectedLoop)) mergeLoopMenuItem.setEnabled(true);
            else mergeLoopMenuItem.setEnabled(false);
             
            // Show popup menu:
            loopsTablePopupMenu.show(loopsTable, x, y);
        }// showLoopsTablePopupMenu
        
        
        /**
         * Handles mouse pressed event.
         * 
         * @param event Mouse pressed event object
         */
        @Override
        public void mousePressed(MouseEvent event)
        {
            if (event.isPopupTrigger()) showLoopsTablePopupMenu(event);
	}// mousePressed
	
        
        /**
         * Handles mouse released event.
         * 
         * @param event Mouse released event object
         */
        @Override
        public void mouseReleased(MouseEvent event) 
        {
            if (event.isPopupTrigger()) showLoopsTablePopupMenu(event);
	}// mouseReleased
    }// LoopsTableMouseAdapter
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loopsTablePopupMenu = new javax.swing.JPopupMenu();
        splitLoopMenuItem = new javax.swing.JMenuItem();
        mergeLoopMenuItem = new javax.swing.JMenuItem();
        mergeAllLoopsMenuItem = new javax.swing.JMenuItem();
        devicesTableToolBarPanel = new javax.swing.JPanel();
        devicesSearchTextFieldLabel = new javax.swing.JLabel();
        devicesSearchTextField = new javax.swing.JTextField();
        devicesTableScrollPane = new javax.swing.JScrollPane();
        loopsTable = new javax.swing.JTable();

        splitLoopMenuItem.setText("Split loop...");
        splitLoopMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                splitLoopMenuItemActionPerformed(evt);
            }
        });
        loopsTablePopupMenu.add(splitLoopMenuItem);

        mergeLoopMenuItem.setText("Merge loop...");
        mergeLoopMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeLoopMenuItemActionPerformed(evt);
            }
        });
        loopsTablePopupMenu.add(mergeLoopMenuItem);

        mergeAllLoopsMenuItem.setText("Merge all splited loops...");
        mergeAllLoopsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mergeAllLoopsMenuItemActionPerformed(evt);
            }
        });
        loopsTablePopupMenu.add(mergeAllLoopsMenuItem);

        devicesSearchTextFieldLabel.setText("Loops search substring:");

        devicesSearchTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                devicesSearchTextFieldKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout devicesTableToolBarPanelLayout = new javax.swing.GroupLayout(devicesTableToolBarPanel);
        devicesTableToolBarPanel.setLayout(devicesTableToolBarPanelLayout);
        devicesTableToolBarPanelLayout.setHorizontalGroup(
            devicesTableToolBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(devicesTableToolBarPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(devicesSearchTextFieldLabel)
                .addGap(3, 3, 3)
                .addComponent(devicesSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(460, Short.MAX_VALUE))
        );
        devicesTableToolBarPanelLayout.setVerticalGroup(
            devicesTableToolBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, devicesTableToolBarPanelLayout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addGroup(devicesTableToolBarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(devicesSearchTextFieldLabel)
                    .addComponent(devicesSearchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        loopsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        devicesTableScrollPane.setViewportView(loopsTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(devicesTableToolBarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(devicesTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(devicesTableToolBarPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 408, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(devicesTableScrollPane)))
        );
    }// </editor-fold>//GEN-END:initComponents

        
    /**
     * Handles loops search field text changed event and triggers an event with
     * selected new search substring text.
     * 
     * @param evt Loops search text field event object
     */
    private void devicesSearchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_devicesSearchTextFieldKeyReleased

        CustomEvent selectionEvent = new CustomEvent(devicesSearchTextField.getText());
        trigger(ViewEvent.CHANGE_LOOPS_SEARCH_STRING, selectionEvent);
    }//GEN-LAST:event_devicesSearchTextFieldKeyReleased

    
    /**
     * Handles "Split loop" popup menu item click event and triggers appropriate
     * event with selected selected loop instance data.
     * 
     * @param evt Popup menu item click event object
     */
    private void splitLoopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splitLoopMenuItemActionPerformed
        
        // Get selected loop index:
        int selectedRow = loopsTable.getSelectedRow();
        int selectedLoopIndex = loopsTable.convertRowIndexToModel(selectedRow);
        
        // Get selected loop instance:
        Loop selectedLoop = model.getLoops().get(selectedLoopIndex);
        
        // Trigger an event with loop instance data:
        CustomEvent splitLoopMenuItemClickEvent = new CustomEvent(selectedLoop);
        trigger(ViewEvent.SPLIT_LOOP_MENU_ITEM_CLICK, splitLoopMenuItemClickEvent);
    }//GEN-LAST:event_splitLoopMenuItemActionPerformed

    
    /**
     * Handles "Merge loop" popup menu item click event and triggers appropriate
     * event with selected selected loop instance data.
     * 
     * @param evt Popup menu item click event object
     */
    private void mergeLoopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeLoopMenuItemActionPerformed
        
        // Get selected loop index:
        int selectedRow = loopsTable.getSelectedRow();
        int selectedLoopIndex = loopsTable.convertRowIndexToModel(selectedRow);
        
        // Get selected loop instance:
        Loop selectedLoop = model.getLoops().get(selectedLoopIndex);
        
        // Trigger an event with loop instance data:
        CustomEvent mergeLoopMenuItemclickEvent = new CustomEvent(selectedLoop);
        this.events.trigger(ViewEvent.MERGE_LOOP_MENU_ITEM_CLICK, mergeLoopMenuItemclickEvent);
    }//GEN-LAST:event_mergeLoopMenuItemActionPerformed

    
    /**
     * Handles "Merge all loops" popup menu item click event and triggers 
     * appropriate event for all subscribers.
     * 
     * @param evt Popup menu item click event object
     */
    private void mergeAllLoopsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeAllLoopsMenuItemActionPerformed
        
        CustomEvent mergeAllLoopsMenuItemclickEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.MERGE_ALL_LOOPS_MENU_ITEM_CLICK, mergeAllLoopsMenuItemclickEvent);
    }//GEN-LAST:event_mergeAllLoopsMenuItemActionPerformed
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField devicesSearchTextField;
    private javax.swing.JLabel devicesSearchTextFieldLabel;
    private javax.swing.JScrollPane devicesTableScrollPane;
    private javax.swing.JPanel devicesTableToolBarPanel;
    private javax.swing.JTable loopsTable;
    private javax.swing.JPopupMenu loopsTablePopupMenu;
    private javax.swing.JMenuItem mergeAllLoopsMenuItem;
    private javax.swing.JMenuItem mergeLoopMenuItem;
    private javax.swing.JMenuItem splitLoopMenuItem;
    // End of variables declaration//GEN-END:variables
}// LoopsTablePanel
