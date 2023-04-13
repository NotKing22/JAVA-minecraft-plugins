package me.zsnow.stone.partygames.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.stone.partygames.Main;
import me.zsnow.stone.partygames.api.ActionBarAPI;
import me.zsnow.stone.partygames.api.LocationAPI;
import me.zsnow.stone.partygames.api.LocationAPI.location;
import me.zsnow.stone.partygames.configs.Configs;
import me.zsnow.stone.partygames.title.TitleAPI;
import me.zsnow.stone.partygames.users.UserData;

@SuppressWarnings("deprecation")
public class CrazyPool implements Listener {
	
	private MainEvent evento = MainEvent.getInstance();
	
	private int timer = 10;
	private boolean start;
	UserData userData = UserData.getInstance();
	public static HashMap<String, Integer> scoreCurrent = new HashMap<>();
	
	public void playerScored(String player, Integer newPoint) {
		if (scoreCurrent.containsKey(player)) {
			int oldPoint = scoreCurrent.get(player);
			scoreCurrent.replace(player, oldPoint += newPoint);
		} else {
			scoreCurrent.put(player, newPoint);
		}
	}
	
	public Integer getPlayerScore(String string) {
			return scoreCurrent.get(string) == null ? 0 : scoreCurrent.get(string);
	}
	
	public HashMap<String, Integer> getScoreMap() {
		return scoreCurrent;
	}
	
