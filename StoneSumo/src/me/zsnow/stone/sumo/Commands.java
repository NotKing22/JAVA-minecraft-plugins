package me.zsnow.stone.sumo;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;


public class Commands implements CommandExecutor, Listener {

	String NO_PERM_ADMIN = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	String NO_PERM_MOD = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	double premio = Main.getPlugin().getConfig().getDouble("premio");
	String premio_message = Main.getPlugin().getConfig().getString("premio-message");
	ArrayList<Player> verDM = new ArrayList<>();  
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sumo")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage("§a§lSUMO §f- §aComandos");
					sender.sendMessage("");
					sender.sendMessage(" §f/sumo forcestart");
					sender.sendMessage(" §f/sumo forcestop");
					sender.sendMessage("");
					return true;
				}
				if (args.length == 1) {
					if ((args[0].equalsIgnoreCase("fstart") || (args[0].equalsIgnoreCase("forcestart")))) {
						if (sender.hasPermission("zs.admin")) {
							SumoManager sumo = SumoManager.manager;
							if ((sumo.getOcorrendoStatus() == false) && 
									(sumo.getEntradaLiberadaStatus() == false)) {
										
								SumoReset();
										
									(new BukkitRunnable() {
										
										@Override
										public void run() {
											if (sumo.getOcorrendoStatus() == true) {
												if (sumo.getTempo() > 1) {
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§a§lSUMO §f- §a§lENTRADA ABERTA");
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§aPara participar digite: §f/sumo entrar");
													Bukkit.broadcastMessage("§aParticipantes: §f" + sumo.getParticipantes().size());
													Bukkit.broadcastMessage("§aPrêmio: §f" + premio_message +" §fde coins");
													Bukkit.broadcastMessage("§aEntrada fechando em: §f" + sumo.getTempo() + " §fsegundos");
													Bukkit.broadcastMessage("§aCategoria: §fDuelo um contra um.");
													Bukkit.broadcastMessage("");
													sumo.setTempo(sumo.getTempo() - 10);
												} else {
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§a§lSUMO §f- §a§lENTRADA FECHADA.");
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§aEvento iniciando...");
													Bukkit.broadcastMessage("§aParticipantes: §f" + sumo.getParticipantes().size());
													Bukkit.broadcastMessage("");
													sumo.setEntradaLiberadaStatus(false);
													PrepararDuelo();
													cancel();
												}
											} else {
												SumoStop();
												cancel();
											}
										}
									}).runTaskTimer(Main.getPlugin(), 0L, 200L);
							} else {
								sender.sendMessage("§cO evento já está aberto.");
								return true;
							}
							
						} else {
							sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
							return true;
						}
					}
					if ((args[0].equalsIgnoreCase("forcestop") || (args[0].equalsIgnoreCase("forcefinalizar")))) {
						if (sender.hasPermission("zs.admin")) {
							if (SumoManager.manager.getOcorrendoStatus() == true) {
								SumoManager.manager.setOcorrendoStatus(false);
								for (Player jogadores : SumoManager.manager.getParticipantes()) {
									sendTo(jogadores, "Saida");
									retirarItens(jogadores);
									disableClanDamage(jogadores);
								}
								SumoManager.manager.getParticipantes().clear();
								SumoManager.manager.setOponenteX(null);
								SumoManager.manager.setOponenteY(null);
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§a§lSUMO §f- §a§lEVENTO FECHADO.");
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§cO evento foi forçado a finalizar por um ADMINISTRADOR.");
								Bukkit.broadcastMessage("");
								Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
								SumoStop();
						} else {
							sender.sendMessage("§cO evento sumo já está fechado.");
							return true;
						}
					} else {
						sender.sendMessage(NO_PERM_ADMIN);
						return true;
					}
					}
				}
			} else {
		Player p = (Player) sender;
			if (args.length == 0) {
				if (p.hasPermission("zs.admin")) {
					p.sendMessage("§a§lSUMO §f- §aComandos");
					p.sendMessage("");
					p.sendMessage(" §f/sumo entrar");
					p.sendMessage(" §f/sumo sair");
					p.sendMessage(" §f/sumo info");
					p.sendMessage(" §c/sumo moderar §7- §cEntre no evento como staff.");
					p.sendMessage(" §c/sumo modsair §7- §cSair do evento.");
					p.sendMessage(" §c/sumo forcestart §7- §cForça o evento a iniciar");
					p.sendMessage(" §c/sumo forcefinalizar §7- §cForça o evento a finalizar");
					p.sendMessage(" §c/sumo setentrada §7- §cDefine a entrada do evento");
					p.sendMessage(" §c/sumo setsaida §7- §cDefine a saída do evento.");
					p.sendMessage(" §c/sumo setpos<1/2> §7- §cDefine a localização do duelo.");
					p.sendMessage(" §c/sumo nextBattle §7- §cForça o início do próximo duelo.");
					p.sendMessage(" §c/sumo verDM §7- §cAparece na actionbar o tempo do deathmatch.");
					p.sendMessage("");
					return true;
				}
				if (p.hasPermission("zs.mod")) {
					p.sendMessage("§a§lSUMO §f- §aComandos");
					p.sendMessage("");
					p.sendMessage(" §f/sumo entrar");
					p.sendMessage(" §f/sumo sair");
					p.sendMessage(" §f/sumo info");
					p.sendMessage(" §c/sumo moderar §7- §cEntre no evento como staff.");
					p.sendMessage(" §c/sumo modsair §7- §cSair do evento.");
					p.sendMessage("");
					return true;
				}
				p.sendMessage("§a§lSUMO §f- §aComandos");
				p.sendMessage("");
				p.sendMessage(" §f/sumo entrar");
				p.sendMessage(" §f/sumo sair");
				p.sendMessage(" §f/sumo info");
				p.sendMessage("");
				return true;
			}
			if (args.length >= 1) {
				if ((args[0].equalsIgnoreCase("fstart") || (args[0].equalsIgnoreCase("forcestart")))) {
					if (p.hasPermission("zs.admin")) {
						if ((SumoManager.manager.getOcorrendoStatus() == false) && 
								(SumoManager.manager.getEntradaLiberadaStatus() == false)) {
									
							SumoReset();

							(new BukkitRunnable() {
									
									@Override
									public void run() {
										if (SumoManager.manager.getOcorrendoStatus() == true) {
											if (SumoManager.manager.getTempo() > 1) {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§a§lSUMO §f- §a§lENTRADA ABERTA");
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§aPara participar digite: §f/sumo entrar");
												Bukkit.broadcastMessage("§aParticipantes: §f" + SumoManager.manager.getParticipantes().size());
												Bukkit.broadcastMessage("§aPrêmio: §f"+ premio_message +" de coins");
												Bukkit.broadcastMessage("§aEntrada fechando em: §f" + SumoManager.manager.getTempo() + " §fsegundos");
												Bukkit.broadcastMessage("§aCategoria: §fDuelo um contra um.");
												Bukkit.broadcastMessage("");
												SumoManager.manager.setTempo(SumoManager.manager.getTempo() - 10);
											} else {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§a§lSUMO §f- §a§lENTRADA FECHADA.");
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§aEvento iniciando...");
												Bukkit.broadcastMessage("§aParticipantes: §f" + SumoManager.manager.getParticipantes().size());
												Bukkit.broadcastMessage("");
												SumoManager.manager.setEntradaLiberadaStatus(false);
												PrepararDuelo();
												cancel();
											}
										} else {
											SumoStop();
											cancel();
										}
									}
								}).runTaskTimer(Main.getPlugin(), 0L, 200L);
						} else {
							p.sendMessage("§cO evento já está aberto.");
							return true;
						}
						
					} else {
						p.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
				if ((args[0].equalsIgnoreCase("ffinalizar") || (args[0].equalsIgnoreCase("forcefinalizar")))) {
					if (p.hasPermission("zs.admin")) {
						if (SumoManager.manager.getOcorrendoStatus() == true) {
							SumoManager.manager.setOcorrendoStatus(false);
							for (Player jogadores : SumoManager.manager.getParticipantes()) {
								sendTo(jogadores, "Saida");
								retirarItens(jogadores);
								disableClanDamage(jogadores);
							}
							SumoManager.manager.getParticipantes().clear();
							SumoManager.manager.setOponenteX(null);
							SumoManager.manager.setOponenteY(null);
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§a§lSUMO §f- §a§lEVENTO FECHADO.");
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§cO evento foi forçado a finalizar por um ADMINISTRADOR.");
							Bukkit.broadcastMessage("");
							Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
							SumoStop();
					} else {
						p.sendMessage("§cO evento sumo já está fechado.");
						return true;
					}
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
					
				}
				if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a área de moderação do Sumo.");
						sendTo(p, "Entrada");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("verDM")) {
					if (p.hasPermission("zs.admin")) {
						if (!verDM.contains(p)) {
							verDM.add(p);
							p.sendMessage("§eAgora você recebe o tempo para o death-match em sua ActionBar.");
							return true;
						} else {
							verDM.remove(p);
							p.sendMessage("§eAgora você não recebe mais o tempo para o death-match em sua ActionBar.");
						}
					} else {
						p.sendMessage(NO_PERM_ADMIN);
						return true;
					}
					
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do Sumo.");
						sendTo(p, "Saida");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("setentrada")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Entrada");
					p.sendMessage("§eA entrada do evento Sumo foi definida com sucesso.");
					return true;
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("setsaida")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Saida");
					p.sendMessage("§eA saída do evento Sumo foi definida com sucesso.");
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("setpos1")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Pos1");
					p.sendMessage("§eA Position 1 do evento foi definida com sucesso.");
					return true;
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("setpos2")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Pos2");
					p.sendMessage("§eA Position 2 do evento foi definida com sucesso.");
					return true;
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("nextbattle")) {
				if (p.hasPermission("zs.admin")) {
					if (SumoManager.manager.getOcorrendoStatus() == true && 
							SumoManager.manager.getEntradaLiberadaStatus() == false && 
								SumoManager.manager.getParticipantes().size() > 1) {
									Player p1 = SumoManager.manager.getOponenteX();
									Player p2 = SumoManager.manager.getOponenteY();
										SumoManager.manager.setOponenteX(null);
										SumoManager.manager.setOponenteY(null);
							p1.setHealth(0);
							p2.setHealth(0);
										SumoManager.manager.getParticipantes().remove(p1);
										SumoManager.manager.getParticipantes().remove(p2);
								sendTo(p1, "Saida");
								sendTo(p2, "Saida");
								PrepararDuelo();
						p.sendMessage("§a§lVocê avançou o evento para a próxima batalha.");
						return true;
					} else {
						p.sendMessage("§cO evento sumo não está ocorrendo ou sua entrada ainda não foi fechada.");
						return true;
					}
				}
			}
				if (args[0].equalsIgnoreCase("entrar")) {
					if (Main.getPlugin().getConfig().getString("Entrada") != null) {
						if ((SumoManager.manager.getOcorrendoStatus() == true) && (SumoManager.manager.getEntradaLiberadaStatus() == true)) {
							if (!SumoManager.manager.getParticipantes().contains(p)) {
								if (p.hasPermission("sumo.entrar")) {
									PlayerInventory inv = p.getInventory();
									for (ItemStack i : inv.getContents()) {
										if(i != null && !(i.getType() == Material.AIR)) {
											p.sendMessage("§cEsvazie seu inventário para entrar no evento.");
											return true;
										} 
									} for (ItemStack i : inv.getArmorContents()) {
											if(i != null && !(i.getType() == Material.AIR)) {
												p.sendMessage("§cRetire sua armadura para entrar no evento.");
												return true;
											}
										}
									for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
										p.removePotionEffect(AllPotionEffects.getType());
									}
									sendTo(p, "Entrada");
									SumoManager.manager.getParticipantes().add(p);
									enableClanDamage(p);
									p.sendMessage("§aVocê entrou no evento Sumo!");
									p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
									return true;
								} else {
									p.sendMessage("§cVocê precisa do rank §6[ArrynIII]§c ou superior para participar do evento.");
									p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
									return true;
								}
							} else {
								p.sendMessage("§cVocê já está no evento.");
								return true;
							}
						} else {
							p.sendMessage("§cO evento sumo não está aberto ou sua entrada foi fechada.");
							return true;
						}
					} else {
						p.sendMessage("§cA entrada do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("Sair")) {
					if (Main.getPlugin().getConfig().getString("Saida") != null) {
						if (SumoManager.manager.getParticipantes().contains(p)) {
							SumoManager.manager.getParticipantes().remove(p);
							for (Player jogadores : Bukkit.getOnlinePlayers()) {
								if (!(jogadores.hasPermission("zs.mod") || jogadores.hasPermission("zs.admin"))) {
									jogadores.showPlayer(p);
								}
							}
							sendTo(p, "Saida");
							disableClanDamage(p);
							p.sendMessage("§eVocê saiu do evento sumo!");
						} else {
							p.sendMessage("§cVocê não está no evento Sumo.");
							return true;
						}
					} else {
						p.sendMessage("§cA saída do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					if (SumoManager.manager.getOcorrendoStatus() == true) {
						p.sendMessage("§a§lSUMO §f- §aInformações");
						p.sendMessage("");
						p.sendMessage("§eParticipantes: §f" + SumoManager.manager.getParticipantes().size());
						p.sendMessage("§eLutas restantes: §f" + ((SumoManager.manager.getParticipantes().size()-1) == -1 ? 0 : SumoManager.manager.getParticipantes().size() - 1));
						p.sendMessage("§eTempo estimado de duração do evento: §f" +  (SumoManager.manager.getParticipantes().size()) + " §fminuto(s)");
						p.sendMessage("");
						return true;
					} else {
						p.sendMessage("§cO evento não está ocorrendo para obter a informação.");
						return true;
					}
				}
			}
		}
		}
			return false;
	}
	
	private void PrepararDuelo() {
		if (SumoManager.manager.getEntradaLiberadaStatus() == false) {
			if (SumoManager.manager.getOponenteX() == null && SumoManager.manager.getOponenteY() == null) {
		if ((!SumoManager.manager.getParticipantes().isEmpty())) {
			if (SumoManager.manager.getParticipantes().size() != 1) {
		SumoManager.manager.setOponenteX(null);
		SumoManager.manager.setOponenteY(null);
		Random random = new Random();
		SumoManager.manager.setOponenteX(SumoManager.manager.getParticipantes().get(random.nextInt(SumoManager.manager.getParticipantes().size())));
		SumoManager.manager.setOponenteY(SumoManager.manager.getParticipantes().get(random.nextInt(SumoManager.manager.getParticipantes().size())));
		
		if (SumoManager.manager.getOponenteX() == SumoManager.manager.getOponenteY() && SumoManager.manager.getParticipantes().size() > 1) {
			
			while (SumoManager.manager.getOponenteX() == SumoManager.manager.getOponenteY()) { 
				SumoManager.manager.setOponenteY(SumoManager.manager.getParticipantes().get(random.nextInt(SumoManager.manager.getParticipantes().size())));
			}
		}
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteX().getName() + " Vs. " + SumoManager.manager.getOponenteY().getName());
		Bukkit.broadcastMessage("");
			
			(new BukkitRunnable() {
				
				@Override
				public void run() {
					if ((SumoManager.manager.getParticipantes().contains(SumoManager.manager.getOponenteX())) 
							&& (SumoManager.manager.getParticipantes().contains(SumoManager.manager.getOponenteY()))) {
						sendToArena(); // Envia para o duelo
						
						//stark task deathmatch
						deathmatch();
						
							enableClanDamage(SumoManager.manager.getOponenteX());
							enableClanDamage(SumoManager.manager.getOponenteY());
								cancel();
					} else {
						PrepararDuelo(); // Sorteia de novo
						cancel();
					}
				}
			}).runTaskTimer(Main.getPlugin(), 0, 120L);
			
		} else {
			final Player vencedor = SumoManager.manager.getParticipantes().get(0);
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§a§l[SUMO] §a§lEVENTO FINALIZADO");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§f" + vencedor.getName() + " §avenceu o evento!");
			//Bukkit.broadcastMessage("§aRecompensa: §fTag §6[Sumo] §7+ §f1.000.000 Coins");
			Bukkit.broadcastMessage("§aCategoria: §fDuelo um contra um.");
			Bukkit.broadcastMessage("§aRecompensa: §6" + premio_message+ "§a Coins");
			Bukkit.broadcastMessage("");
			//darTagSumo(vencedor);
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "coins add " + vencedor.getName() + " " + premio);
			Bukkit.getServer().getConsoleSender().sendMessage("§aRecompensa entregue ao vencedor do sumo.");
			retirarItens(vencedor);
			disableClanDamage(vencedor);
			Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
			SumoStop();
			sendTo(vencedor, "Saida");
			}
		} else {
			if (SumoManager.manager.getEntradaLiberadaStatus() == false) {
				SumoManager.manager.setOcorrendoStatus(false);
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage("§a§l[SUMO] §a§lEVENTO FINALIZADO");
				Bukkit.broadcastMessage("");
				Bukkit.broadcastMessage(" §cAmbos os jogadores na arena deslogaram.");
				Bukkit.broadcastMessage(" §cNão houveram vencedores.");
				Bukkit.broadcastMessage("");
				Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
				SumoStop();
			}
		}
	} 
}
}
	
	private void sendToArena() {
		(new BukkitRunnable() {
			
			@Override
			public void run() {
				if ((SumoManager.manager.getParticipantes().contains(SumoManager.manager.getOponenteX())) && (SumoManager.manager.getParticipantes().contains(SumoManager.manager.getOponenteY()))) {
					removeCheating(SumoManager.manager.getOponenteX());
					removeCheating(SumoManager.manager.getOponenteY());
					sendTo(SumoManager.manager.getOponenteX(), "Pos1");
					sendTo(SumoManager.manager.getOponenteY(), "Pos2");
						for (PotionEffect AllPotionEffects : SumoManager.manager.getOponenteX().getActivePotionEffects()) {
							SumoManager.manager.getOponenteX().removePotionEffect(AllPotionEffects.getType());
						}
						for (PotionEffect AllPotionEffects : SumoManager.manager.getOponenteY().getActivePotionEffects()) {
							SumoManager.manager.getOponenteY().removePotionEffect(AllPotionEffects.getType());
						}
					cancel();
				}
				
			}
		}).runTaskTimer(Main.getPlugin(), 0, 120L);
	}
	
	private void removeCheating(Player p) {
		p.setHealth(20.0D);
	    p.setFoodLevel(20);
	    p.getActivePotionEffects().clear();
	    p.getInventory().clear();
	    p.getInventory().setArmorContents(null);
	    p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
	    p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
	    p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	    p.setAllowFlight(false);
		for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
			p.removePotionEffect(AllPotionEffects.getType());
		}
	}
	
	private void retirarItens(Player p) {
		p.setHealth(20.0D);
	    p.setFoodLevel(20);
	    p.getActivePotionEffects().clear();
	    p.getInventory().clear();
	    p.getInventory().setArmorContents(null);
	    p.setAllowFlight(false);
	}
	
	private void sendTo(Player p, String location) {
		double X = Main.getPlugin().getConfig().getDouble(location + ".X");
        double Y = Main.getPlugin().getConfig().getDouble(location + ".Y");
        double Z = Main.getPlugin().getConfig().getDouble(location + ".Z");
        float Yaw = (float)Main.getPlugin().getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Main.getPlugin().getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Main.getPlugin().getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
	
	
	public static void setLoc(Player p, String LocationName) {
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Main.getPlugin().getConfig().set(LocationName + ".X", Double.valueOf(x));
		Main.getPlugin().getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Main.getPlugin().getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Main.getPlugin().getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Main.getPlugin().getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Main.getPlugin().getConfig().set(LocationName + ".Mundo", world);
		Main.getPlugin().saveConfig();
	}
	
	public void SumoReset() {
		SumoManager.manager.setOcorrendoStatus(true);
		SumoManager.manager.setEntradaLiberadaStatus(true);
		SumoManager.manager.getParticipantes().clear();
		SumoManager.manager.setOponenteX(null);
		SumoManager.manager.setOponenteY(null);
		//SumoManager.manager.setTempo(130);
		SumoManager.manager.setTempo(120);
	}
	
	public void SumoStop() {
		SumoManager.manager.setOcorrendoStatus(false);
		SumoManager.manager.setEntradaLiberadaStatus(false);
		SumoManager.manager.getParticipantes().clear();
		SumoManager.manager.setOponenteX(null);
		SumoManager.manager.setOponenteY(null);
		SumoManager.manager.setTempo(-1);
		Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
	}
	
		//////////////////////////
	
	/*@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Location step = p.getLocation().add(0.0D, -1.0D, 0.0D);
		if ((step.getBlock().getType() == Material.WATER || (step.getBlock().getType() == Material.STATIONARY_WATER))) {
			if (SumoManager.manager.getParticipantes().contains(p)) {
				//SumoManager.manager.getParticipantes().remove(p);
				if (SumoManager.manager.getOponenteX() != null && SumoManager.manager.getOponenteX().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteY().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteX().getName());
					Bukkit.broadcastMessage("");
					sendTo(SumoManager.manager.getOponenteY(), "Entrada");
					SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteX());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(SumoManager.manager.getOponenteX());
					retirarItens(SumoManager.manager.getOponenteY());
					SumoManager.manager.getParticipantes().remove(p);
					sendTo(p, "Saida");
					disableClanDamage(p);
					SumoManager.manager.setOponenteX(null);
					SumoManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				}
				if (SumoManager.manager.getOponenteY() != null && SumoManager.manager.getOponenteY().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteX().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteY().getName());
					Bukkit.broadcastMessage("");
					sendTo(SumoManager.manager.getOponenteX(), "Entrada");
					SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteY());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(SumoManager.manager.getOponenteX());
					retirarItens(SumoManager.manager.getOponenteY());
					SumoManager.manager.getParticipantes().remove(p);
					sendTo(p, "Saida");
					disableClanDamage(p);
					SumoManager.manager.setOponenteX(null);
					SumoManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				}
				
			}
		}
	}*/
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		 if(p.getWorld().getName().equals("PlotMe")) return;
		Location step = p.getLocation().add(0.0D, -1.0D, 0.0D);
		if (e.getFrom().getX() != e.getTo().getX() && (e.getFrom().getZ() != e.getTo().getZ() && 
				(step.getBlock().getType() == Material.WATER) || step.getBlock().getType() == Material.STATIONARY_WATER)) {
			if (SumoManager.manager.getParticipantes().contains(p)) {
				SumoManager sumo = SumoManager.manager;
				if (sumo.getOponenteX() != null && sumo.getOponenteX().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + sumo.getOponenteY().getName() + " §avenceu o duelo contra §f" + sumo.getOponenteX().getName());
					Bukkit.broadcastMessage("");
					sendTo(sumo.getOponenteY(), "Entrada");
					sumo.getParticipantes().remove(sumo.getOponenteX());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(sumo.getOponenteX());
					retirarItens(sumo.getOponenteY());
					sumo.getParticipantes().remove(p);
					sendTo(p, "Saida");
					disableClanDamage(p);
					sumo.setOponenteX(null);
					sumo.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				}
				if (sumo.getOponenteY() != null && sumo.getOponenteY().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + sumo.getOponenteX().getName() + " §avenceu o duelo contra §f" + sumo.getOponenteY().getName());
					Bukkit.broadcastMessage("");
					sendTo(sumo.getOponenteX(), "Entrada");
					SumoManager.manager.getParticipantes().remove(sumo.getOponenteY());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(sumo.getOponenteX());
					retirarItens(sumo.getOponenteY());
					sumo.getParticipantes().remove(p);
					sendTo(p, "Saida");
					disableClanDamage(p);
					sumo.setOponenteX(null);
					sumo.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				}
				
			}
		}
	}
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (SumoManager.manager.getParticipantes().contains(p)) {				
			if (SumoManager.manager.getOponenteX() != null && SumoManager.manager.getOponenteX().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteY().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteX().getName());
					Bukkit.broadcastMessage("");
						sendTo(SumoManager.manager.getOponenteY(), "Entrada");
						SumoManager.manager.getParticipantes().remove(p);
						sendTo(SumoManager.manager.getOponenteX(), "Saida");
							SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteX());
								p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
								retirarItens(SumoManager.manager.getOponenteX());
								retirarItens(SumoManager.manager.getOponenteY());
										sendTo(p, "Saida");
										disableClanDamage(p);
				SumoManager.manager.setOponenteX(null);
				SumoManager.manager.setOponenteY(null);
					PrepararDuelo();
			}
			if (SumoManager.manager.getOponenteY() != null && SumoManager.manager.getOponenteY().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteX().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteY().getName());
					Bukkit.broadcastMessage("");
						sendTo(SumoManager.manager.getOponenteX(), "Entrada");
						SumoManager.manager.getParticipantes().remove(p);
						sendTo(SumoManager.manager.getOponenteY(), "Saida");
							SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteY());
							p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(SumoManager.manager.getOponenteX());
					retirarItens(SumoManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
				SumoManager.manager.setOponenteX(null);
				SumoManager.manager.setOponenteY(null);
					PrepararDuelo();
			} else {
				SumoManager.manager.getParticipantes().remove(p);
				disableClanDamage(p);
				sendTo(p, "Saida");
			}
		}
	}
	
	@EventHandler
	public void onRip(PlayerDeathEvent e) {
		Player p = e.getEntity();
			if (SumoManager.manager.getParticipantes().contains(p)) {
				if (SumoManager.manager.getOponenteX() != null && SumoManager.manager.getOponenteX().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteY().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteX().getName());
					Bukkit.broadcastMessage("");
					sendTo(SumoManager.manager.getOponenteY(), "Entrada");
					SumoManager.manager.getParticipantes().remove(p);
					sendTo(SumoManager.manager.getOponenteX(), "Saida");
					SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteX());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(SumoManager.manager.getOponenteX());
					retirarItens(SumoManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
					SumoManager.manager.setOponenteX(null);
					SumoManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				}
				if (SumoManager.manager.getOponenteY() != null && SumoManager.manager.getOponenteY().equals(p)) {
					Bukkit.broadcastMessage("");
					Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteX().getName() + " §avenceu o duelo contra §f" + SumoManager.manager.getOponenteY().getName());
					Bukkit.broadcastMessage("");
					sendTo(SumoManager.manager.getOponenteX(), "Entrada");
					SumoManager.manager.getParticipantes().remove(p);
					sendTo(SumoManager.manager.getOponenteY(), "Saida");
					SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteY());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(SumoManager.manager.getOponenteX());
					retirarItens(SumoManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
					SumoManager.manager.setOponenteX(null);
					SumoManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getPlugin(), 120L, 0);
				} //else {
				  //	sendTo(p, "Entrada");
				  //}
			}
		}
	  
	  @EventHandler
	  public void blockCMD(PlayerCommandPreprocessEvent e) {
		  Player p = e.getPlayer();
		  if (SumoManager.manager.getOponenteX() != null && SumoManager.manager.getOponenteY() != null) {
		  if ((SumoManager.manager.getOponenteX().equals(p) || SumoManager.manager.getOponenteY().equals(p)) && e.getMessage().startsWith("/sumo")) {
			  e.setCancelled(true);
			  p.sendMessage("§cVocê não pode digitar este comando enquanto está duelando.");
		  }
	  }
  }
	
	  public void deathmatch() {
		
		(new BukkitRunnable() {
			
			int tempo = 60;
			
			@Override
			public void run() {
				if (SumoManager.manager.getOponenteX() != null && SumoManager.manager.getOponenteY() != null) {
					ActionBarAPI.sendActionBarMessage(SumoManager.manager.getOponenteX(), "§a§l[SUMO] §aDeathMatch em: §c" + tempo + " §csegundos");
					ActionBarAPI.sendActionBarMessage(SumoManager.manager.getOponenteY(), "§a§l[SUMO] §aDeathMatch em: §c" + tempo + " §csegundos");
						for (Player staff : verDM) {
							ActionBarAPI.sendActionBarMessage(staff, "§a§l[SUMO] §aDeathMatch em: §c" + tempo + " §csegundos");
						}
					tempo--;
					if (tempo == 25) {
						SumoManager.manager.getOponenteX().getInventory().addItem(giveKnockBackStick());
						SumoManager.manager.getOponenteY().getInventory().addItem(giveKnockBackStick());
						return;
					}
					if (tempo == 0) {
						SumoManager.manager.getOponenteX().getInventory().clear();
							SumoManager.manager.getOponenteY().getInventory().clear();
						SumoManager.manager.getOponenteX().getInventory().setArmorContents(null);
							SumoManager.manager.getOponenteY().getInventory().setArmorContents(null);
							
							SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteX());
								SumoManager.manager.getParticipantes().remove(SumoManager.manager.getOponenteY());
						SumoManager.manager.getOponenteX().getWorld().spawnEntity(SumoManager.manager.getOponenteX()
								.getLocation(), EntityType.FIREWORK);
						SumoManager.manager.getOponenteY().getWorld().spawnEntity(SumoManager.manager.getOponenteY()
								.getLocation(), EntityType.FIREWORK);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§a§l[SUMO] §f" + SumoManager.manager.getOponenteX().getName() + " §7e §f" + SumoManager.manager.getOponenteY().getName() + " §fforam eliminados pelo §cdeath-match.");
						Bukkit.broadcastMessage("");
							Player opX = SumoManager.manager.getOponenteX();
							Player opY = SumoManager.manager.getOponenteY();
								SumoManager.manager.setOponenteX(null);
								SumoManager.manager.setOponenteY(null);
							opX.setHealth(0); 
							opY.setHealth(0);
						PrepararDuelo();
						cancel();
					}
				} else {
					cancel();
				}
		

			//	PrepararDuelo();
			//	cancel();
				
			}
		}).runTaskTimer(Main.getPlugin(), 0, 20L);
	}
	
		public static SimpleClans getSC() {
			return SimpleClans.getInstance();
		}
		  
		public static void enableClanDamage(Player p) {
			if (getSC().getClanManager().getClanPlayer(p) != null)
				getSC().getClanManager().getClanPlayer(p).setFriendlyFire(true); 
		}
		  
		public static void disableClanDamage(Player p) {
			if (getSC().getClanManager().getClanPlayer(p) != null)
				getSC().getClanManager().getClanPlayer(p).setFriendlyFire(false); 
		}
		
		public ItemStack giveKnockBackStick() {
			ItemStack item = new ItemStack(Material.BLAZE_ROD);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§cKnockback - DEATH MATCH");
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			return item;
		}
		
