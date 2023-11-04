package com.emicb.containertracker.utils.sql.migration.schemas;



import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Schema to create skills table in db
 */
public class Schema_1 extends SchemaVersion {

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS `whimc_inventories` (" +
                    "  `rowid`       INT    AUTO_INCREMENT NOT NULL," +
                    "  `uuid`        VARCHAR(36)           NOT NULL," +
                    "  `username`    VARCHAR(16)           NOT NULL," +
                    "  `world`    VARCHAR(36)           NOT NULL," +
                    "  `x`           DOUBLE                NOT NULL," +
                    "  `y`           DOUBLE                NOT NULL," +
                    "  `z`           DOUBLE                NOT NULL," +
                    "  `time`        BIGINT                NOT NULL," +
                    "  `slot1`    VARCHAR(36)           ," +
                    "  `slot2`    VARCHAR(36)           ," +
                    "  `slot3`    VARCHAR(36)           ," +
                    "  `slot4`    VARCHAR(36)           ," +
                    "  `slot5`    VARCHAR(36)           ," +
                    "  `slot6`    VARCHAR(36)           ," +
                    "  `slot7`    VARCHAR(36)           ," +
                    "  `slot8`    VARCHAR(36)           ," +
                    "  `slot9`    VARCHAR(36)           ," +
                    "  `slot10`    VARCHAR(36)           ," +
                    "  `slot11`    VARCHAR(36)           ," +
                    "  `slot12`    VARCHAR(36)           ," +
                    "  `slot13`    VARCHAR(36)           ," +
                    "  `slot14`    VARCHAR(36)           ," +
                    "  `slot15`    VARCHAR(36)           ," +
                    "  `slot16`    VARCHAR(36)           ," +
                    "  `slot17`    VARCHAR(36)           ," +
                    "  `slot18`    VARCHAR(36)           ," +
                    "  `slot19`    VARCHAR(36)           ," +
                    "  `slot20`    VARCHAR(36)           ," +
                    "  `slot21`    VARCHAR(36)           ," +
                    "  `slot22`    VARCHAR(36)           ," +
                    "  `slot23`    VARCHAR(36)           ," +
                    "  `slot24`    VARCHAR(36)           ," +
                    "  `slot25`    VARCHAR(36)           ," +
                    "  `slot26`    VARCHAR(36)           ," +
                    "  `slot27`    VARCHAR(36)           ," +
                    "  PRIMARY KEY    (`rowid`));";

    /**
     * Constructor to specify which migrations to do
     */
    public Schema_1() {
        super(1, null);
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.execute();
        } catch (Exception e){

        }
    }


}
