package me.zsnow.stone.league.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import me.zsnow.stone.league.Main;

public class LeagueConnection {

	protected static Connection con = null;
	private static ConsoleCommandSender sc = Bukkit.getServer().getConsoleSender();
	
	public static void openConnection() {
		FileConfiguration config = Main.getInstance().getConfig();
	    String host = config.getString("Mysql.host");
	    String port = config.getString("Mysql.port");
	    String dbName = config.getString("Mysql.database");
	    String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName;

	    String user = config.getString("Mysql.user");
	    String password = config.getString("Mysql.password");

	    try {
	        con = DriverManager.getConnection(url, user, password);
	        sc.sendMessage("§a[StoneLeague] established sql connection.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        sc.sendMessage("§c[StoneLeague] sql connection failed.");
	        Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
	    }
	}

	public static void createTable() {
		if (con != null) {
			String query = "CREATE TABLE IF NOT EXISTS league_db (UUID VARCHAR(40) UNIQUE, player VARCHAR(20) UNIQUE PRIMARY KEY, league VARCHAR(20), current_xp INT)";
			try (PreparedStatement stm = con.prepareStatement(query)) {
				stm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				sc.sendMessage("§c[StoneLeague] sql connection failed. Create table 'league_db' error!");
				Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
			}
		}
	}
	
	public static void closeConnection() {
		if (con != null) {
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}
