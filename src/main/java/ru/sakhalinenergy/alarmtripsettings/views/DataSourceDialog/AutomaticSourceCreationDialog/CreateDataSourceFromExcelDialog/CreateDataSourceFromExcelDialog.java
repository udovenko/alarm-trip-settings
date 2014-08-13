package ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.CreateDataSourceFromExcelDialog;

import java.awt.Component;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.TableColumn;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import ru.sakhalinenergy.alarmtripsettings.Main;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.implemented.SourcesTypes;
import ru.sakhalinenergy.alarmtripsettings.models.config.ExcelBookParsingDialogSettingsObservable;
import ru.sakhalinenergy.alarmtripsettings.models.entity.Plant;
import ru.sakhalinenergy.alarmtripsettings.models.entity.TagMask;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.PlantsLogicObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.collection.TagMasksObservable;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.SourceEvent;
import ru.sakhalinenergy.alarmtripsettings.models.logic.source.ExcelBookObservable;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.DataSourceDialog;
import ru.sakhalinenergy.alarmtripsettings.views.DataSourceDialog.AutomaticSourceCreationDialog.ViewEvent;


/**
 * Implements dialog for creating data source from sheet of MS Excel book.
 * 
 * @author Denis Udovenko
 * @version 1.0.8
 */
public class CreateDataSourceFromExcelDialog extends DataSourceDialog implements CreateDataSourceFromExcelDialogObservable
{

    private static final String NO_FIELD_SELECTED_VALUE = null;
    
    private static final Byte KEY_FIELD_COLUMN_INDEX           = 1;
    private static final Byte KEY_VALUE_COLUMN_INDEX           = 2;
    private static final Byte VALUE_FIELD_COLUMN_INDEX         = 3;
    private static final Byte POSSIBLE_FLAG_FIELD_COLUMN_INDEX = 4;
    
