package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CompararIP implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("compararip")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (p.hasPermission("zs.mod")) {
				if (args.length <= 1) {
					p.sendMessage("§cSintaxe: /CompararIP <Jogador1> <jogador2>");
					return true;
				}
				if (args.length == 2) {
					Player jogador1 = Bukkit.getPlayer(args[0]);
						Player jogador2 = Bukkit.getPlayer(args[1]);
							if ((jogador1 != null) && (jogador2 != null)) {	
					String ip1 = jogador1.getAddress().getAddress().getHostAddress();
						String ip2 = jogador2.getAddress().getAddress().getHostAddress();
							if (ip1.equals(ip2)) {
								p.sendMessage("§6§l[§e§l!§6§l] §aOs jogadores §e" + jogador1.getName() + " §ae §e" + jogador2.getName() + " §apossuem o mesmo IP.");
								p.playSound(p.getLocation(), Sound.SLIME_WALK2, 1.0F, 0.5F);
								return true;
					} else {
						p.sendMessage("§6§l[§e§l!§6§l] §cEstes jogadores possuem IP's diferentes.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
						return true;
						}
					} else {
						p.sendMessage("§cUm dos nicks informados não se encontra online.");
						p.playSound(p.getLocation(), Sound.VILLAGER_NO, 1.0F, 0.5F);
						return true;
					}
				}
			} else {
				p.sendMessage("§cVocê precisa do cargo Moderador ou superior para executar este comando.");
				return true;
			}
		}
		return false;
	}
	

}
