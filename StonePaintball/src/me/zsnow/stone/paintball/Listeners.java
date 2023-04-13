package me.zsnow.stone.paintball;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stone.paintball.api.ItemBuilder;
import me.zsnow.stone.paintball.api.LocationAPI;
import me.zsnow.stone.paintball.api.LocationAPI.location;
import me.zsnow.stone.paintball.api.NBTItemStack;
import me.zsnow.stone.paintball.configs.Configs;
import me.zsnow.stone.paintball.manager.EventController;
import me.zsnow.stone.paintball.manager.MembersController;
import me.zsnow.stone.paintball.times.TeamBlue;
import me.zsnow.stone.paintball.times.TeamRed;

public class Listeners implements Listener {
	
	 MembersController members = MembersController.getInstance();
	 EventController evento = EventController.getInstance();
	
	TeamBlue teamBlue = TeamBlue.get();
	TeamRed teamRed = TeamRed.get();
	
	@SuppressWarnings("unused")
	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	public static HashMap<Player, Boolean> cooldownSnowball = new HashMap<>();
	
	@EventHandler
	public void onUse(PlayerInteractEvent event) {
	    final Player player = event.getPlayer();
	    if (player.getItemInHand().isSimilar(getGun()) && !members.getParticipantes().contains(player)) {
	    	player.getInventory().remove(getGun());
	    }
	    if (player.getItemInHand().isSimilar(getGun()) && evento.getArmasLiberadas() == true && 
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
	public void onShoot(EntityDamageByEntityEvent event) {
		       Entity damaged = event.getEntity();
		       Entity damageEntity = event.getDamager();
			
			if (damaged instanceof Player && damageEntity instanceof Snowball) {
				Snowball snowball = (Snowball) damageEntity;
		        LivingEntity entityThrower = (LivingEntity) snowball.getShooter();
		            if(entityThrower instanceof Player) {
		                Player playerThrower = (Player) entityThrower;
		                Player playerHit = (Player) damaged;
					if (members.getParticipantes().contains(playerThrower) && members.getParticipantes().contains(playerHit)) {
						if (teamBlue.getTeamBlueList().contains(playerThrower) && teamBlue.getTeamBlueList().contains(playerHit) || 
							teamRed.getTeamRedList().contains(playerThrower) && teamRed.getTeamRedList().contains(playerHit)) {
							event.setCancelled(true);
							playerThrower.sendMessage("§cVocê não pode atacar o próprio time.");
							return;
						} else {
							
							double dano = 8; // cada 2 = 1 cora
							
							event.setDamage(dano);
						if (playerHit.getHealth() <= dano) {
								members.deathVidaRemover(playerHit);
								playerHit.getLocation().getWorld().playEffect(playerHit.getLocation().add(0.0D, 1.0D, 0.0D), Effect.STEP_SOUND, 152);
								playerHit.playSound(playerHit.getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.5f);
								playerHit.sendMessage("§eVocê morreu para §f"+playerThrower.getName()+"§e! Vidas restantes: §c❤ " + members.getVidas(playerHit) + "§c/3");
								playerHit.getInventory().clear();
								playerHit.sendTitle("", "§c§lVocê foi morto. Vidas: ❤ " + members.getVidas(playerHit) + "§c§l/3");
								
								final String msg = "§2➜ §aVocê eliminou §e§l" + playerHit.getName() + "§a. Total de abates: §e§l";
								
								if (teamBlue.getTeamBlueList().contains(playerThrower)) {
									teamBlue.updateAbates(playerThrower);
									playerThrower.sendMessage(msg + teamBlue.getAbates(playerThrower));
									LocationAPI.getLocation().teleportTo(playerHit, location.VERMELHO);
									
								} else if (teamRed.getTeamRedList().contains(playerThrower)) {
									teamRed.updateAbates(playerThrower);
									playerThrower.sendMessage(msg + teamRed.getAbates(playerThrower));
									LocationAPI.getLocation().teleportTo(playerHit, location.AZUL);
									
									}
								playerHit.setHealth(20);
								playerThrower.playSound(playerThrower.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
								
						} else {
							return;
						}
								if (members.getVidas(playerHit) <= 0) {
									for (Player participantes : members.getParticipantes()) {
										participantes.sendMessage(
											"§e[PaintBall] " + getTeamTag(playerHit) + " " + playerHit.getName() + 
											" §7foi eliminado por §f" + getTeamTag(playerThrower) + " " +playerThrower.getName());
									}
									
									setEliminado(playerHit);
									playerHit.sendMessage("§e[PaintBall] §cVocê perdeu todas as suas vidas e foi eliminado por "+playerThrower.getName()+"§c!");
									playerHit.sendTitle("", "§4§lVocê foi eliminado do PaintBall.");
									evento.eventCanKeepRunning();
									return;
								}
								
								playerHit.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 2));
								playerHit.spigot().setCollidesWithEntities(false); // dps add isso de volta
								for (Player participantes : members.getParticipantes()) {
									participantes.hidePlayer(playerHit);
								}
								
								(new BukkitRunnable() {
									int tempo = 5;
									
									@Override
									public void run() {
										if (evento.getEventoStatus() && tempo > 0) {
											playerHit.sendMessage("§eVocê irá renascer em " + tempo + " §esegundos.");
											tempo--;
											if (tempo == 0) {
												playerHit.spigot().setCollidesWithEntities(true);
												for (Player participantes : members.getParticipantes()) {
													participantes.showPlayer(playerHit);
												}
												playerHit.removePotionEffect(PotionEffectType.INVISIBILITY);
												playerHit.sendMessage("§eVocê renasceu!");
												playerHit.getInventory().addItem(getGun());
												cancel();
											}
										} else {
											cancel();
										}
									}
								}).runTaskTimer(Main.getInstance(), 0L, 20L);
							}
						}
					}
				} else {
					if (members.getParticipantes().contains(damageEntity) && members.getParticipantes().contains(damaged)) event.setCancelled(true);
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
				setEliminado(p);
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					evento.eventCanKeepRunning();
				}
			}
		}
	
