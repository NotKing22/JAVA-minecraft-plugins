package me.zsnow.stonebatataquente.manager;

import me.zsnow.stonebatataquente.configs.Configs;

public class EventController {

	private static EventController instance = new EventController();
	
	public static EventController getInstance() {
		return instance;
	}
	
	protected int tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada");
	private boolean entradaStatus;
	private boolean eventoStatus;
	private double recompensa_coins = Configs.config.getConfig().getDouble("recompensa");
	
	public double getRecompensaCoins() {
		return recompensa_coins;
	}
	
	public int getTempoEntrada() {
		return this.tempoEntrada;
	}
	
	public void setTempoEntrada(int tempo) {
		tempoEntrada = tempo;
	}
	
	public boolean getEntradaStatus() {
		return entradaStatus;
	}
	
	public boolean getEventoStatus() {
		return eventoStatus;
	}
	
	public void setEntradaStatus(boolean booleanValue) {
		entradaStatus = booleanValue;
	}
	
	public void setEventoStatus(boolean booleanValue) {
		eventoStatus = booleanValue;
	}
	
}
