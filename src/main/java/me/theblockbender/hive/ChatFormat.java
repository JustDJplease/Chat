package me.theblockbender.hive;

import me.clip.placeholderapi.PlaceholderAPI;
import me.theblockbender.hive.util.Permissions;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class ChatFormat {
    private final Chat chat;
    private final String filename;
    private FileConfiguration fileConfiguration;

    ChatFormat(Chat chat, String filename) {
        this.chat = chat;
        this.filename = filename;

        loadConfig();
    }

    public void send(Player receiver, Player player, Player other, String message) {
        receiver.spigot().sendMessage(getJSONMessage(player, other, message));
    }

    public void broadcast(Player player, Player other, String message) {
        for (Player receiver : chat.getServer().getOnlinePlayers()) {
            send(receiver, player, other, message);
        }
    }

    private TextComponent getJSONMessage(Player player, Player other, String message) {
        TextComponent textComponent = new TextComponent("");
        for (String partKey : fileConfiguration.getConfigurationSection("parts").getKeys(false)) {
            ConfigurationSection partSection = fileConfiguration.getConfigurationSection("parts." + partKey);
            String text = partSection.getString("text", "undefined");
            String hoverText = "";
            Object hoverTextObject = partSection.get("hover");
            if (hoverTextObject instanceof String) hoverText = (String) hoverTextObject;
            else if (hoverTextObject instanceof List) {
                try {
                    //noinspection unchecked
                    List<String> hoverList = (List<String>) hoverTextObject;
                    hoverText = String.join("\n", hoverList);
                } catch (Exception ignored) {
                }
            }
            ClickEvent.Action clickAction = null;
            try {
                clickAction = ClickEvent.Action.valueOf(partSection.getString("click.type", ""));
            } catch (Exception ignored) {
            }
            String clickText = partSection.getString("click.text", "");
            text = format(player, other, message, text);
            hoverText = format(player, other, message, hoverText);
            clickText = format(player, other, message, clickText);
            TextComponent part = new TextComponent(TextComponent.fromLegacyText(text));
            if (!hoverText.isEmpty()) {
                part.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
            }
            if (clickAction != null) {
                part.setClickEvent(new ClickEvent(clickAction, clickText));
            }
            textComponent.addExtra(part);
        }
        return textComponent;
    }

    private String format(Player player, Player other, String message, String toFormat) {
        toFormat = ChatColor.translateAlternateColorCodes('&', toFormat);
        toFormat = toFormat.replaceAll("%player%", player.getName());
        toFormat = toFormat.replaceAll("%player-nick%", player.getDisplayName());
        toFormat = toFormat.replaceAll("%arrow%", "\u00BB");
        if (other != null) {
            toFormat = toFormat.replaceAll("%other%", other.getName());
            toFormat = toFormat.replaceAll("%other-nick%", other.getDisplayName());
            toFormat = toFormat.replaceAll("%other-world%", other.getWorld().getName());
        }
        if (chat.papiPresent) {
            toFormat = PlaceholderAPI.setPlaceholders(player, toFormat);
        }
        toFormat = toFormat.replaceAll("\\\\n", "\n");
        if (Permissions.has(player, "\tessentials.chat.color"))
            message = ChatColor.translateAlternateColorCodes('&', message);
        toFormat = toFormat.replaceAll("%message%", message);
        return toFormat;
    }

    private void loadConfig() {
        try {
            File formatsFolder = new File(chat.getDataFolder().getPath() + "\\formats");
            File file = new File(formatsFolder, filename + ".yml");
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.getParentFile().mkdirs();
                chat.saveResource(String.format("formats\\%s.yml", filename), false);
            }
            fileConfiguration = new YamlConfiguration();
            fileConfiguration.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
