package me.zsnow.multieventos.fastclick;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.redestone.economy.EconomyPlugin;

import me.zsnow.multieventos.Main;
import me.zsnow.multieventos.API.ActionBarAPI;

public class FastClickCMD implements CommandExecutor {
	
	//private NumberFormat formatter = NumberFormat.getCurrencyInstance();
	private NumberFormat formatarTempo = new DecimalFormat("#.#");
	public String format(int value) {
		return EconomyPlugin.getInstance().format(value, true);
	}
	private static int count;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fastclick")) {
			if (sender.hasPermission("multieventos.admin")) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lfastclick §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/fastclick Iniciar §7- §fInicia o evento modo padrão.");
					sender.sendMessage("§f/fastclick Iniciar2 §7- §fInicia o evento com o X revelado.");
					sender.sendMessage("§f/fastclick Troll §7- §fInicia o evento troll com X revelado.");
					sender.sendMessage("§f/fastclick Troll2 §7- §fInicia o evento troll com X oculto.");
					sender.sendMessage("");
					sender.sendMessage("§f/fastclick Parar §7- §fPara o evento");
					sender.sendMessage("");
					return true;
				}
			}
			if (args.length == 2) {
				Player p = (Player) sender;
				FastClickManager evento = FastClickManager.evento;
				if (args[0].equalsIgnoreCase("ganhar") && evento.getOcorrendo() == true) {
					if (args[1].equalsIgnoreCase(evento.getKey())) {
						if (evento.getTroll() == true || evento.getTroll2() == true) {
							if (Main.getInstance().economy.has(p.getName(), evento.getPremio())) {
							 Bukkit.broadcastMessage("");
							 Bukkit.broadcastMessage("        §c§lClique rápido!");
							 Bukkit.broadcastMessage("§f" + p.getName() + " §7Clicou primeiro!");
							 Bukkit.broadcastMessage("§7Tempo: §e" + formatarTempo.format(FastClickManager.evento.getSegundos()) + " §esegundos");
							 Bukkit.broadcastMessage("§7Ele(a) perdeu: §f" + format(FastClickManager.evento.getPremio()) + " §fde coins");
							 Bukkit.broadcastMessage("");
							 p.sendMessage("§aVocê §a§nperdeu§a " + format(FastClickManager.evento.getPremio()) + " §acoins do evento.");
							 FastClickManager.evento.Stop();
							 EconomyPlugin.getInstance().getEconomy().withdrawPlayer(p.getName(), FastClickManager.evento.getPremio());
							 return true;
							} else {
								p.sendMessage("§cVocê não possui coins o suficiente para cair na trollagem. :(");
								return true;
							}
						}
						 Bukkit.broadcastMessage("");
						 Bukkit.broadcastMessage("        §e§lClique rápido!");
						 Bukkit.broadcastMessage("§f" + p.getName() + " §7Clicou primeiro!");
						 Bukkit.broadcastMessage("§7Tempo: §e" + formatarTempo.format(FastClickManager.evento.getSegundos()) + " §esegundos");
						 Bukkit.broadcastMessage("§7Prêmio: §f" + format(FastClickManager.evento.getPremio()) + " §fde coins");
						 Bukkit.broadcastMessage("");
						 p.sendMessage("§aVocê recebeu " + format(FastClickManager.evento.getPremio()) + " §acoins do evento.");
						FastClickManager.evento.Stop();
						 EconomyPlugin.getInstance().getEconomy().depositPlayer(p.getName(), FastClickManager.evento.getPremio());
						return true;
					}
				}
			}
				if (sender.hasPermission("multieventos.admin")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("parar")) {
							if (FastClickManager.evento.getOcorrendo() == true) {
								FastClickManager.evento.Stop();
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§e§lClique rápido!");
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§cEvento encerrado");
									Bukkit.broadcastMessage("§cpor um Administrador.");
									Bukkit.broadcastMessage("");
									for (Player jogadores : Bukkit.getOnlinePlayers()) {
										jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
									}
									return true;
							} else {
								sender.sendMessage("§cO evento fast-click não está ocorrendo.");
								return true;
							}
						}
						if (args[0].equalsIgnoreCase("iniciar")) {
							FastClickManager evento = FastClickManager.evento;
								if (evento.getOcorrendo() == false) {
									evento.deletKey();
									evento.setOcorrendo(true);
									evento.sendFastClickMessage();
									
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
								}
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										
									@Override
									public void run() {
										if (evento.getOcorrendo() == true) {
											evento.setSegundos(evento.getSegundos() + 0.1D);
											for (Player jogadores : Bukkit.getOnlinePlayers()) {
												ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-CLICK §cDuração: " + formatarTempo.format(evento.getSegundos()));
											}
											if (FastClickManager.evento.getSegundos() >= 20.0) {
												evento.Stop();
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage(" §e§lFAST-CLICK");
													Bukkit.broadcastMessage("§cTempo esgotado!");
													Bukkit.broadcastMessage("§cNão houveram vencedores.");
													Bukkit.broadcastMessage("");
													for (Player jogadores : Bukkit.getOnlinePlayers()) {
														jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
													}
													sh.cancelTask(count);
													return;
											}
											
						                } else {
						                	sh.cancelTask(count);
						                	FastClickManager.evento.Stop();
						                	return;
						                  } 
						                }
									}, 0L, 2L);
									
								} else {
									sender.sendMessage("§cO evento Fast-Click já está ocorrendo.");
									return true;
								}
							}
						
						if (args[0].equalsIgnoreCase("iniciar2")) {
							FastClickManager evento = FastClickManager.evento;
								if (evento.getOcorrendo() == false) {
									evento.deletKey();
									evento.setOcorrendo(true);
									evento.sendFastClickMessage2();
									
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
								}
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										
									@Override
									public void run() {
										if (evento.getOcorrendo() == true) {
											evento.setSegundos(evento.getSegundos() + 0.1D);
											for (Player jogadores : Bukkit.getOnlinePlayers()) {
												ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-CLICK §cDuração: " + formatarTempo.format(evento.getSegundos()));
											}
											if (FastClickManager.evento.getSegundos() >= 20.0) {
												evento.Stop();
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage(" §e§lFAST-CLICK");
													Bukkit.broadcastMessage("§cTempo esgotado!");
													Bukkit.broadcastMessage("§cNão houveram vencedores.");
													Bukkit.broadcastMessage("");
													for (Player jogadores : Bukkit.getOnlinePlayers()) {
														jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
													}
													sh.cancelTask(count);
													return;
											}
											
						                } else {
						                	sh.cancelTask(count);
						                	return;
						                  } 
						                }
									}, 0L, 2L);
									
								} else {
									sender.sendMessage("§cO evento Fast-Click já está ocorrendo.");
									return true;
								}
							}
						
						if (args[0].equalsIgnoreCase("troll")) {
							FastClickManager evento = FastClickManager.evento;
								if (evento.getOcorrendo() == false) {
									evento.deletKey();
									evento.setOcorrendo(true);
									evento.sendFastClickMessageTroll();
									evento.setTrollStatus(true);
									
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
								}
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										
									@Override
									public void run() {
										if (evento.getOcorrendo() == true) {
											evento.setSegundos(evento.getSegundos() + 0.1D);
											for (Player jogadores : Bukkit.getOnlinePlayers()) {
												ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-CLICK §cDuração: " + formatarTempo.format(evento.getSegundos()));
											}
											if (evento.getSegundos() >= 20.0) {
												evento.Stop();
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage(" §e§lFAST-CLICK");
													Bukkit.broadcastMessage("§cTempo esgotado!");
													Bukkit.broadcastMessage("§cNão houveram vencedores.");
													Bukkit.broadcastMessage("");
													for (Player jogadores : Bukkit.getOnlinePlayers()) {
														jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
													}
													sh.cancelTask(count);
													return;
											}
											
						                } else {
						                	sh.cancelTask(count);
						                	return;
						                  } 
						                }
									}, 0L, 2L);
									
								} else {
									sender.sendMessage("§cO evento Fast-Click já está ocorrendo.");
									return true;
								}
							}
						
						if (args[0].equalsIgnoreCase("troll2")) {
							FastClickManager evento = FastClickManager.evento;
								if (evento.getOcorrendo() == false) {
									evento.deletKey();
									evento.setOcorrendo(true);
									evento.sendFastClickMessageTroll2();
									evento.setTroll2Status(true);
									
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
								}
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										
									@Override
									public void run() {
										if (evento.getOcorrendo() == true) {
											evento.setSegundos(evento.getSegundos() + 0.1D);
											for (Player jogadores : Bukkit.getOnlinePlayers()) {
												ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-CLICK §cDuração: " + formatarTempo.format(evento.getSegundos()));
											}
											if (evento.getSegundos() >= 10.0) {
												evento.Stop();
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage(" §e§lFAST-CLICK");
													Bukkit.broadcastMessage("§cTempo esgotado!");
													Bukkit.broadcastMessage("§cNão houveram vencedores.");
													Bukkit.broadcastMessage("");
													for (Player jogadores : Bukkit.getOnlinePlayers()) {
														jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
													}
													sh.cancelTask(count);
													return;
											}
											
						                } else {
						                	sh.cancelTask(count);
						                	return;
						                  } 
						                }
									}, 0L, 2L);
									
								} else {
									sender.sendMessage("§cO evento Fast-Click já está ocorrendo.");
									return true;
								}
							}
							
							
						}
					}
				
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
		}
		return false;
	}
	
	/*private static int count;
	
	@SuppressWarnings("deprecation")
	private void StartTask() {
		 for (Player jogadores : Bukkit.getOnlinePlayers()) {
			 jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
		 }
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if (fastclick.getOcorrendo() == true) {
					fastclick.setTempo(fastclick.getTempo() + 0.1D);
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						ActionBarAPI.sendActionBarMessage(jogadores, "§e§lfastclick §cDuração: " + formatarTempo.format(fastclick.getTempo()));
					}
					if (fastclick.getTempo() >= 100) {
						fastclick.setOcorrendo(false);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(" §e§lfastclick");
						Bukkit.broadcastMessage("§cTempo esgotado!");
						Bukkit.broadcastMessage("§cNão houveram vencedores.");
						Bukkit.broadcastMessage("");
						sh.cancelTask(count);
					}
				} else {
					sh.cancelTask(count);
					return;
				}
			}
		}, 0L, 2L);
		
	}*/

}
