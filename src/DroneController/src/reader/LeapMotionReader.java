package reader;

import com.leapmotion.leap.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader extends Listener {

    private Frame frame;
    private Controller controller;
    DecimalFormat df = new DecimalFormat("#.###");

    public Frame getFrame() {
        return frame;
    }

    public static void main(String[] args) {
        LeapMotionReader reader = new LeapMotionReader();

    }

    public LeapMotionReader() {
        controller = new Controller();
        controller.addListener(this);

        df.setRoundingMode(RoundingMode.CEILING);
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

        System.out.println("leftHandY: " + getHandY(getLeftHand()) + " - rightHandY " + getHandY(getRightHand()));
        System.out.println("pitch: " + df.format(Math.toDegrees(getPitch())) + " roll: " + df.format(Math.toDegrees(getRoll())) + " yaw: " + df.format(Math.toDegrees(getYaw())));

        System.out.println("leftHandY: " + getHandY(getLeftHand()) + " - rightHandY " + getHandY(getRightHand()));

    }

    public float getHandX(Hand hand) {

        if (hand != null) {
            return hand.palmPosition().getX();
        } else {
            return 0;
        }
    }

    public float getHandY(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().getY();
        } else {
            return 0;
        }
    }

    public float getHandZ(Hand hand) {

        if (hand != null) {
            return hand.palmPosition().getZ();
        } else {
            return 0;
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

    public float getPitch() {
        Hand hand = getRightHand();
        if (hand != null) {
            return hand.palmPosition().pitch();
        } else {
            return 0;
        }
    }

    public float getRoll() {
        Hand hand = getRightHand();
        if (hand != null) {
            return hand.palmPosition().roll();
        } else {
            return 0;
        }
    }

    public float getYaw() {
        Hand hand = getRightHand();
        if (hand != null) {
            return hand.palmPosition().yaw();
        } else {
            return 0;
        }
    }

    public float getDroneLiftAmount() {
        Hand hand;

        if ((hand = getLeftHand()) != null) {
            return getHandZ(hand);
        } else {
            return 0;
        }
    }

}
