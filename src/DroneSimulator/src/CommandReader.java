

/*
 * The MIT License
 *
 * Copyright 2019 jarinaser.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * La classe CommandReader ha lo scopo di ricevere i metodi mandati via pacchetto
 * attraverso i quali muoverà il drone nel modo corretto.
 * 
 * @author Jari Näser
 * @version 22.02.2019 - xx.xx.xxxx
 */
public class CommandReader{
    
    private static Simulator simulator;
    private static int speed;
    private static String ssid;
    private static String password;
    private static final int BATTERY_PERCENTAGE = 100;
    private static final int ACTUAL_TEMPERATURE = 24;
    private static final int BAROMETER_PRESSURE = 1;
    private static long takeoffTime;

    public CommandReader(Simulator simulator){
        this.simulator = simulator;
    }
    
    private static final String[] COMMANDS = {
        "up",
        "down",
        "left",
        "right",
        "forward",
        "back",
        "cw",
        "ccw",
        "flip",
        "go",
        "curve",
        "speed",
        "rc",
        "wifi ssid"
    };
    
    private static final String[] GUIDE_COMMANDS = {
        "takeoff",
        "land",
        "streamon",
        "streamoff",
        "emergency"
    };
    
    private static final String[] GET_COMMANDS = {
        "speed?",
        "battery?",
        "time?",
        "height?",
        "temp?",
        "attitude?",
        "baro?",
        "acceleration?",
        "tof?",
        "wifi?"
    };
    
    public int getterCommandExists(String command){
        for(String s:GET_COMMANDS){
            if(command.equals(s)){
                switch(s){
                    case "speed?":
                        return getSpeed();
                    case "battery?":
                        return getBattery();
                    case "time?":
                        return getTime();
                    case "height?":
                        return getHeight();
                    case "temp?":
                        return getTemperature();
                    case "attitude?":
                        return getAttitude();
                    case "baro?":
                        return getBarometer();
                    case "acceleration?":
                        return getAcceleration();
                    case "tof?":
                        return getTof();
                    case "wifi?":
                        return getWifi();
                }
            }
        }
        return Integer.MIN_VALUE;
    }
    
    public boolean instructionCommandExists(String command){
        for(String s:GUIDE_COMMANDS){
            if(command.equals(s)){
                switch(s){
                    case "takeoff":
                        return takeoff();
                    case "land":
                        return land();
                    case "streamon":
                        return streamon();
                    case "streamoff":
                        return streamoff();
                    case "emergency":
                        return emergency();
                }
            }
        }
        return false;
    }
    
