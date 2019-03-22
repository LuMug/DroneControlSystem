import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;


/**
 * La classe PositionXYPlotFrame é un frame che permette di visualizzare la 
 * posizione del drone su un grafico cartesiano sugli assi X e Y (il drone viene 
 * visto di profilo)
 * 
 * @author Andrea Rauso
 */
public class PositionXYPlotFrame extends javax.swing.JFrame {

    /**
     * Posizione sull'asse X
     */
    private int positionX; 
    
    /**
     * Posizione sull'asse Y
     */
    private int positionY;
    
    /**
     * Il grafico JFreeChart dove mostrare i dati
     */
    private JFreeChart chart;

    /**
     * Creates new form PositionXYPlotFrame
     * @param PositionX posizione sull'asse X
     * @param PositionY posizione sull'asse Y
     */
    public PositionXYPlotFrame(int PositionX, int PositionY) {
        initComponents();
        setSize(600, 400);
        setTitle("DCS Posizione drone vista di profilo");
        this.positionX = PositionX;
        this.positionY = PositionY;

        XYDataset dataset = createDataset();

        this.chart = ChartFactory.createScatterPlot(
                "Simulatore dati posizione DJI Tello, vista di profilo", //Chart Title
                "Posizione Asse X", // Category axis
                "Posizione Asse Y", // Value axis
                dataset
        );

        setChartRange();
        
        ChartPanel panel = new ChartPanel(this.chart);       
        XYPositionChart.setLayout(new java.awt.BorderLayout());
        XYPositionChart.add(panel,BorderLayout.CENTER);
        XYPositionChart.validate();
        
    }
    
    /**
     * Il Metodo createDataset permette di creare un dataset di tipo XYDataset
     * contenente un solo punto cartesiano (la posizione del drone) da usare per
     * costruire il grafico
     * 
     * @return Il dataset contenente le informazioni sulla posizione del drone
     */
    private XYDataset createDataset() {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries position = new XYSeries("Posizione");
                
        position.add(this.positionX,this.positionY);
        dataset.addSeries(position);
        
        return dataset;
    }
    
    /**
     * La classe updateAxesChart permette di aggiornare il frame mostrando i 
     * nuovi dati della posizione del drone
     */
    private void updateAxesChart(){
        XYDataset dataset = createDataset();
        this.chart = ChartFactory.createScatterPlot(
                "Simulatore dati posizione DJI Tello, vista di profilo", 
                "Poszione asse X", 
                "Poszione asse Y", 
                dataset
        );
        
        setChartRange();
        
        ChartPanel panel = new ChartPanel(this.chart);
        XYPositionChart.revalidate();
        XYPositionChart.add(panel);
        XYPositionChart.validate();
    }
    
    /**
     * La classe setChartRange imposta il range dei numeri in cui mostrare i dati
     */
    private void  setChartRange(){
        XYPlot xyPlot = (XYPlot) this.chart.getPlot();
        xyPlot.setDomainCrosshairVisible(true);
        xyPlot.setRangeCrosshairVisible(true);
        
        XYItemRenderer renderer = xyPlot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);
        
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        if(this.positionX >= 0){
            domain.setRange(-getPositionX()-10,getPositionX()+10);
            domain.setTickUnit(new NumberTickUnit(1));
            domain.setVerticalTickLabels(true);
        }else{
            domain.setRange(getPositionX()-10,Math.abs(getPositionX())+10);
            domain.setTickUnit(new NumberTickUnit(1));
            domain.setVerticalTickLabels(true);
        }
        
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        if(this.positionY >= 0){
            range.setRange(-getPositionY()-5,getPositionY()+5);
            range.setTickUnit(new NumberTickUnit(1));
        }else{
            range.setRange(getPositionY()-5,Math.abs(getPositionY())+5);
            range.setTickUnit(new NumberTickUnit(1));
        }
        
    }
    
    /**
     * Ritorna la posizione sull'asse X
     * @return posizione sull'asse X
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Ritorna la posizione sull'asse Y
     * @return posizione sull'asse Y
     */
    public int getPositionY() {
        return positionY;
    }
    
    /**
     * Imposta il valore della posizione sull'asse X e aggiorna il grafico
     * @param positionX posizione sull'asse X
     */
    public void setPositionX(int positionX) {
        this.positionX += positionX;
        updateAxesChart();
    }

    /**
     * Imposta il valore della posizione sull'asse Y e aggiorna il grafico
     * @param positionY posizione sull'asse Y
     */
    public void setPositionY(int positionY) {
        this.positionY += positionY;
        updateAxesChart();
    }           

//    /**
//     * Creates new form PositionXYPlotFrame
//     */
//    public PositionXYPlotFrame() {
//        initComponents();
//    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        XYPositionChart = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout XYPositionChartLayout = new javax.swing.GroupLayout(XYPositionChart);
        XYPositionChart.setLayout(XYPositionChartLayout);
        XYPositionChartLayout.setHorizontalGroup(
            XYPositionChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        XYPositionChartLayout.setVerticalGroup(
            XYPositionChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        getContentPane().add(XYPositionChart, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PositionXYPlotFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PositionXYPlotFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PositionXYPlotFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PositionXYPlotFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PositionXYPlotFrame(-3,-3).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel XYPositionChart;
    // End of variables declaration//GEN-END:variables
}
