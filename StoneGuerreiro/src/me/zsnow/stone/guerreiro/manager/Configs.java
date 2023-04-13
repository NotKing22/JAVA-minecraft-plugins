package me.zsnow.stone.guerreiro.manager;

import me.zsnow.stone.guerreiro.Main;

public class Configs {
	
	public static final ConfigManager locations = new ConfigManager(Main.getInstance(), "locations.yml");
	public static final ConfigManager config = new ConfigManager(Main.getInstance(), "config.yml");
	public static final ConfigManager itens = new ConfigManager(Main.getInstance(), "itens.yml");

}
