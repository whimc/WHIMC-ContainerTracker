package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.logging.Logger;

public class InventoryCloseListener implements Listener {
    private ContainerTracker plugin;
    public InventoryCloseListener(ContainerTracker plugin){
        this.plugin = plugin;
    }
    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        log.info("[ContainerTracker] inventory close event!");

        Inventory inventory = event.getInventory();
        Player sender = (Player) event.getPlayer();
        // Exit if not viewing a container
        // TODO: Double chest not making it past this check?
        if (!(inventory.getHolder() instanceof Container)) {
            return;
        }

        Container container = (Container) inventory.getHolder();

        // Get data to store
        log.info("[ContainerTracker] logging information:\n"
                + "timestamp: " + System.currentTimeMillis() + "\n"
                + "player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                + "location: " + container.getLocation()
        );

        // Get inventory contents
        ItemStack[] contents = inventory.getContents();
        if (contents.length == 0) {
            log.info("[ContainerTracker] Container is empty!");
            return;
        }
        this.plugin.getQueryer().storeNewInventory(sender, contents);

    }
}
