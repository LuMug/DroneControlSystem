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
    
    
    public static void sendUpCommand(float distance){
        commandManager.sendCommand(up(distance));
    }
    
    public static void main(String[] args) {
        System.out.println("Started Controller :)");
        
        //Reads leapmotion data and sends it to the drone
        while(true){
            Frame frame = leapReader.getFrame();
            System.out.println(frame);
            
            /*
            if(righthand != null){
                float up = translateInput(leapReader.getHandY(righthand), STEP);
                
                //sendUpCommand(translateInput(leapReader.getHandY(righthand), STEP));
                System.out.println("Up: " + up);
            }   
            */
        }
    }
    
    public static float translateInput(float altitude, float step){
        //MAX 60 CM
        //Punto 0 -> 30 CM
        float translated = (altitude - 30) % step;
        return translated;
    }
}
