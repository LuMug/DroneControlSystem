
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

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
 * The Simulator class receives all the requests and commands through a socket
 * listening on port 8889.
 * It filters the commands and reads, checks and forwards the content to 
 * CommandReader's various methods.
 * 
 * @author Jari Näser 
 * @version 15.02.2019
 */
public class Simulator{
    
    // ------------------------- ATTRIBUTES AND CONSTANTS -------------------------
    
    /**
     * Defines the listening port.
     */
    private final int PORT = 8889;
    
    /**
     * Defines the client's IP address.
     */
    private final String ADDRESS_TO_SEND = "192.168.43.246";
    
    /**
     * Defines the received packet's buffer size.
     */
    private final int BUFFER_SIZE = 64;
    
    /**
     * Simulator's CommandReader object.
     */
    private CommandReader commandReader;
    
    /**
     * Socket used to communicate between client and server.
     */
    private DatagramSocket socket;
    
    /**
     * Simulator's TelloChartFrame object.
     */
    private TelloChartFrame telloChartFrame;
    
    /**
     * Simulator's PacketReceivingCheckerThread thread.
     */
    private PacketReceivingCheckerThread prct;
    
    /**
     * Drone position on the X axis.
     */
    private int x = 0;
    
    /**
     * Drone position on the Y axis.
     */
    private int y = 0;
    
    /**
     * Drone position on the Z axis.
     */
    private int z = 0;
    
    /**
     * Drone roll.
     */
    private int roll = 0;
    
    /**
     * Drone yaw.
     */
    private int yaw = 0;
    
    /**
     * Drone pitch.
     */
    private int pitch = 0;
    
    // ------------------------- CONSTRUCTOR -------------------------
    
    public Simulator() throws SocketException, InterruptedException{
        this.commandReader = new CommandReader(this);
        this.telloChartFrame = new TelloChartFrame(x,y,z,pitch,yaw,roll);
        //Start Frames
        this.telloChartFrame.setVisible(true);
        //Start listening on socket
        this.socket = new DatagramSocket(PORT);
        this.startListening();
    }
    
    // ------------------------- SETTERS AND GETTERS -------------------------
    
    /**
     * Getter method for the x attribute.
     * @return Value of x.
     */
    public int getX(){
        return this.x;
    }
    
    /**
     * Setter method for the x attribute
     * @param x New value to set.
     */
    public void setX(int x){
        this.x = x;
        this.telloChartFrame.setPositionX(this.x);
    }
    
    /**
     * Getter method for the y attribute.
     * @return Value of y.
     */
    public int getY(){
        return this.y;
    }
    
    /**
     * Setter method for the y attribute
     * @param y New value to set.
     */
    public void setY(int y){
        this.y = y;
        this.telloChartFrame.setPositionY(this.y);
    }
    
    /**
     * Getter method for the z attribute.
     * @return Value of z.
     */
    public int getZ(){
        return this.z ;
    }
    
    /**
     * Setter method for the z attribute
     * @param z New value to set.
     */
    public void setZ(int z){
        this.z = z;
        this.telloChartFrame.setPositionZ(this.z);
    }
    
    /**
     * Getter method for the roll attribute.
     * @return Value of roll.
     */
    public int getRoll(){
        return this.roll;
    }
    
    /**
     * Setter method for the roll attribute
     * @param rotation New value to set.
     */
    public void setRoll(int rotation){
        this.roll = rotation%360;
        this.telloChartFrame.setRoll(this.roll);
    }
    
    /**
     * Getter method for the yaw attribute.
     * @return Value of yaw.
     */
    public int getYaw(){
        return this.yaw;
    }
    
    /**
     * Setter method for the yaw attribute
     * @param rotation New value to set.
     */
    public void setYaw(int rotation){
        this.yaw = rotation%360;
        this.telloChartFrame.setYaw(this.yaw);
    }
    
    /**
     * Getter method for the pitch attribute.
     * @return Value of pitch.
     */
    public int getPitch(){
        return this.pitch;
    }
    
    /**
     * Setter method for the pitch attribute
     * @param rotation New value to set.
     */
    public void setPitch(int rotation){
        this.pitch = rotation%360;
        this.telloChartFrame.setPitch(this.pitch);
    }
    
    // ------------------------- HELPER METHODS -------------------------
    
