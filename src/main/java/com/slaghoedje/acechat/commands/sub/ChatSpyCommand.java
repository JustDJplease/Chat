package com.slaghoedje.acechat.commands.sub;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.util.Lang;

public class ChatSpyCommand extends SubCommand {
    public ChatSpyCommand(AceChat aceChat) {
        super(aceChat, "spy", "socialspy", "ss", "sspy");
        this.setPermission("acechat.admin.socialspy");
        this.setDescription("command-descriptions.spy");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("You don't need that. You can already see what players are messaging eachother!");
            return;
        }

        if(aceChat.socialSpy.contains(sender)) {
            aceChat.socialSpy.remove(sender);
            sender.sendMessage(Lang.format("spy.disable"));
        } else {
            aceChat.socialSpy.add((Player) sender);
            sender.sendMessage(Lang.format("spy.enable"));
        }
    }
}
