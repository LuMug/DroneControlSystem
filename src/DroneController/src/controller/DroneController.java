package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import communication.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager;
    private FrameHelper helper;
    private Controller controller;
    private List<Float> deltas = new ArrayList<Float>();

    private static final float STEP = 10f;

    private static final int AVERAGE_POINTS = 10;

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
        System.out.println("Connected");
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

    @Override
    public void run() {
        System.out.println("reading");
//        try {
//            System.in.read();
//
//        } catch (IOException ex) {
//            System.err.println("IOException io.read:" + ex.getMessage());
//        }

        while (controller.isConnected()) {
            Frame frame = controller.frame();
            helper.setFrame(frame);
//            System.out.println("frame: " + frame.id());
            if (helper.getFrame().id() != helper.getLastFrame().id()) {
//            int altitude = helper.getHandY(helper.getLeftHand(helper.getFrame()));
                float lastY = helper.getDeltaY();
                if (deltas.size() < 10) {
                    deltas.add(lastY);
                } else {
                    shiftDeltas();
                    deltas.set(0, lastY);

                }
                float average = getAverageDeltas();

                if ((lastY > average || lastY < -average) && average > 0.1) {
                    System.out.println("movement detected: " + average);

                    String message = lastY > 0 ? Commands.up((int)lastY) : Commands.down((int)lastY);
                    System.out.println("sending message: " + message);
//                commandManager.sendCommand(message);

                }
            }
        }
        System.out.println("finished reading");
    }

}
