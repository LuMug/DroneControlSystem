package recorder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * This class describes a flight record file.
 * @author Luca Di Bello
 */
public class FlightRecord {
    
    private Path file_record_path;

    public FlightRecord(Path filePath) {
        this.file_record_path = filePath;
    }
    
    public List<String> getFlightCommands() throws IOException{
        try{
            return Files.readAllLines(file_record_path);
        }
        catch(IOException ex){
            throw new IOException("Can't find any type of file in the path: " + file_record_path);
        }
    }
    
    public Path getSaveLocation() {
        return file_record_path;
    }
}
