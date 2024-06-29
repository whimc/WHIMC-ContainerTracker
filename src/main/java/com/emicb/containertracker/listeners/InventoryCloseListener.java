package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import java.util.logging.Logger;

public class InventoryCloseListener implements Listener {
    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    @EventHandler
    public void OnInventoryClose(InventoryCloseEvent event) {
        // Set up logger
        Logger log = Logger.getLogger("Minecraft");
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Inventory Close Event triggered");
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
            log.info("[ContainerTracker] Logging Information:\n"
                    + "Timestamp: " + System.currentTimeMillis() + "\n"
                    + "Player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "Location: " + container.getLocation()
            );
        }

        // Get inventory contents
        ItemStack[] contents = inventory.getContents();
        if (contents.length == 0) {
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Container contents not logged: Container is empty");
            }
            return;
        }

        //Get region name
        InventoryType inventoryType = inventory.getType();
        if(!(inventoryType == InventoryType.BARREL || inventoryType == InventoryType.SHULKER_BOX)){
            log.info("[ContainerTracker] Container contents not logged: Container is not barrel or shulker");
            return;
        }
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        Location blockLocation = BukkitAdapter.adapt(container.getLocation());
        RegionQuery query = regionContainer.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(blockLocation);
        String regionNames = "";
        for (ProtectedRegion region : set) {
            regionNames += region.getId() + " ";
        }
        ContainerTracker.getInstance().getQueryer().storeNewInventory(sender, inventory, regionNames);
    }
}
