package me.zsnow.stone.paintball.manager;

import java.util.Collections;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stone.paintball.Listeners;
import me.zsnow.stone.paintball.Main;
import me.zsnow.stone.paintball.api.LocationAPI;
import me.zsnow.stone.paintball.api.StringReplaceAPI;
import me.zsnow.stone.paintball.api.LocationAPI.location;
import me.zsnow.stone.paintball.configs.Configs;
import me.zsnow.stone.paintball.times.TeamBlue;
import me.zsnow.stone.paintball.times.TeamRed;

public class EventController {

	private static EventController instance = new EventController();
	
	public static EventController getInstance() {
		return instance;
	}
	
	MembersController members = MembersController.getInstance();
	TeamBlue teamBlue = TeamBlue.get();
	TeamRed teamRed = TeamRed.get();
	
	private int tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada");
	private int tempoLobby = Configs.config.getConfig().getInt("Tempo.Lobby");
	private boolean entradaStatus, eventoStatus, armasLiberadas;
	private int recompensa_coins = Configs.config.getConfig().getInt("Premio_coins");
	
	public int getTempo() {
		return tempoEntrada;
	}
	
	public int getTempoLobby() {
		return tempoLobby;
	}
	
	public boolean getEntradaStatus() {
		return entradaStatus;
	}
	
	public boolean getEventoStatus() {
		return eventoStatus;
	}
	
	public int getRecompensa() {
		return recompensa_coins;
	}
	
	public boolean getArmasLiberadas() {
		return armasLiberadas;
	}
	
	//
	
	public void setTempo(int currentTime) {
		tempoEntrada = currentTime;
	}
	
	public void setTempoLobby(int currentTime) {
		tempoLobby = currentTime;
	}
	
	public void setEntradaStatus(boolean currentStatus) {
		entradaStatus = currentStatus;
	}
	
	public void setEventoStatus(boolean currentStatus) {
		eventoStatus = currentStatus;
	}
	
