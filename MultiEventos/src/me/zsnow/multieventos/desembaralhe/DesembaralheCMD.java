package me.zsnow.multieventos.desembaralhe;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

public class DesembaralheCMD implements CommandExecutor {
	
	DesembaralheManager desembaralhe = new DesembaralheManager();
	private NumberFormat formatarTempo = new DecimalFormat("#.#");
	public String format(double d) {
		return EconomyPlugin.getInstance().format(d, true);
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("desembaralhe")) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lDESEMBARALHE §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/desembaralhe <Palavra> §7- §fUtilize para responder.");
					
					if (sender.hasPermission("multieventos.admin")) {
						sender.sendMessage("§f/desembaralhe Iniciar §7- §fInicia o evento com palavras aleatórias.");
						sender.sendMessage("§f/desembaralhe Parar §7- §fPara o evento");
						sender.sendMessage("§f/desembaralhe enviar <Palavra> §7- §fEnvia uma palavra");
						sender.sendMessage("§f/desembaralhe spy §7- §fVerifica a palavra");
					}
					sender.sendMessage("");
					return true;
				}
			} 
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("parar")) {
					if (sender.hasPermission("multieventos.admin")) {
						if (desembaralhe.getOcorrendo() == true) {
							desembaralhe.Stop(false, null, null);
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§e§lDesembaralhe");
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("§cEvento encerrado");
								Bukkit.broadcastMessage("§cpor um Administrador.");
								Bukkit.broadcastMessage("");
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
								}
								return true;
						} else {
							sender.sendMessage("§cO evento Desembaralhe não está ocorrendo.");
							return true;
						}
					} else {
						sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("iniciar")) {
					if (sender.hasPermission("multieventos.admin")) {
						if (desembaralhe.getOcorrendo() == false) {
							desembaralhe.setPalavra((Configs.config.getConfig().getStringList("desembaralhe-palavras").
									get(new Random().nextInt(Configs.config.getConfig().getStringList("desembaralhe-palavras").size()))));
							
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("         §e§lDesembaralhe");
							Bukkit.broadcastMessage(" §fEvento desembaralhe iniciado!");
							Bukkit.broadcastMessage(" §fDesembaralhe a palavra §6" + shuffleString(desembaralhe.getPalavra()));
							Bukkit.broadcastMessage(" §f§l/desembaralhe <Palavra>");
							Bukkit.broadcastMessage("   §7§nresponda para vencer!");
							Bukkit.broadcastMessage("");
							desembaralhe.setOcorrendo(true);
							StartTask();
							return true;
							
						} else {
							sender.sendMessage("§cO evento Desembaralhe já está ocorrendo.");
							return true;
						}
					} else {
						sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("spy")) {
					if (sender.hasPermission("multieventos.admin")) {
						if (desembaralhe.getOcorrendo() == true) {
							sender.sendMessage("§a§lDesembaralhe: §fA palavra é: §a" + desembaralhe.getPalavra());
							return true;
						} else {
							sender.sendMessage("§cO evento Desembaralhe não está ocorrendo.");
							return true;
						} 
					} else {
						sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
			}
			if (args.length == 2) {
				if (args[0].equalsIgnoreCase("enviar")) {
					if (sender.hasPermission("multieventos.admin")) {
					if (desembaralhe.getOcorrendo() == false) {
						desembaralhe.Start(true, 0, args[1], desembaralhe.getPremio());
							for (Player jogadores : Bukkit.getOnlinePlayers()) {
								jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
							}
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("         §e§lDesembaralhe");
							Bukkit.broadcastMessage(" §fEvento desembaralhe iniciado!");
							Bukkit.broadcastMessage(" §fDesembaralhe a palavra §6" + shuffleString(desembaralhe.getPalavra()));
							Bukkit.broadcastMessage(" §f§l/desembaralhe <Palavra>");
							Bukkit.broadcastMessage("   §7§nresponda para vencer!");
							Bukkit.broadcastMessage("");
								StartTask();
							return true;
						}
					} else {
						sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
						return true;
					}
				}
			}
		
				if (args.length == 1) {
					if (desembaralhe.getOcorrendo() == true) {
						if ((args[0].equalsIgnoreCase(desembaralhe.getPalavra()))) {
							desembaralhe.setOcorrendo(false);
							 for (Player jogadores : Bukkit.getOnlinePlayers()) {
								 jogadores.playSound(jogadores.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
								 TitleAPI.sendTitle(jogadores, 10, 40, 10, "§e§ldesembaralhe", "§aVencedor: §f" + sender.getName());
							 }
							 
							 Bukkit.broadcastMessage("");
							 Bukkit.broadcastMessage("        §e§lDesembaralhe");
							 Bukkit.broadcastMessage("§f" + sender.getName() + " §7venceu o evento!");
							 Bukkit.broadcastMessage("§7Resposta: §f" + desembaralhe.getPalavra());
							 Bukkit.broadcastMessage("§7Tempo decorrido: §f" + formatarTempo.format(desembaralhe.getTempo()) + " §7segundos");
							 Bukkit.broadcastMessage("§7Prêmio: §f" + format(desembaralhe.getPremio()));
							 Bukkit.broadcastMessage("");

							 sender.sendMessage("§aVocê recebeu " + format(desembaralhe.getPremio()) + " §acoins do evento.");
							 desembaralhe.setTempo(0);
							 desembaralhe.setPalavra(null);
							 EconomyPlugin.getInstance().getEconomy().depositPlayer(sender.getName(), desembaralhe.getPremio());
							 return true;
						} else {

							sender.sendMessage("§e§lDesembaralhe: §cPalavra incorreta!");
							Player p = (Player) sender;
							p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
							
							String msgPlayer = "§e§lVocê digitou: ";
					    	final char[] palavraOriginal = desembaralhe.getPalavra().toLowerCase().toCharArray();
							final char[] palavraPlayer = args[0].toLowerCase().toCharArray();
					        if (palavraPlayer.length <= palavraOriginal.length) {
					            for (int i = 0; i < palavraPlayer.length; i++) {
					                if (palavraOriginal[i] == palavraPlayer[i]) {
					                	msgPlayer += "§a" + palavraPlayer[i];
					                } else {
					                	msgPlayer += "§c" + palavraPlayer[i];
					                }
					            }
					        } else {
					            for (int i = 0; i < palavraOriginal.length; i++) {
					                if (palavraOriginal[i] == palavraPlayer[i]) {
					                	msgPlayer += "§a" + palavraPlayer[i];
					                } else {
					                	msgPlayer += "§c" + palavraPlayer[i];
					                }
					            }
					        }
					           p.sendMessage(msgPlayer); 
						}
					} else {
						sender.sendMessage("§cO evento desembaralhe não está acontecendo.");
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
				if (desembaralhe.getOcorrendo() == true) {
					desembaralhe.setTempo(desembaralhe.getTempo() + 0.1D);
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						ActionBarAPI.sendActionBarMessage(jogadores, "§e§ldesembaralhe §cDuração: " + formatarTempo.format(desembaralhe.getTempo()));
					}
					if (desembaralhe.getTempo() >= 100) {
						desembaralhe.setOcorrendo(false);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(" §e§ldesembaralhe");
						Bukkit.broadcastMessage("§cTempo esgotado!");
						Bukkit.broadcastMessage("§cNão houveram vencedores.");
						Bukkit.broadcastMessage("");
						sh.cancelTask(count);
						desembaralhe.setTempo(0);
					}
				} else {
					sh.cancelTask(count);
					return;
				}
			}
		}, 0L, 2L);
		
	}
	
	public static String shuffleString(String string) {
	  List<String> letters = Arrays.asList(string.split(""));
	  Collections.shuffle(letters);
	  String shuffled = "";
	  for (String letter : letters) {
	    shuffled += letter;
	  }
	  return shuffled;
	}

}