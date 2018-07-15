/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.commands;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.commands.sub.ChatHelpCommand;
import me.theblockbender.hive.commands.sub.ChatReloadCommand;
import me.theblockbender.hive.commands.sub.ChatVersionCommand;
import me.theblockbender.hive.commands.sub.SubCommand;
import me.theblockbender.hive.util.Lang;
import me.theblockbender.hive.util.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatCommand implements CommandExecutor {
    public List<SubCommand> subCommands;

    public ChatCommand(Chat chat) {
        subCommands = new ArrayList<>();
        subCommands.add(new ChatHelpCommand(chat, this));
        subCommands.add(new ChatVersionCommand(chat));
        subCommands.add(new ChatReloadCommand(chat));
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
