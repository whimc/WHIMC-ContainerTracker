package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_8  extends SchemaVersion {
    private static final String ADD_COLUMNS_REGION = "    ALTER TABLE `whimc_containers`\n"+
            " ADD COLUMN  region_name        TEXT     ;";
    private static final String DROP_COLUMNS_ID = "    ALTER TABLE `whimc_containers`\n"+
            " DROP COLUMN  puzzle_id        ;";
    private static final String DROP_COLUMNS_TYPE= "    ALTER TABLE `whimc_containers`\n"+
            " DROP COLUMN  puzzle_type       ;";
    public Schema_8() {
        super(8, null);
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ADD_COLUMNS_REGION)) {
            statement.execute();
        }
        try (PreparedStatement statement = connection.prepareStatement(DROP_COLUMNS_ID)) {
            statement.execute();
        }
        try (PreparedStatement statement = connection.prepareStatement(DROP_COLUMNS_TYPE)) {
            statement.execute();
        }

    }
}