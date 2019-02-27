
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
    private static SimulatorPainter painter;
    //Positioning points
    private int x = 0;
    private int y = 0;
    private int z = 0;
    private int rotationX = 0;
    private int rotationY = 0;
    private int rotationZ = 0;
    
    // ------------------- Constructor -------------------
    
    public Simulator() throws SocketException{
        commandReader = new CommandReader(this);
        painter = new SimulatorPainter();
        //Start listening on socket
        socket = new DatagramSocket(PORT);
        this.startListening();
    }
    
    // ------------------- Setters and Getters -------------------
    
    @Override
    public int getX(){
        return this.x;
    }
    
    public void setX(int x){
        this.x = x;
    }
    
    @Override
    public int getY(){
        return this.y;
    }
    
    public void setY(int y){
        this.y = y;
    }
    
    public int getZ(){
        return this.z ;
    }
    
    public void setZ(int z){
        this.z = z;
    }
    
    public int getRotationX(){
        return this.rotationX;
    }
    
    public void setRotationX(int rotationX){
        this.rotationX = rotationX;
    }
    
    public int getRotationY(){
        return this.rotationY;
    }
    
    public void setRotationY(int rotationY){
        this.rotationY = rotationY;
    }
    
    public int getRotationZ(){
        return this.rotationZ;
    }
    
    public void setRotationZ(int rotationZ){
        this.rotationZ = rotationZ;
    }
    
    @Override
    public void paint(Graphics g){
        painter.paint(g);
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
    }
    
    // ------------------- Network Methods -------------------

    private void startListening(){
        boolean droneIsConnected;
        //Create buffer array with right size
        byte[] buffer = new byte[BUFFER_SIZE];

        //Prepare packet to store the recived one
        DatagramPacket recivePacket = new DatagramPacket(buffer, buffer.length);

        System.out.println("Started listener on " + socket.getLocalSocketAddress());

        while (true) {
            try{
                //Set packet size
                recivePacket.setData(new byte[buffer.length]);
                //Waiting for a Packet and to receive a datagram
                socket.receive(recivePacket);

                //Read Packet content
                String message = new String(recivePacket.getData());

                if((message.trim()).equals("command")){
                    sendOK();
                    droneIsConnected = true;
                    while(droneIsConnected){
                        //Set packet size
                        recivePacket.setData(new byte[buffer.length]);
                        //Waiting for a Packet and to receive a datagram
                        socket.receive(recivePacket);
                        //Read Packet content
                        String command = new String(recivePacket.getData());
                        if(commandReader.commandExists(command)){
                            sendOK();
                        }else{
                            sendERROR();
                        }
                    }

                }

            }catch(IOException ioe){
                System.out.println("IOException in listener: " + ioe.getMessage());
            }
        } 
    }
}
