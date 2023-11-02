package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        // Set up config
        FileConfiguration config = ContainerTracker.getInstance().getConfig();

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] player interact event!");
        }

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        // TODO: Add these results to DB
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] logging information:\n"
                    + "timestamp: " + System.currentTimeMillis() + "\n"
                    + "player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "location: " + event.getPlayer().getLocation()
            );
        }
    }
}
