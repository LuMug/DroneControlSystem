package gui;

import communication.CommandManager;
import communication.CommandManagerListener;
import communication.Commands;
import controller.DroneController;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import settings.SettingsManager;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import recorder.FlightBuffer;
import recorder.FlightRecord;
import settings.FlipCommand;
import settings.SettingsListener;

/**
 *
 * @author Luca Di Bello
 */
public class DroneControllerMonitor extends javax.swing.JFrame implements CommandListener, CommandManagerListener, MouseListener {

    private SettingsManager manager = new SettingsManager();
    private SettingsListener listener;
    private DroneController controller = new DroneController();
    private final Path recordFolderPath = Paths.get("records");
    private List<FlightRecord> flightRecords = new ArrayList<>();
    private CommandManager commandManager;

    
    private final String RECORDING_STATUS_DISABLED = "LeapMotion-Controlled";
    private final String RECORDING_STATUS_ENABLED = "Running a recorded flight";
    
    /**
     * Creates new form DroneControllerMonitor
     */
    public DroneControllerMonitor() {
        initComponents();

        DefaultCaret caret = (DefaultCaret) logTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        updateSettingsValues(manager);

        controller.setListener(this);
        this.setListener(controller);
        commandManager = controller.getCommandManager();

        new Thread(controller).start();

        insertRecordsInSelector();
    }

    public void setListener(SettingsListener listener) {
        this.listener = listener;
    }

