package com.binarybrains.sprout.item.tool;

public class Hammer extends Tool {
    public Hammer() {
        super("Hammer", "A hammer is a tool that delivers a sudden impact to an object.");
        setCoolDownTime(500); // half a second cooldown time
    }
}
