package com.emicb.containertracker.database;

import com.emicb.containertracker.ContainerTracker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class MySQLConnection {
    public static final String URL_TEMPLATE = "jdbc:mysql://%s:%s/%s";

    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final String url;
    private final int port;

    private final ContainerTracker plugin;

    public MySQLConnection(ContainerTracker plugin) {
        // TODO: Add error handling
        this.host = plugin.getConfig().getString("mysql.host");
        this.port = plugin.getConfig().getInt("mysql.port");
        this.database = plugin.getConfig().getString("mysql.database");
        this.username = plugin.getConfig().getString("mysql.username");
        this.password = plugin.getConfig().getString("mysql.password");

        this.url = String.format(URL_TEMPLATE, this.host, this.port, this.database);

        this.plugin = plugin;
    }

    public boolean initialize() {
        if (getConnection() == null) {
            return false;
        }

        //SchemaManager manager = new SchemaManager(this.plugin, this.connection);
        //return manager.initialize();
        return  false;
    }

    public Connection getConnection() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                return this.connection;
            }
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException ignored) {
            return null;
        }

        return this.connection;
    }
}
