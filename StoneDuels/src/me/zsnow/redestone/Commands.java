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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.NumberFormatAPI;
import me.zsnow.redestone.api.LocationAPI.loc_sumo;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.api.SimpleclansAPI;
import me.zsnow.redestone.listeners.MenuListeners;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.InviteManager;
import me.zsnow.redestone.manager.SumoDuelManager;
import me.zsnow.redestone.manager.SumoInviteManager;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {

	
	// AO COMEÇAR SETA AS INFO DO DATAINFO DO PLAYER 1 PARA O PLAYER 2 TMB PQ TA FICANDO VAZIO
	

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
			if (args[0].equalsIgnoreCase("help")) { // funfa
				if (args.length >= 1) {
					if (p.hasPermission("zs.gerente")) {
						p.sendMessage(" ");
						p.sendMessage(" §6§lDUELO §e- §f(Comandos)");
						p.sendMessage(" ");
						p.sendMessage(" §f/duelo moderar");
						p.sendMessage(" §f/duelo set [X1/SUMO]");;
						p.sendMessage(" §f/duelo verDuplas <Nick>");
						p.sendMessage(" §f/duelo pararLuta <Nick>");
						p.sendMessage(" §c/duelo manutenção <on/off>");
						//p.sendMessage(" §c/duelo reload");
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
					p.sendMessage("§eEnviado para a área de moderação. Utilize o comando §7'§f/duelo vertodos§7' §epara ver todos os jogadores.");
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
									LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_CLASSICA);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "SUMO_PEQUENA":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_PEQUENA);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "SUMO_GRANDE":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.SUMO_GRANDE);
									p.sendMessage("§eA área de moderação " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_CLASSICA":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS1_SUMO_CLASSICA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_CLASSICA":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS2_SUMO_CLASSICA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_PEQUENA":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS1_SUMO_PEQUENA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_PEQUENA":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS2_SUMO_PEQUENA);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS1_SUMO_GRANDE":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS1_SUMO_GRANDE);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
								case "POS2_SUMO_GRANDE":
									LocationAPI.getLocation().sumoTp(p, loc_sumo.POS2_SUMO_GRANDE);
									p.sendMessage("§eA área de " + local + " foi definida com sucesso!");
									break;
							default:
								p.sendMessage("§cUse: /duelo set [Sumo] <SUMO_CLASSICA, SUMO_PEQUENA, SUMO_GRANDE, POS1_SUMO_CLASSICA, POS2_SUMO_CLASSICA, POS1_SUMO_PEQUENA, POS2_SUMO_PEQUENA>, POS1_SUMO_GRANDE, POS2_SUMO_GRANDE>");
								break;
							}
						}
						p.sendMessage("§cUse: /duelo set [X1/SUMO] <argumentos>");
						return true;
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
			}
			
			// talvez eu apague esse cmd
			
			if (args[0].equalsIgnoreCase("desafiar") || args[0].equalsIgnoreCase("convidar") || args[0].equalsIgnoreCase("invite")) {
				if (args.length == 2) {
					if (DuelManager.getInstance().hasCoin(p)) {
						final Player target = Bukkit.getPlayer(args[1]);
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
							SumoDuelManager sumoManager = SumoDuelManager.getInstance();
							if (DuelManager.getInstance().getDuelando().contains(target)) {
								for (Entry<Player, Player> duplas : sumoManager.duelandoHash.entrySet()) {
								    Player key = duplas.getKey();
								    Player value = duplas.getValue();
								    if (duplas.getKey().getName().equals(target.getName())) {

								    	/*for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
											p.hidePlayer(duelandoPlayers);
										}

								    	p.showPlayer(key);
								    	p.showPlayer(value);

								    	 */
								    	p.sendMessage("§aVocê agora está assistindo o duelo entre " + key.getName() + " §ae " + value.getName() + "§a.");
								    }
								}
							} else {
								p.sendMessage("§cEste jogador não está participando de um duelo.");
								return true;
							}
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
							if (DuelManager.getInstance().getDuelando().contains(target)) {
								for (Entry<Player, Player> duplas : DuelManager.getInstance().duelandoHash.entrySet()) {
								    Player key = duplas.getKey();
								    Player value = duplas.getValue();
								    if (duplas.getKey().getName().equals(target.getName())) {
								    	DuelManager.getInstance().getDuelando().remove(key);
								    	DuelManager.getInstance().getDuelando().remove(value);
								    	
								    	LocationAPI.getLocation().teleportTo(key, location.SAIDA);
							    		DuelManager.getInstance().moneyBackFor(key);
							    		SimpleclansAPI.getAPI().disableClanDamage(key);
							    		
								    	LocationAPI.getLocation().teleportTo(value, location.SAIDA);
							    		DuelManager.getInstance().moneyBackFor(value);
							    		SimpleclansAPI.getAPI().disableClanDamage(value);
								    	
							    		DuelManager.getInstance().moneyBackFor(key);
							    		DuelManager.getInstance().moneyBackFor(value);
							    		key.chat("/on");
							    		value.chat("/on");
							    		key.sendMessage("§cSeu duelo foi encerrado!");
							    		value.sendMessage("§cSeu duelo foi encerrado!");
								    	p.sendMessage("§aVocê encerrou o duelo entre " + key.getName() + " §ae " + value.getName() + "§a.");
								    }
								}
					    		DuelManager.getInstance().duelandoHash.remove(target, DuelManager.getInstance().getMortoBy(target));
					    		DuelManager.getInstance().duelandoHash.remove(DuelManager.getInstance().getMortoBy(target), target);
							} else {
								p.sendMessage("§cEste jogador não está participando de um duelo.");
								return true;
							}
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
					if (args.length < 2) {
						p.sendMessage("§cUse: /duelo manutenção <on/off>");
						return true;
					}
					if (args.length == 2) {
						DuelManager duel = DuelManager.getInstance();
						if (args[1].equalsIgnoreCase("on")) {
							duel.setManutencaoStatus(true);
					    	for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
					    		LocationAPI.getLocation().teleportTo(duelandoPlayers, location.SAIDA);
					    		duel.moneyBackFor(duelandoPlayers);
					    		SimpleclansAPI.getAPI().disableClanDamage(duelandoPlayers);
					    		duelandoPlayers.sendMessage("§cTodos os duelos foram encerrados por um ADMINISTRADOR. Manutenção na arena sendo iniciada!");
								duelandoPlayers.showPlayer(duelandoPlayers);
							}
					    	duel.getDuelando().clear();
					    	duel.duelandoHash.clear();
					    	p.sendMessage("§aManutenção no duelo ativada!");
					    	return true;
						}
						if (args[1].equalsIgnoreCase("off")) {
							duel.setManutencaoStatus(false);
							p.sendMessage("§cManutenção no duelo desativada!");
							return true;
						}
					}
				} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando.");
					return true;
				}
		}
			// CONVIDOU É OPOSTO DE Y
		if (args[0].equalsIgnoreCase("aceitar")) { 
				if (args.length == 1) {
					if (invite.hasInvite(p)) {
						if (DuelManager.getInstance().getManutencaoStatus() == false) {
							if (invite.getConvidou() != p) {
								if (DuelManager.getInstance().hasCoin(invite.getPlayerX()) && DuelManager.getInstance().hasCoin(invite.getPlayerY())) {
									invite.getPlayerX().setHealth(20);
									invite.getPlayerY().setHealth(20);
								DuelManager.getInstance().paymentToEnter(invite.getPlayerX());
								DuelManager.getInstance().paymentToEnter(invite.getPlayerY());
										hideOnEnter(invite.getPlayerX(), invite.getPlayerY());
								LocationAPI.getLocation().teleportTo(invite.getPlayerX(), location.POS1);
								LocationAPI.getLocation().teleportTo(invite.getPlayerY(), location.POS2);
									DuelManager.getInstance().getDuelando().add(invite.getPlayerX());
									DuelManager.getInstance().getDuelando().add(invite.getPlayerY());
								DuelManager.getInstance().duelandoHash.put(invite.getPlayerX(), invite.getPlayerY());
								DuelManager.getInstance().duelandoHash.put(invite.getPlayerY(), invite.getPlayerX());
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
					if (DuelManager.getInstance().getManutencaoSumoStatus() == false) {
						SumoInviteManager sumoInvite = SumoInviteManager.getInstance();
					if (sumoInvite.getConvidou() != p) {
						if (sumoInvite.hasInvite(p)) {
							final Player PlayerX = sumoInvite.getPlayerX();
								final Player PlayerY = sumoInvite.getPlayerY();
						//			final int arena = getDataInfo(PlayerX).getArena();
										//final int potLvl = getDataInfo(PlayerX).getPotLvl();
											//final int kb = getDataInfo(PlayerX).getKB();
											
			                                int kb = getDataInfo(invite.getConvidou()).getKB();
			                                int pot = getDataInfo(invite.getConvidou()).getPotLvl(); 
			                                int arena = getDataInfo(invite.getConvidou()).getArena();	
			                                getDataInfo(p).setKB(kb);
			                                getDataInfo(p).setArena(arena);
			                                getDataInfo(p).setPotLvl(pot);
											
											PlayerX.setHealth(20);
											PlayerY.setHealth(20);
											hideOnEnter(PlayerX, PlayerY);
											
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
							sumoInvite.getConvidou().sendMessage("§a[Sumo] " + PlayerY.getName() + " §aaceitou seu pedido de duelo.");
							sumoInvite.getPlayerY().sendMessage("§a[Sumo] Você aceitou o pedido de duelo de " + PlayerX.getName());
							sumoInvite.recuseOrExpireInvite(sumoInvite.getPlayerX(), sumoInvite.getPlayerY());
							
							// TELEPORTAR PRIMEIRO, ADD NO DUELANDO DEPOIS (SEMPRE)
								return true;
							}
						} else {
							p.sendMessage("§c[Sumo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
							return true;
							}
						} else {
	        			  p.sendMessage("§cO sistema de duelos está em manutenção. Volte novamente mais tarde!");
	        			  return true;
						}
					}
				p.sendMessage("§cVocê não possui um convite de duelo pendente ou ele expirou.");
				return true;
			}
			if (args[0].equalsIgnoreCase("recusar")) {
				SumoInviteManager sumoInvite = SumoInviteManager.getInstance();
				if (args.length == 1) {
					p.sendMessage("Sintaxe: /duelo recusar <X1 ou Sumo>.");
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
							p.sendMessage("§cVocê não possui um convite de duelo pendente ou ele expirou.");
							return true;
						}
					}  else {
							p.sendMessage("§c[Duelo] Você já convidou um jogador, aguarde que ele responda ao seu pedido.");
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
	
	public void staffHidePvPduel(Player staff, Player player1, Player player2) {
		for (Player duelandoPlayers : DuelManager.getInstance().getDuelando()) {
			staff.hidePlayer(duelandoPlayers);
		}
		staff.showPlayer(player1);
		staff.showPlayer(player2);
	}
	
	public void staffHideSumoduel(Player staff, Player player1, Player player2) {
		SumoDuelManager sumo = SumoDuelManager.getInstance();
		for (Player duelandoPlayers : sumo.getDuelando()) {
			staff.hidePlayer(duelandoPlayers);
		}
		staff.showPlayer(player1);
		staff.showPlayer(player2);
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
