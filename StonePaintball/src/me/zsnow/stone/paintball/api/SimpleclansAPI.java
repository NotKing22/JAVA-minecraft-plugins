package me.zsnow.stone.paintball.api;

import org.bukkit.entity.Player;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class SimpleclansAPI {

	private static SimpleclansAPI instance = new SimpleclansAPI();
	
	public static SimpleclansAPI getAPI() {
		return instance;
	}
	
	public static SimpleClans getSC() {
		return SimpleClans.getInstance();
	}
	  
	public void enableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(true); 
	}
	  
	public void disableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(false); 
	}
	
}
