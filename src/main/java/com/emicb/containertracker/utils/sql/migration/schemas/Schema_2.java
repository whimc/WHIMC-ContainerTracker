package com.emicb.containertracker.utils.sql.migration.schemas;

import com.emicb.containertracker.utils.sql.migration.SchemaVersion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Schema_2  extends SchemaVersion {
    private static final String DROP_COLUMNS ="    ALTER TABLE whimc_containers\n" +
            " DROP COLUMN  slot1              ," +
            " DROP COLUMN  slot2              ," +
            " DROP COLUMN  slot3              ," +
            " DROP COLUMN  slot4              ," +
            " DROP COLUMN  slot5              ," +
            " DROP COLUMN  slot6              ," +
            " DROP COLUMN  slot7              ," +
            " DROP COLUMN  slot8              ," +
            " DROP COLUMN  slot9              ," +
            " DROP COLUMN  slot10             ," +
            " DROP COLUMN  slot11             ," +
            " DROP COLUMN  slot12             ," +
            " DROP COLUMN  slot13             ," +
            " DROP COLUMN  slot14             ," +
            " DROP COLUMN  slot15             ," +
            " DROP COLUMN  slot16             ," +
            " DROP COLUMN  slot17             ," +
            " DROP COLUMN  slot18             ," +
            " DROP COLUMN  slot19             ," +
            " DROP COLUMN  slot20             ," +
            " DROP COLUMN  slot21             ," +
            " DROP COLUMN  slot22             ," +
            " DROP COLUMN  slot23             ," +
            " DROP COLUMN  slot24             ," +
            " DROP COLUMN  slot25             ," +
            " DROP COLUMN  slot26             ," +
            " DROP COLUMN  slot27             ;" ;
    private static final String ADD_COLUMNS ="    ALTER TABLE whimc_containers\n" +
            " ADD COLUMN  slot1         TEXT    ," +
            " ADD COLUMN  slot2         TEXT    ," +
            " ADD COLUMN  slot3         TEXT    ," +
            " ADD COLUMN  slot4         TEXT    ," +
            " ADD COLUMN  slot5         TEXT    ," +
            " ADD COLUMN  slot6         TEXT    ," +
            " ADD COLUMN  slot7         TEXT    ," +
            " ADD COLUMN  slot8         TEXT    ," +
            " ADD COLUMN  slot9         TEXT    ," +
            " ADD COLUMN  slot10        TEXT    ," +
            " ADD COLUMN  slot11        TEXT    ," +
            " ADD COLUMN  slot12        TEXT    ," +
            " ADD COLUMN  slot13        TEXT    ," +
            " ADD COLUMN  slot14        TEXT    ," +
            " ADD COLUMN  slot15        TEXT    ," +
            " ADD COLUMN  slot16        TEXT    ," +
            " ADD COLUMN  slot17        TEXT    ," +
            " ADD COLUMN  slot18        TEXT    ," +
            " ADD COLUMN  slot19        TEXT    ," +
            " ADD COLUMN  slot20        TEXT    ," +
            " ADD COLUMN  slot21        TEXT    ," +
            " ADD COLUMN  slot22        TEXT    ," +
            " ADD COLUMN  slot23        TEXT    ," +
            " ADD COLUMN  slot24        TEXT    ," +
            " ADD COLUMN  slot25        TEXT    ," +
            " ADD COLUMN  slot26        TEXT    ," +
            " ADD COLUMN  slot27        TEXT    ;" ;
    /**
     * Constructor to specify which migrations to do
     */
    public Schema_2() {
        super(2, new Schema_3());
    }

    /**
     * Method to migrate SQL
     * @param connection SQL connection
     */
    @Override
    protected void migrateRoutine(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DROP_COLUMNS)) {
            statement.execute();
        }
        try (PreparedStatement statement = connection.prepareStatement(ADD_COLUMNS)) {
            statement.execute();
        }
    }
}
