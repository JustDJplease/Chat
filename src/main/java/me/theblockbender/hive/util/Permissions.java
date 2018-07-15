/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.util;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.CommandSender;

public class Permissions {
    public static boolean vault = false;
    public static Permission permissions = null;

    public static boolean has(CommandSender sender, String permission) {
        if(vault) return permissions.has(sender, permission);
        else return sender.hasPermission(permission);
    }
}
