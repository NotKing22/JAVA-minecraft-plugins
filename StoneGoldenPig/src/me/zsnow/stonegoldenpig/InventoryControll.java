package me.zsnow.stonegoldenpig;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.zsnow.stonegoldenpig.api.ItemBuilder;
import me.zsnow.stonegoldenpig.configs.Configs;
import me.zsnow.stonegoldenpig.manager.SorteioManager;
import net.md_5.bungee.api.ChatColor;


public class InventoryControll implements Listener {
	
	private static InventoryControll instance = new InventoryControll();
	
	public static InventoryControll getInstance() {
		return instance;
	}

	DecimalFormat formatar = new DecimalFormat("###,###,###,###,###.##");
	
	private String mainMenu = "§8Porquinho dourado";
	private String lancesMenu = "§8Gerenciar lances";
	
	static SorteioManager sorteio = SorteioManager.getInstance();

	public void openMenu(Player p) { //4*9
		Inventory inventory = Bukkit.createInventory(null, 5*9, mainMenu);
			final ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
			for (int slots = 0; slots < inventory.getSize(); slots++) {
				inventory.setItem(slots, vidro);
			}//11 13 15 31 32
			
			inventory.setItem(3, getSkull());
			inventory.setItem(5, getSkull2());
			inventory.setItem(4, sorteio.getItemLeiloado());
			inventory.setItem(20, new ItemBuilder(Material.GOLD_BLOCK).displayname("§cEm breve").build());
			inventory.setItem(22, new ItemBuilder(Material.GOLDEN_CARROT).displayname("§aGerenciar lances")
					.lore(Arrays.asList(new String[] {"§eclique para alterar"})).build());
			inventory.setItem(24, new ItemBuilder(Material.GOLD_BARDING).displayname("§aSeu valor depositado")
					.lore(Arrays.asList(new String[] {"§eDinheiro enviado: §6R$" + formatar.format(sorteio.getValorDepositado(p.getName()))})).build());
			inventory.setItem(39, new ItemBuilder(Material.REDSTONE_TORCH_ON).displayname("§eStatus do leilão:").lore(Arrays.asList(new String[] {
					"§7§m-----------------", 
					"", 
					"§7Top lance: §6" + formatar.format(sorteio.getValorMaisAltoApostado()) + " §6coins", 
					"§7Por: §a" + sorteio.getUserTopLance(), 
					"",
					"§7Acaba em: §e" + sorteio.getTempo() + " §eSegundos"})).build());
			inventory.setItem(40, new ItemBuilder(Material.BARRIER).displayname("§cfechar").build());
			inventory.setItem(41, new ItemBuilder(Material.PAPER).displayname("§aStatus do porquinho")
			.lore(Arrays.asList(new String[] {" §7Valor inicial: §6"+ formatar.format(sorteio.getValorInicial()) +" §7Coins",
											  " ",
											  "  §7Valor mais alto apostado: §6" + formatar.format(sorteio.getValorMaisAltoApostado()) + " §7Coins", //sorteio.getUserTopLance(player.getName());
											  " ",
											  " §7Total de participantes: §6" + sorteio.getParticipantes().size(),
											  " §7Tempo restante: §6" + sorteio.getTempo() + " §6segundo(s)"})).build());
			p.openInventory(inventory);
		}
	
