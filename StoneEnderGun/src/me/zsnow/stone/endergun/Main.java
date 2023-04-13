package me.zsnow.stone.endergun;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.stone.endergun.configs.Configs;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		setupEconomy();
		if (!new File(getDataFolder(), "config.yml").exists()) {
			Configs.config.saveDefaultConfig();
			Configs.config.reloadConfig();
		}
		if (!new File(getDataFolder(), "locations.yml").exists()) {
			Configs.locations.saveDefaultConfig();
			Configs.locations.reloadConfig();
		}
		getCommand("endergun").setExecutor(new Comandos());
		Bukkit.getPluginManager().registerEvents(new Listeners(), this);
		Bukkit.getServer().getConsoleSender().sendMessage("§a[StoneEnderGun] Plugin inicializado.");
	}
	
	 public Economy economy = null;
		
	  private boolean setupEconomy() {
	    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	    if (economyProvider != null) {
	      this.economy = ((Economy)economyProvider.getProvider());
	    }
	    return this.economy != null;
	  }
	
}
