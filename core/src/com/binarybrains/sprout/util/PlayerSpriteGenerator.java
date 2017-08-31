package com.binarybrains.sprout.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;


public class PlayerSpriteGenerator {
    /**
     * This is  a test to the handle animation
     */

    float stateTime;
    Animation walkAnimation, walkArmsAnimation, walkPantsAnimation;
    TextureRegion hair;

    // move this to some util package
    public class SheetCoordinates {
        List<Vector2> cords = new ArrayList<Vector2>();

        public SheetCoordinates addCoordinates(float row, float col)
        {
            cords.add(new Vector2(col, row));
            return this;
        }

        public TextureRegion[] buildFrames(TextureRegion[][] spriteSheet) {
            TextureRegion[] frames = new TextureRegion[cords.size()];
            for (int i = 0; i < cords.size(); i++) {
                frames[i] = spriteSheet[(int) cords.get(i).y][(int) cords.get(i).x];
            }
            return frames;
        }
    }

    public PlayerSpriteGenerator() {
        TextureRegion[][] spriteSheet;
        Texture texture = new Texture(Gdx.files.internal("sdv_player.png"));
        spriteSheet = TextureRegion.split(texture, 16, 32);

        // create animation
        SheetCoordinates walk = new SheetCoordinates(); // our own custom class
        SheetCoordinates walkArms = new SheetCoordinates();
        SheetCoordinates walkPants = new SheetCoordinates();


        float frameSpeed = .15f;
        int row = 0;

        hair = spriteSheet[0][24];

        walk.addCoordinates(row,0)
                .addCoordinates(row,1)
                .addCoordinates(row,0)
                .addCoordinates(row,2);

        walkArms.addCoordinates(row,6)
                .addCoordinates(row,7)
                .addCoordinates(row,6)
                .addCoordinates(row,8);

        walkPants.addCoordinates(row,18)
                .addCoordinates(row,19)
                .addCoordinates(row,18)
                .addCoordinates(row,20);


        walkAnimation = new Animation(0f, walk.buildFrames(spriteSheet));
        walkAnimation.setFrameDuration(frameSpeed);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        walkArmsAnimation = new Animation(0f, walkArms.buildFrames(spriteSheet));
        walkArmsAnimation.setFrameDuration(frameSpeed);
        walkArmsAnimation.setPlayMode(Animation.PlayMode.LOOP);

        walkPantsAnimation = new Animation(0f, walkPants.buildFrames(spriteSheet));
        walkPantsAnimation.setFrameDuration(frameSpeed);
        walkPantsAnimation.setPlayMode(Animation.PlayMode.LOOP);

    }

    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        int offset = 1;
        TextureRegion currentFrame = (TextureRegion) walkAnimation.getKeyFrame(stateTime);
        TextureRegion currentPantsFrame = (TextureRegion) walkPantsAnimation.getKeyFrame(stateTime);
        TextureRegion currentArmsFrame = (TextureRegion) walkArmsAnimation.getKeyFrame(stateTime);

        // right order here is important
        batch.draw(currentPantsFrame, 100,500);
        batch.draw(currentFrame, 100,500);
        batch.draw(currentArmsFrame, 100,500);

        System.out.println(walkAnimation.getKeyFrameIndex(stateTime));
        if (walkAnimation.getKeyFrameIndex(stateTime) != 0) {
            offset = 2;
        }
        batch.draw(hair, 100, 500-offset);
    }

    public void update(float delta) {

    }
}

