package com.binarybrains.sprout.item.artifact;

/**
 * Created by erikl on 19/09/17.
 */
public class Artifact {

    public final String name;
    public final String description;

    public Artifact(String name, String description) {
        if (name.length() > 16) throw new RuntimeException("Artifact name cannot be longer than 16 characters!");
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
