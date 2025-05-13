package com.gmail.markushygedombrowski.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.node.Node;

import java.util.UUID;

public class LuckPermsHelpers {
    private LuckPerms luckPerms;

    public LuckPermsHelpers(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    public void addPermission(UUID userUuid, String permission) {
        // Load, modify, then save
        luckPerms.getUserManager().modifyUser(userUuid, user -> {
            // Add the permission
            user.data().add(Node.builder(permission).build());
        });
    }
    public void addPermission(UUID userUuid, String permission,boolean value) {
        // Load, modify, then save
        luckPerms.getUserManager().modifyUser(userUuid, user -> {
            // Add the permission
            user.data().add(Node.builder(permission).build());
        });
    }
}
