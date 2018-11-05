package com.example.kentstringer.bfg.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class User {
    private String name;
    private int level;
    private long experience;
    private double totalDistanceRun;
    private int totalMonsterKilled;
    private ArrayList<PlayerCharacter> playerCharacters = new ArrayList<>();
    private PlayerCharacter activePlayerCharacter;

    public PlayerCharacter getActivePlayerCharacter() {
        return activePlayerCharacter;
    }

    public void setActivePlayerCharacter(PlayerCharacter activePlayerCharacter) {
        this.activePlayerCharacter = activePlayerCharacter;
    }

    public User(String name, int level, long experience, ArrayList<PlayerCharacter> playerCharacters, double totalDistanceRun, int totalMonsterKilled) {
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.playerCharacters = playerCharacters;
        this.totalDistanceRun = totalDistanceRun;
        this.totalMonsterKilled = totalMonsterKilled;
        if (playerCharacters.size() >= 1) {
            activePlayerCharacter = playerCharacters.get(0);
        }
    }

    public User() {}

    public void AddNewCharacter(PlayerCharacter c){
        playerCharacters.add(c);
    }

    public PlayerCharacter GetCharacter(int i){
        if (i < playerCharacters.size()){
            return playerCharacters.get(i);
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

    public ArrayList<PlayerCharacter> getPlayerCharacters() {
        return playerCharacters;
    }

    public void setPlayerCharacters(ArrayList<PlayerCharacter> playerCharacters) {
        this.playerCharacters = playerCharacters;
    }
    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("xp", getExperience());
            jsonObject.put("level", getLevel());
            jsonObject.put("name", getName());
            jsonObject.put("kills", getTotalMonsterKilled());
            jsonObject.put("distance", getTotalDistanceRun());
            jsonObject.put("characters", getPlayerCharacters());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
