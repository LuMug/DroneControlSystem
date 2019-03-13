package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import comunication.Commands;
import comunication.CommandManager;
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

    private static CommandManager commandManager = new CommandManager();
    private FrameHelper helper;
    private Controller controller;
    private List<Float> deltas = new ArrayList<Float>();

    private static final float STEP = 10f;

    private static final int AVERAGE_POINTS = 10;

    public DroneController() {
        controller = new Controller();
        controller.addListener(this);
        helper = new FrameHelper();
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
        helper.setFrame(controller.frame());

        float altitude = helper.getHandZ(helper.getLeftHand(helper.getFrame()));
        deltas.add(helper.getDeltaZ());

        if (deltas.get(deltas.size() - 1) > getAverageDeltas() || deltas.get(deltas.size() - 1) < -getAverageDeltas()) {
//            System.out.println("movement detected: " + altitude);
//            System.out.println("average: " + getAverageDeltas());
            sendUpCommand((float)0.5);

        }

    }

    public float getAverageDeltas() {
        float tot = 0;
        for (Float delta : deltas) {
            tot += Math.abs(delta);
        }
        return tot / deltas.size();
    }

    public float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    @Override
    public void run() {
        System.out.println("reading");
        try {
            System.in.read();

        } catch (IOException ex) {
            System.err.println("IOException io.read:" + ex.getMessage());
        }
        System.out.println("finished reading");
    }

}
