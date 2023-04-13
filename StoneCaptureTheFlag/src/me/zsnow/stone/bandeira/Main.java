package me.zsnow.stone.bandeira;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

import me.zsnow.stone.bandeira.configs.Configs;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		if (!new File(getDataFolder(), "config.yml").exists()) {
			Configs.config.saveDefaultConfig();
			Configs.config.reloadConfig();
		}
		if (!new File(getDataFolder(), "locations.yml").exists()) {
			Configs.locations.saveDefaultConfig();
			Configs.locations.reloadConfig();
		}
		getCommand("capture").setExecutor(new Comandos());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneCaptureTheFlag] Plugin inicializado.");
	}
	
    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin) return (WorldEditPlugin) p;
        else return null;
    }
	
}