	public void setArmasLiberadas(boolean currentStatus) {
		armasLiberadas = currentStatus;
	}
	
	
	// ARRUMAR O TASK E O ITEM
	public void startTaskLobby() {
		
		for (Player participantes : members.getParticipantes()) {
			for (String msg : Configs.config.getStringList("Broadcast.Lobby-timer")) {
				participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
			}
		}
		(new BukkitRunnable() {
			
			@Override
			public void run() {
				if (getEventoStatus() == true) {
					if (getTempoLobby() > 0) {
					setTempoLobby(getTempoLobby() - 1);
						if (getTempoLobby() >= 1) {
						for (Player participantes : members.getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("Broadcast.Lobby-timer")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
							}
						}
					}
					if (getTempoLobby() == 0) {
						for (Player participantes : members.getParticipantes()) {
							members.putVidas(participantes);
							participantes.playSound(participantes.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						}
						setArmasLiberadas(true);
						eventCanKeepRunning();
						cancel();
					}
				} else {
					cancel();
				}
			} else {
				cancel();
			}
		}
		}).runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
	 @SuppressWarnings("deprecation")
	public void eventCanKeepRunning() {
		 if (getEventoStatus() == true && getEntradaStatus() == false && getArmasLiberadas() == true) {
			 int timeAzulSize = teamBlue.getTeamBlueSize();
			 int timeVermelhoSize = teamRed.getTeamRedSize();

			 if (timeAzulSize == 0 && timeVermelhoSize >= 1) {
				 int maxValueInMap=(Collections.max(teamRed.abates.values()));
			        for (Entry<Player, Integer> entry : teamRed.abates.entrySet()) {
			            if (entry.getValue()==maxValueInMap) {
			            	for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
			            		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg2(msg.replace("{abates}", String.valueOf(teamRed.getAbates(entry.getKey()))), entry.getKey(), "§c§lVERMELHO")));
			            	} 
			            	Player vencedor = entry.getKey();
					             for (String cmd : Configs.config.getConfig().getStringList("premio")) {
					  		 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd.replace("{vencedor}", entry.getKey().getName())));
					  		     }
					             vencedor.sendTitle("", "§a§lVocê venceu o evento!");
								 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
								 setEventoStatus(false);
								 setEntradaStatus(false);
								 if (members.getParticipantes().contains(vencedor)) {
						             for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
										 vencedor.removePotionEffect(allPotionEffects.getType());
									 }
									 vencedor.getInventory().setArmorContents(null);
									 vencedor.getInventory().clear();
									 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
								 }
								 resetData();
		            }
		        }
			 }
			 
			 if (timeVermelhoSize == 0 && timeAzulSize >= 1) {
				 int maxValueInMap=(Collections.max(teamBlue.abates.values()));
			        for (Entry<Player, Integer> entry : teamBlue.abates.entrySet()) {
			            if (entry.getValue()==maxValueInMap) {
			            	 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
			            		 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg2(msg.replace("{abates}", String.valueOf(teamBlue.getAbates(entry.getKey()))), entry.getKey(), "§3§lAZUL")));
			            	 }
			             Player vencedor = entry.getKey();
			             for (String cmd : Configs.config.getConfig().getStringList("premio")) {
			  		 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd.replace("{vencedor}", entry.getKey().getName())));
			  		     }
			             vencedor.sendTitle("", "§a§lVocê venceu o evento!");
						 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
						 setEventoStatus(false);
						 setEntradaStatus(false);
						 if (members.getParticipantes().contains(vencedor)) {
				             for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
								 vencedor.removePotionEffect(allPotionEffects.getType());
							 }
							 vencedor.getInventory().setArmorContents(null);
							 vencedor.getInventory().clear();
							 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
						 }
						 setEventoStatus(false);
						 setEntradaStatus(false);
						 resetData();
		            }
		        }
			 }
		 }
	 }
	 
	 @SuppressWarnings("deprecation")
	public void eventoEmpatou() {
		 int timeAzulSize = teamBlue.getTeamBlueSize();
		 int timeVermelhoSize = teamRed.getTeamRedSize();
		 if (timeVermelhoSize > timeAzulSize) {
			 int maxValueInMap=(Collections.max(teamRed.abates.values()));
		        for (Entry<Player, Integer> entry : teamRed.abates.entrySet()) {
		            if (entry.getValue()==maxValueInMap) {
		            	for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
		            		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg2(msg.replace("{abates}", String.valueOf(teamRed.getAbates(entry.getKey()))), entry.getKey(), "§c§lVERMELHO")));
		            	} 
		            	Player vencedor = entry.getKey();
				             for (String cmd : Configs.config.getConfig().getStringList("premio")) {
				  		 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd.replace("{vencedor}", entry.getKey().getName())));
				  		     }
				             vencedor.sendTitle("", "§a§lVocê venceu o evento!");
							 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
							 setEventoStatus(false);
							 setEntradaStatus(false);
							 if (members.getParticipantes().contains(vencedor)) {
					             for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
									 vencedor.removePotionEffect(allPotionEffects.getType());
								 }
								 vencedor.getInventory().setArmorContents(null);
								 vencedor.getInventory().clear();
								 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
							 }
							 resetData();
							 return;
	            }
	        }
		 }
		 if (timeVermelhoSize < timeAzulSize) {
			 int maxValueInMap=(Collections.max(teamBlue.abates.values()));
		        for (Entry<Player, Integer> entry : teamBlue.abates.entrySet()) {
		            if (entry.getValue()==maxValueInMap) {
		            	 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
		            		 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg2(msg.replace("{abates}", String.valueOf(teamBlue.getAbates(entry.getKey()))), entry.getKey(), "§3§lAZUL")));
		            	 }
		             Player vencedor = entry.getKey();
		             for (String cmd : Configs.config.getConfig().getStringList("premio")) {
		  		 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd.replace("{vencedor}", entry.getKey().getName())));
		  		     }
		             vencedor.sendTitle("", "§a§lVocê venceu o evento!");
					 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
					 setEventoStatus(false);
					 setEntradaStatus(false);
					 if (members.getParticipantes().contains(vencedor)) {
			             for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
							 vencedor.removePotionEffect(allPotionEffects.getType());
						 }
						 vencedor.getInventory().setArmorContents(null);
						 vencedor.getInventory().clear();
						 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
					 }
					 resetData();
					 return;
	            }
	        }
		 }
		 eventCanKeepRunning();
	 }
	
	public void resetData() {
		setEntradaStatus(false);
		setEventoStatus(false);
		setArmasLiberadas(false);
		
		if (members.getParticipantes().size() != 0) {
			for (Player participantes : members.getParticipantes()) {
				participantes.getInventory().clear();
				participantes.getInventory().setArmorContents(null);
				for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
					participantes.removePotionEffect(AllPotionEffects.getType());
				}
				participantes.showPlayer(participantes);
				participantes.spigot().setCollidesWithEntities(true);
				LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
			}
		}
		members.vidas.clear();
		teamBlue.abates.clear();
		teamRed.abates.clear();
		teamBlue.getTeamBlueList().clear();
		teamRed.getTeamRedList().clear();
		Listeners.cooldownSnowball.clear();
		members.getParticipantes().clear();
		setTempo(Configs.config.getConfig().getInt("Tempo.Entrada"));
		setTempoLobby(Configs.config.getConfig().getInt("Tempo.Lobby"));
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
	}
}
