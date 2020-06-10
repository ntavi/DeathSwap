package me.NtaVi.DeathSwap.Database;

import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import org.bukkit.entity.Player;

import me.NtaVi.DeathSwap.Main;

public class YmlFile
{
    Main plugin;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public YmlFile() {
        this.plugin = (Main)Main.getPlugin((Class)Main.class);
    }
    
    public void fileCheck(final Player player) {
        final String playerName = player.getName();
        final String playerUuid = player.getUniqueId().toString();
        final File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + "PlayerDatabase");
        final File f = new File(userdata, String.valueOf(File.separator) + playerUuid + ".yml");
        final FileConfiguration playerData = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                playerData.createSection("Data");
                playerData.set("Data.Name", (Object)playerName);
                playerData.set("Data.UUID", (Object)playerUuid);
                playerData.createSection("Stats");
                playerData.set("Stats.Wins", (Object)0);
                playerData.set("Stats.Deaths", (Object)0);
                playerData.set("Stats.Disconnected", (Object)0);
                playerData.set("Stats.Games", (Object)0);
                playerData.set("Stats.Swaps", (Object)0);
                playerData.set("Stats.Time", (Object)0);
                playerData.save(f);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public int ReadData(final UUID uuid, final String data) {
        final File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + "PlayerDatabase");
        final File f = new File(userdata, String.valueOf(File.separator) + uuid + ".yml");
        final FileConfiguration playerData = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            return 0;
        }
        if (!playerData.contains(data)) {
            return 0;
        }
        final int Data = (int)playerData.get(data);
        return Data;
    }
    
    public void WriteData(final Player player, final String data, final Object value) {
        final String playerName = player.getName();
        final String playerUuid = player.getUniqueId().toString();
        final File userdata = new File(this.plugin.getDataFolder(), String.valueOf(File.separator) + "PlayerDatabase");
        final File f = new File(userdata, String.valueOf(File.separator) + playerUuid + ".yml");
        final FileConfiguration playerData = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (!f.exists()) {
            try {
                playerData.createSection("Data");
                playerData.set("Data.Name", (Object)playerName);
                playerData.set("Data.UUID", (Object)playerUuid);
                playerData.createSection("Stats");
                playerData.set("Stats.Wins", (Object)0);
                playerData.set("Stats.Deaths", (Object)0);
                playerData.set("Stats.Disconnected", (Object)0);
                playerData.set("Stats.Games", (Object)0);
                playerData.set("Stats.Swaps", (Object)0);
                playerData.set("Stats.Time", (Object)0);
                playerData.save(f);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        playerData.set(data, value);
        try {
            playerData.save(f);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
