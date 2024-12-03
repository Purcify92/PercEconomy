package com.purcify.dev.percEconomy;

import org.bukkit.plugin.java.JavaPlugin;
import com.purcify.dev.percEconomy.commands.BalanceCommand;
import com.purcify.dev.percEconomy.commands.PayCommand;

// Main class of the PercEconomy plugin.
public class PercEconomy extends JavaPlugin {
    private static PercEconomy instance;

    @Override
    public void onEnable() {
        instance = this;

        // Save the default config.yml if it doesn't exist
        saveDefaultConfig();

        // Initialize the database
        DatabaseManager.getInstance().setupDatabase();

        // Register commands
        getCommand("balance").setExecutor(new BalanceCommand());
        getCommand("pay").setExecutor(new PayCommand());
    }

    @Override
    public void onDisable() {
        // Close the database connection
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Get the instance of the plugin.
     *
     * @return the plugin instance
     */
    public static PercEconomy getInstance() {
        return instance;
    }
}
