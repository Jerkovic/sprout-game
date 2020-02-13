package com.binarybrains.sprout.quest;

import com.binarybrains.sprout.entity.Player;

public interface Requirement {
    boolean check(Player player);
}
