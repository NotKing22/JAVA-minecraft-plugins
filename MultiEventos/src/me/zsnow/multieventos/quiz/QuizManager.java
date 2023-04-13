package me.zsnow.multieventos.quiz;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import me.zsnow.multieventos.configAPI.Configs;

public class QuizManager {
	
	public static QuizManager quiz = new QuizManager();
	
	private boolean ocorrendo;
	private double tempo;
	private String pergunta;
	private String resposta;
	
	private boolean almostAnswer;
	
	private double premio = Configs.config.getConfig().getDouble("quiz-premio"); 
	
	ArrayList<Player> chatEvent = new ArrayList<>();
	public HashMap<Player, Integer> etapas = new HashMap<>();
	
	public HashMap<Player, String> cachePergunta = new HashMap<>();
	public HashMap<Player, String> cacheResposta = new HashMap<>();
	
	public void Start() {
		this.setOcorrendo(true);
		this.setTempo(0);
		this.setPergunta(pergunta);
		this.setResposta(resposta);
	}
	
	public void Stop() {
		this.setOcorrendo(false);
		this.setTempo(0);
		this.setPergunta(null);
		this.setResposta(null);
		this.setAlmostAnswerBoolean(false);
		
	}
	
	public boolean getAlmostAnswerAllowed() {
		return almostAnswer;
	}
	
	public boolean getOcorrendo() {
		return this.ocorrendo;
	}
	
	public double getTempo() {
		return this.tempo;
	}
	
	public String getPergunta() {
		return this.pergunta;
	}
	
	public String getResposta() {
		return this.resposta;
	}
	
	public double getPremio() {
		return this.premio;
	}
	
	//
	
	public void setAlmostAnswerBoolean(boolean booleanValue) {
		this.almostAnswer = booleanValue;
	}
	
	public void setOcorrendo(boolean ocorrendo) {
		this.ocorrendo = ocorrendo;
	}
	
	public void setTempo(double tempo) {
		this.tempo = tempo;
	}
	
	public void setPergunta(String insertQuest) {
		this.pergunta = insertQuest;
	}
	
	public void setResposta(String insertAnswer) {
		this.resposta = insertAnswer;
	}
	
}
