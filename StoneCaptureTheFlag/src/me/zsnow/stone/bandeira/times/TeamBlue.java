package me.zsnow.stone.bandeira.times;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBanner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import me.zsnow.stone.bandeira.configs.Configs;

public class TeamBlue {

	private static TeamBlue instance = new TeamBlue();
	
	public static TeamBlue getInstance() {
		return instance;
	}
	
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public ArrayList<Player> getTeamBlueList() {
		return participantes;
	}
	
	public ArrayList<Player> getParticipantes() {
		return participantes;
	}
	
	private HashMap<Player, Boolean> team = new HashMap<>();
	
	public Boolean getTeam(Player p) {
		if (team.get(p) == null) return false;
		return team.get(p);
	}
	
	public void setPlayerTeam(Player p) {
		team.put(p, true);
	}
	
	public void removePlayerTeam(Player p) {
		team.remove(p);
	}
	
	public void clearTeam() {
		team.clear();
		getTeamBlueList().clear();
	}
	
	public int getTeamBlueSize() {
		return participantes.size();
	}
	
	private boolean has_flag_placed;
	
	public boolean getHasFlagPlaced() {
		return this.has_flag_placed;
	}
	
	public void setFlagPlacedStatus(boolean booleanValue) {
		has_flag_placed = booleanValue;
		
		String local = "BLUE_BANNER";
		double X,Y,Z;
		float Yaw,Pitch;
			X = Configs.locations.getConfig().getDouble(local + ".X");
	        Y = Configs.locations.getConfig().getDouble(local + ".Y");
	        Z = Configs.locations.getConfig().getDouble(local + ".Z");
	        Yaw = (float)Configs.locations.getConfig().getLong(local + ".Yaw");
	        Pitch = (float)Configs.locations.getConfig().getLong(local + ".Pitch");
		    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + ".Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		if (loc != null) {
			if (booleanValue == true) {
				 final Block block = loc.getBlock();
				 	block.setType(Material.STANDING_BANNER);
	               CraftBanner cBanner = new CraftBanner(block);
	               cBanner.setBaseColor(DyeColor.LIGHT_BLUE);
	               cBanner.update(true);
			} else {
				 final Block block = loc.getBlock();
				 block.setType(Material.AIR);
			}
		} else {
			Bukkit.getServer().getConsoleSender().sendMessage("§c[StoneCaptureTheFlag] Uma location nao foi definida. (BLUE_BANNER)");
		}
	}
	
	
	private Player player;
	
	public void clearPlayerWithFlag() {
		player = null;
	}
	
	public Player getPlayerWithFlag() {
		return player;
	}
	
	public Boolean playerHasTheFlag(Player alvo) {
		if (getPlayerWithFlag() == alvo);
		   return true;
	}
	
	public void setPlayerWithFlag(Player alvo) {
			//
			ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
			LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
			lam2.setColor(Color.fromRGB(0, 170, 170));
			lhelmet2.setItemMeta(lam2);
			lhelmet2.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet2.getItemMeta().spigot().setUnbreakable(true);
			//
			ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
			LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
			lam3.setColor(Color.fromRGB(0, 170, 170));
			lhelmet3.setItemMeta(lam3);
			lhelmet3.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet3.getItemMeta().spigot().setUnbreakable(true);
			//
			ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
			LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
			lam4.setColor(Color.fromRGB(0, 170, 170));
			lhelmet4.setItemMeta(lam4);
			lhelmet4.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			lhelmet4.getItemMeta().spigot().setUnbreakable(true);
			
		    ItemStack item = new ItemStack(Material.BANNER, 1);
		    BannerMeta bannerMeta = (BannerMeta) item.getItemMeta();
		    bannerMeta.setBaseColor(DyeColor.RED);
		    item.setItemMeta(bannerMeta);
			
		    ItemStack cap = new ItemStack(Material.GOLDEN_APPLE, 3);
		    
		    alvo.getInventory().addItem(cap);
			alvo.getInventory().setHelmet(item);
			alvo.getInventory().setChestplate(lhelmet2);
			alvo.getInventory().setLeggings(lhelmet3);
			alvo.getInventory().setBoots(lhelmet4);
			player = alvo;
	}
	
	
	public void sendTeamArmorToPlayer(Player p) {
		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
		lam.setColor(Color.fromRGB(0, 170, 170));
		lhelmet.setItemMeta(lam);
		lhelmet.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
		lam2.setColor(Color.fromRGB(0, 170, 170));
		lhelmet2.setItemMeta(lam2);
		lhelmet2.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
		lam3.setColor(Color.fromRGB(0, 170, 170));
		lhelmet3.setItemMeta(lam3);
		lhelmet3.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
		lam4.setColor(Color.fromRGB(0, 170, 170));
		lhelmet4.setItemMeta(lam4);
		lhelmet4.getItemMeta().spigot().setUnbreakable(true);
		
		ItemStack sword = new ItemStack(Material.WOOD_SWORD, 1);
		sword.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().addItem(sword);
		p.getInventory().setHelmet(lhelmet);
		p.getInventory().setChestplate(lhelmet2);
		p.getInventory().setLeggings(lhelmet3);
		p.getInventory().setBoots(lhelmet4);
	}
	
	public void sendTeamArmorToPlayer2(Player p) {
		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
		lam.setColor(Color.fromRGB(0, 170, 170));
		lhelmet.setItemMeta(lam);
		lhelmet.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
		lam2.setColor(Color.fromRGB(0, 170, 170));
		lhelmet2.setItemMeta(lam2);
		lhelmet2.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
		lam3.setColor(Color.fromRGB(0, 170, 170));
		lhelmet3.setItemMeta(lam3);
		lhelmet3.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
		lam4.setColor(Color.fromRGB(0, 170, 170));
		lhelmet4.setItemMeta(lam4);
		lhelmet4.getItemMeta().spigot().setUnbreakable(true);
		
		p.getInventory().setHelmet(lhelmet);
		p.getInventory().setChestplate(lhelmet2);
		p.getInventory().setLeggings(lhelmet3);
		p.getInventory().setBoots(lhelmet4);
	}
}