    private static final Byte ALARM_LOW_LOW_ROW_INDEX   = 0;
    private static final Byte ALARM_LOW_ROW_INDEX       = 1;
    private static final Byte ALARM_HIGH_ROW_INDEX      = 2;
    private static final Byte ALARM_HIGH_HIGH_ROW_INDEX = 3;
    private static final Byte RANGE_MIN_ROW_INDEX       = 4;
    private static final Byte RANGE_MAX_ROW_INDEX       = 5;
    private static final Byte UNITS_ROW_INDEX           = 6;
    private static final Byte SOURCE_SYSTEM_ROW_INDEX   = 7;
        
    
    /**
     * Public constructor. Sets up dialog fields and initializes components.
     * 
     * @param model Wrapper model for creating data source from MS Excel book
     * @param plants Plants collection
     * @param tagMasks Tag masks (formats) collection
     * @param config Dialog configuration object
     */
    public CreateDataSourceFromExcelDialog(ExcelBookObservable model, PlantsLogicObservable plants, 
        TagMasksObservable tagMasks, ExcelBookParsingDialogSettingsObservable config) 
    {
        super(model, plants, tagMasks, config);
                  
        // Subscribe on model's events:
        this.model.on(SourceEvent.BOOK_CONNECTED, new _BookConnectedEventHandler());
                    
        initComponents();
        
        // Set dialog icon:
        this.setIconImage(Main.excelIcon.getImage());
        this.setModal(true);
        
        // Forbid columns permutation:
        parsingSettingsTable.getTableHeader().setReorderingAllowed(false);
        parsingSettingsTable.getTableHeader().setResizingAllowed(false);
        
        try // Set data source types list:
        {
            for (Class innerClass : SourcesTypes.class.getClasses())
            {
                dataSourceToBeCreatedTypeComboBox.addItem(innerClass.getDeclaredField("NAME").get(null));
            }// for
        } catch (Exception exception) {}
        dataSourceToBeCreatedTypeComboBox.setRenderer(new DataSourcesListCellRenderer());
        dataSourceToBeCreatedTypeComboBox.setSelectedItem(config.getDataSourceTypeName());
    }// CreateDataSourceFromExcelDialog

    
    /**
     * Render required objects lists, applies dialog settings and shows form on
     * the screen.
     * 
     * @param parent Parent component, relative to which current dialog will be rendered
     */
    public void render(Component parent)
    {
        // Build palants list and restore plant selection:
        _buildPlantsList(plantsComboBox);

        // Build tag formats list and restore format selection:
        _buildTagMasksList(tagFormatComboBox);
                
        // Block sheet settings controls until sheet structure will be received:
        _setSettingsEnabled(false);
        
        // Set relative location and show dialog:
        setLocationRelativeTo(parent);
        _show();
    }// render
    
    
    /**
     * Inner class - handler for model's book connection establishing event.
     * 
     * @author Denis Udovenko
     * @version 1.0.2
     */
    private class _BookConnectedEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            ExcelBookObservable castedModel = (ExcelBookObservable)model;
            pathToSpreadsheetTextField.setText(castedModel.getBookFilePath());
            HashMap sheetsAndFields = castedModel.getSheetsAndFields();
             _setSheetNamesList(new ArrayList(sheetsAndFields.keySet()));
             _setSettingsEnabled(true);
             _applyConfig();
        }// customEventOccurred
    }// _BookConnectedEventHandler
    
    
    /**
     * Sets up sheet names list of connected MS Excel book.
     * 
     * @param sheetNamesList Sheet names list
     */
    private void _setSheetNamesList(List<String> sheetNamesList)
    {
        this.sourceSheetNameComboBox.removeAllItems();
        for (String name : sheetNamesList)
        {
            this.sourceSheetNameComboBox.addItem(name);
        }// for
    }// setSheetNamesList
          
    
    /**
     * Enable/disables MS Excel book parsing control elements.
     * 
     * @param enabled Flag specifies should controls be enabled or disabled
     */
    private void _setSettingsEnabled(boolean enabled)
    {
        this.sourceSheetNameComboBox.setEnabled(enabled);
        this.tagNameFieldNameComboBox.setEnabled(enabled);
        this.parsingSettingsTable.setEnabled(enabled);
        this.runParsingButton.setEnabled(enabled);
    }// disableSettings
          
    
    /**
     * Applies MS Excel book parsing configuration if configuration settings 
     * match to received selected sheet structure.
     */
    private void _applyConfig()
    {
        // Cast models to concrete classes:
        ExcelBookObservable castedModel = (ExcelBookObservable)model;
        ExcelBookParsingDialogSettingsObservable castedConfig = (ExcelBookParsingDialogSettingsObservable)config;

        // Get available fields list of current sheet:
        String sheetName = (String)sourceSheetNameComboBox.getSelectedItem();
        List<String> fields = (List)castedModel.getSheetsAndFields().get(sheetName);
        
        // Restore tag name selection:
        for (String tempField : (List<String>)castedModel.getSheetsAndFields().get(sheetName))
        {    
            if (tempField.equals(castedConfig.getTagNameField()))
            {    
                this.tagNameFieldNameComboBox.setSelectedItem(tempField);
                break;
            }// if
        }// for
                
        // Restore LL alarm parsing settings selection:
        if (fields.indexOf(castedConfig.getAlarmLowLowKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowLowKeyField(), ALARM_LOW_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getAlarmLowLowKeyValue(), ALARM_LOW_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmLowLowValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowLowValueField(), ALARM_LOW_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmLowLowPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowLowPossibleFlagField(), ALARM_LOW_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        // Restore L alarm parsing settings selection:
        if (fields.indexOf(castedConfig.getAlarmLowKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowKeyField(), ALARM_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getAlarmLowKeyValue(), ALARM_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmLowValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowValueField(), ALARM_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmLowPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmLowPossibleFlagField(), ALARM_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        // Restore H alarm parsing settings selection:
        if (fields.indexOf(castedConfig.getAlarmHighKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighKeyField(), ALARM_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getAlarmHighKeyValue(), ALARM_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmHighValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighValueField(), ALARM_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmHighPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighPossibleFlagField(), ALARM_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);

        // Restore HH alarm parsing settings selection:
        if (fields.indexOf(castedConfig.getAlarmHighHighKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighHighKeyField(), ALARM_HIGH_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getAlarmHighHighKeyValue(), ALARM_HIGH_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmHighHighValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighHighValueField(), ALARM_HIGH_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getAlarmHighHighPossibleFlagField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getAlarmHighHighPossibleFlagField(), ALARM_HIGH_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        // Restore range min parsing settings selection:
        if (fields.indexOf(castedConfig.getRangeMinKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getRangeMinKeyField(), RANGE_MIN_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getRangeMinKeyValue(), RANGE_MIN_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getRangeMinValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getRangeMinValueField(), RANGE_MIN_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        // Restore range max parsing settings selection:
        if (fields.indexOf(castedConfig.getRangeMaxKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getRangeMaxKeyField(), RANGE_MAX_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getRangeMaxKeyValue(), RANGE_MAX_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getRangeMaxValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getRangeMaxValueField(), RANGE_MAX_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        // Restore engineering units parsing settings selection:
        if (fields.indexOf(castedConfig.getUnitsKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getUnitsKeyField(), UNITS_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getUnitsKeyValue(), UNITS_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getUnitsValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getUnitsValueField(), UNITS_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
        
        // Restore source system parsing settings selection:
        if (fields.indexOf(castedConfig.getSourceSystemKeyField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getSourceSystemKeyField(), SOURCE_SYSTEM_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
        parsingSettingsTable.setValueAt(castedConfig.getSourceSystemKeyValue(), SOURCE_SYSTEM_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
        if (fields.indexOf(castedConfig.getSourceSystemValueField()) != -1) parsingSettingsTable.setValueAt(castedConfig.getSourceSystemValueField(), SOURCE_SYSTEM_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// _applyConfig

    
    /**
     * Returns selected plant.
     * 
     * @return Selected plant
     */
    @Override
    public Plant getPlant()
    {
        return (Plant)plantsComboBox.getSelectedItem();
    }// getPlant
        
    
    /**
     * Returns selected data source type.
     * 
     * @return Selected data source type
     */
    @Override
    public String getDataSourceType()
    {
        return (String)dataSourceToBeCreatedTypeComboBox.getSelectedItem();
    }// getDataSourceType
    
    
    /**
     * Returns selected tag mask.
     *
     * @return Selected tag mask
     */
    @Override
    public TagMask getTagMask()
    {
        return (TagMask)tagFormatComboBox.getSelectedItem();
    }// getTagMask
    
    
    /**
     * Returns selected sheet name of MS Excel book. 
     * 
     * @return Selected sheet name
     */
    @Override
    public String getSheetName()
    {
        return (String)this.sourceSheetNameComboBox.getSelectedItem();
    }// getSheetName
    
    
    /**
     * Returns field selected as tag name container.
     * 
     * @return Field selected as tag name container
     */
    @Override
    public String getTagFieldName()
    {
        return (String)this.tagNameFieldNameComboBox.getSelectedItem();
    }// getTagFieldName
    
    
    /**
     * Returns field selected as LL alarm setting key container.
     * 
     * @return Field selected as LL alarm key container
     */
    @Override
    public String getAlarmLowLowKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getAlarmLowLowKeyField
    
    
    /**
     * Returns field selected as LL alarm setting key value container.
     * 
     * @return Field selected as LL alarm setting key value container
     */
    @Override
    public String getAlarmLowLowKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getAlarmLowLowKeyValue
    
    
    /**
     * Returns field selected as LL alarm setting value container.
     * 
     * @return Field selected as LL alarm setting value container
     */
    @Override
    public String getAlarmLowLowValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getAlarmLowLowValueField
    
    
    /**
     * Returns field selected as LL alarm setting "Possible" flag container.
     * 
     * @return Field selected as LL alarm setting "Possible" flag container
     */
    @Override
    public String getAlarmLowLowPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }// getAlarmLowLowPossibleFlagField
    
    
    /**
     * Returns field selected as L alarm setting key container.
     * 
     * @return Field selected as L alarm key container
     */
    @Override
    public String getAlarmLowKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getAlarmLowKeyField
    
    
    /**
     * Returns field selected as L alarm setting key value container.
     * 
     * @return Field selected as L alarm setting key value container
     */
    @Override
    public String getAlarmLowKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getAlarmLowKeyValue
    
    
    /**
     * Returns field selected as L alarm setting value container.
     * 
     * @return Field selected as L alarm setting value container
     */
    @Override
    public String getAlarmLowValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getAlarmLowValueField
    
    
    /**
     * Returns field selected as L alarm setting "Possible" flag container.
     * 
     * @return Field selected as L alarm setting "Possible" flag container
     */
    @Override
    public String getAlarmLowPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_LOW_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }// getAlarmLowPossibleFlagField
    
    
    /**
     * Returns field selected as H alarm setting key container.
     * 
     * @return Field selected as H alarm key container
     */
    @Override
    public String getAlarmHighKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getAlarmHighKeyField
    
    
    /**
     * Returns field selected as H alarm setting key value container.
     * 
     * @return Field selected as H alarm setting key value container
     */
    @Override
    public String getAlarmHighKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getAlarmHighKeyValue
    
    
    /**
     * Returns field selected as H alarm setting value container.
     * 
     * @return Field selected as H alarm setting value container
     */
    @Override
    public String getAlarmHighValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getAlarmHighValueField
    
    
    /**
     * Returns field selected as H alarm setting "Possible" flag container.
     * 
     * @return Field selected as H alarm setting "Possible" flag container
     */
    @Override
    public String getAlarmHighPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }// getAlarmHighPossibleFlagField
    
    
    /**
     * Returns field selected as HH alarm setting key container.
     * 
     * @return Field selected as HH alarm key container
     */
    @Override
    public String getAlarmHighHighKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getAlarmHighHighKeyField
    
    
    /**
     * Returns field selected as HH alarm setting key value container.
     * 
     * @return Field selected as HH alarm setting key value container
     */
    @Override
    public String getAlarmHighHighKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getAlarmHighHighKeyValue
    
    
    /**
     * Returns field selected as HH alarm setting value container.
     * 
     * @return Field selected as HH alarm setting value container
     */
    @Override
    public String getAlarmHighHighValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getAlarmHighHighValueField
    
    
    /**
     * Returns field selected as HH alarm setting "Possible" flag container.
     * 
     * @return Field selected as HH alarm setting "Possible" flag container
     */
    @Override
    public String getAlarmHighHighPossibleFlagField()
    {
        return (String)this.parsingSettingsTable.getValueAt(ALARM_HIGH_HIGH_ROW_INDEX, POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
    }// getAlarmHighHighPossibleFlagField
    
    
    /**
     * Returns field selected as range min setting key container.
     * 
     * @return Field selected as range min setting key container
     */
    @Override
    public String getRangeMinKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getRangeMinKeyField
    
    
    /**
     * Returns field selected as range min setting key value container.
     * 
     * @return Field selected as range min setting key value container
     */
    @Override
    public String getRangeMinKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getRangeMinKeyValue
    
    
    /**
     * Returns field selected as range min setting value container.
     * 
     * @return Field selected as range min setting value container
     */
    @Override
    public String getRangeMinValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MIN_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getRangeMinValueField
        
    
    /**
     * Returns field selected as range max setting key container.
     * 
     * @return Field selected as range max setting key container
     */
    @Override
    public String getRangeMaxKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getRangeMaxKeyField
    
    
    /**
     * Returns field selected as range max setting key value container.
     * 
     * @return Field selected as range max setting key value container
     */
    @Override
    public String getRangeMaxKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// setRangeMaxKeyValue
    
    
    /**
     * Returns field selected as range max setting value container.
     * 
     * @return Field selected as range max setting value container
     */
    @Override
    public String getRangeMaxValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(RANGE_MAX_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getRangeMaxValueField
    
    
    /**
     * Returns field selected as engineering units setting key container.
     * 
     * @return Field selected as engineering units setting key container
     */
    @Override
    public String getUnitsKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getUnitsKeyField
    
    
    /**
     * Returns field selected as engineering units setting key value container.
     * 
     * @return Field selected as engineering units setting key value container
     */
    @Override
    public String getUnitsKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getUnitsKeyValue
    
    
    /**
     * Returns field selected as engineering units setting value container.
     * 
     * @return Field selected as engineering units setting value container
     */
    @Override
    public String getUnitsValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(UNITS_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getUnitsValueField
    
    
    /**
     * Returns field selected as source system setting key container.
     * 
     * @return Field selected as source system units setting key container
     */
    @Override
    public String getSourceSystemKeyField()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, KEY_FIELD_COLUMN_INDEX);
    }// getSourceSystemKeyField
    
    
    /**
     * Returns field selected as source system setting key value container.
     * 
     * @return Field selected as source system setting key value container
     */
    @Override
    public String getSourceSystemKeyValue()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, KEY_VALUE_COLUMN_INDEX);
    }// getSourceSystemKeyValue
    
    
    /**
     * Returns field selected as source system setting value container.
     * 
     * @return Field selected as source system setting value container
     */
    @Override
    public String getSourceSystemValueField()
    {
        return (String)this.parsingSettingsTable.getValueAt(SOURCE_SYSTEM_ROW_INDEX, VALUE_FIELD_COLUMN_INDEX);
    }// getSourceSystemValueField
   
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        parsingSettingsTable = new javax.swing.JTable();
        pathToSpreadsheetTextFieldLabel = new javax.swing.JLabel();
        pathToSpreadsheetTextField = new javax.swing.JTextField();
        selectPathToSpreadsheetTextFieldButton = new javax.swing.JButton();
        sourceSheetNameComboBoxLabel = new javax.swing.JLabel();
        sourceSheetNameComboBox = new javax.swing.JComboBox();
        tagNameFieldNameComboBoxLabel = new javax.swing.JLabel();
        tagNameFieldNameComboBox = new javax.swing.JComboBox();
        runParsingButton = new javax.swing.JButton();
        dataSourceToBeCreatedTypeComboBoxLabel = new javax.swing.JLabel();
        dataSourceToBeCreatedTypeComboBox = new javax.swing.JComboBox();
        plantsComboBoxLabel = new javax.swing.JLabel();
        tagFormatComboBox = new javax.swing.JComboBox();
        tagFormatComboBoxLabel = new javax.swing.JLabel();
        plantsComboBox = new javax.swing.JComboBox();

        setTitle("Create data source from MS Excel sheet");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        parsingSettingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Alarm Low Low", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarrm Low", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarm High", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Alarm High High", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE},
                {"Range Min", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Range Max", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Units", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"},
                {"Source System", NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, NO_FIELD_SELECTED_VALUE, "-"}
            },
            new String [] {
                "Setting", "Key column", "Key", "Value column", "Possible flag column"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex > ALARM_HIGH_HIGH_ROW_INDEX && columnIndex == POSSIBLE_FLAG_FIELD_COLUMN_INDEX) return false;
                return canEdit [columnIndex];
            }
        });
        parsingSettingsTable.setRowSelectionAllowed(false);
        jScrollPane1.setViewportView(parsingSettingsTable);

        pathToSpreadsheetTextFieldLabel.setText("Path to source spreadsheet:");

        pathToSpreadsheetTextField.setEditable(false);

        selectPathToSpreadsheetTextFieldButton.setText("...");
        selectPathToSpreadsheetTextFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPathToSpreadsheetTextFieldButtonActionPerformed(evt);
            }
        });

        sourceSheetNameComboBoxLabel.setText("Source sheet name:");

        sourceSheetNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceSheetNameComboBoxActionPerformed(evt);
            }
        });

        tagNameFieldNameComboBoxLabel.setText("Tag name column:");

        runParsingButton.setText("Run");
        runParsingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runParsingButtonActionPerformed(evt);
            }
        });

        dataSourceToBeCreatedTypeComboBoxLabel.setText("Data source type:");

        plantsComboBoxLabel.setText("plant:");

        tagFormatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tagFormatComboBoxActionPerformed(evt);
            }
        });

        tagFormatComboBoxLabel.setText("Tag format:");

        plantsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantsComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 617, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(runParsingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pathToSpreadsheetTextFieldLabel)
                            .addComponent(sourceSheetNameComboBoxLabel)
                            .addComponent(tagNameFieldNameComboBoxLabel)
                            .addComponent(dataSourceToBeCreatedTypeComboBoxLabel)
                            .addComponent(plantsComboBoxLabel)
                            .addComponent(tagFormatComboBoxLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(pathToSpreadsheetTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectPathToSpreadsheetTextFieldButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sourceSheetNameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tagNameFieldNameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataSourceToBeCreatedTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tagFormatComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(plantsComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plantsComboBoxLabel)
                    .addComponent(plantsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataSourceToBeCreatedTypeComboBoxLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dataSourceToBeCreatedTypeComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagFormatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tagFormatComboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathToSpreadsheetTextFieldLabel)
                    .addComponent(pathToSpreadsheetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectPathToSpreadsheetTextFieldButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceSheetNameComboBoxLabel)
                    .addComponent(sourceSheetNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tagNameFieldNameComboBoxLabel)
                    .addComponent(tagNameFieldNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(runParsingButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    /**
     * Handles select path to MS Excel book button click event and triggers 
     * appropriate event for all subscribers.
     * 
     * @param evt Click event object
     */
    private void selectPathToSpreadsheetTextFieldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPathToSpreadsheetTextFieldButtonActionPerformed
        
        CustomEvent selectPathToSpreadsheetTextFieldButtonClickEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.SELECT_PATH_TO_SPREADSHEET_TEXT_FIELD_BUTTON_CLICK, selectPathToSpreadsheetTextFieldButtonClickEvent);       
    }//GEN-LAST:event_selectPathToSpreadsheetTextFieldButtonActionPerformed

    
    /**
     * Handles sheet name selection event and sets up available headers list for
     * selected sheet in dialog control elements.
     * 
     * @param evt Sheet names combo box selection event object
     */
    private void sourceSheetNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sourceSheetNameComboBoxActionPerformed
        
        String sheetName = (String)sourceSheetNameComboBox.getSelectedItem();
        List<String> fields = (List)((ExcelBookObservable)model).getSheetsAndFields().get(sheetName);
        
        this.tagNameFieldNameComboBox.removeAllItems();
        
        // Add value which means unselected column to combo box:
        JComboBox tableCellsEditorComboBox = new JComboBox();
        tableCellsEditorComboBox.addItem(NO_FIELD_SELECTED_VALUE);
        
        for (String field : fields)
        {    
            this.tagNameFieldNameComboBox.addItem(field);
            tableCellsEditorComboBox.addItem(field);
        }// for
        
        // Set default cell editior instances for parsing settings table columns:
        TableColumn keyFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(KEY_FIELD_COLUMN_INDEX);
        TableColumn valueFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(VALUE_FIELD_COLUMN_INDEX);
        TableColumn possibleFlagFieldColumn = this.parsingSettingsTable.getColumnModel().getColumn(POSSIBLE_FLAG_FIELD_COLUMN_INDEX);
        
        keyFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
        valueFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
        possibleFlagFieldColumn.setCellEditor(new DefaultCellEditor(tableCellsEditorComboBox));
    }//GEN-LAST:event_sourceSheetNameComboBoxActionPerformed

    
    /**
     * Handles run parsing button click event and triggers appropriate event for
     * all subscribers.
     *  
     * @param evt Run parsing button click event object
     */
    private void runParsingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runParsingButtonActionPerformed
        
        CustomEvent runParsingButtonClickEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.RUN_SPREADSHEET_PARSING_BUTTON_CLICK, runParsingButtonClickEvent);
        
        this.setVisible(false);
    }//GEN-LAST:event_runParsingButtonActionPerformed

    
    /**
     * Handles form closing event and triggers appropriate event for all 
     * subscribers.
     *  
     * @param evt Window closing event object
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        
        CustomEvent dialogClosingEvent = new CustomEvent(new Object());
        this.events.trigger(ViewEvent.DIALOG_CLOSING, dialogClosingEvent);
    }//GEN-LAST:event_formWindowClosing

    
    /**
     * Handles new plant selection event and triggers change plant selection 
     * event for all subscribers.
     * 
     * @param evt Plants combo box selection event object
     */
    private void plantsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantsComboBoxActionPerformed
        
        Plant plant = (Plant)plantsComboBox.getSelectedItem();
        CustomEvent selectNewPlantEvent = new CustomEvent(plant);
        this.events.trigger(ViewEvent.CHANGE_PLANT_SELECTION, selectNewPlantEvent);
    }//GEN-LAST:event_plantsComboBoxActionPerformed

    
    /**
     * Handles new tag mask selection event and triggers change tag mask 
     * selection event for all subscribers.
     * 
     * @param evt Tag masks (formats) combo box selection event object
     */
    private void tagFormatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tagFormatComboBoxActionPerformed
        
        TagMask tagMask = (TagMask)this.tagFormatComboBox.getSelectedItem();
        CustomEvent selectNewTagFormatEvent = new CustomEvent(tagMask);
        this.events.trigger(ViewEvent.CHANGE_TAG_FORMAT_SELECTION, selectNewTagFormatEvent);
    }//GEN-LAST:event_tagFormatComboBoxActionPerformed
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dataSourceToBeCreatedTypeComboBox;
    private javax.swing.JLabel dataSourceToBeCreatedTypeComboBoxLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable parsingSettingsTable;
    private javax.swing.JTextField pathToSpreadsheetTextField;
    private javax.swing.JLabel pathToSpreadsheetTextFieldLabel;
    private javax.swing.JComboBox plantsComboBox;
    private javax.swing.JLabel plantsComboBoxLabel;
    private javax.swing.JButton runParsingButton;
    private javax.swing.JButton selectPathToSpreadsheetTextFieldButton;
    private javax.swing.JComboBox sourceSheetNameComboBox;
    private javax.swing.JLabel sourceSheetNameComboBoxLabel;
    private javax.swing.JComboBox tagFormatComboBox;
    private javax.swing.JLabel tagFormatComboBoxLabel;
    private javax.swing.JComboBox tagNameFieldNameComboBox;
    private javax.swing.JLabel tagNameFieldNameComboBoxLabel;
    // End of variables declaration//GEN-END:variables
}// CreateDataSourceFromExcelDialog
