package com.csbarcelona.choremanager;

/**
 * Created by Josh on 2017-11-28.
 */

public class User {
    String _group = "";
    String _id = "";
    String _name = "";
    int _totalPoints = 0;
    String _username = "";


    public User(String group, String id, String name, int totalPoints, String _username ) {
        _group = group;
        _id = id;
        _name = name;
        _totalPoints = totalPoints;
        _username = _username;
    }

    public String get_group(){ return _group; }
    public void set_group(String group){_group = group; }

    public String get_id(){ return _id; }
    public void set_id(String id){ _id = id; }

    public String get_name(){ return _name; }
    public void set_name(String name) {_name = name; }

    public int get_totalPoints(){ return  _totalPoints; }
    public void set_totalPoints(int totalPoints){ _totalPoints = totalPoints; }

    public String get_username(){ return _username; }
    public void set_username(String username){_username = username; }

    public User(){

    }


}
