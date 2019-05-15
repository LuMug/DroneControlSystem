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
    
    public FlightBuffer getFlightCommands() throws IOException{
        FlightBuffer buffer = new FlightBuffer();

        try{
            List<String> lines = Files.readAllLines(file_record_path);
            buffer.addCommands(lines.toArray(new String[lines.size()]));
            
            return buffer;
        }
        catch(IOException ex){
            throw new IOException("Can't find any type of file in the path: " + file_record_path);
        }
    }
    
    public Path getSaveLocation() {
        return file_record_path;
    }
}
