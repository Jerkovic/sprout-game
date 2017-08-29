package com.binarybrains.sprout;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.tweens.EntityAccessor;
import com.binarybrains.sprout.hud.tweens.ActorAccessor;
import com.binarybrains.sprout.screen.GameScreen;

public class SproutGame extends Game {

	public static String name = "Sprout";
	private static TweenManager tweenManager;
	public static AssetManager assets = new AssetManager();

	@Override
	public void create() {
        Gdx.app.log("Gdx version", com.badlogic.gdx.Version.VERSION);
		setTweenManager(new TweenManager());
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.registerAccessor(Entity.class, new EntityAccessor());
		loadAssets();
		//setScreen(new SplashScreen(this));
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

	@Override
	public void dispose() {

        super.dispose();
        assets.dispose();

	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
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
        assets.load("sfx/dog_woof.wav", Sound.class);
        assets.load("sfx/fancy_reward.wav", Sound.class);
        assets.load("sfx/craft_complete.wav", Sound.class);
        assets.load("sfx/watering.wav", Sound.class);

        assets.load("ambience/forest_morning_ambience.mp3", Sound.class);
        assets.load("music/track1.mp3", Music.class);
        assets.load("items2.txt", TextureAtlas.class);

		SproutGame.assets.finishLoading();
	}


}


