package com.example.kentstringer.bfg.models;

public class Monster {
    private int level;
    private long experience;
    private int counterAttackPwr;

    public Monster(int level, long experience, int counterAttackPwr) {
        this.level = level;
        this.experience = experience;
        this.counterAttackPwr = counterAttackPwr;
    }
    public Monster(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExperience() {
        return experience;
    }

    public void setExperience(long experience) {
        this.experience = experience;
    }

    public int getCounterAttackPwr() {
        return counterAttackPwr;
    }

    public void setCounterAttackPwr(int counterAttackPwr) {
        this.counterAttackPwr = counterAttackPwr;
    }
}
