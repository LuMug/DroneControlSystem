package reader;
import com.leapmotion.leap.*;
import java.io.IOException;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        Controller controller = new Controller();
        LeapMotionListener listener = new LeapMotionListener();
        controller.addListener(listener);
        
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
