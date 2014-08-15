package ru.sakhalinenergy.alarmtripsettings.views.dialog.loop;

import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Loop;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;


/**
 * Класс - набоор статических методов для управления деревом тагов выбранного 
 * устройства.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.8
 */
public class TagsTreeOperator
{
    
    /**
     * Метод очищает заданный корневой узел от всех дочерних узлов.
     *
     * @param root Корневой узел, детей которого удаляем
     */
    public static void clearTree(DefaultMutableTreeNode root)
    {
        root.removeAllChildren();
    }//clearTree
    
    
    /**
     * Метод строит дерево тагов полученного устройства от заданного корневого 
     * узла.
     * 
     * @param   root    Корневой узел
     * @param   device  Экземпляр устройства, дерево тагов которого строим
     */
    public static void buildTree(DefaultMutableTreeNode root, Loop loop)
    {
        root.removeAllChildren();
                       
        //Инициализируем список источников данных результатов экспорта из SPI:
        HashMap<Integer, DefaultMutableTreeNode> intoolsDataSourcesNodesMap = new HashMap(),
            documentsDataSourcesNodesMap = new HashMap(), dcsDataSourcesNodesMap = new HashMap(),
            esdDataSourcesNodesMap = new HashMap(), fgsDataSourcesNodesMap = new HashMap();
        
        //Группируем таги SPI по источникам данных:       
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
        }//for
        
        //Получаем коллекции узлов для сортировки и добавления к корню:
        List<DefaultMutableTreeNode> intoolsDataSourcesNodesList   = new ArrayList(intoolsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> documentsDataSourcesNodesList = new ArrayList(documentsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> dscDataSourcesNodesList       = new ArrayList(dcsDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> esdDataSourcesNodesList       = new ArrayList(esdDataSourcesNodesMap.values());
        List<DefaultMutableTreeNode> fgsDataSourcesNodesList       = new ArrayList(fgsDataSourcesNodesMap.values());
        
        //Сортируем коллекуию по типу источника и по приоритету:
        _sortSourceNodes(intoolsDataSourcesNodesList);
        _sortSourceNodes(documentsDataSourcesNodesList);
        _sortSourceNodes(dscDataSourcesNodesList);
        _sortSourceNodes(esdDataSourcesNodesList);
        _sortSourceNodes(fgsDataSourcesNodesList);
                
        //Добавляем в дерево все созданные коллекции узлов источников данных:
        for (DefaultMutableTreeNode node : intoolsDataSourcesNodesList)   root.add(node);
        for (DefaultMutableTreeNode node : documentsDataSourcesNodesList) root.add(node);
        for (DefaultMutableTreeNode node : dscDataSourcesNodesList)       root.add(node);
        for (DefaultMutableTreeNode node : esdDataSourcesNodesList)       root.add(node);
        for (DefaultMutableTreeNode node : fgsDataSourcesNodesList)       root.add(node);
    }//buildTree
      
    
    /**
     * 
     */
    private static void _addTagToSourceNode(Tag tag, HashMap<Integer, DefaultMutableTreeNode> sourceNodesMap)
    {
        Source tempDataSource = tag.getSource();
        int tempDataSourceId = tempDataSource.getId();
        
        //Если узел источника уже создан:
        if (sourceNodesMap.containsKey(tempDataSourceId))
        {
            DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(new TagNodeModel(tag, false));

            //Достраиваем к узлу тага ветвь с параметрами:
            sourceNodesMap.get(tempDataSourceId).add(intoolsTagNode);

        } else { //Если узел источника еще не создан:

            DefaultMutableTreeNode dataSourceNode = new DefaultMutableTreeNode(tempDataSource);
            DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(new TagNodeModel(tag, false));

            //Достраиваем к узлу тага ветвь с параметрами:
            dataSourceNode.add(intoolsTagNode);
            sourceNodesMap.put(tempDataSourceId, dataSourceNode);
        }//else
    }// addTagToSourceNode
    
    
    /**
     * 
     */
    private static void _sortSourceNodes(List<DefaultMutableTreeNode> dataSourcesNodesList)
    {
        //Сортируем коллекуию по типу источника и по приоритету:
        Collections.sort(dataSourcesNodesList, new Comparator<DefaultMutableTreeNode>()
        {
            public int compare(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2)
            {
                Source source1 = (Source)node1.getUserObject();
                Source source2 = (Source)node2.getUserObject();
                
                return source2.getPriority() - source1.getPriority();
            }//compare
        });//sort
    }// _sortSourceNodes
}//TagsTreeOperator