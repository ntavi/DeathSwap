package me.NtaVi.DeathSwap.Commands;

import java.util.ArrayList;
import java.util.List;

import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Game.Game;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

public class DSStop implements TabExecutor
{
    public DSStop(final Main plugin) {
        plugin.getCommand("dsstop").setExecutor((CommandExecutor)this);
        plugin.getCommand("dsstop").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.stop")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (Game.State != 1) {
            sender.sendMessage(ChatColor.RED + "There is no game active!");
            return true;
        }
        Game.Stop();
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        final List<String> list = new ArrayList<String>();
        return list;
    }
}
