package com.example.kentstringer.bfg.Exercises;

import com.example.kentstringer.bfg.models.PlayerCharacter;

public class ShadowBox extends Exercise {

    public ShadowBox(float accelTop, float accelBottom, int orientationTop, int getOrientationBottom, int timer, float forwardMovement, String name) {
        super(accelTop, accelBottom, orientationTop, getOrientationBottom, timer, forwardMovement, name);
    }

    @Override
    public boolean receiveSensorInformation(float accelX, float accelY, float accelZ, double orientation, double direction, PlayerCharacter pc){
        boolean isComplete = false;
        if(isHalfComplete()){
            isComplete = accelX > getAccelTop();
            if(isComplete){setHalfComplete(false);}
        }else{
           setHalfComplete(accelX < getAccelBottom());
        }

        return isComplete;
    }

    @Override
    public int sendAttackInformation(PlayerCharacter pc){
        pc.setShadowboxingComplete(pc.getShadowboxingComplete()+1);
        return pc.getShadowBoxingPwr();
    }
}
