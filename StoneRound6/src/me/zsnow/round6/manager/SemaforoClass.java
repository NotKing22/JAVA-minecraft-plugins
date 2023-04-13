package me.zsnow.round6.manager;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.round6.Main;
import me.zsnow.round6.api.LocationAPI;
import me.zsnow.round6.api.SchematicAPI;
import me.zsnow.round6.api.StringReplaceAPI;
import me.zsnow.round6.api.LocationAPI.location;
import me.zsnow.round6.configs.Configs;


public class SemaforoClass implements Listener {
	
	private static SemaforoClass instance = new SemaforoClass();
	
	public static SemaforoClass getSemaforo() {
		return instance;
	}
	
	PedestreClass members = PedestreClass.getInstance();
	
	private int tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada"), 
			    tempoLobby = Configs.config.getConfig().getInt("Tempo.Lobby");
	private boolean eventoStatus, entradaStatus;
	private int sinal_verde_tempo, sinal_amarelo_tempo, sinal_vermelho_tempo;
	private boolean sinal_verde, sinal_amarelo, sinal_vermelho;
	private int recompensa = Configs.config.getConfig().getInt("Premio_coins");
	
	int TaskID;
	
	public static enum sinal {
		VERDE,
		AMARELO,
		VERMELHO
	}
	
	public int getTempoEntrada() {
		return tempoEntrada;
	}
	
	public int getTempoLobby() {
		return tempoLobby;
	}
	
	public boolean getEventoStatus() {
		return eventoStatus;
	}
	
	public boolean getEntradaStatus() {
		return entradaStatus;
	}
	
	public void setTempoEntrada(int tempo) {
		tempoEntrada = tempo;
	}
	
	public void setTempoLobby(int tempo) {
		tempoLobby = tempo;
	}
	
	public void setEventoStatus(boolean booleanValue) {
		eventoStatus = booleanValue;
	}
	
	public void setEntradaStatus(boolean booleanValue) {
		entradaStatus = booleanValue;
	}
	
	public int getRecompensa() {
		return recompensa;
	}
	
	public int getSinalTempo(sinal sinal) {
		switch (sinal) {
			case VERDE:
				return sinal_verde_tempo;
			case AMARELO:
				return sinal_amarelo_tempo;
			case VERMELHO:
				return sinal_vermelho_tempo;
			default:
				break;
			}
		return 0;
	}
	
	public void setSinalTempo(sinal sinal, int tempo) {
		switch (sinal) {
			case VERDE:
				sinal_verde_tempo = tempo;
				break;
			case AMARELO:
				sinal_amarelo_tempo = tempo;
				break;
			case VERMELHO:
				sinal_vermelho_tempo = tempo;
				break;
			default:
				break;
			}
	}
	
		public void setSinalCor(sinal sinal) {
			PedestreClass pedestre = PedestreClass.getInstance();
				switch (sinal) {
					case VERDE:
						sinal_verde = true;
						sinal_amarelo = false;
						sinal_vermelho = false;
							for (Player pedestres : pedestre.getPedestres()) {
								pedestres.playSound(pedestres.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.5f);
							}
						break;
					case AMARELO:
						sinal_verde = false;
						sinal_amarelo = true;
						sinal_vermelho = false;
							for (Player pedestres : pedestre.getPedestres()) {
								pedestres.playSound(pedestres.getLocation(), Sound.CLICK, 1.0f, 0.5f);
							}
						break;
					case VERMELHO:
						sinal_verde = false;
						sinal_amarelo = false;
						sinal_vermelho = true;
						SchematicAPI.loadSchematicBoneca();
							for (Player pedestres : pedestre.getPedestres()) {
								pedestres.playSound(pedestres.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.5f);
							}
						break;
					default:
						break;
					}
				setPlayersHotbar();
			}
	
	public boolean getSinalCor(sinal sinal) {
		switch (sinal) {
			case VERDE:
				return sinal_verde;
			case AMARELO:
				return sinal_amarelo;
			case VERMELHO:
				return sinal_vermelho;
			default:
				break;
			}
		return sinal_verde;
		}
	
