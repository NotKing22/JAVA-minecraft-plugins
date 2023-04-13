package me.zsnow.stone.sumo;

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
		getCommand("sumo").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new Commands(), this);
		setupEconomy();
		saveDefaultConfig();
		Bukkit.getConsoleSender().sendMessage("§a[StoneSumo] Plugin inicializado com sucesso!");
	}
	
}
