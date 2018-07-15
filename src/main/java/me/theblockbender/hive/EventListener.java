/*
 * Notice: This plugin is a heavily modified copy from the AceChat plugin. Original author is SlagHoedje. This file was modified by TheBlockBender / JustDJplease. The original resource can be found at https://www.spigotmc.org/resources/acechat.48695/
 */

package me.theblockbender.hive;

import me.theblockbender.hive.util.FormatConfigParser;
import me.theblockbender.hive.util.Lang;
import me.theblockbender.hive.util.Permissions;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventListener implements Listener {
    private final Chat chat;

    EventListener(Chat chat) {
        this.chat = chat;
    }

    public void onChat(AsyncPlayerChatEvent event) {
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(), "formats.chat", "chat");
        if (chatFormat == null) return;
        if (event.isCancelled()) return;
        event.setCancelled(true);

        if (Permissions.has(event.getPlayer(), "chat.user.chat")) {
            chatFormat.broadcast(event.getPlayer(), null, event.getMessage());
            chat.getLogger().info(event.getPlayer().getName() + ": " + event.getMessage());
        } else
            event.getPlayer().sendMessage(Lang.format("error.nopermission").replaceAll("%permission%", "chat.user.chat"));
    }

    public void onJoin(PlayerJoinEvent event) {
        boolean jlnoneonnone = chat.config.getBoolean("jl-none-on-none", true);
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(), "formats.join", "join");
        if (jlnoneonnone) {
            if (event.getJoinMessage() == null || event.getJoinMessage().isEmpty()) return;
            event.setJoinMessage("");
        }
        if (chatFormat == null) return;
        if (!jlnoneonnone && (event.getJoinMessage() == null || event.getJoinMessage().isEmpty())) return;
        event.setJoinMessage("");
        chatFormat.broadcast(event.getPlayer(), null, "undefined");
        System.out.println(event.getPlayer().getName() + " joined");
    }

    public void onLeave(PlayerQuitEvent event) {
        boolean jlnoneonnone = chat.config.getBoolean("jl-none-on-none", true);
        ChatFormat chatFormat = FormatConfigParser.parseMultiFormat(event.getPlayer(), "formats.leave", "leave");
        if (jlnoneonnone) {
            if (event.getQuitMessage() == null || event.getQuitMessage().isEmpty()) return;
            event.setQuitMessage("");
        }
        if (chatFormat == null) return;
        if (!jlnoneonnone && (event.getQuitMessage() == null || event.getQuitMessage().isEmpty())) return;
        event.setQuitMessage("");
        chatFormat.broadcast(event.getPlayer(), null, "undefined");
        System.out.println(event.getPlayer().getName() + " left");
    }
}