    /**
     * Method that sends OK response through socket.
     * @throws UnknownHostException Exception if the host is unknown.
     * @throws IOException Exception if there is a problem with the socket.
     */
    private void sendOK() throws UnknownHostException, IOException{
        byte[] response = "OK".getBytes();
        DatagramPacket packet = new DatagramPacket(
            response, 
            response.length, 
            InetAddress.getByName(ADDRESS_TO_SEND), 
            PORT
        );       
        socket.send(packet);
        System.out.println("Sent OK response.");
    }
    
    /**
     * Method that sends ERROR response through socket.
     * @throws UnknownHostException Exception if the host is unknown.
     * @throws IOException Exception if there is a problem with the socket.
     */
    private void sendERROR() throws UnknownHostException, IOException{
        byte[] response = "ERROR".getBytes();
        DatagramPacket packet = new DatagramPacket(
            response, 
            response.length, 
            InetAddress.getByName(ADDRESS_TO_SEND), 
            PORT
        );       
        socket.send(packet);
        System.err.println("Sent ERROR response.");
    }
    
    /**
     * Method that returns requested values to the client via getter commands.
     * @param values Requested values/data.
     * @throws UnknownHostException Exception if the host is unknown.
     * @throws IOException Exception if there is a problem with the socket.
     */
    private void returnValues(int[] values) throws UnknownHostException, IOException{
        //Converting int array to byte array
        byte[] response = new byte[values.length];
        for(int i = 0; i < response.length; i++){
            response[i] = (byte)values[i];
        }
        //Send packet
        DatagramPacket packet = new DatagramPacket(
            response, 
            response.length, 
            InetAddress.getByName(ADDRESS_TO_SEND), 
            PORT
        );       
        socket.send(packet);
        System.out.println("Sent return values: " + Arrays.toString(values));
    }
    
    // ------------------------- NETWORK METHODS -------------------------

    /**
     * Method that listens through the socket and calls the right method with
     * the received message/command.
     * @throws InterruptedException Exception if a thread gets interrupted.
     */
    private void startListening() throws InterruptedException{
        
        boolean droneIsConnected;
        
        //Create buffer array with right size
        byte[] buffer = new byte[BUFFER_SIZE];

        //Prepare packet to store the recived one
        DatagramPacket recivePacket = new DatagramPacket(buffer, buffer.length);

        System.out.println("Started listener on " + socket.getLocalSocketAddress());

        while(true){
            try{
                //Set packet size
                recivePacket.setData(new byte[buffer.length]);
                prct = new PacketReceivingCheckerThread(commandReader);
                prct.start();
                //Waiting for a Packet and to receive a datagram
                socket.receive(recivePacket);
                prct.interrupt();
                //Read Packet content
                String message = new String(recivePacket.getData()).trim();
                
                System.out.println("Recieved packet, content: " + message);
                
                if((message).equals("command")){
                    sendOK();
                    droneIsConnected = true;
                    while(droneIsConnected){
                        //Set packet size
                        recivePacket.setData(new byte[buffer.length]);
                        prct = new PacketReceivingCheckerThread(commandReader);
                        prct.start();
                        //Waiting for a Packet and to receive a datagram
                        socket.receive(recivePacket);
                        prct.interrupt();
                        //Read Packet content
                        String command = new String(recivePacket.getData()).trim();
                        
                        System.out.println("Recieved packet, content: " + command);
                        
                        if(command != null){
                            if(command.charAt(command.length() - 1) == '?'){
                                //Getter commands 
                                if(commandReader.getterCommandExists(command) != Integer.MIN_VALUE){
                                    sendOK();
                                    returnValues(new int[]{commandReader.getterCommandExists(command)});
                                }else{
                                    if(commandReader.getterCommandArrayExists(command) != new int[]{Integer.MIN_VALUE}){
                                        sendOK();
                                        returnValues(commandReader.getterCommandArrayExists(command));
                                    }else{
                                       sendERROR(); 
                                    }
                                }
                            }else if(!command.contains(" ")){
                                //Void commands
                                if(commandReader.instructionCommandExists(command)){
                                    sendOK();
                                }else{
                                    sendERROR();
                                }
                            }else{
                                //Normal commands
                                if(commandReader.commandExists(command)){
                                    sendOK();
                                }else{
                                    sendERROR();
                                }
                            }
                        }else{
                            sendERROR();
                        }
                        
                    }

                }else{
                    sendERROR();
                }

            }catch(IOException ioe){
                System.err.println("IOException in startListening(): " + ioe.getMessage());
            }
        } 
    }
}
