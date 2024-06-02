package com.emicb.containertracker.listeners;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import java.util.logging.Logger;

public class PlayerInteractListener implements Listener {
    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    // Set up logger
    private final Logger log = Logger.getLogger("Minecraft");
    private final String PRESSURE_PLATE = "PRESSURE_PLATE";
    private final String LEVER = "LEVER";
    private final String BUTTON = "BUTTON";
    private final String OBSERVER = "OBSERVER";


    @EventHandler
    public void OnPlayerInteract(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Material blockMaterial = clickedBlock.getType();
        String blockName = blockMaterial.toString().toUpperCase();
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Interact Event triggered " + blockMaterial);
        }

        /**
        // exit if not a physical interaction
        if (event.getAction() != Action.PHYSICAL) {
            if (config.getBoolean("debug")) {
                log.info("[ContainerTracker] Interact Event ignored: action was not of type PHYSICAL");
            }
            return;
        }
        */
        if(!(blockName.contains(PRESSURE_PLATE) || blockName.contains(LEVER) || blockName.contains(BUTTON))){
            return;
        }
        if (config.getBoolean("debug")) {
            log.info("[ContainerTracker] Logging Information:\n"
                    + "Timestamp: " + System.currentTimeMillis() + "\n"
                    + "Player: " + event.getPlayer().getName() + " : " + event.getPlayer().getUniqueId() + "\n"
                    + "Location: " + event.getPlayer().getLocation() + "\n"
                    + "Block Type: " +  event.getClickedBlock().getType() + "\n"
            );
        }
        ContainerTracker.getInstance().getQueryer().logNewPhysicalInteraction(event.getPlayer(), event.getClickedBlock());
    }
}