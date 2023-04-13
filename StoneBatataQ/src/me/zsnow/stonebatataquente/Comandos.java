package me.zsnow.stonebatataquente;

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
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stonebatataquente.api.LocationAPI;
import me.zsnow.stonebatataquente.api.SimpleclansAPI;
import me.zsnow.stonebatataquente.api.LocationAPI.location;
import me.zsnow.stonebatataquente.api.StringReplaceAPI;
import me.zsnow.stonebatataquente.configs.Configs;
import me.zsnow.stonebatataquente.manager.BatataController;
import me.zsnow.stonebatataquente.manager.EventController;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;

public class Comandos implements CommandExecutor {

	int taskID;
	final String NO_PERM_MOD = "§cVocê precisa do cargo Moderador ou superior para executar este comando.";
    final String NO_PERM_ADMIN = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	final private int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("batata")) {
			if (args.length < 1) {
				sender.sendMessage(" §3§lBATATA-QUENTE");
				sender.sendMessage(" ");
				sender.sendMessage(" §f/batata entrar");
				sender.sendMessage(" §f/batata sair");
				sender.sendMessage(" §f/batata info");
				if (sender.hasPermission("batata.admin")) {
					sender.sendMessage(" §c/batata iniciar");
					sender.sendMessage(" §c/batata parar");
					sender.sendMessage(" §c/batata moderar");
				}
				sender.sendMessage(" ");
			}
			
			EventController evento = EventController.getInstance();
			BatataController batata = BatataController.getInstance();
			if (args.length >= 1) { 
				if (args[0].equalsIgnoreCase("entrar")) {
					Player p = (Player) sender;
					if (p.hasPermission("batata.entrar")) {
					if (Configs.locations.getConfig().contains("Entrada".toUpperCase())) {
					if (evento.getEventoStatus() == true && evento.getEntradaStatus() == true) {
						if (!batata.getParticipantes().contains(p)) {
							if (batata.getParticipantes().size() < maxPlayer) {
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
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
								LocationAPI.getLocation().teleportTo(p, location.ENTRADA);
								batata.getParticipantes().add(p);
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.entrou-no-evento")));
								p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("Som.ao-entrar").toUpperCase()), 1.0F, 0.5F);
								enableClanDamage(p);
								return true;
							} else {
								p.sendMessage("§cO evento já está em sua capacidade máxima de jogadores. ("+ batata.getParticipantes().size() + "/"+maxPlayer+")");
								return true;
							}
						} else {
							p.sendMessage("§cVocê já está no evento.");
							return true;
						}
					} else {
						p.sendMessage("§cO evento não está ocorrendo ou sua entrada já foi fechada.");
						return true;
					}
				} else {
					p.sendMessage("§cA entrada do evento ainda não foi definida por um ADMINISTRADOR.");
					return true;
				}
				}else {
							p.sendMessage("§cVocê precisa do rank [MercurioII] para entrar no evento.");
							return true;
						}
			}
				if (args[0].equalsIgnoreCase("sair")) {
					Player p = (Player) sender;
					if (batata.getParticipantes().contains(p)) {
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						batata.getParticipantes().remove(p);
						disableClanDamage(p);
						for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
							p.removePotionEffect(AllPotionEffects.getType());
						}
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						if (batata.getBatataMan() != null && batata.getBatataMan().getName().equals(p.getName())) {
							for (Player participantes : batata.getParticipantes()) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.batata-saiu")));
							}
							batata.canStopEvent();
							batata.setBatataMan(null);
						}
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("Mensagens.saiu-do-evento")));
						p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("Som.ao-sair").toUpperCase()), 1.0F, 0.5F);
						batata.canStopEvent();
					} else {
						p.sendMessage("§cVocê não está participando do evento.");
						return true;
					}
					return true;
				}
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (sender.hasPermission("batata.admin")) {
						if (evento.getEntradaStatus() == false && evento.getEventoStatus() == false) {
							batata.resetData();
							evento.setEntradaStatus(true);
							evento.setEventoStatus(true);
							evento.setTempoEntrada(evento.getTempoEntrada() + 10);
							
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								@Override
								public void run() {
									if (evento.getEntradaStatus() == true && evento.getEventoStatus() == true) {
										if (evento.getTempoEntrada() > 10) {
											evento.setTempoEntrada(evento.getTempoEntrada() - 10);
											for (String msg : Configs.config.getStringList("broadcast.Entrada")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
											}
										} else {
											if (batata.getParticipantes().size() >= Configs.config.getConfig().getInt("minimo-de-participantes")) {
												for (String msg : Configs.config.getStringList("broadcast.Entrada-fechada")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
												}
												batata.startWarningTask();
												evento.setEntradaStatus(false);
												sh.cancelTask(taskID);
											} else {
												for (String msg : Configs.config.getStringList("broadcast.Sem-jogadores")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
												}
												sh.cancelTask(taskID);
												batata.resetData();
											}
										}
									} else {
										sh.cancelTask(taskID);
									}
									
								}
							}, 0L, 200L);
							
						} else {
							sender.sendMessage("§cO evento já está ocorrendo.");
							return true;
						}
					} else {
						sender.sendMessage("§cVocê não tem permissão para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("parar")) {
					if (sender.hasPermission("batata.admin")) {
						if (evento.getEventoStatus() == true) {
							evento.setEntradaStatus(false);
							evento.setEventoStatus(false);
							for (Player participantes : batata.getParticipantes()) {
								 participantes.getInventory().setArmorContents(null);
								 participantes.getInventory().clear();
								LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
								 SimpleclansAPI.getAPI().disableClanDamage(participantes);
								 for (PotionEffect allPotionEffects : participantes.getActivePotionEffects()) {
									 participantes.removePotionEffect(allPotionEffects.getType());
								 }
							}
							batata.getParticipantes().clear();
							batata.resetData();
							for (String msg : Configs.config.getConfig().getStringList("broadcast.admin-finalizou")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
								}
							} else {
								sender.sendMessage("§cO evento não está ocorrendo.");
								return true;
							}
						} else {
							sender.sendMessage(NO_PERM_ADMIN);
							return true;
						}
					}
				if (args[0].equalsIgnoreCase("set")) {
					Player p = (Player) sender;
					if (sender.hasPermission("zs.admin")) {
						if (args.length < 2) {
							sender.sendMessage("§cUse: /batata set <Entrada, Saida, Moderar>");
							return true;
						}
						if (args.length >= 2) {
							if (args[1].equalsIgnoreCase("entrada")) {
								LocationAPI.getLocation().setLocation(p, location.ENTRADA);
								p.sendMessage("§eVocê definiu a área de entrada do evento.");
								return true;
							}
							if (args[1].equalsIgnoreCase("saida")) {
								LocationAPI.getLocation().setLocation(p, location.SAIDA);
								p.sendMessage("§eVocê definiu a área de saída do evento.");
								return true;
							}
							if (args[1].equalsIgnoreCase("moderar")) {
								LocationAPI.getLocation().setLocation(p, location.MODERAR);
								p.sendMessage("§eVocê definiu a área de moderação do evento.");
								return true;
							}
						}
					}
				}
				if (args[0].equalsIgnoreCase("Moderar")) {
					Player p = (Player) sender;
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eEnviado para a área de moderação do evento.");
						LocationAPI.getLocation().teleportTo(p, location.MODERAR);
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0f, 0.5f);
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("Modsair")) {
					Player p = (Player) sender;
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eEnviado para a saída do evento.");
						LocationAPI.getLocation().teleportTo(p, location.SAIDA);
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0f, 0.5f);
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					String status = evento.getEventoStatus() ? "§eEm andamento." : "§cFechado.";
					String entrada = evento.getEntradaStatus() ? "§aAberta." : "§cFechada.";
					for (String msg : Configs.config.getConfig().getStringList("broadcast.evento-info")) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)
							.replace("{status}", status)
								.replace("{entrada}", entrada)));
					}
				}
				if (args[0].equalsIgnoreCase("verlist")) {
					if (sender.hasPermission("zs.admin")) {
						String batataMan = batata.getBatataMan() == null ? "§cNinguém" : batata.getBatataMan().getName();
						StringBuilder sb = new StringBuilder();
						String separe = "";
						sender.sendMessage("");
						sender.sendMessage("§c[Batata-quente] jogadores:");
						sender.sendMessage("§eBatata com: " + batataMan);
						for (Player jogadores : batata.getParticipantes()) {
							sb.append(separe);
							separe = ", ";
							sb.append(jogadores.getName());
						}
						if (batata.getParticipantes().size() == 0) {
							sender.sendMessage(" §eVazio...");
							sender.sendMessage("");
						} else {
							sender.sendMessage("§7[" + sb.toString() + "§7]");
							sender.sendMessage("");
						}
					} else {
						sender.sendMessage(NO_PERM_ADMIN);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("tpbatata")) {
					Player p = (Player) sender;
					if (p.hasPermission("zs.mod")) {
						if (batata.getBatataMan() != null) {
							p.teleport(batata.getBatataMan());
							p.sendMessage("§eVocê foi enviado até o batata-man.");
						}
					}
				}
			}
		}
		return false;
	}
	
	public static SimpleClans getSC() {
		return SimpleClans.getInstance();
	}
	  
	public static void enableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(true); 
	}
	  
	public static void disableClanDamage(Player p) {
		if (getSC().getClanManager().getClanPlayer(p) != null)
			getSC().getClanManager().getClanPlayer(p).setFriendlyFire(false); 
	}
	
}
