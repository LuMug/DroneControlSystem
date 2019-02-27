package reader;

import com.leapmotion.leap.*;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fadil Smajilbasic
 */
public class LeapMotionReader extends Listener {

    
    DecimalFormat df = new DecimalFormat("#.###");
    LeapMotionReaderListener listener;

    public LeapMotionReader() {
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
        if (getFrame().hands().rightmost().isRight()) {
            return getFrame().hands().rightmost();
        } else {
            return null;
        }
    }

    public Hand getLeftHand() {
        if (getFrame().hands().leftmost().isLeft()) {
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
