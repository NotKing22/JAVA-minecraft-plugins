package me.zsnow.stone.sumoall;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;


public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
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
		getCommand("sumoall").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Commands(), this);
		setupEconomy();
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage("§a[StoneSumoALL] Plugin inicializado com sucesso!");
	}
	
}
