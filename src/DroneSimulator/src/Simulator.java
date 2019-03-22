
import java.awt.Graphics;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.swing.JPanel;

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
public class Simulator extends JPanel{
    
    // ------------------- General Variables -------------------
    
    private static final int PORT = 8889;
    private static final String ADDRESS_TO_SEND = "192.168.43.16";
    private static final int BUFFER_SIZE = 64;
    private static CommandReader commandReader;
    private static DatagramSocket socket;
    private static PositionXYPlotFrame positionXYFrame;
    private static PositionXZPlotFrame positionXZFrame;
    private static RotationChartFrame rotationChartFrame;    
//    private static SimulatorPainter painter;
    //Positioning points
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int roll = 0;
    private int yaw = 0;
    private int pitch = 0;
    
    // ------------------- Constructor -------------------
    
    public Simulator() throws SocketException, InterruptedException{
        commandReader = new CommandReader(this);
//        painter = new SimulatorPainter();
        positionXYFrame = new PositionXYPlotFrame(this.x, this.y);
        positionXZFrame = new PositionXZPlotFrame(this.x, this.z);
        rotationChartFrame = new RotationChartFrame(this.pitch,this.yaw,this.roll);
        //Start listening on socket
        socket = new DatagramSocket(PORT);
        startListening();
    }
    
    // ------------------- Setters and Getters -------------------
    
    @Override
    public int getX(){
        return this.x;
    }
    
    public void setX(int x){
        this.x = x;
        positionXYFrame.setPositionX(this.x);
        positionXZFrame.setPositionX(this.x);
    }
    
    @Override
    public int getY(){
        return this.y;
    }
    
    public void setY(int y){
        this.y = y;
        positionXYFrame.setPositionY(this.y);
    }
    
    public int getZ(){
        return this.z ;
    }
    
    public void setZ(int z){
        this.z = z;
        positionXZFrame.setPositionX(this.x);
    }
    
    public int getRoll(){
        return this.roll;
    }
    
    public void setRoll(int rotation){
        if(rotation >= -360 && rotation <= 360){
            this.roll = rotation;
        }else{
            int div = rotation%360;
            this.roll = rotation/div;
        }
        rotationChartFrame.setRoll(this.roll);
    }
    
    public int getYaw(){
        return this.yaw;
    }
    
    public void setYaw(int rotation){
        if(rotation >= -360 && rotation <= 360){
            this.yaw = rotation;
        }else{
            int div = rotation%360;
            this.yaw = rotation/div;
        }
        rotationChartFrame.setYaw(this.yaw);

    }
    
    public int getPitch(){
        return this.pitch;
    }
    
    public void setPitch(int rotation){
        if(rotation >= -360 && rotation <= 360){
            this.pitch = rotation;
        }else{
            int div = rotation%360;
            this.pitch = rotation/div;
        }
        rotationChartFrame.setPitch(this.pitch);

    }
    
    @Override
    public void paint(Graphics g){
//        painter.paint(g);
    }
    
    // ------------------- Helper Methods -------------------
    
    private static void sendOK() throws UnknownHostException, IOException{
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
    
    private static void sendERROR() throws UnknownHostException, IOException{
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
    
    // ------------------- Network Methods -------------------

    private static void startListening() throws InterruptedException{
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
                                }else{
                                    sendERROR();
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
    
    // ------------------- Other Methods -------------------
    
    public void stopMotors(){
        //Stop all four motors immediately.
    }
}
