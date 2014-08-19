package ru.sakhalinenergy.alarmtripsettings.views.panel.summary;

import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.logic.summary.Compliance;


/**
 * Implements abstract parent for summary panels.
 * 
 * @author Denis Udovenko
 * @version 1.0.2
 */
public abstract class SummaryPanel extends JPanel
{
    // Chart plot opacity:
    protected static final float PLOT_FOREGROUND_ALPHA = 0.7F;
    
    protected final Compliance model; 
    
    
    /**
     * Protected constructor. Sets compliance summary model and subscribes on
     * model's events.
     * 
     * @param model Compliance summary model
     */
    protected SummaryPanel(Compliance model)
    {
        this.model = model;
        model.on(Compliance.Event.SUMMARY_CALCULATED, _getSummaryCalculatedEventHandler());
    }// SummaryPanel
    
    
    /**
     * Factory method for getting class-specific model's summary calculated event
     * handler.
     * 
     * @return Model's summary calculated event handler
     */
    protected abstract CustomEventListener _getSummaryCalculatedEventHandler();
    
    
    /**
     * Factory method for getting class-specific charts plots colors array.
     * 
     * @return Charts plots colors array
     */
    protected abstract Color[] _getColors();
    
    
    /**
     * Configures plot for given chart instance. Sets plot's opacity and colors
     * for appropriate dataset keys.
     * 
     * @param chart Chart instance
     * @param dataset Dataset to be rendered
     */
    protected void _configurePlot(JFreeChart chart, DefaultPieDataset dataset)
    {
        PiePlot plot = (PiePlot)chart.getPlot(); 
        plot.setForegroundAlpha(PLOT_FOREGROUND_ALPHA);
        plot.setLabelGenerator(null); 
          
        // Set plot colors:
        List <Comparable> keys = dataset.getKeys(); 
        Color[] plotColors = _getColors();
        int aInt; 

        for (int i = 0; i < keys.size(); i++) 
        { 
            aInt = i % plotColors.length; 
            plot.setSectionPaint(keys.get(i), plotColors[aInt]); 
        }// for 
    }// _configurePlot
}// SummaryPanel
