package me.zsnow.redestone;

import java.io.IOException;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zsnow.redestone.api.LocationAPI;
import me.zsnow.redestone.api.NumberFormatAPI;
import me.zsnow.redestone.api.LocationAPI.location;
import me.zsnow.redestone.config.Configs;
import me.zsnow.redestone.api.SimpleclansAPI;
import me.zsnow.redestone.listeners.MenuListeners;
import me.zsnow.redestone.manager.DuelManager;
import me.zsnow.redestone.manager.InviteManager;
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
			if (args[0].equalsIgnoreCase("help")) { // funfa
				if (args.length >= 1) {
					if (p.hasPermission("zs.gerente")) {
						p.sendMessage(" ");
						p.sendMessage(" §6§lDUELO §e- §f(Comandos)");
						p.sendMessage(" ");
						p.sendMessage(" §f/duelo moderar");
						p.sendMessage(" §f/duelo set <Entrada, Saida, Moderar, Camarote, pos[1/2]>");;
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
				//	Configs.config.saveDefaultConfig();
				//	Configs.locations.saveDefaultConfig();
				//	Configs.mito.saveDefaultConfig();
					//Configs.config.reloadConfig();
					//Configs.locations.reloadConfig();
					//Configs.mito.reloadConfig();
					p.sendMessage("§cConfigurações recarregadas.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("moderar")) {
				if (p.hasPermission("zs.mod")) {
					LocationAPI.getLocation().teleportTo(p, location.MODERAR);
					p.sendMessage("§eEnviado para a área de moderação. Utilize o comando §7'§f/duelo verduplas <nick>§7' §epara assistir apenas um duelo em específico.");
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("set")) {
				if (args.length < 2) {
					if (p.hasPermission("zs.gerente")) {
						p.sendMessage("§cUse: /duelo set <Entrada, Saida, Moderar, Camarote, pos[1/2]>");
						return true;
					} else {
						p.sendMessage("§cVocê não tem permissão para executar este comando.");
						return true;
					}
				} 
				if (args.length == 2) {
					if (p.hasPermission("zs.gerente")) {
						if (args[1].equalsIgnoreCase("Entrada")) {
							LocationAPI.getLocation().setLocation(p, location.ENTRADA);
							p.sendMessage("§eA área de Entrada foi definida com sucesso!");
							return true;
						}
						if (args[1].equalsIgnoreCase("Saida")) {
							LocationAPI.getLocation().setLocation(p, location.SAIDA);
							p.sendMessage("§eA área de Saida foi definida com sucesso!");
							return true;
						}
						if (args[1].equalsIgnoreCase("Moderar")) {
							LocationAPI.getLocation().setLocation(p, location.MODERAR);
							p.sendMessage("§eA área de moderação foi definida com sucesso!");
							return true;
						}
						if (args[1].equalsIgnoreCase("Camarote")) {
							LocationAPI.getLocation().setLocation(p, location.CAMAROTE);
							p.sendMessage("§eA área de Camarote foi definida com sucesso!");
							return true;
						}
						if (args[1].equalsIgnoreCase("pos1")) {
							LocationAPI.getLocation().setLocation(p, location.POS1);
							p.sendMessage("§eA área de pos1 foi definida com sucesso!");
							return true;
						}
						if (args[1].equalsIgnoreCase("pos2")) {
							LocationAPI.getLocation().setLocation(p, location.POS2);
							p.sendMessage("§eA área de pos2 foi definida com sucesso!");
							return true;
						} else {
							p.sendMessage("§cUse: /duelo set <Entrada, Saida, Moderar, Camarote, pos[1/2]>");
							return true;
						}
					} else {
						p.sendMessage("§cVocê não tem permissão para executar este comando.");
						return true;
					}
				}
			}
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
							if (DuelManager.getInstance().getDuelando().contains(target)) {
								for (Entry<Player, Player> duplas : DuelManager.getInstance().duelandoHash.entrySet()) {
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
		if (args[0].equalsIgnoreCase("aceitar")) { // OU TU COPIA A CLASS INVITE OU TU REFAZ TUDO DESDE O PVP
				if (args.length == 1) {
					if (DuelManager.getInstance().getManutencaoStatus() == false) {
					if (invite.getConvidou() != p) {
						if (invite.hasInvite(p)) {
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
								p.sendMessage("§cVocê não possui um convite de duelo pendente ou ele expirou.");
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
				}
			if (args[0].equalsIgnoreCase("recusar")) {
				if (args.length == 1) {
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
	
}
