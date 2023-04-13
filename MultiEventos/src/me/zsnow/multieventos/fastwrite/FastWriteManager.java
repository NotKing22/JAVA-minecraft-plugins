package me.zsnow.multieventos.fastwrite;

import java.util.Random;

import me.zsnow.multieventos.configAPI.Configs;

public class FastWriteManager {
	
	public static FastWriteManager evento = new FastWriteManager();
	private boolean ocorrendo;
	private double segundos;
	private Integer premio = Configs.config.getConfig().getInt("premio-fastwrite");
	private String frase;
	
	public void Stop() {
		this.setOcorrendo(false);
		this.setSegundos(0.0D);
		this.setFrase(null);
	}
	
	public void Start() {
		this.setOcorrendo(true);
		this.setFrase(Configs.config.getConfig().getStringList("fastwrite-frases").
				get(new Random().nextInt(Configs.config.getConfig().getStringList("fastwrite-frases").size())));
	}
	
	public void Start2(String frase) {
		this.setOcorrendo(true);
		this.setFrase(frase);
	}
	
	public String getFrase() {
		return frase;
	}
	
	public Integer getPremio() {
		return this.premio;
	}
	
	public double getTempo() {
		return this.segundos;
	}
	
	public boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public void setOcorrendo(boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}
	
	public void setSegundos(double segundos) {
		this.segundos = segundos;
	}
	
	public void setFrase(String frase) {
		this.frase = frase;
	}
	
}
