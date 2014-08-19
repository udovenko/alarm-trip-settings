package ru.sakhalinenergy.alarmtripsettings.views.panel.summary;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.IntoolsCompliance;


/**
 * Implements panel for rendering SPI alarm settings compliance charts.
 * 
 * @author Denis Udovenko
 * @version  1.0.3
 */
public class IntoolsComplianceSummaryPanel extends SummaryPanel
{
    
    private static final Color COMPLIANT_AGAINST_DOCUMENTS_AND_SYSTEMS_COLOR = new Color(209, 255, 214);
    private static final Color COMPLIANT_AGAINST_DOCUMENTS_COLOR             = Color.YELLOW;
    private static final Color COMPLIANT_AGAINST_SYSTEMS_COLOR               = Color.CYAN;
    private static final Color NON_COMPLIANT_COLOR                           = Color.RED;
    
    // Chart plot colors array:
    private static final Color[] colors = {COMPLIANT_AGAINST_DOCUMENTS_AND_SYSTEMS_COLOR, 
        COMPLIANT_AGAINST_DOCUMENTS_COLOR, COMPLIANT_AGAINST_SYSTEMS_COLOR,
        NON_COMPLIANT_COLOR};

    
    /**
     * Public constructor. Sets SPI compliance summary model and initializes
     * components.
     * 
     * @param model SPI compliance summary model
     */
    public IntoolsComplianceSummaryPanel(IntoolsCompliance model) 
    {
        super(model);
        initComponents();
    }// IntoolsComplianceSummaryPanel

    
    /**
     * Factory method for getting class-specific model's summary calculated event
     * handler.
     * 
     * @return Model's summary calculated event handler
     */
    @Override
    protected CustomEventListener _getSummaryCalculatedEventHandler()
    {
        return new CustomEventListener()
        {
            public void customEventOccurred(CustomEvent event)
            {
                removeAll(); 

                setLayout(new GridLayout(1, 5));
                setPreferredSize(new Dimension(800, 100));

                DefaultPieDataset totalAlarmsSummary = new DefaultPieDataset();
                DefaultPieDataset alarmsLowLowSummary = new DefaultPieDataset();
                DefaultPieDataset alarmsLowSummary = new DefaultPieDataset();
                DefaultPieDataset alarmsHighSummary = new DefaultPieDataset();
                DefaultPieDataset alarmsHighHighSummary = new DefaultPieDataset();

                // Cast model to concrete class:
                IntoolsCompliance castedModel = (IntoolsCompliance)model;
                
                totalAlarmsSummary.setValue("Alarms compliant with documents and systems", castedModel.getTotalAlarmsCompliantWithDocumentsAndSystems());
                totalAlarmsSummary.setValue("Alarms compliant with documents", castedModel.getTotalAlarmsCompliantWithDocuments());
                totalAlarmsSummary.setValue("Alarms compliant with systems", castedModel.getTotalAlarmsCompliantWithSystems());
                totalAlarmsSummary.setValue("Non comliant alarms", castedModel.getTotalNonCompliantAlarms());    

                alarmsLowLowSummary.setValue("LL Alarms compliant with documents and systems", castedModel.getLowLowAlarmsCompliantWithDocumentsAndSystems());
                alarmsLowLowSummary.setValue("LL Alarms compliant with documents", castedModel.getLowLowAlarmsCompliantWithDocuments());
                alarmsLowLowSummary.setValue("LL Alarms compliant with systems", castedModel.getLowLowAlarmsCompliantWithSystems());
                alarmsLowLowSummary.setValue("Non comliant LL alarms", castedModel.getLowLowNonCompliantAlarms());

                alarmsLowSummary.setValue("L Alarms compliant with documents and systems", castedModel.getLowAlarmsCompliantWithDocumentsAndSystems());
                alarmsLowSummary.setValue("L Alarms compliant with documents", castedModel.getLowAlarmsCompliantWithDocuments());
                alarmsLowSummary.setValue("L Alarms compliant with systems", castedModel.getLowAlarmsCompliantWithSystems());
                alarmsLowSummary.setValue("Non comliant L alarms", castedModel.getLowNonCompliantAlarms());

                alarmsHighSummary.setValue("H Alarms compliant with documents and systems", castedModel.getHighHighAlarmsCompliantWithDocumentsAndSystems());
                alarmsHighSummary.setValue("H Alarms compliant with documents", castedModel.getHighAlarmsCompliantWithDocuments());
                alarmsHighSummary.setValue("H Alarms compliant with systems", castedModel.getHighAlarmsCompliantWithSystems());
                alarmsHighSummary.setValue("Non comliant H alarms", castedModel.getHighNonCompliantAlarms());

                alarmsHighHighSummary.setValue("HH Alarms compliant with documents and systems", castedModel.getHighHighAlarmsCompliantWithDocumentsAndSystems());
                alarmsHighHighSummary.setValue("HH Alarms compliant with documents", castedModel.getHighHighAlarmsCompliantWithDocuments());
                alarmsHighHighSummary.setValue("HH Alarms compliant with systems", castedModel.getHighHighAlarmsCompliantWithSystems());
                alarmsHighHighSummary.setValue("Non comliant HH alarms", castedModel.getHighHighNonCompliantAlarms());

                JFreeChart totalChart = ChartFactory.createPieChart3D("Total alarms:", totalAlarmsSummary, true, true, false);
                JFreeChart alarmLowLowChart = ChartFactory.createPieChart3D("LL alarms:", alarmsLowLowSummary, true, true, false);
                JFreeChart alarmLowChart = ChartFactory.createPieChart3D("L alarms:", alarmsLowSummary, true, true, false);
                JFreeChart alarmHighChart = ChartFactory.createPieChart3D("H alarms:", alarmsHighSummary, true, true, false);
                JFreeChart alarmHighHighChart = ChartFactory.createPieChart3D("HH alarms:", alarmsHighHighSummary, true, true, false);

                // Configure chart plots:
                _configurePlot(totalChart, totalAlarmsSummary);
                _configurePlot(alarmLowLowChart, alarmsLowLowSummary);
                _configurePlot(alarmLowChart, alarmsLowSummary);
                _configurePlot(alarmHighChart, alarmsHighSummary);
                _configurePlot(alarmHighHighChart, alarmsHighHighSummary);

                // Add charts to current panel:
                add(new ChartPanel(totalChart));
                add(new ChartPanel(alarmLowLowChart));
                add(new ChartPanel(alarmLowChart ));
                add(new ChartPanel(alarmHighChart));
                add(new ChartPanel(alarmHighHighChart));

                revalidate();
            }// customEventOccurred
        };// CustomEventListener
    }// _SummaryCalculatedEventHandler
  
    
    /**
     * Factory method for getting class-specific charts plots colors array.
     * 
     * @return Charts plots colors array
     */
    @Override
    protected Color[] _getColors()
    {
        return colors;
    }// _getColors
        
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}// IntoolsComplianceSummaryPanel
