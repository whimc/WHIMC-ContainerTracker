package com.emicb.containertracker.commands;

import com.emicb.containertracker.ContainerTracker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class ToggleDebug implements CommandExecutor {
    // Set up config
    FileConfiguration config = ContainerTracker.getInstance().getConfig();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // TODO: add permission guards

        // flip the debug boolean
        config.set("debug", !config.getBoolean("debug"));

        return true;
    }
}
