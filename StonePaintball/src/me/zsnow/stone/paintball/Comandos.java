package me.zsnow.stone.paintball;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stone.paintball.api.LocationAPI;
import me.zsnow.stone.paintball.api.StringReplaceAPI;
import me.zsnow.stone.paintball.api.LocationAPI.location;
import me.zsnow.stone.paintball.configs.Configs;
import me.zsnow.stone.paintball.manager.EventController;
import me.zsnow.stone.paintball.manager.MembersController;
import me.zsnow.stone.paintball.times.TeamBlue;
import me.zsnow.stone.paintball.times.TeamRed;

public class Comandos implements CommandExecutor {

	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	final private String NO_PERM_ADM = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	ItemStack enderArma = Listeners.getGun();
	final String barras = "▂";
	int taskID;
	
	
	// ADICIONAR NO SAIR UMA CHECAGEM DE QUANTOS TEM NA ARENA, NO ONQUIT TMB
	
	// ADICIONAR LIMITE DE VIDA POR CONFIG & SALVAR QUANTAS VOCE TEM NUMA HASHMAP
	
	// LIMPAR TODAS AS VARIAVEL NO LISTENER ON QUIT E ETC
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("paintball")) {
			if (args.length < 1) {
				sender.sendMessage(" ");
				sender.sendMessage(" §e§lPAINT BALL §d- §7(Comandos)");
				sender.sendMessage(" ");
				sender.sendMessage(" §f/paintball entrar");
				sender.sendMessage(" §f/paintball sair");
				sender.sendMessage(" §f/paintball info");
				if (sender.hasPermission("zs.mod")) {
					sender.sendMessage(" §c/paintball moderar");
					sender.sendMessage(" §c/paintball modsair");
				}
				if (sender.hasPermission("zs.admin")) {
					sender.sendMessage(" §c/paintball iniciar");
					sender.sendMessage(" §c/paintball parar");
					sender.sendMessage(" ");
				}
			}
			EventController evento = EventController.getInstance();
			MembersController members = MembersController.getInstance();
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					final Player p = (Player) sender;
					TeamBlue teamBlue = TeamBlue.get();
					TeamRed teamRed = TeamRed.get();
					if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
						if (!members.getParticipantes().contains(p)) {
							if (members.getParticipantes().size() < maxPlayers) {
								PlayerInventory inv = p.getInventory();
								for (ItemStack i : inv.getContents()) {
									if(i != null && !(i.getType() == Material.AIR)) {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.inventario-cheio")));
										return true;
									} 
								} for (ItemStack i : inv.getArmorContents()) {
									if(i != null && !(i.getType() == Material.AIR)) {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.contem-armadura")));
										return true;
									}
								}
								for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
									p.removePotionEffect(AllPotionEffects.getType());
								}
								p.chat("/on");
								LocationAPI.getLocation().teleportTo(p, location.LOBBY);
								members.getParticipantes().add(p);
								p.getInventory().addItem(enderArma);
								Random random = new Random();
						        if (teamRed.getTeamRedSize() == teamBlue.getTeamBlueSize()) {
						            if (random.nextBoolean()) {
						                teamRed.getTeamRedList().add(p);
						                teamRed.sendTeamArmorToPlayer(p);
						                teamRed.putAbates(p);
						            } else {
						                teamBlue.getTeamBlueList().add(p);
						                teamBlue.sendTeamArmorToPlayer(p);
						                teamBlue.putAbates(p);
						            }
						        } else {
						            if (teamRed.getTeamRedSize() < teamBlue.getTeamBlueSize()) {
						            	 teamRed.getTeamRedList().add(p);
						            	 teamRed.sendTeamArmorToPlayer(p);
						            	 teamRed.putAbates(p);
						            } else {
						            	 teamBlue.getTeamBlueList().add(p);
						            	 teamBlue.sendTeamArmorToPlayer(p);
						            	 teamBlue.putAbates(p);
						            }
						        }
								for (String msg : Configs.config.getConfig().getStringList("Entrou-evento")) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("_", barras)));
								}
								p.sendMessage("§e[PaintBall] §fVocê entrou no time " + getTeam(p));
							} else {
								p.sendMessage("§cO evento está lotado para novos participantes (" + members.getParticipantes().size() + "§c/" + maxPlayers + "§c)");
								return true;
							}
						} else {
							p.sendMessage("§cVocê já está no evento!");
							return true;
						}
					} else {
						p.sendMessage("§cO evento não está ocorrendo ou sua entrada se encontra fechada.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (sender.hasPermission("zs.admin")) {
						if (evento.getEventoStatus() == false && evento.getEntradaStatus() == false) {
							evento.resetData();
							evento.setEventoStatus(true);
							evento.setEntradaStatus(true);
							evento.setTempo(evento.getTempo() + 10);
							TeamBlue teamBlue = TeamBlue.get();
							TeamRed teamRed = TeamRed.get();
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								
								@Override
								public void run() {
									if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
										if (evento.getTempo() > 10) {
											evento.setTempo(evento.getTempo() - 10);
											for (String msg : Configs.config.getStringList("Broadcast.Entrada")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
											}
										} else {
											if (members.getParticipantes().size() >= Configs.config.getConfig().getInt("minimo-de-jogadores")) {
												for (String msg : Configs.config.getConfig().getStringList("Broadcast.Entrada-fechada")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
												}
												evento.setEntradaStatus(false);
												for (Player timeAzul : teamBlue.getTeamBlueList()) {
													LocationAPI.getLocation().teleportTo(timeAzul, location.AZUL);
												}
												for (Player timeVermelho : teamRed.getTeamRedList()) {
													LocationAPI.getLocation().teleportTo(timeVermelho, location.VERMELHO);
												}
												evento.startTaskLobby();
												sh.cancelTask(taskID);
												
											} else {
												for (String msg : Configs.config.getConfig().getStringList("Broadcast.Sem-jogadores")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
												}
												sh.cancelTask(taskID);
												evento.resetData();
											}
										}
									} else { sh.cancelTask(taskID); }
								}
							}, 0L, 20*10L);
						} else {
							sender.sendMessage("§cO evento já está ocorrendo.");
							return true;
						}
					} else {
						sender.sendMessage(NO_PERM_ADM);
						return true;
					}
					return true;
				}
				final Player p = (Player) sender;
				if (args[0].equalsIgnoreCase("set")) {
					if (sender.hasPermission("zs.gerente")) {
						if (args.length < 2) {
							sender.sendMessage("§cUtilize: /paintball set <Entrada;Saida;Moderar;Azul;Vermelho>");
							return true;
						}
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("Entrada")) {
								p.sendMessage("§eA entrada do evento paintball foi definida.");
								LocationAPI.getLocation().setLocation(p, location.LOBBY);
								return true;
							}
							if (args[1].equalsIgnoreCase("Saida")) {
								p.sendMessage("§eA saída do evento paintball foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SAIDA);
								return true;
							}
							if (args[1].equalsIgnoreCase("Moderar")) {
								p.sendMessage("§eA área de moderação do evento paintball foi definida.");
								LocationAPI.getLocation().setLocation(p, location.MODERAR);
								return true;
							}
							if (args[1].equalsIgnoreCase("Azul")) {
								p.sendMessage("§eA área do time AZUL do evento paintball foi definida.");
								LocationAPI.getLocation().setLocation(p, location.AZUL);
								return true;
							}
							if (args[1].equalsIgnoreCase("Vermelho")) {
								p.sendMessage("§eA área do time VERMELHO do evento paintball foi definida.");
								LocationAPI.getLocation().setLocation(p, location.VERMELHO);
								return true;
							}
						}
					} else {
						sender.sendMessage("§cVocê não tem permissão para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("parar")) {
					if (p.hasPermission("zs.admin")) {
						if (evento.getEventoStatus() == true) {
							evento.setEventoStatus(false);
							for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-cancelado")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
							}
							for (Player participantes : members.getParticipantes()) {
								participantes.getInventory().clear();
								participantes.getInventory().setArmorContents(null);
								LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
								participantes.showPlayer(participantes);
								participantes.spigot().setCollidesWithEntities(true);
							}
							evento.resetData();
						} else {
							p.sendMessage("§cO evento não está acontecendo!");
							return true;
						}
					} else {
						p.sendMessage(NO_PERM_ADM);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("sair")) {
					if (members.getParticipantes().contains(p)) {
						TeamBlue teamBlue = TeamBlue.get();
						TeamRed teamRed = TeamRed.get();
						members.getParticipantes().remove(p);
						teamRed.getTeamRedList().remove(p);
						teamBlue.getTeamBlueList().remove(p);
						teamBlue.abates.remove(p);
						teamRed.abates.remove(p);
						members.deleteVidas(p);
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						p.spigot().setCollidesWithEntities(true);
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.sendMessage("§aVocê saiu do evento!");
						p.spigot().setCollidesWithEntities(true);
						for (Player participantes : members.getParticipantes()) {
							participantes.showPlayer(p);
							participantes.sendMessage("§c" + p.getName() + "§e saiu do evento.");
						}
						if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
							evento.eventCanKeepRunning();
						}
						return true;
					} else {
						p.sendMessage("§cVocê não está no evento!");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a área de moderação do evento PaintBall.");
						LocationAPI.getLocation().teleportTo(p, location.MODERAR);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do evento PaintBall.");
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					p.sendMessage(" ");
					p.sendMessage(" §6➜ §e§lPAINTBALL §7(Info)");
					p.sendMessage(" ");
					p.sendMessage(" §6Ocorrendo: " + (evento.getEventoStatus() == true ? "§eSim" : "§eNão"));
					p.sendMessage(" §6Combate: " + (evento.getArmasLiberadas() == true ? "§eLiberado" : "§eAguardando liberação"));
					p.sendMessage(" §6Participantes: §e" + members.getParticipantes().size());
					p.sendMessage(" ");
					return true;
				}
			}
		}
		return false;
	}
	
	private String getTeam(Player p) {
		TeamBlue teamBlue = TeamBlue.get();
		TeamRed teamRed = TeamRed.get();
		if (teamRed.getTeamRedList().contains(p)) {
			return "§c§lVERMELHO";
		}
		if (teamBlue.getTeamBlueList().contains(p)) {
			return "§3§lAZUL";
		} else {
			return "§7[?]";
		}
	}
	
}
