package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_3 extends SchemaVersion {

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS `whimc_action_physical` (" +
                    "  `rowid`       INT    AUTO_INCREMENT NOT NULL," +
                    "  `uuid`        VARCHAR(36)           NOT NULL," +
                    "  `username`    VARCHAR(16)           NOT NULL," +
                    "  `world`       VARCHAR(36)           NOT NULL," +
                    "  `x`           DOUBLE                NOT NULL," +
                    "  `y`           DOUBLE                NOT NULL," +
                    "  `z`           DOUBLE                NOT NULL," +
                    "  `time`        BIGINT                NOT NULL," +
                    "  `type`        VARCHAR(36)           NOT NULL," +
                    "  PRIMARY KEY   (`rowid`));";

    /**
     * Constructor to specify which migrations to do
     */
    protected Schema_3() {
        super(3, new Schema_4());
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_TABLE)) {
            statement.execute();
        } catch (Exception e){

        }
    }
}
