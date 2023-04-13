package me.zsnow.stone.sumoall;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class SumoManager {

	public static SumoManager manager = new SumoManager();
	private int Tempo;
	private boolean eventoOcorrendo;
	private boolean entradaLiberada;
	private ArrayList<Player> participantes = new ArrayList<>();
	private boolean pvp;
	
	//
	
	public boolean getPvPStatus() {
		return pvp;
	}
	
	public int getTempo() {
		return this.Tempo;
	}
	
	public boolean getOcorrendoStatus() {
		return this.eventoOcorrendo;
	}
	
	public boolean getEntradaLiberadaStatus() {
		return this.entradaLiberada;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}
	
	//
	
	public void setPvPStatus(boolean pvp) {
		this.pvp = pvp;
	}
	
	public void setTempo(int tempo) {
		this.Tempo =  tempo;
	}
	
	public void setOcorrendoStatus(boolean status) {
		this.eventoOcorrendo = status;
	}
	
	public void setEntradaLiberadaStatus(boolean status) {
		this.entradaLiberada = status;
	}
	
	
	public void addParticipante(Player newPlayer) {
		if (participantes.contains(newPlayer)) {
			newPlayer.sendMessage("§cVocê já está no evento sumo!");
			return;
		}
		participantes.add(newPlayer);
	}
	
}
