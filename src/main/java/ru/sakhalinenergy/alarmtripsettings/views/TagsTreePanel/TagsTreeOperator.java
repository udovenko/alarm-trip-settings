package ru.sakhalinenergy.alarmtripsettings.views.TagsTreePanel;

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
 * Класс - набоор статических методов для управления деревом тагов выбранного 
 * устройства.
 * 
 * @author   Denis.Udovenko
 * @version  1.0.7
 */
public class TagsTreeOperator
{
    
    /**
     * Метод строит дерево тагов полученного устройства от заданного корневого 
     * узла.
     * 
     * @param   root    Корневой узел
     * @param   device  Экземпляр устройства, дерево тагов которого строим
     * @return  void
     */
    public static void buildTree(DefaultMutableTreeNode root, Loop loop)
    {
        root.removeAllChildren();
                       
        //Инициализируем список источников данных результатов экспорта из SPI:
        _addIntoolsSourcesNodes(root, loop);
    }//buildTree
    
    
    /**
     * Метод очищает заданный корневой узел от всех дочерних узлов.
     *
     * @param   root  Корневой узел, детей которого удаляем
     * @return  void 
     */
    public static void clearTree(DefaultMutableTreeNode root)
    {
        root.removeAllChildren();
    }//clearTree
    
    
    /**
     * Метод добавляет к корневому узлу отсортированные по приоритету узлы
     * источников данных тагов SPI с уже достроенными списками тагов и их 
     * свойств.
     * 
     * @param root Корневой узел, к которому пристраиваем дерево тагов
     * @param device Экземпляр устройства, дерево тагов которого строим
     * @return  void
     */
    private static void _addIntoolsSourcesNodes(DefaultMutableTreeNode root, Loop loop)
    {
        Set<Tag> tags = loop.getTags();
        Source tempSource;
        int tempSourceId;
        
        //Инициализируем список источников данных результатов экспорта из SPI:
        HashMap<Integer, DefaultMutableTreeNode> dataSourcesNodesMap = new HashMap();
        
        //Группируем таги SPI по источникам данных:       
        for (Tag tempTag : tags)
        {
            tempSource = tempTag.getSource();
            tempSourceId = tempSource.getId();
            
            //Если узел источника уже создан:
            if (dataSourcesNodesMap.containsKey(tempSourceId))
            {
                DefaultMutableTreeNode intoolsTagNode = new DefaultMutableTreeNode(tempTag);
            
                //Достраиваем к узлу тага ветвь с параметрами:
                _buildSettingsBranch(intoolsTagNode, tempTag);
                dataSourcesNodesMap.get(tempSourceId).add(intoolsTagNode);
                
            } else { //Если узел источника еще не создан:
            
                DefaultMutableTreeNode dataSourceNode = new DefaultMutableTreeNode(tempSource);
                DefaultMutableTreeNode tagNode = new DefaultMutableTreeNode(tempTag);
                
                //Достраиваем к узлу тага ветвь с параметрами:
                _buildSettingsBranch(tagNode, tempTag);
                dataSourceNode.add(tagNode);
                dataSourcesNodesMap.put(tempSourceId, dataSourceNode);
            }//else
        }//for
        
        //Получаем коллекцию узлов для сортировки и добавления к корню:
        List<DefaultMutableTreeNode> intoolsDataSourcesNodesList = new ArrayList(dataSourcesNodesMap.values());
        
        //Сортируем коллекуию по типу источника и по приоритету:
        Collections.sort(intoolsDataSourcesNodesList, new Comparator<DefaultMutableTreeNode>()
        {
            @Override
            public int compare(DefaultMutableTreeNode node1, DefaultMutableTreeNode node2)
            {
                Source source1 = (Source)node1.getUserObject();
                Source source2 = (Source)node2.getUserObject();
                
                if (source1.getTypeId() == source2.getTypeId()) return source2.getPriority() - source1.getPriority();
                else return source1.getTypeId() - source2.getTypeId();
            }//compare
        });//sort
        
        //Добавляем в дерево все созданные узлы источников данных тагов SPI:
        for (DefaultMutableTreeNode node : intoolsDataSourcesNodesList)
        {
            root.add(node);
        }//for
    }//_addIntoolsSourcesNodes 
    
        
    /**
     * Метод строит ветку настроек для заданнгого узла тага.
     * 
     * @param tagNode Узел тага, для которого строим ветку параметров
     * @param tag Экземпляр тага, параметры которого выстраиваем
     * @return void
     */
    private static void _buildSettingsBranch(DefaultMutableTreeNode tagNode, Tag tag)
    {
        Set<TagSetting> settings = tag.getSettings();
        List<TagSetting> settingsList = new ArrayList(settings);
        
        //Сортируем коллекуию по типу настроек:
        Collections.sort(settingsList, new Comparator<TagSetting>()
        {
            @Override
            public int compare(TagSetting setting1, TagSetting setting2)
            {
                return setting1.getTypeId() - setting2.getTypeId();
            }//compare
        });//sort
        
        //Обходим все доступные настройки текущего тага:   
        for (TagSetting tempSetting : settingsList)
        {
            DefaultMutableTreeNode settingNode = new DefaultMutableTreeNode(tempSetting);
            _buildSettingPropertiesBranch(settingNode, tempSetting);
            tagNode.add(settingNode);
        }//for
    }//_buildSettingsBranch
    
    
    /**
     * Метод строит ветку свойств настройки тага прибора. 
     * 
     * @return  void
     */
    private static void _buildSettingPropertiesBranch(DefaultMutableTreeNode settingNode, TagSetting setting)
    {
        for (TagSettingProperty tempSettingProperty : setting.getProperties())
        {
            DefaultMutableTreeNode settingPropertyNode = new DefaultMutableTreeNode(tempSettingProperty);
            settingNode.add(settingPropertyNode);
        }//for
    }//_buildSettingPropertiesBranch
}//TagsTreeModel
