package me.zsnow.eventnexus.task;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.eventnexus.Commands;
import me.zsnow.eventnexus.config.Configs;
import me.zsnow.eventnexus.listeners.Listeners;

public class Task {
	
	private static ArrayList<Player> azulTeam = Commands.AZUL;
	private static ArrayList<Player> vermelhoTeam = Commands.VERMELHO;
	// pvp ativado tava dando unused msm ativo
	
	public static void EntradaTimer(){
		new BukkitRunnable(){
			int tempo = 121;

       public void run(){
    	   
			if ((this.tempo <= 121) && (Commands.EventoAberto == true)) {
				this.tempo--;
    				if ((azulTeam.size() + vermelhoTeam.size()) != 60) {
		    				if (this.tempo >= 120) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 100) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 80) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 60) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 40) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 20) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 10) {
		    					Broadcast(tempo);
		    				}
		    				if (this.tempo == 0) {
		    					cancel();
		    					Commands.EntradaLiberada = false;
		    					Bukkit.broadcastMessage("");
		    					Bukkit.broadcastMessage("§2§lNEXUS §7- §f§lENTRADA FECHADA");
		    					Bukkit.broadcastMessage("");
		    					Bukkit.broadcastMessage(" §aParticipantes: §f" + (azulTeam.size() + vermelhoTeam.size()));
		    					Bukkit.broadcastMessage("");
		    					LiberarPvP();
		    				}
	    			} else {
	    				cancel();
	    				Commands.EntradaLiberada = false;
    					Bukkit.broadcastMessage("");
    					Bukkit.broadcastMessage("§2§lNEXUS §7- §f§lENTRADA FECHADA");
    					Bukkit.broadcastMessage("");
    					Bukkit.broadcastMessage(" §aParticipantes: §f" + ((azulTeam.size() + vermelhoTeam.size()) + "/60"));
    					Bukkit.broadcastMessage("");
    					LiberarPvP();
	    				}
	    			} else {
	    				cancel();
	    			}
	    		}
		}.runTaskTimer(me.zsnow.eventnexus.Main.getInstance(), 0L, 20L);
	}
	
	public static void LiberarPvP(){
		new BukkitRunnable(){
	       int tempo = 31;

	    		public void run(){
	    			if ((this.tempo <= 31) && (Commands.EventoAberto == true) && (Commands.EntradaLiberada == false)) {
	    				this.tempo--;
		    				if (this.tempo == 30) {
		    					Broadcast2(tempo);
		    				}
		    				if (this.tempo == 15) {
		    					Broadcast2(tempo);
		    				}
		    				if (this.tempo == 5) {
		    					Broadcast2(tempo);
		    				}
		    				if (this.tempo == 0) {
		    					cancel();
		    					Commands.EntradaLiberada = false;
		    					Broadcast3();
		    					for (Player p1 : azulTeam) {
		    						sendEnter(p1, "spawn-azul");
		    						p1.sendMessage("§2§lNEXUS: §aDestrua o NEXUS inimigo para vencer!");
		    						p1.sendMessage("§aCaso morra, você renascerá!");
		    						giveItens(p1);
		    					}
		    					for (Player p2 : vermelhoTeam) {
		    						sendEnter(p2, "spawn-vermelho");
		    						p2.sendMessage("§2§lNEXUS: §aDestrua o NEXUS inimigo para vencer!");
		    						p2.sendMessage("§aCaso morra, você renascerá!");
		    						giveItens(p2);
		    					}
		    					
		    					Location redLoc = new Location(Bukkit.getWorld(Configs.positions.getString("nexus-vermelho.Mundo")), Configs.positions.getDouble("nexus-vermelho.X"), Configs.positions.getDouble("nexus-vermelho.Y"), Configs.positions.getDouble("nexus-vermelho.Z"));
		    					 Entity NexusVermelho = Bukkit.getWorld(Configs.positions.getString("nexus-vermelho.Mundo")).spawnEntity(redLoc, EntityType.ENDER_CRYSTAL);
		    					 NexusVermelho.setCustomName("§d§lNEXUS §7- §e§lVERMELHO §7(§aHP: §f"+ Configs.config.getInt("HP-nexus-vermelho") +"§7)");
		    					 NexusVermelho.setCustomNameVisible(true);
		    					Location blueLoc = new Location(Bukkit.getWorld(Configs.positions.getString("nexus-azul.Mundo")), Configs.positions.getDouble("nexus-azul.X"), Configs.positions.getDouble("nexus-azul.Y"), Configs.positions.getDouble("nexus-azul.Z"));
		    					 Entity NexusAzul = Bukkit.getWorld(Configs.positions.getString("nexus-vermelho.Mundo")).spawnEntity(blueLoc, EntityType.ENDER_CRYSTAL);
		    					 NexusAzul.setCustomName("§d§lNEXUS §7- §e§lAZUL §7(§aHP: §f"+ Configs.config.getInt("HP-nexus-azul") +"§7)");
		    					 NexusAzul.setCustomNameVisible(true);
		    					 Listeners.PvPAtivado = true;
		    				}
	    			} else {
	    				cancel();
	    			}
	    		}
		}.runTaskTimer(me.zsnow.eventnexus.Main.getInstance(), 0L, 20L);
	}
		
	private static void Broadcast(int tempo) {
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("§2§lNEXUS §7- §f§lEVENTO ABERTO");
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage(" §aPara entrar no evento, use: §f/nexus entrar");
		Bukkit.broadcastMessage(" §aTempo restante: §f" + tempo + " §asegundos");
		Bukkit.broadcastMessage(" §aParticipantes: §f" + ((azulTeam.size() + vermelhoTeam.size())) + "§f/60");
		Bukkit.broadcastMessage("");
	}
	
	private static void Broadcast2(int tempo) {
		for (Player p1 : azulTeam) {
			p1.sendMessage("");
			p1.sendMessage("§2§lNEXUS §7- §c§lPVP");
			p1.sendMessage("");
			p1.sendMessage(" §6O pvp está desativado.");
			p1.sendMessage(" §6Ele será ativo em §e" + tempo + " §6segundos");
			p1.sendMessage(" §6Você será teleportado para sua base.");
			p1.sendMessage("");	
			p1.playSound(p1.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
		}
		for (Player p2 : vermelhoTeam) {
			p2.sendMessage("");
			p2.sendMessage("§2§lNEXUS §7- §c§lPVP");
			p2.sendMessage("");
			p2.sendMessage(" §6O pvp está desativado.");
			p2.sendMessage(" §6Ele será ativo em §e" + tempo + " §6segundos");
			p2.sendMessage(" §6Você será teleportado para sua base.");
			p2.sendMessage("");
			p2.playSound(p2.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
		}
	}
		
		private static void Broadcast3() {
			for (Player p1 : azulTeam) {
				p1.sendMessage("");
				p1.sendMessage("§2§lNEXUS §7- §c§lPVP");
				p1.sendMessage("");
				p1.sendMessage(" §cPVP ATIVADO");
				p1.sendMessage(" §6Destrua o Inibidor inimigo para vencer!" );
				p1.sendMessage("");	
				p1.playSound(p1.getLocation(), Sound.WITHER_SPAWN, 1.0F, 0.5F);
			}
			for (Player p2 : vermelhoTeam) {
				p2.sendMessage("");
				p2.sendMessage("§2§lNEXUS §7- §c§lPVP");
				p2.sendMessage("");
				p2.sendMessage(" §cPVP ATIVADO");
				p2.sendMessage(" §6Destrua o Inibidor inimigo para vencer!" );
				p2.sendMessage("");
				p2.playSound(p2.getLocation(), Sound.WITHER_SPAWN, 1.0F, 0.5F);
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
	
}
