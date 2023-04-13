package me.zsnow.stone.endergun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BlockIterator;

import me.zsnow.stone.endergun.api.LocationAPI.location;
import me.zsnow.stone.endergun.configs.Configs;
import me.zsnow.stone.endergun.api.LocationAPI;
import me.zsnow.stone.endergun.manager.EventController;
import me.zsnow.stone.endergun.manager.MembersController;

public class Listeners implements Listener {
	
	MembersController members = MembersController.getInstance();
	EventController evento = EventController.getInstance();
	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	public static HashMap<Player, Boolean> cooldownSnowball = new HashMap<>();
	public static HashMap<String, Long> AFK = new HashMap<>();
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onUse(PlayerInteractEvent event) {
		if (!event.getAction().name().contains("RIGHT")) return;
			if (!this.members.getParticipantes().contains(event.getPlayer())) return;
			final Player player = event.getPlayer();
			final ItemStack itemHand = player.getItemInHand();
				if (itemHand.getType() == Material.IRON_BARDING && itemHand.getItemMeta().hasDisplayName() && 
					itemHand.getItemMeta().getDisplayName().equals("§6⚔ §aArma do End §6⚔") && evento.getArmasLiberadas() == true && 
					    evento.getEventoStatus() == true && members.getParticipantes().contains(player)) {
				if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if (!cooldownSnowball.containsKey(player)) {
							cooldownSnowball.put(player, true);
						
							(new BukkitRunnable() {
								
								@Override
								public void run() {
									if (evento.getEventoStatus() == true && members.getParticipantes().contains(player)) {
										Snowball ball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
										ball.setShooter(player);
										ball.setVelocity(player.getLocation().getDirection().multiply(1.5));
										cooldownSnowball.remove(player, true);
											for (Entity nearby : getNearbyEntities(player.getLocation(), 5)) {
												if (nearby instanceof Player) {
													Player pNearby = (Player) nearby;
													pNearby.playSound(player.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
												}
											}
										cancel();
									} else {
										cooldownSnowball.remove(player, true);
										event.setCancelled(true);
										members.getParticipantes().remove(player);
										cancel();
									}
								}
							}).runTaskTimer(Main.getInstance(), 4L, 0L);
						} 
					}
				}
			}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onShoot(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Snowball) {
			final Snowball ball = (Snowball) event.getEntity();
			if (ball.getShooter() instanceof Player && members.getParticipantes().contains(ball.getShooter()) && evento.getEventoStatus() == true) {
				BlockIterator iterator = new BlockIterator(event.getEntity().getWorld(), event.getEntity().getLocation().toVector(), event.getEntity().getVelocity().normalize(), 0.0D, 4);
				Block hitBlock = null;
				while (iterator.hasNext()) {
					hitBlock = iterator.next();
					if (hitBlock.getTypeId() != 0) {
						break;
						}
					}
					if (hitBlock.getTypeId() != 80) { // ID DA NEVE
						return;
					}
					hitBlock.setType(Material.AIR);
				}
			}
		}
	
	 @EventHandler
	 public void itemDrop(PlayerDropItemEvent e) {
		 Player p = e.getPlayer();
		 if (members.getParticipantes().contains(p)) {
			 e.setCancelled(true);
		 }
	 }
	 
	 @EventHandler
   	 public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			final Player vitima = e.getEntity();
			if (members.getParticipantes().contains(vitima)) {
				LocationAPI.getLocation().teleportTo(vitima, location.LOBBY);
			}
		}
	}
	 
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (evento.getEventoStatus() == true && members.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
			LocationAPI.getLocation().teleportTo(p, location.LOBBY);
			p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
		}
	 }
	 
	 @EventHandler
	 public void onQuit(PlayerQuitEvent e) {
			Player p = e.getPlayer();
			if (members.getParticipantes().contains(p)) {
				p.getInventory().clear();	
	      		p.getInventory().setArmorContents(null);
				for (Player jogadores : members.getParticipantes()) {
					jogadores.sendMessage("§e" + p.getName() + "§e desconectou do evento.");
				}
				members.getParticipantes().remove(p);
				LocationAPI.getLocation().teleportTo(p, location.SAIDA);
				for (PotionEffect allPotionEffects : p.getActivePotionEffects()) {
					p.removePotionEffect(allPotionEffects.getType());
				}
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					evento.eventCanKeepRunning();
				}
			}
		}
	 
	 @SuppressWarnings("deprecation")
	@EventHandler
		public void onMove(PlayerMoveEvent e) {
			Player p = e.getPlayer();
			Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
			if ((step.getBlock().getType() == Material.WATER || (step.getBlock().getType() == Material.STATIONARY_WATER))) {
				if (members.getParticipantes().contains(p)) {
					if (evento.getArmasLiberadas() == true) {
						members.getParticipantes().remove(p);
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.sendTitle(" ", "§c§lVocê foi desclassificado §8(§a" + members.getParticipantes().size() + "§8/§c" + maxPlayers + "§8)");
						p.playSound(p.getLocation(), Sound.HORSE_DEATH, 1.0f, 0.5f);
						for (Player participantes : members.getParticipantes()) {
							participantes.sendMessage("§c➜ §e" + p.getName() + "§e foi eliminado! §8(§a" + members.getParticipantes().size() + "§8/§c" + maxPlayers + "§8)");
						}
						evento.eventCanKeepRunning();
					} else {
						LocationAPI.getLocation().teleportTo(p, location.LOBBY);
					}
				}
	 		}
		}
	 
	
	 ////////////////////////////
	 
	    @EventHandler
	    public void detectAfkPlayers(PlayerMoveEvent event){
	        Player player = event.getPlayer();
	        if (members.getParticipantes().contains(player) && evento.getArmasLiberadas() == true) {
	        	AFK.put(player.getName(), System.currentTimeMillis());
	        }
	    }
	    
	    public void registerAfkSystem() {
	    	for (Player jogadores : members.getParticipantes()) {
	    		AFK.put(jogadores.getName(), System.currentTimeMillis());
	    	}
	    }
	    
	    public void refresher(){
	        BukkitScheduler scheduler = Bukkit.getScheduler();
	        scheduler.scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
	            @SuppressWarnings("deprecation")
				@Override
	            public void run(){
	            	ArrayList<Player> lista = new ArrayList<>();
	            	lista.addAll(members.getParticipantes());
	            	
		            	for(Player all : lista){
		            		if (AFK.containsKey(all.getName())) {
			                    long time = AFK.get(all.getName());
			                    if((System.currentTimeMillis() - time) >= 20000){
			                    	members.getParticipantes().remove(all);
			                    	all.getInventory().clear();
			                    	all.getInventory().setArmorContents(null);
			                    	all.getWorld().spawnEntity(all.getLocation(), EntityType.FIREWORK);
									LocationAPI.getLocation().teleportTo(all, location.SAIDA);
									all.sendTitle(" ", "§c§lVocê foi desclassificado §8(§a" + members.getParticipantes().size() + "§8/§c" + maxPlayers + "§8)");
									all.playSound(all.getLocation(), Sound.HORSE_DEATH, 1.0f, 0.5f);
									for (Player participantes : members.getParticipantes()) {
										participantes.sendMessage("§c➜ §e" + all.getName() + "§e foi eliminado! §8(§a" + members.getParticipantes().size() + "§8/§c" + maxPlayers + "§8)");
									}
									for (Player staff : Bukkit.getOnlinePlayers()) {
										if (staff.hasPermission("zs.admin")) {
											staff.sendMessage("§7§o[EnderGun: " + all.getName() + "§7§o foi expulso. Motivo: AFK]");
										}
									}
									evento.eventCanKeepRunning();
		                    	}
		                    }
		                }
		            	lista.clear();
		            }
		        }, 0L, 20L);
		    }

	////////////////////////////
	    
	    
    public static Entity[]  getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
            for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
                for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                    int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                    for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                    }
                }
            }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

}
