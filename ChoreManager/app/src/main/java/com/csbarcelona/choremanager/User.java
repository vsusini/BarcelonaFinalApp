package com.csbarcelona.choremanager;

/**
 * Created by Bukky on 11/30/2017.
 */


import java.util.HashMap;
import java.util.Map;

public class User {
    String name;
    String group;
    float totalPoints;
    String username;

    public User(String name, String group, String username){
        this.name = name;
        this.group = group;
        this.username = username;
        totalPoints = 0;
    }

    public String getName(){
        return name;
    }

    public String getGroup(){
        return group;
    }

    public String getUsername(){
        return username;
    }

    public float getTotalPoints(){
        return totalPoints;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setGroup(String group){
        this.group = group;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setTotalPoints(float totalPoints){
        this.totalPoints = totalPoints;
    }

    /**public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("courseName", courseName);
        return result;
    }
     */

}
