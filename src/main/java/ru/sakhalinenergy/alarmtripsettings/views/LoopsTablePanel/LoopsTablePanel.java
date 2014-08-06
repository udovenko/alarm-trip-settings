package ru.sakhalinenergy.alarmtripsettings.views.LoopsTablePanel;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Collections;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ru.sakhalinenergy.alarmtripsettings.models.config.LoopsTablePanelSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.LoopsTableObservable;


/**
 * Класс реализует панель для отображения таблицы петель для выбранного объекта 
 * иерархии в дереве ассетов.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.2
 */
public class LoopsTablePanel extends javax.swing.JPanel 
{
    public Events events = new Events();
    
    private final LoopsTableObservable model;
    private final LoopsTablePanelSettingsObservable config;
        
    
    /**
     * Конструктор вью. Инициализирует все необходимые компоненты.
     */
    public LoopsTablePanel(LoopsTableObservable model, LoopsTablePanelSettingsObservable config) 
    {
        //Получаем коллекцию и инициализируем компоненты:
        this.model = model;
        this.config = config;
        
        //Подписываеся на события модели:
        this.model.on(CollectionEvent.LOOPS_READ, new _ModelUpdatedEventHandler());
                        
        initComponents();
        
        //Устанавливаем иконки пунктов контентестного меню:
        this.splitLoopMenuItem.setIcon(Main.splitLoopIcon);
        this.mergeLoopMenuItem.setIcon(Main.mergeLoopIcon);
        this.mergeAllLoopsMenuItem.setIcon(Main.mergeAllLoopsIcon);
    }//DevicesTableAndTaskbarPanel
    
    
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
            //Удаляем старый подписчик на события выбора строки таблицы::
            ListSelectionModel cellSelectionModel = loopsTable.getSelectionModel();
            loopsTable.setColumnModel(new DefaultTableColumnModel());
                
            //Создаем экземпляр модели таблицы:
            LoopsTableModel tableModel = new LoopsTableModel(model.getWrappedLoops());
            loopsTable.setModel(tableModel);
               
            //Назначаем всем колонкам таблицы рендерер текущего набора устройств:
            for (TableColumn column : Collections.list(loopsTable.getColumnModel().getColumns()))
            {
                column.setHeaderRenderer(new LoopsTableColumnHeaderRenderer());
                column.setCellRenderer(new LoopsTableCellRenderer(model));
            }//for
        
            //Создаем сортировщик:
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
            sorter.addRowSorterListener(new LoopsTablePanel.LoopsTableSotringListener());
            loopsTable.setRowSorter(sorter);
            loopsTable.setUpdateSelectionOnSort(false);              
     
            //Создаем подписчик на событие смены выбранной строки таблицы устройств заново:
            cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            cellSelectionModel.addListSelectionListener(new LoopsTablePanel.LoopsTableSelectionListener());
        
            loopsTable.addMouseListener(new LoopsTableMouseAdapter());
        
