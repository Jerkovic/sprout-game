package com.binarybrains.sprout.achievement;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.award.Award;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.Stats;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Achievement implements Telegraph
{
    public static final Map<String, Achievement> achievements = new HashMap<String,Achievement>();

    static {
        try {
            achievements.put("gardenerTest",
                    new Achievement("Gardener test", "Prove you are a true zombie slayer.")
                            .addUnlockCriteria("Collect an orange", "Orange", 1)
                            .addUnlockCriteria("Get 10 potatoes", "Potato", 1)
            );
            achievements.put("potatofarmer",
                    new Achievement("Potato Farmer", "get 10 potatoes")
                            .addUnlockCriteria("Get 10 potatoes", "Potato", 10)
                            // .addAward() // this award does not work... it gets awarded directly on creation here
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String name;
    private String desc;
    public ArrayList <UnlockCriteria> unlockCriterias;
    public ArrayList <Award> awards;
    private boolean unlocked;

    public Achievement(String theId, String desc) {
        this.name = theId;
        this.desc = desc;
        this.unlockCriterias = new ArrayList<UnlockCriteria>();
        this.awards = new ArrayList<Award>();
        this.unlocked = false;

        MessageManager.getInstance().addListeners(this,
                TelegramType.PLAYER_STATS_UPDATED
        );
    }

    public String getName() {
        return name;
    }

    /**
     *
     * @param stats
     * @return
     */
    private boolean shallBeAwarded(Stats stats)
    {
        if (unlocked) return false;

        for (UnlockCriteria criteria : unlockCriterias) {
            try {
                Integer value = stats.get(criteria.getStatKey());
                criteria.setCurrentValue(value);
                Float temp = ((float)criteria.getCurrentValue() / (float)criteria.getValueNeeded());
                criteria.progression = Math.min(temp, 1.0f);
                if (value >= criteria.getValueNeeded()) {
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

    public Achievement addAward() { // todo params
        awards.add(new Award()); //this unlocks the basic_key
        return this;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

    /**
     *
     * @param player
     */
    public static void checkAwards(Player player)
    {
        for (Achievement achievement : achievements.values()) {
            if (achievement.shallBeAwarded(player.getStats())) {
                MessageManager.getInstance().dispatchMessage(achievement, TelegramType.PLAYER_ACHIEVEMENT_UNLOCKED);
            }
        }
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", unlockCriterias=" + unlockCriterias +
                ", unlocked=" + unlocked +
                ", awards=" + awards +
                '}';
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        if (msg.message == TelegramType.PLAYER_STATS_UPDATED) {
            Achievement.checkAwards((Player) msg.sender);
            return true;
        }
        return false;
    }
}