	 ////////////////////////////

	////////////////////////////
	    
	    
	public static ItemStack getGun() {
		ItemStack gun = new ItemBuilder(Material.IRON_BARDING).displayname("§eArma de tinta").lore(Arrays.asList(new String[] {
				" ",
				"§6§lCLIQUE COM O DIREITO!", 
				"§7§l╰ §7Clique para atirar bolas de tinta nos adversário."})).build();
		NBTItemStack nbtItemStack = new NBTItemStack(gun);
		nbtItemStack.setString("StoneEnderGun", "Gun12");
		
		return nbtItemStack.getItem();
		
	}
	
	private String getTeamTag(Player p) {
		if (teamRed.getTeamRedList().contains(p)) {
			return "§c§lVERMELHO§c";
		}
		if (teamBlue.getTeamBlueList().contains(p)) {
			return "§3§lAZUL§3";
		} else {
			return "§7[?]";
		}
	}
	
	 static int taskID;
	
	@SuppressWarnings("deprecation")
	public void startEndgameTimer() {
		
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			int tempo = 16*60;
			
			@Override
			public void run() {
				if (evento.getEventoStatus() == false) {
					tempo--;
						if ((tempo/60) == 10) {
							for (Player participantes : members.getParticipantes()) {
								participantes.sendMessage(" ");
								participantes.sendMessage(" §e[PaintBall] §fO evento se encerra em 10 minutos.");
								participantes.sendMessage(" ");
							}
						}
						if ((tempo/60) == 5) {
							for (Player participantes : members.getParticipantes()) {
								participantes.sendMessage(" ");
								participantes.sendMessage(" §e[PaintBall] §fO evento se encerra em 5 minutos.");
								participantes.sendMessage(" ");
							}
						}
						if ((tempo/60) == 0) {
							for (Player participantes : members.getParticipantes()) {
								participantes.sendMessage(" ");
								participantes.sendMessage(" §e[PaintBall] §fO evento excedeu o limite de tempo.");
								participantes.sendMessage(" ");
							}
							evento.eventoEmpatou();
							sh.cancelTask(taskID);
						}	
						
				} else {
					sh.cancelTask(taskID);
				}
			}
		}, 0L, 20L);
	}
	
	private void setEliminado(Player playerHit) {
		members.getParticipantes().remove(playerHit);
		teamRed.getTeamRedList().remove(playerHit);
		teamBlue.getTeamBlueList().remove(playerHit);
		playerHit.getInventory().clear();
		playerHit.getInventory().setArmorContents(null);
		playerHit.spigot().setCollidesWithEntities(true);
		members.deleteVidas(playerHit);
		LocationAPI.getLocation().teleportTo(playerHit, location.SAIDA);
	}
	
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
