package me.zsnow.stone.matamata;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.stone.matamata.listeners.Listeners;
import me.zsnow.stone.matamata.manager.Configs;


public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		if(!new File(getDataFolder(), "locations.yml").exists()){
			Configs.locations.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§5[Mata-Mata] LocationFile gerada com sucesso!");
		}
		if(!new File(getDataFolder(), "config.yml").exists()){
			Configs.config.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§5[Mata-Mata] ConfigurationFile gerada com sucesso!");
		}
		if(!new File(getDataFolder(), "itens.yml").exists()){
			Configs.config.saveDefaultConfig();
			Bukkit.getServer().getConsoleSender().sendMessage("§5[Mata-Mata] ItensFile gerada com sucesso!");
		}
		getCommand("matamata").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getConsoleSender().sendMessage("§a[Mata-Mata] plugin ligado.");
		
	}
	
}
