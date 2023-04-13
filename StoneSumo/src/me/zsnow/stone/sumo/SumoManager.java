package me.zsnow.stone.sumo;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class SumoManager {

	public static SumoManager manager = new SumoManager();
	private int Tempo;
	private boolean eventoOcorrendo;
	private boolean entradaLiberada;
	private Player oponenteX;
	private Player oponenteY;
	private ArrayList<Player> participantes = new ArrayList<>();
	
	//
	
	public int getTempo() {
		return this.Tempo;
	}
	
	public boolean getOcorrendoStatus() {
		return this.eventoOcorrendo;
	}
	
	public boolean getEntradaLiberadaStatus() {
		return this.entradaLiberada;
	}
	
	public Player getOponenteX() {
		return this.oponenteX;
	}
	
	public Player getOponenteY() {
		return this.oponenteY;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}
	
	//
	
	public void setTempo(int tempo) {
		this.Tempo =  tempo;
	}
	
	public void setOcorrendoStatus(boolean status) {
		this.eventoOcorrendo = status;
	}
	
	public void setEntradaLiberadaStatus(boolean status) {
		this.entradaLiberada = status;
	}
	
	public void setOponenteX(Player oponenteX) {
		this.oponenteX = oponenteX;
	}
	
	public void setOponenteY(Player oponenteY) {
		this.oponenteY = oponenteY;
	}
	
	public void addParticipante(Player newPlayer) {
		if (participantes.contains(newPlayer)) {
			newPlayer.sendMessage("§cVocê já está no evento sumo!");
			return;
		}
		participantes.add(newPlayer);
	}
	
}
