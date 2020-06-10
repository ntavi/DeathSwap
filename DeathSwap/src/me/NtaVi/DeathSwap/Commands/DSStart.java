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

public class DSStart implements TabExecutor
{
    public DSStart(final Main plugin) {
        plugin.getCommand("dsstart").setExecutor((CommandExecutor)this);
        plugin.getCommand("dsstart").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.start")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (Game.State != 0) {
            sender.sendMessage(ChatColor.RED + "A game is still active!");
            return true;
        }
        final Boolean dev = args.length == 1 && args[0].equals("true");
        Game.Start(dev);
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        final List<String> list = new ArrayList<String>();
        return list;
    }
}
