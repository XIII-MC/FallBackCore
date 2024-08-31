package com.xiii.fallbackcore;

import com.xiii.fallbackcore.listeners.BukkitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class FallBackCore extends JavaPlugin {

    private static FallBackCore INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        Bukkit.getServer().getPluginManager().registerEvents(new BukkitListener(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public static FallBackCore getInstance() {
        return INSTANCE;
    }
}
