package com.csbarcelona.choremanager;

/**
 * Created by Bukola on 12/1/2017.
 */

public class Resource {

    private String _resourceName;

    public Resource(){

    }

    public Resource(String resourceName){
        _resourceName = resourceName;
    }

    public String getResourceName() {
        return _resourceName;
    }

    public void setResourceName(String resourceName) {
        this._resourceName = resourceName;
    }
}
