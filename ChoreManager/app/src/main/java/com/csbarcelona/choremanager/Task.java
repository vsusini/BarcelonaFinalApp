package com.csbarcelona.choremanager;

/**
 * Created by Josh on 2017-11-28.
 */

public class Task {

    String _assignee = "assignee";
    String _description = "description";
    String _dueDate = "";
    int _duration = 0;
    String _name= "name";
    int _points = 0;
    String _id = "";
    String _resources = "";
    String _group = "";
    String _durationUnits = "";
    String _status = "";
    String _repeat = "";

    public Task(String id, String assignee, String resources, String description,
                int duration, String name, int points, String group, String dueDate, String _durationUnits, String status, String repeat ){
        this._name = name;
        this._description = description;
        this._assignee = assignee;
        this._id = id;
        this._points = points;
        this._dueDate = dueDate;
        this._resources = resources;
        this._group = group;
        this._durationUnits = _durationUnits;
        this._status = status;
        this._repeat = repeat;
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

    public String get_dueDate() { return _dueDate; }
    public void set_dueDate(String dueDate){ _dueDate = dueDate; }

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

    public String get_durationUnits(){ return _durationUnits; }
    public void set_durationUnits(String durationUnits){ _durationUnits = durationUnits; }

    public String get_status(){ return _status; }
    public void set_status(String status){ _status = status; }

    public String get_repeat(){ return  _repeat; }
    public void set_repeat(String repeat){ _repeat = repeat; }
    public Task(){

    }


}
