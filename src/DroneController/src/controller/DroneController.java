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

    private static CommandManager commandManager = new CommandManager();;
    private static SettingsManager settingsManager = new SettingsManager();
    private FrameHelper helper = new FrameHelper();
    private Controller controller = new Controller();
    private List<Float> deltas = new ArrayList<Float>();
    private float controllerSensibility;

    public DroneController() {
        final float CONTROLLER_SENSIBILITY_DEFAULT_VALUE = 2;
        controller.addListener(this);
        
        try{
            this.controllerSensibility = Float.parseFloat(settingsManager.getSetting("sensibility"));
        }
        catch(NumberFormatException ex){
            System.err.println("[Parse error] Can't parse 'sensibility' value from settings, set the default one.");
            this.controllerSensibility = CONTROLLER_SENSIBILITY_DEFAULT_VALUE;
        }
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

    public float getAverageDeltas() {
        float tot = 0;
        for (float delta : deltas) {
            tot += Math.abs(delta);
        }
        return tot / (float) deltas.size();
    }

    public float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    public void shiftDeltas() {
        for (int i = deltas.size() - 1; i > 0; i--) {
            deltas.set(i, deltas.get(i - 1));
        }
    }

    public void checkHeightControl() {

        float lastY = helper.getDeltaY();

        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getLeftHand()) / 10);

        if ((handSpeed > this.controllerSensibility) && lastY != 0.0) {
            if (deltas.size() < 20) {
                deltas.add(lastY);
            } else {
                shiftDeltas();
                deltas.set(0, lastY);
            }
            float average = getAverageDeltas();

            //System.out.println("average: " + average);
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

    public void checkMovementControl() {
        float pitchValue = -helper.getPitch(helper.getRightHand());
        float rollValueReal = -helper.getRoll(helper.getRightHand());
        float yawValue = -helper.getYaw(helper.getRightHand());
        float handSpeed = Math.abs(helper.getHandSpeedY(helper.getRightHand()) / 10);
        float rollValue = helper.getHandAngle(helper.getRightHand());

//        if (rollValue > 0) {
//            rollValue = 180 - rollValue;
//        } else {
//            rollValue = -(180 + rollValue);
//        }
        
//        System.out.println("real roll: " + rollValueReal);
        System.out.println("calc roll: " + rollValue);

//        System.out.println("hand speed: " + handSpeed);
        if (handSpeed > DEAD_ZONE) {
//            System.out.println("movement detected");
//            if (pitchValue != 180 && pitchValue != 0.0) {
//                System.out.println("pitch: " + pitchValue);
//
//            }
//
//            if (yawValue != 180 && yawValue != 0.0) {
//                System.out.println("yaw: " + yawValue);
//            }

            if (rollValue != 180 && rollValue != 0.0) {
                String message = rollValue > 90 ? Commands.right((int) rollValue - 90) : Commands.left(Math.abs((int) rollValue));
                System.out.println("message: " + message);
                System.out.println("roll: " + rollValue);
//                commandManager.sendCommand(message);
            }
        }
    }

    @Override
    public void run() {

        System.out.println("reading");
//        disabled for testing
//        commandManager.sendCommand(Commands.ENABLE_COMMANDS);
        System.out.println("sending command: " + Commands.ENABLE_COMMANDS);
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

}
