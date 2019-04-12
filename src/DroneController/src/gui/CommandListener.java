
package gui;

/**
 *
 * @author Fadil Smajilbasic
 */
public interface CommandListener {
    public void messageRecieved(String message);
    public void controllerMessage(String message);
    public void commandSent(String command);
}
