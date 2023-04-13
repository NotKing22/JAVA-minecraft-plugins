package me.zsnow.stone.bandeira;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.base.Strings;

import me.zsnow.stone.bandeira.api.LocationAPI;
import me.zsnow.stone.bandeira.api.LocationAPI.location;
import me.zsnow.stone.bandeira.api.StringReplaceAPI;
import me.zsnow.stone.bandeira.configs.Configs;
import me.zsnow.stone.bandeira.manager.EventController;
import me.zsnow.stone.bandeira.manager.MembersController;
import me.zsnow.stone.bandeira.times.TeamBlue;
import me.zsnow.stone.bandeira.times.TeamRed;

public class Listeners implements Listener {
	
	 MembersController members = MembersController.getInstance();
	 EventController evento = EventController.getInstance();
	
	TeamBlue teamBlue = TeamBlue.getInstance();
	TeamRed teamRed = TeamRed.getInstance();
	
	@SuppressWarnings("unused")
	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	final private static String bandeira = "⚑";
	
	 ArrayList<Player> captureTiming = new ArrayList<>();
	 
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if(p.getWorld().getName().equals("Plotme")) return;
		 if (e.getFrom().getX() != e.getTo().getX() && (e.getFrom().getZ() != e.getTo().getZ())) {
		 if (EventController.getInstance().getEventoStatus()) {
			 if (teamBlue.getTeam(p) != null && teamBlue.getTeam(p) && teamRed.getHasFlagPlaced() == true) {
					Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
				 if (step.getBlock().hasMetadata("BandeiraVermelha")) {
					 if (!captureTiming.contains(p)) {
						 captureTiming.add(p);
							new BukkitRunnable() {
								
								int tempo = 0;
								
								@Override
								public void run() {
									
									 Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
									 if (step.getBlock().hasMetadata("BandeiraVermelha") && teamRed.getHasFlagPlaced() == true) {
										 p.sendTitle("", "§c⚑ " + getProgressBar(tempo, 100, 10, "|", "§a", "§8") + "");
										tempo+=20;
										if (tempo == 100) {
											teamRed.setFlagPlacedStatus(false);
											p.sendTitle("", "§aCaptura completa");
											captureTiming.remove(p);
											teamBlue.setPlayerWithFlag(p);
											for (Player participantes : teamRed.getParticipantes()) {
												participantes.sendMessage(" ");
												participantes.sendMessage(" §3" + p.getName() + " §cCapturou a sua bandeira.");
												participantes.sendMessage(" §c⚠ §cElimine-o antes que ele possa pontuar.");
												participantes.sendMessage(" ");
												participantes.playSound(participantes.getLocation(), Sound.NOTE_BASS, 1.0f, 0.5f);
											}
											for (Player participantes : teamBlue.getParticipantes()) {
												participantes.sendMessage(" ");
												participantes.sendMessage(" §b" + p.getName() + " §fCapturou a bandeira do time Vermelho.");
												participantes.sendMessage(" §c⚠ §fProteja-o até sua base para que possam pontuar.");
												participantes.sendMessage(" ");
												participantes.playSound(participantes.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
											}
											cancel();
											return;
										}
										return;
									} else {
										p.sendTitle("", "§c§l ✕");
										captureTiming.remove(p);
										cancel();
									}
								}
							}.runTaskTimer(Main.getInstance(), 0L, 20L);
						 }
					 }
				}
				if (teamRed.getTeam(p) != null && teamRed.getTeam(p) && teamBlue.getHasFlagPlaced() == true) {
					
					 Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
					
				 if (step.getBlock().hasMetadata("BandeiraAzul")) {
					 if (!captureTiming.contains(p)) {
						 captureTiming.add(p);
							new BukkitRunnable() {
								
								int tempo = 0;
								
								@Override
								public void run() {
									
									 Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
									 if (step.getBlock().hasMetadata("BandeiraAzul") && teamBlue.getHasFlagPlaced() == true) {
										 p.sendTitle("", "§3⚑ " + getProgressBar(tempo, 100, 10, "|", "§a", "§8") + "");
										tempo+=20;
										if (tempo == 100) {
											teamBlue.setFlagPlacedStatus(false);
											p.sendTitle("", "§aCaptura completa");
											captureTiming.remove(p);
											teamRed.setPlayerWithFlag(p);
											for (Player participantes : teamRed.getParticipantes()) {
												participantes.sendMessage(" ");
												participantes.sendMessage(" §c" + p.getName() + " §fCapturou a bandeira do time Azul.");
												participantes.sendMessage(" §c⚠ §fProteja-o até sua base para que possam pontuar.");
												participantes.sendMessage(" ");
												participantes.playSound(participantes.getLocation(), Sound.NOTE_STICKS, 1.0f, 1.0f);
											}
											for (Player participantes : teamBlue.getParticipantes()) {
												participantes.sendMessage(" ");
												participantes.sendMessage(" §4" + p.getName() + " §cCapturou a sua bandeira.");
												participantes.sendMessage(" §c⚠ §cElimine-o antes que ele possa pontuar.");
												participantes.sendMessage(" ");
												participantes.playSound(participantes.getLocation(), Sound.NOTE_BASS, 1.0f, 0.5f);
											}
											cancel();
											return;
										}
										return;
									} else {
										p.sendTitle("", "§c§l ✕");
										captureTiming.remove(p);
										cancel();
									}
								}
							}.runTaskTimer(Main.getInstance(), 0L, 20L);
						 }
					 }
				}
				
				if (teamRed.getTeam(p) != null && teamRed.getTeam(p) && teamRed.getPlayerWithFlag() != null && teamRed.getPlayerWithFlag().equals(p)) {
					
					 Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
					
					 if (step.getBlock().hasMetadata("BandeiraVermelha")) {
						 for (Player participantes : teamBlue.getParticipantes()) {
							 participantes.playSound(participantes.getLocation(), Sound.CLICK, 1.0f, 1.0f);
						 }
						 for (Player participantes : teamRed.getParticipantes()) {
							 participantes.playSound(participantes.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
						 }
						 teamRed.clearPlayerWithFlag();
						 teamRed.sendTeamArmorToPlayer2(p);
						 p.setHealth(p.getMaxHealth());
						 evento.setPointRed();
						 teamBlue.setFlagPlacedStatus(true);
						 for (Player participantes : members.getParticipantes()) {
							 participantes.sendMessage("§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂");
							 participantes.sendMessage(" ");
							 participantes.sendMessage(" §c⚑ " + p.getName() + " §a§l+1 §2§l⬆");
							 participantes .sendMessage(" §6Conseguiu capturar a bandeira do time §3§lAZUL");
							 participantes .sendMessage(" §6§lPLACAR: §b" + evento.getPointBlue() + " " + bandeira + " §7x §c" + evento.getPointRed() + " " + bandeira + " §7(Máximo: " + evento.getMaxPoint() + "§7 Pontos)");
							 participantes.sendMessage("§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂§b▂§e▂");
						 }
						 p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					     for (String cmd : Configs.config.getConfig().getStringList("premio")) {
						 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd).replace("{vencedor}", p.getName()));
						     }
					     p.sendMessage("§aVocê recebeu $" + evento.getRecompensa() + " §acoins por entregar a bandeira.");
				 	}
				 }
				
				if (teamBlue.getTeam(p) != null && teamBlue.getTeam(p) && teamBlue.getPlayerWithFlag() != null && teamBlue.getPlayerWithFlag().equals(p)) {
					
					 Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
					
					 if (step.getBlock().hasMetadata("BandeiraAzul")) {
						 for (Player participantes : teamBlue.getParticipantes()) {
							 participantes.playSound(participantes.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 1.0f);
						 }
						 for (Player participantes : teamRed.getParticipantes()) {
							 participantes.playSound(participantes.getLocation(), Sound.CLICK, 1.0f, 1.0f);
						 }
						 teamBlue.clearPlayerWithFlag();
						 teamBlue.sendTeamArmorToPlayer2(p);
						 p.setHealth(p.getMaxHealth());
						 evento.setPointBlue();
						 teamRed.setFlagPlacedStatus(true);
						 for (Player participantes : members.getParticipantes()) {
							 participantes.sendMessage("§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂");
							 participantes.sendMessage(" ");
							 participantes.sendMessage(" §3⚑ " + p.getName() + " §a§l+1 §2§l⬆");
							 participantes .sendMessage(" §6Conseguiu capturar a bandeira do time §c§lVERMELHO");
							 participantes .sendMessage(" §6§lPLACAR: §b" + evento.getPointBlue() + " " + bandeira + " §7x §c" + evento.getPointRed() + " " + bandeira + " §7(Máximo: " + evento.getMaxPoint() + "§7 Pontos)");
							 participantes.sendMessage("§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂§c▂§e▂");
						 }
						 p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					     for (String cmd : Configs.config.getConfig().getStringList("premio")) {
						 	   Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), StringReplaceAPI.replaceMsg(cmd).replace("{vencedor}", p.getName()));
					     }
					     p.sendMessage("§aVocê recebeu $" + evento.getRecompensa() + " §acoins por entregar a bandeira.");
				 	}
				 }
				
			 }
		}
	}
	
    private String getProgressBar(int current, int max, int totalBars, String symbol, String color0, String color1) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return color0 + Strings.repeat(symbol, progressBars)
                + color1 + Strings.repeat(symbol, totalBars - progressBars);
    }
	
	 @EventHandler
	 public void itemDrop(PlayerDropItemEvent e) {
		 Player p = e.getPlayer();
		 if (members.getParticipantes().contains(p)) {
			 e.setCancelled(true);
		 }
	 }
	
	 
	 @EventHandler
	 public void onHit(EntityDamageByEntityEvent e) {
		 if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			 final Player entity = (Player) e.getEntity();
			 final Player damager = (Player) e.getDamager();
			 if (members.getParticipantes().contains(entity) && members.getParticipantes().contains(damager)) {
				 
				 if ((teamBlue.getTeam(entity) && teamBlue.getTeam(damager)) || (teamRed.getTeam(entity) && teamRed.getTeam(damager))) {
					 e.setCancelled(true);
					 return;
				 }
				 if (evento.getCaptureLiberado() == false) {
					 damager.sendMessage("§cO pvp no momento está desativado.");
					 e.setCancelled(true);
			 	}
			 }
		 }
	 }
	 
	 @EventHandler
	 public void onBreak(BlockBreakEvent e) {
		 if (teamBlue.getTeam(e.getPlayer()) || teamRed.getTeam(e.getPlayer()) && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) e.setCancelled(true);
	 }
	 
	 @EventHandler
	 public void respawn(PlayerRespawnEvent e) {
		 Player vitima = e.getPlayer();
		 if (evento.getEventoStatus() == true && members.getParticipantes().contains(vitima)) {
				if (teamBlue.getTeam(vitima)) {
					
					new BukkitRunnable() {
					    @Override
					    public void run () {
							LocationAPI.getLocation().teleportTo(vitima, location.AZUL);
							teamBlue.sendTeamArmorToPlayer(vitima);
					    }
					}.runTaskLater(Main.getInstance(), 1L);
					return;
				}
				if (teamRed.getTeam(vitima)) {

					new BukkitRunnable() {
					    @Override
					    public void run () {
							LocationAPI.getLocation().teleportTo(vitima, location.VERMELHO);
							teamRed.sendTeamArmorToPlayer(vitima);
					    }
					}.runTaskLater(Main.getInstance(), 1L);
			}
		 }
	 }
	 
	 @EventHandler
	 public void pickup(PlayerPickupItemEvent e) {
		 if (members.getParticipantes().contains(e.getPlayer())) e.setCancelled(true);
	 }
	 
	 @EventHandler
   	 public void onDeath(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			 Player vitima = e.getEntity();
				if (members.getParticipantes().contains(vitima)) {
					e.getDrops().clear();
					vitima.getInventory().clear();
					vitima.getInventory().setArmorContents(null);
				if (teamBlue.getPlayerWithFlag() != null && teamBlue.getPlayerWithFlag().equals(vitima)) {
					teamBlue.clearPlayerWithFlag();
					teamRed.setFlagPlacedStatus(true);
					for (Player jogadores : members.getParticipantes()) {
						jogadores.sendMessage(" ");
						jogadores.sendMessage(" §3" + vitima.getName() + " §6foi eliminado com a bandeira.");
						jogadores.sendMessage(" ");
					}
				}
				if (teamRed.getPlayerWithFlag() != null && teamRed.getPlayerWithFlag().equals(vitima)) {
					teamRed.clearPlayerWithFlag();
					teamBlue.setFlagPlacedStatus(true);
					for (Player jogadores : members.getParticipantes()) {
						jogadores.sendMessage(" ");
						jogadores.sendMessage(" §c" + vitima.getName() + " §6foi eliminado com a bandeira.");
						jogadores.sendMessage(" ");
					}
				}
				if (e.getEntity().getKiller() instanceof Player) {
					final Player killer = e.getEntity().getKiller();
					vitima.sendMessage("☠ Você foi eliminado por " + getTeamTag(killer) + " " + killer.getName());
					killer.sendMessage("⚔ Você eliminou " + getTeamTag(vitima) + " " + vitima.getName());
					killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
				}
			}
		}
	 }
	 
	 @EventHandler
	 public void worldChange(PlayerChangedWorldEvent e) {
		final Player p = e.getPlayer();
		if (evento.getEventoStatus() == true && members.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("Eventos"))) {
			LocationAPI.getLocation().teleportTo(p, location.LOBBY);
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
				evento.setEliminado(p);
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					if (teamBlue.getPlayerWithFlag() != null && teamBlue.getPlayerWithFlag().equals(p)) {
						for (Player jogadores : teamBlue.getParticipantes()) {
							jogadores.sendMessage(" ");
							jogadores.sendMessage(" §3" + p.getName() + " §eDesconectou do evento com a bandeira.");
							jogadores.sendMessage(" §e A bandeira retornou para o time VERMELHO.");
							jogadores.sendMessage(" ");
							jogadores.playSound(jogadores.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
						}
						teamRed.clearPlayerWithFlag();
						teamRed.setFlagPlacedStatus(true);
						teamRed.removePlayerTeam(p);
					}
					if (teamRed.getPlayerWithFlag() != null && teamRed.getPlayerWithFlag().equals(p)) {
						for (Player jogadores : teamRed.getParticipantes()) {
							jogadores.sendMessage(" ");
							jogadores.sendMessage(" §c" + p.getName() + " §eDesconectou do evento com a bandeira.");
							jogadores.sendMessage(" §e A bandeira retornou para o time AZUL.");
							jogadores.sendMessage(" ");
							jogadores.playSound(jogadores.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
						}
						teamBlue.clearPlayerWithFlag();
						teamBlue.setFlagPlacedStatus(true);
						teamBlue.removePlayerTeam(p);
					}
					evento.eventCanKeepRunning();
				}
			}
		}
	
	 ////////////////////////////

	////////////////////////////
	    
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
								participantes.sendMessage(" §d§l[CF] §fO evento se encerra em 10 minutos.");
								participantes.sendMessage(" ");
							}
						}
						if ((tempo/60) == 5) {
							for (Player participantes : members.getParticipantes()) {
								participantes.sendMessage(" ");
								participantes.sendMessage(" §d§l[CF] §fO evento se encerra em 5 minutos.");
								participantes.sendMessage(" ");
							}
						}
						if ((tempo/60) == 0) {
							for (Player participantes : members.getParticipantes()) {
								participantes.sendMessage(" ");
								participantes.sendMessage(" §d§l[CF] §fO evento excedeu o limite de tempo.");
								participantes.sendMessage(" ");
							}
							if (evento.getPointBlue() == evento.getPointRed()) {
								evento.endEvent();
							}
							evento.endEvent();
							sh.cancelTask(taskID);
						}	
						
				} else {
					sh.cancelTask(taskID);
				}
			}
		}, 0L, 20L);
	}
	
	@SuppressWarnings("unused")
	private String getTeam(Player p) {
		TeamBlue teamBlue = TeamBlue.getInstance();
		TeamRed teamRed = TeamRed.getInstance();
		if (teamRed.getTeamRedList().contains(p)) {
			return "V";
		}
		if (teamBlue.getTeamBlueList().contains(p)) {
			return "A";
		} else {
			return "§7[?]";
		}
	}
	
    @EventHandler(priority = EventPriority.NORMAL)
    public void onClick(InventoryClickEvent event) {
        if(members.getParticipantes().contains(event.getWhoClicked()) && event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
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
