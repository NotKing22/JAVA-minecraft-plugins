package me.zsnow.chain;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

//import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R3.EntityLiving;


public class Commands implements CommandExecutor {

	public static ArrayList<Player> inArena = new ArrayList<>();
	final Main instance = Main.getPlugin();
	//public static NPC dungeonNPC;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chain")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cComando utilizavel apenas por jogadores.");
				return true;
			}
			final Player p = (Player) sender;
			if (args.length == 0) {
				checkPerm(p, "zs.gerente", "zs.mod");
				return true;
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("entrar")) {
					if (!(inArena.contains(p))) {
						if (p.hasPermission("chain.entrar")) {
						final PlayerInventory inv = p.getInventory();
							for (ItemStack i : inv.getContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									p.sendMessage("§cEsvazie seu inventário para entrar na arena Chain.");
									return true;
								} 
							} for (ItemStack i : inv.getArmorContents()) {
									if(i != null && !(i.getType() == Material.AIR)) {
										p.sendMessage("§cRetire sua armadura para entrar na arena Chain.");
										return true;
									}
								}
						if (Main.getPlugin().getConfig().contains("ENTRADA")) {
							sendTo(p, "Entrada");
						
	            		inArena.add(p);
            			ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado(a) para a ARENA CHAIN.");
		            			
            			for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
            					p.removePotionEffect(AllPotionEffects.getType());
            				}
	    					p.setFlying(false);
    						p.setAllowFlight(false);
			            			
							giveSword(p);	
							giveAxe(p);
	            				giveGoldenApple(p);
	            					giveFishingRod(p);
	            						giveArcher(p);
	            							giveArrow(p);
	            								giveArmors(p);
			            			
			            			p.sendMessage(" ");
			            			p.sendMessage("§6§l[!] §eBem-vindo á arena chain.");
			            			p.sendMessage(" ");
			            			p.playSound(p.getLocation(), Sound.NOTE_PIANO, 2.0F, 1.5F);
		            				return true;
						} 
							p.sendMessage("§cA arena chain ainda não possuí uma localização definida.");
							return true;
					} else {
						p.sendMessage("§cVocê precisa do rank §c[Pedra]§c ou superior para acessar a arena Chain.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
						return true;
					}
				}
					p.sendMessage("§e§lVocê já está dentro da arena chain.");
					p.sendMessage("§e§lPara sair, utilize §f/chain sair");
					return true;
				} else if (args[0].equalsIgnoreCase("setlocation")) { 
					if (p.hasPermission("zs.gerente")) {
						        String npc = "NPC";
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".world", p.getLocation().getWorld().getName());
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".x", Double.valueOf(p.getLocation().getX()));
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".y", Double.valueOf(p.getLocation().getY()));
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".z", Double.valueOf(p.getLocation().getZ()));
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".yaw", Float.valueOf(p.getLocation().getYaw()));
						       Main.getPlugin().getConfig().set(String.valueOf(npc) + "." + ".pitch", Float.valueOf(p.getLocation().getPitch()));
						       Main.getPlugin().saveConfig();
						       ActionBarAPI.sendActionBarMessage(p, "§eLocation do NPC definida com sucesso.");
								return true;
							} else {
								p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
								return true;
							}
						} else if (args[0].equalsIgnoreCase("spawnpc") || (args[0].equalsIgnoreCase("spawnnpc") || (args[0].equalsIgnoreCase("npcspawn")))) {
							if (p.hasPermission("zs.gerente")) {
						         // NPCManager.CreateNPC2();
						          ActionBarAPI.sendActionBarMessage(p, "§eNPC spawnado com sucesso.");
						          return true;
							} else {
								p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
								return true;
							}
						}
				else if (args[0].equalsIgnoreCase("sair")) {
					if (inArena.contains(p)) {
						
		            	p.getInventory().clear();	
		            		inArena.remove(p);
		            		Listeners.abates.remove(p);
			              		p.getInventory().setHelmet(null);
			          				p.getInventory().setChestplate(null);
			          					p.getInventory().setLeggings(null);
			          						p.getInventory().setBoots(null);
			          						
			            			ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado(a) para saída.");
			            				p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0F, 1.5F);
			            				sendTo(p, "Saida");
			            				return true;
						}
						 p.sendMessage("§cVocê não está dentro da arena chain!");
						 p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
						 return true;
					
				} else if (args[0].equalsIgnoreCase("setarena") || args[0].equalsIgnoreCase("setentrada")) {
					if (p.hasPermission("zs.gerente")) {
						
										setLoc(p, "Entrada");
		        						ActionBarAPI.sendActionBarMessage(p, "§eEntrada da arena definida com sucesso!");
		        						Main.getPlugin().saveConfig();
		        						return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
						return true;
					}
					
				} else if (args[0].equalsIgnoreCase("setsaida")) {
					if (p.hasPermission("zs.gerente")) {
							setLoc(p, "Saida");
		        						ActionBarAPI.sendActionBarMessage(p, "§eSaída da arena definida com sucesso!");
		        							Main.getPlugin().saveConfig();
		        							return true;
					} else {
						p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
						return true;
					}
				
				} else if (args[0].equalsIgnoreCase("modsair")) {
					if (p.hasPermission("zs.mod")) {
					if (Main.getPlugin().getConfig().contains("SAIDA")) {
						 sendTo(p, "Saida");
					            	p.getInventory().clear();	
					            		
						              		p.getInventory().setHelmet(null);
						          				p.getInventory().setChestplate(null);
						          					p.getInventory().setLeggings(null);
						          						p.getInventory().setBoots(null);
		          						
		            			ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado(a) para saída.");
		            				p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 2.0F, 1.5F);
		            				return true;
						} 
						p.sendMessage("§cA arena chain ainda não possuí uma localização definida.");
						return true;
					}
					p.sendMessage("§cVocê precisa do cargo §6Moderador §cou superior parar executar este comando.");
					return true;
				} else if (args[0].equalsIgnoreCase("moderar")) {
					if (p.hasPermission("zs.mod")) {
						if (Main.getPlugin().getConfig().contains("ENTRADA")) {
							 
							sendTo(p, "Entrada");
		            			ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado em modo MODERAÇAO para a chain.");
		            			
		            			p.sendMessage(" ");
		            			p.sendMessage("§4[!] §cVocê entrou na chain.");
		            			p.sendMessage(" ");
		            			p.playSound(p.getLocation(), Sound.NOTE_PIANO, 2.0F, 1.5F);
	            				
		            			return true;
							}
						} else {
					p.sendMessage("§cVocê precisa do cargo §6Moderador §cou superior para executar este comando.");
					return true;
						}
				} else {
					checkPerm(p, "zs.gerente", "zs.mod");
					return true;
				}
			} if (args.length == 2) {
				if (p.hasPermission("zs.gerente") || p.hasPermission("zs.mod")) {
					if (args[0].equalsIgnoreCase("expulsar") || args[0].equalsIgnoreCase("kick")) {
						if (!(args[1].equals("-todos"))) {
							Player target = Bukkit.getPlayer(args[1]);
								if (!(target == null)) {
									if (inArena.contains(target)) {
										if (Main.getPlugin().getConfig().contains("Saida")) {
											Listeners.abates.remove(target);
											sendTo(target, "Saida");
					            	target.getInventory().clear();	
					            		inArena.remove(target);
					            		
					            		target.getInventory().setHelmet(null);
					            			target.getInventory().setChestplate(null);
					            				target.getInventory().setLeggings(null);
					            					target.getInventory().setBoots(null);
						          						
					            			ActionBarAPI.sendActionBarMessage(target, "§aVocê foi enviado(a) para saída.");
						            			
		            			for (Player staff : Bukkit.getOnlinePlayers()) {
		            				if (staff.hasPermission("zs.mod")) {
		            					staff.sendMessage("§c[!] §e[§6§lCHAIN§e] §6" +target.getName()+ " §efoi expulso da arena chain.");
			            				}
			            			}
		            					p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
			            				return true;
							}
							p.sendMessage("§cEste jogador não está dentro da arena chain.");
							return true;
								}
								p.sendMessage("§cJogador não encontrado em nosso banco de dados.");	
								return true;
						} if (p.hasPermission("zs.gerente")) {	
							for (Player jogadores : inArena) {
									
		            		jogadores.getInventory().clear();	
			            		inArena.remove(jogadores);
			            		
			            		jogadores.getInventory().setHelmet(null);
				            		jogadores.getInventory().setChestplate(null);
					            		jogadores.getInventory().setLeggings(null);
					            			jogadores.getInventory().setBoots(null);
					            			sendTo(jogadores, "Saida");
			            			ActionBarAPI.sendActionBarMessage(jogadores, "§aVocê foi enviado(a) para saída.");
									} 
	        			for (Player staff : Bukkit.getOnlinePlayers()) {
	        				if (staff.hasPermission("zs.mod")) {
	        					staff.sendMessage("§c[!] §e[§6§lCHAIN§e] §6TODOS §eforam expulsos da arena chain.");
					        				}
		        				p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
	            				return true;
					        			}
							}
						p.sendMessage("§cVocê precisa do cargo §6Gerente §cou superior para executar este comando.");
						return true;
					}
					p.sendMessage("§e[§6§lCHAIN§e] §eUtilize /chain expulsar <jogador>.");
					return true;
				} 
				p.sendMessage("§cVocê precisa do cargo §6Moderador §cou superior para executar este comando.");
				return true;
			}
			checkPerm(p, "zs.gerente", "zs.mod");
			return true;
			}
		}
		return false;
	}
	
	private void checkPerm(Player p, String perm1, String perm2) {
		if (!(p.hasPermission(perm1) || p.hasPermission(perm2))) {
			p.sendMessage("§e§lARENA CHAIN §7(Comandos)");
			p.sendMessage("");
			p.sendMessage(" §6* §e/chain entrar");
			p.sendMessage(" §6* §e/chain sair");
			p.sendMessage(" ");
			return;
		} else {
			p.sendMessage("§e§lARENA CHAIN §7(Comandos)");
			p.sendMessage("");
			p.sendMessage(" §6* §e/chain entrar");
			p.sendMessage(" §6* §e/chain sair");
			p.sendMessage(" §4* §c/chain moderar");
			p.sendMessage(" §4* §c/chain modsair");
			p.sendMessage(" §4* §c/chain setarena");
			p.sendMessage(" §4* §c/chain setsaida");
			p.sendMessage(" §4* §c/chain expulsar <Jogador>");
			p.sendMessage("");
		}
	}
	
	  @SuppressWarnings("unused")
	private void setAI(LivingEntity entity, boolean hasAi) {
		    EntityLiving handle = ((CraftLivingEntity)entity).getHandle();
		    handle.getDataWatcher().watch(15, Byte.valueOf((byte)(hasAi ? 0 : 1)));
		  }
	
	
	private void giveSword(Player p) {
		ItemStack item = new ItemStack(Material.WOOD_SWORD);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().addItem(item);
		
	}
	
	private void giveAxe(Player p) {
		ItemStack item = new ItemStack(Material.WOOD_AXE);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().addItem(item);
	}
	
	private void giveGoldenApple(Player p) {
		ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 3);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		p.getInventory().addItem(item);
		
	}
	
	private void giveFishingRod(Player p) {
		ItemStack item = new ItemStack(Material.FISHING_ROD);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().addItem(item);
		
	}
	
	private void giveArcher(Player p) {
		ItemStack item = new ItemStack(Material.BOW);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().addItem(item);
		
	}
	
	private void giveArrow(Player p) {
		ItemStack item = new ItemStack(Material.ARROW, 8);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		p.getInventory().addItem(item);
		
	}
	
	private void giveArmors(Player p) {
		ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET);	
		ItemMeta helmetmeta = helmet.getItemMeta();
		helmetmeta.setDisplayName("§eChain");
		helmet.setItemMeta(helmetmeta);
		helmet.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);	
		ItemMeta chestplatemeta = chestplate.getItemMeta();
		chestplatemeta.setDisplayName("§eChain");
		chestplate.setItemMeta(chestplatemeta);
		chestplate.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack legging = new ItemStack(Material.CHAINMAIL_LEGGINGS);	
		ItemMeta leggingmeta = legging.getItemMeta();
		leggingmeta.setDisplayName("§eChain");
		legging.setItemMeta(leggingmeta);
		legging.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack boots = new ItemStack(Material.CHAINMAIL_BOOTS);	
		ItemMeta bootsmeta = boots.getItemMeta();
		bootsmeta.setDisplayName("§eChain");
		boots.setItemMeta(bootsmeta);
		boots.getItemMeta().spigot().setUnbreakable(true);
		
		p.getInventory().setHelmet(helmet);
		p.getInventory().setChestplate(chestplate);
		p.getInventory().setLeggings(legging);
		p.getInventory().setBoots(boots);
	}
	public void sendTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		final double X = instance.getConfig().getDouble(location + ".X");
		final double Y = instance.getConfig().getDouble(location + ".Y");
		final double Z = instance.getConfig().getDouble(location + ".Z");
		final float Yaw = (float)instance.getConfig().getLong(location + ".Yaw");
		final float Pitch = (float)instance.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(instance.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		p.teleport(loc);
	}
	
	
	public void setLoc(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
		final double x = p.getLocation().getBlockX();
		final double y = p.getLocation().getBlockY();
		final double z = p.getLocation().getBlockZ();
		final float yaw = p.getLocation().getYaw();
		final float pitch = p.getLocation().getPitch();
		final String world = p.getLocation().getWorld().getName().toString();
		instance.getConfig().set(LocationName + ".X", Double.valueOf(x));
		instance.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		instance.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		instance.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		instance.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		instance.getConfig().set(LocationName + ".Mundo", world);
		instance.saveConfig();
	}
	
}
	