    public void insertRecordsInSelector() {
        // read all files in 'records'
        this.jComboBoxSelectRecord.removeAllItems();

        File folder = new File(recordFolderPath.toString());
        File[] files = folder.listFiles();

        if (files != null && files.length != 0) {
            for (File file : files) {
                System.out.println("Found file: " + file.getAbsolutePath());

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
        jButtonDroneUp = new javax.swing.JButton();
        jButtonDroneDown = new javax.swing.JButton();
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
        jComboBoxFlip = new javax.swing.JComboBox<>();
        batteryButton = new javax.swing.JButton();
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

        jButtonDroneUp.setText("FORWARD");
        jButtonDroneUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneUpActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneUp, java.awt.BorderLayout.PAGE_START);

        jButtonDroneDown.setText("BACK");
        jButtonDroneDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneDownActionPerformed(evt);
            }
        });
        jPanelMoveInSpace.add(jButtonDroneDown, java.awt.BorderLayout.PAGE_END);

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

        jButtonDroneFlip.setText("FLIP IT :0");
        jButtonDroneFlip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDroneFlipActionPerformed(evt);
            }
        });
        flipJPanel.add(jButtonDroneFlip);

        jComboBoxFlip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Forward", "Right", "Back", "Left" }));
        jComboBoxFlip.setSelectedIndex(0);
        flipJPanel.add(jComboBoxFlip);

        jPanelExtraCommands.add(flipJPanel);

        batteryButton.setText("Battery");
        batteryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                batteryButtonActionPerformed(evt);
            }
        });
        jPanelExtraCommands.add(batteryButton);

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

        jPanelBody.add(jTabbedPane1, java.awt.BorderLayout.PAGE_START);

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
        commandManager.sendCommandAsync(Commands.EMERGENCY);
        System.out.println("[GUI] Sent landing command to drone");
    }//GEN-LAST:event_jButtonAbortFlightActionPerformed

    private void jButtonDroneFlipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneFlipActionPerformed
        //Send flip commands
        int chosenFlipOption = jComboBoxFlip.getSelectedIndex();

        final int FORWARD = 0;
        final int RIGHT = 1;
        final int BACK = 2;
        final int LEFT = 3;

        switch (chosenFlipOption) {
            case FORWARD:
                commandManager.sendCommandAsync(Commands.flip(FlipCommand.FORWARD));
                break;
            case RIGHT:
                commandManager.sendCommandAsync(Commands.flip(FlipCommand.RIGHT));
                break;
            case BACK:
                commandManager.sendCommandAsync(Commands.flip(FlipCommand.BACK));
                break;
            case LEFT:
                commandManager.sendCommandAsync(Commands.flip(FlipCommand.LEFT));
                break;
            default:
                break;
        }

        System.out.println("[GUI] Sent flip command to drone");
    }//GEN-LAST:event_jButtonDroneFlipActionPerformed

    private void jButtonDroneUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneUpActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.forward(stepValue));
    }//GEN-LAST:event_jButtonDroneUpActionPerformed

    private void jButtonDroneRightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneRightActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.right(stepValue));
    }//GEN-LAST:event_jButtonDroneRightActionPerformed

    private void jButtonDroneDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneDownActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.back(stepValue));
    }//GEN-LAST:event_jButtonDroneDownActionPerformed

    private void jButtonDroneLeftActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneLeftActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.left(stepValue));
    }//GEN-LAST:event_jButtonDroneLeftActionPerformed

    private void jButtonDroneTakeoffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneTakeoffActionPerformed
        commandManager.sendCommand(Commands.TAKEOFF);
    }//GEN-LAST:event_jButtonDroneTakeoffActionPerformed

    private void jButtonDroneLandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDroneLandActionPerformed
        commandManager.sendCommand(Commands.LAND);
    }//GEN-LAST:event_jButtonDroneLandActionPerformed

    private void batteryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_batteryButtonActionPerformed
        commandManager.sendCommand(Commands.getBattery());
    }//GEN-LAST:event_batteryButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.up(stepValue));
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        int stepValue = (Integer) jSpinnerDroneMovementStep.getValue();
        commandManager.sendCommand(Commands.down(stepValue));
    }//GEN-LAST:event_downButtonActionPerformed

    private void jButtonStartRecordingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStartRecordingActionPerformed
        //Start recording
        controller.startRecording();
    }//GEN-LAST:event_jButtonStartRecordingActionPerformed

    private void jButtonStopRecordingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopRecordingActionPerformed
        // Check if added another file
        insertRecordsInSelector();

        //Stop recording and save file
        controller.stopRecording();
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
             
        jLabelRecordingStatusMessage.setText(this.RECORDING_STATUS_ENABLED);
        
        //Get the commands of the selected flight
        try{
            FlightBuffer commands = flightRecords.get(jComboBoxSelectRecord.getSelectedIndex()).getFlightCommands();
            
            new Thread("Recording thread") {
                public void run(){
                    //Sleep a 5 seconds
                    fancyCountdown(5);

                    String command;
                    while((command = commands.getNextCommand()) != null){
                        System.out.println("Executing command: " + command);
                        commandManager.sendCommand(command);
                    }
                }
                
                private void fancyCountdown(int seconds){
                    for(int i = 0; i < seconds; i++){
                        try{
                            System.out.println(String.format("Starting in %d seconds...",seconds-i));
                            Thread.sleep(1000);
                        }
                        catch(InterruptedException ex){}
                    }
                }
            }.start();
        }
        catch(IOException ex){
            System.err.println("[Info] Error while getting the recording commands. Stop pre-configured flight and re-enable the leapmotion controller.");
        }

        controller.EnableLeapMotionController();
        jLabelRecordingStatusMessage.setText(this.RECORDING_STATUS_DISABLED);
    }//GEN-LAST:event_jButtonStartSelectedFlightActionPerformed

    
    private void jButtonStopSelectedFlightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonStopSelectedFlightActionPerformed
        //Cut out the leapmotion controller
        System.err.println("[Info] Trying to re-enable the leapmotion...");
        controller.EnableLeapMotionController();
        jLabelRecordingStatusMessage.setText(this.RECORDING_STATUS_DISABLED);
    }//GEN-LAST:event_jButtonStopSelectedFlightActionPerformed

    public String getDateFromRecordFilename(String filename) {
        //Date [11 - 19]
        String date = filename.substring(11, 19);

        //Time [20 - 16]
        String time = filename.substring(20, 26);

        String formatted_date = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        String formatted_time = time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);

        return formatted_date + " " + formatted_time;
    }

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
    private javax.swing.JButton batteryButton;
    private javax.swing.JButton downButton;
    private javax.swing.JPanel flipJPanel;
    private javax.swing.JTextField heightPointsValueTextBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonAbortFlight;
    private javax.swing.JButton jButtonDroneDown;
    private javax.swing.JButton jButtonDroneFlip;
    private javax.swing.JButton jButtonDroneLand;
    private javax.swing.JButton jButtonDroneLeft;
    private javax.swing.JButton jButtonDroneRight;
    private javax.swing.JButton jButtonDroneTakeoff;
    private javax.swing.JButton jButtonDroneUp;
    private javax.swing.JButton jButtonStartRecording;
    private javax.swing.JButton jButtonStartSelectedFlight;
    private javax.swing.JButton jButtonStopRecording;
    private javax.swing.JButton jButtonStopSelectedFlight;
    private javax.swing.JComboBox<String> jComboBoxFlip;
    private javax.swing.JComboBox<String> jComboBoxSelectRecord;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelRecordSelectorMessage;
    private javax.swing.JLabel jLabelRecordingStatusMessage;
    private javax.swing.JPanel jPaneLogPage;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
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
    private javax.swing.JTextField sensibilityValueTextBox;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void messageRecieved(String message) {
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
}
