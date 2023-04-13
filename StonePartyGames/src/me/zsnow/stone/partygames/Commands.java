package me.zsnow.stone.partygames;

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

import me.zsnow.stone.partygames.api.LocationAPI;
import me.zsnow.stone.partygames.api.LocationAPI.getMinigameType;
import me.zsnow.stone.partygames.api.LocationAPI.location;
import me.zsnow.stone.partygames.api.StringReplaceAPI;
import me.zsnow.stone.partygames.configs.Configs;
import me.zsnow.stone.partygames.games.CrazyPool;
import me.zsnow.stone.partygames.games.MainEvent;
import me.zsnow.stone.partygames.games.MainEvent.minigame;

public class Commands implements CommandExecutor {
	
	private final String prefix = Main.getInstance().getPrefix;
	MainEvent evento = MainEvent.getInstance();
	int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	int taskID;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("partygames")) {
			if (args.length < 1) {
				sender.sendMessage(" ");
				sender.sendMessage("   " + prefix + " §7- (Comandos)");
				sender.sendMessage(" ");
				sender.sendMessage("  §7➲ §f/partyGames entrar");
				sender.sendMessage("  §7➲ §f/partyGames sair");
				sender.sendMessage("  §7➲ §f/partyGames info");
			if (sender.hasPermission("zs.mod")) {
				sender.sendMessage("  §7➲ §c/partyGames moderar");
				sender.sendMessage("  §7➲ §c/partyGames modsair");
				sender.sendMessage("  §7➲ §4/partyGames espectadores <on/off>");
				}
			if (sender.hasPermission("zs.admin")) {
				sender.sendMessage("  §7➲ §4/partyGames iniciar");
				sender.sendMessage("  §7➲ §4/partyGames parar");
				sender.sendMessage("  §7➲ §4/partyGames setLocation <LOC>");
				sender.sendMessage("  §7➲ §4/partyGames kick <jogador>");
				sender.sendMessage("  §7➲ §4/partyGames nextgame");
				sender.sendMessage(" ");
			}
			return true;
		}
			if (args.length == 1) {
				switch (args[0].toLowerCase()) {
					case "entrar":
						final Player p = (Player) sender;
						if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
							if (!evento.getParticipantes().contains(p)) {
								if (evento.getParticipantes().size() < maxPlayers) {
									PlayerInventory inv = p.getInventory();
									for (ItemStack i : inv.getContents()) {
										if(i != null && !(i.getType() == Material.AIR)) {
											p.sendMessage("§cEsvazie o seu inventário para participar do evento.");
											return true;
										} 
									} for (ItemStack i : inv.getArmorContents()) {
										if(i != null && !(i.getType() == Material.AIR)) {
											p.sendMessage("§cRetire sua armadura do corpo para participar do evento.");
											return true;
										}
									}
									for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
										p.removePotionEffect(AllPotionEffects.getType());
									}
									p.chat("/on");
								//	LocationAPI.getLocation().teleportTo(p, location.LOBBY);
									CrazyPool c = new CrazyPool();
									c.startTaskLobby();
									evento.getParticipantes().add(p);
									
								} else {
									p.sendMessage("§cO evento está lotado para novos participantes (" + evento.getParticipantes().size() + "§c/" + maxPlayers + "§c)");
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
						break;
					case "sair":
						Player pa = (Player) sender;
						CrazyPool.instance.createCircle(pa);
						sender.sendMessage("");
						break;
					case "info":
						sender.sendMessage("");
						break;
					case "moderar":
						sender.sendMessage("");
						break;
					case "modsair":
						sender.sendMessage("");
						break;
					case "iniciar":
						if (sender.hasPermission("zs.admin")) {
							if (evento.getEventoStatus() == false && evento.getEntradaStatus() == false) {
								evento.resetData();
								evento.setEventoStatus(true);
								evento.setEntradaStatus(true);
								evento.setPvPStatus(false);
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
												if (evento.getParticipantes().size() >= Configs.config.getConfig().getInt("minimo-de-jogadores")) {
													for (String msg : Configs.config.getConfig().getStringList("Broadcast.Entrada-fechada")) {
														Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
													}
													evento.setEntradaStatus(false);
													evento.registerMinigamesListener(minigame.CRAZY_POOL); // TIRAR DEPOIS
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
							sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
							return true;
						}
						break;
					case "parar":
						sender.sendMessage("");
						break;
					case "nextgame":
						sender.sendMessage("");
						break;
					default:
						sender.sendMessage("§c[PartyGames] Argumento inválido. Se você acha que isso é um erro, considere entrar em contato com um Administrador.");
						break;
					}
				return true;
			}
			
			// INTERMINADOS ABAIXO
			
			//ISSO AQUI DE PENSAR VAI SER DIFICIL PARA UM CACETE
			///party setlocation crazypool entrada
			// 0         1         2          3
			//          0         1          2
			if (args.length >= 1) {
				final Player p = (Player) sender;
				if (args[0].equalsIgnoreCase("setlocation")) {
					if (p.hasPermission("zs.admin")) {
						if (args.length <= 2) {
							switch (args[1].toLowerCase()) {
								case "crazypool":
									p.sendMessage("§c[PartyGames] Use: /partyGames setLocation <MINIGAME> <LOCATION>");
									p.sendMessage("§7Location de CrazyPool: [Entrada, Saida, Pos1-Vidro, Pos2-Vidro, Pos1-Agua, Pos2-Agua]");
									break;
								case "a":
									
									break;
								case "b":
									
									break;
	
								default:
									p.sendMessage("§c[PartyGames] Minigame não encontrado, tente por: [CrazyPool, X, Y, Z]");
									break;
								}
								return true;
						}
						if (args.length == 3) {
							switch (args[1].toLowerCase()) {
								case "crazypool":
									if (args[2].equalsIgnoreCase("Entrada")) {
										LocationAPI.getLocation().setLocation(p, getMinigameType.CRAZYPOOL, location.CRAZYPOOL_ENTRADA);
										p.sendMessage(prefix + " §3Location ENTRADA definida para CRAZYPOOL");
										return true;
									}
									if (args[2].equalsIgnoreCase("Saida")) {
										LocationAPI.getLocation().setLocation(p, getMinigameType.CRAZYPOOL, location.CRAZYPOOL_SAIDA);
										p.sendMessage(prefix + " §3Location SAIDA definida para CRAZYPOOL");
										return true;
									}
									if (args[2].equalsIgnoreCase("Schem-vidro")) {
										LocationAPI.getLocation().setLocation(p, getMinigameType.CRAZYPOOL, location.SCHEM_VIDRO);
										p.sendMessage(prefix + " §3Location SCHEM_VIDRO definida para CRAZYPOOL");
										return true;
									}
									if (args[2].equalsIgnoreCase("Pos2-Agua")) {
										LocationAPI.getLocation().setLocation(p, getMinigameType.CRAZYPOOL, location.CRAZYPOOL_AGUA_2);
										p.sendMessage(prefix + " §3Location AGUA_1 definida para CRAZYPOOL");
										p.sendMessage("§fA água precisa estar abaixo do Y 100 e a plataforma de pulo acima do Y 100.");
										return true;
									}
									if (args[2].equalsIgnoreCase("Pos1-Agua")) {
										LocationAPI.getLocation().setLocation(p, getMinigameType.CRAZYPOOL, location.CRAZYPOOL_AGUA_1);
										p.sendMessage(prefix + " §3Location AGUA_2 definida para CRAZYPOOL");
										p.sendMessage("§fA água precisa estar abaixo do Y 100 e a plataforma de pulo acima do Y 100.");
										return true;
									}
									break;
								case "a":
									
									break;
								case "b":
									
									break;

							default:
								p.sendMessage("§c[PartyGames] Minigame não encontrado, tente por: [CrazyPool, X, Y, Z]");
								break;
							}
							}
					} else {
						p.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
			}
			
			if (args.length == 2) { 
				if (args[0].equalsIgnoreCase("kick")) {
					//
				}
				if (args[0].equalsIgnoreCase("espectadores")) {
					//
				}
			}
	}
		
		
		return false;
	}

}
