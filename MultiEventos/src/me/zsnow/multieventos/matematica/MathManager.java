package me.zsnow.multieventos.matematica;

import me.zsnow.multieventos.configAPI.Configs;

public class MathManager {
	
	private boolean ocorrendo;
	private double tempo;
	private String resultado;
	private String conta;
	private double premio = Configs.config.getConfig().getDouble("matematica-premio"); 
	
	public void Start(boolean ocorrendo, double tempo, String conta, String resultado, double premio) {
		this.setOcorrendo(true);
		this.setTempo(0);
		this.setConta(conta);
		this.setResultado(resultado);
	}
	
	public void Stop(boolean ocorrendo, String conta, String resultado) {
		this.setOcorrendo(false);
		this.setTempo(0);
		this.setConta(null);
		this.setResultado(null);
		
	}
	
	public boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public double getTempo() {
		return this.tempo;
	}
	
	public String getConta() {
		return this.conta;
	}
	
	public String getResultado() {
		return this.resultado;
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
	
	public void setResultado(String resultado) {
		this.resultado = resultado;
	}
	
	public void setConta(String conta) {
		this.conta =  conta;
	}
	
}
