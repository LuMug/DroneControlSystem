/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comunication;

/**
 * This class describes a command that a DJI Tello can read and execute.
 * @author Luca Di Bello
 */
public abstract class CommandBuilder {
    public static final String ENABLE_COMMANDS = "command";
    public static final String TAKEOFF = "takeoff";
    public static final String LAND = "land";
    public static final String STREAM_VIDEO_ON = "streamon";
    public static final String STREAM_VIDEO_OFF = "streamoff";
    public static final String EMERGENCY = "emergency";
    
    //Movimenti possibili
    public static String up(int cmDistance){
        return String.format("up %d", cmDistance);
    }
    
    public static String down(int cmDistance){
        return String.format("down %d", cmDistance);
    }
    
    public static String left(int cmDistance){
        return String.format("left %d", cmDistance);
    }
    
    public static String right(int cmDistance){
        return String.format("right %d", cmDistance);
    }
    
    public static String forward(int cmDistance){
        return String.format("forward %d", cmDistance);
    }
    
    public static String back(int cmDistance){
        return String.format("back %d", cmDistance);
    }
}
