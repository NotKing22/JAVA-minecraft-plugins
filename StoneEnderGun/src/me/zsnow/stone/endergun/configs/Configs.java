package me.zsnow.stone.endergun.configs;

import me.zsnow.stone.endergun.Main;

public class Configs {
	
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager locations = new ConfigManager(Main.getInstance(), "locations.yml");

}
