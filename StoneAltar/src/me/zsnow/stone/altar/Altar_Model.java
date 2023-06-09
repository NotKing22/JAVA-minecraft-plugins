package me.zsnow.stone.altar;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import io.netty.util.internal.ThreadLocalRandom;

public class Altar_Model implements Listener {

	private static Altar_Model instance = new Altar_Model();

	public static Altar_Model getInstance() {
	    return instance;
	}
	
    private static final ConfigManager altar = new ConfigManager(Main.getInstance(), "altar.yml");
    
    public static HashMap<Integer, Double> altar_hp = new HashMap<>();
    
    private HashMap<Player, Long> lasAttackTimes = new HashMap<>();
    private int attackDelayTicks = 200; //200ms
    
    private YamlConfiguration getPath() {
        return altar.getConfig();
    }
    
    public Double getCurrentAltarHP(int altar_id) {
		return altar_hp.get(altar_id);
    }
    
    public void defineAltarCurrentHP(int altar_id) {
        Double pathAltarHP = getPathAltarHP(altar_id);
        altar_hp.put(altar_id, pathAltarHP);
    }
	
    public void updateCurrentAltarHP(int altar_id, Double damage, Double HP) {
    	if (altar_hp.containsKey(altar_id)) {
    		altar_hp.replace(altar_id, (HP - damage));
    		if (damage > HP) {
    			altar_hp.replace(altar_id, 0.0D);
    		}
    		return;
    	}
    }
    
    public void deleteAltarCurrentMap(int altar_id) { // POR ALGUM MOTIVO NAO FUNCIONA DESGRAÇAAAAAAAAAAAAAAAAAAAAAAAAAA VAI TOMA NO
    	altar_hp.remove(altar_id);
    }
    
	public List<String> getAltarName(int ID) {
		List<String> name = new ArrayList<>();
		for (String lines : getPath().getStringList("ALTAR." + ID + ".name")) {
			name.add(ChatColor.translateAlternateColorCodes('&', lines + "§f"));
		}
		return name;
	}
	
	public double getPathAltarHP(int ID) {
		return getPath().getDouble("ALTAR." + ID + ".hp");
	}
	
	public List<String> getPathAltarHorarios(int ID) {
		return getPath().getStringList("ALTAR." + ID + ".spawn-at");
	}
	
	public String getPathAltarLocString(int ID) {
		return getPath().getString("ALTAR." + ID + ".coord");
	}
	
	public boolean isEnableItem(int ID, int itemID) {
		return getPath().getBoolean("ALTAR." + ID + ".drops." + itemID + ".enable_item");
	}
	
	public boolean isUseCommand(int ID, int itemID) {
		return getPath().getBoolean("ALTAR." + ID + ".drops." + itemID + ".use-command");
	}
	
	public boolean isEnableEnchantment(int ID, int itemID) {
		return getPath().getBoolean("ALTAR." + ID + ".drops." + itemID + ".enable_enchantment");
	}
	
	public int getDropChanceFromID(int ID, int itemID) {
		return getPath().getInt("ALTAR." + ID + ".drops." + itemID + ".drop_chance");
	}
	
	public int getCmdChanceFromID(int ID, int itemID) {
		return getPath().getInt("ALTAR." + ID + ".drops." + itemID + ".cmd_chance");
	}
	
	public boolean isTop1Reward(int ID, int itemID) {
		return getPath().getBoolean("ALTAR." + ID + ".drops." + itemID + ".top-1-reward");
	}
	
	public boolean isCounterAttackON(int ID) {
		return getPath().getBoolean("ALTAR." + ID + ".enable-contra-ataque");
	}
	
	// ################################################################################################################### //
	
	
	String altar_inventory_name = "§7Painel de controle #";
	
