package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import communication.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import settings.SettingsManager;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager;
    private static SettingsManager settingsManager;
    private FrameHelper helper;
    private Controller controller;
    private List<Float> deltas = new ArrayList<Float>();
    private final float DEAD_ZONE = 0.55f; 

    private static final float STEP = 10f;

    private static final int AVERAGE_POINTS = 10;

    public DroneController() {
        controller = new Controller();
        controller.addListener(this);
        helper = new FrameHelper();
        commandManager = new CommandManager();
        settingsManager = new SettingsManager();
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

    @Override
    public void run() {

        System.out.println("reading");

        commandManager.sendCommand(Commands.ENABLE_COMMANDS);
        while (controller.isConnected()) {
            Frame frame = controller.frame();
            helper.setFrame(frame);
            if (helper.getFrame().id() != helper.getLastFrame().id()) {
                float lastY = helper.getDeltaY();
                if (deltas.size() < 10) {
                    deltas.add(lastY);
                } else {
                    shiftDeltas();
                    deltas.set(0, lastY);

                }
                float average = getAverageDeltas();
                
                //Controlla media (per calcolare lo scarto)
                if ((lastY > average || lastY < -average) && (average < -this.DEAD_ZONE || average > this.DEAD_ZONE)) {
                    //Casta in integer
                    int yPos = (int) lastY;
                    System.out.println("yPos: " + yPos);
                    
                    //Costruisce la stringa
                    String message = yPos > 0 ? Commands.up(yPos) : Commands.down(Math.abs(yPos));
                    if (yPos != 0) {
                        //Invia la stringa
                        commandManager.sendCommand(message);
                    }
                }
            }
        }
        System.out.println("drone not connected");
    }

}
