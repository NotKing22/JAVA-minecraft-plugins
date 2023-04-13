package me.zsnow.desafioepico;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Strings;

import me.zsnow.desafioepico.configAPI.ActionBarAPI;
import me.zsnow.desafioepico.configAPI.BossBar;
import me.zsnow.desafioepico.configAPI.Configs;
import me.zsnow.desafioepico.configAPI.LocationAPI;
import me.zsnow.desafioepico.controller.EntityController;
import me.zsnow.desafioepico.controller.EventController;
import me.zsnow.desafioepico.controller.MenuController;

public class Commands implements CommandExecutor {
	
	String seta = "»";
	String setaMaior = "➣";
	ArrayList<Player> verificar = new ArrayList<>();
	//public static int taskCreatureIsComing;
	//public static int taskBossIsComingNether;
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("desafio")) {
			if (sender instanceof Player) {
				MenuController menu = new MenuController();
				Player p = (Player) sender;
				if (args.length == 0) {
					menu.openInventory(p);
					return true;
				}
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("moderar")) {
					//	EntityController.enableMortalhaDaNoite();
						if (p.hasPermission("zs.mod")) {
							p.sendMessage("§eEnviado para a área de moderação do Desafio épico.");
							ActionBarAPI.sendActionBarMessage(p, "§aBoa sorte! Não ataque os mobs.");
							p.setAllowFlight(true);
							p.setFlying(true);
							sendTo(p, "ENTRADA-NETHER");
							return true;
						} else {
							p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
							return true;
						}
					}
					if (!(p.hasPermission("zs.gerente"))) {
						menu.openInventory(p);
						return true;
					}
				}
			}
				if (args[0].equalsIgnoreCase("help")) {
					sender.sendMessage("§8§l§m--------------§7§l[§c§lStoneDesafio§7§l]§8§m----------------");
					sender.sendMessage("");
					sender.sendMessage(" §e" + seta + " §6/desafio iniciar <Eden/Nether> §7- Inicia o desafio."); // solved
					sender.sendMessage(" §e" + seta + " §6/desafio parar §7- Encerra o desafio."); 
					sender.sendMessage(" §e" + seta + " §6/desafio set <entrada/saida> [Nether]"); // solved
					sender.sendMessage(" §e" + seta + " §6/desafio set <mobspawn> <número> [Nether]"); // solved
					sender.sendMessage(" §e" + seta + " §6/desafio set <bosspawn> [Nether]");
					//sender.sendMessage(" §e" + seta + " §6/desafio givecoin <nick> [quantidade]");
					sender.sendMessage(" §e" + seta + " §c/desafio reload §7- Recarrega a Config.yml.");
					sender.sendMessage("");
					sender.sendMessage("§8§l§m--------------------------------------------");
					 return true;
				}
				EventController evento = EventController.admin;
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						Configs.config.reloadConfig();
						Configs.locations.reloadConfig();
						sender.sendMessage("§aConfiguração recarregada.");
						return true;
					}
					if (args[0].equalsIgnoreCase("verlist")) {
						if (sender.hasPermission("zs.admin")) {
							StringBuilder sb = new StringBuilder();
							String separe = "";
							sender.sendMessage("");
							sender.sendMessage("§c[StoneDesafio] jogadores:");
							for (Player jogadores : evento.getNetherParticipantes()) {
								sb.append(separe);
								separe = ", ";
								sb.append(jogadores.getName());
							}
							if (evento.getNetherParticipantes().size() == 0) {
								sender.sendMessage(" §e[Vazio]");
								sender.sendMessage("");
								return true;
							} else {
								sender.sendMessage("§7[" + sb.toString() + "§7]");
								sender.sendMessage("");
								return true;
							}
						}
					}
					
					if (args[0].equalsIgnoreCase("disable")) {
						Player p = (Player) sender;
						if (!verificar.contains(p)) {
							if (evento.getNetherOcorrendo() == true) {
								p.sendMessage("§aHá um desafio ocorrendo no momento, deseja reiniciar o plugin mesmo assim?");
								p.sendMessage("§aDigite novamente o comando para §a§lconfirmar.");
								verificar.add(p);
								(new BukkitRunnable() {
									
									int tempo = 10;
									
									@Override
									public void run() {
										tempo--;
										if (tempo == 0) {
											verificar.remove(p);
											cancel();
										}
									}
								}).runTaskTimer(Main.getInstance(), 0, 20L);
								return true;
							} 
						}
						for (Player participantes : evento.getNetherParticipantes()) {
							  LocationAPI.sendTo(participantes, "SAIDA-NETHER");
						  }
						  EventController.admin.getNetherParticipantes().clear();
						Listeners.endInvasion();
						 Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("StoneDesafio");
						 Bukkit.getPluginManager().disablePlugin(plugin);
						// Bukkit.getPluginManager().enablePlugin(plugin);
						// dont work
						p.sendMessage("§ePlugin recarregado com sucesso.");
						verificar.remove(p);
						return true;
					}
					if (args[0].equalsIgnoreCase("parar")) {
						if (evento.getNetherOcorrendo() == true) {
							for (Player participantes : evento.getNetherParticipantes()) {
								LocationAPI.sendTo(participantes, "SAIDA-NETHER");
								participantes.sendMessage("§cO desafio foi encerrado por um ADMINISTRADOR.");
								BossBar.removeStatusBar(participantes);
							}
							sender.sendMessage("§aVocê parou o desafio épico.");
							Listeners.endInvasion();
							evento.getNetherParticipantes().clear();
							Bukkit.getServer().getScheduler().cancelTasks(Main.getInstance());
							Main.getInstance().startCheck();
							//Main.getInstance().startCheck();
							//Bukkit.getServer().getScheduler().cancelTask(taskCreatureIsComing);
							//Bukkit.getServer().getScheduler().cancelTask(taskBossIsComingNether);
							return true;
						} else {
							sender.sendMessage("§cO desafio não está ocorrendo.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("stats")) {
						sender.sendMessage("");
						sender.sendMessage("§fTotal de §amoedas§f dropadas no evento:");
						sender.sendMessage("");
						sender.sendMessage("§bNether:");
						sender.sendMessage(" §6✮ " + getProgressBar(evento.getCoinsDroppedNether(), 200, 50, "|", "§a", "§8") + " §7(" + evento.getCoinsDroppedNether() + "§7/200)");
						sender.sendMessage("§bX:");
						sender.sendMessage(" §6✮ " + getProgressBar(evento.getCoinsDroppedEden(), 200, 50, "|", "§a", "§8") + " §7(" + evento.getCoinsDroppedEden() + "§7/200)");
						sender.sendMessage("");
						sender.sendMessage("§3§lTOTAL§8 " + getProgressBar((evento.getCoinsDroppedNether() + evento.getCoinsDroppedEden()), 
								400, 50, "|", "§a", "§8") + " §7(" + (evento.getCoinsDroppedEden() + evento.getCoinsDroppedNether()) + "§7/400)");
						sender.sendMessage("");
						return true;
					}
					if (args[0].equalsIgnoreCase("iniciar") || (args[0].equalsIgnoreCase("set"))) {
						sender.sendMessage("§c§lERRO! §cUtilize §8/desafio help §cpara obter informações.");
						return true;
					} else {
						sender.sendMessage("§c§lERRO! §cUtilize §8/desafio help §cpara obter informações.");
						return true;
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("iniciar")) {
						if (args[1].equalsIgnoreCase("nether")) {
							if (evento.getNetherOcorrendo() == false) {
								for (World mundo : Bukkit.getWorlds()) {
									for (Entity entidades : mundo.getEntities()) {
										if (!(entidades instanceof Player) && (entidades.hasMetadata("boss-nether") || 
											(entidades.hasMetadata("cavaleironegro-servo") || (entidades.hasMetadata("netherservos"))))) {
											 entidades.remove();
										}
									}
								}
							evento.setMortalhaDaNoiteStatus(false);
							evento.setNetherOcorrendo(true);
							evento.setNetherEntradaLiberada(true);
							EventController.admin.setNetherTempoDecorrido(0);
							EntityController.setNetherBossHP(Configs.config.getConfig().getInt("NetherBoss.HP"));
							for (Player participantes : Bukkit.getOnlinePlayers()) {
								participantes.playSound(participantes.getLocation(), Sound.WITHER_SPAWN, 1.0f, 0.5f);
							}
							(new BukkitRunnable() {
								
								int tempo = 330; // tempo + 30

								@Override
								public void run() {
									if (evento.getNetherOcorrendo() == true) {
										if (tempo > 30) { // 30
											tempo = tempo - 30;
											Bukkit.broadcastMessage("");
											Bukkit.broadcastMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7A invasão ao nether está começando!");
											Bukkit.broadcastMessage("§7Participe dessa invasão com o comando: §8/desafio§7.");
											Bukkit.broadcastMessage(" §d" + seta + " §7A entrada fechará em §5" + tempo + " segundos§7.");
											Bukkit.broadcastMessage("");
											} else {
												if (evento.getNetherParticipantes().size() >= 1) {
													evento.setNetherEntradaLiberada(false);
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §6" + seta + " §7A invasão ao nether está começando!");
													Bukkit.broadcastMessage(" §d" + seta + " §7A entrada foi fechada. Começando a invasão!");
													Bukkit.broadcastMessage("");
													creatureIsComingNether(); // task
													cancel();
											} else {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §6" + seta + " §7A invasão ao nether foi cancelada!");
												Bukkit.broadcastMessage(" §d" + seta + " §7Vocês não possuem a menor chance com menos de §510 jogadores");
												Bukkit.broadcastMessage("");
												cancel();
												Main.StopNether();
											} 
										}
									} else {
										Main.StopNether();
										cancel();
									}
							}
						}).runTaskTimer(Main.getInstance(), 0, 600L);
							//abaixo
							
							// acima
							
							//boss vindo abaixo
							
							//boss vindo acima
							
							// Inicia um task de 2 minutos para entrar
							
							} else {
								sender.sendMessage("§cO desafio nether já está ocorrendo.");
								return true;
							}
							
							
							return true;
						}
						if (args[1].equalsIgnoreCase("eden2139137182372138127")) {
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§d§lΩ DESAFIO ÉPICO §6" + seta + " §cA invasão ao Éden está começando!");
							Bukkit.broadcastMessage("§cParticipe dessa invasão com o comando: §8/desafio§c.");
							Bukkit.broadcastMessage("");
							return true;
						} else {
							sender.sendMessage("§c§lERRO! §8" + args[1] + " §cnão é reconhecido pelo sistema.");
							return true;
						}
					} 
					if (args[0].equalsIgnoreCase("mortalha1")) {
						if (args[1].equalsIgnoreCase("ativar")) {
							sender.sendMessage("§aVocê ativou a mortalha da noite");
							evento.setMortalhaDaNoiteStatus(true);
							return true;
						}
						if (args[1].equalsIgnoreCase("desativar")) {
							sender.sendMessage("§cVocê desativou a mortalha da noite");
							evento.setMortalhaDaNoiteStatus(false);
							return true;
						}
					}
				//   0        1          2              3			4
				//   -        0          1              2			3
					
				//§6/desafio set <mobspawn/bosspawn> <número> [Eden/Nether]");
				//arg0 set | arg1 mobspawn/bosspawn | arg2 numero | arg3 nether/eden
			}
				if (args.length >= 3) {
					if (args[0].equalsIgnoreCase("givecoin")) {
						Player target = Bukkit.getPlayer(args[1]);
						if (target == null) {
							sender.sendMessage("§cJogador não encontrado em nosso banco de dados.");
							return true;
						}
						Integer quantidade = Integer.valueOf(0);
						try {
							quantidade = Integer.valueOf(Integer.parseInt(args[2]));
						} catch (NumberFormatException e) {
							sender.sendMessage("§cInsira um valor válido pra enviar para o jogador.");
							return true;
						}
						target.getInventory().addItem(new MenuController().giveMoeda(target, quantidade));
						sender.sendMessage("§aVocê enviou §e" +quantidade+ " §amoeda(s) para o(a) jogador(a) §e" +target.getName()+ "§a.");
						return true;
					}
					if (args[0].equalsIgnoreCase("set")) {
						if (args[1].equalsIgnoreCase("mobspawn")) {
							if (args[3].equalsIgnoreCase("nether")) {
								Integer spawnNumber = Integer.valueOf(0);
								try {
						            spawnNumber = Integer.valueOf(Integer.parseInt(args[2]));
						            if (spawnNumber != 0 && spawnNumber <= 10) {
							            LocationAPI.setLoc((Player) sender, "nether;mobspawn-" + spawnNumber);
							            sender.sendMessage("§5§l" + setaMaior + " (Nether) §dMobSpawn-" +spawnNumber+ " §ddefinido com sucesso.");
							            return true;
						            } else {
						            	sender.sendMessage("§cO valor fornecido não pode ser maior que 10.");
						            }
								} catch(NumberFormatException e) { 
									sender.sendMessage("§cVocê não forneceu um número válido para o spawn.");
									return true;
								}
							}
							if (args[3].equalsIgnoreCase("Eden")) {
								Integer spawnNumber = Integer.valueOf(0);
								try {
						            spawnNumber = Integer.valueOf(Integer.parseInt(args[2]));
						            if (spawnNumber != 0 && spawnNumber <= 10) {
							            LocationAPI.setLoc((Player) sender, "eden;mobspawn-" + spawnNumber);
							            sender.sendMessage("§5§l" + setaMaior + " (Eden) §dMobSpawn-" +spawnNumber+ " §ddefinido com sucesso.");
							            return true;
						            } else {
						            	sender.sendMessage("§cO valor fornecido não pode ser maior que 10.");
						            }
								} catch(NumberFormatException e) { 
									sender.sendMessage("§cVocê não forneceu um número válido para o spawn.");
									return true;
								}
							} else {
								sender.sendMessage("§c§lERRO! §8" + args[3] + " §cnão é reconhecido pelo sistema.");
								return true;
							}
						} 
						if (args[1].equalsIgnoreCase("bosspawn")) {
							if (args[2].equalsIgnoreCase("nether")) {
								setLoc((Player) sender, "Boss-Nether");
								sender.sendMessage("§5§l" + setaMaior + " §d(Nether) Boss-Spawn definido com sucesso.");
								return true;
							}
							if (args[2].equalsIgnoreCase("eden")) {
								setLoc((Player) sender, "Boss-Eden");
								sender.sendMessage("§5§l" + setaMaior + " §d(Eden) Boss-Spawn definido com sucesso.");
								return true;
							}
						}
							if (args[1].equalsIgnoreCase("entrada")) {
								if (args[2].equalsIgnoreCase("Nether")) {
									LocationAPI.setLoc((Player) sender, "Entrada-Nether");
									sender.sendMessage("§5§l" + setaMaior + " §d(Nether) Entrada definida com sucesso.");
									return true;
								} 
								if (args[2].equalsIgnoreCase("Eden")) {
									LocationAPI.setLoc((Player) sender, "Entrada-Eden");
									sender.sendMessage("§5§l" + setaMaior + " §d(Eden) Entrada definida com sucesso.");
									return true;
								} else {
									sender.sendMessage("§c§lERRO! §8" + args[1] + " §cnão é reconhecido pelo sistema.");
									return true;
								}
							}
							if (args[1].equalsIgnoreCase("saida")) {
								if (args[2].equalsIgnoreCase("Nether")) {
									LocationAPI.setLoc((Player) sender, "Saida-Nether");
									sender.sendMessage("§5§l" + setaMaior + " §d(Nether) Saída definida com sucesso.");
									return true;
								} 
								if (args[2].equalsIgnoreCase("Eden")) {
									LocationAPI.setLoc((Player) sender, "Saida-Eden");
									sender.sendMessage("§5§l" + setaMaior + " §d(Eden) Saída definida com sucesso.");
									return true;
								} else {
									sender.sendMessage("§c§lERRO! §8" + args[1] + " §cnão é reconhecido pelo sistema.");
									return true;
								}
							} else {
								sender.sendMessage("§c§lERRO! §cUtilize §8/desafio help §cpara obter informações.");
								return true;
							}
						} else {
							sender.sendMessage("§c§lERRO! §cUtilize §8/desafio help §cpara obter informações.");
							return true;
							}
						}
					}
		return false;
	}
	
	public void sendTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		p.teleport(loc);
	}
	
	
	public void setLoc(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Configs.locations.getConfig().set(LocationName + ".X", Double.valueOf(x));
		Configs.locations.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Configs.locations.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Configs.locations.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Configs.locations.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Configs.locations.getConfig().set(LocationName + ".Mundo", world);
		Configs.locations.saveConfig();
	}
	
    private String getProgressBar(int current, int max, int totalBars, String symbol, String color0, String color1) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return color0 + Strings.repeat(symbol, progressBars)
                + color1 + Strings.repeat(symbol, totalBars - progressBars);
    }
    
   public void creatureIsComingNether() {
    	
    	(new BukkitRunnable() {
    		EventController evento = EventController.admin;
			int tempo = 90; // 60
	//	int tempo = 30;
			@Override
			public void run() {
				if (tempo > 30) {
					if (evento.getNetherOcorrendo() == true) {
						tempo = tempo - 30;
						for (Player participantes : evento.getNetherParticipantes()) {
							participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7As criaturas estão chegando!");
							participantes.sendMessage(" §d" + seta + " §7Tempo até que elas apareçam: §5" + tempo + " segundos§7.");
						}
					} else {
						cancel();
						Main.StopNether();
						return;
					}
				} else {
					cancel();
					evento.startTempoDecorrido();
					for (Player participantes : evento.getNetherParticipantes()) {
						participantes.playSound(participantes.getLocation(), Sound.GHAST_SCREAM, 1.0f, 0.5f);
						participantes.playSound(participantes.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
						participantes.playSound(participantes.getLocation(), Sound.HORSE_DEATH, 1.0f, 0.5f);
						participantes.playSound(participantes.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Servos.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnServosNether("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Cavaleiro.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnCavaleiroNegro("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Cavaleiro.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnCarminidioSkeleton("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					bossIsComingNether(); // start task
				}
			}
		}).runTaskTimer(Main.getInstance(), 0, 600L);
    }	
    
    public void bossIsComingNether() {
    	
    	(new BukkitRunnable() {
    		EventController evento = EventController.admin;
    														//int tempo = 330; // 5 minutos
			int tempo = 230;
    //	  int tempo = 30;
    		@Override
			public void run() {
				tempo = tempo - 30;
				if (evento.getNetherOcorrendo() == true) {
					if (tempo > 0) {
					for (Player participantes : evento.getNetherParticipantes()) {
						participantes.sendMessage(" ");
						participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7O chefe deles surgirá em breve!");
						participantes.sendMessage(" §d" + seta + " §7Tempo até o chefe final chegue: §5" + tempo + " segundos§7.");
						participantes.sendMessage(" ");
						}
					} else {
						EntityController.spawnNetherBoss("§6§lHADES", "boss-nether");
						for (Player participantes : evento.getNetherParticipantes()) {
							participantes.sendMessage(" ");
							participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7O chefe final surgiu!");
							participantes.sendMessage(" §d" + seta + " §7Derrote ele para escapar antes que o tempo acabe.");
							participantes.sendMessage(" ");
							participantes.playSound(participantes.getLocation(), Sound.WITHER_SPAWN, 1.0F, 0.5F);
						}
						cancel();
					} 
				} else {
					cancel();
				}
			}
		}).runTaskTimer(Main.getInstance(), 0, 600L);
    }
    
    
  /*  async task n deixa spawnar entidade essa corna
	private void creatureIsComingNether() {
		EventController evento = EventController.admin;
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskCreatureIsComing = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
		//	int tempo = 60;
			int tempo = 60;
			@Override
			public void run() {
				if (tempo > 30) {
					if (evento.getNetherOcorrendo() == true) {
						tempo = tempo - 30;
						for (Player participantes : evento.getNetherParticipantes()) {
							participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7As criaturas estão chegando!");
							participantes.sendMessage(" §d" + seta + " §7Tempo até que elas apareçam: §5" + tempo + " segundos§7.");
						}
					} else {
						Main.StopNether();
						sh.cancelTask(taskCreatureIsComing);
						return;
					}
				} else {
					sh.cancelTask(taskCreatureIsComing);
					evento.startTempoDecorrido();
					for (Player participantes : evento.getNetherParticipantes()) {
						participantes.playSound(participantes.getLocation(), Sound.GHAST_SCREAM, 1.0f, 0.5f);
						participantes.playSound(participantes.getLocation(), Sound.AMBIENCE_CAVE, 1.0f, 0.5f);
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Servos.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnServosNether("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Cavaleiro.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnCavaleiroNegro("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					for (int quantidade = 0; quantidade < Configs.config.getConfig().getInt("Cavaleiro.quantidade"); quantidade++) {
						Random random = new Random();
						EntityController.spawnCarminidioSkeleton("NETHER;MOBSPAWN-" + (random.nextInt(5) + 1));
					}
					bossIsComingNether(); // start task
				}
			}
		}, 0L, 600L);
	}
    
	public void bossIsComingNether() {
		EventController evento = EventController.admin;
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		taskBossIsComingNether = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
		//	int tempo = 230;
			int tempo = 60;
			@Override
			public void run() {
				tempo = tempo - 30;
				if (evento.getNetherOcorrendo() == true) {
					if (tempo > 0) {
						for (Player participantes : evento.getNetherParticipantes()) {
							participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7O chefe deles surgirá em breve!");
							participantes.sendMessage(" §d" + seta + " §7Tempo até o chefe final chegue: §5" + tempo + " segundos§7.");
							}
						} else {
							EntityController.spawnNetherBoss("§6§lHADES", "boss-nether");
							for (Player participantes : evento.getNetherParticipantes()) {
								participantes.sendMessage("§5§l[Ω] §d§lDESAFIO ÉPICO §d" + seta + " §7O chefe final surgiu!");
								participantes.sendMessage(" §d" + seta + " §7Derrote ele para escapar antes que o tempo acabe.");
							}
							sh.cancelTask(taskBossIsComingNether);
						} 
					} else {
						sh.cancelTask(taskBossIsComingNether);
					}
				}
			}, 0L, 600L);
		
	}*/
	
   /* public String getPrefix(String name) {
    	final User user = Main.getInstance().getlcApi().getUserManager().getUser(name);
        if (user == null) return "";

        final CachedMetaData metaData = user
              .getCachedData()
              .getMetaData(QueryOptions.defaultContextualOptions());

        String prefix = metaData.getPrefix();
        if (prefix == null) prefix = "";
        return prefix;
    }    */

    
}


