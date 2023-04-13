package me.zsnow.stonegoldenpig;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.zsnow.stonegoldenpig.manager.SorteioManager;

public class ResgatarCMD implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
			if (cmd.getName().equalsIgnoreCase("gpColetar")) {
				SorteioManager sorteio = SorteioManager.getInstance();
				Player p = (Player) sender;
				if (args.length >= 0) {
				if (sorteio.salvarVencedor.contains(p.getName()) && sorteio.salvarItem.containsKey(p.getName())) {
					ItemStack[] contents = p.getInventory().getContents();
					for (ItemStack item : contents) {
						if(item == null || (item.getType() == Material.AIR)) {
							p.getInventory().addItem(sorteio.salvarItem.get(p.getName()));
							sorteio.salvarItem.remove(p.getName());
							sorteio.salvarVencedor.remove(p.getName());
							p.sendMessage("§aVocê coletou o seu item do porquinho dourado.");
							break;
						} else {
							p.sendMessage("§cGaranta espaço no inventário para coletar.");
							return true;
						}
					}
				} else {
					p.sendMessage("§cVocê não possui nada para coletar.");
					return true;
				}
			}
		}
		return false;
	}

}
