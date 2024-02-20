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
            log.info("[ContainerTracker] Interact Event triggered");
        }

        // exit if not a physical interaction
        if (event.getAction() != Action.PHYSICAL) {
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Interact Event ignored: action was not of type PHYSICAL");
            }
            return;
        }

        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Logging Information:\n"
                    + "Timestamp: " + System.currentTimeMillis() + "\n"
                    + "Player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "Location: " + event.getPlayer().getLocation() + "\n"
                    + "Block Type: " +  event.getClickedBlock().getType()
            );
        }

        // Add results to the database
        ContainerTracker.getInstance().getQueryer().logNewPhysicalInteraction(event.getPlayer(), event.getClickedBlock());

    }
}