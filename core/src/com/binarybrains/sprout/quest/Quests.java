package com.binarybrains.sprout.quest;

import com.binarybrains.sprout.structs.TreeNode;

public class Quests {

    public static void sampleData() {
        
        TreeNode<Dialog> root = new TreeNode<>(new Dialog(1));
        {
            TreeNode<Dialog> node0 = root.addChild(new Dialog(11).setNpcText("Hello there!"));
            TreeNode<Dialog> node1 = root.addChild(new Dialog(12).setNpcText("How are you?"));
        }

    }
}
