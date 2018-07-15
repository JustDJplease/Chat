/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.util;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.ChatFormat;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class FormatConfigParser {
    private static Chat chat;

    public static void setChat(Chat chat) {
        FormatConfigParser.chat = chat;
    }

    public static ChatFormat parseSingleFormat(String configLocation, String def) {
        String formatName = chat.config.getString(configLocation, def);
        if(formatName.equalsIgnoreCase("none")) return null;
        return chat.chatFormats.get(formatName);
    }

    public static ChatFormat parseMultiFormat(Player toCheck, String configLocation, String def) {
        if (!chat.config.isConfigurationSection(configLocation)) return parseSingleFormat(configLocation, def);
        ConfigurationSection configurationSection = chat.config.getConfigurationSection(configLocation);
        String group = Permissions.permissions.getPrimaryGroup(toCheck);

        String defaultFormat = chat.config.getString(configLocation + ".default", def);
        for(String key : configurationSection.getKeys(false)) {
            if(key.equalsIgnoreCase(group)) {
                return parseSingleFormat(configLocation + "." + key, defaultFormat);
            }
        }

        return chat.chatFormats.get(defaultFormat);
    }
}
