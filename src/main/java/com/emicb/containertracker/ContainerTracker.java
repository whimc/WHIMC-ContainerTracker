package com.emicb.containertracker;

import com.emicb.containertracker.listeners.InventoryCloseListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ContainerTracker extends JavaPlugin {

    private static ContainerTracker plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        PluginManager pluginManager = Bukkit.getPluginManager();
        saveDefaultConfig();

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        log.info("[ContainerTracker] Hello world!");

        // Link listeners
        pluginManager.registerEvents(new InventoryCloseListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }

    public static ContainerTracker getInstance() { return plugin; }
}
