package me.zsnow.redestone.utils;


import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Cabeca implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {
		if (cmd.getName().equalsIgnoreCase("head")) {
			if (!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			if (!p.hasPermission("zs.head")) {
				p.sendMessage("§cVocê não tem permissão para executar este comando.");
				p.playSound(p.getLocation(), Sound.CAT_MEOW, 1.0F, 0.5F);
				return true;
			}
			if (args.length == 0) {
				p.sendMessage("§cUse /head <jogador>.");
				return true;
			}
			if (args.length == 1) {
				ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
				SkullMeta meta = (SkullMeta) skull.getItemMeta();
				meta.setOwner(args[0]);
				skull.setItemMeta(meta);
				p.getInventory().addItem(skull);
				p.sendMessage("§aCabeça de §e" + args[0] + " §arecebida com sucesso.");
				return true;
			}
		}
		return true;
	}

}
