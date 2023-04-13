package me.zsnow.eventnexus.config;

import me.zsnow.eventnexus.Main;

public class Configs {
	
	public static final ConfigManager positions = new ConfigManager(Main.getInstance(), "positions.yml");
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");

}
