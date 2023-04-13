package me.zsnow.stone.endergun.manager;

import java.util.ArrayList;

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
	
	public void setEliminado(Player currentPlayer) {
		
	}
}
