package com.binarybrains.sprout.hud.skilltree;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.hud.components.FilledRectangle;


public class SkillWindow extends Dialog implements Telegraph {

    TextureAtlas atlas;
    Skin skin;
    Player player;
    ScrollPane recipeTableScrollPane;

    public SkillWindow(Player player, String title, Skin skin) {
        super(title, skin.get("new-ui-win", WindowStyle.class));
        setSkin(skin);

        this.player = player;
        this.skin = skin;
        this.atlas = SproutGame.assets.get("items2.txt");

        setKeepWithinStage(true);
        setMovable(false);
        setModal(true);
        build();
    }

    public void build() {
        clearChildren();
        Table recipesGroup = buildRecipesButtonGroup(skin);
        this.recipeTableScrollPane = new ScrollPane(recipesGroup, skin);
        recipeTableScrollPane.setFlickScroll(false);
        recipeTableScrollPane.setFadeScrollBars(false);
        recipeTableScrollPane.setFillParent(true);
        recipeTableScrollPane.setForceScroll(true, true);


        // container for our recipe scrollPane
        Table recipeContainer = new Table();
        recipeContainer.align(Align.top);
        recipeContainer.left();
        recipeContainer.add(recipeTableScrollPane).height(50.2f * 9).width(1000);

        row();
        add(recipeContainer);
        row();

        TextButton buttonExit = new TextButton("Close", skin, "text-button-default");
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                player.getLevel().screen.game.resume();
                player.getLevel().screen.hud.showMouseItem();

            }
        });
        add(buttonExit).pad(5);
        pack();

        //  auto scroller
        // recipeTableScrollPane.scrollTo(0, 200, 0, 200);
    }

    /**
     *
     * @param skin
     * @return
     */
    public Table buildRecipesButtonGroup(Skin skin) {

        Table recipeRowTable = new Table(skin);
        recipeRowTable.center().top();
        // recipeRowTable.debug();

        TextButton button = new TextButton("Root Skill", skin, "text-button-default");

        // row 1
        recipeRowTable.row();
        recipeRowTable.add(button).colspan(10).pad(16).expandX();



        // row 2 - tier 1
        recipeRowTable.row();
        TextButton button1;
        for(int jj=0; jj < 2; jj++) {
            button1 = new TextButton("Skill Tier 1", skin, "text-button-default");
            recipeRowTable.add(button1).colspan(5).pad(16);
        }
        // row 3 - tier 2
        recipeRowTable.row();
        for(int jj=0; jj < 10; jj++) {
            button1 = new TextButton("Skill Tier 2", skin, "text-button-default");
            button1.setDisabled(true);
            recipeRowTable.add(button1).pad(16);
        }

        recipeRowTable.pack();
        return recipeRowTable;

    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}