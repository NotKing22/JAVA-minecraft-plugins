package me.zsnow.redestone.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Tphere implements CommandExecutor{
	
	  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	  {
	    if (!(sender instanceof Player))
	    {
	      sender.sendMessage("§cComando executavel apenas por jogadores");
	      return true;
	    }
	    Player p = (Player)sender;
	    if (command.getName().equalsIgnoreCase("tphere"))
	    {
	      if (!p.hasPermission("zs.mod"))
	      {
	        p.sendMessage("§cVocê precisa do cargo §6Moderador §cou superior para executar este comando.");
	        return true;
	      }
	      if (args.length == 0)
	      {
	        p.sendMessage("§5§lSPACE: §fUse /tphere <jogador>.");
	        return true;
	      }
	      Player jogador = Bukkit.getPlayer(args[0]);
	      if (jogador == null) {
	        p.sendMessage("§cEste jogador não foi encontrado em nosso banco de dados.");
	      } 
	      
	      if (Tp.Player.contains(jogador.getName())) {
	    	  if (!(p.hasPermission("zs.gerente"))) {
	    		  	@SuppressWarnings("deprecation")
	    		  	String prefix = PermissionsEx.getUser(jogador.getPlayer()).getGroups()[0].getPrefix().replace("&", "§");
	    		  p.sendMessage("§eO player " +prefix+ "§f" +jogador.getName()+ " §eestá com teleporte §cdesativado§e.");
		    		  jogador.sendMessage("§7§o[Server:] §7§o" +p.getName()+ " §7§otentou puxar você.");
		    		  p.playSound(p.getLocation(), Sound.NOTE_PLING, 1.0F, 0.5F);
			    	  return true;
	    	  } else {
	    		  	jogador.teleport(p);
			      p.sendMessage("§5§lSPACE: §fO jogador §d" + jogador.getName() + " §ffoi teleportado até você.");
			      	jogador.sendMessage("§eO membro da equipe §f§l" + p.getName() + " §epuxou você até ele.");
			      for (Player staff : Bukkit.getOnlinePlayers()) {
			    	  if (staff.hasPermission("zs.gerente")) {
			    		  staff.sendMessage("§7§o[Tphere] " +jogador.getName()+ " §7§ofoi puxado para "+p.getName()+ "§7§o.");
		    	  }
		      	}
		      }
	      } else {
	    	  jogador.teleport(p);
		      p.sendMessage("§5§lSPACE: §fO jogador §d" + jogador.getName() + " §ffoi teleportado até você.");
		      if (!(p.hasPermission("zs.gerente"))) {
		    	  jogador.sendMessage("§eO membro da equipe §f§l" + p.getName() + " §epuxou você até ele.");
		      }
		      for (Player staff : Bukkit.getOnlinePlayers()) {
		    	  if (staff.hasPermission("zs.gerente")) {
		    		  staff.sendMessage("§7§o[Tphere] " +jogador.getName()+ " §7§ofoi puxado para "+p.getName()+ "§7§o.");
		    	  }
		      }
	      }
	    }
	    return false;
	  }
}