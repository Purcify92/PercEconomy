package com.purcify.dev.percEconomy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import com.purcify.dev.percEconomy.commands.BalanceCommand;
import com.purcify.dev.percEconomy.commands.PayCommand;
import com.purcify.dev.percEconomy.commands.EconomyCommand;

public class PercEconomy extends JavaPlugin {
    private static PercEconomy instance;
    private VaultHook economyHook;

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
        getCommand("economy").setExecutor(new EconomyCommand());

        // Setup Vault integration
        if (setupEconomy()) {
            getLogger().info("Vault integration enabled successfully!");
        } else {
            getLogger().warning("Vault not found! Plugin will still work, but without Vault integration.");
        }
    }

    @Override
    public void onDisable() {
        // Close the database connection
        DatabaseManager.getInstance().closeDatabase();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        economyHook = new VaultHook();
        getServer().getServicesManager().register(Economy.class, economyHook, this, ServicePriority.Normal);
        return true;
    }

    public static PercEconomy getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }
}
