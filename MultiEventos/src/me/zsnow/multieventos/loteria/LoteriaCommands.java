package me.zsnow.multieventos.loteria;

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
import me.zsnow.multieventos.configAPI.Configs;

public class LoteriaCommands implements CommandExecutor{ 
	
	//sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
	
	LoteriaManager loteria = new LoteriaManager();
	
	private final int numeroMaximo = Configs.config.getConfig().getInt("numero-maximo");
	public String format(Double double1) {
		return EconomyPlugin.getInstance().format(double1, true);
	}
	private static int count;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("loteria")) {
			if (args.length == 0) {
				if (!(sender.hasPermission("multieventos.admin"))) {
					sender.sendMessage("");
					sender.sendMessage("§e§lLOTERIA §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/Loteria apostar <número> §7- §fAposta em um número.");
					sender.sendMessage("");
					return true;
			}
				sender.sendMessage("");
				sender.sendMessage("§e§lLOTERIA §7- §fComandos");
				sender.sendMessage("");
				sender.sendMessage("§f/Loteria apostar <número> §7- §fAposta em um número.");
				sender.sendMessage("§f/Loteria iniciar §7- §fInicia o evento.");
				sender.sendMessage("§f/Loteria parar §7- §fFinaliza o evento.");
				sender.sendMessage("§f/loteria resposta §7- §fRevela o número premiado.");
				sender.sendMessage("");
				return true;
		}
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("apostar")) {
				sender.sendMessage("§6[Loteria] §eUse /loteria apostar <número>");
				return true;
				}
			if (args[0].equalsIgnoreCase("iniciar")) {
				if (sender.hasPermission("multieventos.admin")) {
					if (loteria.getOcorrendo() == false) {
						loteria.setOcorrendo(true);
						
						
						Random random = new Random();
						int numeroMinimo = 0;
						loteria.setNumero(numeroMinimo + random.nextInt((numeroMaximo - numeroMinimo) + 1));
						loteria.setTempo(Configs.config.getConfig().getInt("loteria-sorteio-tempo"));
						 for (Player jogadores : Bukkit.getOnlinePlayers()) {
							 jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
						 }
						
						BukkitScheduler sh = Bukkit.getServer().getScheduler();
						count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
							
							@Override
							public void run() {
								if (loteria.getOcorrendo() == true) {
									if (loteria.getTempo() > 0) {
										loteria.setTempo((loteria.getTempo() - 10));
										Bukkit.broadcastMessage(" ");
										Bukkit.broadcastMessage("          §e§lLoteria");
										Bukkit.broadcastMessage(" §7Escolha um número entre §f0 §7e §f" +Configs.config.getConfig().getInt("numero-maximo") );
										Bukkit.broadcastMessage(" §7Prêmio: §f$" + format(loteria.getPremio()) + " Coins");
										Bukkit.broadcastMessage(" §7Evento encerrando em: §f" + loteria.getTempo() + " §fsegundo(s)");
										Bukkit.broadcastMessage("  §f-> §e/loteria apostar <número>");
										Bukkit.broadcastMessage(" ");
									} else {
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("§e§lLOTERIA");
										Bukkit.broadcastMessage("");
										Bukkit.broadcastMessage("§cTempo esgotado!");
										Bukkit.broadcastMessage("§cNão houveram vencedores.");
										Bukkit.broadcastMessage("§cResposta: Número §e" + loteria.getNumero());
										Bukkit.broadcastMessage(" ");
										Stop();
										sh.cancelTask(count);
										return;
										}
									} else {
										sh.cancelTask(count);
										return;
									}
								}
						}, 0L, 200L);
								
					} else {
						sender.sendMessage("§cO evento loteria já está ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("parar")) {
				if (sender.hasPermission("multieventos.admin")) {
					if (loteria.getOcorrendo() == true) {
						loteria.setOcorrendo(false);
						loteria.setTempo(0);
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§e§lLOTERIA");
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage("§cEvento encerrado");
						Bukkit.broadcastMessage("§cpor um Administrador.");
						Bukkit.broadcastMessage("§cResposta: Número §e" + loteria.getNumero());
						Bukkit.broadcastMessage("");
						loteria.setNumero(0);
						return true;
					} else {
						sender.sendMessage("§cO evento loteria não está ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("resposta")) {
				if (sender.hasPermission("multieventos.admin")) {
					if (loteria.getOcorrendo() == true) {
						sender.sendMessage("§c[Loteria] O número escolhido é: §e" + loteria.getNumero());
						return true;
					} else {
						sender.sendMessage("§cO evento loteria não está ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}
			}
			if (args[0].equalsIgnoreCase("escolher")) {
				if (sender.hasPermission("multieventos.admin")) {
					if (loteria.getOcorrendo() == false) {
						sender.sendMessage("§cUtilize: /loteria escolher <número>");
						return true;
					} else {
						sender.sendMessage("§cO evento loteria já está ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}
			}
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("apostar")) {
				if ((loteria.getOcorrendo() == true) && ((loteria.getTempo() > 1))) {
					String numeroApostado = args[1];
					try {
						 int valor = Integer.parseInt(numeroApostado);
							 if (valor <= numeroMaximo) {
								 if (loteria.getNumero() == valor) {
									 
									 Bukkit.broadcastMessage("");
									 Bukkit.broadcastMessage("          §6§lLoteria");
									 Bukkit.broadcastMessage("  §eEvento finalizado!");
									 Bukkit.broadcastMessage("§f" + sender.getName() + " §eacertou o número!");
									 Bukkit.broadcastMessage(" §eNúmero premiado: §f" + loteria.getNumero());
										Bukkit.broadcastMessage(" §ePrêmio: §fR$" + format(loteria.getPremio()) + " Coins");
									 Bukkit.broadcastMessage(" ");
									 EconomyPlugin.getInstance().getEconomy().depositPlayer(sender.getName(), loteria.getPremio());
									 for (Player jogadores : Bukkit.getOnlinePlayers()) {
										 jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
									 }
									 Stop();
									 
									 return true;
								 } else {
									 sender.sendMessage("§6[Loteria] §cVocê apostou no número errado, tente novamente!");
									 return true;
								 }
							 } else {
								 sender.sendMessage("§6[Loteria] §cO número que você digitou é maior que o número máximo permitido. §e(" + numeroMaximo + "§e)");
								 return true;
							 }
					}catch(NumberFormatException ex) {
						sender.sendMessage("§cVocê não informou um valor válido.");
						return true;
					}
					
				} else {
					sender.sendMessage("§cO evento loteria não está ocorrendo.");
					return true;
				}
			} 
			if (args[0].equalsIgnoreCase("escolher")) {
				if (sender.hasPermission("multieventos.admin")) {
					if (loteria.getOcorrendo() == false) {
						try {
							int numero = Integer.parseInt(args[1]);
								if (numero > 0 && numero <= Configs.config.getConfig().getInt("numero-maximo")) {
									loteria.setNumero(numero);
									loteria.setTempo(Configs.config.getConfig().getInt("loteria-sorteio-tempo"));
									loteria.setOcorrendo(true);
									 for (Player jogadores : Bukkit.getOnlinePlayers()) {
										 jogadores.playSound(jogadores.getLocation(), Sound.BAT_DEATH, 1.0F, 0.5F);
									 }
									
									(new BukkitRunnable() {
										
										@Override
										public void run() {
											loteria.setTempo((loteria.getTempo() - 10));
											if (loteria.getOcorrendo() == true) {
												if (loteria.getTempo() > 1) {
													Bukkit.broadcastMessage(" ");
													Bukkit.broadcastMessage("          §e§lLoteria");
													Bukkit.broadcastMessage(" §7O evento loteria começou!");
													Bukkit.broadcastMessage(" §7Escolha um número entre §f0 §7e §f" +Configs.config.getConfig().getInt("numero-maximo") );
													Bukkit.broadcastMessage(" §7Prêmio: §f" + format(loteria.getPremio()) + " Coins");
													Bukkit.broadcastMessage(" §7Evento encerrando em: §f" + loteria.getTempo() + " §fsegundo(s)");
													Bukkit.broadcastMessage(" ");
												} else {
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§e§lLOTERIA");
													Bukkit.broadcastMessage("");
													Bukkit.broadcastMessage("§cTempo esgotado!");
													Bukkit.broadcastMessage("§cNão houveram vencedores.");
													Bukkit.broadcastMessage("§cResposta: Número §e" + loteria.getNumero());
													Bukkit.broadcastMessage(" ");
													Stop();
													}
												} else {
													cancel();
												}
											}
										}).runTaskTimer(Main.getInstance(), 0L, 200L);
								}
						} catch (NumberFormatException e) {
							sender.sendMessage("§c[Loteria] O seu número precisa ser maior que §e0 §ce menor ou igual a §e" 
									+ Configs.config.getConfig().getInt("numero-maximo") + "§c.");
									return true;
						}
					} else {
						sender.sendMessage("§cO evento loteria já está ocorrendo.");
						return true;
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}
			}
		}
	}
		
		
		return false;
	}

	private void Stop() {
		loteria.setNumero(-1);
		loteria.setOcorrendo(false);
		loteria.setTempo(0);
	}
	
	public boolean isInt(String string) {
	    try {
	        Integer.parseInt(string);
	        return true;
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	}
	
}
