
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.*;
import org.jfree.data.xy.*;

/**
 * Classe TelloChartFrame
 *
 * @author Andrea Rauso
 */
public class TelloChartFrame extends javax.swing.JFrame {

    private int positionX;
    private int positionY;
    private int positionZ;
    private int pitch;
    private int yaw;
    private int roll;
    private JFreeChart xyPositionPanel;
    private JFreeChart xzPositionPanel;
    private JFreeChart rotationPanel;

    /**
     * Costruttore parametrizzato
     * @param positionX la posizione sull'asse x del drone
     * @param positionY la posizione sull'asse y del drone
     * @param positionZ la posizione sull'asse z del drone
     * @param pitch la rotazione sull'asse x
     * @param yaw la rotazione sull'asse y
     * @param roll la rotazione sull'asse Z
     */
    public TelloChartFrame(int positionX, int positionY, int positionZ,
            int pitch, int yaw, int roll) {
        initComponents();
        setSize(1000, 800);
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;

        //Creazione del grafico della posizione di profilo
        XYDataset xyDataset = createDataset("XY");
        this.xyPositionPanel = ChartFactory.createScatterPlot(
                "Posizione DJI Tello, vista di profilo",
                "Posizione asse X",
                "Posizione asse Y",
                xyDataset
        );
        setChartRange((XYPlot) xyPositionPanel.getPlot());
        ChartPanel XYPanel = new ChartPanel(this.xyPositionPanel);
        positionXYPlotPanel.setLayout(new java.awt.BorderLayout());
        positionXYPlotPanel.add(XYPanel, BorderLayout.CENTER);
        positionXYPlotPanel.validate();

        //Creazione del grafico della posizione dall'alto
        XYDataset xzDataset = createDataset("XZ");
        this.xzPositionPanel = ChartFactory.createScatterPlot(
                "Posizione DJI Tello, vista dall'alto",
                "Posizione Asse X",
                "Posizione Asse Z",
                xzDataset
        );
        setChartRange((XYPlot) xzPositionPanel.getPlot());
        ChartPanel XZpanel = new ChartPanel(this.xzPositionPanel);
        positionXZPlotPanel.setLayout(new java.awt.BorderLayout());
        positionXZPlotPanel.add(XZpanel, BorderLayout.CENTER);
        positionXZPlotPanel.validate();

        //Creazione del grafico delle rotazioni sugli assi
        CategoryDataset dataset = createCategoryDataset();
        this.rotationPanel = ChartFactory.createBarChart(
                "Rotazione DJI Tello",
                "Assi di rotazione",
                "Valore rotazione",
                dataset
        );
        ChartPanel panel = new ChartPanel(this.rotationPanel);
        rotationChartPanel.setLayout(new java.awt.BorderLayout());
        rotationChartPanel.add(panel, BorderLayout.CENTER);
        rotationChartPanel.validate();

    }

    /**
     * Questo metodo permette di creare il dataset per il grafico
     * della posizione in vase al valore passato
     * 
     * @param database il dataset da creare
     * @return il dataset contenente la posizione del drone
     */
    public XYDataset createDataset(String database) {
        if (database.equalsIgnoreCase("XY")) {
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries position = new XYSeries("Posizione");
            position.add(this.positionX, this.positionY);
            dataset.addSeries(position);
            return dataset;
        } else {
            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries position = new XYSeries("Posizione");
            position.add(this.positionX, this.positionZ);
            dataset.addSeries(position);
            return dataset;
        }
    }