	@SuppressWarnings("deprecation")
	public void openPanel(Player p, int ALTAR_ID) {
		try {
			Inventory inventory = Bukkit.createInventory(null, 3*9, altar_inventory_name + ALTAR_ID);
			
			Location loc = p.getLocation();
			String locX, locY, locZ;
			locX = String.format("%.2f", loc.getX()); locY = String.format("%.2f", loc.getY()); locZ = String.format("%.2f", loc.getZ());
			
			String horarios = String.join(", ", getPathAltarHorarios(ALTAR_ID));
			String[] partes = getPathAltarLocString(ALTAR_ID).split("@");
			String X = String.format("%.2f", Float.parseFloat(partes[0])); String Y = String.format("%.2f", Float.parseFloat(partes[1])); String Z = String.format("%.2f", Float.parseFloat(partes[2]));
			String World = partes[3];
			
			inventory.setItem(10, new ItemBuilder(Material.ITEM_FRAME).displayname("§e§lALTAR PAINEL").lore(Arrays.asList(new String[] {
					"",
					"§f◆ Nome: §f" + getAltarName(ALTAR_ID) + " §e✎",
					"§f◆ HP: §c" + getPathAltarHP(ALTAR_ID) + " §c❤",
					"§f◆ Horários: §7" + horarios,
					"§f◆ Coordenadas: §7X; " + X + " Y; " + Y + " Z; " + Z + "",
					"§f◆ Mundo: §7" + World,
					"",
					"§f◆ ID: §e§l#0" + ALTAR_ID,
					"",})).build());
			ItemStack fish = new ItemStack(Material.RAW_FISH);
			fish.setDurability((short) 3);
			inventory.setItem(13, new ItemBuilder(fish).displayname("§aRemover altar").lore(Arrays.asList(new String[] {
					"",
					"§eClique para retirar o altar do chão"})).build());
			inventory.setItem(14, new ItemBuilder(Material.COMPASS).displayname("§aMudar localização").lore(Arrays.asList(new String[] {
					"", "§f◆ Coordenadas atuais: " + "§7X; " + X + " Y; " + Y + " Z; " + Z + "", 
						"§f◆ Suas coordenadas: §7X; " + locX + " Y; " + locY + " Z; " + locZ + "",
						"", "§eClique para alterar com base na sua coord. atual!"
			})).build());
			inventory.setItem(15, new ItemBuilder(Material.APPLE).displayname("§aAlterar HP").lore(Arrays.asList(new String[] {
					"", "  §f◆ HP base: §c" + getPathAltarHP(ALTAR_ID) + " ❤", "", "§eClique para alterar o HP base do altar."
			})).glow().build());
			inventory.setItem(16, new ItemBuilder(Material.STORAGE_MINECART).displayname("§aConfigurar drops").lore(Arrays.asList(new String[] {
					 "", "§eClique para gerenciar os drops do altar."
			})).build());
			p.openInventory(inventory);
		} catch (Exception e) {
			e.printStackTrace();
			p.sendMessage("§c[StoneAltar] Houve um erro ao ver o Altar, verifique o console ou tente recarregar o plugin.");
		}
	}
	
	
	public void spawnAltar(int altar_id) {
		String[] partes = getPathAltarLocString(altar_id).replace(" ", "").split("@");
		float X = Float.parseFloat(partes[0]);
		float Y = Float.parseFloat(partes[1]);
		float Z = Float.parseFloat(partes[2]);
		World World = Bukkit.getWorld(partes[3]);
		Location loc = new Location(World, X, Y, Z).add(0.5,0.0,0.5);
		
		try {
			EnderCrystal ender = (EnderCrystal) World.spawnEntity(loc, EntityType.ENDER_CRYSTAL);
			defineAltarCurrentHP(altar_id); // define o HP na map ou da GET
			
			double vidaCurrent = getPathAltarHP(altar_id);
			double vidaMaxima = getPathAltarHP(altar_id);
			double porcentagem = (vidaCurrent / vidaMaxima) * 100;

			DecimalFormat formato = new DecimalFormat("#,##0.00");
			String percentage = formato.format(porcentagem) + "%";
			
			ender.setCustomName(ChatColor.translateAlternateColorCodes('&', "§4 ❤ " + getProgressBar(vidaCurrent, vidaMaxima, 9, "▌", "§a", "§c") + " §7("+percentage+")"));
			ender.setCustomNameVisible(true);

			
			MetadataValue metadataValue = new FixedMetadataValue(Main.getInstance(), altar_id);
			ender.setMetadata("altar_id", metadataValue);

			createHologram(loc, getAltarName(altar_id), altar_id);
		} catch (Exception e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§c[StoneAltar] Houve um erro ao gerar o Altar #0" + altar_id);
		}
	}
	
