package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;
import communication.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager;
    private FrameHelper helper;
    private Controller controller;
    private List<Float> deltas = new ArrayList<Float>();
    private final float DEAD_ZONE = 2f;

    public DroneController() {
        controller = new Controller();
        controller.addListener(this);
        helper = new FrameHelper();
        commandManager = new CommandManager();
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

        float handSpeed = helper.getHandSpeedY(helper.getLeftHand()) / 10;
//        //Controlla media (per calcolare lo scarto)
        if ((handSpeed < -this.DEAD_ZONE || handSpeed > this.DEAD_ZONE) && lastY != 0.0) {
            if (deltas.size() < 20) {
                deltas.add(lastY);
            } else {
                shiftDeltas();
                deltas.set(0, lastY);
            }
            float average = getAverageDeltas();

            System.out.println("average: " + average);

            int yPos = (int) lastY;

            //Costruisce la stringa
            if (yPos != 0 && (yPos < average || yPos > -average)) {
                String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));

                //Invia la stringa
                //commandManager.sendCommand(message);
                System.out.println("sending message: " + message);
            }
        }

    }

    public void checkMovementControl() {
        float pitchValue = helper.getPitch(helper.getRightHand());
        float rollValue = helper.getRoll(helper.getRightHand());
        float yawValue = helper.getYaw(helper.getRightHand());

        if (pitchValue != 180 && pitchValue != 0.0) {
            System.out.println("pitch: " + pitchValue);
        }

        if (yawValue != 180 && yawValue != 0.0) {
            System.out.println("yaw: " + yawValue);
        }

        if (rollValue != 180 && rollValue != 0.0) {
            System.out.println("roll: " + rollValue);
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

//                checkMovementControl();
            }
        }

        System.out.println("controller not connected");
    }

}
