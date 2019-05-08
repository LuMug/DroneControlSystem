package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
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
 *
 * @author Fadil Smajilbasic
 */
public class DroneController extends Listener implements Runnable, SettingsListener, CommandManagerListener {

    private final CommandManager COMMAND_MANAGER = new CommandManager(this);
    private final SettingsManager SETTINGS_MANAGER = new SettingsManager();
    private final FrameHelper FRAME_HELPER = new FrameHelper();
    private final Controller CONTROLLER = new Controller();

    private float controllerSensibility;
    private float controllerDeltaPoints;
    private float controllerDegreesSensibility;
    private float movementDelay;
    private float deltaAverageMultiplier;
    private CommandListener listener;
    private float heightThreshold;
    private boolean executingCommand = false;
    
    //Recording variables
    private boolean isRecordingFlight = false;
    private FlightRecorder recorder = new FlightRecorder();
    private FlightBuffer recordBuffer = new FlightBuffer();
    
    public DroneController() {
        CONTROLLER.addListener(this);
    }

    @Override
    public void run() {

        if (listener != null) {
            listener.controllerMessage("DroneController started\n");
        }

        COMMAND_MANAGER.sendCommand(Commands.ENABLE_COMMANDS);
//        COMMAND_MANAGER.sendCommand(Commands.TAKEOFF);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        }

        listener.controllerMessage("In air\n");

        while (CONTROLLER.isConnected()) {
            Frame frame = CONTROLLER.frame();
            FRAME_HELPER.setFrame(frame);
            if (!getExecutingCommand()) {
                if (FRAME_HELPER.getCurrentFrame().id() != FRAME_HELPER.getLastFrame().id()) {

                    setExecutingCommand(true);
                    checkHeightControl();
                    checkMovementControl();

                }
            } else {
//                System.err.println("not finished executing");
            }
        }

        if (listener != null) {
            listener.controllerMessage("controller not connected\n");
        } else {
            System.out.println("controller not connected");
        }

        COMMAND_MANAGER.sendCommand(Commands.LAND);
    }

    public void setListener(CommandListener listener) {
        this.listener = listener;
    }

    public SettingsManager getSettingsManager() {
        return SETTINGS_MANAGER;
    }

    @Override
    public void onConnect(Controller controller) {
        if (listener != null) {
            listener.controllerMessage("Controller connected\n");
        } else {
            System.out.println("Controller connected");
        }
        loadVariables();
    }

    private void loadVariables() {
        System.out.println("loading variables");
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        final float CONTROLLER_HEIGHT_DELTA_POINTS = 20;
        final float CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE = 5;
        final float MOVEMENT_DELAY_DEFAULT_VALUE = 500;
        final float DELTA_AVERAGE_MULTIPLIER = 1.5f;
        final float HEIGHT_THRESHOLD = 4;

        this.controllerSensibility = getFloatValueFromSetting("sensibility", CONTROLLER_SENSIBILITY_DEFAULT_VALUE);
        this.controllerDeltaPoints = getFloatValueFromSetting("heightPointsNumber", CONTROLLER_HEIGHT_DELTA_POINTS);
        this.controllerDegreesSensibility = getFloatValueFromSetting("degreesSensibility", CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE);
        this.movementDelay = getFloatValueFromSetting("movementDelay", MOVEMENT_DELAY_DEFAULT_VALUE);
        this.deltaAverageMultiplier = getFloatValueFromSetting("deltaAverageMultiplier", DELTA_AVERAGE_MULTIPLIER);
        this.heightThreshold = getFloatValueFromSetting("heightThreshold", HEIGHT_THRESHOLD);

        listener.controllerMessage("Settings updated\n");
        listener.controllerMessage("Controller sensibility: " + controllerSensibility + "\n");
        listener.controllerMessage("Controller delta points: " + controllerDeltaPoints + "\n");
        listener.controllerMessage("degrees sensibility: " + controllerDegreesSensibility + "\n");
        listener.controllerMessage("movement delay: " + movementDelay + "\n");
        listener.controllerMessage("delta average multiplier: " + deltaAverageMultiplier + "\n");
        listener.controllerMessage("height threshold: " + heightThreshold + "\n");

    }

