package me.zsnow.spookyrealm.api;

import java.util.Iterator;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particles {
  private PacketPlayOutWorldParticles packet;
  
  public Particles(EnumParticle type, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
    float x = (float)loc.getX();
    float y = (float)loc.getY();
    float z = (float)loc.getZ();
    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type, true, x, y, z, xOffset, yOffset, 
        zOffset, speed, count, null);
    this.packet = packet;
  }
  
  public void sendToAll() {
    Iterator<? extends Player> var2 = Bukkit.getServer().getOnlinePlayers().iterator();
    while (var2.hasNext()) {
      Player p = var2.next();
      (((CraftPlayer)p).getHandle()).playerConnection.sendPacket((Packet<?>)this.packet);
    } 
  }
  
  public void sendToPlayer(Player p) {
    (((CraftPlayer)p).getHandle()).playerConnection.sendPacket((Packet<?>)this.packet);
  }
}
