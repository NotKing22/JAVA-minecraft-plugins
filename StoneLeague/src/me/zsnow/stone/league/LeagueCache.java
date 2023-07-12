package me.zsnow.stone.league;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class LeagueCache {

	private static LeagueCache instance = new LeagueCache();
	
	public static LeagueCache getInstance() {
		return instance;
	}
	
	private HashMap<String, League> league = new HashMap<>();
	
	public boolean hasLeague(Player player) {
		if (getLeague().containsKey(player.getName()))
			return true;
		return false;
	}

	public HashMap<String, League> getLeague() {
		return league;
	}

	public void setLeague(String player, League liga) {
	    if (league.containsKey(player)) {
	        league.replace(player, liga);
	    } else {
	        league.put(player, liga);
	    }
	}

	
}
