package communication;

import settings.FlipCommand;

/**
 * This class describes a command that a DJI Tello can read and execute.
 * All of the commands are made and formatted as described in the tello sdk.
 * @author Luca Di Bello
 */
public abstract class Commands {
    
    // <editor-fold desc="Complete commands">
    
    /**
     * Command used to enable the command execution.
     */
    public static final String ENABLE_COMMANDS = "command";
    
    /**
     * Command used to takeoff the drone.
     */
    public static final String TAKEOFF = "takeoff";
    
    /**
     * Command used to land the drone.
     */
    public static final String LAND = "land";
    
    /**
     * Command used to enable the video streaming.
     */
    public static final String STREAM_VIDEO_ON = "streamon";
    
    /**
     * Command used to disable the video streaming.
     */
    public static final String STREAM_VIDEO_OFF = "streamoff";
    
    /**
     * Command used stop all the rotors on the drone (emergency landing).
     */
    public static final String EMERGENCY = "emergency";
    // </editor-fold>

    // <editor-fold desc="Semi Commands">
    //Movements
    /**
     * Command used to go up.
     */
    private static final String UP = "up";
    
    /**
     * Command used to go down.
     */
    private static final String DOWN = "down";
    
    /**
     * Command used to go left.
     */
    private static final String LEFT = "left";
    
    /**
     * Command used to go right.
     */
    private static final String RIGHT = "right";
    
    /**
     * Command used to go forward.
     */
    private static final String FORWARD = "forward";
    
    /**
     * Command used to go back.
     */
    private static final String BACK = "back";
    
    /**
     * Command used to rotate clockwise.
     */
    private static final String ROTATE_CLOCKWISE = "cw";
    
    /**
     * Command used to go rotate counter clockwise.
     */
    private static final String ROTATE_COUNTER_CLOCKWISE = "ccw";
    
    /**
     * Command used to flip (loop).
     */
    private static final String FLIP = "flip";
    
    /**
     * Command used to go to a specific location in a [x,y,z] space.
     */
    private static final String GO = "go";
    
    /**
     * Command used to curve between 2 points in the space.
     */
    private static final String CURVE = "curve";
    
    //Setters
    
    /**
     * Command used to set the flight speed.
     */
    private static final String SPEED = "speed";
    
    /**
     * Command used to send a RC control to the drone.
     */
    private static final String RC = "rc";
    
    /**
     * Command used for 2 principals scopes:
     * <ul>
     *  <li>Set a wifi network where the drone will recieve and send data</li>
     *  <li>Get the Signal-to-Noise Ratio between the drone and the wifi beacon</li>
     * </ul>
     */
    private static final String WIFI = "wifi";
    
    //Readers
    
    /**
     * Command used to get the flight speed stored in the drone memory.
     */
    private static final String GET_SPEED = "speed?";
    
    /**
     * Command used to get the drone battery.
     */
    private static final String GET_BATTERY = "battery?";
    
    /**
     * Command used to get the current flight time.
     */
    private static final String GET_TIME = "time?";
    
    /**
     * Command used to get the current drone altitude.
     */
    private static final String GET_HEIGHT = "height?";
    
    /**
     * Command used to get the ambient temperature.
     */
    private static final String GET_TEMPERATURE = "temp?";
    
    /**
     * Command used to get the IMU attitude data.
     */
    private static final String GET_ATTITUDE = "attitude?";
    
    /**
     * Command used to get the current atmospheric pressure.
     */
    private static final String GET_BAROMETER = "baro?";
    
    /**
     * Command used to get the current drone acceleration.
     */
    private static final String GET_ACCELERATION = "acceleration?";
    
    /**
     * Command used to get the distance value from TOF (Time-Of-Flight).
     */
    private static final String GET_TOF = "tof?";
    
    /**
     * TODO: Read SDK.
     */
    private static final String GET_WIFI_SNR = "wifi?";
    // </editor-fold>
    
    // <editor-fold desc="Movements">
    //Movimenti base
    
    /**
     * This method returns the "up" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go upwards.
     * @return The formatted command.
     */
    public static String up(int cmDistance){
        return String.format("%s %d",UP,cmDistance);
    }
    
    /**
     * This method returns the "down" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go downwards.
     * @return The formatted command.
     */
    public static String down(int cmDistance){
        return String.format("%s %d",DOWN,cmDistance);
    }
    
    /**
     * This method returns the "left" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go to the left.
     * @return The formatted command.
     */
    public static String left(int cmDistance){
        return String.format("%s %d",LEFT,cmDistance);
    }
    
    /**
     * This method returns the "right" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go to the left.
     * @return The formatted command.
     */
    public static String right(int cmDistance){
        return String.format("%s %d",RIGHT,cmDistance);
    }
    
    /**
     * This method returns the "forward" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go forward.
     * @return The formatted command.
     */
    public static String forward(int cmDistance){
        return String.format("%s %d",FORWARD,cmDistance);
    }
    
    /**
     * This method returns the "back" command formatted to be readable by the drone.
     * @param cmDistance Distance by which the drone will go backwards.
     * @return The formatted command.
     */
    public static String back(int cmDistance){
        return String.format("%s %d",BACK,cmDistance);
    }
    
