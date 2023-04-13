package me.zsnow.round6;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;

import me.zsnow.round6.api.LocationAPI;
import me.zsnow.round6.api.StringReplaceAPI;
import me.zsnow.round6.api.LocationAPI.location;
import me.zsnow.round6.configs.Configs;
import me.zsnow.round6.manager.PedestreClass;
import me.zsnow.round6.manager.SemaforoClass;
import me.zsnow.round6.manager.SemaforoClass.sinal;

public class Listeners implements Listener {
	
	PedestreClass pedestres = PedestreClass.getInstance();
	SemaforoClass semaforo = SemaforoClass.getSemaforo();
		 
	@EventHandler
	public void playerMove(PlayerMoveEvent event) {
		if (semaforo.getEventoStatus() == true && semaforo.getEntradaStatus() == false && pedestres.getPedestres().contains(event.getPlayer())) {
			Location step = event.getPlayer().getLocation().add(0.0D, -1.0D, 0.0D);
			if (semaforo.getSinalCor(sinal.VERMELHO) && (step.getBlock().getType() == Material.SAND) && ((event.getFrom().getX() != event.getTo().getX()) && (event.getFrom().getZ() != event.getTo().getZ()))) { 
				pedestres.getPedestres().remove(event.getPlayer());
				event.getPlayer().getInventory().clear();
				event.getPlayer().getInventory().setArmorContents(null);
				LocationAPI.getLocation().teleportTo(event.getPlayer(), location.SAIDA);
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.HORSE_DEATH, 1.0f, 0.5f);
				event.getPlayer().sendMessage(" ");
				event.getPlayer().sendMessage("§c§lVocê foi eliminado por andar no sinal vermelho.");
				event.getPlayer().sendMessage(" ");
				for (PotionEffect AllPotionEffects : event.getPlayer().getActivePotionEffects()) {
					event.getPlayer().removePotionEffect(AllPotionEffects.getType());
				}
				for (Player participantes : pedestres.getPedestres()) {
					participantes.playSound(participantes.getLocation(), Sound.EXPLODE, 1.0f, 0.5f);
					participantes.sendMessage("§6➜ §b§l" +event.getPlayer().getName()+ " §cnúmero §l" +pedestres.getPedestres().size()+ " §celiminado!");
					}
				}
				semaforo.eventCanKeepRunning();
			}
		}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void interactToWon(PlayerInteractEvent e) {
			if ((semaforo.getEventoStatus() == true) && (semaforo.getEntradaStatus() == false)) {
				if (pedestres.getPedestres().contains(e.getPlayer())) {
					if (Action.RIGHT_CLICK_BLOCK == e.getAction()) {
						if ((e.getClickedBlock().getType() == Material.SIGN_POST) || (e.getClickedBlock().getType() == Material.WALL_SIGN)) {
							Sign s = (Sign) e.getClickedBlock().getState();
							if (s.getLine(0).equalsIgnoreCase("§9[Evento]") || s.getLine(0).equalsIgnoreCase("[Evento]")) {
								if (SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
									e.getPlayer().sendMessage(" ");
									e.getPlayer().sendMessage("§cVocê não pode clicar na placa enquanto o sinal estiver vermelho... Aguarde.");
									e.getPlayer().sendMessage(" ");
									return;
								}
								//setar o ganhador
								 Player vencedor = e.getPlayer();
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
								 semaforo.setEventoStatus(false);
								 semaforo.setEntradaStatus(false);
								 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
								 semaforo.eventCanKeepRunning();
								 semaforo.resetData();
								
							}
						}
					}
				}
			}
		}
	
	  @EventHandler
	    public void invClick(InventoryClickEvent e)	{
			if (!(e.getWhoClicked() instanceof Player)) {
			      return;
			}
			 Player p = (Player) e.getWhoClicked();
			 if (pedestres.getPedestres().contains(p)) {
				 e.setCancelled(true);
			 }
	  }
	
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (semaforo.getEventoStatus() == true && pedestres.getPedestres().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
			LocationAPI.getLocation().teleportTo(p, location.SAIDA);
			p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
		}
	 }
	 
	 @EventHandler
	 public void itemDrop(PlayerDropItemEvent e) {
		 Player p = e.getPlayer();
		 if (pedestres.getPedestres().contains(p)) {
			 e.setCancelled(true);
		 }
	 }
	 
	 @EventHandler
   	 public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player vitima = e.getEntity();
			if (pedestres.getPedestres().contains(vitima)) {
				LocationAPI.getLocation().teleportTo(vitima, location.ENTRADA);
			}
		}
	}
	 
	 @EventHandler
	 public void onQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (pedestres.getPedestres().contains(p)) {
				p.getInventory().clear();	
	      		p.getInventory().setArmorContents(null);
				for (Player jogadores : pedestres.getPedestres()) {
					jogadores.sendMessage("§e" + p.getName() + "§e desconectou do evento.");
				}
				pedestres.getPedestres().remove(p);
				LocationAPI.getLocation().teleportTo(p, location.SAIDA);
				for (PotionEffect allPotionEffects : p.getActivePotionEffects()) {
					p.removePotionEffect(allPotionEffects.getType());
				}
				if (semaforo.getEventoStatus() == true && semaforo.getEntradaStatus() == false) {
					semaforo.eventCanKeepRunning();
				}
			}
		}
}

	
