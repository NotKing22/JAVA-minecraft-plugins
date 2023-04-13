package me.zsnow.redestone;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.listeners.DuelListeners;
import me.zsnow.redestone.listeners.MenuListeners;
import me.zsnow.redestone.mysql.ConexaoSQL;
import me.zsnow.redestone.mysql.MysqlMethods;
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
	
		public String getMito() {
			return MysqlMethods.mito;
		}
		
	  
	public void onEnable() {
			if(!new File(getDataFolder(), "locations.yml").exists()){
				Configs.locations.saveDefaultConfig();
				Bukkit.getServer().getConsoleSender().sendMessage("§a[Duelo] Locations.yml gerada com sucesso.");
			}
			if(!new File(getDataFolder(), "config.yml").exists()){
				Configs.config.saveDefaultConfig();
				Configs.config.reloadConfig();
				Bukkit.getServer().getConsoleSender().sendMessage("§a[Duelo] config.yml gerada com sucesso.");
			}
			if(!new File(getDataFolder(), "mito.yml").exists()){
				Configs.mito.saveDefaultConfig();
				Configs.mito.reloadConfig();
			}
		setupEconomy();
		getCommand("duelo").setExecutor(new Commands());
		getCommand("mito").setExecutor(new CommandMito());
		Bukkit.getServer().getPluginManager().registerEvents(new MenuListeners(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new DuelListeners(), this);
		ConexaoSQL.openConnection();
		ConexaoSQL.createTable();
		MysqlMethods.loadUserData();
		MysqlMethods.updateDataSQL();
	}
	
	public void onDisable() {
		MysqlMethods.forceUpdateSQL();
		ConexaoSQL.closeConnection();
	}
	
	
}
