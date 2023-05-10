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
import me.zsnow.redestone.manager.InviteManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import me.zsnow.redestone.manager.SumoInviteManager;
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
import org.bukkit.potion.PotionEffect;
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
		SumoDuelManager sumoManager = SumoDuelManager.getInstance();	
	if (sumoManager.getDuelando().contains(morto) && sumoManager.getDuelando().contains(assassino)) {
		if (sumoManager.duelandoHash.get(assassino).equals(morto)) {
			
			String preffix = VaultHook.getPlayerPrefix(assassino.getName()) == null ? "" : VaultHook.getPlayerPrefix(assassino.getName());
			String killerAccuracy = sumoManager.getPercentage(getDataInfo(assassino).getHits(), getDataInfo(assassino).getWrongHits()); 
			String deadAccuracy = sumoManager.getPercentage(getDataInfo(morto).getHits(), getDataInfo(morto).getWrongHits()); 
			
			deleteDataSave(morto);
			e.getDrops().clear();
			LocationAPI.getLocation().teleportTo(morto, location.SAIDA);
			morto.chat("/on");
			
			/*
			 * mensagem para o perdedor
			 */
			
			sendFinalMessage(preffix, morto, assassino, morto, killerAccuracy, deadAccuracy);
			
			/*
			 * 
			 */
			
			assassino.sendTitle("§a§lVITORIA", "§fVocê venceu §7" + morto.getName());
			
		MenuListeners menuSelection = new MenuListeners();
		String kb = menuSelection.getKbTpe(getDataInfo(assassino).getKB()); 
        String pot = menuSelection.getPotType(getDataInfo(assassino).getPotLvl()); 
        String arena = menuSelection.getArenaType(getDataInfo(assassino).getArena());
        
		/*
		 *  broadcast vitoria
		 */
        
			for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-sumo-broadcast")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
						.replace("$vencedor", assassino.getName()).replace("$perdedor", morto.getName()).replace("$kb", kb).replace("$pot", pot).replace("$arena", arena));
			}
			
		/*
		 *  delay mensagem e saida 
		 */
			
			(new BukkitRunnable() {
				@Override
				public void run() {
					deleteDataSave(assassino);
					LocationAPI.getLocation().teleportTo(assassino, location.SAIDA);
				//	duel.paymentToWin(assassino);
					assassino.chat("/on");

					sendFinalMessage(preffix, assassino, assassino, morto, killerAccuracy, deadAccuracy);
					
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
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		final Player p = e.getPlayer();
		SumoDuelManager sumo = SumoDuelManager.getInstance();
		 if(p.getWorld().getName().equals("PlotMe")) return;
		Location step = p.getLocation().add(0.0D, -1.0D, 0.0D);
		if (e.getFrom().getX() != e.getTo().getX() && e.getFrom().getZ() != e.getTo().getZ() && getDataInfo(p) != null) {
		
			
			if ((getDataInfo(p).getUnmove() == true)) {
				p.teleport(e.getFrom());
				return;
			}
			
			
			if ((step.getBlock() != null && (step.getBlock().getType() == Material.WATER || step.getBlock().getType() == Material.STATIONARY_WATER))
			        && getDataInfo(p).getMagicWaterEffect() == true) {
				
			if (sumo.getDuelando().contains(p)) {
				
				final Player vencedor = sumo.duelandoHash.get(p);
				
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				
				
				String preffix = VaultHook.getPlayerPrefix(vencedor.getName()) == null ? "" : VaultHook.getPlayerPrefix(vencedor.getName());
				String killerAccuracy = sumo.getPercentage(getDataInfo(vencedor).getHits(), getDataInfo(vencedor).getWrongHits()); 
				String deadAccuracy = sumo.getPercentage(getDataInfo(p).getHits(), getDataInfo(p).getWrongHits()); 
				
				/*
				 * mensagem para o perdedor
				 */
				
				sendFinalMessage(preffix, p, vencedor, p, killerAccuracy, deadAccuracy);
				
				/*
				 * 
				 */
				
				deleteDataSave(p);
				LocationAPI.getLocation().teleportTo(p, location.SAIDA);
				p.chat("/on");
				
				vencedor.sendTitle("§a§lVITORIA", "§fVocê venceu §7" + p.getName());
				
			MenuListeners menuSelection = new MenuListeners();
			String kb = menuSelection.getKbTpe(getDataInfo(vencedor).getKB()); 
	        String pot = menuSelection.getPotType(getDataInfo(vencedor).getPotLvl()); 
	        String arena = menuSelection.getArenaType(getDataInfo(vencedor).getArena());
	        
	        
			/*
			 *  broadcast vitoria
			 */
	        
				for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-sumo-broadcast")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
							.replace("$vencedor", vencedor.getName()).replace("$perdedor", p.getName()).replace("$kb", kb).replace("$pot", pot).replace("$arena", arena));
				}
				
			/*
			 *  delay mensagem e saida 
			 */
				
				(new BukkitRunnable() {
					@Override
					public void run() {
						deleteDataSave(vencedor);
						LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
					//	duel.paymentToWin(assassino);
						vencedor.chat("/on");
						
						sendFinalMessage(preffix, vencedor, vencedor, p, killerAccuracy, deadAccuracy);
						
						cancel();
					}
				}).runTaskTimer(Main.getInstance(), 2*20L, 2*20);
				return;
				}
			}
		}
	}
		
	public void sendFinalMessage(String preffix, Player recebe_msg, Player vencedor, Player perdedor_name, String killerAccuracy, String deadAccuracy) {
		recebe_msg.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
		recebe_msg.sendMessage(" ");
		recebe_msg.sendMessage("                                  §6§lSumo Duelo");
		recebe_msg.sendMessage("             §f" + preffix + " " + vencedor.getName() + "§e§l VENCEDOR!  §7" + perdedor_name.getName());
		recebe_msg.sendMessage("               " + killerAccuracy + " §7- §f§lApuração de ataques §7- " + deadAccuracy);
		recebe_msg.sendMessage(" ");
		recebe_msg.sendMessage("§A§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
	}
	
	
	public void deleteDataSave(Player target) {
        UUID uuid = target.getUniqueId();
        SumoDuelManager sumo = SumoDuelManager.getInstance();
        sumo.duelandoHash.remove(target);
        sumo.getDuelando().remove(target);
       // SumoDuelManager jogadorInfo = SumoDuelManager.playerData.get(uuid);
       //     SumoDuelManager.playerData.put(uuid, jogadorInfo);
        	target.getInventory().clear();
			for (PotionEffect AllPotionEffects : target.getActivePotionEffects()) {
				target.removePotionEffect(AllPotionEffects.getType());
			}
            SumoDuelManager.playerData.remove(uuid);
        	SimpleclansAPI.getAPI().disableClanDamage(target);
        	if (SumoInviteManager.getInstance().savedTimers.get(target) != null) {
        		Bukkit.getScheduler().cancelTask(SumoInviteManager.getInstance().savedTimers.get(target));
        		SumoInviteManager.getInstance().savedTimers.remove(target);
        	}
	}
	
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
    	SumoDuelManager sumo = SumoDuelManager.getInstance();
    	DuelManager duel = DuelManager.getInstance();
    	Player player = event.getPlayer();
        if (event.isCancelled() && 
        		(sumo.getDuelando().contains(player) || duel.getDuelando().contains(player))
        		&& (SumoInviteManager.getInstance().getProtectionStatus() == true || InviteManager.getInstance().getProtectionStatus() == true)) {
        	    
        		if (sumo.getDuelando().contains(player)) {
        			
        			final Player dupla = sumo.duelandoHash.get(player);
    				
        			deleteDataSave(player);
        			deleteDataSave(dupla);
    				
    				LocationAPI.getLocation().teleportTo(player, location.SAIDA);
    				player.chat("/on");
    				
    				deleteDataSave(player);
					LocationAPI.getLocation().teleportTo(dupla, location.SAIDA);
					dupla.chat("/on");
					
					deleteDataSave(dupla);
					player.sendMessage("§c[ERRO: 770] Ocorreu um erro inesperado e o início dos duelos foram cancelados.");
					dupla.sendMessage("§c[ERRO: 770] Ocorreu um erro inesperado e o início dos duelos foram cancelados.");
        			return;
        		}
        		if (duel.getDuelando().contains(player)) {
        			
        			final Player dupla = duel.duelandoHash.get(player);
        			
        			remover(player, dupla);

        			LocationAPI.getLocation().teleportTo(player, location.SAIDA);
					player.chat("/on");
					LocationAPI.getLocation().teleportTo(dupla, location.SAIDA);
					dupla.chat("/on");
					player.sendMessage("§c[ERRO: 771] Ocorreu um erro inesperado e o início dos duelos foram cancelados.");
					dupla.sendMessage("§c[ERRO: 771] Ocorreu um erro inesperado e o início dos duelos foram cancelados.");
        			
        			return;
        		}
        	
        }
    }
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		DuelManager duel = DuelManager.getInstance();
		SumoDuelManager sumo = SumoDuelManager.getInstance();
		DuelCache cache = DuelCache.getCache();
		final Player morto = e.getPlayer();
		if (duel.getDuelando().contains(e.getPlayer())) {
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
				return;
			}
		if (sumo.getDuelando().contains(morto)) {
			final Player assassino = sumo.getMortoBy(morto);
			if (sumo.getDuelando().contains(assassino) && sumo.duelandoHash.get(assassino).equals(morto)) {

					morto.getInventory().clear();
					morto.getInventory().setArmorContents(null);
					
					
					String preffix = VaultHook.getPlayerPrefix(assassino.getName()) == null ? "" : VaultHook.getPlayerPrefix(assassino.getName());
					String killerAccuracy = sumo.getPercentage(getDataInfo(assassino).getHits(), getDataInfo(assassino).getWrongHits()); 
					String deadAccuracy = sumo.getPercentage(getDataInfo(morto).getHits(), getDataInfo(morto).getWrongHits()); 
					
					deleteDataSave(morto);
					LocationAPI.getLocation().teleportTo(morto, location.SAIDA);
					morto.chat("/on");
					
					/*
					 * mensagem para o perdedor
					 */
					
					sendFinalMessage(preffix, morto, assassino, morto, killerAccuracy, deadAccuracy);
					
					/*
					 * 
					 */
					
					assassino.sendTitle("§a§lVITORIA", "§fVocê venceu §7" + morto.getName());
					
				MenuListeners menuSelection = new MenuListeners();
				String kb = menuSelection.getKbTpe(getDataInfo(assassino).getKB()); 
		        String pot = menuSelection.getPotType(getDataInfo(assassino).getPotLvl()); 
		        String arena = menuSelection.getArenaType(getDataInfo(assassino).getArena());
		        
		        
				/*
				 *  broadcast vitoria
				 */
		        
					for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("venceu-sumo-broadcast")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg)
								.replace("$vencedor", assassino.getName()).replace("$perdedor", morto.getName()).replace("$kb", kb).replace("$pot", pot).replace("$arena", arena));
					}
					
				/*
				 *  delay mensagem e saida 
				 */
					(new BukkitRunnable() {
						@Override
						public void run() {
							deleteDataSave(assassino);
							LocationAPI.getLocation().teleportTo(assassino, location.SAIDA);
						//	duel.paymentToWin(assassino);
							assassino.chat("/on");
							
							sendFinalMessage(preffix, assassino, assassino, morto, killerAccuracy, deadAccuracy);
							
							cancel();
						}
					}).runTaskTimer(Main.getInstance(), 2*20L, 2*20);
					return;
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
	     SumoDuelManager sumoManager = SumoDuelManager.getInstance();	
	     if (e.getDamager() instanceof Player) {
	    	 final Player damager = (Player) e.getDamager();
		    	if (e.getEntity() instanceof Player && sumoManager.getDuelando().contains(damager) && 
		    			getDataInfo(damager).getMagicWaterEffect() == true) {
			        	 getDataInfo(damager).computeHits();
			        	 getDataInfo(damager).unComputeWrongHits();
			             return;
    		 }
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
	 
	 @EventHandler
	 public void interaction(PlayerInteractEvent e) {
		 SumoDuelManager sumoManager = SumoDuelManager.getInstance();	
		  Player p = e.getPlayer();
		 Action click  = e.getAction();
		 
		 // Se getMagicWaterEffect == true então o PvP tá on.
		
		 if (sumoManager.getDuelando().contains(e.getPlayer()) && 
			 (click == Action.LEFT_CLICK_AIR || click == Action.LEFT_CLICK_BLOCK) && getDataInfo(p).getMagicWaterEffect() == true) {
		 		getDataInfo(p).computeWrongHits();
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
	    	if (DuelManager.getInstance().getDuelando().contains(p) || SumoDuelManager.getInstance().getDuelando().contains(p)) e.setCancelled(true);
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
		SimpleclansAPI.getAPI().disableClanDamage(p1);
		SimpleclansAPI.getAPI().disableClanDamage(p2);
		duel.duelandoHash.remove(p1);
		duel.duelandoHash.remove(p2);
	duel.getDuelando().remove(p1);
	duel.getDuelando().remove(p2);
	}
     
	public void showOnExit(Player player1, Player player2) {
		for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
			duelandoPlayers.showPlayer(player1);
			duelandoPlayers.showPlayer(player2);
		}
	}
	
	   public SumoDuelManager getDataInfo(Player jogador) {
	        UUID uuid = jogador.getUniqueId();
	        SumoDuelManager jogadorInfo = SumoDuelManager.playerData.get(uuid);
	        if (jogadorInfo == null) {
	            jogadorInfo = new SumoDuelManager();
	            SumoDuelManager.playerData.put(uuid, jogadorInfo);
	        }
	        return jogadorInfo;
	    }
}
