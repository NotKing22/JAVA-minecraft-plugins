package me.zsnow.stonegoldenpig;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Pig;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.zsnow.stonegoldenpig.configs.Configs;
import me.zsnow.stonegoldenpig.manager.SorteioManager;
import net.milkbowl.vault.economy.Economy;

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
		if (!new File(getDataFolder(), "itens.yml").exists()) {
			Configs.itens.saveDefaultConfig();
			Configs.itens.reloadConfig();
		}
		getCommand("leilao").setExecutor(new Commands());
		getCommand("gpColetar").setExecutor(new ResgatarCMD());
		setupEconomy();
		Bukkit.getPluginManager().registerEvents(new InventoryControll(), this);
		Bukkit.getPluginManager().registerEvents(new Commands(), this);
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (entidades instanceof Pig && entidades.hasMetadata("porquinhodourado")) {
					 entidades.remove();
					 for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : HologramsAPI.getHolograms(Main.getInstance())) {
			  			    Location pigLoc = entidades.getLocation();
			  			    Location hologramLoc = hologram.getLocation();
			  			    if (pigLoc.distance(hologramLoc) < 10) {
			  			        hologram.delete();
			  			    }
			  			}
				}
			}
		}
	}
	
	public void onDisable() {
		SorteioManager.getInstance().resetData();
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (entidades instanceof Pig && entidades.hasMetadata("porquinhodourado")) {
					 entidades.remove();
					 for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : HologramsAPI.getHolograms(Main.getInstance())) {
			  			    Location pigLoc = entidades.getLocation();
			  			    Location hologramLoc = hologram.getLocation();
			  			    if (pigLoc.distance(hologramLoc) < 10) {
			  			        hologram.delete();
			  			    }
			  			}
				}
			}
		}
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
