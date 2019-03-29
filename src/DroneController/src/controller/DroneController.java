package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import java.util.ArrayList;
import java.util.List;
import settings.SettingsManager;

/**
 *
 * @author Fadil Smajilbasic
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager = new CommandManager();
    ;
    private static SettingsManager settingsManager = new SettingsManager();
    private FrameHelper helper = new FrameHelper();
    private Controller controller = new Controller();
    private List<Float> deltas = new ArrayList<Float>();
    private float controllerSensibility;
    private float controllerDeltaPoints;

    public DroneController() {
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        final float CONTROLLER_HEIGHT_DELTA_POINTS = 20;

        controller.addListener(this);

        this.controllerSensibility = getFloatValueFromSetting("sensibility", CONTROLLER_SENSIBILITY_DEFAULT_VALUE);
        this.controllerDeltaPoints = getFloatValueFromSetting("height_points_number", CONTROLLER_HEIGHT_DELTA_POINTS);

        System.out.println("sensibility: " + controllerSensibility);
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

    private void checkHeightControl() {

        float lastY = helper.getDeltaY();

        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getLeftHand()) / 10);

        if ((handSpeed > this.controllerSensibility) && lastY != 0.0) {
            addDeltasValue(lastY);
            float average = getAverageDeltas();

            int yPos = (int) lastY;

            //Costruisce la stringa
            if (yPos != 0 && (yPos < average || yPos > -average)) {
                String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));

                //Invia la stringa
                commandManager.sendCommand(message);
                System.out.println("average: " + average);
                System.out.println("Sending message: " + message);

            }
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

    private void checkMovementControl() {
        float pitchValue = helper.getPitch(helper.getRightHand());
        float rollValue = helper.getRoll(helper.getRightHand());
        float yawValue = helper.getYaw(helper.getRightHand());

        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getRightHand()) / 10);

        System.out.println("pitch: " + pitchValue);
//        System.out.println("hand speed: " + handSpeed);
        if (handSpeed > controllerSensibility) {
            if (rollValue != 0.0 && Math.abs(rollValue) > 5) {
                String message = rollValue < 0 ? Commands.right((int) Math.abs(rollValue)-5) : Commands.left((int) rollValue-5);
                System.out.println("message: " + message);
//                commandManager.sendCommand(message);
            }
        }
    }

    @Override
    public void run() {

        System.out.println("reading");
//        disabled for testing
//        commandManager.sendCommand(Commands.ENABLE_COMMANDS);
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
