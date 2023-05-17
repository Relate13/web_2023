package com.example.nju_tube.ui;

public class UserItem {
    private final int id;
    private final String name;

    public UserItem(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public long getId() {
        return id;
    }
    public String getName() {
        return this.name;
    }
}
