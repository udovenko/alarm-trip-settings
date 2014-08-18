package ru.sakhalinenergy.alarmtripsettings.views.panel.tags;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSettingProperty;


/**
 * Implements a set of static methods for tags tree manipulation.
 * 
 * @author Denis Udovenko
 * @version 1.0.7
 */
public class TagsTreeOperator
{
    
    /**
     * Builds tags tree for given loop starting from given root node.
     * 
     * @param root Root node
     * @param loop Loop entity instance whose tags tree will be built
     */
    public static void buildTree(DefaultMutableTreeNode root, Loop loop)
    {
        root.removeAllChildren();
                       
        // Build data sources branches with tags:
        _buildDataSourcesBranches(root, loop);
    }// buildTree
    
    
    /**
     * Removes all child nodes of given root node.
     *
     * @param root Root node
     */
    public static void clearTree(DefaultMutableTreeNode root)
    {
        root.removeAllChildren();
    }// clearTree
    
    
    /**
     * Builds branches for parent data sources of tags from current loop. Adds 
     * tags and tag settings nodes to parents source branches, then adds built
     * branches to root node.
     * 
     * @param root Root node
     * @param loop Loop entity instance whose tags tree will be built
     */
    private static void _buildDataSourcesBranches(DefaultMutableTreeNode root, Loop loop)
    {
        Set<Tag> tags = loop.getTags();
        Source tempSource;
        int tempSourceId;
        
        // Initialize data source nodes hash:
        HashMap<Integer, DefaultMutableTreeNode> dataSourcesNodesMap = new HashMap();
        
        // Create and group tag nodes into data source brnches:
        for (Tag tempTag : tags)
        {
            tempSource = tempTag.getSource();
            tempSourceId = tempSource.getId();
            
            // If data source node is aleready created:
            if (dataSourcesNodesMap.containsKey(tempSourceId))
            {
                DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(tempTag);
            
                // Add settings branch to tag node:
                _buildSettingsBranch(intoolsTagNode, tempTag);
                dataSourcesNodesMap.get(tempSourceId).add(intoolsTagNode);
                
            } else { // If data source node wasn't created yet:
            
                DefaultMutableTreeNode dataSourceNode = new DefaultMutableTreeNode(tempSource);
                DefaultMutableTreeNode tagNode = new DefaultMutableTreeNode(tempTag);
                
                // Add settings branch to tag node:
                _buildSettingsBranch(tagNode, tempTag);
                dataSourceNode.add(tagNode);
                dataSourcesNodesMap.put(tempSourceId, dataSourceNode);
            }// else
        }// for
        
        // Get data source nodes collection for sorting and adding to the root:
        List<DefaultMutableTreeNode> intoolsDataSourcesNodesList = new ArrayList(dataSourcesNodesMap.values());
        
        // Sort data source nodes by source type and priority:
        Collections.sort(intoolsDataSourcesNodesList, new Comparator<DefaultMutableTreeNode>()
        {
            @Override
            public int compare(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2)
            {
                Source source1 = (Source)node1.getUserObject();
                Source source2 = (Source)node2.getUserObject();
                
                if (source1.getTypeId() == source2.getTypeId()) return source2.getPriority() - source1.getPriority();
                else return source1.getTypeId() - source2.getTypeId();
            }// compare
        });// sort
        
        // Add data source nodes to the root:
        for (DefaultMutableTreeNode node : intoolsDataSourcesNodesList) root.add(node);
    }// _buildDataSourcesBranches 
    
        
    /**
     * Builds tag's node settings branch.
     * 
     * @param tagNode Tag's node whose settings branch will be built
     * @param tag Tag entity instance
     */
    private static void _buildSettingsBranch(DefaultMutableTreeNode tagNode, Tag tag)
    {
        Set<TagSetting> settings = tag.getSettings();
        List<TagSetting> settingsList = new ArrayList(settings);
        
        // Sort settings collection by setting type:
        Collections.sort(settingsList, new Comparator<TagSetting>()
        {
            @Override
            public int compare(TagSetting setting1, TagSetting setting2)
            {
                return setting1.getTypeId() - setting2.getTypeId();
            }// compare
        });// sort
        
        // Build setting properties branch for each setting and add setting branch to a tag:
        for (TagSetting tempSetting : settingsList)
        {
            DefaultMutableTreeNode settingNode = new DefaultMutableTreeNode(tempSetting);
            _buildSettingPropertiesBranch(settingNode, tempSetting);
            tagNode.add(settingNode);
        }// for
    }// _buildSettingsBranch
    
    
    /**
     * Builds tag's setting property branch.
     *
     * @param settingNode Tag's setting none whose settings branch will be built
     * @param setting Setting entity instance
     */
    private static void _buildSettingPropertiesBranch(DefaultMutableTreeNode settingNode, TagSetting setting)
    {
        for (TagSettingProperty tempSettingProperty : setting.getProperties())
        {
            DefaultMutableTreeNode settingPropertyNode = new DefaultMutableTreeNode(tempSettingProperty);
            settingNode.add(settingPropertyNode);
        }// for
    }// _buildSettingPropertiesBranch
}// TagsTreeOperator
