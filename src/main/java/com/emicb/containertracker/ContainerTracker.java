package com.emicb.containertracker;

import com.emicb.containertracker.commands.ToggleDebug;
import com.emicb.containertracker.listeners.InventoryCloseListener;
import com.emicb.containertracker.listeners.PlayerInteractListener;
import com.emicb.containertracker.utils.sql.Queryer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ContainerTracker extends JavaPlugin {
    private static ContainerTracker plugin;
    private Queryer queryer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        PluginManager pluginManager = Bukkit.getPluginManager();
        plugin = this;

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");

        this.queryer = new Queryer(this, q -> {
            // If we couldn't connect to the database disable the plugin
            if (q == null) {
                log.severe("[ContainerTracker] Plugin Disabled: could not establish MySQL connection!");
                getCommand("progress").setExecutor(this);
                getCommand("leaderboard").setExecutor(this);
                return;
            }
        });

        // Link listeners
        pluginManager.registerEvents(new InventoryCloseListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);

        // Link commands
        // TODO: change command structure to sub-commands
        getServer().getPluginCommand("ct-toggle-debug").setExecutor(new ToggleDebug());
        // TODO: add enable / disable logging commands

        log.info("[ContainerTracker] Started successfully!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ContainerTracker getInstance() {
        return plugin;
    }
    public Queryer getQueryer() {
        return this.queryer;
    }
}
