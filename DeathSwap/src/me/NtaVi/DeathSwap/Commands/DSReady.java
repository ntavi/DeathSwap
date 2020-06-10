package me.NtaVi.DeathSwap.Commands;

import org.bukkit.Bukkit;
import org.bukkit.World;

import net.md_5.bungee.api.ChatColor;
import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Game.Game;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import org.bukkit.command.TabExecutor;

public class DSReady implements TabExecutor
{
    public static World world = Bukkit.getServer().getWorld("deathswap");;
    public static List<UUID> Ready;
    static {
        DSReady.Ready = new ArrayList<UUID>();
    }
    
    public DSReady(final Main plugin) {
        plugin.getCommand("dsready").setExecutor((CommandExecutor)this);
        plugin.getCommand("dsready").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.ready")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command!");
            return true;
        }
        final Player p = (Player)sender;
        if (Game.State != 0) {
            p.sendMessage(ChatColor.RED + "A game is still active!");
            return true;
        }
        if (args.length == 0) {
            if (DSReady.Ready.contains(p.getUniqueId())) {
                DSReady.Ready.remove(p.getUniqueId());
                p.sendMessage(ChatColor.DARK_RED + p.getName() + ChatColor.RED + " is no longer ready! " + ChatColor.WHITE + "[" + ChatColor.GREEN + DSReady.Ready.toArray().length + "/" + world.getPlayers().size() + ChatColor.WHITE + "]");
            }
            else {
                DSReady.Ready.add(p.getUniqueId());
                p.sendMessage(ChatColor.DARK_GREEN + p.getName() + ChatColor.GREEN + " is ready! " + ChatColor.WHITE + "[" + ChatColor.GREEN + DSReady.Ready.toArray().length + "/" + world.getPlayers().size() + ChatColor.WHITE + "]");
                if (DSReady.Ready.toArray().length == Bukkit.getOnlinePlayers().toArray().length) {
                    p.sendMessage(ChatColor.GREEN + "You can start the game with /dsready start");
                }
            }
        }
        else if (args[0].equals("get")) {
            if (DSReady.Ready.contains(p.getUniqueId())) {
                p.sendMessage(ChatColor.GREEN + "You are ready " + ChatColor.WHITE + "[" + ChatColor.GREEN + DSReady.Ready.toArray().length + "/" + world.getPlayers().size() + ChatColor.WHITE + "]");
            }
            else {
                p.sendMessage(ChatColor.RED + "You are not ready " + ChatColor.WHITE + "[" + ChatColor.GREEN + DSReady.Ready.toArray().length + "/" + world.getPlayers().size() + ChatColor.WHITE + "]");
            }
        }
        else if (args[0].equals("start")) {
            if (DSReady.Ready.toArray().length == world.getPlayers().size()) {
                DSReady.Ready.clear();
                p.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has started the game!");
                Game.Start(false);
            }
            else {
                p.sendMessage(ChatColor.RED + "Not everyone is ready yet! " + ChatColor.WHITE + "[" + ChatColor.GREEN + DSReady.Ready.toArray().length + "/" + world.getPlayers().size() + ChatColor.WHITE + "]");
            }
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender arg0, final Command arg1, final String arg2, final String[] arg3) {
        final List<String> list = new ArrayList<String>();
        if (arg3.length == 1) {
            list.add("get");
            list.add("start");
        }
        return list;
    }
}
