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
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.IntoolsCompliance;


/**
 * Класс описывает панел для отрисовки графических результатов анализа уставок 
 * по выбранному объекту.
 * 
 * @author Denis Udovenko
 * @version  1.0.3
 */
public class IntoolsComplianceSummaryPanel extends javax.swing.JPanel
{
    private final IntoolsCompliance model; 
    
    private static final Color COMPLIANT_AGAINST_DOCUMENTS_AND_SYSTEMS_COLOR = new Color(209, 255, 214);
    private static final Color COMPLIANT_AGAINST_DOCUMENTS_COLOR = Color.YELLOW;
    private static final Color COMPLIANT_AGAINST_SYSTEMS_COLOR = Color.CYAN;
    private static final Color NON_COMPLIANT_COLOR = Color.RED;
    

    /**
     * Конструктор класса. Вызывает инициализацию всех элементов панели.
     */
    public IntoolsComplianceSummaryPanel(IntoolsCompliance model) 
    {
        
        this.model = model;
        this.model.on(IntoolsCompliance.Event.SUMMARY_CALCULATED, new _SummaryCalculatedEventHandler());
        
        initComponents();
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

            totalAlarmsSummary.setValue("Alarms compliant with documents and systems", model.getTotalAlarmsCompliantWithDocumentsAndSystems());
            totalAlarmsSummary.setValue("Alarms compliant with documents", model.getTotalAlarmsCompliantWithDocuments());
            totalAlarmsSummary.setValue("Alarms compliant with systems", model.getTotalAlarmsCompliantWithSystems());
            totalAlarmsSummary.setValue("Non comliant alarms", model.getTotalNonCompliantAlarms());    

            alarmsLowLowSummary.setValue("LL Alarms compliant with documents and systems", model.getLowLowAlarmsCompliantWithDocumentsAndSystems());
            alarmsLowLowSummary.setValue("LL Alarms compliant with documents", model.getLowLowAlarmsCompliantWithDocuments());
            alarmsLowLowSummary.setValue("LL Alarms compliant with systems", model.getLowLowAlarmsCompliantWithSystems());
            alarmsLowLowSummary.setValue("Non comliant LL alarms", model.getLowLowNonCompliantAlarms());

            alarmsLowSummary.setValue("L Alarms compliant with documents and systems", model.getLowAlarmsCompliantWithDocumentsAndSystems());
            alarmsLowSummary.setValue("L Alarms compliant with documents", model.getLowAlarmsCompliantWithDocuments());
            alarmsLowSummary.setValue("L Alarms compliant with systems", model.getLowAlarmsCompliantWithSystems());
            alarmsLowSummary.setValue("Non comliant L alarms", model.getLowNonCompliantAlarms());

            alarmsHighSummary.setValue("H Alarms compliant with documents and systems", model.getHighHighAlarmsCompliantWithDocumentsAndSystems());
            alarmsHighSummary.setValue("H Alarms compliant with documents", model.getHighAlarmsCompliantWithDocuments());
            alarmsHighSummary.setValue("H Alarms compliant with systems", model.getHighAlarmsCompliantWithSystems());
            alarmsHighSummary.setValue("Non comliant H alarms", model.getHighNonCompliantAlarms());

            alarmsHighHighSummary.setValue("HH Alarms compliant with documents and systems", model.getHighHighAlarmsCompliantWithDocumentsAndSystems());
            alarmsHighHighSummary.setValue("HH Alarms compliant with documents", model.getHighHighAlarmsCompliantWithDocuments());
            alarmsHighHighSummary.setValue("HH Alarms compliant with systems", model.getHighHighAlarmsCompliantWithSystems());
            alarmsHighHighSummary.setValue("Non comliant HH alarms", model.getHighHighNonCompliantAlarms());

            JFreeChart totalChart = ChartFactory.createPieChart3D("Total alarms:", totalAlarmsSummary, true, true, false);
            JFreeChart alarmLowLowChart = ChartFactory.createPieChart3D("LL alarms:", alarmsLowLowSummary, true, true, false);
            JFreeChart alarmLowChart = ChartFactory.createPieChart3D("L alarms:", alarmsLowSummary, true, true, false);
            JFreeChart alarmHighChart = ChartFactory.createPieChart3D("H alarms:", alarmsHighSummary, true, true, false);
            JFreeChart alarmHighHighChart = ChartFactory.createPieChart3D("HH alarms:", alarmsHighHighSummary, true, true, false);

            //Задаем массив цветов чарта:
            Color[] colors = {COMPLIANT_AGAINST_DOCUMENTS_AND_SYSTEMS_COLOR, COMPLIANT_AGAINST_DOCUMENTS_COLOR, COMPLIANT_AGAINST_SYSTEMS_COLOR, NON_COMPLIANT_COLOR}; 

            PiePlot plot = (PiePlot)totalChart.getPlot(); 
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null); 
            PieRenderer renderer = new PieRenderer(colors); 
            renderer.setColor(plot, totalAlarmsSummary); 

            plot = (PiePlot)alarmLowLowChart.getPlot(); 
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsLowLowSummary); 

            plot = (PiePlot)alarmLowChart.getPlot();
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsLowSummary); 

            plot = (PiePlot)alarmHighChart.getPlot();
            plot.setForegroundAlpha(0.7f);
            plot.setLabelGenerator(null);
            renderer = new PieRenderer(colors); 
            renderer.setColor(plot, alarmsHighSummary); 

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
    }// _SummaryCalculatedEventHandler
  
    
    /* 
     * Упрощенный класс-отрисовщик для установки набора цветов чарта.
     * 
     * @author   Denis.Udovenko
     * @version  1.0.1
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
