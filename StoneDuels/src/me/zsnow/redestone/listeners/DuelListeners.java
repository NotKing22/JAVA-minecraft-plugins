package me.zsnow.redestone.listeners;

import me.zsnow.redestone.Main;
import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.api.SimpleclansAPI;
import me.zsnow.redestone.api.VaultHook;
import me.zsnow.redestone.cache.DuelCache;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.itemmethods.InventoryUtils;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import net.md_5.bungee.api.ChatColor;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelListeners implements Listener {
	
		// ADICIONAR EVENTO DE QND TOCAR NA AGUA, DPS QUE O EVENTO ACABA VC N MORRE MAIS NA AGUA
		// TALVEZ POSSA CHECAR ISSO QND VC TIRA O PLAYER DO DUELANDOHASH, VERIFICA SE TA SEM DUPLA SEI LA
	
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
			DuelManager duel = DuelManager.getInstance();
			final Player morto = e.getEntity();
			final Player assassino = e.getEntity().getKiller();
			if (duel.getDuelando().contains(morto) && duel.getDuelando().contains(assassino)) {
				if (duel.duelandoHash.get(assassino).equals(morto)) {
					remover(morto, assassino);
					Inventory armazem = Bukkit.createInventory(null, 6*9, morto.getName());
					for (ItemStack itemDropado : e.getDrops()) {
						if (itemDropado != null && itemDropado != new ItemStack(Material.AIR)) {
							armazem.addItem(itemDropado);
						}
					}
					e.getDrops().clear();
					DuelCache cache = DuelCache.getCache();
					try {
						cache.setPlayerArmazem(assassino.getName(), InventoryUtils.toBase64(armazem));
						armazem.clear();
					} catch (IllegalStateException e1) {
						e1.printStackTrace();
						assassino.sendMessage("§c[Duelo] Houve um erro ao salvar seu armazém. Entre em contato com um Administrador!");
					}
					LocationAPI.getLocation().teleportTo(morto, location.SAIDA);
					assassino.sendTitle("§a§lVITORIA", "§fVocê matou: §7" + morto.getName());
					assassino.sendMessage("§aOs itens de " + morto.getName() + " §aforam para seu armazém.");
					DuelCache.getCache().addVitoriasTo(assassino.getName());
					DuelCache.getCache().addDerrotasTo(morto.getName());
					morto.chat("/on");
					for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-broadcast")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
								.replace("$vencedor", assassino.getName())
								.replace("$perdedor", morto.getName()));
					}
					(new BukkitRunnable() {
						@Override
						public void run() {
							LocationAPI.getLocation().teleportTo(assassino, location.SAIDA);
							duel.paymentToWin(assassino);
							assassino.chat("/on");
							cancel();
						}
					}).runTaskTimer(Main.getInstance(), 3*20L, 3*20);
				} else {
					LocationAPI.getLocation().teleportTo(morto, location.POS1);
					return;
				}
			}
			
			// SUMO CODE
			
	if (duel.getDuelandoSumo().contains(morto) && duel.getDuelandoSumo().contains(assassino)) {
		if (duel.duelandoHash.get(assassino).equals(morto)) {
			remover(morto, assassino);
			e.getDrops().clear();
			LocationAPI.getLocation().teleportTo(morto, location.SAIDA);
			morto.chat("/on");
			
			String preffix = VaultHook.getPlayerPrefix(assassino.getName()) == null ? "" : VaultHook.getPlayerPrefix(assassino.getName());
			String killerAccuracy = getPercentage(50, 5); // mudar valores e pegar do getdatainfo
			String deadAccuracy = getPercentage(10, 13); // mudar valores
			
			/*
			 * mensagem para o perdedor
			 */
			
			morto.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			morto.sendMessage(" ");
			morto.sendMessage("                                  §6§lSumo Duelo");
			morto.sendMessage("             §f" + preffix + " " + assassino.getName() + "§e§l VENCEDOR!  §7" + morto.getName());
			morto.sendMessage("                " + killerAccuracy + " §7- §f§lApuração de ataques §7- " + deadAccuracy);
			morto.sendMessage(" ");
			morto.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
			
			/*
			 * 
			 */
			
			assassino.sendTitle("§a§lVITORIA", "§fVocê venceu §7" + morto.getName());
			assassino.setAllowFlight(true);
			assassino.setFlying(true);
			
			
		MenuListeners menuSelection = new MenuListeners();
		String kb = menuSelection.getKbTpe(getDataInfo(assassino).getKB()); 
        String pot = menuSelection.getPotType(getDataInfo(assassino).getPotLvl()); 
        String arena = menuSelection.getArenaType(getDataInfo(assassino).getArena());
        
        
		/*
		 *  broadcast vitoria
		 */
        
			for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-sumo-broadcast")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
						.replace("$vencedor", assassino.getName())
						.replace("$perdedor", morto.getName())
                        .replace("$kb", kb)
                        .replace("$pot", pot)
                        .replace("$arena", arena));
			}
			
		/*
		 *  delay mensagem e saida 
		 */
			
			(new BukkitRunnable() {
				@Override
				public void run() {
					LocationAPI.getLocation().teleportTo(assassino, location.SAIDA);
				//	duel.paymentToWin(assassino);
					assassino.chat("/on");
					assassino.setAllowFlight(false);
					assassino.setFlying(false);
					assassino.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
					assassino.sendMessage(" ");
					assassino.sendMessage("                                  §6§lSumo Duelo");
					assassino.sendMessage("             §f" + preffix + " " + assassino.getName() + "§e§l VENCEDOR!  §7" + morto.getName());
					assassino.sendMessage("                " + killerAccuracy + " §7- §f§lApuração de ataques §7- " + deadAccuracy);
					assassino.sendMessage(" ");
					assassino.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
					cancel();
				}
			}).runTaskTimer(Main.getInstance(), 2*20L, 2*20);
			return;
		}
	LocationAPI.getLocation().teleportTo(morto, location.POS1);
	return;
	}
}
}
	
	public String getPercentage(int Ataques, int Ataques_errados) {
	    double porcentagem = ((double) Ataques / (Ataques + Ataques_errados)) * 100;
	    
	    if (porcentagem < 30) {
	        return "§c" + String.format("%.1f", porcentagem) + "§c%";
	    } else if (porcentagem < 50) {
	        return "§e" + String.format("%.1f", porcentagem) + "§e%";
	    } else {
	        return "§a" + String.format("%.1f", porcentagem) + "§a%";
	    }
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		DuelManager duel = DuelManager.getInstance();
		DuelCache cache = DuelCache.getCache();
		if (duel.getDuelando().contains(e.getPlayer())) {
			final Player morto = e.getPlayer();
			final Player assassino = duel.getMortoBy(morto);
				if (duel.getDuelando().contains(assassino) && duel.duelandoHash.get(assassino).equals(morto)) {
					remover(morto, assassino);
					Inventory armazem = Bukkit.createInventory(null, 6*9, morto.getName());
					for (ItemStack itemDropado : morto.getInventory().getContents()) {
						if (itemDropado != null && itemDropado != new ItemStack(Material.AIR)) {
							armazem.addItem(itemDropado);
						}
					}
					for (ItemStack armorDropado : morto.getInventory().getArmorContents()) {
						if (armorDropado != null && armorDropado != new ItemStack(Material.AIR)) {
							armazem.addItem(armorDropado);
						}
					}
					morto.getInventory().clear();
					morto.getInventory().setArmorContents(null);
					try {
						cache.setPlayerArmazem(assassino.getName(), InventoryUtils.toBase64(armazem));
						armazem.clear();
					} catch (IllegalStateException e1) {
						e1.printStackTrace();
						assassino.sendMessage("§c[Duelo] Houve um erro ao salvar seu armazém. Entre em contato com um Administrador!");
					}
					LocationAPI.getLocation().teleportTo(morto, location.SAIDA);
					assassino.sendTitle("§a§lVITORIA", "§fVocê matou: §7" + morto.getName());
					assassino.sendMessage("§aOs itens de " + morto.getName() + " §aforam para seu armazém.");
					DuelCache.getCache().addVitoriasTo(assassino.getName());
					DuelCache.getCache().addDerrotasTo(morto.getName());
					morto.chat("/on");
					for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-broadcast")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
								.replace("$vencedor", assassino.getName())
								.replace("$perdedor", morto.getName()));
					}
					(new BukkitRunnable() {
						@Override
						public void run() {
							LocationAPI.getLocation().teleportTo(assassino, location.SAIDA);
							duel.paymentToWin(assassino);
							assassino.chat("/on");
							cancel();
						}
					}).runTaskTimer(Main.getInstance(), 3*20L, 3*20);
				}
			}
		}
	
	 @EventHandler
	    public void onRespawn(PlayerRespawnEvent event) {
	         Player player = event.getPlayer();
	         if (DuelManager.getInstance().getDuelando().contains(player)) {
	        	String location = "POS1";
	     		double X = Configs.locations.getConfig().getDouble(location + ".X");
	            double Y = Configs.locations.getConfig().getDouble(location + ".Y");
	            double Z = Configs.locations.getConfig().getDouble(location + ".Z");
	            float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
	            float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	    	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
	        	 event.setRespawnLocation(loc);
	         }
	    }
	 
	 @EventHandler
	 public void onEntityDamage(EntityDamageByEntityEvent e) {
	     DuelManager duel = DuelManager.getInstance();
	     final Player damager = (Player) e.getDamager();
	     if (damager instanceof Player && duel.getDuelandoSumo().contains(damager)) {
	         if (e.getEntity() instanceof LivingEntity) {
	        	 getDataInfo(damager).computeHits();
	        	 getDataInfo(damager).unComputeWrongHits();
	             Bukkit.broadcastMessage(" ta hitado chefe " + getDataInfo(damager).getHits());
	             Bukkit.broadcastMessage("apuraçao " + getPercentage(getDataInfo(damager).getHits(), getDataInfo(damager).getWrongHits()));
	             return;
	     }
	     
	     if (e.getEntity() instanceof Player && duel.getDuelando().contains(e.getEntity()) && e.getCause() == DamageCause.PROJECTILE) {
	         e.setCancelled(true);
	         if (e.getDamager() instanceof Arrow) {
	             Arrow arrow = ((Arrow) e.getDamager());
	             Player shooter = (Player) arrow.getShooter();
	             shooter.sendMessage("§cVocê não pode atirar projéteis durante os duelos.");
	         }
	     }
	     }
	 }
	 
	 @EventHandler
	 public void interaction(PlayerInteractEvent e) {
		 DuelManager duel = DuelManager.getInstance();
		  Player p = e.getPlayer();
		 Action click  = e.getAction();
		 
		 // VERIFICAR SE O TIMER DO PVP TA LIBERADO
		 if (duel.getDuelandoSumo().contains(e.getPlayer()) &&
				 (click == Action.LEFT_CLICK_AIR || click == Action.LEFT_CLICK_BLOCK)) {
			 
					 getDataInfo(p).computeWrongHits();
		             Bukkit.broadcastMessage(" errou feiao mane " + getDataInfo(p).getWrongHits());
		             Bukkit.broadcastMessage("apuraçao " + getPercentage(getDataInfo(p).getHits(), getDataInfo(p).getWrongHits()));
         }
	 }
	
	    @EventHandler
	    public void PotionsSplash(PotionSplashEvent e){
	        if(e.getEntity().getShooter() instanceof Player) {
	        	for (LivingEntity affectedEntities : e.getAffectedEntities()) {
	        		final Player shooter = (Player) e.getEntity().getShooter();
	        		DuelManager duel = DuelManager.getInstance();
	        		if (affectedEntities instanceof Player) {
	        			if (duel.getDuelando().contains(shooter) && duel.getDuelando().contains(affectedEntities)) {
	        				e.setCancelled(true);
	        				shooter.sendMessage("§cVocê não pode arremessar poções durante o duelo. Utilize apenas poções bebíveis.");
	        				return;
	        				}
	        			}
	        		}
	        	}
	        }
	    
	    @EventHandler
	    public void onDrop(PlayerDropItemEvent e) {
	    	Player p = e.getPlayer();
	    	if (DuelManager.getInstance().getDuelando().contains(p)) e.setCancelled(true);
	    }
	    
	    @EventHandler
	    public void onCollect(PlayerPickupItemEvent e) {
	    	Player p = e.getPlayer();
	    	if (DuelManager.getInstance().getDuelando().contains(p)) e.setCancelled(true);
	    }
    
    @EventHandler
	  public void blockCMD(PlayerCommandPreprocessEvent e) {
		  Player p = e.getPlayer();
		  if (DuelManager.getInstance().getDuelando().contains(p)) {
			  for (String cmd : Configs.config.getStringList("comandos-bloqueados")) {
				  if (e.getMessage().startsWith("/" + cmd) || e.getMessage().startsWith("duelo")) {
					  e.setCancelled(true);
					  p.sendMessage("§cVocê não pode digitar este comando enquanto está duelando.");
				  }
			  }
		  }
	  }
	    
	public void remover(Player p1, Player p2) {
		DuelManager duel = DuelManager.getInstance();
		duel.duelandoHash.remove(p1, p2);
		duel.duelandoHash.remove(p2, p1);
	duel.getDuelando().remove(p1);
	duel.getDuelando().remove(p2);
	SimpleclansAPI.getAPI().disableClanDamage(p1);
	SimpleclansAPI.getAPI().disableClanDamage(p2);
	}
	
	public void showOnExit(Player player1, Player player2) {
		for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
			duelandoPlayers.showPlayer(player1);
			duelandoPlayers.showPlayer(player2);
		}
	}
	
	   private SumoDuelManager getDataInfo(Player jogador) {
	        UUID uuid = jogador.getUniqueId();
	        SumoDuelManager jogadorInfo = SumoDuelManager.playerData.get(uuid);
	        if (jogadorInfo == null) {
	            jogadorInfo = new SumoDuelManager();
	            SumoDuelManager.playerData.put(uuid, jogadorInfo);
	        }
	        return jogadorInfo;
	    }
}
