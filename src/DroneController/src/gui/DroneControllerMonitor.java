package gui;

import communication.CommandManager;
import controller.DroneController;
import java.util.Map;
import settings.SettingsManager;
import javax.swing.JOptionPane;
import settings.FlipCommand;
import settings.SettingsListener;

/**
 *
 * @author Luca Di Bello
 */
public class DroneControllerMonitor extends javax.swing.JFrame implements CommandListener {

    private SettingsManager manager = new SettingsManager();
    private SettingsListener listener;
    private DroneController controller = new DroneController();
    /**
     * Creates new form DroneControllerMonitor
     */
    public DroneControllerMonitor() {
        initComponents();
        updateSettingsValues(manager);
    
        controller.setListener(this);
        this.setListener(controller);
        new Thread(controller).start();
    }

    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelBody = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanePageLog = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanelFastCommands = new javax.swing.JPanel();
        jPanelMoveInSpace = new javax.swing.JPanel();
        jButtonDroneUp = new javax.swing.JButton();
        jButtonDroneDown = new javax.swing.JButton();
        jButtonDroneLeft = new javax.swing.JButton();
        jButtonDroneRight = new javax.swing.JButton();
        jPanelAbortPanel = new javax.swing.JPanel();
        jButtonAbortFlight = new javax.swing.JButton();
        jPanelExtraCommands = new javax.swing.JPanel();
        jButtonDroneFlip = new javax.swing.JButton();
        jComboBoxFlip = new javax.swing.JComboBox<>();
        jPanelSettings = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        sensibilityValueTextBox = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        heightPointsValueTextBox = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        DegreesSensibilityValueTextBox = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        MovementDelayValueTextBox = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        DeltaAverageMultiplierValueTextBox = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelHeader.setBackground(new java.awt.Color(0, 78, 112));

