package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Listener;
import communication.*;
import gui.CommandListener;
import java.io.IOException;
import settings.SettingsListener;
import settings.SettingsManager;
import recorder.FlightBuffer;
import recorder.FlightRecord;
import recorder.FlightRecorder;

/**
 * This class reads the input from the LeapMotion and sends the commands to the
 * drone.
 *
 * @author Fadil Smajilbasic
 * @author Luca Di Bello
 */
public class DroneController extends Listener implements Runnable, SettingsListener, CommandManagerListener {

    /**
     * This constant is the command manager required in order to send commands
     * and receive responses from the drone.
     */
    private final CommandManager COMMAND_MANAGER = new CommandManager(this);
    /**
     * This constant is the settings manager used to load variable values form
     * the config.dcs configuration file.
     */
    private final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    /**
     * This constant is the frame helper containing useful methods to obtain
     * information from a frame read by the LeapMotion.
     */
    private final FrameHelper FRAME_HELPER = new FrameHelper();
    /**
     * This constant is a Controller form the Leap SDK.
     */
    private final Controller CONTROLLER = new Controller();

    /**
     * This field contains the degrees threshold used in the
     * checkMovementControl method.
     */
    private float controllerDegreesSensibility;
    /**
     * This is the listener that gets notified when a new command is registered
     * by the DroneController.
     */
    private CommandListener listener;
    /**
     * This field contains the Threshold of the Y axis used in the
     * checkHeightControl method.
     */
    private float heightThreshold;
    /**
     * This field is a boolean containing the status of the DroneController,
     * whether is is executing a command or is it reading a new frame.
     */
    private boolean executingCommand = false;

    /**
     * This field contains information on whether or not the controller should
     * read new commands from the LeapMotion or not.
     */
    private boolean isLeapMotionEnabled = true;

    /**
     * This field contains information whether the flight commands are being
     * recorded or not.
     */
    private boolean isRecordingFlight = false;
    /**
     * This field is a FlightRecorder object used to record.
     */
    private final FlightRecorder RECORDER = new FlightRecorder();
    /**
     * This field contains the buffer of commands that will be written to the
     * recording file.
     */
    private FlightBuffer recordBuffer = new FlightBuffer();

    /**
     * Drone controller constructor that adds this object as the LeapMotion
     * controller listener.
     */
    public DroneController() {
        CONTROLLER.addListener(this);
    }

    /**
     * The run method reads the LeapMotion input and calls the appropriate
     * methods to translate the input of the LeapMotion to tello commands, after
     * which it sends the commands to the drone.
     */
    @Override
    public void run() {

        if (listener != null) {
            listener.controllerMessage("DroneController started\n");
        }

        COMMAND_MANAGER.sendCommand(Commands.ENABLE_COMMANDS);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        listener.controllerMessage("Sending commands\n");

        while (CONTROLLER.isConnected()) {
            try {
                if (isLeapMotionEnabled) {
                    Frame frame = CONTROLLER.frame();
                    FRAME_HELPER.setFrame(frame);
                    if (!isExecutingCommand()) {
                        if (FRAME_HELPER.getCurrentFrame().id() != FRAME_HELPER.getLastFrame().id()) {

                            setExecutingCommand(true);
                            checkHeightControl();
                            setExecutingCommand(true);
                            checkMovementControl();

                        }
                    } else {
                        System.out.println("not finished executing");
                    }
                } else {
                    System.err.println("[Info] Leap Motion controller is disabled. Wait until notify");

                    synchronized (this) {
                        this.wait();
                    }

                    System.out.println("[Info] LeapMotion controller re-enabled successfully");
                }
            } catch (InterruptedException ex) {
                System.err.println("[Error] Detected an error: " + ex.getMessage());
            }
        }

        if (listener != null) {
            listener.controllerMessage("controller not connected\n");
        } else {
            System.out.println("controller not connected");
        }

        COMMAND_MANAGER.sendCommand(Commands.LAND);
    }

    /**
     * Setter method for the listener field.
     *
     * @param listener the CommandListener that will be notified when a new
     * command is executed.
     */
    public void setListener(CommandListener listener) {
        this.listener = listener;
    }