	final String metadata = "altar_id";
	
	@EventHandler
	public void onAttackAltar(EntityDamageByEntityEvent e) {
		if ((e.getEntity() instanceof EnderCrystal || e.getEntity() instanceof ArmorStand) && (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile)) {
			e.setCancelled(true);
      	    if (e.getEntity().hasMetadata(metadata)) {
      	    	if (e.getDamager() instanceof Arrow) {
      	    		final Arrow ar = (Arrow) e.getDamager();
    				if (ar.getShooter() instanceof Player) {
    					Player shooter = (Player) ar.getShooter();
    					shooter.sendMessage("§cUm campo de força magnético protegeu o altar.");
    					shooter.playSound(shooter.getLocation(), Sound.ENDERMAN_TELEPORT, 0.5f, 1.0f);
    					ar.remove();
    					return;
    				}
      	    	}
    			
			if (e.getEntity().getType() == EntityType.ENDER_CRYSTAL) {
				
				Player p = (Player) e.getDamager();
				
				long currentTime = System.currentTimeMillis();
				long lastAttackTime = lasAttackTimes.getOrDefault(p, 0L);
				
				if (currentTime - lastAttackTime < attackDelayTicks) {
					return;
				}
				lasAttackTimes.put(p, currentTime);
				
		    	int altar_id = e.getEntity().getMetadata(metadata).get(0).asInt();
		    	
		    	updateCurrentAltarHP(altar_id, e.getDamage(), getCurrentAltarHP(altar_id));
		    	
		    	double vidaAtual = getCurrentAltarHP(altar_id);
		    	double vidaMaxima = getPathAltarHP(altar_id);
		    	double porcentagem = (vidaAtual / vidaMaxima) * 100;
		
		    	DecimalFormat formato = new DecimalFormat("#,##0.00");
		    	String percentage = formato.format(porcentagem) + "%";
				
				
				e.getEntity().setCustomName(ChatColor.translateAlternateColorCodes('&', "§4 ❤ " + getProgressBar(vidaAtual, vidaMaxima, 9, "▌", "§a", "§c") + " §7("+percentage+")"));
				
			if (vidaAtual <= 0 || (p.isOp() && p.getItemInHand().getType() == Material.WOOD_AXE)) {
		    	for (Entity nearby : e.getEntity().getNearbyEntities(0, 1, 0)) {
		    		if (nearby.getType() == EntityType.ARMOR_STAND && nearby.getLocation().getY() > e.getEntity().getLocation().getY() && nearby.hasMetadata(metadata)) {
		    			nearby.remove();
		    		}
		    	}
			    	e.getEntity().remove();
			    	deleteAltarCurrentMap(altar_id);
		    	
					Location loc = e.getEntity().getLocation();
					loc.getWorld().strikeLightningEffect(loc);
					ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, 0.5, 0), EntityType.ARMOR_STAND);
					hologram.setVisible(false);
					hologram.setGravity(false);
					hologram.setSmall(true);
					hologram.setCustomName("§6§ldesfragmentando...");
					hologram.setCustomNameVisible(true);
					hologram.setHelmet(getHDskull());
		
					MetadataValue metadataValue = new FixedMetadataValue(Main.getInstance(), altar_id);
					hologram.setMetadata("altar_id", metadataValue);
					
					int numeroSorteado = new Random().nextInt(501) + 400; // random entre 400 e 900
					float delay = (float) numeroSorteado;

			(new BukkitRunnable() {
				
			    float rotation = 0;
			    float levitation_y = 1.5f; 
			    Location hologramLocation = hologram.getLocation().clone();

			    @Override
			    public void run() {
			        rotation += 6.0f;

			        if (rotation >= delay) {
			            hologram.remove();

			            String serializedInventory = Configs.altar.getString("ALTAR." + altar_id + ".drops");
			            if (!serializedInventory.isEmpty()) {
			                ItemStack[] inventory = SerializeItemStack.deserializePlayerInventory(serializedInventory);

			                for (ItemStack item : inventory) {
			                    if (item != null && item.getType() != Material.AIR) {
			                        e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), item);
			                    }
			                }
			            }
			            cancel();
			            return;
			        }

			        levitation_y = (float) (Math.sin(Math.toRadians(rotation)) * 0.3); // amplitude da levitaçao
			        Location location = hologramLocation.clone().add(0, levitation_y, 0);
			        
			        EulerAngle eulerAngle = new EulerAngle(0, Math.toRadians(rotation), 0);
			        hologram.teleport(location);
			        hologram.setHeadPose(eulerAngle);
			    }
			}).runTaskTimer(Main.getInstance(), 0L, 1L);
		}
      	    	// ALTAR VIVO ENTAO SALVE E RODE OS ATAQUES
      	    	
