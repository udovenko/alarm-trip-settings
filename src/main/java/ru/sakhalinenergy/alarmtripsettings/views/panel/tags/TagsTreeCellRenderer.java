package ru.sakhalinenergy.alarmtripsettings.views.panel.tags;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.Main;


/**
 * @author   Denis.Udovenko
 * @version  1.0.3
 */
public class TagsTreeCellRenderer extends DefaultTreeCellRenderer
{
    private final SettingsSelector parentLoopWraper;
    
    //Цвета ячеек для групп алармов:
    private final Color ALARM_LOW_LOW_GROUP_COLOR = new Color(245, 240, 255);
    private final Color ALARM_LOW_GROUP_COLOR = new Color(223, 253, 255);
    private final Color ALARM_HIGH_GROUP_COLOR = new Color(209, 255, 214);
    private final Color ALARM_HIGH_HIGH_GROUP_COLOR = new Color(250, 255, 222);
    
    
    /**
     * Public constructor. 
     * 
     * @param parentLoopWraper Wrapped parent loop instance, from which tree was built
     */
    public TagsTreeCellRenderer(SettingsSelector parentLoopWraper)
    {
        this.parentLoopWraper = parentLoopWraper;
    }//TagsTreeCellRenderer
    
    
    /**
     * Метод перегружает стандартный отрисовщик узла дерева тагов и 
     * устанавливает соответствующие иконки в зависимости от класса или 
     * содержания узла.
     * 
     * @param tree Текущий экземпляр дерева
     * @param value Экземпляр текущего выбранного узла дерева
     * @param sel Флаг "узел выбран"
     * @param expanded Флаг "узел развернут"
     * @param leaf Флаг "узел является листом", т.е не имеет детей
     * @param row Индекс строки
     * @param hasFocus Флаг "установлен фокус"
     * @return Component Отредактированный экземпляр узла.
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, 
            leaf, row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object nodeObject = node.getUserObject();
        
        ImageIcon icon = null;
        Color backgroundColor = null;
        Boolean opaque = false;
        Border border = null;
        
        Source tempSource;
               
        //Если узел - источник списка тагов SPI:
        if (nodeObject instanceof Source)
        {
            tempSource = (Source)nodeObject;
            if (tempSource.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon = Main.intoolsIcon;
            if (tempSource.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
            if (tempSource.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
            if (tempSource.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
            if (tempSource.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
        }//if
        
        //Если узел - заголовк тага:
        if (nodeObject instanceof Tag) icon = Main.tagIcon;
        
        //Если узел - значение параметра тага:
        if (nodeObject instanceof TagSetting)
        {    
            icon = Main.settingIcon;
            TagSetting setting = (TagSetting)nodeObject;
            
            if (setting == parentLoopWraper.getChosenDocumentsAlarmH() || setting == parentLoopWraper.getChosenDocumentsAlarmHH()
                || setting == parentLoopWraper.getChosenDocumentsAlarmL() || setting == parentLoopWraper.getChosenDocumentsAlarmLL()
                || setting == parentLoopWraper.getChosenIntoolsAlarmH() || setting == parentLoopWraper.getChosenIntoolsAlarmHH()
                || setting == parentLoopWraper.getChosenIntoolsAlarmL() || setting == parentLoopWraper.getChosenIntoolsAlarmLL()
                || setting == parentLoopWraper.getChosenSystemsAlarmH() || setting == parentLoopWraper.getChosenSystemsAlarmHH()
                || setting == parentLoopWraper.getChosenSystemsAlarmL() || setting == parentLoopWraper.getChosenSystemsAlarmLL())
            {    
                opaque = true;
                border = new MatteBorder(1, 1, 1, 1, Color.BLACK);
                               
                if (setting.getTypeId() == SettingsTypes.ALARM_LL_SETTING.ID)
                    backgroundColor = ALARM_LOW_LOW_GROUP_COLOR;
                
                if (setting.getTypeId() == SettingsTypes.ALARM_L_SETTING.ID)
                    backgroundColor = ALARM_LOW_GROUP_COLOR;
                
                if (setting.getTypeId() == SettingsTypes.ALARM_H_SETTING.ID)
                    backgroundColor = ALARM_HIGH_GROUP_COLOR;
                
                if (setting.getTypeId() == SettingsTypes.ALARM_HH_SETTING.ID)
                    backgroundColor = ALARM_HIGH_HIGH_GROUP_COLOR;
            }//if
        }//if
                
        this.setBorder(border);
        this.setOpaque(opaque);
        this.setBackground(backgroundColor);    
        this.setIcon(icon);
        return this;
    }//getTreeCellRendererComponent
}//TagsTreeRenderer 
