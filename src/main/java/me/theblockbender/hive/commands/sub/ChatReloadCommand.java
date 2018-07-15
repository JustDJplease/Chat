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
