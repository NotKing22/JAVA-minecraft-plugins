package me.zsnow.desafioepico.controller;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.desafioepico.Listeners;
import me.zsnow.desafioepico.Main;
import me.zsnow.desafioepico.configAPI.LocationAPI;


public class EventController {

	public boolean mortalhaDaNoite;
	
	public static EventController admin = new EventController();
	public HashMap<Player, Integer> playerCoinsNether = new HashMap<>();
	ArrayList<Player> participantesNether = new ArrayList<>();
	
	int tempoTotalNether = 30*60;
	
	int tempoDecorridoNether;
	int CoinsDropedNether;
	boolean ocorrendoNether;
	boolean entradaLiberadaNether;
	
	ArrayList<Player> participantesEden = new ArrayList<>();
	public HashMap<Player, Integer> playerCoinsEden = new HashMap<>();
	
	int tempoTotalEden;
	
	int CoinsDropedEden;
	int tempoDecorridoEden;
	boolean ocorrendoEden;
	boolean entradaLiberadaEden;
	
	public boolean getNetherOcorrendo() {
		return this.ocorrendoNether;
	}
	
	public boolean getNetherEntradaLiberada() {
		return this.entradaLiberadaNether;
	}
	
	public int getNetherTempoDecorrido() {
		return this.tempoDecorridoNether;
	}
	
	public ArrayList<Player> getNetherParticipantes() {
		return this.participantesNether;
	}
	
	//
	
	public void setNetherOcorrendo(boolean booleanStatus) {
		this.ocorrendoNether = booleanStatus;
	}
	
	public void setNetherEntradaLiberada(boolean booleanStatus) {
		this.entradaLiberadaNether = booleanStatus;
	}
	
	public void setNetherTempoDecorrido(int tempo) {
		this.tempoDecorridoNether = tempo;
	}
	
	public void setCoinsDroppedNether(int value) {
		this.CoinsDropedNether = value;
	}
	
	public void setCoinsDroppedEden(int value) {
		this.CoinsDropedEden = value;
	}
	
	public void setMortalhaDaNoiteStatus(Boolean booleanValue) {
		this.mortalhaDaNoite = booleanValue;
		if (booleanValue == true) {
			EntityController.enableMortalhaDaNoite();
		}
	}
	
	public void resetDataStats() {
		playerCoinsNether.clear();
		participantesNether.clear();
		CoinsDropedNether = 0;
		setNetherEntradaLiberada(false);
		setNetherOcorrendo(false);
		setNetherTempoDecorrido(0);
		setMortalhaDaNoiteStatus(false);
	}
	
	//
	
	public boolean getEdenOcorrendo() {
		return this.ocorrendoNether;
	}
	
	public boolean getEdenEntradaLiberada() {
		return this.entradaLiberadaEden;
	}
	
	public int getEdenTempoDecorrido() {
		return this.tempoDecorridoEden;
	}
	
	public ArrayList<Player> getEdenParticipantes() {
		return this.participantesEden;
	}
	
	public void setEdenOcorrendo(boolean booleanStatus) {
		this.ocorrendoEden = booleanStatus;
	}
	
	public void setEdenEntradaLiberada(boolean booleanStatus) {
		this.entradaLiberadaEden = booleanStatus;
	}
	
	public void setEdenTempoDecorrido(int tempo) {
		this.tempoDecorridoEden = tempo;
	}
	
	public int getCoinsDroppedNether() {
		return this.CoinsDropedNether;
	}
	
	public int getCoinsDroppedEden() {
		return this.CoinsDropedEden;
	}
	
	public boolean getMortalhaDaNoiteStatus() {
		return this.mortalhaDaNoite;
	}
//	public void stopTempoDecorrido() {
//		Bukkit.getServer().getScheduler().cancelTask(TaskTempoDecorrido);
//	}
	
	public void startTempoDecorrido() {
		
		(new BukkitRunnable() {
			int time = 0;
			
			@Override
			public void run() {
				if (EventController.admin.getNetherOcorrendo() == true) {
					if (time >= 30*60) {
						Listeners.endInvasion();
						 EventController.admin.playerCoinsNether.clear();
						 
						  for (Player participantes :  EventController.admin.getNetherParticipantes()) {
							  LocationAPI.sendTo(participantes, "SAIDA-NETHER");
							  participantes.sendMessage("");
							  participantes.sendMessage("§cO tempo máximo de duração da dungeon foi excedido. Desafio cancelado.");
							  participantes.sendMessage("");
						  }
						  EventController.admin.getNetherParticipantes().clear();
						  return;
					}
					time++;
					EventController.admin.setNetherTempoDecorrido(time);
				} else {
					cancel();
				}
			}
		}).runTaskTimer(Main.getInstance(), 0, 20);  
	}
	
/*	private static int TaskTempoDecorrido;
	
	public void startTempoDecorrido() {
		
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		TaskTempoDecorrido = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			int tempo = 0;
			
			@Override
			public void run() {
				if (EventController.admin.getNetherOcorrendo() == true) {
					if (tempo >= 30*60) {
						Listeners.endInvasion();
						 EventController.admin.playerCoinsNether.clear();
						 
						  for (Player participantes :  EventController.admin.getNetherParticipantes()) {
							  LocationAPI.sendTo(participantes, "SAIDA-NETHER");
							  participantes.sendMessage("");
							  participantes.sendMessage("§cO tempo máximo de duração do Desafio foi excedido. Desafio cancelado.");
							  participantes.sendMessage("");
						  }
						  EventController.admin.getNetherParticipantes().clear();
						  return;
					}
					tempo++;
					EventController.admin.setNetherTempoDecorrido(tempo);
				}
			}
		}, 0L, 20L);
	}
	*/
	
}
