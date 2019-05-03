package recorder;

import java.util.LinkedList;

/**
 * <INSERT GOOD TITLE>
 * @author Luca Di Bello
 */
public class FlightBuffer{
    
    private LinkedList<String> buffer = new LinkedList<>();
    
    public void addMovement(String command){
        buffer.add(command);
    }
    
    public String getNextCommand(){
        return buffer.poll();
    }
    
    public boolean existsNextCommand(){
        return buffer.poll() != null ? true : false;
    }
}
