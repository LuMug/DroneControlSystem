package reader;

import com.leapmotion.leap.*;
import java.io.IOException;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader extends Listener {

    private Frame frame;
    private Controller controller;

    public Frame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        LeapMotionReader reader = new LeapMotionReader();

    }

    public LeapMotionReader() {
        controller = new Controller();
        controller.addListener(this);
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
        frame = controller.frame();
        System.out.println("rightHandZ: " + getHandY(getRightHand()));
    }

    public float getHandX(Hand hand) {

        if (hand != null) {
            return hand.palmPosition().getX();
        } else {
            return -99;
        }
    }

    public float getHandY(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().getY();
        } else {
            return -99;
        }
    }

    public float getHandZ(Hand hand) {

        if (hand != null) {
            return hand.palmPosition().getZ();
        } else {
            return -99;
        }
    }

    public Hand getRightHand() {
        if (frame.hands().rightmost().isRight()) {
            return frame.hands().rightmost();
        } else {
            return null;
        }
    }

    public Hand getLeftHand() {
        if (frame.hands().leftmost().isLeft()) {
            return frame.hands().leftmost();
        } else {
            return null;
        }
    }

    public float getDroneLiftAmount() {
        Hand hand;

        if ((hand = getLeftHand()) != null) {
            return getHandZ(hand);
        } else {
            return -99;
        }
    }

}
