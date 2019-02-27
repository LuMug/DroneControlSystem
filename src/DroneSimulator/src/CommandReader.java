

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
    
    public boolean commandExists(String command){
        if(command.charAt(command.length() - 1) == '?' || !command.contains(" ")){
            //Get command
            for(String s:GET_COMMANDS){
                if(command.equals(s)){
                    switch(s){
                        case "speed?":
                            getSpeed();
                            break;
                        case "battery?":
                            getBattery();
                            break;
                        case "time?":
                            getTime();
                            break;
                        case "height?":
                            getHeight();
                            break;
                        case "temp?":
                            getTemperature();
                            break;
                        case "attitude?":
                            getAttitude();
                            break;
                        case "baro?":
                            getBarometer();
                            break;
                        case "acceleration?":
                            getAcceleration();
                            break;
                        case "tof?":
                            getTof();
                            break;
                        case "wifi?":
                            getWifi();
                            break;
                    }
                    return true;
                }
            }
            for(String s:GUIDE_COMMANDS){
                if(command.equals(s)){
                    switch(s){
                        case "takeoff":
                            takeoff();
                            break;
                        case "land":
                            land();
                            break;
                        case "streamon":
                            streamon();
                            break;
                        case "streamoff":
                            streamoff();
                            break;
                        case "emergency":
                            emergency();
                            break;
                    }
                    return true;
                }
            }
        }else{
            //Other command
            String[] splittedCommand = command.split(" ");
            if(splittedCommand.length > 1 && splittedCommand.length < 9){
                for(String s:COMMANDS){
                    if(splittedCommand[0].equals(s)){
                        try{
                            switch(splittedCommand[0]){
                                case "up":
                                    if(splittedCommand.length == 2){
                                        up(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "down":
                                    if(splittedCommand.length == 2){
                                        down(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "left":
                                    if(splittedCommand.length == 2){
                                        left(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "right":
                                    if(splittedCommand.length == 2){
                                        right(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "forward":
                                    if(splittedCommand.length == 2){
                                        forward(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "back":
                                    if(splittedCommand.length == 2){
                                        back(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "cw":
                                    if(splittedCommand.length == 2){
                                        cw(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "ccw":
                                    if(splittedCommand.length == 2){
                                        ccw(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "flip":
                                    if(splittedCommand.length == 2){
                                        flip(Integer.parseInt(splittedCommand[1]));
                                        return true;
                                    }
                                    break;
                                case "go":
                                    if(splittedCommand.length == 2){
                                        go(
                                            Integer.parseInt(splittedCommand[1]),
                                            Integer.parseInt(splittedCommand[2]),
                                            Integer.parseInt(splittedCommand[3]),
                                            Integer.parseInt(splittedCommand[4])
                                        );
                                        return true;
                                    }
                                    break;
                                    
                            }
                            return false;
                        }catch(NumberFormatException nfe){
                            System.err.println("Error while parsing parameters: " + nfe.getMessage());
                        }
                    }
                }    
            }
        }
        return false;
    }
    
    // ------------------------- GUIDE COMMANDS -------------------------
    
    public static void takeoff(){
        
    }
    
    public static void land(){
        
    }
    
    public static void streamon(){
        
    }
    
    public static void streamoff(){
        
    }
    
    public static void emergency(){
        
    }
    
    // ------------------------- COMMANDS -------------------------
    
    public static void up(int distance){
        
    }
    
    public static void down(int distance){
        
    }
    
    public static void left(int distance){
        
    }
    
    public static void right(int distance){
        
    }
    
    public static void forward(int distance){
        
    }
    
    public static void back(int distance){
        
    }
    
    public static void cw(int distance){
        
    }
    
    public static void ccw(int distance){
        
    }
    
    public static void flip(int distance){
        
    }
    
    public static void go(int x, int y, int z, int speed){
        
    }
    
    public static void curve(int x1, int y1, int z1, int x2, int y2, int z2, int speed){
        
    }
    
    // ------------------------- SET COMMANDS -------------------------
    
    public static void speed(int speed){
        
    }
    
    public static void rc(int a, int b, int c, int d){
        
    }
    
    public static void wifiSsid(String password){
        
    }
    
    // ------------------------- GET COMMANDS -------------------------
    
    public static int getSpeed(){
        return 0;
    }
    
    public static int getBattery(){
        return 0;
    }
    
    public static int getTime(){
        return 0;
    }
    
    public static int getHeight(){
        return 0;
    }
    
    public static int getTemperature(){
        return 0;
    }
    
    public static int getAttitude(){
        
        //Not sure about the return type.
        //On the SDK it's 'pitch roll yaw'.
        //CONSULT TELLO STATE EXPLANATION
        
        return 0;
    }
    
    public static int getBarometer(){
        
        //Not sure about the return type.
        //On the SDK it's 'get barometer value (m)', not specified.
        //CONSULT TELLO STATE EXPLANATION
        
        return 0;
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
}
