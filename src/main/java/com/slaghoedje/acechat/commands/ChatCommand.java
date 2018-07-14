package com.slaghoedje.acechat.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.commands.sub.*;
import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class ChatCommand implements CommandExecutor {
    public List<SubCommand> subCommands;

    public ChatCommand(AceChat aceChat) {
        subCommands = new ArrayList<>();
        subCommands.add(new ChatHelpCommand(aceChat, this));
        subCommands.add(new ChatVersionCommand(aceChat));
        subCommands.add(new ChatReloadCommand(aceChat));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0) {
            onCommand(sender, cmd, label, new String[] {"version"});
            return true;
        }

        for(SubCommand subCommand : subCommands) {
            for(String command : subCommand.getCommands()) {
                if(args[0].equalsIgnoreCase(command)) {
                    if(!(!(sender instanceof Player) || Permissions.has(sender, subCommand.getPermission()))) {
                        sender.sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", subCommand.getPermission()));
                        return true;
                    }

                    String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                    subCommand.execute(sender, label, args[0], subArgs);
                    return true;
                }
            }
        }

        sender.sendMessage(Lang.format("error.invalidsubcommand").replaceAll("%subcommand%", args[0]).replaceAll("%label%", label));
        return true;
    }
}
