package com.slaghoedje.acechat.commands.sub;

import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class ChatClearCommand extends SubCommand {
    public ChatClearCommand(AceChat aceChat) {
        super(aceChat, "clear", "cl", "c");
        this.setPermission("acechat.admin.clearchat");
        this.setDescription("command-descriptions.clear");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        int clearLines = aceChat.config.getInt("clear-length", 102);

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(Permissions.has(player, "acechat.admin.clearchat.bypass")) continue;
            player.sendMessage(String.join("", Collections.nCopies(clearLines, "\n\u00A7r ")));
        }

        Bukkit.broadcastMessage(Lang.format("chatclear").replaceAll("%player%", sender.getName()));
    }
}
