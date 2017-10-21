package com.slaghoedje.acechat.commands.sub;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.util.Lang;

public class ChatMuteCommand extends SubCommand {
    public ChatMuteCommand(AceChat aceChat) {
        super(aceChat, "mute", "m");
        this.setPermission("acechat.admin.mutechat");
        this.setDescription("command-descriptions.mute");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        if(aceChat.chatMuted) {
            aceChat.chatMuted = false;
            Bukkit.broadcastMessage(Lang.format("global-mute.unmute").replaceAll("%player%", sender.getName()));
        } else {
            aceChat.chatMuted = true;
            Bukkit.broadcastMessage(Lang.format("global-mute.mute").replaceAll("%player%", sender.getName()));
        }
    }
}
