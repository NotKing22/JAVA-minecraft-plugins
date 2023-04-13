package me.zsnow.redestone.listeners;

import me.zsnow.redestone.Main;
import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.api.SimpleclansAPI;
import me.zsnow.redestone.cache.DuelCache;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.itemmethods.InventoryUtils;
import me.zsnow.redestone.manager.DuelManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class DuelListeners implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
			DuelManager duel = DuelManager.getInstance();
			DuelCache cache = DuelCache.getCache();
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
			} else {
				LocationAPI.getLocation().teleportTo(morto, location.POS1);
				return;
			}
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
	    	if(e.getEntity() instanceof Player && duel.getDuelando().contains(e.getEntity()) && e.getCause() == DamageCause.PROJECTILE) {
				 e.setCancelled(true);
				 if (e.getDamager() instanceof Arrow) {
					    Arrow arrow = ((Arrow) e.getDamager());
					    Player shooter = (Player) arrow.getShooter();
					    shooter.sendMessage("§cVocê não pode atirar projéteis durante os duelos.");
					}
				 return;
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
}
