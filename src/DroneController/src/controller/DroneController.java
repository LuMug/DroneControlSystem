package controller;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import comunication.Commands;
import comunication.CommandManager;
import reader.LeapMotionReader;

/**
 *
 * @author Luca Di Bello
 */
public class DroneController extends Commands{
    private static CommandManager commandManager = new CommandManager();
    private static LeapMotionReader leapReader = new LeapMotionReader();
    private static final float STEP = 10f;
    private static final int AVERAGE_POINTS = 10;
    
    public static void sendUpCommand(float distance){
        commandManager.sendCommand(up(distance));
    }
    
    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        
        //Reads leapmotion data and sends it to the drone
        while(true){
            float altitudeCm = translateAltitude(getAverageAltitude(AVERAGE_POINTS), STEP);
           
            if(altitudeCm > 0){
                System.out.println("Up: " + altitudeCm);
            }
            else{
                System.out.println("Down: " + altitudeCm);
            }
        }
    }
    
    //Right hand feature
    public static float translateAltitude(float altitude, float step){
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = ((altitude / 10) - 30) / step;
        return translated;
    }
    
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
    }
}
