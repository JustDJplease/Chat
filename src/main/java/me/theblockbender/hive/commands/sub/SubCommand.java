/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.commands.sub;

import me.theblockbender.hive.Chat;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    private String permission = "";
    private String description = "";
    private final String[] commands;
    protected final Chat chat;

    SubCommand(Chat chat, String... commands) {
        this.commands = commands;
        this.chat = chat;
    }

    void setPermission(String permission) {
        this.permission = permission;
    }

    void setDescription(String description) {
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