    public boolean commandExists(String command) throws InterruptedException{
        String[] splittedCommand = command.split(" ");
        if(splittedCommand.length > 1 && splittedCommand.length < 9){
            for(String s:COMMANDS){
                if(splittedCommand[0].equals(s)){
                    try{
                        switch(splittedCommand[0]){
                            case "up":
                                if(splittedCommand.length == 2){
                                    return up(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "down":
                                if(splittedCommand.length == 2){
                                    return down(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "left":
                                if(splittedCommand.length == 2){
                                    return left(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "right":
                                if(splittedCommand.length == 2){
                                    return right(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "forward":
                                if(splittedCommand.length == 2){
                                    return forward(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "back":
                                if(splittedCommand.length == 2){
                                    return back(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "cw":
                                if(splittedCommand.length == 2){
                                    return cw(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "ccw":
                                if(splittedCommand.length == 2){
                                    return ccw(Integer.parseInt(splittedCommand[1]));
                                }
                                break;
                            case "flip":
                                if(splittedCommand.length == 2){
                                    return flip(splittedCommand[1].charAt(0));
                                }
                                break;
                            case "go":
                                if(splittedCommand.length == 2){
                                    return go(
                                        Integer.parseInt(splittedCommand[1]),
                                        Integer.parseInt(splittedCommand[2]),
                                        Integer.parseInt(splittedCommand[3]),
                                        Integer.parseInt(splittedCommand[4])
                                    );
                                }
                                break;
                        } 
                    }catch(NumberFormatException nfe){
                        System.err.println("Error while parsing parameters: " + nfe.getMessage());
                    }
                }
            }    
        }
        return false;
    }
    
    // ------------------------- GUIDE COMMANDS -------------------------
    
    public static boolean takeoff(){
        //From 0,0,0
        takeoffTime = System.currentTimeMillis();
        return true;
    }
    
    public static boolean land(){
        //Go back to 0,0,0
        return true;
    }
    
    public static boolean streamon(){
        return true;
    }
    
    public static boolean streamoff(){
        return true;
    }
    
    public static boolean emergency(){
        return true;
    }
    
    // ------------------------- COMMANDS -------------------------
    
    public static boolean up(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setY(simulator.getY() + distance);
            return true;
        }
        return false;
    }
    
    public static boolean down(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setY(simulator.getY() - distance);
            return true;
        }
        return false;
    }
    
    public static boolean left(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setX(simulator.getX() - distance);
            return true;
        }
        return false;
    }
    
    public static boolean right(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setX(simulator.getX() + distance);
            return true;
        }
        return false;
    }
    
    public static boolean forward(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setZ(simulator.getZ() - distance);
            return true;
        }
        return false;
    }
    
    public static boolean back(int distance){
        if(isInsideRange(distance, 20, 500)){
            simulator.setZ(simulator.getZ() + distance);
            return true;
        }
        return false;
    }
    
    public static boolean cw(int rotation){
        if(isInsideRange(rotation, 1, 3600)){
            rotation /= 10;
            simulator.setRotationX(simulator.getRotationX() + rotation);
            return true;
        }
        return false;
    }
    
    public static boolean ccw(int rotation){
        if(isInsideRange(rotation, 1, 3600)){
            rotation /= 10;
            simulator.setRotationX(simulator.getRotationX() - rotation);
            return true;
        }
        return false;
    }
    
    public static boolean flip(char where) throws InterruptedException{
        if(where == 'l' || where == 'r' || where == 'f' || where == 'b'){
            switch(where){
                case 'l':
                    //Left flip on X axis
                    for(int i = simulator.getRotationX(); i < simulator.getRotationX() + 360; i += 36){
                        simulator.setRotationX(simulator.getRotationX() + i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'r':
                    //Right flip on X axis
                    for(int i = simulator.getRotationX(); i < simulator.getRotationX() + 360; i += 36){
                        simulator.setRotationX(simulator.getRotationX() - i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'f':
                    //Left flip on Z axis
                    for(int i = simulator.getRotationZ(); i < simulator.getRotationZ() + 360; i += 36){
                        simulator.setRotationZ(simulator.getRotationZ() + i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'b':
                    //Left flip on Z axis
                    for(int i = simulator.getRotationZ(); i < simulator.getRotationZ() + 360; i += 36){
                        simulator.setRotationZ(simulator.getRotationZ() - i);
                        Thread.sleep(100);
                    }
                    return true;
            }
        }
        return false;
    }
    
    public static boolean go(int x, int y, int z, int speed) throws InterruptedException{
        if(isInsideRange(x, 20, 500) || isInsideRange(x, -500, -20) &&
                isInsideRange(y, 20, 500) || isInsideRange(y, -500, -20) &&
                isInsideRange(z, 20, 500) || isInsideRange(z, -500, -20)){
            if(speed >= 10 && speed <= 100){
                
                int biggest;
                if(unsign(x) > unsign(y)){
                    biggest = x;
                }else if(unsign(y) > unsign(z)){
                    biggest = y;
                }else{
                    biggest = z;
                }
                
                int delay = biggest / speed;
                
                for(int i = 0; i < delay; i++){
                    simulator.setX(simulator.getX() + x/delay);
                    simulator.setY(simulator.getY() + y/delay);
                    simulator.setZ(simulator.getZ() + z/delay);
                    Thread.sleep(delay);
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean curve(int x1, int y1, int z1, int x2, int y2, int z2, int speed) throws InterruptedException{
        if(isInsideRange(x1, 20, 500) || isInsideRange(x1, -500, -20) &&
                isInsideRange(y1, 20, 500) || isInsideRange(y1, -500, -20) &&
                isInsideRange(z1, 20, 500) || isInsideRange(z1, -500, -20) &&
                isInsideRange(x2, 20, 500) || isInsideRange(x2, -500, -20) &&
                isInsideRange(y2, 20, 500) || isInsideRange(y2, -500, -20) &&
                isInsideRange(z2, 20, 500) || isInsideRange(z2, -500, -20)){
            
            if(isInsideRange(speed, 10, 60)){
                go(x1, y1, z1, speed);
                go(x2, y2, z2, speed);
                return true;
            }
            
        }
        return false;
    }
    
    // ------------------------- SET COMMANDS -------------------------
    
    public static boolean speed(int value){
        if(isInsideRange(value, 10, 100)){
            speed = value;
            return true;
        }
        return false;
    }
    
    public static boolean rc(int a, int b, int c, int d){
        return true;
    }
    
    public static boolean wifi(String ssidValue, String passwordValue){
        if(passwordValue.length() > 0 && ssidValue.length() > 0){
            password = passwordValue;
            ssid = ssidValue;
            
            //Join network or do what needed.
            //Still to discuss and modify.
            
            return true;
        }
        return false;
    }
    
    // ------------------------- GET COMMANDS -------------------------
    
    public static int getSpeed(){
        return speed;
    }
    
    public static int getBattery(){
        return BATTERY_PERCENTAGE;
    }
    
    public static int getTime(){
        //In seconds
        return (int)((System.currentTimeMillis() - takeoffTime)/1000);
    }
    
    public static int getHeight(){
        return simulator.getX();
    }
    
    public static int getTemperature(){
        return ACTUAL_TEMPERATURE;
    }
    
    public static int getAttitude(){
        
        //Not sure about the return type.
        //On the SDK it's 'pitch roll yaw'.
        //CONSULT TELLO STATE EXPLANATION
        
        return 0;
    }
    
    public static int getBarometer(){
        //Return number is in Atmospheres (1 Atmosphere = 101325 Pascal)
        return BAROMETER_PRESSURE;
    }
    
    public static int getAcceleration(){
        
        //Not sure about the return type.
        //On the SDK it's 'get IMU angular acceleration data (0.001g)'
        //and it should return 'x y z'.
        //CONSULT TELLO STATE EXPLANATION
        
        return 0;
    }
    
    public static int getTof(){     
        return 0;
    }
    
    public static int getWifi(){   
        
        //Not sure about the return type.
        //On the SDK it's ' get Wi-Fi SNR' and it should return 'snr'.
        //CONSULT TELLO STATE EXPLANATION
        
        return 0;
    }
    
    // ------------------------- HELPER METHODS -------------------------
    
    public static int unsign(int value){
        return (value < 0)?value*-1:value;
    }
    
    public static boolean isInsideRange(int value, int min, int max){
        return (value <= min && value >= max);
    }
}
