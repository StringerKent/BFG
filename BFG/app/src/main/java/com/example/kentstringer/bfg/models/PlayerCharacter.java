package com.example.kentstringer.bfg.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PlayerCharacter implements Serializable {


    private String workoutClass;
    private String pcName = "Thor";

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

    private int hp;
    private int maxHp;
    private int level;

    private int monstersKilled;
    private double totalDistanceRan;

    private long experience;
    private long experienceNeeded;

    private long squatsComplete;
    private long lungesComplete;
    private long burpeesComplete;
    private long shadowboxingComplete;
    private long sprintsComplete;

    //loaded characters
    public PlayerCharacter(String workoutClass, String pcName, int squatPwr, int lungePwr,
                           int burpeePwr, int shadowBoxingPwr, int sprintPwr, int squatChance,
                           int lungeChange, int burpeeChance, int shadowChance, int sprintChance,
                           int hp, int maxHp, int level, int monstersKilled, double totalDistanceRan,
                           long experience, long experienceNeeded, long squatsComplete, long lungesComplete, long burpeesComplete, long shadowboxingComplete, long sprintsComplete) {
        this.workoutClass = workoutClass;
        this.pcName = pcName;
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
        this.hp = hp;
        this.maxHp = maxHp;
        this.level = level;
        this.monstersKilled = monstersKilled;
        this.totalDistanceRan = totalDistanceRan;
        this.experience = experience;
        this.experienceNeeded = experienceNeeded;
        this.squatsComplete = squatsComplete;
        this.lungesComplete = lungesComplete;
        this.burpeesComplete = burpeesComplete;
        this.shadowboxingComplete = shadowboxingComplete;
        this.sprintsComplete = sprintsComplete;
    }
    //new characters
    public PlayerCharacter(String workoutClass, String pcName, int squatPwr, int lungePwr,
                           int burpeePwr, int shadowBoxingPwr, int sprintPwr, int squatChance,
                           int lungeChange, int burpeeChance, int shadowChance, int sprintChance) {//for new characters
        this.workoutClass = workoutClass;
        this.pcName = pcName;
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
        setLevel(1);
        this.monstersKilled = 0;
        this.totalDistanceRan = 0;
        this.experience = 0;
        this.squatsComplete = 0;
        this.lungesComplete = 0;
        this.burpeesComplete = 0;
        this.shadowboxingComplete = 0;
        this.sprintsComplete = 0;
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

    public long getExperienceNeeded() {
        return experienceNeeded;
    }

    public void setExperienceNeeded(long experienceNeeded) {
        this.experienceNeeded = experienceNeeded;
    }

    public long getSquatsComplete() {
        return squatsComplete;
    }

    public void setSquatsComplete(long squatsComplete) {
        this.squatsComplete = squatsComplete;
        if (getSquatsComplete()%100 == 0){
            setSquatPwr((int)getSquatsComplete()/10 + 10);
        }
    }

    public long getLungesComplete() {
        return lungesComplete;
    }

    public void setLungesComplete(long lungesComplete) {
        this.lungesComplete = lungesComplete;
        if (getLungesComplete()%100 == 0){
            setLungePwr((int)getLungesComplete()/10 + 10);
        }
    }

    public long getBurpeesComplete() {
        return burpeesComplete;
    }

    public void setBurpeesComplete(long burpeesComplete) {
        this.burpeesComplete = burpeesComplete;
        if (getBurpeesComplete()%100 == 0){
            setBurpeePwr((int)getBurpeesComplete()/10 + 10);
        }
    }

    public long getShadowboxingComplete() {
        return shadowboxingComplete;
    }

    public void setShadowboxingComplete(long shadowboxingComplete) {
        this.shadowboxingComplete = shadowboxingComplete;
        if (getShadowboxingComplete()%100 == 0){
            setShadowBoxingPwr((int)getShadowboxingComplete()/10 + 10);
        }
    }

    public long getSprintsComplete() {
        return sprintsComplete;
    }

    public void setSprintsComplete(long sprintsComplete) {
        this.sprintsComplete = sprintsComplete;
        if (getSprintsComplete()%100 == 0){
            setSprintPwr((int)getSprintsComplete()/10 + 10);
        }
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", getPcName());
            jsonObject.put("squatPwr", getSquatPwr());
            jsonObject.put("lungePwr", getLungePwr());
            jsonObject.put("burpeePwr", getBurpeePwr());
            jsonObject.put("shadowPwr", getShadowBoxingPwr());
            jsonObject.put("sprintPwr", getSprintPwr());
            jsonObject.put("sprintChance", getSprintChance());
            jsonObject.put("sprintComplete", getSprintsComplete());
            jsonObject.put("squatChance", getSquatChance());
            jsonObject.put("lungeChance", getLungeChange());
            jsonObject.put("burpeeChance", getBurpeeChance());
            jsonObject.put("shadowChance", getShadowChance());
            jsonObject.put("squatComplete", getSquatsComplete());
            jsonObject.put("lungeComplete", getLungesComplete());
            jsonObject.put("burpeeComplete", getBurpeesComplete());
            jsonObject.put("shadowComplete", getShadowboxingComplete());
            jsonObject.put("level", getLevel());
            jsonObject.put("experience", getExperience());
            jsonObject.put("experienceNeeded", getExperienceNeeded());
            jsonObject.put("HP", getMaxHp());
            jsonObject.put("class", getWorkoutClass());
            jsonObject.put("monstersKilled", getMonstersKilled());
            jsonObject.put("totalDistanceRan", getTotalDistanceRan());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
        setHp(maxHp);
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        experienceNeeded = level * 1000;
        setMaxHp(level * 100);
    }

    public void levelUp(){
        int level = getLevel();
        setLevel(++level);
        setExperience(0);
    }

    public void killMonster(long xpEarned){
        int num = getMonstersKilled() + 1;
        setMonstersKilled(num);
        long experience = getExperience() + xpEarned;
        if (experience >= experienceNeeded){
            levelUp();
        }else{
            setExperience(experience);
        }
    }

    public void takeDmg(int dmg){
        int hp = getHp();
        setHp(hp - dmg);
    }

    public int getMonstersKilled() {
        return monstersKilled;
    }

    public void setMonstersKilled(int monstersKilled) {
        this.monstersKilled = monstersKilled;
    }

    public double getTotalDistanceRan() {
        return totalDistanceRan;
    }

    public void setTotalDistanceRan(double totalDistanceRan) {
        this.totalDistanceRan = totalDistanceRan;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getPercentageLife() {
        double percent = ((double)getHp()/(double)getMaxHp())*100;
        return (int)percent;
    }

}
