package me.zsnow.desafioepico.configAPI;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class LocationAPI {

	//<entrada/saida/mobspawn/bosspawn>
	/*
	public enum location {
		ENTRADA,
		SAIDA,
		MOBSPAWN,
		BOSSSPAWN,
	}
	
	
	public String getLocation(final Player p, location Location) {
		switch (Location) {
		case ENTRADA:
			return "Entrada";
		case SAIDA:
			return "Saida";
		case MOBSPAWN:
			return "Mobspawn";
		case BOSSSPAWN:
			return "BossSpawn";
		default:
			break;
		}
		return null;
	}
	*/
	
	public static void sendTo(Player p, String location) {
		location = location.toUpperCase(); //adicionei
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		p.teleport(loc);
	}
	
	
	public static void setLoc(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		Configs.locations.getConfig().set(LocationName + ".X", Double.valueOf(x));
		Configs.locations.getConfig().set(LocationName + ".Y", Double.valueOf(y));
		Configs.locations.getConfig().set(LocationName + ".Z", Double.valueOf(z));
		Configs.locations.getConfig().set(LocationName + ".Yaw", Float.valueOf(yaw));
		Configs.locations.getConfig().set(LocationName + ".Pitch", Float.valueOf(pitch));
		Configs.locations.getConfig().set(LocationName + ".Mundo", world);
		Configs.locations.saveConfig();
	}
	
}
