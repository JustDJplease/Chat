package com.slaghoedje.acechat;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.slaghoedje.acechat.util.Lang;
import com.slaghoedje.acechat.util.Permissions;

public class EventListener implements Listener {
    private final AceChat aceChat;

    public EventListener(AceChat aceChat) {
        this.aceChat = aceChat;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        if(event.isCancelled()) return;
        event.setCancelled(true);

        if(Permissions.has(event.getPlayer(), "acechat.user.chat")) {
            ChatFormat chatFormat = aceChat.chatFormats.get(aceChat.config.getString("formats.chat", "chat"));
            Bukkit.spigot().broadcast(chatFormat.getJSONMessage(event.getPlayer(), null, event.getMessage()));
        } else
            event.getPlayer().sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "acechat.user.chat"));
    }
}
