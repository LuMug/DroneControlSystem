
package communication;

/**
 * This listener is used by the DroneController, in order to be notified when a command has finished executing.
 * @author Fadil Smajilbasic
 */
public interface CommandManagerListener {
    /**
     * Method that notifies the listener that a command has been executed.
     */
    public void doneExecuting();
}
