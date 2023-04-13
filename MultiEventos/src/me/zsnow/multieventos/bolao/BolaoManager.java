package me.zsnow.multieventos.bolao;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.zsnow.multieventos.configAPI.Configs;

public class BolaoManager {

	private boolean ocorrendo;
	private boolean apostasLiberadas;
	private ArrayList<Player> participantes = new ArrayList<>();
	private int valorAcumulado = Configs.config.getConfig().getInt("valor-acumulado-inicial");
	private int valorAposta = Configs.config.getConfig().getInt("bolao-valor-aposta");
	private int tempo = Configs.config.getConfig().getInt("tempo-bolao");
	
	//getters
	
	public Boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public Boolean apostasLiberadas() {
		return this.apostasLiberadas;
	}
	
	public ArrayList<Player> getParticipantes() {
		return this.participantes;
	}
	
	public int getTempo() {
		return this.tempo;
	}
	
	public int getValorAcumulado() {
		return this.valorAcumulado;
	}
	
	public int getValorAposta() {
		return this.valorAposta;
	}
	
	//setters
	
	
	public void setOcorrendoStatus(Boolean status) {
		this.ocorrendo = status;
	}
	
	public void setApostasLiberadas(Boolean status) {
		this.apostasLiberadas = status;
	}
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setValorAcumulado(Integer valor) {
		this.valorAcumulado = valor;
	}
	
	
}

