package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import recorder.FlightBuffer;
import recorder.FlightRecord;
import recorder.FlightRecorder;
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
     * This field contains information whether the flight commands are being
     * recorded or not.
     */
    private boolean isRecordingFlight = false;
    
    /**
     * This field is a FlightRecorder object used to record.
     */
    private final FlightRecorder RECORDER = new FlightRecorder();
    
    /**
     * This field contains the buffer of commands that will be written to the
     * recording file.
     */
    private FlightBuffer recordBuffer = new FlightBuffer();
    
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
    public CommandManager(CommandManagerListener listener) throws SocketException {
        try {
            commandSocket = new DatagramSocket(TELLO_COMMAND_LISTEN_PORT);
            System.out.println("[SUCCESS] Listening on port " + this.TELLO_COMMAND_LISTEN_PORT);
            this.LISTENER = listener;
        } catch (SocketException ex) {
            throw new SocketException("Can't create client socket: " + ex.getMessage());
        }
    }

    /**
     * This method sends a command to DJI Tello.
     *
     * @param command Command to send to the drone.
     */
    public void sendCommand(String command) {
        System.out.println("sending command: " + command);
        
        //Add commands to recorder
        if(isRecordingFlight){
            System.out.println("Add command: " + command);
            recordBuffer.addCommand(command);
        }
                    
        try {
            DatagramPacket packet;

            // Identifies the command in order to use the right method
            packet = this.createPacket(command);

            commandSocket.send(packet);

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
            this.createPacket(command);
        } catch (UnknownHostException uhe) {
            System.out.println("Cannot resolve hostname: " + uhe.getMessage());
        } catch (IOException ioex) {
            System.out.println("Cannot send packet: " + ioex.getMessage());
        }
    }

    /**
     * This method creates a DatagramPacket based on the command that comes as
     * the parameter.
     *
     * @param command the command to include in the packet.
     * @return the DataGramPacket that cam be sent as is.
     * @throws UnknownHostException exception thrown if the packet address is
     * invalid.
     * @throws IOException exception thrown when an IO exception occurs during
     * the creation of the packet.
     */
    private DatagramPacket createPacket(String command) throws UnknownHostException, IOException {

        byte[] commandData = command.getBytes();
        DatagramPacket packet;
        if (command.contains("?")) {
            System.out.println("Contains state command");
            packet = new DatagramPacket(
                    commandData,
                    commandData.length,
                    InetAddress.getByName(this.TELLO_ADDRESS),
                    this.TELLO_STATE_SEND_PORT
            );
        } else {
            packet = new DatagramPacket(
                    commandData,
                    commandData.length,
                    InetAddress.getByName(this.TELLO_ADDRESS),
                    TELLO_COMMAND_SEND_PORT
            );

        }

        return packet;
    }

    /**
     * This method is used to send multiple commands received as an array of
     * Commands. This method uses the sendCommand(String command) method
     *
     * @param commands the array of commands to send.
     */
    public void sendCommands(String[] commands) {
        for (String command : commands) {
            if (command != null) {
                if (!command.equals("")) {
                    sendCommand(command);
                }
            }
        }
    }
    
    public void startRecording() {
        this.recordBuffer.clear();
        this.isRecordingFlight = true;

        System.out.println("[Info] Start recording");
    }

    public void stopRecording() {
        if(isRecordingFlight){
            this.isRecordingFlight = false;
            try{
                if(recordBuffer.length() > 0){
                    RECORDER.createBase();
                    FlightRecord record = RECORDER.generateRecordFile();
                    System.out.println("Generated file: " + record.getSaveLocation());
                    RECORDER.saveFlightPattern(this.recordBuffer, record);
                    System.out.println("[Info] File saved to path: " + record.getSaveLocation());
                }
                else{
                    System.err.println("[Recorder] Empty file detected, the buffer is empty...");
                }
            } catch (IOException ex) {
                System.out.println("[Info] Can't save the flight. *SAD SMILE*");
            }
        }
        else{
            System.err.println("[Recorder] I was not recording!");
        }

        System.out.println("[Info] Stopped recording");
    }
}
