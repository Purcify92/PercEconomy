package com.purcify.dev.percEconomy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

// Manages player balances in the SQLite database.
public class PlayerBalanceManager {

    /**
     * Retrieves the balance of a player.
     *
     * @param uuid the UUID of the player
     * @return the player's balance
     */
    public double getBalance(UUID uuid) {
        double balance = 0.0;
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            String sql = "SELECT balance FROM player_balances WHERE uuid=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            } else {
                // If player not found, set starting balance
                balance = PercEconomy.getInstance().getConfig().getDouble("starting-balance", 0.0);
                setBalance(uuid, balance);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    /**
     * Sets the balance of a player.
     *
     * @param uuid   the UUID of the player
     * @param amount the amount to set
     */
    public void setBalance(UUID uuid, double amount) {
        try {
            Connection conn = DatabaseManager.getInstance().getConnection();
            String sql = "REPLACE INTO player_balances (uuid, balance) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, uuid.toString());
            ps.setDouble(2, amount);
            ps.executeUpdate();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an amount to the player's balance.
     *
     * @param uuid   the UUID of the player
     * @param amount the amount to add
     */
    public void addBalance(UUID uuid, double amount) {
        double currentBalance = getBalance(uuid);
        setBalance(uuid, currentBalance + amount);
    }

    /**
     * Withdraws an amount from the player's balance if sufficient funds are available.
     *
     * @param uuid   the UUID of the player
     * @param amount the amount to withdraw
     * @return true if the withdrawal was successful, false otherwise
     */
    public boolean withdrawBalance(UUID uuid, double amount) {
        double currentBalance = getBalance(uuid);
        if (currentBalance >= amount) {
            setBalance(uuid, currentBalance - amount);
            return true;
        } else {
            return false;
        }
    }
}
