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
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.OveralCompliance;


/**
 * Implements panel for rendering overall alarm settings compliance charts.
 * 
 * @author Udovenko Denis
 * @version 1.0.3
 */
public class OverallComplianceSummaryPanel extends SummaryPanel
{
    
    private final static Color COMPLIANT_ALARMS_COLOR = new Color(209, 255, 214);
    private final static Color SEMI_COMPLIANT_ALARMS_COLOR = Color.PINK;
    private final static Color NON_COMPLIANT_ALARMS_COLOR = Color.RED;
    
    // Chart plot colors array:
    Color[] colors = {COMPLIANT_ALARMS_COLOR, SEMI_COMPLIANT_ALARMS_COLOR, NON_COMPLIANT_ALARMS_COLOR}; 
    

    /**
     * Public constructor. Sets overall compliance summary model and initializes
     * components.
     * 
     * @param model Overall compliance summary model
     */
    public OverallComplianceSummaryPanel(OveralCompliance model) 
    {
        super(model);        
        initComponents();
    }// OverallComplianceSummaryPanel

    
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
            @Override
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
                OveralCompliance castedModel = (OveralCompliance)model;
                
                totalAlarmsSummary.setValue("Fully compliant Alarms (" + castedModel.getTotalAlarmsWithFullConformity() + ")", castedModel.getTotalAlarmsWithFullConformity());
                totalAlarmsSummary.setValue("Semi compliant Alarms (" + castedModel.getTotalAlarmsWithSemiConformity() + ")", castedModel.getTotalAlarmsWithSemiConformity());
                totalAlarmsSummary.setValue("Not compliant Alarms (" + castedModel.getTotalAlarmsWithNullConformity() + ")", castedModel.getTotalAlarmsWithNullConformity());

                alarmsLowLowSummary.setValue("Fully compliant Alarms LL (" + castedModel.getAlarmsLowLowWithFullConformity() + ")", castedModel.getAlarmsLowLowWithFullConformity());
                alarmsLowLowSummary.setValue("Semi compliant Alarms LL (" + castedModel.getAlarmsLowLowWithSemiConformity() + ")", castedModel.getAlarmsLowLowWithSemiConformity());
                alarmsLowLowSummary.setValue("Not compliant Alarms LL (" + castedModel.getAlarmsLowLowWithNullConformity() + ")", castedModel.getAlarmsLowLowWithNullConformity());

                alarmsLowSummary.setValue("Fully compliant Alarms L (" + castedModel.getAlarmsLowWithFullConformity() + ")", castedModel.getAlarmsLowWithFullConformity());
                alarmsLowSummary.setValue("Semi compliant Alarms L (" + castedModel.getAlarmsLowWithSemiConformity() + ")", castedModel.getAlarmsLowWithSemiConformity());
                alarmsLowSummary.setValue("Not compliant Alarms L (" + castedModel.getAlarmsLowWithNullConformity() + ")", castedModel.getAlarmsLowWithNullConformity());

                alarmsHighSummary.setValue("Fully compliant Alarms H (" + castedModel.getAlarmsHighWithFullConformity() + ")", castedModel.getAlarmsHighWithFullConformity());
                alarmsHighSummary.setValue("Semi compliant Alarms H (" + castedModel.getAlarmsHighWithSemiConformity() + ")", castedModel.getAlarmsHighWithSemiConformity());
                alarmsHighSummary.setValue("Not compliant Alarms H (" + castedModel.getAlarmsHighWithNullConformity() + ")", castedModel.getAlarmsHighWithNullConformity());

                alarmsHighHighSummary.setValue("Fully compliant Alarms HH (" + castedModel.getAlarmsHighHighWithFullConformity() + ")", castedModel.getAlarmsHighHighWithFullConformity());
                alarmsHighHighSummary.setValue("Semi compliant Alarms HH (" + castedModel.getAlarmsHighHighWithSemiConformity() + ")", castedModel.getAlarmsHighHighWithSemiConformity());
                alarmsHighHighSummary.setValue("Not compliant Alarms HH (" + castedModel.getAlarmsHighHighWithNullConformity() + ")", castedModel.getAlarmsHighHighWithNullConformity());

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
    }//_SummaryCalculatedEventHandler
    
    
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
}// OverallComplianceSummaryPanel
