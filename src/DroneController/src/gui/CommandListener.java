package gui;

/**
 * Listener used to display useful logs to the GUI
 *
 * @author Fadil Smajilbasic
 */
public interface CommandListener {

    /**
     * Appends "Received: " string before the message string and then makes the
     * message visible to the user in the Log GUI tab.
     *
     * @param message
     */
    public void messageReceived(String message);

    /**
     * Appends "Controller message: " string before the message string and then
     * makes the message visible to the user in the Log GUI tab.
     *
     * @param message
     */
    public void controllerMessage(String message);

    /**
     * Appends "Sent: " string before the message string and then
     * makes the message visible to the user in the Log GUI tab.
     *
     * @param message
     */
    public void commandSent(String command);
}
