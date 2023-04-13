package me.zsnow.stone.paintball.times;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class TeamRed {

	private static TeamRed instance = new TeamRed();
	
	public static TeamRed get() {
		return instance;
	}
	
	private ArrayList<Player> participantes = new ArrayList<>();
	
	public ArrayList<Player> getTeamRedList() {
		return participantes;
	}
	
	public int getTeamRedSize() {
		return participantes.size();
	}
	
	public HashMap<Player, Integer> abates = new HashMap<>();
	
	public int getAbates(Player player) {
		return abates.get(player) == null ? 0 : abates.get(player);
	}
	
	public void updateAbates(Player player) {
		if (abates.containsKey(player)) {
			abates.replace(player, getAbates(player)+1);
		} else {
			abates.put(player, 1);
		}
	}
	
	public void putAbates(Player p) {
		abates.put(p, 0);
	}
	
	public void sendTeamArmorToPlayer(Player p) {
		ItemStack lhelmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)lhelmet.getItemMeta();
		lam.setColor(Color.fromRGB(150,50,50));
		lhelmet.setItemMeta(lam);
		lhelmet.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet2 = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam2 = (LeatherArmorMeta)lhelmet2.getItemMeta();
		lam2.setColor(Color.fromRGB(150,50,50));
		lhelmet2.setItemMeta(lam2);
		lhelmet2.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet3 = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta lam3 = (LeatherArmorMeta)lhelmet3.getItemMeta();
		lam3.setColor(Color.fromRGB(150,50,50));
		lhelmet3.setItemMeta(lam3);
		lhelmet3.getItemMeta().spigot().setUnbreakable(true);
		//
		ItemStack lhelmet4 = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lam4 = (LeatherArmorMeta)lhelmet4.getItemMeta();
		lam4.setColor(Color.fromRGB(150,50,50));
		lhelmet4.setItemMeta(lam4);
		lhelmet4.getItemMeta().spigot().setUnbreakable(true);
		p.getInventory().setHelmet(lhelmet);
		p.getInventory().setChestplate(lhelmet2);
		p.getInventory().setLeggings(lhelmet3);
		p.getInventory().setBoots(lhelmet4);
	}
	
}
