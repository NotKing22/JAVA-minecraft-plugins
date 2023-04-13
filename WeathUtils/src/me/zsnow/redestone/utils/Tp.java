package me.zsnow.redestone.utils;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Tp implements CommandExecutor{
	
	public static List<String> Player = Main.getPlugin().getConfig().getStringList("tptoggle-on");
	
	 @SuppressWarnings({ "deprecation" })
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if (!(sender instanceof Player)) {
	      sender.sendMessage("§cComando executavel via console");
	      return true;
	    }
	    Player p = (Player)sender;
	    if (cmd.getName().equalsIgnoreCase("tp")) {
	      if (!p.hasPermission("zs.mod")) {
	        p.sendMessage("§cVocê precisa do cargo §6Moderador §cou superior para executar este comando.");
	        return true;
	      }
	      if (args.length == 0) {
	        p.sendMessage("§5§lSPACE: §fUse /tp <Jogador>.");
	        return true;
	      }
	      if (args.length == 1) {
	    	  Player jogador = Bukkit.getPlayer(args[0]);
		      if (jogador == null) {
		        p.sendMessage("§cEste jogador não foi encontrado em nosso banco de dados.");
		        return true;
		      } 
		      if (Player.contains(jogador.getName())) {
		    	  if (!(p.hasPermission("zs.gerente") || (p.hasPermission("ir.tp")))) {
		    		  String prefix = PermissionsEx.getUser(jogador.getPlayer()).getGroups()[0].getPrefix().replace("&", "§");
			    	  p.sendMessage("§eO player " +prefix  +jogador.getName()+ " §eestá com teleporte §cdesativado§e.");
			    	  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
			    	  return true;
		    	  } else {
				      p.teleport(jogador);
				      p.sendMessage("§5§lSPACE: §avocê foi teleportado até §e" + jogador.getName() + " §acom sucesso.");
				      p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
				      if (!(p.hasPermission("zs.gerente"))) {
				    	  for (Player staff : Bukkit.getOnlinePlayers()) {
				    		  if (staff.hasPermission("zs.gerente")) {
				    			  staff.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§ose teleportou até" +jogador.getName()+ "§7§o.");
				    		  }
				    	  }
				      }
				      return true;
		    	  }
		      } else {
			      p.teleport(jogador);
			      p.sendMessage("§5§lSPACE: §avocê foi teleportado até §e" + jogador.getName() + " §acom sucesso.");
			      p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
			      if (!(p.hasPermission("zs.gerente"))) {
			    	  for (Player staff : Bukkit.getOnlinePlayers()) {
			    		  if (staff.hasPermission("zs.gerente")) {
			    			  staff.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§ose teleportou até " +jogador.getName()+ "§7§o.");
			    		  }
			    	  }
			      }
			      return true;
		      }
	    } if (args.length == 2) {
		      Player jogador = Bukkit.getPlayer(args[0]);
		      Player jogador2 = Bukkit.getPlayer(args[1]);
		      if ((jogador == null) || (jogador2 == null)) {
			        p.sendMessage("§cEste jogador não foi encontrado em nosso banco de dados.");
			        return true;
		      }
		      if (Player.contains(jogador.getName()) || (Player.contains(jogador2.getName()))) {
		    	  if (!(p.hasPermission("zs.gerente") || (p.hasPermission("ir.tp")))) {
			    	  p.sendMessage("§eUm dos jogadores está com o teletransporte desabilitado.");
			    	  jogador.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§otentou ir até você.");
			    	  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
			    	  return true;
		    	  } else {
				      jogador.teleport(jogador2);
				      p.sendMessage("§5§lSPACE: §aVocê teleportou §e" +jogador.getName()+ " §aaté §e" +jogador2.getName()+ "§a.");
				      p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
				      if (!(p.hasPermission("zs.gerente"))) {
				    	  for (Player staff : Bukkit.getOnlinePlayers()) {
				    		  if (staff.hasPermission("zs.gerente")) {
				    			  staff.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§ose teleportou " +jogador.getName()+ " §7§oaté " +jogador2.getName()+ "§7§o.");
				    		  }
				    	  }
				      }
				      return true;
		    	  }
		      } else {
			      jogador.teleport(jogador2);
			      p.sendMessage("§5§lSPACE: §aVocê teleportou §e" +jogador.getName()+ " §aaté §e" +jogador2.getName()+ "§a.");
			      p.playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 0.5F);
			      if (!(p.hasPermission("zs.gerente"))) {
			    	  for (Player staff : Bukkit.getOnlinePlayers()) {
			    		  if (staff.hasPermission("zs.gerente")) {
			    			  staff.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§ose teleportou " +jogador.getName()+ " §7§oaté " +jogador2.getName()+ "§7§o.");
			    		  }
			    	  }
			      }
			      return true;
		      }
	    }
    }
    return false;
  }
	
}
