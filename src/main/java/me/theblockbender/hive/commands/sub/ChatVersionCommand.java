package me.theblockbender.hive.commands.sub;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.util.Lang;
import org.bukkit.command.CommandSender;

public class ChatVersionCommand extends SubCommand {
    public ChatVersionCommand(Chat chat) {
        super(chat, "version", "v", "ver");
        this.setPermission("chat.admin.version");
        this.setDescription("command-descriptions.version");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        sender.sendMessage(Lang.format("version").replaceAll("%version%", chat.getDescription().getVersion()));
    }
}
