package ru.sakhalinenergy.alarmtripsettings.views.panel.plants;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.config.PlantsTreePanelSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeArea;
import ru.sakhalinenergy.alarmtripsettings.models.logic.classes.plantstree.TreeUnit;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.CollectionEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsTreeObservable;
import ru.sakhalinenergy.alarmtripsettings.views.panel.Panel;


/**
 * Implements panel for rendering plants (PAU objects) tree.
 * 
 * @author Denis Udovenko
 * @version 1.0.4
 */
public class PlantsTreePanel extends Panel 
{
    
    private static final String PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR = ",";
    private static final String PAU_OBJECTS_ID_SEPARATOR                     = "_";
    
    private static final String PLANT_NODE_TYPE_NAME = "plant";
    private static final String AREA_NODE_TYPE_NAME  = "area";
    private static final String UNIT_NODE_TYPE_NAME  = "unit";

    private final PlantsTreeObservable model;
    private final PlantsTreePanelSettingsObservable config;
   
    
    /**
     * Public constructor. Sets plants tree and panel configuration models and 
     * initializes components.
     * 
     * @param model Plants tree model instance
     * @param config Panel configuration object
     */
    public PlantsTreePanel(PlantsTreeObservable model, PlantsTreePanelSettingsObservable config) 
    {
        this.model = model;
        this.config = config;
                
        initComponents();
        
        // Subscribe on model's events:
        model.on(CollectionEvent.TREE_READ, new _PlantsTreeUpdatesHandler());
    }// PlantsTreePanel
    
    
    /**
     * Inner class - handler for model's tree updated event.
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
                
            // Get tree model and root:
            DefaultTreeModel treeModel = (DefaultTreeModel)plantsTree.getModel();
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)treeModel.getRoot();
        
            // Clear tree:
            root.removeAllChildren();
        
            // Add plant nodes:
            for (Plant plant : model.getPlants())
            {
                DefaultMutableTreeNode plantNode = new DefaultMutableTreeNode(plant);
                        
                // Add area nodes to current plant node:
                for (TreeArea area : plant.getAreas())
                {
                    DefaultMutableTreeNode areaNode = new DefaultMutableTreeNode(area);
                
                    // Add unit nodes to current area node:
                    for (TreeUnit unit : area.getUnits())
                    {
                        DefaultMutableTreeNode unitNode = new DefaultMutableTreeNode(unit);
                        areaNode.add(unitNode);
                    }// for
                                
                    plantNode.add(areaNode);
                }// for
            
                root.add(plantNode);
            }// for
            
            // Reload tree:
            treeModel.reload(root);
        
            // Restore panel configuration:
            _applyConfig();
        }// setPlantsTree
    }// _PlantsTreeUpdatesHandler
        
    
    /**
     * Gets expanded tree nodes list and returns them as two elements array of 
     * expansion paths where first element is expanded plants token plants and
     * second - expanded areas token.
     * 
     * @return Two elements array of expansion paths for plants and areas
     */
    public String[] getTreeExpansionState()
    {
        String[] result = new String[2];
        
        List plantsId = new ArrayList(), areasId = new ArrayList();
        String plantsIdString, areasIdString;
        
        Object root = plantsTree.getModel().getRoot();
        Enumeration enumeration = plantsTree.getExpandedDescendants(new TreePath(root));   
        
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
                    
                    if (nodeObject instanceof Plant)
                    {
                        Plant tempPlant = (Plant)nodeObject;
                        if (!plantsId.contains(tempPlant.getId())) plantsId.add(tempPlant.getId());
                    }// if
                    
                    if (nodeObject instanceof TreeArea)
                    {
                        TreeArea tempArea = (TreeArea)nodeObject;
                        areasId.add(tempArea.getPlant() + PAU_OBJECTS_ID_SEPARATOR + tempArea.getName());
                    }// if
                }// for
            }// while
        }// if
        
        // Convert expanded nodes identifiers arrays to string tokens:
        plantsIdString = plantsId.toString();
        areasIdString = areasId.toString();
        
        String replaceExp = "[\\[\\]\\s]";
        plantsIdString = plantsIdString.replaceAll(replaceExp, "");
        areasIdString  = areasIdString.replaceAll(replaceExp, "");
        
        // Return tokens as array of two elements:
        result[0] = plantsIdString;
        result[1] = areasIdString;
                
        return result;
    }// getTreeExpansionState
       
    
    /**
     * Returns PAU tree selection state as array of two elements. First one
     * is type of selected object, second - object's identifier.
     * 
     * @return String array of two elements - object's type and object's identifier
     */
    public String[] getTreeSelectionState()
    {
        String[] result = new String[2];
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)plantsTree.getLastSelectedPathComponent();
        
        Object nodeObject = node.getUserObject();
                    
        // Combine tree selection state array depending on selected object type:
        if (nodeObject instanceof Plant)
        {
            Plant plant = (Plant)nodeObject;
            result[0] = PLANT_NODE_TYPE_NAME;
            result[1] = plant.getId();
            
        } else if (nodeObject instanceof TreeArea) {
            
            TreeArea area = (TreeArea)nodeObject;
            result[0] = AREA_NODE_TYPE_NAME;
            result[1] = area.getPlant() + PAU_OBJECTS_ID_SEPARATOR + area.getName();
                    
        } else if (nodeObject instanceof TreeUnit) {
                
            TreeUnit unit = (TreeUnit)nodeObject;
            result[0] = UNIT_NODE_TYPE_NAME;
            result[1] = unit.getPlant() + PAU_OBJECTS_ID_SEPARATOR + unit.getArea() + PAU_OBJECTS_ID_SEPARATOR + unit.getName();
        }// else if
        
        return result;
    }// getTreeSelectionState
    
    
    /**
     * Restores panel settings from configuration object.
     */
    public void _applyConfig()
    {
        _setTreeExpansionState(config.getExpandedPlants(), config.getExpandedAreas());
        _setTreeSelectionState(config.getSelectedNodeType(), config.getSelectedNodeId());
    }// _applyConfig
    
    
    /**
     * Applies PAU tree nodes expansion state settings.
     *
     * @param expandedPlants Expanded plant nodes identifiers token string
     * @param expandedAreas Expanded area nodes identifiers token string
     */
    private void _setTreeExpansionState(String expandedPlants, String expandedAreas)
    {
        // Extract expanded plants identifiers from token:
        StringTokenizer plantsId = new StringTokenizer(expandedPlants, PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR);
        String tempPlantId; 
        Plant tempPlant;
        
        while (plantsId.hasMoreTokens())
        {
            tempPlantId = plantsId.nextToken();
            
            for (int i = 0; i < plantsTree.getRowCount(); i++)
            {
                TreePath path = plantsTree.getPathForRow(i);
                DefaultMutableTreeNode plantNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                
                if (plantNode.getUserObject() instanceof Plant)
                {
                    tempPlant = (Plant)plantNode.getUserObject();
                    
                    if (tempPlant.getId().equals(tempPlantId)) plantsTree.expandRow(i);
                }// if
            }// for
        }// while
        
        // Extract expanded areas identifiers from token:
        StringTokenizer areasId = new StringTokenizer(expandedAreas, PLANTS_TREE_EXPANDED_OBJECTS_CODES_SEPARATOR);
        String tempAreaId, tempAreaName;
        TreeArea tempArea;
        
        while (areasId.hasMoreTokens())
        {
            tempAreaId = areasId.nextToken();
            String[] areaIdComponents = tempAreaId.split(PAU_OBJECTS_ID_SEPARATOR);
            tempPlantId = areaIdComponents[0];
            tempAreaName = areaIdComponents[1];
            
            for (int i = 0; i < plantsTree.getRowCount(); i++)
            {
                TreePath path = plantsTree.getPathForRow(i);
                DefaultMutableTreeNode areaNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                
                if (areaNode.getUserObject() instanceof TreeArea)
                {
                    tempArea = (TreeArea)areaNode.getUserObject();
                    
                    if (tempArea.getPlant().equals(tempPlantId)
                        && tempArea.getName().equals(tempAreaName)) plantsTree.expandRow(i);
                }// if
            }// for
        }// while
    }// _setTreeExpansionState
    
    
    /**
     * Restores PAU tree nodes selection state settings.
     * 
     * @param selectedNodeType Selected node type
     * @param selectedNodeId Selected node identifier
     */
    private void _setTreeSelectionState(String selectedNodeType, String selectedNodeId)
    {
        TreePath tempPath;
        DefaultMutableTreeNode tempNode;
        Object tempNodeObject;
        String tempPlantId;
        String tempAreaName;
        String tempUnitName;
        
        for (int i = 0; i < plantsTree.getRowCount(); i++)
        {
            tempPath = plantsTree.getPathForRow(i);
            tempNode = (DefaultMutableTreeNode)tempPath.getLastPathComponent();
            tempNodeObject = tempNode.getUserObject();
            
            if (tempNodeObject instanceof Plant 
                && selectedNodeType.equals(PLANT_NODE_TYPE_NAME))
            {
                Plant plant = (Plant)tempNodeObject;
                
                if (selectedNodeId.equals(plant.getId()))
                {
                     plantsTree.setSelectionRow(i);
                     break;
                }// if
            
            } else if (tempNodeObject instanceof TreeArea
                && selectedNodeType.equals(AREA_NODE_TYPE_NAME)) {
            
                TreeArea area = (TreeArea)tempNodeObject;
                
                String[] areaIdComponents = selectedNodeId.split(PAU_OBJECTS_ID_SEPARATOR);
                tempPlantId = areaIdComponents[0];
                tempAreaName = areaIdComponents[1];
                
                if (tempPlantId.equals(area.getPlant()) && tempAreaName.equals(area.getName()))
                {
                    plantsTree.setSelectionRow(i);
                    break;
                }// if
                    
            } else if (tempNodeObject instanceof TreeUnit
                && selectedNodeType.equals(UNIT_NODE_TYPE_NAME)) {
                
                TreeUnit unit = (TreeUnit)tempNodeObject;
                
                String[] unitIdComponents = selectedNodeId.split(PAU_OBJECTS_ID_SEPARATOR);
                tempPlantId = unitIdComponents[0];
                tempAreaName = unitIdComponents[1];
                tempUnitName = unitIdComponents[2];
                
                if (tempPlantId.equals(unit.getPlant()) 
                    && tempAreaName.equals(unit.getArea())
                    && tempUnitName.equals(unit.getName()))
                {
                    plantsTree.setSelectionRow(i);
                    break;
                }// if
            }// else if
        }// for
    }// _setTreeSelectionState
    
    
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
     * Handles PAU tree node selection event and triggers appropriate event with
     * selected node user object data.
     * 
     * @param evt Tree event object
     */
    private void plantsTreeValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_plantsTreeValueChanged

        DefaultMutableTreeNode node = (DefaultMutableTreeNode)plantsTree.getLastSelectedPathComponent();

        if (node != null)
        {
            CustomEvent plantsTreeChangeSelectionEvent = new CustomEvent(node.getUserObject());
            trigger(ViewEvent.PLANTS_TREE_NODE_SELECTION, plantsTreeChangeSelectionEvent);
        }// if
    }//GEN-LAST:event_plantsTreeValueChanged

    
    /**
     * Handles PAU tree node collapsed event and triggers appropriate event with
     * expansion state tokens data.
     * 
     * @param evt Tree event object
     */
    private void plantsTreeTreeCollapsed(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_plantsTreeTreeCollapsed
        
         CustomEvent plantsTreeChangeExpansionEvent = new CustomEvent(getTreeExpansionState());
         trigger(ViewEvent.PLANTS_TREE_EXPANSION_STATE_CHANGE, plantsTreeChangeExpansionEvent);
    }//GEN-LAST:event_plantsTreeTreeCollapsed

    
    /**
     * Handles PAU tree node expanded event and triggers appropriate event with
     * expansion state tokens data.
     * 
     * @param evt Tree event object
     */
    private void plantsTreeTreeExpanded(javax.swing.event.TreeExpansionEvent evt) {//GEN-FIRST:event_plantsTreeTreeExpanded
        
        CustomEvent myEvent = new CustomEvent(getTreeExpansionState());
        this.events.trigger(ViewEvent.PLANTS_TREE_EXPANSION_STATE_CHANGE, myEvent);
    }//GEN-LAST:event_plantsTreeTreeExpanded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTree plantsTree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
}// PlantsTreePanel
