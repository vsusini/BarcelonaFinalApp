package com.csbarcelona.choremanager;

/**
 * Created by Josh on 2017-11-28.
 */

public class Task {

    String _name;
    String _description;
    String _assignee;
    String _id;

    public Task(String name, String description, String assignee, String id ){
        this._name = name;
        this._description = description;
        this._assignee = assignee;
        this._id = id;
    }

    public String get_name(){
        return _name;
    }

    public String get_description(){
        return _description;
    }

    public String get_assignee(){
        return _assignee;
    }
    public Task(){

    }


}
