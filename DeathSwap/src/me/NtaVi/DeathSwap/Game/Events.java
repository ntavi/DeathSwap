package me.NtaVi.DeathSwap.Game;

import org.bukkit.event.EventPriority;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.Bukkit;

import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Commands.DSReady;
import me.NtaVi.DeathSwap.Database.YmlFile;

import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.GameMode;
import org.bukkit.event.Listener;

public class Events implements Listener
{
    
    @EventHandler
    public void onPlayerDisconect(final PlayerQuitEvent event) {
        DSReady.Ready.remove(event.getPlayer().getUniqueId());
        if (Game.State != 0) {
            if (Game.InGame.contains(event.getPlayer().getUniqueId())) {
                final YmlFile ymlfile = new YmlFile();
                ymlfile.WriteData(event.getPlayer(), "Stats.Disconnected", 1 + ymlfile.ReadData(event.getPlayer().getUniqueId(), "Stats.Disconnected"));
            }
            Game.RemovePlayer(event.getPlayer());
            event.getPlayer().teleport(Main.world.getSpawnLocation());
            final ScoreboardManager manager = Bukkit.getScoreboardManager();
            event.getPlayer().setScoreboard(manager.getNewScoreboard());
            event.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        final Player p = event.getEntity();
        final Location l = p.getLocation();
        if (Game.State != 0 && Game.InGame.contains(p.getUniqueId())) {
            final YmlFile ymlfile = new YmlFile();
            ymlfile.WriteData(p, "Stats.Deaths", 1 + ymlfile.ReadData(p.getUniqueId(), "Stats.Deaths"));
        }
        Game.RemovePlayer(p);
        if (Game.State != 0) {
            Bukkit.broadcastMessage(event.getDeathMessage());
            event.setDeathMessage((String)null);
            p.sendMessage(ChatColor.AQUA + "You can use /dstp <player> to teleport to a other player!");
            p.spigot().respawn();
            p.teleport(l);
        }
    }
}
