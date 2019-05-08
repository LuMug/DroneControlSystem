package recorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 *
 * @author Luca Di Bello
 */
public class FlightRecord {
    
    private Path file_record_path;
    
    public FlightRecord(Path filePath) {
        this.file_record_path = filePath;
    }
    
    public List<String> getFlightCommands(){
        try{
            return Files.readAllLines(file_record_path);
        }
        catch(IOException ex){
            
        }
    }
}
