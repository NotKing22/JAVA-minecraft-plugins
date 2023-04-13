package me.zsnow.round6.api;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;

import me.zsnow.round6.Main;
import me.zsnow.round6.configs.Configs;
import me.zsnow.round6.manager.SemaforoClass;
import me.zsnow.round6.manager.SemaforoClass.sinal;

@SuppressWarnings("deprecation")
public class SchematicAPI {

	private static String schematicName = Configs.config.getConfig().getString("Schematic-file-name");
	private static String schematicNameBoneca = Configs.config.getConfig().getString("Schematic-file-name-boneca");
	
	public static void loadSchematicParede() {
		if (Configs.locations.getConfig().contains("SCHEMATIC-LOC")) {
			 double x = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.X");
		     double y = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.Y");
		   	 double z = Configs.locations.getConfig().getDouble("SCHEMATIC-LOC.Z");
		   	 World world = Bukkit.getWorld(Configs.locations.getConfig().getString("SCHEMATIC-LOC.Mundo"));
			 WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
			File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "schematics/" + schematicName + ".schematic");
			 EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000);
			 try {
				CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
				clipboard.rotate2D(0);
				clipboard.paste(session, new Vector(x, y, z), false); // true = no air
				(new BukkitRunnable() {
					
					@Override
					public void run() {
						if (SemaforoClass.getSemaforo().getEventoStatus() == true) {
							if (SemaforoClass.getSemaforo().getSinalCor(sinal.VERDE)) {
								session.undo(worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000));
								cancel();
							}
						} else {
							session.undo(worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000));
							cancel();
						}
					}
				}).runTaskTimer(Main.getInstance(), 20L, 0);
			} catch (MaxChangedBlocksException | DataException | IOException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("§c[StoneRound6] Houve um erro ao colar a Schematic.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("§cA schematic nao teve sua localizacao definida.");
		}
	}
	
	public static void loadSchematicBoneca() {
		if (Configs.locations.getConfig().contains("SCHEMATIC-BONECA-LOC")) {
			 double x = Configs.locations.getConfig().getDouble("SCHEMATIC-BONECA-LOC.X");
		     double y = Configs.locations.getConfig().getDouble("SCHEMATIC-BONECA-LOC.Y");
		   	 double z = Configs.locations.getConfig().getDouble("SCHEMATIC-BONECA-LOC.Z");
		   	 World world = Bukkit.getWorld(Configs.locations.getConfig().getString("SCHEMATIC-BONECA-LOC.Mundo"));
			 WorldEditPlugin worldEditPlugin = (WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit");
			File schematic = new File(worldEditPlugin.getDataFolder() + File.separator + "schematics/" + schematicNameBoneca + ".schematic");
			 EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000);
			 try {
				CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
				clipboard.rotate2D(0);
				if (SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
					clipboard.rotate2D(180);
				}
			(new BukkitRunnable() {
				
				@Override
				public void run() {
					if (SemaforoClass.getSemaforo().getEventoStatus() == true) {
						if (!SemaforoClass.getSemaforo().getSinalCor(sinal.VERMELHO)) {
							session.undo(worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000));
							}
						} else {
							session.undo(worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(world), 1000000));
							cancel();
						}
					}
				}).runTaskTimer(Main.getInstance(), 20L, 0L);
				
				clipboard.paste(session, new Vector(x, y, z), false); // true = no air
			} catch (MaxChangedBlocksException | DataException | IOException e) {
				e.printStackTrace();
				Bukkit.getConsoleSender().sendMessage("§c[StoneRound6] Houve um erro ao colar a Schematic.");
			}
		} else {
			Bukkit.getConsoleSender().sendMessage("§cA schematic nao teve sua localizacao definida.");
		}
	}
	
	public static void setAirAtLocation() {
		World world = Bukkit.getWorld(Configs.locations.getConfig().getString("SCHEMATIC-LOC.Mundo"));
		CuboidSelection cuboid = new CuboidSelection (world, getAirLocation("PAREDE-AR-1"), getAirLocation("PAREDE-AR-2"));
		Location min = cuboid.getMinimumPoint();
		Location max = cuboid.getMaximumPoint();
        for (int x = min.getBlockX(); x <= max.getX(); x++) {
            for (int z = (int) min.getZ(); z <= max.getZ(); z++) {
                for (int y = (int) min.getY(); y <= max.getY(); y++) {
                    Location location = new Location(world, x, y, z);
                    Block block = location.getBlock();
                    block.setType(Material.AIR);
                }
            }
        }
	}
	
	public static Location getAirLocation(String locName) {
		String local = locName.toString();
		double X,Y,Z;
		float Yaw,Pitch;
			X = Configs.locations.getConfig().getDouble(local + ".X");
	        Y = Configs.locations.getConfig().getDouble(local + ".Y");
	        Z = Configs.locations.getConfig().getDouble(local + ".Z");
	        Yaw = (float)Configs.locations.getConfig().getLong(local + ".Yaw");
	        Pitch = (float)Configs.locations.getConfig().getLong(local + ".Pitch");
		    World Mundo = Bukkit.getWorld(Configs.locations.getConfig().getString(local + ".Mundo"));
		  	Location loc = new Location(Mundo, X, Y, Z, Yaw, Pitch);
			return loc;
	}
	
}
