package com.example.kentstringer.bfg.models;

public class Monster {
    private int level;
    private long experience;
    private int counterAttackPwr;
    private int hp;
    private int maxHp;

    public Monster(int level) {
        setLevel(level);
    }
    public Monster(){}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        setMaxHp(level*100);
        setExperience(level * 300);
        setCounterAttackPwr(level * 10);
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
    public void takeDmg(int dmg){
        setHp(getHp()-dmg);
    }
    public int getPercentageLife(){
        double percent = ((double)getHp()/(double)getMaxHp())*100;
        return (int)percent;
    }
}
