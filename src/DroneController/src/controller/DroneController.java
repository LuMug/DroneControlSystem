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
import reader.LeapMotionReaderListener;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Listener {

    private static CommandManager commandManager = new CommandManager();
    private static LeapMotionReader leapReader;
    private static Frame frame;
    private Controller controller;

    private static final float STEP = 10f;
    private static final int AVERAGE_POINTS = 10;

    public DroneController(){
        controller = new Controller();
        controller.addListener(this);
        
        try {
            System.in.read();
        } catch (IOException ex) {
            System.err.println("IOException io.read:" + ex.getMessage());
        }
    }        

    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        DroneController dc = new DroneController();
    }

    
    public static float translateAltitude(float altitude, float step) {
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }
    //right hand altitude
    public static float getAverageAltitude(int points){
        int total = 0;

        for(int i = 0; i < points; i++){
            Hand rHand = leapReader.getRightHand(getFrame());
            
            if(rHand != null){
                total += leapReader.getHandY(rHand);
            }
            else{
                //Reset
                i--;
            }
        }

        return total/points;
    }
    
    
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
        setFrame(controller.frame());
        //System.out.println("Controller: " +controller.frame().currentFramesPerSecond());
        //System.out.println("Frame: " + getFrame().currentFramesPerSecond());
        
        System.out.println(getFrame().hands().rightmost().palmPosition().getY());
        //System.out.println(leapReader.getHandY(getFrame().hands().rightmost()));
    } 
    
    public synchronized void setFrame(Frame frame) {
        this.frame = frame;
    }

    public static synchronized Frame getFrame() {
        return frame;
    }
}
