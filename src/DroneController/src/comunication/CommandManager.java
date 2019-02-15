package comunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import settings.TelloComunicationData;

/**
 * TODO: <INSERT DESCRIPTION>
 * @author Luca Di Bello
 */
public class CommandManager {
    
    /**
     * This method sends a command to DJI Tello.
     * @param command Command to send to the drone.
     */
    public void sendCommand(String command){
        //Create a socket for sending the data
        try {
            //Creo il socket per la comunicazione
            DatagramSocket commandSocket = new DatagramSocket();
           
            //Creo il pacchetto
            byte[] commandData = command.getBytes();
            
            DatagramPacket packet = new DatagramPacket(
                    commandData,
                    commandData.length, 
                    InetAddress.getByName(TelloComunicationData.JARI_ADDRESS), 
                    TelloComunicationData.TELLO_COMMAND_SEND_PORT
            );
            
            commandSocket.send(packet);
            
            System.out.println("Message sent to " + packet.getSocketAddress());
            commandSocket.close();
        } catch (SocketException ex) {
            System.err.println("Can't create client socket: " + ex.getMessage());
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }
    
    public void listenData(int port,int bufferSize){
        try {
            //Start listening socket
            DatagramSocket serverSocket = new DatagramSocket(port);

            //Create buffer size
            byte[] buffer = new byte[bufferSize];

            //Prepare a Packet to store the recived one
            DatagramPacket recivePacket = new DatagramPacket(buffer, buffer.length);

            System.out.println("Started listener on " + serverSocket.getLocalSocketAddress());
            
            //Waiting for a packet
            while (true) {
                try{
                    // Wait to receive a datagram
                    serverSocket.receive(recivePacket);
                    
                    //LEGGERE PACCHETTO
                    String msg = new String(recivePacket.getData());
                    System.err.println("Response recived: " + msg);
                    
                    // Reset the length of the packet before reusing it.
                    recivePacket.setLength(buffer.length);
                }
                catch(IOException ioe){
                    System.out.println("IOException in listener: " + ioe.getMessage());
                }
            }
        }
        catch (SocketException se) {
            System.err.println("Can't create listen socket: " + se.getMessage());
        }
    }
}
