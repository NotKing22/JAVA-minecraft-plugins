package me.zsnow.spookyrealm.configs;

import me.zsnow.spookyrealm.Main;

public class Configs {
	
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager locations = new ConfigManager(Main.getInstance(), "locations.yml");

}
