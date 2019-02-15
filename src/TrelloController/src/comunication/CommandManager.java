package comunication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import settings.TelloComunicationData;

/**
 * 
 * @author Luca Di Bello
 */
public class CommandManager {
    
    public void sendCommand(Command command){
        //Create a socket for sending the data
        try {
            //Creo il socket per la comunicazione
            DatagramSocket sendSocket = new DatagramSocket();
           
            //Creo il pacchetto
            byte[] data = command.toString().getBytes();
            
            DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length, 
                    InetAddress.getByName(TelloComunicationData.TELLO_ADDRESS), 
                    TelloComunicationData.TELLO_COMMAND_SEND_PORT
            );
            
            sendSocket.send(packet);
            
            System.out.println("Message sent to " + packet.getSocketAddress());
            
            sendSocket.close();
        } catch (SocketException ex) {
            System.err.println("Can't create client socket: " + ex.getMessage());
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }
}