            //Применяем конфигурацию:
            _applyConfig();
        }//customEventOccurred
    }//_PlantCodeSetEventHandler
    
    
    /**
     * Возвращает идентификатор выбранного контура в таблице.
     * 
     * @return Идентификатор выбранного контура
     */
    public String getSelectedLoop()
    {
        int selectedRow = this.loopsTable.getSelectedRow();
        Loop selectedDevice = model.getLoops().get(this.loopsTable.convertRowIndexToModel(selectedRow));
        
        return Integer.toString(selectedDevice.getId());
    }//getSelectedLoop
    
    
    /**
     * Метод восстанавливает выбраннрую строку таблицы устройств и прокручивает
     * таблицы к выбранной строке.
     * 
     * @return  void
     */
    private void _applyConfig()
    {
        for (int i=0; i < this.loopsTable.getRowCount(); i++)
        {
            Loop device = model.getLoops().get(this.loopsTable.convertRowIndexToModel(i));
            
            if (device.getId() == Integer.parseInt(config.getSelectedLoop()))
            {
                this.loopsTable.setRowSelectionInterval(i, i);
                this._scrollTableToSelectedCell(i, 0);
                break;
            }//if
        }//for
    }//_applyConfig
       
    
    /**
     * Метод прокручивает таблицу устрорйств к выбранной ячейке.
     * 
     * @param   rowIndex   Индекс строки
     * @param   vColIndex  Индекс столбца
     * @return  void 
     */
    private void _scrollTableToSelectedCell(int rowIndex, int vColIndex)
    {
        if (!(this.loopsTable.getParent() instanceof JViewport)) return;
        
        JViewport viewport = (JViewport)this.loopsTable.getParent();
        Rectangle rect = this.loopsTable.getCellRect(rowIndex, vColIndex, true);
        Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        this.loopsTable.scrollRectToVisible(rect);
    }//scrollTableToSelectedCell
    
    
    /**
     * Метод устанавливает фильтр списка устройств по заданной подстроке поиска.
     * Найденными окажутся все устройства, маска которых содержит такую 
     * подстроку.
     * 
     * @param   searchString  Подстрока поиска
     * @return  void
     */
    public void setSearchDevicesFilter(String searchString)
    {
        RowFilter rowFilter = null;
        
        //Пробуем применить фильтр к сортировщику таблицы, если она существует:
        try 
        {
            rowFilter = RowFilter.regexFilter(searchString, 0);
            TableRowSorter<TableModel> rowSorter = (TableRowSorter<TableModel>)this.loopsTable.getRowSorter();
            rowSorter.setRowFilter(rowFilter);
        } catch (Exception exception) {
            
            return;
        }//catch
    }//setSearchDevicesFilter
    
    
    /**
     * Внутренний класс-обработчик события выбора устройства в таблице.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.1
     */
    private class LoopsTableSelectionListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(ListSelectionEvent e)
        {
            int selectedRow = loopsTable.getSelectedRow();
            SettingsSelector selectedLoopWraper = model.getWrappedLoops().get(loopsTable.convertRowIndexToModel(selectedRow));
            CustomEvent loopSelectionEvent = new CustomEvent(selectedLoopWraper);
            LoopsTablePanel.this.events.trigger(ViewEvent.CHANGE_LOOPS_TABLE_SELECTION, loopSelectionEvent);
        }//valueChanged
    };//addListSelectionListener
  
    
    /**
     * Внутренний класс-обработчик события сортировки таблицы устройств.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.1
     */
    private class LoopsTableSotringListener implements RowSorterListener
    {
        @Override
        public void sorterChanged(RowSorterEvent rowsorterevent) 
        {
            if (rowsorterevent.getType() == RowSorterEvent.Type.SORTED)
            {
                if (LoopsTablePanel.this.loopsTable.getRowCount() > 0)
                {
                    LoopsTablePanel.this.loopsTable.setRowSelectionInterval(0, 0);
                }//if
            }//if
        }//sorterChanged
    };//RowSorterListener
    
    
    /**
     * Внутренний класс, описывающий адаптер мыши таблицы контуров для текущего
     * выбранного объекта.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.0
     */
    private class LoopsTableMouseAdapter extends MouseAdapter
    {
        
        /**
         * Метод - обработчик событий мыши для таблицы контуров. Вызывает 
         * контектсное меню для выбранной строки таблицы.
         * 
         * @param   e  Событие мыши
         * @return  void
         */
        private void showLoopsTablePopupMenu(MouseEvent e)
        {
            int x = e.getX();
            int y = e.getY();
            JTable loopsTable = (JTable)e.getSource();
            int rowIndex = loopsTable.rowAtPoint(e.getPoint());
            
            //Выбираем строку, по которой сделан клик:
            loopsTable.setRowSelectionInterval(rowIndex, rowIndex);
          
            //Получаем экземпляр выбранной петли:
            int selectedLoopIndex = loopsTable.convertRowIndexToModel(rowIndex);
            Loop selectedLoop = model.getLoops().get(selectedLoopIndex);
            
            //Блокируем или разрешаем пукт меню для объединения петли в зависимости от того, разделена она или нет: 
            //if (selectedLoop.getSplit()) LoopsTablePanel.this.mergeLoopMenuItem.setEnabled(true);
            //else LoopsTablePanel.this.mergeLoopMenuItem.setEnabled(false);
             
            //Отображаем контекстное меню:
            LoopsTablePanel.this.loopsTablePopupMenu.show(loopsTable, x, y);
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
            if (e.isPopupTrigger()) showLoopsTablePopupMenu(e);
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
            if (e.isPopupTrigger()) showLoopsTablePopupMenu(e);
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
     * 
     * 
     */
    private void devicesSearchTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_devicesSearchTextFieldKeyReleased

        CustomEvent selectionEvent = new CustomEvent(this.devicesSearchTextField.getText());
        this.events.trigger(ViewEvent.CHANGE_LOOPS_SEARCH_STRING, selectionEvent);
    }//GEN-LAST:event_devicesSearchTextFieldKeyReleased

    
    /**
     * Метод обрабатывает клик по пункту контекстного меню "Split loop..." и 
     * рассылает всем подписчикам событие с контекстом информации о выбранных
     * объектах таблицы.
     * 
     * @param   evt   Событие клика по пункту контекстного меню
     * @return  void 
     */
    private void splitLoopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_splitLoopMenuItemActionPerformed
        
        //Получаем индекс петли в коллекции:
        int selectedRow = loopsTable.getSelectedRow();
        int selectedLoopIndex = loopsTable.convertRowIndexToModel(selectedRow);
        
        // Get selected loop instance:
        Loop selectedLoop = model.getLoops().get(selectedLoopIndex);
        
        //Создаем и рассылаем событие:
        CustomEvent splitLoopMenuItemClickEvent = new CustomEvent(selectedLoop);
        this.events.trigger(ViewEvent.SPLIT_LOOP_MENU_ITEM_CLICK, splitLoopMenuItemClickEvent);
    }//GEN-LAST:event_splitLoopMenuItemActionPerformed

    
    /**
     * Метод обрабатывает клик по пункту контекстного меню "Merge loop..." и 
     * рассылает всем подписчикам событие с контекстом информации о выбранных
     * объектах таблицы.
     * 
     * @param   evt   Событие клика по пункту контекстного меню
     * @return  void 
     */
    private void mergeLoopMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeLoopMenuItemActionPerformed
        
        //Получаем индекс петли в коллекции:
        int selectedRow = this.loopsTable.getSelectedRow();
        int selectedLoopIndex = this.loopsTable.convertRowIndexToModel(selectedRow);
        
        //Создаем и рассылаем событие:
        CustomEvent mergeLoopMenuItemclickEvent = new CustomEvent(selectedLoopIndex);
        this.events.trigger(ViewEvent.MERGE_LOOP_MENU_ITEM_CLICK, mergeLoopMenuItemclickEvent);
    }//GEN-LAST:event_mergeLoopMenuItemActionPerformed

    
    /**
     * Метод обрабатывает клик по пункту контекстного меню "Merge all loops..." и 
     * рассылает всем подписчикам событие с контекстом информации о выбранных
     * объектах таблицы.
     * 
     * @param   evt   Событие клика по пункту контекстного меню
     * @return  void 
     */
    private void mergeAllLoopsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mergeAllLoopsMenuItemActionPerformed
        
        CustomEvent mergeAllLoopsMenuItemclickEvent = new CustomEvent(this);
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
}
