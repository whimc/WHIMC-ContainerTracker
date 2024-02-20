package com.emicb.containertracker.commands;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.logging.Logger;

public class ToggleDebug implements CommandExecutor {
    // Set up config
    FileConfiguration config = ContainerTracker.getInstance().getConfig();

    // Set up logger
    private final Logger log = Logger.getLogger("Minecraft");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: add permission guards

        // flip the debug boolean
        config.set("debug", !config.getBoolean("debug"));

        // notify player
        if (config.getBoolean("debug")) {
            log.info("[Container Tracker] Debug Mode: Enabled by " + sender.getName());
            sender.sendMessage("[Container Tracker] Debug Mode: Enabled");
        } else {
            log.info("[Container Tracker] Debug Mode: Disabled by " + sender.getName());
            sender.sendMessage("[Container Tracker] Debug Mode: Disabled");
        }

        return true;
    }
}
