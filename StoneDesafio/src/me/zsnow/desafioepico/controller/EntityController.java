package me.zsnow.desafioepico.controller;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.zsnow.desafioepico.Main;
import me.zsnow.desafioepico.configAPI.Configs;
import me.zsnow.desafioepico.configAPI.Particles;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.GenericAttributes;


public class EntityController {

	//EntityType MobsRaros;
	//EntityType MobsBonus;
	//EntityType Boss;
	public static String BossName;
	public static int NetherBossHP = Configs.config.getConfig().getInt("NetherBoss.HP");
	public static int servosNetherHP = Configs.config.getConfig().getInt("Servos.HP");
	public static int carminidioNetherHP = Configs.config.getConfig().getInt("Carminidio.HP");
	public static int CavaleiroHP = Configs.config.getConfig().getInt("Cavaleiro.HP");
// BossHP - dano ; if (bossHP >= 0)
	
	public static Integer setNetherBossHP(Integer hp) {
		return NetherBossHP = hp;
	}
	
	public static Integer getNetherBossHP() {
		return NetherBossHP;
	}
	
/*	public void createBoss(EntityType entity, int hp, String name, Location loc) {
		this.Boss = entity;
		this.BossHP = hp;
		this.BossName = name;
		
	}*/
	
	public static void spawnNetherBoss(String name, String location) {
		BossName = name;
		location = location.toUpperCase();
			double X = Configs.locations.getConfig().getDouble(location + ".X");
	        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
	        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
	        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
	        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
		    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		//MagmaCube bossMC = (MagmaCube)loc.getWorld().spawnEntity(loc, EntityType.MAGMA_CUBE);
		MagmaCube bossMC = (MagmaCube)loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), MagmaCube.class);
		bossMC.setMetadata("boss-nether", new FixedMetadataValue(Main.getInstance(), "boss-nether"));
		bossMC.setCustomName(name);
		bossMC.setCustomNameVisible(true);
		bossMC.setSize(20);
		bossMC.setCanPickupItems(false);
		bossMC.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 4));
		bossMC.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 4));
		((CraftLivingEntity) bossMC).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D); 
	}
	
	public static Entity spawnCavaleiroNegro(String location) {
		location = location.toUpperCase();
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		//Skeleton cavaleiro = p.getLocation().getBlock().getWorld().spawn(p.getLocation().getBlock().getLocation().add(0.5,0.0,0.5), Skeleton.class);
		Skeleton cavaleiro = (Skeleton)loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), Skeleton.class);
		cavaleiro.setMaxHealth(CavaleiroHP);
		cavaleiro.setHealth(cavaleiro.getMaxHealth());
		cavaleiro.setCustomName("§4§l✠ §8§lCavaleiro negro §a" + (int) cavaleiro.getHealth() + "§7/§a" + (int) cavaleiro.getMaxHealth() + " §c❤");
		cavaleiro.setCustomNameVisible(true);
		
		cavaleiro.getEquipment().setHelmet(getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTgzMWI2MDEwMDdjNzQzMDE1MjVjMGVmZmExZDljZjU2MWVmMWMxYmZmOGU5YjE0ZTE5NzExNDU5NmFkMzVkZiJ9fX0="));
		ItemStack espada = new ItemStack(Material.DIAMOND_SWORD);
		espada.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
		cavaleiro.getEquipment().setItemInHand(espada);
		cavaleiro.getEquipment().setChestplate(new ItemStack(getChestplateBlack()));
		cavaleiro.getEquipment().setLeggings(new ItemStack(getLeggingsBlack()));
		cavaleiro.getEquipment().setBoots(new ItemStack(getBootsBlack()));
		
		cavaleiro.setSkeletonType(SkeletonType.WITHER);
		cavaleiro.setMetadata("cavaleironegro-servo", new FixedMetadataValue(Main.getInstance(), "cavaleironegro-servo"));
		cavaleiro.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4)); //7
		cavaleiro.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		cavaleiro.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
		cavaleiro.setCanPickupItems(false);
		((CraftLivingEntity) cavaleiro).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D); 

		return cavaleiro;
	}
	
	public static Entity spawnServosNether(String location) {
		location = location.toUpperCase();
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		PigZombie servo = loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), PigZombie.class);
		servo.setMaxHealth(servosNetherHP);
		servo.setHealth(servo.getMaxHealth());
		servo.setCustomName("§4§l✠ §8§lGuardião do submundo §a" + (int) servo.getHealth() + "§7/§a" + (int) servo.getMaxHealth() + " §c❤");
		servo.setCustomNameVisible(true);
		
		servo.getEquipment().setHelmet(getSkull("ewogICJ0aW1lc3RhbXAiIDogMTYwMzE2NTMyNTgxMCwKICAicHJvZmlsZUlkIiA6ICJiNzQ3OWJhZTI5YzQ0YjIzYmE1NjI4MzM3OGYwZTNjNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTeWxlZXgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIyODhjNDYzYjYzNTU1N2JjYzczMmIyMzdlZGI5NWQ2ZDJmNjFhNTFmYWRlZGE2NzE1YjQxYzRlNTFhZjBkZSIKICAgIH0KICB9Cn0="));
		ItemStack espada = new ItemStack(Material.STONE_AXE);
		espada.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
		servo.getEquipment().setItemInHand(espada);
		servo.getEquipment().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
		servo.getEquipment().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
		servo.getEquipment().setBoots(new ItemStack(Material.GOLD_BOOTS));
		
		servo.setMetadata("netherservos", new FixedMetadataValue(Main.getInstance(), "netherservos"));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)); //7
		servo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
		servo.setCanPickupItems(false);
		servo.setAngry(true);
		((CraftLivingEntity) servo).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D); 
		
		return servo;
	}
	
	public static Entity spawnCarminidioSkeleton(String location) {
		location = location.toUpperCase();
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		Skeleton carminidio = loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), Skeleton.class);
		carminidio.setMaxHealth(carminidioNetherHP);
		carminidio.setHealth(carminidio.getMaxHealth());
		carminidio.setCustomName("§4§l✠ §8§lArqueiro Carminidio §a" + (int) carminidio.getHealth() + "§7/§a" + (int) carminidio.getMaxHealth() + " §c❤");
		carminidio.setCustomNameVisible(true);
		
		carminidio.getEquipment().setHelmet(getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTg4YjFjZDk1NzQ2NzJlOGUzMjYyZjIxMGMwZGRkYmMwODJlYTc1NjllOGU3MGYwYzA3YjRiZWU3NWUzMmY2MiJ9fX0="));
		ItemStack arco = new ItemStack(Material.BOW);
		arco.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 200);
		arco.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 3);
		carminidio.getEquipment().setItemInHand(arco);
		carminidio.getEquipment().setChestplate(getChestplateBlack());
		carminidio.getEquipment().setLeggings(getLeggingsBlack());
		carminidio.getEquipment().setBoots(getBootsBlack());
		
		carminidio.setMetadata("netherservos", new FixedMetadataValue(Main.getInstance(), "netherservos"));
		carminidio.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)); //7
		carminidio.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		carminidio.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 3));
		carminidio.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 1)); //7
		carminidio.setCanPickupItems(false);
		((CraftLivingEntity) carminidio).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D); 
		
		return carminidio;
	}
	
	public static ItemStack getSkull(String skinUrl) {
		// url = texture data
		String url = skinUrl;  
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
	
	public static ItemStack getChestplateBlack() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.BLACK);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	
	public static ItemStack getLeggingsBlack() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.BLACK);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	
	public static ItemStack getBootsBlack() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.BLACK);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	private static boolean poder;
	public static void enableMortalhaDaNoite() {
		if (EventController.admin.getNetherOcorrendo()) {
			poder = false;
			float radius = 20.0f;
			final String worldName = "Eventos";
		
			for (Player participantes : EventController.admin.getNetherParticipantes()) {
				participantes.sendTitle("§8§lMORTALHA DA NOITE", "§6Mantenham distância do §lBOSS§6 para não morrerem");
				participantes.sendMessage(" ");
				participantes.sendMessage("§c§l[!] §3A Mortalha da Noite será ativa! Fiquem a "
						+ "§3pelo menos 20 blocos de distância do boss para que não morram.");
				participantes.sendMessage(" ");
			}
			
			(new BukkitRunnable() {
				int tempo = 6;
				@Override
				public void run() {
					if (tempo > 0) {
						tempo--;
						return;
					}
					if (tempo == 0) {
						cancel();
						poder = true;
					}

				}
			}).runTaskTimer(Main.getInstance(), 0L, 20L);
			//durar entre 15 a 30 segundos
					new BukkitRunnable() {

						@Override
						public void run() {
							if (poder == true) {
								if (EventController.admin.getMortalhaDaNoiteStatus() == true) {
									if (Bukkit.getServer().getWorld(worldName) != null) {
								for (Entity entities : Bukkit.getServer().getWorld(worldName).getEntities()) {
									if ((entities != null && entities instanceof MagmaCube) && entities.hasMetadata("boss-nether")) {
										LivingEntity boss = (MagmaCube) entities;
								 Location location1 = boss.getEyeLocation();
				                Location location2 = boss.getEyeLocation();
				                 Location location3 = boss.getEyeLocation();
					                int particles = 50;
					                for (int i = 0; i < particles; i++) {
					                    double angle, x, z;
					                    angle = 2 * Math.PI * i / particles;
					                    x = Math.cos(angle) * radius;
					                    z = Math.sin(angle) * radius;
					                    location1.add(x, 0, z);
					                    location2.add(x, -0.66, z);
					                    location3.add(x, -1.33, z);
					                    Particles packet = new Particles(EnumParticle.VILLAGER_ANGRY, location1, 0, 0, 0, 0, 1);
					                    location1.subtract(x, 0, z);
					                    location2.subtract(x, -0.66, z);
					                    location3.subtract(x, -1.33, z);
					                    packet.sendToAll();	
					                }
					                
					    			for  (Entity nearby : getNearbyEntities(boss.getLocation(), ((int) radius+3)) )  {
					    				if (nearby instanceof Player && EventController.admin.getNetherParticipantes().contains(nearby)) {
					    					Player jogador = (Player) nearby;
					    					jogador.sendMessage("");
					    					jogador.sendMessage(" §4§lVocê está sendo pego pela mortalha da noite, se afaste!");
					    					jogador.sendMessage("");
				    					final int damagePerSecond = 4;
					    					if ((jogador.getHealth()/2) > damagePerSecond) {
					    						jogador.setHealth((jogador.getHealth() - (damagePerSecond*2)));
					    					}
					    					if ((jogador.getHealth()/2) < damagePerSecond) {
					    						jogador.setHealth(0);
					    						EventController.admin.getNetherParticipantes().remove(jogador);
					    					
								    				}
								    			}
											}
										}
									}
								}
							} else {
								for (Player participantes : EventController.admin.getNetherParticipantes()) {
									participantes.sendTitle("§8§lMORTALHA DA NOITE", "§6O efeito se desativou temporariamente");
									participantes.sendMessage(" ");
									participantes.sendMessage("§c§l[!] §3A Mortalha da Noite foi desativada!");
									participantes.sendMessage(" ");
								}
								cancel();
							}
								
						}
					}
			}.runTaskTimer(Main.getInstance(), 0L, 20L);
		} else {
			EventController.admin.setMortalhaDaNoiteStatus(false);
		}
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
}
