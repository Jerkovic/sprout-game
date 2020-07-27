package com.binarybrains.sprout.quest.dialog;

import com.binarybrains.sprout.quest.Dialog;
import com.binarybrains.sprout.structs.TreeNode;
import org.junit.Test;


public class DialogTreeTest {

    @Test
    public void debug() {
        TreeNode<Dialog> tree = new TreeNode<>(new Dialog(0).setNpcText("Hej root"));
        TreeNode<Dialog> child = tree.addChild(new Dialog(1).setNpcText("How are you?"));
        child.addChild(new Dialog(1).setNpcText("Good yeah? How is Sarah?"));

        for (TreeNode<Dialog> node : tree) {
            String indent = createIndent(node.getLevel());
            System.out.println(indent + node);
        }

    }

    private static String createIndent(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }
}