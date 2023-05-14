package br.com.zsnow.eventocustom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commands implements CommandExecutor {
	
	BossManager boss = new BossManager();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("evento")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(Main.prefix + ": §eComando executavel por jogadores in-game");
				return true;
			}
			Player p = (Player) sender;
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					if (!(Main.participantes.contains(p))) {
						if (Main.EventoOcorrendo == true) {
							if (Main.EntradaLiberada == true) {
								//if (!(p.hasPermission("customevent.staff"))) {
									if (p.hasPermission("extras.entrar")) {
										PlayerInventory inv = p.getInventory();
											for (ItemStack i : inv.getContents()) {
												if(i != null && !(i.getType() == Material.AIR)) {
													p.sendMessage("§cEsvazie seu inventário para entrar no CustomEvent.");
													return true;
												} 
											} for (ItemStack i : inv.getArmorContents()) {
													if(i != null && !(i.getType() == Material.AIR)) {
														p.sendMessage("§cRetire sua armadura para entrar no CustomEvent.");
														return true;
											}
										}
								//	}
									getLoc.sendEnter(p);
									Main.participantes.add(p);
									p.sendMessage(Main.prefix + "§7§l: §eVocê entrou no evento.");
			            				for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
			            					p.removePotionEffect(AllPotionEffects.getType());
			            				}
									} else {
										p.sendMessage("§cVocê precisa do rank §7[EsqueletoIII] §cou superior para participar do evento.");
										return true;
									}
							return true;
						} else {
							p.sendMessage(Main.prefix + "§7§l: §cA entrada para o evento já está fechada para novos participantes.");
							return true;
						}
					} else {
						p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
						return true;
					}
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cVocê já está no evento.");
					return true;
				}
			} if (args[0].equalsIgnoreCase("sair")) {
					if (Main.participantes.contains(p)) {
						if (Main.EventoOcorrendo == true) {
							Main.participantes.remove(p);
							p.sendMessage(Main.prefix + "§7§l: §eVocê saiu do evento.");
								getLoc.sendExit(p);
								for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
	            					p.removePotionEffect(AllPotionEffects.getType());
	            				}
								return true;
						} else {
							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
							return true;
						}
					} else {
						p.sendMessage(Main.prefix + "§7§l: §cVocê não está no evento.");
						return true;
					}
				} if (args[0].equalsIgnoreCase("jogadores")) {
					p.sendMessage(Main.prefix + "§7§l: §eJogadores participando do evento: §f" + Main.participantes.size() + "§e.");
					return true;
				}
			}
			if (args.length == 0) {
				if (!(p.hasPermission("customevent.staff"))) {
					p.sendMessage(Main.prefix + " §7(Comandos)");
					p.sendMessage("§7* §e/evento entrar");
					p.sendMessage("§7* §e/evento sair");
					p.sendMessage("§7* §e/evento jogadores");
					p.sendMessage(" ");
					p.sendMessage("§6* §eEste sistema é autêntico da rede RedeStone.");
					p.sendMessage("§eDesenvolvido por: §3@§bzSnowReach§e.");
					return true;
				}
				p.sendMessage(Main.prefix + " §7(Comandos)");
				p.sendMessage("§7* §e/evento entrar");
				p.sendMessage("§7* §e/evento sair");
				p.sendMessage("§7* §e/evento jogadores");
				p.sendMessage("§7* §c/evento setentrada.");
				p.sendMessage("§7* §c/evento setsaida.");
				p.sendMessage("§7* §c/evento Comandos.");
				p.sendMessage(" ");
				p.sendMessage("§6* §eEste sistema é autêntico da rede RedeStone.");
				p.sendMessage("§eDesenvolvido por: §3@§bzSnowReach§e.");
				return true;
			}
		if (p.hasPermission("customevent.staff")) {
			if (args[0].equalsIgnoreCase("sair")) {
				if (Main.participantes.contains(p)) {
					if (Main.EventoOcorrendo == true) {
						Main.participantes.remove(p);
						p.sendMessage(Main.prefix + "§7§l: §eVocê saiu do evento.");
							getLoc.sendExit(p);
								return true;
					} else {
						p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
						return true;
					}
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cVocê não está no evento.");
					return true;
				}
			} if (args[0].equalsIgnoreCase("pvp")) { 
				if (args.length <= 1) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent pvp [Ativar/Desativar]");
					return true;
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("desativar")) {
						if (Main.EventoOcorrendo == true) {
							Main.pvp = false;
							for (Player jogadores : Main.participantes) {
									jogadores.sendMessage(Main.prefix + "§7§l: §eO PvP no evento foi §c§lDESATIVADO§e.");
									jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
							if (!(Main.participantes.contains(p))) {
								p.sendMessage(Main.prefix + "§7§l: §eO PvP no evento foi §c§lDESATIVADO§e.");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								return true;
							}
						} else {
							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
							return true;
						}
					}if (args[1].equalsIgnoreCase("ativar")) {
						if (Main.EventoOcorrendo == true) {
							Main.pvp = true;
							for (Player jogadores : Main.participantes) {
									jogadores.sendMessage(Main.prefix + "§7§l: §eO PvP no evento foi §a§lATIVADO§e.");
									jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
							}
							if (!(Main.participantes.contains(p))) {
								p.sendMessage(Main.prefix + "§7§l: §eO PvP no evento foi §a§lATIVADO§e.");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								return true;
							}
						} else {
							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
							return true;
						}
					}
				}
			} if (args[0].equalsIgnoreCase("blocos")) { 
				if (args.length <= 2) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent blocos [Quebrar/Colocar] [Ativar/Desativar]");
					return true;
					
				} if (args.length == 3) {
					if (args[1].equalsIgnoreCase("quebrar")) {
						if (args[2].equalsIgnoreCase("desativar")) {
							if (Main.EventoOcorrendo == true) {
								Main.Quebrarblocos = true;
								for (Player jogadores : Main.participantes) {
										jogadores.sendMessage(Main.prefix + "§7§l: §eQuebrar blocos no evento foi §c§lDESATIVADO§e.");
										jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
								if (!(Main.participantes.contains(p))) {
									p.sendMessage(Main.prefix + "§7§l: §eQuebrar blocos no evento foi §c§lDESATIVADO§e.");
									p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
									return true;
								}
							} else {
								p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
								return true;
							}
						}if (args[2].equalsIgnoreCase("ativar")) {
							if (Main.EventoOcorrendo == true) {
								Main.Quebrarblocos = false;
								for (Player jogadores : Main.participantes) {
										jogadores.sendMessage(Main.prefix + "§7§l: §eQuebrar blocos no evento foi §a§lATIVADO§e.");
										jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
								if (!(Main.participantes.contains(p))) {
									p.sendMessage(Main.prefix + "§7§l: §eQuebrar blocos no evento foi §a§lATIVADO§e.");
									p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
									return true;
								}
							} else {
								p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
								return true;
							}
						}
					} if (args[1].equalsIgnoreCase("Colocar")) {
						if (args[2].equalsIgnoreCase("desativar")) {
							if (Main.EventoOcorrendo == true) {
								Main.Colocarblocos = true;
								for (Player jogadores : Main.participantes) {
										jogadores.sendMessage(Main.prefix + "§7§l: §eColocar blocos no evento foi §c§lDESATIVADO§e.");
										jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
								if (!(Main.participantes.contains(p))) {
									p.sendMessage(Main.prefix + "§7§l: §eColocar blocos no evento foi §c§lDESATIVADO§e.");
									p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
									return true;
								}
							} else {
								p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
								return true;
							}
						}if (args[2].equalsIgnoreCase("ativar")) {
							if (Main.EventoOcorrendo == true) {
								Main.Colocarblocos = false;
								for (Player jogadores : Main.participantes) {
										jogadores.sendMessage(Main.prefix + "§7§l: §eColocar blocos no evento foi §a§lATIVADO§e.");
										jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
								if (!(Main.participantes.contains(p))) {
									p.sendMessage(Main.prefix + "§7§l: §eColocar blocos no evento foi §a§lATIVADO§e.");
									p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
									return true;
								}
							} else {
								p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
								return true;
							}
						}
					}
				}
			}
			//evc boss <setName/Life/addEffect/setArmour/setItemInHand/setReward> <NAME/HP/EFEITO/setArmour/Item/ItemPremio>
			// ELE N SETA SETANDO AS PARADA NA VARIAVEL
	/*		if (args[0].equalsIgnoreCase("createBoss")) {
				if (args.length == 1) {
					p.sendMessage(Main.prefix + 
					"§7§l: §cUse /EventoCustom createBoss <info/setName/setHP/addEffect/removeEffect/setArmour/setItemInHand/setReward>");
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("setArmour")) {
						boss.SetArmours(null);
						ItemStack[] armaduras = p.getInventory().getArmorContents();
						boss.SetArmours(armaduras);
						p.sendMessage(Main.prefix + "§7§l: §eA armadura do boss foi definida baseada na sua.");
						return true;
				} if (args[1].equalsIgnoreCase("info")) {
					p.sendMessage(Main.prefix + " §eInformações do boss:");
					p.sendMessage(" §eNome: §f" + boss.getName());
					p.sendMessage(" §eHP: §f" + formatValue(boss.GetHP()));
					p.sendMessage(" §eEntidade: §f" + boss.GetBossEntity());
					p.sendMessage(" §eRecompensa: §f" + boss.GetReward());
					return true;
				}
				}
				if (args.length >= 3) {
					String nome = ChatColor.translateAlternateColorCodes('&', Mensagem(args));
					if (args[1].equalsIgnoreCase("setName")) {
						boss.setName(Mensagem(args));
						p.sendMessage(Main.prefix + "§7§l: §eO nome do boss foi definido para '" + nome + "§e'");
						p.sendMessage("");
						p.sendMessage(boss.getName());
						p.sendMessage("" + formatValue(boss.GetHP()));
						return true;
					}
					if (args[1].equalsIgnoreCase("setHP") || (args[1].equalsIgnoreCase("setLife"))) {
						Double hp = Double.valueOf(0);
						try {
							hp = Double.valueOf(Double.parseDouble(args[2]));
						} catch (NumberFormatException e) {
							sender.sendMessage("§cInsira um valor válido para prosseguir.");
							return true;
						}
						boss.SetHealth(hp);
						p.sendMessage(Main.prefix + "§7§l: §eA vida do boss foi definida para §c" + formatValue(boss.GetHP()));
						return true;
					}
					//terminar add e fazer o remove
					if (args[1].equalsIgnoreCase("addEffect")) {
						if (boss.GetBossEntity() == null) {
							p.sendMessage("§cVocê não definiu ainda o MOB do boss.");
							return true;
						}
					//	p.addPotionEffect(PotionEffect(PotionEffectType.))
						//addpo
						boss.addEffect(effects);
					}
				}
				// fazer o cmd de spawnar dai criar o metodo igual o de Itens pra ele setar tudo
			}	*/
			if (args[0].equalsIgnoreCase("staff")) {	
				if (args.length == 3) {
					if (args[1].equalsIgnoreCase("-p")) {
						Player target = Bukkit.getPlayer(args[2]);
						if (target == null) {
							p.sendMessage("§cJogador não encontrado em nosso banco de dados.");
							return true;
						} else {
							if (!(Main.participantes.contains(target))) {
								getLoc.sendEnter(target);
								Main.participantes.add(target);
								p.sendMessage(Main.prefix + "§7§l: §cJogador enviado para a entrada.");
								return true;
							} else {
								p.sendMessage(Main.prefix + "§7§l §cEste jogador já está no evento.");
								return true;
							}
						}
					}
				}else if (args.length == 1) {
					getLoc.sendEnter(p);
					p.sendMessage(Main.prefix + "§7§l: §aVocê foi enviado para a entrada sem participar do evento.");
					return true;
				} 
			} if ((args[0].equalsIgnoreCase("DropOnDeath") || (args[0].equalsIgnoreCase("DoD") || (args[0].equalsIgnoreCase("Drop"))))) {
				if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom DropOnDeath <ativar/desativar>.");
        			return true;
        		}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ativar")) {
						p.sendMessage("§aDrop dos itens ao morrer ativado.");
						Main.DropOnDeath = false;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§oativou o drop de itens ao morrer no CustomEvent.");
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("desativar")) {
						p.sendMessage("§aDrop dos itens ao morrer desativado.");
						Main.DropOnDeath = true;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§odesativou o drop de itens ao morrer no CustomEvent.");
							}
						}
						return true;
					}
				}
			} if (args[0].equalsIgnoreCase("Sopa")) {
				if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom Sopa <ativar/desativar>.");
        			return true;
        		}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ativar")) {
						p.sendMessage("§aRestaurar vida utilizando sopas ativado no evento.");
						Main.Sopa = true;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§oativou o uso de sopas no CustomEvent.");
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("desativar")) {
						p.sendMessage("§aRestaurar vida utilizando sopas desativado no evento.");
						Main.Sopa = false;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§odesativou o uso de sopas no CustomEvent.");
							}
						}
						return true;
					}
				}	
			}  if ((args[0].equalsIgnoreCase("DeathBroadcast") || (args[0].equalsIgnoreCase("db")))) {
				if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom DeathBroadcast <ativar/desativar>.");
        			return true;
        		}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ativar")) {
						p.sendMessage("§aBroadcast de mortes ativado no evento.");
						Main.DeathBroadcast = true;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§oativou o Broadcast de mortes no CustomEvent.");
							}
						}
						return true;
					}
					if (args[1].equalsIgnoreCase("desativar")) {
						p.sendMessage("§aBroadcast de mortes desativado no evento.");
						Main.DeathBroadcast = false;
						for (Player staff : Bukkit.getOnlinePlayers()) {
							if (staff.hasPermission("customevent.staff")) {
								staff.sendMessage("§a§o" + p.getName() + " §a§odesativou o Broadcast de mortes no CustomEvent.");
							}
						}
						return true;
					}
				}
			} if (args[0].equalsIgnoreCase("kick")) {
				if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom kick <jogador>");
        			return true;
        		}
        		if (args.length == 2) {
        			Player target = Bukkit.getPlayer(args[1]);
        			if (target == null) {
        				p.sendMessage(Main.prefix + "§7§l: §cJogador não encontrado");
        				return true;
        			} else {
	    				if (Main.participantes.contains(target)) {
	    					if (Main.EventoOcorrendo == true) {
	    						Main.participantes.remove(target);
	    						target.sendMessage(Main.prefix + "§7§l: §cVocê foi kickado do evento.");
	    						p.sendMessage(Main.prefix + "§7§l: §eVocê kickou §f" +target.getName()+ " §edo evento.");
	    						target.getInventory().clear();
	    						target.getInventory().setArmorContents(null);
	    						getLoc.sendExit(target);
	    						for (PotionEffect AllPotionEffects : target.getActivePotionEffects()) {
	            					target.removePotionEffect(AllPotionEffects.getType());
	            				}
	    					} else {
    							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
    							return true;
    						}
	    				} else {
    						p.sendMessage(Main.prefix + "§7§l: §cEste jogador não está no evento.");
    						return true;
    					}
        			}
				}
			} if (args[0].equalsIgnoreCase("setwinner")) {
        		if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom setwinner <jogador>");
        			return true;
        		}
        		if (args.length == 2) {
        			Player target = Bukkit.getPlayer(args[1]);
        			if (target == null) {
        				p.sendMessage(Main.prefix + "§7§l: §cJogador não encontrado");
        				return true;
        			} else {
	    				if (Main.participantes.contains(target)) {
	    					if (Main.EventoOcorrendo == true) {
	    						String prefix = VaultHook.getPlayerPrefix(p.getName());
	    						Bukkit.broadcastMessage(" ");
	    						Bukkit.broadcastMessage(Main.prefix);
	    						Bukkit.broadcastMessage("§6* §eEvento finalizado.");
	    						Bukkit.broadcastMessage("§6* §aGanhador: §f" + prefix + target.getName());
	    						Bukkit.broadcastMessage("§6* §eRecompensa será entregue pela STAFF.");
	    						Bukkit.broadcastMessage(" ");
						Main.EventoOcorrendo = false;
						Main.EntradaLiberada = false;
    						if (Main.participantes.size() > 0) {
    							for (Player jogadores : Main.participantes) {	
    								getLoc.sendExit(jogadores);
					    				jogadores.getInventory().clear();	
					    				jogadores.getInventory().setArmorContents(null);
						    				for (PotionEffect AllPotionEffects : jogadores.getActivePotionEffects()) {
						    					jogadores.removePotionEffect(AllPotionEffects.getType());
    					    				}
    				            		}
    									Main.participantes.clear();
    								}
    						Main.participantes.clear();
	    						for (Player jogadores : Bukkit.getOnlinePlayers()) {
	    		        			jogadores.playSound(jogadores.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 0.5F);
	    		        		}
    						} else {
    							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
    							return true;
    						}
    					} else {
    						p.sendMessage(Main.prefix + "§7§l: §cEste jogador não está no evento.");
    						return true;
    					}
        			}
        			return true;
        		}

			} if (args[0].equalsIgnoreCase("respawn")) {
				if (args.length == 1) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom Respawn <ativar/desativar>");
					return true;
				} if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ativar")) {
						Main.Respawn = true;
						for (Player jogadores : Main.participantes) {
							jogadores.sendMessage(Main.prefix + "§7§l: §eRenascer no evento foi §a§lATIVADO§e.");
						    jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						}
							if (!(Main.participantes.contains(p))) {
								p.sendMessage(Main.prefix + "§7§l: §eRenascer no evento foi §a§lATIVADO§e.");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								return true;
						}
					}
					if (args[1].equalsIgnoreCase("desativar")) {
						Main.Respawn = false;
						for (Player jogadores : Main.participantes) {
							jogadores.sendMessage(Main.prefix + "§7§l: §eRenascer no evento foi §c§lDESATIVADO§e.");
						    jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						}
							if (!(Main.participantes.contains(p))) {
								p.sendMessage(Main.prefix + "§7§l: §eRenascer no evento foi §c§lDESATIVADO§e.");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								return true;
						}	
					}
				}
			} if (args[0].equalsIgnoreCase("maxslot")) {
        		if (args.length == 1) {
        			p.sendMessage(Main.prefix + "§7§l: §cUse /EventoCustom maxslot <quantidade>");
        			return true;
        		} if (args.length == 2) {
        			Integer slots = Integer.valueOf(0);
					try {
						slots = Integer.valueOf(Integer.parseInt(args[1]));
					} catch (NumberFormatException e) {
						sender.sendMessage(Main.prefix + "§7§l: §cVocê não definiu um valor válido.");
						return true;
					}
					Main.players_slot = slots;
					p.sendMessage("§aVocê definiu o número de slots para §e" + slots);
					for (Player staff : Bukkit.getOnlinePlayers()) {
						if (staff.hasPermission("customevent.staff")) {
							staff.sendMessage("§a§o" + p.getName() + " §a§odefiniu os slots do CustomEvent para §e" + slots);
						}
					}
					return true;
        		}
        		
			}if (args[0].equalsIgnoreCase("giveitem")) {
				if (!(p.getItemInHand().getType() == null || p.getItemInHand().getType() == Material.AIR)) {
					if (Main.EventoOcorrendo == true) {
			         ItemStack item = new ItemStack(p.getItemInHand());
			         	for (Player jogadores : Main.participantes) {
								jogadores.getInventory().addItem(item);
							}
			         	p.sendMessage(Main.prefix + "§7§l: §eItem enviado para todos do evento.");
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
					return true;
					}
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cSegure um item válido para enviar.");
					return true;
				}
			} if (args[0].equalsIgnoreCase("giveInv") || args[0].equalsIgnoreCase("giveInventory") || args[0].equalsIgnoreCase("giveInventario")) {
		        	for (Player participantes : Main.participantes) {
			            ItemStack[] armor = p.getInventory().getArmorContents();
				            participantes.getInventory().addItem(p.getInventory().getContents());
				            participantes.getInventory().setArmorContents(armor);
				            participantes.updateInventory();
			          }
	         	p.sendMessage(Main.prefix + "§7§l: §eInvetário enviado para todos do evento.");
	         	return true;
			} if (args[0].equalsIgnoreCase("frozen")) { 
				if (args.length <= 1) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent frozen [Ativar/Desativar]");
					return true;
					}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("desativar")) {
						if (Main.EventoOcorrendo == true) {
							Main.frozen = true;
							for (Player jogadores : Main.participantes) {
									jogadores.sendMessage(Main.prefix + "§7§l: §eA locomoção dos players foi §A§lATIVADA§e.");
									jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
							}
							if (!(Main.participantes.contains(p))) {
								p.sendMessage(Main.prefix + "§7§l: §eA locomoção dos players foi §A§lATIVADA§e.");
								p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								return true;
							}
						} else {
							p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
							return true;
							}
						}if (args[1].equalsIgnoreCase("ativar")) {
							if (Main.EventoOcorrendo == true) {
								Main.frozen = false;
								for (Player jogadores : Main.participantes) {
									jogadores.sendMessage(Main.prefix + "§7§l: §eA locomoção dos players foi §c§lDESATIVADA§e.");
										jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
								}
								if (!(Main.participantes.contains(p))) {
									p.sendMessage(Main.prefix + "§7§l: §eA locomoção dos players foi §c§lDESATIVADA§e.");
									p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
									return true;
								}
							} else {
								p.sendMessage(Main.prefix + "§7§l: §cO evento não está em andamento.");
								return true;
								}
							}
						}
			
			} if (args[0].equalsIgnoreCase("abrir")) {
				if (!(Main.EventoOcorrendo == true)) {
					if (!(Main.EntradaLiberada == true)) {
						Main.participantes.clear();
							Main.EventoOcorrendo = true;
								Main.EntradaLiberada = true;
			            			Main.Colocarblocos = true;
			            				Main.frozen = false;
			            					Main.pvp = false;
			            					  Main.Sopa = false;
			            					     Main.DropOnDeath = false;
			            					       Main.DeathBroadcast = true;
			            					     	  Main.Quebrarblocos = true;
			            					     	     Main.Respawn = false;
						Task.EntradaTimer();
					}
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cO evento já está ocorrendo.");
					return true;
				}
				
			} if (args[0].equalsIgnoreCase("parar")) {
				if (Main.EventoOcorrendo == true) {
					Main.EventoOcorrendo = false;
						Main.EntradaLiberada = false;
	            			Main.Colocarblocos = false;
	            				Main.frozen = false;
	            					Main.pvp = false;
	            					  Main.Sopa = false;
	            					     Main.DropOnDeath = false;
	            					       Main.DeathBroadcast = true;
	            					     	  Main.Quebrarblocos = false;
	            					     	     Main.Respawn = false;
											Bukkit.broadcastMessage("");
							        		Bukkit.broadcastMessage(Main.prefix);
							        		Bukkit.broadcastMessage("§6 §cEvento finalizado pela STAFF");
							    			Bukkit.broadcastMessage("§6 §cTeleportando jogadores para a saída..");
											Bukkit.broadcastMessage("");
				for (Player jogadores : Main.participantes) {
					getLoc.sendExit(jogadores);
	    				jogadores.getInventory().clear();	
	    				jogadores.getInventory().setArmorContents(null);
		    				for (PotionEffect AllPotionEffects : jogadores.getActivePotionEffects()) {
		    					jogadores.removePotionEffect(AllPotionEffects.getType());
	    				}
            		}
					Main.participantes.clear();
				}
				
				
				
			} if ((args[0].equalsIgnoreCase("friendlyfire") || args[0].equalsIgnoreCase("fogoaoamigo"))) {
				if (args.length == 1) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent FriendlyFire <Ativar/Desativar>.");
					return true;
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("ativar")) {
						for (Player jogadores : Main.participantes) {
							jogadores.sendMessage(Main.prefix + "§7§l: §eO fogo ao amigo entre os clãn's foi §a§lATIVADO§e.");
								jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						}
						if (!(Main.participantes.contains(p))) {
							p.sendMessage(Main.prefix + "§7§l: §eO fogo ao amigo entre os clãn's foi §a§lATIVADO§e.");
							p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
							return true;
						}
						//Ativar
						return true;
					}
					if (args[1].equalsIgnoreCase("desativar")) {
						for (Player jogadores : Main.participantes) {
							jogadores.sendMessage(Main.prefix + "§7§l: §eO fogo ao amigo entre os clãn's foi §c§lDESATIVADA§e.");
								jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0F, 0.5F);
						}
						if (!(Main.participantes.contains(p))) {
							p.sendMessage(Main.prefix + "§7§l: §eO fogo ao amigo entre os clãn's foi §c§lDESATIVADA§e.");
							p.playSound(p.getLocation(), Sound.CLICK, 1.0F, 0.5F);
							return true;
						}
						//Desativar
						return true;
					}
				}
			} if (args[0].equalsIgnoreCase("setentrada")) {
				getLoc.setLoc(p, "Entrada");
					p.sendMessage(Main.prefix + "§7§l: §eEntrada do evento definida.");
						return true;
						
			} if (args[0].equalsIgnoreCase("setsaida")) {
				getLoc.setExit(p);
					p.sendMessage(Main.prefix + "§7§l: §eSaída do evento definida.");
						return true;
				
			} if (args[0].equalsIgnoreCase("puxartodos")) {
				for (Player jogadores : Main.participantes) {
					jogadores.teleport(p);
						jogadores.playSound(jogadores.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
				}
				p.sendMessage(Main.prefix + "§7§l: §eTodos foram puxados até você.");
				return true;
				
			} if (args[0].equalsIgnoreCase("comandos")) {
				p.sendMessage("§7* §e/eventocustom entrar");
				p.sendMessage("§7* §e/eventocustom sair");
				p.sendMessage("§7* §c/eventocustom abrir");
				p.sendMessage("§7* §c/eventocustom parar");
				p.sendMessage("§7* §c/eventocustom puxartodos §4- §cPuxa todos até sua localização.");
				p.sendMessage("§7* §c/eventocustom giveitem §4- §cEntrega o item da sua mão á todos.");
				//p.sendMessage("§7* §c/eventocustom giveInv §4- §cEntrega TODO seu inventário á todos.");
				p.sendMessage("§7* §c/eventocustom staff §4- §cEntra no evento sem participar.");
				p.sendMessage("§7* §c/eventocustom setwinner <jogador> §4- §cDefine o vencedor.");
				p.sendMessage("§7* §c/eventocustom frozen §4- §cImpossibilita eles de andar.");
				p.sendMessage("§7* §c/eventocustom pvp §4- §cAtiva/Desativa o PvP.");
				p.sendMessage("§7* §c/eventocustom blocos §4- §cAtiva/Desativa de porem blocos.");
				p.sendMessage("§7* §c/eventocustom anunciar §4- §cEnvia um anúncio no evento.");
				p.sendMessage("§7* §c/eventocustom kick <jogador> §4- §cExpulsa um player do evento.");
				p.sendMessage("§7* §c/eventocustom addEffect §4- §cAdiciona um efeito para todos.");
				p.sendMessage("§7* §c/eventocustom removerEfeitos §4- §cRetira os efeitos de todos.");
				p.sendMessage("§7* §c/eventocustom Sopa §4- §cAtiva o uso de sopas no evento (Ex: HG).");
				p.sendMessage("§7* §c/eventocustom DeathBroadcast §4- §cAtiva as mensagens de morte.");
				p.sendMessage("§7* §c/eventocustom DropOnDeath §4- §cNão dropam itens ao morrer.");
			//	p.sendMessage("§7* §c/eventocustom Respawn §4- §cPermite renascer ao morrer.");
				p.sendMessage("§7* §c/eventocustom MaxSlot §4- §cDefine o limite de jogadores.");
				//p.sendMessage("§7* §c/eventocustom friendlyFire §4- §cAtiva/Desativa fogo ao amigo(Clã).");
				return true;
			} if (args[0].equalsIgnoreCase("anunciar")) {
				if (args.length >= 2) {
					for (Player jogadores : Main.participantes) {
							jogadores.sendMessage(" ");
							jogadores.sendMessage("§6[!] §c[MANAGER] " +p.getName()+ "§7: " + Main.getMensagem(args).replaceAll("&", "§"));
							jogadores.sendMessage(" ");
							jogadores.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
						}
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						if (jogadores.hasPermission("zs.gerente")) {
							if (!(Main.participantes.contains(jogadores))) {
								jogadores.sendMessage(" ");
								jogadores.sendMessage("§6[!] §c[MANAGER] " +p.getName()+ "§7: " + Main.getMensagem(args).replaceAll("&", "§"));
								jogadores.sendMessage(" ");
							}
						}
					}
				} else {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent anunciar <Mensagem>.");
					return true;
					}
				}
			if (args[0].equalsIgnoreCase("addeffect") || args[0].equalsIgnoreCase("addefeito")) {
				if (args.length <= 2) {
					p.sendMessage(Main.prefix + "§7§l: §cUse /CustomEvent addEffect <Efeito> [Tempo(segundos)] [Amplificador]");
					return true;
				}
				if (args.length >= 3) {
					Integer Amplificador = Integer.valueOf(0);
					Integer Tempo = Integer.valueOf(0);
					try {
						Amplificador = Integer.valueOf(Integer.parseInt(args[3]));
						Tempo = Integer.valueOf(Integer.parseInt(args[2]));
					} catch (NumberFormatException e) {
						sender.sendMessage("§cVocê não inseriu um número válido para executar a ação.");
						return true;
					}
					for (Player jogadores : Main.participantes) {	
						jogadores.addPotionEffect(new PotionEffect(TranslateValue(args[1], p), Tempo * 120, Amplificador - 1 ));
					}
					p.sendMessage(Main.prefix + "§7§l: §eEfeito adicionado para todos do evento.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("removerefeitos") || args[0].equalsIgnoreCase("removeEffect")) {
				for (Player jogadores : Main.participantes) {
	    				for (PotionEffect AllPotionEffects : jogadores.getActivePotionEffects()) {
	    					jogadores.removePotionEffect(AllPotionEffects.getType());
	    				}
            		}
				p.sendMessage(Main.prefix + "§7§l: §eEfeitos removidos para todos do evento.");
				return true;
				}
			
			} else {
				p.sendMessage("§cVocê não tem permissão para executar este comando.");
				return true;
			}
		}
		return false;
	}
	
	private PotionEffectType TranslateValue(String effect, Player p) {
		if (effect.equalsIgnoreCase("absorcao") || effect.equalsIgnoreCase("absorption")) {
			return PotionEffectType.ABSORPTION;
		}
		if (effect.equalsIgnoreCase("cegueira") || effect.equalsIgnoreCase("blindness")) {
			return PotionEffectType.BLINDNESS;
		}
		if (effect.equalsIgnoreCase("nausea") || effect.equalsIgnoreCase("confusion")) {
			return PotionEffectType.CONFUSION;
		}
		if (effect.equalsIgnoreCase("resistencia") || effect.equalsIgnoreCase("resistance")) {
			return PotionEffectType.DAMAGE_RESISTANCE;
		}
		if (effect.equalsIgnoreCase("haste") || effect.equalsIgnoreCase("pressa")) {
			return PotionEffectType.FAST_DIGGING;
		}
		if (effect.equalsIgnoreCase("resistenciaAoFogo") || effect.equalsIgnoreCase("fire_resistance") || effect.equalsIgnoreCase("fireResistance")) {
			return PotionEffectType.FIRE_RESISTANCE;
		}
		if (effect.equalsIgnoreCase("vida") || effect.equalsIgnoreCase("heal")) {
			return PotionEffectType.HEAL;
		} 
		if (effect.equalsIgnoreCase("fome") || effect.equalsIgnoreCase("hunger")) {
			return PotionEffectType.HUNGER;
		} 
		if (effect.equalsIgnoreCase("forca") || effect.equalsIgnoreCase("strength")) {
			return PotionEffectType.INCREASE_DAMAGE;
		} 
		if (effect.equalsIgnoreCase("invisibilidade") || effect.equalsIgnoreCase("invisibility")) {
			return PotionEffectType.INVISIBILITY;
		} 
		if (effect.equalsIgnoreCase("superpulo") || effect.equalsIgnoreCase("jump_boost")) {
			return PotionEffectType.JUMP;
		} 
		if (effect.equalsIgnoreCase("visaoNoturna") || effect.equalsIgnoreCase("night_vision")) {
			return PotionEffectType.NIGHT_VISION;
		} 
		if (effect.equalsIgnoreCase("veneno") || effect.equalsIgnoreCase("poison")) {
			return PotionEffectType.POISON;
		} 
		if (effect.equalsIgnoreCase("regeneracao") || effect.equalsIgnoreCase("regeneration")) {
			return PotionEffectType.REGENERATION;
		} 
		if (effect.equalsIgnoreCase("saturacao") || effect.equalsIgnoreCase("saturaçao") || effect.equalsIgnoreCase("saturation")) {
			return PotionEffectType.SATURATION;
		} 
		if (effect.equalsIgnoreCase("lentidao") || effect.equalsIgnoreCase("slowness")) {
			return PotionEffectType.SLOW;
		} 
		if (effect.equalsIgnoreCase("cansaco") || effect.equalsIgnoreCase("slow_digging")) {
			return PotionEffectType.SLOW_DIGGING;
		} 
		if (effect.equalsIgnoreCase("velocidade") || effect.equalsIgnoreCase("speed")) {
			return PotionEffectType.SPEED;
		} 
		if (effect.equalsIgnoreCase("respiracao") || effect.equalsIgnoreCase("respicaoAquatica") || effect.equalsIgnoreCase("water_breathing")) {
			return PotionEffectType.WATER_BREATHING;
		} 
		if (effect.equalsIgnoreCase("fraqueza") || effect.equalsIgnoreCase("weakness")) {
			return PotionEffectType.WEAKNESS;
		} 
		if (effect.equalsIgnoreCase("wither") || effect.equalsIgnoreCase("withereffect") || effect.equalsIgnoreCase("whiter")) {
			return PotionEffectType.WITHER;
		} else {
			p.sendMessage("§cO efeito descrito não foi encontrado.");
		}
		return null;
		
	}
	
	public static String Mensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 2; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
	public String formatValue(Double value) {
        return String.format("%.0f", value);
    }
}

