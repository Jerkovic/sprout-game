package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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


public class CraftingWindow extends Window {

    TextureAtlas atlas;
    Skin skin;
    Crafting craft;
    Player player;

    public Table createTabs() {
        Table main = new Table();
        main.setFillParent(true);

        // Create the tab buttons
        HorizontalGroup group = new HorizontalGroup();
        final Button tab1 = new TextButton("Tab1", getSkin(), "toggle");
        final Button tab2 = new TextButton("Tab2", getSkin(), "toggle");
        final Button tab3 = new TextButton("Tab3", getSkin(), "toggle");
        group.addActor(tab1);
        group.addActor(tab2);
        group.addActor(tab3);
        main.add(group);
        main.row();
        return main;

    }

    public CraftingWindow(Player player, String title, Skin skin) {
        super("Workbench Crafting", skin);

        this.player = player;
        this.skin = skin;
        this.craft = new Crafting(Crafting.workbenchRecipes, player.getInventory());
        this.atlas = SproutGame.assets.get("items2.txt");


        setKeepWithinStage(true);
        setModal(true);
        setMovable(false);
        center();
        setResizable(false);
        setZIndex(200);

        // craft.debugCrafting();
        build();


    }


    public void build() {
        clearChildren();

        ButtonGroup Inventorygroup = buildInventoryButtonGroup(skin);
        Table itemTable = new Table(skin);
        for (int i = 0, n = Inventorygroup.getButtons().size; i < n; i++) {
            itemTable.add((Actor) Inventorygroup.getButtons().get(i));
            if ((i + 1) % 12 == 0) itemTable.row();
        }
        //itemTable.pack();
        // build our Inventory
        ScrollPane itemTableScrollPane = new ScrollPane(itemTable, skin);
        itemTableScrollPane.setFlickScroll(false);
        itemTableScrollPane.setFadeScrollBars(false);
        itemTableScrollPane.setForceScroll(false, false);
        // container for our scrollPane
        Table inventoryContainer = new Table();
        inventoryContainer.align(Align.top);
        inventoryContainer.center();
        //container.add(createTabs());
        inventoryContainer.add(itemTableScrollPane).height(54);


        // BUILD Recipes container test

        Table recipesGroup = buildRecipesButtonGroup(skin);
        // build or RecipeItemTableScrollPane
        ScrollPane recipeTableScrollPane = new ScrollPane(recipesGroup, skin);
        recipeTableScrollPane.setFlickScroll(false);
        recipeTableScrollPane.setFadeScrollBars(false);
        recipeTableScrollPane.setForceScroll(false, true);
        recipeTableScrollPane.setWidth(500);
        // container for our recipe scrollPane
        Table recipeContainer = new Table();
        recipeContainer.align(Align.top);
        recipeContainer.left();
        recipeContainer.add(recipeTableScrollPane).height(202).width(800);
        // end test

        row();
        //add(createTabs());
        //row();
        add(recipeContainer);
        row(); // recipe container
        add("Your Inventory:");
        row();
        add(inventoryContainer);
        row(); // inventory container'
        TextButton buttonExit = new TextButton("Close Window", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                // unpause the game
                //remove();

            }
        });
        add(buttonExit).pad(5);

        pack();
    }

    public Table buildRecipesButtonGroup(Skin skin) {

        Table recipeRowTable = new Table(skin);
        recipeRowTable.setWidth(600);
        //recipeRowTable.setFillParent(true);
        recipeRowTable.left().top();

        //recipeRowTable.debug();
        int index = 0;
        for (Recipe recipe : craft.getRecipes()) {

            //if (!recipe.canCraft) continue;
            Button button = new Button(new Image(atlas.findRegion(recipe.getItem().getRegionId())), skin, "default");
            button.setName("" + index++);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //System.out.println(event.getTarget().getName());
                    craft.startCraft(player, Integer.parseInt(event.getTarget().getName()));
                    onCrafting();

                }
            });

            recipeRowTable.add(button);

            Label lc = new Label(recipe.toString(), skin);
            lc.setAlignment(Align.left);
            recipeRowTable.add(lc).padLeft(8).left(); // this made my evening. the left() made the table look better!
            //recipeRowTable.debugTable();
            // todo improve the ingredients list
            for (int ci = 0; ci < recipe.getCost().size(); ci++) {
                Item cost = recipe.getCost().get(ci);
                int cost_count = 1;
                if (cost instanceof ResourceItem)
                {
                    cost_count = ((ResourceItem) cost).count;
                }
                recipeRowTable.add(cost.toString() + " x" + cost_count);
            }
            recipeRowTable.row();
        }

        recipeRowTable.pack();
        return recipeRowTable;

    }

    public void onCrafting() {
        //clearChildren();
        //syncInventory(inventory);
        build();
        centerMe();
    }

    public void centerMe() {
        //setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2), ??);
    }


    public ButtonGroup buildInventoryButtonGroup(Skin skin) {

        ButtonGroup group = new ButtonGroup();
        group.uncheckAll();


        for (Item item : player.getInventory().getItems()) {

            Button button = new Button(new Image(atlas.findRegion(item.getRegionId())), skin, "default");

            String counter = "";
            if (item instanceof ResourceItem) {
                counter = "" + player.getInventory().count(item);
            }

            Label lc = new Label(counter, skin);
            lc.setAlignment(Align.bottomRight);
            lc.setColor(Color.BLACK);
            button.add(lc);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // nothing right ?
                }
            });
            group.add(button);
        }

        group.setMinCheckCount(0);
        group.setMaxCheckCount(0);
        return group;
    }

}