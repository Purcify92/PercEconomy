package com.purcify.dev.percEconomy.commands;

import com.purcify.dev.percEconomy.PlayerBalanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// Handles the /pay command.
public class PayCommand implements CommandExecutor, TabCompleter {

    private PlayerBalanceManager balanceManager = new PlayerBalanceManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) { // Check if the sender is a player
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length != 2) { // Check if the correct number of arguments is provided
            player.sendMessage(ChatColor.RED + "Usage: /pay <player> <amount>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) { // Check if the target player is online
            player.sendMessage(ChatColor.RED + "Player not found or not online.");
            return true;
        }

        if (target.equals(player)) { // Check if the target player is not the sender
            player.sendMessage(ChatColor.RED + "You cannot pay yourself.");
            return true;
        }

        // Parse the amount to pay
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                player.sendMessage(ChatColor.RED + "Amount must be positive.");
                return true;
            }
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Invalid amount.");
            return true;
        }

        // Withdraw the amount from the sender and add it to the target
        if (balanceManager.withdrawBalance(player.getUniqueId(), amount)) {
            balanceManager.addBalance(target.getUniqueId(), amount);
            player.sendMessage(ChatColor.GREEN + "You have paid " + ChatColor.GOLD + amount + ChatColor.GREEN + " to " + ChatColor.AQUA + target.getName());
            target.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.GREEN + " has paid you " + ChatColor.GOLD + amount);
        } else {
            player.sendMessage(ChatColor.RED + "You do not have enough balance.");
        }

        return true;
    }

    // Tab completion for player names and common amounts
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partialName = args[0].toLowerCase();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (!onlinePlayer.equals(sender) && onlinePlayer.getName().toLowerCase().startsWith(partialName)) {
                    completions.add(onlinePlayer.getName());
                }
            }
        } else if (args.length == 2) {
            // Suggest common amounts
            completions.add("10");
            completions.add("50");
            completions.add("100");
            completions.add("500");
        }
        return completions;
    }
}
