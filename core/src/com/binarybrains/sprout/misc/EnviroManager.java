package com.binarybrains.sprout.misc;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;
import com.badlogic.gdx.graphics.Color;


public class EnviroManager implements Telegraph {

    private static final EnviroManager instance = new EnviroManager();

    public final static long DAWN_TIME = 6; // 6:00 am
    public final static long DAY_TIME = 10; // noon 12:00pm
    public final static long DUSK_TIME = 18; // 6:00 pm
    public final static long NIGHT_TIME = 20; // 8:00 pm


    public final static Color DAWN_COLOR = new Color(102f / 255f,150f  / 255f,186f  / 255f, 1f);
    public final static Color DAY_COLOR = new Color(255f  / 255f,255f  / 255f,255f  / 255f, 1f); // Correct
    public final static Color DUSK_COLOR = new Color(126f  / 255f,75f  / 255f,104f / 255f, .5f);
    public final static Color NIGHT_COLOR = new Color(50f  / 255f,50f  / 255f,164f / 255f , .9f);

    private Level level;

    public static EnviroManager getInstance () {
        return instance;
    }

    public EnviroManager() {

    }

    public void init(Level level) {
        this.level = level;
        MessageManager.getInstance().addListeners(this,
                TelegramType.TIME_HOUR_INC
        );
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch (msg.message) {
            case TelegramType.TIME_HOUR_INC:

                GameTime.Gdt gameTime = (GameTime.Gdt) msg.extraInfo;

                if (gameTime.hour == DAWN_TIME) {
                    level.setAmbientColor(DAWN_COLOR);
                }
                if (gameTime.hour == DAY_TIME) {
                    level.setAmbientColor(DAY_COLOR);
                }
                if (gameTime.hour == DUSK_TIME) {
                    level.setAmbientColor(DUSK_COLOR);
                }
                if (gameTime.hour == NIGHT_TIME) {
                    level.setAmbientColor(NIGHT_COLOR);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + msg.message);
        }

        return false;
    }

}
