package me.zsnow.spookyrealm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import me.zsnow.spookyrealm.api.ItemBuilder;
import me.zsnow.spookyrealm.api.LocationAPI;
import me.zsnow.spookyrealm.api.LocationAPI.location;

public class Commands implements CommandExecutor {

	int taskID;
	public static boolean entrada;
	
	String storeMenu = "";
	
	ArrayList<Player> participantes = new ArrayList<>();
	String[] horrorMessages = new String[] {
			"§c§l Você sente o olhar de uma presença maligna..", 
			"§c§l O ar está ficando mais frio ao seu redor..",
			"§c§l A Lua de Sangue está subindo...", 
			"§c§l A desgraça certa aproxima-se...",
			"§c§l Um arrepio percorre sua coluna...", 
			"§c§l Você sente um frio na espinha...",
			"§c§l Uma presença maligna passeia entre você...", 
			"§c§l A selva está cada vez mais inquieta...",
			"§c§l Gritos ecoam ao seu redor...",
			"§c§l Você sente um calafrio terrível no corpo inteiro...",};
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vilarejo")) {
			if (!(sender instanceof Player)) return false;
				Player p = (Player) sender;
					if (args.length == 0) {
						openMenu(p);
						return true;
					}
					if (args.length >= 1) {
						if (args[0].equalsIgnoreCase("menushop")) {
							new Listeners().openShop(p);
						}
						if (sender.hasPermission("zs.gerente")) {
							if (args[0].equalsIgnoreCase("help")) {
								p.sendMessage(" ");
								p.sendMessage("  §f✦ §d§lSPOOKY REALM §5- §6§LHALLOWEEN EDITION §F✦");
								p.sendMessage(" ");
								p.sendMessage("   §8* §7/vilarejo §f<open/close>");
								p.sendMessage("   §8* §7/vilarejo §fmrl reset (-s)");
								p.sendMessage("   §8* §7/vilarejo §fsetloc <entrada/saida/mob|1-20|>");
								p.sendMessage("   §8* §7/vilarejo §fgive <type:Candy/Fragment> <Player> <Ammount>");
								p.sendMessage(" ");
								new Listeners().openShop(p);
								return true;
							}
							if (args[0].equalsIgnoreCase("open")) {
								final int minutos = 15;
								if (entrada == false) {
									entrada = true;
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									taskID = sh.scheduleSyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										@Override
										public void run() {
											if (entrada == true) {
												int stack = 0;
												for (World mundo : Bukkit.getWorlds()) { // MUDAR NOME DO MUNDO
													for (Entity entidades : mundo.getEntities()) {
														if (!(entidades instanceof Player) && (entidades.isCustomNameVisible() &&
																entidades.hasMetadata("vilarejo"))) {
															 stack++;
														}
													}
												}
												Bukkit.broadcastMessage(" ");
												Bukkit.broadcastMessage(" §5§lSPOOKY REALM §e➟ §6§lO VILAREJO ESTA SENDO ASSOMBRADO!");
												Bukkit.broadcastMessage(" §d⚒  §ePegue sua armadura e ajude a elimina-las para ser recompensado. §6Acesso rápido com: §d/vilarejo");
												Bukkit.broadcastMessage(" ");
												for (Player membros : participantes) {
													membros.sendMessage("");
													membros.sendMessage(horrorMessages[new Random().nextInt(horrorMessages.length)]);
													membros.sendMessage("");
													membros.playSound(membros.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
												}
												EntityManagement entidade = EntityManagement.getInstance();
												try {
													for (int quantidade = 20; stack <= quantidade; stack++) {
														Random randomMob = new Random();
														if (randomMob.nextInt(4) == 3) {
															Random randomSpawn = new Random();
															entidade.spawnGhostFace((randomSpawn.nextInt(20)));
														}
														if (randomMob.nextInt(4) == 2) {
															Random randomSpawn = new Random();
															entidade.spawnCriaturaMaligna((randomSpawn.nextInt(20) + 1));
															}
														if (randomMob.nextInt(4) == 1) {
															Random randomSpawn = new Random();
															entidade.spawnVampire((randomSpawn.nextInt(20) + 1));
														}
													}
												} catch (IllegalArgumentException e) {
													Bukkit.getConsoleSender().sendMessage("§c[Vilarejo] A localidade dos monstros nao foi definida.");
													sh.cancelTask(taskID);
												}
												Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mrl reset mina_fantasmagorica");
												}
												sh.cancelTask(taskID);
												return;
												}
									}, 0L, 20L*60L*minutos);
								} else {
									if (entrada == true) {
										p.sendMessage("§c[Vilarejo] O vilarejo já está aberto.");
										return true;
									}
								}
								return true;
							}
							if (args[0].equalsIgnoreCase("close")) {
								entrada = false;
								p.sendMessage("§e[Vilarejo] Você fechou o vilarejo.");
								for (Player participantes : participantes) {
									LocationAPI.getLocation().teleportTo(participantes, location.SAIDA);
									participantes.sendMessage("§cUm administrador interditou o vilarejo.");
								}
								return true;
							}
							if (args[0].equalsIgnoreCase("give")) {
								if (args.length >= 4) {
									if (args[1].equalsIgnoreCase("Candy") || args[1].equalsIgnoreCase("doce")) {
										Player target = Bukkit.getPlayer(args[2]);
										if (target == null) {
											p.sendMessage("§cO jogador não se encontra online.");
											return true;
										}
										int quantidade = Integer.valueOf(0);
										try {
											quantidade = Integer.valueOf(Integer.parseInt(args[3]));
										} catch (NumberFormatException e) {
											sender.sendMessage("§cInsira valor válido para enviar ao jogador.");
											return true;
										}
										final ItemStack item = new Listeners().getCandy(quantidade);
										target.getInventory().addItem(item);
										p.sendMessage("§aVocê enviou " + quantidade + " doces para " + target.getName());
										return true;
									}
									if (args[1].equalsIgnoreCase("Fragment") || args[1].equalsIgnoreCase("Fragmento")) {
										Player target = Bukkit.getPlayer(args[2]);
										if (target == null) {
											p.sendMessage("§cO jogador não se encontra online.");
											return true;
										}
										int quantidade = Integer.valueOf(0);
										try {
											quantidade = Integer.valueOf(Integer.parseInt(args[3]));
										} catch (NumberFormatException e) {
											sender.sendMessage("§cInsira valor válido para enviar ao jogador.");
											return true;
										}
										final ItemStack item = new Listeners().getFragmento(quantidade);
										target.getInventory().addItem(item);
										p.sendMessage("§aVocê enviou " + quantidade + " fragmentos para " + target.getName());
										return true;
									}
								}
								p.sendMessage("§cUse: /vilarejo give <type:Candy/Fragment> <Player> <Ammount>");
								return true;
							}
							if (args[0].equalsIgnoreCase("mrl")) {
								if (args.length >= 2) {
									if (args[1].equalsIgnoreCase("reset")) {
										Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mrl reset mina_fantasmagorica");
										if (args.length >= 3) {
											if (args[2].equalsIgnoreCase("-s")) {
												p.sendMessage("§e[Vilarejo] Você resetou a mina fantasmagórica.");
												return true;
											}
										}
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("§5§lSPOOKY REALM §e➟ §6§lA mina fantasmagórica resetou. §d(/vilarejo)");
										Bukkit.broadcastMessage("");
										return true;
									} 
								}
								p.sendMessage("§cUse: /vilarejo mrl reset (-s)");
								return true;
							}
							if (args[0].equalsIgnoreCase("setloc")) {
								if (args.length >= 2) {
									if (args[1].equalsIgnoreCase("entrada")) {
										LocationAPI.getLocation().setLocation(p, location.ENTRADA);
										p.sendMessage("§e[Vilarejo] Localidade ENTRADA definida com sucesso!");
										return true;
									}
									if (args[1].equalsIgnoreCase("saida")) {
										LocationAPI.getLocation().setLocation(p, location.SAIDA);
										p.sendMessage("§e[Vilarejo] Localidade SAIDA definida com sucesso!");
										return true;
									}
									if (args[1].equalsIgnoreCase("mina")) {
										return true;
									}
								}
								if (args.length >= 3) {
									if (args[1].equalsIgnoreCase("mob")) {
										int limiteMob = 20;
										int number = Integer.valueOf(0);
										try {
											number = Integer.valueOf(Integer.parseInt(args[2]));
										} catch (NumberFormatException e) {
											sender.sendMessage("§cInsira valor válido >= 1 para ser o ponto de spawn.");
											return true;
										}
										if (number >= 1 && number <= limiteMob) {
											LocationAPI.setAnyMobLocation(p, number);
											p.sendMessage("§e[Vilarejo] Localidade MOB-"  + number + " definida com sucesso!");
										} else {
											p.sendMessage("§cO valor precisa ser maior que 1 e menor que " + limiteMob);
											return true;
										}
										return true;
									}
									p.sendMessage("§cUse: /vilarejo setloc <entrada/saida/mina/mob(1-20)>");
									return true;
								}
								p.sendMessage("§cUse: /vilarejo setloc <entrada/saida/mina/mob(1-20)>");
								return true;
							}
							p.sendMessage("§cUse: /vilarejo help.");
							return true;
						}
						openMenu(p);
					}
				}
				return false;
			}
			
	public void openMenu(Player p) {
		//String configTime = Configs.config.getConfig().getString("Horario.Nether");
		//12:00 // 15:00 // 18:00 // 21:00 //
		/*
		SimpleDateFormat Dateformat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");
        Date data = new Date();
        
        String dateStart = Dateformat.format(data) + " " + Timeformat.format(data);
        String[] dateSplit = Dateformat.format(data).split("/");
        int dia = Integer.parseInt(dateSplit[0]);
        int mes = Integer.parseInt(dateSplit[1]);
        int ano = Integer.parseInt(dateSplit[2]);
        dia++;
        
        String dateStart2 = dia+"/"+mes+"/"+ano + " " + configTime;

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        
        long segundos = 0;
        long minutos = 0;
        long horas = 0;
        
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStart2);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            segundos = diff / 1000 % 60;
            minutos = diff / (60 * 1000) % 60;
            horas = diff / (60 * 60 * 1000) % 24;

        } catch (Exception e) {
            e.printStackTrace();
        } */
		Inventory menu = Bukkit.createInventory(null, 3*9, "Caminho para o Vilarejo");
		//>2 5 7 9 10 12 //13// 14 15 16 19 22 25
		ItemStack web = new ItemBuilder(Material.WEB).displayname("§cTeia aterrorizante").build();
		ItemStack compass = new ItemBuilder(Material.COMPASS).displayname("§aVilarejo assombrado " + (entrada == true ? "§e§lABERTO" : "§c§lFECHADO")).lore(Arrays.asList(new String[] {
				"§8Clique para ir até o vilarejo.",
				"",
				" §fEsteja pronto para batalhar contra terríveis",
				" §fcriaturas, mas elas não são o único problema.",
				" §fJogadores também querem sua cabeça!",
				"",
				"  §e§lℹ §7Elimine criaturas e tenha a chance de receber",
				"  §3fragmentos fantasmagóricos §7que podem ser trocados",
				"  §7por alguns itens de halloween, basta falar com o §f☃ Gasparzinho§7.",
				"",
				" §f┌ Tenha acesso a §eMina fantasmagórica §fe aumente sua chance",
				"  §fde conseguir fragmentos fantasmagóricos.",
				""})).build();
		menu.setItem(2, web);
		menu.setItem(5, web);
		menu.setItem(7, web);
		menu.setItem(9, web);
		menu.setItem(10, web);
		menu.setItem(12, web);
		menu.setItem(13, compass);
		menu.setItem(14, web);
		menu.setItem(15, web);
		menu.setItem(16, web);
		menu.setItem(19, web);
		menu.setItem(22, web);
		menu.setItem(25, web);
		p.openInventory(menu);
	}
	
}
