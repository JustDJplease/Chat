package com.slaghoedje.acechat.util;

import org.bukkit.command.CommandSender;

import net.milkbowl.vault.permission.Permission;

public class Permissions {
    public static boolean vault = false;
    public static Permission permissions = null;

    public static boolean has(CommandSender sender, String permission) {
        if(vault) return permissions.has(sender, permission);
        else return sender.hasPermission(permission);
    }
}
