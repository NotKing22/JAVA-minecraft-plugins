package me.zsnow.stone.partygames.configs;

import me.zsnow.stone.partygames.Main;

public class Configs {
	
	//public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "", "/config.yml");
	public static final ConfigManager crazypool_locations = new ConfigManager(Main.getInstance(), "/locations", "/crazypool_locations.yml");
	public static final ConfigManager locations_lobby = new ConfigManager(Main.getInstance(), "/locations", "/locations_lobby.yml");
	//public static final ConfigManager locations_lobby = new ConfigManager(Main.getInstance(), "/locations/locations_lobby.yml");
	//public static final ConfigManager crazypool_locations = new ConfigManager(Main.getInstance(), "/locations/crazypool_locations.yml");

}
