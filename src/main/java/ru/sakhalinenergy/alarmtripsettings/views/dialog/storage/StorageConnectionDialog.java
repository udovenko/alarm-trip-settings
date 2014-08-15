package ru.sakhalinenergy.alarmtripsettings.views.dialog.storage;

import java.awt.Component;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.models.config.StorageConnectionDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.views.dialog.Dialog;


/**
 * Implements storage connection settings dialog.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class StorageConnectionDialog extends Dialog 
{
    private final String SQLITE_STORAGE_NAME = "sqlite";
    private final String MYSQL_STORAGE_NAME = "mysql";
    
    private final StorageConnectionDialogSettingsObservable config;
      
    
    /**
     * Public constructor. Initializes components and sets configuration object
     * instance.
     * 
     * @param config Storage connection configuration object
     */
    public StorageConnectionDialog(StorageConnectionDialogSettingsObservable config)
    {
        this.config = config;
        
        this.setModal(true);
        initComponents();
               
        // Set dialog icon:
        ImageIcon img = Main.storageConnectionIcon;
        this.setIconImage(img.getImage());
    }// SetStorageConnectionForm

    
    /**
     * Applies dialog's settings and shows form on the screen.
     * 
     * @param parent Parent component, relative to which current dialog will be rendered
     */
    public void render(Component parent)
    {
        // Apply config:
        _applyConfig();
    
        // Set relative location and show dialog:
        setLocationRelativeTo(parent);
        _show();
    }// render
    
    
    /**
     * Sets dialog hiding action to the of EDT queue.
     */
    public void close()
    {
        _close();
    }// close
    
    
    /**
     * Returns selected storage type.
     * 
     * @return Selected storage type
     */
    public String getStorageType()
    {
        if (this.useMySqlStorageRadioButton.isSelected())  return MYSQL_STORAGE_NAME;
        if (this.useSqliteStorageRadioButton.isSelected()) return SQLITE_STORAGE_NAME;
        
        return null;
    }// getStorageType
    
    
    /**
     * Returns host name for MySQL database connection.
     * 
     * @return MySQL host name
     */
    public String getMySqlStorageHost()
    {
        return mySqlStorageHostTextField.getText();
    }// getMySqlStorageHost
    
    
    /**
     * Returns port number for MySQL database connection.
     * 
     * @return MySQL port
     */
    public String getMySqlStoragePort()
    {
        return mySqlStoragePortTextField.getText();
    }// getMySqlStoragePort
    
    
    /**
     * Returns MySQL database name.
     * 
     * @return MySQL database name
     */
    public String getMySqlStorageDatabaseName()
    {
        return mySqlStorageDatabasenameTextField.getText();
    }// getMySqlStorageDatabaseName
    
    
    /**
     * Returns user name for MySQL database connection.
     * 
     * @return MySQL user name
     */
    public String getMySqlStorageUserName()
    {
        return mySqlStorageUserNameTextField.getText();
    }// getMySqlStorageUserName
    
    
    /**
     * Returns password for MySQL database connection.
     * 
     * @return MySQL password
     */
    public String getMySqlStoragePassword()
    {
        return mySqlStoragePasswordTextField.getText();
    }// getMySqlStoragePassword
    
    
    /**
     * Returns path to SQLite database file.
     * 
     * @return Path to SQLite database file
     */
    public String getSqliteDatabasePath()
    {
        return sqliteDatabaseFilePathTextField.getText();
    }// getSqliteDatabasePath
        
    
    /**
     * Restores storage connection configuration settings into dialog's control
     * elements.
     */
    private void _applyConfig()
    {
        // Restore data source type:
        if (config.getStorageType().equals(MYSQL_STORAGE_NAME))
        {
            this.useMySqlStorageRadioButton.setSelected(true);
            this.storagesSettingsPanesLayeredPane.moveToFront(this.mySqlStorageSettingsPanel); 
        
        } else if (config.getStorageType().equals(SQLITE_STORAGE_NAME)) {
            
            this.useSqliteStorageRadioButton.setSelected(true);
            this.storagesSettingsPanesLayeredPane.moveToFront(this.SqliteStorageSettingsPanel);
        }// else if
        
        // Restore MySQL connection settings:
        this.mySqlStorageHostTextField.setText(config.getMySqlHost());
        this.mySqlStoragePortTextField.setText(config.getMySqlPort());
        this.mySqlStorageDatabasenameTextField.setText(config.getMySqlDatabase());
        this.mySqlStorageUserNameTextField.setText(config.getMySqlUser());
        this.mySqlStoragePasswordTextField.setText(config.getMySqlPassword());
        
        // Restore SQLite storage settings:
        this.sqliteDatabaseFilePathTextField.setText(config.getSqliteDatabasePath());
    }// _applyConfig
    
    
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
     * Handles selection of SQLite storage radio button and brings SQLite
     * storage settings to front.
     * 
     * @param evt SQLite storage radio button selection event
     */
    private void useSqliteStorageRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useSqliteStorageRadioButtonActionPerformed
        
        this.storagesSettingsPanesLayeredPane.moveToFront(SqliteStorageSettingsPanel);
    }//GEN-LAST:event_useSqliteStorageRadioButtonActionPerformed

    
    /**
     * Handles selection of MySQL storage radio button and brings MySQL
     * storage settings to front.
     * 
     * @param evt MySQL storage radio button selection event
     */
    private void useMySqlStorageRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useMySqlStorageRadioButtonActionPerformed
       
        this.storagesSettingsPanesLayeredPane.moveToFront(mySqlStorageSettingsPanel);    
    }//GEN-LAST:event_useMySqlStorageRadioButtonActionPerformed

    
    /**
     * Handles "Cancel" button click event and closes dialog.
     * 
     * @param "Cancel" button click event object
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        _close();
    }//GEN-LAST:event_cancelButtonActionPerformed

        
    /**
     * Handles "Connect" button click event and triggers appropriate event for
     * all subscribers.
     * 
     * @param evt "Connect" button click event object
     */
    private void connectToStorageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectToStorageButtonActionPerformed
        
        CustomEvent myEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.CONNECT_TO_STORAGE, myEvent);
    }//GEN-LAST:event_connectToStorageButtonActionPerformed

    
    /**
     * Handles select path to SQLite database file button click event and shows
     * file path selection dialog.
     * 
     * @param evt Button click event object
     */
    private void setSqlliteDatabasePathButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSqlliteDatabasePathButtonActionPerformed
        
        // Create file choser dialog:
        String working_directory = System.getProperty("user.dir");
        JFileChooser fileChooser = new JFileChooser(working_directory);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("SQLite Database", "sqlite");
        fileChooser.setFileFilter(filter);
        
        // Show dialog with given title:
        int dialog_result = fileChooser.showDialog(this, "Select SQLite database file");

        // Handle file path selection result:
        if (dialog_result == JFileChooser.APPROVE_OPTION)
        {
            File directory = fileChooser.getSelectedFile();
            String filename = directory.getAbsolutePath();
            
            // Update file path text field:
            sqliteDatabaseFilePathTextField.setText(filename);
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
}// StorageConnectionDialog
