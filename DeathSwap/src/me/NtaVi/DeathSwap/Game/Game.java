package me.NtaVi.DeathSwap.Game;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatMessageType;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.command.CommandSender;
import java.util.Random;
import java.util.Collections;
import org.bukkit.GameRule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Entity;
import org.bukkit.World;

import me.NtaVi.DeathSwap.Main;
import me.NtaVi.DeathSwap.Database.YmlFile;

import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.DisplaySlot;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import org.bukkit.Location;
import java.util.UUID;
import java.util.List;

public class Game
{
    public static List<UUID> InGame;
    public static List<UUID> RandomTP;
    public static List<Location> RandomLoc;
    public static World world = Bukkit.getServer().getWorld("deathswap");
    public static int State;
    static int Try;
    static int Trys;
    static Boolean TrySucces;
    static Boolean dev;
    static int TotalTimer;
    static int MainTimer;
    static int Timer;
    static int Seconds;
    static int Minutes;
    static int UntilSwap;
    static int TotalSwap;
    
    static {
        Game.InGame = new ArrayList<UUID>();
        Game.RandomTP = new ArrayList<UUID>();
        Game.RandomLoc = new ArrayList<Location>();
        Game.State = 0;
        Game.Try = 0;
        Game.Trys = 0;
        Game.TotalTimer = 0;
        Game.MainTimer = 0;
        Game.Timer = 0;
        Game.Seconds = 0;
        Game.Minutes = 0;
        Game.UntilSwap = 0;
        Game.TotalSwap = 0;
    }
    
