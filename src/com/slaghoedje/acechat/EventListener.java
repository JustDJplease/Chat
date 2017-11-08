package com.slaghoedje.acechat;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class EventListener implements Listener {
    private final AceChat aceChat;

    public EventListener(AceChat aceChat) {
        this.aceChat = aceChat;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        if(aceChat.config.getString("formats.chat", "chat").equalsIgnoreCase("none")) return;
        if(event.isCancelled()) return;
        event.setCancelled(true);

        if(aceChat.chatMuted && !Permissions.has(event.getPlayer(), "acechat.admin.mutechat.bypass")) {
            event.getPlayer().sendMessage(Lang.format("error.chatmuted"));
            return;
        }

        if(Permissions.has(event.getPlayer(), "acechat.user.chat")) {
            ChatFormat chatFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.chat", "chat"));
            Bukkit.spigot().broadcast(chatFormat.getJSONMessage(event.getPlayer(), null, event.getMessage()));
            System.out.println(event.getPlayer().getName() + ": " + event.getMessage());
        } else
            event.getPlayer().sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "acechat.user.chat"));
    }

    public void onJoin(PlayerJoinEvent event) {
        if(aceChat.config.getString("formats.join", "join").equalsIgnoreCase("none")) return;
        if(event.getJoinMessage() == null || event.getJoinMessage().isEmpty()) return;
        event.setJoinMessage("");

        ChatFormat chatFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.join", "join"));
        Bukkit.spigot().broadcast(chatFormat.getJSONMessage(event.getPlayer(), null, "undefined"));
        System.out.println(event.getPlayer().getName() + " joined");
    }

    public void onLeave(PlayerQuitEvent event) {
        if(aceChat.config.getString("formats.leave", "leave").equalsIgnoreCase("none")) return;
        if(event.getQuitMessage() == null || event.getQuitMessage().isEmpty()) return;
        event.setQuitMessage("");

        ChatFormat chatFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.leave", "leave"));
        Bukkit.spigot().broadcast(chatFormat.getJSONMessage(event.getPlayer(), null, "undefined"));
        System.out.println(event.getPlayer().getName() + " left");

        if(aceChat.socialSpy.contains(event.getPlayer())) aceChat.socialSpy.remove(event.getPlayer());
    }
}
