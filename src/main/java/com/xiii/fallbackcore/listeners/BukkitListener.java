package com.xiii.fallbackcore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent e) {
        e.setJoinMessage("");

        //Hide all players
        for (final Player p : Bukkit.getOnlinePlayers()){
            e.getPlayer().hidePlayer(p);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler(ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent e) {
        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(final BlockDamageEvent e) {
        e.setCancelled(true);
    }
}
