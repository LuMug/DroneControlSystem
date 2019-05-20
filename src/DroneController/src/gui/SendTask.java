package gui;

import communication.CommandManager;
import java.util.concurrent.Callable;

/**
 *
 * @author Luca Di Bello
 */
public class SendTask implements Callable<String>{

    private final CommandManager MANAGER;
    private final String COMMAND;
    
    public SendTask(CommandManager manager, String command) {
        this.MANAGER = manager;
        this.COMMAND = command;
    }
    
    @Override
    public String call() throws Exception {
        MANAGER.sendCommand(COMMAND);
        return COMMAND;
    }
}
