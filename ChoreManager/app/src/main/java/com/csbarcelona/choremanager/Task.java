package com.csbarcelona.choremanager;

/**
 * Created by Josh on 2017-11-28.
 */

public class Task {

    String _assignee = "assignee";
    String _description = "description";
    //String _dueDate = "";
    int _duration = 0;
    String _name= "name";
    int _points = 0;
    String _id = "";
    String _resources = "";
    String _group = "";

    public Task(String id, String assignee, String resources, String description, int duration, String name, int points, String group ){
        this._name = name;
        this._description = description;
        this._assignee = assignee;
        this._id = id;
        this._points = points;
//        this._dueDate = dueDate;
        this._resources = resources;
        this._group = group;
    }

    public String get_assignee(){return _assignee;}
    public void set_assignee(String assignee) { _assignee = assignee; }

    public String get_resources(){return _resources;}
    public void set_resources(String res) { _resources = res; }

    public String get_description(){
        return _description;
    }
    public void set_description(String description){ _description = description; }

    public String get_group() { return _group; }
    public void set_group(String group){_group = group; }

//    public String get_dueDate() { return _dueDate; }
//    public void set_dueDate(String dueDate){ _dueDate = dueDate; }

    public int get_duration() { return _duration; }
    public void set_duration(int duration){ _duration = duration; }

    public String get_name(){
        return _name;
    }
    public void set_name(String name){ _name = name; }

    public int get_points() { return _points; }
    public void set_points(int points){ _points = points; }

    public String get_id(){ return _id; }
    public void setId(String id){ _id = id; }

    public Task(){

    }


}
