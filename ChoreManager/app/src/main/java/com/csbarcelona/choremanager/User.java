package com.csbarcelona.choremanager;

public class User {
    private String _name;

    private int _totalPoints = 0;
    private String _id;
    private String _username;
    private String _group;

    public User() {
    }
    public User(String name, String group, int totalPoints, String username, String id) {
        _name = name;
        _group = group;
        _id = id;
        _totalPoints = totalPoints;
        _username = username;
    }

    public void set_name(String name) {_name = name;}
    public String get_name() {
        return _name;
    }
    public void set_group(String group){_group=group;}
    public String get_group(){return _group;}
    public void set_id(String id){_id = id;}
    public String get_id(){return _id;}
    public void set_totalPoints(int totalPoints){_totalPoints = totalPoints; }
    public int get_totalPoints(){return _totalPoints;}
    public void set_username(String username){_username = username;}
    public String get_username(){return _username;}
}

