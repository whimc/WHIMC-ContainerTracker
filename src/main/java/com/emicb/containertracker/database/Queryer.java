package com.emicb.containertracker.database;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.Bukkit;

import java.util.function.Consumer;

public class Queryer {
    /** The instance of the plugin. */
    private ContainerTracker plugin;
    /** The connection to the SQL database. */
    private MySQLConnection sqlConnection;

    /**
     * Constructs a Queryer.
     *
     * @param plugin the instance of the plugin.
     * @param callback the event callback.
     */
    public Queryer(ContainerTracker plugin, Consumer<Boolean> callback) {
        this.plugin = plugin;
        this.sqlConnection = new MySQLConnection(plugin);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final boolean success = this.sqlConnection.initialize();
            Bukkit.getScheduler().runTask(plugin, () -> {
                callback.accept(success);
            });
        });
    }
}
