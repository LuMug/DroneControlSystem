package comunication;

import settings.FlipCommand;

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
    
    //Movements
    private static final String UP = "up";
    private static final String DOWN = "down";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String FORWARD = "forward";
    private static final String BACK = "back";
    private static final String ROTATE_CLOCKWISE = "cw";
    private static final String ROTATE_COUNTER_CLOCKWISE = "ccw";
    private static final String FLIP = "flip";
    private static final String GO = "go";
    private static final String CURVE = "curve";
    
    //Setters
    private static final String SPEED = "speed";
    private static final String RC = "rc";
    private static final String WIFI = "wifi";
    
    //Readers
    private static final String GET_SPEED = "speed?";
    private static final String GET_BATTERY = "battery?";
    private static final String GET_TIME = "time?";
    private static final String GET_HEIGHT = "height?";
    private static final String GET_TEMPERATURE = "temp?";
    private static final String GET_ATTITUDE = "attitude?";
    private static final String GET_BAROMETER = "baro?";
    private static final String GET_ACCELERATION = "acceleration?";
    private static final String GET_TOF = "tof?";
    private static final String GET_WIFI_SNR = "wifi?";
    
    // <editor-fold desc="Movements">
    
    //Movimenti base
    public static String up(int cmDistance){
        return String.format("%s %d",UP,cmDistance);
    }
    
    public static String down(int cmDistance){
        return String.format("%s %d",DOWN,cmDistance);
    }
    
    public static String left(int cmDistance){
        return String.format("%s %d",LEFT,cmDistance);
    }
    
    public static String right(int cmDistance){
        return String.format("%s %d",RIGHT,cmDistance);
    }
    
    public static String forward(int cmDistance){
        return String.format("%s %d",FORWARD,cmDistance);
    }
    
    public static String back(int cmDistance){
        return String.format("%s %d",BACK,cmDistance);
    }
    
    //Rotazioni
    public static String rotateClockwise(int degrees){
        if(degrees > 0 && degrees <= 3600){
            return String.format("%s %d",ROTATE_CLOCKWISE,degrees);
        }
        else{
            throw new IllegalArgumentException("degrees range: [1 - 3600]");
        }
    }
    
    public static String rotateCounterClockwise(int degrees){
        if(degrees > 0 && degrees <= 3600){
            return String.format("%s %d",ROTATE_COUNTER_CLOCKWISE,degrees);
        }
        else{
            throw new IllegalArgumentException("degrees range: [1 - 3600]");
        }
    }
    
    //Flip
    public static String flip(FlipCommand flip){
        return String.format("%s %s",FLIP,flip.getValue());
    }
    
    //Go to with specified Speed
    public static String go(int x, int y, int z, int speed){
        if((x >= 20 && x <= 500) && (y >= 20 && y <= 500) && 
                (z >= 20 && z<= 500) && (speed >= 10 && speed <= 100)){
                    return String.format("%s %d %d %d %d",GO,x,y,z,speed);
        }
        else{
            throw new IllegalArgumentException("x,y,z range: [20-500] <-> speed range: [10,100]");
        }
    }
    
    //Curve between current position and other two points with a specific speed
    public static String curve(int x1,int y1,int z1, int x2, int y2, int z2, int speed){
        //DA IMPLEMENTARE
        return "WORK IN PROGRESS";
    }
    
    // </editor-fold>

    // <editor-fold desc="Setters">
    public String setSpeed(int speed){
        if(speed >= 10 && speed <= 100){
            return String.format("%s %d",SPEED,speed);
        }
        else{
            throw new IllegalArgumentException("speed range: [10-100]");
        }
    }
    
    public String rc(int a, int b, int c, int d){
        return "WORK IN PROGRESS";
    }
    
    public String setWifi(String ssid, String psw){
        return String.format("%s %s %s",WIFI,ssid,psw);
    }
    // </editor-fold>
    
    // <editor-fold desc="Readers">
    public static String getSpeed(){
        return String.format("%s",GET_SPEED);
    }
    
    public static String getBattery(){
        return String.format("%s",GET_BATTERY);
    }
    
    public static String getTime(){
        return String.format("%s",GET_TIME);
    }
    
    public static String getHeight(){
        return String.format("%s",GET_HEIGHT);
    }
     
    public static String getTemperature(){
        return String.format("%s",GET_TEMPERATURE);
    }
    
    public static String getAttitude(){
        return String.format("%s",GET_ATTITUDE);
    }
    
    public static String getBarometer(){
        return String.format("%s",GET_BAROMETER);
    }
    
    public static String getAcceleration(){
        return String.format("%s",GET_ACCELERATION);
    }
    
    public static String getTof(){
        return String.format("%s",GET_TOF);
    }
    
    public static String getWifiSnr(){
        return String.format("%s",GET_WIFI_SNR);
    }
    // </editor-fold>
}    