	public void openMenuLances(Player p) {
		Inventory inventory = Bukkit.createInventory(null, 4*9, lancesMenu);
		final ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
		for (int slots = 0; slots < inventory.getSize(); slots++) {
			inventory.setItem(slots, vidro);
			}//11 13 15 31 32
			inventory.setItem(11, new ItemBuilder(Material.DOUBLE_PLANT).displayname("§6Enviar um lance")
					.lore(Arrays.asList(new String[] {"§7clique para digitar um valor"})).build());
			inventory.setItem(13, new ItemBuilder(Material.POWERED_RAIL).displayname("§fValor mínimo para apostar: §6" + formatar.format(sorteio.getValorComTaxa(p.getName()))) // trocar a msg
					.lore(Arrays.asList(new String[] {"§7O valor mínimo que um", 
													  "§7player pode oferecer para", 
													  "§7participar do evento.",
													  "",
													  "§6 ● §eTaxa de 10%",
													  "",
													  "§eClique para apostar o valor mínimo!"})).build());
			inventory.setItem(15, new ItemBuilder(Material.WATCH).displayname("§fDuração: §e" + sorteio.getTempo() + " Segundos.") // alterar msg
					.lore(Arrays.asList(new String[] {"§7Este é o tempo que os", 
													  "§7jogadores terão para fazer", 
													  "§7os seus lances no evento."})).build());
			inventory.setItem(31, new ItemBuilder(Material.ARROW).displayname("§aVoltar página").build());
			p.openInventory(inventory);
	}
	
