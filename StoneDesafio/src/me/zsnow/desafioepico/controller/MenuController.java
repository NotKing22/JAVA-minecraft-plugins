package me.zsnow.desafioepico.controller;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.plotsquared.bukkit.util.SendChunk;

import me.zsnow.desafioepico.Main;
import me.zsnow.desafioepico.configAPI.BossBar;
import me.zsnow.desafioepico.configAPI.Configs;
import me.zsnow.desafioepico.configAPI.LocationAPI;
import me.zsnow.desafioepico.configAPI.NBTItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

@SuppressWarnings("unused")
public class MenuController implements Listener {
	
	 String accessMenu = "§8» §d§lDESAFIO ÉPICO";
	 
	 final String repairMenu = "§8» §d§lDESAFIO ÉPICO §7- Fix";
	 final String storeMenu = "§8» §d§lDESAFIO ÉPICO §7- Loja";
	 final String netherInfos = "§8» §d§lNether §7- informações";
	 final String confirmation1 = "§8» §d§lDESAFIO ÉPICO §7- Nether";
	 final String confirmation2 = "§8» §d§lDESAFIO ÉPICO §7- Éden";
	 final String storeItem = "§6➣ §5§lLoja épica";
	 final String fireballItem = "§4§lInvasão ao Nether";
	 
	 final String getCraftItemMenuNether = "§b§lTODOS OS CRAFTS DO NETHER";
	 
	 final String repairItemName = "§6Ω Reparador Místico Ω";
	 final static String repairAnvilClickName = "§aClique para reparar!";
	 final static String woolRepairClickCancelName = "§cClique aqui para cancelar.";
	 
	 final String carminitaBarName = "§6Ω Minério de Carminita Ω";
	 final String getEmberCristalName = "§bCristal de Ember";
	 final String getEmberStickName = "§bCabo de Ember";
	 final static String carminitaSwordName = "§bEspada de Carminita";
	 final static String getEmberPickaxeName = "§bPicareta de Ember";
	 final String getCarminitaSpadeName = "§bPá de Carminita";
	 final String getCarminitaEmpunhaduraName = "§6Ω Empunhadura de Carminita Ω";
	 
	 final String edenItem = "§b§lInvasão ao Éden";
	 final String arrowToBack = "§cRetornar página";
	 
	  int CARMINITA_SWORD_PRICE = Configs.config.getConfig().getInt("carminita_espada");
	  int EMBER_PICKAXE_PRICE = Configs.config.getConfig().getInt("picareta_ember");
	  int CARMINITA_SPADE = Configs.config.getConfig().getInt("carminita_pa");
	  int CARMINITA_EMPUNHADURA_PRICE = Configs.config.getConfig().getInt("carminita_empunhadura");
	  int EMBER_CABO_PRICE = Configs.config.getConfig().getInt("ember_cabo");
	  int CARMINITA_BAR_PRICE = Configs.config.getConfig().getInt("carminita_barra");
	  int EMBER_CRISTAL_PRICE = Configs.config.getConfig().getInt("ember_cristal");
	
	public void openInventory(Player p) {
		Inventory menu1 = Bukkit.createInventory(null, 3*9, accessMenu);
			for (int slot = 0; menu1.getSize() > slot; slot++) {
				menu1.setItem(slot, getGlass());
			}
			menu1.setItem(4, addProtection(getBookInfo()));
			menu1.setItem(14, addProtection(getFireball()));
			menu1.setItem(13, addProtection(repairItem()));
			menu1.setItem(12, addProtection(ComingSon()));
			menu1.setItem(22, addProtection(getEpicStorage()));
			if (EventController.admin.getNetherParticipantes().contains(p)) {
				menu1.setItem(18, addProtection(Saida()));
			}
			p.openInventory(menu1);
			return;
	}
	
	public void openInventoryNetherInfos(Player p) {
		 Inventory menu = Bukkit.createInventory(null, 3*9, netherInfos);
			for (int slot = 0; menu.getSize() > slot; slot++) {
				menu.setItem(slot, addProtection(getGlass()));
			}
		menu.setItem(11, addProtection(ComingSon()));
		menu.setItem(15, addProtection(getCraftsNetherItem()));
		menu.setItem(18, addProtection(getArrowToBack()));
		p.openInventory(menu);
	}
	
	public void openInventoryCraftsNether(Player p) {
		Inventory menu = Bukkit.createInventory(null, 3*9, getCraftItemMenuNether);
		menu.setItem(11, addProtection(getCarminitaSword()));
		menu.setItem(12, addProtection(getEmberPickaxe()));
		menu.setItem(13, addProtection(getCarminitaSpade()));
		menu.setItem(18, addProtection(getArrowToBack()));
		p.openInventory(menu);
	}
	