    public static void Start(final Boolean devv) {
        Game.dev = devv;
        if (Game.State != 0) {
            return;
        }
        if (Bukkit.getOnlinePlayers().toArray().length < 2) {
            Bukkit.broadcastMessage(ChatColor.RED + "You need at least 2 players to start the game!");
            return;
        }
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
        	if (p.getWorld().getName().equals("deathswap")){
        		p.sendMessage("DeathSwap is about to start!");
        	}
        }
        Main.world.setTime(0L);
        Game.InGame.clear();
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard board = manager.getNewScoreboard();
        final Objective objective = board.registerNewObjective("showhealth", "health", "Health");
        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        objective.setRenderType(RenderType.HEARTS);
        for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
        	if (p.getWorld().getName().equals("deathswap")){
	            Game.InGame.add(p.getUniqueId());
	            p.setGameMode(GameMode.SURVIVAL);
	            p.getInventory().clear();
	            for (final PotionEffect effect : p.getActivePotionEffects()) {
	                p.removePotionEffect(effect.getType());
	            }
	            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GetSetting("Safe"), 255));
	            p.teleport(Main.world.getSpawnLocation());
	            p.setScoreboard(board);
	            p.setHealth(19.0);
	            p.setSaturation(20.0f);
	            p.setFoodLevel(20);
	            p.setFallDistance(1.0f);
	            p.setExp(0.0f);
	            p.setLevel(0);
	            final YmlFile ymlfile = new YmlFile();
	            ymlfile.WriteData(p, "Stats.Games", 1 + ymlfile.ReadData(p.getUniqueId(), "Stats.Games"));
            }
        }
        for (final World w : Bukkit.getWorlds()) {
            for (final Entity e : w.getEntities()) {
                if (e.getType() == EntityType.ENDER_PEARL) {
                    e.remove();
                }
            }
            w.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.valueOf(false));
            w.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, Boolean.valueOf(true));
        }
        if (Main.GetSetting("TeleportMode") != 3) {
            Collections.shuffle(Game.InGame);
        }
        final Random rand = new Random();
        Game.UntilSwap = rand.nextInt(Main.GetSetting("MaxTimer") + 1 - Main.GetSetting("MinTimer")) + Main.GetSetting("MinTimer");
        Game.TotalTimer = 0;
        Game.Timer = 0;
        Game.Seconds = 0;
        Game.Minutes = 0;
        Game.MainTimer = 0;
        Game.Trys = 0;
        Game.TotalSwap = 0;
        final ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.getServer().dispatchCommand((CommandSender)console, String.format("spreadplayers %d %d %d %d %b %s", 0, 0, 100, 2000, false, "@a[distance=0..2000]"));
        for (final World w2 : Bukkit.getWorlds()) {
        	w2.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, Boolean.valueOf(true));
        }
        String devm = "";
        Boolean first = true;
        for (final UUID uuid : Game.InGame) {
            if (!first) {
                devm = String.valueOf(devm) + ", ";
            }
            devm = String.valueOf(devm) + Bukkit.getPlayer(uuid).getName();
            first = false;
        }
        for (final Player p2 : Bukkit.getOnlinePlayers()) {
        	if (p2.getWorld().getName().equals("deathswap")){
        		if (p2.isOp() && Game.dev) {
        			p2.sendMessage(ChatColor.DARK_GREEN + "The next circle of teleportation is: " + devm);
        		}
        	}
        }
        Game.State = 1;
    }
    
    public static void Stop() {
        if (Game.State < 1) {
            return;
        }
        if (Game.InGame.size() == 1) {
            final Player tt = Bukkit.getPlayer((UUID)Game.InGame.get(0));
            Bukkit.broadcastMessage(ChatColor.GOLD + tt.getName() + ChatColor.RESET + ChatColor.GREEN + " has won the game!");
            for (final Player t : Bukkit.getServer().getOnlinePlayers()) {
            	if (t.getWorld().getName().equals("deathswap")){
	                t.teleport(Main.world.getSpawnLocation());
	                if (!t.getUniqueId().equals(tt.getUniqueId())) {
	                    t.sendTitle(ChatColor.GOLD + tt.getName() + ChatColor.RESET + ChatColor.GREEN + " has won!", ChatColor.DARK_RED + "You lost...", 10, 80, 10);
	                    t.playSound(t.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
	                }
	                else {
	                    t.sendTitle(ChatColor.GOLD + "You" + ChatColor.RESET + ChatColor.GREEN + " win!", ChatColor.DARK_GREEN + "Good Job!", 10, 80, 10);
	                    t.playSound(t.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
	                    final YmlFile ymlfile = new YmlFile();
	                    ymlfile.WriteData(t, "Stats.Wins", 1 + ymlfile.ReadData(t.getUniqueId(), "Stats.Wins"));
	                    ymlfile.WriteData(t, "Stats.Time", ymlfile.ReadData(t.getUniqueId(), "Stats.Time") + Game.TotalTimer);
	                }
            	}
            }
        }
        else {
            for (final Player p : Bukkit.getServer().getOnlinePlayers()) {
            	if (p.getWorld().getName().equals("deathswap")){
	                p.teleport(Main.world.getSpawnLocation());
	                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            	}
            }
            Bukkit.broadcastMessage("DeathSwap has stopped!");
        }
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        for (final Player p2 : Bukkit.getServer().getOnlinePlayers()) {
        	if (p2.getWorld().getName().equals("deathswap")){
	            p2.setGameMode(GameMode.SURVIVAL);
	            p2.setFallDistance(0.0f);
	            p2.setHealth(19.0);
	            p2.setSaturation(20.0f);
	            p2.setFoodLevel(20);
	            p2.setScoreboard(manager.getNewScoreboard());
        	}
        }
        for (final World w : Bukkit.getWorlds()) {
            for (final Entity e : w.getEntities()) {
                if (e.getType() == EntityType.ENDER_PEARL) {
                    e.remove();
                }
            }
        }
        Game.InGame.clear();
        Game.State = 0;
    }
    
    public static void Teleport0() {
        Game.RandomTP.clear();
        Game.RandomTP.addAll(Game.InGame);
        Game.Try = 0;
        Game.TrySucces = true;
        Teleport00();
        if (!Game.TrySucces) {
            Collections.shuffle(Game.InGame);
            Teleport1();
            return;
        }
        for (final UUID uuid : Game.InGame) {
            final Player p = Bukkit.getPlayer(uuid);
            final Player t = Bukkit.getPlayer((UUID)Game.RandomTP.get(Game.InGame.indexOf(uuid)));
            p.teleport((Location)Game.RandomLoc.get(Game.InGame.indexOf(uuid)));
            p.sendMessage(ChatColor.GREEN + "You've been teleported to " + ChatColor.GOLD + t.getName());
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GetSetting("Safe"), 255));
        }
    }
    
    public static void Teleport00() {
        ++Game.Try;
        if (Game.Try == 500) {
            ++Game.Trys;
            for (final Player p : Bukkit.getOnlinePlayers()) {
            	if (p.getWorld().getName().equals("deathswap")){
	                if (p.isOp() && Game.dev) {
	                    p.sendMessage(ChatColor.DARK_RED + "Couldn't find random assortment in 500 try's! Used random_circle method instead!");
	                    if (Game.Trys != 10) {
	                        continue;
	                    }
	                    p.sendMessage(ChatColor.DARK_RED + "This has happend 10 times now, you can change the teleport mode to random circle to get rid of these messages!");
	                }
            	}
            }
            Game.TrySucces = false;
            return;
        }
        Game.RandomLoc.clear();
        Collections.shuffle(Game.RandomTP);
        for (final UUID uuid : Game.InGame) {
            if (uuid.equals(Game.RandomTP.get(Game.InGame.indexOf(uuid)))) {
                Teleport00();
            }
            final Player t = Bukkit.getPlayer((UUID)Game.RandomTP.get(Game.InGame.indexOf(uuid)));
            Game.RandomLoc.add(t.getLocation());
        }
    }
    
    public static void Teleport1() {
        Location location = null;
        String name = "";
        for (final UUID uuid : Game.InGame) {
            final Player p = Bukkit.getPlayer(uuid);
            if (Game.InGame.indexOf(uuid) == 0) {
                location = p.getLocation();
                name = p.getName();
            }
            if (Game.InGame.indexOf(uuid) + 1 == Game.InGame.size()) {
                p.teleport(location);
                p.sendMessage(ChatColor.GREEN + "You've been teleported to " + ChatColor.GOLD + name);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GetSetting("Safe"), 255));
            }
            else {
                final Player t = Bukkit.getPlayer((UUID)Game.InGame.get(Game.InGame.indexOf(uuid) + 1));
                p.teleport(t.getLocation());
                p.sendMessage(ChatColor.GREEN + "You've been teleported to " + ChatColor.GOLD + t.getName());
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Main.GetSetting("Safe"), 255));
            }
        }
    }
    
    public static void RemovePlayer(final Player p) {
        if (Game.State != 1 || !Game.InGame.contains(p.getUniqueId())) {
            return;
        }
        p.setGameMode(GameMode.SPECTATOR);
        Game.InGame.remove(p.getUniqueId());
        Bukkit.broadcastMessage(ChatColor.DARK_RED + p.getName() + ChatColor.RED + " is out! " + ChatColor.DARK_GREEN + Game.InGame.toArray().length + ChatColor.GREEN + " Remaining!");
        p.getWorld().strikeLightningEffect(p.getLocation());
        final YmlFile ymlfile = new YmlFile();
        ymlfile.WriteData(p, "Stats.Time", ymlfile.ReadData(p.getUniqueId(), "Stats.Time") + Game.TotalTimer);
        if (Game.InGame.size() <= 1) {
            for (final Player t : Bukkit.getServer().getOnlinePlayers()) {
            	if (t.getWorld().getName().equals("deathswap")){
            		t.setGameMode(GameMode.SPECTATOR);
            	}
            }
            Game.State = 2;
        }
    }
    
    public static void Timer() {
        if (Game.State != 1) {
            return;
        }
        --Game.UntilSwap;
        ++Game.MainTimer;
        ++Game.TotalTimer;
        ++Game.Timer;
        if (Game.Timer == 20) {
            ++Game.Seconds;
            Game.Timer = 0;
        }
        if (Game.Seconds == 60) {
            ++Game.Minutes;
            Game.Seconds = 0;
        }
        String TimerMessage = "";
        if (Game.Seconds <= 9) {
            TimerMessage = String.valueOf(TimerMessage) + Game.Minutes + ":0" + Game.Seconds;
        }
        else {
            TimerMessage = String.valueOf(TimerMessage) + Game.Minutes + ":" + Game.Seconds;
        }
        TimerMessage = String.valueOf(TimerMessage) + ChatColor.GRAY + " Swaps [" + ChatColor.DARK_GREEN + Game.TotalSwap + ChatColor.GRAY + "]";
        if (Main.GetSetting("Warning") >= Game.UntilSwap) {
            final BigDecimal US = new BigDecimal(Game.UntilSwap / 20.0f);
            TimerMessage = String.valueOf(TimerMessage) + ChatColor.DARK_RED + " Swapping in: " + US.setScale(1, RoundingMode.HALF_UP) + " Seconds!";
        }
        for (final Player p : Bukkit.getOnlinePlayers()) {
        	if (p.getWorld().getName().equals("deathswap")){
	            if (Game.MainTimer < Main.GetSetting("MinTimer")) {
	                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN + "[safe] Time since swap: " + TimerMessage));
	            }
	            else {
	                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + "[unsafe] Time since swap: " + TimerMessage));
	            }
        	}
        }
        if (Game.UntilSwap == 0) {
            Bukkit.broadcastMessage(ChatColor.BOLD + "SWAP!");
            Game.Timer = 0;
            Game.Seconds = 0;
            Game.Minutes = 0;
            Game.MainTimer = 0;
            ++Game.TotalSwap;
            int EPearl = 0;
            for (final World w : Bukkit.getWorlds()) {
                for (final Entity e : w.getEntities()) {
                    if (e.getType() == EntityType.ENDER_PEARL) {
                        e.remove();
                        ++EPearl;
                    }
                }
            }
            if (EPearl > 0) {
                for (final Player p2 : Bukkit.getOnlinePlayers()) {
                	if (p2.getWorld().getName().equals("deathswap")){
	                    if (p2.isOp() && Game.dev) {
	                        p2.sendMessage("Removed " + EPearl + " enderpearl(s)!");
	                    }
                	}
                }
            }
            for (final UUID uuid : Game.InGame) {
                final Player p3 = Bukkit.getPlayer(uuid);
                final YmlFile ymlfile = new YmlFile();
                ymlfile.WriteData(p3, "Stats.Swaps", 1 + ymlfile.ReadData(p3.getUniqueId(), "Stats.Swaps"));
            }
        }
        if (Game.UntilSwap == 0 && Main.GetSetting("TeleportMode") == 0) {
            Teleport0();
            final Random rand = new Random();
            Game.UntilSwap = rand.nextInt(Main.GetSetting("MaxTimer") + 1 - Main.GetSetting("MinTimer")) + Main.GetSetting("MinTimer");
        }
        else if (Game.UntilSwap == 0 && Main.GetSetting("TeleportMode") != 0) {
            Teleport1();
            final Random rand = new Random();
            Game.UntilSwap = rand.nextInt(Main.GetSetting("MaxTimer") + 1 - Main.GetSetting("MinTimer")) + Main.GetSetting("MinTimer");
            if (Main.GetSetting("TeleportMode") == 2) {
                Collections.shuffle(Game.InGame);
            }
            Boolean first = true;
            String devm = "";
            for (final UUID uuid2 : Game.InGame) {
                if (!first) {
                    devm = String.valueOf(devm) + ", ";
                }
                devm = String.valueOf(devm) + Bukkit.getPlayer(uuid2).getName();
                first = false;
            }
            for (final Player p3 : Bukkit.getOnlinePlayers()) {
            	if (p3.getWorld().getName().equals("deathswap")) {
	                if (p3.isOp() && Game.dev) {
	                    p3.sendMessage(ChatColor.DARK_GREEN + "The next circle of teleportation is: " + devm);
	                }
            	}
            }
        }
    }
}
