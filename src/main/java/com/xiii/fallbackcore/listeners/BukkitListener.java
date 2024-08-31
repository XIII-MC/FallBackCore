package com.xiii.fallbackcore.listeners;

import com.xiii.fallbackcore.FallBackCore;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.*;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class BukkitListener implements Listener {

    private static final String gteamPrefix = "§b§lGTeam §7»§r§f ";

    @EventHandler(ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent e) {
        e.setJoinMessage("");

        //Hide all players
        for (final Player p : Bukkit.getOnlinePlayers()){
            e.getPlayer().hidePlayer(p);
        }

        for (int i = 0; i <= 100; i++) {
            e.getPlayer().sendMessage(" ");
        }

        // Main status
        boolean mainStatus = false;
        try {
            new Socket().connect(new InetSocketAddress("192.168.1.250", 25570), 1000);
            mainStatus = true;
        } catch (final IOException ignored) {}

        e.getPlayer().sendMessage("§e§l      ✿§r §6§lWELCOME TO§1§l §b§lGTEAM'S§6§l NETWORK§e§l ✿");
        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("      Network Status:");
        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("  - Proxy: §aOnline");
        e.getPlayer().sendMessage("  - FallBack: §aOnline");
        e.getPlayer().sendMessage("  - Main: " + (mainStatus ? "§aOnline" : "§cOffline"));
        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("     §a§oYou are playing on GTeam's Network. 1.8.x-1.21.1 !");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        if (!e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDamage(final BlockDamageEvent e) {
        if (!e.getPlayer().isOp()) e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Block block = e.getClickedBlock();
        final Material myBlock = block.getType();
        if (myBlock.equals(Material.SIGN_POST) || myBlock.equals(Material.WALL_SIGN)) {
            final Sign sign = (Sign) block.getState();
            final String[] ln = sign.getLines();
            if(ln[0].toLowerCase().contains("[connect ->")) {
                sendPlayerToServer(e.getPlayer(), "main");
            }
        }
    }

    public void sendPlayerToServer(final Player player, final String server) {

        player.sendMessage(gteamPrefix + "Connecting to '" + server + "', please wait...");

        try {

            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(FallBackCore.getInstance(), "BungeeCord", b.toByteArray());

            b.close();
            out.close();

            for (int i = 0; i <= 100; i++) {
                player.sendMessage(" ");
            }
        }
        catch (final Exception e) {
            player.sendMessage(gteamPrefix + "An error occurred when connecting to '" + server + "'. Is the server online?");
        }
    }
}
