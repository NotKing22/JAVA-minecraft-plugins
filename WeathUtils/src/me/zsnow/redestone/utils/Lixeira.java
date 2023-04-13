package me.zsnow.redestone.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Lixeira implements CommandExecutor, Listener {
	// Kibado, mas feito 100% por eu
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lixeira")) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("§cSe um console nao e um jogador, por que voce quer uma lixeira???");
			return true;
		}
		Player p = (Player) sender;
		Inventory inv = Bukkit.getServer().createInventory(null, 3*9, "§6Escolha abaixo:");
		ItemStack caldeirao = new ItemStack(Material.CAULDRON_ITEM);
		ItemMeta metacaldeirao = caldeirao.getItemMeta();
		ItemStack barreira = new ItemStack(Material.BARRIER);
		ItemMeta barreirameta = barreira.getItemMeta();
		metacaldeirao.setDisplayName("§eAbrir a lixeira");
		barreirameta.setDisplayName("§eLimpar inventário");
		ArrayList<String> lorecaldeirao = new ArrayList<>();
		lorecaldeirao.add("§bAo clicar, abrirá a lixeira.");
		lorecaldeirao.add("§bJogue itens específicos no lixo.");
		metacaldeirao.setLore(lorecaldeirao);
		ArrayList<String> lorebarreira = new ArrayList<>();
		lorebarreira.add("§c§lCUIDADO§e, ");
		lorebarreira.add("§eesta função limpará §e§nTODO§e seu inventário");
		lorebarreira.add("§7(Possui menu de confirmação).");
		barreirameta.setLore(lorebarreira);
		caldeirao.setItemMeta(metacaldeirao);
		barreira.setItemMeta(barreirameta);
		inv.setItem(11, caldeirao);
		inv.setItem(15, barreira);
		p.openInventory(inv);
		p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0F, 0.5F);
		}
		return true;
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equalsIgnoreCase("§6Escolha abaixo:")) {
			e.setCancelled(true);
			if (e.getCurrentItem().getType() == Material.CAULDRON_ITEM) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eAbrir a lixeira")) {
					Inventory lixeira = Bukkit.getServer().createInventory(null, 6*9, "§c[Lixeira] §7§lDescarte itens");
					p.openInventory(lixeira);
					p.playSound(p.getLocation(), Sound.CHEST_OPEN, 1.0F, 0.5F);
				}
			} else if (e.getCurrentItem().getType() == Material.BARRIER){
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§eLimpar inventário")) {
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
					Inventory confirm = Bukkit.getServer().createInventory(null, 3*9, "§6Confirme sua decisão");
					ItemStack greenwool = new ItemStack(35, 1, (short)5);
					ItemMeta metawool = greenwool.getItemMeta();
					metawool.setDisplayName("§aClique para confirmar.");
					ItemStack redwool = new ItemStack(35, 1, (short)14);
					ItemMeta metawool2 = redwool.getItemMeta();
					metawool2.setDisplayName("§cClique para cancelar.");
					greenwool.setItemMeta(metawool);
					redwool.setItemMeta(metawool2);
					confirm.setItem(11, greenwool);
					confirm.setItem(15, redwool);
					p.openInventory(confirm);
				}
			}
		} else if (e.getInventory().getName().equalsIgnoreCase("§6Confirme sua decisão")) {
			if (e.getCurrentItem().getType() == Material.WOOL) {
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§aClique para confirmar.")) {
					p.closeInventory();
					p.sendMessage
					("§aSeu inventário foi limpo com sucesso. A staff não se responsabiliza por itens perdidos");
					p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1.0F, 0.5F);
					p.getInventory().clear();
				}
				else if (e.getCurrentItem().getItemMeta().getDisplayName().contains("§cClique para cancelar.")) {
					p.closeInventory();
					p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
					p.sendMessage("§cLixeira fechada. Nenhum item seu foi perdido durante o processo.");
				}
			}
		}
	}
}
	

