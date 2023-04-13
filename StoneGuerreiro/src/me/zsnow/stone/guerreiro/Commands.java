package me.zsnow.stone.guerreiro;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.stone.guerreiro.manager.Configs;
import me.zsnow.stone.guerreiro.manager.EventManager;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("guerreiro")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§a§lGUERREIRO §c- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§e/guerreiro forcestart");
					sender.sendMessage("§e/guerreiro forcestop");
					sender.sendMessage("");
					return true;
				}
				 if (args[0].equalsIgnoreCase("forcestart")) {
					  if (sender.hasPermission("zs.admin")) {
						  if (EventManager.manager.getEventoOcorrendo() == false) {
							  EventManager.manager.getParticipantes().clear();
							  EventManager.manager.setPvPStatus(false);
							  EventManager.manager.setEntradaLiberada(true);
							  EventManager.manager.setEventoOcorrendo(true);
							  EventManager.manager.setTempo(Configs.config.getInt("tempo-para-entrar"));
							 // EventManager.manager.setTempo(60);

							  (new BukkitRunnable() {
								
								@Override
								public void run() {
									if (EventManager.manager.getEventoOcorrendo() == true) {
										if (EventManager.manager.getTempo() > 1) {
											  for (String msg : Configs.config.getConfig().getStringList("broadcast.start")) {
												  Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&', msg
													.replace("{tempo}", String.valueOf(EventManager.manager.getTempo()))
													.replace("{numero_de_participantes}", String.valueOf(EventManager.manager.getParticipantes().size()))
													.replace("{max_players}", String.valueOf(maxPlayer)))));
											  }
											  
											EventManager.manager.setTempo(EventManager.manager.getTempo() - 10);

										} else {
											if (EventManager.manager.getParticipantes().size() >= 10) {
												for (String msg : Configs.config.getConfig().getStringList("broadcast.enter-closed")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
													.replace("{numero_de_participantes}", String.valueOf(EventManager.manager.getParticipantes().size()))
													.replace("{max_players}", String.valueOf(maxPlayer))));
												}
												EventManager.manager.setEntradaLiberada(false);
												StartTaskToPvP();
												cancel();
											} else {
												cancel();
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§a§lGUERREIRO §7- §cEVENTO CANCELADO.");
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§cO evento precisa ter no mínimo 10 jogadores para iniciar.");
												Bukkit.broadcastMessage("");
												  EventManager.manager.setPvPStatus(false);
												  EventManager.manager.setEntradaLiberada(false);
												  EventManager.manager.setEventoOcorrendo(false);
												  for (Player jogadores : EventManager.manager.getParticipantes()) {
														jogadores.getInventory().setArmorContents(null);
															jogadores.getInventory().clear();
																EventManager.manager.sendTo(jogadores, "saida");
														for (PotionEffect allPotionEffects : jogadores.getActivePotionEffects()) {
															jogadores.removePotionEffect(allPotionEffects.getType());
														}
													}
													EventManager.manager.getParticipantes().clear();
											}
										}
									} else {
										cancel();
									}
								}
							}).runTaskTimer(Main.getInstance(), 0L, 200L);
								
						  } else {
							  sender.sendMessage("§cO evento já está ocorrendo.");
							  return true;
						  }
					  } else {
						  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
						  return true;
					  }
				  }
				  if (args[0].equalsIgnoreCase("forcestop")) {
					  if (sender.hasPermission("zs.admin")) {
						  if (EventManager.manager.getEventoOcorrendo() == true) {
								EventManager.manager.setEntradaLiberada(false);
								EventManager.manager.setEventoOcorrendo(false);
								EventManager.manager.setPvPStatus(false);
								for (String msg : Configs.config.getConfig().getStringList("broadcast.force-stop")) {
									Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
								}
								for (Player jogadores : EventManager.manager.getParticipantes()) {
									jogadores.getInventory().setArmorContents(null);
										jogadores.getInventory().clear();
											EventManager.manager.sendTo(jogadores, "saida");
											EventManager.manager.disableClanDamage(jogadores);
									for (PotionEffect allPotionEffects : jogadores.getActivePotionEffects()) {
										jogadores.removePotionEffect(allPotionEffects.getType());
									}
								}
								EventManager.manager.getParticipantes().clear();
								return true;
						  } else {
							  sender.sendMessage("§cO evento não está ocorrendo.");
							  return true;
						  }
					  }  else {
						  sender.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
						  return true;
					  }
				  }
			} else {
		Player p = (Player) sender;
		if (args.length == 0) {
			if (p.hasPermission("zs.mod")) {
				p.sendMessage("");
				p.sendMessage("§a§lGUERREIRO §c- §fComandos");
				p.sendMessage("");
				p.sendMessage("§e/guerreiro entrar");
				p.sendMessage("§e/guerreiro sair");
				p.sendMessage("§e/guerreiro info");
				p.sendMessage("§c/guerreiro moderar");
				p.sendMessage("§c/guerreiro modsair");
				p.sendMessage("§c/guerreiro setPvP <on/off>");
				p.sendMessage("§c/guerreiro kick <player>"); //boolean na config se mostra o kick ou n
				p.sendMessage("§c/guerreiro forceStart - §ninicia o evento.");
				p.sendMessage("§c/guerreiro forceStop - §nencerra o evento.");
				p.sendMessage("§c/guerreiro setpos <entrada/saida>"); // ao executar arg[0] checa a perm 
				p.sendMessage("§c/guerreiro verlist"); // ao executar arg[0] checa a perm 
				p.sendMessage("");
				return true;
			} else {
				p.sendMessage("");
				p.sendMessage("§a§lGUERREIRO §c- §fComandos");
				p.sendMessage("");
				p.sendMessage("§e/guerreiro entrar");
				p.sendMessage("§e/guerreiro sair");
				p.sendMessage("§e/guerreiro info");
				p.sendMessage("");
				return true;
			}
		}   /// p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("")));
		
		// add limite de jogadores na arena
		if (args.length == 1) {  
			if (args[0].equalsIgnoreCase("entrar")) {
				if (!EventManager.manager.getParticipantes().contains(p)) {
					if (EventManager.manager.getEventoOcorrendo() == true && EventManager.manager.getEntradaLiberada() == true) {
						//if (Configs.locations.getConfig().getString("entrada").toUpperCase() != null) {
							if (!(EventManager.manager.getParticipantes().size() == maxPlayer)) {
								if (p.hasPermission("guerreiro.entrar")) {
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
								//
								EventManager.manager.sendTo(p, "Entrada");
								EventManager.manager.getParticipantes().add(p);
								EventManager.manager.enableClanDamage(p);
								p.sendTitle("", "§c§lProibido time/aliança.");
								p.chat("/on");
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.on-join")));
								if (Configs.config.getConfig().getBoolean("sounds.on-enter") == true) {
									p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("sounds.on-enter-sound").toUpperCase()), 1.0F, 0.5F);
								}
							    int loop;
							    for (loop = 0; loop <= 35; loop++) {
							      p.getInventory().setItem(loop, Configs.itens.getItemStack("Itens.Slot." + loop)); 
							      p.updateInventory();
							    }
							    for (loop = 36; loop <= 39; loop++) {
							      p.getInventory().setItem(loop, Configs.itens.getItemStack("Armadura.Slot." + loop));
							      p.updateInventory();
							    }
								return true;
							} else {
								p.sendMessage("§cVocê precisa do rank [Vênus]§c ou superior para participar do evento.");
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
								return true;
							}
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.evento-lotado").replace("{limite}",String.valueOf(maxPlayer))));
							return true;
						}
						//} else {
						//	p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.entrada-nao-definida")));
						//	return true;
						//}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.evento-nao-ocorrendo")));
						return true;
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.voce-ja-esta-no-evento")));
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("sair")) {
				if (EventManager.manager.getParticipantes().contains(p)) {
						//if (Configs.locations.getConfig().getString("Saida").toUpperCase() != null) {
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						EventManager.manager.getParticipantes().remove(p);
							EventManager.manager.sendTo(p, "Saida");
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.saiu-do-evento")));
								if (Configs.config.getConfig().getBoolean("sounds.on-exit") == true) {
									p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("sounds.on-exit-sound").toUpperCase()), 1.0F, 0.5F);
								}
								for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
									p.removePotionEffect(AllPotionEffects.getType());
								}
								defineWinner();
								EventManager.manager.disableClanDamage(p);
						//} else {
						//	p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.saida-nao-definida")));
						//	return true;
						//}
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("messages.voce-nao-esta-no-evento")));
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("info")) {
				p.sendMessage("");
				p.sendMessage("§a§lGUERREIRO §d- §d§lINFOS");
				p.sendMessage("");
				p.sendMessage("§2Ocorrendo: §a" + EventManager.manager.getEventoOcorrendo());
				p.sendMessage("§2PvP status: §a" + EventManager.manager.getPvPStatus());
				p.sendMessage("§2Participantes: §a" + EventManager.manager.getParticipantes().size());
				p.sendMessage("");
				return true;
				}
			if (args[0].equalsIgnoreCase("reload")) {
				if (p.hasPermission("zs.admin")) {
					Configs.config.saveDefaultConfig();
					Configs.config.reloadConfig();
					p.sendMessage("§eConfiguração recarregada.");
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("moderar")) {
				if (p.hasPermission("zs.mod")) {
					EventManager.manager.sendTo(p, "entrada");
						p.sendMessage("§eVocê foi enviado para a área de moderação do guerreiro.");
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
						return true;
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-MOD")));
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("modsair")) {
				if (p.hasPermission("zs.mod")) {
					EventManager.manager.sendTo(p, "saida");
						p.sendMessage("§eVocê foi enviado para a saída do guerreiro.");
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
						return true;
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-MOD")));
						return true;
					}
				}
			}
			if (args[0].equalsIgnoreCase("verlist")) {
				if (p.hasPermission("zs.admin")) {
					StringBuilder sb = new StringBuilder();
					String separe = "";
					p.sendMessage("");
					p.sendMessage("§c[Guerreiro] jogadores:");
					for (Player jogadores : EventManager.manager.getParticipantes()) {
						sb.append(separe);
						separe = ", ";
						sb.append(jogadores.getName());
					}
					if (EventManager.manager.getParticipantes().size() == 0) {
						p.sendMessage(" §eVazio...");
						p.sendMessage("");
					} else {
						p.sendMessage("§7[" + sb.toString() + "§7]");
						p.sendMessage("");
					}
				} else {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("salvaritem")) {
				if (p.hasPermission("zs.gerente")) {
		              int loop;
		              for (loop = 0; loop <= 35; loop++)
		               Configs.itens.set("Itens.Slot." + loop, p.getInventory().getItem(loop)); 
		              for (loop = 36; loop <= 39; loop++)
		                Configs.itens.set("Armadura.Slot." + loop, p.getInventory().getItem(loop)); 
		              p.sendMessage("§eOs item do evento Guerreiro foram definidos com sucesso!");
		              p.getInventory().clear();
		              p.getInventory().setArmorContents(null);
		              Configs.itens.saveConfig();
		              return true;
				} else {
					p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("editaritem")) {
				if (p.hasPermission("zs.gerente")) {
				    int loop;
					    for (loop = 0; loop <= 35; loop++) {
					      p.getInventory().setItem(loop, Configs.itens.getItemStack("Itens.Slot." + loop)); 
					      p.updateInventory();
					    }
					    for (loop = 36; loop <= 39; loop++) {
					      p.getInventory().setItem(loop, Configs.itens.getItemStack("Armadura.Slot." + loop));
					      p.updateInventory();
					    }
				    p.sendMessage("§eOs item do evento Guerreiro foram entregues!");
	                return true;
				} else {
					p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
					return true;
				}
			}
		  if (args[0].equalsIgnoreCase("forcestart")) {
			  if (p.hasPermission("zs.admin")) {
				  if (EventManager.manager.getEventoOcorrendo() == false) {
					  EventManager.manager.getParticipantes().clear();
					  EventManager.manager.setPvPStatus(false);
					  EventManager.manager.setEntradaLiberada(true);
					  EventManager.manager.setEventoOcorrendo(true);
					  EventManager.manager.setTempo(Configs.config.getInt("tempo-para-entrar"));
					 // EventManager.manager.setTempo(60);

					  (new BukkitRunnable() {
						
						@Override
						public void run() {
							if (EventManager.manager.getEventoOcorrendo() == true) {
								if (EventManager.manager.getTempo() > 1) {
									  for (String msg : Configs.config.getConfig().getStringList("broadcast.start")) {
										  Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&', msg
											.replace("{tempo}", String.valueOf(EventManager.manager.getTempo()))
											.replace("{numero_de_participantes}", String.valueOf(EventManager.manager.getParticipantes().size()))
											.replace("{max_players}", String.valueOf(maxPlayer)))));
									  }
									  
									EventManager.manager.setTempo(EventManager.manager.getTempo() - 10);

								} else {
									if (EventManager.manager.getParticipantes().size() >= 10) {
										for (String msg : Configs.config.getConfig().getStringList("broadcast.enter-closed")) {
										Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
											.replace("{numero_de_participantes}", String.valueOf(EventManager.manager.getParticipantes().size()))
											.replace("{max_players}", String.valueOf(maxPlayer))));
										}
										EventManager.manager.setEntradaLiberada(false);
										StartTaskToPvP();
										cancel();
									} else {
										cancel();
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("§a§lGUERREIRO §7- §cEVENTO CANCELADO.");
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("§cO evento precisa ter no mínimo 10 jogadores para iniciar.");
										Bukkit.broadcastMessage("");
										  EventManager.manager.setPvPStatus(false);
										  EventManager.manager.setEntradaLiberada(false);
										  EventManager.manager.setEventoOcorrendo(false);
										  for (Player jogadores : EventManager.manager.getParticipantes()) {
												jogadores.getInventory().setArmorContents(null);
													jogadores.getInventory().clear();
														EventManager.manager.sendTo(jogadores, "saida");
												for (PotionEffect allPotionEffects : jogadores.getActivePotionEffects()) {
													jogadores.removePotionEffect(allPotionEffects.getType());
												}
											}
											EventManager.manager.getParticipantes().clear();
									}
								}
							} else {
								cancel();
							}
						}
					}).runTaskTimer(Main.getInstance(), 0L, 200L);
						
				  } else {
					  p.sendMessage("§cO evento já está ocorrendo.");
					  return true;
				  }
			  } else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
				  return true;
			  }
		  }
		  if (args[0].equalsIgnoreCase("forcestop")) {
			  if (p.hasPermission("zs.admin")) {
				  if (EventManager.manager.getEventoOcorrendo() == true) {
						EventManager.manager.setEntradaLiberada(false);
						EventManager.manager.setEventoOcorrendo(false);
						EventManager.manager.setPvPStatus(false);
						for (String msg : Configs.config.getConfig().getStringList("broadcast.force-stop")) {
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
						}
						for (Player jogadores : EventManager.manager.getParticipantes()) {
							jogadores.getInventory().setArmorContents(null);
								jogadores.getInventory().clear();
									EventManager.manager.sendTo(jogadores, "saida");
									EventManager.manager.disableClanDamage(jogadores);
							for (PotionEffect allPotionEffects : jogadores.getActivePotionEffects()) {
								jogadores.removePotionEffect(allPotionEffects.getType());
							}
						}
						EventManager.manager.getParticipantes().clear();
						return true;
				  } else {
					  p.sendMessage("§cO evento não está ocorrendo.");
					  return true;
				  }
			  }  else {
				  p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
				  return true;
			  }
		  }
		  
		
		if (args.length == 2) {
			if (p.hasPermission("zs.admin")) {
				if (args[0].equalsIgnoreCase("setPvP")) {
					if (EventManager.manager.getEventoOcorrendo() == true && EventManager.manager.getEntradaLiberada() == false) {
						if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
							if (EventManager.manager.getPvPStatus() != true) {
								EventManager.manager.setPvPStatus(true);
								for (Player jogadores : EventManager.manager.getParticipantes()) {
								      for (String msg : Configs.config.getConfig().getStringList("enable-pvp-message")) {
								    	  jogadores.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								    		 jogadores.playSound(jogadores.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("sound-on-change-pvp-status").toUpperCase()), 1.0F, 0.5F);
								      }
								}
								for (Player staff : Bukkit.getOnlinePlayers()) {
									if (staff.hasPermission("zs.mod") || staff.hasPermission("zs.admin") 
											&& !EventManager.manager.getParticipantes().contains(staff)) {
										for (String msg : Configs.config.getConfig().getStringList("enable-pvp-message")) {
											staff.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
										}
									}
								}
							} else {
								p.sendMessage("§a[Guerreiro] §cO pvp já está ativado.");
								return true;
							}
						}
						if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
							//List<String> message = Configs.config.getConfig().getStringList("disable-pvp-message");
							if (EventManager.manager.getPvPStatus() != false) {
								EventManager.manager.setPvPStatus(false);
								for (Player jogadores : EventManager.manager.getParticipantes()) {
									for (String msg : Configs.config.getConfig().getStringList("disable-pvp-message")) {
								    	  jogadores.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								    		 jogadores.playSound(jogadores.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("sound-on-change-pvp-status").toUpperCase()), 1.0F, 0.5F);
								      }
								}
								for (Player staff : Bukkit.getOnlinePlayers()) {
									if (staff.hasPermission("zs.mod") || staff.hasPermission("zs.admin") && 
											!EventManager.manager.getParticipantes().contains(staff)) {
										for (String msg : Configs.config.getConfig().getStringList("disable-pvp-message")) {
											staff.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
										}
									}
								}
							} else {
								p.sendMessage("§a[Guerreiro] §cO pvp já está desativado.");
								return true;
							}
						}
					} else {
						p.sendMessage("§cVocê ainda não pode alterar o status de pvp do evento, aguarde o início oficial do pvp.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("kick")) {
					Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							p.sendMessage("§cO jogador especificado não se encontra online.");
							return true;
						}
					if (EventManager.manager.getParticipantes().contains(target)) {
						target.getInventory().clear();
						EventManager.manager.getParticipantes().remove(target);
						EventManager.manager.sendTo(target, "Saida");
						EventManager.manager.disableClanDamage(target);
						target.getInventory().setArmorContents(null);
							for (PotionEffect allPotionEffects : target.getActivePotionEffects()) {
								target.removePotionEffect(allPotionEffects.getType());
							}
						if (Configs.config.getBoolean("broadcast-on-kick") == true) {
							Bukkit.broadcastMessage(Configs.config.getConfig().getString("player-kicked-message").replace("{player}", target.getName()));
						}
						defineWinner();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("on-kick-message").replace("{player}", target.getName())));
						return true;
					} else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("invalid-kick")));
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("setpos")) {
					if (args[1].equalsIgnoreCase("entrada")) {
						EventManager.manager.setLoc(p, "entrada");
						p.sendMessage("§eVocê definiu a entrada do guerreiro.");
					}
					if (args[1].equalsIgnoreCase("Saida")) {
						EventManager.manager.setLoc(p, "saida");
						p.sendMessage("§eVocê definiu a saída do guerreiro.");
						return true;
					} else {
						p.sendMessage("§a§l[guerreiro] §cUtilize: /guerreiro setpos <Entrada/Saida>");
						return true;
					}
				}
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("NO-PERM-ADMIN")));
				return true;
			}
		}
		}
		}
		return false;
	}
	public void StartTaskToPvP() {
		(new BukkitRunnable() {
			int tempo = Configs.config.getConfig().getInt("time.pvp");
			
			@Override
			public void run() {
				if (EventManager.manager.getEventoOcorrendo() == true) {
					if (tempo > 1) {
						for (String msg : Configs.config.getConfig().getStringList("broadcast.waiting-for-pvp")) {
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
								.replace("{tempo_to_pvp}", String.valueOf(tempo))));
								tempo = (tempo - 10);
						}
					} else {
						for (String msg : Configs.config.getConfig().getStringList("broadcast.pvp-enabled")) {
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
						}
							for (Player participantes : EventManager.manager.getParticipantes()) {
								for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
									participantes.removePotionEffect(AllPotionEffects.getType());
								}
							}
							EventManager.manager.setPvPStatus(true);
							cancel();
						}
					} else {
						cancel();
					}
				}
			}).runTaskTimer(Main.getInstance(), 0L, 200L);
		}
	

	
	public void equipArmors(Player p) {
		ItemStack helmet = new ItemStack(Material.IRON_HELMET);
		ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		ItemStack leggings = new ItemStack(Material.IRON_LEGGINGS);
		ItemStack boots = new ItemStack(Material.IRON_BOOTS);
		helmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		chestplate.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		leggings.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		boots.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(leggings);
		p.getInventory().setBoots(boots);
	}
	
	public void giveSwordAndPots(Player p) {
		ItemStack sword = new ItemStack(Material.IRON_SWORD);
		sword.addEnchantment(Enchantment.DURABILITY, 3);
		p.getInventory().addItem(sword);
		ItemStack cap = new ItemStack(Material.GOLDEN_APPLE, 10);
		p.getInventory().addItem(cap);
	}
	
	 public void defineWinner() {
		 if (EventManager.manager.getParticipantes().size() == 1 && EventManager.manager.getEntradaLiberada() == false) {
			 Player vencedor = EventManager.manager.getParticipantes().get(0);
			 for (String msg : Configs.config.getConfig().getStringList("broadcast.end-event")) {
				 Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{vencedor}", vencedor.getName())));
			 }
			 for (String cmd : Configs.config.getConfig().getStringList("premio")) {
				 Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd.replace("{vencedor}", vencedor.getName()));
		 	 }
			 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
				 vencedor.removePotionEffect(allPotionEffects.getType());
			 }
			 vencedor.getInventory().setArmorContents(null);
			 vencedor.getInventory().clear();
			  EventManager.manager.setPvPStatus(false);
			  EventManager.manager.setEntradaLiberada(false);
			  EventManager.manager.setEventoOcorrendo(false);
			 EventManager.manager.sendTo(vencedor, "Saida");
			 EventManager.manager.getParticipantes().clear();
		 }
	 }
}