      	     if (isCounterAttackON(altar_id)) {
      	    	float randomChance = ThreadLocalRandom.current().nextFloat() * 100;
      	    	Location loc = e.getEntity().getLocation();
      	    		if (randomChance <= 5.000 && randomChance >= 2.500) {
							double launchStrength = 1.5;
							double launchRadius = 5;
							for (Entity nearby : loc.getWorld().getNearbyEntities(loc, launchRadius, launchRadius, launchRadius)) {
								if (nearby instanceof Player && (!nearby.hasPermission("zs.mod"))) {
									Player pNearby = (Player) nearby;
									Vector launchVector = new Vector(0, launchStrength, 0);
									pNearby.setVelocity(launchVector);
									pNearby.damage(4); // 2 cora
									pNearby.playSound(e.getEntity().getLocation(), Sound.WITHER_HURT, 1.0f, 0.5f);
									pNearby.playSound(e.getEntity().getLocation(), Sound.GHAST_CHARGE, 1.0f, 0.5f);
								}
							}
							return;
      	    			}
						if (randomChance <= 7.500 && randomChance >= 5.500) {
							for (Entity nearby : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
								if (nearby instanceof Player  && (!nearby.hasPermission("zs.mod"))) {
									Player pNearby = (Player) nearby;
									pNearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 1));
									pNearby.damage(8); // 4 cora
									pNearby.playSound(e.getEntity().getLocation(), Sound.WITHER_HURT, 1.0f, 0.5f);
									
										}
									}
								return;
								}
						if (randomChance <= 7.900 && randomChance >= 6.500) {
							for (Entity nearby : loc.getWorld().getNearbyEntities(loc, 5, 5, 5)) {
								if (nearby instanceof Player  && (!nearby.hasPermission("zs.mod"))) {
									Player pNearby = (Player) nearby;
									int randomEffect = ThreadLocalRandom.current().nextInt(4);
									pNearby.playSound(e.getEntity().getLocation(), Sound.GHAST_SCREAM, 1.0f, 0.5f);
									switch (randomEffect) {
										case 1:
											if (!pNearby.hasPotionEffect(PotionEffectType.WITHER)) {
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*5, 3));
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 1));
											}
											break;
										case 2:
											if (!pNearby.hasPotionEffect(PotionEffectType.WEAKNESS)) {
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20*10, 3));
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 1));
											}
											break;
										case 3:
											if (!pNearby.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*10, 3));
												pNearby.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*2, 1));
											}
											break;
										default:
											break;
										}
									}
								}
		      	    		}
		      	     }
      	    	return;
      	    	}
  	    	}
		}
	} 
	
	@EventHandler
	public void interactAltar(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof EnderCrystal && e.getPlayer().isOp() && e.getRightClicked().hasMetadata(metadata)) 
			openPanel(e.getPlayer(), e.getRightClicked().getMetadata(metadata).get(0).asInt());
	}
	
	HashMap<Player, Integer> ID_HP = new HashMap<>();
	HashMap<Player, Integer> editando_drop = new HashMap<>();
	String drops_inventory_title = " §7Personalizando drops  ";
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().contains(altar_inventory_name) && p.hasPermission("altar.gerente")) {
			e.setCancelled(true);
			final ItemStack currentItem = e.getCurrentItem();
			if (currentItem != null && currentItem.hasItemMeta() && currentItem.getItemMeta().hasDisplayName() && currentItem.getItemMeta().hasLore()) {
				final String currentName = e.getCurrentItem().getItemMeta().getDisplayName();

				String[] parts = e.getInventory().getName().split("#");
				if (parts.length > 1) {
				    String numberString = parts[1];
				    
				    try {
				    	int ID = Integer.parseInt(numberString.trim());
						switch (currentName) {
							case "§aMudar localização":
								Location loc = p.getLocation();
								Configs.altar.set("ALTAR." + ID + ".coord", loc.getX() + "@" + loc.getY() + "@" + loc.getZ() + "@" + loc.getWorld().getName() + " ");
								Configs.altar.saveConfig();
								p.closeInventory();
								p.sendMessage("§e[StoneAltar] A localização atual do altar §f#0" + ID + " §efoi redefinida.");
								break;
							case "§aAlterar HP":
								if (!ID_HP.containsKey(p)) {
									ID_HP.put(p, ID);
									p.closeInventory();
									p.sendMessage(" ");
									p.sendMessage(" §aAgora digite no chat um valor inteiro para que ele se torne a vida base do Altar §f#0" + ID);
									p.sendMessage(" §aVocê pode encerrar esta ação digitando §f'cancelar' §ano chat.");
									p.sendMessage(" ");
									return;
								}
								p.sendMessage("§cVocê já está definindo a vida base do altar.");
								p.closeInventory();
								break;
							case "§aRemover altar":
								if (altar_hp.containsKey(ID)) {
									for (World world : Bukkit.getWorlds()) {
										for (Entity entities : world.getEntities()) {
											if (entities.getType() == EntityType.ENDER_CRYSTAL && 
												entities.hasMetadata(metadata) && entities.getMetadata(metadata).get(0).asInt() == ID) {
												deleteAltarCurrentMap(ID);
												removeHologramByID(ID);
												entities.remove();
												p.sendMessage("§aAltar removido com sucesso.");
											}
										}
									}
									return;
								} else {
									p.sendMessage("§cEsse altar não está vivo.");
									return;
								}
							case "§aConfigurar drops":
								Inventory inventory = Bukkit.createInventory(null, 4*9, drops_inventory_title);
								if (Configs.altar.getConfigurationSection("ALTAR.") == null || Configs.config.getStringList("ALTAR." + ID) == null) {
									p.sendMessage("§c[StoneAltar] O arquivo de configuração está invalido ou não existe um atlar com esse ID #" + ID); 
								}
									 String serializedInventory = Configs.altar.getString("ALTAR."+ID+".drops");
									 editando_drop.put(p, ID);
									 if (serializedInventory.isEmpty()) {
										 p.openInventory(inventory);
										 return;
									 }
									 ItemStack[] inventoryDeserialized = SerializeItemStack.deserializePlayerInventory(serializedInventory); //
									 inventory.setContents(inventoryDeserialized);
									 p.openInventory(inventory);
									 break;
							default:
								break;
							}
				} catch (NumberFormatException exception) {
					exception.printStackTrace();
					p.sendMessage("§c[StoneAltar] Há um erro em altar.yml! O ID do altar não é um valor válido: " + numberString.trim());
					}
				}
			}
		}
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent e) {
	     Player player = (Player) e.getPlayer();
	     Inventory inventory = e.getInventory();
	     if (inventory.getName().equals(drops_inventory_title) && player.hasPermission("zs.gerente") && editando_drop.containsKey(player)) {
	    	 Integer ID = editando_drop.get(player);
	    	   String serializedInventory =SerializeItemStack.serializeInventory(inventory);
	    	   Configs.altar.set("ALTAR."+ID+".drops", serializedInventory);
			   Configs.altar.saveConfig();
			   editando_drop.remove(player);
			   player.sendMessage("O inventário foi salvo com sucesso. Recarregue o plugin para que a alteração entre em vigor.");
	     }
	}
	
	
	/// ARRUMAR DROPS ACIMA
	
	@EventHandler
	public void sendmessage(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (ID_HP.containsKey(p)) {
			e.setCancelled(true);
			if (e.getMessage().equalsIgnoreCase("cancelar")) {
				ID_HP.remove(p);
				p.sendMessage("§eVocê cancelou esta ação.");
				return;
			}
			try {
				double number = Double.parseDouble(e.getMessage());
				int ID = ID_HP.get(p);
					if (number >= 1) {
						Configs.altar.set("ALTAR." + ID + ".hp", number);
						Configs.altar.saveConfig();
						Configs.altar.reloadConfig();
						ID_HP.remove(p);
						p.sendMessage("§e[StoneAltar] Você definiu a vida base do altar §f#0" + ID + " §epara §c" + number + " §c❤");
					} else {
						p.sendMessage("§cA vida do altar precisa ser maior ou igual a 1.");
						return;
					}
			} catch (NumberFormatException e2) {
				p.sendMessage("§c'" + e.getMessage() + "' não é um número válido.");
			}
		}
	}
	
	public void createHologram(Location loc, List<String> lines, int altar_id) {
	    double height = lines.size() * 0.3; // Altura total do holograma
	    double lineHeight = 0.3; // Distancia entre cada linha
	    
	    for (String line : lines) {
	        ArmorStand hologram = (ArmorStand) loc.getWorld().spawnEntity(loc.clone().add(0, height, 0), EntityType.ARMOR_STAND);
	        hologram.setVisible(false);
	        hologram.setGravity(false);
	        hologram.setCustomName(ChatColor.translateAlternateColorCodes('&', line));
	        hologram.setCustomNameVisible(true);
	        
	        MetadataValue metadataValue = new FixedMetadataValue(Main.getInstance(), altar_id);
	        hologram.setMetadata("altar_id", metadataValue);
	        
	        height -= lineHeight; // Diminui a altura para a proxima linha
	    }
	}
	
	public void removeHologramByID(int altar_id) {
		for (World world : Bukkit.getWorlds()) {
			for (Entity entities : world.getEntities()) {
				if (entities.getType() == EntityType.ARMOR_STAND) {
			    	for (Entity nearby : entities.getNearbyEntities(0, 1, 0)) {
			    		if (nearby.hasMetadata(metadata) && !entities.getMetadata(metadata).isEmpty() && entities.getMetadata(metadata).get(0).asInt() == altar_id) {
		  	    			nearby.remove();
	  	    			}
	  	    		}
		    	}
    		}
    	}
    }
	
    public static ItemStack getHDskull() {
        String url = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWM4YzljMTAxM2NlNzQ5MTE1MTc0NGQzZTYyOTYyN2UxNjk5MWZhYTY2ZTk0NDAwNzRmY2ZiYWVmMGE4ZmM4OCJ9fX0=";
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", url));
        try {
            Field profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | NoSuchFieldException | SecurityException | IllegalAccessException error) {
            error.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }
	
	// codigo alterado para sempre ter ao menos uma barra verde
    private String getProgressBar(double current, double max, int totalBars, String symbol, String color0, String color1) {
        double percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return color0 + symbol + Strings.repeat(symbol, progressBars)
                + color1 + Strings.repeat(symbol, totalBars - progressBars);
    }
}
	



