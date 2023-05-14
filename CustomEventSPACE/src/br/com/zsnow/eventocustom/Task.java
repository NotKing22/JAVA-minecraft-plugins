package br.com.zsnow.eventocustom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Task {
	
	public static void EntradaTimer(){
		
		new BukkitRunnable(){
	    	int tempo = 180 + 20;

	    		public void run(){
	    			if (Main.EntradaLiberada == true) {
        				tempo = tempo - 20;
	        				if (Main.participantes.size() < Main.players_slot) {
			            		Bukkit.broadcastMessage("");
			            		Bukkit.broadcastMessage(Main.prefix);
			            		Bukkit.broadcastMessage("§6* §eEvento começando em: §f" + this.tempo + " §esegundos.");
			            		Bukkit.broadcastMessage("§6* §eNúmero de participantes: §f" + Main.participantes.size() + "§7/§f" + Main.players_slot);
			            		Bukkit.broadcastMessage("§6* §eEntre utilizando: §3§l/evento entrar");
			            		Bukkit.broadcastMessage("");
	        				} else {
			            		for (Player participantes : Main.participantes) {
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(Main.prefix + " §eO evento já irá começar. Apenas aguarde o §c[MANAGER] §eexplicá-lo.");
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(" ");
			            		}
			            		Main.EntradaLiberada = false;
			            		cancel();
	        				}
	        				if (tempo == 0) {
			            		for (Player participantes : Main.participantes) {
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(Main.prefix + " §eO evento já irá começar. Apenas aguarde o §c[MANAGER] §eexplicá-lo.");
			            			participantes.sendMessage(" ");
			            			participantes.sendMessage(" ");
			            		}
			            		Main.EntradaLiberada = false;
			            		cancel();
			            		
	        				}
	    			} else {
	    				cancel();
	    			}
	    		}
		    }.runTaskTimer(Main.getPlugin(), 0L, 20*20L);
		}
	
}
