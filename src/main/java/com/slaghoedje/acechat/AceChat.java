package com.slaghoedje.acechat;

import com.earth2me.essentials.Essentials;
import com.slaghoedje.acechat.commands.ChatCommand;
import com.slaghoedje.acechat.commands.MsgReplyCommand;
import com.slaghoedje.acechat.util.FormatConfigParser;
import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class AceChat extends JavaPlugin {
    public Map<String, ChatFormat> chatFormats;
    public List<Player> socialSpy;

    public File configFile;
    public FileConfiguration config;

    public File messagesFile;
    public FileConfiguration messages;

    public boolean papiPresent = false;
    public boolean chatMuted = false;

    public Essentials ess;

    public void onEnable() {
        Plugin essentials = Bukkit.getPluginManager().getPlugin("Essentials");
        if (essentials == null) {
            getLogger().severe("Essentials is missing");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        ess = (Essentials) essentials;
        loadConfig();

        FormatConfigParser.setAceChat(this);
        Lang.setAceChat(this);
        socialSpy = new ArrayList<>();

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

    public void onDisable() {

    }

    public void loadConfig() {
        try {
            configFile = new File(getDataFolder(), "config.yml");
            messagesFile = new File(getDataFolder(), "messages.yml");

            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                saveResource("config.yml", false);
            }

            if (!messagesFile.exists()) {
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

    public void loadChatFormats() {
        chatFormats = new HashMap<>();

        File formatsDirectory = new File(getDataFolder(), "formats");
        if (!formatsDirectory.exists()) formatsDirectory.mkdirs();
        if (!formatsDirectory.isDirectory()) throw new RuntimeException("\\formats is not a directory!");

        List<String> formats = new ArrayList<>();
        String[] files = formatsDirectory.list();

        assert files != null;
        formats.addAll(Arrays.asList(files));

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