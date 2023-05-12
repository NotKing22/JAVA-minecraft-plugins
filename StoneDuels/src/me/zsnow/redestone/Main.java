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
	
		public static void load() {
			try {
				me.zsnow.redestone.api.TitleAPI.load();	
			} 
			catch (Exception e) {}
		}
		
		private void enablePlugin() {
			load();
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
		checkServerVersion();
		enablePlugin();
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
	
	private Version checkServerVersion() {
		String ver = Bukkit.getVersion();

		if (ver.contains("1.13"))
			return Version.v1_13;
		else if (ver.contains("1.12"))
			return Version.v1_12;
		else if (ver.contains("1.11"))
			return Version.v1_11;
		else if (ver.contains("1.10"))
			return Version.v1_10;
		else if (ver.contains("1.9"))
			return Version.v1_9;
		else if (ver.contains("1.8"))
			return Version.v1_8;
		else if (ver.contains("1.7"))
			return Version.v1_7;
		else if (ver.contains("1.6"))
			return Version.v1_6;
		else if (ver.contains("1.5"))
			return Version.v1_5;
		else
			return Version.DESCONHECIDA;
	}

	
	
}
