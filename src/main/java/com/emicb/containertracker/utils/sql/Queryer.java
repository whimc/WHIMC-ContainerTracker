package com.emicb.containertracker.utils.sql;

import com.emicb.containertracker.ContainerTracker;
import com.emicb.containertracker.utils.Utils;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.sql.*;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Handles storing position data
 *
 * @author Sam
 */
public class Queryer {

    //Query for inserting skills into the database.
    private static final String QUERY_SAVE_INVENTORY =
            "INSERT INTO whimc_containers " +
                    "(uuid, username, world, x, y, z, time, slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9, slot10," +
                    "slot11, slot12, slot13, slot14, slot15, slot16, slot17, slot18, slot19, slot20, slot21, slot22, slot23, slot24, slot25," +
                    "slot26, slot27, inventory_type, region_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // query for inserting physical interactions into the database
    // Action.PHYSICAL docs: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/event/block/Action.html#PHYSICAL
    private static final String QUERY_SAVE_ACTION_PHYSICAL =
            "INSERT INTO whimc_action_physical " +
                    "(uuid, username, world, x, y, z, time, type, region_name) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String QUERY_SAVE_BARRELBOT_OUTCOME =
            "INSERT INTO whimc_barrelbot_outcome " +
                    "(uuid, username, world, x, y, z, time, outcome, puzzle_name, inventory_row_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String QUERY_GET_LATEST_INVENTORY =
            "SELECT t1.* " +
                "FROM whimc_containers t1 " +
                "WHERE t1.time = (SELECT MAX(t2.time) " +
                "FROM whimc_containers t2 " +
                "WHERE t2.username = ?)";

    private final ContainerTracker plugin;
    private final MySQLConnection sqlConnection;
    private Logger log;

    // Set up config
    private final FileConfiguration config = ContainerTracker.getInstance().getConfig();

