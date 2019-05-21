package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import gui.CommandListener;
import gui.SendTask;
import gui.TimeoutThread;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;
import settings.ControllerSettings;
import settings.SettingsListener;

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
    private final CommandManager COMMAND_MANAGER;
    /**
     * This constant is a ControllerSettings object used to load variable values
     * saved on the config file to the local variables in this class.
     */
    private final ControllerSettings CONTROLLER_SETTINGS = new ControllerSettings();
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

    private TimeoutThread executor = new TimeoutThread(2, TimeUnit.SECONDS);

    /**
     * Drone controller constructor that adds this object as the LeapMotion
     * controller listener.
     *
     * @param listener Listener used to display useful logs to the GUI.
     * @throws SocketException thrown when the server port it's already used.
     */
    public DroneController(CommandListener listener) throws SocketException {
        COMMAND_MANAGER = new CommandManager(this);

        //Cross-set controller listener for cross communication between threads
        CONTROLLER.addListener(this);
        this.listener = listener;

        loadVariables();
    }

    /**
     * The run method reads the LeapMotion input and calls the appropriate
     * methods to translate the input of the LeapMotion to tello commands, after
     * which it sends the commands to the drone.
     */
    @Override
    public void run() {

        listener.controllerMessage("DroneController started\n");

        executor.execute(new SendTask(COMMAND_MANAGER, Commands.ENABLE_COMMANDS));

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

        listener.controllerMessage("controller not connected\n");

        System.out.println("controller not connected");

        //COMMAND_MANAGER.sendCommand(Commands.LAND);
        executor.execute(new SendTask(COMMAND_MANAGER, Commands.LAND));
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
        listener.controllerMessage("Controller connected\n");
        System.out.println("Controller connected");
    }

    /**
     * This method loads the variables found in the config.dcs using the
     * SETTINGS_MANAGER
     */
    private void loadVariables() {
        System.out.println("loading variables");

        this.heightThreshold = CONTROLLER_SETTINGS.getHeightThreshold();
        this.controllerDegreesSensibility = CONTROLLER_SETTINGS.getControllerDegreesSensibility();

        listener.controllerMessage("Settings updated\n");
        listener.controllerMessage("degrees sensibility: " + controllerDegreesSensibility + "\n");
        listener.controllerMessage("height threshold: " + heightThreshold + "\n");
    }

    /**
     *
     */
    private void loadDefaultVariables() {
        final float HEIGHT_THRESHOLD = 4;
        final float DEGREES_SENSIBILITY = 10;

        this.heightThreshold = HEIGHT_THRESHOLD;
        this.controllerDegreesSensibility = DEGREES_SENSIBILITY;
    }

    /**
     * This method gets the height of the left hand from the frame read by the
     * LeapMotion and then calculates the command to send to the drone regarding
     * its height. There is a threshold to prevent accidental height commands.
     */
    private void checkHeightControl() {

        float lastHeightReal = FRAME_HELPER.getHandY(FRAME_HELPER.getLeftHand(null));
        String[] commands = new String[2];
        float lastY = (int) ((lastHeightReal - 300) / 2);

        if (FRAME_HELPER.getLeftHand(null) != null) {

            float rollLeftValue = FRAME_HELPER.getRoll(FRAME_HELPER.getLeftHand(null));

            if (Math.abs(rollLeftValue) > controllerDegreesSensibility * 3) {

                int rollLeftRelative = (int) (Math.abs(rollLeftValue) - controllerDegreesSensibility * 3);

                if (rollLeftRelative != 0) {
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
                    String message = lastY > 0 ? Commands.up((int) lastY - (int) heightThreshold)
                            : Commands.down(Math.abs((int) lastY + (int) heightThreshold));
                    commands[1] = message;
                    listener.commandSent(message + "\n");
                }
            }
        }
        /*TODO: use executor object*/
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
    }

    /**
     * Method called when the user updates the settings from the GUI
     */
    @Override
    public void settingsChanged() {
        System.out.println("[Info] Settings updated, reload settings variables.");
        try {
            //Reload all the settings
            CONTROLLER_SETTINGS.updateSettings();

            //Reload local settings
            loadVariables();
        } catch (IllegalArgumentException ex) {
            System.err.println("[Error] Error while loading settings. Set the default ones.");
            loadDefaultVariables();
        }
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

    @Override
    public void droneResponse(String response) {
        System.out.println("Drone rsponse " + response);
    }

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