	public void openRepairMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 4*9, repairMenu);
		
		 menu.setItem(13, addProtection(p.getItemInHand()));
		 menu.setItem(20, addProtection(item1(p)));
		 menu.setItem(24, addProtection(item2()));
		 menu.setItem(27, addProtection(item3()));
		p.openInventory(menu);
	}
	
	
	public void openStoreMenu(Player p) {
		Inventory menu = Bukkit.createInventory(null, 4*9, storeMenu);
		
		if (CARMINITA_SWORD_PRICE != 0) {
		menu.setItem(11, addPrice(addProtection(getCarminitaSword()), CARMINITA_SWORD_PRICE));
		menu.setItem(12, addPrice(addProtection(getEmberPickaxe()), EMBER_PICKAXE_PRICE));
		menu.setItem(13, addPrice(addProtection(getCarminitaSpade()), CARMINITA_SPADE));
		menu.setItem(20, addPrice(addProtection(getCarminitaBar()), CARMINITA_BAR_PRICE));
		menu.setItem(22, addPrice(addProtection(getEmberStick()), EMBER_CABO_PRICE));
		menu.setItem(21, addPrice(addProtection(getCarminitaEmpunhadura()), CARMINITA_EMPUNHADURA_PRICE));
		menu.setItem(23, addPrice(addProtection(getCristalEmber()), EMBER_CRISTAL_PRICE));
		} else {
			menu.setItem(11, addProtection(ComingSon()));
			menu.setItem(12, addProtection(ComingSon()));
			menu.setItem(13, addProtection(ComingSon()));
			menu.setItem(20, addProtection(ComingSon()));
			menu.setItem(21, addProtection(ComingSon()));
			menu.setItem(22, addProtection(ComingSon()));
			menu.setItem(23, addProtection(ComingSon()));
			
		}
		menu.setItem(27, addProtection(getArrowToBack()));
		// 20 pra frente
		p.openInventory(menu);
	}
	
	public ItemStack addPrice(ItemStack item, int price) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<>();
		if (meta.hasLore()) lore.addAll(meta.getLore());
		String novaLinha = "";
		novaLinha += "§eCusto: §f" + price + " moedas";
		lore.add(" ");
		lore.add(novaLinha);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	
	public void showCarminitaSwordCraft(Player p) {
		Inventory menu = Bukkit.createInventory(null, 5*9, getCraftItemMenuNether);
		makeCraftingFormat(menu);
		menu.setItem(25, addProtection(getCarminitaSword()));
		menu.setItem(11, addProtection(getCarminitaBar()));
		menu.setItem(20, addProtection(getCarminitaBar()));
		menu.setItem(29, addProtection(getCarminitaEmpunhadura()));
		
		p.openInventory(menu);
	}
			
	public void showCarminitaSpadeCraft(Player p) {
		Inventory menu = Bukkit.createInventory(null, 5*9, getCraftItemMenuNether);
		makeCraftingFormat(menu);
		menu.setItem(25, addProtection(getCarminitaSpade()));
		menu.setItem(11, addProtection(getCarminitaBar()));
		menu.setItem(20, addProtection(getCarminitaEmpunhadura()));
		menu.setItem(29, addProtection(getCarminitaEmpunhadura()));
		
		p.openInventory(menu);
	}		
	
	public void showEmberPickaxeCraft(Player p) {
		Inventory menu = Bukkit.createInventory(null, 5*9, getCraftItemMenuNether);
		makeCraftingFormat(menu);
		menu.setItem(25, addProtection(getEmberPickaxe()));
		menu.setItem(10, addProtection(getCristalEmber()));
		menu.setItem(11, addProtection(getCristalEmber()));
		menu.setItem(12, addProtection(getCristalEmber()));
		menu.setItem(29, addProtection(getEmberStick()));
		menu.setItem(20, addProtection(getEmberStick()));
		
		p.openInventory(menu);
	}
	
	public void makeCraftingFormat(Inventory inv) {
		for (int slot = 0; inv.getSize() > slot; slot++) {
			inv.setItem(slot, getGlass());
		}
		inv.setItem(10, new ItemStack(Material.AIR));
		inv.setItem(11, new ItemStack(Material.AIR));
		inv.setItem(12, new ItemStack(Material.AIR));
		inv.setItem(19, new ItemStack(Material.AIR));
		inv.setItem(20, new ItemStack(Material.AIR));
		inv.setItem(21, new ItemStack(Material.AIR));
		inv.setItem(28, new ItemStack(Material.AIR));
		inv.setItem(29, new ItemStack(Material.AIR));
		inv.setItem(30, new ItemStack(Material.AIR));
		inv.setItem(23, getArrowRight());
		// 25 o item final
		
	}
	
	public ItemStack ComingSon() {
		ItemStack barreira = new ItemStack(Material.BARRIER);
		ItemMeta metaBarreira = barreira.getItemMeta();
		metaBarreira.setDisplayName("§cEm breve");
		barreira.setItemMeta(metaBarreira);
		return barreira;
	}
	
	public ItemStack Saida() {
		ItemStack saida = new ItemStack(Material.DARK_OAK_DOOR_ITEM);
		ItemMeta metaSaida = saida.getItemMeta();
		metaSaida.setDisplayName("§eClique para sair");
		metaSaida.setLore(Arrays.asList(new String[] { "§cVocê não pode sair depois", "§cque a entrada for fechada.", "" }));
		saida.setItemMeta(metaSaida);
		return saida;
	}
	
	public ItemStack repairItem() {
		ItemStack bigorna = new ItemStack(Material.ANVIL);
		ItemMeta metaBigorna = bigorna.getItemMeta();
		metaBigorna.setDisplayName(repairItemName);
		metaBigorna.setLore(Arrays.asList(new String[] { " §fClique para reparar seus §fitens especiais.", "§cObs: §fEsteja com ele em mãos." }));
		bigorna.setItemMeta(metaBigorna);
		return bigorna;
	}
	
	public ItemStack addProtection(ItemStack item) {
		NBTItemStack nbtItemStack = new NBTItemStack(item);
		nbtItemStack.setInteger("protection", 666);
		return nbtItemStack.getItem();
	}
	
	  @EventHandler
	    public void invClick(InventoryClickEvent e){
			if (!(e.getWhoClicked() instanceof Player)) {
			      return;
			}
			 Player p = (Player) e.getWhoClicked();
			if (e.getInventory().getTitle().equals(repairMenu)) {
				e.setCancelled(true);
	            ItemStack currentItem = e.getCurrentItem();
	            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
	            	final Integer repararCusto = Configs.config.getConfig().getInt("reparar-custo");
	            	final Integer descontoRei =   repararCusto/100*Configs.config.getConfig().getInt("desconto.vip-rei");
	        		final Integer descontoDuque = repararCusto/100*Configs.config.getConfig().getInt("desconto.vip-duque");
	        	    final Integer descontoLorde = repararCusto/100*Configs.config.getConfig().getInt("desconto.vip-lorde");
	        	   
	        	    if (currentItem.getItemMeta().getDisplayName().equals(repairAnvilClickName)) {
	        	    	if (Main.getInstance().economy.has(p, repararCusto)) {
	        	    		if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR &&
	        	    				p.getItemInHand().getType() != Material.DOUBLE_PLANT) {
		        	    		ItemStack finalItemStack = p.getItemInHand();
			         		    NBTItemStack nbtFinalItemStack = new NBTItemStack(finalItemStack);
				         	    if (nbtFinalItemStack.getInteger("value") == 322) {
				         	    	if (p.getItemInHand().getDurability() == 0) {
				         	    		p.sendMessage("§cO item em sua mão já está reparado.");
				         	    		p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0f, 0.5f);
				         	    		p.closeInventory();
				         	    		return;
				         	    	}
				         	    	Main.getInstance().economy.withdrawPlayer(p, repararCusto);
				         	    	p.getItemInHand().setDurability((short)0);
				         	    	p.sendMessage("§6Ω O seu item especial foi reparado!");
				         	    	p.playSound(p.getLocation(), Sound.ANVIL_USE, 0.5f, 0.5f);
				         	    	p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 0.5f);
				         	    	p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.5f);
				         	    	p.closeInventory();
				         	    	return;
			         	    	} else {
			         	    		p.closeInventory();
			         	    		p.sendMessage("§cO item em sua mão já não é mais o mesmo.");
			         	    		return;
			         	    	}
			         	    } else {
			         	    	p.closeInventory();
			         	    	return;
			         	    }
	        	    	} else {
	        	    		p.closeInventory();
	        	    		p.sendMessage("§cVocê não possui dinheiro para realizar esta ação.");
	        	    		return;
	        	    	}
	        	    }
	        	    if (currentItem.getItemMeta().getDisplayName().equals(woolRepairClickCancelName)) {
	        	    	p.closeInventory();
	        	    	p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0f, 0.5f);
	        	    	return;
	        	    }
	            }
			}
	        if(e.getInventory().getTitle().equals(accessMenu)) {
	        	e.setCancelled(true);
	            ItemStack currentItem = e.getCurrentItem();
	            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
	            	if (currentItem.getItemMeta().getDisplayName().equals(fireballItem)) {
	            		if (e.getClick() == ClickType.LEFT) {
	            		if (EventController.admin.getNetherOcorrendo() == true && EventController.admin.getNetherEntradaLiberada() == true) {
	            			if (!EventController.admin.getNetherParticipantes().contains(p)) {
	            				if (p.getInventory().getBoots() != null
	            						&& p.getInventory().getChestplate() != null
	            						&& p.getInventory().getLeggings() != null &&
	            						p.getInventory().getBoots() != null && 
	            						p.getInventory().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= 4
	            					&& p.getInventory().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= 4
	            						&& p.getInventory().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= 4
	            							&& p.getInventory().getBoots().getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) >= 4) {
	            					
	            				p.sendMessage("§eVocê agora está participando do Desafio épico.");
	            				EventController.admin.getNetherParticipantes().add(p);
	            				LocationAPI.sendTo(p, "ENTRADA-NETHER");
	            				EventController.admin.playerCoinsNether.put(p, 0);
	            				if (BossBar.hasStatusBar(p)) {
	            					BossBar.removeStatusBar(p);
	            				}
	            				BossBar.setStatusBar(p, "§5§lObjetivo: §dMate os mobs e derrote o boss.", 100);
	            				} else {
	            					p.sendMessage("§cPara acessar o desafio épico é necessário pelo menos uma armadura completa com Proteção IV.");
	            				}
	            				return;
	            				
	            			} else {
	            				p.sendMessage("§cVocê já está no desafio.");
	            				return;
	            			}
		            		} else {
		            			p.sendMessage("§cO desafio épico não está ocorrendo ou sua entrada já está fechada.");
		            			return;
		            		}
		    			//menu1.setItem(14, getFireball());
		    			//menu1.setItem(12, getQuartz());
	            		}
	            		if (e.getClick() == ClickType.RIGHT) {
	            			openInventoryNetherInfos(p);
	            		}
	            	}
	            	if (currentItem.getItemMeta().getDisplayName().equals(repairItemName)) {
	            		if (p.getItemInHand() != null && p.getItemInHand().getType() != Material.AIR 
	            				&& p.getItemInHand().getType() != Material.DOUBLE_PLANT) {
	            		ItemStack finalItemStack = p.getItemInHand();
	         		    NBTItemStack nbtFinalItemStack = new NBTItemStack(finalItemStack);
	         		    	if (nbtFinalItemStack.getInteger("value") == 1334) {
	         		    		p.closeInventory();
	         		    		p.sendMessage("§cO seu item possui proteção contra indestrutibilidade, portanto não pode ser reparado.");
	         		    		return;
	         		    	}
			         	    if (nbtFinalItemStack.getInteger("value") == 322) {
			         	    	openRepairMenu(p);
			         	    	return;
			         	    } else {
			         	    	p.closeInventory();
			         	    	p.sendMessage("§cVocê não possui um item especial em mãos.");
			         	    	return;
			         	    }
	            		} else {
	            			p.closeInventory();
	            			p.sendMessage("§cVocê precisa está segurando um item especial para reparar.");
	            			return;
	            		}
	            	}
	            	if (currentItem.getType().equals(Material.DARK_OAK_DOOR_ITEM)) {
	            		if (EventController.admin.getNetherEntradaLiberada() == true) {
		            		Main.sendTo(p, "SAIDA-NETHER");
		            		EventController.admin.getNetherParticipantes().remove(p);
		            		BossBar.removeStatusBar(p);
		            		p.sendMessage("§aVocê saiu do desafio épico.");
		            		return;
	            		} else {
	            			p.sendMessage("§cVocê não pode sair enquanto o desafio ocorre.");
	            			return;
	            		}
	            	}
	            	if (currentItem.getItemMeta().getDisplayName().equals(storeItem)) {
	            		openStoreMenu(p);
	            	}
	            }
	        }
	        
	        if(e.getInventory().getTitle().equals(storeMenu)) {
	        	e.setCancelled(true);
	        	  ItemStack currentItem = e.getCurrentItem();
		            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
		            	if (currentItem.getItemMeta().getDisplayName().equals(carminitaSwordName)) {
		            		buyItem(p, CARMINITA_SWORD_PRICE, getCarminitaSword());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(getEmberPickaxeName)) {
		            		buyItem(p, EMBER_PICKAXE_PRICE, getEmberPickaxe());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(getCarminitaSpadeName)) {
		            		buyItem(p, CARMINITA_SPADE, getCarminitaSpade());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(getCarminitaEmpunhaduraName)) {
		            		buyItem(p, CARMINITA_EMPUNHADURA_PRICE, getCarminitaEmpunhadura());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(getEmberStickName)) {
		            		buyItem(p, EMBER_CABO_PRICE, getEmberStick());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(carminitaBarName)) {
		            		buyItem(p, CARMINITA_BAR_PRICE, getCarminitaBar());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(getEmberCristalName)) {
		            		buyItem(p, EMBER_CRISTAL_PRICE, getCristalEmber());
		            		p.updateInventory();
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals(arrowToBack)) {
		            		openInventory(p);
		            	}
	            	}
	        }
	        /*
	         * 		menu.setItem(11, addPrice(getCarminitaSword(), CARMINITA_SWORD_PRICE));
		menu.setItem(12, addPrice(getEmberPickaxe(), EMBER_PICKAXE_PRICE));
		menu.setItem(13, addPrice(getCarminitaSpade(), CARMINITA_SPADE));
		menu.setItem(20, addPrice(getCarminitaEmpunhadura(), CARMINITA_EMPUNHADURA_PRICE));
		menu.setItem(21, addPrice(getEmberStick(), EMBER_CABO_PRICE));
		menu.setItem(22, addPrice(getCarminitaBar(), CARMINITA_BAR_PRICE));
		menu.setItem(23, addPrice(getCristalEmber(), EMBER_CRISTAL_PRICE));
	         */
	        
	        // separa o craft do artefato
	        if(e.getInventory().getTitle().equals(netherInfos)) {
	        	e.setCancelled(true);
	            ItemStack currentItem = e.getCurrentItem();
	            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
	            	if (currentItem.getItemMeta().getDisplayName().equals(arrowToBack)) {
	            		openInventory(p);
	            	}
	            	if (currentItem.getItemMeta().getDisplayName().equals(getCraftItemMenuNether)) {
	            		openInventoryCraftsNether(p);
	            	}
	            }
	        }
	        // mostra os item craft
	        if (e.getInventory().getTitle().equals(getCraftItemMenuNether)) {
	        	e.setCancelled(true);
	        	ItemStack currentItem = e.getCurrentItem();
	        	if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
	        		if (currentItem.getItemMeta().getDisplayName().equals(arrowToBack)) {
	        			openInventoryNetherInfos(p);
	        		}
	        		if (currentItem.getItemMeta().getDisplayName().equals(carminitaSwordName)) {
	        			showCarminitaSwordCraft(p);
	        		}
	        		if (currentItem.getItemMeta().getDisplayName().equals(getEmberPickaxeName)) {
	        			showEmberPickaxeCraft(p);
	        		}
	        		if (currentItem.getItemMeta().getDisplayName().equals(getCarminitaSpadeName)) {
	        			showCarminitaSpadeCraft(p);
	        		}
	        	}
	        }
	        // mostra o craft
}

	
	public ItemStack getGlass() {
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
		return glass;
	}
	
	//manager.getEntrada == true ? "Sim" : "não";
	
	public ItemStack getFireball() {
		String configTime = Configs.config.getConfig().getString("Horario.Nether");
		
		SimpleDateFormat Dateformat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");
        Date data = new Date();
        
        String dateStart = Dateformat.format(data) + " " + Timeformat.format(data);
        String[] dateSplit = Dateformat.format(data).split("/");
        int dia = Integer.parseInt(dateSplit[0]);
        int mes = Integer.parseInt(dateSplit[1]);
        int ano = Integer.parseInt(dateSplit[2]);
        dia++;
        
        String dateStart2 = dia+"/"+mes+"/"+ano + " " + configTime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        
        long segundos = 0;
        long minutos = 0;
        long horas = 0;
        
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStart2);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            segundos = diff / 1000 % 60;
            minutos = diff / (60 * 1000) % 60;
            horas = diff / (60 * 60 * 1000) % 24;

        } catch (Exception e) {
            e.printStackTrace();
        }
        
		ItemStack fireball = new ItemStack(Material.FIREBALL);
		ItemMeta metaFire = fireball.getItemMeta();
		metaFire.setDisplayName(fireballItem);
		if (EventController.admin.getNetherOcorrendo() == false) {
				metaFire.setLore(Arrays.asList(new String[] { "", "§c§lStatus: §4§lFECHADO", 
						"§c§lInicia em: §4" + horas + "h " + minutos + "m " + segundos + "s", "", 
						"§7Clique com o §eesquerdo§7 para entrar.", 
						"§7Clique com o §edireito§7 para mais informações."}));
			} else {
				int minutes = EventController.admin.getNetherTempoDecorrido() / 60;
				int seconds = EventController.admin.getNetherTempoDecorrido() % 60; 
				
				String stringMinutes = (minutes < 10 ? "0" : "") + minutes;
				String stringSeconds = (seconds < 10 ? "0" : "") + seconds;
				DecimalFormat numberFormat = new DecimalFormat("#.#");
				metaFire.setLore(Arrays.asList(new String[] { "", EventController.admin.getNetherEntradaLiberada() == true ?
					"§c§lStatus: §a§lABERTO" : "§c§lStatus: §e§lEM ANDAMENTO", 
					"§c§lBoss HP: §4"+ EntityController.getNetherBossHP() + " "
							+ "§7["+ numberFormat.format(EntityController.getNetherBossHP()/Configs.config.getConfig().getDouble("NetherBoss.HP")*100)+"%]", 
					"§c§lMobs raros: §4ON", 
					"§c§lMobs bonus: §4ON", 
					"§c§lDuração máxima do evento: §430 minutos.", 
					"  §7(Tempo decorrido §f"+ stringMinutes +"§7m §f" +stringSeconds+ "§7s)",
					"§c§lJogadores: §4" + EventController.admin.getNetherParticipantes().size() + ""}));
			}
		fireball.setItemMeta(metaFire);
		return fireball;
	}
	
	private ItemStack getQuartz() {
		ItemStack quartz = new ItemStack(Material.QUARTZ);
		ItemMeta metaQuartz = quartz.getItemMeta();
		metaQuartz.setDisplayName(edenItem);
		if (EventController.admin.getEdenOcorrendo() == false) {
				metaQuartz.setLore(Arrays.asList(new String[] { "", "§b§lStatus: §c§l[@boolean.status]", "§b§lInicia em: §f[@time.format]", ""}));
			} else {
				metaQuartz.setLore(Arrays.asList(new String[] { "", "§b§lStatus: §a§l[@boolean.status]", 
					"§b§lBoss HP: §f+boss.life+ §7[@porcentagem%]", 
					"§b§lMobs raros: §f[@mobs.raros]", "§b§lMobs bonus: §f[@mobs.bonus]", "§b§lTempo restante: §f[@time.format HH/MM]", 
					"  §7(Tempo decorrido §f[@Runtime])", "§b§lJogadores: §f[@List.size]"}));
			}
		quartz.setItemMeta(metaQuartz);
		
		return quartz;
	}
	
	public ItemStack getBookInfo() {
		ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta metaBook = book.getItemMeta();
		metaBook.setDisplayName("§6➣ §c§lRede Stone");
		metaBook.setLore(Arrays.asList(new String[] {
								" ", 
				"§fEsses desafios se consistem numa §d§lDungeon", 
				"§fonde haverá um §f§lboss§f e seus §f§lservos§f.", 
				"§fCada criatura possui uma chance de §eX% §fde dropar", 
				"§fuma §5§lmoeda épica§f. Com ela, você poderá acessar", 
				"§fa nossa loja épica com itens exclusivos.", 
								" ",
				"§fOs desafios épicos são abertos todos os dias", 
				"§fàs 15:00 §7(Horário de Brasília)", 
								" ", 
				"§e§lNOTA: §eDesafios épicos possuem um alto nível", 
				"§ede dificuldade. Junte-se com seu Clã e venha ", 
				"§ebem preparado para enfretar o boss e seus servos."}));
		
		book.setItemMeta(metaBook);
		return book;
	}
	
	public ItemStack getEpicStorage() {
		ItemStack storage = new ItemStack(Material.ENDER_CHEST);
		ItemMeta metaStorage = storage.getItemMeta();
		metaStorage.setDisplayName(storeItem);
		metaStorage.setLore(Arrays.asList(new String[] { 
								" ", 
				"§fCompre itens especiais com suas §5§lMoedas épicas§f.", 
				"§fMoedas épicas podem ser conseguidas matando criaturas", 
				"§fdurante um §d§ldesafio épico§f. §fCada entidade possui", 
				"§fSua porcentagem de chance de drop."}));
		
		storage.setItemMeta(metaStorage);
		return storage;
	}
	
	public ItemStack getDropsItemMenuNether() {
		String url = "eyJ0aW1lc3RhbXAiOjE1ODc4NzU2OTI0MzAsInByb2ZpbGVJZCI6ImQ2MGYzNDczNmExMjQ3YTI5YjgyY2M3MTViMDA0OGRiIiwicHJvZmlsZU5hbWUiOiJCSl9EYW5pZWwiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI4MDBmYWRkNTFmODBkOTY1OWIxNGQ0MTIyNjM0ZTZmNzk0MTY0NjVjYWMxMWE1NTAyNTc5ZTk4ZmE5MDJiNjIifX19";  
  	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
  	    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	   
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    profile.getProperties().put("textures", new Property("textures", url));
	    try {
	        Field profileField = headMeta.getClass().getDeclaredField("profile");
	        profileField.setAccessible(true);
	        profileField.set(headMeta, profile);
	    }
	    catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error) {
	        error.printStackTrace();
	    }
	    headMeta.setDisplayName("§6Artefatos do Nether");
	    headMeta.setLore(Arrays.asList(new String[] { 
	    		" ", 
	    		"§eAqui você poderá encontrar §6§ltodos os drops", 
	    		"§epossíveis de se obter durante uma invasão ao Nether", 
	    		"§ee derrotando o chefe final.", 
	    		" ", 
	    		}));
	    head.setItemMeta(headMeta);
		
		return head;
	}
	
	public ItemStack getArrowRight() {
		String url = "eyJ0aW1lc3RhbXAiOjE1Mjg3MjA5NTU4NDIsInByb2ZpbGVJZCI6IjUwYzg1MTBiNWVhMDRkNjBiZTlhN2Q1NDJkNmNkMTU2IiwicHJvZmlsZU5hbWUiOiJNSEZfQXJyb3dSaWdodCIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM0ZWYwNjM4NTM3MjIyYjIwZjQ4MDY5NGRhZGMwZjg1ZmJlMDc1OWQ1ODFhYTdmY2RmMmU0MzEzOTM3NzE1OCJ9fX0";  
  	    ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
  	    SkullMeta headMeta = (SkullMeta) head.getItemMeta();
	   
	    GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	    profile.getProperties().put("textures", new Property("textures", url));
	    try {
	        Field profileField = headMeta.getClass().getDeclaredField("profile");
	        profileField.setAccessible(true);
	        profileField.set(headMeta, profile);
	    }
	    catch (IllegalArgumentException|NoSuchFieldException|SecurityException | IllegalAccessException error) {
	        error.printStackTrace();
	    }
	    headMeta.setDisplayName("§6§lCRIA O ITEM A SEGUIR");
	    head.setItemMeta(headMeta);
		
		return head;
	}
	
	public ItemStack getCraftsNetherItem() {
		ItemStack item = new ItemStack(Material.WORKBENCH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getCraftItemMenuNether);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.setLore(Arrays.asList(new String[] { 
				" ", 
				"§fAqui você poderá conferir como", 
				"§fcriar todos os itens especiais utilizando", 
				"§fos artefatos do §7Nether§f.", 
				" ", }));
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getArrowToBack() {
		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta meta = arrow.getItemMeta();
		meta.setDisplayName(arrowToBack);
		meta.setLore(Arrays.asList(new String[] { " §8» §7Clique para voltar" }));
		arrow.setItemMeta(meta);
		return arrow;
	}
	
	public ItemStack getCarminitaBar() {
		ItemStack carminita = new ItemStack(Material.NETHER_BRICK_ITEM);
		ItemMeta metaCarminita = carminita.getItemMeta();
		metaCarminita.setDisplayName(carminitaBarName);
		metaCarminita.setLore(Arrays.asList(new String[] { 
				"§7Uma pedra desconhecida encontrada", 
				"§7durante uma invasão ao Nether.",
				"§7É possível sentir uma estranha", 
				"§7energia vindo dela.",
				"", 
				"§ePermite criar §6itens de Carminita§e.",}));
		metaCarminita.addEnchant(Enchantment.DURABILITY, 1, true);
		metaCarminita.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		carminita.setItemMeta(metaCarminita);
		
		NBTItemStack nbtItemStack = new NBTItemStack(carminita);
		nbtItemStack.setInteger("value", 1333); 
		
		return nbtItemStack.getItem();
	}
	
	public static ItemStack getCarminitaSword() {

		ItemStack item = new ItemStack(Material.GOLD_SWORD);
		 item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
		 item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 3);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(carminitaSwordName);
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
	
	public ItemStack getCarminitaEmpunhadura() {
		ItemStack empunhadura = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = empunhadura.getItemMeta();
		meta.setDisplayName(getCarminitaEmpunhaduraName);
		meta.setLore(Arrays.asList(new String[] { 
				"§7Uma empunhadura diferente, que parece", 
				"§7emanar uma energia poderosa. Combinada", 
				"§7com algum minério especial, é possível", 
				"§7criar um item novo.", 
				"", 
				"§ePermite criar §6itens de Carminita§e.", }));
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		empunhadura.setItemMeta(meta);
		
		NBTItemStack nbtItemStack = new NBTItemStack(empunhadura);
		nbtItemStack.setInteger("value", 1335); 
		
		return nbtItemStack.getItem();
	}
	
	public ItemStack getCristalEmber() {
		ItemStack Ember = new ItemStack(Material.QUARTZ);
		ItemMeta metaEmber = Ember.getItemMeta();
		metaEmber.setDisplayName(getEmberCristalName);
		metaEmber.setLore(Arrays.asList(new String[] { 
				"§7Cristal misterioso que parece permitir",
				"§7criar ferramentas que consigam portar o uso",
				"§7de habilidades especiais consigo.",
				"",
				"§ePermite criar §6ferramentas especiais",
				}));
		metaEmber.addEnchant(Enchantment.DURABILITY, 1, true);
		metaEmber.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		Ember.setItemMeta(metaEmber);
		
		NBTItemStack nbtItemStack = new NBTItemStack(Ember);
		nbtItemStack.setInteger("value", 1336); 
		
		return nbtItemStack.getItem();
	}
	
	public ItemStack getEmberStick() {
		ItemStack Ember = new ItemStack(Material.STICK);
		ItemMeta metaEmber = Ember.getItemMeta();
		metaEmber.setDisplayName(getEmberStickName);
		metaEmber.setLore(Arrays.asList(new String[] { 
				"§7Um cabo resistente de Ember",
				"§7que permite criar ferramentas",
				"§7especiais feitas de Ember.",
				"",
				"§ePermite criar §6ferramentas especiais",
				}));
		metaEmber.addEnchant(Enchantment.DURABILITY, 1, true);
		metaEmber.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		Ember.setItemMeta(metaEmber);
		
		NBTItemStack nbtItemStack = new NBTItemStack(Ember);
		nbtItemStack.setInteger("value", 1337); 
		
		return nbtItemStack.getItem();
	}
	
	public static ItemStack getEmberPickaxe() {
		ItemStack Ember = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta metaEmber = Ember.getItemMeta();
		metaEmber.setDisplayName(getEmberPickaxeName);
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
	
	public ItemStack getCarminitaSpade() {
		ItemStack carminitaSpade = new ItemStack(Material.GOLD_SPADE);
		ItemMeta metaCarminitaSpade = carminitaSpade.getItemMeta();
		metaCarminitaSpade.setDisplayName(getCarminitaSpadeName);
		carminitaSpade.addUnsafeEnchantment(Enchantment.DIG_SPEED, 10);
		metaCarminitaSpade.addEnchant(Enchantment.DURABILITY, 10, true);
		metaCarminitaSpade.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 4, true);
		metaCarminitaSpade.setLore(Arrays.asList(new String[] { 
				"§7§lExplosive I",
				"",
				"§d§lITEM EXCLUSIVO DO DESAFIO"
				}));
		carminitaSpade.setItemMeta(metaCarminitaSpade);
		
		NBTItemStack nbtItemStack = new NBTItemStack(carminitaSpade);
		nbtItemStack.setInteger("value", 322); 
		
		return nbtItemStack.getItem();
	}
	
	public ItemStack Moeda() {
		ItemStack moeda = new ItemStack(Material.DOUBLE_PLANT);
		ItemMeta metaMoeda = moeda.getItemMeta();
		metaMoeda.setDisplayName("§5§lMoeda épica");
		metaMoeda.setLore(Arrays.asList(new String[] { 
				"§fMoeda adquirida durante um §ddesafio épico§f!", 
				"§fPode ser utilizada para compra de itens na loja épica §7(/desafio)"}));
		metaMoeda.addEnchant(Enchantment.DURABILITY, 1, true);
		metaMoeda.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		moeda.setItemMeta(metaMoeda);

		NBTItemStack nbtItemStack = new NBTItemStack(moeda);
		nbtItemStack.setInteger("value", 322);
		//nbtItemStack.getItem();
		
		return nbtItemStack.getItem();
	}

	public ItemStack giveMoeda(Player p,int quantidade) {
		ItemStack moeda = new ItemStack(Material.DOUBLE_PLANT, quantidade);
		ItemMeta metaMoeda = moeda.getItemMeta();
		metaMoeda.setDisplayName("§5§lMoeda épica");
		metaMoeda.setLore(Arrays.asList(new String[] { 
				"§fMoeda adquirida durante um §ddesafio épico§f!", 
				"§fPode ser utilizada para compra de itens na loja épica §7(/desafio)"}));
		metaMoeda.addEnchant(Enchantment.DURABILITY, 1, true);
		metaMoeda.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		moeda.setItemMeta(metaMoeda);
		
		NBTItemStack nbtItemStack = new NBTItemStack(moeda);
		nbtItemStack.setInteger("value", 322);
		
		return nbtItemStack.getItem();
	}
	
	 // material type = material da moeda, string itemname é o nome da moeda
 /*   private void buyItem(Player p, int valor, ItemStack buyedItem) {
        if (valor <= 0) return;
	        final int size = p.getInventory().getSize();
	        final String comprado_sucesso = "§aItem adquirido com sucesso!";
	        for (int slot = 0; slot < size; slot++) {
	            ItemStack itemSlot = p.getInventory().getItem(slot);
	            if (itemSlot == null) continue;
		            if (Moeda().getType() == itemSlot.getType()) {
		            	if (itemSlot.hasItemMeta() && 
		            			itemSlot.getItemMeta().getDisplayName().equals(Moeda().getItemMeta().getDisplayName()) && 
		            				itemSlot.getItemMeta().hasLore()) {
		     		        if (p.getInventory().firstEmpty() == -1) {
		     		        	p.sendMessage("§c§lVocê não possue espaço no seu inventário.");
		     		        	return;
		     		        }
		            		if (itemSlot.getAmount() >= valor) {
				                int newAmount = itemSlot.getAmount() - valor;
					                if (newAmount > 0) {
					                	itemSlot.setAmount(newAmount);
			            				p.getInventory().addItem(buyedItem);
			            				p.sendMessage(comprado_sucesso);
					                    break;
					                } else {
					                	p.getInventory().clear(slot);
					                    valor = -newAmount;
			            				p.getInventory().addItem(buyedItem);
			            				p.sendMessage(comprado_sucesso);
					                    if (valor == 0) break;
					                	}
				                } else {
				                	p.sendMessage("§cVocê não tem moedas o suficiente para adquirir este item.");
				                	p.sendMessage("§c§lOBS: §cColoque as moedas agrupadas no inventário.");
				                	return;
				                	}
				                }
			                }
				        }
				    }
	*/
	
	public void buyItem(Player p, int valor, ItemStack buyedItem) {
		int quantidade = 0;
	    int a = 0;
	    List<Integer> slots2 = new ArrayList<Integer>();
	    for (ItemStack item : p.getInventory()) {
	        if (item != null)
	            if (item.getType() == Moeda().getType()) {
	        		
	            	NBTItemStack nbtItemStack = new NBTItemStack(Moeda());
	            	
	            	if (nbtItemStack.getItem().isSimilar(item) && item.hasItemMeta() && item.getItemMeta().getDisplayName().equals(Moeda().getItemMeta().getDisplayName()) && item.getItemMeta().hasLore()) {
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
	        p.sendMessage("§aItem adquirido com sucesso!");
	    } else {
	    	p.sendMessage("§cVocê não tem moedas o suficiente para adquirir este item.");
	    }
	}
	
	
	
	public static ItemStack item1(Player p) {
		Integer descontoRei = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip");
	//	Integer descontoDuque = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip-duque");
	 //   Integer descontoLorde = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip-lorde");
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(repairAnvilClickName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");

		      if (p.isOp()) {
				lore.add("§eCusto: §a" + (Configs.config.getConfig().getInt("reparar-custo") - descontoRei + "§2$"));
				lore.add("§eDesconto de: §6§l" + Configs.config.getConfig().getInt("desconto.vip") + "§e%");
		      }
		  /*  } else if (p.hasPermission("reparar.lorde")) {
				lore.add("§eCusto: §a" + (Configs.config.getConfig().getInt("reparar-custo") - descontoLorde + "§2$"));
				lore.add("§eDesconto de: §6§l" + Configs.config.getConfig().getInt("desconto.lorde") + "§e%");
			
			} else if (p.hasPermission("reparar.duque")) {
				lore.add("§eCusto: §a" + (Configs.config.getConfig().getInt("reparar-custo") - descontoDuque + "§2$"));
				lore.add("§eDesconto de: §6§l" + Configs.config.getConfig().getInt("desconto.vip-duque") + "§e%");
			}*/
			  else if (p.hasPermission("desafio.desconto.vip")) {
				lore.add("§eCusto: §a" + (Configs.config.getConfig().getInt("reparar-custo") - descontoRei) + "§2$");
				lore.add("§eDesconto de: §6§l" + Configs.config.getConfig().getInt("desconto.vip") + "§e%");
			} else {
				lore.add("§eCusto: §a" + (Configs.config.getConfig().getInt("reparar-custo") + "§2$"));
				lore.add("§cVocê não possui descontos.");
			}
	
		 metaItem.setLore(lore);
		item.setItemMeta(metaItem);
		return item;
	}
	
	private ItemStack item2() { 
		ItemStack item = new ItemStack(35, 1, (short)14);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(woolRepairClickCancelName);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7(Ao cancelar, nada será descontado de sua conta.)");
		metaItem.setLore(lore);
		item.setItemMeta(metaItem);
		return item;
	}
	
	private ItemStack item3() {
		Integer descontoWarrior = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip");
	//	Integer descontoDuque = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip-duque");
	 //   Integer descontoLorde = Configs.config.getConfig().getInt("reparar-custo")/100*Configs.config.getConfig().getInt("desconto.vip-lorde");
		ItemStack item = new ItemStack(Material.EMPTY_MAP);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName("§6Desconto:");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(" ");
		lore.add("§6* §6§lVIP §e: §e" + Configs.config.getConfig().getInt("desconto.vip") + "§e% (§f" +(Configs.config.getConfig().getInt("reparar-custo") - descontoWarrior)+ "§f coins§e)");
	//	lore.add("§6* §eVIP §5Duque§e: §6" +Configs.config.getConfig().getInt("desconto.vip-duque")+ "§e% (§f" +(Configs.config.getConfig().getInt("reparar-custo") - descontoDuque)+ "§f coins§e)");
	//	lore.add("§6* §eVIP §9Lorde§e: §c" +Configs.config.getConfig().getInt("desconto.vip-lorde")+ "§e% (§f" +(Configs.config.getConfig().getInt("reparar-custo") - descontoLorde)+ "§f coins§e)");
		metaItem.setLore(lore);
		item.setItemMeta(metaItem);
		return item;
	}
	
	

}

