package me.zsnow.multieventos.loteria;

import me.zsnow.multieventos.configAPI.Configs;

public class LoteriaManager {

	public static LoteriaManager manager = new LoteriaManager();
	private boolean ocorrendo;
	private Integer numero;
	private Integer tempo = Configs.config.getConfig().getInt("loteria-sorteio-tempo");
	private Double premio = Configs.config.getConfig().getDouble("premio-loteria");
	
	public boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public Integer getNumero() {
		return this.numero;
	}
	
	public Integer getTempo() {
		return this.tempo;
	}
	
	public Double getPremio() {
		return this.premio;
	}
	
	public void getTempo(double premio) {
		this.premio = premio;
	}
	//
	
	public void setOcorrendo(boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
}