/*		 @EventHandler
		 public void onTeleport(PlayerTeleportEvent e) {
	    	Player p = e.getPlayer();
			if (SumoManager.manager.getParticipantes().contains(p) && !p.hasPermission("zs.admin")) {
	    		p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
	    		e.setCancelled(true);
	    		if (SumoManager.manager.getOcorrendoStatus() == false) {
	    			sendTo(p, "Saida");
	    			SumoManager.manager.getParticipantes().remove(p);
	    		}
	    	}
	    }	*/
		
		 @EventHandler
		 public void worldChange(PlayerChangedWorldEvent e) {
			final Player p = e.getPlayer();
			if (SumoManager.manager.getOcorrendoStatus() == true && SumoManager.manager.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
				sendTo(p, "Entrada");
				p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
			}
		 }


		/*
		private void darTagSumo(Player p) {
			Main.getPlugin().getConfig().getString("vencedor").replace(Main.getPlugin().getConfig().getString("vencedor"), "");
			Main.getPlugin().getConfig().set("vencedor", p.getName());
			Main.getPlugin().saveConfig();
		}
		
		@EventHandler
		private void onChat(ChatMessageEvent e) {
			Player p = (Player) e.getSender();
		    if(e.getTags().contains("sumotag") && Main.getPlugin().getConfig().getString("vencedor") != null 
		    		&& Main.getPlugin().getConfig().getString("vencedor").equals(p.getName())) 
		        e.setTagValue("sumotag","&6[Sumo] ");
		}
		*/
	
}
