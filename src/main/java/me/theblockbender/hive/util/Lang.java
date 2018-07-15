/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.util;

import me.theblockbender.hive.Chat;
import org.bukkit.ChatColor;

public class Lang {
    private static Chat chat;

    public static void setChat(Chat chat) {
        Lang.chat = chat;
    }

    public static String format(String key) {
        return ChatColor.translateAlternateColorCodes('&', chat.messages.getString(key, key).replaceAll("%arrow%", "\u00BB"));
    }
}
