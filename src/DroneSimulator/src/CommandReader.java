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
    
    /************
     * TO DO:.
     * 
     * - Add safety feature, drone lands after 15 seconds without any commands.
     * - On simulation add informations such as battery, flight time, 
     *   temperature, pressure and so on...
     */
    
    private final int TELLO_BATTERY_FLIGHT_TIME = 13;
    private final int ACTUAL_TEMPERATURE = 24;
    private final int BAROMETER_PRESSURE = 1;
    private Simulator simulator;
    private BatteryThread batteryThread;
    private int speed;
    private long takeoffTime;

    public CommandReader(Simulator simulator){
        this.simulator = simulator;
        takeoffTime = System.currentTimeMillis();
    }
    
    private final String[] COMMANDS = {
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
        "wifi"
    };
    
    private final String[] GUIDE_COMMANDS = {
        "takeoff",
        "land",
        "streamon",
        "streamoff",
        "emergency"
    };
    
    private final String[] GET_COMMANDS = {
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
                    case "baro?":
                        return getBarometer();
                    case "tof?":
                        return getTof();
                    case "wifi?":
                        return getWifi();
                }
            }
        }
        return Integer.MIN_VALUE;
    }
    
    public int[] getterCommandArrayExists(String command){
        for(String s:GET_COMMANDS){
            if(command.equals(s)){
                switch(s){
                    case "attitude?":
                        return getAttitude();     
                    case "acceleration?":
                        return getAcceleration();            
                }
            }
        }
        return new int[]{Integer.MIN_VALUE};
    }
    
    public boolean instructionCommandExists(String command) throws InterruptedException{
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
    
    public boolean takeoff() throws InterruptedException{
        //From 0,0,0
        this.batteryThread = new BatteryThread(this);
        this.batteryThread.start();
        go(0, 100, 0, 50);
        return true;
    }
    
    public boolean land() throws InterruptedException{
        go(simulator.getX(), 0, simulator.getZ(), 50);
        this.takeoffTime = 0;
        this.batteryThread.interrupt();
        return true;
    }
    
    public boolean streamon(){
        //VideoStream on, not implemented yet in the simulation.
        return false;
    }
    
    public boolean streamoff(){
        //VideoStream off, not implemented yet in the simulation.
        return false;
    }
    
    public boolean emergency() throws InterruptedException{
        go(simulator.getX(), 0, simulator.getZ(), 100);
        this.takeoffTime = 0;
        this.batteryThread.interrupt();
        return true;
    }
    
    // ------------------------- COMMANDS -------------------------
    
    public boolean up(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setY(simulator.getY() + distance);
            return true;
        }
        return false;
    }
    
    public boolean down(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setY(simulator.getY() - distance);
            return true;
        }
        return false;
    }
    
    public boolean left(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setX(simulator.getX() - distance);
            return true;
        }
        return false;
    }
    
    public boolean right(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setX(simulator.getX() + distance);
            return true;
        }
        return false;
    }
    
    public boolean forward(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setZ(simulator.getZ() - distance);
            return true;
        }
        return false;
    }
    
    public boolean back(int distance){
        if(isInsideRange(distance, 0, 500)){
            simulator.setZ(simulator.getZ() + distance);
            return true;
        }
        return false;
    }
    
    public boolean cw(int rotation){
        if(isInsideRange(rotation, 1, 3600)){
            rotation /= 10;
            simulator.setRoll(simulator.getRoll() + rotation);
            return true;
        }
        return false;
    }
    
    public boolean ccw(int rotation){
        if(isInsideRange(rotation, 1, 3600)){
            rotation /= 10;
            simulator.setRoll(simulator.getRoll() - rotation);
            return true;
        }
        return false;
    }
    
    public boolean flip(char where) throws InterruptedException{
        if(where == 'l' || where == 'r' || where == 'f' || where == 'b'){
            switch(where){
                case 'l':
                    //Left flip on X axis
                    for(int i = simulator.getRoll(); i < simulator.getRoll() + 360; i += 36){
                        simulator.setRoll(simulator.getRoll() + i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'r':
                    //Right flip on X axis
                    for(int i = simulator.getRoll(); i < simulator.getRoll() + 360; i += 36){
                        simulator.setRoll(simulator.getRoll() - i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'f':
                    //Left flip on Z axis
                    for(int i = simulator.getPitch(); i < simulator.getPitch() + 360; i += 36){
                        simulator.setPitch(simulator.getPitch() + i);
                        Thread.sleep(100);
                    }
                    return true;
                case 'b':
                    //Left flip on Z axis
                    for(int i = simulator.getPitch(); i < simulator.getPitch() + 360; i += 36){
                        simulator.setPitch(simulator.getPitch() - i);
                        Thread.sleep(100);
                    }
                    return true;
            }
        }
        return false;
    }
    
    public boolean go(int x, int y, int z, int speed) throws InterruptedException{
        if(isInsideRange(x, 20, 500) || isInsideRange(x, -500, -20) &&
                isInsideRange(y, 20, 500) || isInsideRange(y, -500, -20) &&
                isInsideRange(z, 20, 500) || isInsideRange(z, -500, -20) &&
                isInsideRange(speed, 10, 100)){
            
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
                    Thread.sleep(delay*1000);
                }
                return true;
            }
        return false;
    }
    
    public boolean curve(int x1, int y1, int z1, int x2, int y2, int z2, int speed) throws InterruptedException{
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
    
    public boolean speed(int value){
        if(isInsideRange(value, 10, 100)){
            speed = value;
            return true;
        }
        return false;
    }
    
    public boolean rc(int a, int b, int c, int d){
        /**
         * a: left/right
         * b: forward/backward
         * c: up/down
         * d: yaw
         */
        if(isInsideRange(a, -100, 100) && isInsideRange(b, -100, 100) 
                && isInsideRange(c, -100, 100) && isInsideRange(d, -100, 100)){
            if(a > -1){
                right(a);
            }else{
                left(a);
            }
            
            if(b > -1){
                forward(b);
            }else{
                back(b);
            }
            
            if(c > -1){
                up(c);
            }else{
                down(c);
            }
            
            if(d > -1){
                simulator.setYaw(simulator.getYaw() + d);
            }else{
                simulator.setYaw(simulator.getYaw() - d);
            }
            return true;
        }
        return false;
    }
    
    public boolean wifi(String ssidValue, String passwordValue){
        //Should join a network
        return false;
    }
    
    // ------------------------- GET COMMANDS -------------------------
    
    public int getSpeed(){
        return speed;
    }
    
    public int getBattery(){
        return ((TELLO_BATTERY_FLIGHT_TIME * 60 - getTime())/(TELLO_BATTERY_FLIGHT_TIME*60)*100);
    }
    
    public int getTime(){
        //In seconds
        return (int)((System.currentTimeMillis() - takeoffTime)/1000);
    }
    
    public int getHeight(){
        return simulator.getX();
    }
    
    public int getTemperature(){
        return ACTUAL_TEMPERATURE;
    }
    
    public int[] getAttitude(){
        return new int[]{
            simulator.getPitch(),
            simulator.getRoll(),
            simulator.getYaw()
        };
    }
    
    public int getBarometer(){
        //Return number is in Atmospheres (1 Atmosphere = 101325 Pascal)
        return BAROMETER_PRESSURE;
    }
    
    public int[] getAcceleration(){
        return new int[]{
            simulator.getX(),
            simulator.getY(),
            simulator.getZ()
        };
    }
    
    public int getTof(){   
        //Can't do it in the simulation, range imaging camera should be required.
        return Integer.MIN_VALUE;
    }
    
    public int getWifi(){   
        //Wifi not implemented yet.
        return Integer.MIN_VALUE;
    }
    
    // ------------------------- HELPER METHODS -------------------------
    
    public int unsign(int value){
        return Math.abs(value);
    }
    
    public boolean isInsideRange(int value, int min, int max){
        return (value >= min && value <= max);
    }
}
