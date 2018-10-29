package com.example.kentstringer.bfg.models;

import java.util.ArrayList;

public class User {
    private String name;
    private int level;
    private long experience;
    private double totalDistanceRun;
    private int totalMonsterKilled;
    private ArrayList<Character> characters = new ArrayList<>();


    public User(String name, int level, long experience, ArrayList<Character> characters, double totalDistanceRun, int totalMonsterKilled) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.characters = characters;
        this.totalDistanceRun = totalDistanceRun;
        this.totalMonsterKilled = totalMonsterKilled;
    }

    public User() {}

    public void AddNewCharacter(Character c){
        characters.add(c);
    }

    public Character GetCharacter(int i){
        if (i < characters.size()){
            return characters.get(i);
        }else{
            return null;
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public double getTotalDistanceRun() {
        return totalDistanceRun;
    }

    public void setTotalDistanceRun(double totalDistanceRun) {
        this.totalDistanceRun = totalDistanceRun;
    }

    public int getTotalMonsterKilled() {
        return totalMonsterKilled;
    }

    public void setTotalMonsterKilled(int totalMonsterKilled) {
        this.totalMonsterKilled = totalMonsterKilled;
    }

    public ArrayList<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(ArrayList<Character> characters) {
        this.characters = characters;
    }
}
