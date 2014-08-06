package ru.sakhalinenergy.alarmtripsettings.views.StorageConnectionDialog;

import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettingsObservable;



/**
 * @author Denis.Udovenko
 * @version 1.0.2
 */
public class StorageConnectionDialog extends javax.swing.JDialog 
{
    public static final Byte CONNECT_TO_STORAGE_EVENT = 1;
    
    public Events events = new Events();
    private final StorageConnectionDialogSettingsObservable config;
      
    
    /**
     * Конструктор формы. Инициализирует все компоненты.
     * 
     * @param config Экземпляр, реализущий интефрейс файла конфигурации диалога настроек подключения к храилищу
     */
    public StorageConnectionDialog(StorageConnectionDialogSettingsObservable config)
    {
        this.config = config;
        
        this.setModal(true);
        initComponents();
               
        //Устанавливаем иконку диалога:
        ImageIcon img = Main.storageConnectionIcon;
        this.setIconImage(img.getImage());
        
        //Применяем насройки:
        _applyConfig();
    }//SetStorageConnectionForm

    
    /**
     * Возвращает тип используемого хранилища.
     * 
     * @return Тип используемого хранилища
     */
    public String getStorageType()
    {
        //Получаем тип используемого хранилища: 
        if (this.useMySqlStorageRadioButton.isSelected()) return "mysql";
        if (this.useSqliteStorageRadioButton.isSelected()) return "sqlite";
        
        return null;
    }//getStorageType
    
    
    /**
     * Возвращает хост для подключения к базе данных MySQL.
     * 
     * @return Xост для подключения к базе данных MySQL
     */
    public String getMySqlStorageHost()
    {
        return mySqlStorageHostTextField.getText();
    }//getMySqlStorageHost
    
    
    /**
     * Возвращает порт для подключения к базе данных MySQL.
     * 
     * @return Порт для подключения к базе данных MySQL
     */
    public String getMySqlStoragePort()
    {
        return mySqlStoragePortTextField.getText();
    }//getMySqlStoragePort
    
    
    /**
     * Возвращает имя базы данных для подключения к базе данных MySQL.
     * 
     * @return Имя базы данных для подключения к базе данных MySQL
     */
    public String getMySqlStorageDatabaseName()
    {
        return mySqlStorageDatabasenameTextField.getText();
    }//getMySqlStorageDatabaseName
    
    
    /**
     * Возвращает имя пользователя для подключения к базе данных MySQL.
     * 
     * @return Имя пользователя для подключения к базе данных MySQL
     */
    public String getMySqlStorageUserName()
    {
        return mySqlStorageUserNameTextField.getText();
    }//getMySqlStorageUserName
    
    
    /**
     * Возвращает паролдь для подключения к базе данных MySQL.
     * 
     * @return Пароль для подключения к базе данных MySQL
     */
    public String getMySqlStoragePassword()
    {
        return mySqlStoragePasswordTextField.getText();
    }//getMySqlStoragePassword
    
    
    /**
     * Вовзращает путь к базе данных SQLite.
     * 
     * @return Путь к базе данных SQLite
     */
    public String getSqliteDatabasePath()
    {
        return sqliteDatabaseFilePathTextField.getText();
    }//getSqliteDatabasePath
        
    
    /**
     * Метод помещает в элементы управления формы полученные настройки
     * подключения к хранлищу.
     */
    private void _applyConfig()
    {
        //Восстанавливаем тип хранлища:
        if (config.getStorageType().equals("mysql"))
        {
            this.useMySqlStorageRadioButton.setSelected(true);
            this.storagesSettingsPanesLayeredPane.moveToFront(this.mySqlStorageSettingsPanel); 
        }//if
        
        if (config.getStorageType().equals("sqlite"))
        {
            this.useSqliteStorageRadioButton.setSelected(true);
            this.storagesSettingsPanesLayeredPane.moveToFront(this.SqliteStorageSettingsPanel);
        }//if
        
        //Восстанавливаем настройки хранилища MySQL:
        this.mySqlStorageHostTextField.setText(config.getMySqlHost());
        this.mySqlStoragePortTextField.setText(config.getMySqlPort());
        this.mySqlStorageDatabasenameTextField.setText(config.getMySqlDatabase());
        this.mySqlStorageUserNameTextField.setText(config.getMySqlUser());
        this.mySqlStoragePasswordTextField.setText(config.getMySqlPassword());
        
        //Восстанавливаем настройки хранилища MS Excel:
        this.sqliteDatabaseFilePathTextField.setText(config.getSqliteDatabasePath());
    }//restoreStorageSettings
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        selectStorageTypeRadioButtonGroup = new javax.swing.ButtonGroup();
        useMySqlStorageRadioButton = new javax.swing.JRadioButton();
        selectStorageTypeRadioButtonGroupLabel = new javax.swing.JLabel();
        useSqliteStorageRadioButton = new javax.swing.JRadioButton();
        storagesSettingsPanesLayeredPane = new javax.swing.JLayeredPane();
        mySqlStorageSettingsPanel = new javax.swing.JPanel();
        mySqlStorageHostTextFieldLabel = new javax.swing.JLabel();
        mySqlStorageHostTextField = new javax.swing.JTextField();
        mySqlStoragePortTextFieldLabel = new javax.swing.JLabel();
        mySqlStoragePortTextField = new javax.swing.JTextField();
        mySqlStorageDatabasenameTextField = new javax.swing.JTextField();
        mySqlStorageDatabasenameTextFieldLabel = new javax.swing.JLabel();
        mySqlStorageUserNameTextFieldLabel = new javax.swing.JLabel();
        mySqlStoragePasswordTextFieldLabel = new javax.swing.JLabel();
        mySqlStorageUserNameTextField = new javax.swing.JTextField();
        mySqlStoragePasswordTextField = new javax.swing.JPasswordField();
        SqliteStorageSettingsPanel = new javax.swing.JPanel();
        sqliteDatabaseFilePathTextFieldLabel = new javax.swing.JLabel();
        sqliteDatabaseFilePathTextField = new javax.swing.JTextField();
        setSqlliteDatabasePathButton = new javax.swing.JButton();
        storagesSettingsPanesLayeredPaneLabel = new javax.swing.JLabel();
        connectToStorageButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setTitle("Storage connection");
        setAlwaysOnTop(true);
        setResizable(false);

