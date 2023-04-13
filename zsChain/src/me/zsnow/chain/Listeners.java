package me.zsnow.chain;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
//import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

//import net.citizensnpcs.api.CitizensAPI;
//import net.citizensnpcs.api.event.NPCRightClickEvent;
//import net.citizensnpcs.api.npc.NPC;

public class Listeners implements Listener {

	ArrayList<String> commands = new ArrayList<>();
	public static HashMap<Player, Integer> abates = new HashMap<>();

	@EventHandler
	public void deathEvent(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player vitima = e.getEntity();
			if (Commands.inArena.contains(vitima)) {
				Commands.inArena.remove(vitima);
				e.getDrops().clear();
				sendTo(vitima, "Saida");
				if (e.getEntity().getKiller() instanceof Player) {
					if (Commands.inArena.contains(e.getEntity().getKiller())) {
						Player assassino = e.getEntity().getKiller();
							String vitimaPrefix = VaultHook.getPlayerPrefix(vitima.getName());
							String assassinoPrefix = VaultHook.getPlayerPrefix(assassino.getName());
				ActionBarAPI.sendActionBarMessage(assassino, "§eVocê matou §7" + vitimaPrefix + vitima.getName()+ " §ee recebeu §6recompensas§e.");
						vitima.sendMessage("§eVocê foi morto por §7" + assassinoPrefix + assassino.getName());
						if (abates.containsKey(vitima)) {
							abates.remove(vitima);
						}
						
						ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);	
							ItemMeta itemmeta = item.getItemMeta();
							itemmeta.setDisplayName("§eChain");
							item.setItemMeta(itemmeta);
							assassino.getInventory().addItem(item);
						ItemStack item2 = new ItemStack(Material.ARROW, 2);	
							ItemMeta itemmeta2 = item.getItemMeta();
							itemmeta2.setDisplayName("§eChain");
							item2.setItemMeta(itemmeta2);
							assassino.getInventory().addItem(item2);
			
			assassino.playSound(assassino.getLocation(), Sound.ANVIL_LAND, 1.0F, 0.5F);
			
			if (!abates.containsKey(assassino)) {
				abates.put(assassino, 1);
			} else {
				abates.put(assassino, (abates.get(assassino) + 1));
			}
		
		assassino.sendMessage("§eVocê matou §7" + vitimaPrefix + vitima.getName()+ " §ee recebeu:");
		assassino.sendMessage("§6* §f1x Maçã dourada§e.");
		assassino.sendMessage("§6* §f2x Flechas§e.");
		assassino.sendMessage("§c§l* §fTotal de abates: §7" + abates.get(assassino));
		

		
		ItemStack[] inv = assassino.getInventory().getContents();
		for (ItemStack inventory : inv) {
		if (inventory != null && inventory.getType() != Material.AIR) {
			if (abates.get(assassino) <= 2) {
				if (inventory.getType().equals(Sword().getType()))  {
					inventory.addEnchantment(Enchantment.DAMAGE_ALL, abates.get(assassino));
					assassino.sendMessage("§6[Chain] §eSua espada e machado uparam para o nível §c§l" + romanFormat(abates.get(assassino)) +"§e.");
					}
				if (inventory.getType().equals(Axe().getType()))  {
					inventory.addEnchantment(Enchantment.DAMAGE_ALL, abates.get(assassino));
					return;
					}
				}
				if (abates.get(assassino) <= 5 && abates.get(assassino) >= 3) {
					if (inventory.getType().equals(Bow().getType())) {
						if (abates.get(assassino) == 3) {
							inventory.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
							assassino.sendMessage("§6[Chain] §eSeu arco upou para o nível §e§lI§e.");
							return;
						}
						if (abates.get(assassino) == 4) {
							inventory.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
							assassino.sendMessage("§6[Chain] §eSeu arco upou para o nível §6§lII§e.");
							return;
						}
						if (abates.get(assassino) == 5) {
							inventory.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
							assassino.sendMessage("§6[Chain] §eSeu arco upou para o nível §c§lIII§e.");
							return;
						}
					}
				}
			}
		}
		ItemStack[] armor = assassino.getEquipment().getArmorContents();
		if (abates.get(assassino) <= 7 && abates.get(assassino) >= 6) {
			for (ItemStack armadura : armor) {
				if (armadura != null && armadura.getType() != Material.AIR) {
					if (armadura.getType().equals(Chestplate().getType())) {
						if (abates.get(assassino) == 6) {
							armadura.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
							assassino.sendMessage("§6[Chain] §eSeu peitoral upou para o nível §6§lI§e.");
							return;
						}
						if (abates.get(assassino) == 7) {
							armadura.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
							assassino.sendMessage("§6[Chain] §eSeu peitoral upou para o nível §c§lII§e.");
							return;
						}
					}
				}
			}
		}
		if (abates.get(assassino) >= 10) {
			assassino.sendMessage("§6[Chain] §eVocê recuperou metade da vida.");
				if (assassino.getHealth() < 20 && assassino.getHealth() <= 10) {
					assassino.setHealth(assassino.getHealth() + 10);
					return;
				}
				if (assassino.getHealth() < 20 && assassino.getHealth() >= 10) {
					assassino.setHealth(20);
					return;
				}
		}
		if (abates.get(assassino) == 11) {
			Bukkit.broadcastMessage(" ");
			Bukkit.broadcastMessage("§6§l[CHAIN] §f" + assassinoPrefix + assassino.getName() + " §eestá dominando a árena chain. §7(/chain entrar)");
			Bukkit.broadcastMessage(" ");
		}
		
		
	/*	for (ItemStack espadaLevel : inv) {
			if (espadaLevel != null) {
				if (espadaLevel.getType() == Sword().getType()) {
					if (espadaLevel.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 1) {
						espadaLevel.addEnchantment(Enchantment.DAMAGE_ALL, 1);
						espadaLevel.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
						assassino.sendMessage("§6[Chain] §eSua espada upou para o nível §6§lI§e.");
						return;
					} 
					if (espadaLevel.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 1) {
						espadaLevel.addEnchantment(Enchantment.DAMAGE_ALL, 2);
						assassino.sendMessage("§6[Chain] §eSua espada upou para o nível §c§lII§7(final)§e.");
						return;
					}
					if (espadaLevel.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 2) {
						for (ItemStack arcoLevel : inv) {
							if (arcoLevel != null) {
								if (arcoLevel.getType() == Bow().getType()) {
									if (arcoLevel.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) < 1) {
										arcoLevel.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
										assassino.sendMessage("§6[Chain] §eSua arco upou para o nível §e§lI§e.");
										return;
									}
									if (arcoLevel.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 1) {
										arcoLevel.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
										assassino.sendMessage("§6[Chain] §eSua arco upou para o nível §6§lII§e.");
										return;
									}
									if (arcoLevel.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 2) {
										if (arcoLevel.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK) < 1) {
											arcoLevel.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
											assassino.sendMessage("§6[Chain] §eSua arco upou para o nível §c§lIII§7(final)§e.");
											return;
										}
									}
									if (arcoLevel.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK) == 1) {
										ItemStack[] armor = assassino.getEquipment().getArmorContents();
										for (ItemStack peitoralLevel : armor) {
											if (peitoralLevel != null) {
												if (peitoralLevel.getType() == Chestplate().getType()) {
													if (peitoralLevel.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) < 1) {
													peitoralLevel.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1);
													assassino.sendMessage("§6[Chain] §eSua peitoral upou para o nível §6§lI§e.");
													return;
													}
												}
												if (peitoralLevel.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) == 1) {
													peitoralLevel.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2);
													assassino.sendMessage("§6[Chain] §eSua peitoral upou para o nível §c§lII§7(final)§e.");
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				} 
			}
		}*/
	}
}
				Commands.inArena.remove(vitima);
	}
		}
			}
	
	private ItemStack Sword() {
		ItemStack item = new ItemStack(Material.WOOD_SWORD);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		return item;
	}
	
	private ItemStack Axe() {
		ItemStack item = new ItemStack(Material.WOOD_AXE);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		return item;
	}
	
	private ItemStack Bow() {
		ItemStack item = new ItemStack(Material.BOW);	
		ItemMeta itemmeta = item.getItemMeta();
		itemmeta.setDisplayName("§eChain");
		item.setItemMeta(itemmeta);
		item.getItemMeta().spigot().setUnbreakable(true);
		return item;
	}
	
	private ItemStack Chestplate() {
		ItemStack chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);	
		ItemMeta chestplatemeta = chestplate.getItemMeta();
		chestplatemeta.setDisplayName("§eChain");
		chestplate.setItemMeta(chestplatemeta);
		chestplate.getItemMeta().spigot().setUnbreakable(true);
		return chestplate;
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = (Player) e.getPlayer();
		if (Commands.inArena.contains(p)) {
          	p.getInventory().clear();	
          		p.getInventory().setHelmet(null);
          			p.getInventory().setChestplate(null);
          				p.getInventory().setLeggings(null);
          					p.getInventory().setBoots(null);
			if (Main.getPlugin().getConfig().contains("Saida")) {
			      	Commands.inArena.remove(p);
			      		sendTo(p, "Saida");
								ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado(a) para saída.");
							}
						}
							}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		Player p = (Player) e.getPlayer();
			if (Commands.inArena.contains(p)) {
	          	p.getInventory().clear();	
	          		p.getInventory().setHelmet(null);
	          			p.getInventory().setChestplate(null);
	          				p.getInventory().setLeggings(null);
	          					p.getInventory().setBoots(null);
				if (Main.getPlugin().getConfig().contains("Saida")) {
				      	Commands.inArena.remove(p);
				      		sendTo(p, "Saida");
  								ActionBarAPI.sendActionBarMessage(p, "§aVocê foi enviado(a) para saída.");
								}
							}
						}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
			if (Commands.inArena.contains(p)) {
				if (!(p.hasPermission("zs.gerente"))) {
					e.setCancelled(true);
					}
				}
			}
	
	@EventHandler
	  public void inventoryClickE(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
		      return;
		}
		Player p = (Player) e.getWhoClicked();
	    if (e.getInventory().getTitle().equals("§7§lEntrada/informações")) {
	        e.setCancelled(true);
	        ItemStack currentItem = e.getCurrentItem();
	        if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
	           if (e.getSlot() == 13) {
	        	   if (e.getCurrentItem().getItemMeta().getDisplayName() == "§eEntrar na arena") 
	        	   {  p.closeInventory();
	        		  p.chat("/chain entrar");
	        	   	  return; }
	        }
	           if (e.getSlot() == 18) {
	        	   if (e.getCurrentItem().getItemMeta().getDisplayName() == "§eInformações") {
	        		   return;
	            }
	           }
	        }
	    }
	}
	
	
	
	//@EventHandler
	public void npcDamage(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof PigZombie)) {
			PigZombie villager = (PigZombie)e.getEntity();
			  if ((villager.getCustomName() != null) && (
				        (villager.getCustomName().contains("§eChain entrada")))) {
							e.setCancelled(true);
			  if (((e.getDamager() instanceof Player)) && 
					  (e.getDamager().isOp())) {
				  Player p = (Player)e.getDamager();
				  	if (p.getItemInHand().getType() == Material.WOOD_AXE) {
				  		villager.damage(100.0D);
				  			p.sendMessage("§4[§4§lX§4] §cNPC §e'arena Chain§e' §cremovido com êxito.");
				  			 
					    }
				    }
				}
			}
		}
	
	 @EventHandler
	 public void itemDrop(PlayerDropItemEvent e) {
		 Player p = e.getPlayer();
		 if (Commands.inArena.contains(p)) {
			 e.setCancelled(true);
		 }
	 }
	/*
	  @EventHandler
	  public void DamageOnVillager(EntityDamageByEntityEvent e) {
	    if (e.getEntity() instanceof NPC)
	      e.setCancelled(true); 
	    if (CitizensAPI.getNPCRegistry().isNPC(e.getEntity())) {
	      e.setCancelled(true);
	      return;
	  }
 }
 */
	  /*
	    @EventHandler
	    public void clickr(NPCRightClickEvent e) {
	    	Player p = e.getClicker();
	        if(Commands.dungeonNPC == e.getNPC()){
	  		      openInv(p);
	  		      e.setCancelled(true);
	  		      return;
	  	      }
	        }
	        */
	    
	    public void openInv(Player p) {
	    	Inventory inv = Bukkit.createInventory(null, 3*9, "§7§lEntrada/informações");
			
			ItemStack espada = new ItemStack(Material.IRON_SWORD);
			ItemMeta metaespada = espada.getItemMeta();
			metaespada.setDisplayName("§eEntrar na arena");
			ArrayList<String> espadalore = new ArrayList<>();
			espadalore.add("");
			espadalore.add("§fJogadores na arena: §e" + Commands.inArena.size());
			espadalore.add("§fclique para entrar.");
			metaespada.setLore(espadalore);
			espada.setItemMeta(metaespada);
			inv.setItem(13, espada);
			
			ItemStack papel = new ItemStack(Material.PAPER);
			ItemMeta metapapel = papel.getItemMeta();
			metapapel.setDisplayName("§eInformações");
			ArrayList<String> lore = new ArrayList<>();
			
			lore.add(" ");
			lore.add("§fNesta arena, não é preciso levar itens,");
			lore.add("§fpor padrão, os jogadores possuem os mesmos itens.");
			lore.add("§fÉ permitido times/alianças.");
			lore.add("§6* §e/chain entrar");
			lore.add(" ");

			metapapel.setLore(lore);
			papel.setItemMeta(metapapel);
			inv.setItem(18, papel);
			
			p.openInventory(inv);
	    }
	
	    final Main instance = Main.getPlugin();
	    
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
	    
	    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
	    private static final int[] NUMBERS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
	    
	    public static String romanFormat(int number) {
	      for (int i = 0; i < NUMBERS.length; i++) {
	        if (number >= NUMBERS[i]) {
	          return SYMBOLS[i] + romanFormat(number - NUMBERS[i]);
	        }
	      }
	      return "";
	    }
	    
	    public static int unnumural(String number) {
	      for (int i = 0; i < SYMBOLS.length; i++) {
	        if (number.startsWith(SYMBOLS[i])) {
	          return NUMBERS[i] + unnumural(number.replaceFirst(SYMBOLS[i], ""));
	        }
	      }
	      return 0;
	    }
	    
	
	/*
	 * 		if (e.getEntity() instanceof Player) {
			if (e.getEntity().getKiller() instanceof Player) {
				if (Commands.inArena.contains(e.getEntity())) {
					if (Commands.inArena.contains(e.getEntity().getKiller())) {
						ActionBarAPI.sendActionBarMessage(e.getEntity().getKiller(), "§eVocê matou §6" +e.getEntity()+ " §ee recebeu uma §6maçã dourada§e.");
							ItemStack item = new ItemStack(Material.GOLDEN_APPLE, 1);	
							ItemMeta itemmeta = item.getItemMeta();
							itemmeta.setDisplayName("§eChain");
							item.setItemMeta(itemmeta);
								e.getEntity().getInventory().addItem(item);
									Commands.inArena.remove(e.getEntity());
	 */
		
	
	
	
/*	
	@EventHandler
	public void commandProcess(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
			if (Commands.inArena.contains(p)) {
				if (!(p.hasPermission("zs.gerente"))) {
					if (Main.getPlugin().getConfig().getList("Comandos-permitidos:") != null) {
						String commands = e.getMessage();
						if (Main.getPlugin().getConfig().getList("Comandos-permitidos:").contains(commands)) {
							e.setCancelled(true);
								p.sendMessage("§e§l[§6§lCHAIN§e§l] §cEste comando está proibido dentro da arena CHAIN.");
						} else {
							e.setCancelled(false);
						}
					}
				
				}
				
			}
	}
// if (!(e.getMessage().startsWith("/chain")) || (!(e.getMessage().startsWith("/g")) || (!(e.getMessage().startsWith("/report"))))) {
*/
}
