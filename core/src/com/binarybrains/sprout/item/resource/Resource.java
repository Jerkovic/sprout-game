package com.binarybrains.sprout.item.resource;


public class Resource {

    public final String name;
    public final String description;

    public Resource(String name, String description) {
        if (name.length() > 12) throw new RuntimeException("Name cannot be longer than 12 characters!");
        this.name = name;
        this.description = description;
    }

}