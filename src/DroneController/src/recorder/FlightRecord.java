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
    
    /**
     * Path of the record file.
     */
    private Path fileRecordPath;

    /**
     * Default constructor.
     * @param filePath Path of the record file.
     */
    public FlightRecord(Path filePath) {
        this.fileRecordPath = filePath;
    }
    
    /**
     * This methods reads all the commands from the record file and pack 
     * them into a FlightBuffer object.
     * @return FlightBuffer object containing all the flight instructions (commands).
     * @throws IOException thrown when the file is not accessible or non-existent.
     */
    public FlightBuffer getFlightCommands() throws IOException{
        FlightBuffer buffer = new FlightBuffer();

        try{
            List<String> lines = Files.readAllLines(fileRecordPath);
            buffer.addCommands(lines.toArray(new String[lines.size()]));
            
            return buffer;
        }
        catch(IOException ex){
            throw new IOException("Can't find any type of file in the path: " + fileRecordPath);
        }
    }
    
    /**
     * getter method for fileRecordPath attribute.
     * @return Path of the record file.
     */
    public Path getSaveLocation() {
        return fileRecordPath;
    }
}
