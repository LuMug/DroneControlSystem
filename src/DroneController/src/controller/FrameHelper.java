package controller;

import com.leapmotion.leap.*;

/**
 * This class contains useful methods in order to extract the necessary data
 * from the Frames read by the LeapMotion
 *
 * @author Fadil Smajilbasic
 */
public class FrameHelper {

    /**
     * The latest frame read by the LeapMotion.
     */
    private Frame currentFrame = new Frame();

    /**
     * The Frame that was captured before the currentFrame. Used primarily in
     * order to calculate the delta values
     */
    private Frame lastFrame;

    /**
     * getHandY checks if the hand parameter is valid and returns the y position
     * of the hand.
     *
     * @param hand the LeapMotion Hand object.
     * @return the height, from the origin, of the hand object passed as
     * parameter.
     */
    public float getHandY(Hand hand) {
        if (hand != null && hand.isValid()) {
            return hand.palmPosition().getY();
        } else {
            return 0;
        }
    }

    /**
     * This method extracts the Hand object of the right hand from the frame
     * passed as parameter, if the passed parameter is null it gets the frame
     * saved in the currentFrame variable
     *
     * @param frame the frame from which it needs to get the Hand object
     * @return the Hand object if there is a right hand in the frame, null
     * otherwise.
     */
    public Hand getRightHand(Frame frame) {
        Hand rHand;
        if (frame != null && frame.isValid()) {
            rHand = frame.hands().rightmost();
        } else {
            rHand = getCurrentFrame().hands().rightmost();
        }
        if (rHand.isRight()) {
            return rHand;
        } else {
            return null;
        }
    }

    /**
     * This method extracts the Hand object of the left hand from the frame
     * passed as parameter, if the passed parameter is null it gets the frame
     * saved in the currentFrame variable
     *
     * @param frame the frame from which it needs to get the Hand object
     * @return the Hand object if there is a left hand in the frame, null
     * otherwise.
     */
    public Hand getLeftHand(Frame frame) {
        Hand lHand;
        if (frame != null && frame.isValid()) {
            lHand = frame.hands().rightmost();
        } else {
            lHand = getCurrentFrame().hands().rightmost();
        }
        if (lHand.isLeft()) {
            return lHand;
        } else {
            return null;
        }
    }

    /**
     * This method returns the pitch angle of the hand
     *
     * @param hand the hand from which it needs to read the pitch angle
     * @return the pitch angle in degrees
     */
    public float getPitch(Hand hand) {
        try {
            Vector middleFinger = hand.fingers().get(2).tipPosition();
            Vector palmCenter = hand.palmPosition();
            return (float) -Math.toDegrees(Math.atan2(palmCenter.getY() - middleFinger.getY(), palmCenter.getZ() - middleFinger.getZ()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }

    /**
     * This method returns the roll angle of the hand
     *
     * @param hand the hand from which it needs to read the roll angle
     * @return the roll angle in degrees
     */
    public float getRoll(Hand hand) {
        try {
            Vector lVector = hand.fingers().leftmost().tipPosition();
            Vector rVector = hand.fingers().rightmost().tipPosition();
            return (float) Math.toDegrees(Math.atan2(rVector.getY() - lVector.getY(), rVector.getX() - lVector.getX()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }

    /**
     * This method returns the yaw angle of the hand
     *
     * @param hand the hand from which it needs to read the yaw angle
     * @return the yaw angle in degrees
     */
    public float getYaw(Hand hand) {
        try {
            Vector middleFinger = hand.fingers().get(2).tipPosition();
            Vector palmCenter = hand.palmPosition();
            return (float) Math.toDegrees(Math.atan2(palmCenter.getX() - middleFinger.getX(), palmCenter.getZ() - middleFinger.getZ()));

        } catch (NullPointerException npe) {
            return 0;
        }
    }
    
    /**
     * This method returns the velocity of the hand passed as the parameter
     * @param hand the hand whose velocity is requested
     * @return the velocity of the hand
     */
    public float getHandSpeedY(Hand hand) {
        if (hand != null && hand.isValid()) {
            return hand.palmVelocity().getY();
        } else {
            return 0;
        }
    }

    /**
     * This method calculates the delta value between the height of the left
     * hand from the current frame and the previous one
     *
     * @return the delta of the Y value between the current and the last frame
     */
    public float getDeltaY() {
        return getHandY(getLeftHand(currentFrame)) - getHandY(getLeftHand(lastFrame));
    }

    /**
     * Setter method for the currentFrame variable
     *
     * @param frame the frame to set
     */
    public synchronized void setFrame(Frame frame) {
        this.lastFrame = this.currentFrame;
        this.currentFrame = frame;

    }

    /**
     * Getter method for the lastFrame variable
     *
     * @return the lastFrame variable
     */
    public Frame getLastFrame() {
        return this.lastFrame;
    }

    /**
     * Getter method fro the currentFrame variable
     * @return the currentFrame variable
     */
    public synchronized Frame getCurrentFrame() {
        return currentFrame;
    }
}
