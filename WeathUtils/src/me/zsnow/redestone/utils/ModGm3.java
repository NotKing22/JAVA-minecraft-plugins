package me.zsnow.redestone.utils;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModGm3 implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("gm3")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cComando bloqueado via console.");
				return true;
			}
			Player p = (Player) sender;
			if (!(p.hasPermission("zs.mod"))) {
				p.sendMessage("§cvocê precisa do cargo Moderador ou superior para executar este comando.");
				p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage("§cSintaxe: /gm3 [ON/OFF].");
				p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
				return true;
			}
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("on")) {
					if (p.getGameMode().equals(GameMode.SPECTATOR)) {
						p.sendMessage("§cvocê já se encontra no GameMode §6SPECTATOR§c.");
						return true;
					} else {
						p.setGameMode(GameMode.SPECTATOR);
						p.sendMessage("§eModo spectator §aativado.");
						p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0F, 0.5F);
						return true;
					}
				} else if (args[0].equalsIgnoreCase("off")) {
					if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
						p.sendMessage("§cvocê já se encontra fora do GameMode §6SPECTATOR§c.");
						return true;
					} else {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage("§eModo spectator §cdesativado.");
						p.playSound(p.getLocation(), Sound.ITEM_PICKUP, 1.0F, 0.5F);
						return true;
						}
					} else {
						p.sendMessage("§cUso incorreto, utilize: /gm3 [ON/OFF].");
						p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
						return true;
				}
			}
		}	
		return true;
	}
	
}
