package me.zsnow.stone.partygames;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.zsnow.stone.partygames.configs.Configs;
import me.zsnow.stone.partygames.title.Version;

public class Main extends JavaPlugin {
	
	public final String getPrefix = "§c§lP§6§lA§e§lR§2§lT§3§lY §c§lG§6§lA§e§lM§2§lE§e§lS";
	
	public static Main getInstance() {
		return getPlugin(Main.class);
	}
	
	public void onEnable() {
		checkServerVersion();
		enablePlugin();
		
		//GENERATING FOLDER
		if (!new File(getDataFolder(), "/schematics").exists()) {
			File file = new File(getDataFolder() + "/schematics");
			file.mkdir();
		}
		if (!new File(getDataFolder(), "/locations").exists()) {
			File file = new File(getDataFolder() + "/locations");
			file.mkdir();
		} 
		//end
		
		// GENERATING YML
		if (!new File(getDataFolder(), "config.yml").exists()) {
			Configs.config.saveDefaultConfig("");
			Configs.config.reloadConfig();
		}
		if (!new File(getDataFolder(), "/locations/locations_lobby.yml").exists()) {
			Configs.locations_lobby.saveDefaultConfig("locations/");
			Configs.locations_lobby.reloadConfig();
		}
		if (!new File(getDataFolder(), "/locations/crazypool_locations.yml").exists()) {
			Configs.crazypool_locations.saveDefaultConfig("locations/");
			Configs.crazypool_locations.reloadConfig();
		}
		//end
		// DA UM JEITO DE LER DENTRO DA PASTA, PASTA PASTA LOCATIONS
		//Bukkit.getPluginManager().registerEvents(new CrazyPool(), this);
		getCommand("partygames").setExecutor(new Commands());
		
	}
	
	
	/* ######################################################################################### */
	
	public static void load() {
		try {
			me.zsnow.stone.partygames.title.TitleAPI.load();	
		} 
		catch (Exception e) {}
	}
	
	private void enablePlugin() {
		load();
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
