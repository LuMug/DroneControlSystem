package gui;

import communication.CommandManager;
import communication.CommandManagerListener;
import communication.Commands;
import controller.DroneController;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import settings.SettingsManager;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import recorder.FlightBuffer;
import recorder.FlightRecord;
import settings.FlipCommand;
import settings.SettingsListener;

/**
 * This class describes the drone controller monitor: a GUI where you can record
 * your flights, control the drone, and many other things.
 *
 * @author Luca Di Bello
 */
public class DroneControllerMonitor extends javax.swing.JFrame implements CommandListener, CommandManagerListener, MouseListener {

    private SettingsManager manager = new SettingsManager();
    private SettingsListener listener;
    private DroneController controller;
    private final Path recordFolderPath = Paths.get("records");
    private List<FlightRecord> flightRecords = new ArrayList<>();
    private CommandManager commandManager;

    private final String BASE_STATUS = "Current status:";
    private final String RECORDING_DISABLED = BASE_STATUS + " DISABLED";
    private final String RECORDING_ENABLED = BASE_STATUS + " ENABLED";
    private Thread recordingExecutionThread;
    private final TimeoutThread timeoutThread = new TimeoutThread(2, TimeUnit.SECONDS);

    private final int FORWARD = 0;
    private final int RIGHT = 1;
    private final int BACK = 2;
    private final int LEFT = 3;

    /**
     * Constructor method, it creates new form DroneControllerMonitor.
     */
    public DroneControllerMonitor() {
        //Prepare GUI components
        initComponents();

        try {

            controller = new DroneController(this);
            this.setListener(controller);
        } catch (SocketException se) {
            System.out.println(se.getMessage());
            System.exit(0);
        }

        //Autoscroll textarea for logging
        DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        //Insert settings values in textboxes
        updateSettingsValues(manager);

        //Get the commandManager from the controller
        commandManager = controller.getCommandManager();

        //Finally start the controller in a new thread
        new Thread(controller).start();

        //Insert in the selector all the recorded flights
        insertRecordsInSelector();

    }

    /**
     * This method start the GUI in a new thread.
     *
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        final DroneControllerMonitor dcm = new DroneControllerMonitor();

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                dcm.setVisible(true);
            }
        });
    }

    // <editor-fold desc="Methods used by the GUI" defaultstate="collapsed">
    /**
     * This method reads all flight recording files located in the folder
     * 'records' and add them in the combobox present in the recording section.
     */
    public final void insertRecordsInSelector() {
        // read all files in 'records'
        this.jComboBoxSelectRecord.removeAllItems();

        File folder = new File(recordFolderPath.toString());
        File[] files = folder.listFiles();

        if (files != null && files.length != 0) {
            for (File file : files) {
                System.out.println("Found recording: " + file.getAbsolutePath());

                //Create FlightRecord object
                FlightRecord record = new FlightRecord(Paths.get(file.getAbsolutePath()));
                flightRecords.add(record);

                this.jComboBoxSelectRecord.addItem(file.getName());
            }
        } else {
            jComboBoxSelectRecord.setEnabled(false);
            jLabelRecordSelectorMessage.setText("Cannot find any record file");
        }
    }

    /**
     * This method is used for getting the date of a recording using its
     * filename.
     *
     * @param filename The recording filename
     * @return A formatted datetime (yyyy-mm-dd hh:mm:ss)
     */
    public String getDateFromRecordFilename(String filename) {
        //Date [11 - 19]
        String date = filename.substring(11, 19);

        //Time [20 - 16]
        String time = filename.substring(20, 26);

        String formatted_date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        String formatted_time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);

