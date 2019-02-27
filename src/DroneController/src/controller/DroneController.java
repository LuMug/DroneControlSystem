package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import comunication.Commands;
import comunication.CommandManager;
import reader.LeapMotionReader;
import reader.LeapMotionReaderListener;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener {

    private static CommandManager commandManager = new CommandManager();
    private LeapMotionReader leapReader;
    private Frame frame;
    private Controller controller;

    private static final float STEP = 10f;

//    public static void sendUpCommand(float distance) {
//        commandManager.sendCommand(up(distance));
//    }

    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        Controller controller = new Controller();
        //Reads leapmotion data and sends it to the drone
        while(true){
<<<<<<< HEAD
            float altitudeCm = translateAltitude(getAverageAltitude(AVERAGE_POINTS), STEP);
           
            if(altitudeCm > 0){
                System.out.println("Up: " + altitudeCm);
            }
            else{
                System.out.println("Down: " + altitudeCm);
            }
=======
            Frame frame = controller.frame();
            System.out.println("frame: " + frame);
>>>>>>> fbefed35e7b7eca7ed908b9e0a3931c03c81f2c6
        }
    }
    
    
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
        
        setFrame(controller.frame());
        System.out.println("frame received: " + getFrame());
    }
    public synchronized void setFrame(Frame frame) {
        this.frame = frame;
    }

    public synchronized Frame getFrame() {
        return frame;
    }

    public static float translateInput(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }
<<<<<<< HEAD
    
    //right hand altitude
    public static float getAverageAltitude(int points){
        int total = 0;
        
        for(int i = 0; i < points; i++){
            Hand rHand = leapReader.getRightHand();
            
            if(rHand != null){
                total += leapReader.getHandY(rHand);
            }
            else{
                //Reset
                i--;
            }
        }
        
        return total/points;
=======


    public DroneController() {
//        leapReader = new LeapMotionReader();
>>>>>>> fbefed35e7b7eca7ed908b9e0a3931c03c81f2c6
    }
    
}
