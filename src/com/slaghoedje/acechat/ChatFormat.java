package com.slaghoedje.acechat;

import java.io.File;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatFormat {
    private final AceChat aceChat;
    private final String filename;

    private File file;
    private FileConfiguration fileConfiguration;

    public ChatFormat(AceChat aceChat, String filename) {
        this.aceChat = aceChat;
        this.filename = filename;

        loadConfig();
    }

    public void send(Player receiver, Player player, Player other, String message) {
        receiver.spigot().sendMessage(getJSONMessage(player, other, message));
    }

    public TextComponent getJSONMessage(Player player, Player other, String message) {
        TextComponent textComponent = new TextComponent();

        for(String partKey : fileConfiguration.getConfigurationSection("parts").getKeys(false)) {
            ConfigurationSection partSection = fileConfiguration.getConfigurationSection("parts." + partKey);
            String text = partSection.getString("text", "undefined");
            String hoverText = partSection.getString("hover", "");

            ClickEvent.Action clickAction = null;
            try {
                clickAction = ClickEvent.Action.valueOf(partSection.getString("click.type", ""));
            } catch(Exception ignored) { }

            String clickText = partSection.getString("click.text", "");

            text = format(player, other, message, text);
            hoverText = format(player, other, message, hoverText);
            clickText = format(player, other, message, clickText);

            TextComponent part = new TextComponent(text);

            if(!hoverText.isEmpty()) {
                part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
            }

            if(clickAction != null) {
                part.setClickEvent(new ClickEvent(clickAction, clickText));
            }

            textComponent.addExtra(part);
        }

        return textComponent;
    }

    private String format(Player player, Player other, String message, String toFormat) {
        toFormat = toFormat.replaceAll("%player%", player.getName());
        toFormat = toFormat.replaceAll("%player-nick%", player.getDisplayName());

        if(other != null) {
            toFormat = toFormat.replaceAll("%other%", other.getName());
            toFormat = toFormat.replaceAll("%other-nick%", other.getDisplayName());
        }

        if(aceChat.papiPresent) {
            toFormat = PlaceholderAPI.setPlaceholders(player, toFormat);
        }

        toFormat = toFormat.replaceAll("%message%", message);

        return toFormat;
    }

    private void loadConfig() {
        try {
            File formatsFolder = new File(aceChat.getDataFolder().getPath() + "\\formats");
            file = new File(formatsFolder, filename + ".yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();
                aceChat.saveResource(String.format("formats\\%s.yml", filename), false);
            }

            fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
