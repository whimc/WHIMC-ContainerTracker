package com.emicb.containertracker.commands;

import com.emicb.containertracker.ContainerTracker;
import com.emicb.containertracker.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PuzzleProgressCommand implements CommandExecutor, TabCompleter {
    // Set up config
    FileConfiguration config = ContainerTracker.getInstance().getConfig();

    // Set up logger
    private final Logger log = Logger.getLogger("Minecraft");
    private ContainerTracker plugin;
    public PuzzleProgressCommand(ContainerTracker plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("You need to add another argument. Please try again");
            return true;
        }
        Player playerPuzzlesToRetrieve = Bukkit.getPlayer(args[0]);
        if(playerPuzzlesToRetrieve == null){
            sender.sendMessage("Player does not exist on the server currently. Please try again");
            return true;
        }
        HashMap<String, Integer> puzzlesCompleted = plugin.getPlayerPuzzleSuccess(playerPuzzlesToRetrieve);
        if(puzzlesCompleted == null){
            sender.sendMessage("Player has not completed any puzzles on this world!");
        } else {
            Utils.msg(sender, "&b&l" + playerPuzzlesToRetrieve.getName() + " has completed:");
            for(String key : puzzlesCompleted.keySet()){
                if(puzzlesCompleted.get(key) > 1){
                    Utils.msg(sender, "    &6" + key + " " + puzzlesCompleted.get(key) + " times");
                } else {
                    Utils.msg(sender, "    &6" + key);
                }
            }
        }
        if (config.getBoolean("debug")) {
            log.info("[Container Tracker]" + sender.getName() + " viewed " + playerPuzzlesToRetrieve.getName() + "'s puzzle progress");
        }
        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            list.add(p.getName());
        }

        return list;
    }
}