package me.zsnow.eventnexus.listeners;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.zsnow.eventnexus.Commands;
import me.zsnow.eventnexus.Main;
import me.zsnow.eventnexus.config.Configs;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Listeners implements Listener {
	
	private static ArrayList<Player> azulTeam = Commands.AZUL;
	private static ArrayList<Player> vermelhoTeam = Commands.VERMELHO;
	public static boolean PvPAtivado;
	public static int HPazul = Configs.config.getConfig().getInt("HP-nexus-azul");
	public static int HPvermelho = Configs.config.getConfig().getInt("HP-nexus-vermelho");
	
	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.LOWEST)
	public void nexusDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof EnderCrystal && (e.getDamager() instanceof Player)) {
		     e.setCancelled(true); 
				Player p = (Player)e.getDamager();
				if ((e.getEntity().isCustomNameVisible()) && 
						(e.getEntity().getCustomName().contains("§d§lNEXUS §7- §e§lVERMELHO §7(§aHP: §f"+HPvermelho+"§7)"))) {
							if (p.isOp() && (p.getItemInHand().isSimilar(new ItemStack(Material.WOOD_AXE)))) {
								e.getEntity().remove();
								p.sendMessage("§eO nexus foi removido.");
								return;
							}
					if (vermelhoTeam.contains(p)) {
						p.sendMessage("§e[NEXUS] §cVocê deve proteger este nexus e destruir o do inimigo para vencer!");
						return;
					} 
					if (azulTeam.contains(p)) {
						if (HPvermelho <= 10) {
							HPazul = Configs.config.getConfig().getInt("HP-nexus-azul");
							HPvermelho = Configs.config.getConfig().getInt("HP-nexus-vermelho");
							p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 0.5F);
							removeEntities();
							PvPAtivado = false;
							for (Player p1 : azulTeam) {
				            	p1.sendTitle("", "§6§lVITÓRIA");
				            	p1.sendTitle("", "§6§lVITÓRIA");
							}
							for (Player p2 : vermelhoTeam) {
				            	p2.sendTitle("", "§c§lDERROTA");
				            	p2.sendTitle("", "§c§lDERROTA");
							}
							delay(p);
							endGame("azul");
							return;
						}
						HPvermelho = (HPvermelho - 10);
						e.getEntity().setCustomName("§d§lNEXUS §7- §e§lVERMELHO §7(§aHP: §f"+ HPvermelho +"§7)");
						p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0F, 0.5F);
					}
				}
				if ((e.getEntity().isCustomNameVisible()) && 
						(e.getEntity().getCustomName().contains("§d§lNEXUS §7- §e§lAZUL §7(§aHP: §f"+HPazul+"§7)"))) {
							if (p.isOp() && (p.getItemInHand().isSimilar(new ItemStack(Material.WOOD_AXE)))) {
								e.getEntity().remove();
								p.sendMessage("§eO nexus foi removido.");
								return;
							}
					if (azulTeam.contains(p)) {
						p.sendMessage("§e[NEXUS] §cVocê deve proteger este nexus e destruir o do inimigo para vencer!");
						return;
					}
					if (vermelhoTeam.contains(p)) {
						if (HPazul <= 10) {
							HPazul = Configs.config.getConfig().getInt("HP-nexus-azul");
							HPvermelho = Configs.config.getConfig().getInt("HP-nexus-vermelho");
							e.getEntity().remove();
							p.playSound(p.getLocation(), Sound.EXPLODE, 1.0F, 0.5F);
							removeEntities();
							PvPAtivado = false;
							for (Player p1 : azulTeam) {
				            	p1.sendTitle("", "§c§lDERROTA");
				            	p1.sendTitle("", "§c§lDERROTA");
				            	p1.playSound(p1.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 0.5F);
							}
							for (Player p2 : vermelhoTeam) {
				            	p2.sendTitle("", "§6§lVITÓRIA");
				            	p2.sendTitle("", "§6§lVITÓRIA");
				            	p2.playSound(p2.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 0.5F);
							}
							delay(p);
							endGame("vermelho");
							return;
						}
						HPazul = (HPazul - 10);
						e.getEntity().setCustomName("§d§lNEXUS §7- §e§lAZUL §7(§aHP: §f"+ HPazul +"§7)");
						p.playSound(p.getLocation(), Sound.BLAZE_HIT, 1.0F, 0.5F);
					} 
				}
			}
		if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Player)) {
			Player p = (Player)e.getDamager();
		    Player entity = (Player) e.getEntity();
		    if ((PvPAtivado == false) && (azulTeam.contains(p) || vermelhoTeam.contains(p))) {
				p.sendMessage("§cPvP desativado!");
				e.setCancelled(true);
				return;
			}
		    if ((azulTeam.contains(p) && azulTeam.contains(entity) || (vermelhoTeam.contains(p) && vermelhoTeam.contains(entity)))) {
		    	e.setCancelled(true);
		    	return;
		    }
		}
		 if (e.getEntity() instanceof Player && e.getDamager() instanceof Projectile) {
				if (!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Arrow)) {
					return;
				}
				final Arrow ar = (Arrow) e.getDamager();
				if (!(ar.getShooter() instanceof Player)) {
					return;
				}
				final Player p = (Player) e.getEntity();
				final Player p2 = (Player) ar.getShooter();
				if (azulTeam.contains(p) || vermelhoTeam.contains(p2)) {
					if ((azulTeam.contains(p) && azulTeam.contains(p2))|| (vermelhoTeam.contains(p) &&  vermelhoTeam.contains(p2))) {
						e.setCancelled(true);
						return;
					}
				    if ((PvPAtivado == false) && (azulTeam.contains(p) || vermelhoTeam.contains(p))) {
						p2.sendMessage("§cPvP desativado!");
						e.setCancelled(true);
						return;
				    }
				}
			}
		 }
	
	@EventHandler
	public void disconect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (azulTeam.contains(p) || (vermelhoTeam.contains(p))) {
			p.getInventory().clear();
			p.getInventory().setArmorContents(null);
			sendEnter(p, "Saida");
			azulTeam.remove(p);
			vermelhoTeam.remove(p);
			for (Player p1 : azulTeam) {
				p1.sendMessage("§e" + p.getName() + " §edesconectou do evento.");
			}
			for (Player p2 : vermelhoTeam) {
				p2.sendMessage("§e" + p.getName() + " §edesconectou do evento.");
			}
			if (PvPAtivado == true) {
				CheckAndFinalize();
			}
		}
	}
	
	@EventHandler
	public void dropitem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (azulTeam.contains(p) || (vermelhoTeam.contains(p))) e.setCancelled(true);
	}
	
	/*
	@EventHandler
	public void damage(EntityDamageEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (PvPAtivado == false) {
				 e.setCancelled(azulTeam.contains(p) || (vermelhoTeam.contains(p)));
			}
		}
	}
	*/
	
	@EventHandler
	public void deathE(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player) {
		Player morreu = (Player) e.getEntity();
		if (azulTeam.contains(morreu) || vermelhoTeam.contains(morreu)) {
			morreu.getInventory().clear();
			morreu.getInventory().setArmorContents(null);
			}
		}
	}
	
	
	@EventHandler
	public void chatMessage(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (Commands.EntradaLiberada == false) { 
			if ((azulTeam.contains(p) || (vermelhoTeam.contains(p)))) {
				e.setCancelled(true);
				if (azulTeam.contains(p)) {
				@SuppressWarnings("deprecation")
				String tag = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer()).getGroups()[0].getPrefix());
				for (Player p1 : azulTeam) {
					p1.sendMessage("§3[Azul] §7" + tag + p.getName() + "§7:§f " + e.getMessage());
				}
			}
		}
			if (vermelhoTeam.contains(p)) {
				@SuppressWarnings("deprecation")
				String tag = ChatColor.translateAlternateColorCodes('&', PermissionsEx.getUser(e.getPlayer()).getGroups()[0].getPrefix());
				for (Player p1 : vermelhoTeam) {
					p1.sendMessage("§c[Vermelho] §7" + tag + p.getName() + "§7:§f " + e.getMessage());
				}
			}
				
		}
	}
		
	
	@EventHandler
	public void respawnE(PlayerRespawnEvent e) {
		final Player p = e.getPlayer();
		if (azulTeam.contains(p)) {

			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

                @Override
                public void run() {
        			sendEnter(p, "spawn-azul");
        			p.getInventory().clear();
        			p.getInventory().setArmorContents(null);
        			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 3));
        			giveItens(p);
                }
   
            }, 10L);
		}
		if (vermelhoTeam.contains(p)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

                @Override
                public void run() {
        			sendEnter(p, "spawn-vermelho");
        			p.getInventory().clear();
        			p.getInventory().setArmorContents(null);
        			p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 120, 3));
        			giveItens(p);
                }
   
            }, 10L);
		}

	}
	
	@EventHandler
    public void onClick(InventoryClickEvent e){
    	 Player p = (Player) e.getWhoClicked();
        if(e.getSlotType().equals(SlotType.ARMOR) && (azulTeam.contains(p) || (vermelhoTeam.contains(p)))){
            e.setCancelled(true);
        }
    }
	
    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
    	Player p = e.getPlayer();
    	if (azulTeam.contains(p) && (vermelhoTeam.contains(p))) {
    		e.setCancelled(true);
    	}
    }
	
	@EventHandler
	void explode(ExplosionPrimeEvent e) {
		if (e.getEntity() instanceof EnderCrystal) {
			e.setCancelled(true);
			e.setRadius(0.0F);
			e.setFire(false);
		}
	}
	
	private static void sendEnter(Player p, String location) {
		double X = Configs.positions.getConfig().getDouble(location + ".X");
        double Y = Configs.positions.getConfig().getDouble(location + ".Y");
        double Z = Configs.positions.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.positions.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.positions.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.positions.getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
	
	private static void giveItens(Player p) {
		if (vermelhoTeam.contains(p)) {
			ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
			lam.setColor(Color.fromRGB(150,50,50));
			lhelmet.setItemMeta(lam);
			//
			ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
			lam2.setColor(Color.fromRGB(150,50,50));
			lhelmet2.setItemMeta(lam2);
			//
			ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
			lam3.setColor(Color.fromRGB(150,50,50));
			lhelmet3.setItemMeta(lam3);
			//
			ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
			lam4.setColor(Color.fromRGB(150,50,50));
			lhelmet4.setItemMeta(lam4);
			ItemStack espada = new ItemStack(Material.STONE_SWORD);
			ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3);	
			ItemStack arrow = new ItemStack(Material.BOW);
			ItemStack flecha = new ItemStack(Material.ARROW, 6);
			espada.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet2.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet3.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet4.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			p.getInventory().addItem(espada);
			p.getInventory().addItem(apple);
			p.getInventory().addItem(flecha);
			p.getInventory().addItem(arrow);
			p.getInventory().setHelmet(lhelmet);
			p.getInventory().setChestplate(lhelmet2);
			p.getInventory().setLeggings(lhelmet3);
			p.getInventory().setBoots(lhelmet4);
		}
		if (azulTeam.contains(p)) {
			ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
			LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
			lam.setColor(Color.fromRGB(0, 170, 170));
			lhelmet.setItemMeta(lam);
			//
			ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
			lam2.setColor(Color.fromRGB(0, 170, 170));
			lhelmet2.setItemMeta(lam2);
			//
			ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
			lam3.setColor(Color.fromRGB(0, 170, 170));
			lhelmet3.setItemMeta(lam3);
			//
			ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
			lam4.setColor(Color.fromRGB(0, 170, 170));
			lhelmet4.setItemMeta(lam4);
			ItemStack espada = new ItemStack(Material.STONE_SWORD);
			ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3);	
			ItemStack arrow = new ItemStack(Material.BOW);
			ItemStack flecha = new ItemStack(Material.ARROW, 6);
			espada.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet2.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet3.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			lhelmet4.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
			p.getInventory().addItem(espada);
			p.getInventory().addItem(apple);
			p.getInventory().addItem(flecha);
			p.getInventory().addItem(arrow);
			p.getInventory().setHelmet(lhelmet);
			p.getInventory().setChestplate(lhelmet2);
			p.getInventory().setLeggings(lhelmet3);
			p.getInventory().setBoots(lhelmet4);
	}
}
	
	private void removeEntities() {
		if (Configs.positions.getConfig().getString("Entrada.Mundo") != null) {
		World mundo = Bukkit.getServer().getWorld(Configs.positions.getConfig().getString("Entrada.Mundo"));
			for (Entity entidades : mundo.getEntities()) {
				if ((entidades.getCustomName() != null) && (entidades.isCustomNameVisible())
						&& (entidades.getCustomName().contains("§d§lNEXUS §7-"))) {
							entidades.remove();
			}
		}
	}
}
	
	private void endGame(String team) {
		Commands.EntradaLiberada = false;
		Commands.EventoAberto = false;
		if (team.equalsIgnoreCase("azul")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§2§lNEXUS §7- §6§lVITORIA");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(" §eO time §3§lAZUL §evenceu a batalha!");
			Bukkit.broadcastMessage(" §eJogadores vivos: §3AZUL §f(" + azulTeam.size() + "§f)§7, §cVERMELHO §f(" + vermelhoTeam.size() + "§f)");
			Bukkit.broadcastMessage("");
		}
		if (team.equalsIgnoreCase("vermelho")) {
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§2§lNEXUS §7- §6§lVITORIA");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(" §eO time §c§lVERMELHO §evenceu a batalha!");
			Bukkit.broadcastMessage(" §eJogadores vivos: §3AZUL §f(" + azulTeam.size() + "§f)§7, §cVERMELHO §f(" + vermelhoTeam.size() + "§f)");
			Bukkit.broadcastMessage("");
		}
	}
	
	private void delay(Player p) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {

			@Override
            public void run() {
				for (Player p1 : azulTeam) {
					sendEnter(p1, "Saida");
					p1.getInventory().clear();
					p1.getInventory().setArmorContents(null);
				}
				for (Player p2 : vermelhoTeam) {
					sendEnter(p2, "Saida");
					p2.getInventory().clear();
					p2.getInventory().setArmorContents(null);
				}
				azulTeam.clear();
				vermelhoTeam.clear();
            }

        }, 20*5L);
	}
	
	private void CheckAndFinalize() {
		if ((azulTeam.size() == 0) || (vermelhoTeam.size() == 0)) {
			Commands.EventoAberto = false;
			Commands.EntradaLiberada = false;
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage("§2§lNEXUS §7- §c§lENCERRADO");
			Bukkit.broadcastMessage("");
			Bukkit.broadcastMessage(" §eO evento foi encerrado.");
			Bukkit.broadcastMessage(" §eMotivo: Um dos times abandonou o jogo.");
			Bukkit.broadcastMessage("");
			for (Player p1 : azulTeam) {
				sendEnter(p1, "Saida");
				p1.getInventory().clear();
				p1.getInventory().setArmorContents(null);
			}
			for (Player p2 : vermelhoTeam) {
				sendEnter(p2, "Saida");
				p2.getInventory().clear();
				p2.getInventory().setArmorContents(null);
			}
			azulTeam.clear();
			vermelhoTeam.clear();
		}
	}
}
