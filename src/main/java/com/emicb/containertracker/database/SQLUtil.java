package com.emicb.containertracker.database;

public class SQLUtil {
    // TODO: Add columns for all data (item metadata + slot position)
    /** The SQL command to create a table.This is for storing the current item status of containers */
    public static final String CREATE_TABLE_CONTAINERS =
            "CREATE TABLE IF NOT EXISTS `whimc_container_status` (" +
                    "  `rowid`    BIGINT AUTO_INCREMENT NOT NULL," +
                    "  `x`        INT                   NOT NULL," +
                    "  `y`        INT                   NOT NULL," +
                    "  `z`        INT                   NOT NULL," +
                    "  `world`    VARCHAR(64)           NOT NULL," +
                    "  `username` VARCHAR(16)           NOT NULL," +
                    "  `uuid`     VARCHAR(36)           NOT NULL," +
                    "  `time` 	  BIGINT                NOT NULL," +
                    "  PRIMARY KEY (`rowid`));";

    // TODO: Make sure this has columns for all data we want to collect
    /** The SQL command to create a table. This is for storing physical interactions
     * https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/block/Action.html#PHYSICAL */
    public static final String CREATE_TABLE_INTERACT_PHYS =
            "CREATE TABLE IF NOT EXISTS `whimc_physical_interact` (" +
                    "  `rowid`    BIGINT AUTO_INCREMENT NOT NULL," +
                    "  `x`        INT                   NOT NULL," +
                    "  `y`        INT                   NOT NULL," +
                    "  `z`        INT                   NOT NULL," +
                    "  `world`    VARCHAR(64)           NOT NULL," +
                    "  `username` VARCHAR(16)           NOT NULL," +
                    "  `uuid`     VARCHAR(36)           NOT NULL," +
                    "  `time` 	  BIGINT                NOT NULL," +
                    "  PRIMARY KEY (`rowid`));";
}
