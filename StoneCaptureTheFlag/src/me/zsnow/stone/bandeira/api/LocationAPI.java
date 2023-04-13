package me.zsnow.stone.bandeira.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.zsnow.stone.bandeira.configs.Configs;

public class LocationAPI {

	private static LocationAPI location = new LocationAPI();
	
	public static LocationAPI getLocation() {
		return location;
	}
	
	public enum location {
		LOBBY,
		SAIDA,
		MODERAR,
		AZUL,
		VERMELHO,
		RED_BANNER,
		BLUE_BANNER,
		BLUE_FLAG,
		RED_FLAG
	}
	
	
	public String teleportTo(final Player p, location location) {
		String local = location.toString();
		double X,Y,Z;
		float Yaw,Pitch;
			X = Configs.locations.getConfig().getDouble(local + ".X");
	        Y = Configs.locations.getConfig().getDouble(local + ".Y");
	        Z = Configs.locations.getConfig().getDouble(local + ".Z");
	        Yaw = (float)Configs.locations.getConfig().getLong(local + ".Yaw");
	        Pitch = (float)Configs.locations.getConfig().getLong(local + ".Pitch");
		    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + ".Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		  	
		switch (location) {
			case LOBBY:
				local = "LOBBY";
					p.teleport(loc);
					break;
			case SAIDA:
				local = "SAIDA";
					p.teleport(loc);
					break;
			case MODERAR:
				local = "MODERAR";
					p.teleport(loc);
					break;
			case AZUL:
				local = "AZUL";
					p.teleport(loc);
					break;
			case VERMELHO:
				local = "VERMELHO";
					p.teleport(loc);
					break;
			default:
				throw new IllegalArgumentException("A localidade informada nao pode ser encontrada: " + local);
			}
		return local;
		}
	
	
	public String setLocation(final Player p, location location) {
		String local = null;
		    double x = p.getLocation().getBlockX();
	    	double y = p.getLocation().getBlockY();
			double z = p.getLocation().getBlockZ();
			float yaw = p.getLocation().getYaw();
			float pitch = p.getLocation().getPitch();
			String world = p.getLocation().getWorld().getName().toString();
		  	
		switch (location) {
			case LOBBY:
				local = "LOBBY";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case SAIDA:
				local = "SAIDA";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case MODERAR:
				local = "MODERAR";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case AZUL:
				local = "AZUL";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case VERMELHO:
				local = "VERMELHO";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case BLUE_BANNER:
				local = "BLUE_BANNER";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case RED_BANNER:
				local = "RED_BANNER";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
					break;
			case BLUE_FLAG:
				local = "BLUE_FLAG";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
				break;
			case RED_FLAG:
				local = "RED_FLAG";
					Configs.locations.getConfig().set(local + ".X", Double.valueOf(x));
					Configs.locations.getConfig().set(local + ".Y", Double.valueOf(y));
					Configs.locations.getConfig().set(local + ".Z", Double.valueOf(z));
					Configs.locations.getConfig().set(local + ".Yaw", Float.valueOf(yaw));
					Configs.locations.getConfig().set(local + ".Pitch", Float.valueOf(pitch));
					Configs.locations.getConfig().set(local + ".Mundo", world);
					Configs.locations.saveConfig();
				break;
			default:
				throw new IllegalArgumentException("A localidade informada nao pode ser encontrada: " + local);
			}
		return local;
		}
	
	public void teleportToAny(Player p, String location) {
		location = location.toUpperCase(); 
		double X = Configs.locations.getConfig().getDouble(location + ".X");
        double Y = Configs.locations.getConfig().getDouble(location + ".Y");
        double Z = Configs.locations.getConfig().getDouble(location + ".Z");
        float Yaw = (float)Configs.locations.getConfig().getLong(location + ".Yaw");
        float Pitch = (float)Configs.locations.getConfig().getLong(location + ".Pitch");
	    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(location + ".Mundo"));
	  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		p.teleport(loc);
	}
	
	
	public void setAnyLocation(Player p, String LocationName) {
		LocationName = LocationName.toUpperCase();
		YamlConfiguration location = Configs.locations.getConfig();
	    double x = p.getLocation().getBlockX();
    	double y = p.getLocation().getBlockY();
		double z = p.getLocation().getBlockZ();
		float yaw = p.getLocation().getYaw();
		float pitch = p.getLocation().getPitch();
		String world = p.getLocation().getWorld().getName().toString();
		location.set(LocationName + ".X", Double.valueOf(x));
		location.set(LocationName + ".Y", Double.valueOf(y));
		location.set(LocationName + ".Z", Double.valueOf(z));
		location.set(LocationName + ".Yaw", Float.valueOf(yaw));
		location.set(LocationName + ".Pitch", Float.valueOf(pitch));
		location.set(LocationName + ".Mundo", world);
		Configs.locations.saveConfig();
	}
	
}
