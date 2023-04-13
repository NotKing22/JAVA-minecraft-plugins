package me.zsnow.multieventos.fastwrite;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.redestone.economy.EconomyPlugin;

import me.zsnow.multieventos.Main;
import me.zsnow.multieventos.API.ActionBarAPI;
import me.zsnow.multieventos.API.TitleAPI;

public class FastWriteCMD implements CommandExecutor, Listener {

	private NumberFormat formatarTempo = new DecimalFormat("#.#");
	public String format(int value) {
		return EconomyPlugin.getInstance().format(value, true);
	}
	private static int count;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("fastwrite")) {
				if (!(sender.hasPermission("multieventos.admin"))) {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando");
					return true;
				}
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lFAST-WRITE §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/fastwrite Iniciar §7- §fInicia o evento");
					sender.sendMessage("§f/fastwrite enviar <Mensagem> §7- §fEnvia a mensagem pro evento.");
					sender.sendMessage("§f/fastwrite Parar §7- §fPara o evento");
					sender.sendMessage("");
					return true;
				}
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("enviar")) {
						if (args.length == 1) {
							sender.sendMessage("§cUse: /fastwrite enviar <mensagem>.");
							return true;
						}
						if (args.length >= 2) {
							if (FastWriteManager.evento.getOcorrendo() == false) {
								final String msg = getMensagem(args).replaceFirst("\\s++$", "");
								FastWriteManager.evento.Start2(msg);
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("        §e§lFrase rápida");
									Bukkit.broadcastMessage("§7O evento frase rápida começou!");
									Bukkit.broadcastMessage("§7Seja o primeiro a digitar '§f" + msg + "§7' no §nchat local§7 para vencer.");
									Bukkit.broadcastMessage("§7Prêmio: §fR$" + format(FastWriteManager.evento.getPremio()) + " §fcoins.");
									Bukkit.broadcastMessage("");
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
								}
									BukkitScheduler sh = Bukkit.getServer().getScheduler();
									count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
										
									@Override
									public void run() {
										if (FastWriteManager.evento.getOcorrendo() == true) {
											FastWriteManager.evento.setSegundos(FastWriteManager.evento.getTempo() + 0.1D);
											for (Player jogadores : Bukkit.getOnlinePlayers()) {
												ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-WRITE §cDuração: " + formatarTempo.format(FastWriteManager.evento.getTempo()));
											}
											if (FastWriteManager.evento.getTempo() == 100) {
												FastWriteManager.evento.Stop();
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage(" §e§lFAST-WRITE");
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
									sender.sendMessage("§cO evento Fast-Write já está ocorrendo.");
									return true;
								}
						}
					}
					if (args[0].equalsIgnoreCase("parar")) {
						if (FastWriteManager.evento.getOcorrendo() == true) {
							FastWriteManager.evento.Stop();
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§e§lFAST-WRITE");
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§cEvento encerrado");
								Bukkit.broadcastMessage("§cpor um Administrador.");
								Bukkit.broadcastMessage("");
								return true;
							} else {
								sender.sendMessage("§cO evento Fast-Write não está ocorrendo.");
								return true;
							}
						}
					if (args[0].equalsIgnoreCase("iniciar")) {
						if (FastWriteManager.evento.getOcorrendo() == false) {
							FastWriteManager.evento.Start();
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("        §e§lFrase rápida");
								Bukkit.broadcastMessage("§7O evento frase rápida começou!");
								Bukkit.broadcastMessage("§7Seja o primeiro a digitar '§f" + FastWriteManager.evento.getFrase() + "§7' no §nchat local§7 para vencer.");
								Bukkit.broadcastMessage("§7Prêmio: §fR$" + format(FastWriteManager.evento.getPremio()) + " §fcoins.");
								Bukkit.broadcastMessage("");
							for (Player jogadores : Bukkit.getOnlinePlayers()) {
								jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
							}
								BukkitScheduler sh = Bukkit.getServer().getScheduler();
								count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
									
								@Override
								public void run() {
									if (FastWriteManager.evento.getOcorrendo() == true) {
										FastWriteManager.evento.setSegundos(FastWriteManager.evento.getTempo() + 0.1D);
										for (Player jogadores : Bukkit.getOnlinePlayers()) {
											ActionBarAPI.sendActionBarMessage(jogadores, "§e§lFAST-WRITE §cDuração: " + formatarTempo.format(FastWriteManager.evento.getTempo()));
										}
										if (FastWriteManager.evento.getTempo() >= 30) {
											FastWriteManager.evento.Stop();
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage(" §e§lFAST-WRITE");
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
								sender.sendMessage("§cO evento Fast-Write já está ocorrendo.");
								return true;
							}
						}
					}
				}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void responder(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if ((FastWriteManager.evento.getOcorrendo() == true) && (e.getMessage().equalsIgnoreCase(FastWriteManager.evento.getFrase()))) {
			if (FastWriteManager.evento.getOcorrendo()) {
				if (e.getMessage().equals(FastWriteManager.evento.getFrase())) {
					 for (Player jogadores : Bukkit.getOnlinePlayers()) {
						 jogadores.playSound(jogadores.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
						 TitleAPI.sendTitle(jogadores, 10, 40, 10, "§e§lFAST-WRITE", "§aVencedor: §f" + p.getName());
					 }
					 final double tempo = FastWriteManager.evento.getTempo();
					 FastWriteManager.evento.Stop();
					 Bukkit.broadcastMessage("");
					 Bukkit.broadcastMessage("        §e§lFrase rápida");
					 Bukkit.broadcastMessage("§f" + p.getName() + " §7venceu o evento!");
					 Bukkit.broadcastMessage("§7Tempo: §e" + formatarTempo.format(tempo) + " §esegundos");
					Bukkit.broadcastMessage("§7Prêmio: §fR$" + format(FastWriteManager.evento.getPremio()) + " §fcoins.");
					 Bukkit.broadcastMessage("");
					 p.sendMessage("§aVocê recebeu " + format(FastWriteManager.evento.getPremio()) + " §acoins do evento.");
					 EconomyPlugin.getInstance().getEconomy().depositPlayer(p.getName(), FastWriteManager.evento.getPremio());
					 FastWriteManager.evento.setSegundos(0.0D);
				}
			}
		}
	}
	
	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
}
