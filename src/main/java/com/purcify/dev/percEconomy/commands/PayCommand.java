package com.purcify.dev.percEconomy.commands;

import com.purcify.dev.percEconomy.PercEconomy;
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
    private PercEconomy plugin = PercEconomy.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String currencySymbol = plugin.getConfig().getString("currency-symbol", "$"); // Get the currency symbol

        if (!(sender instanceof Player)) { // Check if the sender is a player
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        // Check if correct amount of arguments were given
        if (args.length != 2) {
            String usage = plugin.getConfig().getString("messages.pay.usage");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', usage));
            return true;
        }

        // Check if provided player is valid/online
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            String message = plugin.getConfig().getString("messages.pay.player-not-found");
            message = message.replace("{target}", args[0]);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        // Check if player is trying to pay themselves
        if (target.equals(player)) {
            String message = plugin.getConfig().getString("messages.pay.pay-self");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        // Check if amount is valid
        double amount;
        try {
            amount = Double.parseDouble(args[1]);
            if (amount <= 0) {
                String message = plugin.getConfig().getString("messages.pay.negative-amount");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                return true;
            }
        } catch (NumberFormatException e) {
            String message = plugin.getConfig().getString("messages.pay.invalid-amount");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        // Check if player has enough balance and transfer the amount
        if (balanceManager.withdrawBalance(player.getUniqueId(), amount)) {
            balanceManager.addBalance(target.getUniqueId(), amount);

            double senderBalance = balanceManager.getBalance(player.getUniqueId());
            double receiverBalance = balanceManager.getBalance(target.getUniqueId());

            String senderMessage = plugin.getConfig().getString("messages.pay.success");
            senderMessage = senderMessage.replace("{amount}", String.format("%.2f", amount))
                    .replace("{receiver}", target.getName())
                    .replace("{balance}", String.format("%.2f", senderBalance))
                    .replace("{currency}", currencySymbol);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', senderMessage));

            String receiverMessage = plugin.getConfig().getString("messages.pay.received");
            receiverMessage = receiverMessage.replace("{amount}", String.format("%.2f", amount))
                    .replace("{sender}", player.getName())
                    .replace("{balance}", String.format("%.2f", receiverBalance))
                    .replace("{currency}", currencySymbol);
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', receiverMessage));
        } else {
            String message = plugin.getConfig().getString("messages.pay.insufficient-funds");
            message = message.replace("{balance}", String.format("%.2f", balanceManager.getBalance(player.getUniqueId())))
                    .replace("{currency}", currencySymbol);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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
