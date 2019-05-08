
import java.awt.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.*;
import org.jfree.data.xy.*;

/**
 * The class TelloChartFrame allows you to display information about the position and rotation 
 * of the drone
 *
 * @author Andrea Rauso
 */
public class TelloChartFrame extends javax.swing.JFrame {

    /**
     * The position on X-axis
     */
    private int positionX;
    
    /**
     * The position on the Y-axis
     */
    private int positionY;
    
    /**
     * The position on the Z-axis
     */
    private int positionZ;
    
    /**
     * The rotation on the X-axis
     */
    private int pitch;
    
    /**
     * The rotation on the Y-axis
     */
    private int yaw;
    
    /**
     * The rotation on the Z-axis
     */
    private int roll;
    
    /**
     * The chart representing the X & Y position of the drone
     */
    private JFreeChart xyPositionChart;
    
    /**
     * The chart representing the X & Z position of the drone
     */
    private JFreeChart xzPositionChart;
    
    /**
     * The chart representing the X, Y & Z axis rotation of the drone
     */
    private JFreeChart rotationChart;

    /**
     * TelloChartFrame constructor
     * 
     * @param positionX the position of the drone on X-axis
     * @param positionY the position of the drone on Y-axis
     * @param positionZ the position of the drone on Z-axis
     * @param pitch the rotation on X-axis
     * @param yaw the rotation on Y-axis
     * @param roll the rotation on Z-axis
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

        //XY Position ScatterPlot creation (profile view)
        XYDataset xyDataset = createDataset("XY");
        this.xyPositionChart = ChartFactory.createScatterPlot(
                "Posizione DJI Tello, vista di profilo",
                "Posizione asse X",
                "Posizione asse Y",
                xyDataset
        );
        setChartRange((XYPlot) xyPositionChart.getPlot());
        ChartPanel XYPanel = new ChartPanel(this.xyPositionChart);
        positionXYPlotPanel.setLayout(new java.awt.BorderLayout());
        positionXYPlotPanel.add(XYPanel, BorderLayout.CENTER);
        positionXYPlotPanel.validate();

        //XY Position ScatterPlot creation (seen from above)
        XYDataset xzDataset = createDataset("XZ");
        this.xzPositionChart = ChartFactory.createScatterPlot(
                "Posizione DJI Tello, vista dall'alto",
                "Posizione Asse X",
                "Posizione Asse Z",
                xzDataset
        );
        setChartRange((XYPlot) xzPositionChart.getPlot());
        ChartPanel XZpanel = new ChartPanel(this.xzPositionChart);
        positionXZPlotPanel.setLayout(new java.awt.BorderLayout());
        positionXZPlotPanel.add(XZpanel, BorderLayout.CENTER);
        positionXZPlotPanel.validate();

        //Creazione del grafico delle rotazioni sugli assi
        CategoryDataset dataset = createCategoryDataset();
        this.rotationChart = ChartFactory.createBarChart(
                "Rotazione DJI Tello",
                "Assi di rotazione",
                "Valore rotazione",
                dataset
        );
        ChartPanel panel = new ChartPanel(this.rotationChart);
        rotationChartPanel.setLayout(new java.awt.BorderLayout());
        rotationChartPanel.add(panel, BorderLayout.CENTER);
        rotationChartPanel.validate();

    }

    /**
     * This method allows to create the dataset for the position chart based on 
     * the value passed
     * 
     * @param database the dataset to be created
     * @return the dataset containing the position of the drone
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
     * This method allows to create the dataset containing the information of 
     * the tilt values on the three rotation axes
     * 
     * @return the dataset containig the tilt values of the three axes 
     */
    public CategoryDataset createCategoryDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(this.pitch, "Asse", "Pitch (Beccheggio)");
        dataset.addValue(this.yaw, "Asse", "Yaw(Imbardata)");
        dataset.addValue(this.roll, "Asse", "Roll (Rollio)");
        return dataset;
    }

    /**
     * This method allows to update the chart containing the position of the 
     * drone.
     * 
     * @param database the chart you want to update
     */
    private void updatePlotChart(String database) {
        if (database.equalsIgnoreCase("XY")) {
            XYDataset dataset = createDataset("XY");
            this.xyPositionChart = ChartFactory.createScatterPlot(
                    "Posizione DJI Tello, vista di profilo",
                    "Poszione asse X",
                    "Poszione asse Y",
                    dataset
            );

            setChartRange((XYPlot) xyPositionChart.getPlot());

            ChartPanel panel = new ChartPanel(this.xyPositionChart);
            positionXYPlotPanel.revalidate();
            positionXYPlotPanel.add(panel);
            positionXYPlotPanel.validate();
        } else {
            XYDataset dataset = createDataset("XZ");
            this.xzPositionChart = ChartFactory.createScatterPlot(
                    "Posizione DJI Tello, vista dall'alto",
                    "Posizione asse X",
                    "Posizione asse Z",
                    dataset
            );
            setChartRange((XYPlot) xzPositionChart.getPlot());
            ChartPanel panel = new ChartPanel(this.xzPositionChart);
            positionXZPlotPanel.revalidate();
            positionXZPlotPanel.add(panel);
            positionXZPlotPanel.validate();
        }
    }

    /**
     * This method allows to update the chart containing the tilt values on the 
     * three axes of the drone.
     */
    private void updateAxesChart() {
        CategoryDataset dataset = createCategoryDataset();
        this.rotationChart = ChartFactory.createBarChart(
                "Rotazione DJI Tello",
                "Assi di rotazione",
                "Valore rotazione",
                dataset
        );
        ChartPanel panel = new ChartPanel(this.rotationChart);
        rotationChartPanel.revalidate();
        rotationChartPanel.add(panel);
        rotationChartPanel.validate();
    }

    /**
     * This method allows to set the range of values displayed on the position 
     * chart.

     * @param plot the chart to be set 
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
     * Return the value of the pitch attribute.
     * 
     * @return the value of the pitch attribute
     */
    public int getPitch() {
        return pitch;
    }

    /**
     * Return the value of the yaw attribute.
     * 
     * @return the value of the yaw attribute
     */
    public int getYaw() {
        return yaw;
    }

    /**
     * Return the value of the roll attribute.
     * 
     * @return the value of the roll attribute
     */
    public int getRoll() {
        return roll;
    }

    /**
     * This method sets the value to the yaw attribute and updates the graph 
     * containing the rotation values of the drone.
     * 
     * @param yaw the rotation value on the y-axis
     */
    public void setYaw(int yaw) {
        this.yaw = yaw;
        updateAxesChart();
    }

    /**
     * This method sets the value to the pitch attribute and updates the graph 
     * containing the rotation values of the drone.
     * 
     * @param pitch the rotation value on the x-axis
     */
    public void setPitch(int pitch) {
        this.pitch = pitch;
        updateAxesChart();
    }

    /**
     * This method sets the value to the roll attribute and updates the graph 
     * containing the rotation values of the drone.
     * 
     * @param roll the rotation value on the z-axis
     */
    public void setRoll(int roll) {
        this.roll = roll;
        updateAxesChart();
    }

    /**
     * Return the value of the positionX attribute.
     * 
     * @return the value of the positionX attribute
     */
    public int getPositionX() {
        return positionX;
    }

    /**
     * This method sets the value to the positionX attribute and updates the graph 
     * containing the position of the drone.
     * 
     * @param positionX the position on X-axis
     */
    public void setPositionX(int positionX) {
        this.positionX = positionX;
        updatePlotChart("XY");
        updatePlotChart("XZ");
    }

    /**
     * Return the value of the positionY attribute.
     * 
     * @return the value of the positionY attribute
     */
    public int getPositionY() {
        return positionY;
    }

   /**
     * This method sets the value to the positionY attribute and updates the graph 
     * containing the position of the drone.
     * 
     * @param positionY the position on Y-axis
     */
    public void setPositionY(int positionY) {
        this.positionY = positionY;
        updatePlotChart("XY");
    }

    /**
     * Return the value of the positionZ attribute.
     * 
     * @return the value of the positionZ attribute
     */
    public int getPositionZ() {
        return positionZ;
    }

    /**
     * This method sets the value to the positionZ attribute and updates the graph 
     * containing the position of the drone.
     * 
     * @param positionZ the position on Z-axis
     */
    public void setPositionZ(int positionZ) {
        this.positionZ = positionZ;
        updatePlotChart("XZ");
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
        informationPanel = new javax.swing.JPanel();

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

        javax.swing.GroupLayout informationPanelLayout = new javax.swing.GroupLayout(informationPanel);
        informationPanel.setLayout(informationPanelLayout);
        informationPanelLayout.setHorizontalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
        );
        informationPanelLayout.setVerticalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );

        getContentPane().add(informationPanel);

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
    private javax.swing.JPanel informationPanel;
    private javax.swing.JPanel positionXYPlotPanel;
    private javax.swing.JPanel positionXZPlotPanel;
    private javax.swing.JPanel rotationChartPanel;
    // End of variables declaration//GEN-END:variables
}
