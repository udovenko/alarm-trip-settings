package ru.sakhalinenergy.alarmtripsettings.views;

import java.util.List;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Color;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.OveralCompliance;


/**
 * Класс описывает панел для отрисовки графических результатов анализа уставок 
 * по выбранному объекту.
 * 
 * @author   Udovenko
 * @version  1.0.3
 */
public class OverallComplianceSummaryPanel extends javax.swing.JPanel
{
    private final OveralCompliance model; 
    
    private final static Color COMPLIANT_ALARMS_COLOR = new Color(209, 255, 214);
    private final static Color SEMI_COMPLIANT_ALARMS_COLOR = Color.PINK;
    private final static Color NON_COMPLIANT_ALARMS_COLOR = Color.RED;
    

    /**
     * Конструктор класса. Вызывает инициализацию всех элементов панели.
     */
    public OverallComplianceSummaryPanel(OveralCompliance model) 
    {
        initComponents();
        this.model = model;
        
        //Подписываеся на события модели:
        this.model.on(OveralCompliance.Event.SUMMARY_CALCULATED, new _SummaryCalculatedEventHandler());
    }//SummaryPanel

    
    /**
     * 
     */
    private class _SummaryCalculatedEventHandler implements CustomEventListener
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

            totalAlarmsSummary.setValue("Fully compliant Alarms (" + model.getTotalAlarmsWithFullConformity() + ")", model.getTotalAlarmsWithFullConformity());
            totalAlarmsSummary.setValue("Semi compliant Alarms (" + model.getTotalAlarmsWithSemiConformity() + ")", model.getTotalAlarmsWithSemiConformity());
            totalAlarmsSummary.setValue("Not compliant Alarms (" + model.getTotalAlarmsWithNullConformity() + ")", model.getTotalAlarmsWithNullConformity());

            alarmsLowLowSummary.setValue("Fully compliant Alarms LL (" + model.getAlarmsLowLowWithFullConformity() + ")", model.getAlarmsLowLowWithFullConformity());
            alarmsLowLowSummary.setValue("Semi compliant Alarms LL (" + model.getAlarmsLowLowWithSemiConformity() + ")", model.getAlarmsLowLowWithSemiConformity());
            alarmsLowLowSummary.setValue("Not compliant Alarms LL (" + model.getAlarmsLowLowWithNullConformity() + ")", model.getAlarmsLowLowWithNullConformity());

            alarmsLowSummary.setValue("Fully compliant Alarms L (" + model.getAlarmsLowWithFullConformity() + ")", model.getAlarmsLowWithFullConformity());
            alarmsLowSummary.setValue("Semi compliant Alarms L (" + model.getAlarmsLowWithSemiConformity() + ")", model.getAlarmsLowWithSemiConformity());
            alarmsLowSummary.setValue("Not compliant Alarms L (" + model.getAlarmsLowWithNullConformity() + ")", model.getAlarmsLowWithNullConformity());

            alarmsHighSummary.setValue("Fully compliant Alarms H (" + model.getAlarmsHighWithFullConformity() + ")", model.getAlarmsHighWithFullConformity());
            alarmsHighSummary.setValue("Semi compliant Alarms H (" + model.getAlarmsHighWithSemiConformity() + ")", model.getAlarmsHighWithSemiConformity());
            alarmsHighSummary.setValue("Not compliant Alarms H (" + model.getAlarmsHighWithNullConformity() + ")", model.getAlarmsHighWithNullConformity());

            alarmsHighHighSummary.setValue("Fully compliant Alarms HH (" + model.getAlarmsHighHighWithFullConformity() + ")", model.getAlarmsHighHighWithFullConformity());
            alarmsHighHighSummary.setValue("Semi compliant Alarms HH (" + model.getAlarmsHighHighWithSemiConformity() + ")", model.getAlarmsHighHighWithSemiConformity());
            alarmsHighHighSummary.setValue("Not compliant Alarms HH (" + model.getAlarmsHighHighWithNullConformity() + ")", model.getAlarmsHighHighWithNullConformity());


            JFreeChart totalChart = ChartFactory.createPieChart3D("Total alarms:", totalAlarmsSummary, true, true, false);
            JFreeChart alarmLowLowChart = ChartFactory.createPieChart3D("LL alarms:", alarmsLowLowSummary, true, true, false);
            JFreeChart alarmLowChart = ChartFactory.createPieChart3D("L alarms:", alarmsLowSummary, true, true, false);
            JFreeChart alarmHighChart = ChartFactory.createPieChart3D("H alarms:", alarmsHighSummary, true, true, false);
            JFreeChart alarmHighHighChart = ChartFactory.createPieChart3D("HH alarms:", alarmsHighHighSummary, true, true, false);

            PiePlot plot = (PiePlot)totalChart.getPlot(); 
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null); 

            // Specify the colors here 
            Color[] colors = {COMPLIANT_ALARMS_COLOR, SEMI_COMPLIANT_ALARMS_COLOR, NON_COMPLIANT_ALARMS_COLOR}; 
            PieRenderer renderer = new PieRenderer(colors); 
            renderer.setColor(plot, totalAlarmsSummary); 

            // Specify the colors here 
            plot = (PiePlot)alarmLowLowChart.getPlot(); 
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsLowLowSummary); 

            // Specify the colors here 
            plot = (PiePlot)alarmLowChart.getPlot();
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsLowSummary); 

            // Specify the colors here 
            plot = (PiePlot)alarmHighChart.getPlot();
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsHighSummary); 

            // Specify the colors here 
            plot = (PiePlot)alarmHighHighChart.getPlot(); 
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsHighHighSummary); 

            add(new ChartPanel(totalChart));
            add(new ChartPanel(alarmLowLowChart));
            add(new ChartPanel(alarmLowChart ));
            add(new ChartPanel(alarmHighChart));
            add(new ChartPanel(alarmHighHighChart));

            revalidate();
        }// customEventOccurred
    }//_SummaryCalculatedEventHandler
  
    
    /* 
     * Упрощенный класс-отрисовщик для установки набора цветов чарта.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.0
     */ 
    public static class PieRenderer 
    { 
        private Color[] color; 
        
        public PieRenderer(Color[] color) 
        { 
            this.color = color; 
        }//PieRenderer        
        
        public void setColor(PiePlot plot, DefaultPieDataset dataset) 
        { 
            List <Comparable> keys = dataset.getKeys(); 
            int aInt; 
            
            for (int i = 0; i < keys.size(); i++) 
            { 
                aInt = i % this.color.length; 
                plot.setSectionPaint(keys.get(i), this.color[aInt]); 
            }//for 
        }//setColor 
    }//PieRenderer 
       
    
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
}
