package me.zsnow.chain;

import org.bukkit.Bukkit;
import org.bukkit.Location;
//import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.entity.EntityType;
//import org.bukkit.inventory.ItemStack;

//import net.citizensnpcs.api.CitizensAPI;
//import net.citizensnpcs.api.npc.NPC;
//import net.citizensnpcs.api.trait.trait.Equipment;
//import net.citizensnpcs.api.trait.trait.Equipment.EquipmentSlot;

public class NPCManager {

    public static Location getLoc(String loc) {
        World w2 = Bukkit.getWorld(Main.getPlugin().getConfig().getString(String.valueOf(loc) + ".world"));
        double x2 = Main.getPlugin().getConfig().getDouble(String.valueOf(loc) + ".x");
        double y2 = Main.getPlugin().getConfig().getDouble(String.valueOf(loc) + ".y");
        double z2 = Main.getPlugin().getConfig().getDouble(String.valueOf(loc) + ".z");
        float yaw2 = (float)Main.getPlugin().getConfig().getDouble(String.valueOf(loc) + ".yaw");
        float pitch2 = (float)Main.getPlugin().getConfig().getDouble(String.valueOf(loc) + ".pitch");
        Location saidaloc = new Location(w2, x2, y2, z2, yaw2, pitch2);
        return saidaloc;
      }
    /*
    public static void CreateNPC2() {
        Location l = getLoc("NPC");
        if (l == null) {
          Bukkit.getConsoleSender().sendMessage("§cChain NPC sem location disponivel");
          return;
        } 
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "CHAIN-ENTER");
        npc.data().set("player-skin-name", "DJCODE");
        npc.data().set("player-skin-textures", 
            "eyJ0aW1lc3RhbXAiOjE1ODI2MzUwOTM0NTgsInByb2ZpbGVJZCI6IjkxOGEwMjk1NTlkZDRjZTZiMTZmN2E1ZDUzZWZiNDEyIiwicHJvZmlsZU5hbWUiOiJCZWV2ZWxvcGVyIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84YzIyMTEyNGU2Y2I4ZGMxZWU5OGQ5ZjBmMzE5N2NjN2RjMDU1YWRjZGZjNGU3ODNlNDc0Nzk3NzMwMzBhMTU4In19fQ==");
        npc.data().set("player-skin-signature", 
            "V51+jVDbcsWLvTeXwcYnfLK7jUilRKcyjy+W4MTTQyc0GxblaIBCBBRlopLAZdBLso6Gr9d9zmttVfksrZ45Grh0D6tWI5SX+kD1WZA17CVptnKFXB7mSX2NYtlFMU6FrQO0ASEgXVx/ZCykvxdw/T2/SWzFsZP/y/XGiL3aRBDH3Xq6JEGU4Ad4nJup5+B2emp0lPRE+eweom0Ds2Uddnakcr8HOU8yjV7tC3QdE5CkGmGT0mKbq0uP7CHbg9hZ/v8bM6JiRzUaNFOb+U7Gpnab4zq39SgDanbMqPcA+9d34MjA4i//FuxvAKyOdrcrGFL1G5cndT6BPTMlbVKjJ1IRrFK8ck/0Ng9fow9fBen5SQQd3jNUnw0YRKUHAzCdAU67M3A6/j6lCc3ivPj0RApokh4GMCnNo93Tugssmrj3BH44YtoGfcOZUWdMH8DyfYFiGO0JizYUIQqXPlC90mG1K5MnNJxzKHuXeduT7VcR5E8Fj/YeSHkZ8kUJZuGH00pGV+v1gIhPN7CLC8O/bqOHe4JLL8MA85vO5RqjezYuKYX9/XIjPj28ouRN9TwZl08EA2k6mGCl/a2m5Q3uKxmD0lftgvm4qo0lQ2xhdw66rSVK+0199u1iAAripLkCQdfpXvADQDqPFG1r9PHLP6SQVO21qnEDoUSVqnZ2Is0=");
        npc.setName("§6§lCHAIN ENTRADA");
        npc.getTrait(Equipment.class).set(EquipmentSlot.BOOTS, new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        npc.getTrait(Equipment.class).set(EquipmentSlot.LEGGINGS, new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        npc.getTrait(Equipment.class).set(EquipmentSlot.CHESTPLATE, new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        npc.getTrait(Equipment.class).set(EquipmentSlot.HELMET, new ItemStack(Material.CHAINMAIL_HELMET, 1));
        npc.setProtected(true);
    	npc.spawn(l);
    	Commands.dungeonNPC = npc;
    }
    */
	
}
