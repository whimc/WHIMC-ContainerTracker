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
import org.apache.commons.lang.StringUtils;
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
    /**
     * Defines system chat listener for barrelbot completion
     */
    public void registerChatListener(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SYSTEM_CHAT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                final String BARRELBOTFLAG = "[Barrelbot]";
                final String JSONMESSAGEKEY = "extra";
                final String JSONTEXTKEY = "text";

                String message = "";
                Player recipient = event.getPlayer();
                try {
                    message = event.getPacket().getChatComponents().getValues().get(0).getJson();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(message.contains(BARRELBOTFLAG)) {
                    Logger log = Logger.getLogger("Minecraft");
                    log.info("[ContainerTracker] Chat Event triggered");
                    JsonObject jsonMessage;
                    jsonMessage = new JsonParser().parse(message).getAsJsonObject();
                    JsonElement jsonElement = jsonMessage.get(JSONMESSAGEKEY);
                    if(jsonElement != null) {
                        JsonArray jsonArray = jsonElement.getAsJsonArray();
                        //Has username in proper location
                        if(recipient != null) {
                            JsonElement puzzleNameElement = jsonArray.get(jsonArray.size() - 2);
                            JsonObject jsonPuzzleIDObject = (JsonObject) puzzleNameElement;
                            JsonElement puzzleNameTextElement = jsonPuzzleIDObject.get(JSONTEXTKEY);
                            String puzzleNameString = puzzleNameTextElement.getAsString();
                            ContainerTracker.getInstance().getQueryer().storeNewBarrelbotOutcome(recipient, true, puzzleNameString);
                        }
                    } else {
                        //Found barrelbot message but not formatted how expected
                        log.info("[ContainerTracker] Barrelbot outcome not tracked, no text found.");
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
