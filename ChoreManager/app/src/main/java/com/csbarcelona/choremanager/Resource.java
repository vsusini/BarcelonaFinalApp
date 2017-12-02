package com.csbarcelona.choremanager;

/**
 * Created by Bukola on 12/1/2017.
 */

public class Resource {

    private String _resourceName;
    private String _id;

    public Resource(){

    }

    public Resource(String resourceName){
        _resourceName = resourceName;
    }

    public Resource (String id, String resourceName){
        _resourceName = resourceName;
        _id = id;
    }

    public String getResourceName() {
        return _resourceName;
    }

    public String getID(){ return _id;}

    public void setResourceName(String resourceName) {
        this._resourceName = resourceName;
    }

    public void setId(String id){_id = id;}
}
