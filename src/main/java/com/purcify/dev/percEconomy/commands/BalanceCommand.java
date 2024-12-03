package com.purcify.dev.percEconomy.commands;

import com.purcify.dev.percEconomy.PercEconomy;
import com.purcify.dev.percEconomy.PlayerBalanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// Handles the /balance command.
public class BalanceCommand implements CommandExecutor, TabCompleter {

    private PlayerBalanceManager balanceManager = new PlayerBalanceManager();
    private PercEconomy plugin = PercEconomy.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$"); // Get the currency symbol

        if (!(sender instanceof Player)) { // Check if the sender is a player
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        // Handle Arguments for the /balance command
        if (args.length == 0) {
            // Show own balance
            double balance = balanceManager.getBalance(player.getUniqueId());
            String message = plugin.getConfig().getString("messages.balance.self");
            message = message.replace("{balance}", String.format("%.2f", balance))
                    .replace("{currency}", currencySymbol)
                    .replace("{player}", player.getName());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));;
        } else if (args.length == 1 && player.hasPermission("perceconomy.balance.others")) {
            // Show another player's balance
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                double balance = balanceManager.getBalance(target.getUniqueId());
                String message = plugin.getConfig().getString("messages.balance.others");
                message = message.replace("{balance}", String.format("%.2f", balance))
                        .replace("{currency}", currencySymbol)
                        .replace("{player}", target.getName());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            } else {
                String message = plugin.getConfig().getString("messages.balance.player-not-found");
                message = message.replace("{target}", args[0]);
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            }
        } else {
            String usage = plugin.getConfig().getString("messages.balance.usage");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
        }

        return true;
    }

    // Tab completion for player names
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission("perceconomy.balance.others")) {
            String partialName = args[0].toLowerCase();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getName().toLowerCase().startsWith(partialName)) {
                    completions.add(onlinePlayer.getName());
                }
            }
        }
        return completions;
    }
}
