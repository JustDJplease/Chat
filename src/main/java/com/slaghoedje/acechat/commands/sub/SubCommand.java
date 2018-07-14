package com.slaghoedje.acechat.commands.sub;

import org.bukkit.command.CommandSender;

import com.slaghoedje.acechat.AceChat;

public abstract class SubCommand {
    private String permission = "";
    private String description = "";
    private final String[] commands;
    protected final AceChat aceChat;

    public SubCommand(AceChat aceChat, String... commands) {
        this.commands = commands;
        this.aceChat = aceChat;
    }

    protected void setPermission(String permission) {
        this.permission = permission;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public String[] getCommands() {
        return commands;
    }

    public String getPermission() {
        return permission;
    }

    public String getDescription() {
        return description;
    }

    public abstract void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args);
}
