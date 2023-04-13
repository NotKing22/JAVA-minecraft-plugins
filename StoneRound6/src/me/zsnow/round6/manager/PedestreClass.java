package me.zsnow.round6.manager;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class PedestreClass {

	private static PedestreClass instance = new PedestreClass();
	
	public static PedestreClass getInstance() {
		return instance;
	}
	
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public ArrayList<Player> getPedestres() {
		return participantes;
	}
	
}