/*	private HashMap<String, HashMap<Integer, Double>> dataMap = new HashMap<>();

	public void updatePlayerData(String player_name, int ID, double damage) {
	    HashMap<Integer, Double> playerData = dataMap.get(player_name);
	    if (playerData == null) {
	        playerData = new HashMap<>();
	        dataMap.put(player_name, playerData);
	    }

	    if (playerData.containsKey(ID)) {
	        double currentDamage = playerData.get(ID);
	        playerData.put(ID, currentDamage + damage);
	    } else {
	        playerData.put(ID, damage);
	    }
	}
	
	public double getDamageFromPlayer(String player_name, int ID) {
	    HashMap<Integer, Double> playerData = dataMap.get(player_name);
	    if (playerData != null && playerData.containsKey(ID)) {
	        return playerData.get(ID);
	    }
	    return 0.0;
	}

	public HashMap<Integer, Double> getPlayerData(String player_name) {
	    return dataMap.get(player_name);
	}
	
	public void deletePlayerDataWithID(int ID) {
	    for (HashMap<Integer, Double> playerData : dataMap.values()) {
	        playerData.remove(ID);
	    }
	}
	
	public void deletePlayerDataFromPlayer(String player_name) {
	    dataMap.remove(player_name);
	}
	
	 private static String heart = "❤";
	
	public String getTop1FromAltarID(int ALTAR_ID) {
		String PlayerWithMostDamage = null;
		double maxDamage = 0.0d;
		
		Bukkit.broadcastMessage(" ");
		for (Map.Entry<String, HashMap<Integer, Double>> entry : dataMap.entrySet()) {
			HashMap<Integer, Double> playerData = entry.getValue();
			if (playerData.containsKey(ALTAR_ID)) {
				double damage = playerData.get(ALTAR_ID);
				Player loopedPlayer = Bukkit.getPlayerExact(entry.getKey());
					if (loopedPlayer != null) {
						String msg = Configs.config.getString("private-message")
							.replace("{heart-emoji}", heart)
						    .replace("{damage}", String.valueOf(damage));
						loopedPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
					}
				if (damage > maxDamage) {
					maxDamage = damage;
					PlayerWithMostDamage = entry.getKey();
				}
			}
		}
		if (PlayerWithMostDamage != null) {
			for (String msg : Configs.config.getStringList("altar-defeat")) {
				Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
						msg.replace("{player}", PlayerWithMostDamage)
					    .replace("{heart-emoji}", heart)
					    .replace("{damage}", String.valueOf(maxDamage))));
			}
		}
		return PlayerWithMostDamage;
	}
	
	public String getTop1Only(int ALTAR_ID) {
		String PlayerWithMostDamage = null;
		double maxDamage = 0.0d;
		
		for (Map.Entry<String, HashMap<Integer, Double>> entry : dataMap.entrySet()) {
			HashMap<Integer, Double> playerData = entry.getValue();
			if (playerData.containsKey(ALTAR_ID)) {
				double damage = playerData.get(ALTAR_ID);
				if (damage > maxDamage) {
					maxDamage = damage;
					PlayerWithMostDamage = entry.getKey();
				}
			}
		}
		return PlayerWithMostDamage;
	}
	
	
	private-message: '&c&l[STATUS] &fVocê causou &c{damage} {heart-emoji} &fde dano.'
altar-defeat:
  - '&d&l[ALTAR MISTICO] &e{player} &ecausou o maior dano no altar &c({damage} {heart-emoji}) &ee recebeu um &6&lDROP ESPECIAL'
  - ''
	
   */

	
