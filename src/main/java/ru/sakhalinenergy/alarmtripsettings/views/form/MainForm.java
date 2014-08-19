package ru.sakhalinenergy.alarmtripsettings.views.form;

import java.awt.Frame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.models.config.MainFormSettingsObservable;


/**
 * Вью главной формы приложения. Реализeует набор фреймов для вставки вью 
 * отдельных моделей.
 * 
 * @author   Denis.Udovenko
 * @version  1.1.7
 */
public class MainForm extends javax.swing.JFrame
{
    public Events events = new Events();
    
    private static final String LOGS_TAB_NAME                       = "log";
    private static final String OVERALL_COMPLIANCE_SUMMARY_TAB_NAME = "overall_compliance_summary";
    private static final String INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME = "intools_compliance_summary";
    private static final String DATA_SOURCES_TAB_NAME               = "data_sources";
    
    private int windowLeftBeforeMaximizing; 
    private int windowTopBeforeMaximizing; 
    private int windowWitdthBeforeMaximizing; 
    private int windowHeigthBeforeMaximizing; 
   
    private int maximizedWindowWorkspaceHeight;
    private int maximizedWindowAssetsTreeWidth;
    private int maximizedWindowTagDetailsTreeWidth;
    
    private int minimizedWindowWorkspaceHeight;
    private int minimizedWindowAssetsTreeWidth;
    private int minimizedWindowTagDetailsTreeWidth;
    
    private final MainFormSettingsObservable config;
    
    
    /**
     * Конструктор формы. Инициализирует все компоненты.
     */
    public MainForm(MainFormSettingsObservable config)
    {
        this.config = config;
        
        //Инициализируем компоненты:
        initComponents();
        
        //Применяем конфигурацию:
        _applyConfig();
        
        //Устанавливаем иконки пунктов главного меню:
        this.openStorageMenuItem.setIcon(Main.storageConnectionIcon);
        this.versionMenuItem.setIcon(Main.aboutIcon);
    }//MainFrom

    
    /**
     * Метод устанавливает контент панели прокрутки для дерева ассетов.
     * 
     * @param plantsTreePanel Панель, отображающая дерево ассетов
     */
    public void setPlantsTreePanelContent(JPanel plantsTreePanel)
    {
        this.plantsTreeScrollPane.add(plantsTreePanel);
        this.plantsTreeScrollPane.setViewportView(plantsTreePanel);
    }//setPlantsTreePanelContent
    
    
    /**
     * Метод устанавливает контент панели прокрутки для таблицы устройств.
     * 
     * @param devicesTableandTaskBarPanel Панель, отображающая таблицу устройств
     */
    public void setDevicesTableAndTaskbarPanelContent(JPanel devicesTableandTaskBarPanel)
    {
        this.devicesTableAndTaskBarPanelScrollPane.add(devicesTableandTaskBarPanel);
        this.devicesTableAndTaskBarPanelScrollPane.setViewportView(devicesTableandTaskBarPanel);
    }//setDevicesTablePanelContent
    
    
    /**
     * Метод устанавливает контент панели прокрутки для графического отображения
     * итогов анализа общего соответсвия уставок по выбранному объекту.
     * 
     * @param overallComplianceSummaryPanel Панель, отображающая соответсвующую информацию  
     */
    public void setOverallComplianceSummaryPanelContent(JPanel overallComplianceSummaryPanel)
    {
        this.overallComplianceSummaryScrollPane.add(overallComplianceSummaryPanel);
        this.overallComplianceSummaryScrollPane.setViewportView(overallComplianceSummaryPanel);
    }//setOverallComplianceSummaryPanelContent
    
    
    /**
     * Метод устанавливает контент панели прокрутки для графического отображения
     * итогов анализа соответствия уставок SPI по выбранному объекту.
     * 
     * @param intoolsComplianceSummaryPanel Панель, отображающая соответсвующую информацию  
     */
    public void setIntoolsComplianceSummaryPanelContent(JPanel intoolsComplianceSummaryPanel)
    {
        this.intoolsComplianceSummaryScrollPane.add(intoolsComplianceSummaryPanel);
        this.intoolsComplianceSummaryScrollPane.setViewportView(intoolsComplianceSummaryPanel);
    }//setIntoolsComplianceSummaryPanelContent
    

