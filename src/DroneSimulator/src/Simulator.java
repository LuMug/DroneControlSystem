
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
 * La classe Simulator si occupa di ricevere i dati dalla classe Controller
 * sottoforma di pacchetti UDP e di stamparli su SimulatorFrame simulando un 
 * drone DJI Tello.
 * 
 * @author Jari Näser
 * @version 15.02.2019 - xx.xx.xxxx
 */
public class Simulator{
    
    // ------------------- General Variables -------------------
    
    private final int PORT = 8889;
    private final String ADDRESS_TO_SEND = "192.168.43.246";
    private final int BUFFER_SIZE = 64;
    private CommandReader commandReader;
    private DatagramSocket socket;
    private TelloChartFrame telloChartFrame;
    //Positioning points
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int roll = 0;
    private int yaw = 0;
    private int pitch = 0;
    
    // ------------------- Constructor -------------------
    
    public Simulator() throws SocketException, InterruptedException{
        this.commandReader = new CommandReader(this);
        this.telloChartFrame = new TelloChartFrame(x,y,z,pitch,yaw,roll);
        //Start Frames
        this.telloChartFrame.setVisible(true);
        //Start listening on socket
        this.socket = new DatagramSocket(PORT);
        this.startListening();
    }
    
    // ------------------- Setters and Getters -------------------
    

    public int getX(){
        return this.x;
    }
    
    public void setX(int x){
        this.x = x;
        this.telloChartFrame.setPositionX(this.x);
    }
    

    public int getY(){
        return this.y;
    }
    
    public void setY(int y){
        this.y = y;
        this.telloChartFrame.setPositionY(this.y);
    }
    
    public int getZ(){
        return this.z ;
    }
    
    public void setZ(int z){
        this.z = z;
        this.telloChartFrame.setPositionZ(this.z);
    }
    
    public int getRoll(){
        return this.roll;
    }
    
    public void setRoll(int rotation){
        this.roll = rotation%360;
        this.telloChartFrame.setRoll(this.roll);
    }
    
    public int getYaw(){
        return this.yaw;
    }
    
    public void setYaw(int rotation){
        this.yaw = rotation%360;
        this.telloChartFrame.setYaw(this.yaw);

    }
    
    public int getPitch(){
        return this.pitch;
    }
    
    public void setPitch(int rotation){
        this.pitch = rotation%360;
        this.telloChartFrame.setPitch(this.pitch);
    }
    
    // ------------------- Helper Methods -------------------
    
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
    
    // ------------------- Network Methods -------------------

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
                //Waiting for a Packet and to receive a datagram
                socket.receive(recivePacket);
                //Read Packet content
                String message = new String(recivePacket.getData()).trim();
                
                System.out.println("Recieved packet, content: " + message);
                
                if((message).equals("command")){
                    sendOK();
                    droneIsConnected = true;
                    while(droneIsConnected){
                        //Set packet size
                        recivePacket.setData(new byte[buffer.length]);
                        //Waiting for a Packet and to receive a datagram
                        socket.receive(recivePacket);
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
                System.out.println("IOException in startListening(): " + ioe.getMessage());
            }
        } 
    }
}
