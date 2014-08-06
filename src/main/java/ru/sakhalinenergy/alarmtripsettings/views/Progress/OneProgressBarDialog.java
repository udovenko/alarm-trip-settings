package ru.sakhalinenergy.alarmtripsettings.views.Progress;

import java.util.HashMap;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEvent;
import ru.sakhalinenergy.alarmtripsettings.events.CustomEventListener;
import ru.sakhalinenergy.alarmtripsettings.models.ModelObservable;


/**
 * Implements dialog for process visualization with single progress bar.
 *
 * @author Denis.Udovenko
 * @version 1.0.3
 */
public class OneProgressBarDialog extends ProgressDialog
{
   
    /**
     * Public constructor.
     * 
     * @param model Model instance which progress events will be processed
     * @param progressEvent Model progress event key
     */
    public OneProgressBarDialog(ModelObservable model, Enum progressEvent)
    {
        super(model, progressEvent);
        model.on(this.progressEvent, new _ProgressEventHandler());
               
        initComponents();
    }// OneProgressBarDialog
    
    
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

        progressBarCaptionLabel.setText("Current action");

        progressBarPercentageLabel.setText("0%");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel progressBarCaptionLabel;
    private javax.swing.JLabel progressBarPercentageLabel;
    // End of variables declaration//GEN-END:variables
}
