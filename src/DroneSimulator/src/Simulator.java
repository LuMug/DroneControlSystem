
import java.awt.Graphics;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
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
 * @author Andrea Rauso
 * @version 15.02.2019 - xx.xx.xxxx
 */
public class Simulator extends JPanel{
    private static final int COMMAND_PORT = 8889;
    private static final int BUFFER_SIZE = 64;
    private static boolean commandSet = false;
    
    @Override
    public void paint(Graphics g){
        
    }
    
    public static void main(String[] args) {
        try {
            //Start listening socket
            DatagramSocket serverSocket = new DatagramSocket(COMMAND_PORT);

            //Create buffer array with right size
            byte[] buffer = new byte[BUFFER_SIZE];

            //Prepare packet to store the recived one
            DatagramPacket recivePacket = new DatagramPacket(buffer, buffer.length);

            System.out.println("Started listener on " + serverSocket.getLocalSocketAddress());
           
            while (true) {
                try{
                    //Set packet size
                    recivePacket.setData(new byte[buffer.length]);
                    //Waiting for a Packet and to receive a datagram
                    serverSocket.receive(recivePacket);
                    
                    //Read Packet content
                    String message = new String(recivePacket.getData());
                    
                    if((message.trim()).equals("command")){
                        System.out.println("COMMAND RECIEVED");
                        byte[] response = "OK".getBytes();
                        try{
                            //DatagramSocket socket = new DatagramSocket();
                            //DatagramPacket packet = new DatagramPacket(response, response.length);
                           
                            DatagramPacket packet = new DatagramPacket(
                                    response, 
                                    response.length, 
                                    InetAddress.getByName("192.168.37.1"), 
                                    8889
                            );
                            
                            serverSocket.send(packet);
                            //socket.send(packet);
                            System.out.println("Message <" + new String(response) + "> sent from " + serverSocket.getLocalSocketAddress() + " to " + packet.getSocketAddress());
                        }catch(SocketException se){
                            System.err.println("SocketException: " + se.getMessage());
                        }catch(IOException ioe){
                            System.err.println("IOException: " + ioe.getMessage());
                        }
                        //commandSet = true;
                    }
//                    if(commandSet){
//                        switch(message.trim()){
//                            case "takeoff":
//                                System.out.println("Command");
//                        }
//                    }
                    
                    
                    
                }
                catch(IOException ioe){
                    System.out.println("IOException in listener: " + ioe.getMessage());
                }
            }
        } catch (SocketException se) {
            System.err.println("Can't create listener on socket: " + se.getMessage());
        }
    }
}
