package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
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
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;


public class CraftingWindow extends Dialog {

    //ButtonGroup group;
    TextureAtlas atlas;
    Inventory inventory;
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
        this.inventory = player.getInventory();
        this.craft = new Crafting(Crafting.workbenchRecipes, inventory);


        this.atlas = SproutGame.assets.get("items2.txt");

        setKeepWithinStage(true);
        setModal(true);
        setMovable(false);
        center();
        setResizable(false);
        setZIndex(200);

        setModal(true);


        // craft.debugCrafting();


        TextButton buttonExit = new TextButton("Close", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setVisible(false);
                //remove();

            }
        });

        // setPosition(300, 400);

        ButtonGroup Inventorygroup = buildInventoryButtonGroup(skin);

        Table itemTable = new Table(skin);
        for (int i = 0, n = Inventorygroup.getButtons().size; i < n; i++) {
            itemTable.add((Actor) Inventorygroup.getButtons().get(i));
            if ((i + 1) % 12 == 0) itemTable.row();
        }
        //itemTable.pack();

        // build or ItemTableScrollPane
        ScrollPane itemTableScrollPane = new ScrollPane(itemTable, skin);
        itemTableScrollPane.setFlickScroll(false);
        itemTableScrollPane.setFadeScrollBars(false);
        itemTableScrollPane.setForceScroll(false, false);
        // container for our scrollPane
        Table container = new Table();
        container.align(Align.top);
        container.center();
        //container.add(createTabs());
        container.add(itemTableScrollPane).height(54);


        // BUILD Recipes container test
        Table recipesGroup = buildRecipesButtonGroup(skin);



        // build or RecipeItemTableScrollPane
        ScrollPane recipeTableScrollPane = new ScrollPane(recipesGroup, skin);
        recipeTableScrollPane.setFlickScroll(false);
        recipeTableScrollPane.setFadeScrollBars(false);
        recipeTableScrollPane.setForceScroll(false, true);
        // container for our scrollPane
        Table recipeContainer = new Table();
        recipeContainer.align(Align.top);
        recipeContainer.add(recipeTableScrollPane).height(48*5).width(800);
        // end test

        add("Recipes").row();
        add(recipeContainer).row(); // recipe container
        add("Inventory").row();
        add(container).row(); // inventory container
        add(buttonExit).pad(5);

        pack();
    }

    public Table buildRecipesButtonGroup(Skin skin) {

        Table recipeRowTable = new Table(skin);
        recipeRowTable.left().top();

        recipeRowTable.debug();
        int index = 0;
        for (Recipe recipe : craft.getRecipes()) {

            //if (!recipe.canCraft) continue;

            //System.out.println(recipe.getItem().getRegionId());
            Button button = new Button(new Image(atlas.findRegion(recipe.getItem().getRegionId())), skin, "default");
            button.setName("" + index++);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println(event.getTarget().getName());
                    craft.startCraft(player, Integer.parseInt(event.getTarget().getName()));
                }
            });

            recipeRowTable.add(button);
            //recipeRowTable.padRight(40);


            Table lcTable = new Table();
            lcTable.setWidth(200);
            lcTable.align(Align.left);
            lcTable.left().top();
            lcTable.debug();
            Label lc = new Label(recipe.toString() + "\n" + recipe.getItem().getDescription(), skin);
            lc.setAlignment(Align.left);
            lc.setColor(Color.WHITE);
            lcTable.add(lc);
            //lcTable.pack();
            recipeRowTable.add(lcTable);
            // todo add ingredients list
            recipeRowTable.row();
        }

        recipeRowTable.pack();
        return recipeRowTable;

    }

    public ButtonGroup buildInventoryButtonGroup(Skin skin) {

        ButtonGroup group = new ButtonGroup();
        group.uncheckAll();


        for (Item item : inventory.getItems()) {

            Button button = new Button(new Image(atlas.findRegion(item.getRegionId())), skin, "default");

            Label lc = new Label("" + inventory.count(item), skin);
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
