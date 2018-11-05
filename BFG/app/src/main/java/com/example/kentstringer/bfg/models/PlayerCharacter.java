package com.example.kentstringer.bfg.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerCharacter {


    private String workoutClass;
    private String pcName;
    private int squatPwr;
    private int lungePwr;
    private int burpeePwr;
    private int shadowBoxingPwr;
    private int sprintPwr;
    private int squatChance;
    private int lungeChange;
    private int burpeeChance;
    private int shadowChance;
    private int sprintChance;

    public PlayerCharacter(String workoutClass, int squatPwr, int lungePwr, int burpeePwr, int shadowBoxingPwr, int sprintPwr, int squatChance, int lungeChange, int burpeeChance, int shadowChance, int sprintChance) {
        this.workoutClass = workoutClass;
        this.squatPwr = squatPwr;
        this.lungePwr = lungePwr;
        this.burpeePwr = burpeePwr;
        this.shadowBoxingPwr = shadowBoxingPwr;
        this.sprintPwr = sprintPwr;
        this.squatChance = squatChance;
        this.lungeChange = lungeChange;
        this.burpeeChance = burpeeChance;
        this.shadowChance = shadowChance;
        this.sprintChance = sprintChance;
    }

    public PlayerCharacter() {
    }

    public String getWorkoutClass() {
        return workoutClass;
    }

    public void setWorkoutClass(String workoutClass) {
        this.workoutClass = workoutClass;
    }

    public int getSquatPwr() {
        return squatPwr;
    }

    public void setSquatPwr(int squatPwr) {
        this.squatPwr = squatPwr;
    }

    public int getLungePwr() {
        return lungePwr;
    }

    public void setLungePwr(int lungePwr) {
        this.lungePwr = lungePwr;
    }

    public int getBurpeePwr() {
        return burpeePwr;
    }

    public void setBurpeePwr(int burpeePwr) {
        this.burpeePwr = burpeePwr;
    }

    public int getShadowBoxingPwr() {
        return shadowBoxingPwr;
    }

    public void setShadowBoxingPwr(int shadowBoxingPwr) {
        this.shadowBoxingPwr = shadowBoxingPwr;
    }

    public int getSprintPwr() {
        return sprintPwr;
    }

    public void setSprintPwr(int sprintPwr) {
        this.sprintPwr = sprintPwr;
    }

    public int getSquatChance() {
        return squatChance;
    }

    public void setSquatChance(int squatChance) {
        this.squatChance = squatChance;
    }

    public int getLungeChange() {
        return lungeChange;
    }

    public void setLungeChange(int lungeChange) {
        this.lungeChange = lungeChange;
    }

    public int getBurpeeChance() {
        return burpeeChance;
    }

    public void setBurpeeChance(int burpeeChance) {
        this.burpeeChance = burpeeChance;
    }

    public int getShadowChance() {
        return shadowChance;
    }

    public void setShadowChance(int shadowChance) {
        this.shadowChance = shadowChance;
    }

    public int getSprintChance() {
        return sprintChance;
    }

    public void setSprintChance(int sprintChance) {
        this.sprintChance = sprintChance;
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getPcName());
            jsonObject.put("squatPwr", getSquatPwr());
            jsonObject.put("lungePwr", getLungePwr());
            jsonObject.put("burpeePwr", getBurpeePwr());
            jsonObject.put("shadowPwr", getShadowBoxingPwr());
            jsonObject.put("squatChance", getSquatChance());
            jsonObject.put("lungeChance", getLungeChange());
            jsonObject.put("burpeeChance", getBurpeeChance());
            jsonObject.put("shadowChance", getShadowChance());
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }


    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }
}
