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

public class DSSettings implements TabExecutor
{
    public DSSettings(final Main plugin) {
        plugin.getCommand("dssettings").setExecutor((CommandExecutor)this);
        plugin.getCommand("dssettings").setTabCompleter((TabCompleter)this);
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.hasPermission("ds.settings")) {
            sender.sendMessage(ChatColor.RED + "You do not have permision to use this command!");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /dssettings <set,get,help>");
            return true;
        }
        if (args.length == 1 && args[0].equals("help")) {
            sender.sendMessage(ChatColor.GOLD + "'dssettings' help:");
            sender.sendMessage(ChatColor.DARK_GREEN + "... help : " + ChatColor.GREEN + "shows this page");
            sender.sendMessage(ChatColor.DARK_GREEN + "... get : " + ChatColor.GREEN + "sends all the settings with values");
            sender.sendMessage(ChatColor.DARK_GREEN + "... set : " + ChatColor.GREEN + "change a setting");
            sender.sendMessage(ChatColor.DARK_GREEN + "(use '/dssettings help set' for more info)");
        }
        else if (args.length == 2 && args[0].equals("help")) {
            sender.sendMessage(ChatColor.GOLD + "'dssettings set' help:");
            sender.sendMessage(ChatColor.DARK_GREEN + "... MinTimer : " + ChatColor.GREEN + "Set the minimum amount of time before a swap");
            sender.sendMessage(ChatColor.DARK_GREEN + "... MaxTimer : " + ChatColor.GREEN + "Set the maximum amount of time before a swap");
            sender.sendMessage(ChatColor.DARK_GREEN + "... Warning : " + ChatColor.GREEN + "Set the time you'll get a warning before a swap");
            sender.sendMessage(ChatColor.DARK_GREEN + "... Safe : " + ChatColor.GREEN + "Set the time of invincibility after a swap");
            sender.sendMessage(ChatColor.DARK_GREEN + "... TeleportMode : " + ChatColor.GREEN + "Set the method that will be used when telporting");
            sender.sendMessage(ChatColor.RED + "Time is measured in ticks! (20 Ticks is 1 second)");
            sender.sendMessage(ChatColor.DARK_GREEN + "(use '/dssettings help set TeleportMode' for more info)");
        }
        else if (args.length >= 3 && args[0].equals("help")) {
            sender.sendMessage(ChatColor.GOLD + "'dssettings set TeleportMode' help:");
            sender.sendMessage(ChatColor.DARK_GREEN + "... random : " + ChatColor.GREEN + "Players will be teleported to a random player (If it can't find a assortment in 500 trys it wil use the random circle method!)");
            sender.sendMessage(ChatColor.DARK_GREEN + "... circle : " + ChatColor.GREEN + "Players will move in a random circle thats generated at the start of the game");
            sender.sendMessage(ChatColor.DARK_GREEN + "... random_circle : " + ChatColor.GREEN + "Circle method only the circle will change when a swap occurs");
            sender.sendMessage(ChatColor.DARK_GREEN + "... random_circle : " + ChatColor.GREEN + "Circle method only the circle won't change between games");
        }
        else if (args.length == 1 || args.length == 2) {
            if (args[0].equals("get")) {
                sender.sendMessage(Main.GetSettings());
            }
            else if (args[0].equals("set")) {
                sender.sendMessage(ChatColor.RED + "Useage: /dssettings <set> <MinTimer,MaxTimer,Warning,Safe,TeleportMode> <value> (Time needs to be in ticks!)");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Usage: /dssettings <set,get,help>");
            }
        }
        else if (args.length >= 3) {
            if (args[0].equals("get")) {
                sender.sendMessage(Main.GetSettings());
            }
            else {
                if (args[0].equals("set")) {
                    sender.sendMessage(Main.SetSettings(args[1], args[2]));
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "Usage: /dssettings <set,get,help>");
            }
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final List<String> list = new ArrayList<String>();
        if (args.length == 1) {
            list.add("get");
            list.add("set");
            list.add("help");
        }
        else if (args.length == 2 && args[0].equals("set")) {
            list.add("MinTimer");
            list.add("MaxTimer");
            list.add("Warning");
            list.add("Safe");
            list.add("TeleportMode");
        }
        else if (args.length == 3 && args[0].equals("set") && args[1].equals("MinTimer")) {
            list.add("2400");
        }
        else if (args.length == 3 && args[0].equals("set") && args[1].equals("MaxTimer")) {
            list.add("6000");
        }
        else if (args.length == 3 && args[0].equals("set") && args[1].equals("Warning")) {
            list.add("200");
        }
        else if (args.length == 3 && args[0].equals("set") && args[1].equals("Safe")) {
            list.add("60");
        }
        else if (args.length == 3 && args[0].equals("set") && args[1].equals("TeleportMode")) {
            list.add("circle");
            list.add("random");
            list.add("random_circle");
            list.add("fixed_circle");
        }
        else if (args.length == 2 && args[0].equals("help")) {
            list.add("set");
        }
        else if (args.length == 3 && args[0].equals("help")) {
            list.add("TeleportMode");
        }
        return list;
    }
}
