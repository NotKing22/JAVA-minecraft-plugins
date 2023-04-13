package me.zsnow.stonebatataquente.manager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.stonebatataquente.Main;
import me.zsnow.stonebatataquente.api.ActionBarAPI;
import me.zsnow.stonebatataquente.api.ItemBuilder;
import me.zsnow.stonebatataquente.api.LocationAPI;
import me.zsnow.stonebatataquente.api.NumberFormatAPI;
import me.zsnow.stonebatataquente.api.Particles;
import me.zsnow.stonebatataquente.api.LocationAPI.location;
import me.zsnow.stonebatataquente.api.NBTItemStack;
import me.zsnow.stonebatataquente.api.SimpleclansAPI;
import me.zsnow.stonebatataquente.api.StringReplaceAPI;
import me.zsnow.stonebatataquente.configs.Configs;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class BatataController {
	
	EventController evento = EventController.getInstance();
	
	private static BatataController instance = new BatataController();
	
	public static BatataController getInstance() {
		return instance;
	}
	
	private int tempoDaBatata = Configs.config.getConfig().getInt("Tempo.batata-explode-em")+1;
	
	private int radius = Configs.config.getConfig().getInt("raio-de-explosao");
	
	private ArrayList<Player> participantes = new ArrayList<>();
	
	private Player batataMan;
	
	public Player getBatataMan() {
		return batataMan;
	}
	
	public void setBatataMan(Player player) {
		batataMan = player;
		if (player != null) {
			NBTItemStack nbtItemStack = new NBTItemStack(new ItemBuilder(Material.TNT).displayname("§cVocê está com a batata.")
					.lore("§6§lBata em alguém para passar a batata.").build());
			nbtItemStack.setString("batata1", "elmobatata");; 
			NBTItemStack nbtItemStack2 = new NBTItemStack(new ItemBuilder(Material.BAKED_POTATO).displayname("§c§lBatata quente")
					.lore(new String[] {
							"§7Você está com a §cBatata quente§7. Tente",
							"§7passa-la para outra pessoa acertando-a com",
							"§7sua batata.",
							" ",
							"§cCaso segure-a por muito tempo, você",
							"§ccomeçará a queimar e será eliminado."}).build());
			nbtItemStack2.setString("batata2", "batataquente");
			player.getWorld().strikeLightningEffect(player.getLocation());
			player.getInventory().setHelmet(nbtItemStack.getItem());
			player.getInventory().addItem(nbtItemStack2.getItem());
			player.getActivePotionEffects().clear();
		}
	}
	
	public ArrayList<Player> getParticipantes() {
		return participantes;
	}
	
	public boolean isBatataMan(Player entity) {
		if (getBatataMan() != null) {
			if (getBatataMan().getName().equals(entity.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void resetData() {
		setBatataMan(null);
		evento.setEntradaStatus(false);
		evento.setEventoStatus(false);
		if (getParticipantes().size() != 0) {
			for (Player participantes : getParticipantes()) {
				participantes.getInventory().clear();
				participantes.getInventory().setArmorContents(null);
				SimpleclansAPI.getAPI().disableClanDamage(participantes);
				for (PotionEffect AllPotionEffects : participantes.getActivePotionEffects()) {
					participantes.removePotionEffect(AllPotionEffects.getType());
				}
				LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
			}
		}
		getParticipantes().clear();
		evento.tempoEntrada = Configs.config.getConfig().getInt("Tempo.Entrada");
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		sh.cancelTasks(Main.getInstance());
	
	}
	
	public void sortearBatata() {
		canStopEvent();
		if (getParticipantes().size() > 0) {
			Player playerBatataMan = getParticipantes().get(new Random().nextInt(getParticipantes().size()));
			setBatataMan(playerBatataMan);
			startBatataTask();
		}
	}
	
	/*public int TaskIDbatata;
	
	@SuppressWarnings("deprecation")
	public void startBatataTask() {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		
		TaskIDbatata = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
		
			int tempoCurrency = tempoDaBatata;
			boolean hasActionBarMsg = Configs.config.getConfig().getBoolean("actionbar-contagem");
			String actionBarMsg = Configs.config.getConfig().getString("broadcast.actionbar-contagem");
			
			@Override
			public void run() {
				if (evento.getEntradaStatus() == false && evento.getEventoStatus() == true && getBatataMan().isOnline() && 
					getBatataMan() != null) {
						if (tempoCurrency > 0) {
							tempoCurrency--;
							if ((tempoCurrency == 20) || (tempoCurrency == 10) || (tempoCurrency == 5) || (tempoCurrency == 4) || (tempoCurrency == 3) || (tempoCurrency == 2) || (tempoCurrency == 1)) {
								for (Player participantes : getParticipantes()) {
									participantes.playSound(participantes.getLocation(), Sound.CLICK, 1.0f, 1.0f);
								}
								for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-tempo")) {
									for (Player participantes : getParticipantes()) {
										participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
												.replace("{tempo_batata}", String.valueOf(tempoCurrency))
												.replace("{batata_man}", getBatataMan().getName()));
									}
								}
							}
						}
							if (hasActionBarMsg) {
								for (Player participantes : getParticipantes()) {
									ActionBarAPI.sendActionBarMessage(participantes, ChatColor.translateAlternateColorCodes('&', 
											StringReplaceAPI.replaceMsg(actionBarMsg)
											.replace("{tempo_batata}", String.valueOf(tempoCurrency))
											.replace("{batata_man}", getBatataMan().getName())));
								}
							}
						Particles packet_flame = new Particles(EnumParticle.FLAME, getBatataMan().getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 8);
						packet_flame.sendToAll();
						if (tempoCurrency <= 0) {
							Particles packet = new Particles(EnumParticle.EXPLOSION_LARGE, getBatataMan().getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
							packet.sendToAll();
							getParticipantes().remove(getBatataMan());
							getBatataMan().getInventory().clear();
							getBatataMan().getInventory().setArmorContents(null);
							int explodidos = 0;
							for (Entity nearby : getNearbyEntities(getBatataMan().getLocation(), radius)) {
								if (nearby instanceof Player && getParticipantes().contains(nearby)) {
									explodidos++;
									Player pNearby = (Player) nearby;
									pNearby.getInventory().clear();
									pNearby.getInventory().setArmorContents(null);
									getParticipantes().remove(pNearby);
									pNearby.playSound(pNearby.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
									 SimpleclansAPI.getAPI().disableClanDamage(pNearby);
									pNearby.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getString("pego-na-explosao").replace("{batata_man}", getBatataMan().getName())));
									LocationAPI.getLocation().teleportTo((Player) nearby, location.SAIDA);
									pNearby.sendTitle("", "§c§lEliminado!");
								}
							}
							LocationAPI.getLocation().teleportTo(getBatataMan(), location.SAIDA);
							getBatataMan().sendTitle("", "§c§lEliminado!");
							getBatataMan().sendMessage("§3§l[Batata-quente] §eVocê segurou a batata por muito tempo e foi queimado!");
							setBatataMan(null);
							for (Player participantes : getParticipantes()) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&',
								Configs.config.getConfig().getString("broadcast.aviso-de-quantos-explodiram").replace("{explodidos}", String.valueOf(explodidos))));
								participantes.playSound(participantes.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
							}
							sh.cancelTask(TaskIDbatata);
							sortearBatata();
						}
				} else {
					sh.cancelTask(TaskIDbatata);
					sortearBatata();
				}
			}
		}, 0L, 20L);
		
	}*/
	
/*	int TaskWarning;
	
	@SuppressWarnings("deprecation")
	public void startWarningTask() {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		TaskWarning = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
		int tempo = 5+1;
			@Override
			public void run() {
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					tempo--;
					if (tempo == 5) {
						for (Player participantes : getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-aviso")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								}
							}
						}
					if (tempo == 0) {
						sortearBatata();	// já inicia o task
						sh.cancelTask(TaskWarning);
						for (Player participantes : getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-sorteada")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)
										.replace("{batata_man}", getBatataMan().getName())));
							}
						}
					}
				} else {
					sh.cancelTask(TaskWarning);
				}
			}
		}, 0L, 20L);
	}*/
	
		public void startBatataTask() {
			
			(new BukkitRunnable() {
			
			int tempoCurrency = tempoDaBatata;
			boolean hasActionBarMsg = Configs.config.getConfig().getBoolean("actionbar-contagem");
			String actionBarMsg = Configs.config.getConfig().getString("broadcast.actionbar-contagem");
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (evento.getEntradaStatus() == false && evento.getEventoStatus() == true && getBatataMan() != null && 
					getBatataMan().isOnline()) {
						if (tempoCurrency > 0) {
							tempoCurrency--;
							if ((tempoCurrency == 20) || (tempoCurrency == 10) || (tempoCurrency == 5) || (tempoCurrency == 4) || (tempoCurrency == 3) || (tempoCurrency == 2) || (tempoCurrency == 1)) {
								for (Player participantes : getParticipantes()) {
									participantes.playSound(participantes.getLocation(), Sound.CLICK, 1.0f, 1.0f);
								}
								for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-tempo")) {
									for (Player participantes : getParticipantes()) {
										participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', msg)
												.replace("{tempo_batata}", String.valueOf(tempoCurrency))
												.replace("{batata_man}", getBatataMan().getName()));
									}
								}
							}
						}
							if (hasActionBarMsg) {
								for (Player participantes : getParticipantes()) {
									ActionBarAPI.sendActionBarMessage(participantes, ChatColor.translateAlternateColorCodes('&', 
											StringReplaceAPI.replaceMsg(actionBarMsg)
											.replace("{tempo_batata}", String.valueOf(tempoCurrency))
											.replace("{batata_man}", getBatataMan().getName())));
								}
							}
						Particles packet_flame = new Particles(EnumParticle.FLAME, getBatataMan().getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 8);
						packet_flame.sendToAll();
						if (tempoCurrency <= 0) {
							Particles packet = new Particles(EnumParticle.EXPLOSION_LARGE, getBatataMan().getLocation(), 1.0F, 1.0F, 1.0F, 0.05F, 3);
							packet.sendToAll();
							getParticipantes().remove(getBatataMan());
							getBatataMan().getInventory().clear();
							getBatataMan().getInventory().setArmorContents(null);
							int explodidos = 0;
							for (Entity nearby : getNearbyEntities(getBatataMan().getLocation(), radius)) {
								if (nearby instanceof Player && getParticipantes().contains(nearby)) {
									explodidos++;
									Player pNearby = (Player) nearby;
									pNearby.getInventory().clear();
									pNearby.getInventory().setArmorContents(null);
									getParticipantes().remove(pNearby);
									pNearby.playSound(pNearby.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
									 SimpleclansAPI.getAPI().disableClanDamage(pNearby);
									pNearby.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getString("pego-na-explosao").replace("{batata_man}", getBatataMan().getName())));
									LocationAPI.getLocation().teleportTo((Player) nearby, location.SAIDA);
									pNearby.sendTitle("", "§c§lEliminado!");
									for (PotionEffect AllPotionEffects : pNearby.getActivePotionEffects()) {
										pNearby.removePotionEffect(AllPotionEffects.getType());
									}
								}
							}
							LocationAPI.getLocation().teleportTo(getBatataMan(), location.SAIDA);
							getBatataMan().sendTitle("", "§c§lEliminado!");
							getBatataMan().sendMessage("§3§l[Batata-quente] §eVocê segurou a batata por muito tempo e foi queimado!");
							for (PotionEffect AllPotionEffects : getBatataMan().getActivePotionEffects()) {
								getBatataMan().removePotionEffect(AllPotionEffects.getType());
							}
							setBatataMan(null);
							for (Player participantes : getParticipantes()) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&',
								Configs.config.getConfig().getString("broadcast.aviso-de-quantos-explodiram").replace("{explodidos}", String.valueOf(explodidos))));
								participantes.playSound(participantes.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
							}
							cancel();
							sortearBatata();
						}
				} else {
					cancel();
					startWarningTask();
				}
			}
		}).runTaskTimer(Main.getInstance(), 0L, 20L);
}

	
	public void startWarningTask() {
		
		(new BukkitRunnable() {
			
			int tempo = 5+1;
			@Override
			public void run() {
				if (evento.getEventoStatus() == true && evento.getEntradaStatus() == false) {
					tempo--;
					if (tempo == 5) {
						for (Player participantes : getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-aviso")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
								}
							}
						}
					if (tempo == 0) {
						sortearBatata();	// já inicia o task
						cancel();
						for (Player participantes : getParticipantes()) {
							for (String msg : Configs.config.getConfig().getStringList("broadcast.batata-sorteada")) {
								participantes.sendMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)
										.replace("{batata_man}", getBatataMan().getName())));
							}
						}
					}
				} else {
					cancel();
				}
			}
		})
		.runTaskTimer(Main.getInstance(), 0L, 20L);
	}
	
    public static Entity[]  getNearbyEntities(Location l, int radius){
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
        HashSet<Entity> radiusEntities = new HashSet<Entity>();
            for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++){
                for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++){
                    int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
                    for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities()){
                        if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
                    }
                }
            }
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
    
	@SuppressWarnings("deprecation")
	public void canStopEvent() {
		if (evento.getEntradaStatus() == false && evento.getEventoStatus() == true) {
			if (getParticipantes().size() == 0) {
				 evento.setEntradaStatus(false);
				 evento.setEventoStatus(false);
					for (String msg : Configs.config.getConfig().getStringList("broadcast.Sem-vencedores")) {
						Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg)));
					}
					for (Player participantes : getParticipantes()) {
						 participantes.getInventory().setArmorContents(null);
						 participantes.getInventory().clear();
						LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
						 SimpleclansAPI.getAPI().disableClanDamage(participantes);
						 for (PotionEffect allPotionEffects : participantes.getActivePotionEffects()) {
							 participantes.removePotionEffect(allPotionEffects.getType());
						 }
					}
					getParticipantes().clear();
					resetData();
					return;
			} 
			if (getParticipantes().size() == 1) {
				 evento.setEntradaStatus(false);
				 evento.setEventoStatus(false);
				Player vencedor = getParticipantes().get(0);
				for (String msg : Configs.config.getConfig().getStringList("broadcast.Com-vencedores")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', StringReplaceAPI.replaceMsg(msg).replace("{vencedor}", vencedor.getName())));
				}
				 for (PotionEffect allPotionEffects : vencedor.getActivePotionEffects()) {
					 vencedor.removePotionEffect(allPotionEffects.getType());
				 }
				 vencedor.getInventory().setArmorContents(null);
				 vencedor.getInventory().clear();
				 NumberFormatAPI formatter = new NumberFormatAPI();
					String custo = formatter.formatNumber(evento.getRecompensaCoins());
				 vencedor.sendMessage(ChatColor.translateAlternateColorCodes('&', Configs.config.getConfig().getString("vencedor-message").replace("{premio}", String.valueOf(custo))));

				 SimpleclansAPI.getAPI().disableClanDamage(vencedor);
				 LocationAPI.getLocation().teleportTo(vencedor, location.SAIDA);
				 Main.getInstance().economy.depositPlayer(vencedor.getName(), evento.getRecompensaCoins());
				 resetData();
			}
		}
	}
}
