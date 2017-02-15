package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.PlantableResource;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.Axe;
import com.binarybrains.sprout.item.tool.Hoe;
import com.binarybrains.sprout.item.tool.PickAxe;
import com.binarybrains.sprout.item.tool.WateringCan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Crafting {

    public static final List<Recipe> anvilRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> ovenRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> furnaceRecipes = new ArrayList<Recipe>();
    public static final List<Recipe> workbenchRecipes = new ArrayList<Recipe>();

    static {
        try {
            workbenchRecipes.add(new ToolRecipe(new Hoe(), 0).addCost(Resource.stone, 1).addCost(Resource.wood, 1));
            //workbenchRecipes.add(new ToolRecipe(new Hoe(), 2).addCost(Resource.goldIngot, 10).addCost(Resource.wood, 3));

            workbenchRecipes.add(new ToolRecipe(new Axe(), 0).addCost(Resource.stone, 1).addCost(Resource.wood, 1));
            workbenchRecipes.add(new ToolRecipe(new PickAxe(), 0).addCost(Resource.stone, 2).addCost(Resource.wood, 1));

            workbenchRecipes.add(new ToolRecipe(new WateringCan(), 0).addCost(Resource.ironBar, 5));

            // change to Furnace Recipes
            workbenchRecipes.add(new ResourceRecipe(Resource.ironBar).addCost(Resource.ironOre, 4).addCost(Resource.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resource.goldIngot).addCost(Resource.goldNugget, 4).addCost(Resource.coal, 1));
            workbenchRecipes.add(new ResourceRecipe(Resource.woodFence).addCost(Resource.wood, 1));

            //furnaceRecipes.add(new ResourceRecipe(Resource.glass).addCost(Resource.sand, 4).addCost(Resource.coal, 1));


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Recipe> recipes;

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

    public void startCraft(Player player, int index) {
        Recipe recipe = recipes.get(index);
        recipe.checkCanCraft(player.getInventory());
        if (recipe.canCraft) {
            recipe.deductCost(player.getInventory());
            recipe.craft(player.getInventory());

        }
    }


    public void debugCrafting() {
        for (int i = 0; i < getRecipes().size(); i++) {
            Recipe recipe = getRecipes().get(i);
            System.out.println(recipe.toString() + " Costs: ");
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