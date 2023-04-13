package me.zsnow.spookyrealm;

import java.lang.reflect.Field;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.zsnow.spookyrealm.configs.Configs;
import net.minecraft.server.v1_8_R3.GenericAttributes;

public class EntityManagement {

	private static EntityManagement instance = new EntityManagement();
	
	public static EntityManagement getInstance() {
		return instance;
	}
	
	public Entity spawnGhostFace(int numberLoc) {
		String location = "MOBSPAWN";
		double X = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + "." + numberLoc + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);

		Skeleton servo = loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), Skeleton.class);
		servo.setMaxHealth(100);
		servo.setHealth(servo.getMaxHealth());
		servo.setCustomName("§8§lGhost-Face §a" + (int) servo.getHealth() + "§7/§a" + (int) servo.getMaxHealth() + " §c❤");
		servo.setCustomNameVisible(true);
		
		servo.getEquipment().setHelmet(getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzIxOTYxNjQyZDk4Y2I4MDFhMTc2MDhiYTRhMjMyOTc3YjQ2MmVmNjY3OWI5NzhjOWJiNjQ5NWQxNTE2MjczIn19fQ=="));
		ItemStack espada = new ItemStack(Material.STONE_HOE);
		espada.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
		servo.getEquipment().setItemInHand(espada);
		servo.getEquipment().setChestplate(getChestplateBlack());
		servo.getEquipment().setLeggings(getLeggingsBlack());
		servo.getEquipment().setBoots(getBootsBlack());
		
		servo.setMetadata("vilarejo", new FixedMetadataValue(Main.getInstance(), "vilarejo"));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)); //7
		servo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
		servo.setCanPickupItems(false);
		//servo.setAngry(true);
		((CraftLivingEntity) servo).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D); 
		
		return servo;
	}
	
	public Entity spawnVampire(int numberLoc) {
		String location = "MOBSPAWN";
		double X = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + "." + numberLoc + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		Skeleton servo = loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), Skeleton.class);
		servo.setSkeletonType(SkeletonType.WITHER);
		servo.setMaxHealth(100);
		servo.setHealth(servo.getMaxHealth());
		servo.setCustomName("§8§lJack Ripper §a" + (int) servo.getHealth() + "§7/§a" + (int) servo.getMaxHealth() + " §c❤");
		servo.setCustomNameVisible(true);
		
		servo.getEquipment().setHelmet(getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdjNTE4MDA4YjJhYjEyNWIzNmUwNGY2NTg3NDA3NTBlMzg3NmYxZWI2ZDI0YTNiMTA2YjdjN2JlNDkzNGYwMyJ9fX0="));
		ItemStack espada = new ItemStack(Material.IRON_SWORD);
		espada.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
		servo.getEquipment().setItemInHand(espada);
		servo.getEquipment().setChestplate(getChestplatePurple());
		servo.getEquipment().setLeggings(getLeggingsPurple());
		servo.getEquipment().setBoots(getBootsPurple());
		
		servo.setMetadata("vilarejo", new FixedMetadataValue(Main.getInstance(), "vilarejo"));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)); //7
		servo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
		servo.setCanPickupItems(false);
		//servo.setAngry(true);
		((CraftLivingEntity) servo).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D); 
		
		return servo;
	}
	
	public Entity spawnCriaturaMaligna(int numberLoc) {
		String location = "MOBSPAWN";
		double X = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + "." + numberLoc + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + "." + numberLoc + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + "." + numberLoc + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		PigZombie servo = loc.getBlock().getWorld().spawn(loc.getBlock().getLocation().add(0.5,0.0,0.5), PigZombie.class);
		servo.setMaxHealth(100);
		servo.setHealth(servo.getMaxHealth());
		servo.setCustomName("§8§lCriatura maligna §a" + (int) servo.getHealth() + "§7/§a" + (int) servo.getMaxHealth() + " §c❤");
		servo.setCustomNameVisible(true);
		
		servo.getEquipment().setHelmet(getSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg1ZmJmYzllNTMzYmQ5MDIwMjNhNmVhYzIyZWFiNzU3NjBlNDE2YzYyNDRjN2MwNjYwYThlMDlmMDdlZDAxYyJ9fX0="));
		ItemStack espada = new ItemStack(Material.GOLD_SWORD);
		espada.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 4);
		servo.getEquipment().setItemInHand(espada);
		servo.getEquipment().setChestplate(getChestplateBlack());
		servo.getEquipment().setLeggings(getLeggingsBlack());
		servo.getEquipment().setBoots(getBootsBlack());
		
		servo.setMetadata("vilarejo", new FixedMetadataValue(Main.getInstance(), "vilarejo"));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2)); //7
		servo.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
		servo.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
		servo.setCanPickupItems(false);
		servo.setAngry(true);
		((CraftLivingEntity) servo).getHandle().getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(30.0D); 
		
		return servo;
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
	
	public static ItemStack getChestplatePurple() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.PURPLE);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	
	public static ItemStack getLeggingsPurple() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.PURPLE);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	
	public static ItemStack getBootsPurple() {
		ItemStack LeatherHelmet1 = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta meta = (LeatherArmorMeta) LeatherHelmet1.getItemMeta();
		meta.setColor(Color.PURPLE);
		LeatherHelmet1.setItemMeta(meta);
		LeatherHelmet1.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		return LeatherHelmet1;
	}
	
	
}
