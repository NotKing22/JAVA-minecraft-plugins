package me.zsnow.stone.bandeira.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

import me.zsnow.stone.bandeira.Main;
import me.zsnow.stone.bandeira.api.LocationAPI;
import me.zsnow.stone.bandeira.api.StringReplaceAPI;
import me.zsnow.stone.bandeira.api.LocationAPI.location;
import me.zsnow.stone.bandeira.configs.Configs;
import me.zsnow.stone.bandeira.times.TeamBlue;
import me.zsnow.stone.bandeira.times.TeamRed;

public class EventController {
	
	// arrumar o evento empatou

	private static EventController instance = new EventController();
	
	public static EventController getInstance() {
		return instance;
	}
	
	MembersController members = MembersController.getInstance();
	TeamBlue teamBlue = TeamBlue.getInstance();
	TeamRed teamRed = TeamRed.getInstance();
	
	private int tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada");
	private int tempoLobby = Configs.config.getConfig().getInt("Tempo.Lobby");
	private boolean entradaStatus, eventoStatus, captureLiberado;
	private int recompensa_coins = Configs.config.getConfig().getInt("Premio_coins");
	
	private int max_point = Configs.config.getConfig().getInt("pontos_maximos");
	
	private int point_blue;
	private int point_red;
	
	public int getPointBlue() {
		return point_blue;
	}
	
	public int getPointRed() {
		return point_red;
	}
	
	public int getMaxPoint() {
		return max_point;
	}
	
	public void setPointBlue() {
		point_blue++;
		if (point_blue == max_point) eventCanKeepRunning();
	}
	
	public void setPointRed() {
		point_red++;
		if (point_red == max_point) eventCanKeepRunning();
	}
	
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
	
	public boolean getCaptureLiberado() {
		return captureLiberado;
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
	
	public void setCaptureLiberado(boolean currentStatus) {
		captureLiberado = currentStatus;
	}
	
	
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
							participantes.playSound(participantes.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						}
						
						eventCanKeepRunning();
						// 1 min - 2 max
					 try {
						 	String local = "BLUE_FLAG";
							 World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + ".Mundo"));
							 CuboidSelection cuboid = new CuboidSelection (Mundo, getLocal("BLUE_FLAG"), getLocal("BLUE_FLAG_2"));
								Location min = cuboid.getMinimumPoint();
								Location max = cuboid.getMaximumPoint();
						        for (int x = min.getBlockX(); x <= max.getX(); x++) {
						            for (int z = (int) min.getZ(); z <= max.getZ(); z++) {
						                for (int y = (int) min.getY(); y <= max.getY(); y++) {
						                	
						                 Location location = new Location(Mundo, x, y, z);
						           		 Block playerLocationBlock = location.getWorld().getBlockAt(location.subtract(0 , 0, 0));
						           		 playerLocationBlock.setMetadata("BandeiraAzul", new FixedMetadataValue(Main.getInstance(), playerLocationBlock));
						                }
						            }
						        }
						        
