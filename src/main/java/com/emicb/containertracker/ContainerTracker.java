package com.emicb.containertracker;

import com.emicb.containertracker.listeners.InventoryCloseListener;
import com.emicb.containertracker.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ContainerTracker extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        PluginManager pluginManager = Bukkit.getPluginManager();

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        log.info("[ContainerTracker] Hello world!");

        // Link listeners
        //pluginManager.registerEvents(new PlayerInteractListener(), this);
        pluginManager.registerEvents(new InventoryCloseListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
