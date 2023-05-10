package me.zsnow.redestone.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.redestone.Main;
import me.zsnow.redestone.api.ActionBarAPI;
import me.zsnow.redestone.config.Configs;

public class SumoInviteManager {
	
	private static SumoInviteManager instance = new SumoInviteManager();
	
	public static SumoInviteManager getInstance() {
		return instance;
	}
	
	public HashMap<Player, Integer> savedTimers = new HashMap<>();
	
	private static int contador;
	
	private Player PlayerInviteX;
	private Player PlayerInviteY;
	private Player Convidou;
	private int tempo = Configs.config.getConfig().getInt("convite-expira-em");
	private Boolean convitesOcupados = false;
	private Boolean protection;
	
	public int contadorPvP;
	private int tempoPvP = 6;
	
	public Player getPlayerX() {
		return PlayerInviteX;
	}
	
	public Player getPlayerY() {
		return PlayerInviteY;
	}
	
	public Player getConvidou() {
		return Convidou;
	}
	
	public int getTempo() {
		return tempo;
	}
	
	public boolean hasInvite(Player p) {
		if (PlayerInviteX == p || PlayerInviteY == p) {
			return true;
		}
		return false;
	}
	
	public boolean canInvite() {
		if (!convitesOcupados) {
			return true;
		}
		return false;
	}
	
	public void sendInviteTo(Player p, Player target) {
		PlayerInviteX = p;
		Convidou = p;
		PlayerInviteY = target;
		convitesOcupados = true;
		tempo = Configs.config.getConfig().getInt("convite-expira-em");
	}
	
	public void recuseOrExpireInvite(Player p, Player target) {
		PlayerInviteX = null;
		PlayerInviteY = null;
		Convidou = null;
		convitesOcupados = false;
		setProtection(false);
		tempo = Configs.config.getConfig().getInt("convite-expira-em");
	}
	
	public boolean getProtectionStatus() {
		return this.protection;
	}
	
	public void setProtection(boolean trueOrFalse) {
		this.protection = trueOrFalse;
	}
	
	@SuppressWarnings("deprecation")
	public void startTask() {
		
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		contador = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
		int tempo = Configs.config.getConfig().getInt("convite-expira-em");

			@Override
			public void run() {
				if (convitesOcupados == true) {
					tempo--;
					if ((!(PlayerInviteX.isOnline() && PlayerInviteY.isOnline())) || PlayerInviteX == null || PlayerInviteY == null) {
						String msg = "§c[Sumo] O jogador optou por ignorar o pedido de duelo.";
						PlayerInviteX.sendMessage(msg);
						PlayerInviteY.sendMessage(msg);
						recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
						tempo = getTempo();
						sh.cancelTask(contador);
						return;
						}
					SumoDuelManager sumoManager = SumoDuelManager.getInstance();
					if (sumoManager.getDuelando().contains(PlayerInviteX) &&
							sumoManager.getDuelando().contains(PlayerInviteY)) {
						recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
						tempo = getTempo();
						sh.cancelTask(contador);
						return;
					}
					if (tempo <= 0) { 
						String msg = "§c[Sumo] O tempo para que o pedido de duelo fosse respondido foi excedido.";
						PlayerInviteX.sendMessage(msg);
						PlayerInviteY.sendMessage(msg);
						recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
						tempo = getTempo();
						sh.cancelTask(contador);
						return;
					}
					
				} else {
					tempo = getTempo();
					recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
					sh.cancelTask(contador);
				}
			}
			
		}, 20, 20L);
	}
	
	// SUMO
	@SuppressWarnings("deprecation")
	public void runPvPTimer(Player playerX, Player playerY) {
			
		SumoDuelManager.getInstance().getDataInfo(playerX).setUnmove(true);
		SumoDuelManager.getInstance().getDataInfo(playerY).setUnmove(true);
		
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		contadorPvP = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			int timer = tempoPvP;
				
			@Override
			public void run() {
				if (timer > 1) {
					timer--;
					ActionBarAPI.sendActionBarMessage(playerX, "§b§l" + timer);
					playerX.playSound(playerX.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
						
					ActionBarAPI.sendActionBarMessage(playerY, "§b§l" + timer);
					playerY.playSound(playerY.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
					return;
				}
				SumoDuelManager.getInstance().getDataInfo(playerX).setMagicWaterStatus(true); 
				SumoDuelManager.getInstance().getDataInfo(playerY).setMagicWaterStatus(true);
					
				ActionBarAPI.sendActionBarMessage(playerX, "§6§lLUTEM!");				
				ActionBarAPI.sendActionBarMessage(playerY, "§6§lLUTEM!");
					
				playerX.playSound(playerX.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
				playerY.playSound(playerY.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
				sh.cancelTask(savedTimers.get(playerX));
				sh.cancelTask(savedTimers.get(playerY));
				savedTimers.remove(playerX);
				savedTimers.remove(playerY);
				SumoDuelManager.getInstance().getDataInfo(playerX).setUnmove(false);
				SumoDuelManager.getInstance().getDataInfo(playerY).setUnmove(false);
				return;
			}
		}, 10, 20L);
		savedTimers.put(playerX, contadorPvP);
		savedTimers.put(playerY, contadorPvP);
	}
	
	/*HashMap<Player, Player> invite = new HashMap<>();
	
	public Boolean hasInvite(Player p) {
		if (invite.containsKey(p)) {
			return true;
		}
		return false;
	}
	
	public Player getInviteSender(Player p) {
		return invite.get(p);
	}
	
	public void sendInvite(Player p, Player target) {
		invite.put(p, target);
		invite.put(target, p);
	}
	
	public void removeInvite(Player p, Player target) {
		invite.remove(p, target);
		invite.remove(target, p);
	}*/
	
}
