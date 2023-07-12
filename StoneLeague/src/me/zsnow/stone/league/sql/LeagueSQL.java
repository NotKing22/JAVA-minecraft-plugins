package me.zsnow.stone.league.sql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.zsnow.stone.league.League;
import me.zsnow.stone.league.LeagueCache;
import me.zsnow.stone.league.Main;

public class LeagueSQL extends LeagueConnection {
	
	private static LeagueSQL instance = new LeagueSQL();
	
	public static LeagueSQL getInstance() {
		return instance;
	}
	
	public void loadToCache() {
		if (con == null) {
			openConnection();
		}
		
		String selectQuery = "SELECT * FROM league_db";
		LeagueCache cache = LeagueCache.getInstance();
		League league = League.UNRANKED;
		
		try (PreparedStatement stm = con.prepareStatement(selectQuery)){
			ResultSet rs = stm.executeQuery();
			
			while (rs.next()) {
				
				String player = rs.getString("player");
				String league_name = rs.getString("league");
				int currentXP = rs.getInt("current_xp");
				
				league = League.valueOf(league_name);
				league.setXPcurrent(currentXP);
				
				cache.setLeague(player, league);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void saveToSQL(Player player) {
	    if (con == null) {
	        openConnection();
	    }

	    String selectQuery = "SELECT * FROM league_db WHERE uuid = ? OR player = ?";
	    LeagueCache cache = LeagueCache.getInstance();

	    try (PreparedStatement stm = con.prepareStatement(selectQuery)) {
	        stm.setString(1, player.getUniqueId().toString());
	        stm.setString(2, player.getName());

	        ResultSet resultSet = stm.executeQuery();
	        if (resultSet.next()) {
	        	executeUpdateSQL(player);
	            return;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return;
	    }

	    League league = cache.getLeague().get(player.getName());
	    if (league != null) {
	        String insertQuery = "INSERT INTO league_db (uuid, player, league, current_xp) VALUES (?,?,?,?)";
	        try (PreparedStatement insertStm = con.prepareStatement(insertQuery)) {
	            insertStm.setString(1, player.getUniqueId().toString());
	            insertStm.setString(2, player.getName());
	            insertStm.setString(3, league.name());
	            insertStm.setInt(4, league.getXPcurrent());
	            insertStm.execute();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void executeUpdateSQL(Player p) {
		if (con == null) {
			openConnection();
		}
		
		String updateQuery = "UPDATE league_db SET league = ?, current_xp = ? WHERE uuid = ? OR player = ?";
		try (PreparedStatement stm = con.prepareStatement(updateQuery)){
			
			League league = LeagueCache.getInstance().getLeague().get(p.getName());
			if (league != null) {
				stm.setString(1, league.name()); 
				stm.setInt(2, league.getXPcurrent()); 
				stm.setString(3, p.getUniqueId().toString());
				stm.setString(4, p.getName());  
				stm.executeUpdate();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("§c[StoneLeague] SQL error! Failed to update database.");
		}
	}
	
	public void updateTask() {
		HashMap<String, League> cache = LeagueCache.getInstance().getLeague();
		
		(new BukkitRunnable() {
			
			@Override
			public void run() {
				
				for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
					if (cache.containsKey(onlinePlayers.getName())) {
						saveToSQL(onlinePlayers);
					}
				}
				Bukkit.getConsoleSender().sendMessage("§a[StoneLeague] SQL updated!");
			}
		}).runTaskTimerAsynchronously(Main.getInstance(), 0L, 20*60*30); //cada 30 min teste
	}
	
	public void forceSaveSQL() {
		HashMap<String, League> cache = LeagueCache.getInstance().getLeague();
		for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
			if (cache.containsKey(onlinePlayers.getName())) {
				saveToSQL(onlinePlayers);
			}
		}
	}
	
}
