package com.binarybrains.sprout.achievement;

import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Stats;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Achievement
{
    public  static final Map<String, Achievement> achievements = new HashMap<String,Achievement>();

    static {
        try {
            achievements.put("zombieSlayer1",
                    new Achievement("Zombie Slayer Level 1", "Prove you are a true zombie slayer.")
                            .addUnlockCriteria("Kill 10 Zombies", "zombie_kills", 10)
                            .addUnlockCriteria("Get 10 potatoes", "Potato", 10)
            );
            achievements.put("potatofarmer",
                    new Achievement("Potato Farmer", "get 10 potatoes")
                            .addUnlockCriteria("Get 10 potatoes", "Potato", 10)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String name;
    private String desc;
    public ArrayList <UnlockCriteria> unlockCriterias;
    private boolean unlocked;

    public Achievement(String theId, String desc) {
        this.name = theId;
        this.desc = desc;
        this.unlockCriterias = new ArrayList<UnlockCriteria>();
        this.unlocked = false;
    }

    public String getName() {
        return name;
    }

    public boolean shallBeAwarded(Stats stats)
    {
        if (unlocked) return false;

        for (UnlockCriteria criteria : unlockCriterias) {
            try {
                Object value = stats.get(criteria.getStatKey());
                criteria.setCurrentValue((Integer) value);
                Float temp = ((float)criteria.getCurrentValue() / (float)criteria.getValueNeeded());
                criteria.progression = Math.min(temp, 1.0f);
                if ((Integer) value >= criteria.getValueNeeded()) {
                    criteria.setUnlocked();
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }

        }
        for (UnlockCriteria criteria : unlockCriterias) {
            if (!criteria.isUnlocked()) {
                setUnlocked(false);
                return false;
            }
        }

        setUnlocked(true);
        return true;
    }

    public Achievement addUnlockCriteria(UnlockCriteria unlockCriteria) {
        unlockCriterias.add(unlockCriteria);
        return this;
    }

    public Achievement addUnlockCriteria(String name, String statKey, int valueNeeded) {
        unlockCriterias.add(new UnlockCriteria(name, statKey, valueNeeded));
        return this;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    public static void checkAwards(Stats player, Level level)
    {
        if (achievements.get("potatofarmer").shallBeAwarded(player))
        {
            //SproutGame.playSound("");
            String msg = achievements.get("potatofarmer").getName();
            System.out.println("New achievement:" + msg);
        }

    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", unlockCriterias=" + unlockCriterias +
                ", unlocked=" + unlocked +
                '}';
    }
}