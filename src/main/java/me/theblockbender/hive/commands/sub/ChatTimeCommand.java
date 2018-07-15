/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive.commands.sub;

import me.clip.placeholderapi.PlaceholderAPI;
import me.theblockbender.hive.Chat;
import me.theblockbender.hive.util.Lang;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatTimeCommand extends SubCommand {
    public ChatTimeCommand(Chat chat) {
        super(chat, "time", "t", "servertime");
        this.setPermission("chat.admin.time");
        this.setDescription("command-descriptions.time");
    }

    public void execute(CommandSender sender, String mainCommandLabel, String subCommandLabel, String[] args) {
        String msg = Lang.format("time");
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.format("error.noplayer"));
            return;
        }
        Player player = (Player) sender;
        if (chat.papiPresent) {
            msg = PlaceholderAPI.setPlaceholders(player, msg);
        }
        sender.sendMessage(msg);
    }
}
