package com.binarybrains.sprout.structs;

import com.binarybrains.sprout.crafting.Crafting;
import com.binarybrains.sprout.crafting.Recipe;
import com.binarybrains.sprout.item.resource.Resources;

/*
 * See https://github.com/gt4dev/yet-another-tree-structure/blob/master/java/src/com/tree/SampleIterating.java
 */
class SampleData {

    // Unlock tree
    // Ability
    // Crafting Recipe
    //
    public static TreeNode<String> getSet1() {
        TreeNode<String> root = new TreeNode<String>("Woodwork");
        {
            
            TreeNode<String> node0 = root.addChild("Coppar Axe - Item");
            TreeNode<String> node1 = root.addChild("Wooden stick - Recipe");
            TreeNode<String> node2 = root.addChild("");
            {
                TreeNode<String> node20 = node2.addChild(null);
                TreeNode<String> node21 = node2.addChild("node21");
                {
                    TreeNode<String> node210 = node21.addChild("node210");
                    TreeNode<String> node211 = node21.addChild("node211");
                }
            }
            TreeNode<String> node3 = root.addChild("node3");
            {
                TreeNode<String> node30 = node3.addChild("node30");
            }
        }

        return root;
    }

    public static TreeNode<String> getSetSOF() {
        TreeNode<String> root = new TreeNode<String>("root");
        {
            TreeNode<String> node0 = root.addChild("node0");
            TreeNode<String> node1 = root.addChild("node1");
            TreeNode<String> node2 = root.addChild("node2");
            {
                TreeNode<String> node20 = node2.addChild(null);
                TreeNode<String> node21 = node2.addChild("node21");
                {
                    TreeNode<String> node210 = node20.addChild("node210");
                }
            }
        }

        return root;
    }
}
