package ru.sakhalinenergy.alarmtripsettings.views.PlantsTreePanel;

import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.events.Events;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultTreeModel;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.config.PlantsTreePanelSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsTreeObservable;


/**
 * Класс реализует панель для отобюражения дерева ассетов.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class PlantsTreePanel extends javax.swing.JPanel 
{
    public static final Byte PLANTS_TREE_NODE_SELECTION_EVENT = 1;
    public static final Byte PLANTS_TREE_EXPANSION_STATE_CHANGE_EVENT = 2;
    
    public Events events = new Events();
    
    private static final String PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR = ",";
    private static final String AREAS_AND_UNITS_ID_SEPARATOR = "_";
    private static final String PLANT_NODE_TYPE_NAME = "plant";
    private static final String AREA_NODE_TYPE_NAME = "area";
    private static final String UNIT_NODE_TYPE_NAME = "unit";

    private final PlantsTreeObservable model;
    private final PlantsTreePanelSettingsObservable config;
   
    
    /**
     * Конструктор вью.
     */
    public PlantsTreePanel(PlantsTreeObservable model, PlantsTreePanelSettingsObservable config) 
    {
        initComponents();
        this.model = model;
        this.config = config;
        
        //Подписываеся на события модели:
        this.model.on(CollectionEvent.TREE_READ, new _PlantsTreeUpdatesHandler());
    }//PlantsTreePanel
    
    
    /**
     * Внутренний класс - обработчик события модели установки соединения с 
     * книгой MS Excel.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _PlantsTreeUpdatesHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            plantsTree.setCellRenderer(new PlantsTreeCellRenderer());
                
            //Получаем модель дерева и корневой узел:
            DefaultTreeModel treeModel = (DefaultTreeModel)plantsTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
            //Очищаем модель дерева ассетов:
            root.removeAllChildren();
        
            //Добавляем узлы ассетов:
            for (Plant plant : model.getPlants())
            {
                DefaultMutableTreeNode plantNode = new DefaultMutableTreeNode(plant);
                        
                //Добавляем в текущий узел ассета все узлы зон:
                for (TreeArea area : plant.getAreas())
                {
                    DefaultMutableTreeNode areaNode = new DefaultMutableTreeNode(area);
                
                    //Добавляем в текущий узел зоны все узлы юнитов:
                    for (TreeUnit unit : area.getUnits())
                    {
                        DefaultMutableTreeNode unitNode = new DefaultMutableTreeNode(unit);
                        areaNode.add(unitNode);
                    }//for
                                
                    plantNode.add(areaNode);
                }//for
            
                root.add(plantNode);
            }//for
            
            //Перегружаем дерево:   
            treeModel.reload(root);
        
            //Применяем настройки конфигурации:
            _applyConfig();
        }//setPlantsTree
    };//_PlantsTreeUpdatesHandler
        
    
    /**
     * Метод получает массивы путей раскрытых узлов дерева ассетов и возвращает
     * их в виде экземпляра настроек путей раскрытых узлов дерева ассетов.
     * 
     * @return Массив из двух строк со списками раскрытых узлов ассетов и зон
     */
    public String[] getTreeExpansionState()
    {
        String[] result = new String[2];
        
        List plantsId = new ArrayList();
        List areasId = new ArrayList();
        
        String plantsIdString = "";
        String areasIdString = "";
        
        Object root = this.plantsTree.getModel().getRoot();
        Enumeration enumeration = this.plantsTree.getExpandedDescendants(new TreePath(root));   
        
        if (enumeration != null) 
        {
            while (enumeration.hasMoreElements())
            {
                TreePath treePath = (TreePath) enumeration.nextElement();
                Object[] nodes = treePath.getPath();
                
                for (Object node : nodes)
                {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)node;
                    Object nodeObject = treeNode.getUserObject();
                    
                    if (nodeObject.getClass() == Plant.class)
                    {
                        Plant tempPlant = (Plant)nodeObject;
                        if (!plantsId.contains(tempPlant.getId())) plantsId.add(tempPlant.getId());
                    }//if
                    
                    if (nodeObject.getClass() == TreeArea.class)
                    {
                        TreeArea tempArea = (TreeArea)nodeObject;
                        areasId.add(tempArea.getPlant() + AREAS_AND_UNITS_ID_SEPARATOR + tempArea.getName());
                    }//if
                }//for
            }//while
        }//if
        
        //Преобразуем массивы id открытых объектов в строки (знаю, что криво, лень писать регэкспы):
        plantsIdString = plantsId.toString();
        areasIdString = areasId.toString();
        plantsIdString = plantsIdString.replace("[", "");
        plantsIdString = plantsIdString.replace("]", "");
        plantsIdString = plantsIdString.replace(" ", "");
        areasIdString = areasIdString.replace("[", "");
        areasIdString = areasIdString.replace("]", "");
        areasIdString = areasIdString.replace(" ", "");
        
        result[0] = plantsIdString;
        result[1] = areasIdString;
                
        return result;
    }//getTreeExpansionState
       
    
    /**
     * Метод получает информацию о текущем выбранном узле дерева ассетов для
     * сохранения в конфиг.
     * 
     * @return Массив из двух строк. Первый элемент содержит тип выбранного узла, второй - его идентификатор
     */
    public String[] getTreeSelectionState()
    {
        String[] result = new String[2];
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.plantsTree.getLastSelectedPathComponent();
        
        Object nodeObject = node.getUserObject();
                    
        //Формируем экземпляр настроек выбранного зла дерева ассетов в зависимости от типа узла:
        if (nodeObject.getClass().equals(Plant.class))
        {
            Plant plant = (Plant)nodeObject;
            result[0] = PLANT_NODE_TYPE_NAME;
            result[1] = plant.getId();
            
        } else if (nodeObject.getClass().equals(TreeArea.class)){
            
            TreeArea area = (TreeArea)nodeObject;
            result[0] = AREA_NODE_TYPE_NAME;
            result[1] = area.getPlant() + AREAS_AND_UNITS_ID_SEPARATOR + area.getName();
                    
        } else if (nodeObject.getClass().equals(TreeUnit.class)){
                
            TreeUnit unit = (TreeUnit)nodeObject;
            result[0] = UNIT_NODE_TYPE_NAME;
            result[1] = unit.getPlant() + AREAS_AND_UNITS_ID_SEPARATOR + unit.getArea() + AREAS_AND_UNITS_ID_SEPARATOR + unit.getName();
            
        }//else if
        
        return result;
    }//getTreeSelectionState
    
    
    /**
     * Применяет настройки конфигурации.
     */
    public void _applyConfig()
    {
        _setTreeExpansionState(config.getExpandedPlants(), config.getExpandedAreas());
        _setTreeSelectionState(config.getSelectedNodeType(), config.getSelectedNodeId());
    }//_applyConfig
    
    
    /**
     * Метод применяет настройки раскрытых путей дерева ассетов.
     * 
     * @param expandedPlants Строка со списком развернутых узлов ассетов
     * @param expandedAreas Строка со списком развернутых узлов зон
     */
    private void _setTreeExpansionState(String expandedPlants, String expandedAreas)
    {
        //Раскрываем узлы ассетов:        
        StringTokenizer plantsId = new StringTokenizer(expandedPlants, PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR);
        String tempPlantId; 
        Plant tempPlant;
        
        while (plantsId.hasMoreTokens())
        {
            tempPlantId = plantsId.nextToken();
            
            for (int i = 0; i < this.plantsTree.getRowCount(); i++)
            {
                TreePath path = this.plantsTree.getPathForRow(i);
                DefaultMutableTreeNode plantNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                
                if (plantNode.getUserObject().getClass() == Plant.class)
                {
                    tempPlant = (Plant)plantNode.getUserObject();
                    
                    if (tempPlant.getId().equals(tempPlantId)) this.plantsTree.expandRow(i);
                }//if
            }//for
        }//while
        
        //Раскрываем узлы зон:
        StringTokenizer areasId = new StringTokenizer(expandedAreas, PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR);
        String tempAreaId;
        String tempAreaName;
        TreeArea tempArea;
        
        while (areasId.hasMoreTokens())
        {
            tempAreaId = areasId.nextToken();
            String[] areaIdComponents = tempAreaId.split(AREAS_AND_UNITS_ID_SEPARATOR);
            tempPlantId = areaIdComponents[0];
            tempAreaName = areaIdComponents[1];
            
            for (int i = 0; i < this.plantsTree.getRowCount(); i++)
            {
                TreePath path = this.plantsTree.getPathForRow(i);
                DefaultMutableTreeNode areaNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                
                if (areaNode.getUserObject().getClass() == TreeArea.class)
                {
                    tempArea = (TreeArea)areaNode.getUserObject();
                    
                    if (tempArea.getPlant().equals(tempPlantId) && tempArea.getName().equals(tempAreaName)) this.plantsTree.expandRow(i);
                }//if
            }//for
        }//while
    }//setTreeExpansionState
    
    
    /**
     * Метод восстанавливает выбранный узел дерева асскетов на основании 
     * полученного экземплряра настроек выбранноного узла.
     * 
     * @param selectedNodeType Тип выбранного узла
     * @param selectedNodeId Идентификатор выбранного узла
     */
    private void _setTreeSelectionState(String selectedNodeType, String selectedNodeId)
    {
        TreePath tempPath;
        DefaultMutableTreeNode tempNode;
        Object tempNodeObject;
        String tempPlantId;
        String tempAreaName;
        String tempUnitName;
        
        for (int i = 0; i < this.plantsTree.getRowCount(); i++)
        {
            tempPath = this.plantsTree.getPathForRow(i);
            tempNode = (DefaultMutableTreeNode)tempPath.getLastPathComponent();
            tempNodeObject = tempNode.getUserObject();
            
            if (tempNodeObject.getClass() == Plant.class 
                && selectedNodeType.equals(PLANT_NODE_TYPE_NAME))
            {
                Plant plant = (Plant)tempNodeObject;
                
                if (selectedNodeId.equals(plant.getId()))
                {
                     this.plantsTree.setSelectionRow(i);
                     break;
                }//if
            
            } else if (tempNodeObject.getClass() == TreeArea.class && selectedNodeType.equals(AREA_NODE_TYPE_NAME)) {
            
                TreeArea area = (TreeArea)tempNodeObject;
                
                String[] areaIdComponents = selectedNodeId.split(AREAS_AND_UNITS_ID_SEPARATOR);
                tempPlantId = areaIdComponents[0];
                tempAreaName = areaIdComponents[1];
                
                if (tempPlantId.equals(area.getPlant()) && tempAreaName.equals(area.getName()))
                {
                    this.plantsTree.setSelectionRow(i);
                    break;
                }//if
                    
            } else if (tempNodeObject.getClass() == TreeUnit.class && selectedNodeType.equals(UNIT_NODE_TYPE_NAME)){
                
                TreeUnit unit = (TreeUnit)tempNodeObject;
                
                String[] unitIdComponents = selectedNodeId.split(AREAS_AND_UNITS_ID_SEPARATOR);
                tempPlantId = unitIdComponents[0];
                tempAreaName = unitIdComponents[1];
                tempUnitName = unitIdComponents[2];
                
                if (tempPlantId.equals(unit.getPlant()) 
                    && tempAreaName.equals(unit.getArea())
                    && tempUnitName.equals(unit.getName()))
                {
                    this.plantsTree.setSelectionRow(i);
                    break;
                }//if
            }//else if
        }//for
    }//setTreeSelectionState
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        treeScrollPane = new javax.swing.JScrollPane();
        plantsTree = new javax.swing.JTree();

        setPreferredSize(new java.awt.Dimension(0, 0));

        treeScrollPane.setBorder(null);

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        plantsTree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        plantsTree.setRootVisible(false);
        plantsTree.setShowsRootHandles(true);
        plantsTree.addTreeExpansionListener(new javax.swing.event.TreeExpansionListener() {
            public void treeCollapsed(javax.swing.event.TreeExpansionEvent evt) {
                plantsTreeTreeCollapsed(evt);
            }
            public void treeExpanded(javax.swing.event.TreeExpansionEvent evt) {
                plantsTreeTreeExpanded(evt);
            }
        });
        plantsTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                plantsTreeValueChanged(evt);
            }
        });
        treeScrollPane.setViewportView(plantsTree);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 21, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 22, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(treeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Метод обрабатывает событие выбора узла дерева ассетов и рассылает его
     * всем подписчикам с контекстом объекта, отображаемого выбранным узлом.
     * 
     * @param   evt  Событие выбора узла дерева
     * @return  void
     */
    private void plantsTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_plantsTreeValueChanged

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.plantsTree.getLastSelectedPathComponent();

        //Если узел выбран:
        if (node != null)
        {
            CustomEvent myEvent = new CustomEvent(node.getUserObject());
            this.events.trigger(ViewEvent.PLANTS_TREE_NODE_SELECTION, myEvent);
        }//if
    }//GEN-LAST:event_plantsTreeValueChanged

    
    /**
     * Метод обрабатывает событие сворачивания узла дерева ассетов и рассылет
     * его всем подписчикам с контекстом развернутых путей дерева.
     * 
     * @param   evt  Событие сворачивания узла
     * @return  void
     */
    private void plantsTreeTreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_plantsTreeTreeCollapsed
        
         CustomEvent myEvent = new CustomEvent(getTreeExpansionState());
         this.events.trigger(ViewEvent.PLANTS_TREE_EXPANSION_STATE_CHANGE, myEvent);
    }//GEN-LAST:event_plantsTreeTreeCollapsed

    
    /**
     * Метод обрабатывает событие раскрытия узла дерева ассетов и рассылает
     * его всем подписчикам с контекстом развернутых путей дерева.
     * 
     * @param   evt  Событие раскрытия узла
     * @return  void
     */
    private void plantsTreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_plantsTreeTreeExpanded
        
        CustomEvent myEvent = new CustomEvent(getTreeExpansionState());
        this.events.trigger(ViewEvent.PLANTS_TREE_EXPANSION_STATE_CHANGE, myEvent);
    }//GEN-LAST:event_plantsTreeTreeExpanded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree plantsTree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
}
