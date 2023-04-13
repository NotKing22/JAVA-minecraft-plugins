package me.zsnow.multieventos.bolao;

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
import me.zsnow.multieventos.API.TitleAPI;
import me.zsnow.multieventos.configAPI.Configs;

public class Commands implements CommandExecutor {
	
	BolaoManager manager = new BolaoManager();
			
	public String format(int value) {
		return EconomyPlugin.getInstance().format(value, true);
	}
	
	public static int count;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("bolao")) {
			if (!(sender instanceof Player)) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lBolão §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/bolao iniciar");
					sender.sendMessage("§f/bolao parar");
					sender.sendMessage("");
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("iniciar")) {
						if (sender.hasPermission("multieventos.admin")) {
							if (manager.getOcorrendo() == false && manager.apostasLiberadas() == false) {
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
								manager.setValorAcumulado(Configs.config.getConfig().getInt("valor-acumulado-inicial"));
								manager.getParticipantes().clear();
								manager.setOcorrendoStatus(true);
								manager.setApostasLiberadas(true);
								BukkitScheduler sh = Bukkit.getServer().getScheduler();
								count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
									
									@Override
									public void run() {
										if (manager.getOcorrendo() == true) {
											if (manager.getTempo() >= 20) {
												manager.setTempo(manager.getTempo() - 20);
												Bukkit.broadcastMessage(" ");
												Bukkit.broadcastMessage("           §e§lBolão");
												Bukkit.broadcastMessage(" §7O prêmio está acumulado em §f" + format(manager.getValorAcumulado()) + " §fcoins");
												Bukkit.broadcastMessage(" §7Digite: §f'/bolao apostar' §7para participar");
												Bukkit.broadcastMessage(" §7Valor para participar §f$" + format(manager.getValorAposta()));
												Bukkit.broadcastMessage(" ");
												return;
											} else {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§e§l[Bolão] §7As apostas foram finalizadas");
												Bukkit.broadcastMessage("§e§l[Bolão] §7O prêmio está acumulado em §f" + format(manager.getValorAcumulado()));
												Bukkit.broadcastMessage("§e§l[Bolão] §7E o vencedor é...");
												Bukkit.broadcastMessage("");
												manager.setApostasLiberadas(false);
												iniciarSorteio();
												sh.cancelTask(count);
											}
										} else {
											sh.cancelTask(count);
										}
									}
								}, 0L, 400L);
							
							} else {
								sender.sendMessage("§cO evento já está ocorrendo!");
								return true;
							}
						}
					}
					if (args[0].equalsIgnoreCase("parar")) {
						if (sender.hasPermission("multieventos.admin")) {
							if (manager.getOcorrendo() == true) {
								Bukkit.broadcastMessage(" ");
								Bukkit.broadcastMessage("§e§l[Bolão] §cO evento bolão foi finalizado");
								Bukkit.broadcastMessage("§e§l[Bolão] §cUm administrador forçou o fim do evento;");
								Bukkit.broadcastMessage(" ");
								for (Player participantes : manager.getParticipantes()) {
									 EconomyPlugin.getInstance().getEconomy().depositPlayer(participantes.getName(), manager.getValorAposta());
									participantes.sendMessage("§aO seu dinheiro do bolão foi devolvido por um Administrador");
								}
								manager.getParticipantes().clear();
								manager.setApostasLiberadas(false);
								manager.setOcorrendoStatus(false);
								Bukkit.getServer().getScheduler().cancelTask(count);
								Bukkit.getServer().getScheduler().cancelTask(contador);
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
								manager.setValorAcumulado(Configs.config.getConfig().getInt("valor-acumulado-inicial"));
								return true;
							} else {
								sender.sendMessage("§cO evento não está ocorrendo!");
								return true;
							}
						}
					}
				}
			
			} else {
			
				Player p = (Player) sender;
				if (args.length == 0) {
					p.sendMessage("");
					p.sendMessage("§e§lBolão §7- §fComandos");
					p.sendMessage("");
					p.sendMessage(" §f/bolao apostar");
				if (p.hasPermission("multieventos.admin")) {
					p.sendMessage(" §c/bolao iniciar");
					p.sendMessage(" §c/bolao parar");
				}
					p.sendMessage("");
				}
				if (args.length >= 1) {
					if (args[0].equalsIgnoreCase("apostar")) {
						if (manager.getOcorrendo() == true && manager.apostasLiberadas() == true) {
							if (!manager.getParticipantes().contains(p)) {
								if (Main.getInstance().economy.has(p, manager.getValorAposta())) {
									EconomyPlugin.getInstance().getEconomy().withdrawPlayer(p, manager.getValorAposta());
									manager.getParticipantes().add(p);
									manager.setValorAcumulado(manager.getValorAcumulado() + manager.getValorAposta());
									p.sendMessage("§eYaY! Agora você está participando do bolão!");
									p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0F, 0.5F);
									return true;
								} else {
									p.sendMessage("§cVocê precisa de §e$" + manager.getValorAposta() + " §cpara participar do bolão." );
									return true;
								}
							} else {
								p.sendMessage("§cVocê já está participando!");
								return true;
							}
						} else {
							p.sendMessage("§cO evento não está ocorrendo ou sua entrada foi encerrada.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("iniciar")) {
						if (p.hasPermission("multieventos.admin")) {
							if (manager.getOcorrendo() == false && manager.apostasLiberadas() == false) {
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
								manager.setValorAcumulado(Configs.config.getConfig().getInt("valor-acumulado-inicial"));
								manager.getParticipantes().clear();
								manager.setOcorrendoStatus(true);
								manager.setApostasLiberadas(true);
								BukkitScheduler sh = Bukkit.getServer().getScheduler();
								count = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
									
									@Override
									public void run() {
										if (manager.getOcorrendo() == true) {
											if (manager.getTempo() > 0) {
												manager.setTempo(manager.getTempo() - 20);
												Bukkit.broadcastMessage(" ");
												Bukkit.broadcastMessage("           §e§lBolão");
												Bukkit.broadcastMessage(" §7O prêmio está acumulado em §f" + format(manager.getValorAcumulado()) + " §fcoins");
												Bukkit.broadcastMessage(" §7Digite: §f'/bolao apostar' §7para participar");
												Bukkit.broadcastMessage(" §7Valor para participar §f$" + format(manager.getValorAposta()));
												Bukkit.broadcastMessage(" ");
												return;
											} else {
												Bukkit.broadcastMessage("");
												Bukkit.broadcastMessage("§e§l[Bolão] §7As apostas foram finalizadas");
												Bukkit.broadcastMessage("§e§l[Bolão] §7O prêmio está acumulado em §f" + format(manager.getValorAcumulado()));
												Bukkit.broadcastMessage("§e§l[Bolão] §7E o vencedor é...");
												Bukkit.broadcastMessage("");
												manager.setApostasLiberadas(false);
												iniciarSorteio();
												sh.cancelTask(count);
											}
										} else {
											sh.cancelTask(count);
										}
									}
								}, 0L, 400L);
							
							} else {
								sender.sendMessage("§cO evento já está ocorrendo!");
								return true;
							}
						} else {
							p.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("parar")) {
						if (p.hasPermission("multieventos.admin")) {
							if (manager.getOcorrendo() == true) {
								Bukkit.broadcastMessage(" ");
								Bukkit.broadcastMessage("§e§l[Bolão] §cO evento bolão foi finalizado");
								Bukkit.broadcastMessage("§e§l[Bolão] §cUm administrador forçou o fim do evento;");
								Bukkit.broadcastMessage(" ");
								for (Player participantes : manager.getParticipantes()) {
									Main.getInstance().economy.depositPlayer(participantes.getName(), manager.getValorAposta());
									EconomyPlugin.getInstance().getEconomy().depositPlayer(p, manager.getValorAposta());
									participantes.sendMessage("§aO seu dinheiro do bolão foi devolvido por um Administrador");
								}
								manager.getParticipantes().clear();
								manager.setApostasLiberadas(false);
								manager.setOcorrendoStatus(false);
								Bukkit.getServer().getScheduler().cancelTask(count);
								Bukkit.getServer().getScheduler().cancelTask(contador);
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
								manager.setValorAcumulado(Configs.config.getConfig().getInt("valor-acumulado-inicial"));
								return true;
							} else {
								p.sendMessage("§cO evento não está ocorrendo!");
								return true;
							}
						} else {
							p.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
							return true;
						}
					}
				}
			}
			
			
			
		}
		return false;
	}
	
	private static int contador;
	@SuppressWarnings("deprecation")
	public void iniciarSorteio() {
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		contador = sh.scheduleAsyncRepeatingTask(Main.getInstance(), new BukkitRunnable() {
			
			int tempo = 20;
			
			@Override
			public void run() {
				if (manager.getOcorrendo() == true) {
					if (tempo > 0) {
						tempo--;
						if (manager.getParticipantes().size() >= 2) {
							Random random = new Random();
							Player vencedor = manager.getParticipantes().get(random.nextInt(manager.getParticipantes().size()));
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									jogadores.sendTitle("§e§lBolão", "§7Vencedor: §f" + vencedor.getName());
									jogadores.playSound(jogadores.getLocation(), Sound.CLICK, 1.0f, 0.5f);
								}
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
							} else {
								Bukkit.broadcastMessage(" ");
								Bukkit.broadcastMessage("§e§l[Bolão] §cO evento bolão foi finalizado");
								Bukkit.broadcastMessage("§e§l[Bolão] §cNão houveram participantes o suficiente.");
								Bukkit.broadcastMessage(" ");
								for (Player participantes : manager.getParticipantes()) {
									EconomyPlugin.getInstance().getEconomy().depositPlayer(participantes.getName(), manager.getValorAposta());
									participantes.sendMessage("§aO seu dinheiro do bolão foi devolvido por um Administrador");
								}
								manager.getParticipantes().clear();
								manager.setApostasLiberadas(false);
								manager.setOcorrendoStatus(false);
								Bukkit.getServer().getScheduler().cancelTask(count);
								sh.cancelTask(contador);
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
								return;
							}
						} else {
							Random random = new Random();
							Player vencedor = manager.getParticipantes().get(random.nextInt(manager.getParticipantes().size()));
								for (Player jogadores : Bukkit.getOnlinePlayers()) {
									TitleAPI.sendTitle(jogadores, 0, 40, 40, "§e§lBolão", "§aVencedor: §f" + vencedor.getName());
									jogadores.playSound(jogadores.getLocation(), Sound.LEVEL_UP, 1.0f, 0.5f);
								}
								Bukkit.broadcastMessage("");
								Bukkit.broadcastMessage("           §e§lBolão");
								Bukkit.broadcastMessage(" §7Evento bolão finalizado");
								Bukkit.broadcastMessage(" §7Vencedor: §f" + vencedor.getName());
								Bukkit.broadcastMessage(" §7Prêmio: §f$" + format(manager.getValorAcumulado()));
								Bukkit.broadcastMessage("");
								EconomyPlugin.getInstance().getEconomy().depositPlayer(vencedor.getName(), manager.getValorAcumulado());
								vencedor.sendMessage("§aVocê recebeu $" +manager.getValorAcumulado()+ " §ado bolão!");
								manager.getParticipantes().clear();
								manager.setOcorrendoStatus(false);
								manager.setApostasLiberadas(false);
								Bukkit.getServer().getScheduler().cancelTask(count);
								sh.cancelTask(contador);
								manager.setTempo(Configs.config.getConfig().getInt("tempo-bolao"));
						}
					} else {
						sh.cancelTask(contador);
					}
				}
			}, 0L, 20L);
		}

}
