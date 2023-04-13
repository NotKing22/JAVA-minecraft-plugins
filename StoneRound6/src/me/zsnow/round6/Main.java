package me.zsnow.round6;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.round6.configs.Configs;


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
		getCommand("round6").setExecutor(new Commands());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
	}
	
}
