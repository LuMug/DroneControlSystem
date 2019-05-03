package communication;

//import static controller.DroneController.settingsManager;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import settings.ControllerSettings;

/**
 *
 * @author Luca Di Bello
 */
public class CommandManager {

    private DatagramSocket commandSocket;

    private ControllerSettings settings = new ControllerSettings();

    private final String JARI_ADDRESS = settings.getCommunicationJariAddress();
    private final String TELLO_ADDRESS = settings.getCommunicationTelloAddress();
    private final int TELLO_COMMAND_LISTEN_PORT = settings.getCommunicationListenPortCommand();
    private final int TELLO_COMMAND_SEND_PORT = settings.getCommunicationSendPortCommand();
    private final CommandManagerListener listener;

    public CommandManager(CommandManagerListener listener) {
        try {
            commandSocket = new DatagramSocket(TELLO_COMMAND_LISTEN_PORT);
            System.out.println("[SUCCESS] Listening on port " + this.TELLO_COMMAND_LISTEN_PORT);

        } catch (SocketException ex) {
            System.err.println("Can't create client socket: " + ex.getMessage());
        }
        this.listener = listener;

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
            DatagramPacket packet = this.sendString(command);

            //Wait for response
            System.out.println("Wait for response from drone");
            packet.setData(new byte[255]);
            commandSocket.receive(packet);

            String response = new String(packet.getData()).trim();
            
            listener.doneExecuting();
            
            if (response.equalsIgnoreCase("OK")) {
                System.out.println("--> " + command + " is ok");
            } else {
                System.err.println("--> " + command + " ERROR");
            }
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    public void sendCommandAsync(String command) {
        System.out.println("sending command async: " + command);
        //Create a socket for sending the data
        try {
            //Prima di inviare il pacchetto aspetta che il drone ha riposto correttamente (OK) al comando precedente
            this.sendString(command);
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    private DatagramPacket sendString(String command) throws UnknownHostException, IOException {
        //Creo il pacchetto
        byte[] commandData = command.getBytes();

        DatagramPacket packet = new DatagramPacket(
                commandData,
                commandData.length,
                InetAddress.getByName(this.TELLO_ADDRESS),
                TELLO_COMMAND_SEND_PORT
        );

        commandSocket.send(packet);

        return packet;
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