        return formatted_date + " " + formatted_time;
    }

    /**
     * This method is used for updating the config file.
     *
     * @param manager The setting manager.
     */
    private void updateSettingsValues(SettingsManager manager) {
        Map<String, String> options = manager.getSettings();

        this.degreesSensibilityValue.setText(options.get("degreesSensibility"));
        this.heightThreasholdValueTextBox.setText(options.get("heightThreshold"));
    }
    // </editor-fold>

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelHeader = new javax.swing.JPanel();
        titleJLabel = new javax.swing.JLabel();
        jPanelBody = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPaneLogPage = new javax.swing.JPanel();
        logScrollPane = new javax.swing.JScrollPane();
        logTextArea = new javax.swing.JTextArea();
        jPanelFastCommands = new javax.swing.JPanel();
        jPanelMoveInSpace = new javax.swing.JPanel();
        jButtonDroneForward = new javax.swing.JButton();
        jButtonDroneBack = new javax.swing.JButton();
        jButtonDroneLeft = new javax.swing.JButton();
        jButtonDroneRight = new javax.swing.JButton();
        jPanelAbortPanel = new javax.swing.JPanel();
        jButtonAbortFlight = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        movementSteopJLabel = new javax.swing.JLabel();
        jSpinnerDroneMovementStep = new javax.swing.JSpinner();
        jPanelExtraCommands = new javax.swing.JPanel();
        jButtonDroneTakeoff = new javax.swing.JButton();
        jButtonDroneLand = new javax.swing.JButton();
        flipJPanel = new javax.swing.JPanel();
        jButtonDroneFlip = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jComboBoxFlip = new javax.swing.JComboBox<>();
        jPanelRecording = new javax.swing.JPanel();
        jPanelRecordFiles = new javax.swing.JPanel();
        jPanelRecordSelector = new javax.swing.JPanel();
        jComboBoxSelectRecord = new javax.swing.JComboBox<>();
        jLabelRecordSelectorMessage = new javax.swing.JLabel();
        jPanelInfos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldRecordedOn = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldTotalCommands = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextFieldEstimatedExecutionTime = new javax.swing.JTextField();
        jPanelRecordedFlightButtons = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jButtonStartSelectedFlight = new javax.swing.JButton();
        jButtonStopSelectedFlight = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabelRecordingStatusMessage = new javax.swing.JLabel();
        jPanelRecordButtons = new javax.swing.JPanel();
        jButtonStartRecording = new javax.swing.JButton();
        jButtonStopRecording = new javax.swing.JButton();
        jPanelSettings = new javax.swing.JPanel();
        sensibilityJPanel = new javax.swing.JPanel();
        heightThresholdLabel = new javax.swing.JLabel();
        heightThreasholdValueTextBox = new javax.swing.JTextField();
        degreesJPanel = new javax.swing.JPanel();
        degreesLabel = new javax.swing.JLabel();
        degreesSensibilityValue = new javax.swing.JTextField();
        jButtonApplySettings = new javax.swing.JButton();
        jButtonRefreshSettings = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(400, 725));
        setName("JFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(404, 800));

        jPanelHeader.setBackground(new java.awt.Color(0, 78, 112));

        titleJLabel.setFont(new java.awt.Font("Comic Sans MS", 1, 18)); // NOI18N
        titleJLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleJLabel.setText("Drone Controller");
        jPanelHeader.add(titleJLabel);

        getContentPane().add(jPanelHeader, java.awt.BorderLayout.PAGE_START);

        jPanelBody.setBackground(new java.awt.Color(29, 182, 209));
        jPanelBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanelBody.setLayout(new java.awt.BorderLayout());

        jPaneLogPage.setLayout(new java.awt.BorderLayout());

        logScrollPane.setAutoscrolls(true);

        logTextArea.setColumns(20);
        logTextArea.setRows(5);
        logTextArea.setText("[Developed with <3 by DCS Team]\n\n\n\n");
        logScrollPane.setViewportView(logTextArea);

        jPaneLogPage.add(logScrollPane, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Log", jPaneLogPage);

        jPanelFastCommands.setLayout(new java.awt.GridLayout(2, 0));

        jPanelMoveInSpace.setBorder(javax.swing.BorderFactory.createTitledBorder("Move in space"));
        jPanelMoveInSpace.setLayout(new java.awt.BorderLayout());

        jButtonDroneForward.setText("FORWARD");
        jButtonDroneForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneForwardActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneForward, java.awt.BorderLayout.PAGE_START);

        jButtonDroneBack.setText("BACK");
        jButtonDroneBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneBackActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneBack, java.awt.BorderLayout.PAGE_END);

        jButtonDroneLeft.setText("LEFT");
        jButtonDroneLeft.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneLeftActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneLeft, java.awt.BorderLayout.LINE_START);

        jButtonDroneRight.setText("RIGHT");
        jButtonDroneRight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneRightActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneRight, java.awt.BorderLayout.LINE_END);

        jPanelAbortPanel.setLayout(new java.awt.GridLayout(4, 1));

        jButtonAbortFlight.setBackground(new java.awt.Color(255, 100, 100));
        jButtonAbortFlight.setText("ABORT MISSION!");
        jButtonAbortFlight.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAbortFlight.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jButtonAbortFlight.setMargin(new java.awt.Insets(30, 30, 30, 30));
        jButtonAbortFlight.setPreferredSize(new java.awt.Dimension(120, 60));
        jButtonAbortFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAbortFlightActionPerformed(evt);
            }
        });
        jPanelAbortPanel.add(jButtonAbortFlight);

        upButton.setText("UP");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        jPanelAbortPanel.add(upButton);

        downButton.setText("DOWN");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        jPanelAbortPanel.add(downButton);

        movementSteopJLabel.setText("Movement Step");
        jPanel2.add(movementSteopJLabel);

        jSpinnerDroneMovementStep.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        jSpinnerDroneMovementStep.setPreferredSize(new java.awt.Dimension(100, 34));
        jSpinnerDroneMovementStep.setValue(20);
        jPanel2.add(jSpinnerDroneMovementStep);

        jPanelAbortPanel.add(jPanel2);

        jPanelMoveInSpace.add(jPanelAbortPanel, java.awt.BorderLayout.CENTER);

        jPanelFastCommands.add(jPanelMoveInSpace);

        jPanelExtraCommands.setBorder(javax.swing.BorderFactory.createTitledBorder("Extra commands :)"));
        jPanelExtraCommands.setLayout(new java.awt.GridLayout(2, 2));

        jButtonDroneTakeoff.setText("Start flight!");
        jButtonDroneTakeoff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneTakeoffActionPerformed(evt);
            }
        });
        jPanelExtraCommands.add(jButtonDroneTakeoff);

        jButtonDroneLand.setText("Land drone");
        jButtonDroneLand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneLandActionPerformed(evt);
            }
        });
        jPanelExtraCommands.add(jButtonDroneLand);

        flipJPanel.setLayout(new java.awt.GridLayout(2, 2));

        jButtonDroneFlip.setText("FLIP IT");
        jButtonDroneFlip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneFlipActionPerformed(evt);
            }
        });
        flipJPanel.add(jButtonDroneFlip);

        jPanelExtraCommands.add(flipJPanel);

        jPanel4.setLayout(new java.awt.GridLayout(2, 0));

        jComboBoxFlip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Forward", "Right", "Back", "Left" }));
        jComboBoxFlip.setSelectedIndex(0);
        jPanel4.add(jComboBoxFlip);

        jPanelExtraCommands.add(jPanel4);

        jPanelFastCommands.add(jPanelExtraCommands);

        jTabbedPane1.addTab("Fast Commands", jPanelFastCommands);

        jPanelRecording.setLayout(new java.awt.BorderLayout());

        jPanelRecordFiles.setLayout(new java.awt.GridLayout(3, 1));

        jPanelRecordSelector.setLayout(new java.awt.GridBagLayout());

        jComboBoxSelectRecord.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxSelectRecord.setPreferredSize(new java.awt.Dimension(180, 20));
        jComboBoxSelectRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxSelectRecordActionPerformed(evt);
            }
        });
        jPanelRecordSelector.add(jComboBoxSelectRecord, new java.awt.GridBagConstraints());

        jLabelRecordSelectorMessage.setForeground(new java.awt.Color(255, 0, 0));
        jPanelRecordSelector.add(jLabelRecordSelectorMessage, new java.awt.GridBagConstraints());

        jPanelRecordFiles.add(jPanelRecordSelector);

        jPanelInfos.setBorder(javax.swing.BorderFactory.createTitledBorder("Recording info"));
        jPanelInfos.setLayout(new java.awt.GridLayout(3, 2));

        jLabel1.setText("Recorded on");
        jPanelInfos.add(jLabel1);

        jTextFieldRecordedOn.setEditable(false);
        jPanelInfos.add(jTextFieldRecordedOn);

        jLabel7.setText("Total commands");
        jPanelInfos.add(jLabel7);

        jTextFieldTotalCommands.setEditable(false);
        jPanelInfos.add(jTextFieldTotalCommands);

        jLabel9.setText("Estimated execution time");
        jPanelInfos.add(jLabel9);

        jTextFieldEstimatedExecutionTime.setEditable(false);
        jPanelInfos.add(jTextFieldEstimatedExecutionTime);

        jPanelRecordFiles.add(jPanelInfos);

        jPanelRecordedFlightButtons.setBorder(javax.swing.BorderFactory.createTitledBorder("Controls"));
        jPanelRecordedFlightButtons.setLayout(new java.awt.GridLayout(2, 1));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonStartSelectedFlight.setText("Start flight");
        jButtonStartSelectedFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartSelectedFlightActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonStartSelectedFlight, new java.awt.GridBagConstraints());

        jButtonStopSelectedFlight.setText("Stop flight");
        jButtonStopSelectedFlight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopSelectedFlightActionPerformed(evt);
            }
        });
        jPanel1.add(jButtonStopSelectedFlight, new java.awt.GridBagConstraints());

        jPanelRecordedFlightButtons.add(jPanel1);

        jLabelRecordingStatusMessage.setText("Current Status: LeapMotion-Controlled");
        jPanel3.add(jLabelRecordingStatusMessage);

        jPanelRecordedFlightButtons.add(jPanel3);

        jPanelRecordFiles.add(jPanelRecordedFlightButtons);

        jPanelRecording.add(jPanelRecordFiles, java.awt.BorderLayout.PAGE_START);

        jPanelRecordButtons.setLayout(new java.awt.GridBagLayout());

        jButtonStartRecording.setBackground(new java.awt.Color(0, 0, 0));
        jButtonStartRecording.setFont(new java.awt.Font("Comic Sans MS", 3, 14)); // NOI18N
        jButtonStartRecording.setForeground(new java.awt.Color(51, 255, 51));
        jButtonStartRecording.setText("Start recording");
        jButtonStartRecording.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStartRecordingActionPerformed(evt);
            }
        });
        jPanelRecordButtons.add(jButtonStartRecording, new java.awt.GridBagConstraints());

        jButtonStopRecording.setBackground(new java.awt.Color(0, 0, 0));
        jButtonStopRecording.setFont(new java.awt.Font("Comic Sans MS", 3, 14)); // NOI18N
        jButtonStopRecording.setForeground(new java.awt.Color(204, 0, 0));
        jButtonStopRecording.setText("Stop recording");
        jButtonStopRecording.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonStopRecordingActionPerformed(evt);
            }
        });
        jPanelRecordButtons.add(jButtonStopRecording, new java.awt.GridBagConstraints());

        jPanelRecording.add(jPanelRecordButtons, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Recording", jPanelRecording);

        jPanelSettings.setLayout(new java.awt.GridLayout(8, 1));

        sensibilityJPanel.setLayout(new java.awt.GridLayout(1, 0));

        heightThresholdLabel.setText("HeightThreshold");
        sensibilityJPanel.add(heightThresholdLabel);

        heightThreasholdValueTextBox.setText("VALUE");
        sensibilityJPanel.add(heightThreasholdValueTextBox);

        jPanelSettings.add(sensibilityJPanel);

        degreesJPanel.setLayout(new java.awt.GridLayout(1, 0));

        degreesLabel.setText("Degrees Sensibility");
        degreesJPanel.add(degreesLabel);

        degreesSensibilityValue.setText("VALUE");
        degreesJPanel.add(degreesSensibilityValue);

        jPanelSettings.add(degreesJPanel);

        jButtonApplySettings.setText("Apply settings");
        jButtonApplySettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonApplySettingsActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButtonApplySettings);

        jButtonRefreshSettings.setText("Refresh settings");
        jButtonRefreshSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRefreshSettingsActionPerformed(evt);
            }
        });
        jPanelSettings.add(jButtonRefreshSettings);

        jTabbedPane1.addTab("Settings", jPanelSettings);

        jPanelBody.add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanelBody, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // <editor-fold desc="Generated code (GUI events)" defaultstate="collapsed">
    private void jButtonApplySettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonApplySettingsActionPerformed
        int userAnswer = JOptionPane.showConfirmDialog(null,
                "Do you wanna apply this settings?", "DCS Controller - Settings", JOptionPane.YES_NO_OPTION);

        if (userAnswer == JOptionPane.YES_OPTION) {
            manager.setSetting("degreesSensibility", this.degreesSensibilityValue.getText());
            manager.setSetting("heightThreshold", this.heightThreasholdValueTextBox.getText());

            System.out.println("[Success] Settings applied successfully");

            notifyChangeSettings();
        } else if (userAnswer == JOptionPane.NO_OPTION) {
            this.updateSettingsValues(manager);
            System.err.println("Apply settings operation aborted.");
        }
    }//GEN-LAST:event_jButtonApplySettingsActionPerformed

    private void jButtonRefreshSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRefreshSettingsActionPerformed
        this.updateSettingsValues(manager);
    }//GEN-LAST:event_jButtonRefreshSettingsActionPerformed

    private void jButtonAbortFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAbortFlightActionPerformed
        //Send emergency command
        commandManager.sendCommand(Commands.EMERGENCY);
        System.out.println("[GUI] Sent landing command to drone");
    }//GEN-LAST:event_jButtonAbortFlightActionPerformed

    private void jButtonDroneFlipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneFlipActionPerformed
        //Send flip commands
        int chosenFlipOption = jComboBoxFlip.getSelectedIndex();
        String command = "";

        switch (chosenFlipOption) {
            case FORWARD:
                command = Commands.flip(FlipCommand.FORWARD);
                break;
            case RIGHT:
                command = Commands.flip(FlipCommand.RIGHT);
                break;
            case BACK:
                command = Commands.flip(FlipCommand.BACK);
                break;
            case LEFT:
                command = Commands.flip(FlipCommand.LEFT);
                break;
            default:
                break;
        }

        timeoutThread.execute(new SendTask(commandManager, command));
    }//GEN-LAST:event_jButtonDroneFlipActionPerformed

    private void jButtonDroneForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneForwardActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.forward(stepValue)));
    }//GEN-LAST:event_jButtonDroneForwardActionPerformed

    private void jButtonDroneRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneRightActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.right(stepValue)));
    }//GEN-LAST:event_jButtonDroneRightActionPerformed

    private void jButtonDroneBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneBackActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.back(stepValue)));
    }//GEN-LAST:event_jButtonDroneBackActionPerformed

    private void jButtonDroneLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneLeftActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.left(stepValue)));
    }//GEN-LAST:event_jButtonDroneLeftActionPerformed

    private void jButtonDroneTakeoffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneTakeoffActionPerformed
        timeoutThread.execute(new SendTask(commandManager, Commands.TAKEOFF));
    }//GEN-LAST:event_jButtonDroneTakeoffActionPerformed

    private void jButtonDroneLandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneLandActionPerformed
        timeoutThread.execute(new SendTask(commandManager, Commands.LAND));
    }//GEN-LAST:event_jButtonDroneLandActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.up(stepValue)));
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        timeoutThread.execute(new SendTask(commandManager, Commands.down(stepValue)));
    }//GEN-LAST:event_downButtonActionPerformed

    private void jButtonStartRecordingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartRecordingActionPerformed
        //Start recording
        commandManager.startRecording();
    }//GEN-LAST:event_jButtonStartRecordingActionPerformed

    private void jButtonStopRecordingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopRecordingActionPerformed
        //Stop recording and save file
        commandManager.stopRecording();

        // Check if added another file
        insertRecordsInSelector();
    }//GEN-LAST:event_jButtonStopRecordingActionPerformed

    private void jComboBoxSelectRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxSelectRecordActionPerformed
        //Read filename and parse
        String filename = (String) jComboBoxSelectRecord.getSelectedItem();

        if (filename != null && filename.length() == 32) {
            jTextFieldRecordedOn.setText(getDateFromRecordFilename(filename));

            FlightRecord record = flightRecords.get(jComboBoxSelectRecord.getSelectedIndex());

            try {
                FlightBuffer commands = record.getFlightCommands();
                jTextFieldTotalCommands.setText("" + commands.length());
                jTextFieldEstimatedExecutionTime.setText("WIP");
            } catch (IOException ex) {
                jTextFieldTotalCommands.setText("Unknown");
                jTextFieldEstimatedExecutionTime.setText("Unknown");

                System.err.println("Error: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jComboBoxSelectRecordActionPerformed

    private void jButtonStartSelectedFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartSelectedFlightActionPerformed
        //Cut out the leapmotion controller
        System.err.println("[Info] Trying to disable the leapmotion...");
        controller.DisableLeapMotionController();

        System.out.println("[Recorder] Setting recording status");
        jLabelRecordingStatusMessage.setText(this.RECORDING_ENABLED);
        jLabelRecordSelectorMessage.repaint();

        try {
            //Get the commands of the selected flight
            FlightBuffer commands = flightRecords.get(jComboBoxSelectRecord.getSelectedIndex()).getFlightCommands();

            //Start the command execution in new thread -> GUI doesn't blocks
            recordingExecutionThread = new Thread("Recording thread") {
                @Override
                public void run() {
                    String command;
                    while (commands.length() > 0 && !this.isInterrupted()) {
                        command = commands.getNextCommand();
                        System.out.println("[Thread] Executing command: " + command);
                        commandManager.sendCommand(command);
                    }
                    controller.EnableLeapMotionController();
                }
            };

            //Start the recording execution on the drone
            recordingExecutionThread.start();
        } catch (IOException ex) {
            System.err.println("[Info] Error while getting the recording commands. Stop pre-configured flight and re-enable the leapmotion controller.");
        }

        jLabelRecordingStatusMessage.setText(RECORDING_DISABLED);
    }//GEN-LAST:event_jButtonStartSelectedFlightActionPerformed


    private void jButtonStopSelectedFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopSelectedFlightActionPerformed
        //Cut out the leapmotion controller
        if (recordingExecutionThread != null) {
            if (!controller.isLeapMotionEnabled() || recordingExecutionThread.isAlive()) {
                //Interrupt the code execution in recordingExecutionThread
                System.err.println("[Info] Interrupting recording execution...");
                recordingExecutionThread.interrupt();

                System.err.println("[Info] Trying to re-enable the leapmotion...");
                controller.EnableLeapMotionController();

                jLabelRecordingStatusMessage.setText(this.RECORDING_DISABLED);
                jLabelRecordingStatusMessage.repaint();

                JOptionPane.showMessageDialog(this,
                        "LeapMotion controller enabled successfully.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(this,
                        "LeapMotion controller is already enabled.",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "There is any flight running.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }//GEN-LAST:event_jButtonStopSelectedFlightActionPerformed
    // </editor-fold>

    // <editor-fold desc="Custom events" defaultstate="collapsed">
    @Override
    public void messageReceived(String message) {
        logTextArea.append("Recieved: " + message);
    }

    @Override
    public void controllerMessage(String message) {
        logTextArea.append("Controller message: " + message);
    }

    @Override
    public void commandSent(String command) {
        logTextArea.append("Sent: " + command);
    }

    @Override
    public void doneExecuting() {
        logTextArea.append("Command manager has sent the command ");
    }

    @Override
    public void droneResponse(String response) {
        logTextArea.append("Drone response: " + response);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        logTextArea.requestFocus();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        logTextArea.requestFocus();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
    // </editor-fold>

    // <editor-fold desc="Getter & Setter" defaultstate="collapsed">
    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }
    // </editor-fold>

    // <editor-fold desc="Event triggers" defaultstate="collapsed">
    public void notifyChangeSettings() {
        listener.settingsChanged();
    }
    // </editor-fold>

    // <editor-fold desc="Variables declaration" defaultstate="collapsed">
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel degreesJPanel;
    private javax.swing.JLabel degreesLabel;
    private javax.swing.JTextField degreesSensibilityValue;
    private javax.swing.JButton downButton;
    private javax.swing.JPanel flipJPanel;
    private javax.swing.JTextField heightThreasholdValueTextBox;
    private javax.swing.JLabel heightThresholdLabel;
    private javax.swing.JButton jButtonAbortFlight;
    private javax.swing.JButton jButtonApplySettings;
    private javax.swing.JButton jButtonDroneBack;
    private javax.swing.JButton jButtonDroneFlip;
    private javax.swing.JButton jButtonDroneForward;
    private javax.swing.JButton jButtonDroneLand;
    private javax.swing.JButton jButtonDroneLeft;
    private javax.swing.JButton jButtonDroneRight;
    private javax.swing.JButton jButtonDroneTakeoff;
    private javax.swing.JButton jButtonRefreshSettings;
    private javax.swing.JButton jButtonStartRecording;
    private javax.swing.JButton jButtonStartSelectedFlight;
    private javax.swing.JButton jButtonStopRecording;
    private javax.swing.JButton jButtonStopSelectedFlight;
    private javax.swing.JComboBox<String> jComboBoxFlip;
    private javax.swing.JComboBox<String> jComboBoxSelectRecord;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRecordSelectorMessage;
    private javax.swing.JLabel jLabelRecordingStatusMessage;
    private javax.swing.JPanel jPaneLogPage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelAbortPanel;
    private javax.swing.JPanel jPanelBody;
    private javax.swing.JPanel jPanelExtraCommands;
    private javax.swing.JPanel jPanelFastCommands;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelInfos;
    private javax.swing.JPanel jPanelMoveInSpace;
    private javax.swing.JPanel jPanelRecordButtons;
    private javax.swing.JPanel jPanelRecordFiles;
    private javax.swing.JPanel jPanelRecordSelector;
    private javax.swing.JPanel jPanelRecordedFlightButtons;
    private javax.swing.JPanel jPanelRecording;
    private javax.swing.JPanel jPanelSettings;
    private javax.swing.JSpinner jSpinnerDroneMovementStep;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextFieldEstimatedExecutionTime;
    private javax.swing.JTextField jTextFieldRecordedOn;
    private javax.swing.JTextField jTextFieldTotalCommands;
    private javax.swing.JScrollPane logScrollPane;
    private javax.swing.JTextArea logTextArea;
    private javax.swing.JLabel movementSteopJLabel;
    private javax.swing.JPanel sensibilityJPanel;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
    // </editor-fold>
}
