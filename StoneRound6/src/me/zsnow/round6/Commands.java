package me.zsnow.round6;

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

import me.zsnow.round6.api.LocationAPI;
import me.zsnow.round6.api.SchematicAPI;
import me.zsnow.round6.api.StringReplaceAPI;
import me.zsnow.round6.api.LocationAPI.location;
import me.zsnow.round6.configs.Configs;
import me.zsnow.round6.manager.PedestreClass;
import me.zsnow.round6.manager.SemaforoClass;
import me.zsnow.round6.manager.SemaforoClass.sinal;

public class Commands implements CommandExecutor {
	
	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	final private String NO_PERM_ADM = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	final String barras = "▂";
	int taskID;

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("round6")) {
			if (args.length < 1) {
				sender.sendMessage(" ");
				sender.sendMessage(" §6§lROUND-6 §7- Infos");
				sender.sendMessage(" ");
				sender.sendMessage(" §e/Round6 entrar");
				sender.sendMessage(" §e/Round6 sair");
				sender.sendMessage(" §e/Round6 info");
				if (sender.hasPermission("zs.mod")) {
					sender.sendMessage(" §c/Round6 iniciar");
					sender.sendMessage(" §c/Round6 parar");
					sender.sendMessage(" §c/Round6 moderar");
					sender.sendMessage(" §c/Round6 modsair");
					sender.sendMessage(" §c/Round6 kick <jogador>");
					sender.sendMessage(" §c/Round6 set <Loc>");
					sender.sendMessage(" ");
				}
				return true;
			}
			SemaforoClass evento = SemaforoClass.getSemaforo();
			PedestreClass members = PedestreClass.getInstance();
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					final Player p = (Player) sender;
					if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
						if (!members.getPedestres().contains(p)) {
							if (members.getPedestres().size() < maxPlayers) {
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
								LocationAPI.getLocation().teleportTo(p, location.ENTRADA);
								members.getPedestres().add(p);
								for (String msg : Configs.config.getConfig().getStringList("Entrou-evento")) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("_", barras)));
								}
							} else {
								p.sendMessage("§cO evento está lotado para novos participantes (" + members.getPedestres().size() + "§c/" + maxPlayers + "§c)");
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
							SchematicAPI.setAirAtLocation(); // limpando o espaço para por a parede
							SchematicAPI.loadSchematicParede();
							evento.setEventoStatus(true);
							evento.setEntradaStatus(true);
							evento.setTempoEntrada(evento.getTempoEntrada() + 10);
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								
								@Override
								public void run() {
									if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
										if (evento.getTempoEntrada() > 10) {
											evento.setTempoEntrada(evento.getTempoEntrada() - 10);
											for (String msg : Configs.config.getStringList("Broadcast.Entrada")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
											}
										} else {
											if (members.getPedestres().size() >= Configs.config.getConfig().getInt("minimo-de-jogadores")) {
												for (String msg : Configs.config.getConfig().getStringList("Broadcast.Entrada-fechada")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
												}
												evento.setEntradaStatus(false);
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
							sender.sendMessage("§cUtilize: /Round6 set <Entrada/Saida/Moderar/Schem/Boneca-head/AR1/AR2>");
							return true;
						}
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("Entrada")) {
								p.sendMessage("§eA entrada do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.ENTRADA);
								return true;
							}
							if (args[1].equalsIgnoreCase("Saida")) {
								p.sendMessage("§eA saída do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SAIDA);
								return true;
							}
							if (args[1].equalsIgnoreCase("schem")) {
								p.sendMessage("§eA schematic do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SCHEM);
								return true;
							}
							if (args[1].equalsIgnoreCase("Moderar")) {
								p.sendMessage("§eA área de moderação do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.MODERAR);
								return true;
							}
							if (args[1].equalsIgnoreCase("AR1")) {
								p.sendMessage("§eA área de AR do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.LOC_AIR_1);
								return true;
							}
							if (args[1].equalsIgnoreCase("AR2")) {
								p.sendMessage("§eA área de AR do evento Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.LOC_AIR_2);
								return true;
							}
							if (args[1].equalsIgnoreCase("Boneca-head")) {
								p.sendMessage("§eA área da cabeça da boneca Round-6 foi definida.");
								LocationAPI.getLocation().setLocation(p, location.BONECA);
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
							if (SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
								p.sendMessage("§cO evento está sendo finalizado. Por favor, aguarde.");
								p.sendMessage("§cEsta ação pode demorar um pouquinho afim de evitar bugs.");
								(new BukkitRunnable() {
									
									@Override
									public void run() {
										if (SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
											return;
										} else {
											evento.setEventoStatus(false);
											for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-cancelado")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
											}
											for (Player participantes : members.getPedestres()) {
												participantes.getInventory().clear();
												participantes.getInventory().setArmorContents(null);
												LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
												for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
													participantes.removePotionEffect(AllPotionEffects.getType());
												}
											}
											evento.resetData();
											cancel();
										}
									}
								}).runTaskTimer(Main.getInstance(), 20L, 0L);
							} else {
								evento.setEventoStatus(false);
								for (String msg : Configs.config.getConfig().getStringList("Broadcast.Evento-cancelado")) {
									Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
								}
								for (Player participantes : members.getPedestres()) {
									participantes.getInventory().clear();
									participantes.getInventory().setArmorContents(null);
									LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
									for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
										participantes.removePotionEffect(AllPotionEffects.getType());
									}
								}
								evento.resetData();
							}
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
					if (members.getPedestres().contains(p)) {
						members.getPedestres().remove(p);
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.sendMessage("§aVocê saiu do evento!");
						for (Player participantes : members.getPedestres()) {
							participantes.sendMessage("§c" + p.getName() + "§e saiu do evento.");
						}
						for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
							p.removePotionEffect(AllPotionEffects.getType());
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
						p.sendMessage("§eVocê foi enviado para a área de moderação do evento Round-6.");
						LocationAPI.getLocation().teleportTo(p, location.MODERAR);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do evento Round-6.");
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					p.sendMessage(" ");
					p.sendMessage(" §e➜ §6§lRound-6 §7(Info)");
					p.sendMessage(" ");
					p.sendMessage(" §6Ocorrendo: " + (evento.getEventoStatus() == true ? "§dSim" : "§dNão"));
					p.sendMessage(" §6Participantes: §d" + members.getPedestres().size());
					p.sendMessage(" ");
					return true;
				}
			}
		}
		return false;
	}
	
}
