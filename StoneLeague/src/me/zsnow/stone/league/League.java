package me.zsnow.stone.league;

import org.bukkit.entity.Player;

public enum League {
	  //  NONE("Nenhuma", 0, 1), 
	    UNRANKED("§f§l" + "-", 0, 500), 
	    PRIMARY("§a" + "☰", 0, 1000), 
	    ADVANCED("§e" + "☲", 0, 1100), 
	    EXPERT("§1" + "☷", 0, 1200), 
	    SILVER("§7" + "✶", 0, 1300), 
	    GOLD("§6" + "✷", 0, 1500), 
	    DIAMOND("§b" + "✦", 0, 1600), 
	    ELITE("§5" + "✹", 0, 1800), 
	    GOD("§c" + "✫", 0, 2000), 
	    LEGENDARY("§4" + "✪", 0, 2500), 
	    CHALLENGER("§3" + "☯", 0, 3000);

		private String symbol;
		
		int killstreak;
		
		private int xp_current;
		private int xp_to_levelup;      
		
		private int xp_per_kill = 36;

		League(String symbol, int xp, int xp_up) {
			this.symbol = symbol;
			this.xp_current = xp;
			this.xp_to_levelup = xp_up; 
		}
		
		public League getNextLeague() {
		    int nextIndex = ordinal() + 1;
		    if (nextIndex < League.values().length) {
		        return League.values()[nextIndex];
		    }
		    return null; 
		}

	    public League getPreviousLeague() {
	        if (this != League.UNRANKED) {
	            int previousIndex = ordinal() - 1;
	            if (previousIndex >= 0) {
	                return values()[previousIndex];
	            }
	        }
	        return null; 
	    }
	
	    public String getSymbol() {
	        return symbol;
	    }
	    
	    public int getXPcurrent() {
	    	return xp_current;
	    }
	    
	    public int getXPperKill() {
	    	return xp_per_kill;
	    }
	    
	    public int getXPlevelUP() {
	    	return xp_to_levelup;
	    }
	    
	    public int getKillStreak() {
	    	return this.killstreak;
	    }
	    
	    public void increaseStreak() {
	    	this.killstreak++;
	    }
	    
	    public void resetStreak() {
	    	this.killstreak = 0;
	    }
	    
	    public boolean hasKillStreak() {
	    	if (this.killstreak % 10 == 0 && this.killstreak > 10) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public boolean canStreakBroadcast() {
	    	if (this.killstreak % 10 == 0 || this.killstreak > 10) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public void setXPcurrent(int currentXP) {
	    	if (this == League.CHALLENGER && getXPcurrent() >= getXPlevelUP()) {
	    		return;
	    	}
	    	this.xp_current = currentXP;
	    }
	    
	    public void addXPcurrent(int currentXP) {
	    	if (this == League.CHALLENGER && getXPcurrent() >= getXPlevelUP()) {
	    		return;
	    	}
	    	this.xp_current = xp_current + currentXP;
	    }
	    
	    public void removeXPcurrent(int currentXP) {
	    	this.xp_current = xp_current - currentXP;
	    }
	    
	    public int sendXPperKill(Player victim, Player killer) {
	    	int XP = xp_per_kill;
	    	
	    	LeagueCache cache = LeagueCache.getInstance();
	    	int victimLeague = cache.getLeague().get(victim.getName()).ordinal();
	    	int killerLeague = cache.getLeague().get(killer.getName()).ordinal();
	    
	    	int diferençaLigas = victimLeague - killerLeague;
	    	
	        if (victimLeague > killerLeague) {
	            XP *= diferençaLigas;
	            
	            cache.getLeague().get(killer.getName()).addXPcurrent(XP);
	            cache.getLeague().get(victim.getName()).removeXPcurrent(XP);
	            
	        } else if (killerLeague > victimLeague) {
	        	XP = Math.max(XP / (diferençaLigas), 1);
	            
	            cache.getLeague().get(victim.getName()).removeXPcurrent(XP);
	            cache.getLeague().get(killer.getName()).addXPcurrent(XP);
	        }
	    	return XP;
	    }

	    public boolean canLevelUP() {
	    	if (getXPcurrent() >= getXPlevelUP() && this != League.CHALLENGER) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public boolean canDropLeague(int XP) {
	    	if (getXPcurrent() <= XP && this != League.UNRANKED) {
	    		return true;
	    	}
	    	return false;
	    }
	    
	    public void update(Level level, Player player) {
	        LeagueCache userCache = LeagueCache.getInstance();
	        League league = userCache.getLeague().get(player.getName());

	        switch (level) {
	            case UP:
	                League nextLeague = league.getNextLeague();
	                int bonusXP = nextLeague.getXPlevelUP() - league.getXPcurrent();
	                if (bonusXP > 0) {
	                    nextLeague.setXPcurrent(0);
	                } else {
	                    nextLeague.addXPcurrent(bonusXP);
	                }
	                userCache.setLeague(player.getName(), nextLeague);
	                break;
	            case DOWN:
	                League previousLeague = league.getPreviousLeague();
	                int remainingXP = league.getXPcurrent() - previousLeague.getXPlevelUP();
	                userCache.setLeague(player.getName(), previousLeague);

	                if (remainingXP > 0) {
	                    previousLeague.setXPcurrent(remainingXP);
	                }
	                break;
		        }
		    }

	    public enum Level {
	        UP,
	        DOWN
	    }
	  
}
