package com.slaghoedje.acechat;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.slaghoedje.acechat.util.FormatConfigParser;
import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class EventListener implements Listener {
    private final AceChat aceChat;

    public EventListener(AceChat aceChat) {
        this.aceChat = aceChat;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(),"formats.chat", "chat");
        if(chatFormat == null) return;
        if(event.isCancelled()) return;
        event.setCancelled(true);

        if(aceChat.chatMuted && !Permissions.has(event.getPlayer(), "acechat.admin.mutechat.bypass")) {
            event.getPlayer().sendMessage(Lang.format("error.chatmuted"));
            return;
        }

        if(Permissions.has(event.getPlayer(), "acechat.user.chat")) {
            chatFormat.broadcast(event.getPlayer(), null, event.getMessage());
            System.out.println(event.getPlayer().getName() + ": " + event.getMessage());
        } else
            event.getPlayer().sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "acechat.user.chat"));
    }

    public void onJoin(PlayerJoinEvent event) {
        boolean jlnoneonnone = aceChat.config.getBoolean("jl-none-on-none", true);
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(),"formats.join", "join");

        if(jlnoneonnone) {
            if(event.getJoinMessage() == null || event.getJoinMessage().isEmpty()) return;
            event.setJoinMessage("");
        }

        if(chatFormat == null) return;
        if(!jlnoneonnone && (event.getJoinMessage() == null || event.getJoinMessage().isEmpty())) return;
        event.setJoinMessage("");

        chatFormat.broadcast(event.getPlayer(), null, "undefined");
        System.out.println(event.getPlayer().getName() + " joined");
    }

    public void onLeave(PlayerQuitEvent event) {
        boolean jlnoneonnone = aceChat.config.getBoolean("jl-none-on-none", true);
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(),"formats.leave", "leave");

        if(jlnoneonnone) {
            if(event.getQuitMessage() == null || event.getQuitMessage().isEmpty()) return;
            event.setQuitMessage("");
        }

        if(chatFormat == null) return;
        if(!jlnoneonnone && (event.getQuitMessage() == null || event.getQuitMessage().isEmpty())) return;
        event.setQuitMessage("");

        chatFormat.broadcast(event.getPlayer(), null, "undefined");
        System.out.println(event.getPlayer().getName() + " left");

        if(aceChat.socialSpy.contains(event.getPlayer())) aceChat.socialSpy.remove(event.getPlayer());
    }
}
