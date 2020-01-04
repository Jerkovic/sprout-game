package com.binarybrains.sprout.award;

import com.binarybrains.sprout.crafting.Crafting;
import com.binarybrains.sprout.crafting.Recipe;

/**
 * NEEDS TO BE REWRITTEN! Created by erikl on 04/01/18.
 */
public class Award {

    // this wont work :)
    public Award() {

        // move to some rewarder method
        Recipe recipe = Crafting.findRecipeByIdentifier(Crafting.workbenchRecipes, "basic_key");
        if (recipe != null) {
            recipe.setUnlocked();

        } else {
            throw new RuntimeException("failed to find award");
        }

    }
}
