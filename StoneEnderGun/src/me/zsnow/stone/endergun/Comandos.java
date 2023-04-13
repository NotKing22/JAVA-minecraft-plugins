package me.zsnow.stone.endergun;

import java.util.Arrays;

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

import me.zsnow.stone.endergun.api.ItemBuilder;
import me.zsnow.stone.endergun.api.LocationAPI;
import me.zsnow.stone.endergun.api.LocationAPI.location;
import me.zsnow.stone.endergun.api.NBTItemStack;
import me.zsnow.stone.endergun.api.SchematicAPI;
import me.zsnow.stone.endergun.api.StringReplaceAPI;
import me.zsnow.stone.endergun.configs.Configs;
import me.zsnow.stone.endergun.manager.EventController;
import me.zsnow.stone.endergun.manager.MembersController;

public class Comandos implements CommandExecutor {

	final private int maxPlayers = Configs.config.getConfig().getInt("maximo-de-jogadores");
	final private String NO_PERM_ADM = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	final String barras = "▂";
	int taskID;
	
	
	// ADICIONAR NO SAIR UMA CHECAGEM DE QUANTOS TEM NA ARENA, NO ONQUIT TMB
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("endergun")) {
			if (args.length < 1) {
				sender.sendMessage(" ");
				sender.sendMessage(" §5§lENDER GUN §d- §7(Comandos)");
				sender.sendMessage(" ");
				sender.sendMessage(" §d/enderGun entrar");
				sender.sendMessage(" §d/enderGun sair");
				sender.sendMessage(" §d/enderGun info");
				if (sender.hasPermission("zs.admin")) {
					sender.sendMessage(" §c/enderGun moderar");
					sender.sendMessage(" §c/enderGun modsair");
					sender.sendMessage(" §c/enderGun iniciar");
					sender.sendMessage(" §c/enderGun parar");
					sender.sendMessage(" ");
				}
			}
			EventController evento = EventController.getInstance();
			MembersController members = MembersController.getInstance();
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					final Player p = (Player) sender;
					if (p.hasPermission("endergun.entrar")) {
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
								LocationAPI.getLocation().teleportTo(p, location.LOBBY);
								members.getParticipantes().add(p);
								p.getInventory().addItem(getGun());
								for (String msg : Configs.config.getConfig().getStringList("Entrou-evento")) {
									p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("_", barras)));
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
				} else {
					p.sendMessage("§cVocê precisa do rank [MercurioII] para entrar no evento.");
					return true;
				}
			}
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (sender.hasPermission("zs.admin")) {
						if (evento.getEventoStatus() == false && evento.getEntradaStatus() == false) {
							evento.resetData();
							SchematicAPI.loadSchematic();
							evento.setEventoStatus(true);
							evento.setEntradaStatus(true);
							evento.setTempo(evento.getTempo() + 10);
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
							sender.sendMessage("§cUtilize: /endergun set <Entrada/Saida/Moderar/Schem>");
							return true;
						}
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("Entrada")) {
								p.sendMessage("§eA entrada do evento EnderGun foi definida.");
								LocationAPI.getLocation().setLocation(p, location.LOBBY);
								return true;
							}
							if (args[1].equalsIgnoreCase("Saida")) {
								p.sendMessage("§eA saída do evento EnderGun foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SAIDA);
								return true;
							}
							if (args[1].equalsIgnoreCase("schem")) {
								p.sendMessage("§eA schematic do evento EnderGun foi definida.");
								LocationAPI.getLocation().setLocation(p, location.SCHEM);
								return true;
							}
							if (args[1].equalsIgnoreCase("Moderar")) {
								p.sendMessage("§eA área de moderação do evento EnderGun foi definida.");
								LocationAPI.getLocation().setLocation(p, location.MODERAR);
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
						members.getParticipantes().remove(p);
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.sendMessage("§aVocê saiu do evento!");
						for (Player participantes : members.getParticipantes()) {
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
						p.sendMessage("§eVocê foi enviado para a área de moderação do evento EnderGun.");
						LocationAPI.getLocation().teleportTo(p, location.MODERAR);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do evento EnderGun.");
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					p.sendMessage(" ");
					p.sendMessage(" §d➜ §5§lENDERGUN §7(Info)");
					p.sendMessage(" ");
					p.sendMessage(" §6Ocorrendo: " + (evento.getEventoStatus() == true ? "§dSim" : "§dNão"));
					p.sendMessage(" §6Quebrar neve: " + (evento.getArmasLiberadas() == true ? "§dLiberado" : "§dAguardando liberação"));
					p.sendMessage(" §6Participantes: §d" + members.getParticipantes().size());
					p.sendMessage(" ");
					return true;
				}
				if (args[0].equalsIgnoreCase("restoremap")) {
					if (p.hasPermission("zs.admin")) {
						p.sendMessage("§aSchematic foi carregada com sucesso...");
						SchematicAPI.loadSchematic();
					}
				}
			}
		}
		return false;
	}
	
	public static ItemStack getGun() {
	ItemStack gun = new ItemBuilder(Material.IRON_BARDING).displayname("§6⚔ §aArma do End §6⚔").lore(Arrays.asList(new String[] {
			" ",
			"§6§lCLIQUE COM O DIREITO!", 
			"§c§l╰ §eClique para atirar pérolas que quebram blocos."})).build();
	NBTItemStack nbtItemStack = new NBTItemStack(gun);
	nbtItemStack.setString("StoneEnderGun", "Gun12");
	return nbtItemStack.getItem();
	
	}
	
}
