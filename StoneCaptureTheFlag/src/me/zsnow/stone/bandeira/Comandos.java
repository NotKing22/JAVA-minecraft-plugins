package me.zsnow.stone.bandeira;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.sk89q.worldedit.bukkit.selections.Selection;

import me.zsnow.stone.bandeira.api.LocationAPI;
import me.zsnow.stone.bandeira.api.StringReplaceAPI;
import me.zsnow.stone.bandeira.api.LocationAPI.location;
import me.zsnow.stone.bandeira.configs.Configs;
import me.zsnow.stone.bandeira.manager.EventController;
import me.zsnow.stone.bandeira.manager.MembersController;
import me.zsnow.stone.bandeira.times.TeamBlue;
import me.zsnow.stone.bandeira.times.TeamRed;

public class Comandos implements CommandExecutor {

	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	final private String NO_PERM_ADM = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	final String barras = "▂";
	int taskID;
	
	// AO KICKAR A BANDEIRA N RETORNA POR ALGUM MOTIVO TALVEZ TENHA A VER COM O KEEPRUNNING E O SETTEAM N FIZ AINDA
	
	EventController evento = EventController.getInstance();
	MembersController members = MembersController.getInstance();
	TeamBlue teamBlue = TeamBlue.getInstance();
	TeamRed teamRed = TeamRed.getInstance();
	
