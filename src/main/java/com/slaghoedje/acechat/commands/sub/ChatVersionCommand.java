package com.slaghoedje.acechat.commands.sub;

import org.bukkit.command.CommandSender;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.util.Lang;

public class ChatVersionCommand extends SubCommand {
    public ChatVersionCommand(AceChat aceChat) {
        super(aceChat, "version", "v", "ver");
        this.setPermission("acechat.admin.version");
        this.setDescription("command-descriptions.version");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("version").replaceAll("%version%", aceChat.getDescription().getVersion()));
    }
}
