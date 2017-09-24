package com.binarybrains.sprout;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.tweens.EntityAccessor;
import com.binarybrains.sprout.hud.tweens.ActorAccessor;
import com.binarybrains.sprout.hud.tweens.CameraAccessor;
import com.binarybrains.sprout.misc.Camera;
import com.binarybrains.sprout.screen.GameScreen;

public class SproutGame extends Game {

	public static String name = "Sprout";
	private static TweenManager tweenManager;
	public static AssetManager assets = new AssetManager();
	private BitmapFont font;

	@Override
	public void create() {
        Gdx.app.log("LibGdx version:", com.badlogic.gdx.Version.VERSION);
		setTweenManager(new TweenManager());
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.registerAccessor(Entity.class, new EntityAccessor());
		Tween.registerAccessor(Camera.class, new CameraAccessor());
		loadAssets();


		setScreen(new GameScreen(this));
	}

	public SproutGame() {
		super();
	}

	public static void setTweenManager(TweenManager tweenManager) {
		SproutGame.tweenManager = tweenManager;
	}

	public static TweenManager getTweenManager() {

		return tweenManager;
	}

	public BitmapFont getFont() {
		return font;
	}

	@Override
	public void dispose() {
        super.dispose();
        assets.dispose();
        getScreen().dispose();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

    public static void playSound(String name) {
        ((Sound) SproutGame.assets.get("sfx/" + name + ".wav")).play();
    }

    public static void playSound(String name, float volume) {
        ((Sound) SproutGame.assets.get("sfx/" + name + ".wav")).play(volume);
    }

    /**
     *
     * @param name
     * @param volume
     * @param pitch the pitch multiplier, 1 == default, >1 == faster, <1 == slower, the value has to be between 0.5 and 2.0
     * @param pan
     */
    public static void playSound(String name, float volume, float pitch, float pan) {
        ((Sound) SproutGame.assets.get("sfx/" + name + ".wav")).play(volume, pitch, pan);
    }

    public void loadAssets() {
		assets.load("sfx/chopping_Wood_1.wav", Sound.class);
        assets.load("sfx/chopping_Wood_2.wav", Sound.class);
		assets.load("sfx/blop.wav", Sound.class);
        assets.load("sfx/door_open.wav", Sound.class);
        assets.load("sfx/door_close1.wav", Sound.class);
		assets.load("sfx/water_splash.wav", Sound.class);
		assets.load("sfx/powerup.wav", Sound.class);
        assets.load("sfx/bomb_explosion.wav", Sound.class);
        assets.load("sfx/dog_woof.wav", Sound.class); // replace
        assets.load("sfx/fancy_reward.wav", Sound.class);
        assets.load("sfx/craft_complete.wav", Sound.class);
        assets.load("sfx/dirt_digging.wav", Sound.class);
        assets.load("sfx/watering.wav", Sound.class);
        assets.load("sfx/grass_walk.wav", Sound.class);
		assets.load("sfx/leaves_rustling.wav", Sound.class);
		assets.load("sfx/yawning.wav", Sound.class);
		assets.load("sfx/garbage_can.wav", Sound.class);
		assets.load("sfx/magic_upgrade.wav", Sound.class);
		assets.load("sfx/button_click.wav", Sound.class);
		assets.load("sfx/speaker_blip.wav", Sound.class);
		assets.load("sfx/break_stone.wav", Sound.class);
		assets.load("sfx/swing_03.wav", Sound.class);
		assets.load("sfx/metal_hit.wav", Sound.class);
		assets.load("sfx/small_ground_hit.wav", Sound.class);
		assets.load("sfx/tree_fall.wav", Sound.class);

		// todo find snoring
		// Alot of different sounds https://www.youtube.com/watch?v=WL3AeICxSuU
		// Magic want ..wingggg https://www.youtube.com/watch?v=rvtiLLJEMGs
		// Magic spell https://www.youtube.com/watch?v=cJ8TOsuiH08
		// Magic blast https://www.youtube.com/watch?v=JRZYh-ZO9HY
		// Bubble pop https://www.youtube.com/watch?v=5ZCahoiTIds
        // More footsteps https://www.youtube.com/watch?v=wYREdw4nz4E
        // Open futuristic chest https://www.youtube.com/watch?v=n_Ug7hgd4kE
        // Open close chest https://www.youtube.com/watch?v=PnjKFzFQpfU
        // Rope stretch https://www.youtube.com/watch?v=UhtONqZ-nag

        // Ambience sounds
        assets.load("ambience/forest_morning_ambience.mp3", Sound.class);
        assets.load("ambience/forest_night_ambience.mp3", Sound.class);
        // https://www.youtube.com/watch?v=W8tVwiYsgHg
        // todo cave ambience


        // Music (check out Ross Bugden)
        assets.load("music/track1.mp3", Music.class);
        assets.load("music/track2.mp3", Music.class); // https://www.youtube.com/watch?v=je9bnuIqVVc
        // https://www.youtube.com/watch?v=9qk-vZ1qicI
        // https://www.youtube.com/watch?v=BnmglWHoVrk
        // https://www.youtube.com/watch?v=XVHVFwwuOa0
        // https://www.youtube.com/watch?v=OiVYAXKVh_k
        // https://www.youtube.com/watch?v=ZOrxwqvfD2E Scary

        //  Sprite items
        assets.load("items2.txt", TextureAtlas.class);

		SproutGame.assets.finishLoading();

	}

}


