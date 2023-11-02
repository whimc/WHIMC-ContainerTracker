package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        // Set up config
        FileConfiguration config = ContainerTracker.getInstance().getConfig();

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] inventory close event!");
        }

        Inventory inventory = event.getInventory();

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

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) {
                if (config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: nothing");
                }
                continue;
            }

            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] slot " + i + " has: " + item.toString());
            }

        }
    }
}