    /**
     * Constructor to instantiate instance variables and connect to SQL
     * @param plugin StudentFeedback plugin instance
     * @param callback callback to signal that process completed
     */
    public Queryer(ContainerTracker plugin, Consumer<Queryer> callback) {
        this.plugin = plugin;
        this.sqlConnection = new MySQLConnection(plugin);
        log = Logger.getLogger("Minecraft");
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final boolean success = sqlConnection.initialize();
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(success ? this : null));
        });
    }

    /**
     * Generated a PreparedStatement for saving an inventory
     * @param connection MySQL Connection
     * @param player Closing the inventory of a chest, barrel, etc.
     * @param inventory The inventory of the barrelbot or shulker
     * @param regionNames Names oof the regions where the container is located
     * @return PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement insertInventory(Connection connection, Player player, Inventory inventory, String regionNames) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SAVE_INVENTORY, Statement.RETURN_GENERATED_KEYS);
        ItemStack[] contents = inventory.getContents();
        String inventoryType = inventory.getType().name();
        Location inventoryLocation = inventory.getLocation();
        final int CHEST_SIZE = 27;
        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getName());
        statement.setString(3, inventoryLocation.getWorld().getName());
        statement.setDouble(4, inventoryLocation.getX());
        statement.setDouble(5, inventoryLocation.getY());
        statement.setDouble(6, inventoryLocation.getZ());
        statement.setLong(7, System.currentTimeMillis());
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            //Safety check for larger chests can't store in our db
            if(i >= CHEST_SIZE){
                if (config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " is larger than what can be stored in the db and won't be tracked");
                }
                net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
                CompoundTag tag = nmsItem.getTag();
                if (item == null && config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: nothing");
                } else if (tag != null && config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: " + tag);
                } else {
                    if (config.getBoolean("debug")) {
                        log.info("[ContainerTracker] slot " + i + " has: " + nmsItem);
                    }
                }
                continue;
            }

            if (item == null) {
                if (config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: nothing");
                }
                statement.setString(i+8, null);
                continue;
            }
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            CompoundTag tag = nmsItem.getTag();
            if(tag != null){
                //Parse nbttag as string ex: {CustomModelData:130000,barrelbot:{instruction:"move_forward"},display:{Lore:['{"text":"Moves the barrelbot forward","color":"gray","italic":false}','{"text":"1 tile, if it is open","color":"gray","italic":false}','{"text":" "}','{"text":"Instruction","color":"blue","italic":false}'],Name:'{"text":"Move Forward","color":"#FFAA00","italic":false}'}}
                String text = "";
                final String BARRELBOTKEY = "barrelbot";
                final String INSTRUCTIONKEY = "instruction";
                if(tag.contains(BARRELBOTKEY)) {
                    CompoundTag barrelbot = tag.getCompound(BARRELBOTKEY);
                    if (barrelbot.contains(INSTRUCTIONKEY)) {
                        text = barrelbot.getString(INSTRUCTIONKEY);
                    } else {
                        text = barrelbot.toString();
                    }
                } else {
                    text = tag.toString();
                }
                if (config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: " + text);
                }
                statement.setString(i + 8, text);
            } else {
                if (config.getBoolean("debug")) {
                    log.info("[ContainerTracker] slot " + i + " has: " + nmsItem);
                }
                statement.setString(i + 8, nmsItem.toString());
            }
        }
        statement.setString(35, inventoryType);
        statement.setString(36, regionNames);
        return statement;
    }

    /**
     * Stores an inventory for a specific player and inventory on close
     * @param player Player closing inventory
     * @param inventory The inventory of the barrelbot or shulker
     * @param regionNames Names of regions that the container is in
     */
    public void storeNewInventory(Player player, Inventory inventory, String regionNames) {
        async(() -> {
            Utils.debug("Storing command to database:");

            try (Connection connection = this.sqlConnection.getConnection()) {
                try (PreparedStatement statement = insertInventory(connection, player, inventory, regionNames)) {
                    String query = statement.toString().substring(statement.toString().indexOf(" ") + 1);
                    Utils.debug("  " + query);
                    statement.executeUpdate();
                    if (config.getBoolean("debug")) {
                        log.info("[Container Tracker] Container's inventory has been logged");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Generate a prepared statement for logging pressure plate interaction.
     * @param connection The MySQL connection
     * @param player The player interacting with the pressure plate
     * @param completed true if solution solves puzzle, false if not
     * @param puzzleName unique id for puzzle
     * @param inventoryID the inventooryID associated with the outcome
     * @return the generated PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement insertBarrelbotOutcome(Connection connection, Player player, boolean completed, String puzzleName, int inventoryID) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SAVE_BARRELBOT_OUTCOME, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getName());
        statement.setString(3, player.getWorld().getName());
        statement.setDouble(4, player.getLocation().getX());
        statement.setDouble(5, player.getLocation().getY());
        statement.setDouble(6, player.getLocation().getZ());
        statement.setLong(7, System.currentTimeMillis());
        if(completed) {
            statement.setString(8, "Success");
        } else {
            statement.setString(8, "Failure");
        }
        statement.setString(9, puzzleName);
        statement.setInt(10, inventoryID);
        return statement;
    }

    /**
     * Stores an inventory for a specific player and inventory on close
     * @param player Player that finished executing an attempt
     * @param completed true if solution solves puzzle, false if not
     * @param puzzleName Name for puzzle
     */
    public void storeNewBarrelbotOutcome(Player player, boolean completed, String puzzleName) {
        async(() -> {
            Utils.debug("Storing command to database:");

            getInventoryID(player, id -> {
                try (Connection connection = this.sqlConnection.getConnection()) {
                        try (PreparedStatement statement = insertBarrelbotOutcome(connection, player, completed, puzzleName, id)) {
                            String query = statement.toString().substring(statement.toString().indexOf(" ") + 1);
                            Utils.debug("  " + query);
                            statement.executeUpdate();
                            if (config.getBoolean("debug")) {
                                log.info("[Container Tracker] Player barrelbot outcome has been logged");
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

            });
        }

    public void getInventoryID(Player player, Consumer<Integer> callback) {
        loadTemporaryInventoryID(QUERY_GET_LATEST_INVENTORY, statement -> {
            try {
                statement.setString(1, player.getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, callback);
    }

    /**
     * Generate a prepared statement for logging pressure plate interaction.
     * @param connection The MySQL connection
     * @param player The player interacting with the pressure plate
     * @return the generated PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement insertPhysicalInteraction(Connection connection, Player player, Block clickedBlock, String regionNames) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SAVE_ACTION_PHYSICAL, Statement.RETURN_GENERATED_KEYS);

        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getName());
        statement.setString(3, player.getWorld().getName());
        statement.setDouble(4, player.getLocation().getX());
        statement.setDouble(5, player.getLocation().getY());
        statement.setDouble(6, player.getLocation().getZ());
        statement.setLong(7, System.currentTimeMillis());
        statement.setString(8, clickedBlock.getType().toString());
        statement.setString(9, regionNames);
        return statement;
    }

    public void logNewPhysicalInteraction(Player player, Block clickedBlock, String regionNames) {
        async(() -> {
            try (Connection connection = this.sqlConnection.getConnection()) {
                try (PreparedStatement statement = insertPhysicalInteraction(connection, player, clickedBlock, regionNames)) {
                    String query = statement.toString().substring(statement.toString().indexOf(" ") + 1);
                    Utils.debug(" " + query);
                    statement.executeUpdate();
                    if (config.getBoolean("debug")) {
                        log.info("[Container Tracker] Interaction has been logged");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadTemporaryInventoryID(String query, Consumer<PreparedStatement> prepare, Consumer<Integer> callback) {
        async(() -> {
            try (Connection connection = this.sqlConnection.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    prepare.accept(statement);
                    try (ResultSet results = statement.executeQuery()) {
                        int id = -1;
                        while (results.next()) {
                            id = results.getInt("rowid");
                        }
                        sync(callback, id);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private <T> void sync(Consumer<T> cons, T val) {
        Bukkit.getScheduler().runTask(this.plugin, () -> cons.accept(val));
    }

    private void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(this.plugin, runnable);
    }

    private void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }
}
