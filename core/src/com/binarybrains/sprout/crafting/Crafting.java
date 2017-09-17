package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
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
            workbenchRecipes.add(new ResourceRecipe(Resource.stick).addCost(Resource.wood, 1));
            workbenchRecipes.add(new ToolRecipe(new Hoe(), 0).addCost(Resource.stone, 1).addCost(Resource.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new Axe(), 0).addCost(Resource.stone, 1).addCost(Resource.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new PickAxe(), 0).addCost(Resource.stone, 2).addCost(Resource.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new WateringCan(), 0).addCost(Resource.ironBar, 5));
            workbenchRecipes.add(new ToolRecipe(new Scythe(), 0).addCost(Resource.ironBar, 2).addCost(Resource.stick, 2));
            workbenchRecipes.add(new ToolRecipe(new FishingPole(), 0).addCost(Resource.ironBar, 1).addCost(Resource.stick, 1).addCost(Resource.string, 1));
            workbenchRecipes.add(new ToolRecipe(new Key(), 0).addCost(Resource.ironBar, 2).setRemoveRecipeOnCrafted());

            workbenchRecipes.add(new ResourceRecipe(Resource.cloth).addCost(Resource.wool, 3));
            workbenchRecipes.add(new ResourceRecipe(Resource.cider).addCost(Resource.apple, 17));

            // change to Furnace Recipes
            workbenchRecipes.add(new ResourceRecipe(Resource.bomb).addCost(Resource.ironOre, 4).addCost(Resource.coal, 2));
            workbenchRecipes.add(new ResourceRecipe(Resource.ironBar).addCost(Resource.ironOre, 4).addCost(Resource.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resource.goldIngot).addCost(Resource.goldNugget, 4).addCost(Resource.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resource.woodFence).addCost(Resource.wood, 1));
            workbenchRecipes.add(new ResourceRecipe(Resource.ladder).addCost(Resource.stick, 6));

            //furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Resource.sand, 4).addCost(Resource.coal, 1));
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