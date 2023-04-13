package me.zsnow.stone.endergun.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stone.endergun.Listeners;
import me.zsnow.stone.endergun.Main;
import me.zsnow.stone.endergun.api.LocationAPI;
import me.zsnow.stone.endergun.api.LocationAPI.location;
import me.zsnow.stone.endergun.api.SchematicAPI;
import me.zsnow.stone.endergun.api.StringReplaceAPI;
import me.zsnow.stone.endergun.configs.Configs;

public class EventController {

	private static EventController instance = new EventController();
	
	public static EventController getInstance() {
		return instance;
	}
	
	MembersController members = MembersController.getInstance();
	
	private int tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada");
	private int tempoLobby = Configs.config.getConfig().getInt("Tempo.Lobby");
	private boolean entradaStatus, eventoStatus, armasLiberadas;
	private double recompensa_coins = Configs.config.getConfig().getDouble("Premio_coins");
	
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
	
	public double getRecompensa() {
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
							participantes.playSound(participantes.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						}
						setArmasLiberadas(true);
						Listeners listener = new Listeners();
						listener.registerAfkSystem();
						listener.refresher();
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
		 if (members.getParticipantes().size() == 1 && getEventoStatus() == true && getEntradaStatus() == false) {
			 Player vencedor = members.getParticipantes().get(0);
			 int premio = Configs.config.getInt("Premio_coins");
			 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
				 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
			 }
			 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
				 vencedor.removePotionEffect(allPotionEffects.getType());
			 }
			 vencedor.sendTitle("", "§a§lVocê venceu o evento!");
			 Main.getInstance().economy.depositPlayer(vencedor, premio);
			 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
			 vencedor.getInventory().setArmorContents(null);
			 vencedor.getInventory().clear();
			 setEventoStatus(false);
			 setEntradaStatus(false);
			 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
			 members.getParticipantes().clear();
			 resetData();
			 SchematicAPI.loadSchematic();
		 }
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
				LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
			}
		}
		Listeners.cooldownSnowball.clear();
		members.getParticipantes().clear();
		setTempo(Configs.config.getConfig().getInt("Tempo.Entrada"));
		setTempoLobby(Configs.config.getConfig().getInt("Tempo.Lobby"));
		Listeners.AFK.clear();
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
	}
}
