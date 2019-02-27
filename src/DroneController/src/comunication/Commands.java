package comunication;

/**
 * This class describes a command that a DJI Tello can read and execute.
 * @author Luca Di Bello
 */
public abstract class Commands {
    
    //Complete commands
    public static final String ENABLE_COMMANDS = "command";
    public static final String TAKEOFF = "takeoff";
    public static final String LAND = "land";
    public static final String STREAM_VIDEO_ON = "streamon";
    public static final String STREAM_VIDEO_OFF = "streamoff";
    public static final String EMERGENCY = "emergency";
    
    //Semi commands -> Keywords used in complex commands
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String FORWARD = "forward";
    private static final String BACK = "BACK";
    
    
    //Movimenti possibili
    public static String up(float cmDistance){
        return String.format("%s %d",UP,cmDistance);
    }
    
    public static String down(float cmDistance){
        return String.format("%s %d",DOWN,cmDistance);
    }
    
    public static String left(float cmDistance){
        return String.format("%s %d",LEFT,cmDistance);
    }
    
    public static String right(float cmDistance){
        return String.format("%s %d",RIGHT,cmDistance);
    }
    
    public static String forward(float cmDistance){
        return String.format("%s %d",FORWARD,cmDistance);
    }
    
    public static String back(float cmDistance){
        return String.format("%s %d",BACK,cmDistance);
    }
}
