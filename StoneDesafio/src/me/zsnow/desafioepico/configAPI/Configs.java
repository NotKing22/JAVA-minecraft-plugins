package me.zsnow.desafioepico.configAPI;

import me.zsnow.desafioepico.Main;

public class Configs {
	
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager locations = new ConfigManager(Main.getInstance(), "locations.yml");

}