    private void checkHeightControl() {

        float lastHeightReal = FRAME_HELPER.getHandY(FRAME_HELPER.getLeftHand(null));

        float lastY = FRAME_HELPER.getDeltaY();
        lastY = (int) ((lastHeightReal - 300) / 2);
        if (FRAME_HELPER.getLeftHand(null) != null) {

            if (Math.abs(lastY) > heightThreshold && lastY != 0.0) {
                if (lastY != 0.0) {
                    String message = lastY > 0 ? Commands.up((int) lastY - (int) heightThreshold) : Commands.down(Math.abs((int) lastY + (int) heightThreshold));
                    COMMAND_MANAGER.sendCommand(message);
                    
                    //Add commands to recorder
                    if(isRecordingFlight){
                        recordBuffer.addCommand(message);
                    }
                    
                    listener.commandSent(message + "\n");

                }
            }
        }

    }

    private void checkMovementControl() {
        String[] commands = new String[3];
        if (FRAME_HELPER.getRightHand(null) != null) {
            float pitchValue = FRAME_HELPER.getPitch(FRAME_HELPER.getRightHand(null));
            float rollValue = FRAME_HELPER.getRoll(FRAME_HELPER.getRightHand(null));
            float yawValue = FRAME_HELPER.getYaw(FRAME_HELPER.getLeftHand(null));

            if (Math.abs(rollValue) > controllerDegreesSensibility) {

                if ((Math.abs((int) rollValue) - controllerDegreesSensibility) != 0) {
                    String message = rollValue < 0
                            ? Commands.right((int) Math.abs(rollValue - controllerDegreesSensibility))
                            : Commands.left((int) (rollValue - controllerDegreesSensibility));
                    commands[0] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }
                }
            }

            if (Math.abs(pitchValue) > controllerDegreesSensibility) {
                if ((Math.abs((int) pitchValue) - controllerDegreesSensibility) != 0) {
                    String message = pitchValue > 0
                            ? Commands.back((int) (pitchValue - controllerDegreesSensibility))
                            : Commands.forward((int) Math.abs(pitchValue - controllerDegreesSensibility));
                    commands[1] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }
                }
            }

            if (Math.abs(yawValue) > controllerDegreesSensibility) {
                if ((Math.abs((int) yawValue) - controllerDegreesSensibility) != 0) {
                    String message = yawValue > 0
                            ? Commands.rotateCounterClockwise((int) (yawValue - controllerDegreesSensibility))
                            : Commands.rotateClockwise((int) Math.abs(yawValue - controllerDegreesSensibility));
                    commands[1] = message;
                    if (listener != null) {
                        listener.commandSent(message + "\n");
                    }

                }

            }
        }

        COMMAND_MANAGER.sendCommands(commands);
        
        if(isRecordingFlight){
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

    public CommandManager getCommandManager() {
        return this.COMMAND_MANAGER;
    }

    /**
     * Method called when the user updates the settings from the GUI
     */
    @Override
    public void settingsChanged() {
        System.out.println("Settings updated");
        loadVariables();
    }

    public synchronized boolean getExecutingCommand() {
        return executingCommand;
    }

    public synchronized void setExecutingCommand(boolean executingCommand) {
        executingCommand = false;
    }
    
    public void startRecording(){
        this.recordBuffer.clear();
        this.isRecordingFlight = true;
    }
    
    public void stopRecording(){
        this.recordBuffer.clear();
        this.isRecordingFlight = false;
        
        try{
            recorder.createBase();
            FlightRecord record = recorder.generateRecordFile();
            recorder.saveFlightPattern(this.recordBuffer, record);
        }
        catch(IOException ex){
            System.out.println("[Info] Can't save the flight. *SAD SMILE*");
        }
    }

    @Override
    public void doneExecuting() {
        setExecutingCommand(false);
    }
}
