package me.zsnow.redestone.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import me.zsnow.redestone.Main;
import me.zsnow.redestone.config.Configs;

public class ConexaoSQL {

	protected static Connection con = null;
	public static ConsoleCommandSender sc = Bukkit.getConsoleSender();
	
	public static void openConnection() {
		String host = Configs.config.getConfig().getString("host");
		String port = Configs.config.getConfig().getString("port");
		String databaseName = Configs.config.getConfig().getString("databaseName");
		String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
		
		String user = Configs.config.getConfig().getString("user");
		String password = Configs.config.getConfig().getString("password");
		
		try {
			con = DriverManager.getConnection(url, user, password);
			sc.sendMessage("§a[Duels] A conexão MySQL foi estabelecida.");
		} catch (Exception e) {
			e.printStackTrace();
			sc.sendMessage("§c[Duels] Houve um erro ao estabelecer a conexão MySQL.");
			Main.getInstance().getPluginLoader().disablePlugin(Main.getInstance());
		}
	}
	
	public static void createTable() {
		if (con != null) {
			try {
				PreparedStatement stm = con.prepareStatement
			 ("CREATE TABLE IF NOT EXISTS dados"
			+ "(player VARCHAR(24) UNIQUE NOT NULL, vitorias INT(16) NOT NULL, derrotas INT(16) NOT NULL, kdr Double NOT NULL, armazem VARCHAR(6000))");
		stm.executeUpdate();
		sc.sendMessage("§a[Duels] Banco de dados gerado com exito.");
			} catch (Exception e) {
				e.printStackTrace();
				sc.sendMessage("§c[Duels] Houve um erro ao gerar a tabela.");
			}
		}
	}
	
	public static void closeConnection() {
		if (con != null) {
			try {
				con.close();
				sc.sendMessage("§a[Duels] Conexão encerrada com sucesso.");
			} catch (Exception e) {
				sc.sendMessage("§c[Duels] Houve um erro ao encerrar a conexão.");
				
			}
		}
	}
	
}