    /**
     * Questo metodo permette di creare il grafico contenente
     * le informazioni dei valori di inclinazione sui tre assi di rotazione
     * 
     * @return il database contenente i valori dei 3 assi
     */
    public CategoryDataset createCategoryDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(this.pitch, "Asse", "Pitch (Beccheggio)");
        dataset.addValue(this.yaw, "Asse", "Yaw(Imbardata)");
        dataset.addValue(this.roll, "Asse", "Roll (Rollio)");
        return dataset;
    }

    /**
     * Questo metodo permette di aggiornare i grafici della posizione del drone
     * @param database il database che si vuole aggiornare
     */
    private void updatePlotPanel(String database) {
        if (database.equalsIgnoreCase("XY")) {
            XYDataset dataset = createDataset("XY");
            this.xyPositionPanel = ChartFactory.createScatterPlot(
                    "Posizione DJI Tello, vista di profilo",
                    "Poszione asse X",
                    "Poszione asse Y",
                    dataset
            );

            setChartRange((XYPlot) xyPositionPanel.getPlot());

            ChartPanel panel = new ChartPanel(this.xyPositionPanel);
            positionXYPlotPanel.revalidate();
            positionXYPlotPanel.add(panel);
            positionXYPlotPanel.validate();
        } else {
            XYDataset dataset = createDataset("XZ");
            this.xzPositionPanel = ChartFactory.createScatterPlot(
                    "Posizione DJI Tello, vista dall'alto",
                    "Posizione asse X",
                    "Posizione asse Z",
                    dataset
            );
            setChartRange((XYPlot) xzPositionPanel.getPlot());
            ChartPanel panel = new ChartPanel(this.xzPositionPanel);
            positionXZPlotPanel.revalidate();
            positionXZPlotPanel.add(panel);
            positionXZPlotPanel.validate();
        }
    }

    /**
     * Questo metodo permette di aggiornare il grafico contenente la rotazione 
     * del drone sugli assi cartesiani
     */
    private void updateAxesChart() {
        CategoryDataset dataset = createCategoryDataset();
        this.rotationPanel = ChartFactory.createBarChart(
                "Rotazione DJI Tello",
                "Assi di rotazione",
                "Valore rotazione",
                dataset
        );
        ChartPanel panel = new ChartPanel(this.rotationPanel);
        rotationChartPanel.revalidate();
        rotationChartPanel.add(panel);
        rotationChartPanel.validate();
    }

    /**
     * Questo metodo permette di impostare il range dei numeri mostrati sui 2 
     * assi cartesiani del grafico
     * @param plot il grafico che bisogna impostare 
     */
    private void setChartRange(XYPlot plot) {
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

        NumberAxis domain = (NumberAxis) plot.getDomainAxis();

        domain.setRange(-500, 500);
        domain.setTickUnit(new NumberTickUnit(50));
        domain.setVerticalTickLabels(true);

        NumberAxis range = (NumberAxis) plot.getRangeAxis();
        range.setRange(-500, 500);
        range.setTickUnit(new NumberTickUnit(100));
    }

    /**
     * Ritorna il valore dell'attributo pitch
     * @return il valore dell'attributo pitch
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Ritorna il valore dell'attributo yaw
     * @return il vslore dell'attributo yaw
     */
    public int getYaw() {
        return yaw;
    }

    /**
     * Ritorna il valore dell'attributo roll
     * @return il valore dell'attributo roll
     */
    public int getRoll() {
        return roll;
    }

    /**
     * Imposta il valore all'attributo yaw e aggiorna il grafico della rotazione
     * del drone 
     * 
     * @param yaw il valore della rotazione sull'asse y
     */
    public void setYaw(int yaw) {
        this.yaw = yaw;
        updateAxesChart();
    }

    /**
     * Imposta il valore dell'attributo pitch e aggiorna il grafico della rotazione
     * del drone 
     * 
     * @param pitch il valore della rotazione sull'asse x
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
        updateAxesChart();
    }

    /**
     * Imposta il valore dell'attributo roll e aggiorna il grafico della rotazione 
     * del drone
     * 
     * @param roll il valore della rotazione sull'asse z
     */
    public void setRoll(int roll) {
        this.roll = roll;
        updateAxesChart();
    }

    /**
     * Ritorna il valore dell'attributo positionX
     * @return il valore dell'attributo positionX
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * Imposta il valore dell'attributo positionX e aggiorna i grafici della 
     * posizione
     * @param positionX il valore della posizione sull'asse x
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
        updatePlotPanel("XY");
        updatePlotPanel("XZ");
    }

    /**
     * Ritorna il valore dell'attributo positionY
     * @return il valore dell'attributo positionY
     */
    public int getPositionY() {
        return positionY;
    }

    /**
     * Imposta il valore dell'attributo positionY e aggiorna i grafici della 
     * posizione
     * @param positionY il valore della posizione sull'asse y
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
        updatePlotPanel("XY");
    }

    /**
     * Ritorna il valore dell'attributo positionZ
     * @return il valore dell'attributo positionZ
     */
    public int getPositionZ() {
        return positionZ;
    }

    /**
     * Imposta il valore dell'attributo positionZ e aggiorna i grafici della 
     * posizione
     * @param positionZ il valore della posizione sull'asse z
     */
    public void setPositionZ(int positionZ) {
        this.positionZ = positionZ;
        updatePlotPanel("XZ");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        positionXYPlotPanel = new javax.swing.JPanel();
        positionXZPlotPanel = new javax.swing.JPanel();
        rotationChartPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(2, 2));

        javax.swing.GroupLayout positionXYPlotPanelLayout = new javax.swing.GroupLayout(positionXYPlotPanel);
        positionXYPlotPanel.setLayout(positionXYPlotPanelLayout);
        positionXYPlotPanelLayout.setHorizontalGroup(
            positionXYPlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        positionXYPlotPanelLayout.setVerticalGroup(
            positionXYPlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(positionXYPlotPanel);

        javax.swing.GroupLayout positionXZPlotPanelLayout = new javax.swing.GroupLayout(positionXZPlotPanel);
        positionXZPlotPanel.setLayout(positionXZPlotPanelLayout);
        positionXZPlotPanelLayout.setHorizontalGroup(
            positionXZPlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        positionXZPlotPanelLayout.setVerticalGroup(
            positionXZPlotPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(positionXZPlotPanel);

        javax.swing.GroupLayout rotationChartPanelLayout = new javax.swing.GroupLayout(rotationChartPanel);
        rotationChartPanel.setLayout(rotationChartPanelLayout);
        rotationChartPanelLayout.setHorizontalGroup(
            rotationChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        rotationChartPanelLayout.setVerticalGroup(
            rotationChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(rotationChartPanel);

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
            java.util.logging.Logger.getLogger(TelloChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TelloChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TelloChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TelloChartFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelloChartFrame(0, 0, 0, 0, 0, 0).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel positionXYPlotPanel;
    private javax.swing.JPanel positionXZPlotPanel;
    private javax.swing.JPanel rotationChartPanel;
    // End of variables declaration//GEN-END:variables
}
