package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.resource.Resources;
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
            workbenchRecipes.add(new ResourceRecipe(Resources.stick).addCost(Resources.wood, 1).setXP(10));

            workbenchRecipes.add(new ToolRecipe(new Hoe(), 0).addCost(Resources.stone, 1).addCost(Resources.stick, 1));

            // Axe upgrade recipes
            workbenchRecipes.add(new ToolUpgradeRecipe(Tools.axe, 1).addCost(Resources.copperBar, 4).addCost(Resources.stick, 1).setRemoveRecipeOnCrafted().setXP(50));
            workbenchRecipes.add(new ToolUpgradeRecipe(Tools.axe, 2).addCost(Resources.ironBar, 4).addCost(Resources.stick, 1).setRemoveRecipeOnCrafted().setXP(100));
            workbenchRecipes.add(new ToolUpgradeRecipe(Tools.axe, 3).addCost(Resources.goldIngot, 4).addCost(Resources.stick, 1).setRemoveRecipeOnCrafted().setXP(175));

            workbenchRecipes.add(new ToolRecipe(new PickAxe(), 0).addCost(Resources.stone, 2).addCost(Resources.stick, 1));
            workbenchRecipes.add(new ToolRecipe(new WateringCan(), 0).addCost(Resources.ironBar, 5));
            workbenchRecipes.add(new ToolRecipe(new Scythe(), 0).addCost(Resources.ironBar, 2).addCost(Resources.stick, 2));
            workbenchRecipes.add(new ToolRecipe(new FishingPole(), 0).addCost(Resources.ironBar, 1).addCost(Resources.stick, 1).addCost(Resources.string, 1));
            workbenchRecipes.add(new ToolRecipe(new Key(), 0).addCost(Resources.ironBar, 2).setRemoveRecipeOnCrafted().setLocked());


            workbenchRecipes.add(new ResourceRecipe(Resources.cloth).addCost(Resources.wool, 3).setXP(200));

            workbenchRecipes.add(new ResourceRecipe(Resources.cider).addCost(Resources.apple, 17).setXP(175));
            workbenchRecipes.add(new ResourceRecipe(Resources.cocktail).addCost(Resources.banana, 1).addCost(Resources.apple, 1).addCost(Resources.coconut, 1)); // todo add more


            // change to Furnace Recipes
            workbenchRecipes.add(new ResourceRecipe(Resources.bomb).addCost(Resources.ironOre, 4).addCost(Resources.coal, 2));
            workbenchRecipes.add(new ResourceRecipe(Resources.copperBar).addCost(Resources.copperOre, 4).addCost(Resources.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resources.ironBar).addCost(Resources.ironOre, 4).addCost(Resources.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resources.goldIngot).addCost(Resources.goldNugget, 4).addCost(Resources.coal, 1));


            workbenchRecipes.add(new ResourceRecipe(Resources.woodFence).addCost(Resources.wood, 1));
            workbenchRecipes.add(new ResourceRecipe(Resources.ladder).addCost(Resources.stick, 6));

            //furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Resources.sand, 4).addCost(Resources.coal, 1));
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

        sortRecipes();

    }

    public void sortRecipes() {
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
     * Find recipe by uid
     * @param id
     * @return
     */
    public static Recipe findRecipeByIdentifier(List<Recipe> searchRecipes, String id) {

        for (int i = 0; i < searchRecipes.size(); i++) {
            Recipe recipe = searchRecipes.get(i);
            if (recipe.getIdentifier().equals(id))
            {
                return recipe;
            }
        }
        return null;
    }

    /**
     * start crafting
     * @param player
     * @param index
     * @return
     */
    public boolean startCraft(Player player, int index) {
        Recipe recipe = recipes.get(index);
        recipe.checkCanCraft(player.getInventory());

        if (recipe.canCraft && player.getInventory().hasSpaceFor(recipe.getItem())) {
            recipe.deductCost(player.getInventory());
            recipe.craft(player.getInventory());
            player.increaseXP(recipe.getXpGain());
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