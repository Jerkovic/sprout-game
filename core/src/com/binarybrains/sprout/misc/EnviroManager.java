package com.binarybrains.sprout.misc;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;

public class EnviroManager implements Telegraph {

    private static final EnviroManager instance = new EnviroManager();

    public final static long DAWN_TIME = 7; // 7:00 am
    public final static long DAY_TIME = 10; // noon 12:00pm
    public final static long DUSK_TIME = 18; // 6:00 pm
    public final static long NIGHT_TIME = 20; // 8:00 pm




    private Level level;

    public static EnviroManager getInstance () {
        return instance;
    }

    public EnviroManager() {

    }

    public void init(Level level) {
        this.level = level;
        MessageManager.getInstance().addListeners(this,
                TelegramType.TIME_MINUTE_INC,
                TelegramType.TIME_HOUR_INC
        );
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case TelegramType.TIME_MINUTE_INC:
                System.out.println("Minute changed " + msg.message);
                break;
            case TelegramType.TIME_HOUR_INC:
                GameTime.Gdt gameTime = (GameTime.Gdt) msg.extraInfo;
                if (gameTime.hour == DAWN_TIME) {
                    level.setAmbientColor(50, 50, 190, 1f); // Night
                    AmbienceSound.setSoundAndStart("forest_night_ambience");
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + msg.message);
        }

        return false;
    }

}
