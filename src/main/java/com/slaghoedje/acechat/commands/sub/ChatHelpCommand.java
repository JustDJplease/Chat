package com.slaghoedje.acechat.commands.sub;

import org.bukkit.command.CommandSender;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.commands.ChatCommand;
import com.slaghoedje.acechat.util.Lang;

public class ChatHelpCommand extends SubCommand {
    private final ChatCommand chatCommand;

    public ChatHelpCommand(AceChat aceChat, ChatCommand chatCommand) {
        super(aceChat, "help", "?", "h");
        this.chatCommand = chatCommand;

        this.setPermission("acechat.admin.help");
        this.setDescription("command-descriptions.help");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("help.header"));
        for(SubCommand subCommand : chatCommand.subCommands) {
            sender.sendMessage(Lang.format("help.item")
                    .replaceAll("%command%", String.format("/%s %s", mainCommandLabel, subCommand.getCommands()[0]))
                    .replaceAll("%description%", Lang.format(subCommand.getDescription())));
        }
    }
}
