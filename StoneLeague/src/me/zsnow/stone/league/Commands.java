package me.zsnow.stone.league;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.zsnow.stone.league.League.Level;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("league")) {
			if (!(sender instanceof Player)) return false;
			
			Player p = (Player) sender;
			if (args.length == 0) {
				if (LeagueCache.getInstance().hasLeague(p)) {
					League league = LeagueCache.getInstance().getLeague().get(p.getName());
					p.sendMessage(" ");
					p.sendMessage("§6§lSua liga: §7(" + league.getSymbol() + "§7) " + league.name() + " §f(§3" + league.getXPcurrent() + "§f/§3" + league.getXPlevelUP() + " XP§f)");
					try {
						p.sendMessage("§6§lSua próxima liga: §7(" + league.getNextLeague().getSymbol() + "§7) " + league.getNextLeague().name());
						p.sendMessage(" ");
					} catch (NullPointerException e) {
						p.sendMessage("§a§lVocê está na liga máxima!");
						p.sendMessage(" ");
					}
				} else {
					League league = League.UNRANKED;
					p.sendMessage(" ");
					p.sendMessage(" §6§lSua liga: §7(" + league.getSymbol() + "§7) §l" + league.name() + " §f(§3" + league.getXPcurrent() + "§f/§3" + league.getXPlevelUP() + " XP§f)");
					p.sendMessage(" §6§lSua próxima liga: §7(" + league.getNextLeague().getSymbol() + "§7) §l" + league.getNextLeague().name());
					p.sendMessage(" ");
				}
				p.sendMessage(" §6ℹ §7Confira as ligas digitando '§f/ligas listar§7'.");
				return true;
			}
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("listar")) {
					   League[] leagues = {
						    League.UNRANKED,
						    League.PRIMARY,
						    League.ADVANCED,
						    League.EXPERT,
						    League.SILVER,
						    League.GOLD,
						    League.DIAMOND,
						    League.ELITE,
						    League.GOD,
						    League.LEGENDARY,
						    League.CHALLENGER
						};
						p.sendMessage(" ");
						p.sendMessage("§b⚒ §eRanking de ligas do servidor:");
						for (League league : leagues) {
						    p.sendMessage("  §7(" + league.getSymbol() + "§7) §7§l" + league.name() + " §f(§30§f/§3" + league.getXPlevelUP() + "XP§f)");
						}
						p.sendMessage(" ");
						return true;
				}
				if (p.hasPermission("zs.gerente")) {
					LeagueCache cache = LeagueCache.getInstance();
					if (args[0].equalsIgnoreCase("setleague")) {
						if (args.length < 3) {
							p.sendMessage("§cSintaxe: /league <setLeague> <nickname> <league>");
							return true;
						}

					}
					if (args[0].equalsIgnoreCase("setXP")) {
						if (args.length < 3) {
							p.sendMessage("§cSintaxe: /league <setXP> <nickname> <xp>");
							return true;
						}
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							League league = cache.getLeague().get(target.getName());
							try {
								int XP = Integer.parseInt(args[2]);
								league.setXPcurrent(XP);
								p.sendMessage("§3§lXPs: §fVocê setou o XP de §e" + target.getName() + " §fpara §3§l" + XP + " XPs");
								if (league.canLevelUP()) {
									league.update(Level.UP, target);
								}
								return true;
							} catch (Exception e) {
								p.sendMessage("§cInsira um número válido.");
								return true;
							}
						} else {
							p.sendMessage("§cO jogador não está online.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("addXP")) {
						if (args.length < 3) {
							p.sendMessage("§cSintaxe: /league <addXP> <nickname> <xp>");
							return true;
						}
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							League league = cache.getLeague().get(target.getName());
							try {
								int XP = Integer.parseInt(args[2]);
								league.addXPcurrent(XP);;
								p.sendMessage("§3§lXPs: §fVocê adicionou para §e" + target.getName() + " §fa quantia de §3§l" + XP + " XPs");
								if (league.canLevelUP()) {
									league.update(Level.UP, target);
								}
								return true;
							} catch (Exception e) {
								p.sendMessage("§cInsira um número válido.");
								return true;
							}
						} else {
							p.sendMessage("§cO jogador não está online.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("removeXP")) {
						if (args.length < 3) {
							p.sendMessage("§cSintaxe: /league <removeXP> <nickname> <xp>");
							return true;
						}
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							League league = cache.getLeague().get(target.getName());
							try {
								int XP = Integer.parseInt(args[2]);
								league.removeXPcurrent(XP);
								p.sendMessage("§3§lXPs: §fVocê removeu de §e" + target.getName() + " §fa quantia de §3§l" + XP + " XPs");
								return true;
							} catch (Exception e) {
								p.sendMessage("§cInsira um número válido.");
								return true;
							}
						} else {
							p.sendMessage("§cO jogador não está online.");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("setleague")) {
						if (args.length < 3) {
							p.sendMessage("§cSintaxe: /league <addXP> <nickname> <xp>");
							return true;
						}
						Player target = Bukkit.getPlayer(args[1]);
						if (target != null) {
							try {
								League league = League.valueOf(args[2].toUpperCase());
								league.setXPcurrent(0);
								cache.setLeague(target.getName(), league);
								p.sendMessage("§6§lLEAGUE: §6Você definiu a liga de §l" + target.getName() + " §6como §7(" + league.getSymbol() + "§7) §7" + league.name());
								return true;
							} catch (IllegalArgumentException e) {
								p.sendMessage("§cInsira uma liga válida.");
								return true;
							}
						} else {
							p.sendMessage("§cO jogador não está online.");
							return true;
						}
					}
					p.sendMessage("§cSintaxe: /league <setLeague/setXP/addXP/removeXP> <nickname> <league/xp>");
					return true;
				} else {
					if (LeagueCache.getInstance().hasLeague(p)) {
						League league = LeagueCache.getInstance().getLeague().get(p.getName());
						p.sendMessage(" ");
						p.sendMessage("§6§lSua liga: §7(" + league.getSymbol() + "§7) " + league.name() + " §f(§3" + league.getXPcurrent() + "§f/§3" + league.getXPlevelUP() + " XP§f)");
						try {
							p.sendMessage("§6§lSua próxima liga: §7(" + league.getNextLeague().getSymbol() + "§7) " + league.getNextLeague().name());
							p.sendMessage(" ");
						} catch (NullPointerException e) {
							p.sendMessage("§a§lVocê está na liga máxima!");
						}
					} else {
						League league = League.UNRANKED;
						p.sendMessage(" ");
						p.sendMessage(" §6§lSua liga: §7(" + league.getSymbol() + "§7) §l" + league.name() + " §f(§3" + league.getXPcurrent() + "§f/§3" + league.getXPlevelUP() + " XP§f)");
						p.sendMessage(" §6§lSua próxima liga: §7(" + league.getNextLeague().getSymbol() + "§7) §l" + league.getNextLeague().name());
						p.sendMessage(" ");
					}
					p.sendMessage(" §6ℹ §7Confira as ligas digitando '§f/ligas listar§7'.");
					return true;
				}
			}
		}
		return false;
	}
	
}



/*
League league = League.PRIMARY;
Bukkit.broadcastMessage("League: " + league.getSymbol() + " " + league.name() + "§f. Próxima liga: " + league.getNextLeague());
Bukkit.broadcastMessage("XP ATUAL: " + league.xp_current + "/" + league.xp_to_levelup);

					League liga = League.UNRANKED;
					LeagueCache.getInstance().league.put(p, liga);

*/