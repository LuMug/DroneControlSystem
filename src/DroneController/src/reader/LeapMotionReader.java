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
    private static Frame frame;

    public LeapMotionReader() {
    }

    

    public float getHandX(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().getX();
        } else {
            return 0;
        }
    }

    public float getHandY() {
        if (getLeftHand() != null) {
            return getLeftHand().palmPosition().getY();
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
        Hand rhand = frame.hands().rightmost();
        if (rhand.isRight()) {
            return rhand;
        } else {
            return null;
        }
    }

    public Hand getLeftHand() {
        Hand lhand = frame.hands().leftmost();
        if (lhand.isLeft()) {
            return lhand;
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
    
    public synchronized void setFrame(Frame frame) {
        this.frame = frame;
    }

    public synchronized Frame getFrame() {
        return frame;
    }
}
