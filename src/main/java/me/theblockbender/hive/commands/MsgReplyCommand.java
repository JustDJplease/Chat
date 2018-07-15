package me.theblockbender.hive.commands;

import me.theblockbender.hive.Chat;
import me.theblockbender.hive.ChatFormat;
import me.theblockbender.hive.util.FormatConfigParser;
import me.theblockbender.hive.util.Lang;
import me.theblockbender.hive.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class MsgReplyCommand implements CommandExecutor {
    private final Chat chat;
    private Map<String, String> reply;


    public MsgReplyCommand(Chat chat) {
        this.chat = chat;
        reply = new HashMap<>();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Permissions.has(sender, "chat.user.msg")) {
            sender.sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "chat.user.msg"));
            return true;
        }

        if(!(sender instanceof Player)) {
            sender.sendMessage("Sorry, but any not-players are not allowed to send messages, it has to do with player placeholders and such.");
            return true;
        }

        if(command.getName().equalsIgnoreCase("tell")) {
            if(args.length < 2) {
                sender.sendMessage(Lang.format("error.toolessargsmsg").replaceAll("%label%", label));
                return true;
            }

            Player other = Bukkit.getPlayer(args[0]);

            if(other == null) {
                sender.sendMessage(Lang.format("error.playernotonline").replaceAll("%player%", args[0]));
                return true;
            }

            String[] messageArray = Arrays.copyOfRange(args, 1, args.length);
            String message = String.join(" ", messageArray);

            ChatFormat senderFormat = FormatConfigParser.parseSingleFormat("formats.private-sender", "privatesender");
            if(senderFormat != null) senderFormat.send((Player) sender, (Player) sender, other, message);

            ChatFormat receiverFormat = FormatConfigParser.parseSingleFormat("formats.private-receiver", "privatereceiver");
            if(receiverFormat != null) receiverFormat.send(other, (Player) sender, other, message);
            reply.put(sender.getName(), other.getName());
            reply.put(other.getName(), sender.getName());
        } else if(command.getName().equalsIgnoreCase("reply")) {
            if(args.length < 1) {
                sender.sendMessage(Lang.format("error.toolessargsreply").replaceAll("%label%", label));
                return true;
            }

            String[] newArgs = Stream.concat(Arrays.stream(new String[] {reply.get(sender.getName())}), Arrays.stream(args)).toArray(String[]::new);
            onCommand(sender, chat.getCommand("tell"), "tell", newArgs);
        }

        return true;
    }
}
