package me.zsnow.redestone.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Btp implements CommandExecutor {

	public static List<String> BlackList = Main.getPlugin().getConfig().getStringList("btp-blacklist");
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("btp")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cComando apenas para jogadores.");
				return true;
			}
			Player p = (Player) sender;
			if (!(p.hasPermission("zs.ajudante"))) {
				p.sendMessage("§cVocê precisa do cargo §eAjudante §cou superior para executar este comando.");
				p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage("§c§oSintaxe: /btp <jogador>");
				return true;
			}
			if (args.length == 1) {
				Player target = Bukkit.getPlayer(args[0]);
					if (target == null) {
						p.sendMessage("§cJogador não encontrado em nosso banco de dados.");
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
						return true;
				}
			if (!(BlackList.contains(p.getName()))) {
				if (!(Tp.Player.contains(target.getName()))) {	
					if (p.getWorld().getName().equalsIgnoreCase(Main.getPlugin().getConfig().getString("btp-allow-world"))) {
						if (target.getWorld().getName().equalsIgnoreCase(Main.getPlugin().getConfig().getString("btp-allow-world"))) {
								p.teleport(target);
									p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
										p.sendMessage("§eTeleportado até §d" +target.getName()+ " §eem modo ajudante.");
								for (Player staff : Bukkit.getOnlinePlayers()) {
									if (staff.hasPermission("zs.admin")) {
										String prefix = PermissionsEx.getUser(p.getPlayer()).getGroups()[0].getPrefix().replace("&", "§");
										String prefix2 = PermissionsEx.getUser(target.getPlayer()).getGroups()[0].getPrefix().replace("&", "§");
										staff.sendMessage("§e§l[BTP] " + prefix + p.getName() + " §3se teleportou até §7" +prefix2 + target.getName() + "§3.");
								}
							}
						} else {
						p.sendMessage("§cParece que o jogador §c" +target.getName()+ " §cse encontra fora do mundo de terrenos, portanto, o teleporte para você é inacessível.");
						return true;
						}
					} else {
					p.sendMessage("§cVocê não tem permissão para executar este comando fora do mundo dos Terrenos.");
					return true;
					}
				} else {
					 String prefix = PermissionsEx.getUser(target.getPlayer()).getGroups()[0].getPrefix().replace("&", "§");
		    	  p.sendMessage("§eO player " +prefix+ "§f" +target.getName()+ " §eestá com teleporte §cdesativado§e.");
		    	  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
		    	  return true;
					}
				} else {
					p.sendMessage("§4§lBLACKLIST: §6Você está impossibilitado de usar o BTP.");
					return true;
				}
			}
		}
		return false;
	}
}
