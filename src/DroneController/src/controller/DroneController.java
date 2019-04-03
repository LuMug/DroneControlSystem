package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import settings.SettingsManager;

/**
 *
 * @author Fadil Smajilbasic
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager = new CommandManager();
    ;
    public static SettingsManager settingsManager = new SettingsManager();
    private FrameHelper helper = new FrameHelper();
    private Controller controller = new Controller();
    private List<Float> deltas = new ArrayList<Float>();
    private float controllerSensibility;
    private float controllerDeltaPoints;
    private float controllerDegreesSensibility;
    private float movementDelay;
    private float deltaAverageMultiplier;
    private long lastMessageTimestamp = System.currentTimeMillis();

    public DroneController() {
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        final float CONTROLLER_HEIGHT_DELTA_POINTS = 20;
        final float CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE = 5;
        final float MOVEMENT_DELAY_DEFAULT_VALUE = 500;
        final float DELTA_AVERAGE_MULTIPLIER = 1.5f;

        controller.addListener(this);

        this.controllerSensibility = getFloatValueFromSetting("sensibility", CONTROLLER_SENSIBILITY_DEFAULT_VALUE);
        this.controllerDeltaPoints = getFloatValueFromSetting("height_points_number", CONTROLLER_HEIGHT_DELTA_POINTS);
        this.controllerDegreesSensibility = getFloatValueFromSetting("degrees_sensibility", CONTROLLER_DEGREES_SENSIBILITY_DEFAULT_VALUE);
        this.movementDelay = getFloatValueFromSetting("movementDelay", MOVEMENT_DELAY_DEFAULT_VALUE);
        this.deltaAverageMultiplier = getFloatValueFromSetting("deltaAverageMultiplier", MOVEMENT_DELAY_DEFAULT_VALUE);

        System.out.println("sensibility: " + controllerDegreesSensibility);
    }

    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        DroneController controller = new DroneController();
        controller.run();
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected leap");
    }

    public void onFrame(Controller controller) {

    }

    private float getAverageDeltas() {
        float tot = 0;
        for (float delta : deltas) {
            tot += Math.abs(delta);
        }
        return tot / (float) deltas.size();
    }

    private float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    private void shiftDeltas() {
        for (int i = deltas.size() - 1; i > 0; i--) {
            deltas.set(i, deltas.get(i - 1));
        }
    }

    private void addDeltasValue(float value) {
        if (deltas.size() < 20) {
            deltas.add(value);
        } else {
            shiftDeltas();
            deltas.set(0, value);
        }
    }

    private void checkHeightControl() {

        float lastY = helper.getDeltaY();
        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getLeftHand()) / 36);

        if ((handSpeed > this.controllerSensibility) && lastY != 0.0) {

            float average = getAverageDeltas();
            int yPos = (int) lastY;

            //Costruisce la stringa
            if (Math.abs(yPos) > average * deltaAverageMultiplier) {
                if (yPos != 0) {
                    String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));

                    //Invia la stringa
//                commandManager.sendCommand(message);
                    System.out.println("average: " + average);
                    System.out.println("Sending message: " + message);
                }
            } else {
                System.err.println("Value not accepted: " + yPos);
            }
        }

        addDeltasValue(lastY);

    }

    private void checkMovementControl() {
        String[] commands = new String[3];

        if ((System.currentTimeMillis() - lastMessageTimestamp) > movementDelay) {
            float pitchValue = helper.getPitch(helper.getRightHand()) / 10;
            float rollValue = helper.getRoll(helper.getRightHand()) / 10;
            float yawValue = helper.getYaw(helper.getRightHand()) / 10;

            float handSpeed = Math.abs(helper.getHandSpeedY(helper.getRightHand()) / 10);

//        System.out.println("yawValue: " + yawValue);
//
            if (Math.abs(rollValue) > controllerDegreesSensibility) {

                if (((int) rollValue - controllerDegreesSensibility) != 0) {
                    String message = rollValue < 0
                            ? Commands.right((int) Math.abs(rollValue - controllerDegreesSensibility))
                            : Commands.left((int) (rollValue - controllerDegreesSensibility));

//                commandManager.sendCommand(message);
//                    System.out.println("message: " + message);
                    commands[0] = message;
                }

            }

            if (Math.abs(pitchValue) > controllerDegreesSensibility) {
                if (((int) pitchValue - controllerDegreesSensibility) != 0) {
                    String message = pitchValue > 0
                            ? Commands.back((int) (pitchValue - controllerDegreesSensibility))
                            : Commands.forward((int) Math.abs(pitchValue - controllerDegreesSensibility));
//                    System.out.println("message: " + message);
//                    commandManager.sendCommand(message);
                    commands[1] = message;

                }
            }

            if (Math.abs(yawValue) > controllerDegreesSensibility) {
                if (((int) yawValue - controllerDegreesSensibility) != 0) {
                    String message = yawValue > 0
                            ? Commands.rotateCounterClockwise((int) (yawValue - controllerDegreesSensibility))
                            : Commands.rotateClockwise((int) Math.abs(yawValue - controllerDegreesSensibility));
//                    System.out.println("message: " + message);
//                    commandManager.sendCommand(message);
                    commands[1] = message;

                }
            }
            lastMessageTimestamp = System.currentTimeMillis();
        }

//        commandManager.sendCommands(commands);
    }

    @Override
    public void run() {

        System.out.println("reading");
//      disabled for testing
//      commandManager.sendCommand(Commands.ENABLE_COMMANDS);
//      sleep required for testing without the simulator
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
        }

        while (controller.isConnected()) {
            Frame frame = controller.frame();
            helper.setFrame(frame);
            if (helper.getFrame().id() != helper.getLastFrame().id()) {
                checkHeightControl();
                checkMovementControl();
            }
        }

        System.out.println("controller not connected");
    }

    private static float getFloatValueFromSetting(String settingName, float defaultValue) {
        try {
            return Float.parseFloat(settingsManager.getSetting(settingName));
        } catch (NumberFormatException ex) {
            System.err.println("[Parse error] Can't parse '" + settingName + "' value from settings, set the default one.");
            return defaultValue;
        } catch (IllegalArgumentException ex) {
            System.err.println("[Settings name error] Can't get setting value with name: '" + settingName + "'");
            return defaultValue;
        }
    }
}
