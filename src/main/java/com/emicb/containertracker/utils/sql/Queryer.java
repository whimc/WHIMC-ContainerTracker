package com.emicb.containertracker.utils.sql;

import com.emicb.containertracker.ContainerTracker;
import com.emicb.containertracker.utils.Utils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_20_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
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
            "INSERT INTO whimc_inventories " +
                    "(uuid, username, world, x, y, z, time, slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9, slot10," +
                    "slot11, slot12, slot13, slot14, slot15, slot16, slot17, slot18, slot19, slot20, slot21, slot22, slot23, slot24, slot25," +
                    "slot26, slot27) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


    private final ContainerTracker plugin;
    private final MySQLConnection sqlConnection;
    private  Logger log;
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
     * @param contents the contents of the inventory
     * @return PreparedStatement
     * @throws SQLException
     */
    private PreparedStatement insertInventory(Connection connection, Player player,ItemStack[] contents) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SAVE_INVENTORY, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, player.getUniqueId().toString());
        statement.setString(2, player.getName());
        statement.setString(3, player.getWorld().getName());
        statement.setDouble(4, player.getLocation().getX());
        statement.setDouble(5, player.getLocation().getY());
        statement.setDouble(6, player.getLocation().getZ());
        statement.setLong(7, System.currentTimeMillis());
        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) {
                log.info("[ContainerTracker] slot " + i + " has: nothing");
                statement.setString(i+8, null);
                continue;
            }
            net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = nmsItem.v();
            if(tag != null){
                //Parse nbttag as string ex: {CustomModelData:130000,barrelbot:{instruction:"move_forward"},display:{Lore:['{"text":"Moves the barrelbot forward","color":"gray","italic":false}','{"text":"1 tile, if it is open","color":"gray","italic":false}','{"text":" "}','{"text":"Instruction","color":"blue","italic":false}'],Name:'{"text":"Move Forward","color":"#FFAA00","italic":false}'}}
                String tagInfo = tag.toString();
                //Gets text for everything in the Name section
                int indexName = tagInfo.indexOf("Name");
                String name = tagInfo.substring(indexName);
                int indexSquigglyLeft = name.indexOf('{');
                int indexSquigglyRight= name.indexOf('}');
                name = name.substring(indexSquigglyLeft+1,indexSquigglyRight);
                //Gets text for everything in the text section within the Name
                int indexText = name.indexOf("text");
                String text = name.substring(indexText);
                int indexColon = text.indexOf(':');
                int indexComma = text.indexOf(',');
                text = text.substring(indexColon + 1, indexComma);
                log.info("[ContainerTracker] slot " + i + " has: " + text);
                statement.setString(i + 8, text);
            } else {
                log.info("[ContainerTracker] slot " + i + " has: " + nmsItem);
                statement.setString(i + 8, nmsItem.toString());
            }
        }
        return statement;
    }

    /**
     * Stores an inventory for a specific player and inventory on close
     * @param player Player closing inventory
     * @param contents The contents of the chest, barrelbot, or shulker
     */
    public void storeNewInventory(Player player, ItemStack[] contents) {
        async(() -> {
            Utils.debug("Storing command to database:");

            try (Connection connection = this.sqlConnection.getConnection()) {
                try (PreparedStatement statement = insertInventory(connection, player, contents)) {
                    String query = statement.toString().substring(statement.toString().indexOf(" ") + 1);
                    Utils.debug("  " + query);
                    statement.executeUpdate();
                    log.info("Inventory has been stored!");
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
