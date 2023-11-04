package com.emicb.containertracker;

import com.emicb.containertracker.listeners.InventoryCloseListener;
import com.emicb.containertracker.utils.sql.Queryer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ContainerTracker extends JavaPlugin {
    private Queryer queryer;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        PluginManager pluginManager = Bukkit.getPluginManager();

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        log.info("[ContainerTracker] Hello world!");
        this.queryer = new Queryer(this, q -> {
            // If we couldn't connect to the database disable the plugin
            if (q == null) {
                this.getLogger().severe("Could not establish MySQL connection! Disabling plugin...");
                getCommand("progress").setExecutor(this);
                getCommand("leaderboard").setExecutor(this);
                return;
            }


        });
        // Link listeners
        pluginManager.registerEvents(new InventoryCloseListener(this), this);
    }
    public Queryer getQueryer() {
        return this.queryer;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
