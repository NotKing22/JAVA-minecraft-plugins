package me.zsnow.spookyrealm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zsnow.spookyrealm.api.ItemBuilder;
import me.zsnow.spookyrealm.api.LocationAPI;
import me.zsnow.spookyrealm.api.LocationAPI.location;
import me.zsnow.spookyrealm.api.NBTItemStack;
import me.zsnow.spookyrealm.api.Particles;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class Listeners implements Listener {

	String storeMenu = "Halloween Shop";
	private int pricePriceSwordS8 = 20;	private int priceSwordLoot = 25; private int pricePickaxe10 = 30; private int priceCandy = 3; 
	private int priceEmberPickaxe = 20; private int priceCarminitaSword = 30; private int priceHelmetP8 = 10;
	private int priceChestplateP8 = 10; private int priceLeggingsP8 = 10; private int priceBootsP8 = 10; //private int priceExplosive = 15;
	
	
	public void openShop(Player p) {
		////getSwordS8  getSwordSLoot  getPickaxe
		Inventory menu = Bukkit.createInventory(null, 5*9, storeMenu);
		//menu.setItem(11, getCaixa());
		//menu.setItem(10, getMatadora());
		menu.setItem(11, addPrice(getSwordS8(), pricePriceSwordS8, p));
		menu.setItem(12, addPrice(getSwordSLoot(), priceSwordLoot, p));
		menu.setItem(13, addPrice(getPickaxe(), pricePickaxe10, p));
		menu.setItem(14, addPrice(getCandy(5), priceCandy, p));
		menu.setItem(15, addPrice(getEmberPickaxe(), priceEmberPickaxe, p));
		menu.setItem(16, addPrice(getCarminitaSword(), priceCarminitaSword, p));
		menu.setItem(20, addPrice(getHelmetP8(), priceHelmetP8, p));
		menu.setItem(21, addPrice(getChestplateP8(), priceChestplateP8, p));
		menu.setItem(22, addPrice(getLegginsP8(), priceLeggingsP8, p));
		menu.setItem(23, addPrice(getBootsP8(), priceBootsP8, p));
		p.openInventory(menu);
	}
	
	public ItemStack book() {
		return new ItemBuilder(Material.BOOK).displayname("Livro aqui").lore("").build();
	}
	
	  @EventHandler
	  public void entityDeath(EntityDeathEvent e) {
		  if (e.getEntity().getKiller() != null && (!(e.getEntity() instanceof Player))) {
			  final Player assassino = (Player) e.getEntity().getKiller();
			  if (e.getEntity().hasMetadata("vilarejo")) {
					e.getDrops().clear();
					int randomChance = ThreadLocalRandom.current().nextInt(100);
					int randomDrop = ThreadLocalRandom.current().nextInt(3); // 1 amais
					if (randomDrop == 0) {
						randomDrop++;
					}
					if (randomChance <= 20) { // probabilidade de dropar
						assassino.sendMessage("✬ §bVocê coletou um §6§lH§E§lY§6§lP§e§lE §6§lC§e§lA§6§lN§e§lD§6§lY§b");
						assassino.sendMessage("§bUma gostosura ou uma travessura?");
						for (int i = 0; i < randomDrop; i++) {
							assassino.getInventory().addItem(getCandyMethod());
							assassino.playSound(assassino.getLocation(), Sound.ITEM_PICKUP, 0.5F, 0.5F);
						}
						return;
					}
					if (randomChance <= 30) { // probabilidade de dropar
						assassino.sendMessage("§6✠ §eA essência do mal se materializa. Você coletou §d" + randomDrop + "x §5Fragmentos fantasmagóricos.");
						for (int i = 0; i < randomDrop; i++) {
							assassino.getInventory().addItem(getFragmentoMethod());
							assassino.playSound(assassino.getLocation(), Sound.ITEM_PICKUP, 0.5F, 0.5F);
						}
						return;
					}
		  		}
		  }
	  }
	  
		@EventHandler
		public void onUse(PlayerInteractEvent event) {
		    final Player player = event.getPlayer();
		    if (player.getItemInHand().isSimilar(getCandyMethod()) && (event.getAction() == Action.RIGHT_CLICK_AIR 
		    		|| event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
		    	event.setCancelled(true);
		    		ItemStack item = player.getItemInHand();
			    		if (item.getAmount() > 1) {
		                  item.setAmount(item.getAmount() - 1);
		                } else {
		                  player.setItemInHand(null);
				        }
			    		int randomChance = ThreadLocalRandom.current().nextInt(100);
			    		if (randomChance >= 90) { // chance maior que 80
			    			Particles packet = new Particles(EnumParticle.VILLAGER_ANGRY, player.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 8);
			    			int randomEffect = ThreadLocalRandom.current().nextInt(5);
			    			int segundos1, amplificador1, segundos2, amplificador2;
				    		player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
			    			player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 1.0f, 0.5f);
		    			switch (randomEffect) {
							case 0:
			    				amplificador1 = 3; amplificador2 = 2;
			    				segundos1 = 20; segundos2 = 30;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, segundos2*20, amplificador2-1));
								break;
							case 1:
			    				amplificador1 = 5; amplificador2 = 2;
			    				segundos1 = 30; segundos2 = 20;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, segundos2*20, amplificador2-1));
				    			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
								break;
							case 2:
			    				amplificador1 = 5; amplificador2 = 2;
			    				segundos1 = 30; segundos2 = 30;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, segundos2*20, amplificador2-1));
				    			player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
								break;
							case 3:
			    				amplificador1 = 2; amplificador2 = 2;
			    				segundos1 = 30; segundos2 = 30;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, segundos2*20, amplificador2-1));
				    			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
								break;
							case 4:
			    				amplificador1 = 2; amplificador2 = 2;
			    				segundos1 = 25; segundos2 = 25;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, segundos2*20, amplificador2-1));
								break;
							
							default:
								break;
							}
		    			packet.sendToAll();
		    			player.sendMessage("§c§l(⬇) §e§lHYPE CANDY: §cVocê comeu um doce estragado.");
		    			return;
			    		}
			    		if (randomChance <= 90) { // chance menor que 80
			    			int randomEffect = ThreadLocalRandom.current().nextInt(5);
			    			int segundos1, amplificador1, segundos2, amplificador2;
				    		 Particles packet = new Particles(EnumParticle.HEART, player.getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 8);
				    			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.5f);
				    			player.playSound(player.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
		    			switch (randomEffect) {
							case 0:
			    				amplificador1 = 3; amplificador2 = 3;
			    				segundos1 = 60; segundos2 = 60;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, segundos2*20, amplificador2-1));
								break;
							case 1:
			    				amplificador1 = 3; amplificador2 = 3;
			    				segundos1 = 60; segundos2 = 60;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, segundos2*20, amplificador2-1));
								break;
							case 2:
			    				amplificador1 = 2; amplificador2 = 2;
			    				segundos1 = 60; segundos2 = 60;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, segundos2*20, amplificador2-1));
								break;
							case 3:
			    				amplificador1 = 2; amplificador2 = 4;
			    				segundos1 = 60; segundos2 = 30;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, segundos2*20, amplificador2-1));
								break;
							case 4:
			    				amplificador1 = 2; amplificador2 = 2;
			    				segundos1 = 60; segundos2 = 60;
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, segundos1*20, amplificador1-1));
				    			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, segundos2*20, amplificador2-1));
								break;
							
							default:
								break;
							}
		    			packet.sendToAll();
		    			player.sendMessage("§3§l(✚) §e§lHYPE CANDY: §6Você comeu um doce abençoado.");
			    		}
				    }
				}
		
		 @EventHandler
		    public void invClick(InventoryClickEvent e){
				if (!(e.getWhoClicked() instanceof Player)) {
				      return;
				}
				 Player p = (Player) e.getWhoClicked();
				if (e.getInventory().getTitle().equals("Caminho para o Vilarejo")) {
					e.setCancelled(true);
		            ItemStack currentItem = e.getCurrentItem();
		            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
		            	if (currentItem.getItemMeta().getDisplayName().contains("§aVilarejo assombrado")) {
		            		if (Commands.entrada) {
		            			try {
		            				LocationAPI.getLocation().teleportTo(p, location.ENTRADA);
								} catch (IllegalArgumentException e2) {
									Bukkit.getConsoleSender().sendMessage("§c[Vilarejo] A localizacao do vilarejo nao esta definida.");
									p.sendMessage("§cHouve um erro ao acessar o vilarejo. Contacte um Administrador.");
								}
		            			return;
		            		}
		            		p.sendMessage("§cO vilarejo está protegido, volte mais tarde quando ele estiver sendo atacado.");
		            		return;
		            	}
		            }
				}
				if (e.getInventory().getTitle().equals(storeMenu)) {
					e.setCancelled(true);
					 ItemStack currentItem = e.getCurrentItem();
					 if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && e.getSlot() >= 11) {
					switch (e.getSlot()) {
						case 11:
		        	    	if (hasFragmentos(p, pricePriceSwordS8)) {
		        	    		buyItem(p, pricePriceSwordS8, getSwordS8());
		        	    		return;
		        	    	}
							break;
						case 12:
		        	    	if (hasFragmentos(p, priceSwordLoot)) {
		        	    		buyItem(p, priceSwordLoot, getSwordSLoot());
		        	    		return;
		        	    	}
							break;
						case 13:
		        	    	if (hasFragmentos(p, pricePickaxe10)) {
		        	    		buyItem(p, pricePickaxe10, getPickaxe());
		        	    		return;
		        	    	}
							break;
						case 14:
		        	    	if (hasFragmentos(p, priceCandy)) {
		        	    		buyItem(p, priceCandy, getCandy(5));
		        	    		return;
		        	    	}
							break;
						case 15:
		        	    	if (hasFragmentos(p, priceEmberPickaxe)) {
		        	    		buyItem(p, priceEmberPickaxe, getEmberPickaxe());
		        	    		return;
		        	    	}
							break;
						case 16:
		        	    	if (hasFragmentos(p, priceCarminitaSword)) {
		        	    		buyItem(p, priceCarminitaSword, getCarminitaSword());
		        	    		return;
		        	    	}
							break;
						case 20:
		        	    	if (hasFragmentos(p, priceHelmetP8)) {
		        	    		buyItem(p, priceHelmetP8, getHelmetP8());
		        	    		return;
		        	    	}
							break;
						case 21:
		        	    	if (hasFragmentos(p, priceChestplateP8)) {
		        	    		buyItem(p, priceChestplateP8, getChestplateP8());
		        	    		return;
		        	    	}
							break;
						case 22:
		        	    	if (hasFragmentos(p, priceLeggingsP8)) {
		        	    		buyItem(p, priceLeggingsP8, getLegginsP8());
		        	    		return;
		        	    	}
							break;
						case 23:
		        	    	if (hasFragmentos(p, priceBootsP8)) {
		        	    		buyItem(p, priceBootsP8, getBootsP8());
		        	    		return;
		        	    	}
							break;
					default:
						break;
					}
					p.sendMessage("§cFragmentos insuficientes para resgatar o item selecionado.");
					 }
				}
		 }
		 
			public static ItemStack getEmberPickaxe() {
				ItemStack Ember = new ItemStack(Material.IRON_PICKAXE);
				ItemMeta metaEmber = Ember.getItemMeta();
				metaEmber.setDisplayName("§bPicareta de Ember");
				metaEmber.addEnchant(Enchantment.DURABILITY, 4, true);
				metaEmber.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 4, true);
				metaEmber.setLore(Arrays.asList(new String[] { 
						"§7§lExplosive I",
						"",
						"§d§lITEM EXCLUSIVO DO DESAFIO"
						}));
				Ember.setItemMeta(metaEmber);
				Ember.addUnsafeEnchantment(Enchantment.DIG_SPEED, 8);
				
				NBTItemStack nbtItemStack = new NBTItemStack(Ember);
				nbtItemStack.setInteger("value", 322); 
				
				return nbtItemStack.getItem();
			}
		 
		 public static ItemStack getCarminitaSword() {

				ItemStack item = new ItemStack(Material.GOLD_SWORD);
				 item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
				 item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
				ItemMeta metaItem = item.getItemMeta();
				metaItem.setDisplayName("§bEspada de Carminita");
				metaItem.setLore(Arrays.asList(new String[] {
						"",
						"§6Habilidade do item: §e§lASPECTO DEMONITA CORRUPTO",
						"§7Uma arma forjada com um minério de carminita",
						"§7que parece possuir um §6aspecto flamejante §7superior",
						"§7ao convencional, capaz até de sobrepor a resistência ao fogo.",
						"",
						"  §e§lℹ §7Aumenta §3ligeiramente §7a chance de fazer seu inimigo queimar.",
						"",
						"§e✉ §a'Uma marca permanente de uma obra-prima",
						"§aque transcende toda a existência humana.",
						"§aEmana assustadoramente uma sede por sangue.'",
						"",
						"§9+9 Dano de ataque", 
						"§9+6,5 Penetração de armadura",
						"§9Durabilidade desconhecida",
						"§9Dano final: §l15,25",
						"§8[❂] [⚔]",
						"",
						"§d§lITEM EXCLUSIVO DO DESAFIO"
				}));
				
				metaItem.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
				metaItem.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				metaItem.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
				item.setItemMeta(metaItem);
				item.getItemMeta().spigot().setUnbreakable(true);
				
				NBTItemStack nbtItemStack = new NBTItemStack(item);
				nbtItemStack.setInteger("value", 1334); 
				
				return nbtItemStack.getItem();
			}
		 
		public ItemStack getCandyMethod() {
			ItemStack candy = new ItemBuilder(Material.NETHER_STALK).displayname("§6§lH§E§lY§6§lP§e§lE §6§lC§e§lA§6§lN§e§lD§6§lY")
					.lore(Arrays.asList(new String[] {
							"  §8Um doce carmesin contido em uma embalhagem velha.",
							"§8Parece possuir um soro brilhante com ícones sagrados;",
							"§8aparentemente pode causar alguns efeitos quando consumidos.",
							" ",
							"§6Item habilidade: §d§lDOCE INCONSTANTE",
							"",
							" §e§lℹ §7Aplica um efeito §3positivo§7 quando consumido,",
							"  §7sem garantias de que não haja efeitos colaterais.",
							"",
							"§6§lHALLOWEEN EDITION"})).build();
			ItemMeta metaCandy = candy.getItemMeta();
			metaCandy.addEnchant(Enchantment.DURABILITY, 1, true);
			metaCandy.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			candy.setItemMeta(metaCandy);
			
			NBTItemStack nbtItemStack = new NBTItemStack(candy);
			nbtItemStack.setInteger("value", 2002); 
			
			return nbtItemStack.getItem();
		}	
		
	public ItemStack getCandy(int quantidade) {
		ItemStack candy = new ItemBuilder(Material.NETHER_STALK).displayname("§6§lH§E§lY§6§lP§e§lE §6§lC§e§lA§6§lN§e§lD§6§lY")
				.lore(Arrays.asList(new String[] {
						"  §8Um doce carmesin contido em uma embalhagem velha.",
						"§8Parece possuir um soro brilhante com ícones sagrados;",
						"§8aparentemente pode causar alguns efeitos quando consumidos.",
						" ",
						"§6Item habilidade: §d§lDOCE INCONSTANTE",
						"",
						" §e§lℹ §7Aplica um efeito §3positivo§7 quando consumido,",
						"  §7sem garantias de que não haja efeitos colaterais.",
						"",
						"§6§lHALLOWEEN EDITION"})).amount(quantidade).build();
		ItemMeta metaCandy = candy.getItemMeta();
		metaCandy.addEnchant(Enchantment.DURABILITY, 1, true);
		metaCandy.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		candy.setItemMeta(metaCandy);
		
		NBTItemStack nbtItemStack = new NBTItemStack(candy);
		nbtItemStack.setInteger("value", 2002); 
		
		return nbtItemStack.getItem();
	}
	
	public ItemStack getFragmentoMethod() {
		ItemStack frag = new ItemBuilder(Material.QUARTZ).displayname("§3Fragmento fantasmagórico")
				.lore(Arrays.asList(new String[] {
						"  §e✉ §8A essência de uma criatura desconhecida se materializa.",
						"§8Homens engenhosos devem crer em bruxas e aparições;",
						"§8se eles duvidavam da realidade dos espíritos,",
						"§8eles não só negavam demônios, mas",
						"§8também o Deus todo-poderoso.",
						" ",
						" §e§lℹ §7Entregue esse fragmento ao §f☃ Gasparzinho §7para",
						"  §7ajudar a expurgar o mal do mundo, e em troca, receba",
						"  §7sua recompensa de halloween.",
						"",
						"§6§lHALLOWEEN EDITION"})).build();
		ItemMeta metaFrag = frag.getItemMeta();
		metaFrag.addEnchant(Enchantment.DURABILITY, 1, true);
		metaFrag.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		frag.setItemMeta(metaFrag);
		
		NBTItemStack nbtItemStack = new NBTItemStack(frag);
		nbtItemStack.setInteger("value", 2001); 
		
		return nbtItemStack.getItem();
	}
	
	public ItemStack getFragmento(int quantidade) {
		ItemStack frag = new ItemBuilder(Material.QUARTZ).displayname("§3Fragmento fantasmagórico")
				.lore(Arrays.asList(new String[] {
						"  §e✉ §8A essência de uma criatura desconhecida se materializa.",
						"§8Homens engenhosos devem crer em bruxas e aparições;",
						"§8se eles duvidavam da realidade dos espíritos,",
						"§8eles não só negavam demônios, mas",
						"§8também o Deus todo-poderoso.",
						" ",
						" §e§lℹ §7Entregue esse fragmento ao §f☃ Gasparzinho §7para",
						"  §7ajudar a expurgar o mal do mundo, e em troca, receba",
						"  §7sua recompensa de halloween.",
						"",
						"§6§lHALLOWEEN EDITION"})).amount(quantidade).build();
		ItemMeta metaFrag = frag.getItemMeta();
		metaFrag.addEnchant(Enchantment.DURABILITY, 1, true);
		metaFrag.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		frag.setItemMeta(metaFrag);
		
		NBTItemStack nbtItemStack = new NBTItemStack(frag);
		nbtItemStack.setInteger("value", 2001); 
		
		return nbtItemStack.getItem();
	}

	public ItemStack getSwordS8() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD).displayname("§6Espada de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""}))
				.enchant(Enchantment.DAMAGE_ALL, 8)
				.enchant(Enchantment.DURABILITY, 7)
				.enchant(Enchantment.FIRE_ASPECT, 3).build();
		item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
		item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
		return item;
	}
	
	public ItemStack getSwordSLoot() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_SWORD).displayname("§6Espada de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
		item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 4);
		item.addUnsafeEnchantment(Enchantment.DAMAGE_UNDEAD, 4);
		item.addUnsafeEnchantment(Enchantment.DAMAGE_ARTHROPODS, 3);
		return item;
	}
	
	public ItemStack getPickaxe() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_PICKAXE).displayname("§6Picareta de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
		item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
		return item;
	}
	
	public ItemStack getHelmetP8() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_HELMET).displayname("§6Armadura de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 8);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
		return item;
	}
	
	public ItemStack getChestplateP8() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_CHESTPLATE).displayname("§6Armadura de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 8);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
		return item;
	}
	
	public ItemStack getLegginsP8() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_LEGGINGS).displayname("§6Armadura de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 8);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
		return item;
	}
	
	public ItemStack getBootsP8() {
		ItemStack item = new ItemBuilder(Material.DIAMOND_BOOTS).displayname("§6Armadura de Halloween").lore(Arrays.asList(new String[] {
				"",
				"§eUm exclusivo do Halloween. Item fornecido aos", 
				"§eguerreiros que protegeram o mundo da destruição.",
				""})).build();
		item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 8);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
		return item;
	}
	
	final private static String moeda = "⛃";
	
	public ItemStack addPrice(ItemStack item, int price, Player buyer) {
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(metaItem.getDisplayName() + " §f(" + price + "§f Fragmentos)");
		ArrayList<String> oldLore = new ArrayList<>();
		oldLore.addAll(metaItem.getLore());
		oldLore.add("");
		if (hasFragmentos(buyer, price)) {
			oldLore.add("§a "+moeda+" §aClique para comprar!");
		} else {
			oldLore.add("§c "+moeda+" §cSaldo insuficiente para comprar");
		}
		metaItem.setLore(oldLore);
		item.setItemMeta(metaItem);
		return item;
	}
	
	public void buyItem(Player p, int valor, ItemStack buyedItem) {
		int quantidade = 0;
	    int a = 0;
	    List<Integer> slots2 = new ArrayList<Integer>();
	    for (ItemStack item : p.getInventory()) {
	        if (item != null)
	            if (item.getType() == getFragmentoMethod().getType()) {
	        		
	            	NBTItemStack nbtItemStack = new NBTItemStack(getFragmentoMethod());
	            	
	            	if (nbtItemStack.getItem().isSimilar(item) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(getFragmentoMethod().getItemMeta().getDisplayName()) && item.getItemMeta().hasLore()) {
	            	quantidade += item.getAmount();
	                slots2.add(a);
	            	} //
	            }
	         a++;
	    }
	
	    if (quantidade >= valor) {
	        if (p.getInventory().firstEmpty() == -1) {
	        	p.sendMessage("§c§lVocê não possue espaço no seu inventário.");
	        	return;
	        }
	        int quantidade2 = 0;
	        for (int slot : slots2) {
	            quantidade2 += p.getInventory().getItem(slot).getAmount();
	            if (quantidade2 > valor) {
	                p.getInventory().getItem(slot).setAmount(quantidade2 - valor);
	                break;
	            } else
	                p.getInventory().setItem(slot, new ItemStack(Material.AIR));
	        }
	        p.getInventory().addItem(buyedItem);
	        p.closeInventory();
	        openShop(p);
	        p.sendMessage("[Gasparzinho] Obrigado por ajudar a expurgar o mal!");
	    } else {
	    	p.sendMessage("§cVocê não possui fragmentos o suficiente para adquirir este item.");
	    }
	}
	
	public boolean hasFragmentos(Player p, int price) {
		int quantidade = 0;
	    int a = 0;
	    List<Integer> slots2 = new ArrayList<Integer>();
	    for (ItemStack item : p.getInventory()) {
	        if (item != null)
	            if (item.getType() == getFragmentoMethod().getType()) {
	        		
	            	NBTItemStack nbtItemStack = new NBTItemStack(getFragmentoMethod());
	            	
	            	if (nbtItemStack.getItem().isSimilar(item) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(getFragmentoMethod().getItemMeta().getDisplayName()) && item.getItemMeta().hasLore()) {
	            	quantidade += item.getAmount();
	                slots2.add(a);
	            	} //
	            }
	         a++;
	    }
	    if (quantidade >= price) {
	    	return true;
	    } else {
	    	return false;
	    }
	}
	
	
	public ItemStack getMatadora() {
		return new ItemBuilder(Material.WOOD_SWORD).displayname("§eMatadora de Bosses").lore(Arrays.asList(new String[] {
				"§cDano X",
				"",
				"§a§lHALLOWEEN EDITION"
		})).build();
	}
	
	public ItemStack getCaixa() {
		return new ItemBuilder(Material.PUMPKIN).displayname("§6Caixa de Halloween").build();
	}
	
	public ItemStack getBoosterMachine() {
		return new ItemBuilder(Material.EXP_BOTTLE).displayname("§aBooster de máquinas").lore("§fBooster de X minutos").build();
	}
	
	public ItemStack getBoosterSpawner() {
		return new ItemBuilder(Material.EXP_BOTTLE).displayname("§aBooster de Spawners").lore("§fBooster de X minutos").build();
	}
	
}