	public void setStartedCrazyPool() {
		start = true;
	}
	
	
	// RESETAR O MAPA COM WORLDEDIT BRUUUUUUUH
	
	
	public static CrazyPool instance = new CrazyPool();
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) { 
		Player p = e.getPlayer();
		 if(p.getWorld().getName().equals("PlotMe")) return;
		  Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
		  Location stepBellow = p.getLocation().add(0.0D, -1.0D, 0.0D);
			if ((e.getFrom().getY() != e.getTo().getY()) && evento.getParticipantes().contains(p) ) {
					if (start == false) { // CHANGE TO TRUE
					if (e.getTo().getBlock().isLiquid()) {
						if ((stepBellow.getBlock().getType() == Material.WATER) || stepBellow.getBlock().getType() == Material.STATIONARY_WATER) {
							setRandomColoredWoolAtLocation(p.getLocation().add(0.0D, -1.0D, 0.0D), getRandomNumber());
							playerScored(p.getName(), 1);
							ActionBarAPI.sendActionBarMessage(p, "§a+1 Ponto adicionado (Pontos: " + getPlayerScore(p.getName()) + "§a)");
							LocationAPI.getLocation().teleportTo(p, location.CRAZYPOOL_ENTRADA, Configs.crazypool_locations); 
							p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
							return;
						}
						if ((step.getBlock().getType() == Material.WATER) || step.getBlock().getType() == Material.STATIONARY_WATER) {
							setRandomColoredWoolAtLocation(step, getRandomNumber());
							setRandomColoredWoolAtLocation(p.getLocation().add(0.0D, 0.0D, 0.0D), getRandomNumber());
							playerScored(p.getName(), 1);
							ActionBarAPI.sendActionBarMessage(p, "§a+1 Ponto adicionado (Pontos: " + getPlayerScore(p.getName()) + "§a)");
							LocationAPI.getLocation().teleportTo(p, location.CRAZYPOOL_ENTRADA, Configs.crazypool_locations); 
							p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
							return;
							} 
						} else {
							if (stepBellow.getBlock().getType() != Material.AIR && stepBellow.getBlockY() < 100) {
								ActionBarAPI.sendActionBarMessage(p, "§c+0 Pontos adicionados");
								LocationAPI.getLocation().teleportTo(p, location.CRAZYPOOL_ENTRADA, Configs.crazypool_locations); 
								p.teleport(step);
								LocationAPI.getLocation().teleportTo(p, location.CRAZYPOOL_ENTRADA, Configs.crazypool_locations); 
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
									return;
								}
						}
					} else {
						p.sendMessage("§cO evento ainda não começou!");
					}
				}
			}
	
	public int getRandomNumber() {
		Random random = new Random();
		int upperbound = 16;
		int int_random = random.nextInt(upperbound); 
		return int_random;
	}

	public void startCountdown() {
		(new BukkitRunnable() {
			
			int tempo = timer;
			
			@Override
			public void run() {
				Bukkit.broadcastMessage(" §6tempo " + tempo);
				if (tempo > 0) {
					tempo--;
				}
				if (tempo == 0) {
					// DESCOBRIR PQ O USERDATA N TA DANDO GET NOS PLAYER NO TOP 3 MINIGAME
					// E O TOTAL DE TOOD MUNDO TA 3 QUE MERDA É ESSA, o valor ta igual o do zsnowreach pra todos
					playerScored("OscarAlho", 2);
					playerScored("SpamCaxota", 5);
					userData.playerScored("LimaMay", 6);
					userData.playerScored("ThomasTurbano", 4);
					userData.playerScored("GhostTheDawnCool", 8);
					ArrayList<String> top3This = new ArrayList<>();
				    List<Map.Entry<String, Integer>> listThis = new ArrayList<>(scoreCurrent.entrySet());
				    Collections.sort(listThis, new Comparator<Map.Entry<String, Integer>>() {
				      @Override
				      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				        return o2.getValue().compareTo(o1.getValue());
				      }
				    });
				    
					ArrayList<String> top3Minigame = new ArrayList<>();
				    List<Map.Entry<String, Integer>> listMinigame = new ArrayList<>(userData.getScoreMap().entrySet());
				    Collections.sort(listMinigame, new Comparator<Map.Entry<String, Integer>>() {
				      @Override
				      public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				        return o2.getValue().compareTo(o1.getValue());
				      }
				    });
				    
				    //// §e§l         §6§l           §c§l         /
				    for (Player participantes : evento.getParticipantes()) {
				    	// essa desgraça de getplaerscore fica nulo, suspeito que seja pq em algo n tta pegando string e ta setando player
			    		userData.playerScored(participantes.getName(), getPlayerScore(participantes.getName())); //COMPUTANDO PONTOS NA DB FINAL
				    	participantes.sendMessage("§6§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
				    	participantes.sendMessage("§c§l               ");
				    	participantes.sendMessage(" §B§l                TOP 3 §F- §B§lCRAZY-POOL");
				    	participantes.sendMessage(" ");
				    	for (int i = 0; i < 3 && i < listThis.size(); i++) {
					    	//String prefix = VaultHook.getPlayerPrefix(participantes.getName());
				    		String prefix = "§6[GM] ";
				    		String prefix2 = "§a[VIP] ";
				    		String prefix3 = "§7[Membro] ";
					if (i == 0) {
						participantes.sendMessage(("§e§l               1§f. §7" + prefix2 + listThis.get(i).getKey() + " §7- §eScore: §6✮ " + listThis.get(i).getValue() + " §7(Total: " + userData.getPlayerScore(participantes) + ")"));
							}
					if (i == 1) {
						participantes.sendMessage(("§6§l                   2§f. §7" + prefix + listThis.get(i).getKey() + " §7- §eScore: §6✮ " + listThis.get(i).getValue() + " §7(Total: " + userData.getPlayerScore(participantes) + ")"));
							}
					if (i == 2) {
						participantes.sendMessage(("§c§l               3§f. §7" + prefix3 + listThis.get(i).getKey() + " §7- §eScore: §6✮ " + listThis.get(i).getValue() + " §7(Total: " + userData.getPlayerScore(participantes) + ")"));
						}
					    	top3This.add(participantes.getName());
					    }
				    	participantes.sendMessage("§c§l               ");
				    	participantes.sendMessage("§7§m--------------------------------------------");
				    	participantes.sendMessage("§c§l               ");
				    	participantes.sendMessage(" §B§l                TOP 3 §F- §B§lMINIGAMES GERAIS");
				    	participantes.sendMessage("§c§l               ");
				    	//######################################################################################################################
				    	for (int i = 0; i < 3 && i < listMinigame.size(); i++) {
					    	//String prefix = VaultHook.getPlayerPrefix(participantes.getName());
				    		String prefix = "§6[GM] ";
				    		String prefix2 = "§a[VIP] ";
				    		String prefix3 = "§7[Membro] ";
					if (i == 0) {
						participantes.sendMessage(("§e§l               1§f. §7" + prefix2 + listMinigame.get(i).getKey() + " §7- §fPontos totais: §b✪ " + userData.getPlayerScore(participantes) + ""));
							}
					if (i == 1) {
						participantes.sendMessage(("§6§l                   2§f. §7" + prefix + listMinigame.get(i).getKey() + " §7- §fPontos totais: §b✪ " + userData.getPlayerScore(participantes) + ""));
							}
					if (i == 2) {
						participantes.sendMessage(("§c§l               3§f. §7" + prefix3 + listMinigame.get(i).getKey() + " §7- §fPontos totais: §b✪ " + userData.getPlayerScore(participantes) + ""));
						}
					    	top3Minigame.add(participantes.getName());
					    }
						participantes.sendMessage(" ");
					    if (top3This.contains(participantes.getName())) { //botar !
					    	String prefix = "§b[BETA] ";
						    participantes.sendMessage("                                      ...");
						    participantes.sendMessage(("          §4⬇ §7 " + prefix + participantes.getName() + " §7- §eScore: §6✮ " + getPlayerScore(participantes.getName()) + " §7(Total: " + userData.getPlayerScore(participantes) + ")"));
					    }
					    participantes.sendMessage("§6§L▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
					    participantes.sendMessage(" ");
						cancel();
				    }
				}
				
			}
		}).runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
	public void unregisterListener() {
		HandlerList.unregisterAll();
	}
 	
	public void startTaskLobby() {
		(new BukkitRunnable() {
			int tempo = 0;
			@Override
			public void run() {
				tempo++;
				switch (tempo) {
					case 1:
						for (Player participantes : evento.getParticipantes()) {
							TitleAPI.sendTitle(participantes, 0, 70, 0, "§e§lPiscina Maluca", "Pule na água para pontuar");
							participantes.playSound(participantes.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						}
						break;
					case 4:
						for (Player participantes : evento.getParticipantes()) {
							TitleAPI.sendTitle(participantes, 0, 70, 0, "§e§lPiscina Maluca", "Atingir a lã não dá pontos");
							participantes.playSound(participantes.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.5f);
						}
						break;
					case 7:
						for (Player participantes : evento.getParticipantes()) {
							TitleAPI.sendTitle(participantes, 0, 30, 0, "", "§6➌");
							participantes.playSound(participantes.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
						}
						break;
					case 8:
						for (Player participantes : evento.getParticipantes()) {
							TitleAPI.sendTitle(participantes, 0, 30, 0, "", "§c➋");
							participantes.playSound(participantes.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
						}
						break;
					case 9:
						for (Player participantes : evento.getParticipantes()) {
							TitleAPI.sendTitle(participantes, 0, 30, 0, "", "§4➊");
							participantes.playSound(participantes.getLocation(), Sound.NOTE_STICKS, 1.0f, 0.5f);
						}
						break;
					case 10:
						for (Player participantes : evento.getParticipantes()) {
							participantes.playSound(participantes.getLocation(), Sound.ITEM_BREAK, 1.0f, 0.5f);
						}
						startCountdown();
						cancel();
						break;
	
					default:
						break;
					}
				}
			}).runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
	/*public static void pasteWater(Player p) {
		if (Configs.crazypool_locations.getConfig().contains("CRAZYPOOL_ENTRADA")) {
			 int x = Configs.crazypool_locations.getConfig().getInt("CRAZYPOOL_ENTRADA.X");
			 int y = Configs.crazypool_locations.getConfig().getInt("CRAZYPOOL_ENTRADA.Y");
			 int z = Configs.crazypool_locations.getConfig().getInt("CRAZYPOOL_ENTRADA.Z");
		   	 World world = Bukkit.getWorld(Configs.crazypool_locations.getConfig().getString("CRAZYPOOL_ENTRADA.Mundo"));
			 WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
			 EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000);
			 try {
				 //editSession.makeCylinder(center, dirtPattern, radius, height, filled);
				//session.makeCylinder(session, new Vector(x, y, z), ,false);
				 session.makeCylinder(new Vector(x, y, z), , 5, 1, false);
			} catch (MaxChangedBlocksException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("§c[StonePartyGames] Houve um erro ao posicionar a agua.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("§c[StonePartyGames] A posicao da agua nao foi definida.");
		}
	}*/
	
	public void createCircle(Player p) {
		int radius = 5;
        int centerX = p.getLocation().getBlockX();
        int centerZ = p.getLocation().getBlockZ();
		for (double x = centerX - radius; x <= centerX + radius; x++) {
		    for (double z = centerZ - radius; z <= centerZ + radius; z++) {
		        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2));
		        if (distance <= radius) {
		            int y = p.getLocation().getWorld().getHighestBlockYAt((int) x, (int) z);
		            p.getLocation().getWorld().getBlockAt((int) x, y, (int) z).setType(Material.GRASS);
		        }
		    }
		}
	}

	
	@SuppressWarnings("deprecation")
	public static void setRandomColoredWoolAtLocation(Location loc, int random_Wool_ID) {
		switch (random_Wool_ID) {
			case 0:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 0);
				break;
			case 1:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 1);
				break;
			case 2:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 2);
				break;
			case 3:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 3);
				break;
			case 4:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 4);
				break;
			case 5:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 5);
				break;
			case 6:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 6);
				break;
			case 7:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 7);
				break;
			case 8:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 8);
				break;
			case 9:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 9);
				break;
			case 10:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 10);
				break;
			case 11:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 11);
				break;
			case 12:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 12);
				break;
			case 13:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 13);
				break;
			case 14:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 14);
				break;
			case 15:
	            loc.getBlock().setTypeId(35);
	            loc.getBlock().setData((byte) 15);
				break;
			default:
				break;
			}
		}
}
