package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_7  extends SchemaVersion {
    private static final String ADD_COLUMNS_REGION = "    ALTER TABLE `whimc_action_physical`\n"+
            " ADD COLUMN  region_name        TEXT     ;";
    public Schema_7() {
        super(7, null);
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

    }
}
