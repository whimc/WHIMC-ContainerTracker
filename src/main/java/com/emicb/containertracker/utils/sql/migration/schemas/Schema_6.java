package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_6  extends SchemaVersion {
    private static final String ADD_COLUMNS_SUCCESS = "    ALTER TABLE `whimc_barrelbot_outcome`\n"+
            " ADD COLUMN  puzzle_name        TEXT     NOT NULL;";
    private static final String DROP_COLUMNS_SUCCESS = "    ALTER TABLE `whimc_barrelbot_outcome`\n"+
            " DROP COLUMN  puzzle_id        ;";
    public Schema_6() {
        super(6, new Schema_7());
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(ADD_COLUMNS_SUCCESS)) {
            statement.execute();
        }
        try (PreparedStatement statement = connection.prepareStatement(DROP_COLUMNS_SUCCESS)) {
            statement.execute();
        }
    }
}
