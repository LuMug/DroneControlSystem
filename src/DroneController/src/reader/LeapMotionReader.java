package reader;

import com.leapmotion.leap.*;
import java.text.DecimalFormat;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader extends Listener {

    
    DecimalFormat df = new DecimalFormat("#.###");
    LeapMotionReaderListener listener;

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

    public Hand getRightHand(Frame frame) {
        Hand rhand = frame.hands().rightmost();
        if (rhand.isRight()) {
            return rhand;
        } else {
            return null;
        }
    }

    public Hand getLeftHand(Frame frame) {
        Hand lhand = frame.hands().leftmost();
        if (lhand.isLeft()) {
            return lhand;
        } else {
            return null;
        }
    }

    public float getPitch(Frame frame) {
        Hand hand = getRightHand(frame);
        if (hand != null) {
            return hand.palmPosition().pitch();
        } else {
            return 0;
        }
    }

    public float getRoll(Frame frame) {
        Hand hand = getRightHand(frame);
        if (hand != null) {
            return hand.palmPosition().roll();
        } else {
            return 0;
        }
    }

    public float getYaw(Frame frame) {
        Hand hand = getRightHand(frame);
        if (hand != null) {
            return hand.palmPosition().yaw();
        } else {
            return 0;
        }
    }

    public float getDroneLiftAmount(Frame frame) {
        Hand hand;
        if ((hand = getLeftHand(frame)) != null) {
            return getHandZ(hand);
        } else {
            return 0;
        }
    }

}
