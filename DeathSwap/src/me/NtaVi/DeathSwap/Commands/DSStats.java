package me.NtaVi.DeathSwap.Commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;

import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Database.YmlFile;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

public class DSStats implements TabExecutor
{
    public DSStats(final Main plugin) {
        plugin.getCommand("dsstats").setExecutor((CommandExecutor)this);
        plugin.getCommand("dsstats").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.stats")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only player may execute this command");
            return true;
        }
        final YmlFile yml = new YmlFile();
        final Player p = (Player)sender;
        if (args.length == 0) {
            p.sendMessage(ChatColor.DARK_GREEN + "Stats of " + ChatColor.GOLD + p.getName());
            p.sendMessage(ChatColor.GREEN + "Games: " + ChatColor.DARK_GREEN + yml.ReadData(p.getUniqueId(), "Stats.Games"));
            p.sendMessage(ChatColor.GREEN + "Wins: " + ChatColor.GOLD + yml.ReadData(p.getUniqueId(), "Stats.Wins"));
            p.sendMessage(ChatColor.GREEN + "Swaps: " + ChatColor.DARK_GREEN + yml.ReadData(p.getUniqueId(), "Stats.Swaps"));
            p.sendMessage(ChatColor.GREEN + "Deaths: " + ChatColor.RED + yml.ReadData(p.getUniqueId(), "Stats.Deaths"));
            p.sendMessage(ChatColor.GREEN + "Disconnected: " + ChatColor.RED + yml.ReadData(p.getUniqueId(), "Stats.Disconnected"));
            int Time = yml.ReadData(p.getUniqueId(), "Stats.Time");
            int Minutes = 0;
            while (Time >= 1200) {
                ++Minutes;
                Time -= 1200;
            }
            if (Time < 200) {
                p.sendMessage(ChatColor.GREEN + "Time played: " + ChatColor.DARK_GREEN + Minutes + ":0" + Time / 20.0f);
            }
            else {
                p.sendMessage(ChatColor.GREEN + "Time played: " + ChatColor.DARK_GREEN + Minutes + ":" + Time / 20.0f);
            }
        }
        else {
            @SuppressWarnings("deprecation")
			final OfflinePlayer tt = Bukkit.getOfflinePlayer(args[0]);
            if (!tt.hasPlayedBefore()) {
                sender.sendMessage(ChatColor.RED + tt.getName() + " never played on the server!");
                return true;
            }
            p.sendMessage(ChatColor.DARK_GREEN + "Stats of " + ChatColor.GOLD + tt.getName());
            p.sendMessage(ChatColor.GREEN + "Games: " + ChatColor.DARK_GREEN + yml.ReadData(tt.getUniqueId(), "Stats.Games"));
            p.sendMessage(ChatColor.GREEN + "Wins: " + ChatColor.GOLD + yml.ReadData(tt.getUniqueId(), "Stats.Wins"));
            p.sendMessage(ChatColor.GREEN + "Swaps: " + ChatColor.DARK_GREEN + yml.ReadData(tt.getUniqueId(), "Stats.Swaps"));
            p.sendMessage(ChatColor.GREEN + "Deaths: " + ChatColor.RED + yml.ReadData(tt.getUniqueId(), "Stats.Deaths"));
            p.sendMessage(ChatColor.GREEN + "Disconnected: " + ChatColor.RED + yml.ReadData(tt.getUniqueId(), "Stats.Disconnected"));
            int Time2 = yml.ReadData(p.getUniqueId(), "Stats.Time");
            int Minutes2 = 0;
            while (Time2 >= 1200) {
                ++Minutes2;
                Time2 -= 1200;
            }
            if (Time2 < 200) {
                p.sendMessage(ChatColor.GREEN + "Time played: " + ChatColor.DARK_GREEN + Minutes2 + ":0" + Time2 / 20.0f);
            }
            else {
                p.sendMessage(ChatColor.GREEN + "Time played: " + ChatColor.DARK_GREEN + Minutes2 + ":" + Time2 / 20.0f);
            }
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        final List<String> list = new ArrayList<String>();
        if (arg3.length == 1) {
            for (final Player p : Bukkit.getOnlinePlayers()) {
                list.add(p.getName());
            }
        }
        return list;
    }
}
