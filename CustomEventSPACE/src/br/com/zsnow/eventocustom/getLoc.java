package br.com.zsnow.eventocustom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class getLoc {

	public static void sendEnter(Player target) {
		if (Main.getPlugin().getConfig().contains("Entrada")) {
			  double X = Main.getPlugin().getConfig().getDouble("Entrada.X");
	            double Y = Main.getPlugin().getConfig().getDouble("Entrada.Y");
	            	double Z = Main.getPlugin().getConfig().getDouble("Entrada.Z");
	            		float Yaw = (float)Main.getPlugin().getConfig().getLong("Entrada.Yaw");
	            			float Pitch = (float)Main.getPlugin().getConfig().getLong("Entrada.Pitch");
		  World Mundo = Bukkit.getWorld(Main.getPlugin().getConfig().getString("Entrada.Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
	  			target.teleport(loc);
		}
	}
	
	public static void sendExit(Player jogadores) {
		if (Main.getPlugin().getConfig().contains("Saida")) {
		  double X = Main.getPlugin().getConfig().getDouble("Saida.X");
	        double Y = Main.getPlugin().getConfig().getDouble("Saida.Y");
	        	double Z = Main.getPlugin().getConfig().getDouble("Saida.Z");
	        		float Yaw = (float)Main.getPlugin().getConfig().getLong("Saida.Yaw");
	        			float Pitch = (float)Main.getPlugin().getConfig().getLong("Saida.Pitch");
		  World Mundo = Bukkit.getWorld(Main.getPlugin().getConfig().getString("Saida.Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
	  			jogadores.teleport(loc);
		}
	}
	
	public static void setLoc(Player p, String LocationName) {
	    double x = p.getLocation().getBlockX();
	    	double y = p.getLocation().getBlockY();
	    		double z = p.getLocation().getBlockZ();
	    			float yaw = p.getLocation().getYaw();
	    				float pitch = p.getLocation().getPitch();
	    					String world = p.getLocation().getWorld().getName().toString();

		Main.getPlugin().getConfig().set(LocationName + ".X", Double.valueOf(x));
			Main.getPlugin().getConfig().set(LocationName + ".Y", Double.valueOf(y));
				Main.getPlugin().getConfig().set(LocationName + ".Z", Double.valueOf(z));
					Main.getPlugin().getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
						Main.getPlugin().getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
							Main.getPlugin().getConfig().set(LocationName + ".Mundo", world);
								Main.getPlugin().saveConfig();
	}
	
	public static void setExit(Player p) {
	    double x = p.getLocation().getBlockX();
	    	double y = p.getLocation().getBlockY();
	    		double z = p.getLocation().getBlockZ();
	    			float yaw = p.getLocation().getYaw();
	    				float pitch = p.getLocation().getPitch();
	    					String world = p.getLocation().getWorld().getName().toString();

		Main.getPlugin().getConfig().set("Saida.X", Double.valueOf(x));
			Main.getPlugin().getConfig().set("Saida.Y", Double.valueOf(y));
				Main.getPlugin().getConfig().set("Saida.Z", Double.valueOf(z));
					Main.getPlugin().getConfig().set("Saida.Yaw", Float.valueOf(yaw));
						Main.getPlugin().getConfig().set("Saida.Pitch", Float.valueOf(pitch));
							Main.getPlugin().getConfig().set("Saida.Mundo", world);
								Main.getPlugin().saveConfig();
	}
}
