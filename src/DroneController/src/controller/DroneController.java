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
import reader.LeapMotionReaderListener;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener {

    private static CommandManager commandManager = new CommandManager();
//    private LeapMotionReader leapReader;
    private Frame frame;
    private Controller controller;

    private static final float STEP = 10f;
    
    
    
    public synchronized void setFrame(Frame frame) {
        this.frame = frame;
    }

    public synchronized Frame getFrame() {
        return frame;
    }

    public static void sendUpCommand(float distance) {
        commandManager.sendCommand(up(distance));
    }
    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        Controller controller = new Controller();
        DroneController mineController = new DroneController();
        controller.addListener(mineController);
        try {
            System.in.read();
        } catch (IOException ex) {
            System.out.println("err");
        }
    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {

        setFrame(controller.frame());
        System.out.println("frame received: " + getFrame());
        
    }

    

    public static float translateInput(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }

    public DroneController() {
//        leapReader = new LeapMotionReader();
    }

}
