package me.zsnow.redestone;

import java.io.IOException;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.NumberFormatAPI;
import me.zsnow.redestone.api.LocationAPI.loc_sumo;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.api.SimpleclansAPI;
import me.zsnow.redestone.listeners.DuelListeners;
import me.zsnow.redestone.listeners.MenuListeners;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.InviteManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import me.zsnow.redestone.manager.SumoInviteManager;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("duelo")) {
			if (!(sender instanceof Player)) return false;
		}
		Player p = (Player) sender;
		InviteManager invite = InviteManager.getInstance();

		if (args.length < 1) {
			try {
				MenuListeners.openMenuPrincipal(p);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage("§cHouve um erro ao abrir o menu de duelo. Entre em contato com um Administrador!");
			}
		}
			if (args[0].equalsIgnoreCase("help")) {
				if (args.length >= 1) {
					if (p.hasPermission("zs.gerente")) {
						p.sendMessage(" ");
						p.sendMessage(" §6§lDUELO §e- §f(Comandos) v" + Main.getInstance().getDescription().getVersion());
						p.sendMessage(" ");
						p.sendMessage(" §f/duelo moderar");
						p.sendMessage(" §f/duelo set [X1/SUMO]");;
						p.sendMessage(" §f/duelo verTodos <Nick>");
						p.sendMessage(" §f/duelo verDuplas <Nick>");
						p.sendMessage(" §c/duelo pararLuta <Nick>");
						p.sendMessage(" §c/duelo manutenção <on/off>");
						p.sendMessage(" ");
						return true;
						}
					if (p.hasPermission("zs.mod")) {
						p.sendMessage(" ");
						p.sendMessage(" §6§lDUELO §e- §f(Comandos)");
						p.sendMessage(" ");
						p.sendMessage(" §f/duelo moderar");
						p.sendMessage(" §f/duelo verTodos <Nick>");
						p.sendMessage(" §f/duelo verDuplas <Nick>");
						p.sendMessage(" ");
						return true;
						}
					}
				}
			if (args[0].equalsIgnoreCase("reload")) {
				if (p.hasPermission("zs.gerente")) {
					Configs.config.saveConfig();
					Configs.locations.saveConfig();
					Configs.mito.saveConfig();
					p.sendMessage("§cConfigurações recarregadas.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("moderar")) {
				if (p.hasPermission("zs.mod")) {
					MenuListeners menu = new MenuListeners();
					menu.openModerarMenu(p);
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("set")) {
				if (p.hasPermission("zs.gerente")) {
					if (args.length == 2) {
						if (args[1].equalsIgnoreCase("X1")) {
							p.sendMessage("§cUse: /duelo set [X1] <Entrada, Saida, Moderar, Camarote, pos[1/2]>");
							return true;
						}
						if (args[1].equalsIgnoreCase("SUMO")) {
							p.sendMessage("§cUse: /duelo set [Sumo] <SUMO_CLASSICA, SUMO_PEQUENA, SUMO_GRANDE, POS1_SUMO_CLASSICA, POS2_SUMO_CLASSICA, POS1_SUMO_PEQUENA, POS2_SUMO_PEQUENA>, POS1_SUMO_GRANDE, POS2_SUMO_GRANDE>");
							return true;
						}
					}
					if (args.length < 3) {
							p.sendMessage("§cUse: /duelo set [X1/SUMO] <argumentos>");
							return true;
					} 
				if (args.length == 3) {
						if (args[1].equalsIgnoreCase("X1")) {
							final String local = args[2].toUpperCase();
							switch (local) {
								case "ENTRADA":
									LocationAPI.getLocation().setLocation(p, location.ENTRADA);
									p.sendMessage("§eA área de Entrada foi definida com sucesso!");
									break;
								case "SAIDA":
									LocationAPI.getLocation().setLocation(p, location.SAIDA);
									p.sendMessage("§eA área de Saida foi definida com sucesso!");
									break;
								case "MODERAR":
									LocationAPI.getLocation().setLocation(p, location.MODERAR);
									p.sendMessage("§eA área de moderação foi definida com sucesso!");
									break;
								case "CAMAROTE":
									LocationAPI.getLocation().setLocation(p, location.CAMAROTE);
									p.sendMessage("§eA área de Camarote foi definida com sucesso!");
									break;
								case "POS1":
									LocationAPI.getLocation().setLocation(p, location.POS1);
									p.sendMessage("§eA área de pos1 foi definida com sucesso!");
									break;
								case "POS2":
									LocationAPI.getLocation().setLocation(p, location.POS2);
									p.sendMessage("§eA área de pos2 foi definida com sucesso!");
									break;
								
							default:
								p.sendMessage("§cUse: /duelo set [X1] <Entrada, Saida, Moderar, Camarote, pos[1/2]>");
								break;
							}
						}
						if (args[1].equalsIgnoreCase("Sumo")) {
							final String local = args[2].toUpperCase();
							switch (local) {
								case "SUMO_CLASSICA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.SUMO_CLASSICA);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "SUMO_PEQUENA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.SUMO_PEQUENA);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "SUMO_GRANDE":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.SUMO_GRANDE);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_CLASSICA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS1_SUMO_CLASSICA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_CLASSICA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS2_SUMO_CLASSICA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_PEQUENA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS1_SUMO_PEQUENA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_PEQUENA":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS2_SUMO_PEQUENA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_GRANDE":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS1_SUMO_GRANDE);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_GRANDE":
									LocationAPI.getLocation().setSumoLocation(p, loc_sumo.POS2_SUMO_GRANDE);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
							default:
								p.sendMessage("§cUse: /duelo set [Sumo] <SUMO_CLASSICA, SUMO_PEQUENA, SUMO_GRANDE, POS1_SUMO_CLASSICA, POS2_SUMO_CLASSICA, POS1_SUMO_PEQUENA, POS2_SUMO_PEQUENA>, POS1_SUMO_GRANDE, POS2_SUMO_GRANDE>");
								break;
							}
						}
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
				return true;
			}
			
			
			if (args[0].equalsIgnoreCase("desafiar") || args[0].equalsIgnoreCase("convidar") || args[0].equalsIgnoreCase("invite")) {
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("sumo")) {
						PlayerInventory inv = p.getInventory();
		            	for (ItemStack i : inv.getContents()) {
							if(i != null && !(i.getType() == Material.AIR)) {
								p.sendMessage("§cEsteja com o inventário vázio antes de convidar um jogador.");
								p.closeInventory();
								return true;
							} 
						} for (ItemStack i : inv.getArmorContents()) {
							if(i != null && !(i.getType() == Material.AIR)) {
								p.sendMessage("§cEsteja sem armaduras equipadas antes de convidar um jogador.");
								p.closeInventory();
								return true;
							}
						}
							MenuListeners menu = new MenuListeners();
		                	menu.openMenuSelection(p);
		                	return true;
					}
				}
				if (args.length < 3) {
					p.sendMessage("§eSintaxe: /duelo desafiar <X1> <jogador>.");
					p.sendMessage(" §c➥ /duelo desafiar <SUMO>.");
					return true;
				}
				if (args.length == 3) {
					if (args[1].equalsIgnoreCase("x1")) {
					if (DuelManager.getInstance().hasCoin(p)) {
						final Player target = Bukkit.getPlayer(args[2]);
						if (target != null) {
							if (target != p) {
								if (!invite.hasInvite(target)) { 
									if (invite.canInvite()) {
										invite.sendInviteTo(p, target);
									
										NumberFormatAPI formatter = new NumberFormatAPI();
										String custo = formatter.formatNumber(DuelManager.getInstance().getCusto());
										if (Configs.config.getBoolean("has-duelo-broadcast") == true) {
											for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("duelo-broadcast")) {
												p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
													.replace("$convidado", target.getName()))
													.replace("$jogador", p.getName())
													.replace("$tempo", String.valueOf(invite.getTempo())
													.replace("$valor", custo)));
											}
										}
										for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-enviado")) {
											p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
												.replace("$convidado", target.getName()))
												.replace("$tempo", String.valueOf(invite.getTempo()))
												.replace("$valor", custo));
										}
										for (String msg : me.zsnow.redestone.config.Configs.config.getConfig().getStringList("convite-recebido")) {
											target.sendMessage(ChatColor.translateAlternateColorCodes('&', msg
													.replace("$jogador", p.getName()))
													.replace("$tempo", String.valueOf(invite.getTempo()))
													.replace("$valor", custo));
										}
									invite.startTask();
									return true;
								} else {
									p.sendMessage("§cJá há 2 jogadores na fila de duelo. Aguarde que o pedido expire ou seja aceito por um deles para poder enviar novamente.");
									return true;
								}
							} else {
								p.sendMessage("§cEste jogador já possui um pedido de duelo pendente.");
								return true;
							}
						} else {
							p.sendMessage("§cVocê não pode desafiar a sí mesmo.");
							return true;
						}
					} else {
						p.sendMessage("§cO jogador especificado não está online.");
						return true;
					}
				} else {
					p.sendMessage("§cVocê não tem dinheiro o suficiente para desafiar um jogador.");
					return true;
				}
			}
			p.sendMessage("§cSintaxe: /duelo desafiar <X1/Sumo>.");
			p.sendMessage(" §c➥ /duelo desafiar <SUMO>.");
			return true;
		}
	}
			if (args[0].equalsIgnoreCase("verduplas")) {
				if (p.hasPermission("zs.mod")) {
					if (args.length < 2) {
						p.sendMessage("§cUse: /duelo verduplas <jogador>");
						return true;
					}
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							SumoDuelManager sumo = SumoDuelManager.getInstance();
							DuelManager duel = DuelManager.getInstance();
							if (duel.getDuelando().contains(target)) {
									for (Entry<Player, Player> duplas : duel.duelandoHash.entrySet()) {
									    Player key = duplas.getKey();
									    Player value = duplas.getValue();
									    if (duplas.getKey().getName().equals(target.getName())) {
									    	for (Player duelandoPlayers : duel.getDuelando()) {
												p.hidePlayer(duelandoPlayers);
											}
									    	p.showPlayer(key);
									    	p.showPlayer(value);
									    	p.sendMessage("§eVocê agora está assistindo o SUMO entre " + key.getName() + " §ee " + value.getName() + "§e.");
									    	return true;
									    }
									}
								} 
							if (sumo.getDuelando().contains(target)) {
								MenuListeners user = new MenuListeners();
								for (Entry<Player, Player> duplas : sumo.duelandoHash.entrySet()) {
								    Player key = duplas.getKey();
								    Player value = duplas.getValue();
								    if (duplas.getKey().getName().equals(target.getName())) {
								    	for (Player duelandoPlayers : sumo.getDuelando()) {
											p.hidePlayer(duelandoPlayers);
										}
								    	p.showPlayer(key);
								    	p.showPlayer(value);
								    	p.sendMessage("§eVocê agora está assistindo o SUMO entre " + key.getName() + " §ee " + value.getName() + "§e.");
								    	p.sendMessage(
								    		"§e§lINFO: §fArena §7" + user.getArenaType(getDataInfo(key).getArena()).toUpperCase() +
				            				 " §fEfeito §7" + user.getPotType(getDataInfo(key).getPotLvl()).toUpperCase() + 
				            			     " §fRepulsão §7" + user.getKbTpe(getDataInfo(key).getKB()).toUpperCase());
								    	
								    	return true;
								    }
								}
							}
							p.sendMessage("§c" + target.getName() + " §cnão faz parte de nenhum SUMO ou X1.");
							return true;
						} else {
							p.sendMessage("§cO jogador especificado não está online..");
							return true;
						}
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("vertodos")) {
				if (p.hasPermission("zs.mod")) {
					if (args.length == 1) {
						for (Player duelandoPlayers : SumoDuelManager.getInstance().getDuelando()) {
							p.showPlayer(duelandoPlayers);
						}
				    	for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
							p.showPlayer(duelandoPlayers);
						}
				    	p.sendMessage("§eAgora todos os jogadores do X1 e SUMO estão visíveis pra você.");
						return true;
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
			
			// arrumar esse cod e adaptar pro sumo
			
			if (args[0].equalsIgnoreCase("stopfight") || args[0].equalsIgnoreCase("pararluta") || args[0].equalsIgnoreCase("pararduelo")) {
				if (p.hasPermission("zs.mod")) {
					if (args.length < 2) {
						p.sendMessage("§cUse: /duelo pararduelo <jogador>");
						return true;
					}
					if (args.length == 2) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							DuelManager duel = DuelManager.getInstance();
							if (duel.getDuelando().contains(target)) {
								for (Entry<Player, Player> duplas : duel.duelandoHash.entrySet()) {
								    Player key = duplas.getKey();
								    Player value = duplas.getValue();
								    if (duplas.getKey().getName().equals(target.getName())) {
								    	duel.getDuelando().remove(key);
								    	duel.getDuelando().remove(value);
								    	
								    	LocationAPI.getLocation().teleportTo(key, location.SAIDA);
							    		duel.moneyBackFor(key);
							    		SimpleclansAPI.getAPI().disableClanDamage(key);
							    		
								    	LocationAPI.getLocation().teleportTo(value, location.SAIDA);
							    		duel.moneyBackFor(value);
							    		SimpleclansAPI.getAPI().disableClanDamage(value);
								    	
							    		duel.moneyBackFor(key);
							    		duel.moneyBackFor(value);
							    		key.chat("/on");
							    		value.chat("/on");
							    		key.sendMessage("§cSeu duelo foi encerrado!");
							    		value.sendMessage("§cSeu duelo foi encerrado!");
								    	p.sendMessage("§aVocê encerrou o duelo entre " + key.getName() + " §ae " + value.getName() + "§a.");
								    }
								}
					    		DuelManager.getInstance().duelandoHash.remove(target, DuelManager.getInstance().getMortoBy(target));
					    		DuelManager.getInstance().duelandoHash.remove(DuelManager.getInstance().getMortoBy(target), target);
					    		return true;
							}
							SumoDuelManager sumo = SumoDuelManager.getInstance();
							if (sumo.getDuelando().contains(target)) {
			        			final Player dupla = sumo.duelandoHash.get(target);
			    				DuelListeners data = new DuelListeners();

			    				target.getInventory().clear();
			    				target.getInventory().setArmorContents(null);
			    				
			    				data.deleteDataSave(target);
			    				LocationAPI.getLocation().teleportTo(target, location.SAIDA);
			    				target.chat("/on");
			    				
			    				data.deleteDataSave(dupla);
								LocationAPI.getLocation().teleportTo(dupla, location.SAIDA);
								dupla.chat("/on");
					    		target.sendMessage("§cSeu duelo foi encerrado!");
					    		dupla.sendMessage("§cSeu duelo foi encerrado!");
						    	p.sendMessage("§aVocê encerrou o duelo entre " + target.getName() + " §ae " + dupla.getName() + "§a.");
						    	return true;
							}
							p.sendMessage("§cEste jogador não está participando de nenhum duelo ou sumo.");
							return true;
						} else {
							p.sendMessage("§cO jogador especificado não está online..");
							return true;
						}
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
			
			// add manutençao pro sumo
			
			if (args[0].equalsIgnoreCase("manutencao") || args[0].equalsIgnoreCase("manutençao") || args[0].equalsIgnoreCase("manutenção")) {
				if (p.hasPermission("zs.admin")) {
					if (args.length < 3) {
						p.sendMessage("§cUse: /duelo manutenção <on/off> <X1/SUMO>");
						return true;
					}
					if (args.length == 3) {
						DuelManager duel = DuelManager.getInstance();
						SumoDuelManager sumo = SumoDuelManager.getInstance();
						
						if (args[1].equalsIgnoreCase("on")) {
							if (args[2].equalsIgnoreCase("x1")) {
								duel.setManutencaoStatus(true);
						    	for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
						    		LocationAPI.getLocation().teleportTo(duelandoPlayers, location.SAIDA);
						    		duel.moneyBackFor(duelandoPlayers);
						    		SimpleclansAPI.getAPI().disableClanDamage(duelandoPlayers);
						    		duelandoPlayers.sendMessage("§cTodos os duelos foram encerrados por um ADMINISTRADOR. Manutenção na arena sendo iniciada!");
						    		duelandoPlayers.sendMessage("§c§lSe você ainda estiver na arena, chame um STAFF ou relogue do servidor.");
						    		duelandoPlayers.showPlayer(duelandoPlayers);
								}
						    	duel.getDuelando().clear();
						    	duel.duelandoHash.clear();
						    	p.sendMessage("§aManutenção no duelo ativada!");
						    	return true;
							}
							if (args[2].equalsIgnoreCase("sumo")) {
								sumo.setManutencaoStatus(true);
						    	for (Player duelandoPlayers : sumo.getDuelando()) {
						    		LocationAPI.getLocation().teleportTo(duelandoPlayers, location.SAIDA);
						    		SimpleclansAPI.getAPI().disableClanDamage(duelandoPlayers);
						    		duelandoPlayers.sendMessage("§cTodos os duelos foram encerrados por um ADMINISTRADOR. Manutenção na arena sendo iniciada!");
						    		duelandoPlayers.sendMessage("§c§lSe você ainda estiver na arena, chame um STAFF ou relogue do servidor.");
						    		duelandoPlayers.showPlayer(duelandoPlayers);
							        UUID uuid = duelandoPlayers.getUniqueId();
							        sumo.duelandoHash.remove(duelandoPlayers);
							        duelandoPlayers.getInventory().clear();
										for (PotionEffect AllPotionEffects : duelandoPlayers.getActivePotionEffects()) {
											duelandoPlayers.removePotionEffect(AllPotionEffects.getType());
										}
							            SumoDuelManager.playerData.remove(uuid);
							        	SimpleclansAPI.getAPI().disableClanDamage(duelandoPlayers);
							        	if (SumoInviteManager.getInstance().savedTimers.get(duelandoPlayers) != null) {
							        		Bukkit.getScheduler().cancelTask(SumoInviteManager.getInstance().savedTimers.get(duelandoPlayers));
							        		SumoInviteManager.getInstance().savedTimers.remove(duelandoPlayers);
							        	}
								}
						    	sumo.getDuelando().clear();
						    	p.sendMessage("§aManutenção no sumo ativada!");
						    	return true;
							}
						}
						if (args[1].equalsIgnoreCase("off")) {
							if (args[2].equalsIgnoreCase("x1")) {
								duel.setManutencaoStatus(false);
								p.sendMessage("§cManutenção no duelo desativada!");
								return true;
							}
							if (args[2].equalsIgnoreCase("sumo")) {
								sumo.setManutencaoStatus(false);
								p.sendMessage("§cManutenção no sumo desativada!");
								return true;
							}
						}
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
		}
			// CONVIDOU É OPOSTO DE Y
			if (args[0].equalsIgnoreCase("aceitar")) { 
				if (args.length < 2) {
					p.sendMessage("§cUtilize: /Duelo aceitar <Sumo/X1>");
					return true;
				}
				if (args.length == 2) {
					SumoInviteManager sumoInvite = SumoInviteManager.getInstance();
					if (args[1].equalsIgnoreCase("X1")) {
					if (invite.hasInvite(p) && !sumoInvite.hasInvite(p)) {
						if (DuelManager.getInstance().getManutencaoStatus() == false) {
							if (invite.getConvidou() != p) {
								if (DuelManager.getInstance().hasCoin(invite.getPlayerX()) && DuelManager.getInstance().hasCoin(invite.getPlayerY())) {
									DuelManager duel = DuelManager.getInstance();
									invite.setProtection(true);
									invite.getPlayerX().setHealth(20);
									invite.getPlayerY().setHealth(20);
								duel.paymentToEnter(invite.getPlayerX());
								duel.paymentToEnter(invite.getPlayerY());
										hideOnEnter(invite.getPlayerX(), invite.getPlayerY());
								LocationAPI.getLocation().teleportTo(invite.getPlayerX(), location.POS1);
								LocationAPI.getLocation().teleportTo(invite.getPlayerY(), location.POS2);
									duel.getDuelando().add(invite.getPlayerX());
									duel.getDuelando().add(invite.getPlayerY());
								duel.duelandoHash.put(invite.getPlayerX(), invite.getPlayerY());
								duel.duelandoHash.put(invite.getPlayerY(), invite.getPlayerX());
									SimpleclansAPI.getAPI().enableClanDamage(invite.getPlayerX());
									SimpleclansAPI.getAPI().enableClanDamage(invite.getPlayerY());
										invite.getConvidou().sendMessage("§a[Duelo] " + invite.getPlayerY().getName() + " §aaceitou seu pedido de duelo.");
										invite.getPlayerY().sendMessage("§a[Duelo] Você aceitou o pedido de duelo de " + invite.getPlayerX().getName());
											invite.recuseOrExpireInvite(invite.getPlayerX(), invite.getPlayerY());
											
									// TELEPORTAR PRIMEIRO, ADD NO DUELANDO DEPOIS (SEMPRE)
									return true;
								} else {
									p.sendMessage("§cUm dos jogadores na fila de duelo não tem dinheiro o suficiente para aceitar o convite.");
									return true;
								}
							} else {
								p.sendMessage("§c[Duelo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
								return true;
							}
						} else {
	        			  p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
	        			  return true;
						}
					}
					if (sumoInvite.hasInvite(p)) {
						p.sendMessage("§cVocê está sendo convidado para um X1 PVP mas também possui um convite de SUMO pendente.");
						return true;
					}
					p.sendMessage("§cVocê não possui um convite de duelo pendente ou ele expirou.");
					return true;
				}
				if (args[1].equalsIgnoreCase("sumo")) {
					if (DuelManager.getInstance().getManutencaoSumoStatus() == false) {
					if (sumoInvite.getConvidou() != p) {
						if (sumoInvite.hasInvite(p) && !invite.hasInvite(p)) { //cuidado com aqui, ele pode n ter sumoinvite e ter invite e talvez entre no laço
							final Player PlayerX = sumoInvite.getPlayerX();
							final Player PlayerY = sumoInvite.getPlayerY();
							
							PlayerInventory INVENTORY_player_X = PlayerX.getInventory();
							PlayerInventory INVENTORY_player_Y = PlayerY.getInventory();
							
		                	for (ItemStack i : INVENTORY_player_X.getContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									PlayerX.sendMessage("§cEsteja com o inventário vázio antes de aceitar um convite de duelo.");
									PlayerY.sendMessage("§cO duelo não pode começar porque §e" + PlayerX.getName() + " §cestá com o inventário cheio.");
									return true;
								} 
							} for (ItemStack i : INVENTORY_player_X.getArmorContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									PlayerX.sendMessage("§cEsteja sem armaduras equipadas antes de aceitar um convite de duelo.");
									PlayerY.sendMessage("§cO duelo não pode começar porque §e" + PlayerX.getName() + " §cestá com armaduras equipadas.");
									return true;
								}
							}
							
		                	for (ItemStack i : INVENTORY_player_Y.getContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									PlayerY.sendMessage("§cEsteja com o inventário vázio antes de aceitar um convite de duelo.");
									PlayerX.sendMessage("§cO duelo não pode começar porque §e" + PlayerY.getName() + " §cestá com o inventário cheio.");
									return true;
								} 
							} for (ItemStack i : INVENTORY_player_Y.getArmorContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									PlayerY.sendMessage("§cEsteja sem armaduras equipadas antes de aceitar um convite de duelo.");
									PlayerX.sendMessage("§cO duelo não pode começar porque §e" + PlayerY.getName() + " §cestá com armaduras equipadas.");
									return true;
								}
							}
							
								sumoInvite.getConvidou().sendMessage("§a[Sumo] " + PlayerY.getName() + " §aaceitou seu pedido de duelo.");
								sumoInvite.getPlayerY().sendMessage("§a[Sumo] Você aceitou o pedido de duelo de " + PlayerX.getName());
											
                                int kb = getDataInfo(PlayerX).getKB();
                                int pot = getDataInfo(PlayerX).getPotLvl(); 
                                int arena = getDataInfo(PlayerX).getArena();	
	                                getDataInfo(p).setKB(kb);
	                                getDataInfo(p).setArena(arena);
	                                getDataInfo(p).setPotLvl(pot);
								PlayerX.setHealth(20);
								PlayerY.setHealth(20);
								hideOnEnterSumo(PlayerX, PlayerY);
											
							switch (arena) {
								case 0:
									LocationAPI.getLocation().sumoTp(PlayerX, loc_sumo.POS1_SUMO_CLASSICA);
									LocationAPI.getLocation().sumoTp(PlayerY, loc_sumo.POS2_SUMO_CLASSICA);
									break;
								case 1:
									LocationAPI.getLocation().sumoTp(PlayerX, loc_sumo.POS1_SUMO_PEQUENA);
									LocationAPI.getLocation().sumoTp(PlayerY, loc_sumo.POS2_SUMO_PEQUENA);
									break;
								case 2:
									LocationAPI.getLocation().sumoTp(PlayerX, loc_sumo.POS1_SUMO_GRANDE);
									LocationAPI.getLocation().sumoTp(PlayerY, loc_sumo.POS2_SUMO_GRANDE);
									break;
								default:
									break;
							}
							switch (pot) {
								case 0:
									break;
								case 1:
									PlayerX.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
									PlayerY.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
									break;
								case 2:
									PlayerX.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
									PlayerY.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
									break;
								case 3:
									PlayerX.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
									PlayerY.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
									break;
								default:
									break;
							}
							switch (kb) {
								case 0:
									break;
								case 1:
									PlayerX.getInventory().addItem(addKbItem(p, 1));
									PlayerY.getInventory().addItem(addKbItem(p, 1));
									break;
								case 2:
									PlayerX.getInventory().addItem(addKbItem(p, 2));
									PlayerY.getInventory().addItem(addKbItem(p, 2));
									break;
								case 3:
									PlayerX.getInventory().addItem(addKbItem(p, 3));
									PlayerY.getInventory().addItem(addKbItem(p, 3));
									break;
								default:
									break;
						}
							SumoDuelManager sumoManager = SumoDuelManager.getInstance();
							sumoManager.getDuelando().add(PlayerX);
							sumoManager.getDuelando().add(PlayerY);
							sumoManager.duelandoHash.put(PlayerX, PlayerY);
							sumoManager.duelandoHash.put(PlayerY, PlayerX);
							SimpleclansAPI.getAPI().enableClanDamage(PlayerX);
							SimpleclansAPI.getAPI().enableClanDamage(PlayerY);
							getDataInfo(PlayerX).setMagicWaterStatus(false);
							getDataInfo(PlayerY).setMagicWaterStatus(false);
							PlayerX.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600*20, 0));
							PlayerY.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 600*20, 0));
							
							sumoInvite.runPvPTimer(PlayerX, PlayerY);
							
							sumoInvite.recuseOrExpireInvite(sumoInvite.getPlayerX(), sumoInvite.getPlayerY());
							
							// TELEPORTAR PRIMEIRO, ADD NO DUELANDO DEPOIS (SEMPRE)
								return true;
							}
							if (invite.hasInvite(p)) {
								p.sendMessage("§cVocê está sendo convidado para um SUMO mas também possui um convite de X1 PVP pendente.");
								return true;
							}
							p.sendMessage("§cVocê não possui um convite de sumo pendente ou ele expirou.");
							return true;
						} else {
							p.sendMessage("§c[Sumo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
							return true;
							}
						} else {
	        			  p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
	        			  return true;
						}
					}
					p.sendMessage("§cUtilize: /Duelo aceitar <Sumo/X1>");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("recusar")) {
				SumoInviteManager sumoInvite = SumoInviteManager.getInstance();
				if (args.length == 1) {
					p.sendMessage("§cSintaxe: /duelo recusar <X1 ou Sumo>.");
					return true;
				}
				if (args.length == 2) {
					if (args[1].equalsIgnoreCase("x1")) {
					if (invite.getConvidou() != p) {
						if (invite.hasInvite(p)) {
							invite.getConvidou().sendMessage("§c[Duelo] " + invite.getPlayerY().getName() + " §crecusou seu pedido de duelo.");
							invite.getPlayerY().sendMessage("§c[Duelo] Você recusou o pedido de duelo de " + invite.getPlayerX().getName());
							invite.recuseOrExpireInvite(invite.getPlayerX(), invite.getPlayerY());
							return true;
						} else {
							p.sendMessage("§cVocê não possui um convite de duelo pendente ou ele expirou.");
							return true;
						}
					} else {
						p.sendMessage("§c[Duelo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
						return true;
					}
				}
				}
				// SUMO CODE	
				if (args[1].equalsIgnoreCase("sumo")) {
					if (sumoInvite.getConvidou() != p) {
						if (sumoInvite.hasInvite(p)) {
							sumoInvite.getConvidou().sendMessage("§c[Sumo] " + sumoInvite.getPlayerY().getName() + " §crecusou seu pedido de duelo.");
							sumoInvite.getPlayerY().sendMessage("§c[Sumo] Você recusou o pedido de duelo de " + sumoInvite.getPlayerX().getName());
							sumoInvite.recuseOrExpireInvite(sumoInvite.getPlayerX(), sumoInvite.getPlayerY());
							return true;
						} else {
							p.sendMessage("§cVocê não possui um convite de sumo pendente ou ele expirou.");
							return true;
						}
					}  else {
						p.sendMessage("§c[Sumo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
						return true;
					}
				}
				p.sendMessage("Sintaxe: /duelo recusar <X1 ou Sumo>.");
				return true;
			}
			
			
			try {
				MenuListeners.openMenuPrincipal(p);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				p.sendMessage("§cHouve um erro ao abrir o menu de duelo. Entre em contato com um Administrador!");
			}
	return false;
	
}
	
	public void hideOnEnter(Player player1, Player player2) {
		for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
			duelandoPlayers.hidePlayer(player1);
			duelandoPlayers.hidePlayer(player2);
			player1.hidePlayer(duelandoPlayers);
			player2.hidePlayer(duelandoPlayers);
		}
		player1.showPlayer(player2);
		player2.showPlayer(player1);
	}

	
	public void hideOnEnterSumo(Player player1, Player player2) {
		for (Player duelandoPlayers : SumoDuelManager.getInstance().getDuelando()) {
			duelandoPlayers.hidePlayer(player1);
			duelandoPlayers.hidePlayer(player2);
			player1.hidePlayer(duelandoPlayers);
			player2.hidePlayer(duelandoPlayers);
		}
		player1.showPlayer(player2);
		player2.showPlayer(player1);
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
	
	   private ItemStack addKbItem(Player p, int lvl) {
		    ItemStack item = new ItemStack(Material.BLAZE_ROD);
		    ItemMeta meta = item.getItemMeta();
		    meta.setDisplayName("§aKnockback " + lvl);
		    item.setItemMeta(meta);
		    item.addUnsafeEnchantment(Enchantment.KNOCKBACK, lvl);
		    return item;
		}
}
