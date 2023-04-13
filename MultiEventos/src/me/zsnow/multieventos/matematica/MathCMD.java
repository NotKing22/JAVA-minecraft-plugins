package me.zsnow.multieventos.matematica;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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
import me.zsnow.multieventos.API.TitleAPI;
import me.zsnow.multieventos.configAPI.Configs;

public class MathCMD implements CommandExecutor {
	
	MathManager matematica = new MathManager();
	private NumberFormat formatarTempo = new DecimalFormat("#.#");
	public String format(double d) {
		return EconomyPlugin.getInstance().format(d, true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("matematica")) {
			if (sender.hasPermission("multieventos.admin")) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lMATEMATICA §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/matematica Iniciar §7- §fInicia o evento com cálculo aleatório.");
					sender.sendMessage("§f/matematica Parar §7- §fPara o evento");
					sender.sendMessage("§f/matematica enviar <Conta> [Resultado] §7- §fEnvia um cálculo");
					sender.sendMessage("§f/matematica <resultado> §7- §fUtilize para responder.");
					sender.sendMessage("");
					return true;
				}
			} else {
				if (args.length <= 1) {
					sender.sendMessage("");
					sender.sendMessage("§e§lMATEMATICA §7- §fComando");
					sender.sendMessage("");
					sender.sendMessage("§f/matematica responder <resposta> §7- §fUtilize para responder.");
					sender.sendMessage("");
					return true;
				}
			}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("responder")) {
						if (matematica.getOcorrendo() == true) {
							if ((args[1].equalsIgnoreCase(matematica.getResultado()))) {
								matematica.setOcorrendo(false);
								 for (Player jogadores : Bukkit.getOnlinePlayers()) {
									 jogadores.playSound(jogadores.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
									 TitleAPI.sendTitle(jogadores, 10, 40, 10, "§e§lMATEMATICA", "§aVencedor: §f" + sender.getName());
								 }
								 Bukkit.broadcastMessage("");
								 Bukkit.broadcastMessage("        §e§lMatemática");
								 Bukkit.broadcastMessage("§f" + sender.getName() + " §7venceu o evento!");
								 Bukkit.broadcastMessage("§7Resposta: §f" + matematica.getResultado());
								 Bukkit.broadcastMessage("§7Tempo decorrido: §f" + formatarTempo.format(matematica.getTempo()) + " §7segundos");
								 Bukkit.broadcastMessage("§7Prêmio: §f" + format(matematica.getPremio()));
								 Bukkit.broadcastMessage("");
								 sender.sendMessage("§aVocê recebeu " + format(matematica.getPremio()) + " §acoins do evento.");
								 matematica.setTempo(0);
								 matematica.setResultado(null);
								 EconomyPlugin.getInstance().getEconomy().depositPlayer(sender.getName(), matematica.getPremio());
								 return true;
							} else {
								sender.sendMessage("§e§lMATEMATICA: §cResposta incorreta!");
								Player p = (Player) sender;
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
								return true;
							}
						} else {
							sender.sendMessage("§cO evento matemática não está acontecendo.");
							return true;
						}
					}
				}
				if (sender.hasPermission("multieventos.admin")) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("parar")) {
							if (matematica.getOcorrendo() == true) {
								matematica.Stop(false, null, null);
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§e§lMATEMATICA");
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§cEvento encerrado");
									Bukkit.broadcastMessage("§cpor um Administrador.");
									Bukkit.broadcastMessage("");
									for (Player jogadores : Bukkit.getOnlinePlayers()) {
										jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
									}
									return true;
							} else {
								sender.sendMessage("§cO evento matemática não está ocorrendo.");
								return true;
							}
						}
						if (args[0].equalsIgnoreCase("iniciar")) {
							if (matematica.getOcorrendo() == false) {
								String calculo = Configs.config.getConfig().getStringList("Calculos").
										get(new Random().nextInt(Configs.config.getConfig().getStringList("Calculos").size()));
								try {
									ScriptEngineManager manager = new ScriptEngineManager();
									ScriptEngine se = manager.getEngineByName("JavaScript");        
									Object result = se.eval(calculo);
									matematica.setResultado(result.toString());
								} catch (Exception e) {
									sender.sendMessage("§cNão foi possível transformar o valor da config em número §e(" + calculo +"§e)");
									return true;
								}
								matematica.setConta(calculo);
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("         §e§lMatemática");
								Bukkit.broadcastMessage(" §7Evento matemática iniciado!");
								Bukkit.broadcastMessage(" §7Responda com o seguinte comando:");
								Bukkit.broadcastMessage(" §f/matematica responder <resultado>");
								Bukkit.broadcastMessage("       §7cálculo: §f" + matematica.getConta());					
								Bukkit.broadcastMessage("   §7§nresponda para vencer!");
								Bukkit.broadcastMessage("");
								matematica.setOcorrendo(true);
								StartTask();
								
							} else {
								sender.sendMessage("§cO evento matemática já está ocorrendo.");
								return true;
							}
						}
					}
					if (args.length == 3) {
						if (args[0].equalsIgnoreCase("enviar")) {
							if (matematica.getOcorrendo() == false) {
								matematica.Start(true, 0, args[1], args[2], matematica.getPremio());
									for (Player jogadores : Bukkit.getOnlinePlayers()) {
										jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
									}
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("         §e§lMatemática");
										Bukkit.broadcastMessage(" §7Evento matemática iniciado!");
										Bukkit.broadcastMessage(" §7Responda com o seguinte comando:");
										Bukkit.broadcastMessage(" §f/matematica responder <resultado>");
										Bukkit.broadcastMessage("       §7cálculo: §f" + matematica.getConta());					
										Bukkit.broadcastMessage("   §7§nresponda para vencer!");
										Bukkit.broadcastMessage("");
										StartTask();
									
							}
							return true;
						}
					}
				
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
			}
		}
		return false;
	}
	
	private static int count;
	
	@SuppressWarnings("deprecation")
	private void StartTask() {
		 for (Player jogadores : Bukkit.getOnlinePlayers()) {
			 jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
		 }
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			@Override
			public void run() {
				if (matematica.getOcorrendo() == true) {
					matematica.setTempo(matematica.getTempo() + 0.1D);
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						ActionBarAPI.sendActionBarMessage(jogadores, "§e§lMATEMATICA §cDuração: " + formatarTempo.format(matematica.getTempo()));
					}
					if (matematica.getTempo() >= 100) {
						matematica.setOcorrendo(false);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(" §e§lMATEMATICA");
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
		
	}

}
