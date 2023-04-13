package me.zsnow.stonegoldenpig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import me.zsnow.stonegoldenpig.api.LocationAPI;
import me.zsnow.stonegoldenpig.configs.Configs;
import me.zsnow.stonegoldenpig.manager.SorteioManager;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

@SuppressWarnings("deprecation")
public class Commands implements CommandExecutor, Listener {
	
	private final String seta = "»";
	SorteioManager sorteio = SorteioManager.getInstance();
	private static int contador;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("leilao")) {
				if (!(sender instanceof Player)) return false;
				
				Player p = (Player) sender;
				
				if (args.length == 0) {
					if (sorteio.getOcorrendo()) {
						InventoryControll.getInstance().openMenu(p);
					} else {
						p.sendMessage("§cNão há um leilão ocorrendo no momento.");
						return true;
					}
				}
				if (args.length >= 1) {
					if (p.hasPermission("zs.gerente")) {
						if (args[0].equalsIgnoreCase("help")) {
								sender.sendMessage("§e§l§m-------------§8§l[§6§lStoneGoldenPIG§8§l]§e§m----------------");
								sender.sendMessage(" §7" + seta + " §c§l/leilao spawn §6- §eSpawna o porco dourado.");
								sender.sendMessage(" §7" + seta + " §c§l/leilao additem <valor-inicial> §6- §eSpawna o porco dourado.");
								sender.sendMessage(" §7" + seta + " §c§l/leilao remover §6- §eCancela o evento.");
								sender.sendMessage(" §7" + seta + " §c§l/leilao setspawn <Número> §6- §edefine o spawn.");
								sender.sendMessage(" §7" + seta + " §c§l/leilao reload §6- §eRecarrega a configuração.");
								sender.sendMessage("§e§l§m--------------------§8§l[§6§X§8§l]§e§m--------------------");
								return true;
							}
					if (args[0].equalsIgnoreCase("additem")) {
						if (args.length < 2) {
							p.sendMessage("§cUse: /leilao additem <valor-minimo>");
							return true;
						}
						if (args.length == 2) {
							if (p.getItemInHand().getType() != Material.AIR && p.getItemInHand() != null) {
								double valor;
								try {
									valor = Double.parseDouble(args[1]);
								} catch (NumberFormatException e) {
									p.sendMessage("§cO valor informado não é um número.");
									return true;
								}
								
								if (sorteio.getOcorrendo() == false) {
									sorteio.resetData();
									
									apagarPorco();
								
								ArrayList<String> loc = new ArrayList<>();
								for (String key : Configs.locations.getConfig().getConfigurationSection("Porco-Location").getKeys(false)) {
									loc.add(key);
								}
							int RNG = new Random().nextInt(loc.size() + 1);
							while (RNG == 0) {
								RNG = new Random().nextInt(loc.size() + 1);
							}
							
							Pig porquinho = (Pig)LocationAPI.getLocation().location(RNG).getBlock().getWorld()
									.spawn(LocationAPI.getLocation().location(RNG).getBlock().getLocation(), Pig.class);
							
							HolographicDisplaysAPI.createHologram(Main.getInstance(), porquinho.getLocation().add(0, 2, 0), 
									"§6§lPorquinho dourado", "§7Clique para interagir.", "§e(/leilao)");
							
							porquinho.setMetadata("porquinhodourado", new FixedMetadataValue(Main.getInstance(), "porquinhodourado"));
							porquinho.setCustomNameVisible(false);
							porquinho.teleport(LocationAPI.getLocation().location(RNG));
							noAI(porquinho);
						
							for (String msg : Configs.config.getConfig().getStringList("Porco-spawn")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
							}
							if (Configs.config.getConfig().getBoolean("Play-spawn-sound") == true) {
								p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("Sound-type").toUpperCase()), 1.0F, 0.5F);
							}
							
							sorteio.setOcorrendoStatus(true);
							sorteio.setApostasLiberadas(true);
							
							InventoryControll.loadItemFromPlayerHand(p.getItemInHand(), valor);
							
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							contador = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								
								@Override
								public void run() {
									
									if (sorteio.getOcorrendo() == true && sorteio.apostasLiberadas() == true) {
										if (sorteio.getTempo() > 10) {
											sorteio.setTempo(sorteio.getTempo()-10);
											for (String msg : Configs.config.getConfig().getStringList("Porco-task-msg")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
											}
										} else {
											if (sorteio.getParticipantes().size() >= 1) {
												sorteio.setApostasLiberadas(false);
												sorteio.chatEvent.clear();
												
												for (String msg : Configs.config.getConfig().getStringList("Porco-sorteando")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
												}
												sortearTask();
												
												sh.cancelTask(contador);
												
												
												
												
											} else {
												for (String msg : Configs.config.getConfig().getStringList("Porco-sem-participantes")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
												}
												sh.cancelTask(contador);
												sorteio.resetData();
												
											}
										}
									} else {
										sh.cancelTask(contador);
									}
								}
							}, 0L, 200L);
						} else {
							p.sendMessage("§cJá há um evento em andamento.");
							return true;
						}
							} else {
								p.sendMessage("§cEsteja segurando um item em mãos ao executar o comando.");
							}
						}
					}
					
					if (args[0].equalsIgnoreCase("spawn")) {
						if (sorteio.getOcorrendo() == false) {
							sorteio.resetData();
							
							apagarPorco();
							
							ArrayList<String> loc = new ArrayList<>();
								for (String key : Configs.locations.getConfig().getConfigurationSection("Porco-Location").getKeys(false)) {
									loc.add(key);
								}
							int RNG = new Random().nextInt(loc.size() + 1);
							while (RNG == 0) {
								RNG = new Random().nextInt(loc.size() + 1);
							}
							
							Pig porquinho = (Pig)LocationAPI.getLocation().location(RNG).getBlock().getWorld()
									.spawn(LocationAPI.getLocation().location(RNG).getBlock().getLocation(), Pig.class);
							
							HolographicDisplaysAPI.createHologram(Main.getInstance(), porquinho.getLocation().add(0, 2, 0), 
									"§6§lPorquinho dourado", "§7Clique para interagir.", "§e(/leilao)");
							
							porquinho.setMetadata("porquinhodourado", new FixedMetadataValue(Main.getInstance(), "porquinhodourado"));
							porquinho.setCustomNameVisible(false);
							porquinho.teleport(LocationAPI.getLocation().location(RNG));
							noAI(porquinho);
						
							for (String msg : Configs.config.getConfig().getStringList("Porco-spawn")) {
								Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
							}
							if (Configs.config.getConfig().getBoolean("Play-spawn-sound") == true) {
								p.playSound(p.getLocation(), Sound.valueOf(Configs.config.getConfig().getString("Sound-type").toUpperCase()), 1.0F, 0.5F);
							}
							
							sorteio.setOcorrendoStatus(true);
							sorteio.setApostasLiberadas(true);
							InventoryControll.loadItem(InventoryControll.sortearItem());
							
							BukkitScheduler sh = Bukkit.getServer().getScheduler();
							contador = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
								
								@Override
								public void run() {
									
									if (sorteio.getOcorrendo() == true && sorteio.apostasLiberadas() == true) {
										if (sorteio.getTempo() > 10) {
											sorteio.setTempo(sorteio.getTempo()-10);
											for (String msg : Configs.config.getConfig().getStringList("Porco-task-msg")) {
												Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
											}
										} else {
											if (sorteio.getParticipantes().size() >= 1) {
												sorteio.setApostasLiberadas(false);
												sorteio.chatEvent.clear();
												
												for (String msg : Configs.config.getConfig().getStringList("Porco-sorteando")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
												}
												sortearTask();
												
												sh.cancelTask(contador);
												
												
												
												
											} else {
												for (String msg : Configs.config.getConfig().getStringList("Porco-sem-participantes")) {
													Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
												}
												sh.cancelTask(contador);
												sorteio.resetData();
												
											}
										}
									} else {
										sh.cancelTask(contador);
									}
									
								}
							}, 0L, 200L);
						} else {
							p.sendMessage("§cJá há um evento em andamento.");
							return true;
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("remover")) {
						SorteioManager.getInstance().resetData();
						apagarPorco();
						return true;
					}
					if (args[0].equalsIgnoreCase("setspawn")) {
						if (args.length  == 1) {
							p.sendMessage("§cUse /leilao setspawn <number>");
							return true;
						}
						if (args.length == 2) {
							int number;
							try {
								 number = Integer.parseInt(args[1]);
							} catch (NumberFormatException e) {
								p.sendMessage("§cInforme um valor válido.");
								return true;
							}
							if (number > 10 || number <= 0) {
								p.sendMessage("§cNão é possível definir mais que 10 localizaçoes.");
								return true;
							}
							LocationAPI.getLocation().setLocation(p, number);
							p.sendMessage("§aVocê definiu a localização " + number + " §ado porquinho.");
							return true;
						}
					}
			} else {
				sender.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
				}
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void setAI(LivingEntity entity, boolean hasAi) {
	    EntityLiving handle = ((CraftLivingEntity)entity).getHandle();
	    handle.getDataWatcher().watch(15, Byte.valueOf((byte)(hasAi ? 0 : 1)));
	  }
	
	void noAI(Entity bukkitEntity) {
	    net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) bukkitEntity).getHandle();
	    NBTTagCompound tag = nmsEntity.getNBTTag();
	    if (tag == null) {
	        tag = new NBTTagCompound();
	    }
	    nmsEntity.c(tag);
	    tag.setInt("NoAI", 1);
	    nmsEntity.f(tag);
	}
	
	// CANCELAR O EVENTO NO FIM
	@EventHandler
	public void npcDamage(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof Pig)) {
			Pig porco = (Pig)e.getEntity();
			  if (porco.hasMetadata("porquinhodourado")) {
				e.setCancelled(true);
			  if (((e.getDamager() instanceof Player)) && (e.getDamager().isOp())) {
				  Player p = (Player)e.getDamager();
				  	if (p.getItemInHand().getType() == Material.WOOD_AXE) {
				  		porco.damage(100.0D);
				  			p.sendMessage("§4[§4§lX§4] §cO porquinho dourado foi removido.");
				  			SorteioManager.getInstance().resetData();
				  			for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : HologramsAPI.getHolograms(Main.getInstance())) {
				  			    Location pigLoc = porco.getLocation();
				  			    Location hologramLoc = hologram.getLocation();
				  			    if (pigLoc.distance(hologramLoc) < 10) {
				  			        hologram.delete();
				  			    }
				  			}
					    }
				    }
				}
			}
		}
	
	public void apagarPorco() {
		for (World mundo : Bukkit.getWorlds()) {
			for (Entity entidades : mundo.getEntities()) {
				if (entidades instanceof Pig && entidades.hasMetadata("porquinhodourado")) {
					 entidades.remove();
					 for (com.gmail.filoghost.holographicdisplays.api.Hologram hologram : HologramsAPI.getHolograms(Main.getInstance())) {
			  			    Location pigLoc = entidades.getLocation();
			  			    Location hologramLoc = hologram.getLocation();
			  			    if (pigLoc.distance(hologramLoc) < 10) {
			  			        hologram.delete();
			  			    }
			  			}
				}
			}
		}
	}
	
	public void sortearTask() {
		
		new BukkitRunnable() {
			SorteioManager sorteio = SorteioManager.getInstance();
			DecimalFormat formatar = new DecimalFormat("###,###,###,###,###.##");
			int tempo = Configs.config.getConfig().getInt("Sorteio-final-tempo");
			
			@Override
			public void run() {
				
				if (sorteio.getOcorrendo() == true) {
					
					if (tempo >= 1) {
						tempo--;
						for (Player participantes : sorteio.getParticipantes()) {
							participantes.playSound(participantes.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
						}
					} else {
						for (Player participantes : sorteio.getParticipantes()) {
							participantes.playSound(participantes.getLocation(), Sound.LEVEL_UP, 1.0f, 0.5f);
						}
						String vencedor = sorteio.getUserTopLance();
						double valor = sorteio.getValorMaisAltoApostado();
						for (String msg : Configs.config.getConfig().getStringList("Porco-sorteou")) {
							Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("$player", vencedor).replace("$valor", formatar.format(valor)));
						}
						Player pn = Bukkit.getServer().getPlayerExact(vencedor);
						if (pn == null && sorteio.givarItem()) {
							sorteio.salvarItem.put(vencedor, sorteio.getItemLeiloado());
							sorteio.salvarVencedor.add(vencedor);
						} else {
							if (sorteio.givarItem() == true) {
							ItemStack[] contents = pn.getInventory().getContents();
							for (ItemStack item : contents) {
								if(item == null || (item.getType() == Material.AIR)) {
									pn.getInventory().addItem(sorteio.getItemLeiloado());
									break;
								}
							}
						}
						}
						sorteio.valorDepositado.remove(sorteio.getUserTopLance());
						sorteio.resetData();
						cancel();
					}
				} else {
					cancel();
				}
			}
		}.runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
	/*
	 * 
	 * for(Entity en : player.getNearbyEntities(10, 10, 10)) {
            if(en instanceof ArmorStand) {
                en.remove();
            }
	 */
	
	
}
