package me.NtaVi.DeathSwap.Commands;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

import me.NtaVi.DeathSwap.Main;

public class DSInfo implements TabExecutor
{
    public DSInfo(final Main plugin) {
        plugin.getCommand("dsinfo").setExecutor((CommandExecutor)this);
        plugin.getCommand("dsinfo").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.info")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "Death Swap Info");
        sender.sendMessage(ChatColor.DARK_GREEN + "Creator of plugin: " + ChatColor.GREEN + "NtaVi");
        sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + Main.Version);
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        final List<String> list = new ArrayList<String>();
        return list;
    }
}
