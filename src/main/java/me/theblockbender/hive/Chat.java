package me.theblockbender.hive;

import me.theblockbender.hive.commands.ChatCommand;
import me.theblockbender.hive.commands.MsgReplyCommand;
import me.theblockbender.hive.util.FormatConfigParser;
import me.theblockbender.hive.util.Lang;
import me.theblockbender.hive.util.Permissions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

//copyright

public class Chat extends JavaPlugin {
    public Map<String, ChatFormat> chatFormats;
    public FileConfiguration config;
    public FileConfiguration messages;
    public boolean papiPresent = false;

    public void onEnable() {
        loadConfig();
        FormatConfigParser.setChat(this);
        Lang.setChat(this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            papiPresent = true;
            getLogger().info("Hooked into PlaceholderAPI!");
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            Permissions.vault = true;
            RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
            Permissions.permissions = permissionProvider.getProvider();
            if (Permissions.permissions == null) {
                Permissions.vault = false;
                getLogger().info("Tried to hook into Vault, but no permission plugin found!");
            } else {
                getLogger().info("Hooked into Vault permissions!");
            }
        }
        loadChatFormats();
        registerEvents();
        MsgReplyCommand msgReplyCommandExecutor = new MsgReplyCommand(this);
        getCommand("chat").setExecutor(new ChatCommand(this));
        getCommand("tell").setExecutor(msgReplyCommandExecutor);
        getCommand("reply").setExecutor(msgReplyCommandExecutor);
    }

    private void loadConfig() {
        try {
            File configFile = new File(getDataFolder(), "config.yml");
            File messagesFile = new File(getDataFolder(), "messages.yml");
            if (!configFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                saveResource("config.yml", false);
            }
            if (!messagesFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                messagesFile.getParentFile().mkdirs();
                saveResource("messages.yml", false);
            }
            config = new YamlConfiguration();
            config.load(configFile);
            messages = new YamlConfiguration();
            messages.load(messagesFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadChatFormats() {
        chatFormats = new HashMap<>();
        File formatsDirectory = new File(getDataFolder(), "formats");
        if (!formatsDirectory.exists()) //noinspection ResultOfMethodCallIgnored
            formatsDirectory.mkdirs();
        if (!formatsDirectory.isDirectory()) throw new RuntimeException("\\formats is not a directory!");
        String[] files = formatsDirectory.list();
        assert files != null;
        List<String> formats = new ArrayList<>(Arrays.asList(files));
        if (formats.isEmpty()) {
            formats.add("chat.yml");
            formats.add("join.yml");
            formats.add("leave.yml");
            formats.add("privatesender.yml");
            formats.add("privatereceiver.yml");
        }
        for (String fileName : formats) {
            String formatName = fileName.substring(0, fileName.length() - 4);
            chatFormats.put(formatName, new ChatFormat(this, formatName));
            getLogger().info("Loaded chat format: " + fileName);
        }
    }

    private void registerEvents() {
        EventListener eventListener = new EventListener(this);
        Bukkit.getPluginManager().registerEvent(AsyncPlayerChatEvent.class, eventListener, EventPriority.HIGHEST,
                (listener, event) -> ((EventListener) listener).onChat((AsyncPlayerChatEvent) event), this);
        Bukkit.getPluginManager().registerEvent(PlayerJoinEvent.class, eventListener, EventPriority.HIGHEST,
                (listener, event) -> ((EventListener) listener).onJoin((PlayerJoinEvent) event), this);
        Bukkit.getPluginManager().registerEvent(PlayerQuitEvent.class, eventListener, EventPriority.HIGHEST,
                (listener, event) -> ((EventListener) listener).onLeave((PlayerQuitEvent) event), this);
    }

    public void reload() {
        loadConfig();
        loadChatFormats();
    }
}
