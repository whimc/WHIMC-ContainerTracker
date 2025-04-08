package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    // Set up logger
    private final Logger log = Logger.getLogger("Minecraft");
    private final String PRESSURE_PLATE = "PRESSURE_PLATE";
    private final String LEVER = "LEVER";
    private final String BUTTON = "BUTTON";
    private Integer AIR_CLICK = 0;
    String regionNames = "";
    //private final String OBSERVER = "OBSERVER";


    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Interact Event triggered");
        }
        /*
        if (event.getClickedBlock() == null){
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Interact Event ignored: action did not interact with a block");
            }
            return;
        }*/
        Block clickedBlock = event.getClickedBlock();
        Material blockMaterial = clickedBlock.getType();
        if(blockMaterial == Material.AIR){
            AIR_CLICK += 1;
            if (AIR_CLICK > 5) {
                log.info("[ContainerTracker] Interact Event: clicked air over 5 times");
                ContainerTracker.getInstance().getQueryer().logNewPhysicalInteraction(event.getPlayer(), event.getClickedBlock(), regionNames);
                AIR_CLICK = 0;
            }
            return;
        }
        String blockName = blockMaterial.toString().toUpperCase();
        /*
        // exit if not a physical interaction
        if (event.getAction() != Action.PHYSICAL) {
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Interact Event ignored: action was not of type PHYSICAL");
            }
            return;
        }
        */
        if(!(blockName.contains(PRESSURE_PLATE) || blockName.contains(LEVER) || blockName.contains(BUTTON))){
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Interact Event ignored: action not with pressure plate, lever, or button");
            }
            return;
        }
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        Location blockLocation = BukkitAdapter.adapt(clickedBlock.getLocation());
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(blockLocation);
        for (ProtectedRegion region : set) {
            regionNames += region.getId() + " ";
        }


        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Logging Information:\n"
                    + "Timestamp: " + System.currentTimeMillis() + "\n"
                    + "Player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "Location: " + event.getPlayer().getLocation() + "\n"
                    + "Block Type: " +  event.getClickedBlock().getType() + "\n"
                    + "Region Name: " +  regionNames + "\n"
            );
        }
        ContainerTracker.getInstance().getQueryer().logNewPhysicalInteraction(event.getPlayer(), event.getClickedBlock(), regionNames);
    }
}