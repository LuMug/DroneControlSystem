package controller;

import com.leapmotion.leap.*;

/**
 * This class contains useful methods in order to extract the necessary data from the Frames read by the LeapMotion
 * @author Fadil Smajilbasic
 */
public class FrameHelper {

    /**
     * The most current frame read by the LeapMotion.
     */
    private Frame currentFrame = new Frame();
    
    /**
     * The Frame that was captured before the currentFrame.
     * Used primarily in order to calculate the delta values
     */
    private Frame lastFrame;

    /**
     * getHandY checks if the hand parameter is valid and returns the y position of the hand.
     * @param hand the LeapMotion Hand object.
     * @return the height, from the origin, of the hand object passed as parameter.
     */
    public float getHandY(Hand hand) {
        if (hand != null && hand.isValid() == true) {
            return hand.palmPosition().getY();
        } else {
            return 0;
        }
    }

    /**
     * This method extracts the Hand object of the current Frame 
     * @param frame 
     * @return 
     */
    public Hand getRightHand(Frame frame) {
        Hand rHand;
        if (frame != null && frame.isValid() == true) {
            rHand = frame.hands().rightmost();
        } else {
            rHand = getFrame().hands().rightmost();
        }
        if (rHand.isRight()) {
            return rHand;
        } else {
            return null;
        }
    }

    public Hand getLeftHand(Frame frame) {
        Hand lHand;
        if (frame != null && frame.isValid() == true) {
            lHand = frame.hands().rightmost();
        } else {
            lHand = getFrame().hands().rightmost();
        }
        if (lHand.isLeft()) {
            return lHand;
        } else {
            return null;
        }
    }

    public float getPitch(Hand hand) {
        try {
            Vector middleFinger = hand.fingers().get(2).tipPosition();
            Vector palmCenter = hand.palmPosition();
            return (float) -Math.toDegrees(Math.atan2(palmCenter.getY() - middleFinger.getY(), palmCenter.getZ() - middleFinger.getZ()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }

    public float getRoll(Hand hand) {
        try {
            Vector lVector = hand.fingers().leftmost().tipPosition();
            Vector rVector = hand.fingers().rightmost().tipPosition();
            return (float) Math.toDegrees(Math.atan2(rVector.getY() - lVector.getY(), rVector.getX() - lVector.getX()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }

    public float getYaw(Hand hand) {
        try {
            Vector middleFinger = hand.fingers().get(2).tipPosition();
            Vector palmCenter = hand.palmPosition();
            return (float) Math.toDegrees(Math.atan2(palmCenter.getX() - middleFinger.getX(), palmCenter.getZ() - middleFinger.getZ()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }

    public float getDroneLiftAmount() {
        Hand hand;
        if ((hand = getLeftHand(null)) != null) {
            return hand.palmPosition().getZ();
        } else {
            return 0;
        }
    }

    public float getHandSpeedY(Hand hand) {
        if (hand != null && hand.isValid() == true) {
            return hand.palmVelocity().getY();
        } else {
            return 0;
        }
    }

    public float getDeltaY() {
        return getHandY(getLeftHand(currentFrame)) - getHandY(getLeftHand(lastFrame));
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
