package me.zsnow.multieventos.quiz;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

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
import me.zsnow.multieventos.configAPI.Configs;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class QuizCMD implements CommandExecutor, Listener {
	
	QuizManager quiz = QuizManager.quiz;
	private NumberFormat formatarTempo = new DecimalFormat("#.#");
	public String format(double d) {
		return EconomyPlugin.getInstance().format(d, true);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("quiz")) {
			if (sender.hasPermission("multieventos.admin")) {
				if (args.length == 0) {
					sender.sendMessage("");
					sender.sendMessage("§e§lQUIZ §7- §fComandos");
					sender.sendMessage("");
					sender.sendMessage("§f/quiz Iniciar §7- §fInicia o evento com pergunta aleatória.");
					sender.sendMessage("§f/quiz Parar §7- §fPara o evento");
					sender.sendMessage("§f/quiz enviar §7- §fDigite e siga o passo a passo.");
					sender.sendMessage("§f/quiz resposta §7- §fVerifique a resposta do quiz.");
					sender.sendMessage("§f/quiz dica <Mensagem> §7- §fEnvie uma dica para os jogadores.");
					sender.sendMessage("§f/quiz responder <resposta> §7- §fUtilize para responder.");
					sender.sendMessage("");
					return true;
				}
			} else {
				if (args.length <= 1) {
					sender.sendMessage("");
					sender.sendMessage("§e§lQUIZ §7- §fComando");
					sender.sendMessage("");
					sender.sendMessage("§f/quiz responder <resposta> §7- §fUtilize para responder.");
					sender.sendMessage("");
					return true;
				}
			}
				if (args.length >= 2) {
					if (args[0].equalsIgnoreCase("responder")) {
						if (quiz.getOcorrendo() == true) {
							final String msg = getMensagem(args).replaceFirst("\\s++$", "");
							if ((msg.equalsIgnoreCase(quiz.getResposta()))) {
								quiz.setOcorrendo(false);
								 for (Player jogadores : Bukkit.getOnlinePlayers()) {
									 jogadores.playSound(jogadores.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
									 TitleAPI.sendTitle(jogadores, 10, 40, 10, "§e§lQUIZ", "§aVencedor: §f" + sender.getName());
								 }
								 
								 Bukkit.broadcastMessage("");
								 Bukkit.broadcastMessage("        §e§lQuiz");
								 Bukkit.broadcastMessage("§f" + sender.getName() + " §7venceu o evento!");
								 Bukkit.broadcastMessage("§7Pergunta: §f" + quiz.getPergunta());
								 Bukkit.broadcastMessage("§7Resposta: §6§l" + quiz.getResposta());
								 Bukkit.broadcastMessage("§7Tempo decorrido: §f" + formatarTempo.format(quiz.getTempo()) + " §7segundos");
								 Bukkit.broadcastMessage("§7Prêmio: §f" + format(quiz.getPremio()));
								 Bukkit.broadcastMessage("");
								 
								 sender.sendMessage("§aVocê recebeu " + format(quiz.getPremio()) + " §acoins do evento.");
								 quiz.Stop();
								 EconomyPlugin.getInstance().getEconomy().depositPlayer(sender.getName(), quiz.getPremio());
								 
								 return true;
							} else {
								if (quiz.getAlmostAnswerAllowed() == true && (msg.contains(quiz.getResposta()))) {
									quiz.setOcorrendo(false);
									 for (Player jogadores : Bukkit.getOnlinePlayers()) {
										 jogadores.playSound(jogadores.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
										 TitleAPI.sendTitle(jogadores, 10, 40, 10, "§e§lQUIZ", "§aVencedor: §f" + sender.getName());
									 }
									 
									 Bukkit.broadcastMessage("");
									 Bukkit.broadcastMessage("        §e§lQuiz Perguntas & Respostas");
									 Bukkit.broadcastMessage(" §f" + sender.getName() + " §7venceu o evento!");
									 Bukkit.broadcastMessage(" §7Pergunta: §f" + quiz.getPergunta());
									 Bukkit.broadcastMessage(" §7Resposta: §6§l" + quiz.getResposta());
									 Bukkit.broadcastMessage(" §7Tempo decorrido: §f" + formatarTempo.format(quiz.getTempo()) + " §7segundos");
									 Bukkit.broadcastMessage(" §7Prêmio: §f" + format(quiz.getPremio()));
									 Bukkit.broadcastMessage("");
									 
									 sender.sendMessage("§aVocê recebeu " + format(quiz.getPremio()) + " §acoins do evento.");
									 quiz.Stop();
									 EconomyPlugin.getInstance().getEconomy().depositPlayer(sender.getName(), quiz.getPremio());
									return true;
								}
								if (((msg.contains(quiz.getResposta())))) {
									sender.sendMessage("§e§lQUIZ: §cVocê chegou próximo da resposta!");
									Player p = (Player) sender;
									p.playSound(p.getLocation(), Sound.VILLAGER_IDLE, 1.0F, 0.5F);
									return true;
								}
								sender.sendMessage("§e§lQUIZ: §cResposta incorreta!");
								Player p = (Player) sender;
								p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
								return true;
							}
						} else {
							sender.sendMessage("§cO evento quiz não está acontecendo.");
							return true;
						}
					}
				}
				if (sender.hasPermission("multieventos.admin")) {
					if (args.length >= 1) {
						if ((args[0].equalsIgnoreCase("dica") && args.length == 1)) {
							sender.sendMessage("§cUse: /quiz dica <mensagem>");
							return true;
						}
						if (args[0].equalsIgnoreCase("dica")) {
							if (quiz.getOcorrendo() == true) {
								final String msg = getMensagem(args).replaceFirst("\\s++$", "");
								 Bukkit.broadcastMessage("");
								 Bukkit.broadcastMessage("        §e§lQuiz §6§lDICA");
								 Bukkit.broadcastMessage(" §7A dica é: §F§l" + msg);
								 Bukkit.broadcastMessage(" §7Tempo decorrido até o momento: §f" + formatarTempo.format(quiz.getTempo()) + " §7segundos");
								 Bukkit.broadcastMessage("");
								 return true;
							} else {
								sender.sendMessage("§cO evento quiz não está ocorrendo.");
								return true;
							}
						}
					}
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("cancelar")) {
							Player p = (Player) sender;
							quiz.cachePergunta.remove(p);
							quiz.cacheResposta.remove(p);
							quiz.etapas.remove(p);
							quiz.Stop();
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§e§lQUIZ");
							Bukkit.broadcastMessage("");
							Bukkit.broadcastMessage("§cEvento encerrado");
							Bukkit.broadcastMessage("§cpor um Administrador.");
							Bukkit.broadcastMessage("");
							p.sendMessage("§eVocê cancelou o processo de criação do Quiz.");
							return true;
						}
						if (args[0].equalsIgnoreCase("enviar")) {
							if (quiz.getOcorrendo() == false) {
								Player p = (Player) sender;
								quiz.cachePergunta.remove(p);
								quiz.cacheResposta.remove(p);
								quiz.etapas.remove(p);
								if (!quiz.chatEvent.contains(p)) {
									quiz.chatEvent.add(p);
									quiz.etapas.put(p, 1);
									
									p.sendMessage("");
									p.sendMessage("");
									p.sendMessage(" §eDigite no chat a §npergunta§e que você deseja enviar para o evento.");
									p.sendMessage(" §fVocê pode encerrar essa ação digitando §f'§7cancelar§f'");
									p.sendMessage("");
									return true;
								} else {
									p.sendMessage("§cVocê já está realizando essa ação. Para cancelar use: /quiz cancelar");
									return true;
								}
									
							}
							return true;
						}
						if (args[0].equalsIgnoreCase("resposta")) {
							if (quiz.getOcorrendo() == true) {
								sender.sendMessage("§e§lQUIZ:");
								sender.sendMessage("§7Pergunta: §f " + quiz.getPergunta());
								sender.sendMessage("§7Resposta: §f " + quiz.getResposta());
								return true;
							} else {
								sender.sendMessage("§cO evento quiz não está ocorrendo.");
								return true;
							}
						}
						if (args[0].equalsIgnoreCase("parar")) {
							if (quiz.getOcorrendo() == true) {
								quiz.Stop();
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§e§lQUIZ");
									Bukkit.broadcastMessage("");
									Bukkit.broadcastMessage("§cEvento encerrado");
									Bukkit.broadcastMessage("§cpor um Administrador.");
									Bukkit.broadcastMessage("");
									for (Player jogadores : Bukkit.getOnlinePlayers()) {
										jogadores.playSound(jogadores.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
									}
									return true;
							} else {
								sender.sendMessage("§cO evento Quiz não está ocorrendo.");
								return true;
							}
						}
						if (args[0].equalsIgnoreCase("iniciar")) {
							if (quiz.getOcorrendo() == false) {
								
								ArrayList<String> position = new ArrayList<>();
									for (String key : Configs.config.getConfig().getConfigurationSection("quiz-faq").getKeys(false)) {
										position.add(key);
									}
									int RNG = new Random().nextInt(position.size() + 1);
									while (RNG == 0) {
										RNG = new Random().nextInt(position.size() + 1);
									}
									quiz.setPergunta(Configs.config.getConfig().getString("quiz-faq." + RNG + ".P"));
									quiz.setResposta(Configs.config.getConfig().getString("quiz-faq." + RNG + ".Q"));
									
									quiz.setTempo(0);
									sendBukkitMessageEvent();
									
									quiz.setOcorrendo(true);
									StartTask();
									return true;
								}  else {
									sender.sendMessage("§aO evento Quiz já está ocorrendo.");
									return true;
							}
						}
					}
				} else {
					sender.sendMessage("§cVocê precisa do cargo Administrador ou superior para executar este comando.");
					return true;
				}

		}
		return false;
	}
	
	
	
	@EventHandler
	public void responder(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		if (quiz.chatEvent.contains(p)) {
			e.setCancelled(true);
			if (e.getMessage().equalsIgnoreCase("cancelar")) {
				quiz.cachePergunta.remove(p);
				quiz.cacheResposta.remove(p);
				quiz.etapas.remove(p);
				quiz.chatEvent.remove(p);
				p.sendMessage("§aVocê cancelou esta ação.");
				return;
			}
			if (quiz.getOcorrendo() == false) {
				if (quiz.etapas.get(p) == 1) {  // ETAPA 1
					quiz.cachePergunta.put(p, e.getMessage());
					quiz.etapas.replace(p, 2);
					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(" §eDigite no chat a §nresposta§e que você deseja enviar para o evento.");
					p.sendMessage(" §fVocê pode encerrar essa ação digitando §f'§7cancelar§f'");
					p.sendMessage("");
					return;
					}
				if (quiz.etapas.get(p) == 2) { // ETAPA 2
					quiz.cacheResposta.put(p, e.getMessage());
					quiz.etapas.replace(p, 3);
					
					 TextComponent jsonYES = new TextComponent("§a§l[CONFIRMAR] ");
			         TextComponent jsonNO = new TextComponent(" §c§l[NEGAR]");
			         
			         jsonYES.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "Y"));
			         jsonNO.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "N"));
			        
			         jsonYES.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique para permitir.").create()));
			         jsonNO.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique para negar.").create()));
				
			         jsonYES.addExtra(jsonNO);
			         
					p.sendMessage("");
					p.sendMessage("");
					p.sendMessage(" §6§lAgora uma pergunta: §eVocê deseja que o jogador vença caso ele digite uma palavra semelhante a sua resposta?");
					p.sendMessage(" §fExemplo: [Resposta: Uma bola] [Resposta do jogador: Bola]");
					p.sendMessage("");
					p.spigot().sendMessage(jsonYES);
					p.sendMessage("");
					return;
					}
				if (quiz.etapas.get(p) == 3) { // ETAPA 3
					if (quiz.getOcorrendo() == false) {
						if (e.getMessage().equalsIgnoreCase("Y") || e.getMessage().equalsIgnoreCase("n")) {
							if (e.getMessage().equalsIgnoreCase("Y")) {
								quiz.setAlmostAnswerBoolean(true);
							}
							if (e.getMessage().equalsIgnoreCase("N")) {
								quiz.setAlmostAnswerBoolean(false);
							}
						} else {
							 TextComponent jsonYES = new TextComponent("§a§l[CONFIRMAR] ");
					         TextComponent jsonNO = new TextComponent(" §c§l[NEGAR]");
					         
					         jsonYES.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "Y"));
					         jsonNO.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "N"));
					        
					         jsonYES.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClique para permitir.").create()));
					         jsonNO.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§cClique para negar.").create()));
						
					         jsonYES.addExtra(jsonNO);
					         p.sendMessage("");
							p.sendMessage("§c§lVocê precisa realizar o processo de confirmação para prosseguir.");
							p.sendMessage("");
							p.sendMessage(" §6§lAgora uma pergunta: §eVocê deseja que o jogador vença caso ele digite uma palavra semelhante a sua resposta?");
							p.sendMessage(" §fExemplo: [Resposta: Uma bola] [Resposta do jogador: Bola]");
							p.sendMessage("");
							p.spigot().sendMessage(jsonYES);
							return;
						}
							quiz.setPergunta(quiz.cachePergunta.get(p));
							quiz.setResposta(quiz.cacheResposta.get(p));
							quiz.cachePergunta.remove(p);
							quiz.cacheResposta.remove(p);
							quiz.etapas.remove(p);
							quiz.chatEvent.remove(p);
							quiz.setTempo(0);
							sendBukkitMessageEvent();
							quiz.setOcorrendo(true);
							StartTask();
							return;
						} else {
							quiz.cachePergunta.remove(p);
							quiz.cacheResposta.remove(p);
							quiz.etapas.remove(p);
							quiz.chatEvent.remove(p);
							p.sendMessage("§aAção cancelada pois o evento já está ocorrendo.");
						}
					}
				} else {
					quiz.etapas.remove(p);
					quiz.chatEvent.remove(p);
					p.sendMessage("§aAção cancelada pois o evento já está ocorrendo.");
				}
			}
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
				if (quiz.getOcorrendo() == true) {
					quiz.setTempo(quiz.getTempo() + 0.1D);
					for (Player jogadores : Bukkit.getOnlinePlayers()) {
						ActionBarAPI.sendActionBarMessage(jogadores, "§e§lQUIZ §cDuração: " + formatarTempo.format(quiz.getTempo()));
					}
					if (quiz.getTempo() >= 200) {
						quiz.Stop();
						Bukkit.broadcastMessage("");
						Bukkit.broadcastMessage(" §e§lQUIZ");
						Bukkit.broadcastMessage("§cTempo esgotado!");
						Bukkit.broadcastMessage("§cNão houveram vencedores.");
						Bukkit.broadcastMessage("");
						sh.cancelTask(count);
					}
				} else {
					quiz.Stop();
					sh.cancelTask(count);
					return;
				}
			}
		}, 0L, 2L);
	}
	
	public static String getMensagem(String[] args) {
		StringBuilder sb = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			sb.append(args[i] + " ");
		}
		return sb.toString();
	}
	
	public void sendBukkitMessageEvent() {
		Bukkit.broadcastMessage("");
		Bukkit.broadcastMessage("        §e§lQuiz Perguntas & Respostas");
		Bukkit.broadcastMessage(" §7Pergunta: §f" + quiz.getPergunta());
		Bukkit.broadcastMessage(" §7Prêmio: §f" + format(quiz.getPremio()));
		Bukkit.broadcastMessage(" §7Responda com o seguinte comando:");
		Bukkit.broadcastMessage("  §f/quiz responder <resposta>");
		Bukkit.broadcastMessage(" ");
	}

}
