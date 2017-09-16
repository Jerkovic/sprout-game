package com.binarybrains.sprout.entity;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Stats implements Serializable {

    private Map<String, Integer> dataMap = new HashMap<String, Integer>();
    private static final String PATH = "saved_game.txt";

    public void increase(String statKey, Integer increment) {
        if (dataMap.containsKey(statKey)) {
            dataMap.put(statKey, dataMap.get(statKey) + increment);
        } else {
            dataMap.put(statKey, increment);
        }
        debug();
    }

    public int get(String statKey) {
        if (dataMap.containsKey(statKey)) {
            return dataMap.get(statKey);
        }
        return 0;
    }

    public void debug() {
        System.out.println("-------------------Stats----------------------");
        for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    public void save() throws IOException {
        saveFile(dataMap);
    }

    private void saveFile(Map<String, Integer> users)  throws IOException  {

        ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(PATH));
        os.writeObject(users);

    }

    private Map<String, Integer> readFile()  throws ClassNotFoundException, IOException {
        ObjectInputStream is = new ObjectInputStream(new FileInputStream(PATH));
        return (Map<String, Integer>) is.readObject();
    }
}
