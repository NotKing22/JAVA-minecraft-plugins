package me.zsnow.multieventos.desembaralhe;

import me.zsnow.multieventos.configAPI.Configs;

public class DesembaralheManager {

	private boolean ocorrendo;
	private double tempo;
	private String palavra;
	private double premio = Configs.config.getConfig().getDouble("desembaralhe-premio");  // por na config
	
	public void Start(boolean ocorrendo, double tempo, String palavra, double premio) {
		this.setOcorrendo(true);
		this.setTempo(0);
		this.setPalavra(palavra);
	}
	
	public void Stop(boolean ocorrendo, String conta, String resultado) {
		this.setOcorrendo(false);
		this.setTempo(0);
		this.setPalavra(null);
		
	}
	
	public boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public double getTempo() {
		return this.tempo;
	}
	
	public String getPalavra() {
		return this.palavra;
	}
	
	public double getPremio() {
		return this.premio;
	}
	
	//
	
	public void setOcorrendo(boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}
	
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}
	
	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}
	
}
