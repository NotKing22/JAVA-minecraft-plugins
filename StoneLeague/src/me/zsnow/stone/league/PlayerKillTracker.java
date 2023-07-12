package me.zsnow.stone.league;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.zsnow.stone.league.League.Level;

public class PlayerKillTracker implements Listener {

	private Map<Player, LinkedList<String>> lastKilledPlayers = new HashMap<>();
	int MAX_PREVIOUS_KILLS = 3;
	
	// fazer a parte do killstreak
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
	    Player victim = event.getEntity();
	    Player killer = victim.getKiller();
	    LeagueCache cache = LeagueCache.getInstance();
	    if (killer != null && killer != victim && cache.hasLeague(victim) && cache.hasLeague(killer) && !me.zsnow.chain.Commands.inArena.contains(killer)) {
	    	LinkedList<String> previousKills = lastKilledPlayers.getOrDefault(victim, new LinkedList<>());
	        int victimLeagueOrdinal = cache.getLeague().get(victim.getName()).ordinal();
	        int killerLeagueOrdinal = cache.getLeague().get(killer.getName()).ordinal();
	        League killerLeague = cache.getLeague().get(killer.getName());
	        League victimLeague = cache.getLeague().get(victim.getName());
	        String killerSymbol = killerLeague.getSymbol();
	        String victimSymbol = victimLeague.getSymbol();

	        if (!previousKills.contains(killer.getName())) {
	            previousKills.addLast(killer.getName());
	            while (previousKills.size() > MAX_PREVIOUS_KILLS) {
	                previousKills.removeFirst();
	            }
	            lastKilledPlayers.put(victim, previousKills);
	        
	            int baseXP = killerLeague.getXPperKill();
	            int differenceInLeagues = victimLeagueOrdinal - killerLeagueOrdinal;
	            int XP = calculateXP(baseXP, differenceInLeagues);

	            if (victimLeague.getXPcurrent() >= XP) {
	                victimLeague.removeXPcurrent(XP);
	            } else {
	                if (victimLeague != League.UNRANKED) {
	                    League newLeague = victimLeague.getPreviousLeague();
	                    int newXP = newLeague.getXPlevelUP() - XP;
	                    if (victimLeague.canDropLeague(XP)) {
	                        victimLeague.update(Level.DOWN, victim);
	    	    	        victimLeague = cache.getLeague().get(victim.getName());
	    	    	        victimSymbol = victimLeague.getSymbol();
	                        infoMSG(level.LEVEL_DOWN, victim, victimSymbol, victimLeague.name());
	                        victimLeague.setXPcurrent(newXP);
	                    }
	                }
	            }
	            if (victimLeague.hasKillStreak()) {
	            	Bukkit.broadcastMessage
	            		    ("§4§lKILLSTREAK §7(" + victimLeague.getSymbol() + "§7) §f" + victim.getName() + 
	            			" §6perdeu seu streak de §4§l" + victimLeague.getKillStreak() +
	            			" §6por §7(" + killerLeague.getSymbol() + "§7) §f" + killer.getName());
	            	victimLeague.resetStreak();
	            }
	            
	            killerLeague.addXPcurrent(XP);
	            victim.sendMessage(sendMsgMorte(killerSymbol, killer, XP));
	            killer.sendMessage(sendMsgAbate(victimSymbol, victim, XP));
	            
	            if (killerLeague.canLevelUP()) {
	                killerLeague.update(Level.UP, killer);
	    	        killerLeague = cache.getLeague().get(killer.getName());
	    	        killerSymbol = killerLeague.getSymbol();
	    	        infoMSG(level.LEVEL_UP, killer, killerSymbol, killerLeague.name());
	            }
	            killerLeague.increaseStreak();
	            if (killerLeague.canStreakBroadcast()) {
	            	Bukkit.broadcastMessage("§4§lKILLSTREAK §7(" + killerLeague.getSymbol() + "§7) §f§l" + killer.getName() + " §6está com um killstreak de §4§l" + killerLeague.getKillStreak());
	            }
	            
	        } else {
	        	
	        	//matando mesmo player
	        	
	            int baseXP = killerLeague.getXPperKill();
	            int differenceInLeagues = victimLeagueOrdinal - killerLeagueOrdinal;
	            int XP = Math.max(calculateXP(baseXP, differenceInLeagues) / lastKilledPlayers.size(), 1);
	            if (victimLeague.getXPcurrent() >= XP) {
	                victimLeague.removeXPcurrent(XP);
	            } else {
	                if (victimLeague != League.UNRANKED) {
	                    League newLeague = victimLeague.getPreviousLeague();
	                    int newXP = newLeague.getXPlevelUP() - XP;
	                    if (victimLeague.canDropLeague(XP)) {
	                    	victimLeague.update(Level.DOWN, victim);
	    	    	        victimLeague = cache.getLeague().get(victim.getName());
	    	    	        victimSymbol = victimLeague.getSymbol();
	    	    	        infoMSG(level.LEVEL_DOWN, victim, victimSymbol, victimLeague.name());
	                        victimLeague.setXPcurrent(newXP);
	                    }
	                }
	            }
	            killerLeague.addXPcurrent(XP);
	            victim.sendMessage(sendMsgMorte(killerSymbol, killer, XP));
	            killer.sendMessage(sendMsgAbate(victimSymbol, victim, XP));

	            if (killerLeague.canLevelUP()) {
	                killerLeague.update(Level.UP, killer);
	    	        killerLeague = cache.getLeague().get(killer.getName());
	    	        killerSymbol = killerLeague.getSymbol();
	                infoMSG(level.LEVEL_UP, killer, killerSymbol, killerLeague.name());
	            }
	        }
	    }
	}

	private int calculateXP(int baseXP, int differenceInLeagues) {
	    if (differenceInLeagues > 0) {
	        return baseXP * (differenceInLeagues + 1);
	    } else if (differenceInLeagues < 0) {
	        return Math.max(baseXP / Math.abs(differenceInLeagues), 10);
	    } else {
	        return baseXP;
	    }
	}
    public LinkedList<String> getLastKilledPlayers(Player player) {
        return lastKilledPlayers.getOrDefault(player, new LinkedList<>());
    }
  
    
    public String sendMsgMorte(String killer_symbol, Player killer, int XP) {
        return "§4§lMORTE §fvocê morreu para §7(" + killer_symbol + "§7) §e§l" + killer.getName() + " §fe perdeu §3§l" + XP + " §3§lXPs";
    }
    
    public String sendMsgAbate(String victim_symbol, Player victim, int xP) {
    	return "§e⚒ §e§lABATE §fvocê eliminou §7(" + victim_symbol + "§7) §e§l" + victim.getName() + " §fe ganhou §3§l" + xP + " §3§lXPs";
    }

    public enum level {
    	LEVEL_UP, LEVEL_DOWN;
    }
    
    public void infoMSG(level level, Player player, String symbol, String league_name) {
    	switch (level) {
			case LEVEL_UP:
		    	player.sendMessage(" ");
		    	player.sendMessage("§e§l⬆ §6§lLEVEL UP! §eVocê obteve uma §lNOVA LIGA!");
		    	player.sendMessage(" §6§lSua nova liga §f➟ §7(" + symbol + "§7) §l" + league_name);
		    	player.sendMessage(" ");
				break;
			case LEVEL_DOWN:
		    	player.sendMessage(" ");
		    	player.sendMessage("§4⬇ §c§lLEVEL DOWN! §f§lVocê perdeu muito XP e desceu de LIGA!");
		    	player.sendMessage(" §6§lSua nova liga §f➟ §7(" + symbol + "§7) §l" + league_name);
		    	player.sendMessage(" ");
				break;

			default:
				break;
		}
    }
    
}