	@SuppressWarnings("deprecation")
	public void startSemaforoTask() {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		randomSemaforoTimer();
		TaskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if (getEventoStatus() == true && getEntradaStatus() == false) {
					if (getSinalTempo(sinal.VERDE) > 0) {
						if (!getSinalCor(sinal.VERDE)) {
							setSinalCor(sinal.VERDE);
						}
						setSinalTempo(sinal.VERDE, getSinalTempo(sinal.VERDE) - 1);
						return;
					}
					if (getSinalTempo(sinal.AMARELO) > 0) {
						if (!getSinalCor(sinal.AMARELO)) {
							setSinalCor(sinal.AMARELO);
						}
						setSinalTempo(sinal.AMARELO, getSinalTempo(sinal.AMARELO) - 1);
						return;
					}
					if (getSinalTempo(sinal.VERMELHO) > 0) {
						if (!getSinalCor(sinal.VERMELHO)) {
							setSinalCor(sinal.VERMELHO);
						}
						setSinalTempo(sinal.VERMELHO, getSinalTempo(sinal.VERMELHO) - 1);
						return;
					}
					if (getSinalTempo(sinal.VERMELHO) == 0) {
						if (!getSinalCor(sinal.VERMELHO)) {
							setSinalCor(sinal.VERMELHO);
						}
						randomSemaforoTimer();
					}
					
				} else {
					sh.cancelTask(TaskID);
				}
			}
		}, 0L, 20L);
	}
	
	public void startTaskLobby() {
		PedestreClass pedestre = PedestreClass.getInstance();
		setTempoLobby(Configs.config.getConfig().getInt("Tempo.Lobby"));
		for (Player participantes : pedestre.getPedestres()) {
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
						for (Player participantes : pedestre.getPedestres()) {
							for (String msg : Configs.config.getConfig().getStringList("Broadcast.Lobby-timer")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
							}
							participantes.playSound(participantes.getLocation(), Sound.CLICK, 1.0f, 0.5f);
						}
					}
					if (getTempoLobby() == 0) {
						for (Player participantes : pedestre.getPedestres()) {
							participantes.playSound(participantes.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.5f);
							participantes.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2));
						}
						setSinalCor(sinal.VERDE);
						SchematicAPI.setAirAtLocation();
						startSemaforoTask();
						cancel();
						// add o necessario para iniciar o evento
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
	
	public void randomSemaforoTimer() {
		Random random = new Random();
		sinal_verde_tempo = random.nextInt(6-2) + 2; // de 5 a 2 segundos
		sinal_amarelo_tempo = random.nextInt(5-2) + 2; // de 4 a 2 segundos
		sinal_vermelho_tempo = random.nextInt(4-1) + 1; // de 3 a 2 segundos
	}
	
	public void setPlayersHotbar() {
		PedestreClass members = PedestreClass.getInstance();
		if (getSinalCor(sinal.VERDE)) {
			for (Player geral : members.getPedestres()) {
				for (int i = 0; i < 9; i++) {
				geral.getInventory().setItem(i, greenClay());
				}
			}
		}
		if (getSinalCor(sinal.AMARELO)) {
			for (Player geral : members.getPedestres()) {
				for (int i = 0; i < 9; i++) {
				geral.getInventory().setItem(i, yellowClay());
				}
			}
		}
		if (getSinalCor(sinal.VERMELHO)) {
			for (Player geral : members.getPedestres()) {
				for (int i = 0; i < 9; i++) {
				geral.getInventory().setItem(i, redClay());
				}
			}
		}
	}
	

	
	// som https://www.youtube.com/watch?v=gH6zuc8_lds
	// som de contagem regressiva
	// som de iniciou
	// som de amarelo
	// som de vermelho
	// som de verde
	
	public ItemStack greenClay() {
		ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§2⚠ §a§lBatatinha frita, 1...");
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack yellowClay() {
		ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§6⚠ §e§l2...");
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack redClay() {
		ItemStack item = new ItemStack(Material.STAINED_CLAY, 1, (byte) 14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4⚠ §c§l3...");
		item.setItemMeta(meta);
		return item;
	}
	
	 @SuppressWarnings("deprecation")
	public void eventCanKeepRunning() {
		 (new BukkitRunnable() {
			
			@Override
			public void run() {
				if (SemaforoClass.getSemaforo().getEventoStatus() == true && SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
					return;
				}
				 if (members.getPedestres().size() == 0 && getEventoStatus() == true && getEntradaStatus() == false) {
					 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Sem-vencedor")) {
						 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
					 }
					 setEventoStatus(false);
					 setEntradaStatus(false);
					 resetData();
				 }
				 if (members.getPedestres().size() == 1 && getEventoStatus() == true && getEntradaStatus() == false) {
					 Player vencedor = members.getPedestres().get(0);
					 for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-acabou")) {
						 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
					 }
				     for (String cmd : Configs.config.getConfig().getStringList("premio")) {
				 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd));
				     }
					 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
						 vencedor.removePotionEffect(allPotionEffects.getType());
					 }
					 vencedor.sendTitle("", "§a§lVocê venceu o evento!");
					 vencedor.playSound(vencedor.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
					 vencedor.getInventory().setArmorContents(null);
					 vencedor.getInventory().clear();
					 setEventoStatus(false);
					 setEntradaStatus(false);
					 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
					 resetData();
				 }
				cancel();
			}
		}).runTaskTimer(Main.getInstance(), 20L, 0L);
	 }
	
	public void resetData() {
		setEntradaStatus(false);
		setEventoStatus(false);
		sinal_verde = false;
		sinal_amarelo = false;
		sinal_vermelho = false;
		if (members.getPedestres().size() != 0) {
			for (Player participantes : members.getPedestres()) {
				participantes.getInventory().clear();
				participantes.getInventory().setArmorContents(null);
				for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
					participantes.removePotionEffect(AllPotionEffects.getType());
				}
				LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
			}
		}
		members.getPedestres().clear();
		setTempoEntrada(Configs.config.getConfig().getInt("Tempo.Entrada"));
		setTempoLobby(Configs.config.getConfig().getInt("Tempo.Lobby"));
		//SchematicAPI.loadSchematicBoneca();
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
	}
	
}
