package com.binarybrains.sprout.achievement;

public class Property
{
    private String name;
    private double value;
    private String activation;
    private int activationValue;
    private int initialValue;

    public Property(String theName, int theInitialValue, String theActivation, int theActivationValue) {
        this.name = theName;
        this.activation = theActivation;
        this.activationValue = theActivationValue;
        this.initialValue = theInitialValue;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getActivation() {
        return activation;
    }

    public int getActivationValue() {
        return activationValue;
    }

    public boolean isActive() {
        boolean aRet = false;
        return aRet;
    }
}