        selectStorageTypeRadioButtonGroup.add(useMySqlStorageRadioButton);
        useMySqlStorageRadioButton.setSelected(true);
        useMySqlStorageRadioButton.setText("MySQL Database");
        useMySqlStorageRadioButton.setToolTipText("");
        useMySqlStorageRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useMySqlStorageRadioButtonActionPerformed(evt);
            }
        });

        selectStorageTypeRadioButtonGroupLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectStorageTypeRadioButtonGroupLabel.setText("Select your storage type:");
        selectStorageTypeRadioButtonGroupLabel.setToolTipText("");

        selectStorageTypeRadioButtonGroup.add(useSqliteStorageRadioButton);
        useSqliteStorageRadioButton.setText("SQLite Database");
        useSqliteStorageRadioButton.setToolTipText("");
        useSqliteStorageRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useSqliteStorageRadioButtonActionPerformed(evt);
            }
        });

        mySqlStorageHostTextFieldLabel.setText("Host:");
        mySqlStorageHostTextFieldLabel.setToolTipText("");

        mySqlStorageHostTextField.setToolTipText("");

        mySqlStoragePortTextFieldLabel.setText("Port:");
        mySqlStoragePortTextFieldLabel.setToolTipText("");

        mySqlStoragePortTextField.setToolTipText("");

        mySqlStorageDatabasenameTextField.setToolTipText("");

        mySqlStorageDatabasenameTextFieldLabel.setText("Database name:");
        mySqlStorageDatabasenameTextFieldLabel.setToolTipText("");

        mySqlStorageUserNameTextFieldLabel.setText("User:");
        mySqlStorageUserNameTextFieldLabel.setToolTipText("");

        mySqlStoragePasswordTextFieldLabel.setText("Password:");
        mySqlStoragePasswordTextFieldLabel.setToolTipText("");

        mySqlStorageUserNameTextField.setToolTipText("");

        mySqlStoragePasswordTextField.setToolTipText("");

        javax.swing.GroupLayout mySqlStorageSettingsPanelLayout = new javax.swing.GroupLayout(mySqlStorageSettingsPanel);
        mySqlStorageSettingsPanel.setLayout(mySqlStorageSettingsPanelLayout);
        mySqlStorageSettingsPanelLayout.setHorizontalGroup(
            mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mySqlStorageSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mySqlStorageHostTextFieldLabel)
                    .addComponent(mySqlStoragePortTextFieldLabel)
                    .addComponent(mySqlStorageDatabasenameTextFieldLabel)
                    .addComponent(mySqlStorageUserNameTextFieldLabel)
                    .addComponent(mySqlStoragePasswordTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(mySqlStoragePortTextField)
                    .addComponent(mySqlStorageDatabasenameTextField)
                    .addComponent(mySqlStorageHostTextField)
                    .addComponent(mySqlStorageUserNameTextField)
                    .addComponent(mySqlStoragePasswordTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        mySqlStorageSettingsPanelLayout.setVerticalGroup(
            mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mySqlStorageSettingsPanelLayout.createSequentialGroup()
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mySqlStorageHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mySqlStorageHostTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mySqlStoragePortTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mySqlStoragePortTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mySqlStorageDatabasenameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mySqlStorageDatabasenameTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mySqlStorageUserNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mySqlStorageUserNameTextFieldLabel))
                .addGap(8, 8, 8)
                .addGroup(mySqlStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mySqlStoragePasswordTextFieldLabel)
                    .addComponent(mySqlStoragePasswordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        storagesSettingsPanesLayeredPane.add(mySqlStorageSettingsPanel);
        mySqlStorageSettingsPanel.setBounds(0, 0, 280, 140);
        storagesSettingsPanesLayeredPane.setLayer(mySqlStorageSettingsPanel, javax.swing.JLayeredPane.MODAL_LAYER);

        sqliteDatabaseFilePathTextFieldLabel.setText("Path to file:");
        sqliteDatabaseFilePathTextFieldLabel.setToolTipText("");

        sqliteDatabaseFilePathTextField.setToolTipText("");

        setSqlliteDatabasePathButton.setText("jButton1");
        setSqlliteDatabasePathButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSqlliteDatabasePathButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SqliteStorageSettingsPanelLayout = new javax.swing.GroupLayout(SqliteStorageSettingsPanel);
        SqliteStorageSettingsPanel.setLayout(SqliteStorageSettingsPanelLayout);
        SqliteStorageSettingsPanelLayout.setHorizontalGroup(
            SqliteStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SqliteStorageSettingsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sqliteDatabaseFilePathTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sqliteDatabaseFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(setSqlliteDatabasePathButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SqliteStorageSettingsPanelLayout.setVerticalGroup(
            SqliteStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SqliteStorageSettingsPanelLayout.createSequentialGroup()
                .addGroup(SqliteStorageSettingsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sqliteDatabaseFilePathTextFieldLabel)
                    .addComponent(sqliteDatabaseFilePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setSqlliteDatabasePathButton))
                .addGap(0, 107, Short.MAX_VALUE))
        );

        storagesSettingsPanesLayeredPane.add(SqliteStorageSettingsPanel);
        SqliteStorageSettingsPanel.setBounds(0, 0, 290, 130);
        storagesSettingsPanesLayeredPane.setLayer(SqliteStorageSettingsPanel, javax.swing.JLayeredPane.MODAL_LAYER);

        storagesSettingsPanesLayeredPaneLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        storagesSettingsPanesLayeredPaneLabel.setText("Setup selected storage:");
        storagesSettingsPanesLayeredPaneLabel.setToolTipText("");

        connectToStorageButton.setText("Connect");
        connectToStorageButton.setToolTipText("");
        connectToStorageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectToStorageButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(storagesSettingsPanesLayeredPane)
                    .addComponent(selectStorageTypeRadioButtonGroupLabel)
                    .addComponent(useMySqlStorageRadioButton)
                    .addComponent(useSqliteStorageRadioButton)
                    .addComponent(storagesSettingsPanesLayeredPaneLabel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(connectToStorageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectStorageTypeRadioButtonGroupLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(useMySqlStorageRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(useSqliteStorageRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(storagesSettingsPanesLayeredPaneLabel)
                .addGap(12, 12, 12)
                .addComponent(storagesSettingsPanesLayeredPane, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(connectToStorageButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод обрабатывает событие выбора книги MS Excel в качестве отображаемого
     * хранилища данных и выводит на передний план панель натроек хранлища 
     * MS Excel.
     * 
     * @param   evt   Событие выбора радио-кнопки с типом хранлища
     * @return  void
     */
    private void useSqliteStorageRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useSqliteStorageRadioButtonActionPerformed
        
        this.storagesSettingsPanesLayeredPane.moveToFront(SqliteStorageSettingsPanel);
    }//GEN-LAST:event_useSqliteStorageRadioButtonActionPerformed

    
    /**
     * Метод обрабатывает событие выбора базы данных MySQL в качестве 
     * отображаемого хранилища данных и выводит на передний план панель натроек 
     * хранлища MySQL.
     * 
     * @param   evt   Событие выбора радио-кнопки с типом хранлища
     * @return  void
     */
    private void useMySqlStorageRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useMySqlStorageRadioButtonActionPerformed
       
        this.storagesSettingsPanesLayeredPane.moveToFront(mySqlStorageSettingsPanel);    
    }//GEN-LAST:event_useMySqlStorageRadioButtonActionPerformed

    
    /**
     * Метод закрывает текущий диалог по нажатию кнопки "Cancel".
     * 
     * @return  void
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

        
    /**
     * Метод обрабатывает нажатие кнопки "Connect" и рассылает всем подписчикам
     * событие подключения к храгнилищу с контентом настроек подключения.
     * 
     * @param   evt   Событие нажатия кнопки
     * @return  void 
     */
    private void connectToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToStorageButtonActionPerformed
        
        CustomEvent myEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.CONNECT_TO_STORAGE, myEvent);
    }//GEN-LAST:event_connectToStorageButtonActionPerformed

    
    /**
     * Метод обрабатывает нажатие кнопки setSqlliteDatabasePathButton и выводит
     * диалог выбора файла базы данных SQLite.
     * 
     * @param   evt  Событие нажатия на кнопку
     * @return  void
     */
    private void setSqlliteDatabasePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSqlliteDatabasePathButtonActionPerformed
        
        //Создаем диалог выбора дирректории:
        String working_directory = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(working_directory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database", "sqlite");
        fileChooser.setFileFilter(filter);
        
        //Отображаем диалог, указываем заголовок:
        int dialog_result = fileChooser.showDialog(this, "Select SQLite database file");

        //Обрабатываем результат вывбора дирректории:
        if (dialog_result == JFileChooser.APPROVE_OPTION)
        {
            //Получаем ссылку на дирректорию и ее имя:
            File directory = fileChooser.getSelectedFile();
            String filename = directory.getAbsolutePath();
            
            //Выводим имя полученной дирректории в инпут на форме:
            this.sqliteDatabaseFilePathTextField.setText(filename);
        }//if
    }//GEN-LAST:event_setSqlliteDatabasePathButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel SqliteStorageSettingsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton connectToStorageButton;
    private javax.swing.JTextField mySqlStorageDatabasenameTextField;
    private javax.swing.JLabel mySqlStorageDatabasenameTextFieldLabel;
    private javax.swing.JTextField mySqlStorageHostTextField;
    private javax.swing.JLabel mySqlStorageHostTextFieldLabel;
    private javax.swing.JPasswordField mySqlStoragePasswordTextField;
    private javax.swing.JLabel mySqlStoragePasswordTextFieldLabel;
    private javax.swing.JTextField mySqlStoragePortTextField;
    private javax.swing.JLabel mySqlStoragePortTextFieldLabel;
    private javax.swing.JPanel mySqlStorageSettingsPanel;
    private javax.swing.JTextField mySqlStorageUserNameTextField;
    private javax.swing.JLabel mySqlStorageUserNameTextFieldLabel;
    private javax.swing.ButtonGroup selectStorageTypeRadioButtonGroup;
    private javax.swing.JLabel selectStorageTypeRadioButtonGroupLabel;
    private javax.swing.JButton setSqlliteDatabasePathButton;
    private javax.swing.JTextField sqliteDatabaseFilePathTextField;
    private javax.swing.JLabel sqliteDatabaseFilePathTextFieldLabel;
    private javax.swing.JLayeredPane storagesSettingsPanesLayeredPane;
    private javax.swing.JLabel storagesSettingsPanesLayeredPaneLabel;
    private javax.swing.JRadioButton useMySqlStorageRadioButton;
    private javax.swing.JRadioButton useSqliteStorageRadioButton;
    // End of variables declaration//GEN-END:variables
}//SetStorageConnectionForm
