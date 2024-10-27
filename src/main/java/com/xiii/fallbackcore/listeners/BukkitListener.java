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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent e) {

        e.setJoinMessage("");

        //Hide all players
        for (final Player p : Bukkit.getOnlinePlayers()){
            e.getPlayer().hidePlayer(p);
        }

        // Clear player's chat
        for (int i = 0; i <= 100; i++) {
            e.getPlayer().sendMessage(" ");
        }

        e.getPlayer().sendMessage("§e§l      ✿§r §6§lWELCOME TO§1§l §b§lGTEAM'S§6§l NETWORK§e§l ✿");
        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("      Network Status:");
        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("  - Proxy: §aOnline");
        e.getPlayer().sendMessage("  - FallBack: §aOnline");

        // Check Main server status
        try (final Socket mainSocket = new Socket()) {

            mainSocket.connect(new InetSocketAddress("192.168.1.250", 25570), 1000);

            e.getPlayer().sendMessage("  - Main: §aOnline");

        } catch (final IOException ignored) {

            e.getPlayer().sendMessage("  - Main: §cOffline");

        }

        e.getPlayer().sendMessage(" ");
        e.getPlayer().sendMessage("     §a§oYou are playing on GTeam's Network. 1.7.10-1.21.3 !");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent event) {
        event.setQuitMessage("");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChat(final AsyncPlayerChatEvent event) {
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDamage(final BlockDamageEvent event) {
        if (!event.getPlayer().isOp() && event.getBlock().getType() != Material.SIGN) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(final PlayerInteractEvent event) {

        final Block block = event.getClickedBlock();

        if (block != null) {

            final Material myBlock = block.getType();

            if (myBlock == Material.SIGN_POST || myBlock == Material.WALL_SIGN) {

                final Sign sign = (Sign) block.getState();
                final String[] lines = sign.getLines();

                if (lines.length > 0 && "x-x SERVER x-x".equalsIgnoreCase(lines[0])) {

                    sendPlayerToServer(event.getPlayer(), lines[2].toLowerCase());

                }
            }
        }

    }

    private void sendPlayerToServer(final Player player, final String server    ) {

        final String prefix = "§b§lGTeam §7»§r§f ";

        player.sendMessage(" ");
        player.sendMessage(prefix + "Connecting to '" + server + "', please wait...");
        player.sendMessage(" ");

        try {

            final ByteArrayOutputStream b = new ByteArrayOutputStream();
            final DataOutputStream out = new DataOutputStream(b);

            out.writeUTF("Connect");
            out.writeUTF(server);

            player.sendPluginMessage(FallBackCore.getInstance(), "BungeeCord", b.toByteArray());

            b.close();
            out.close();

        } catch (final Exception ignored) {
            player.sendMessage(prefix + "An error occurred when connecting to '" + server + "'. Is the server online?");
        }
    }
}