    /**
     * Метод устанавливает контент панели прокрутки для дерева источников данных
     * тагов по выбранному объекту.
     * 
     * @param  dataSourcesPanel  Панель, отображающая соответсвующую информацию  
     */
    public void setDataSourcesPanelContent(JPanel dataSourcesPanel)
    {
        this.objectDataSourcesTreeScrollPane.add(dataSourcesPanel);
        this.objectDataSourcesTreeScrollPane.setViewportView(dataSourcesPanel);
    }//setDataSourcesPanelContent
    
    
    /**
     * Метод устанавливает контент панели прокрутки для дерева тагов выбранного
     * устройства.
     * 
     * @param   tagsTreePanel  Панель, отображающая дерево тагов
     */
    public void setTagsTreePanelContent(JPanel tagsTreePanel)
    {
        this.tagDetailsTreeScrollPane.add(tagsTreePanel);
        this.tagDetailsTreeScrollPane.setViewportView(tagsTreePanel);
    }//setTagsTreePanelContent
                
    
    /**
     * Метод делает недоступным пункт меню "File -> Connect..."
     */
    public void disableConnectMenu()
    {
        this.fileMenu.getItem(0).setEnabled(false);
    }//disableConnectMenu
    
    
    /**
     * Метод возвращает текущее состояние формы (развернута на весь экран или
     * нет). Состояние формы определяется строковыми значениями "true" или 
     * "false".
     * 
     * @return Текущее состояние формы (развернута на весь экран или нет)
     */
    public String isMaximized()
    {
        if (this.getExtendedState() == Frame.MAXIMIZED_BOTH) return Boolean.TRUE.toString();
        else return Boolean.FALSE.toString();
    }//getFormExtendedState
    
    
    /**
     * Метод возвращает горизонтальный отступ левого верхнего угла формы от 
     * начала координат экрана.
     *
     * @return Горизонтальный отступ левого верхнего угла формы от начала координат экрана
     */
    public String getFormX()
    {
        return Integer.toString(this.windowLeftBeforeMaximizing);
    }//getFormX

    
    /**
     * Метод возвращает вертикальный отступ левого верхнего угла формы от 
     * начала координат экрана.
     *
     * @return Вертикальный отступ левого верхнего угла формы от начала координат экрана
     */
    public String getFormY()
    {
        return Integer.toString(this.windowTopBeforeMaximizing);
    }//getFormX
    
    
    /**
     * Метод возвращает ширину формы.
     * 
     * @return Ширина формы
     */
    public String getFormWidth()
    {
        return Integer.toString(this.windowWitdthBeforeMaximizing);
    }//getFormWidth
    
    
    /**
     * Метод возвращает высоту формы.
     * 
     * @return Высота формы
     */
    public String getFormHeigt()
    {
        return Integer.toString(this.windowHeigthBeforeMaximizing);
    }//getFormHeigt
    
    
    /**
     * Метод возвращает ширину дерева ассетов для формы, развернутой на весь 
     * экран.
     * 
     * @return Ширина дерева ассетов для формы, развернутой на весь экран
     */
    public String getMaximizedWindowAssetsTreeWidth()
    {
        return Integer.toString(maximizedWindowAssetsTreeWidth);
    }//getWorkspaceWidth
    
    
    /**
     * Метод возвращает высоту рабочего пространства для формы, развернутой на 
     * весь экран.
     * 
     * @return Высоту рабочего пространства для формы, развернутой на весь экран
     */
    public String getMaximizedWindowWorkspaceHeight()
    {
        return Integer.toString(maximizedWindowWorkspaceHeight);
    }//getWorkspaceHeight
    
    
    /**
     * Метод возвращает ширину дерева свойств тагов выбранного контура для 
     * формы, развернутой на весь экран.
     * 
     * @return Ширина дерева свойств тагов выбранного контура для формы, развернутой на весь экран
     */
    public String getMaximizedWindowTagDetailsTreeWidth()
    {
        return Integer.toString(maximizedWindowTagDetailsTreeWidth);
    }//tagDetailsTreeWidth
    
    
    /**
     * Метод возвращает ширину дерева ассетов для формы, не развернутой на весь 
     * экран.
     * 
     * @return Ширина дерева ассетов для формы, развернутой на весь экран
     */
    public String getMinimizedWindowAssetsTreeWidth()
    {
        return Integer.toString(minimizedWindowAssetsTreeWidth);
    }//getWorkspaceWidth
    
    
    /**
     * Метод возвращает высоту рабочего пространства для формы, не развернутой на 
     * весь экран.
     * 
     * @return Высоту рабочего пространства для формы, развернутой на весь экран
     */
    public String getMinimizedWindowWorkspaceHeight()
    {
        return Integer.toString(minimizedWindowWorkspaceHeight);
    }//getWorkspaceHeight
    
    
    /**
     * Метод возвращает ширину дерева свойств тагов выбранного контура для 
     * формы, не развернутой на весь экран.
     * 
     * @return Ширина дерева свойств тагов выбранного контура для формы, развернутой на весь экран
     */
    public String getMinimizedWindowTagDetailsTreeWidth()
    {
        return Integer.toString(minimizedWindowTagDetailsTreeWidth);
    }//tagDetailsTreeWidth
    
    
    /**
     * Метод возвращает назавание текущей активной вкладки нижней панели формы.
     * 
     * @return Hазавание текущей активной вкладки нижней панели формы
     */
    public String getActiveBottomTab()
    {
        if (this.bottomTabbedPane.getSelectedComponent() == this.applicationLogScrollPane) return LOGS_TAB_NAME;
        if (this.bottomTabbedPane.getSelectedComponent() == this.overallComplianceSummaryScrollPane) return OVERALL_COMPLIANCE_SUMMARY_TAB_NAME;
        if (this.bottomTabbedPane.getSelectedComponent() == this.intoolsComplianceSummaryScrollPane) return INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME;
        if (this.bottomTabbedPane.getSelectedComponent() == this.objectDataSourcesTreeScrollPane) return DATA_SOURCES_TAB_NAME;
        
        return null;
    }//getActiveBottomTab
       
    
    /**
     * Метод применяет настройки интерфейса формы.
     */
    private void _applyConfig()
    {
        //Применяем размеры окна формы:
        this.windowWitdthBeforeMaximizing = Integer.parseInt(config.getWindowWidth());
        this.windowHeigthBeforeMaximizing = Integer.parseInt(config.getWindowHeight());
        this.setSize(this.windowWitdthBeforeMaximizing, this.windowHeigthBeforeMaximizing);
        
        //Применяем положение окна:
        this.windowLeftBeforeMaximizing = Integer.parseInt(config.getWindowLeft());
        this.windowTopBeforeMaximizing = Integer.parseInt(config.getWindowTop());
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                setLocation(windowLeftBeforeMaximizing, windowTopBeforeMaximizing);
            }//run
        });//invokeLater
               
        //Воввтанавливаем внутренние переменные для хранения размеров областей:
        maximizedWindowWorkspaceHeight = Integer.parseInt(config.getMaximizedWindowWorkspaceHeight());
        maximizedWindowAssetsTreeWidth = Integer.parseInt(config.getMaximizedWindowAssetsTreeWidth());
        maximizedWindowTagDetailsTreeWidth = Integer.parseInt(config.getMaximizedWindowTagDetailsTreeWidth());
        minimizedWindowWorkspaceHeight = Integer.parseInt(config.getMinimizedWindowWorkspaceHeight());
        minimizedWindowAssetsTreeWidth = Integer.parseInt(config.getMinimizedWindowAssetsTreeWidth());
        minimizedWindowTagDetailsTreeWidth = Integer.parseInt(config.getMinimizedWindowTagDetailsTreeWidth());
        
        //Разворачиваем окно на весь экран, если такая настройка задана:
        if (config.getWindowMaximized().equals(Boolean.TRUE.toString()))
        {
            this.setExtendedState(Frame.MAXIMIZED_BOTH);
               
            //Устанвливаем ширину рабочих областей для развернутого на весь экран окна:
            workspaceAndLogSplitPane.setDividerLocation(maximizedWindowWorkspaceHeight);
            assetsTreeAndTagsInfoSplitPanel.setDividerLocation(maximizedWindowAssetsTreeWidth);
            tagsTableAndTagDetailsSplitPanel.setDividerLocation(maximizedWindowTagDetailsTreeWidth);
        
        } else {
        
            //Устанвливаем ширину рабочих областей для неразвернутого на весь экран окна:
            workspaceAndLogSplitPane.setDividerLocation(minimizedWindowWorkspaceHeight);
            assetsTreeAndTagsInfoSplitPanel.setDividerLocation(minimizedWindowAssetsTreeWidth);
            tagsTableAndTagDetailsSplitPanel.setDividerLocation(minimizedWindowTagDetailsTreeWidth);
        }//else
        
        //Устанавливаем активную вкладку нижней панели:
        if (config.getActiveBottomTab().equals(LOGS_TAB_NAME)) this.bottomTabbedPane.setSelectedComponent(this.applicationLogScrollPane);
        if (config.getActiveBottomTab().equals(OVERALL_COMPLIANCE_SUMMARY_TAB_NAME)) this.bottomTabbedPane.setSelectedComponent(this.overallComplianceSummaryScrollPane);
        if (config.getActiveBottomTab().equals(INTOOLS_COMPLIANCE_SUMMARY_TAB_NAME)) this.bottomTabbedPane.setSelectedComponent(this.intoolsComplianceSummaryScrollPane);
        if (config.getActiveBottomTab().equals(DATA_SOURCES_TAB_NAME)) this.bottomTabbedPane.setSelectedComponent(this.objectDataSourcesTreeScrollPane);
    }//_applyConfig
    
    
    /**
     * Метод добавляет запись в тесктовое полен лога приложения.
     * 
     * @param record Запись, добавляемая в лог.
     */
    public void addLogRecord(String record)
    {
        this.applicationLogTextArea.append(record + "\n");
        this.applicationLogTextArea.setCaretPosition(this.applicationLogTextArea.getCaretPosition() + record.length());
    }//addLogRecord
        
    
    /**
     * Метод устанавливает содержимое метки с названием подключенной базы данных
     * в статусной строке.
     * 
     * @param databaseName Строка с название базы данных
     */
    public void setStatusBarDatabaseName(String databaseName)
    {
        this.statusBarDatabaseNameLabel.setText(databaseName);
    }//setStatusBarDatabaseName
         
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        workspaceAndLogSplitPane = new javax.swing.JSplitPane();
        assetsTreeAndTagsInfoSplitPanel = new javax.swing.JSplitPane();
        tagsTableAndTagDetailsSplitPanel = new javax.swing.JSplitPane();
        tagDetailsTreeScrollPane = new javax.swing.JScrollPane();
        deviceTagsTree = new javax.swing.JTree();
        devicesTableAndTaskbarPanel = new javax.swing.JPanel();
        devicesTableAndTaskBarPanelScrollPane = new javax.swing.JScrollPane();
        plantsTreeScrollPane = new javax.swing.JScrollPane();
        bottomTabbedPane = new javax.swing.JTabbedPane();
        applicationLogScrollPane = new javax.swing.JScrollPane();
        applicationLogTextArea = new javax.swing.JTextArea();
        overallComplianceSummaryScrollPane = new javax.swing.JScrollPane();
        intoolsComplianceSummaryScrollPane = new javax.swing.JScrollPane();
        objectDataSourcesTreeScrollPane = new javax.swing.JScrollPane();
        statusBar = new org.jdesktop.swingx.JXStatusBar();
        statusBarDatabaseNameLabel = new javax.swing.JLabel();
        mainMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openStorageMenuItem = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenu();
        versionMenuItem = new javax.swing.JMenuItem();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });

        workspaceAndLogSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tagDetailsTreeScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                tagDetailsTreeScrollPaneComponentResized(evt);
            }
        });

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        deviceTagsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        deviceTagsTree.setRootVisible(false);
        deviceTagsTree.setShowsRootHandles(true);
        tagDetailsTreeScrollPane.setViewportView(deviceTagsTree);

        tagsTableAndTagDetailsSplitPanel.setRightComponent(tagDetailsTreeScrollPane);

        javax.swing.GroupLayout devicesTableAndTaskbarPanelLayout = new javax.swing.GroupLayout(devicesTableAndTaskbarPanel);
        devicesTableAndTaskbarPanel.setLayout(devicesTableAndTaskbarPanelLayout);
        devicesTableAndTaskbarPanelLayout.setHorizontalGroup(
            devicesTableAndTaskbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 23, Short.MAX_VALUE)
            .addGroup(devicesTableAndTaskbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(devicesTableAndTaskBarPanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
        );
        devicesTableAndTaskbarPanelLayout.setVerticalGroup(
            devicesTableAndTaskbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 322, Short.MAX_VALUE)
            .addGroup(devicesTableAndTaskbarPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(devicesTableAndTaskBarPanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
        );

        tagsTableAndTagDetailsSplitPanel.setLeftComponent(devicesTableAndTaskbarPanel);

        assetsTreeAndTagsInfoSplitPanel.setRightComponent(tagsTableAndTagDetailsSplitPanel);

        plantsTreeScrollPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                plantsTreeScrollPaneComponentResized(evt);
            }
        });
        assetsTreeAndTagsInfoSplitPanel.setLeftComponent(plantsTreeScrollPane);

        workspaceAndLogSplitPane.setLeftComponent(assetsTreeAndTagsInfoSplitPanel);

        bottomTabbedPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                bottomTabbedPaneComponentResized(evt);
            }
        });

        applicationLogScrollPane.setWheelScrollingEnabled(false);

        applicationLogTextArea.setEditable(false);
        applicationLogTextArea.setColumns(20);
        applicationLogTextArea.setRows(5);
        applicationLogScrollPane.setViewportView(applicationLogTextArea);

        bottomTabbedPane.addTab("Log", applicationLogScrollPane);

        overallComplianceSummaryScrollPane.setBorder(null);
        overallComplianceSummaryScrollPane.setWheelScrollingEnabled(false);
        bottomTabbedPane.addTab("Overall compliance summary", overallComplianceSummaryScrollPane);
        bottomTabbedPane.addTab("SPI compliance summary", intoolsComplianceSummaryScrollPane);

        objectDataSourcesTreeScrollPane.setBorder(null);
        objectDataSourcesTreeScrollPane.setWheelScrollingEnabled(false);
        bottomTabbedPane.addTab("Data sources", objectDataSourcesTreeScrollPane);

        workspaceAndLogSplitPane.setRightComponent(bottomTabbedPane);

        statusBar.setMinimumSize(new java.awt.Dimension(27, 10));
        statusBar.add(statusBarDatabaseNameLabel);

        fileMenu.setText("File");

        openStorageMenuItem.setText("Connect...");
        openStorageMenuItem.setToolTipText("");
        openStorageMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openStorageMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openStorageMenuItem);

        mainMenuBar.add(fileMenu);

        aboutMenu.setText("About");

        versionMenuItem.setText("Version...");
        versionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                versionMenuItemActionPerformed(evt);
            }
        });
        aboutMenu.add(versionMenuItem);

        mainMenuBar.add(aboutMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(workspaceAndLogSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
            .addComponent(statusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(workspaceAndLogSplitPane)
                .addGap(0, 0, 0)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    /**
     * Метод обрабатывает вызов пункта главного меню "File->Connect" и 
     * рассылает соответсвующее событие всем подписчикам.  
     * 
     * @param evt Событие нажатия на пункт меню
     * @return void
     */
    private void openStorageMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openStorageMenuItemActionPerformed
        
        CustomEvent myEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.CONNECT_MENU_ITEM_CLICK, myEvent);
    }//GEN-LAST:event_openStorageMenuItemActionPerformed
           
    
    /**
     * Метод обрабатывает событие изменения размеров дерева свойств выбранного 
     * тага и сохраняет текущее значение в переменную в зависимости от 
     * состояния главной формы (развернута на весь экран или нет).
     * 
     * @param evt Событие изменения размеров панели
     */
    private void tagDetailsTreeScrollPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_tagDetailsTreeScrollPaneComponentResized
        
        if (this.getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowTagDetailsTreeWidth = tagsTableAndTagDetailsSplitPanel.getDividerLocation();
        else minimizedWindowTagDetailsTreeWidth = tagsTableAndTagDetailsSplitPanel.getDividerLocation();
    }//GEN-LAST:event_tagDetailsTreeScrollPaneComponentResized
   
    
    /**
     * Метод обрабатывает вызов пункта главного меню "About->Version..." и 
     * рассылает соответсвующее событие всем подписчикам.  
     * 
     * @param evt Событие нажатия на пункт меню
     */
    private void versionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_versionMenuItemActionPerformed
        
        CustomEvent myEvent = new CustomEvent(this);
        this.events.trigger(ViewEvent.VERSION_MENU_ITEM_CLICK, myEvent);
    }//GEN-LAST:event_versionMenuItemActionPerformed
   
           
    /**
     * Метод обрабатывает событие закрытия окна формы и рассылает событие 
     * сохранения настроек интерфейса с контентом настроек всем подписчикам.
     * 
     * @param evt Событие изменения размеров формы
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        CustomEvent myEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.FORM_CLOSING, myEvent);
    }//GEN-LAST:event_formWindowClosing

    
    
    /**
     * Метод обрабатывает событие изменения высоты нижней панели со вкладками
     * и сохраняет текущее значение в соответствующую переменную в зависимости 
     * от состояния главной формы (развернута на весь экран или нет).
     * 
     * @param evt Событие изменения размеров панели
     */
    private void bottomTabbedPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_bottomTabbedPaneComponentResized
 
        if (this.getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowWorkspaceHeight = workspaceAndLogSplitPane.getDividerLocation();
        else minimizedWindowWorkspaceHeight = workspaceAndLogSplitPane.getDividerLocation();     
    }//GEN-LAST:event_bottomTabbedPaneComponentResized

    
    /**
     * Метод обрабатывает событие изменения ширины панели дерева ассетов и 
     * сохраняет текущее значение в соответствующую переменную в зависимости 
     * от состояния главной формы (развернута на весь экран или нет).
     * 
     * @param evt Событие изменения размеров панели
     */
    private void plantsTreeScrollPaneComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_plantsTreeScrollPaneComponentResized
        
        if (this.getExtendedState() == Frame.MAXIMIZED_BOTH) maximizedWindowAssetsTreeWidth = assetsTreeAndTagsInfoSplitPanel.getDividerLocation();
        else minimizedWindowAssetsTreeWidth = assetsTreeAndTagsInfoSplitPanel.getDividerLocation();
    }//GEN-LAST:event_plantsTreeScrollPaneComponentResized

    
    /**
     * Метод обрабатывает события изменения развернутого состояния формы 
     * (сворачивание/разворачивание) и восстанавливает пропорции панелей формы
     * для нового состояния.
     * 
     * @param evt Событие изменения состояния панели
     */
    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                //Если окно было развернуто на весь экран, восстанавливаем размеры параметров панелей для развернутого окна:
                if (getExtendedState() == Frame.MAXIMIZED_BOTH)
                {
                    workspaceAndLogSplitPane.setDividerLocation(maximizedWindowWorkspaceHeight);
                    assetsTreeAndTagsInfoSplitPanel.setDividerLocation(maximizedWindowAssetsTreeWidth);
                    tagsTableAndTagDetailsSplitPanel.setDividerLocation(maximizedWindowTagDetailsTreeWidth);

                } else { //Если окно было уменьшено:
        
                    workspaceAndLogSplitPane.setDividerLocation(minimizedWindowWorkspaceHeight);
                    assetsTreeAndTagsInfoSplitPanel.setDividerLocation(minimizedWindowAssetsTreeWidth);
                    tagsTableAndTagDetailsSplitPanel.setDividerLocation(minimizedWindowTagDetailsTreeWidth);
                }//else
            }//run
        });//invokeLater
    }//GEN-LAST:event_formWindowStateChanged

    
    /**
     * Метод обрабатывает событие изменения размеров формы и сохраняет новые
     * размеры в соответвующие поля.
     * 
     * @param evt Событие изменения состояния панели
     */
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        
        if (getExtendedState() != Frame.MAXIMIZED_BOTH)
        {
            windowWitdthBeforeMaximizing = this.getSize().width;
            windowHeigthBeforeMaximizing = this.getSize().height;
        }//if
    }//GEN-LAST:event_formComponentResized

    
    /**
     * Метод обрабатывает событие перемещения формы и сохраняет новые координаты 
     * в соответствующие поля.
     * 
     * @param evt Событие перемещения панели
     */
    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        
        if (getExtendedState() != Frame.MAXIMIZED_BOTH)
        {
            windowLeftBeforeMaximizing = this.getLocation().x;
            windowTopBeforeMaximizing = this.getLocation().y;
        }//if
    }//GEN-LAST:event_formComponentMoved
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu aboutMenu;
    private javax.swing.JScrollPane applicationLogScrollPane;
    private javax.swing.JTextArea applicationLogTextArea;
    private javax.swing.JSplitPane assetsTreeAndTagsInfoSplitPanel;
    private javax.swing.JTabbedPane bottomTabbedPane;
    private javax.swing.JTree deviceTagsTree;
    private javax.swing.JScrollPane devicesTableAndTaskBarPanelScrollPane;
    private javax.swing.JPanel devicesTableAndTaskbarPanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JScrollPane intoolsComplianceSummaryScrollPane;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JScrollPane objectDataSourcesTreeScrollPane;
    private javax.swing.JMenuItem openStorageMenuItem;
    private javax.swing.JScrollPane overallComplianceSummaryScrollPane;
    private javax.swing.JScrollPane plantsTreeScrollPane;
    private org.jdesktop.swingx.JXStatusBar statusBar;
    private javax.swing.JLabel statusBarDatabaseNameLabel;
    private javax.swing.JScrollPane tagDetailsTreeScrollPane;
    private javax.swing.JSplitPane tagsTableAndTagDetailsSplitPanel;
    private javax.swing.JMenuItem versionMenuItem;
    private javax.swing.JSplitPane workspaceAndLogSplitPane;
    // End of variables declaration//GEN-END:variables
}//MainForm