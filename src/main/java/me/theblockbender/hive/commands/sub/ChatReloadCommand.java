/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.commands.sub;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.util.Lang;
import org.bukkit.command.CommandSender;

public class ChatReloadCommand extends SubCommand {
    public ChatReloadCommand(Chat chat) {
        super(chat, "reload", "rl", "r");
        this.setPermission("chat.admin.reload");
        this.setDescription("command-descriptions.reload");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("reload.before"));
        chat.reload();
        sender.sendMessage(Lang.format("reload.after"));
    }
}
