package me.NtaVi.DeathSwap;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;

import me.NtaVi.DeathSwap.Commands.DSInfo;
import me.NtaVi.DeathSwap.Commands.DSReady;
import me.NtaVi.DeathSwap.Commands.DSSettings;
import me.NtaVi.DeathSwap.Commands.DSStart;
import me.NtaVi.DeathSwap.Commands.DSStats;
import me.NtaVi.DeathSwap.Commands.DSStop;
import me.NtaVi.DeathSwap.Commands.DSTP;
import me.NtaVi.DeathSwap.Game.Events;
import me.NtaVi.DeathSwap.Game.Game;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
    static FileConfiguration config;
    public static World world = Bukkit.getServer().getWorld("deathswap");
    public static String Version;
    
    public void onEnable() {
        new DSSettings(this);
        new DSStart(this);
        new DSStop(this);
        new DSInfo(this);
        new DSReady(this);
        new DSTP(this);
        new DSStats(this);
        Main.config = this.getConfig();
        final PluginDescriptionFile pdf = this.getDescription();
        Main.Version = pdf.getVersion();
        this.getServer().getPluginManager().registerEvents((Listener)new Events(), (Plugin)this);
        System.out.println("[DeathSwap] Enabled plugin!");
        this.Timer();
    }
    
    public void onDisable() {
        Main.config.options().copyDefaults(true);
        this.saveConfig();
    }
    
    public static String SetSettings(final String Setting, final String Value) {
        if (!Setting.equals("MinTimer") && !Setting.equals("MaxTimer") && !Setting.equals("Warning")) {
            if (!Setting.equals("Safe")) {
                if (!Setting.equals("TeleportMode")) {
                    return ChatColor.RED + "Useage: /dssettings <set> <MinTimer,MaxTimer,Warning,Safe,TeleportMode,WorldName> <value> (Time needs to be in ticks!)";
                }
                if (Value.equals("random") || Value.equals("circle") || Value.equals("random_circle") || Value.equals("fixed_circle")) {
                    Main.config.set(Setting, (Object)Value);
                    return ChatColor.GREEN + "Setting '" + Setting + "' changed to " + Value;
                }
                return ChatColor.RED + "Useage: /dssettings set TeleportMode <random, circle, random_circle, fixed_circle>";
            }
        }
        int Number;
        try {
            Number = Integer.parseInt(Value);
        }
        catch (NumberFormatException e) {
            return ChatColor.RED + "The value is not a number!";
        }
        if (Setting.equals("MinTimer") && (Number > (int)Main.config.get("MaxTimer") || Number < 1)) {
            return ChatColor.RED + "MinTimer can't be more than MaxTimer or less than 1";
        }
        if (Setting.equals("MaxTimer") && (Number < (int)Main.config.get("MinTimer") || Number < 1)) {
            return ChatColor.RED + "MaxTimer can't be less than MinTimer or less than 1";
        }
        if (Number < 0) {
            return ChatColor.RED + Setting + " can't be less than 0!";
        }
        final float Seconds = Number / 20.0f;
        Main.config.set(Setting, (Object)Number);
        return ChatColor.GREEN + "Setting '" + Setting + "' changed to " + Seconds + " seconds";
    }

    public static String GetSettings() {
        final String Config = "MinTimer: " + Main.config.get("MinTimer") + ", MaxTimer: " + Main.config.get("MaxTimer") + ", Warning: " + Main.config.get("Warning") + ", Safe: " + Main.config.get("Safe") + ", TeleportMode: " + Main.config.get("TeleportMode") + Main.config.get("WorldName");
        return Config;
    }
    
    public static String GetSettings2() {
        final String Config = "The swap will occur between " + ChatColor.GREEN + (int)Main.config.get("MinTimer") / 20.0f + ChatColor.WHITE + " and " + ChatColor.GREEN + (int)Main.config.get("MaxTimer") / 20.0f + ChatColor.WHITE + " seconds\nYou'll get a warning " + ChatColor.GREEN + (int)Main.config.get("Warning") / 20.0f + ChatColor.WHITE + " seconds before a swap\nYou'll be " + ChatColor.GREEN + (int)Main.config.get("Safe") / 20.0f + ChatColor.WHITE + " seconds invincible after a swap\nTeleportation is based on a " + ChatColor.GREEN + Main.config.get("TeleportMode") + ChatColor.WHITE + " method";
        return Config;
    }
    
    public static int GetSetting(final String Setting) {
        if (Setting.equals("TeleportMode")) {
            if ("random".equals(Main.config.get(Setting))) {
                return 0;
            }
            if ("circle".equals(Main.config.get(Setting))) {
                return 1;
            }
            if ("random_circle".equals(Main.config.get(Setting))) {
                return 2;
            }
            if ("fixed_circle".equals(Main.config.get(Setting))) {
                return 3;
            }
        }
        return (int)Main.config.get(Setting);
    }
    
    public void Timer() {
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                Game.Timer();
                if (Game.State == 2) {
                    Main.this.EndTimer();
                    Game.State = 3;
                }
                Main.this.Timer();
            }
        }, 1L);
    }
    
    public void EndTimer() {
        final BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask((Plugin)this, (Runnable)new Runnable() {
            @Override
            public void run() {
                Game.Stop();
            }
        }, 19L);
    }
}
