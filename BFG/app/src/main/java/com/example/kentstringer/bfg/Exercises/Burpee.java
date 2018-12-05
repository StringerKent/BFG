package com.example.kentstringer.bfg.Exercises;

import com.example.kentstringer.bfg.models.PlayerCharacter;

public class Burpee extends Exercise {

    public Burpee(float accelTop, float accelBottom, int orientationTop, int getOrientationBottom, int timer, float forwardMovement, String name) {
        super(accelTop, accelBottom, orientationTop, getOrientationBottom, timer, forwardMovement, name);
    }

    @Override
    public boolean receiveSensorInformation(float accelX, float accelY, float accelZ, double orientation, double direction, PlayerCharacter pc){
        boolean isComplete = false;
        if(isHalfComplete()){
            isComplete = orientation > getAccelBottom() && orientation < getAccelTop();
            if(isComplete){setHalfComplete(false);}
        }else{
           setHalfComplete(orientation > getGetOrientationBottom() && orientation < getOrientationTop());
        }

        return isComplete;
    }

    @Override
    public int sendAttackInformation(PlayerCharacter pc){
        pc.setBurpeesComplete(pc.getBurpeesComplete()+1);
        return pc.getBurpeePwr();
    }
}