    /**
     * Getter method for the SETTINGS_MANAGER field.
     *
     * @return the SETTINGS_MANAGER object.
     */
    public SettingsManager getSettingsManager() {
        return SETTINGS_MANAGER;
    }

    /**
     * Getter method for the COMMAND_MANAGER field.
     *
     * @return the command manager object.
     */
    public CommandManager getCommandManager() {
        return this.COMMAND_MANAGER;
    }

    /**
     * This method is called by the LeapMotion service when a LeapMotion
     * controller is connected.
     *
     * @param controller the LeapMotion Controller
     */
    @Override
    public void onConnect(Controller controller) {
        if (listener != null) {
            listener.controllerMessage("Controller connected\n");
        } else {
            System.out.println("Controller connected");
        }
        loadVariables();
        System.out.println("enabled gestures");
        controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
    }

    /**
     * This method loads the variables found in the config.dcs using the
     * SETTINGS_MANAGER
     */
    private void loadVariables() {
        System.out.println("loading variables");
        final float HEIGHT_THRESHOLD = 4;
        final float DEGREES_SENSIBILITY = 10;

        this.heightThreshold = getFloatValueFromSetting("heightThreshold", HEIGHT_THRESHOLD);
        this.controllerDegreesSensibility = getFloatValueFromSetting("degreesSensibility", DEGREES_SENSIBILITY);

        listener.controllerMessage("Settings updated\n");
        listener.controllerMessage("degrees sensibility: " + controllerDegreesSensibility + "\n");
        listener.controllerMessage("height threshold: " + heightThreshold + "\n");

    }

