package com.purcify.dev.percEconomy;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// Manages the SQLite database connection.
public class DatabaseManager {
    private static DatabaseManager instance = new DatabaseManager();
    private Connection connection;

    public static DatabaseManager getInstance() {
        return instance;
    }

    /**
     * Sets up the SQLite database connection and creates the player_balances table if it doesn't exist.
     */
    public void setupDatabase() {
        try {
            File dbFile = new File(PercEconomy.getInstance().getDataFolder(), "economy.db");
            if (!dbFile.exists()) {
                PercEconomy.getInstance().getDataFolder().mkdirs();
                dbFile.createNewFile();
            }
            String url = "jdbc:sqlite:" + dbFile.getAbsolutePath();
            connection = DriverManager.getConnection(url);

            // Create the player_balances table if it doesn't exist
            String sql = "CREATE TABLE IF NOT EXISTS player_balances (" +
                    "uuid TEXT PRIMARY KEY," +
                    "balance DOUBLE" +
                    ");";
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the SQLite database connection.
     */
    public void closeDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the SQLite database connection.
     *
     * @return the database connection
     */
    public Connection getConnection() {
        return connection;
    }
}
