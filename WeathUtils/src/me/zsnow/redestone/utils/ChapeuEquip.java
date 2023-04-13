package me.zsnow.redestone.utils;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChapeuEquip implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("chapeu")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("§cComo o console equiparia um chapeu? (O.o)");
				return true;
			}
		    Player p = (Player) sender;
		    if (!(p.hasPermission("zs.chapeu"))) {
		    	p.sendMessage("§cVocê não tem permisão para executar este comando.");
		    	return true;
		    }
			if (p.getInventory().getItemInHand().getType() == Material.BANNER) {
				if (p.getInventory().getHelmet() != null) {
					p.sendMessage("§cVocê já está utilizando algo no local do seu capacete.");
					return true;
				}
				p.getInventory().setHelmet(p.getItemInHand());
				p.sendMessage("§aChapéu colocado com sucesso. Aproveite seu novo visual!");
				p.getInventory().removeItem(p.getItemInHand());
				p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0F, 0.5F);
				return true;
			}
		if ((p.getInventory().getItemInHand().getType().isTransparent()) || ((!p.getInventory().getItemInHand().getType().isBlock()))) {
				p.sendMessage("§cItem inválido. Você só pode utilizar blocos como chapéu");
				return true;
			}
			if (p.getInventory().getHelmet() != null) {
				p.sendMessage("§cVocê já está utilizando algo no local do seu capacete.");
				return true;
			}
			p.getInventory().setHelmet(p.getItemInHand());
			p.sendMessage("§aChapéu colocado com sucesso. Aproveite seu novo visual!");
			p.getInventory().removeItem(p.getItemInHand());
			p.playSound(p.getLocation(), Sound.HORSE_SADDLE, 1.0F, 0.5F);
			return true;
		}
		return false;
	}
	
}
