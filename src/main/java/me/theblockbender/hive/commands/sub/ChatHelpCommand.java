/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.commands.sub;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.commands.ChatCommand;
import me.theblockbender.hive.util.Lang;
import org.bukkit.command.CommandSender;

public class ChatHelpCommand extends SubCommand {
    private final ChatCommand chatCommand;

    public ChatHelpCommand(Chat chat, ChatCommand chatCommand) {
        super(chat, "help", "?", "h");
        this.chatCommand = chatCommand;

        this.setPermission("chat.admin.help");
        this.setDescription("command-descriptions.help");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("help.header"));
        for (SubCommand subCommand : chatCommand.subCommands) {
            sender.sendMessage(Lang.format("help.item")
                    .replaceAll("%command%", String.format("/%s %s", mainCommandLabel, subCommand.getCommands()[0]))
                    .replaceAll("%description%", Lang.format(subCommand.getDescription())));
        }
    }
}
