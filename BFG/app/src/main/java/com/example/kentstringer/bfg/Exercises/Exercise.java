package com.example.kentstringer.bfg.Exercises;

import com.example.kentstringer.bfg.models.PlayerCharacter;

public class Exercise {
    private float accelTop;
    private float accelBottom;
    private int orientationTop;
    private int getOrientationBottom;
    private int timer;
    private String name;
    private float forwardMovement;
    private boolean halfComplete = false;

    public Exercise(float accelTop, float accelBottom, int orientationTop, int getOrientationBottom, int timer, float forwardMovement, String name) {
        this.accelTop = accelTop;
        this.accelBottom = accelBottom;
        this.orientationTop = orientationTop;
        this.getOrientationBottom = getOrientationBottom;
        this.timer = timer;
        this.forwardMovement = forwardMovement;
        this.name = name;
    }

    public Exercise() {
    }

    public float getForwardMovement() {
        return forwardMovement;
    }

    public float getAccelTop() {
        return accelTop;
    }

    public float getAccelBottom() {
        return accelBottom;
    }

    public int getOrientationTop() {
        return orientationTop;
    }

    public int getGetOrientationBottom() {
        return getOrientationBottom;
    }

    public int getTimer() {
        return timer;
    }

    public String getName() {
        return name;
    }

    public boolean isHalfComplete() {
        return halfComplete;
    }

    public void setHalfComplete(boolean halfComplete) {
        this.halfComplete = halfComplete;
    }

    public boolean receiveSensorInformation(float accelX, float accelY, float accelZ, double orientation, double direction, PlayerCharacter pc){
        return false;
    }

    public int sendAttackInformation(PlayerCharacter pc){
        return 0;
    }
}
