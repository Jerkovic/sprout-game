package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
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
import com.binarybrains.sprout.screen.GameScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;


public class CraftingWindow extends Dialog {

    TextureAtlas atlas;
    Skin skin;
    Crafting craft;
    Player player;
    ScrollPane recipeTableScrollPane;
    private float rememberScrollY = 0;

    public CraftingWindow(Player player, String title, Skin skin) {
        super("Workbench Crafting", skin);

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
        row(); // recipe container

        TextButton buttonExit = new TextButton("   Close   ", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                player.getLevel().screen.gameState = GameScreen.GameState.RUN;
                player.getLevel().gameTimer.resume();

            }
        });
        add(buttonExit).pad(5);
        pack();

    }

    public Table buildRecipesButtonGroup(Skin skin) {

        Table recipeRowTable = new Table(skin);
        recipeRowTable.setWidth(600);
        recipeRowTable.left().top();
        //recipeRowTable.debug();

        int index = 0;
        for (Recipe recipe : craft.getRecipes()) {
            recipe.checkCanCraft(player.getInventory());

            Image icon = new Image(atlas.findRegion(recipe.getItem().getRegionId()));
            if (!recipe.canCraft) icon.setColor(0,0,0,.55f);

            Button button = new Button(icon, skin, "default");
            if (!recipe.canCraft) button.setDisabled(true);
            button.setName("" + index++);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (craft.startCraft(player, Integer.parseInt(event.getTarget().getName()))) {
                        //rememberScrollY = recipeTableScrollPane.getVisualScrollY();
                        onCrafting();
                        // temp sound remake this.
                        ((Sound) SproutGame.assets.get("sfx/craft_complete.wav")).play(.15f);
                    } else {
                        player.getLevel().screen.hud.addToasterMessage("Inventory" ,"Inventory is full!");
                    }
                }
            });

            recipeRowTable.add(button);

            Label lc = new Label(recipe.getItem().getName(), skin);
            lc.setAlignment(Align.left);
            recipeRowTable.add(lc).padLeft(8).left(); // this made my evening. the left() made the table look better!

            for (int ci = 0; ci < recipe.getCost().size(); ci++) {
                Item cost = recipe.getCost().get(ci);
                int cost_count = 1;
                if (cost instanceof ResourceItem)
                {
                    cost_count = ((ResourceItem) cost).count;
                }
                recipeRowTable.add(makeIngredientLabel(cost.toString() + " x" + cost_count)).padLeft(30).left();
            }
            recipeRowTable.row();
        }

        recipeRowTable.pack();
        return recipeRowTable;

    }

    private Label makeIngredientLabel(String text) {
        Label il = new Label(text, skin);
        il.setAlignment(Align.left);
        return il;
    }

    private void onCrafting() {
        build();
        //recipeTableScrollPane.setScrollY(100);
        getStage().setScrollFocus(recipeTableScrollPane);
    }

    public void setScrollFocus(Stage stage) {
        stage.setScrollFocus(recipeTableScrollPane);
    }
}