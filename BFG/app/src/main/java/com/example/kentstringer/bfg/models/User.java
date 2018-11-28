package com.example.kentstringer.bfg.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
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

    public void addNewCharacter(PlayerCharacter c){
        playerCharacters.add(c);
    }

    public PlayerCharacter GetCharacter(int i){
        if (i < playerCharacters.size()){
            return playerCharacters.get(i);
        }else{
            return null;
        }

    }

    public void removeCharacter(PlayerCharacter c){
        if (playerCharacters.contains(c)){
            playerCharacters.remove(c);
            if (c == getActivePlayerCharacter()){
                if (playerCharacters.size() == 0){
                    setActivePlayerCharacter(null);
                }else{
                    setActivePlayerCharacter(getPlayerCharacters().get(0));
                }
            }
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
        if (getExperience() >= 2500*getLevel()){
            setLevel(getLevel()+1);
        }
    }

    public double getTotalDistanceRun() {
        double total = 0;
        for (PlayerCharacter pc:playerCharacters) {
            total += pc.getTotalDistanceRan();
        }
        return total;
    }

    public void setTotalDistanceRun(double totalDistanceRun) {
        this.totalDistanceRun = totalDistanceRun;
        if(totalDistanceRun > 5280){
            int miles = (int)totalDistanceRun/5280;
            receiveXP(miles*200);
        }
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

    public void receiveXP(long xp){
        setExperience(getExperience() + xp);
    }
    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("xp", getExperience());
            jsonObject.put("level", getLevel());
            jsonObject.put("name", getName());
            jsonObject.put("kills", getTotalMonsterKilled());
            jsonObject.put("distance", getTotalDistanceRun());
            jsonObject.put("characterNum", getPlayerCharacters().size());

            for (int i = 0; i < getPlayerCharacters().size(); i++) {
                jsonObject.put("character" + i, getPlayerCharacters().get(i).toJSON());
            }

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
