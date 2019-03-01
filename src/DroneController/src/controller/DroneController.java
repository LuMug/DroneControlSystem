package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import comunication.Commands;
import comunication.CommandManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import reader.LeapMotionReader;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener implements Runnable {

    private static CommandManager commandManager = new CommandManager();
    private static LeapMotionReader leapReader;
    private Controller controller;

    private static final float STEP = 10f;

    private static final int AVERAGE_POINTS = 10;

    public DroneController() {
        controller = new Controller();
        controller.addListener(this);
        leapReader = new LeapMotionReader();
    }


    public static void sendUpCommand(float distance) {
        commandManager.sendCommand(Commands.up(distance));
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
        leapReader.setFrame(controller.frame());
        float altitude = leapReader.getHandZ(leapReader.getLeftHand());
        System.out.println(altitude);
        sendUpCommand(translateAltitude(altitude,STEP));
    }

    public float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    //right hand altitude
    public float getAverageAltitude(int points) {
        int total = 0;

        for (int i = 0; i < points; i++) {
            Hand rHand = leapReader.getRightHand();

            if (rHand != null) {
                total += leapReader.getHandY(rHand);
            } else {
                //Reset
                i--;
            }
        }

        return total / points;
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
