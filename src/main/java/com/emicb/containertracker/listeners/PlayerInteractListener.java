package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    // Set up logger
    private final Logger log = Logger.getLogger("Minecraft");

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] player interact event!");
        }

        // exit if not a physical interaction
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] logging information:\n"
                    + "timestamp: " + System.currentTimeMillis() + "\n"
                    + "player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "location: " + event.getPlayer().getLocation()
            );
            log.info(event.getClickedBlock().getType().toString());
        }

        // Add results to the database
        ContainerTracker.getInstance().getQueryer().logNewPhysicalInteraction(event.getPlayer(), event.getClickedBlock());

    }
}