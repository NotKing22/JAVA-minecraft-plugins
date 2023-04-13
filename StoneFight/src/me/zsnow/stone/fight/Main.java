package me.zsnow.stone.fight;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.stone.fight.config.Configs;
import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	 public Economy economy = null;
		
	  private boolean setupEconomy() {
	    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	    if (economyProvider != null) {
	      this.economy = ((Economy)economyProvider.getProvider());
	    }
	    return this.economy != null;
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
		if (!new File(getDataFolder(), "itens.yml").exists()) {
			Configs.itens.saveDefaultConfig();
			Configs.itens.reloadConfig();
			
		}
		getCommand("fight").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Commands(), this);
		setupEconomy();
		Bukkit.getConsoleSender().sendMessage("§a[StoneFight] Plugin inicializado com sucesso!");
	}
	
}
