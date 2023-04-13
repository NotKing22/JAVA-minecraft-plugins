package me.zsnow.redestone.config;

import me.zsnow.redestone.Main;

public class Configs {
	
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager mito = new ConfigManager(Main.getInstance(), "mito.yml");
	public static final ConfigManager locations = new ConfigManager(Main.getInstance(), "locations.yml");

}
