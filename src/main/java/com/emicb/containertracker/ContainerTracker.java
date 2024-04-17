package com.emicb.containertracker;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.emicb.containertracker.commands.ToggleDebug;
import com.emicb.containertracker.listeners.InventoryCloseListener;
import com.emicb.containertracker.listeners.PlayerInteractListener;
import com.emicb.containertracker.utils.sql.Queryer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ContainerTracker extends JavaPlugin {
    private static ContainerTracker plugin;
    private Queryer queryer;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        PluginManager pluginManager = Bukkit.getPluginManager();
        plugin = this;

        // Set up logger
        Logger log = Logger.getLogger("Minecraft");

        this.queryer = new Queryer(this, q -> {
            // If we couldn't connect to the database disable the plugin
            if (q == null) {
                log.severe("[ContainerTracker] Plugin Disabled: could not establish MySQL connection!");
                return;
            }
        });

        // Link listeners
        pluginManager.registerEvents(new InventoryCloseListener(), this);
        pluginManager.registerEvents(new PlayerInteractListener(), this);
        registerChatListener();
        // Link commands
        // TODO: change command structure to sub-commands
        getServer().getPluginCommand("ct-toggle-debug").setExecutor(new ToggleDebug());
        // TODO: add enable / disable logging commands

        log.info("[ContainerTracker] Started successfully!");
    }

    public void registerChatListener(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final String BARRELBOTFLAG = "[Barrelbot]";
                final String JSONMESSAGEKEY = "extra";
                final String JSONTEXTKEY = "text";
                final String COMPLETEDKEYWORD = "completed";
                String message = "";
                try {
                    message = event.getPacket().getChatComponents().getValues().get(0).getJson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(message.contains(BARRELBOTFLAG)) {
                    boolean completed = false;
                    if(message.contains(COMPLETEDKEYWORD)){
                        completed = true;
                    }
                    Player player = event.getPlayer();
                    Logger log = Logger.getLogger("Minecraft");
                    log.info("[ContainerTracker] Chat Event triggered");
                    JsonObject jsonMessage;
                    try {
                        jsonMessage = new JsonParser().parse(message).getAsJsonObject();
                        JsonElement jsonElement = jsonMessage.get(JSONMESSAGEKEY);
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        JsonElement puzzleIDElement = jsonArray.get(jsonArray.size()-2);
                        JsonObject jsonPuzzleIDObject = (JsonObject) puzzleIDElement;
                        String puzzleIDString = jsonPuzzleIDObject.get(JSONTEXTKEY).getAsString();
                        if (puzzleIDString != "") {
                            int puzzleID = Integer.parseInt(puzzleIDString);
                            ContainerTracker.getInstance().getQueryer().storeNewBarrelbotOutcome(player, completed, puzzleID);
                        } else {
                            ContainerTracker.getInstance().getQueryer().storeNewBarrelbotOutcome(player, completed, -1);
                        }
                    }  catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ContainerTracker getInstance() {
        return plugin;
    }
    public Queryer getQueryer() {
        return this.queryer;
    }
}
