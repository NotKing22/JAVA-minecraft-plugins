package me.zsnow.stone.fight;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.stone.fight.config.Configs;
import net.md_5.bungee.api.ChatColor;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;


public class Commands implements CommandExecutor, Listener {

	String NO_PERM_GERENTE = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	String NO_PERM_ADMIN = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	String NO_PERM_MOD = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	
	String seta = "»";
	String setaMaior = "➣";
	final private int maxPlayer = Configs.config.getConfig().getInt("maximo-de-jogadores");
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fight")) {
			if (!(sender instanceof Player)) return false;
		}
		Player p = (Player) sender;
			if (args.length == 0) {
				p.sendMessage("");
				p.sendMessage(" §a§lSTONE FIGHT §7- (Comandos)");
				p.sendMessage("");
				p.sendMessage("§7" + seta + " §a/fight participar");
				p.sendMessage("§7" + seta + " §a/fight sair");
				//p.sendMessage("§7" + seta + " §a/fight camarote");
				p.sendMessage("§7" + seta + " §a/fight status");
				if (p.hasPermission("zs.mod")) {
					p.sendMessage("§c" + seta + " /fight moderar");
					p.sendMessage("§c" + seta + " /fight modsair");
				}
				if (p.hasPermission("zs.admin")) {
					p.sendMessage("§c" + seta + " /fight forcestart");
					p.sendMessage("§c" + seta + " /fight forcestop");
					p.sendMessage("§c" + seta + " /fight set <Entrada/Saida/Pos1,2/Camarote>");
					p.sendMessage("§c" + seta + " /fight salvaritem");
					p.sendMessage("§c" + seta + " /fight reload");
				}
				p.sendMessage("");
				return true;
			}
			if (args.length == 1) {
				if ((args[0].equalsIgnoreCase("fstart") || (args[0].equalsIgnoreCase("forcestart")))) {
					if (p.hasPermission("zs.admin")) {
						if ((FightManager.manager.getOcorrendoStatus() == false) && 
								(FightManager.manager.getEntradaLiberadaStatus() == false)) {
									
							FightReset();
									
								(new BukkitRunnable() {
									
									@Override
									public void run() {
										if (FightManager.manager.getOcorrendoStatus() == true) {
											if (FightManager.manager.getTempo() > 0) {
												 for (String msg : Configs.config.getConfig().getStringList("broadcast.start")) {
													  Bukkit.broadcastMessage((ChatColor.translateAlternateColorCodes('&', msg
														.replace("{tempo}", String.valueOf(FightManager.manager.getTempo()))
														.replace("{numero_de_participantes}", String.valueOf(FightManager.manager.getParticipantes().size()))
														.replace("{max_players}", String.valueOf(maxPlayer)))));
												  }
												FightManager.manager.setTempo(FightManager.manager.getTempo() - 10);
												return;
											} else {
												if (FightManager.manager.getParticipantes().size() < Configs.config.getConfig().getInt("minimo-de-player")) {
													for (String msg : Configs.config.getConfig().getStringList("broadcast.sem-player")) {
														Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{minimo_para_iniciar}", String.valueOf(Configs.config.getConfig().getInt("minimo-de-player")))));
														}
													FightStop();
													cancel();
													return;
												}
												for (String msg : Configs.config.getConfig().getStringList("broadcast.enter-closed")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
														.replace("{numero_de_participantes}", String.valueOf(FightManager.manager.getParticipantes().size()))
														.replace("{max_players}", String.valueOf(maxPlayer))));
													}
												FightManager.manager.setEntradaLiberadaStatus(false);
												PrepararDuelo();
												cancel();
											}
									} else {
										FightStop();
										cancel();
									}
								}
							}).runTaskTimer(Main.getInstance(), 0L, 200L);
						} else {
							p.sendMessage("§cO evento já está aberto.");
							return true;
						}
						
					} else {
						p.sendMessage(NO_PERM_ADMIN);
						return true;
					}
				}
				if ((args[0].equalsIgnoreCase("forcestop") || (args[0].equalsIgnoreCase("parar")))) {
					if (p.hasPermission("zs.admin")) {
						if (FightManager.manager.getOcorrendoStatus() == true) {
							FightManager.manager.setOcorrendoStatus(false);
							for (Player jogadores : FightManager.manager.getParticipantes()) {
								sendTo(jogadores, "Saida");
								retirarItens(jogadores);
								disableClanDamage(p);
							}
							FightManager.manager.getParticipantes().clear();
							FightManager.manager.setOponenteX(null);
							FightManager.manager.setOponenteY(null);
							for (String msg : Configs.config.getConfig().getStringList("broadcast.force-stop")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
							}
							Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
							FightStop();
					} else {
						p.sendMessage("§cO evento fight já está fechado.");
						return true;
					}
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
					
				}
				if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a área de moderação do Fight.");
						sendTo(p, "Entrada");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("reload")) {
					if (p.hasPermission("zs.gerente")) {
						Configs.config.reloadConfig();
						Configs.locations.reloadConfig();
						Configs.itens.reloadConfig();
						p.sendMessage("§cConfigurações recarregadas...");
					} else {
						p.sendMessage(NO_PERM_GERENTE);
						return true;
					}
				}
				
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						sendTo(p, "Saida");
						p.sendMessage("§eVocê foi enviado para a saída do Fight.");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("set")) {
				if (p.hasPermission("zs.admin")) {
					p.sendMessage("§c" + seta + " §e/fight set <Entrada/Saida/Pos1,2/Camarote>");
					return true;
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("salvaritem")) {
				if (p.hasPermission("zs.gerente")) {
		              int loop;
		              for (loop = 0; loop <= 35; loop++)
		               Configs.itens.set("Itens.Slot." + loop, p.getInventory().getItem(loop)); 
		              for (loop = 36; loop <= 39; loop++)
		                Configs.itens.set("Armadura.Slot." + loop, p.getInventory().getItem(loop)); 
		              p.sendMessage("§eOs item do duelo no Fight foram definidos com sucesso!");
		              p.getInventory().clear();
		              p.getInventory().setArmorContents(null);
		              Configs.itens.saveConfig();
		              return true;
				} else {
					p.sendMessage(NO_PERM_GERENTE);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("nextfight")) {
				if (p.hasPermission("zs.admin")) {
					if (FightManager.manager.getOcorrendoStatus() == true && 
							FightManager.manager.getEntradaLiberadaStatus() == false && 
								FightManager.manager.getParticipantes().size() > 1) {
									Player p1 = FightManager.manager.getOponenteX();
									Player p2 = FightManager.manager.getOponenteY();
										FightManager.manager.setOponenteX(null);
										FightManager.manager.setOponenteY(null);
							p1.setHealth(0);
							p2.setHealth(0);
										FightManager.manager.getParticipantes().remove(p1);
										FightManager.manager.getParticipantes().remove(p2);
								sendTo(p1, "Saida");
								sendTo(p2, "Saida");
								PrepararDuelo();
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("force-skip-duel")));
						return true;
					} else {
						p.sendMessage("§cO evento fight não está ocorrendo ou sua entrada ainda não foi fechada.");
						return true;
					}
				}
			}
				if (args[0].equalsIgnoreCase("entrar") || args[0].equalsIgnoreCase("participar")) {
					if (Configs.locations.getConfig().contains("Entrada".toUpperCase())) {
						if ((FightManager.manager.getOcorrendoStatus() == true) && (FightManager.manager.getEntradaLiberadaStatus() == true)) {
							if (FightManager.manager.getParticipantes().size() < maxPlayer) {
								if (!FightManager.manager.getParticipantes().contains(p)) {
									if (p.hasPermission("sumo.entrar")) { // sumo pra n ter q atualizar as perm tudo
										PlayerInventory inv = p.getInventory();
										for (ItemStack i : inv.getContents()) {
											if(i != null && !(i.getType() == Material.AIR)) {
												p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("inv-cheio")));
												return true;
											} 
										} for (ItemStack i : inv.getArmorContents()) {
												if(i != null && !(i.getType() == Material.AIR)) {
													p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("contem-armadura")));
													return true;
												}
											}
										for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
											p.removePotionEffect(AllPotionEffects.getType());
										}
										sendTo(p, "Entrada");
										FightManager.manager.getParticipantes().add(p);
										enableClanDamage(p);
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("entrou-no-evento")));
										p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("som-ao-entrar").toUpperCase()), 1.0F, 0.5F);
										return true;
									} else {
										p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("sem-rank-para-entrar")));
										p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("sem-rank-som").toUpperCase()), 1.0F, 0.5F);
										return true;
									}
								} else {
									p.sendMessage("§cVocê já está no evento.");
									return true;
								}
							} else {
								p.sendMessage("§cO evento já está com o limite de jogadores.");
								return true;
							}
						} else {
							p.sendMessage("§cO evento fight não está aberto ou sua entrada foi fechada.");
							return true;
						}
					} else {
						p.sendMessage("§cA entrada do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					} 
				}
				if (args[0].equalsIgnoreCase("Sair")) {
					if (Configs.locations.getConfig().contains("Saida".toUpperCase())) {
						if (FightManager.manager.getParticipantes().contains(p)) {
							FightManager.manager.getParticipantes().remove(p);
							for (Player jogadores : Bukkit.getOnlinePlayers()) {
								if (!(jogadores.hasPermission("zs.mod") || jogadores.hasPermission("zs.admin"))) {
									jogadores.showPlayer(p);
								}
							}
							sendTo(p, "Saida");
							disableClanDamage(p);
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("saiu-fight")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("nao-ta-no-evento")));
							return true;
						}
					} else {
						p.sendMessage("§cA saída do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("status")) {
					if (FightManager.manager.getOcorrendoStatus() == true) {
						for (String msg : Configs.config.getConfig().getStringList("mostrar-infos")) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{lutas_restantes}", String.valueOf((Integer.valueOf(FightManager.manager.getParticipantes().size()/2))))
									.replace("{numero_de_participantes}", String.valueOf(FightManager.manager.getParticipantes().size()))
									.replace("{max_players}", String.valueOf(maxPlayer))));
						}
						return true;
					} else {
						p.sendMessage("§cO evento não está ocorrendo para obter a informação.");
						return true;
					}
				}
		}
			if (args.length == 2) {
				if (p.hasPermission("zs.gerente")) {
					if (args[0].equalsIgnoreCase("set")) {
						if (args[1].equalsIgnoreCase("entrada")) {
								setLoc(p, "Entrada");
								p.sendMessage("§eA entrada do evento Fight foi definida com sucesso.");
								return true;
							} 
						if (args[1].equalsIgnoreCase("saida")) {
								setLoc(p, "Saida");
								p.sendMessage("§eA saída do evento Fight foi definida com sucesso.");
								return true;
							}
						if (args[1].equalsIgnoreCase("pos1")) {
								setLoc(p, "Pos1");
								p.sendMessage("§eA Position 1 do Fight foi definida com sucesso.");
								return true;
							} 
						if (args[1].equalsIgnoreCase("pos2")) {
								setLoc(p, "Pos2");
								p.sendMessage("§eA Position 2 do Fight foi definida com sucesso.");
								return true;
							} else {
								p.sendMessage("§cVocê inseriu um argumento inválido, digite /fight para ver os comandos.");
								return true;
							}
						} else {
							p.sendMessage("§cVocê inseriu um argumento inválido, digite /fight para ver os comandos.");
							return true;
						}
					
				} else {
					p.sendMessage(NO_PERM_GERENTE);
					return true;
				}
			}
			return false;
	}
	
	private void PrepararDuelo() {
		if (FightManager.manager.getEntradaLiberadaStatus() == false) {
			if (FightManager.manager.getOponenteX() == null && FightManager.manager.getOponenteY() == null) {
		if ((!FightManager.manager.getParticipantes().isEmpty())) {
			if (FightManager.manager.getParticipantes().size() != 1) {
		FightManager.manager.setOponenteX(null);
		FightManager.manager.setOponenteY(null);
		Random random = new Random();
		FightManager.manager.setOponenteX(FightManager.manager.getParticipantes().get(random.nextInt(FightManager.manager.getParticipantes().size())));
		FightManager.manager.setOponenteY(FightManager.manager.getParticipantes().get(random.nextInt(FightManager.manager.getParticipantes().size())));
		
		if (FightManager.manager.getOponenteX() == FightManager.manager.getOponenteY() && FightManager.manager.getParticipantes().size() > 1) {
			
			while (FightManager.manager.getOponenteX() == FightManager.manager.getOponenteY()) { 
				FightManager.manager.setOponenteY(FightManager.manager.getParticipantes().get(random.nextInt(FightManager.manager.getParticipantes().size())));
			}
		}
		
		for (String msg : Configs.config.getConfig().getStringList("duelos-mensagem")) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
					.replace("{oponenteX}", FightManager.manager.getOponenteX().getName())
					.replace("{oponenteY}", FightManager.manager.getOponenteY().getName()).replace("{tempo_entre_duelos}", String.valueOf(Configs.config.getConfig().getInt("tempo-para-iniciar-duelo")))));
		}
			(new BukkitRunnable() {
				
				int tempo = Configs.config.getConfig().getInt("tempo-para-iniciar-duelo");
				
				@Override
				public void run() {
					if (tempo > 0) {
						tempo--;
						return;
					}
					if (tempo == 0) {
					if ((FightManager.manager.getParticipantes().contains(FightManager.manager.getOponenteX())) 
							&& (FightManager.manager.getParticipantes().contains(FightManager.manager.getOponenteY()))) {
								FightManager.manager.getOponenteX().setFoodLevel(20);
								FightManager.manager.getOponenteY().setFoodLevel(20);
								sendToArena(); 
								deathmatch();
						
							enableClanDamage(FightManager.manager.getOponenteX());
							enableClanDamage(FightManager.manager.getOponenteY());
							cancel();
					} else {
						for (Player participantes : FightManager.manager.getParticipantes()) {
							participantes.sendMessage("");
							participantes.sendMessage("§6[Fight] &cUm dos jogadores desconectou antes de começar o duelo.");
							participantes.sendMessage("");
						}
						cancel();
						PrepararDuelo(); // Sorteia de novo
						}
					}
				}
			}).runTaskTimer(Main.getInstance(), 0L, 20L);
			
		} else {
			final Player vencedor = FightManager.manager.getParticipantes().get(0);
			for (String msg : Configs.config.getConfig().getStringList("broadcast.vencedor")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg
						.replace("{vencedor}", vencedor.getName())));
			}
			for (String comandos : Configs.config.getConfig().getStringList("recompensas")) {
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), comandos.replace("{vencedor}", vencedor.getName()));
			}
			for (String mensagemRecompensas : Configs.config.getConfig().getStringList("mensagem-ao-vencer")) {
				vencedor.sendMessage(ChatColor.translateAlternateColorCodes('&', mensagemRecompensas));
			}
			//darTagfight(vencedor);
			Bukkit.getServer().getConsoleSender().sendMessage("§aRecompensa entregue ao vencedor do fight.");
			retirarItens(vencedor);
			disableClanDamage(vencedor);
			Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
			FightStop();
			sendTo(vencedor, "Saida");
			}
		} else {
			if (FightManager.manager.getEntradaLiberadaStatus() == false) {
				FightManager.manager.setOcorrendoStatus(false);
				for (String msg : Configs.config.getConfig().getStringList("broadcast.ambos-quitaram")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
				}
				Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
				FightStop();
			}
		}
	} 
}
}
	
	
	private void sendToArena() {
		(new BukkitRunnable() {
			
			@Override
			public void run() {
				if ((FightManager.manager.getParticipantes().contains(FightManager.manager.getOponenteX())) && (FightManager.manager.getParticipantes().contains(FightManager.manager.getOponenteY()))) {
					removeCheating(FightManager.manager.getOponenteX());
					removeCheating(FightManager.manager.getOponenteY());
					equipaPlayer(FightManager.manager.getOponenteX());
					equipaPlayer(FightManager.manager.getOponenteY());
						for (PotionEffect AllPotionEffects : FightManager.manager.getOponenteX().getActivePotionEffects()) {
							FightManager.manager.getOponenteX().removePotionEffect(AllPotionEffects.getType());
						}
						for (PotionEffect AllPotionEffects : FightManager.manager.getOponenteY().getActivePotionEffects()) {
							FightManager.manager.getOponenteY().removePotionEffect(AllPotionEffects.getType());
						}
						sendTo(FightManager.manager.getOponenteX(), "Pos1");
						sendTo(FightManager.manager.getOponenteY(), "Pos2");
					cancel();
				}
				
			}
		}).runTaskTimer(Main.getInstance(), 0, 120L);
	}
	
	private void removeCheating(Player p) {
		p.setHealth(20.0D);
	    p.setFoodLevel(20);
	    p.getActivePotionEffects().clear();
	    p.getInventory().clear();
	//    p.getInventory().setArmorContents(null);
	//    p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
	//    p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
	//    p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	    p.setAllowFlight(false);
	}
	
	private void retirarItens(Player p) {
		p.setHealth(20.0D);
	    p.setFoodLevel(20);
	    p.getActivePotionEffects().clear();
	    p.getInventory().clear();
	    p.getInventory().setArmorContents(null);
	    p.setAllowFlight(false);
	}
	
	private void sendTo(Player p, String location) {
		location = location.toUpperCase();
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);;
		p.teleport(loc);
	}
	
	
	public static void setLoc(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Configs.locations.getConfig().set(LocationName + ".X", Double.valueOf(x));
		Configs.locations.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Configs.locations.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Configs.locations.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Configs.locations.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Configs.locations.getConfig().set(LocationName + ".Mundo", world);
		Configs.locations.saveConfig();
	}
	
	public void FightReset() {
		FightManager.manager.setOcorrendoStatus(true);
		FightManager.manager.setEntradaLiberadaStatus(true);
		FightManager.manager.getParticipantes().clear();
		FightManager.manager.setOponenteX(null);
		FightManager.manager.setOponenteY(null);
		//fightManager.manager.setTempo(130);
		FightManager.manager.setTempo(Configs.config.getConfig().getInt("tempo-para-entrar"));
	}
	
	public void FightStop() {
		FightManager.manager.setOcorrendoStatus(false);
		FightManager.manager.setEntradaLiberadaStatus(false);
		for (Player participantes : FightManager.manager.getParticipantes()) {
			sendTo(participantes, "SAIDA");
		}
		FightManager.manager.getParticipantes().clear();
		FightManager.manager.setOponenteX(null);
		FightManager.manager.setOponenteY(null);
		FightManager.manager.setTempo(-1);
		Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
	}
	
		//////////////////////////
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (FightManager.manager.getParticipantes().contains(p)) {				
			if (FightManager.manager.getOponenteX() != null && FightManager.manager.getOponenteX().equals(p)) {
					for (String msg : Configs.config.getConfig().getStringList("broadcast.quitar-durante-duelo")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
					}
						sendTo(FightManager.manager.getOponenteY(), "Entrada");
						FightManager.manager.getParticipantes().remove(p);
						sendTo(FightManager.manager.getOponenteX(), "Saida");
							FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteX());
								p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
								retirarItens(FightManager.manager.getOponenteX());
								retirarItens(FightManager.manager.getOponenteY());
										sendTo(p, "Saida");
										disableClanDamage(p);
				FightManager.manager.setOponenteX(null);
				FightManager.manager.setOponenteY(null);
					PrepararDuelo();
			}
			if (FightManager.manager.getOponenteY() != null && FightManager.manager.getOponenteY().equals(p)) {
					for (String msg : Configs.config.getConfig().getStringList("broadcast.quitar-durante-duelo")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
					}
						sendTo(FightManager.manager.getOponenteX(), "Entrada");
						FightManager.manager.getParticipantes().remove(p);
						sendTo(FightManager.manager.getOponenteY(), "Saida");
							FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteY());
							p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(FightManager.manager.getOponenteX());
					retirarItens(FightManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
				FightManager.manager.setOponenteX(null);
				FightManager.manager.setOponenteY(null);
					PrepararDuelo();
			} else {
				FightManager.manager.getParticipantes().remove(p);
				disableClanDamage(p);
				sendTo(p, "Saida");
			}
		}
	}
	
	@EventHandler
	public void onRip(PlayerDeathEvent e) {
		Player p = e.getEntity();
			if (FightManager.manager.getParticipantes().contains(p)) {
				if (FightManager.manager.getOponenteX() != null && FightManager.manager.getOponenteX().equals(p)) {
					for (String msg : Configs.config.getConfig().getStringList("broadcast.oponenteY-venceu")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{oponenteX}", FightManager.manager.getOponenteX().getName())
								.replace("{oponenteY}", FightManager.manager.getOponenteY().getName())));
					}
					sendTo(FightManager.manager.getOponenteY(), "Entrada");
					FightManager.manager.getParticipantes().remove(p);
					sendTo(FightManager.manager.getOponenteX(), "Saida");
					FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteX());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(FightManager.manager.getOponenteX());
					retirarItens(FightManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
					FightManager.manager.setOponenteX(null);
					FightManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getInstance(), 120L, 0);
				}
				if (FightManager.manager.getOponenteY() != null && FightManager.manager.getOponenteY().equals(p)) {
					for (String msg : Configs.config.getConfig().getStringList("broadcast.oponenteX-venceu")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{oponenteX}", FightManager.manager.getOponenteX().getName())
								.replace("{oponenteY}", FightManager.manager.getOponenteY().getName())));
					}
					sendTo(FightManager.manager.getOponenteX(), "Entrada");
					FightManager.manager.getParticipantes().remove(p);
					sendTo(FightManager.manager.getOponenteY(), "Saida");
					FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteY());
					p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
					retirarItens(FightManager.manager.getOponenteX());
					retirarItens(FightManager.manager.getOponenteY());
					sendTo(p, "Saida");
					disableClanDamage(p);
					FightManager.manager.setOponenteX(null);
					FightManager.manager.setOponenteY(null);
					(new BukkitRunnable() {
						
						@Override
						public void run() {
							PrepararDuelo();
							cancel();
						}
					}).runTaskTimer(Main.getInstance(), 120L, 0);
				} //else {
				  //	sendTo(p, "Entrada");
				  //}
			}
		}
	  
	  @EventHandler
	  public void blockCMD(PlayerCommandPreprocessEvent e) {
		  Player p = e.getPlayer();
		  if (FightManager.manager.getOponenteX() != null && FightManager.manager.getOponenteY() != null) {
		  if ((FightManager.manager.getOponenteX().getName().equals(p.getName()) || 
				  FightManager.manager.getOponenteY().getName().equals(p.getName())) && e.getMessage().startsWith("/fight")) {
			  p.sendMessage("§cVocê não pode digitar este comando enquanto está duelando.");
			  e.setCancelled(true);
		  }
	  }
  }
	
	  public void deathmatch() {
		
		(new BukkitRunnable() {
			
			int tempo = Configs.config.getConfig().getInt("death-match-tempo");
			
			@Override
			public void run() {
				if (FightManager.manager.getOponenteX() != null && FightManager.manager.getOponenteY() != null) {
					ActionBarAPI.sendActionBarMessage(FightManager.manager.getOponenteX(), "§a§l[FIGHT] §aDeathMatch em: §c" + tempo + " §csegundos");
					ActionBarAPI.sendActionBarMessage(FightManager.manager.getOponenteY(), "§a§l[FIGHT] §aDeathMatch em: §c" + tempo + " §csegundos");
					tempo--;
					//if (tempo == 25) {
					//	FightManager.manager.getOponenteX().getInventory().addItem(giveKnockBackStick());
					//	FightManager.manager.getOponenteY().getInventory().addItem(giveKnockBackStick());
					//	return;
					//}
					if (tempo == 0) {
						FightManager.manager.getOponenteX().getInventory().clear();
							FightManager.manager.getOponenteY().getInventory().clear();
						FightManager.manager.getOponenteX().getInventory().setArmorContents(null);
							FightManager.manager.getOponenteY().getInventory().setArmorContents(null);
							
							FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteX());
								FightManager.manager.getParticipantes().remove(FightManager.manager.getOponenteY());
						FightManager.manager.getOponenteX().getWorld().spawnEntity(FightManager.manager.getOponenteX()
								.getLocation(), EntityType.FIREWORK);
						FightManager.manager.getOponenteY().getWorld().spawnEntity(FightManager.manager.getOponenteY()
								.getLocation(), EntityType.FIREWORK);
						for (String msg : Configs.config.getConfig().getStringList("broadcast.deathmatch-morte")) {
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg.replace("{oponenteX}", FightManager.manager.getOponenteX().getName())
									.replace("{oponenteY}", FightManager.manager.getOponenteY().getName())));
						}
							Player opX = FightManager.manager.getOponenteX();
							Player opY = FightManager.manager.getOponenteY();
								FightManager.manager.setOponenteX(null);
								FightManager.manager.setOponenteY(null);
							opX.setHealth(0); 
							opY.setHealth(0);
						PrepararDuelo();
						cancel();
					}
				} else {
					cancel();
				}
		

			//	PrepararDuelo();
			//	cancel();
				
			}
		}).runTaskTimer(Main.getInstance(), 0, 20L);
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
		
		public ItemStack giveKnockBackStick() {
			ItemStack item = new ItemStack(Material.BLAZE_ROD);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§cKnockback - DEATH MATCH");
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
			return item;
		}
		
