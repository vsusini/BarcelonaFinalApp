package com.example.sizum.userlist2;

public class User {
    private String _name;
    private String _description;

    public User() {
    }
    public User(String name, String description) {
        _name = name;
        _description = description;
    }

    public void setName(String name) {
        _name = name;
    }
    public String getName() {
        return _name;
    }
    public void setDescription(String description) {_description = description;}
    public String getDescription() {
        return _description;
    }
}

