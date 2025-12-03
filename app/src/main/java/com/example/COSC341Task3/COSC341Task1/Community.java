package com.example.COSC341Task3.COSC341Task1;

import java.io.Serializable;


public class Community implements Serializable {    private String name;
    private String description;

    public Community(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
