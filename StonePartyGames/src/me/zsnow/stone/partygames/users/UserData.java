package me.zsnow.stone.partygames.users;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class UserData {
	
	private final static UserData instance = new UserData();
	
	public static UserData getInstance() {
		return instance;
	}
	
	private HashMap<String, Integer> score = new HashMap<>();
	private HashMap<Player, Short> currentLives = new HashMap<>();
	private Short maxCurrentLives = 5;
	
	public void playerScored(String player, Integer newPoint) {
		if (score.containsKey(player)) {
			int oldPoint = score.get(player);
			score.replace(player, oldPoint += newPoint);
		} else {
			score.put(player, newPoint);
		}
	}
	
	public HashMap<String, Integer> getScoreMap() {
		return score;
	}
	
	public Integer getPlayerScore(Player p) {
		return score.get(p.getName()) == null ? 0 : score.get(p.getName());
	}
	
	public void removeScore(Player player, Integer scoreToRemove) {
		if (score.containsKey(player.getName())) {
			int oldScore = score.get(player.getName()) > 0 ? score.get(player.getName()) : 0;
			score.replace(player.getName(), oldScore -= scoreToRemove);
		}
	}
	
	public void removeCurrentLives(Player player, Short livesToRemove) {
		if (currentLives.containsKey(player)) {
			int oldCurrentLives = currentLives.get(player);
			currentLives.replace(player, (short) (oldCurrentLives -= livesToRemove));
			if (currentLives.get(player) <= 0) {
				
			}
		} else {
			currentLives.put(player, maxCurrentLives);
		}
	}

}
