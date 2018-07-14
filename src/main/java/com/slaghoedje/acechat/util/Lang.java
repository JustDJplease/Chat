package com.slaghoedje.acechat.util;

import org.bukkit.ChatColor;

import com.slaghoedje.acechat.AceChat;

public class Lang {
    private static AceChat aceChat;

    public static void setAceChat(AceChat aceChat) {
        Lang.aceChat = aceChat;
    }

    public static String format(String key) {
        return ChatColor.translateAlternateColorCodes('&', aceChat.messages.getString(key, key).replaceAll("%arrow%", "\u00BB"));
    }
}
