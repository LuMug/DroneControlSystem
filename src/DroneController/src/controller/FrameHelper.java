package controller;

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
public class FrameHelper {
    private Frame currentFrame = new Frame();
    private Frame lastFrame;

    public FrameHelper() {
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

    public float getPitch(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().pitch();
        } else {
            return 0;
        }
    }

    public float getRoll(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().roll();
        } else {
            return 0;
        }
    }

    public float getYaw(Hand hand) {
        if (hand != null) {
            return hand.palmPosition().yaw();
        } else {
            return 0;
        }
    }

    public float getDroneLiftAmount() {
        Hand hand;
        if ((hand = getLeftHand(currentFrame)) != null) {
            return getHandZ(hand);
        } else {
            return 0;
        }
    }
    
    public float getDeltaZ(){
        return getHandZ(getLeftHand(currentFrame)) - getHandZ(getLeftHand(lastFrame));        
    }
    
    public synchronized void setFrame(Frame frame) {
        this.lastFrame = this.currentFrame;
        this.currentFrame = frame;
        
    }

    public Frame getLastFrame() {
        return this.lastFrame;
    }
    
    

    public synchronized Frame getFrame() {
        return currentFrame;
    }
}