    //Rotazioni
    
    /**
     * This method returns the "rotate clockwise" command formatted to be readable by the drone.
     * @param degrees Degrees by which the drone will rotate.
     * @return The formatted command.
     */
    public static String rotateClockwise(int degrees){
        if(degrees > 0 && degrees <= 3600){
            return String.format("%s %d",ROTATE_CLOCKWISE,degrees);
        }
        else{
            throw new IllegalArgumentException("degrees range: [1 - 3600]");
        }
    }
    
    /**
     * This method returns the "rotate counterclockwise" command formatted to be readable by the drone.
     * @param degrees Degrees by which the drone will rotate counterclockwise.
     * @return The formatted command.
     */
    public static String rotateCounterClockwise(int degrees){
        if(degrees > 0 && degrees <= 3600){
            return String.format("%s %d",ROTATE_COUNTER_CLOCKWISE,degrees);
        }
        else{
            throw new IllegalArgumentException("degrees range: [1 - 3600]");
        }
    }
    
    //Flip
    
    /**
     * This method returns the "flip" command formatted to be readable by the drone.
     * @param flip Direction which by the drone will flip (loop).
     * @return The formatted command.
     */
    public static String flip(FlipCommand flip){
        return String.format("%s %s",FLIP,flip.getValue());
    }
    
    //Go to with specified Speed
    /**
     * This method returns the "go" command formatted to be readable by the drone.
     * @param x X coordinate of the point of arrival
     * @param y Y coordinate of the point of arrival
     * @param z Z coordinate of the point of arrival
     * @param speed Speed which will be used by the drone to go to the arrive Point
     * @return The formatted command.
     */
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
    
    /**
     * This method returns the "curve" command formatted to be readable by the drone.
     * @param x1 X coordinate of the first point of the curve
     * @param y1 Y coordinate of the first point of the curve
     * @param z1 Z coordinate of the first point of the curve
     * @param x2 X coordinate of the second point of the curve
     * @param y2 Y coordinate of the second point of the curve
     * @param z2 Z coordinate of the second point of the curve
     * @param speed Speed which will be used by the drone to do the curve between the two points.
     * @return The formatted command.
     */
    public static String curve(int x1,int y1,int z1, int x2, int y2, int z2, int speed){
        //DA IMPLEMENTARE
        return "WORK IN PROGRESS";
    }
    
    // </editor-fold>

    // <editor-fold desc="Setters">
    
    /**
     * This method returns the "speed" command formatted to be readable by the drone.
     * @param speed The speed which will be set on the drone.
     * @return The formatted command.
     */
    public String setSpeed(int speed){
        if(speed >= 10 && speed <= 100){
            return String.format("%s %d",SPEED,speed);
        }
        else{
            throw new IllegalArgumentException("speed range: [10-100]");
        }
    }
    
    /**
     * This method returns the "rc" command formatted to be readable by the drone.
     * @param a Move left or right. Range: [-100,100]
     * @param b Move forward or backwards. Range: [-100,100]
     * @param c Move upwards or downwards. Range: [-100,100]
     * @param d Set yaw. Range: [-100,100]
     * @return The formatted command.
     */
    public String rc(int a, int b, int c, int d){
        return "WORK IN PROGRESS";
    }
    
    /**
     * This method returns the "wifi" command formatted to be readable by the drone.
     * @param ssid Wifi network SSID.
     * @param psw Wifi network password.
     * @return The formatted command.
     */
    public String setWifi(String ssid, String psw){
        return String.format("%s %s %s",WIFI,ssid,psw);
    }
    // </editor-fold>
    
    // <editor-fold desc="Readers">
    /**
     * This method returns the "speed?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getSpeed(){
        return String.format("%s",GET_SPEED);
    }
    
    /**
     * This method returns the "battery?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getBattery(){
        return String.format("%s",GET_BATTERY);
    }
    
    /**
     * This method returns the "time?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getTime(){
        return String.format("%s",GET_TIME);
    }
    
    /**
     * This method returns the "height?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getHeight(){
        return String.format("%s",GET_HEIGHT);
    }
    
    /**
     * This method returns the "temperature?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getTemperature(){
        return String.format("%s",GET_TEMPERATURE);
    }
    
    /**
     * This method returns the "attitude?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getAttitude(){
        return String.format("%s",GET_ATTITUDE);
    }
    
    /**
     * This method returns the "baro?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getBarometer(){
        return String.format("%s",GET_BAROMETER);
    }
    
    /**
     * This method returns the "acceleration?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getAcceleration(){
        return String.format("%s",GET_ACCELERATION);
    }
    
    /**
     * This method returns the "tof?" getter command formatted to be readable by the drone.
     * @return The formatted command.
     */
    public static String getTof(){
        return String.format("%s",GET_TOF);
    }
    
    /**
     * This method returns the "wifi?" getter command formatted to be readable by the drone
     * @return The formatted command.
     */
    public static String getWifiSnr(){
        return String.format("%s",GET_WIFI_SNR);
    }
    // </editor-fold>
}