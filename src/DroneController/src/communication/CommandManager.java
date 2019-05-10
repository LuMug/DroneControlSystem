package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import settings.ControllerSettings;

/**
 * This class is used to send and receive data from ant to the Drone.
 *
 * @author Luca Di Bello
 */
public class CommandManager {

    /**
     * The socket used to communicate.
     */
    private DatagramSocket commandSocket;

    /**
     * Class used to load settings found in the config.dcs file.
     */
    private ControllerSettings settings = new ControllerSettings();

    /**
     * This constant contains the IP address of the tello drone.
     */
    private final String TELLO_ADDRESS = settings.getCommunicationTelloAddress();
    /**
     * This constant contains the tello communication port to which the tello
     * will send the responses to the commands sent.
     */
    private final int TELLO_COMMAND_LISTEN_PORT = settings.getCommunicationListenPortCommand();
    /**
     * This constant contains the tello communication port to which the commands
     * will be sent to.
     */
    private final int TELLO_COMMAND_SEND_PORT = settings.getCommunicationSendPortCommand();
    /**
     * This constant contains the tello communication port to which we send
     * commands about the state of the drone. Using this port we can query the
     * drone about it's battery status, it's velocity, altitude and other
     * parameters.
     */
    private final int TELLO_STATE_SEND_PORT = settings.getTelloStatePort();
    /**
     * This field contains the listener of the Command manager class. This
     * listener gets notified when a command is executed.
     */
    private final CommandManagerListener LISTENER;

    /**
     * The constructor of the CommandManager that creates a new DatagramSocket.
     *
     * @param listener the listener of the CommandManager class.
     */
    public CommandManager(CommandManagerListener listener) {
        try {
            commandSocket = new DatagramSocket(TELLO_COMMAND_LISTEN_PORT);
            System.out.println("[SUCCESS] Listening on port " + this.TELLO_COMMAND_LISTEN_PORT);

        } catch (SocketException ex) {
            System.err.println("Can't create client socket: " + ex.getMessage());
        }
        this.LISTENER = listener;

    }

    /**
     * This method sends a command to DJI Tello.
     *
     * @param command Command to send to the drone.
     */
    public void sendCommand(String command) {
        System.out.println("sending command: " + command);

        try {
            DatagramPacket packet;

            // Identifies the command in order to use the right method
            packet = this.createPackage(command);

            System.out.println("Wait for response from drone");
            packet.setData(new byte[255]);
            commandSocket.receive(packet);

            String response = new String(packet.getData()).trim();

            LISTENER.doneExecuting(); // notifies the listener that the drone has executed the command, no matter if it is postitive or negative

            if (command.contains("?")) {
                System.out.println("Response: " + response);
            } else {
                if (response.equalsIgnoreCase("OK")) {
                    System.out.println("--> " + command + " is ok");
                } else {
                    System.err.println("--> " + command + " ERROR");
                }
            }

        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    /**
     * This command sends the command without waiting for the drone's response.
     *
     * @param command the command to send
     */
    public void sendCommandAsync(String command) {

        System.out.println("sending command async: " + command);

        try {
            this.createPackage(command);
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    /**
     * This method sends
     *
     * @param command
     * @return
     * @throws UnknownHostException
     * @throws IOException
     */
    private DatagramPacket createPackage(String command) throws UnknownHostException, IOException {

        if (command.contains("?")) {
            System.out.println("Contains state command");
        } else {
        }

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

    private DatagramPacket sendStateString(String command) throws UnknownHostException, IOException {

        byte[] commandData = command.getBytes();

        DatagramPacket packet = new DatagramPacket(
                commandData,
                commandData.length,
                InetAddress.getByName(this.TELLO_ADDRESS),
                this.TELLO_STATE_SEND_PORT
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
