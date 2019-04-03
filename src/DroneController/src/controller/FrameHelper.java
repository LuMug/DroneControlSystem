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
        Hand rhand = getFrame().hands().rightmost();
        if (rhand.isRight()) {
            return rhand;
        } else {
            return null;
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

    public Hand getLeftHand() {
        Hand lhand = getFrame().hands().rightmost();
        if (lhand.isLeft()) {
            return lhand;
        } else {
            return null;
        }
    }

    public Hand getLeftHand(Frame frame) {

        Hand lhand = frame.hands().rightmost();
        if (lhand.isLeft()) {
            return lhand;
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
//            System.err.println("[ERROR] Unable to get hand object from frame");
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
//            System.err.println("[ERROR] Unable to get hand object from frame");
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

    public float getHandSpeedY(Hand hand) {
        if (hand != null) {
            return hand.palmVelocity().getY();
        } else {
            return 0;
        }
    }

    public float getDeltaY() {
        return getHandY(getLeftHand(currentFrame)) - getHandY(getLeftHand(lastFrame));
    }

    public float getDeltaRoll() {
        return getRoll(getRightHand(currentFrame)) - getRoll(getRightHand(lastFrame));
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
