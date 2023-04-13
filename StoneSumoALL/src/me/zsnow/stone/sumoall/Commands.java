package me.zsnow.stone.sumoall;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;


public class Commands implements CommandExecutor, Listener {

	String NO_PERM_ADMIN = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	String NO_PERM_MOD = "§cVocê precisa do cargo Administrador ou superior para executar este comando.";
	double premio = Main.getPlugin().getConfig().getDouble("premio");
	String premio_message = Main.getPlugin().getConfig().getString("premio-message");
	ArrayList<Player> verDM = new ArrayList<>();  
	
	
	
	// ADD NO ONDEATH SAIR DO EVENTO
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("sumoall")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage("§a§lSUMO ALL §f- §aComandos");
					sender.sendMessage("");
					sender.sendMessage(" §f/sumoall iniciar");
					sender.sendMessage(" §f/sumoall parar");
					sender.sendMessage("");
					return true;
				}
				if (args.length == 1) {
					if ((args[0].equalsIgnoreCase("iniciar") || (args[0].equalsIgnoreCase("forcestart")))) {
						if (sender.hasPermission("zs.admin")) {
							SumoManager sumo = SumoManager.manager;
							if ((sumo.getOcorrendoStatus() == false) && 
									(sumo.getEntradaLiberadaStatus() == false)) {
										
								SumoReset();
										
									(new BukkitRunnable() {
										
										@Override
										public void run() {
											if (sumo.getOcorrendoStatus() == true) {
												if (sumo.getTempo() > 1) {
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lENTRADA ABERTA");
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§aPara participar digite: §f/sumoall entrar");
													Bukkit.broadcastMessage("§aParticipantes: §f" + sumo.getParticipantes().size());
													Bukkit.broadcastMessage("§aPrêmio: §f" + premio_message +" §fde coins");
													Bukkit.broadcastMessage("§aEntrada fechando em: §f" + sumo.getTempo() + " §fsegundos");
													Bukkit.broadcastMessage("§aCategoria: §fTodos contra todos.");
													Bukkit.broadcastMessage("");
													sumo.setTempo(sumo.getTempo() - 10);
												} else {
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lENTRADA FECHADA.");
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§aEvento iniciando...");
													Bukkit.broadcastMessage("§aParticipantes: §f" + sumo.getParticipantes().size());
													Bukkit.broadcastMessage("");
													sumo.setEntradaLiberadaStatus(false);
													sumo.setPvPStatus(true);
													deathmatch();
													cancel();
												}
											} else {
												SumoStop();
												cancel();
											}
										}
									}).runTaskTimer(Main.getPlugin(), 0L, 200L);
							} else {
								sender.sendMessage("§cO evento já está aberto.");
								return true;
							}
							
						} else {
							sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
							return true;
						}
					}
					if ((args[0].equalsIgnoreCase("parar") || (args[0].equalsIgnoreCase("forcefinalizar")))) {
						if (sender.hasPermission("zs.admin")) {
							if (SumoManager.manager.getOcorrendoStatus() == true) {
								SumoManager.manager.setOcorrendoStatus(false);
								for (Player jogadores : SumoManager.manager.getParticipantes()) {
									sendTo(jogadores, "Saida");
									retirarItens(jogadores);
									disableClanDamage(jogadores);
								}
								SumoManager.manager.getParticipantes().clear();
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lEVENTO FECHADO.");
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§cO evento foi forçado a finalizar por um ADMINISTRADOR.");
								Bukkit.broadcastMessage("");
								Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
								SumoStop();
						} else {
							sender.sendMessage("§cO evento sumo-all já está fechado.");
							return true;
						}
					} else {
						sender.sendMessage(NO_PERM_ADMIN);
						return true;
					}
					}
				}
			} else {
		Player p = (Player) sender;
			if (args.length == 0) {
				if (p.hasPermission("zs.admin")) {
					p.sendMessage("§a§lSUMO ALL §f- §aComandos");
					p.sendMessage("");
					p.sendMessage(" §f/sumoall entrar");
					p.sendMessage(" §f/sumoall sair");
					p.sendMessage(" §f/sumoall info");
					p.sendMessage(" §c/sumoall moderar §7- §cEntre no evento como staff.");
					p.sendMessage(" §c/sumoall modsair §7- §cSair do evento.");
					p.sendMessage(" §c/sumoall iniciar §7- §cForça o evento a iniciar");
					p.sendMessage(" §c/sumoall parar §7- §cForça o evento a finalizar");
					p.sendMessage(" §c/sumoall setentrada §7- §cDefine a entrada do evento");
					p.sendMessage(" §c/sumoall setsaida §7- §cDefine a saída do evento.");
					p.sendMessage("");
					return true;
				}
				if (p.hasPermission("zs.mod")) {
					p.sendMessage("§a§lSUMO ALL §f- §aComandos");
					p.sendMessage("");
					p.sendMessage(" §f/sumoall entrar");
					p.sendMessage(" §f/sumoall sair");
					p.sendMessage(" §f/sumoall info");
					p.sendMessage(" §c/sumoall moderar §7- §cEntre no evento como staff.");
					p.sendMessage(" §c/sumoall modsair §7- §cSair do evento.");
					p.sendMessage("");
					return true;
				}
				p.sendMessage("§a§lSUMO ALL §f- §aComandos");
				p.sendMessage("");
				p.sendMessage(" §f/sumoall entrar");
				p.sendMessage(" §f/sumoall sair");
				p.sendMessage(" §f/sumoall info");
				p.sendMessage("");
				return true;
			}
			if (args.length >= 1) {
				if ((args[0].equalsIgnoreCase("forcestart") || (args[0].equalsIgnoreCase("iniciar")))) {
					if (p.hasPermission("zs.admin")) {
						if ((SumoManager.manager.getOcorrendoStatus() == false) && 
								(SumoManager.manager.getEntradaLiberadaStatus() == false)) {
									
							SumoReset();

							(new BukkitRunnable() {
									
									@Override
									public void run() {
										if (SumoManager.manager.getOcorrendoStatus() == true) {
											if (SumoManager.manager.getTempo() > 1) {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lENTRADA ABERTA");
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§aPara participar digite: §f/sumoall entrar");
												Bukkit.broadcastMessage("§aParticipantes: §f" + SumoManager.manager.getParticipantes().size());
												Bukkit.broadcastMessage("§aPrêmio: §f"+ premio_message +" de coins");
												Bukkit.broadcastMessage("§aEntrada fechando em: §f" + SumoManager.manager.getTempo() + " §fsegundos");
												Bukkit.broadcastMessage("§aCategoria: §fTodos contra todos.");
												Bukkit.broadcastMessage("");
												SumoManager.manager.setTempo(SumoManager.manager.getTempo() - 10);
											} else {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lENTRADA FECHADA.");
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§aEvento iniciando...");
												Bukkit.broadcastMessage("§aParticipantes: §f" + SumoManager.manager.getParticipantes().size());
												Bukkit.broadcastMessage("");
												SumoManager.manager.setEntradaLiberadaStatus(false);
												SumoManager.manager.setPvPStatus(true);
												deathmatch();
												cancel();
											}
										} else {
											SumoStop();
											cancel();
										}
									}
								}).runTaskTimer(Main.getPlugin(), 0L, 200L);
						} else {
							p.sendMessage("§cO evento já está aberto.");
							return true;
						}
						
					} else {
						p.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
				if ((args[0].equalsIgnoreCase("parar") || (args[0].equalsIgnoreCase("forcefinalizar")))) {
					if (p.hasPermission("zs.admin")) {
						if (SumoManager.manager.getOcorrendoStatus() == true) {
							SumoManager.manager.setOcorrendoStatus(false);
							for (Player jogadores : SumoManager.manager.getParticipantes()) {
								sendTo(jogadores, "Saida");
								retirarItens(jogadores);
								disableClanDamage(jogadores);
							}
							SumoManager.manager.getParticipantes().clear();
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§a§lSUMO ALL §f- §a§lEVENTO FECHADO.");
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§cO evento foi forçado a finalizar por um ADMINISTRADOR.");
							Bukkit.broadcastMessage("");
							Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
							SumoStop();
					} else {
						p.sendMessage("§cO evento sumo-all já está fechado.");
						return true;
					}
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
					
				}
				if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a área de moderação do Sumo All.");
						sendTo(p, "Entrada");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("verDM")) {
					if (p.hasPermission("zs.admin")) {
						if (!verDM.contains(p)) {
							verDM.add(p);
							p.sendMessage("§eAgora você recebe o tempo para o death-match em sua ActionBar.");
							return true;
						} else {
							verDM.remove(p);
							p.sendMessage("§eAgora você não recebe mais o tempo para o death-match em sua ActionBar.");
						}
					} else {
						p.sendMessage(NO_PERM_ADMIN);
						return true;
					}
					
				}
				if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
						p.sendMessage("§eVocê foi enviado para a saída do Sumo-All.");
						sendTo(p, "Saida");
						return true;
					} else {
						p.sendMessage(NO_PERM_MOD);
						return true;
					}
				}
			if (args[0].equalsIgnoreCase("setentrada")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Entrada");
					p.sendMessage("§eA entrada do evento Sumo-All foi definida com sucesso.");
					return true;
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("setsaida")) {
				if (p.hasPermission("zs.admin")) {
					setLoc(p, "Saida");
					p.sendMessage("§eA saída do evento Sumo-All foi definida com sucesso.");
				} else {
					p.sendMessage(NO_PERM_ADMIN);
					return true;
				}
			}
				if (args[0].equalsIgnoreCase("entrar")) {
					if (Main.getPlugin().getConfig().getString("Entrada") != null) {
						if ((SumoManager.manager.getOcorrendoStatus() == true) && (SumoManager.manager.getEntradaLiberadaStatus() == true)) {
							if (!SumoManager.manager.getParticipantes().contains(p)) {
								if (p.hasPermission("sumo.entrar")) {
									PlayerInventory inv = p.getInventory();
									for (ItemStack i : inv.getContents()) {
										if(i != null && !(i.getType() == Material.AIR)) {
											p.sendMessage("§cEsvazie seu inventário para entrar no evento.");
											return true;
										} 
									} for (ItemStack i : inv.getArmorContents()) {
											if(i != null && !(i.getType() == Material.AIR)) {
												p.sendMessage("§cRetire sua armadura para entrar no evento.");
												return true;
											}
										}
									for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
										p.removePotionEffect(AllPotionEffects.getType());
									}
									sendTo(p, "Entrada");
									SumoManager.manager.getParticipantes().add(p);
									removeCheating(p);
									enableClanDamage(p);
									p.sendMessage("§aVocê entrou no evento Sumo-All!");
									p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
									return true;
								} else {
									p.sendMessage("§cVocê precisa do rank §6[ArrynIII]§c ou superior para participar do evento.");
									p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
									return true;
								}
							} else {
								p.sendMessage("§cVocê já está no evento.");
								return true;
							}
						} else {
							p.sendMessage("§cO evento sumo-All não está aberto ou sua entrada foi fechada.");
							return true;
						}
					} else {
						p.sendMessage("§cA entrada do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("Sair")) {
					if (Main.getPlugin().getConfig().getString("Saida") != null) {
						if (SumoManager.manager.getParticipantes().contains(p)) {
							SumoManager.manager.getParticipantes().remove(p);
							for (Player jogadores : Bukkit.getOnlinePlayers()) {
								if (!(jogadores.hasPermission("zs.mod") || jogadores.hasPermission("zs.admin"))) {
									jogadores.showPlayer(p);
								}
							}
							p.getInventory().clear();
							sendTo(p, "Saida");
							disableClanDamage(p);
							p.sendMessage("§eVocê saiu do evento sumo-All!");
						} else {
							p.sendMessage("§cVocê não está no evento Sumo-All.");
							return true;
						}
					} else {
						p.sendMessage("§cA saída do evento ainda não foi definida por um ADMINISTRADOR.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("info")) {
					if (SumoManager.manager.getOcorrendoStatus() == true) {
						p.sendMessage("§a§lSUMO ALL §f- §aInformações");
						p.sendMessage("");
						p.sendMessage("§eParticipantes: §f" + SumoManager.manager.getParticipantes().size());
						p.sendMessage("");
						return true;
					} else {
						p.sendMessage("§cO evento não está ocorrendo para obter a informação.");
						return true;
					}
				}
			}
		}
		}
			return false;
	}
	
	
	public ItemStack giveKnockBackStick() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cKnockback");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
		item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
		return item;
	}
	
	private void removeCheating(Player p) {
		p.setHealth(20.0D);
	    p.setFoodLevel(20);
	    p.getActivePotionEffects().clear();
	    p.getInventory().clear();
	    p.getInventory().setArmorContents(null);
	    p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
	    p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
	    p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
	    p.setAllowFlight(false);
	    p.getInventory().addItem(giveKnockBackStick());
	    p.getInventory().addItem(giveGoldenApple());
		for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
			p.removePotionEffect(AllPotionEffects.getType());
		}
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
		double X = Main.getPlugin().getConfig().getDouble(location + ".X");
        double Y = Main.getPlugin().getConfig().getDouble(location + ".Y");
        double Z = Main.getPlugin().getConfig().getDouble(location + ".Z");
        float Yaw = (float)Main.getPlugin().getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Main.getPlugin().getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Main.getPlugin().getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
	
	
	public static void setLoc(Player p, String LocationName) {
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Main.getPlugin().getConfig().set(LocationName + ".X", Double.valueOf(x));
		Main.getPlugin().getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Main.getPlugin().getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Main.getPlugin().getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Main.getPlugin().getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Main.getPlugin().getConfig().set(LocationName + ".Mundo", world);
		Main.getPlugin().saveConfig();
	}
	
	public void SumoReset() {
		SumoManager.manager.setOcorrendoStatus(true);
		SumoManager.manager.setEntradaLiberadaStatus(true);
		SumoManager.manager.getParticipantes().clear();
		//SumoManager.manager.setTempo(130);
		SumoManager.manager.setTempo(120);
	}
	
	public void SumoStop() {
		SumoManager.manager.setOcorrendoStatus(false);
		SumoManager.manager.setEntradaLiberadaStatus(false);
		SumoManager.manager.getParticipantes().clear();
		SumoManager.manager.setPvPStatus(false);
		SumoManager.manager.setTempo(-1);
		Bukkit.getServer().getScheduler().cancelTasks(Main.getPlugin());
	}
	
		//////////////////////////
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		 if(p.getWorld().getName().equals("PlotMe")) return;
		Location step = p.getLocation().add(0.0D, 0.0D, 0.0D);
		if (e.getFrom().getX() != e.getTo().getX() && (e.getFrom().getZ() != e.getTo().getZ() && 
				(step.getBlock().getType() == Material.WATER) || step.getBlock().getType() == Material.STATIONARY_WATER)) {
			if (SumoManager.manager.getPvPStatus() == false && SumoManager.manager.getParticipantes().contains(p)) {
				sendTo(p, "Entrada");
				p.sendMessage("§eAtenção! Não enconste na água quando o evento começar.");
				return;
			}
			if (SumoManager.manager.getParticipantes().contains(p)) {
				for (Player participantes : SumoManager.manager.getParticipantes()) {
					participantes.sendMessage("§a§l[SUMO-ALL] §f" + p.getName() + " §afoi eliminado do evento.");
				}
				SumoManager.manager.getParticipantes().remove(p);
				p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
				retirarItens(p);
				sendTo(p, "Saida");
				disableClanDamage(p);
				canKeepRunning();
				
			}
		}
	}
	
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (SumoManager.manager.getParticipantes().contains(p)) {			
			for (Player participantes : SumoManager.manager.getParticipantes()) {
				participantes.sendMessage("§a§l[SUMO-ALL] §f" + p.getName() + " §afoi eliminado do evento.");
			}
			SumoManager.manager.getParticipantes().remove(p);
			p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
			retirarItens(p);
			sendTo(p, "Saida");
			disableClanDamage(p);
			canKeepRunning();
		} 
	}
	
	 @EventHandler
	 public void onBreak(BlockBreakEvent e) {
		 if (SumoManager.manager.getParticipantes().contains(e.getPlayer()) && e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) e.setCancelled(true);
	 }
	
	@EventHandler
	public void onRip(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (SumoManager.manager.getParticipantes().contains(p)) {			
			if (SumoManager.manager.getEntradaLiberadaStatus() == false) {
				for (Player participantes : SumoManager.manager.getParticipantes()) {
					participantes.sendMessage("§a§l[SUMO-ALL] §f" + p.getName() + " §afoi eliminado do evento.");
				}
				SumoManager.manager.getParticipantes().remove(p);
				p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
				retirarItens(p);
				sendTo(p, "Saida");
				disableClanDamage(p);
				canKeepRunning();
			} else {
				sendTo(p, "Entrada");
				removeCheating(p);
			}
		}
	}
	
	
	// DEPOIS MEXO NISSO
	
	public ItemStack giveGoldenApple() {
		ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 32);
		return item;
	}

	
	public ItemStack giveKnockBackStick2() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cKnockback");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 6);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 4);
		return item;
	}
	public ItemStack giveKnockBackStick3() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cKnockback");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 8);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		return item;
	}
	public ItemStack giveKnockBackStick4() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§cKnockback");
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
		item.addEnchantment(Enchantment.DAMAGE_ALL, 7);
		return item;
	}
	
	
	  public void deathmatch() {
		
		(new BukkitRunnable() {
			
			int tempo = 1200; // 5 minutos
			
			@Override
			public void run() {
				if (SumoManager.manager.getParticipantes().size() > 1) {
					tempo--;
					if (tempo == 400) {
						for (Player participantes : SumoManager.manager.getParticipantes()) {
							participantes.getInventory().clear();
							participantes.getInventory().addItem(giveKnockBackStick2());
							participantes.sendMessage(" ");
							participantes.sendMessage(" §6§l[SUMO-ALL] Nível de repulsão aumentado §7(Nível 2).");
							participantes.sendMessage(" ");
						}
						return;
					}
					if (tempo == 300) {
						for (Player participantes : SumoManager.manager.getParticipantes()) {
							participantes.getInventory().clear();
							participantes.getInventory().addItem(giveKnockBackStick2());
							participantes.sendMessage(" ");
							participantes.sendMessage(" §6§l[SUMO-ALL] Nível de repulsão aumentado §7(Nível 3).");
							participantes.sendMessage(" ");
						}
						return;
					}
					
					if (tempo == 100) {
						for (Player participantes : SumoManager.manager.getParticipantes()) {
							participantes.getInventory().clear();
							participantes.getInventory().addItem(giveKnockBackStick2());
							participantes.sendMessage(" ");
							participantes.sendMessage(" §6§l[SUMO-ALL] Nível de repulsão aumentado §7(Nível 5). §c§lDUELO FINAL!");
							participantes.sendMessage(" ");
						}
						return;
					}
					if (tempo == 0) {
						SumoManager.manager.setOcorrendoStatus(false);
						SumoManager.manager.setEntradaLiberadaStatus(false);
						for (Player participantes : SumoManager.manager.getParticipantes()) {
							sendTo(participantes, "Saida");
							disableClanDamage(participantes);
						}
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§a§l[SUMO-ALL] §a§lEVENTO FINALIZADO");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§aNão houveram vencedores. O tempo máximo do evento foi esgotado!");
						Bukkit.broadcastMessage("§aCategoria: §fTodos contra todos.");
						Bukkit.broadcastMessage("");
						SumoStop();
						cancel();
					}
				} else {
					canKeepRunning();
					cancel();
				}
		

			//	PrepararDuelo();
			//	cancel();
				
			}
		}).runTaskTimer(Main.getPlugin(), 0, 20L);
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
		
		
		 @EventHandler
		 public void worldChange(PlayerChangedWorldEvent e) {
			final Player p = e.getPlayer();
			if (SumoManager.manager.getOcorrendoStatus() == true && SumoManager.manager.getParticipantes().contains(p) && (!p.getWorld().getName().equalsIgnoreCase("eventos"))) {
				sendTo(p, "Entrada");
				p.sendMessage("§cVocê não pode teleportar enquanto estiver no evento.");
			}
		 }


			public void canKeepRunning() {
			 SumoManager sumo = SumoManager.manager;
				if (sumo.getEntradaLiberadaStatus() == false && sumo.getOcorrendoStatus() == true) {
					if (sumo.getParticipantes().size() == 1) {
						sumo.setEntradaLiberadaStatus(false);
						sumo.setOcorrendoStatus(false);
						Player vencedor = sumo.getParticipantes().get(0);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§a§l[SUMO-ALL] §a§lEVENTO FINALIZADO");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§f" + vencedor.getName() + " §avenceu o evento!");
						Bukkit.broadcastMessage("§aCategoria: §fTodos contra todos.");
						//Bukkit.broadcastMessage("§aRecompensa: §fTag §6[Sumo] §7+ §f1.000.000 Coins");
						Bukkit.broadcastMessage("§aRecompensa: §6" + premio_message+ "§a Coins");
						Bukkit.broadcastMessage("");
							
						 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
							 vencedor.removePotionEffect(allPotionEffects.getType());
						 }
						 vencedor.getInventory().setArmorContents(null);
						 vencedor.getInventory().clear();
						 NumberFormatAPI formatter = new NumberFormatAPI();
							String custo = formatter.formatNumber(premio);
						 vencedor.sendMessage("§aVocê recebeu $" + custo + " §apor vencer o evento.");

						 disableClanDamage(vencedor);
						 sendTo(vencedor, "Saida");
							Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "coins add " + vencedor.getName() + " " + premio);
							Bukkit.getServer().getConsoleSender().sendMessage("§aRecompensa entregue ao vencedor do sumo.");
						 SumoStop();
						 return;
					}
					if (sumo.getParticipantes().size() == 0) {
						sumo.setEntradaLiberadaStatus(false);
						sumo.setOcorrendoStatus(false);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§a§l[SUMO-ALL] §a§lEVENTO FINALIZADO");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§aNão houveram vencedores. ");
						Bukkit.broadcastMessage("§aCategoria: §fTodos contra todos.");
						Bukkit.broadcastMessage("");
							for (Player participantes : sumo.getParticipantes()) {
								 participantes.getInventory().setArmorContents(null);
								 participantes.getInventory().clear();
								 sendTo(participantes, "Saida");
								 disableClanDamage(participantes);
								 for (PotionEffect allPotionEffects : participantes.getActivePotionEffects()) {
									 participantes.removePotionEffect(allPotionEffects.getType());
								 }
							}
							sumo.getParticipantes().clear();
							SumoStop();
					} 
				}
			}
		 
		 
		 @EventHandler
		 public void onHit(EntityDamageByEntityEvent e) {
			 if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
				 SumoManager sumo = SumoManager.manager;
				 final Player entity = (Player) e.getEntity();
				 final Player damager = (Player) e.getDamager();
				 if (sumo.getParticipantes().contains(entity) && sumo.getParticipantes().contains(damager)) {
					 if (sumo.getPvPStatus() == false) {
						 damager.sendMessage("§cO pvp no momento está desativado.");
						 e.setCancelled(true);
				 	}
				 }
			 }
		 }
		 
		 
		/*
		private void darTagSumo(Player p) {
			Main.getPlugin().getConfig().getString("vencedor").replace(Main.getPlugin().getConfig().getString("vencedor"), "");
			Main.getPlugin().getConfig().set("vencedor", p.getName());
			Main.getPlugin().saveConfig();
		}
		
		@EventHandler
		private void onChat(ChatMessageEvent e) {
			Player p = (Player) e.getSender();
		    if(e.getTags().contains("sumotag") && Main.getPlugin().getConfig().getString("vencedor") != null 
		    		&& Main.getPlugin().getConfig().getString("vencedor").equals(p.getName())) 
		        e.setTagValue("sumotag","&6[Sumo] ");
		}
		*/
	
}
