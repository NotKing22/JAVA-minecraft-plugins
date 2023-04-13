package me.zsnow.stone.paintball.manager;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class MembersController {

	private static MembersController instance = new MembersController();
	
	public static MembersController getInstance() {
		return instance;
	}
	
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public ArrayList<Player> getParticipantes() {
		return participantes;
	}
	
	public HashMap<Player, Integer> vidas = new HashMap<>();
	
	public void putVidas(Player player) {
		if (vidas.containsKey(player)) {
			return;
		}
		vidas.put(player, 3);
	}
	
	public int getVidas(Player player) {
		return vidas.get(player) == null ? 0 : vidas.get(player);
	}
	
	public void deleteVidas(Player p) {
		vidas.remove(p);
	}
	
	public void deathVidaRemover(Player player) {
		if (vidas.get(player) != null) {
			vidas.replace(player, getVidas(player)-1);
		}
	}
}