	private Selection selection;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("capture")) {
			if (args.length < 1) {
				sender.sendMessage(" ");
				sender.sendMessage(" §d§lCAPTURE THE FLAG §d- §7(Comandos)");
				sender.sendMessage(" ");
				sender.sendMessage(" §f/capture entrar");
				sender.sendMessage(" §f/capture sair");
				sender.sendMessage(" §f/capture info");
				if (sender.hasPermission("zs.mod")) {
					sender.sendMessage(" §c/capture moderar");
					sender.sendMessage(" §c/capture modsair");
				}
				if (sender.hasPermission("zs.admin")) {
					sender.sendMessage(" §c/capture iniciar");
					sender.sendMessage(" §c/capture parar");
					sender.sendMessage(" §c/capture kick <jogador>");
				}
				if (sender.hasPermission("zs.gerente")) {
					sender.sendMessage(" §4/capture setloc");
					sender.sendMessage(" §4/capture setTeam <Player> <Blue;Red>");
				}
				sender.sendMessage(" ");
			}
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					final Player p = (Player) sender;
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
								Random random = new Random();
						        if (teamRed.getTeamRedSize() == teamBlue.getTeamBlueSize()) {
						            if (random.nextBoolean()) {
						                teamRed.getTeamRedList().add(p);
						                teamRed.sendTeamArmorToPlayer(p);
						                teamRed.setPlayerTeam(p);
						            } else {
						                teamBlue.getTeamBlueList().add(p);
						                teamBlue.sendTeamArmorToPlayer(p);
						                teamBlue.setPlayerTeam(p);
						            }
						        } else {
						            if (teamRed.getTeamRedSize() < teamBlue.getTeamBlueSize()) {
						            	 teamRed.getTeamRedList().add(p);
						            	 teamRed.sendTeamArmorToPlayer(p);
						            	   teamRed.setPlayerTeam(p);
						            } else {
						            	 teamBlue.getTeamBlueList().add(p);
						            	 teamBlue.sendTeamArmorToPlayer(p);
						            	 teamBlue.setPlayerTeam(p);
						            }
						        }
								for (String msg : Configs.config.getConfig().getStringList("Entrou-evento")) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("_", barras).replace("{time}", getTeam(p))));
								}
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
							evento.setTempo(evento.getTempo() + 30);
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								
								@Override
								public void run() {
									if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
										if (evento.getTempo() > 30) {
											evento.setTempo(evento.getTempo() - 30);
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
				if (args[0].equalsIgnoreCase("kick")) {
					if (sender.hasPermission("zs.admin")) {
						if (args.length == 2) {
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								sender.sendMessage("§cO jogador solicitado não se encontra online no momento!");
								return true;
							}
							if (!members.getParticipantes().contains(target)) {
								sender.sendMessage("§cO jogador solicitado não se encontra no evento!");
								return true;
							}
							
							for (Player participantes : members.getParticipantes()) {
								participantes.showPlayer(p);
								participantes.sendMessage("§e" + p.getName() + " saiu do evento.");
							}
							if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
								if (teamBlue.getPlayerWithFlag() != null && teamBlue.getPlayerWithFlag().equals(p)) {
									for (Player jogadores : teamBlue.getParticipantes()) {
										jogadores.sendMessage(" ");
										jogadores.sendMessage(" §3" + p.getName() + " §eAbandonou o evento com a bandeira.");
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
										jogadores.sendMessage(" §c" + p.getName() + " §eAbandonou o evento com a bandeira.");
										jogadores.sendMessage(" §e A bandeira retornou para o time AZUL.");
										jogadores.sendMessage(" ");
										jogadores.playSound(jogadores.getLocation(), Sound.SHOOT_ARROW, 1.0f, 1.0f);
									}
									teamBlue.clearPlayerWithFlag();
									teamBlue.setFlagPlacedStatus(true);
									teamBlue.removePlayerTeam(p);
								}
							}
							evento.setEliminado(target);
							sender.sendMessage("§eO jogador " + target.getName() + " §efoi expulso do evento!");
							evento.eventCanKeepRunning();
							return true;
						}
						if (args.length < 2) {
							sender.sendMessage("§cUse: /capture kick <jogador>.");
						}
						return true;
					} else {
						sender.sendMessage(NO_PERM_ADM);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("setTeam")) {
					if (p.hasPermission("zs.gerente")) {
						if (args.length <= 2) {
							p.sendMessage("§cUse: /capture setTeam <User> <Blue;Red>");
							return true;
						}
						if (args.length == 3) {
							if (evento.getEntradaStatus() == true) {
							Player target = Bukkit.getPlayer(args[1]);
							if (target == null) {
								p.sendMessage("§cO jogador solicitado não se encontra online!");
								return true;
							}
							if (!members.getParticipantes().contains(target)) {
								p.sendMessage("§cO jogador solicitado não se encontra no evento!");
								return true;
							}
							if (args[2].equalsIgnoreCase("Blue") || args[2].equalsIgnoreCase("Azul")) {
								if (!teamBlue.getTeamBlueList().contains(p)) {
									target.getInventory().clear();
									teamRed.getTeamRedList().remove(p);
									teamRed.removePlayerTeam(p);
									
					                teamBlue.getTeamBlueList().add(p);
					                teamBlue.sendTeamArmorToPlayer(p);
					                teamBlue.setPlayerTeam(p);
					                p.sendMessage("§4§l[CF] §4Você alterou o time de " + target.getName() + " §4para Azul.");
								} else {
									p.sendMessage("§cVocê já está no time Azul!");
								}
				                return true;
							}
							if (args[2].equalsIgnoreCase("Red") || args[2].equalsIgnoreCase("Vermelho")) {
								if (!teamRed.getTeamRedList().contains(p)) {
									target.getInventory().clear();
									teamBlue.getTeamBlueList().remove(p);
									teamBlue.removePlayerTeam(p);
									
					                teamRed.getTeamRedList().add(p);
					                teamRed.sendTeamArmorToPlayer(p);
					                teamRed.setPlayerTeam(p);
					                p.sendMessage("§4§l[CF] §4Você alterou o time de " + target.getName() + " §4para Vermelho.");
								} else {
									p.sendMessage("§cVocê já está no time Vermelho!");
								}
				                return true;
							}
							p.sendMessage("§cO time específicado não existe. Escolha entre Azul ou Vermelho.");
							return true;
							} else {
								p.sendMessage("§cVocê não pode alterar o time após o evento ter iniciado.");
								return true;
							}
						}
					} else {
						p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("setloc")) {
					if (sender.hasPermission("zs.gerente")) {
						if (args.length < 2) {
							sender.sendMessage("§cUtilize: /capture set <Entrada;Saida;Moderar;Azul;Vermelho;RedFlag;BlueFlag;RedBanner;BlueBanner>");
							sender.sendMessage("§cOBS: RedFlag e BlueFlag necessitam de seleção via WorldEdit.");
							return true;
						}
						if (args.length >= 2) {
							 selection = Main.getInstance().getWorldEdit().getSelection(p);
							if (args[1].equalsIgnoreCase("Entrada")) {
								p.sendMessage("§eA entrada do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.LOBBY);
								return true;
							}
							if (args[1].equalsIgnoreCase("Saida")) {
								p.sendMessage("§eA saída do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SAIDA);
								return true;
							}
							if (args[1].equalsIgnoreCase("Moderar")) {
								p.sendMessage("§eA área de moderação do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.MODERAR);
								return true;
							}
							if (args[1].equalsIgnoreCase("Azul")) {
								p.sendMessage("§eA área do time AZUL do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.AZUL);
								return true;
							}
							if (args[1].equalsIgnoreCase("Vermelho")) {
								p.sendMessage("§eA área do time VERMELHO do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.VERMELHO);
								return true;
							}
							if (args[1].equalsIgnoreCase("RedBanner")) {
								p.sendMessage("§eA área do time VERMELHO(Banner) do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.RED_BANNER);
								return true;
							}
							if (args[1].equalsIgnoreCase("BlueBanner")) {
								p.sendMessage("§eA área do time AZUL(Banner) do evento Capture the flag foi definida.");
								LocationAPI.getLocation().setLocation(p, location.BLUE_BANNER);
								return true;
							}
							if (args[1].equalsIgnoreCase("RedFlag")) {
								if (selection == null) {
									p.sendMessage("§cPrimeiramente selecione a área da bandeira utilizando o WorldEdit antes de prosseguir.");
									return true;
								}
								String local = "RED_FLAG";
								double x = selection.getMinimumPoint().getX();
						    	double y = selection.getMinimumPoint().getY();
								double z = selection.getMinimumPoint().getZ();
								float yaw = selection.getMinimumPoint().getYaw();
								float pitch = selection.getMinimumPoint().getPitch();
								String world = selection.getMinimumPoint().getWorld().getName();
								
								String local2 = "RED_FLAG_2";
								double x2 = selection.getMaximumPoint().getX();
						    	double y2 = selection.getMaximumPoint().getY();
								double z2 = selection.getMaximumPoint().getZ();
								float yaw2 = selection.getMaximumPoint().getYaw();
								float pitch2 = selection.getMaximumPoint().getPitch();
								String world2 = selection.getMaximumPoint().getWorld().getName();
								
								Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
								Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
								Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
								Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
								Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
								Configs.locations.getConfig().set(local + ".Mundo", world);
								
								Configs.locations.getConfig().set(local2 + ".X", Double.valueOf(x2));
								Configs.locations.getConfig().set(local2 + ".Y", Double.valueOf(y2));
								Configs.locations.getConfig().set(local2 + ".Z", Double.valueOf(z2));
								Configs.locations.getConfig().set(local2 + ".Yaw", Float.valueOf(yaw2));
								Configs.locations.getConfig().set(local2 + ".Pitch", Float.valueOf(pitch2));
								Configs.locations.getConfig().set(local2 + ".Mundo", world2);
								Configs.locations.saveConfig();
								p.sendMessage("§eA bandeira do time VERMELHO do evento Capture the flag foi definida.");
								return true;
							}
							if (args[1].equalsIgnoreCase("BlueFlag")) {
								if (selection == null) {
									p.sendMessage("§cPrimeiramente selecione a área da bandeira utilizando o WorldEdit antes de prosseguir.");
									return true;
								}
								String local = "BLUE_FLAG";
								double x = selection.getMinimumPoint().getX();
						    	double y = selection.getMinimumPoint().getY();
								double z = selection.getMinimumPoint().getZ();
								float yaw = selection.getMinimumPoint().getYaw();
								float pitch = selection.getMinimumPoint().getPitch();
								String world = selection.getMinimumPoint().getWorld().getName();
								
								String local2 = "BLUE_FLAG_2";
								double x2 = selection.getMaximumPoint().getX();
						    	double y2 = selection.getMaximumPoint().getY();
								double z2 = selection.getMaximumPoint().getZ();
								float yaw2 = selection.getMaximumPoint().getYaw();
								float pitch2 = selection.getMaximumPoint().getPitch();
								String world2 = selection.getMaximumPoint().getWorld().getName();
								
								Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
								Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
								Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
								Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
								Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
								Configs.locations.getConfig().set(local + ".Mundo", world);
								
								Configs.locations.getConfig().set(local2 + ".X", Double.valueOf(x2));
								Configs.locations.getConfig().set(local2 + ".Y", Double.valueOf(y2));
								Configs.locations.getConfig().set(local2 + ".Z", Double.valueOf(z2));
								Configs.locations.getConfig().set(local2 + ".Yaw", Float.valueOf(yaw2));
								Configs.locations.getConfig().set(local2 + ".Pitch", Float.valueOf(pitch2));
								Configs.locations.getConfig().set(local2 + ".Mundo", world2);
								Configs.locations.saveConfig();
								p.sendMessage("§eA bandeira do time AZUL do evento Capture the flag foi definida.");
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
						evento.setEliminado(p);
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.sendMessage("§aVocê saiu do evento!");
						for (Player participantes : members.getParticipantes()) {
							participantes.showPlayer(p);
							participantes.sendMessage("§e" + p.getName() + " saiu do evento.");
						}
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
						return true;
					} else {
						p.sendMessage("§cVocê não está no evento!");
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a área de moderação do evento Capture the flag.");
						LocationAPI.getLocation().teleportTo(p, location.MODERAR);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do evento Capture the flag.");
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					final String bandeira = "⚑";
					p.sendMessage(" ");
					p.sendMessage(" §6➜ §e§lCAPTURE THE FLAG §7(Info)");
					p.sendMessage(" ");
					p.sendMessage(" §e§lPLACAR: §b" + evento.getPointBlue() + " " + bandeira + " §7x §c" + evento.getPointRed() + " " + bandeira + "");
					p.sendMessage(" ");
					p.sendMessage(" §eOcorrendo: " + (evento.getEventoStatus() == true ? "§fSim" : "§fNão"));
					p.sendMessage(" §eCombate: " + (evento.getCaptureLiberado() == true ? "§fAtivado" : "§fDesativado"));
					p.sendMessage(" §eParticipantes: §f" + members.getParticipantes().size());
					p.sendMessage(" ");
					return true;
				}
			}
		}
		return false;
	}
	
	private String getTeam(Player p) {
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
