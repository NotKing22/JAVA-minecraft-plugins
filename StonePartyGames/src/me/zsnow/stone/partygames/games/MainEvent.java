package me.zsnow.stone.partygames.games;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import me.zsnow.stone.partygames.Main;
import me.zsnow.stone.partygames.configs.Configs;

public class MainEvent {
	
	private final static MainEvent instance = new MainEvent();
	
	public static MainEvent getInstance() {
		return instance;
	}
	
	private int tempo = Configs.config.getConfig().getInt("Tempo.Entrada");
	private boolean pvpStatus;
	private boolean entradaStatus;
	private boolean eventoStatus;
	private int eventoAtual;
	@SuppressWarnings("unused")
	private String eventoAtualNome;
	private ArrayList<Player> participantes = new ArrayList<>();
	private ArrayList<Player> espectadores = new ArrayList<>();
	
	public int getEventoAtual() {
		return this.eventoAtual;
	}
	
	public int getTempo() {
		return this.tempo;
	}
	
	public boolean getEntradaStatus() {
		return this.entradaStatus;
	}
	
	public boolean getEventoStatus() {
		return this.eventoStatus;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}

	public ArrayList<Player> getSpectadores() {
		return this.participantes;
	}
	
	public boolean getPvPStatus() {
		return this.pvpStatus;
	}
	
	//
	
	public void setEventoAtual(int number) {
		this.eventoAtual = number;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setEntradaStatus(boolean booleanValue) {
		this.entradaStatus = booleanValue;
	}
	
	public void setEventoStatus(boolean booleanValue) {
		this.eventoStatus = booleanValue;
	}
	
	public void setPvPStatus(boolean booleanValue) {
		this.pvpStatus = booleanValue;
	}
	
	public void setSpectator(Player player) {
		this.espectadores.add(player);
		//add armor, tirar do participantes e tals
	}
	
	private void setEventoAtualNome(String eventoType) {
		this.eventoAtualNome = eventoType;
	}
	
	//primeiro seta o eventoatual e depois usa o defineEventoAtualNome pra atualizar a string
	
	//Q CODIGO MERDA, DESFAZ ESSA MISERA
	public void defineEventoAtualNome() {
		switch (getEventoAtual()) {
			case 1:
				setEventoAtualNome(minigame.CRAZY_POOL.toString());
				break;
	
			default:
				break;
			}
		}
	//
	
	
	// ############################################################################################################## //
	
	public enum minigame { // default listener sera os eventos que estarao sempre ativos, como o de quit e etc
		DEFAULT_LISTENER, CRAZY_POOL, 
	}
	
	public void registerMinigamesListener(minigame minigame) {
		switch (minigame) {
			case CRAZY_POOL:
				Bukkit.getPluginManager().registerEvents(new CrazyPool(), Main.getInstance());
				Bukkit.getConsoleSender().sendMessage("[StonePartyGames] (start-event) All " + minigame + " listeners have been enabled");
				break;
	
			default:
				break;
			}
	}
	
	public void unRegisterMinigamesListener(minigame minigame) {
		HandlerList.unregisterAll(Main.getInstance());
		Bukkit.getConsoleSender().sendMessage("[StonePartyGames] (end-event) All listeners have been disabled");
	}
	
	// ############################################################################################################## //
	
	public void resetData() {
		Bukkit.broadcastMessage("resetData MainEvent was not applied");
	}

	public void startTaskLobby() {
		Bukkit.broadcastMessage("startTaskLobby MainEvent was not applied");
		
	}
	

}
