package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Items;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Crafting {

    public static final List<Recipe> anvilRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> cookingRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> furnaceRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> workbenchRecipes = new ArrayList<Recipe>();

    private List<Recipe> recipes;

    static {
        try {
            workbenchRecipes.add(new ResourceRecipe(Items.stick).addCost(Items.wood, 1));
            workbenchRecipes.add(new ToolRecipe(new Hoe(), 0).addCost(Items.stone, 1).addCost(Items.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new Axe(), 0).addCost(Items.stone, 1).addCost(Items.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new PickAxe(), 0).addCost(Items.stone, 2).addCost(Items.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new WateringCan(), 0).addCost(Items.ironBar, 5));
            workbenchRecipes.add(new ToolRecipe(new Scythe(), 0).addCost(Items.ironBar, 2).addCost(Items.stick, 2));
            workbenchRecipes.add(new ToolRecipe(new FishingPole(), 0).addCost(Items.ironBar, 1).addCost(Items.stick, 1).addCost(Items.string, 1));
            workbenchRecipes.add(new ToolRecipe(new Key(), 0).addCost(Items.ironBar, 2).setRemoveRecipeOnCrafted().setLocked());

            workbenchRecipes.add(new ResourceRecipe(Items.cloth).addCost(Items.wool, 3));
            workbenchRecipes.add(new ResourceRecipe(Items.cider).addCost(Items.apple, 17));

            // change to Furnace Recipes
            workbenchRecipes.add(new ResourceRecipe(Items.bomb).addCost(Items.ironOre, 4).addCost(Items.coal, 2));
            workbenchRecipes.add(new ResourceRecipe(Items.ironBar).addCost(Items.ironOre, 4).addCost(Items.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Items.goldIngot).addCost(Items.goldNugget, 4).addCost(Items.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Items.woodFence).addCost(Items.wood, 1));
            workbenchRecipes.add(new ResourceRecipe(Items.ladder).addCost(Items.stick, 6));

            //furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Items.sand, 4).addCost(Items.coal, 1));
            // ovenRecipes.add(new FoodRecipe(Food.pizza)

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Crafting(List<Recipe> recipes, Inventory inventory) {
        this.recipes = new ArrayList<Recipe>(recipes);

        for (int i = 0; i < recipes.size(); i++) {
            this.recipes.get(i).checkCanCraft(inventory);
        }

        // this should probably move to its own method
        Collections.sort(this.recipes, new Comparator<Recipe>() {
            public int compare(Recipe r1, Recipe r2) {
                if (r1.canCraft && !r2.canCraft) return -1;
                if (!r1.canCraft && r2.canCraft) return 1;
                return 0;
            }
        });
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Finds a recipe by name
     * @param name
     * @return
     */
    public Recipe findRecipeByName(String name) {

        for (int i = 0; i < getRecipes().size(); i++) {
            Recipe recipe = getRecipes().get(i);
            System.out.println(recipe.getItem().getName());
            if (recipe.getItem().getName().equals(name)) {
                return recipe;
            }
        }
        return null;
    }

    public boolean startCraft(Player player, int index) {
        Recipe recipe = recipes.get(index);
        recipe.checkCanCraft(player.getInventory());

        if (recipe.canCraft && player.getInventory().hasSpaceFor(recipe.getItem())) {
            recipe.deductCost(player.getInventory());
            recipe.craft(player.getInventory());
            if (recipe.shouldRemoveRecipeOnCrafted()) {
                recipes.remove(index);
            }
            return true;
        }

        return false;
    }

    public void debugCrafting() {
        for (int i = 0; i < getRecipes().size(); i++) {
            Recipe recipe = getRecipes().get(i);
            for (int ci = 0; ci < recipe.getCost().size(); ci++) {
                Item cost = recipe.getCost().get(ci);
                int cost_count = 1;
                if (cost instanceof ResourceItem)
                {
                    cost_count = ((ResourceItem) cost).count;
                }
                System.out.println("-" + cost.toString() + " x" + cost_count);
            }
        }
    }

}