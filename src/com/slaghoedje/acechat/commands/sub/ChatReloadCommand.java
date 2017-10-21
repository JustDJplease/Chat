package com.slaghoedje.acechat.commands.sub;

import org.bukkit.command.CommandSender;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.util.Lang;

public class ChatReloadCommand extends SubCommand {
    public ChatReloadCommand(AceChat aceChat) {
        super(aceChat, "reload", "rl", "r");
        this.setPermission("acechat.admin.reload");
        this.setDescription("command-descriptions.reload");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("reload.before"));
        aceChat.reload();
        sender.sendMessage(Lang.format("reload.after"));
    }
}
