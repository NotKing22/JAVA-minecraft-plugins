package me.zsnow.stone.league;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import me.zsnow.stone.league.sql.LeagueConnection;
import me.zsnow.stone.league.sql.LeagueSQL;

public class Main extends JavaPlugin {
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	LeagueSQL sql = LeagueSQL.getInstance();
	LeagueCache cache = LeagueCache.getInstance();
	
	private FileConfiguration config;
	
    public FileConfiguration getConfig() {
        return config;
    }
	
    public void onEnable() {
    	File file = new File(getDataFolder(), "config.yml");

    	if (file.exists()) {
    	    config = YamlConfiguration.loadConfiguration(file);
    	} else {
    	   saveDefaultConfig();
    	}
    	if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
    		Bukkit.getServer().getConsoleSender().sendMessage("§c[StoneLeague] PlaceHolderAPI not found.");
    	}
		getCommand("league").setExecutor(new Commands());
		Bukkit.getServer().getPluginManager().registerEvents(new joinListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerKillTracker(), this);
		LeagueConnection.openConnection();
		LeagueConnection.createTable();
        sql.loadToCache();
		sql.updateTask();
		registerPlaceholders();
		//load e update dps
	}
	
	public void onDisable() {
		sql.forceSaveSQL();
	}
	
	 @SuppressWarnings("deprecation")
	private void registerPlaceholders() {
	        PlaceholderAPI.registerPlaceholderHook("stoneleague", new PlaceholderHook() {
	        	
	            @Override
	            public String onRequest(OfflinePlayer p, String params) {
	                if(p != null && p.isOnline()){
	                    return onPlaceholderRequest(p.getPlayer(), params);
	                }
	                return null;
	            }

	            @Override
	            public String onPlaceholderRequest(Player p, String params) {
	                if(p == null){
	                    return null;
	                }
	                // %stoneleague_PARAMS%
	                if(params.equalsIgnoreCase("symbol")){
	                    return cache.getLeague().get(p.getName()).getSymbol();
	                }
	                if(params.equalsIgnoreCase("league")){
	                    return cache.getLeague().get(p.getName()).name();
	                }
	                if(params.equalsIgnoreCase("nextleague")){
	                    return cache.getLeague().get(p.getName()).getNextLeague().name();
	                }
	                return null;
	            }
	        });
	    }
}
