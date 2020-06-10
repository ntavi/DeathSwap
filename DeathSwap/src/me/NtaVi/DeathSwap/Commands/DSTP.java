package me.NtaVi.DeathSwap.Commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Game.Game;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

public class DSTP implements TabExecutor
{
    public DSTP(final Main plugin) {
        plugin.getCommand("dstp").setExecutor((CommandExecutor)this);
        plugin.getCommand("dstp").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.tp")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only player may execute this command");
            return true;
        }
        final Player p = (Player)sender;
        if (Game.State == 0 || Game.InGame.contains(p.getUniqueId())) {
            p.sendMessage(ChatColor.RED + "Your not a spectator!");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(ChatColor.RED + "Usage: /dstp <player>");
            return true;
        }
        final Player t = Bukkit.getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(ChatColor.RED + args[0] + " is not online!");
        }
        else {
            p.teleport(t.getLocation());
            p.sendMessage(ChatColor.GREEN + "Teleported to: " + args[0]);
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
