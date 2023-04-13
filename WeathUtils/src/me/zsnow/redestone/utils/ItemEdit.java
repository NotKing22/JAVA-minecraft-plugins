package me.zsnow.redestone.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("all")
public class ItemEdit implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String lbl, String[] args) {
		if (cmd.getName().equalsIgnoreCase("editaritem")) {
			if (!(s instanceof Player)) {
				s.sendMessage("§cComando executavel apenas por jogadores");
				return true;
		}
		Player p = (Player)s;
		if (p.hasPermission("zs.gerente")) {
			if (args.length < 1) {
				s.sendMessage("§6Sintaxe: §c/editarItem <Renomear, glow, removelore, addlore, negativo, addflags, removeflags>");
				return true;
		}
		
		ItemStack item = p.getItemInHand();
		
		if (item == null || item.getType() == Material.AIR) {
			s.sendMessage("§cVocê não possui um item válido em sua mão.");
			return true;
		}
			
		ItemMeta meta = item.getItemMeta();
			
		if (args[0].equalsIgnoreCase("renomear")) {
			String nome = "";
			for (int i = 1; i < args.length; i++) {nome += args[i] + " ";}
			meta.setDisplayName(nome.replace('&', '§').trim());
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão foi renomeado!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("addflags")) {
			meta.addItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve a flag adicionada!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("removeflags")) {
			meta.removeItemFlags(ItemFlag.values());
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve a flag removida!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("glow")) {
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve o brilho adicionado!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("removelore")) {
			meta.setLore(null);
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve a lore removida!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("addlore")) {
			List<String> lore = new ArrayList<>();
			if (meta.hasLore()) lore.addAll(meta.getLore());
			String novaLinha = "";
			for (int i = 1; i < args.length; i++) {novaLinha  += args[i] + " ";}
			lore.add(novaLinha.replace('&', '§'));
			meta.setLore(lore);
			item.setItemMeta(meta);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve a lore adicionada!");
			return true;
		}
			
		if (args[0].equalsIgnoreCase("negativo")) {
			item.setAmount(Short.MAX_VALUE);
			s.sendMessage("§6Sucesso§7, §aO item em sua mão teve seu valor alterado para negativo!");
			return true;
		}
		
		s.sendMessage("§6Sintaxe: §c/editarItem <Renomear, glow, removelore, addlore, negativo, addflags, removeflags>");
		return true;
	} else {
		p.sendMessage("§cvocê precisa do cargo §6Gerente §cou superior para executar este comando.");
		return true;
	}
		}
		return false;
	
	}
	private <E extends Enum<E>> List<E> getEnumList(Class<E> enumClass) {
		return new ArrayList<E>(Arrays.asList(enumClass.getEnumConstants()));
	}
	
}

enum Attribute {
	
	DAMAGE("generic.attackDamage"),
	KNOCKBACKRESISTANCE("generic.knockbackResistance"),
	FOLLOWRANGE("generic.followRange"),
	MAXHEALTH("generic.maxHealth"),
	SPEED("generic.movementSpeed");
	
	private String attribute;
	
	Attribute(String attribute) {
		this.attribute = attribute;
	}
	
	public String getAttribute() {
		return this.attribute;
	}
}