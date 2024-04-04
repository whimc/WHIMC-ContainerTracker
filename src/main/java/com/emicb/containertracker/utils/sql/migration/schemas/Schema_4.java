package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_4  extends SchemaVersion {
    private static final String ADD_COLUMNS ="    ALTER TABLE whimc_containers\n" +
            " ADD COLUMN  inventory_type    TEXT    ," +
            " ADD COLUMN  puzzle_id         INT    ," +
            " ADD COLUMN  puzzle_type       INT    ;";
    /**
     * Constructor to specify which migrations to do
     */
    public Schema_4() {
        super(4, null);
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) throws SQLException {

        try (PreparedStatement statement = connection.prepareStatement(ADD_COLUMNS)) {
            statement.execute();
        }
    }
}