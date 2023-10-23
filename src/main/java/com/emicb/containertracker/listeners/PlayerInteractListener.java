package com.emicb.containertracker.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        // Set up logger
        Logger log = Logger.getLogger("Minecraft");

        // Get block being interacted with and ensure it exists
        Block interactBlock = event.getClickedBlock();
        if (interactBlock == null) {
            return;
        }

        // Exit if player not interacting with a container
        if (!(interactBlock.getState() instanceof InventoryHolder)) {
            return;
        }

        InventoryHolder container = (InventoryHolder) interactBlock.getState();
        log.info("[ContainerTracker] Interacting with container: "
                + interactBlock.getBlockData().getMaterial() + "\n"
                + interactBlock.getBlockData().getAsString()
        );
    }
}
