package me.zsnow.stonebatataquente;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zsnow.stonebatataquente.api.LocationAPI;
import me.zsnow.stonebatataquente.api.StringReplaceAPI;
import me.zsnow.stonebatataquente.api.LocationAPI.location;
import me.zsnow.stonebatataquente.api.SimpleclansAPI;
import me.zsnow.stonebatataquente.configs.Configs;
import me.zsnow.stonebatataquente.manager.BatataController;
import me.zsnow.stonebatataquente.manager.EventController;
import net.md_5.bungee.api.ChatColor;

public class Listeners implements Listener {
	
	BatataController batata = BatataController.getInstance();
	EventController evento = EventController.getInstance();
	
	@EventHandler
	public void transferPotato(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			final Player atacante = (Player) e.getDamager();
			final Player atacado = (Player) e.getEntity();
			if (batata.getParticipantes().contains(atacante) && batata.getParticipantes().contains(atacado)) {
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					e.setDamage(0);
					atacado.setHealth(20);
					if (batata.isBatataMan(atacante)) {
						batata.setBatataMan(atacado);
						for (Player participantes : batata.getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-sorteada")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{batata_man}", batata.getBatataMan().getName())));
								}
							}
						atacante.getInventory().clear();
						atacante.getInventory().setArmorContents(null);
						}
					} else {
						e.setCancelled(true);
					}
				}
			}
		}
	
	@EventHandler
    public void onClick(InventoryClickEvent e){
    	 Player p = (Player) e.getWhoClicked();
         if(e.getSlotType().equals(SlotType.ARMOR) && (batata.getParticipantes().contains(p))){
            e.setCancelled(true);
        }
    }
	
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
    	Player p = e.getPlayer();
    	if (batata.getParticipantes().contains(p)) {
    		e.setCancelled(true);
    	}
    }
    
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
    	Player p = e.getEntity();
    	if (evento.getEventoStatus() == true && batata.getParticipantes().contains(p)) {
    		LocationAPI.getLocation().teleportTo(p, location.ENTRADA);
    		p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
    		p.getInventory().clear();
    	}
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
    	Player p = (Player) e.getPlayer();
		if (batata.getParticipantes().contains(p)) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			batata.getParticipantes().remove(p);
			SimpleclansAPI.getAPI().disableClanDamage(p);
			for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
				p.removePotionEffect(AllPotionEffects.getType());
			}
			LocationAPI.getLocation().teleportTo(p, location.SAIDA);
			if (batata.getBatataMan() != null && batata.getBatataMan().getName().equals(p.getName())) {
				for (Player participantes : batata.getParticipantes()) {
					participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.batata-saiu")));
				}
				batata.canStopEvent();
				batata.setBatataMan(null);
			}
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.saiu-do-evento")));
			p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("Som.ao-sair").toUpperCase()), 1.0F, 0.5F);
			batata.canStopEvent();
		}
    }
    
    @EventHandler
    public void onFeed(PlayerItemConsumeEvent e) {
    	Player p = e.getPlayer();
    	if (batata.getParticipantes().contains(p) && e.getItem().hasItemMeta() && e.getItem().getType().equals(Material.BAKED_POTATO)) {
    		p.sendMessage("§cVocê queimou a língua.");
    		e.setCancelled(true);
    	}
    }
    
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (evento.getEventoStatus() == true && batata.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
			LocationAPI.getLocation().teleportTo(p, location.ENTRADA);
			p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
		}
	 }
	
	
}

