package me.zsnow.eventnexus;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.eventnexus.config.Configs;
import me.zsnow.eventnexus.listeners.Listeners;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		getCommand("nexus").setExecutor(new Commands());
		if(!new File(getDataFolder(), "positions.yml").exists()){
			Configs.positions.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§3§l[NEXUS] LocationFile gerada com sucesso!");
		}
		if(!new File(getDataFolder(), "config.yml").exists()){
			Configs.config.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§3§l[NEXUS] ConfigurationFile gerada com sucesso!");
		}
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getConsoleSender().sendMessage("§3§l[NEXUS] §ePlugin inicializado com sucesso.");
	}
	
}
