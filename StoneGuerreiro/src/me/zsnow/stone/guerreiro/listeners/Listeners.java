package me.zsnow.stone.guerreiro.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import me.zsnow.stone.guerreiro.manager.Configs;
import me.zsnow.stone.guerreiro.manager.EventManager;

public class Listeners implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getEntity().getKiller() instanceof Player) {
				Player assasino = e.getEntity().getKiller();
				Player vitima = e.getEntity();
				if (EventManager.manager.getParticipantes().contains(vitima)) {
					for (Player jogadores : EventManager.manager.getParticipantes()) {
						jogadores.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("jogador-eliminado")
							.replace("{player_death}", vitima.getName())
							.replace("{player_murder}", assasino.getName())));
							
					}
					EventManager.manager.getParticipantes().remove(vitima);
					EventManager.manager.sendTo(vitima, "saida");
					assasino.playSound(vitima.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
					ItemStack cap = new ItemStack(Material.GOLDEN_APPLE, 2);
					assasino.getInventory().addItem(cap);
					defineWinner();
					EventManager.manager.disableClanDamage(vitima);
				if (Configs.config.getConfig().getBoolean("limpar-drops") == true) {
					e.getDrops().clear();
					}
				}
			} else {
				Player vitima = e.getEntity();
				if (EventManager.manager.getParticipantes().contains(vitima)) {
					for (Player jogadores : EventManager.manager.getParticipantes()) {
						jogadores.sendMessage("§a[Guerreiro] §f" + vitima.getName() + "§7 morreu sozinho.");
					}
					EventManager.manager.getParticipantes().remove(vitima);
					EventManager.manager.disableClanDamage(vitima);
					if (Configs.config.getConfig().getBoolean("limpar-drops") == true) {
						e.getDrops().clear();
					}
					EventManager.manager.sendTo(vitima, "SAIDA");
				}
				defineWinner();
			}
		}
	/*	if (EventManager.manager.getParticipantes().contains(e.getEntity())) {
			
			EventManager.manager.getParticipantes().remove(e.getEntity());
			EventManager.manager.sendTo(e.getEntity(), "saida");
			Player p = e.getEntity();
			EventManager.manager.sendTo(p, "saida");
			if (Configs.config.getConfig().getBoolean("limpar-drops") == true) {
				e.getDrops().clear();
			}
			for (Player jogadores : EventManager.manager.getParticipantes()) {
				jogadores.sendMessage("§5[Mata-Mata] §f" + p.getName() + "§7 morreu sozinhoA.");
			}
			defineWinner();
		}*/
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (EventManager.manager.getParticipantes().contains(p)) {
			for (Player jogadores : EventManager.manager.getParticipantes()) {
				jogadores.sendMessage("§a[Guerreiro] §f" + p.getName() + "§7 desconectou do evento.");
			}
			EventManager.manager.getParticipantes().remove(p);
			EventManager.manager.sendTo(p, "saida");
			for (PotionEffect allPotionEffects : p.getActivePotionEffects()) {
				p.removePotionEffect(allPotionEffects.getType());
			}
			p.getInventory().clear();	
      		p.getInventory().setArmorContents(null);
      		defineWinner();
      		EventManager.manager.disableClanDamage(p);
		}
	}
	
	 @EventHandler
	 public void itemDrop(PlayerDropItemEvent e) {
		 Player p = e.getPlayer();
		 if (EventManager.manager.getParticipantes().contains(p)) {
			 if (Configs.config.getConfig().getBoolean("desativar-dropar-item") == true) {
				 e.setCancelled(true);
			 }
		 }
	 }
	 
	 @EventHandler
	 public void onHit(EntityDamageByEntityEvent e) {
		 //Player p = (Player)e.getDamager();
		 if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			 if (EventManager.manager.getParticipantes().contains(e.getEntity()) 
				&& EventManager.manager.getParticipantes().contains(e.getDamager())) {
				 if (EventManager.manager.getPvPStatus() == false) {
					 e.getDamager().sendMessage("§cO pvp está desativado.");
					 e.setCancelled(true);
				 }
			 }
		 }
	 }
	 
	 public void defineWinner() {
		 if (EventManager.manager.getParticipantes().size() == 1 && EventManager.manager.getEntradaLiberada() == false) {
			 Player vencedor = EventManager.manager.getParticipantes().get(0);
			 for (String msg : Configs.config.getConfig().getStringList("broadcast.end-event")) {
				 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{vencedor}", vencedor.getName())));
			 }
			 for (String cmd : Configs.config.getConfig().getStringList("premio")) {
				 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{vencedor}", vencedor.getName()));
		 	 }
			 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
				 vencedor.removePotionEffect(allPotionEffects.getType());
			 }
			 vencedor.getInventory().setArmorContents(null);
			 vencedor.getInventory().clear();
			 EventManager.manager.disableClanDamage(vencedor);
			 EventManager.manager.getParticipantes().clear();
			  EventManager.manager.setPvPStatus(false);
			  EventManager.manager.setEntradaLiberada(false);
			  EventManager.manager.setEventoOcorrendo(false);
			 EventManager.manager.sendTo(vencedor, "Saida");
		 }
	 }
	 
	 
/*ERRO	 @EventHandler
	 public void onTeleport(PlayerTeleportEvent e) {
    	Player p = e.getPlayer();
		if (EventManager.manager.getParticipantes().contains(p) && !p.hasPermission("zs.admin")) {
    		p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
    		e.setCancelled(true);
    		if (EventManager.manager.getEventoOcorrendo() == false) {
    			EventManager.manager.sendTo(p, "saida");
    			EventManager.manager.getParticipantes().remove(p);
    		}
    	}
    }	*/
	 
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (EventManager.manager.getEventoOcorrendo() == true && EventManager.manager.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
			EventManager.manager.sendTo(p, "Entrada");
			p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
		}
	 }
	 
}
