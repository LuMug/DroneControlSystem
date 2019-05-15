package recorder;

import java.util.LinkedList;

/**
 * <INSERT GOOD TITLE>
 * @author Luca Di Bello
 */
public class FlightBuffer{
    
    private LinkedList<String> buffer = new LinkedList<>();
    
    public synchronized void addCommand(String command){
        if(command != null){
            buffer.add(command);
        }
    }
    
    public synchronized void addCommands(String[] commands){
        for(String command : commands){
            buffer.add(command);
        }
    }
    
    public synchronized String getNextCommand(){
        return buffer.poll();
    }
    
    public void clear(){
        buffer.clear();
    }
    
    public int length(){
        return buffer.size();
    }

    public synchronized boolean existsNextCommand(){
        return buffer.peek() != null;
    }
}
