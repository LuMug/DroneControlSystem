package reader;

import com.leapmotion.leap.*;
import java.io.IOException;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader extends Listener {

    public static void main(String[] args) {

        Controller controller = new Controller();
        LeapMotionReader listener = new LeapMotionReader();
        controller.addListener(listener);

        System.out.println("Press Enter to quit...");
        String frame = "";

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    public void onFrame(Controller controller) {
        Frame frame = controller.frame();

        System.out.println("Frame id: " + frame.id()
                + ", fps: " + frame.timestamp()
                + ", hands: " + frame.hands().count()
                + ", fingers: " + frame.fingers().count()
                + ", left Hand: " + frame.hands().get(0).palmPosition()
                + ", rigth Hand: " + frame.hands().get(1).palmPosition());

    }

}
