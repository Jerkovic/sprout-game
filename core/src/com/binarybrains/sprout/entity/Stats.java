package com.binarybrains.sprout.entity;


import java.util.HashMap;
import java.util.Map;

public class Stats {

    private Map<String, Integer> dataMap = new HashMap<String, Integer>();

    public void increase(String statKey, Integer increment) {
        if (dataMap.containsKey(statKey)) {
            dataMap.put(statKey, dataMap.get(statKey) + increment);
        } else {
            dataMap.put(statKey, increment);
        }
    }

    public int get(String statKey) {
        if (dataMap.containsKey(statKey)) {
            return dataMap.get(statKey);
        }
        return 0;
    }
}
