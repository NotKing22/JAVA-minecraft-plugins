package me.zsnow.eventnexus;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;

import me.zsnow.eventnexus.config.Configs;
import me.zsnow.eventnexus.listeners.Listeners;
import me.zsnow.eventnexus.task.Task;

public class Commands implements CommandExecutor {
	
	public static boolean EventoAberto;
	public static boolean EntradaLiberada;
	public static ArrayList<Player> AZUL = new ArrayList<>();
	public static ArrayList<Player> VERMELHO = new ArrayList<>();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nexus")) {
			if (!(sender instanceof Player)) return false;
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			if (p.hasPermission("zs.gerente")) {
				p.sendMessage("§3§lEVENTO NEXUS");
				p.sendMessage(" ");
				p.sendMessage(" §f/nexus entrar");
				p.sendMessage(" §f/nexus sair");
				p.sendMessage(" §f/nexus info");
				p.sendMessage(" §c/nexus moderar §7- Entra no evento.");
				p.sendMessage(" §c/nexus modsair §7- Sai do evento.");
				p.sendMessage(" §c/nexus abrir §7- Abre o evento.");
				p.sendMessage(" §c/nexus parar §7- Para o evento.");
				p.sendMessage(" §c/nexus setentrada §7- Define a entrada.");
				p.sendMessage(" §c/nexus setsaida §7- Define a saída.");
				p.sendMessage(" §c/nexus definirspawn [AZUL/VERMELHO] §7- Define onde nascerão");
				p.sendMessage(" §c/nexus inibidorSpawn [AZUL/VERMELHO] §7- Define spawn do nexus.");
				p.sendMessage(" ");
				return true;
			}
			if (p.hasPermission("zs.mod")) {
				p.sendMessage("§3§lEVENTO NEXUS");
				p.sendMessage(" ");
				p.sendMessage(" §f/nexus entrar");
				p.sendMessage(" §f/nexus sair");
				p.sendMessage(" §f/nexus info");
				p.sendMessage(" §c/nexus moderar §7- Entra no evento.");
				p.sendMessage(" §c/nexus modsair §7- Sai do evento.");
				p.sendMessage("");
			}
			p.sendMessage("§3§lEVENTO NEXUS");
			p.sendMessage(" ");
			p.sendMessage(" §f/nexus entrar");
			p.sendMessage(" §f/nexus sair");
			p.sendMessage(" §f/nexus info");
			p.sendMessage("");
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("teste1")) {
				if (p.hasPermission("zs.gerente")) {
					int HP = Configs.config.getConfig().getInt("HP-nexus-vermelho");
					EnderCrystal ender = (EnderCrystal)p.getWorld().spawnEntity(p.getLocation(), EntityType.ENDER_CRYSTAL);
					ender.setCustomName("§d§lNEXUS §7- §e§lVERMELHO §7(§aHP: §f"+ (int) HP +"§7)");
					ender.setCustomNameVisible(true);
				}
			}
			if (args[0].equalsIgnoreCase("teste2")) {
				if (p.hasPermission("zs.gerente")) {
					int HP = Configs.config.getConfig().getInt("HP-nexus-azul");
					EnderCrystal ender = (EnderCrystal)p.getWorld().spawnEntity(p.getLocation(), EntityType.ENDER_CRYSTAL);
					ender.setCustomName("§d§lNEXUS §7- §e§lAZUL §7(§aHP: §f"+ (int) HP +"§7)");
					ender.setCustomNameVisible(true);
				}
			}
			if (args[0].equalsIgnoreCase("entrar")) {
				if (Configs.positions.getString("Entrada") != null) {
					if ((EventoAberto == true) && (EntradaLiberada == true)) {
						if (!(AZUL.contains(p) || (VERMELHO.contains(p)))) {
							PlayerInventory inv = p.getInventory();
							for (ItemStack i : inv.getContents()) {
								if(i != null && !(i.getType() == Material.AIR)) {
									p.sendMessage("§cEsvazie seu inventário para entrar no evento.");
									return true;
								} 
							} for (ItemStack i : inv.getArmorContents()) {
									if(i != null && !(i.getType() == Material.AIR)) {
										p.sendMessage("§cRetire sua armadura para entrar no evento.");
										return true;
									}
								}
							for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
								p.removePotionEffect(AllPotionEffects.getType());
							}
							Random random = new Random();
					        if (VERMELHO.size() == AZUL.size()) {
					            if (random.nextBoolean()) {
					                VERMELHO.add(p);
					            } else {
					                AZUL.add(p);
					            }
					        } else {
					            if (VERMELHO.size() < AZUL.size()) {
					                VERMELHO.add(p);
					            } else {
					                AZUL.add(p);
					            }
					        }
					        p.sendMessage("§2§lNEXUS: §eVocê entrou no time: " + translateTeam(p));
					        sendEnter(p);
					        giveItens(p);
					        return true;
						}
						p.sendMessage("§cVocê já está dentro do evento.");
						return true;
					}
					p.sendMessage("§cO evento não está aberto ou sua entrada foi fechada.");
					return true;
				}
				p.sendMessage("§cA entrada do evento não foi definida pelo ADMINISTRADOR.");
			}
			if (args[0].equalsIgnoreCase("sair")) {
				if (AZUL.contains(p) || (VERMELHO.contains(p))) {
					if (Configs.positions.getString("Saida") != null) { 
						p.sendMessage("§2§lNEXUS: §cVocê saiu do evento.");
						p.getInventory().clear();
						p.getInventory().setArmorContents(null);
						AZUL.remove(p);
						VERMELHO.remove(p);
						for (PotionEffect AllPotionEffects : p.getActivePotionEffects()) {
							p.removePotionEffect(AllPotionEffects.getType());
						}
						sendExit(p);
						return true;
					}
					p.sendMessage("§cA saida do evento não foi definida pelo ADMINISTRADOR");
					return true;
				}
				p.sendMessage("§cVocê não está no evento.");
			}
			if (args[0].equalsIgnoreCase("info")) {
				p.sendMessage(" ");
				p.sendMessage("§2§lNEXUS §7- §f§lINFO");
				p.sendMessage("");
				p.sendMessage(" §eTime §3AZUL§e:§f " + AZUL.size() + " §eparticipante(s)");
				p.sendMessage(" §eVida do Inibidor §3AZUL§e:§a " + Listeners.HPazul);
				p.sendMessage(" §eTime §cVERMELHO§e: §f" + VERMELHO.size() +  " §eparticipante(s)");
				p.sendMessage(" §eVida do Inibidor §cVERMELHO§e: " + "§a" + Listeners.HPvermelho);
				p.sendMessage(" ");
			}
			if (args[0].equalsIgnoreCase("moderar")) {
				if (p.hasPermission("zs.mod")) {
					sendEnter(p);
					p.sendMessage("§c§lNEXUS: §7§oVocê entrou no evento para moderar.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("setentrada")) {
				if (p.hasPermission("zs.gerente")) {
					setLoc(p, "Entrada");
					p.sendMessage("§c§lNEXUS: §7§oA entrada do evento foi definida.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("setsaida")) {
				if (p.hasPermission("zs.gerente")) {
					setLoc(p, "Saida");
					p.sendMessage("§c§lNEXUS: §7§oA saída do evento foi definida.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("abrir") || (args[0].equalsIgnoreCase("iniciar"))) {
				if (p.hasPermission("zs.gerente")) {
					if ((!EventoAberto == true) && (!EntradaLiberada == true)) {
						EventoAberto = true;
						EntradaLiberada = true;
						Listeners.PvPAtivado = false;
						AZUL.clear();
						VERMELHO.clear();
						removeEntities();
						Task.EntradaTimer();
						return true;
					}
					p.sendMessage("§cO evento já está aberto.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("moderar")) {
				if (p.hasPermission("zs.mod")) {
					if (EventoAberto == true) {
						sendEnter(p);
						p.sendMessage("§eVocê entrou no evento para moderar.");
						return true;
					}
					p.sendMessage("§cO evento não está aberto.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("modsair")) {
				if (p.hasPermission("zs.mod")) {
					sendExit(p);
					p.sendMessage("§eVocê saiu do evento.");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("parar") || (args[0].equalsIgnoreCase("finalizar"))) {
				if (p.hasPermission("zs.gerente")) {
					if (EventoAberto == true) {
						EventoAberto = false;
						EntradaLiberada = false;
						for (Player p1 : AZUL) {
							p1.getInventory().clear();
							p1.getInventory().setArmorContents(null);
							sendExit(p1);
							for (PotionEffect AllPotionEffects : p1.getActivePotionEffects()) {
								p1.removePotionEffect(AllPotionEffects.getType());
							}
						}
						for (Player p2 : VERMELHO) {
							p2.getInventory().clear();
							p2.getInventory().setArmorContents(null);
							sendExit(p2);
							for (PotionEffect AllPotionEffects : p2.getActivePotionEffects()) {
								p2.removePotionEffect(AllPotionEffects.getType());
							}
						}
						AZUL.clear();
						VERMELHO.clear();
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§2§lNEXUS §7- §c§lENCERRADO");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§cUm Administrador forçou o encerramento do evento.");
						Bukkit.broadcastMessage("");
						World mundo = Bukkit.getServer().getWorld(Configs.positions.getConfig().getString("Entrada.Mundo"));
						for (Entity entidades : mundo.getEntities()) {
							if ((entidades.getCustomName() != null) && (entidades.isCustomNameVisible())
									&& (entidades.getCustomName().contains("§d§lNEXUS §7-"))) {
										entidades.remove();
							}
						}
						return true;
					}
					p.sendMessage("§cO evento não está ocorrendo!");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("definirspawn")) {
				if (p.hasPermission("zs.gerente")) {
					p.sendMessage("§2§lNEXUS §c/nexus definirspawn [AZUL/VERMELHO]");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
			if (args[0].equalsIgnoreCase("inibidorspawn")) {
				if (p.hasPermission("zs.gerente")) {
					p.sendMessage("§2§lNEXUS §c/nexus inibidorspawn [AZUL/VERMELHO]");
					return true;
				}
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
		}
		if (args.length >= 2) { 
			if (p.hasPermission("zs.gerente")) {
				if (args[0].equalsIgnoreCase("inibidorspawn")) {
					if (args[1].equalsIgnoreCase("VERMELHO")) {
						setLoc(p, "nexus-vermelho");
						p.sendMessage("§c§lNEXUS: §7§oInibidor vermelho teve seu spawn definido.");
						return true;
					}
					if (args[1].equalsIgnoreCase("AZUL")) {
						setLoc(p, "nexus-azul");
						p.sendMessage("§c§lNEXUS: §7§oInibidor azul teve seu spawn definido.");
						return true;
					} else {
						p.sendMessage("§2§lNEXUS §fO inibidor precisa ser 'AZUL' ou 'VERMELHO'.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("definirspawn")) {
					if (args[1].equalsIgnoreCase("VERMELHO")) {
						setLoc(p, "spawn-vermelho");
						p.sendMessage("§c§lNEXUS: §7§oO time VERMELHO teve seu spawn definido.");
						return true;
					}
					if (args[1].equalsIgnoreCase("AZUL")) {
						setLoc(p, "spawn-azul");
						p.sendMessage("§c§lNEXUS: §7§oO time AZUL teve seu spawn definido.");
						return true;
					} else {
						p.sendMessage("§2§lNEXUS §fOs times precisam ser 'AZUL' ou 'VERMELHO'.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("anunciar")) {
						for (Player p1 : AZUL) {
							p1.sendMessage(" ");
							p1.sendMessage("§6[!] §c[MANAGER] " +p.getName()+ "§7: " + getMensagem(args).replace("&", "§"));
							p1.sendMessage(" ");
							p1.playSound(p1.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
						}
						for (Player p2 : VERMELHO) {
							p2.sendMessage(" ");
							p2.sendMessage("§6[!] §c[MANAGER] " +p.getName()+ "§7: " + getMensagem(args).replace("&", "§"));
							p2.sendMessage(" ");
							p2.playSound(p2.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
						}
						if (!AZUL.contains(p) || (!VERMELHO.contains(p))) {
							p.sendMessage(" ");
							p.sendMessage("§6[!] §c[MANAGER] " +p.getName()+ "§7: " + getMensagem(args).replace("&", "§"));
							p.sendMessage(" ");
							p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
							return true;
					}
				}
			} else {
				p.sendMessage("§cVocê precisa do cargo Gerente ou superior para executar este comando.");
				return true;
			}
		}
		return false;
	}
	
	public static void setLoc(Player p, String LocationName) {
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Configs.positions.getConfig().set(LocationName + ".X", Double.valueOf(x));
		Configs.positions.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Configs.positions.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Configs.positions.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Configs.positions.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Configs.positions.getConfig().set(LocationName + ".Mundo", world);
		Configs.positions.saveConfig();
	}
	
	private String translateTeam(Player p) {
		if (VERMELHO.contains(p)) {
			return "§c§lVERMELHO";
		}
		if (AZUL.contains(p)) {
			return "§3§lAZUL";
		} else {
			return "§7[?]";
		}
	}

	private void sendEnter(Player p) {
		double X = Configs.positions.getConfig().getDouble("Entrada.X");
        double Y = Configs.positions.getConfig().getDouble("Entrada.Y");
        double Z = Configs.positions.getConfig().getDouble("Entrada.Z");
        float Yaw = (float)Configs.positions.getConfig().getLong("Entrada.Yaw");
        float Pitch = (float)Configs.positions.getConfig().getLong("Entrada.Pitch");
	    World Mundo = Bukkit.getWorld(Configs.positions.getString("Entrada.Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
		p.teleport(loc);
	}
	
	private static void giveItens(Player p) {
		if (VERMELHO.contains(p)) {
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
			p.getInventory().setHelmet(lhelmet);
			p.getInventory().setChestplate(lhelmet2);
			p.getInventory().setLeggings(lhelmet3);
			p.getInventory().setBoots(lhelmet4);
		}
		if (AZUL.contains(p)) {
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
			p.getInventory().setHelmet(lhelmet);
			p.getInventory().setChestplate(lhelmet2);
			p.getInventory().setLeggings(lhelmet3);
			p.getInventory().setBoots(lhelmet4);
	}
}

	private void sendExit(Player p) {
	  double X = Configs.positions.getConfig().getDouble("Saida.X");
      double Y = Configs.positions.getConfig().getDouble("Saida.Y");
      double Z = Configs.positions.getConfig().getDouble("Saida.Z");
  	  float Yaw = (float)Configs.positions.getConfig().getLong("Saida.Yaw");
  	  float Pitch = (float)Configs.positions.getConfig().getLong("Saida.Pitch");
	  World Mundo = Bukkit.getWorld(Configs.positions.getConfig().getString("Saida.Mundo"));
  	  Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
	  p.teleport(loc);
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
		
	private String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
}
