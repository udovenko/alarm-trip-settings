package ru.sakhalinenergy.alarmtripsettings.views.Progress;

import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;


/**
 * Implements dialog for process visualization with single progress bar and log
 * text field.
 *
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public class OneProgressBarWithLogDialog extends ProgressDialog
{
    private final Enum logEvent;
    
    
    /**
     * Public constructor.
     * 
     * @param model Model instance which progress events will be processed
     * @param progressEvent Model progress event key
     * @param logEvent Model log event key
     */
    public OneProgressBarWithLogDialog(ModelObservable model, Enum progressEvent, Enum logEvent)
    {
        super(model, progressEvent);
        this.logEvent = logEvent;   
        model.on(this.progressEvent, new _ProgressEventHandler());
        model.on(this.logEvent, new _LogEventHandler());
        
        initComponents();
    }//OneProgressBarDialog

    
    /**
     * Internal class - handler for a model progress event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    protected class _ProgressEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent evt)
        {
            HashMap<Enum, Object> progressInfo = (HashMap)evt.getSource();
            
            progressBarCaptionLabel.setText((String)progressInfo.get(ModelObservable.ProgressInfoKey.CYCLE_CAPTION));
            
            int progress = (Integer)progressInfo.get(ModelObservable.ProgressInfoKey.CYCLE_PERCENTAGE);
            progressBar.setValue(progress);
            progressBarPercentageLabel.setText(progress + "%");
        }// customEventOccurred
    }// _ProgressEventHandler
    
    
    /**
     * Internal class - handler for a model progress event.
     * 
     * @author Denis Udovenko
     * @version 1.0.1
     */
    protected class _LogEventHandler implements CustomEventListener
    {
        @Override
        public void customEventOccurred(CustomEvent event)
        {
            Object[] exceptionWrapper = (Object[])event.getSource();
            String comment = (String)exceptionWrapper[0];
            Exception exception = (Exception)exceptionWrapper[1];
            logTextArea.append(comment + ": " + exception + "\n");
        }// customEventOccurred
    }// _LogEventHandler

    
    /**
     * Returns log text.
     * 
     * @return Log text
     */
    public String getLog()
    {
        return logTextArea.getText();
    }//getLog
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBarCaptionLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        progressBarPercentageLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();

        progressBarCaptionLabel.setText("Current action");

        progressBarPercentageLabel.setText("0%");

        logTextArea.setEditable(false);
        logTextArea.setColumns(20);
        logTextArea.setLineWrap(true);
        logTextArea.setRows(5);
        logTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(logTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressBarCaptionLabel)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(progressBarPercentageLabel)
                        .addGap(17, 17, 17))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(progressBarCaptionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressBarPercentageLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarCaptionLabel;
    private javax.swing.JLabel progressBarPercentageLabel;
    // End of variables declaration//GEN-END:variables
}