	@SuppressWarnings("deprecation")
	public void confirmationMenu(Player p) {
		Inventory inventory = Bukkit.createInventory(null, 5*9, mainMenu);
		ItemStack greenWool = new ItemStack(Material.WOOL, 1, DyeColor.GREEN.getData());
		ItemStack redWool = new ItemStack(Material.WOOL, 1, DyeColor.RED.getData());
		
		ItemMeta metaGreenWool = greenWool.getItemMeta();
		ItemMeta metaRedWool = redWool.getItemMeta();
		
		metaGreenWool.setDisplayName("§aConfirmar");
		metaRedWool.setDisplayName("§cRecusar");
		
		metaGreenWool.setLore(Arrays.asList(new String[] {"§7Clique para confirmar"}));
		metaRedWool.setLore(Arrays.asList(new String[] {"§7Clique para recusar"}));
		
		greenWool.setItemMeta(metaGreenWool);
		redWool.setItemMeta(metaRedWool);
		inventory.setItem(29, greenWool);
		inventory.setItem(33, redWool);
		inventory.setItem(13, new ItemBuilder(Material.GOLDEN_CARROT).displayname("§6Leia antes de prosseguir.").lore(Arrays.asList(new String[] {
				"§7Você está prestes a pagar §aR$" + sorteio.getValorComTaxa(p.getName()) + " Coins", 
				"§7para participar do evento.",
				"§7Para confirmar, basta clicar no botão §averde§7."})).build());
		p.openInventory(inventory);
		// 29 33 13
		
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player)) return;
			final Player player = (Player) event.getWhoClicked();
			if (event.getInventory().getTitle().equals(mainMenu) || event.getInventory().getTitle().equals(lancesMenu)) {
				event.setCancelled(true);
				if (sorteio.getOcorrendo() == true && sorteio.apostasLiberadas() == true) {
		            ItemStack currentItem = event.getCurrentItem();
		            if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName()) {
		            	if (currentItem.getItemMeta().getDisplayName().equals("§aGerenciar lances")) {
		            		openMenuLances(player);
		            		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals("§aVoltar página")) {
		            		openMenu(player);
		            		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals("§cfechar")) {
		            		player.closeInventory();
		            		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().contains("§fValor mínimo para apostar: §6")) {
		            		if (Main.getInstance().economy.has(player, sorteio.getValorComTaxa(player.getName()))) {
		            			confirmationMenu(player);
		            		} else {
		            			player.sendMessage("§cVocê não possui dinheiro o suficiente para concluir essa transação.");
		            		}
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals("§cRecusar")) {
		            		player.closeInventory();
		            		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
		            		return;
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals("§aConfirmar")) {
		            		if (Main.getInstance().economy.has(player, sorteio.getValorComTaxa(player.getName()))) {
		            			sorteio.upContador();
		            			Main.getInstance().economy.withdrawPlayer(player, sorteio.getValorComTaxa(player.getName()));
		            			
		    					player.sendMessage("§aVocê depositou " + formatar.format(sorteio.getValorComTaxa(player.getName()))+ " §ano porquinho dourado.");
		    					
		    					sorteio.setValorDepositado(player.getName(), sorteio.getValorComTaxa(player.getName()));
									sorteio.setValorMaisAltoApostado(sorteio.getValorDepositado(player.getName()));
									sorteio.setUserTopLance(player.getName());
							
		    					sorteio.setValorAcumulado(sorteio.getValorAcumulado() + sorteio.getValorComTaxa(player.getName()));
		    					
								sorteio.updateNumeroLances();
								
		            			player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1.0f, 0.5f);
		            			player.closeInventory();
		            			if (!sorteio.getParticipantes().contains(player)) {
		            				sorteio.getParticipantes().add(player);
		            			}
		            		} else {
		            			player.sendMessage("§cVocê não possui dinheiro o suficiente para concluir essa transação.");
		            			return;
		            		}
		            	}
		            	if (currentItem.getItemMeta().getDisplayName().equals("§6Enviar um lance")) {
		            		if (!sorteio.chatEvent.contains(player)) {
			            		sorteio.chatEvent.add(player);
			            		player.closeInventory();
			            		player.sendMessage(" ");
			            		player.sendMessage(" ");
			            		player.sendMessage(" §aDigite no chat a quantia que você deseja enviar para o porquinho.");
			            		player.sendMessage(" §aO valor mínimo de envio é R$" + formatar.format(sorteio.getValorComTaxa(player.getName())) + " §aCoins.");
			            		player.sendMessage(" §7Para encerrar esta ação digite '§6cancelar§7'.");
			            		player.sendMessage(" ");
			            		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1.0f, 0.5f);
			            		return;
		            		}
		            	}
		            }
	            } else {
	            	player.closeInventory();
	            }
			}
		}
	
	@EventHandler
	public void chatEvent(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		if (sorteio.getOcorrendo() && sorteio.apostasLiberadas()) {
			if (sorteio.chatEvent.contains(p)) {
				event.setCancelled(true);
				if (event.getMessage().equalsIgnoreCase("cancelar")) {
					sorteio.chatEvent.remove(p);
					p.sendMessage("§eVocê cancelou esta ação.");
					return;
				}
				double valor;
				try {
					valor = Double.parseDouble(event.getMessage());
				} catch (NumberFormatException e) {
					p.sendMessage("§cO valor informado não é um número.");
					return;
				}
				if (valor < sorteio.getValorComTaxa(p.getName())) {
					p.sendMessage("§cO valor mínimo para apostas é de " + formatar.format(sorteio.getValorComTaxa(p.getName())));
					return;
				}
				if (Main.getInstance().economy.has(p, sorteio.getValorComTaxa(p.getName())) && Main.getInstance().economy.has(p, valor)){
					sorteio.chatEvent.remove(p);
					sorteio.upContador();
					Main.getInstance().economy.withdrawPlayer(p, valor);
					if (!sorteio.getParticipantes().contains(p)) {
						sorteio.getParticipantes().add(p);
					}
					
						sorteio.setValorMaisAltoApostado(sorteio.getValorDepositado(p.getName()) + valor);
						sorteio.setUserTopLance(p.getName());
						p.sendMessage("§eO valor mais alto enviado até agora é seu.");
					
					sorteio.setValorDepositado(p.getName(), valor);
					
					sorteio.setValorAcumulado(sorteio.getValorAcumulado() + valor);
					sorteio.updateNumeroLances();
					p.sendMessage("§aVocê depositou " + formatar.format(valor)+ " §ano porquinho dourado.");
					} else {
						p.sendMessage("§cVocê não possui dinheiro o suficiente para completar esta ação. Seu saldo: " + formatar.format(Main.getInstance().economy.getBalance(p)));
					}
			} 
		} 
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
			if ((event.getRightClicked() instanceof Pig) && event.getRightClicked().hasMetadata("porquinhodourado")) {
				player.playSound(player.getLocation(), Sound.ZOMBIE_PIG_IDLE, 1.0f, 0.5f);
				openMenu(player);
			}
		}
	public static void loadItem(String stringItem) {
		int amount = Configs.itens.getConfig().getInt("Itens."+stringItem+".Amount");
		int ID = Configs.itens.getConfig().getInt("ID");
		ItemStack item = new ItemStack(Material.getMaterial(Configs.itens.getConfig().getString("Itens."+stringItem+".Material")), amount, (short) ID);
		ItemMeta metaItem = item.getItemMeta();
		metaItem.setDisplayName(ChatColor.translateAlternateColorCodes('&', Configs.itens.getConfig().getString("Itens."+stringItem+".Displayname")));
		ArrayList<String> lore = new ArrayList<>();
			for (String configLore : Configs.itens.getConfig().getStringList("Itens."+stringItem+".Lore")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', configLore));
			}
			metaItem.setLore(lore);
			item.setItemMeta(metaItem);
			for (String fromConfig : Configs.itens.getConfig().getStringList("Itens."+stringItem+".Enchantments")) {

				String enchantName = fromConfig.split(":")[0];
                Integer enchantLevel = Integer.valueOf(fromConfig.split(":")[1]);
				
			    item.addUnsafeEnchantment(Enchantment.getByName(enchantName), enchantLevel);
			}	
			sorteio.setValorInicial(Configs.itens.getConfig().getDouble("Itens."+stringItem+".Lance-inicial"));
			sorteio.setValorAcumulado(Configs.itens.getConfig().getDouble("Itens."+stringItem+".Lance-inicial"));
			sorteio.setValorMaisAltoApostado(sorteio.getValorInicial());
			sorteio.setUserTopLance("§cNinguém...");
			sorteio.setItemLeiloado(item);
			sorteio.setGivarItem(Configs.itens.getConfig().getBoolean("Itens."+stringItem+".Give-item"));
	}
	
	public static void loadItemFromPlayerHand(ItemStack itemHand, Double valorInicial) {
			sorteio.setValorInicial(valorInicial);
			sorteio.setValorAcumulado(valorInicial);
			sorteio.setValorMaisAltoApostado(valorInicial);
			sorteio.setUserTopLance("§cNinguém...");
			sorteio.setItemLeiloado(itemHand);
			sorteio.setGivarItem(true);
	}
	
	public static String sortearItem() {
		ArrayList<String> item = new ArrayList<>();
		for (String key : Configs.itens.getConfig().getConfigurationSection("Itens.").getKeys(false)) {
			item.add(key);
		}
		return item.get(new Random().nextInt(item.size()));
	}
	
	
	public ItemStack getSkull() {
		String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzMzYWU4ZGU3ZWQwNzllMzhkMmM4MmRkNDJiNzRjZmNiZDk0YjM0ODAzNDhkYmI1ZWNkOTNkYThiODEwMTVlMyJ9fX0="; 
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
	    head.setItemMeta(headMeta);
		return head;
	}
	
	public ItemStack getSkull2() {
		String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFjOTZhNWMzZDEzYzMxOTkxODNlMWJjN2YwODZmNTRjYTJhNjUyNzEyNjMwM2FjOGUyNWQ2M2UxNmI2NGNjZiJ9fX0="; 
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
	    head.setItemMeta(headMeta);
		return head;
	}
	/*
	 * Itens:
  espada:
    Material: 'DIAMOND_SWORD'
    Amount: 1
    Lance-minimo: 100000
    Displayname: '&bEspada de zeus'
    Lore:
    - ''
    - '&bEsta é espada de zeus'
    - ''
    Enchants:
    - 'DAMAGE_ALL:5'
    - ''
    HideAttributes: true
    Usar-comando: true
    - 'give @player espada 1'
    
	 */
}
