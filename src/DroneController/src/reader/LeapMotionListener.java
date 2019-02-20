package reader;

import com.leapmotion.leap.*;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionListener extends Listener {
    
    @Override
    public void onConnect(Controller controller) {
        System.out.println("Connected");
    }

    @Override
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
