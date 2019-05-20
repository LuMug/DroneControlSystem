package recorder;

import java.util.LinkedList;

/**
 * This class rappresents a buffer that contains a list of commands.
 * @author Luca Di Bello
 */
public class FlightBuffer{
    
    /**
     * Buffer that contains all the commands.
     */
    private LinkedList<String> buffer = new LinkedList<>();
    
    /**
     * This method adds a command to the buffer.
     * @param command Command as string.
     */
    public synchronized void addCommand(String command){
        if(command != null){
            buffer.add(command);
        }
    }
    
    /**
     * This methos adds an array of commands to the buffer.
     * @param commands String array of commands.
     */
    public synchronized void addCommands(String[] commands){
        for(String command : commands){
            buffer.add(command);
        }
    }
        
    /**
     * This method returns the first command of the buffer and then deletes itself.
     * @return The first command of the buffer.
     */
    public synchronized String getNextCommand(){
        return buffer.poll();
    }
    
    /**
     * This method clears the buffer.
     */
    public void clear(){
        buffer.clear();
    }
    
    /**
     * This method returns the length of the buffer. 
     * @return length of the buffer as integer
     */
    public int length(){
        return buffer.size();
    }
    
    /**
     * This method returns a flag that identifies if the buffer is empty or not.
     * @return True if the buffer isn't empty otherwise false.
     */
    public synchronized boolean existsNextCommand(){
        return buffer.peek() != null;
    }
}
