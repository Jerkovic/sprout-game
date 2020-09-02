package com.binarybrains.sprout;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.tweens.EntityAccessor;
import com.binarybrains.sprout.hud.tweens.ActorAccessor;
import com.binarybrains.sprout.hud.tweens.CameraAccessor;
import com.binarybrains.sprout.misc.Camera;
import com.binarybrains.sprout.screen.LoadingScreen;

public class SproutGame extends Game {

	public static String name = "game";
	public static String SKIN_FILE = "skin/uiskin.json";

	private static TweenManager tweenManager;
	public static AssetManager assets = new AssetManager();

	private Skin skin;

	@Override
	public void create() {
        Gdx.app.log("LibGdx version:", com.badlogic.gdx.Version.VERSION);
        boolean isDesktop = (Gdx.app.getType() == Application.ApplicationType.Desktop);
        System.out.println("Desktop: " + isDesktop);
		setTweenManager(new TweenManager());
		Tween.setCombinedAttributesLimit(4);
		Tween.registerAccessor(Actor.class, new ActorAccessor());
        Tween.registerAccessor(Entity.class, new EntityAccessor());
		Tween.registerAccessor(Camera.class, new CameraAccessor());

		assets.load(SKIN_FILE, Skin.class);
		assets.finishLoadingAsset(SKIN_FILE);
		this.skin = assets.get(SKIN_FILE, Skin.class);

		// Yaml loader
		FileHandle[] files = Gdx.files.local("dialogue/").list();
		for(FileHandle file: files) {
			String text = file.readString();
            //Yaml yaml = new Yaml();
            //TestDialog obj = (TestDialog) yaml.load(text);
            //System.out.println("Loaded object type: " + obj.getClass());
            //System.out.println(obj.getId() + " " +  obj.getSay());
		}
		// end loader

		setScreen(new LoadingScreen(this));
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

	public Skin getSkin() {
		return skin;
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

	/**
	 * Load all assets
	 */
	public void loadAssets() {
		assets.load("new_ui_experiment/ui.atlas", TextureAtlas.class);

		assets.load("haley-sheet.png", Texture.class);
		assets.load("willy.png", Texture.class);
		assets.load("player/player_temp.png", Texture.class);
		assets.load("sprites/shadow.png", Texture.class);

		// trees 9x10 tiles 10 animation frames on 2 rows
		assets.load("world/grass_land/anims/tree1A_ss.png", Texture.class);

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
		assets.load("sfx/pickaxe_stone.wav", Sound.class);
		assets.load("sfx/small_ground_hit.wav", Sound.class);
		assets.load("sfx/tree_fall.wav", Sound.class);
		assets.load("sfx/bump_against.wav", Sound.class);
		assets.load("sfx/pickup_fanfar.wav", Sound.class);
		assets.load("sfx/menu_select.wav", Sound.class); // not used yet
		assets.load("sfx/fuse.wav", Sound.class);
		assets.load("sfx/splat.wav", Sound.class);
		assets.load("sfx/eating.wav", Sound.class);
		assets.load("sfx/cash_register.wav", Sound.class);
		assets.load("sfx/heartbeat.wav", Sound.class);
		assets.load("sfx/inventory_bag_open.wav", Sound.class);
		assets.load("sfx/inventory_close.wav", Sound.class);
		assets.load("sfx/blop2.wav", Sound.class);
		assets.load("sfx/god_morning.wav", Sound.class);

		assets.load("sfx/magic_swish.wav", Sound.class);
		assets.load("sfx/open_chest.wav", Sound.class);
		assets.load("sfx/close_chest.wav", Sound.class);
		assets.load("sfx/jump.wav", Sound.class);
		assets.load("sfx/slap_iron_clatter.wav", Sound.class);

		// todo find snoring
		// More footsteps https://www.youtube.com/watch?v=wYREdw4nz4E
        // Anvil sound https://www.youtube.com/watch?v=0L7Rsjyiqmc

        // Ambience sounds
        assets.load("ambience/forest_morning_ambience.mp3", Sound.class);
        assets.load("ambience/forest_night_ambience.mp3", Sound.class);
		//assets.load("ambience/cave_ambience.mp3", Sound.class);

		// Wolfman Sachs
		// And https://www.youtube.com/watch?v=Ic-a585qFMM&list=PL9aCqI0yupNZIeh9JlgZKOHii5OQTuCS6&index=13
        assets.load("music/track4.mp3", Music.class);
        assets.load("music/track2.mp3", Music.class); // https://www.youtube.com/watch?v=je9bnuIqVVc
		assets.load("music/track3.mp3", Music.class);
		assets.load("music/track1.mp3", Music.class);
        // https://www.youtube.com/watch?v=9qk-vZ1qicI
        // https://www.youtube.com/watch?v=BnmglWHoVrk
        // https://www.youtube.com/watch?v=XVHVFwwuOa0
        // https://www.youtube.com/watch?v=OiVYAXKVh_k
        // https://www.youtube.com/watch?v=ZOrxwqvfD2E Scary

        //  Sprite items
        assets.load("items2.txt", TextureAtlas.class);
	}
}


