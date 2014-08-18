package ru.sakhalinenergy.alarmtripsettings.views.panel.tags;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.implemented.SettingsTypes;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.logic.settings.SettingsSelector;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Tag;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagSetting;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Source;


/**
 * Implements cell renderer for tags tree.
 * 
 * @author Denis Udovenko
 * @version 1.0.3
 */
public class TagsTreeCellRenderer extends DefaultTreeCellRenderer
{
    private final SettingsSelector parentLoopWraper;
    
    // Cells background colors for alam groups cells:
    private final Color ALARM_LOW_LOW_GROUP_COLOR = new Color(245, 240, 255);
    private final Color ALARM_LOW_GROUP_COLOR = new Color(223, 253, 255);
    private final Color ALARM_HIGH_GROUP_COLOR = new Color(209, 255, 214);
    private final Color ALARM_HIGH_HIGH_GROUP_COLOR = new Color(250, 255, 222);
    
    
    /**
     * Public constructor. Sets loop wrapper instance.
     * 
     * @param parentLoopWraper Wrapped loop instance, from which tree was built
     */
    public TagsTreeCellRenderer(SettingsSelector parentLoopWraper)
    {
        this.parentLoopWraper = parentLoopWraper;
    }// TagsTreeCellRenderer
    
    
    /**
     * Returns component that renderer uses to draw the value depending on node 
     * object type.
     * 
     * @param tree JTree the receiver is being configured for
     * @param value Value of the current tree cell to be set
     * @param selected Specifies that cell will be drawn as selected
     * @param expanded Specifies that node will be drawn in expended state
     * @param leaf Specifies that node represents a leaf 
     * @param row Row index in tree
     * @param hasFocus Specifies that node currently has focus
     * @return Component that renderer uses to draw the value depending on node object type
     */
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, 
            leaf, row, hasFocus);
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object nodeObject = node.getUserObject();
        
        ImageIcon icon = null;
        Color backgroundColor = null;
        Boolean opaque = false;
        Border border = null;
        
        Source tempSource;
               
        // If current node is data source:
        if (nodeObject instanceof Source)
        {
            tempSource = (Source)nodeObject;
            if (tempSource.getTypeId() == SourcesTypes.INTOOLS_EXPORT_DOCUMENT.ID) icon = Main.intoolsIcon;
            if (tempSource.getTypeId() == SourcesTypes.ALARM_AND_TRIP_SCHEDULE.ID) icon = Main.documentsIcon;
            if (tempSource.getTypeId() == SourcesTypes.DCS_VARIABLE_TABLE.ID) icon = Main.dcsIcon;
            if (tempSource.getTypeId() == SourcesTypes.ESD_VARIABLE_TABLE.ID) icon = Main.esdIcon;
            if (tempSource.getTypeId() == SourcesTypes.FGS_VARIABLE_TABLE.ID) icon = Main.fgsIcon;
        }// if
        
        // If current node is tag:
        if (nodeObject instanceof Tag) icon = Main.tagIcon;
        
        // If current node is tag setting:
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
            }// if
        }// if
                
        setBorder(border);
        setOpaque(opaque);
        setBackground(backgroundColor);    
        setIcon(icon);
        
        return this;
    }// getTreeCellRendererComponent
}// TagsTreeCellRenderer 
