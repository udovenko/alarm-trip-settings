package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;


/**
 * Implements set of static methods for tags and sources tree manipulation.
 * 
 * @author Denis Udovenko
 * @version 1.0.8
 */
public class TagsTreeOperator
{
    
    /**
     * Clears given root node from all children.
     *
     * @param root Root node whose children will be removed
     */
    public static void clearTree(DefaultMutableTreeNode root)
    {
        root.removeAllChildren();
    }// clearTree
    
    
    /**
     * Builds tags and sources tree for given loop starting from given root node.
     * 
     * @param root Root node
     * @param loop Loop entity instance
     */
    public static void buildTree(DefaultMutableTreeNode root, Loop loop)
    {
        root.removeAllChildren();
                       
        // Initialize data sources hashes:
        HashMap<Integer, DefaultMutableTreeNode> intoolsDataSourcesNodesMap = new HashMap(),
            documentsDataSourcesNodesMap = new HashMap(), dcsDataSourcesNodesMap = new HashMap(),
            esdDataSourcesNodesMap = new HashMap(), fgsDataSourcesNodesMap = new HashMap();
        
        // Group tags by data source types:
        for (Tag tempTag : loop.getTags())
        {
            if (tempTag.getSource().getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID)
                _addTagToSourceNode(tempTag, intoolsDataSourcesNodesMap);
            if (tempTag.getSource().getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID)
                _addTagToSourceNode(tempTag, documentsDataSourcesNodesMap);
            if (tempTag.getSource().getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID)
                _addTagToSourceNode(tempTag, dcsDataSourcesNodesMap);
            if (tempTag.getSource().getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID)
                _addTagToSourceNode(tempTag, esdDataSourcesNodesMap);
            if (tempTag.getSource().getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID)
                _addTagToSourceNode(tempTag, fgsDataSourcesNodesMap);
        }// for
        
        // Get source nodes collections for sorting and adding to root:
        List<DefaultMutableTreeNode> intoolsDataSourcesNodesList   = new ArrayList(intoolsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> documentsDataSourcesNodesList = new ArrayList(documentsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> dscDataSourcesNodesList       = new ArrayList(dcsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> esdDataSourcesNodesList       = new ArrayList(esdDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> fgsDataSourcesNodesList       = new ArrayList(fgsDataSourcesNodesMap.values());
        
        // Sort collections by data source type and priority:
        _sortSourceNodes(intoolsDataSourcesNodesList);
        _sortSourceNodes(documentsDataSourcesNodesList);
        _sortSourceNodes(dscDataSourcesNodesList);
        _sortSourceNodes(esdDataSourcesNodesList);
        _sortSourceNodes(fgsDataSourcesNodesList);
                
        // Add created collections to the tree root:
        for (DefaultMutableTreeNode node : intoolsDataSourcesNodesList)   root.add(node);
        for (DefaultMutableTreeNode node : documentsDataSourcesNodesList) root.add(node);
        for (DefaultMutableTreeNode node : dscDataSourcesNodesList)       root.add(node);
        for (DefaultMutableTreeNode node : esdDataSourcesNodesList)       root.add(node);
        for (DefaultMutableTreeNode node : fgsDataSourcesNodesList)       root.add(node);
    }// buildTree
      
    
    /**
     * Creates and adds tag node to appropriate data source node.
     * 
     * @param tag Tag entity instance
     * @param sourceNodesMap Data source nodes hash
     */
    private static void _addTagToSourceNode(Tag tag, HashMap<Integer, DefaultMutableTreeNode> sourceNodesMap)
    {
        Source tempDataSource = tag.getSource();
        int tempDataSourceId = tempDataSource.getId();
        
        // Is data source node is already created:
        if (sourceNodesMap.containsKey(tempDataSourceId))
        {
            DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(new TagNodeModel(tag, false));

            // Build and add settings branch to tag node:
            sourceNodesMap.get(tempDataSourceId).add(intoolsTagNode);

        } else { // If data source node is not crated yet:

            DefaultMutableTreeNode dataSourceNode = new DefaultMutableTreeNode(tempDataSource);
            DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(new TagNodeModel(tag, false));

            // Build and add settings branch to tag node:
            dataSourceNode.add(intoolsTagNode);
            sourceNodesMap.put(tempDataSourceId, dataSourceNode);
        }// else
    }// addTagToSourceNode
    
    
    /**
     * Implements data source nodes list sorter method.
     * 
     * @param dataSourcesNodesList List of data source nodes to be sorted
     */
    private static void _sortSourceNodes(List<DefaultMutableTreeNode> dataSourcesNodesList)
    {
        // Sort collection by data source type and priority:
        Collections.sort(dataSourcesNodesList, new Comparator<DefaultMutableTreeNode>()
        {
            public int compare(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2)
            {
                Source source1 = (Source)node1.getUserObject();
                Source source2 = (Source)node2.getUserObject();
                
                return source2.getPriority() - source1.getPriority();
            }// compare
        });// sort
    }// _sortSourceNodes
}// TagsTreeOperator