package com.example.kentstringer.bfg.Exercises;

import com.example.kentstringer.bfg.models.PlayerCharacter;

public class Lunge extends Exercise {

    public Lunge(float accelTop, float accelBottom, int orientationTop, int getOrientationBottom, int timer, float forwardMovement, String name) {
        super(accelTop, accelBottom, orientationTop, getOrientationBottom, timer, forwardMovement, name);
    }

    @Override
    public boolean receiveSensorInformation(float accelX, float accelY, float accelZ, double orientation, double direction, PlayerCharacter pc){
        boolean isComplete = false;
        if(isHalfComplete()){
            isComplete = accelY > getAccelTop() && (orientation > getGetOrientationBottom() && orientation < getOrientationTop());
            if(isComplete){setHalfComplete(false);}
        }else{
           setHalfComplete(accelY < getAccelBottom() && direction > getForwardMovement() && (orientation > getGetOrientationBottom() && orientation < getOrientationTop()));
        }
        return isComplete;
    }

    @Override
    public int sendAttackInformation(PlayerCharacter pc){
        pc.setLungesComplete(pc.getLungesComplete()+1);
        return pc.getLungePwr();
    }
}