							 CuboidSelection cuboidRed = new CuboidSelection (Mundo, getLocal("RED_FLAG"), getLocal("RED_FLAG_2"));
								Location minRed = cuboidRed.getMinimumPoint();
								Location maxRed = cuboidRed.getMaximumPoint();
						        for (int x = minRed.getBlockX(); x <= maxRed.getX(); x++) {
						            for (int z = (int) minRed.getZ(); z <= maxRed.getZ(); z++) {
						                for (int y = (int) minRed.getY(); y <= maxRed.getY(); y++) {
						                	
						                 Location location = new Location(Mundo, x, y, z);
						           		 Block playerLocationBlock = location.getWorld().getBlockAt(location.subtract(0 , 0, 0));
						           		 playerLocationBlock.setMetadata("BandeiraVermelha", new FixedMetadataValue(Main.getInstance(), playerLocationBlock));
						                }
						            }
						        }
						 
						} catch (Exception e) {
							e.printStackTrace();
							Bukkit.getServer().getConsoleSender().sendMessage("§c[StoneCaptureTheFlag] Houve um erro ao iniciar o evento. Uma das location nao foi definida");
						}
						teamBlue.setFlagPlacedStatus(true);
						teamRed.setFlagPlacedStatus(true);
						
						setCaptureLiberado(true);
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
	
	public void eventCanKeepRunning() {
		 if (eventoStatus == true && entradaStatus == false) {
			 if (teamBlue.getParticipantes().size() >= 1 && teamRed.getParticipantes().size() >= 1) {
				 if (getPointBlue() == max_point || getPointRed() == max_point) {
					 if (getPointBlue() > getPointRed()) {
						 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
							 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{time}", "§3§lAZUL")));
						 }
						 setEventoStatus(false);
						 for (Player participantes : members.getParticipantes()) {
								teamRed.getTeamRedList().remove(participantes);
								teamBlue.getTeamBlueList().remove(participantes);
								teamRed.removePlayerTeam(participantes);
								teamBlue.removePlayerTeam(participantes);
								participantes.getInventory().clear();
								participantes.getInventory().setArmorContents(null);
								LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
						 }
						 members.getParticipantes().clear();
						 resetData();
						 return;
					 }
					 if (getPointRed() > getPointBlue()) {
						 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
							 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{time}", "§c§lVERMELHO")));
						 }
						 setEventoStatus(false);
						 for (Player participantes : members.getParticipantes()) {
								teamRed.getTeamRedList().remove(participantes);
								teamBlue.getTeamBlueList().remove(participantes);
								teamRed.removePlayerTeam(participantes);
								teamBlue.removePlayerTeam(participantes);
								participantes.getInventory().clear();
								participantes.getInventory().setArmorContents(null);
								LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
						 }
						 members.getParticipantes().clear();
						 resetData();
						 return;
					 }
					 if (getPointBlue() == getPointRed()) {
						 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-empatou")) {
							 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{time}", "§c§lVERMELHO")));
						 }
						 setEventoStatus(false);
						 for (Player participantes : members.getParticipantes()) {
								teamRed.getTeamRedList().remove(participantes);
								teamBlue.getTeamBlueList().remove(participantes);
								teamRed.removePlayerTeam(participantes);
								teamBlue.removePlayerTeam(participantes);
								participantes.getInventory().clear();
								participantes.getInventory().setArmorContents(null);
								LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
						 }
						 members.getParticipantes().clear();
						 resetData();
						 return;
					 } 
					 }
				 }
			 } else {
				 endEvent();
			 }
		 }
	
	public void endEvent() {
		 if (eventoStatus == true && entradaStatus == false) {
			 if (getPointBlue() > getPointRed()) {
				 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
					 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{time}", "§3§lAZUL")));
				 }
				 resetData();
				 return;
			 }
			 if (getPointRed() > getPointBlue()) {
				 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
					 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{time}", "§c§lVERMELHO")));
				 }
				 resetData();
				 return;
			 }
			 if (getPointBlue() == getPointRed()) {
				 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-empatou")) {
					 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
				 }
				 resetData();
			 }
		 }
	}
	 

	
	public Location getLocal(String local) {
		String location = local.toUpperCase(); 
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
	  	return loc;
	}
	
	public void resetData() {
		setEntradaStatus(false);
		setEventoStatus(false);
		setCaptureLiberado(false);
		
		if (members.getParticipantes().size() != 0) {
			for (Player participantes : members.getParticipantes()) {
				participantes.getInventory().clear();
				participantes.getInventory().setArmorContents(null);
				for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
					participantes.removePotionEffect(AllPotionEffects.getType());
				}
				LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
			}
		}
		teamBlue.getTeamBlueList().clear();
		teamRed.getTeamRedList().clear();
		members.getParticipantes().clear();
		teamBlue.clearTeam();
		teamRed.clearTeam();
		teamBlue.clearPlayerWithFlag();
		teamRed.clearPlayerWithFlag();
		teamBlue.setFlagPlacedStatus(false);
		teamRed.setFlagPlacedStatus(false);
		point_blue = 0;
		point_red = 0;;
		setTempo(Configs.config.getConfig().getInt("Tempo.Entrada"));
		setTempoLobby(Configs.config.getConfig().getInt("Tempo.Lobby"));
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
	}
	
	public void setEliminado(Player playerHit) {
		members.getParticipantes().remove(playerHit);
		teamRed.getTeamRedList().remove(playerHit);
		teamBlue.getTeamBlueList().remove(playerHit);
		teamRed.removePlayerTeam(playerHit);
		teamBlue.removePlayerTeam(playerHit);
		if (playerHit.getWorld().getName().equals("Eventos")) {
			playerHit.getInventory().clear();
			playerHit.getInventory().setArmorContents(null);
		}
		LocationAPI.getLocation().teleportTo(playerHit, location.SAIDA);
	}
	
	public void eliminar(Player playerHit) {
		members.getParticipantes().remove(playerHit);
		teamRed.getTeamRedList().remove(playerHit);
		teamBlue.getTeamBlueList().remove(playerHit);
		teamRed.removePlayerTeam(playerHit);
		teamBlue.removePlayerTeam(playerHit);
		if (playerHit.getWorld().getName().equals("Eventos")) {
			playerHit.getInventory().clear();
			playerHit.getInventory().setArmorContents(null);
		}
		LocationAPI.getLocation().teleportTo(playerHit, location.SAIDA);
	}
	
}