/*		 @EventHandler
		 public void onTeleport(PlayerTeleportEvent e) {
	    	Player p = e.getPlayer();
			if (fightManager.manager.getParticipantes().contains(p) && !p.hasPermission("zs.admin")) {
	    		p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
	    		e.setCancelled(true);
	    		if (fightManager.manager.getOcorrendoStatus() == false) {
	    			sendTo(p, "Saida");
	    			fightManager.manager.getParticipantes().remove(p);
	    		}
	    	}
	    }	*/
		
		 @EventHandler
		 public void worldChange(PlayerChangedWorldEvent e) {
			final Player p = e.getPlayer();
			if (FightManager.manager.getOcorrendoStatus() == true && FightManager.manager.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
				sendTo(p, "Entrada");
				p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
			}
		 }
		 
		  private void equipaPlayer(Player p) {
			    int loop;
			    for (loop = 0; loop <= 35; loop++) {
			      p.getInventory().setItem(loop, Configs.itens.getItemStack("Itens.Slot." + loop)); 
			      p.updateInventory();
			    }
			    for (loop = 36; loop <= 39; loop++) {
			      p.getInventory().setItem(loop, Configs.itens.getItemStack("Armadura.Slot." + loop));
			      p.updateInventory();
			    }
			//    p.updateInventory();
			  }


		/*
		private void darTagfight(Player p) {
			Main.getInstance().getConfig().getString("vencedor").replace(Main.getInstance().getConfig().getString("vencedor"), "");
			Main.getInstance().getConfig().set("vencedor", p.getName());
			Main.getInstance().saveConfig();
		}
		
		@EventHandler
		private void onChat(ChatMessageEvent e) {
			Player p = (Player) e.getSender();
		    if(e.getTags().contains("fighttag") && Main.getInstance().getConfig().getString("vencedor") != null 
		    		&& Main.getInstance().getConfig().getString("vencedor").equals(p.getName())) 
		        e.setTagValue("fighttag","&6[fight] ");
		}
		*/
	
}
