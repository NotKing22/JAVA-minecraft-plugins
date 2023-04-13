package me.zsnow.stonegoldenpig.api;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.zsnow.stonegoldenpig.configs.Configs;

public class LocationAPI {

	public static LocationAPI location = new LocationAPI();
	
	public static LocationAPI getLocation() {
		return location;
	}
	
	String local = "Porco-Location";
	
	public Location location(Integer number) {
		double X,Y,Z;
		float Yaw,Pitch;
			X = Configs.locations.getConfig().getDouble(local + "." + number + ".X");
	        Y = Configs.locations.getConfig().getDouble(local + "." + number + ".Y");
	        Z = Configs.locations.getConfig().getDouble(local + "." + number +".Z");
	        Yaw = (float)Configs.locations.getConfig().getLong(local + "." + number +".Yaw");
	        Pitch = (float)Configs.locations.getConfig().getLong(local + "." + number + ".Pitch");
		    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + "." + number + ".Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch).add(0.5,0.0,0.5);
		  	return loc;
		  	
		}
	
	public void setLocation(final Player p, Integer number) {
		    double x = p.getLocation().getBlockX();
	    	double y = p.getLocation().getBlockY();
			double z = p.getLocation().getBlockZ();
			float yaw = p.getLocation().getYaw();
			float pitch = p.getLocation().getPitch();
			String world = p.getLocation().getWorld().getName().toString();
			Configs.locations.getConfig().set(local + "." + number + ".X", Double.valueOf(x));
			Configs.locations.getConfig().set(local + "." + number + ".Y", Double.valueOf(y));
			Configs.locations.getConfig().set(local + "." + number + ".Z", Double.valueOf(z));
			Configs.locations.getConfig().set(local + "." + number + ".Yaw", Float.valueOf(yaw));
			Configs.locations.getConfig().set(local + "." + number + ".Pitch", Float.valueOf(pitch));
			Configs.locations.getConfig().set(local + "." + number + ".Mundo", world);
			Configs.locations.saveConfig();
		
		}
	
	/*public void teleportTo(Player p, String location) {
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
	
	
	public void setLocation(Player p, String LocationName) {
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
	}*/
	
}
