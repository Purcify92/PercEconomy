package com.purcify.dev.percEconomy.commands;

import com.purcify.dev.percEconomy.PercEconomy;
import com.purcify.dev.percEconomy.PlayerBalanceManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

// Handles the /economy command and its subcommands.
public class EconomyCommand implements CommandExecutor, TabCompleter {

    private PlayerBalanceManager balanceManager = new PlayerBalanceManager();
    private PercEconomy plugin = PercEconomy.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("perceconomy.admin")) { // Check if the sender has permission
            String message = plugin.getConfig().getString("messages.economy.no-permission");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        if (args.length == 0) { // Check if the correct number of arguments is provided
            String usage = plugin.getConfig().getString("messages.economy.usage");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        // Handle subcommands
        switch (subCommand) {
            case "give":
                if (args.length != 3) {
                    String usage = plugin.getConfig().getString("messages.economy.usage");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                    return true;
                }
                handleGive(sender, args[1], args[2]);
                break;
            case "take":
                if (args.length != 3) {
                    String usage = plugin.getConfig().getString("messages.economy.usage");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                    return true;
                }
                handleTake(sender, args[1], args[2]);
                break;
            case "set":
                if (args.length != 3) {
                    String usage = plugin.getConfig().getString("messages.economy.usage");
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                    return true;
                }
                handleSet(sender, args[1], args[2]);
                break;
            case "reload":
                plugin.reloadPluginConfig();
                String message = plugin.getConfig().getString("messages.economy.reload");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                break;
            default:
                String usage = plugin.getConfig().getString("messages.economy.usage");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
                break;
        }

        return true;
    }

    // Handle the /economy give subcommand
    private void handleGive(CommandSender sender, String playerName, String amountStr) {
        Player target = Bukkit.getPlayer(playerName);
        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$");

        if (target == null || !target.isOnline()) {
            String message = plugin.getConfig().getString("messages.economy.player-not-found");
            message = message.replace("{target}", playerName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                String message = plugin.getConfig().getString("messages.economy.invalid-amount");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return;
            }
        } catch (NumberFormatException e) {
            String message = plugin.getConfig().getString("messages.economy.invalid-amount");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        balanceManager.addBalance(target.getUniqueId(), amount);
        double newBalance = balanceManager.getBalance(target.getUniqueId());

        String message = plugin.getConfig().getString("messages.economy.give");
        message = message.replace("{player}", target.getName())
                .replace("{amount}", String.format("%.2f", amount))
                .replace("{balance}", String.format("%.2f", newBalance))
                .replace("{currency}", currencySymbol);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // Handle the /economy take subcommand
    private void handleTake(CommandSender sender, String playerName, String amountStr) {
        Player target = Bukkit.getPlayer(playerName);
        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$");

        if (target == null || !target.isOnline()) {
            String message = plugin.getConfig().getString("messages.economy.player-not-found");
            message = message.replace("{target}", playerName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                String message = plugin.getConfig().getString("messages.economy.invalid-amount");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return;
            }
        } catch (NumberFormatException e) {
            String message = plugin.getConfig().getString("messages.economy.invalid-amount");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        balanceManager.withdrawBalance(target.getUniqueId(), amount);
        double newBalance = balanceManager.getBalance(target.getUniqueId());

        String message = plugin.getConfig().getString("messages.economy.take");
        message = message.replace("{player}", target.getName())
                .replace("{amount}", String.format("%.2f", amount))
                .replace("{balance}", String.format("%.2f", newBalance))
                .replace("{currency}", currencySymbol);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // Handle the /economy set subcommand
    private void handleSet(CommandSender sender, String playerName, String amountStr) {
        Player target = Bukkit.getPlayer(playerName);
        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$");

        if (target == null || !target.isOnline()) {
            String message = plugin.getConfig().getString("messages.economy.player-not-found");
            message = message.replace("{target}", playerName);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount < 0) {
                String message = plugin.getConfig().getString("messages.economy.invalid-amount");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return;
            }
        } catch (NumberFormatException e) {
            String message = plugin.getConfig().getString("messages.economy.invalid-amount");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return;
        }

        balanceManager.setBalance(target.getUniqueId(), amount);
        double newBalance = balanceManager.getBalance(target.getUniqueId());

        String message = plugin.getConfig().getString("messages.economy.set");
        message = message.replace("{player}", target.getName())
                .replace("{balance}", String.format("%.2f", newBalance))
                .replace("{currency}", currencySymbol);
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    // Tab completion for subcommands and player names
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("perceconomy.admin")) {
            return new ArrayList<>();
        }

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("give".startsWith(partial)) completions.add("give");
            if ("take".startsWith(partial)) completions.add("take");
            if ("set".startsWith(partial)) completions.add("set");
            if ("reload".startsWith(partial)) completions.add("reload");
        } else if (args.length == 2 && !args[0].equalsIgnoreCase("reload")) {
            String partialName = args[1].toLowerCase();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.getName().toLowerCase().startsWith(partialName)) {
                    completions.add(onlinePlayer.getName());
                }
            }
        } else if (args.length == 3 && !args[0].equalsIgnoreCase("reload")) {
            // Suggest common amounts
            completions.add("100");
            completions.add("500");
            completions.add("1000");
        }
        return completions;
    }
}
