package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDcsVariableTableFromYokogawaBackupDialog;

import java.util.List;
import java.awt.Component;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.yokogawa.YgStationRecord;
import ru.sakhalinenergy.alarmtripsettings.Main;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import ru.sakhalinenergy.alarmtripsettings.models.config.CreateDcsVariableTableFromYokogawaBackupDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.YokogawaDcsBackupObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.ViewEvent;
import ru.sakhalinenergy.alarmtripsettings.views.DialogWithEvents;


/**
 * Класс реализует диалог для указания настроек парсинга бакапа DCS Yokogawa.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class CreateDcsVariableTableFromYokogawaBackupDialog extends DialogWithEvents implements CreateDcsVariableTableFromYokogawaBackupDialogObservable
{
        
    private final YokogawaDcsBackupObservable model;
    private final PlantsLogicObservable plants;
    private final TagMasksObservable tagMasks;
    private final CreateDcsVariableTableFromYokogawaBackupDialogSettingsObservable config;
    
    
    /**
     * Public constructor.
     * 
     * @param model Source wrapped into Yokogawa DCS backup parsing logic
     * @param plants Wrapped plants collection
     * @param tagMasks Wrapped tag masks collection
     */
    public CreateDcsVariableTableFromYokogawaBackupDialog(YokogawaDcsBackupObservable model, PlantsLogicObservable plants,
            TagMasksObservable tagMasks, CreateDcsVariableTableFromYokogawaBackupDialogSettingsObservable config) 
    {
        this.model = model;
        this.plants = plants;
        this.tagMasks = tagMasks;
        this.config = config;
        
        this.setModal(true);
        initComponents();
        this.setIconImage(Main.yokogawaIcon.getImage());
        
        //Subscribing on model events:
        model.on(SourceEvent.STATIONS_READ, new _StationsReadEventHandler());
    }//CreateDcsVariableTableFromYokogawaBackupDialog
    
    
    /**
     * Renders the dialog.
     * 
     * @param parent Parent component relative to which dialog will be rendered
     */
    public void render(Component parent)
    {
        this.setLocationRelativeTo(parent);
        
        //Формируем список форматов тагов:
        for (TagMask tempMask : tagMasks.getMasks()) tagMaskComboBox.addItem(tempMask);

        //Строим список производственных объектов:
        for (Plant tempPlant : plants.getPlants()) plantsComboBox.addItem(tempPlant);
        
        //Applying config:
        _applyConfig();
        
        this.setVisible(true);
    }//render  
    
    
    /**
     * Internal class - handler for model stations collection read event.
     * 
     * @author Denis.Udovenko
     * @version 1.0.3
     */
    class _StationsReadEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            //Setting up path to backup folder:
            pathToBackupFolderTextField.setText(model.getBackupFolderPath());
            
            //Setting up stations list tree:
            stationsCheckBoxesTree.setCellRenderer(new StationsTreeNodeRenderer());
            stationsCheckBoxesTree.setCellEditor(new StationsTreeNodeEditor(stationsCheckBoxesTree));
            stationsCheckBoxesTree.setEditable(true);
 
            DefaultTreeModel treeModel = (DefaultTreeModel)stationsCheckBoxesTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
            root.removeAllChildren();
        
            for (YgStationRecord tempStation : model.getStations())
            {
                DefaultMutableTreeNode stationNode = new DefaultMutableTreeNode(new CheckboxNode(tempStation, true));
                root.add(stationNode);
            }//for
        
            //Reloading tree:
            treeModel.reload(root);
        }//customEventOccurred
    }//_SetPathToBackupFolderButtonClickHandler

    
    /**
     * Restores dialog control elements values from current configuration 
     * instance.
     */
    private void _applyConfig()
    {        
        //Восстанавливаем выбранный производственный объект:
        for (Plant tempPlant : plants.getPlants())
        {    
            if (tempPlant.getId().equals(config.getPlantCode()))
            {    
                this.plantsComboBox.setSelectedItem(tempPlant);
                break;
            }//if
        }//for
        
        for (TagMask tempTagMask : this.tagMasks.getMasks())
        {    
            if (tempTagMask.getExample().equals(config.getTagFormat()))
            {    
                this.tagMaskComboBox.setSelectedItem(tempTagMask);
                break;
            }//if
        }//for
    }//_applyConfig
    
    
    /**
     * Метод возвращает список выбранных в дереве тагов, которые необходимо
     * вынести в отдельную копию петли.
     * 
     * @return Коллекцию выбранных станций
     */
    @Override
    public List<YgStationRecord> getSelectedStations()
    {
        List<YgStationRecord> result = new ArrayList();
        
        DefaultMutableTreeNode stationNode;
        Object stationNodeUserObject;
          
        //Получаем модель дерева и корневой узел:
        DefaultTreeModel treeModel = (DefaultTreeModel)stationsCheckBoxesTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
        for (int i = 0; i < root.getChildCount(); i++)
        {
            stationNode = (DefaultMutableTreeNode)root.getChildAt(i);
            stationNodeUserObject = stationNode.getUserObject(); 

            if (stationNodeUserObject.getClass() == CheckboxNode.class)
            {
                //Если чекбокс выбран - добавляем станцию в список:
                CheckboxNode checkboxTagNode = (CheckboxNode)stationNodeUserObject;
                if (checkboxTagNode.isSelected()) result.add((YgStationRecord)checkboxTagNode.getObject());
            }//if
        }//for
        
        return result;
    }//getSelectedTags
    
    
    /**
     * Метод устанавливает значение текстового поля для указания пути к папке
     * с бэкапом.
     * 
     * @param   path  Строка с путем к папке бэкапа
     * @return  void
     */
    public void setPathToBackup(String path)
    {
        this.pathToBackupFolderTextField.setText(path);
    }//setPathToBackup
    
    
    /**
     * Метод возвращает текущий код ассета.
     * 
     * @return Текущий код ассета
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)this.plantsComboBox.getSelectedItem();
    }//getPlantCode
    

    /**
     * Return current tag mask.
     * 
     * @return Current tag mask
     */
    @Override
    public TagMask getTagMask()
    {
        return (TagMask)tagMaskComboBox.getSelectedItem();
    }//getTagMask
    
    
    /**
     * Метод возвращает значение текстового поля для указания пути к папке
     * с бэкапом.
     * 
     * @return  String  Значение текстового поля
     */
    public String getPathToBackup()
    {
        return this.pathToBackupFolderTextField.getText();
    }//getPathToBackup
    
    
    /**
     * Метод блокирует/разблокирует набор элементов управления формы кроме 
     * кнопки вызова диалога для указания пути к бэкапу.
     * 
     * @param lock Флаг состояния, в которое устанавливается набор элементов (блокирован/разблокирован)
     * @return void
     */
    public void setControlsEnabled(Boolean lock)
    {
        this.stationsCheckBoxesTree.setEnabled(lock);
        this.runParsingButton.setEnabled(lock);
    }//setControlsEnabled
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pathToBackupFolderTextFieldLabel = new javax.swing.JLabel();
        pathToBackupFolderTextField = new javax.swing.JTextField();
        setPathToBackupFolderButton = new javax.swing.JButton();
        runParsingButton = new javax.swing.JButton();
        plantCodeLabelCaption = new javax.swing.JLabel();
        stationsCheckBoxesTreeLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        stationsCheckBoxesTree = new javax.swing.JTree();
        plantsComboBox = new javax.swing.JComboBox();
        tagMaskComboBoxLabel = new javax.swing.JLabel();
        tagMaskComboBox = new javax.swing.JComboBox();

        setTitle("Create extract from Yokogawa DCS backup");
        setResizable(false);

        pathToBackupFolderTextFieldLabel.setText("Path to backup folder:");

        pathToBackupFolderTextField.setEditable(false);

        setPathToBackupFolderButton.setText("...");
        setPathToBackupFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPathToBackupFolderButtonActionPerformed(evt);
            }
        });

        runParsingButton.setText("Run");
        runParsingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runParsingButtonActionPerformed(evt);
            }
        });

        plantCodeLabelCaption.setText("Plant:");

        stationsCheckBoxesTreeLabel.setText("Stations in backup:");

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        stationsCheckBoxesTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        stationsCheckBoxesTree.setRootVisible(false);
        jScrollPane2.setViewportView(stationsCheckBoxesTree);

        plantsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantsComboBoxActionPerformed(evt);
            }
        });

        tagMaskComboBoxLabel.setText("Tag format:");

        tagMaskComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagMaskComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runParsingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tagMaskComboBoxLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tagMaskComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(plantCodeLabelCaption)
                            .addGap(39, 39, 39)
                            .addComponent(plantsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pathToBackupFolderTextFieldLabel)
                                .addComponent(stationsCheckBoxesTreeLabel)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(pathToBackupFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(setPathToBackupFolderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantCodeLabelCaption)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagMaskComboBoxLabel)
                    .addComponent(tagMaskComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(pathToBackupFolderTextFieldLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathToBackupFolderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setPathToBackupFolderButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stationsCheckBoxesTreeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(runParsingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод рассылает событие клика по кнопке для указания пути к папке с 
     * бэкапом.
     *  
     * @param   evt  Cобытие клика
     * @return  void
     */
    private void setPathToBackupFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPathToBackupFolderButtonActionPerformed
        
        CustomEvent myEvent = new CustomEvent(this);
        this.trigger(ViewEvent.SET_PATH_TO_BACKUP_FOLDER_BUTTON_CLICK, myEvent);
    }//GEN-LAST:event_setPathToBackupFolderButtonActionPerformed

    
    /**
     * Метод рассылает событие клика по кнопке запуска парсинга выбранного 
     * бэкапа и создания из него нового источника данных.
     *  
     * @param   evt  Cобытие клика
     * @return  void
     */
    private void runParsingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runParsingButtonActionPerformed
        
        this.setVisible(false);
        
        CustomEvent myEvent = new CustomEvent(this);
        this.trigger(ViewEvent.RUN_BACKUP_PARSING_BUTTON_CLICK, myEvent);
    }//GEN-LAST:event_runParsingButtonActionPerformed

    
    /**
     * Handles plant code selection event and fires an event contains selected 
     * plant instance.
     * 
     * @param evt Plants combo box event
     */
    private void plantsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantsComboBoxActionPerformed
        
        Plant plant = getPlant();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        this.events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);        
    }//GEN-LAST:event_plantsComboBoxActionPerformed

    
    /**
     * Handles tag format selection event and fires an event contains selected
     * tag mask instance.
     * 
     * @param evt Tag masks combo box event
     */
    private void tagMaskComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagMaskComboBoxActionPerformed
        
        TagMask tagFormat = getTagMask();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagFormat);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagMaskComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField pathToBackupFolderTextField;
    private javax.swing.JLabel pathToBackupFolderTextFieldLabel;
    private javax.swing.JLabel plantCodeLabelCaption;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JButton runParsingButton;
    private javax.swing.JButton setPathToBackupFolderButton;
    private javax.swing.JTree stationsCheckBoxesTree;
    private javax.swing.JLabel stationsCheckBoxesTreeLabel;
    private javax.swing.JComboBox tagMaskComboBox;
    private javax.swing.JLabel tagMaskComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}
