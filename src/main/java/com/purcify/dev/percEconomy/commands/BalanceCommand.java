package com.purcify.dev.percEconomy.commands;

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            // Show own balance
            double balance = balanceManager.getBalance(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Your balance is: " + ChatColor.GOLD + balance);
        } else if (args.length == 1 && player.hasPermission("perceconomy.balance.others")) {
            // Show another player's balance
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                double balance = balanceManager.getBalance(target.getUniqueId());
                player.sendMessage(ChatColor.GREEN + target.getName() + "'s balance is: " + ChatColor.GOLD + balance);
            } else {
                player.sendMessage(ChatColor.RED + "Player not found.");
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /balance [player]");
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
