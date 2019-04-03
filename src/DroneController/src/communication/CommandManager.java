package communication;

import static controller.DroneController.settingsManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import settings.ControllerSettings;
import settings.SettingsManager;

/**
 *
 * @author Luca Di Bello
 */
public class CommandManager {

    private DatagramSocket commandSocket;

    private ControllerSettings settings = new ControllerSettings();

    private final String JARI_ADDRESS = settings.getCommunicationJariAddress();
    private final int TELLO_COMMAND_LISTEN_PORT = settings.getCommunicationListenPortCommand();
    private final int TELLO_COMMAND_SEND_PORT = settings.getCommunicationSendPortCommand();

    public CommandManager() {
        try {
            commandSocket = new DatagramSocket(TELLO_COMMAND_LISTEN_PORT);
            System.out.println("[SUCCESS] Listening on port " + this.TELLO_COMMAND_LISTEN_PORT);

        } catch (SocketException ex) {
            System.err.println("Can't create client socket: " + ex.getMessage());
        }
    }

    /**
     * This method sends a command to DJI Tello.
     *
     * @param command Command to send to the drone.
     */
    public void sendCommand(String command) {
        System.out.println("sending command: " + command);
        //Create a socket for sending the data
        try {
            //Prima di inviare il pacchetto aspetta che il drone ha riposto correttamente (OK) al comando precedente

            //Creo il pacchetto
            byte[] commandData = command.getBytes();

            DatagramPacket packet = new DatagramPacket(
                    commandData,
                    commandData.length,
                    InetAddress.getByName(this.JARI_ADDRESS),
                    TELLO_COMMAND_SEND_PORT
            );

            /*
            //FOR TESTING
            DatagramPacket packet = new DatagramPacket(
                    commandData,
                    commandData.length, 
                    InetAddress.getByName("127.0.0.1"), 
                    5555
            );
             */
            commandSocket.send(packet);

            //Wait for response
            System.out.println("Wait for response from drone");
            packet.setData(new byte[64]);
            commandSocket.receive(packet);

            String response = new String(packet.getData()).trim();

            if (response.equals("OK")) {
//                System.out.println("--> " + new String(command) + " is ok");
            } else {
                System.err.println("--> " + new String(command) + " ERROR");
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    public void sendCommands(String[] commands) {
        for (String command : commands) {
            if (command != null) {
                if (!command.equals("")) {
                    sendCommand(command);
                }
            }
        }
    }
}
