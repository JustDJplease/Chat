package com.slaghoedje.acechat.util;

import java.util.Objects;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.ChatFormat;

public class FormatConfigParser {
    private static AceChat aceChat;

    public static void setAceChat(AceChat aceChat) {
        FormatConfigParser.aceChat = aceChat;
    }

    public static ChatFormat parseSingleFormat(String configLocation, String def) {
        String formatName = aceChat.config.getString(configLocation, def);
        if(formatName.equalsIgnoreCase("none")) return null;
        return aceChat.chatFormats.get(formatName);
    }

    public static ChatFormat parseMultiFormat(Player toCheck, String configLocation, String def) {
        if(!aceChat.config.isConfigurationSection(configLocation)) return parseSingleFormat(configLocation, def);
        ConfigurationSection configurationSection = aceChat.config.getConfigurationSection(configLocation);
        String group = Permissions.permissions.getPrimaryGroup(toCheck);

        String defaultFormat = aceChat.config.getString(configLocation +  ".default", def);
        for(String key : configurationSection.getKeys(false)) {
            if(key.equalsIgnoreCase(group)) {
                return parseSingleFormat(configLocation + "." + key, defaultFormat);
            }
        }

        return aceChat.chatFormats.get(defaultFormat);
    }
}