        jLabel1.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Drone Controller");
        jPanelHeader.add(jLabel1);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.PAGE_START);

        jPanelBody.setBackground(new java.awt.Color(29, 182, 209));
        jPanelBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanelBody.setLayout(new java.awt.BorderLayout());

        jPanePageLog.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setAutoscrolls(true);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("Developed by DCS Team.\nDesigned by Luca Di Bello.\nDeveloped by Fadil Smajilbasic\n---------------------------------------\n\n");
        jScrollPane1.setViewportView(jTextArea1);

        jPanePageLog.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Log", jPanePageLog);

        jPanelFastCommands.setLayout(new java.awt.GridLayout(2, 0));

        jPanelMoveInSpace.setBorder(javax.swing.BorderFactory.createTitledBorder("Move in space"));
        jPanelMoveInSpace.setLayout(new java.awt.BorderLayout());

        jButtonDroneUp.setText("UP");
        jPanelMoveInSpace.add(jButtonDroneUp, java.awt.BorderLayout.PAGE_START);

        jButtonDroneDown.setText("DOWN");
        jPanelMoveInSpace.add(jButtonDroneDown, java.awt.BorderLayout.PAGE_END);

        jButtonDroneLeft.setText("LEFT");
        jPanelMoveInSpace.add(jButtonDroneLeft, java.awt.BorderLayout.LINE_START);

        jButtonDroneRight.setText("RIGHT");
        jPanelMoveInSpace.add(jButtonDroneRight, java.awt.BorderLayout.LINE_END);

        jPanelAbortPanel.setLayout(new java.awt.GridBagLayout());

        jButtonAbortFlight.setBackground(new java.awt.Color(255, 0, 0));
        jButtonAbortFlight.setText("ABORT MISSION!");
        jButtonAbortFlight.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        jButtonAbortFlight.setMargin(new java.awt.Insets(10, 10, 10, 10));
        jButtonAbortFlight.setPreferredSize(new java.awt.Dimension(120, 60));
        jButtonAbortFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbortFlightActionPerformed(evt);
            }
        });
        jPanelAbortPanel.add(jButtonAbortFlight, new java.awt.GridBagConstraints());

        jPanelMoveInSpace.add(jPanelAbortPanel, java.awt.BorderLayout.CENTER);

        jPanelFastCommands.add(jPanelMoveInSpace);

        jPanelExtraCommands.setBorder(javax.swing.BorderFactory.createTitledBorder("Extra commands :)"));
        jPanelExtraCommands.setLayout(new java.awt.BorderLayout());

        jButtonDroneFlip.setText("FLIP IT :0");
        jButtonDroneFlip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneFlipActionPerformed(evt);
            }
        });
        jPanelExtraCommands.add(jButtonDroneFlip, java.awt.BorderLayout.CENTER);

        jComboBoxFlip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Forward", "Right", "Down", "Left" }));
        jComboBoxFlip.setSelectedIndex(0);
        jPanelExtraCommands.add(jComboBoxFlip, java.awt.BorderLayout.PAGE_START);

        jPanelFastCommands.add(jPanelExtraCommands);

        jTabbedPane1.addTab("Fast Commands", jPanelFastCommands);

        jPanelSettings.setLayout(new java.awt.GridLayout(6, 2));

        jLabel2.setText("Sensibility");
        jPanelSettings.add(jLabel2);

        sensibilityValueTextBox.setText("VALUE");
        jPanelSettings.add(sensibilityValueTextBox);

        jLabel3.setText("Height Points");
        jPanelSettings.add(jLabel3);

        heightPointsValueTextBox.setText("VALUE");
        jPanelSettings.add(heightPointsValueTextBox);

        jLabel4.setText("Degrees Sensibility");
        jPanelSettings.add(jLabel4);

        DegreesSensibilityValueTextBox.setText("VALUE");
        jPanelSettings.add(DegreesSensibilityValueTextBox);

        jLabel5.setText("Moviment Delay");
        jPanelSettings.add(jLabel5);

        MovementDelayValueTextBox.setText("VALUE");
        jPanelSettings.add(MovementDelayValueTextBox);

        jLabel6.setText("Delta Average Multiplier");
        jPanelSettings.add(jLabel6);

        DeltaAverageMultiplierValueTextBox.setText("VALUE");
        jPanelSettings.add(DeltaAverageMultiplierValueTextBox);

        jButton1.setText("Apply settings");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton1);

        jButton2.setText("Refresh settings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButton2);

        jTabbedPane1.addTab("Settings", jPanelSettings);

        jPanelBody.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        int cake = JOptionPane.showConfirmDialog(null,
                "Do you wanna apply this settings?", "DCS Controller - Settings", JOptionPane.YES_NO_OPTION);

        if (cake == JOptionPane.YES_OPTION) {
            manager.setSetting("degrees_sensibility", this.DegreesSensibilityValueTextBox.getText());
            manager.setSetting("deltaAverageMultiplier", this.DeltaAverageMultiplierValueTextBox.getText());
            manager.setSetting("movementDelay", this.MovementDelayValueTextBox.getText());
            manager.setSetting("sensibility", this.sensibilityValueTextBox.getText());
            manager.setSetting("height_points_number", this.heightPointsValueTextBox.getText());

            System.out.println("[Success] Settings applied successfully");

            notifyChangeSettings();
        } else if (cake == JOptionPane.NO_OPTION) {
            this.updateSettingsValues(manager);
            System.err.println("Apply settings operation aborted.");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.updateSettingsValues(manager);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonAbortFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbortFlightActionPerformed
        //Send emergency command
        this.controller.getCommandManager().sendCommandAsync(communication.Commands.LAND);
        System.out.println("[GUI] Sent landing command to drone");
    }//GEN-LAST:event_jButtonAbortFlightActionPerformed

    private void jButtonDroneFlipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneFlipActionPerformed
        //Send flip commands
        int chosenFlipOption = jComboBoxFlip.getSelectedIndex();
        
        final int FORWARD = 0;
        final int RIGHT = 1;
        final int BACK = 2;
        final int LEFT = 3;
        
        if(chosenFlipOption == FORWARD){
            this.controller.getCommandManager().sendCommandAsync(communication.Commands.flip(FlipCommand.FORWARD));
        }
        else if(chosenFlipOption == RIGHT){
            this.controller.getCommandManager().sendCommandAsync(communication.Commands.flip(FlipCommand.RIGHT));

        }
        else if(chosenFlipOption == BACK){
            this.controller.getCommandManager().sendCommandAsync(communication.Commands.flip(FlipCommand.BACK));

        }
        else if(chosenFlipOption == LEFT){
            this.controller.getCommandManager().sendCommandAsync(communication.Commands.flip(FlipCommand.LEFT));
        }
        
        System.out.println("[GUI] Sent flip command to drone");
    }//GEN-LAST:event_jButtonDroneFlipActionPerformed

    public void notifyChangeSettings() {
        listener.settingsChanged();
    }

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
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DroneControllerMonitor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        DroneControllerMonitor dcm = new DroneControllerMonitor();
        /* Create and display the form */
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dcm.setVisible(true);
            }
        });
    }

    private void updateSettingsValues(SettingsManager manager) {
        Map<String, String> options = manager.getSettings();

        this.DegreesSensibilityValueTextBox.setText(options.get("degreesSensibility"));
        this.DeltaAverageMultiplierValueTextBox.setText(options.get("deltaAverageMultiplier"));
        this.MovementDelayValueTextBox.setText(options.get("movementDelay"));
        this.sensibilityValueTextBox.setText(options.get("sensibility"));
        this.heightPointsValueTextBox.setText(options.get("heightPointsNumber"));
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField DegreesSensibilityValueTextBox;
    private javax.swing.JTextField DeltaAverageMultiplierValueTextBox;
    private javax.swing.JTextField MovementDelayValueTextBox;
    private javax.swing.JTextField heightPointsValueTextBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAbortFlight;
    private javax.swing.JButton jButtonDroneDown;
    private javax.swing.JButton jButtonDroneFlip;
    private javax.swing.JButton jButtonDroneLeft;
    private javax.swing.JButton jButtonDroneRight;
    private javax.swing.JButton jButtonDroneUp;
    private javax.swing.JComboBox<String> jComboBoxFlip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanePageLog;
    private javax.swing.JPanel jPanelAbortPanel;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JPanel jPanelExtraCommands;
    private javax.swing.JPanel jPanelFastCommands;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelMoveInSpace;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField sensibilityValueTextBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void messageRecieved(String message) {
        jTextArea1.append("Recieved: " + message);
    }

    @Override
    public void controllerMessage(String message) {
        jTextArea1.append("Controller message: " + message);
    }

    @Override
    public void commandSent(String command) {
        jTextArea1.append("Sent: " + command);
    }
}
