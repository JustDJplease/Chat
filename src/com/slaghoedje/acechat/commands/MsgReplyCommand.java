package com.slaghoedje.acechat.commands;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.slaghoedje.acechat.AceChat;
import com.slaghoedje.acechat.ChatFormat;
import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class MsgReplyCommand implements CommandExecutor {
    private final AceChat aceChat;
    private Map<String, String> reply;


    public MsgReplyCommand(AceChat aceChat) {
        this.aceChat = aceChat;
        reply = new HashMap<>();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!Permissions.has(sender, "acechat.user.msg")) {
            sender.sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "acechat.user.msg"));
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

            ChatFormat senderFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.private-sender", "privatesender"));
            senderFormat.send((Player) sender, (Player) sender, other, message);

            ChatFormat receiverFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.private-receiver", "privatereceiver"));
            receiverFormat.send(other, (Player) sender, other, message);

            for(Player spyer : aceChat.socialSpy) {
                if(spyer.equals(sender) || spyer.equals(other)) continue;

                spyer.sendMessage(Lang.format("spy.format")
                        .replaceAll("%player1%", sender.getName())
                        .replaceAll("%player2%", other.getName())
                        .replaceAll("%message%", message));
            }

            reply.put(sender.getName(), other.getName());
            reply.put(other.getName(), sender.getName());
        } else if(command.getName().equalsIgnoreCase("reply")) {
            if(args.length < 1) {
                sender.sendMessage(Lang.format("error.toolessargsreply").replaceAll("%label%", label));
                return true;
            }

            String[] newArgs = Stream.concat(Arrays.stream(new String[] {reply.get(sender.getName())}), Arrays.stream(args)).toArray(String[]::new);
            onCommand(sender, aceChat.getCommand("tell"), "tell", newArgs);
        }

        return true;
    }
}
