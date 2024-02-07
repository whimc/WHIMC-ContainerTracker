package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.logging.Logger;

public class InventoryCloseListener implements Listener {
    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] inventory close event!");
        }

        Inventory inventory = event.getInventory();
        Player sender = (Player) event.getPlayer();
        // Exit if not viewing a container
        // TODO: Double chest not making it past this check?
        if (!(inventory.getHolder() instanceof Container)) {
            return;
        }

        Container container = (Container) inventory.getHolder();

        // Get data to store
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] logging information:\n"
                    + "timestamp: " + System.currentTimeMillis() + "\n"
                    + "player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "location: " + container.getLocation()
            );
        }

        // Get inventory contents
        ItemStack[] contents = inventory.getContents();
        if (contents.length == 0) {
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Container is empty!");
            }

            return;
        }

        ContainerTracker.getInstance().getQueryer().storeNewInventory(sender, contents);
    }
}