    /**
     * This method gets the height of the left hand from the frame read by the
     * LeapMotion and then calculates the command to send to the drone regarding
     * its height. There is a threshold to prevent accidental height commands.
     */
    private void checkHeightControl() {

        float lastHeightReal = FRAME_HELPER.getHandY(FRAME_HELPER.getLeftHand(null));
        String[] commands = new String[2];
        float lastY = FRAME_HELPER.getDeltaY();
        lastY = (int) ((lastHeightReal - 300) / 2);
        if (FRAME_HELPER.getLeftHand(null) != null) {

            float rollLeftValue = FRAME_HELPER.getRoll(FRAME_HELPER.getLeftHand(null));

            if (Math.abs(rollLeftValue) > controllerDegreesSensibility * 3) {

                int rollLeftRelative = (int) (Math.abs(rollLeftValue) - controllerDegreesSensibility * 3);

                if (rollLeftRelative != 0) {
                    System.out.println("yaw: " + rollLeftRelative);
                    String message = rollLeftValue < 0
                            ? Commands.rotateClockwise(rollLeftRelative)
                            : Commands.rotateCounterClockwise(rollLeftRelative);
                    commands[0] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }
                }
            }

            if (Math.abs(lastY) > heightThreshold && lastY != 0.0 && Math.abs(lastY) > 20 && Math.abs(lastY) < 500) {
                if (lastY != 0.0) {
                    String message = lastY > 0 ? Commands.up((int) lastY - (int) heightThreshold) : Commands.down(Math.abs((int) lastY + (int) heightThreshold));
                    commands[1] = message;

                    //Add commands to recorder
                    if (isRecordingFlight) {
                        //REMOVE
                        System.err.println("Added command to file: " + message);
                        recordBuffer.addCommand(message);
                    }

                    listener.commandSent(message + "\n");

                }
            }
        }
        COMMAND_MANAGER.sendCommands(commands);
        doneExecuting();

    }

    /**
     * This method gets the right hand object and calculates the angle of pitch,
     * yaw and roll angles and then sends the appropriate command to the drone.
     */
    private void checkMovementControl() {
        String[] commands = new String[3];
        if (FRAME_HELPER.getRightHand(null) != null) {
            float pitchValue = FRAME_HELPER.getPitch(FRAME_HELPER.getRightHand(null));
            float rollRightValue = FRAME_HELPER.getRoll(FRAME_HELPER.getRightHand(null));

            int rollRightRelative = (int) (Math.abs((int) rollRightValue) - controllerDegreesSensibility);

            if (Math.abs(rollRightValue) > controllerDegreesSensibility) {

                if (rollRightRelative != 0) {
                    String message = rollRightValue < 0
                            ? Commands.right((int) rollRightRelative)
                            : Commands.left((int) rollRightRelative);
                    commands[0] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }
                }
            }

            if (Math.abs(pitchValue) > controllerDegreesSensibility) {
                int pitchRelative = (int) (Math.abs((int) pitchValue) - controllerDegreesSensibility);

                if (pitchRelative != 0) {
                    String message = pitchValue > 0
                            ? Commands.back(pitchRelative)
                            : Commands.forward(pitchRelative);
                    commands[1] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }
                }
            }

        }

        COMMAND_MANAGER.sendCommands(commands);
        doneExecuting();
        if (isRecordingFlight) {
            recordBuffer.addCommands(commands);
        }
    }

    /**
     * This method uses the SETTINGS_MANAGER in order to read the settings
     * values from the config file
     *
     * @param settingName The name of the setting to search
     * @param defaultValue The default value of that setting
     * @return the value read from the file
     */
    private float getFloatValueFromSetting(String settingName, float defaultValue) {
        try {
            return Float.parseFloat(SETTINGS_MANAGER.getSetting(settingName));
        } catch (NumberFormatException ex) {
            System.err.println("[Parse error] Can't parse '" + settingName + "' value from settings, set the default one.");
            return defaultValue;
        } catch (IllegalArgumentException ex) {
            System.err.println("[Settings name error] Can't get setting value with name: '" + settingName + "'");
            return defaultValue;
        }
    }

    /**
     * Method called when the user updates the settings from the GUI
     */
    @Override
    public void settingsChanged() {
        System.out.println("Settings updated");
        loadVariables();
    }

    /**
     * Getter method used to check if the Controller is currently executing any
     * command.
     *
     * @return true if the controller is executing a command, false if it
     * finished executing the last command.
     */
    public synchronized boolean isExecutingCommand() {
        return executingCommand;
    }

    /**
     * Setter method for the executingCommand field.
     *
     * @param executingCommand the value to set
     */
    public synchronized void setExecutingCommand(boolean executingCommand) {
        this.executingCommand = executingCommand;
    }

    /**
     * This method clears the recording buffer and starts a new recording.
     *
     */
    public void startRecording() {
        this.recordBuffer.clear();
        this.isRecordingFlight = true;

        System.out.println("[Info] Start recording");
    }

    /**
     * This method stops the recording of the commands buffer and saves them to
     * a file.
     */
    public void stopRecording() {
        this.isRecordingFlight = false;

        try {
            if (recordBuffer.length() > 0) {
                RECORDER.createBase();
                FlightRecord record = RECORDER.generateRecordFile();
                System.out.println("Generated file: " + record.getSaveLocation());
                RECORDER.saveFlightPattern(this.recordBuffer, record);
                System.out.println("[Info] File saved to path: " + record.getSaveLocation());
            }
        } catch (IOException ex) {
            System.out.println("[Info] Can't save the flight. *SAD SMILE*");
        }

        System.out.println("[Info] Stopped recording");
    }

    /**
     * 
     */
    @Override
    public synchronized void doneExecuting() {
        setExecutingCommand(false);
    }

    /**
     * This method enables the LeapMotion controller.
     */
    public void EnableLeapMotionController() {
        if (isLeapMotionEnabled) {
            return;
        }
        this.isLeapMotionEnabled = true;

        synchronized (this) {
            System.out.println("[Info] Enabled the LeapMotion controller");
            this.notifyAll();
        }
    }

    /**
     * This method disables the LeapMotion controller.
     */
    public void DisableLeapMotionController() {
        if (!isLeapMotionEnabled) {
            return;
        }
        this.isLeapMotionEnabled = false;
    }

    /**
     * Getter method for the controller enabled flag.
     */
    public boolean isLeapMotionEnabled() {
        return isLeapMotionEnabled;
    }
}
