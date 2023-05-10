package me.zsnow.redestone.manager;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.redestone.Main;
import me.zsnow.redestone.config.Configs;

public class InviteManager {
	
	private static InviteManager instance = new InviteManager();
	
	public static InviteManager getInstance() {
		return instance;
	}
	
	private int contador;
	public int contadorPvP;
	
	private Player PlayerInviteX;
	private Player PlayerInviteY;
	private Player Convidou;
	private int tempo = Configs.config.getConfig().getInt("convite-expira-em");
	private Boolean convitesOcupados = false;
	private Boolean protection;
	
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
						String msg = "§c[Duelo] O jogador optou por ignorar o pedido de duelo.";
						PlayerInviteX.sendMessage(msg);
						PlayerInviteY.sendMessage(msg);
						recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
						tempo = getTempo();
						sh.cancelTask(contador);
						return;
						}
					if (DuelManager.getInstance().getDuelando().contains(PlayerInviteX) &&
							DuelManager.getInstance().getDuelando().contains(PlayerInviteY)) {
						recuseOrExpireInvite(PlayerInviteX, PlayerInviteY);
						tempo = getTempo();
						sh.cancelTask(contador);
						return;
					}
					if (tempo <= 0) { 
						String msg = "§c[Duelo] O tempo para que o pedido de duelo fosse respondido foi excedido.";
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
	
	public boolean getProtectionStatus() {
		return this.protection;
	}
	
	public void setProtection(boolean trueOrFalse) {
		this.protection = trueOrFalse;
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
