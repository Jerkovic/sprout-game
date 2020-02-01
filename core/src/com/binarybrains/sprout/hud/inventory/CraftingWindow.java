package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.crafting.Crafting;
import com.binarybrains.sprout.crafting.Recipe;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;


public class CraftingWindow extends Dialog implements Telegraph {

    TextureAtlas atlas;
    Skin skin;
    Crafting craft;
    Player player;
    ScrollPane recipeTableScrollPane;
    private float rememberScrollY = 0;

    public CraftingWindow(Player player, String title, Skin skin) {
        super("CRAFTING STATION", skin.get("new-ui-win", WindowStyle.class));
        setSkin(skin);

        this.player = player;
        this.skin = skin;
        this.craft = new Crafting(Crafting.workbenchRecipes, player.getInventory());
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
        recipeTableScrollPane.setForceScroll(false, true);
        recipeTableScrollPane.setWidth(500);


        // container for our recipe scrollPane
        Table recipeContainer = new Table();
        recipeContainer.align(Align.top);
        recipeContainer.left();
        recipeContainer.add(recipeTableScrollPane).height(50.2f * 9).width(800);

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

        recipeTableScrollPane.scrollTo(0, 200, 0, 200);
    }

    /**
     *
     * @param skin
     * @return
     */
    public Table buildRecipesButtonGroup(Skin skin) {

        Table recipeRowTable = new Table(skin);
        recipeRowTable.setWidth(600);
        recipeRowTable.left().top();

        int index = 0;
        craft.sortRecipes();
        for (Recipe recipe : craft.getRecipes()) {
            recipe.checkCanCraft(player.getInventory());

            // add error handling getRegion may throw NullPointerException
            Image icon = new Image(atlas.findRegion(recipe.getItem().getRegionId()));
            if (!recipe.canCraft) icon.setColor(0,0,0,.55f);
            if (!recipe.isUnlocked) icon.setColor(0,0,0,.05f);

            ImageButton button = new ImageButton(skin, "inventory-slot-btn");
            button.add(icon);

            if (!recipe.canCraft) button.setDisabled(true);
            button.setName("" + index++);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (craft.startCraft(player, Integer.parseInt(event.getTarget().getName()))) {
                        onCrafting();
                        // temp sound remake this.
                        SproutGame.playSound("menu_select", 1f, MathUtils.random(0.7f, 1.1f), 1f);
                        // ((Sound) SproutGame.assets.get("sfx/craft_complete.wav")).play(.1f);
                        player.getLevel().screen.hud.refreshInventory();

                    } else {
                        player.getLevel().screen.hud.addToasterMessage("Crafting" ,"Inventory is full!");
                    }
                }
            });

            recipeRowTable.add(button);

            // todo nice overlay over the row if the recipe is locked
            Label lc = new Label(recipe.getItem().getName() + " (Recipe unlocked: " + recipe.isUnlocked + ")", skin);
            lc.setAlignment(Align.left);
            recipeRowTable.add(lc).padLeft(8).left(); // this made my evening. the left() made the table look better!
            for (int ci = 0; ci < recipe.getCost().size(); ci++) {
                Item cost = recipe.getCost().get(ci);
                int cost_count = 1;
                if (cost instanceof ResourceItem)
                {
                    cost_count = ((ResourceItem) cost).count;
                }
                recipeRowTable.add(makeIngredientIconStack(cost.getRegionId(), cost_count)).padLeft(20).left();
            }
            recipeRowTable.row();
        }

        recipeRowTable.pack();
        return recipeRowTable;

    }

    private Stack makeIngredientIconStack(String name, int counter) {
        Stack stack = new Stack();

        TextureAtlas.AtlasRegion icon;

        icon = atlas.findRegion(name);
        Image image = new Image(icon);
        stack.add(image);

        Label lc = new Label(counter +"", skin);
        lc.setStyle(skin.get("small", Label.LabelStyle.class));
        lc.setAlignment(Align.bottomRight);

        Table overlay = new Table();
        overlay.add(lc).expand().fillX().bottom().left();
        stack.add(overlay);

        stack.layout();
        return stack;
    }

    private void onCrafting() {
        build();
        getStage().setScrollFocus(recipeTableScrollPane);
        player.getLevel().screen.hud.hideMouseItem();
    }

    public void setScrollFocus(Stage stage) {
        stage.setScrollFocus(recipeTableScrollPane);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}